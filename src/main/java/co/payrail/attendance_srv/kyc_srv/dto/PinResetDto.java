package co.payrail.attendance_srv.kyc_srv.dto;

import lombok.Data;

@Data
public class PinResetDto {

    private Long userId;
    private String oldPassword;

}
