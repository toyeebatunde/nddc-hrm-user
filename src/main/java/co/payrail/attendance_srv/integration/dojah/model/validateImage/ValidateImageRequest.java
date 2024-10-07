package co.payrail.attendance_srv.integration.dojah.model.validateImage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidateImageRequest {
    String bvn;
    String selfieImage;
    String nin;
    String lastName;
    String FirstName;
    String photoImage;

}
