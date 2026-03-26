package ca.cars365.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ca.cars365.entity.Vehicle;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

	Optional<Vehicle> findByVinNumber(String vinNumber);

    boolean existsByVinNumber(String vinNumber);

    List<Vehicle> findBySellerId(Long sellerId);
}
