package co.payrail.attendance_srv.integration.dojah.model.validatephonenumber.validatephonebasic;

import lombok.Data;

public @Data class ValidatePhoneBasicResponse{
	private String firstName;
	private String lastName;
	private String address;
	private String gender;
	private String addressState;
	private String dateOfBirth;
	private String msisdn;
	private String email;
	private String addressCity;
	private String error;
}