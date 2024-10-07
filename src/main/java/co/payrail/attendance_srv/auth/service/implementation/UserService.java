package co.payrail.attendance_srv.auth.service.implementation;

import co.payrail.attendance_srv.auth.dto.input.*;
import co.payrail.attendance_srv.auth.dto.output.CreateUserAccountResponseModel;
import co.payrail.attendance_srv.auth.dto.output.LoginResponseDTO;
import co.payrail.attendance_srv.auth.entity.Email;
import co.payrail.attendance_srv.auth.entity.Roles;
import co.payrail.attendance_srv.auth.entity.User;
import co.payrail.attendance_srv.auth.repository.RolesRepository;
import co.payrail.attendance_srv.auth.repository.UserRepository;
import co.payrail.attendance_srv.auth.service.RoleService;
import co.payrail.attendance_srv.auth.util.GenericUtil;
import co.payrail.attendance_srv.auth.util.OTPService;
import co.payrail.attendance_srv.cache.PasswordRequestCacheHandler;
import co.payrail.attendance_srv.config.ThreadConfig;
import co.payrail.attendance_srv.config.TokenProvider;
import co.payrail.attendance_srv.dto.enums.Status;
import co.payrail.attendance_srv.dto.output.BasicResponseDTO;
import co.payrail.attendance_srv.employer.dto.EmployerMapper;
import co.payrail.attendance_srv.employer.dto.out.EmployerDTO;
import co.payrail.attendance_srv.employer.entity.Employer;
import co.payrail.attendance_srv.employer.service.EmployerService;
import co.payrail.attendance_srv.exception.BranchlessBankingException;
import co.payrail.attendance_srv.exception.BranchlessInvalidRequestException;
import co.payrail.attendance_srv.integration.dojah.DojahApi;
import co.payrail.attendance_srv.integration.dojah.model.validatecac.ValidateCACRequest;
import co.payrail.attendance_srv.integration.dojah.model.validatecac.ValidateCacResponse;
import co.payrail.attendance_srv.integration.service.IntegrationService;
import co.payrail.attendance_srv.integration.service.MailService;
import co.payrail.attendance_srv.integration.termii.service.Termii;
import co.payrail.attendance_srv.kyc_srv.dto.KycDto;
import co.payrail.attendance_srv.kyc_srv.service.KycService;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService implements co.payrail.attendance_srv.auth.service.UserService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final RolesRepository rolesRepository;
    private final PasswordRequestCacheHandler cacheHandler;
    private final PasswordEncoder passwordEncoder;

    private final OTPService otpService;

    private ModelMapper mapper;

    private final MessageSource messageSource;

    private final Locale locale = LocaleContextHolder.getLocale();

    private final ThreadPoolTaskExecutor executorService = ThreadConfig.getExecutor();
    private final Termii termiiService;

    private final MailService mailService;

    private final DojahApi dojahApiService;

    private final IntegrationService integrationService;

    private final RoleService roleService;

    private final KycService kycService;

    private final EmployerService employerService;



    @Value("${host.url}")
    private String hostUrl;


    public UserService(UserRepository userRepository, TokenProvider tokenProvider, RolesRepository rolesRepository, PasswordRequestCacheHandler cacheHandler, PasswordEncoder passwordEncoder, OTPService otpService, ModelMapper mapper, MessageSource messageSource, Termii termiiService, MailService mailService, DojahApi dojahApiService, IntegrationService integrationService, RoleService roleService, KycService kycService, EmployerService employerService) {
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
        this.rolesRepository = rolesRepository;
        this.cacheHandler = cacheHandler;
        this.passwordEncoder = passwordEncoder;

        this.otpService = otpService;
        this.mapper = mapper;
        this.messageSource = messageSource;
        this.termiiService = termiiService;
        this.mailService = mailService;
        this.dojahApiService = dojahApiService;
        this.integrationService = integrationService;
        this.roleService = roleService;
        this.kycService = kycService;
        this.employerService = employerService;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), getAuthority(user));
    }

    private Collection<SimpleGrantedAuthority> getAuthority(User user) {
        // If the user has no role, return a default authority, e.g., "Basic"
        if (Objects.isNull(user.getRole())) {
            return List.of(new SimpleGrantedAuthority("ROLE_BASIC"));
        }

        // Map the user's permissions to SimpleGrantedAuthority objects
        return user.getRole().getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getCode()))
                .collect(Collectors.toSet());
    }

    @Override
    public Optional<User> findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public LoginResponseDTO login(LoginInputDTO loginDto, AuthenticationManager authenticationManager) throws Exception {
        String phoneNumber = loginDto.getPhoneNumber();
        String password = loginDto.getPassword();

        if (!isValidPhoneNumber(phoneNumber)){
            throw new BranchlessInvalidRequestException("Invalid phone number");
        }
        User user = userRepository.findByUserName(loginDto.getPhoneNumber())
                .orElseThrow(()-> {
                    throw new BranchlessInvalidRequestException("User not found");
                }
        );


        if (user.getStatus() != null && user.getStatus().equalsIgnoreCase("I")){
            throw new BranchlessInvalidRequestException("Account Deactivated! Please get in touch with customer support");
        }
        if (!passwordEncoder.matches(password, user.getPassword())){
            throw new BranchlessInvalidRequestException("Invalid credentials");
        }


        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getPhoneNumber(), loginDto.getPassword(), getAuthority(user))
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.generateJWTToken(user);
        KycDto kyc = kycService.getUserKyc(user);
        user.setLastLoginDate(new Date());
        EmployerDTO employer = employerService.getLoginByOwnerId(user.getParent());
       Map<String, Object> objectMap = new HashMap();
        objectMap.put("user", user);
        objectMap.put("employer", employer);
        userRepository.save(user);
        return new LoginResponseDTO(Status.SUCCESS, token, objectMap, kyc);
    }

    @Override
    public BasicResponseDTO signup(CreateAccountDto dto) {
//        if(!isValidPassword(dto.getPassword())) {
//            throw new BranchlessInvalidRequestException("Password contains invalid characters: ['\\', '^', '$', '', '(', ')', '[', ']']");
//        }

        if (!dto.getPassword().equals(dto.getConfirmPassword())){
            return new BasicResponseDTO(Status.BAD_REQUEST, "Password and Confirm Password do not match");

        }

        if (!isValidPhoneNumber(dto.getPhoneNumber())){
            return new BasicResponseDTO(Status.BAD_REQUEST, "Invalid phone number");

        }

        if (dto.getEmail() != null && !isValidEmail(dto.getEmail())){
            return new BasicResponseDTO(Status.BAD_REQUEST, "Invalid email address");

        }

        if (userRepository.findByUserName(dto.getPhoneNumber()).isPresent()) {

            return new  BasicResponseDTO(
                    Status.BAD_REQUEST,
                    new CreateUserAccountResponseModel()
                            .setError("phone number already exists"));
        }

        User user = new User();
        user.setNeedSetup(true);
        user.setUserName(dto.getPhoneNumber());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        user.setUserType(dto.getUserType());
        user.setClassification(dto.getClassification());
        User savedUser = userRepository.save(user);

        if (savedUser.getId() == null){
            log.error("User not saved");
            throw new BranchlessBankingException("Something unexpected happened");
        }
        user.setParent(savedUser.getId());
        UserDto userDto = mapper.map(savedUser, UserDto.class);
        userRepository.save(savedUser);
        String otp = generateOtp(dto.getPhoneNumber());

        sendOtpViaPhone(dto.getPhoneNumber(), otp);
        roleService.createAdminRole(savedUser.getId());

        if (user.getEmail() != null){
            sendOtpViaEmail(user.getEmail(), otp);
        }


        return new  BasicResponseDTO(
                Status.SUCCESS,
                new CreateUserAccountResponseModel()
                        .setOtp(otp)
                        .setUserDto(userDto)
                        .setOtpExpirationTimeInMinutes("4"));
    }

    @Override
    public BasicResponseDTO isValidOtp(String userName, String otp) {

      boolean  response = otpService.validateOtp(userName, otp);

      Map<String, String> resObject  = new HashMap();
      Optional<User> userOptional = userRepository.findByUserName(userName);

      if (userOptional.isEmpty()){

          return new BasicResponseDTO(Status.BAD_REQUEST, "Invalid phone number");
      }
      User user = userOptional.get();



      if (response){
          user.setStatus("A");

          userRepository.save(user);
          resObject.put("isValid", "true");
          resObject.put("message", "OTP validated");
          return new BasicResponseDTO(Status.SUCCESS, resObject);
      }else {
          resObject.put("isValid", "false");
          resObject.put("message", "OTP validation failed");
          return new BasicResponseDTO(Status.BAD_REQUEST, resObject);
      }

    }

    @Override
    public BasicResponseDTO resendOtp(String phoneNumber) throws IOException {
        if (!isValidPhoneNumber(phoneNumber)){
            return new BasicResponseDTO(Status.BAD_REQUEST, "Invalid phone number");
        }
        otpService.clearOTP(phoneNumber);
        String otp = String.valueOf(otpService.generateOTP(phoneNumber));
        sendOtpViaPhone(phoneNumber, otp);

        Map<String, String> map = new HashMap<>();
        map.put("message", otp);
        map.put("message", "OTP sent to phone successfully");
        return new  BasicResponseDTO(Status.SUCCESS, map);
    }

    @Override
    public BasicResponseDTO logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            authentication.setAuthenticated(false);
        }
        return new BasicResponseDTO(Status.SUCCESS);
    }

    @Override
    public BasicResponseDTO beginResetPassword(BeginResetPasswordDTO dto) {
        Optional<User> userOptional = userRepository.findByUserName(dto.getPhoneNumber());
        if (!userOptional.isPresent()) {
            return new BasicResponseDTO(Status.NOT_FOUND, "User doesn't exist");
        }
//        User user = userOptional.get();
//        int otp = GenericUtil.generateOtpCode();
//
//        userRepository.save(user);
        String code = GenericUtil.generateAlphaNumeric(12);
        cacheHandler.addToCache(code, dto.getPhoneNumber());
        sendOtpViaPhone(dto.getPhoneNumber(), code);

        return new BasicResponseDTO(Status.SUCCESS);
    }

    @Override
    public BasicResponseDTO verifyResetCode(ResetCodeInputDTO dto, String code) {
        String username = cacheHandler.getFromCache(code);
        Optional<User> userOptional = userRepository.findByUserName(username);
        if (!userOptional.isPresent()) {
            return new BasicResponseDTO(Status.NOT_FOUND, "User doesn't exist");
        }
        User user = userOptional.get();
        if (dto.getOtpCode() != user.getOtp()) {
            return new BasicResponseDTO(Status.BAD_REQUEST, "Invalid OTP Code");
        }
        cacheHandler.removeCache(code);
        return new BasicResponseDTO(Status.SUCCESS, code);
    }

    @Override
    public BasicResponseDTO verifyCac(String rcNumber, String companyName, String userName) throws Exception {
        log.info("UserName --> {}", userName);
        ValidateCACRequest validateCACRequest = new ValidateCACRequest();
        validateCACRequest.setCompanyName(companyName);
        validateCACRequest.setRcNumber(rcNumber);
        ValidateCacResponse response = dojahApiService.validateCAC(validateCACRequest);
        Map<String, String> resObject  = new HashMap();
        if (response.getError() == null && response.getRcNumber() != null && response.getCompanyName() != null){
//            saveCacInfoInKYC(rcNumber, companyName, userName);

            resObject.put("RcNumber", response.getRcNumber());
            resObject.put("RcNumber", response.getCompanyName());
            resObject.put("success", "true");
            return new BasicResponseDTO(Status.SUCCESS, resObject);
        } else {
            resObject.put("error", response.getError());
            resObject.put("success", "false");
            return new BasicResponseDTO(Status.FAILED_VALIDATION, resObject);

        }
    }

    @Override
    public BasicResponseDTO resetPassword(ChangePasswordDTO dto, String code) {
        return null;
    }

    @Override
    public BasicResponseDTO addUser(AddUserInputDTO dto) {
        try{
            Optional<User> userOptional = userRepository.findByUserName(dto.getPhoneNumber());
            if(userOptional.isPresent()){
                return new BasicResponseDTO(Status.BAD_REQUEST,"A user with this email already exist");
            }


            String username;
            username = dto.getPhoneNumber();

            User user = new User();
            user.setFirstName(dto.getFirstname());
            user.setLastName(dto.getLastname());
            user.setUserName(username);
            user.setEmail(dto.getEmail());
            user.setPhoneNumber(dto.getPhoneNumber());
            user.setUserType(dto.getUserType());
            user.setClassification(dto.getClassification());
            user.setParent(tokenProvider.getId());
            user.setPendingPasswordReset(Boolean.TRUE);
            user.setRole(getRole(dto.getRole()));
            user.setLastLoginDate(new Date());

            user.setStatus("A");
            String password = GenericUtil.generateAlphaNumeric(8);
            user.setPassword(passwordEncoder.encode(password));
            String code = GenericUtil.generateAlphaNumeric(12);

            if (!dto.getPhoneNumber().isEmpty()){

                String adminUrl = (hostUrl != null) ? hostUrl + "/change-password?code="+code : "";
                String message = "Hi " + user.getUserName() + " , Welcome to Payrail Admin Portal , use this credential to login, Username: " + user.getUserName() + " Click this link to change your password, Admin URL: "+adminUrl;
                integrationService.sendSms(user.getPhoneNumber(), message);
            }
            cacheHandler.addToCache(code,dto.getEmail());
            sendCreationMessage(user,password,code);
            userRepository.save(user);

//            auditService.successfulAudit(request,"Add User (" + dto.getFirstname() + " " + dto.getLastname() + ")", tokenProvider);
            return new BasicResponseDTO(Status.SUCCESS,user);
        }catch (Exception ex) {
//            auditService.failedAudit(request,"Add User (" + dto.getFirstname() + " " + dto.getLastname() + ")", tokenProvider);
            return new BasicResponseDTO(Status.BAD_REQUEST, ex.getMessage());
        }
    }
    private Roles getRole(String role) {
        if (!Objects.isNull(role)){
            Roles roles = rolesRepository.findByNameAndOwnerId(role, tokenProvider.getParent()).get();
            roles.setTeamMembers(roles.getTeamMembers() + 1);
            rolesRepository.save(roles);
            return roles;
        }
        return null;
    }
    public void sendCreationMessage(User user,String password, String code) {
        try {

            String adminUrl = (hostUrl != null) ? hostUrl + "/change-password?code="+code : "";
            log.info("Email service ");
            if ("A".equals(user.getStatus())) {


                log.info("Email service got to this condition");
                String fullName = user.getFirstName() + " " + user.getLastName();

                Context context = new Context();
                context.setVariable("fullName", fullName);
                context.setVariable("username", user.getUserName());
                context.setVariable("password", password);
                context.setVariable("adminUrl", adminUrl);

                Email email = new Email.Builder()
                        .setRecipient(user.getEmail())
                        .setSubject("Welcome to Payrail Admin Portal")
                        .setTemplate("mail/admincreation")
                        .build();


                mailService.sendGridMail(email, context);
                log.info("Email service got sent");
            }
        } catch (Exception e) {
            log.error("Error occurred sending creation credentials", e);
        }
    }

    @Override
    public BasicResponseDTO findUser(String firstName, String lastName, String email) {
        return null;
    }

    @Override
    public BasicResponseDTO findUserById(Long id) {
        return null;
    }

    @Override
    public BasicResponseDTO deleteUser(Long id) {
        return null;
    }

    @Override
    public BasicResponseDTO updateUser(AddUserInputDTO dto, Long id) {
        return null;
    }

    @Override
    public BasicResponseDTO getUsers(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo,pageSize);

        List<User> users = userRepository.findByParent(tokenProvider.getParent(), pageable).toList();

        return new BasicResponseDTO(Status.SUCCESS,users);
    }

    public boolean isValidPassword(String password) {
        HashSet<Character> invalidCharacters = new HashSet<>(Arrays.asList('\\', '^', '$', '|', '(', ')', '[', ']'));
        HashSet<Character> passwordSet = new HashSet<>();
        for (char c : password.toCharArray()){
            passwordSet.add(c);
        }
        passwordSet.retainAll(invalidCharacters);

        if (passwordSet.size() != 0){
            log.info("Password contains invalid characters");
            return false;
        }
        return true;
    }

    public boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.startsWith("+234") && (phoneNumber.length() == 14);
    }

    private boolean isValidEmail(String email) {
        if (StringUtils.isBlank(email)){
            return false;
        }
        String regex = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void sendOtpViaPhone(String phoneNumber, String otp) {
        executorService.submit(() -> {
            try {
                termiiService.sendSmsTo(phoneNumber, otp);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Could not send sms");
            }
        });
    }

    private void sendOtpViaEmail(String emailAddress, String otp) {
        try {
            Context context = new Context();
            context.setVariable("code", otp);

            Email email = new Email.Builder()
                    .setRecipient(emailAddress)
                    .setSubject(messageSource.getMessage("user.creation.email.subject", null, locale))
                    .setTemplate("mail/otp")
                    .build();

            mailService.sendGridMail(email, context);
        } catch (Exception e) {
            e.printStackTrace();
//            throw new RuntimeException("could not send otp via email");
        }
    }

    private String generateOtp(String phoneNumber) {
        return String.valueOf(otpService.generateOTP(phoneNumber));
    }

}
