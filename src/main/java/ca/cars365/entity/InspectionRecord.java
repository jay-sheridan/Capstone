package ca.cars365.entity;

import java.time.LocalDate;

import org.hibernate.annotations.Check;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "inspection_records")
@Check(constraints = "odometer_km >= 0")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InspectionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inspection_record_id")
    private Long inspectionRecordId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @Column(name = "dealer_id")
    private Long dealerId;

    @Column(name = "service_date", nullable = false)
    private LocalDate serviceDate;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "service_center", length = 200)
    private String serviceCenter;

    @Column(name = "odometer_km", nullable = false)
    private Integer odometerKm;
}
