package co.payrail.attendance_srv.auth.repository;

import co.payrail.attendance_srv.auth.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<Permission,Long> {
    List<Permission> findAllByCategory(String category);

    Permission findByCode(String code);

    boolean existsByCode(String code);
}
