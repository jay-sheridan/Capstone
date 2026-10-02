package ca.cars365.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "appointment_documents")
public class AppointmentDocument {
    @Id private String id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "appointment_id")
    private InspectionAppointment appointment;
    @Column(nullable = false) private String name;
    @Column(nullable = false) private String kind;
    @Column(nullable = false) private String contentType;
    @Column(nullable = false) private long size;
    @Column(nullable = false) private LocalDateTime submittedAt;
    @Lob @Column(nullable = false) private byte[] content;
}
