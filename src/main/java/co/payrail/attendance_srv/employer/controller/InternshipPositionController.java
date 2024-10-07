package co.payrail.attendance_srv.employer.controller;

import co.payrail.attendance_srv.employer.entity.InternshipPosition;
import co.payrail.attendance_srv.employer.service.InternshipPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/internship-positions")
public class InternshipPositionController {

    @Autowired
    private InternshipPositionService internshipPositionService;

    // Get all internship positions
    @GetMapping
    public List<InternshipPosition> getAllInternshipPositions() {
        return internshipPositionService.getAllInternshipPositions();
    }

    // Get internship positions for a specific employer
    @GetMapping("/employer/{employerId}")
    public List<InternshipPosition> getInternshipPositionsByEmployer(@PathVariable Long employerId) {
        return internshipPositionService.getInternshipPositionsByEmployer(employerId);
    }

    // Add an internship position
    @PostMapping("/employer/{employerId}")
    public InternshipPosition addInternshipPosition(@RequestBody InternshipPosition internshipPosition,
                                                    @PathVariable Long employerId) {
        return internshipPositionService.addInternshipPosition(internshipPosition, employerId);
    }

    // Update an internship position
    @PutMapping("/{id}")
    public InternshipPosition updateInternshipPosition(@PathVariable Long id,
                                                       @RequestBody InternshipPosition internshipPosition) {
        return internshipPositionService.updateInternshipPosition(id, internshipPosition);
    }

    // Delete an internship position
    @DeleteMapping("/{positionId}")
    public ResponseEntity<Void> deletePosition(@PathVariable Long positionId) {
        internshipPositionService.deleteInternshipPosition(positionId);
        return ResponseEntity.noContent().build();
    }

}

