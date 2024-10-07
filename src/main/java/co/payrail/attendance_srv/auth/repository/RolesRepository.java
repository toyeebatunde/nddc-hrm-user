package co.payrail.attendance_srv.auth.repository;
import co.payrail.attendance_srv.auth.entity.Roles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolesRepository extends JpaRepository<Roles,Long> {
    Optional<Roles> findByNameAndOwnerId(String name, Long ownerId);

    Page<Roles> findByNameAndOwnerId(String pattern, Long ownerId, Pageable pageable);

    Page<Roles> findByOwnerId(Long ownerId, Pageable pageable);

}
