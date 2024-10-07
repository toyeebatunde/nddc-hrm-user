package co.payrail.attendance_srv.integration.dojah.model.age_verification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidateAgeRequest {
    String accountNumber;
    String bvn;
    String phoneNumber;
    String mode;
}
