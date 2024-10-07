package co.payrail.attendance_srv.kyc_srv.dto;

import lombok.Data;

@Data
public class SetupPinDto {

    private Long userId;
    private String oldPin;
    private String newPin;
    private String confirmPin;

}