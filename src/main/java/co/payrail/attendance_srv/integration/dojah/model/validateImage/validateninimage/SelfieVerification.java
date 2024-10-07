package co.payrail.attendance_srv.integration.dojah.model.validateImage.validateninimage;

import lombok.Data;

public @Data class SelfieVerification{
	private boolean match;
	private double confidenceValue;
}