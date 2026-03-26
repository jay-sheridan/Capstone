package ca.cars365.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ca.cars365.entity.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
	Optional<Users> findByEmail(String email);

	boolean existsByEmail(String email);
	boolean existsByPhoneNumber(String phoneNumber);

}
