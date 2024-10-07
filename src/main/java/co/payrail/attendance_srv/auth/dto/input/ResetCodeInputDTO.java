package co.payrail.attendance_srv.auth.dto.input;


import lombok.Data;

@Data
public class ResetCodeInputDTO {

    private String otpCode;
}
