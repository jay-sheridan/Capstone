package ca.cars365.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import ca.cars365.entity.Users;
import ca.cars365.repositories.UserRepository;
import ca.cars365.services.UserService;
import ca.cars365.utils.SecurityUtils;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{

	private final UserRepository userRepository;
	
	@Override
	public Users saveUser(Users user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Users getUserById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Users getUserByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Users> getAllUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Users updateUser(Long id, Users updatedUser) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteUser(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Users getCurrentUserEntity() {
	    String email = SecurityUtils.getCurrentUserEmail();
	    return userRepository.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("User not found"));
	}

}
