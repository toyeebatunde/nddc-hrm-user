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
public class BvnVerificationRequest {
    @JsonProperty("bvn")
    @NotBlank(message = "bvn is required")
    private String bvn;

    @JsonProperty("userName")
    @NotBlank(message = "userName is required")
    private String userName;
}
