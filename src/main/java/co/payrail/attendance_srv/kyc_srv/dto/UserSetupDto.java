package co.payrail.attendance_srv.kyc_srv.dto;

import lombok.Data;

@Data
public class UserSetupDto {

    private String userName ;
    private String oldPin ;
    private String newPin ;
    private String deviceId ;
    private String deviceName ;
    //Device type e.g MOBILE_PHONE, TERMINAL
    private String deviceType;

}
