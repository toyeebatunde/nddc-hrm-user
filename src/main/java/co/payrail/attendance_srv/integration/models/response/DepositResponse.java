package co.payrail.attendance_srv.integration.models.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class DepositResponse {
    private boolean sucess = false ;
    private String respDesc ;
    private String respCode;
}
