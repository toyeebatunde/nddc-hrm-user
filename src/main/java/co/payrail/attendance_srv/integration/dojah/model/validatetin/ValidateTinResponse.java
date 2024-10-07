package co.payrail.attendance_srv.integration.dojah.model.validatetin;

import lombok.Data;

public @Data class ValidateTinResponse {
	private String firstin;
	private String search;
	private String taxOffice;
	private String phoneNumber;
	private String jittin;
	private String taxpayerName;
	private String email;
	private String cacRegNumber;
	private String error;
}