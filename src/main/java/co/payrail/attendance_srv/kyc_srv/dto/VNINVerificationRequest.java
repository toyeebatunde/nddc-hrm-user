package co.payrail.attendance_srv.kyc_srv.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;



@AllArgsConstructor
@Getter
@NoArgsConstructor
@ToString
public class VNINVerificationRequest {
    @JsonProperty("vnin")
    @NotBlank(message = "virtual nin is required")
    private String vnin;

    @JsonProperty("userName")
    @NotBlank(message = "userName is required")
    private String userName;
}
