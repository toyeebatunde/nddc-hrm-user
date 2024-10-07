package co.payrail.attendance_srv.auth.service;


import co.payrail.attendance_srv.auth.dto.input.*;
import co.payrail.attendance_srv.auth.dto.output.LoginResponseDTO;
import co.payrail.attendance_srv.auth.entity.User;
import co.payrail.attendance_srv.dto.output.BasicResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

public interface UserService extends UserDetailsService{
    Optional<User> findByUserName(String userName);

    LoginResponseDTO login(LoginInputDTO loginInputDTO, AuthenticationManager authenticationManager) throws Exception;
    BasicResponseDTO signup(CreateAccountDto dto);
    BasicResponseDTO logout();

    BasicResponseDTO beginResetPassword(BeginResetPasswordDTO dto);
    BasicResponseDTO isValidOtp(String userName, String otp);
    public BasicResponseDTO resendOtp(String phoneNumber) throws IOException;
    BasicResponseDTO verifyResetCode(ResetCodeInputDTO dto, String code);

    BasicResponseDTO resetPassword(ChangePasswordDTO dto, String code);

    BasicResponseDTO addUser(AddUserInputDTO dto);
    BasicResponseDTO verifyCac(String rcNumber, String companyName, String userName) throws Exception;


    BasicResponseDTO findUser(String firstName, String lastName, String email);

    BasicResponseDTO findUserById(Long id);

    BasicResponseDTO deleteUser(Long id);

    BasicResponseDTO updateUser(AddUserInputDTO dto, Long id);

    BasicResponseDTO getUsers(Integer pageNo, Integer pageSize);
}
