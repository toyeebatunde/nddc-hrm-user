package co.payrail.attendance_srv.integration.dojah.model.validatenuban;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidateNubanRequest {
    String accountNumber;
    String bankCode;
}
