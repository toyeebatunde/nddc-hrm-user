package co.payrail.attendance_srv.integration.service.dto.kyc;

import lombok.Data;

@Data
public class BranchlessNameEnquiryRequest {

    private String accountNumber ;
    private String bankCode;
}
