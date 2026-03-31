package ca.cars365.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "vin_decode_records")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VinDecodeRecord extends BaseEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vin_decode_record_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 17)
    private String vin;

    @Column(length = 100)
    private String make;

    @Column(length = 100)
    private String model;

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
    
    private LocalDateTime vinDecodedAt;

    @Column(nullable = false)
    @Builder.Default
    private Boolean decodeSuccess = true;

    
	@OneToOne(mappedBy = "vinDecodeRecord", fetch = FetchType.LAZY)
	@JsonIgnore
    private Vehicle vehicle;
    
    @PrePersist
    public void prePersist() {
    			if (vinDecodedAt == null) {
			vinDecodedAt = LocalDateTime.now();
		}
    }
}
