package co.payrail.attendance_srv.integration.service.dto.kyc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchlessNameEnquiryResponse {

    private String accountName ;
}
