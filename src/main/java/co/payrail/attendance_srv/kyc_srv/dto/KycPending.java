package co.payrail.attendance_srv.kyc_srv.dto;

import lombok.Data;

@Data
public class KycPending {

    private Long userId;
    private String userIdentifier;
    private String userName;
    private String userClass;

    private String pendingItems;

}
