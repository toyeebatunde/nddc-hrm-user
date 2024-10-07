package co.payrail.attendance_srv.integration.dojah.model.validateImage.validatephotoId;

import lombok.Data;

public @Data class Selfie{
	private boolean match;
	private int confidenceValue;
}