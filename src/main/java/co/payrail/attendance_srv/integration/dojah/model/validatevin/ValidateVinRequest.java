package co.payrail.attendance_srv.integration.dojah.model.validatevin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidateVinRequest {
    String vin;
    String state;
    String lastname;
    String firstname;
    String dob;
    String mode;
}
