package co.payrail.attendance_srv.integration.service.dto.kyc;

import lombok.Data;

@Data
public class BranchlessAccountEnquiryRequest {
    private String bankCode ;
    private String accountNumber ;
}
