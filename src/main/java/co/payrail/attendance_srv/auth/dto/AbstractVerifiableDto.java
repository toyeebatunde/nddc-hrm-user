package co.payrail.attendance_srv.auth.dto;

import lombok.Data;

import java.util.Date;

@Data
public abstract class AbstractVerifiableDto {

    private Long id ;
    private int version;
    protected Date dateCreated;


}
