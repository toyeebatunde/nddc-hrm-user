package co.payrail.attendance_srv.kyc_srv.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class VerifyBVN_WithSelfieDto {
    @NotNull(message = "bvn is required")
    @NotBlank(message = "bvn is required")
    @JsonProperty("bvn")
    private String bvn;

    @NotNull(message = "selfie image is required")
    @NotBlank(message = "selfie image is required")
    @JsonProperty("base64SelfieImage")
    private String base64SelfieImage;
}
