package co.payrail.attendance_srv.auth.dto.output;

import co.payrail.attendance_srv.auth.dto.input.UserDto;
import lombok.Data;

@Data
public class AuthorizationDetails {

    private Long userId ;
    private String userName;
    private String [] permissions;
    private UserDto user ;
    private String userType ;
    private String classification ;
}
