package co.payrail.attendance_srv.kyc_srv.dto;

import co.payrail.attendance_srv.auth.dto.AbstractVerifiableDto;
import co.payrail.attendance_srv.auth.entity.User;
import co.payrail.attendance_srv.kyc_srv.entity.KYCStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Toyeeb Atunde
 * @project branchless
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class KycDto extends AbstractVerifiableDto {

    private Long userId;

    private String userIdentifier;
    private String userClass;

    private String pendingItems;

    private String cacBase64Img;
    protected Date cacUploadedDate;
    private KYCStatus cacKycStatus;

    private String selfieBase64Img;
    protected Date selfieUploadedDate;
    private KYCStatus selfieKycStatus;


    private String idCardBase64Img;
    protected Date idCardUploadedDate;
    private KYCStatus idCardKycStatus;

    protected String bvn;
    protected Date bvnUploadedDate;
    private KYCStatus bvnKycStatus;

    protected String nin;
    protected Date ninUploadedDate;
    private KYCStatus ninKycStatus;

    protected String rcNumber;
    protected Date rcNumberUploadedDate;
    protected KYCStatus rcNumberKycStatus;

    protected String contractNo;
    protected Date contractNoUploadedDate;
    protected KYCStatus contractNoKycStatus;

    protected String tin;
    protected Date tinUploadedDate;
    protected KYCStatus tinKycStatus;

    private Date verifiedOrDeclinedDate;
    //private AdminUser verifiedOrDeclinedBy;

}
