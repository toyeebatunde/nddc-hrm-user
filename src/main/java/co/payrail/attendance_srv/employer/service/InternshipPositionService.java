package co.payrail.attendance_srv.employer.service;

import co.payrail.attendance_srv.employer.entity.Employer;
import co.payrail.attendance_srv.employer.entity.InternshipPosition;
import co.payrail.attendance_srv.employer.repository.EmployerRepository;
import co.payrail.attendance_srv.employer.repository.InternshipPositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InternshipPositionService {

    @Autowired
    private InternshipPositionRepository internshipPositionRepository;

    @Autowired
    private EmployerRepository employerRepository;

    // Fetch all internship positions
    public List<InternshipPosition> getAllInternshipPositions() {
        return internshipPositionRepository.findAll();
    }

    // Fetch internship positions by employer ID
    public List<InternshipPosition> getInternshipPositionsByEmployer(Long employerId) {
        return internshipPositionRepository.findByEmployer_Id(employerId);
    }

    // Add a new internship position
    public InternshipPosition addInternshipPosition(InternshipPosition position, Long employerId) {
        Employer employer = employerRepository.findById(employerId)
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        position.setEmployer(employer);
        return internshipPositionRepository.save(position);
    }

    // Update an internship position
    public InternshipPosition updateInternshipPosition(Long id, InternshipPosition positionDetails) {
        InternshipPosition existingPosition = internshipPositionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Internship position not found"));

        existingPosition.setDepartment(positionDetails.getDepartment());
        existingPosition.setRequiredQualifications(positionDetails.getRequiredQualifications());
        existingPosition.setTasksAndOutcomes(positionDetails.getTasksAndOutcomes());
        existingPosition.setWorkHours(positionDetails.getWorkHours());
        existingPosition.setWorkLocation(positionDetails.getWorkLocation());
        existingPosition.setAdditionalStipend(positionDetails.getAdditionalStipend());
        existingPosition.setStipendAmount(positionDetails.getStipendAmount());

        return internshipPositionRepository.save(existingPosition);
    }

    // Delete an internship position
    public void deleteInternshipPosition(Long id) {
        InternshipPosition position = internshipPositionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Internship position not found"));
        internshipPositionRepository.delete(position);
    }
}

