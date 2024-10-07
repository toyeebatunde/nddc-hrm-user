package co.payrail.attendance_srv.kyc_srv.service;

import co.payrail.attendance_srv.auth.dto.input.UserDto;
import co.payrail.attendance_srv.auth.entity.User;
import co.payrail.attendance_srv.kyc_srv.dto.KycDto;
import co.payrail.attendance_srv.kyc_srv.dto.KycReqquest;
import co.payrail.attendance_srv.kyc_srv.dto.KycView;
import co.payrail.attendance_srv.kyc_srv.entity.Kyc;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * @author Toyeeb Atunde
 * @project branchless
 */
public interface KycService {

    Map<String, String> uploadBvn(User user, KycReqquest kycRequestModel);

    String uploadNin(User user, KycReqquest kycRequestModel);

    String uploadRcNumber(User user, KycReqquest kycRequestModel);

    String uploadTin(User user, KycReqquest kycRequestModel);

    String uploadContractNo(User user, KycReqquest kycRequestModel);

    String uploadSelfie(User user, KycReqquest kycRequestModel);

    String uploadCac(User user, KycReqquest kycRequestModel);

    String uploadIdCard(User user, KycReqquest kycRequestModel);

    KycDto getUserKyc(User user);

    Kyc getKycByUser(User user);

    KycDto getKyc(Long id);

    void checkBvn(String bvn);

    String checkNin(String nin);

    void checkRCNumber(String rcNumber);

    void validateNin(String nin);

    void checkTin(String tin);

    void checkContractNo(String cNo);

    String createUserKyc(UserDto userDto);

    String createUserKyc(co.payrail.attendance_srv.kyc_srv.dto.UserDto userDto);

    String verifyBvnKyc(Long id);

    String verifyNinKyc(Long id);
//    String verifyVirtualNIN(Long id);

    String verifyRcNumberKyc(Long id);

    String verifySelfieKyc(Long id);

    String verifyCacKyc(Long id);

    String verifyTinKyc(Long id);

    String verifyIdCardKyc(Long id);

    String verifyContractNoKyc(Long id);

    String declineBvnKyc(Long id);

    String declineNinKyc(Long id);

    String declineRcNumberKyc(Long id);

    String declineSelfieKyc(Long id);

    String declineCacKyc(Long id);

    String declineTinKyc(Long id);

    String declineIdCardKyc(Long id);

    String declineContractNoKyc(Long id);

    boolean isKycComplete(User user);

    List<KycView> getPending(Pageable pageable);

    void saveKyc(Kyc kyc);
    Map<String, String> verifyBvnAndUpdateUserDetails(String bvn, String userName);

    Map<String, String> verifyVirtualNINAndUpdateUserDetails(String vnin, String userName);

    String verifyCac(String rcNumber, String companyName, String userName) throws Exception;
//    Page<KycDto> getPending(Pageable pageDetails);

}
