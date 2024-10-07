package co.payrail.attendance_srv.kyc_srv.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@AllArgsConstructor
@Getter
@NoArgsConstructor
@ToString
public class CACVerificationRequest {
    @JsonProperty("rcNumber")
    @NotNull(message = "rc number cannot be null")
    @NotBlank(message = "rc number cannot be blank")
    private String rcNumber;

    @JsonProperty("companyName")
    @NotNull(message = "company name cannot be null")
    @NotBlank(message = "company name cannot be blank")
    private String companyName;

    @JsonProperty("userName")
    @NotNull(message = "user name cannot be null")
    @NotBlank(message = "user name cannot be blank")
    private String userName;
}
