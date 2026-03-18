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
public class RegistrationRequest {
	
	@NotBlank(message = "Full name is required")
	private String fullName;
	
	@NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
	private String email;
	
	@NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
	@Pattern(
		    regexp = "^(?=.*[A-Z])(?=.*[0-9]).{6,}$",
		    message = "Password must contain at least one uppercase letter and one number"
		)
	private String password;
	
	@NotBlank(message = "Phone number is required")
	@Pattern(regexp = "^[0-9]{10}$", message = "Phone must be 10 digits")
	private String phone;
	
}
