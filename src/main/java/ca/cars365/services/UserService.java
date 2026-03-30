package ca.cars365.services;

import java.util.List;

import org.springframework.stereotype.Service;

import ca.cars365.entity.Users;

@Service
public interface UserService {

	Users saveUser(Users user);

    Users getUserById(Long id);

    Users getUserByEmail(String email);

    List<Users> getAllUsers();

    Users updateUser(Long id, Users updatedUser);

    void deleteUser(Long id);
}
