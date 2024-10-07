package co.payrail.attendance_srv.employer.service;

import co.payrail.attendance_srv.employer.dto.EmployerMapper;
import co.payrail.attendance_srv.employer.dto.out.EmployerDTO;
import co.payrail.attendance_srv.employer.entity.Employer;
import co.payrail.attendance_srv.employer.repository.EmployerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployerService {
    @Autowired
    private EmployerRepository employerRepository;

    @Cacheable("employers")
    public List<EmployerDTO> getAllEmployers() {
        return employerRepository.findAll()
                .stream()
                .map(EmployerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "employer", key = "#id")
    public EmployerDTO getEmployerById(Long id) {
        Employer employer = employerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employer not found"));
        return EmployerMapper.toDTO(employer);
    }

    @Cacheable(value = "employer", key = "#authId")
    public EmployerDTO getEmployerByOwnerId(Long authId) {
        Employer employer = employerRepository.findByAuthId(authId)
                .orElseThrow(() -> new RuntimeException("Employer not found"));
        return EmployerMapper.toDTO(employer);
    }

    public EmployerDTO getLoginByOwnerId(Long authId) {
        return employerRepository.findByAuthId(authId)
                .map(EmployerMapper::toDTO)
                .orElse(null); // or handle it differently based on your use case
    }


    public Employer addEmployer(Employer employer) {
        return employerRepository.save(employer);
    }

    // Fetch paginated employers
    public Page<Employer> getEmployersPaginated(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        return employerRepository.findAll(pageable);
    }


    // Search employers by industry with pagination and sorting
    public Page<Employer> getEmployersByIndustry(String industry, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        return employerRepository.findByIndustry(industry, pageable);
    }

    // Search employers by company name
    public Page<Employer> searchEmployersByName(String companyName, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        return employerRepository.findByCompanyNameContainingIgnoreCase(companyName, pageable);
    }

    public List<Employer> createMultipleEmployers(List<Employer> employers) {
        return employerRepository.saveAll(employers);
    }
    // Other CRUD methods
}