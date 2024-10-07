package co.payrail.attendance_srv.kyc_srv.entity;

import co.payrail.attendance_srv.auth.entity.AbstractEntity;
import co.payrail.attendance_srv.auth.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

/**
 * @author Rasaq Agbalaya
 * @project branchless
 */
@Data
@Entity
@Table(name = "nddc_kyc")
public class Kyc extends AbstractEntity {

    @OneToOne
    private User user;

    @Column(name = "rc_number", columnDefinition = "VARCHAR(255)")
    private String rcNumber;
    protected Date rcNumberUploadedDate;
    @Enumerated(value = EnumType.STRING)
    @NotNull
    protected KYCStatus rcNumberKycStatus = KYCStatus.PENDING_UPLOAD;


    protected String contractNo;
    protected Date contractNoUploadedDate;
    @Enumerated(value = EnumType.STRING)
    @NotNull
    protected KYCStatus contractNoKycStatus = KYCStatus.PENDING_UPLOAD;


    protected String tin;
    protected Date tinUploadedDate;
    @Enumerated(value = EnumType.STRING)
    @NotNull
    protected KYCStatus tinKycStatus = KYCStatus.PENDING_UPLOAD;


    protected String cacBase64Img;
    protected Date cacUploadedDate;
    @Enumerated(value = EnumType.STRING)
    @NotNull
    protected KYCStatus cacKycStatus = KYCStatus.PENDING_UPLOAD;


    protected String selfieBase64Img;
    protected Date selfieUploadedDate;
    @Enumerated(value = EnumType.STRING)
    @NotNull
    protected KYCStatus selfieKycStatus = KYCStatus.PENDING_UPLOAD;


    protected String idCardBase64Img;
    protected Date idCardUploadedDate;
    @Enumerated(value = EnumType.STRING)
    @NotNull
    protected KYCStatus idCardKycStatus = KYCStatus.PENDING_UPLOAD;

    @Column(unique = true)
    protected String bvn;
    protected Date bvnUploadedDate;
    @Enumerated(value = EnumType.STRING)
    @NotNull
    protected KYCStatus bvnKycStatus = KYCStatus.PENDING_UPLOAD;

    @Column(unique = true)
    protected String nin;
    protected Date ninUploadedDate;
    @Enumerated(value = EnumType.STRING)
    @NotNull
    protected KYCStatus ninKycStatus = KYCStatus.VERIFIED;

    private Date verifiedOrDeclinedDate;

    protected String driversLicenceNumber;
    protected String vin;
    protected String passportNumber;
    protected String cacCompanyName;

}
