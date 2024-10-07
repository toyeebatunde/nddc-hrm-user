package co.payrail.attendance_srv.integration.dojah.model.validatecac.validatecacadvance;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class ValidateCacAdvanceResponse{

	@SerializedName("Status")
	private String status;

	@SerializedName("Email")
	private String email;

	@SerializedName("Name_of_Company")
	private String nameOfCompany;

	@SerializedName("LGA")
	private String lGA;

	@SerializedName("City")
	private String city;

	@SerializedName("Date_of_Registration")
	private String dateOfRegistration;

	@SerializedName("Share_capital_in_words")
	private String shareCapitalInWords;

	@SerializedName("Head_Office_Address")
	private String headOfficeAddress;

	@SerializedName("Number_of_Affiliates")
	private String numberOfAffiliates;

	@SerializedName("Share_capital")
	private String shareCapital;

	@SerializedName("State")
	private String state;

	@SerializedName("Classification")
	private String classification;

	@SerializedName("Affiliates")
	private List<AffiliatesItem> affiliates;

	@SerializedName("Branch_Address")
	private String branchAddress;

	@SerializedName("Type_of_Company")
	private String typeOfCompany;

	@SerializedName("RC_Number")
	private String rCNumber;

	@SerializedName("error")
	private String error;
}