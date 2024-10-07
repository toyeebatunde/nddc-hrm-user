package co.payrail.attendance_srv.integration.dojah.model.age_verification;

import lombok.Data;

public @Data class ValidateAgeResponse{
	private String dateOfBirth;
	private String lastName;
	private String firstName;
	private boolean verification;
	private String error;
}