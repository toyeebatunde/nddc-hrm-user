package co.payrail.attendance_srv.kyc_srv.dto;

import lombok.Data;

@Data
public class PasswordChangeDto {

    private Long userId;
    private String resetCode;
    private String newPassword;
}
