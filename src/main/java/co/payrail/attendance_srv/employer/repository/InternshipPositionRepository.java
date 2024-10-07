package co.payrail.attendance_srv.employer.repository;

import co.payrail.attendance_srv.employer.entity.InternshipPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InternshipPositionRepository extends JpaRepository<InternshipPosition, Long> {
    List<InternshipPosition> findByEmployer_Id(Long employerId);
}
