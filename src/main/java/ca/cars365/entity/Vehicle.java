package ca.cars365.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vehicles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vehicle_id")
    private Long id;

    @Column(name = "vin_number", nullable = false, unique = true, length = 17)
    private String vinNumber;

    @Column(length = 100)
    private String make;

    @Column(length = 100)
    private String model;

    @Column(name = "model_year")
    private Integer modelYear;

    @Column(length = 100)
    private String engineModel;

    @Column(length = 100)
    private String transmissionStyle;

    @Column(length = 100)
    private String fuelTypePrimary;

    @Column(length = 100)
    private String vehicleType;

    @Column(length = 100)
    private String plantCountry;

    @Column(length = 100)
    private String trimLevel;

    @Column(length = 100)
    private String bodyStyle;

    @Column(length = 100)
    private String exteriorColor;

    @Column(length = 100)
    private String interiorColor;

    private Integer odometerKm;

    @Column(columnDefinition = "TEXT")
    private String conditionSummary;

    private Double askingPrice;

    @Column(length = 100)
    private String registrationProvince;

    @Column(nullable = false)
    private Boolean vinDecoded = false;

    private LocalDateTime vinDecodedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private Users seller;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VehiclePhoto> photos = new ArrayList<>();

//    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<VehicleDocument> documents = new ArrayList<>();

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InspectionRequest> inspectionRequests = new ArrayList<>();

    @OneToOne(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    private VehicleListing listing;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CertificationRecord> certificationRecords = new ArrayList<>();

    @OneToOne(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    private VinDecodeRecord vinDecodeRecord;
}