package co.payrail.attendance_srv.integration.dojah.model.validatebvn;

import lombok.Data;

public @Data class ValidateBvnResponse{
	private DateOfBirth dateOfBirth;
	private LastName lastName;
	private Bvn bvn;
	private FirstName firstName;
	private PhoneNumber phoneNumber;
	private int status;
	private String error;
}