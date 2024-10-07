package co.payrail.attendance_srv.integration.dojah.model.validatecac.validatecacadvance;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class AffiliatesItem{

	@SerializedName("otherDirectorshipDetails")
	private Object otherDirectorshipDetails;

	@SerializedName("firstname")
	private String firstname;

	@SerializedName("shareAllotted")
	private Object shareAllotted;

	@SerializedName("occupation")
	private Object occupation;

	@SerializedName("gender")
	private Object gender;

	@SerializedName("city")
	private String city;

	@SerializedName("corporationName")
	private Object corporationName;

	@SerializedName("companyName")
	private Object companyName;

	@SerializedName("lga")
	private Object lga;

	@SerializedName("formerSurname")
	private Object formerSurname;

	@SerializedName("companyRcNumber")
	private Object companyRcNumber;

	@SerializedName("shareType")
	private Object shareType;

	@SerializedName("dateOfTermination")
	private Object dateOfTermination;

	@SerializedName("corporateName")
	private Object corporateName;

	@SerializedName("othername")
	private String othername;

	@SerializedName("rcNumber")
	private Object rcNumber;

	@SerializedName("surname")
	private String surname;

	@SerializedName("isCorporate")
	private Boolean isCorporate;

	@SerializedName("formerNationality")
	private Object formerNationality;

	@SerializedName("state")
	private String state;

	@SerializedName("formerName")
	private Object formerName;

	@SerializedName("email")
	private String email;

	@SerializedName("searchScore")
	private Object searchScore;

	@SerializedName("address")
	private String address;

	@SerializedName("streetNumber")
	private Object streetNumber;

	@SerializedName("postcode")
	private Object postcode;

	@SerializedName("isChairman")
	private Boolean isChairman;

	@SerializedName("dateOfBirth")
	private Object dateOfBirth;

	@SerializedName("affiliateType")
	private String affiliateType;

	@SerializedName("corporateRcNumber")
	private Object corporateRcNumber;

	@SerializedName("companyId")
	private Integer companyId;

	@SerializedName("phoneNumber")
	private String phoneNumber;

	@SerializedName("nationality")
	private Object nationality;

	@SerializedName("name")
	private String name;

	@SerializedName("dateOfAppointment")
	private Object dateOfAppointment;

	@SerializedName("accreditationnumber")
	private Object accreditationnumber;

	@SerializedName("countryName")
	private String countryName;

	@JsonProperty("status")
	private String status;
}