package ca.cars365.controllers;

import ca.cars365.entity.*;
import ca.cars365.enums.ListingStatus;
import ca.cars365.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

@RestController
@RequestMapping("/api/v1/listings")
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ListingDetailsController {
    private final VehicleListingRepository listings;
    private final AppointmentDocumentRepository documents;
    private final InspectionReportRepository reports;

    VehicleListing published(Long id) {
        return listings.findById(id).filter(l -> l.getStatus() == ListingStatus.PUBLISHED && Boolean.TRUE.equals(l.getVisibleToBuyers()))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Published vehicle not found."));
    }
    private List<AppointmentDocument> photos(VehicleListing listing) {
        return documents.findByAppointmentVehicleIdAndKindOrderBySubmittedAtAsc(listing.getVehicle().getId(), "Vehicle photos")
            .stream().filter(d -> Set.of("image/jpeg", "image/png").contains(d.getContentType())).toList();
    }
    private InspectionReport latest(VehicleListing listing) {
        return reports.findByVehicleId(listing.getVehicle().getId()).stream()
            .max(Comparator.comparing(InspectionReport::getId)).orElse(null);
    }
    @GetMapping("/{id}")
    public Map<String, Object> detail(@PathVariable Long id) {
        var listing = published(id);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", id); result.put("title", listing.getTitle()); result.put("description", listing.getDescription());
        result.put("listingPrice", listing.getListingPrice()); result.put("inspectionAgencyName", listing.getInspectionAgency().getName());
        result.put("photos", photos(listing).stream().map(d -> Map.of("id", d.getId(), "name", d.getName())).toList());
        var report = latest(listing);
        result.put("inspectionScore", report == null ? null : report.getFinalizedScore());
        result.put("condition", report == null ? null : report.conditionSummary());
        result.put("report", report == null ? null : reportData(report));
        return result;
    }
    // Only report scalar fields are shared: no user, appointment or agency entities.
    private Map<String, Object> reportData(InspectionReport report) {
        Map<String, Object> values = new LinkedHashMap<>();
        for (var field : InspectionReport.class.getDeclaredFields()) {
            var type = field.getType();
            if (!(type == String.class || type == Integer.class || type == Boolean.class || type == Long.class || type.isEnum() || type == java.time.LocalDateTime.class)) continue;
            try { field.setAccessible(true); values.put(field.getName(), field.get(report)); }
            catch (IllegalAccessException ex) { throw new IllegalStateException(ex); }
        }
        values.put("score", report.getFinalizedScore());
        values.put("rating", report.getFinalizedScore() == null ? null : report.getRating());
        values.put("condition", report.conditionSummary());
        return values;
    }
    @GetMapping("/{id}/photos/{documentId}")
    public ResponseEntity<byte[]> photo(@PathVariable Long id, @PathVariable String documentId) {
        var listing = published(id);
        var photo = photos(listing).stream().filter(d -> d.getId().equals(documentId)).findFirst()
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle photo not found."));
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(photo.getContentType()))
            .header("X-Content-Type-Options", "nosniff").header("Cache-Control", "no-store").body(photo.getContent());
    }
    @GetMapping("/{id}/report")
    public ResponseEntity<Map<String, Object>> report(@PathVariable Long id) {
        var report = latest(published(id));
        if (report == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Inspection report not available.");
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=inspection-report-" + id + ".json")
            .body(reportData(report));
    }
}
