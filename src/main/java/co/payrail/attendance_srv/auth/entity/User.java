package co.payrail.attendance_srv.auth.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Where;

import java.util.Date;

@Data
@Entity
@Table(name = "auth_srv_tbl")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // Specify the strategy
@DiscriminatorColumn(name = "type")
@Where(clause = "del_Flag='N'")
public class User extends AbstractEntity {

    protected String businessName;

    @Column(unique = true, nullable = false)
    protected String userName;

    protected String firstName;

    protected String lastName;

    @Column(unique = true)
    protected String email;

    protected String phoneNumber;

    protected String password;

    protected String status;

    protected Date expiryDate;

    protected Date lockedUntilDate;

    protected Date lastLoginDate;

    protected int noOfLoginAttempts;

    protected boolean pendingPasswordReset;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    protected Classification classification;

    protected String otp;

    @Enumerated(EnumType.STRING)
    @Column(length = 50) // Set length based on your UserType enum string representation
    protected UserType userType;

    @ManyToOne
    @JoinColumn(name = "role_id") // Ensure not null
    private Roles role;

    protected boolean pinReset;

    protected boolean needSetup;

    protected boolean changePassword;
    @Column(columnDefinition = "BIT DEFAULT 0")
    protected boolean selfRegistration = false;

    @Column(columnDefinition = "BIT DEFAULT 0") // Use BIT for SQL Server
    protected boolean isOnLien = false;
    @Column(columnDefinition = "BIT DEFAULT 0")
    protected boolean isKycComplete = false;

    protected Long parent;
}
