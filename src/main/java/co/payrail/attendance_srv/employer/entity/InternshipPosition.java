package co.payrail.attendance_srv.employer.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name="nddc_internship_position")
public class InternshipPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String roleTitle;
    private String roleDesc;
    private String department;
    private String numberOfSlot;
    private String requiredQualifications;
    private List<String> requiredSkills;
    private String tasksAndOutcomes;
    private String workHours;
    private Location workLocation;
    private Boolean additionalStipend;
    private Double stipendAmount;
    private String opportunityForPD;
    private Boolean possibilityForRetaining;
    private Boolean possibilityOfExtendingDuration;
    private Boolean willCompleteDuration;
    @ManyToOne
    @JoinColumn(name = "employer_id")
    private Employer employer;

    // Getters and Setters
}
