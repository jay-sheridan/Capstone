package ca.cars365.controllers;

import ca.cars365.entity.AppointmentDocument;
import ca.cars365.entity.InspectionAppointment;
import ca.cars365.repositories.AppointmentDocumentRepository;
import ca.cars365.repositories.InspectionAppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/v1/appointment-documents")
@RequiredArgsConstructor
@Transactional
public class AppointmentDocumentController {
    private final AppointmentDocumentRepository documents;
    private final InspectionAppointmentRepository appointments;
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> documentError(ResponseStatusException error) {
        return ResponseEntity.status(error.getStatusCode()).body(Map.of("message", Objects.toString(error.getReason(), "Document request failed.")));
    }
    public record DocumentInfo(String id, String name, String kind, long size, LocalDateTime submittedAt) {}
    private DocumentInfo info(AppointmentDocument doc) {
        return new DocumentInfo(doc.getId(), doc.getName(), doc.getKind(), doc.getSize(), doc.getSubmittedAt());
    }
    private InspectionAppointment authorize(Long id, Authentication auth, boolean upload) {
        InspectionAppointment appointment = appointments.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Appointment not found. Please book a new inspection."));
        boolean seller = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("REGULAR_USER"))
            && appointment.getSeller().getEmail().equals(auth.getName());
        boolean agency = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("INSPECTION_AGENCY"))
            && appointment.getInspectionAgency().getLicenseNumber().equals(auth.getName());
        if (!seller && (upload || !agency)) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot access documents for this appointment.");
        return appointment;
    }
    @GetMapping("/{appointmentId}")
    public List<DocumentInfo> list(@PathVariable Long appointmentId, Authentication auth) {
        authorize(appointmentId, auth, false);
        return documents.findByAppointmentIdOrderBySubmittedAtAsc(appointmentId).stream().map(this::info).toList();
    }
    @PostMapping(value = "/{appointmentId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public DocumentInfo upload(@PathVariable Long appointmentId, @RequestParam String clientId,
            @RequestParam String kind, @RequestParam MultipartFile file, Authentication auth) throws IOException {
        InspectionAppointment appointment = authorize(appointmentId, auth, true);
        try { UUID.fromString(clientId); } catch (IllegalArgumentException ex) { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid document ID."); }
        if (!Set.of("Ownership proof", "Vehicle photos", "Service records").contains(kind))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid document category.");
        Optional<AppointmentDocument> existing = documents.findById(clientId);
        if (existing.isPresent()) {
            if (!existing.get().getAppointment().getId().equals(appointmentId)) throw new ResponseStatusException(HttpStatus.CONFLICT, "Document ID already in use.");
            return info(existing.get());
        }
        if (file.isEmpty() || file.getSize() > 10 * 1024 * 1024) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Files must be nonempty and no larger than 10 MB.");
        byte[] data = file.getBytes();
        String type;
        if (data.length >= 5 && new String(data, 0, 5, StandardCharsets.US_ASCII).equals("%PDF-")) type = "application/pdf";
        else if (data.length >= 8 && Arrays.equals(Arrays.copyOf(data, 8), new byte[]{(byte)137,80,78,71,13,10,26,10})) type = "image/png";
        else if (data.length >= 3 && data[0] == (byte)255 && data[1] == (byte)216 && data[2] == (byte)255) type = "image/jpeg";
        else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only PDF, JPG and PNG documents are accepted.");
        String name = Objects.toString(file.getOriginalFilename(), "document").replaceAll("[\\\\/\\p{Cntrl}]", "_");
        if (name.length() > 200) name = name.substring(name.length() - 200);
        AppointmentDocument doc = new AppointmentDocument();
        doc.setId(clientId); doc.setAppointment(appointment); doc.setName(name); doc.setKind(kind);
        doc.setContentType(type); doc.setSize(data.length); doc.setContent(data); doc.setSubmittedAt(LocalDateTime.now());
        return info(documents.save(doc));
    }
    @GetMapping("/{appointmentId}/{documentId}/download")
    public ResponseEntity<byte[]> download(@PathVariable Long appointmentId, @PathVariable String documentId, Authentication auth) {
        authorize(appointmentId, auth, false);
        AppointmentDocument doc = documents.findById(documentId).filter(d -> d.getAppointment().getId().equals(appointmentId))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found."));
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(doc.getContentType()))
            .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment().filename(doc.getName(), StandardCharsets.UTF_8).build().toString())
            .header(HttpHeaders.CACHE_CONTROL, "no-store").header("X-Content-Type-Options", "nosniff")
            .body(doc.getContent());
    }
}
