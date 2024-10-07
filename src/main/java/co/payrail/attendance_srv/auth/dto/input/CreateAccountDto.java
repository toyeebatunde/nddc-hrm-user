package co.payrail.attendance_srv.auth.dto.input;

import co.payrail.attendance_srv.auth.entity.Classification;
import co.payrail.attendance_srv.auth.entity.UserType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

/**
 * This model represents the signup form for new users
 * @author Ehis Edemakhiota
 * February 27, 2023
 */

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccountDto implements Serializable {
    @JsonProperty("phoneNumber")
    @NotBlank(message = "phoneNumber cannot be empty")
    @Size(min = 14, max = 14, message = "Invalid Phone Number")
    private String phoneNumber;

    @JsonProperty("password")
    @NotBlank(message = "password cannot be empty")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[0-9])(?=.*[A-Z])(?=.*\\W).+$", message = "Password must contain at least one uppercase letter, at least one lowercase letter and at least one special character")
    @Size(min = 6, message = "The length of password must be between 6 and 15 characters.")
    private String password;

    @JsonProperty("confirmPassword")
    @NotBlank(message = "confirmPassword cannot be empty")
    private String confirmPassword;

    @JsonProperty("email")
    private String email;

    @JsonProperty("userType")
    private UserType userType;

    @JsonProperty("classification")
    private Classification classification;

}
