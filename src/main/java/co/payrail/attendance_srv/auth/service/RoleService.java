package co.payrail.attendance_srv.auth.service;


import co.payrail.attendance_srv.auth.dto.enums.RoleEnum;
import co.payrail.attendance_srv.auth.dto.enums.RoleType;
import co.payrail.attendance_srv.auth.dto.input.CreateRoleInputDTO;
import co.payrail.attendance_srv.auth.dto.input.PermissionDTO;
import co.payrail.attendance_srv.auth.dto.input.UpdateRoleInputDTO;
import co.payrail.attendance_srv.auth.entity.Permission;
import co.payrail.attendance_srv.auth.entity.Roles;
import co.payrail.attendance_srv.auth.repository.PermissionRepository;
import co.payrail.attendance_srv.auth.repository.RolesRepository;
import co.payrail.attendance_srv.config.TokenProvider;
import co.payrail.attendance_srv.dto.enums.Status;
import co.payrail.attendance_srv.dto.output.BasicResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleService {

    private final RolesRepository rolesRepository;

    private final PermissionRepository permissionRepository;


    private final TokenProvider tokenProvider;

    private Collection<Permission> getAllPermission(List<String> permissionsList) {
        List<Permission> permissions = new ArrayList<>();
        permissionsList.forEach(name -> {
            Permission permission = permissionRepository.findByCode(name.toUpperCase());
            permissions.add(permission);
        });
        return permissions;
    }

    private Roles updateRoleObject(UpdateRoleInputDTO dto, Roles role) {
        if(Objects.nonNull(dto.getName())){
            role.setName(dto.getName());
        }
        if(Objects.nonNull(dto.getPermissions())) {
            role.getPermissions().addAll(getAllPermission(dto.getPermissions()));
        }

        return role;
    }

    public BasicResponseDTO createRole(CreateRoleInputDTO dto, HttpServletRequest request) throws BadRequestException {

        try{
            Optional<Roles> rolesOptional = rolesRepository.findByNameAndOwnerId(dto.getName(), tokenProvider.getId());

            if(rolesOptional.isPresent()) {
                return new BasicResponseDTO(Status.BAD_REQUEST, "Role already exist");
            }
            Roles roles = new Roles();
            roles.setName(dto.getName().toUpperCase());
            roles.setPermissions(getAllPermission(dto.getPermissions()));
            roles.setCreatedBy(tokenProvider.getFirstname() + " " + tokenProvider.getLastname());
            roles.setRoleType(RoleType.CUSTOM);
            roles.setOwnerId(tokenProvider.getParent());
            rolesRepository.save(roles);
//            auditService.successfulAudit(request,"Create Role",tokenProvider);
            return new BasicResponseDTO(Status.CREATED, roles);
        } catch (Exception ex) {
//            auditService.failedAudit(request,"Create Role",tokenProvider);
            throw new BadRequestException(ex.getMessage());
        }
    }

    public BasicResponseDTO updateRole(UpdateRoleInputDTO dto, Long id, HttpServletRequest request) throws BadRequestException {

        try{
            Optional<Roles> rolesOptional = rolesRepository.findById(id);

            if(!rolesOptional.isPresent()) {
                return new BasicResponseDTO(Status.BAD_REQUEST, "Role doesn't exist");
            }
            Roles roles = updateRoleObject(dto,rolesOptional.get());
            roles.setLastUpdatedTime(new Date());
            rolesRepository.save(roles);
//            auditService.successfulAudit(request,"Update role",tokenProvider);
            return new BasicResponseDTO(Status.CREATED, roles);
        }catch (Exception ex) {
//            auditService.failedAudit(request,"Update role",tokenProvider);
            throw new BadRequestException(ex.getMessage());
        }
    }

    public BasicResponseDTO removePermissionFromRole(PermissionDTO dto, Long id, HttpServletRequest request) throws BadRequestException {
        try{
            Optional<Roles> rolesOptional = rolesRepository.findById(id);

            if(!rolesOptional.isPresent()) {
                return new BasicResponseDTO(Status.BAD_REQUEST, "Role doesn't exist");
            }
            Permission permission = permissionRepository.findByCode(dto.getPermission());
            if(permission.equals(null)) {
                return new BasicResponseDTO(Status.BAD_REQUEST, "Permission doesn't exist");
            }
            Roles roles = rolesOptional.get();
            roles.getPermissions().remove(permission);
            roles.setLastUpdatedTime(new Date());
            rolesRepository.save(roles);
//            auditService.successfulAudit(request,"Update role",tokenProvider);
            return new BasicResponseDTO(Status.CREATED, roles);
        }catch (Exception ex) {
//            auditService.failedAudit(request,"Update role",tokenProvider);
            throw new BadRequestException(ex.getMessage());
        }
    }

    public BasicResponseDTO findRoleByName(String name){

        Optional<Roles> rolesOptional = rolesRepository.findByNameAndOwnerId(name, tokenProvider.getParent());

        if(!rolesOptional.isPresent()) {
            return new BasicResponseDTO(Status.BAD_REQUEST, "Role doesn't exist");
        }

        return new BasicResponseDTO(Status.SUCCESS, rolesOptional.get());
    }

    public BasicResponseDTO findRoleById(Long id){

        Optional<Roles> rolesOptional = rolesRepository.findById(id);

        if(!rolesOptional.isPresent()) {
            return new BasicResponseDTO(Status.BAD_REQUEST, "Role doesn't exist");
        }

        return new BasicResponseDTO(Status.SUCCESS, rolesOptional.get());
    }

    public BasicResponseDTO getAllRoles(int pageNo, int pageSize) {
        System.out.println("____AUTH__Got here");
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        System.out.println("____AUTH__ID"+tokenProvider.getParent());
        List<Roles> roles = rolesRepository.findByOwnerId(tokenProvider.getParent(), pageable).toList();

        return new BasicResponseDTO(Status.SUCCESS,roles);

    }

    public BasicResponseDTO searchRoles(String pattern, int pageNo, int pageSize) {
        try{

            Pageable pageable = PageRequest.of(pageNo,pageSize);

            List<Roles> roles = rolesRepository.findByNameAndOwnerId(pattern, tokenProvider.getParent(), pageable).toList();

            return new BasicResponseDTO(Status.SUCCESS, roles);
        }catch (Exception ex) {
            return new BasicResponseDTO(Status.BAD_REQUEST, ex.getMessage());
        }
    }
    public BasicResponseDTO fetchRoles(){
        List<Roles> allRoles = rolesRepository.findAll();
        log.info("{}",allRoles);
        return new BasicResponseDTO(Status.SUCCESS, allRoles);
    }

    public BasicResponseDTO deleteRole(Long id, HttpServletRequest request) throws BadRequestException {
        Optional<Roles> rolesOptional = rolesRepository.findById(id);

        try{
            if(!rolesOptional.isPresent()) {
                return new BasicResponseDTO(Status.BAD_REQUEST, "Role doesn't exist");
            }
            Roles roles = rolesOptional.get();
            rolesRepository.delete(roles);
//            auditService.successfulAudit(request,"Delete Role (" + roles.getName() +  ")", tokenProvider);
            return new BasicResponseDTO(Status.NO_CONTENT);
        }catch (Exception ex) {
//            auditService.failedAudit(request,"Delete Role (" + rolesOptional.get().getName() +  ")", tokenProvider);
            throw new BadRequestException(ex.getMessage());
        }
    }
    @Transactional
    public void createAdminRole(Long ownerId){
        Roles superAdmin = new Roles();
        if (checkIFRoleAlreadyExist(RoleEnum.ADMIN)) return;
        superAdmin.setName(RoleEnum.ADMIN.name());
        superAdmin.setPermissions(getAdminPermission());
        superAdmin.setOwnerId(ownerId);
        rolesRepository.save(superAdmin);
    }

    private boolean checkIFRoleAlreadyExist(RoleEnum engineers) {
        Optional<Roles> roles = rolesRepository.findByNameAndOwnerId(engineers.name(), tokenProvider.getParent());
        if(roles.isPresent()){
            return true;
        }
        return false;
    }

    private List<Permission> getAdminPermission() {
        List<Permission> superAdminPermission = new ArrayList<>();
        superAdminPermission.addAll(permissionRepository.findAll());
        return superAdminPermission;
    }




}
