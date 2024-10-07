package co.payrail.attendance_srv.employer.repository;

import co.payrail.attendance_srv.employer.entity.Employer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployerRepository extends JpaRepository<Employer, Long> {
    // Find by industry with pagination and sorting
    Page<Employer> findByIndustry(String industry, Pageable pageable);

    Optional<Employer> findByAuthId(Long authId);

    // Search by company name (case-insensitive)
    Page<Employer> findByCompanyNameContainingIgnoreCase(String companyName, Pageable pageable);


}
