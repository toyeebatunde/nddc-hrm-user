package co.payrail.attendance_srv.auth.entity;

import lombok.Getter;

import java.io.Serializable;

@Getter
public enum UserType implements Serializable {

    ORGANIZATION,
    USER,
    STAFF;
}
