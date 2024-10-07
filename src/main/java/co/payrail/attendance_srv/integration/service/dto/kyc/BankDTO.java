package co.payrail.attendance_srv.integration.service.dto.kyc;

import co.payrail.attendance_srv.auth.dto.AbstractVerifiableDto;
import lombok.Data;

@Data
public class BankDTO extends AbstractVerifiableDto {

    private String institutionCode;
    private String institutionName;
    private boolean integrated;
    private String bankAccountNumber ;
    private String bankAccountName;
    private String ledgerPurseNumber ;
    private String nipCode;
}
