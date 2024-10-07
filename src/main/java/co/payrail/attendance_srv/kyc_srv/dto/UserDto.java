package co.payrail.attendance_srv.kyc_srv.dto;

import co.payrail.attendance_srv.auth.dto.AbstractVerifiableDto;
import co.payrail.attendance_srv.auth.dto.utility.PrettySerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
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
    private String password;
    private String createdOnDate;
    private Date expiryDate;
    private String lastLoginDate;
    private String status;

    private Date dateOfLastRecordedTransaction;

    private String accountStatus;
//    @NotEmpty(message = "userIdentifier")
    protected String userIdentifier;
    protected String transactionPin;
//    @NotEmpty(message = "address")
    protected String address;
    protected String city;
    protected String lga;
    protected String state;
    protected String country;
    protected String dob;
//    @NotEmpty(message = "bvn")
    protected String bvn;

    protected Long parentUserId = 0L ;
    protected String parentUserName ;
    protected String userType;
    protected Long referrerId;
    protected String referrerName;
    protected String classification;
    protected String bankAccountNumber;
    protected String bankInstitutionCode;
    protected String nin;
    protected String vnin;
    protected String tin;
    protected boolean isOnLien;
    protected String contractNo;
    protected String rcNumber;
    protected String CACBase64Img;
    protected String selfieBase64Img;
    protected String idCardBase64Img;
    protected boolean kycComplete;

    protected boolean pinReset;
    protected boolean needSetup ;
    protected boolean changePassword ;
    protected boolean selfRegistration;
    protected String referralCode;

//    private String oldPin ;
//    private String newPin ;
    private String deviceId ;
    private String deviceName ;
    //Device type e.g MOBILE_PHONE, TERMINAL
    private String deviceType;

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
                gen.writeStringField("User ID", user.userIdentifier);
                gen.writeStringField("Username", user.userName);
                gen.writeStringField("First Name", user.firstName);
                gen.writeStringField("Last Name", user.lastName);
                gen.writeStringField("Email", user.email);
                gen.writeStringField("Phone", user.phoneNumber);
                gen.writeStringField("Status", user.getStatus());
                gen.writeStringField("accountStatus", user.getAccountStatus());
                gen.writeStringField("dateOfLastRecordedTransaction", DateFormat.getDateInstance().format(user.getDateOfLastRecordedTransaction()));
                gen.writeStringField("Address", user.getAddress());
                gen.writeStringField("City", user.getCity());
                gen.writeStringField("State", user.getState());
                gen.writeStringField("Country", user.getCountry());
                gen.writeStringField("BVN", user.getBvn());
                gen.writeStringField("NIN", user.getNin());
                gen.writeStringField("TIN", user.getTin());
                gen.writeStringField("CONTRACT NO", user.getContractNo());
                gen.writeStringField("RC NUMBER", user.getRcNumber());
                gen.writeStringField("Account Number", user.getBankAccountNumber());
                gen.writeStringField("Bank CBN Code", user.getBankInstitutionCode());
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
                ", password='" + password + '\'' +
                ", createdOnDate='" + createdOnDate + '\'' +
                ", expiryDate=" + expiryDate +
                ", lastLoginDate='" + lastLoginDate + '\'' +
                ", status='" + status + '\'' +
                ", accountStatus='" + accountStatus + '\'' +
                ", userIdentifier='" + userIdentifier + '\'' +
                ", transactionPin='" + transactionPin + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", lga='" + lga + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", bvn='" + bvn + '\'' +
                ", pinReset=" + pinReset +
                ", needSetup=" + needSetup +
                ", isOnLien=" + isOnLien +
                ", changePassword=" + changePassword +
                ", parentUserId=" + parentUserId +
                ", parentUserName='" + parentUserName + '\'' +
                ", userType='" + userType + '\'' +
                ", classification='" + classification + '\'' +
                ", bankAccountNumber='" + bankAccountNumber + '\'' +
                ", bankInstitutionCode='" + bankInstitutionCode + '\'' +
                ", nin='" + nin + '\'' +
                ", rcNumber='" + rcNumber + '\'' +
                ", referralCode='" + referralCode + '\'' +
                ", dateOfLastRecordedTransaction='" + dateOfLastRecordedTransaction + '\'' +
                ", dateCreated=" + dateCreated +
                '}';
    }
}
