package co.payrail.attendance_srv.auth.dto.input;

import co.payrail.attendance_srv.auth.entity.Classification;
import co.payrail.attendance_srv.auth.entity.UserType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


@GroupSequence({
        AddUserInputDTO.First.class,
        AddUserInputDTO.Second.class,
        AddUserInputDTO.Third.class,
        AddUserInputDTO.class
})
@Data
public class AddUserInputDTO {

    @NotBlank(message = "{user.firstname.notEmpty}", groups = First.class)
    @Size(min = 3, max = 50, message = "{user.firstname.sizeError}", groups = Second.class)
    private String firstname;

    @NotBlank(message = "{user.lastname.notEmpty}", groups = First.class)
    @Size(min = 3, max = 50, message = "{user.lastname.sizeError}", groups = Second.class)
    private String lastname;

    @NotBlank(message = "{user.email.notEmpty}", groups = First.class)
    @Email(message = "{user.email.validEmail}", groups = Second.class)
    private String email;

    private String phoneNumber;

    private String role;

    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    @NotNull(message = "userType cannot be null")
    private UserType userType;

    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    @NotNull(message = "classification cannot be null")
    private Classification classification;

    interface First {
    }

    interface Second {
    }

    interface Third {
    }

}
