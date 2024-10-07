package co.payrail.attendance_srv.integration.dojah.model.international_passport;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ValidateInternationalPassportResponse {
    @JsonProperty("passport_number")
    protected String passportNumber;

    @JsonProperty("date_of_issue")
    protected String dateOfIssue;

    @JsonProperty("expiry_date")
    protected String expiryDate;

    @JsonProperty("document_type")
    protected String documentType;

    @JsonProperty("issue_place")
    protected String issuePlace;

    @JsonProperty("surname")
    protected String surname;

    @JsonProperty("first_name")
    protected String firstName;

    @JsonProperty("other_names")
    protected String otherNames;

    @JsonProperty("date_of_birth")
    protected String dateOfBirth;
    @JsonProperty("gender")
    protected String gender;

    @JsonProperty("photo")
    protected String photo;

    @JsonProperty("error")
    protected String error;
}
