package co.payrail.attendance_srv.integration.dojah.model.validatecac;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class ValidateCacResponse{

    @SerializedName("rc_number")
    private String rcNumber;

    @SerializedName("type_of_company")
    private String typeOfCompany;

    @SerializedName("address")
    private String address;

    @SerializedName("company_name")
    private String companyName;

    @SerializedName("date_of_registration")
    private String dateOfRegistration;

	private String error;
}