package co.payrail.attendance_srv.kyc_srv.repository;

import co.payrail.attendance_srv.auth.entity.User;
import co.payrail.attendance_srv.kyc_srv.entity.KYCStatus;
import co.payrail.attendance_srv.kyc_srv.entity.Kyc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Toyeeb Atunde
 * @project branchless
 */
@Repository
public interface KycRepository extends JpaRepository<Kyc, Long> {

    Kyc findByUser(User user);

    Kyc findByUser_Id(Long userId);

    Boolean existsByUser(User user);

    Kyc findFirstByNinIgnoreCase(String nin);

    Kyc findFirstByRcNumberIgnoreCase(String rcNumber);

    Kyc findFirstByContractNoIgnoreCase(String contractNo);

    Kyc findFirstByTinIgnoreCase(String tin);

    Kyc findFirstByBvnIgnoreCase(String bvn);

    Optional<Kyc> findKycByBvn(String bvn);

    List<Kyc> findByBvnKycStatus(KYCStatus status);
    List<Kyc> findByNinKycStatus(KYCStatus status);
    List<Kyc> findByIdCardKycStatus(KYCStatus status);
    List<Kyc> findByCacKycStatus(KYCStatus status);
    List<Kyc> findBySelfieKycStatus(KYCStatus status);

//    Page<Kyc> findByTinKycStatusOrContractNoKycStatusOrRcNumberKycStatusOrCacKycStatusOrSelfieKycStatusOrIdCardKycStatusOrBvnKycStatusOrNinKycStatus(KYCStatus tin, KYCStatus cNo, KYCStatus rc, KYCStatus cac, KYCStatus selfie, KYCStatus id, KYCStatus bvn, KYCStatus nin);

    @Query(value = "select k.user_id from kyc as k where k.tin_kyc_status = 'PENDING_VERIFICATION' or k.contract_no_kyc_status = 'PENDING_VERIFICATION' or k.rc_number_kyc_status = 'PENDING_VERIFICATION'or k.cac_kyc_status = 'PENDING_VERIFICATION' or k.selfie_kyc_status = 'PENDING_VERIFICATION' or k.id_card_kyc_status = 'PENDING_VERIFICATION' or k.bvn_kyc_status = 'PENDING_VERIFICATION' or k.nin_kyc_status = 'PENDING_VERIFICATION' order by id desc ", nativeQuery = true)
    List<String> findByTinKycStatusOrContractNoKycStatusOrRcNumberKycStatusOrCacKycStatusOrSelfieKycStatusOrIdCardKycStatusOrBvnKycStatusOrNinKycStatus(KYCStatus tin, KYCStatus cNo, KYCStatus rc, KYCStatus cac, KYCStatus selfie, KYCStatus id, KYCStatus bvn, KYCStatus nin);

//    @Query(value = "select count(id) from kyc where user_id=:id", nativeQuery = true)
//    BigInteger findIdByUser_Id(Long id);

}
