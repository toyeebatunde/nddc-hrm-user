package co.payrail.attendance_srv.kyc_srv.entity;

import co.payrail.attendance_srv.auth.entity.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Data;



@Table(name = "nddc_bvn_data")
@Data
@Entity
public class BVN extends AbstractEntity {

    protected String residentialAddress;

    protected String watchListed;

    protected String gender;

    protected String stateOfOrigin;

    protected String dateOfBirth;

    protected String lgaOfOrigin;

    protected String lgaOfResidence;

    protected String title;

    protected String enrollmentBranch;

    protected String nin;

    @Column(unique = true)
    protected String bvn;

    protected String firstName;

    protected String email;

    protected String stateOfResidence;

    @Column(columnDefinition = "TEXT")
    protected String image;

    protected String enrollmentBank;

    protected String lastName;

    protected String middleName;

    protected String phoneNumber2;

    protected String maritalStatus;

    protected String registrationDate;

    protected String nationality;

    protected String levelOfAccount;

    protected String phoneNumber1;

    protected String nameOnCard;

    protected String error;
}