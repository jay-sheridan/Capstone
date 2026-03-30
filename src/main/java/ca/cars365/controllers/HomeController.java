package ca.cars365.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.cars365.entity.Users;
import ca.cars365.repositories.UserRepository;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/home/test")
@AllArgsConstructor
public class HomeController {
	
	private UserRepository userRepository;

	@GetMapping("/")
	public ResponseEntity<List<Users>> home() {
		List<Users> users = userRepository.findAll();
		return ResponseEntity.ok(users);
	}
	
	@GetMapping("/user")
	public ResponseEntity<String> userTest() {
		return ResponseEntity.ok("Hello, regular user!");
	}
	
	@GetMapping("/agency")
	public ResponseEntity<String> agencyTest() {
		return ResponseEntity.ok("Hello, inspection agency!");
	}
	@GetMapping("/all")
	public ResponseEntity<String> allTest() {
		return ResponseEntity.ok("Hello, authenticated user!");
	}
}
