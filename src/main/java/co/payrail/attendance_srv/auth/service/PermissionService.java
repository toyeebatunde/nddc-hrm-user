package co.payrail.attendance_srv.auth.service;

import co.payrail.attendance_srv.auth.entity.Permission;
import co.payrail.attendance_srv.auth.repository.PermissionRepository;
import co.payrail.attendance_srv.config.TokenProvider;
import co.payrail.attendance_srv.dto.enums.Status;
import co.payrail.attendance_srv.dto.output.BasicResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionService {


    private final TokenProvider tokenProvider;
    private final PermissionRepository permissionRepository;

    public BasicResponseDTO fetchPermissions() {
        List<Permission> permissions = permissionRepository.findAll();
        return new BasicResponseDTO(Status.SUCCESS,permissions);
    }



}
