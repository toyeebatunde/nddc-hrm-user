package co.payrail.attendance_srv.kyc_srv.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author RToyeeb Atunde
 * @project angala-backend
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SecurityQuestionDto {
    private Long userId;

    //Security questions

    protected String question1;
    protected String answer1;
    protected String question2;
    protected String answer2;
}
