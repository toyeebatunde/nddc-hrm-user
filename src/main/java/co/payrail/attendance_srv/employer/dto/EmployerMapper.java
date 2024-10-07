package co.payrail.attendance_srv.employer.dto;

import co.payrail.attendance_srv.employer.dto.out.EmployerDTO;
import co.payrail.attendance_srv.employer.entity.ContactInformation;
import co.payrail.attendance_srv.employer.entity.Employer;
import co.payrail.attendance_srv.employer.entity.Location;
import lombok.Data;

@Data
public class EmployerMapper {

    public static EmployerDTO toDTO(Employer employer) {
        Location location = employer.getLocation();
        ContactInformation contactInformation = employer.getContactInformation();

        return new EmployerDTO(
                employer.getId(),
                employer.getAuthId(),
                employer.getCompanyName(),
                employer.getIndustry(),
                employer.getCacCertificationAvailable(),
                employer.getYearsPostIncorporation(),
                employer.getCompanyType(),
                location.getAddress(),
                location.getState(),
                location.getCountry(),
                location.getLga(),
                location.getLongitude(),
                location.getLatitude(),
                contactInformation.getEmail(),
                contactInformation.getPhoneNumber(),
                contactInformation.getWebsite(),
                contactInformation.getFaxNumber()
        );
    }

    public static Employer toEntity(EmployerDTO employerDTO) {
        Location location = new Location(
                employerDTO.getAddress(),
                employerDTO.getState(),
                employerDTO.getCountry(),
                employerDTO.getLga(),
                employerDTO.getLongitude(),
                employerDTO.getLatitude()
        );

        ContactInformation contactInformation = new ContactInformation(
                employerDTO.getEmail(),
                employerDTO.getPhoneNumber(),
                employerDTO.getWebsite(),
                employerDTO.getFaxNumber()
        );

        return new Employer(
                employerDTO.getId(),
                employerDTO.getCompanyName(),
                employerDTO.getIndustry(),

                employerDTO.getCacCertificationAvailable(),
                employerDTO.getYearsPostIncorporation(),
                employerDTO.getCompanyType(),
                location,
                contactInformation,
                employerDTO.getAuthId()
        );
    }
}
