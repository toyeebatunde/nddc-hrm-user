package co.payrail.attendance_srv.kyc_srv.repository;

import co.payrail.attendance_srv.kyc_srv.entity.BVN;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BVNRepository extends JpaRepository<BVN, Long> {
    @Query(value = "select * from bvn_data where bvn=:bvn;", nativeQuery = true)
    BVN findByBvn(String bvn);

    BVN findFirstByBvn(String bvn);
}