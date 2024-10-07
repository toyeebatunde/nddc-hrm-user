package co.payrail.attendance_srv.integration.dojah.model.validatedl;

import lombok.Data;

public @Data class ValidateDlResponse{
	private String expiryDate;
	private String firstName;
	private String lastName;
	private String issuedDate;
	private String stateOfIssue;
	private String licenseNo;
	private String gender;
	private String photo;
	private String middleName;
	private String uuid;
	private String birthDate;
	private String error;
}