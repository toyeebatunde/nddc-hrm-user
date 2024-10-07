package co.payrail.attendance_srv.auth.dto.input;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResendOTPDTO {

    @NotBlank(message = "Phone number  must not be null")
    private String phoneNumber;
}
