package co.payrail.attendance_srv.integration.dojah.model.validatenuban;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class ValidateNubanResponse {

	@SerializedName("account_currency")
	private String accountCurrency;

	@SerializedName("gender")
	private String gender;

	@SerializedName("address_1")
	private String address1;

	@SerializedName("expiry_date")
	private String expiryDate;

	@SerializedName("address_2")
	private String address2;

	@SerializedName("last_name")
	private String lastName;

	@SerializedName("other_names")
	private String otherNames;

	@SerializedName("identity_number")
	private String identityNumber;

	@SerializedName("country_code")
	private String countryCode;

	@SerializedName("bank")
	private String bank;

	@SerializedName("nationality")
	private String nationality;

	@SerializedName("phone")
	private String phone;

	@SerializedName("dob")
	private String dob;

	@SerializedName("account_name")
	private String accountName;

	@SerializedName("country_of_birth")
	private String countryOfBirth;

	@SerializedName("identity_type")
	private String identityType;

	@SerializedName("postal_code")
	private String postalCode;

	@SerializedName("state_code")
	private String stateCode;

	@SerializedName("first_name")
	private String firstName;

	@SerializedName("error")
	private String error;
}