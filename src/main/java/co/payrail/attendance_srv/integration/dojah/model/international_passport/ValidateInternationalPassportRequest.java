package co.payrail.attendance_srv.integration.dojah.model.international_passport;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ValidateInternationalPassportRequest {
    private String passportNumber;
    private String lastName;
}
