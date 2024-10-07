package co.payrail.attendance_srv.kyc_srv.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Lob;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class KycReqquest {

    private String bvnOrNinOrRc;
    @Lob
    private String fileKey;

}
