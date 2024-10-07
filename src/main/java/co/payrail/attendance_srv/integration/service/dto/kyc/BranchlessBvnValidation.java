package co.payrail.attendance_srv.integration.service.dto.kyc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BranchlessBvnValidation {

    private String firstName;
    private String lastName;
    private String bvn;

}
