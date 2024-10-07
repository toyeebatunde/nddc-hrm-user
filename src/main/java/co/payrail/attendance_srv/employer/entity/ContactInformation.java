package co.payrail.attendance_srv.employer.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactInformation {

    private String email;
    private String phoneNumber;
    private String website;  // Optional
    private String faxNumber;  // Optional

}
