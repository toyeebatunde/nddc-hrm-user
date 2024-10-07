package co.payrail.attendance_srv.kyc_srv.dto;

import lombok.Data;

@Data
public class OtpValidationDto {

    private String phoneNumber;
    private String email;
    private String otp;
}
