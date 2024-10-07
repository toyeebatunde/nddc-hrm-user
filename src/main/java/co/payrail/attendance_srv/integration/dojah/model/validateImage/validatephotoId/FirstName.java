package co.payrail.attendance_srv.integration.dojah.model.validateImage.validatephotoId;

import lombok.Data;

public @Data class FirstName{
	private boolean match;
	private String firstName;
	private int confidenceValue;
}