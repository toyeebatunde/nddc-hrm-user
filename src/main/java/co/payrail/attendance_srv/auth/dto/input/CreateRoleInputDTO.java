package co.payrail.attendance_srv.auth.dto.input;

import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@GroupSequence({
        CreateRoleInputDTO.First.class,
        CreateRoleInputDTO.Second.class,
        CreateRoleInputDTO.Third.class,
        CreateRoleInputDTO.class
})
@Data
public class CreateRoleInputDTO {

    @NotBlank(message = "{role.name.notEmpty}", groups = First.class)
    private String name;

    private List<String> permissions;



    interface First {
    }

    interface Second {
    }

    interface Third {
    }
}
