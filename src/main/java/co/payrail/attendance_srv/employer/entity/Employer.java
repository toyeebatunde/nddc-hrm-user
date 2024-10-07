package co.payrail.attendance_srv.employer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="nddc_employer")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Employer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String companyName;

    private String industry;
    private Boolean cacCertificationAvailable;
    private Integer yearsPostIncorporation;

    private String companyType;
    @Embedded
    private Location location;
    @Embedded
    private ContactInformation contactInformation;
    private Long authId;

    // Getters and Setters
}
