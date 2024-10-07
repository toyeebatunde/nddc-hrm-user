package co.payrail.attendance_srv.integration.service.dto.kyc;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class BranchlessNinValidation {
    private String nin;
    private String firstName;
    private String lastName;

}
