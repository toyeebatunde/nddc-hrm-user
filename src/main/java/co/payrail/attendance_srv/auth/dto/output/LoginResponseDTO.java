package co.payrail.attendance_srv.auth.dto.output;


import co.payrail.attendance_srv.dto.enums.Status;
import co.payrail.attendance_srv.dto.output.StandardResponseDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class LoginResponseDTO extends StandardResponseDTO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String token;

    private Object data;

    private Object kyc;

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(Status status) {
        super(status);
    }

    public LoginResponseDTO(Status status, Object data) {
        super(status);
        this.data = data;
    }

    public LoginResponseDTO(Status status, String token, Object data, Object kyc) {
        super(status);
        this.token = token;
        this.data = data;
        this.kyc = kyc;
    }

}
