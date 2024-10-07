package co.payrail.attendance_srv.auth.dto.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@GroupSequence({
        BeginResetPasswordDTO.First.class,
        BeginResetPasswordDTO.Second.class,
        BeginResetPasswordDTO.Third.class,
        BeginResetPasswordDTO.class
})
@Data
public class BeginResetPasswordDTO {

    @Email(message = "{user.email.validEmail}", groups = Second.class)
//    @NotBlank(message = "{user.email.notEmpty}", groups = First.class)
//    private String email;

    @JsonProperty("phoneNumber")
    @NotBlank(message = "phoneNumber cannot be empty")
    @Size(min = 14, max = 14, message = "Invalid Phone Number")
    private String phoneNumber;

    interface First {
    }

    interface Second {
    }

    interface Third {
    }
}
