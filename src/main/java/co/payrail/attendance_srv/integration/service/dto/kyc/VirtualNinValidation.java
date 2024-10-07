package co.payrail.attendance_srv.integration.service.dto.kyc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VirtualNinValidation {
    private String firstName;
    private String lastName;
    private String virtualNin;
}
