package co.payrail.attendance_srv.auth.repository;

import co.payrail.attendance_srv.auth.entity.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {
    ApiKey findByKey(String key);

    @Query("SELECT a FROM ApiKey a WHERE a.key = :key AND a.deleted = false")
    ApiKey findByKeyAndDeletedFalse(String key);

    @Modifying
    @Transactional
    @Query("UPDATE ApiKey a SET a.delFlag = 'Y' WHERE a.user.id = :userId")
    void deactivateApiKeys(Long userId);
}
