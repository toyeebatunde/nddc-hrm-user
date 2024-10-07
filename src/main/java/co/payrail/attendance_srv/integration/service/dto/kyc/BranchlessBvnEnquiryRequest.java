package co.payrail.attendance_srv.integration.service.dto.kyc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchlessBvnEnquiryRequest {
    private String firstName;
    private String lastName;
    private String bvn ;
}
