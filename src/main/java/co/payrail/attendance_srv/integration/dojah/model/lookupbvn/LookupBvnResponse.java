package co.payrail.attendance_srv.integration.dojah.model.lookupbvn;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.ToString;

@Data
public class LookupBvnResponse{

	@SerializedName("residential_address")
	private String residentialAddress;

	@SerializedName("watch_listed")
	private String watchListed;

	@SerializedName("gender")
	private String gender;

	@SerializedName("state_of_origin")
	private String stateOfOrigin;

	@SerializedName("date_of_birth")
	private String dateOfBirth;

	@SerializedName("lga_of_origin")
	private String lgaOfOrigin;

	@SerializedName("lga_of_residence")
	private String lgaOfResidence;

	@SerializedName("title")
	private String title;

	@SerializedName("enrollment_branch")
	private String enrollmentBranch;

	@SerializedName("nin")
	private String nin;

	@SerializedName("bvn")
	private String bvn;

	@SerializedName("first_name")
	private String firstName;

	@SerializedName("email")
	private String email;

	@SerializedName("state_of_residence")
	private String stateOfResidence;

	@SerializedName("image")
	@ToString.Exclude
	private String image;

	@SerializedName("enrollment_bank")
	private String enrollmentBank;

	@SerializedName("last_name")
	private String lastName;

	@SerializedName("middle_name")
	private String middleName;

	@SerializedName("phone_number2")
	private String phoneNumber2;

	@SerializedName("marital_status")
	private String maritalStatus;

	@SerializedName("registration_date")
	private String registrationDate;

	@SerializedName("nationality")
	private String nationality;

	@SerializedName("level_of_account")
	private String levelOfAccount;

	@SerializedName("phone_number1")
	private String phoneNumber1;

	@SerializedName("name_on_card")
	private String nameOnCard;

	@SerializedName("error")
	private String error;
}