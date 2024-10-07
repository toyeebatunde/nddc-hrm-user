package co.payrail.attendance_srv.integration.dojah.model.validatevin;

import lombok.Data;

public @Data class ValidateVinResponse{
	private String voterIdentificationNumber;
	private String gender;
	private String fullName;
	private String registrationAreaWard;
	private String state;
	private String pollingUnitCode;
	private String occupation;
	private String pollingUnit;
	private String localGovernment;
	private String timeOfRegistration;
	private String error;
}