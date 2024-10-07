package co.payrail.attendance_srv.kyc_srv.dto;


import co.payrail.attendance_srv.auth.dto.AbstractVerifiableDto;
import co.payrail.attendance_srv.auth.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiKeyResponseDTO extends AbstractVerifiableDto {

    private String token;
    private Object data;
    private Long id;
    private String key;
    private User user;

}
