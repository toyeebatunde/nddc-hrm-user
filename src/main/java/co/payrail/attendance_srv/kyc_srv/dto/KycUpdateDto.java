package co.payrail.attendance_srv.kyc_srv.dto;


import lombok.Data;

@Data
public class KycUpdateDto {
    private String bvn;
    private String nin;
    private String bankAccountNumber;
    private String bankInstitutionCode;
    private String CACBase64Img;
    private String rcNumber;
    private String selfieBase64Img;
    protected String idCardBase64Img;
}
