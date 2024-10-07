package co.payrail.attendance_srv.integration.dojah.model.validatebvn;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidateBvnRequest {

    String firstName;

    String lastName;

    String dob;

    String bvn;

}
