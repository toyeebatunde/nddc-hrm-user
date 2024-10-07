package co.payrail.attendance_srv.integration.termii.model;

import lombok.Data;

@Data
public class TermiiSmsResponse {

    private String message_id;
    private String message;
    private float balance;
    private String user;
}
