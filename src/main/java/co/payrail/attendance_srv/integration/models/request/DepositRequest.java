package co.payrail.attendance_srv.integration.models.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class DepositRequest {

    private String accountName;
    private String accountNumber;
    private Double amount;
    private String cbnBankCode;
    private String transactionRef;
}
