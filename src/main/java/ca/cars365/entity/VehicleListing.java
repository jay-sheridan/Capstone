package ca.cars365.entity;

import java.time.LocalDateTime;

import ca.cars365.enums.ListingStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vehicle_listings")
public class VehicleListing extends BaseEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vehicle_listing_id")
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Double listingPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ListingStatus status = ListingStatus.DRAFT;

    @Column(nullable = false)
    private Boolean visibleToBuyers = false;

    private LocalDateTime publishedAt;

    @Column(columnDefinition = "TEXT")
    private String hiddenReason;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false, unique = true)
    private Vehicle vehicle;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inspection_agency_id")
    private InspectionAgency inspectionAgency;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private Users seller;

}
