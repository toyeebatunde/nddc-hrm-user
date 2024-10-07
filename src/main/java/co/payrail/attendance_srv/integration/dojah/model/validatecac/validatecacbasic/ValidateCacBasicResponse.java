package co.payrail.attendance_srv.integration.dojah.model.validatecac.validatecacbasic;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class ValidateCacBasicResponse{

	@SerializedName("rc_number")
	private String rcNumber;

	@SerializedName("company_type")
	private String companyType;

	@SerializedName("company_classification")
	private Object companyClassification;

	@SerializedName("address")
	private String address;

	@SerializedName("city")
	private String city;

	@SerializedName("business_nature_cat")
	private Object businessNatureCat;

	@SerializedName("share_capital_words")
	private String shareCapitalWords;

	@SerializedName("classification")
	private String classification;

	@SerializedName("business_nature")
	private String businessNature;

	@SerializedName("reg_date")
	private String regDate;

	@SerializedName("share_capital")
	private String shareCapital;

	@SerializedName("company_name")
	private String companyName;

	@SerializedName("objectives")
	private String objectives;

	@SerializedName("state")
	private String state;

	@SerializedName("email")
	private String email;

	@SerializedName("error")
	private String error;
}