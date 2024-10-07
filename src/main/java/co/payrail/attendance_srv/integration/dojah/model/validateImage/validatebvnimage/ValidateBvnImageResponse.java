package co.payrail.attendance_srv.integration.dojah.model.validateImage.validatebvnimage;

import lombok.Data;

import java.io.Serializable;

public @Data class ValidateBvnImageResponse implements Serializable {
	private String residential_address;
	private String watch_listed;
	private String gender;
	private String state_of_origin;
	private String date_of_birth;
	private String lga_of_origin;
	private String lga_of_residence;
	private String title;
	private String enrollment_branch;
	private String nin;
	private SelfieVerification selfie_verification;
	private String bvn;
	private String first_name;
	private String email;
	private String state_of_residence;
	private String enrollment_bank;
	private String image;
	private String last_name;
	private String middle_name;
	private String phone_number2;
	private String marital_status;
	private String registration_date;
	private String nationality;
	private String level_of_account;
	private String name_on_card;
	private String phone_number1;
	private String error;
}