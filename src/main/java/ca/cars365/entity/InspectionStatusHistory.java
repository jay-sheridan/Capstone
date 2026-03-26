package ca.cars365.entity;

import ca.cars365.enums.InspectionRequestStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "inspection_status_history")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InspectionStatusHistory extends BaseEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inspection_status_history_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private InspectionRequestStatus oldStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private InspectionRequestStatus newStatus;

    private Long changedByUserId;

    @Column(length = 50)
    private String changedByRole;

    @Column(columnDefinition = "TEXT")
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inspection_request_id", nullable = false)
    private InspectionRequest inspectionRequest;
}
