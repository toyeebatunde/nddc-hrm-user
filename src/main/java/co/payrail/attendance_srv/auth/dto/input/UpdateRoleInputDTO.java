package co.payrail.attendance_srv.auth.dto.input;

import lombok.Data;

import java.util.List;


@Data
public class UpdateRoleInputDTO {

    private String name;

    private List<String> permissions;

}
