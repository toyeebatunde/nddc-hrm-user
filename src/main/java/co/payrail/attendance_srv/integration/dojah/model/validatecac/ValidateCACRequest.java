package co.payrail.attendance_srv.integration.dojah.model.validatecac;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidateCACRequest {
    String rcNumber;
    String companyName;
    String type;
}
