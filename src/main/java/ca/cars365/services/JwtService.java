package ca.cars365.services;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import ca.cars365.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	private static final String SECRET_KEY = "VGhpcyBpcyBhIHNlY3JldGtleSAtIGl0IHNob3VsZCBiZSBvcmlnaW5hbCAtIGFuZCBvZiBhIGRlY2VudGxlbmd0aA==";

//	@Value("${application.security.jwt.secret-key}")
//	private static String SECRET_KEY;
//
//	@Value("${application.security.jwt.expiration}")
//	private long jwtExpiration;

	private SecretKey getSigningKey() {
		byte[] keyBytes = SECRET_KEY.getBytes();
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {

		return Jwts.builder().claims(extraClaims).subject(userDetails.getUsername())
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)).signWith(getSigningKey())
				.compact();
	}

	public String generateToken(UserDetails userDetails, String accountType) {
		Map<String, Object> extraClaims = new HashMap<>();
		extraClaims.put("role", userDetails.getAuthorities().stream().findFirst().orElseThrow().getAuthority());
		extraClaims.put("accountType", accountType);
		return generateToken(extraClaims, userDetails);
	}

	public Claims extractAllClaims(String token) {
		return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
	}

	public String extractRole(String token) {
		return extractAllClaims(token).get("role", String.class);
	}

	public String extractAccountType(String token) {
		return extractAllClaims(token).get("accountType", String.class);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public boolean isTokenValid(String token, UserDetails userDetails) {
		String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
}
