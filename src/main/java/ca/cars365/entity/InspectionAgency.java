package ca.cars365.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import ca.cars365.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "inspection_agencies")
public class InspectionAgency extends BaseEntity implements UserDetails {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inspection_agency_id")
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(length = 255)
    private String address;

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String provinceState;

    @Column(length = 30)
    private String postalCode;

    @Column(length = 100)
    private String country;

    @Column(nullable = false, unique = true, length = 100)
    private String licenseNumber;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String passwordHash;

    @Column(unique = true, length = 255)
    private String email;

    @Column(length = 30)
    private String phoneNumber;

    @Column(length = 150)
    private String contactPersonName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private UserRole role = UserRole.INSPECTION_AGENCY;

    @Column(nullable = false)
    @Builder.Default
    private Boolean approved = false;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    @OneToMany(mappedBy = "inspectionAgency")
    @Builder.Default
    private List<InspectionRequest> inspectionRequests = new ArrayList<>();

    @OneToMany(mappedBy = "inspectionAgency")
    @Builder.Default
    private List<InspectionReport> inspectionReports = new ArrayList<>();

    @OneToMany(mappedBy = "issuedByAgency")
    @Builder.Default
    private List<CertificationRecord> certificationRecords = new ArrayList<>();

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		return List.of(new SimpleGrantedAuthority(role.name()));
	}

	@Override
	public @Nullable String getPassword() {
		// TODO Auto-generated method stub
		return this.passwordHash;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.licenseNumber;
	}

}