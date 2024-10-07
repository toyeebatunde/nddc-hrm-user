package co.payrail.attendance_srv.kyc_srv.dto;

import lombok.Data;

@Data
public class UserValidator {

    private String userIdentifier;
    private String name;
    private String email;
    private String address;

    private String externalAccountNo;
    private String externalAccountName;
    private String externalCustomerId;
    private String externalBankName;

}
