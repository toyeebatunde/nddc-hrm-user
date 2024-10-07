package co.payrail.attendance_srv.integration.dojah.model.validateImage.validatephotoId;

import lombok.Data;

public @Data class LastName{
	private boolean match;
	private String lastName;
	private int confidenceValue;
}