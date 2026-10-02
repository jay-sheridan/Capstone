package ca.cars365.repositories;
import ca.cars365.entity.AppointmentDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface AppointmentDocumentRepository extends JpaRepository<AppointmentDocument, String> {
    List<AppointmentDocument> findByAppointmentIdOrderBySubmittedAtAsc(Long appointmentId);
}
