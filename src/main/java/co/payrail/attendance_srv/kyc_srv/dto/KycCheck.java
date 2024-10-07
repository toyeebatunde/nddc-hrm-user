package co.payrail.attendance_srv.kyc_srv.dto;

import lombok.Data;

@Data
public class KycCheck {
    private Boolean nin = false;
    private Boolean bvn = false;
    private Boolean account = false;
    private Boolean rcnumber = false;
    private Boolean cac = false;
    private Boolean selfie = false;
}
