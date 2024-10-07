package co.payrail.attendance_srv.employer.dto.out;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployerDTO {
    private Long id;
    private Long authId;
    private String companyName;
    private String industry;
    private Boolean cacCertificationAvailable;
    private Integer yearsPostIncorporation;
    private String companyType;
    // Location fields
    private String address;
    private String state;
    private String country;
    private String lga;
    private Double longitude;
    private Double latitude;

    // Contact information fields
    private String email;
    private String phoneNumber;
    private String website;
    private String faxNumber;



    // Constructor, Getters, and Setters
}
