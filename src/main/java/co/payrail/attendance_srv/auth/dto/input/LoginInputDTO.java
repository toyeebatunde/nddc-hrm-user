package co.payrail.attendance_srv.auth.dto.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class LoginInputDTO {

    @JsonProperty("phoneNumber")
    @NotNull(message = "phoneNumber cannot be empty")
    @NotBlank(message = "phoneNumber cannot be empty")
    private String phoneNumber;

    @JsonProperty("password")
    @NotNull(message = "password cannot be empty")
    @NotBlank(message = "password cannot be empty")
    private String password;

}
