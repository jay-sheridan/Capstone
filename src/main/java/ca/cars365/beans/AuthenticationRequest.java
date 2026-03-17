package ca.cars365.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {

	private String fullName;
	private String email;
	private String password;
	private String phone;
}
