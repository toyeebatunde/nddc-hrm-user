package co.payrail.attendance_srv.auth.util;


import co.payrail.attendance_srv.auth.dto.enums.Category;
import co.payrail.attendance_srv.auth.dto.enums.PermissionEnum;
import co.payrail.attendance_srv.auth.entity.Permission;
import co.payrail.attendance_srv.auth.repository.PermissionRepository;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class DefaultInitializer implements
        ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;


    private PasswordEncoder passwordEncoder;

    private final PermissionRepository permissionRepository;

    @Autowired
    public DefaultInitializer(PasswordEncoder passwordEncoder, PermissionRepository permissionRepository) {
        this.passwordEncoder = passwordEncoder;
        this.permissionRepository = permissionRepository;
    }

    @Override
    @Transactional
    @SneakyThrows
    public void onApplicationEvent(ContextRefreshedEvent event) {



        List<Permission> permissions = new ArrayList<>();

        permissions.addAll(createUserPermission());
        permissions.addAll(createRecruitmentPermission());
        permissions.addAll(createPayrollPermission());
        permissions.addAll(createLearningResourcePermission());
        permissions.addAll(createTaskPermission());
        permissions.addAll(createSecurityPermission());

        permissionRepository.saveAll(permissions);
    }

    @Transactional
    List<Permission> createUserPermission() {

        List<Permission> userPermission = new ArrayList<>();

        if(!Objects.isNull(permissionRepository.findAllByCategory(Category.USER.name()))){
            return userPermission;
        }

        userPermission.add(createPermissionObject(Category.USER, PermissionEnum.ADD_USER_PERMISSION));
        userPermission.add(createPermissionObject(Category.USER, PermissionEnum.VIEW_USER_PERMISSION));
        userPermission.add(createPermissionObject(Category.USER, PermissionEnum.DELETE_USER_PERMISSION));
        userPermission.add(createPermissionObject(Category.USER, PermissionEnum.EDIT_USER_PERMISSION));
        userPermission.add(createPermissionObject(Category.USER, PermissionEnum.UPDATE_USER_PERMISSION));
        return userPermission;

    }

    @Transactional
    List<Permission> createRecruitmentPermission() {

        List<Permission> userPermission = new ArrayList<>();

        if(!permissionRepository.findAllByCategory(Category.RECRUITMENT.name()).isEmpty()){
            return userPermission;
        }

        userPermission.add(createPermissionObject(Category.RECRUITMENT, PermissionEnum.ADD_RECRUITMENT_PERMISSION));
        userPermission.add(createPermissionObject(Category.RECRUITMENT, PermissionEnum.EDIT_RECRUITMENT_PERMISSION));
        userPermission.add(createPermissionObject(Category.RECRUITMENT, PermissionEnum.DELETE_RECRUITMENT_PERMISSION));
        userPermission.add(createPermissionObject(Category.RECRUITMENT, PermissionEnum.VIEW_RECRUITMENT_PERMISSION));
        return userPermission;

    }

    @Transactional
    List<Permission> createPayrollPermission() {

        List<Permission> userPermission = new ArrayList<>();

        if(!permissionRepository.findAllByCategory(Category.PAYROLL.name()).isEmpty()){
            return userPermission;
        }

        userPermission.add(createPermissionObject(Category.PAYROLL, PermissionEnum.ADD_PAYROLL_PERMISSION));
        userPermission.add(createPermissionObject(Category.PAYROLL, PermissionEnum.EDIT_PAYROLL_PERMISSION));
        userPermission.add(createPermissionObject(Category.PAYROLL, PermissionEnum.VIEW_PAYROLL_PERMISSION));
        userPermission.add(createPermissionObject(Category.PAYROLL, PermissionEnum.UPDATE_PAYROLL_PERMISSION));
        userPermission.add(createPermissionObject(Category.PAYROLL, PermissionEnum.DELETE_PAYROLL_PERMISSION));
        return userPermission;

    }

    @Transactional
    List<Permission> createLearningResourcePermission() {

        List<Permission> userPermission = new ArrayList<>();

        if(!permissionRepository.findAllByCategory(Category.LEARNING_RESOURCE.name()).isEmpty()){
            return userPermission;
        }

        userPermission.add(createPermissionObject(Category.LEARNING_RESOURCE, PermissionEnum.ADD_LEARNING_RESOURCE_PERMISSION));
        userPermission.add(createPermissionObject(Category.LEARNING_RESOURCE, PermissionEnum.DELETE_LEARNING_RESOURCE_PERMISSION));
        userPermission.add(createPermissionObject(Category.LEARNING_RESOURCE, PermissionEnum.VIEW_LEARNING_RESOURCE_PERMISSION));

        return userPermission;

    }

    @Transactional
    List<Permission> createTaskPermission() {

        List<Permission> userPermission = new ArrayList<>();

        if(!permissionRepository.findAllByCategory(Category.TASK.name()).isEmpty()){
            return userPermission;
        }

        userPermission.add(createPermissionObject(Category.TASK, PermissionEnum.ADD_TASK_PERMISSION));
        userPermission.add(createPermissionObject(Category.TASK, PermissionEnum.EDIT_TASK_PERMISSION));
        userPermission.add(createPermissionObject(Category.TASK, PermissionEnum.DELETE_TASK_PERMISSION));
        userPermission.add(createPermissionObject(Category.TASK, PermissionEnum.UPDATE_TASK_PERMISSION));
        userPermission.add(createPermissionObject(Category.TASK, PermissionEnum.VIEW_TASK_PERMISSION));

        return userPermission;

    }

    @Transactional
    List<Permission> createSecurityPermission() {

        List<Permission> userPermission = new ArrayList<>();

        if(!permissionRepository.findAllByCategory(Category.SECURITY.name()).isEmpty()){
            return userPermission;
        }

        userPermission.add(createPermissionObject(Category.SECURITY, PermissionEnum.ADD_SECURITY_DATA_PERMISSION));
        userPermission.add(createPermissionObject(Category.SECURITY, PermissionEnum.EDIT_SECURITY_DATA_PERMISSION));
        userPermission.add(createPermissionObject(Category.SECURITY, PermissionEnum.DELETE_SECURITY_DATA_PERMISSION));
        userPermission.add(createPermissionObject(Category.SECURITY, PermissionEnum.UPDATE_SECURITY_DATA_PERMISSION));
        userPermission.add(createPermissionObject(Category.SECURITY, PermissionEnum.VIEW_SECURITY_DATA_PERMISSION));

        return userPermission;

    }

    private static Permission createPermissionObject(Category category, PermissionEnum permissionEnum) {
        Permission permission = new Permission();
        permission.setCategory(category.name());
        permission.setCode(permissionEnum.name());
        return permission;
    }


    }

