package co.payrail.attendance_srv.integration.dojah.model.validatedl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidateDlRequest {
    String licenseNumber;
    String dob;
}
