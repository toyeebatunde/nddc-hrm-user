package co.payrail.attendance_srv.kyc_srv.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Toyeeb Atunde
 * @project angala-backend
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ValidateQuestionDto {

    String phoneNumber;
    int number;
    String answer;
}
