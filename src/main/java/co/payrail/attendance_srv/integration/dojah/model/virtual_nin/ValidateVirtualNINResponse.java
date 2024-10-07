package co.payrail.attendance_srv.integration.dojah.model.virtual_nin;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ValidateVirtualNINResponse {
    private final String vnin;
    private final String firstname;
    private final String middlename;
    private final String surname;
    private final String userId;
    private final String gender;
    private final String mobile;
    private final String dateOfBirth;
    private final String photo;

    private String error;

}
