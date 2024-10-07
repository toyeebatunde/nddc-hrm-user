package co.payrail.attendance_srv.auth.controller;

import co.payrail.attendance_srv.auth.dto.input.*;
import co.payrail.attendance_srv.auth.dto.output.LoginResponseDTO;
import co.payrail.attendance_srv.auth.service.UserService;
import co.payrail.attendance_srv.basicController.Controller;
import co.payrail.attendance_srv.dto.output.BasicResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/user")
public class UserController extends Controller {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    public UserController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }


    @GetMapping("/users")
    @PreAuthorize("hasAuthority('VIEW_USER_PERMISSION')")
    public BasicResponseDTO getUsers(@RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize) throws IOException {
        return userService.getUsers(pageNo,pageSize);
    }

    @PostMapping("/add_user")
    @PreAuthorize("hasAuthority('ADD_USER_PERMISSION')")
    public BasicResponseDTO addNewUser(@RequestBody @Valid AddUserInputDTO dto) {
        return updateHttpStatus(userService.addUser(dto));
    }

    @PatchMapping("/begin_reset_password")
    public BasicResponseDTO beginResetPassword(@RequestBody @Valid BeginResetPasswordDTO dto) throws Exception {
        return updateHttpStatus(userService.beginResetPassword(dto));
    }

    @PatchMapping("/verify_otp")
    public BasicResponseDTO verifyOtp(@RequestParam String code, @RequestBody @Valid ResetCodeInputDTO dto) throws Exception {
        return updateHttpStatus(userService.verifyResetCode(dto,code));
    }

    @PatchMapping("/reset_password")
    public BasicResponseDTO resetPassword(@RequestParam String code,@RequestBody @Valid ChangePasswordDTO dto) throws Exception {
        return updateHttpStatus(userService.resetPassword(dto,code));
    }


    @PatchMapping("/logout")
    public BasicResponseDTO logout(HttpServletRequest request) throws BadRequestException {
        return updateHttpStatus(userService.logout());
    }

}
