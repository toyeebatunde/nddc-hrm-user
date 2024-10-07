package co.payrail.attendance_srv.auth.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Where;



@Entity
@Data
@Table(name = "nddc_api_key")
@Where(clause = "del_Flag = 'N'")
public class ApiKey extends AbstractEntity {

    @Column(name = "api_key", unique = true, nullable = false)
    protected String key;
    @ManyToOne
    protected User user;
    @Column(name = "deleted", nullable = false, columnDefinition = "BIT DEFAULT 0")
    protected boolean deleted = false;
}
