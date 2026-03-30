package ca.cars365.filters;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import ca.cars365.enums.UserRole;
import ca.cars365.services.InspectionAgencyDetailsService;
import ca.cars365.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;
	private final InspectionAgencyDetailsService inspectionAgencyDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authHeader = request.getHeader("Authorization");
		String jwt;
		String username;
		String accountType;

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {

			filterChain.doFilter(request, response);
			return;
		}

		jwt = authHeader.substring(7);
		username = jwtService.extractUsername(jwt);
		accountType = jwtService.extractAccountType(jwt);

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			UserDetails userDetails = null;
			if (UserRole.valueOf(accountType).equals(UserRole.REGULAR_USER)) {
				
				 userDetails = userDetailsService.loadUserByUsername(username);

			}
			 else if (UserRole.valueOf(accountType).equals(UserRole.INSPECTION_AGENCY)) {
			
				  userDetails = inspectionAgencyDetailsService.loadByLicenseNumber(username);
			 }
			else {
				filterChain.doFilter(request, response);
				return;
			}

			if (jwtService.isTokenValid(jwt, userDetails)) {

				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
						null, userDetails.getAuthorities());

				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}

		filterChain.doFilter(request, response);
	}

}
