package co.payrail.attendance_srv.employer.controller;

import co.payrail.attendance_srv.employer.dto.out.EmployerDTO;
import co.payrail.attendance_srv.employer.entity.Employer;
import co.payrail.attendance_srv.employer.service.EmployerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employers")
public class EmployerController {

    @Autowired
    private EmployerService employerService;



    @PostMapping
    public Employer createEmployer(@RequestBody Employer employer) {
        return employerService.addEmployer(employer);
    }

    @GetMapping
    public ResponseEntity<Page<Employer>> getEmployers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "companyName") String sortBy) {
        System.out.println("SORT+++++ "+sortBy);
        Page<Employer> employers = employerService.getEmployersPaginated(page, size, sortBy);
        return ResponseEntity.ok(employers);
    }

    @GetMapping("/{authId}")
    public ResponseEntity<EmployerDTO> getEmployer(
            @PathVariable  Long authId) {
        EmployerDTO employer = employerService.getEmployerByOwnerId(authId);
        return ResponseEntity.ok(employer);
    }

    // Search by industry with pagination
    @GetMapping("/search/industry")
    public ResponseEntity<Page<Employer>> getEmployersByIndustry(
            @RequestParam String industry,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "companyName") String sortBy) {
        Page<Employer> employers = employerService.getEmployersByIndustry(industry, page, size, sortBy);
        return ResponseEntity.ok(employers);
    }

    // Search by company name with pagination
    @GetMapping("/search/name")
    public ResponseEntity<Page<Employer>> searchEmployersByName(
            @RequestParam String companyName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "companyName") String sortBy) {
        Page<Employer> employers = employerService.searchEmployersByName(companyName, page, size, sortBy);
        return ResponseEntity.ok(employers);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<Employer>> createMultipleEmployers(@RequestBody List<Employer> employers) {
        List<Employer> createdEmployers = employerService.createMultipleEmployers(employers);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployers);
    }

    // Other endpoints
}

