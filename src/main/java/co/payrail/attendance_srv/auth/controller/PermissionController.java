package co.payrail.attendance_srv.auth.controller;



import co.payrail.attendance_srv.auth.service.PermissionService;
import co.payrail.attendance_srv.basicController.Controller;
import co.payrail.attendance_srv.dto.output.BasicResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/permission")
@Slf4j
@RequiredArgsConstructor
public class PermissionController extends Controller {

    private final PermissionService permissionService;

    @GetMapping("/all")
    public BasicResponseDTO fetchPermissions() throws Exception {
        return updateHttpStatus(permissionService.fetchPermissions());
    }
}
