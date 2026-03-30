package ca.cars365.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InspectionAgencyRegistrationRequest {

    @NotBlank(message = "Agency name is required")
    @Size(max = 150, message = "Agency name must not exceed 150 characters")
    private String name;

    @NotBlank(message = "Address is required")
    @Size(max = 255, message = "Address must not exceed 255 characters")
    private String address;

    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City must not exceed 100 characters")
    private String city;

    @NotBlank(message = "Province/State is required")
    @Size(max = 100, message = "Province/State must not exceed 100 characters")
    private String provinceState;

    @NotBlank(message = "Postal code is required")
    @Pattern(
        regexp = "^[A-Za-z0-9\\s-]{5,10}$",
        message = "Invalid postal code format"
    )
    private String postalCode;

    @NotBlank(message = "Country is required")
    @Size(max = 100, message = "Country must not exceed 100 characters")
    private String country;

    @NotBlank(message = "License number is required")
    @Size(min = 5, max = 50, message = "License number must be between 5 and 50 characters")
    private String licenseNumber;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 6 characters")
    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*[0-9]).{6,}$",
        message = "Password must contain at least one uppercase letter and one number"
    )
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(
        regexp = "^[0-9]{10}$",
        message = "Phone number must be 10 digits"
    )
    private String phoneNumber;

    @NotBlank(message = "Contact person name is required")
    @Size(max = 150, message = "Contact person name must not exceed 150 characters")
    private String contactPersonName;
}