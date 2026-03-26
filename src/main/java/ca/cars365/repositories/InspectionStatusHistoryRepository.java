package ca.cars365.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ca.cars365.entity.InspectionStatusHistory;

@Repository
public interface InspectionStatusHistoryRepository extends JpaRepository<InspectionStatusHistory, Long> {

	List<InspectionStatusHistory> findByInspectionRequestIdOrderByCreatedAtAsc(Long inspectionRequestId);
}
