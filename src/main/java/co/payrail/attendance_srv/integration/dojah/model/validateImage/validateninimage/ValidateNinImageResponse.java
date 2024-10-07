package co.payrail.attendance_srv.integration.dojah.model.validateImage.validateninimage;

import lombok.Data;

import java.util.List;

public @Data class ValidateNinImageResponse{
	private String gender;
	private String signature;
	private String birthDate;
	private String title;
	private String birthState;
	private String birthCountry;
	private String originState;
	private String reference;
	private String residenceStatus;
	private String surname;
	private SelfieVerification selfieVerification;
	private String employmentStatus;
	private String originLga;
	private String firstName;
	private Object email;
	private String height;
	private String profession;
	private Object maidenName;
	private String originPlace;
	private String centralId;
	private String birthLga;
	private String residenceAddress;
	private String telephone;
	private String middleName;
	private String nokFirstName;
	private String picture;
	private String residenceState;
	private String religion;
	private String nokState;
	private String maritalStatus;
	private String nokSurname;
	private String nationality;
	private String educationalLevel;
	private List<Object> nokAddress2;
	private String nokLga;
	private String nokAddress1;
	private String nokTown;
	private String residenceLga;
	private String spokenLanguage;
	private String residenceTown;
	private String error;
}