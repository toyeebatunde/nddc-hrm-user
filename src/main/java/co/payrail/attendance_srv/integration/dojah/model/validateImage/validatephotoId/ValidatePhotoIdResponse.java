package co.payrail.attendance_srv.integration.dojah.model.validateImage.validatephotoId;

import lombok.Data;

public @Data class ValidatePhotoIdResponse{
	private Selfie selfie;
	private LastName lastName;
	private FirstName firstName;
	private String error;
}