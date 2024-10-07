package co.payrail.attendance_srv.auth.dto.input;

import co.payrail.attendance_srv.auth.dto.AbstractVerifiableDto;
import co.payrail.attendance_srv.auth.dto.utility.PrettySerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;


import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

@Data
public class UserDto extends AbstractVerifiableDto implements PrettySerializer {

    private String businessName;
    @NotEmpty(message = "userName")
    private String userName;
    private String fullName;
    @NotEmpty(message = "firstName")
    private String firstName;
    @NotEmpty(message = "lastName")
    private String lastName;
//    @NotEmpty(message = "email")
    private String email;
    @NotEmpty(message = "phoneNumber")
    private String phoneNumber;
//    private String password;
    private String createdOnDate;
    private Date expiryDate;
    private String lastLoginDate;
    private String status;

    private String accountStatus;
    protected String transactionPin;

    protected Long parentUserId = 0L ;
    protected String userType;
    protected String classification;
    protected boolean isOnLien;

    protected boolean pinReset;
    protected boolean needSetup ;
    protected boolean changePassword ;
    protected boolean selfRegistration;

    private String middleName;
    private String gender;



    @Override
    @JsonIgnore
    public JsonSerializer<UserDto> getSerializer() {
        return new JsonSerializer<UserDto>() {
            @Override
            public void serialize(UserDto user, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {

                gen.writeStartObject();

                gen.writeStringField("Username", user.userName);
                gen.writeStringField("First Name", user.firstName);
                gen.writeStringField("Last Name", user.lastName);
                gen.writeStringField("Email", user.email);
                gen.writeStringField("Phone", user.phoneNumber);
                gen.writeStringField("Status", user.getStatus());
                gen.writeStringField("accountStatus", user.getAccountStatus());
                gen.writeStringField("Created On Date", user.getCreatedOnDate());
                gen.writeStringField("Last Login Date", user.getLastLoginDate());

                gen.writeEndObject();
            }
        };
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "userName='" + userName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
//                ", password='" + password + '\'' +
                ", createdOnDate='" + createdOnDate + '\'' +
                ", expiryDate=" + expiryDate +
                ", lastLoginDate='" + lastLoginDate + '\'' +
                ", status='" + status + '\'' +
                ", accountStatus='" + accountStatus + '\'' +
                ", transactionPin='" + transactionPin + '\'' +
                ", pinReset=" + pinReset +
                ", needSetup=" + needSetup +
                ", isOnLien=" + isOnLien +
                ", changePassword=" + changePassword +
                ", parentUserId=" + parentUserId +
                ", classification='" + classification + '\'' +
                ", dateCreated=" + dateCreated +
                '}';
    }
}
