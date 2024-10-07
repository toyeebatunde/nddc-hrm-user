package co.payrail.attendance_srv.auth.dto.input;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerifyOTPDTO {

    @NotBlank(message = "opt code must not be null")
    private String otp;

    @NotBlank(message = "userName  must not be null")
    private String userName;
}
