package co.payrail.attendance_srv.kyc_srv.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KycView {
    private String userId;

    private String userName;
    private String userIdentifier;
    private String userClass;
}
