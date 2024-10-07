package co.payrail.attendance_srv.employer.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location {

    private String address;
    private String state;
    private String country;
    private String lga; // Local Government Area
    private Double longitude;
    private Double latitude;


}
