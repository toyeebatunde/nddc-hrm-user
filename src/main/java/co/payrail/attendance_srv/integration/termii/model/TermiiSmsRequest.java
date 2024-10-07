package co.payrail.attendance_srv.integration.termii.model;

import lombok.Data;

@Data
public class TermiiSmsRequest {

    private String to;
    private String from;
    private String sms;
    private String type = "plain";
    private String channel = "dnd";
    private String api_key;

}
