package co.payrail.attendance_srv.integration.dojah.model.validateImage.validatebvnimage;

import lombok.Data;

import java.io.Serializable;

public @Data class SelfieVerification implements Serializable {
	private boolean match;
	private double confidence_value;
}