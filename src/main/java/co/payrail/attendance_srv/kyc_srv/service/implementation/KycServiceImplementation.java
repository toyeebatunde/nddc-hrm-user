package co.payrail.attendance_srv.kyc_srv.service.implementation;


import co.payrail.attendance_srv.auth.dto.input.UserDto;
import co.payrail.attendance_srv.auth.entity.Classification;
import co.payrail.attendance_srv.auth.entity.Email;
import co.payrail.attendance_srv.auth.entity.User;
import co.payrail.attendance_srv.auth.repository.UserRepository;
import co.payrail.attendance_srv.auth.util.PhoneNumberUtil;
import co.payrail.attendance_srv.exception.BranchlessBankingException;
import co.payrail.attendance_srv.exception.BranchlessInvalidRequestException;
import co.payrail.attendance_srv.exception.DuplicateObjectException;
import co.payrail.attendance_srv.integration.dojah.DojahApi;
import co.payrail.attendance_srv.integration.dojah.model.lookupbvn.LookupBvnResponse;
import co.payrail.attendance_srv.integration.dojah.model.validatecac.ValidateCACRequest;
import co.payrail.attendance_srv.integration.dojah.model.validatecac.ValidateCacResponse;
import co.payrail.attendance_srv.integration.dojah.model.validatetin.ValidateTinRequest;
import co.payrail.attendance_srv.integration.dojah.model.validatetin.ValidateTinResponse;
import co.payrail.attendance_srv.integration.dojah.model.virtual_nin.ValidateVirtualNINRequest;
import co.payrail.attendance_srv.integration.dojah.model.virtual_nin.ValidateVirtualNINResponse;
import co.payrail.attendance_srv.integration.dojah.service.DojahApiService;
import co.payrail.attendance_srv.integration.service.IntegrationService;
import co.payrail.attendance_srv.integration.service.MailService;
import co.payrail.attendance_srv.integration.service.dto.kyc.BranchlessBvnEnquiryRequest;
import co.payrail.attendance_srv.kyc_srv.dto.KycDto;
import co.payrail.attendance_srv.kyc_srv.dto.KycReqquest;
import co.payrail.attendance_srv.kyc_srv.dto.KycView;
import co.payrail.attendance_srv.kyc_srv.entity.BVN;
import co.payrail.attendance_srv.kyc_srv.entity.KYCStatus;
import co.payrail.attendance_srv.kyc_srv.entity.Kyc;
import co.payrail.attendance_srv.kyc_srv.repository.BVNRepository;
import co.payrail.attendance_srv.kyc_srv.repository.KycRepository;
import co.payrail.attendance_srv.kyc_srv.service.KycService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.thymeleaf.context.Context;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Rasaq Agbalaya
 * @project branchless
 */
@Slf4j
@Service
public class KycServiceImplementation implements KycService {

    private MailService mailService;
    private MessageSource messageSource;
    private UserRepository userRepository;
    private KycRepository kycRepository;
    private ModelMapper modelMapper;
    private BVNRepository bvnRepository;

    private DojahApi dojahApiService;

    private IntegrationService integrationService;


    private Locale locale = LocaleContextHolder.getLocale();


    @Autowired
    public KycServiceImplementation(MailService mailService, MessageSource messageSource, UserRepository userRepository, KycRepository kycRepository, ModelMapper modelMapper, BVNRepository bvnRepository, DojahApi dojahApiService, IntegrationService integrationService) {
        this.mailService = mailService;
        this.messageSource = messageSource;
        this.userRepository = userRepository;
        this.kycRepository = kycRepository;
        this.modelMapper = modelMapper;
        this.bvnRepository = bvnRepository;
        this.dojahApiService = dojahApiService;

        this.integrationService = integrationService;

    }

    public Map<String, String> verifyBvnAndUpdateUserDetails(String bvn, String userName) {
        if (bvn.length() != 11 || bvn.trim().equals("")){
            log.info("+++Invalid BVN+++ {}", "Invalid BVN");
            System.out.println("Invalid BVN");
            throw new BranchlessInvalidRequestException("Invalid BVN");
        }
        log.info("+++USERNAME+++ {}", userName);
        String validatedUsername = PhoneNumberUtil.validateNumber(userName);
        log.info("+++USERNAME 2+++ {}", validatedUsername);
        User agent = userRepository.findByUserName(validatedUsername).orElseThrow(
                ()->{
                    log.info("invalid username. Agent not found");
                    throw new BranchlessInvalidRequestException("Invalid username. Agent not found");
                }
        );

        KycReqquest kycReqquest = new KycReqquest();
        kycReqquest.setBvnOrNinOrRc(bvn);
        Map<String, String> response = this.uploadBvn(agent, kycReqquest);
        agent.setFirstName(response.get("firstName"));
        agent.setLastName(response.get("lastName"));
        userRepository.save(agent);
        return response;
    }


    @Override
    public Map<String, String> verifyVirtualNINAndUpdateUserDetails(String vnin, String userName) {
        if (vnin.trim().equals("")){
            throw new BranchlessInvalidRequestException("Invalid BVN");
        }
        User agent = userRepository.findByUserName(userName).orElseThrow(
                ()->{
                    log.info("invalid username. Agent not found");
                    throw new BranchlessInvalidRequestException("Invalid username. Agent not found");
                }
        );

        KycReqquest kycReqquest = new KycReqquest();
        kycReqquest.setBvnOrNinOrRc(vnin);
        String response = this.uploadNin(agent, kycReqquest);
        Map<String, String> mapObject = new HashMap();
        mapObject.put("message",response);
        return mapObject;
    }

    @Override
    public String verifyCac(String rcNumber, String companyName, String userName) throws Exception {

        ValidateCACRequest validateCACRequest = new ValidateCACRequest();
        validateCACRequest.setCompanyName(companyName);
        validateCACRequest.setRcNumber(rcNumber);
        ValidateCacResponse response = dojahApiService.validateCAC(validateCACRequest);
        log.info("CAC VALIDATION RESPONSE --> {}", response);
        if (response.getError() == null && response.getRcNumber() != null && response.getCompanyName() != null){
            saveCacInfoInKYC(rcNumber, companyName, userName);
            return "Licence found!";
        } else {
            return "Not found!";
        }
    }
    private void saveCacInfoInKYC(String rcNumber, String companyName, String userName) {
        User agent = userRepository.findByUserName(userName).orElseThrow(
                ()->{
                    log.info("invalid username. Agent not found");
                    throw new BranchlessInvalidRequestException("Invalid username. Agent not found");
                }
        );
        Kyc kyc = kycRepository.findByUser_Id(agent.getId());
        if (kyc == null){
            kyc = createKyc(agent);
        }
        kyc.setRcNumber(rcNumber);
        kyc.setCacCompanyName(companyName);
        kyc.setCacKycStatus(KYCStatus.VERIFIED);
        kycRepository.save(kyc);
    }
    @Override
    public Map<String, String> uploadBvn(User user, KycReqquest kycRequestModel) {

            if (kycRequestModel == null || StringUtils.isEmpty(kycRequestModel.getBvnOrNinOrRc())) {
                throw new BranchlessBankingException("Field cannot be blank");
            }

            //Auto Verify
            checkBvn(kycRequestModel.getBvnOrNinOrRc().trim());
            BranchlessBvnEnquiryRequest branchlessBvnEnquiryRequest = new BranchlessBvnEnquiryRequest();
            branchlessBvnEnquiryRequest.setBvn(kycRequestModel.getBvnOrNinOrRc().trim());
            branchlessBvnEnquiryRequest.setFirstName(user.getFirstName());
            branchlessBvnEnquiryRequest.setLastName(user.getLastName());
            LookupBvnResponse response = integrationService.bvn(branchlessBvnEnquiryRequest);
            if (!org.apache.commons.lang3.StringUtils.isEmpty(response.getError())) {
                throw new BranchlessBankingException(response.getError());
            }
            BVN bvn = modelMapper.map(response, BVN.class);
            log.info("BVN: " + bvn);
            if (Objects.isNull(bvnRepository.findFirstByBvn(bvn.getBvn()))){
                bvnRepository.save(bvn);
            }

            //validate
            if (Objects.nonNull(user.getFirstName()) && Objects.nonNull(user.getLastName())){
                if (!bvn.getFirstName().equalsIgnoreCase(branchlessBvnEnquiryRequest.getFirstName()) || !bvn.getLastName().equalsIgnoreCase(branchlessBvnEnquiryRequest.getLastName())){
                    throw new BranchlessBankingException(messageSource.getMessage("bvn.details.mismatch", null, locale));
                }
            }

            Kyc kyc = kycRepository.findByUser(user);
            if (Objects.isNull(kyc)){
                kyc = createKyc(user);
            }
            kyc.setBvn(kycRequestModel.getBvnOrNinOrRc().trim());
            kyc.setBvnUploadedDate(new Date());
            kyc.setBvnKycStatus(KYCStatus.VERIFIED);
            Kyc saved = kycRepository.save(kyc);

            //sendPendingVerificationKycEmail(saved, "BVN");
            isKycComplete(user);
            Map<String, String> map = new HashMap<>();
            map.put("message",  "BVN uploaded successfully");
            map.put("firstName", response.getFirstName());
            map.put("lastName", response.getLastName());
            map.put("gender", response.getGender());
            map.put("dateOfBirth", response.getDateOfBirth());
            map.put("middleName", response.getMiddleName());
        map.put("address", response.getResidentialAddress());
        map.put("lgaOfOrigin", response.getLgaOfOrigin());
        map.put("stateOfOrigin", response.getStateOfOrigin());
        map.put("lgaOfResidence", response.getLgaOfResidence());
        map.put("nationality", response.getNationality());
        map.put("stateOfResidence", response.getStateOfResidence());



            return map;
    }

    @Override
    public String uploadNin(User user, KycReqquest kycRequestModel) {
        try {

            if (kycRequestModel == null || StringUtils.isEmpty(kycRequestModel.getBvnOrNinOrRc())) {
                throw new BranchlessBankingException("Field cannot be blank");
            }

            ValidateVirtualNINRequest virtualNinValidation = new ValidateVirtualNINRequest();

            virtualNinValidation.setVirtualNin(kycRequestModel.getBvnOrNinOrRc());
            //Auto Verify
            ValidateVirtualNINResponse response = integrationService.validateVirtualNIN(virtualNinValidation);
            if (!org.apache.commons.lang3.StringUtils.isEmpty(response.getError())) {
                throw new BranchlessBankingException(response.getError());
            }

            Kyc kyc = kycRepository.findByUser(user);
            kyc.setNin(kycRequestModel.getBvnOrNinOrRc().trim());
            kyc.setNinUploadedDate(new Date());
            kyc.setNinKycStatus(KYCStatus.PENDING_VERIFICATION);
            Kyc saved = kycRepository.save(kyc);

            //sendPendingVerificationKycEmail(saved, "NIN");
            isKycComplete(user);

            return "NIN uploaded successfully";
        } catch (BranchlessBankingException pe) {
            throw new BranchlessBankingException(pe.getMessage());
        } catch (Exception e) {
            throw new BranchlessBankingException("NIN upload failed");
        }
    }

    @Override
    public String uploadRcNumber(User user, KycReqquest kycRequestModel) {
        try {

            if (kycRequestModel == null || StringUtils.isEmpty(kycRequestModel.getBvnOrNinOrRc())) {
                throw new BranchlessBankingException("Field cannot be blank");
            }

            if (user.getClassification().equals(Classification.INDIVIDUAL)) {
                throw new BranchlessBankingException("Invalid KYC Provided");
            }

            //Auto Verify
            Kyc kyc = kycRepository.findByUser(user);
            kyc.setRcNumber(kycRequestModel.getBvnOrNinOrRc().trim());
            kyc.setRcNumberUploadedDate(new Date());
            kyc.setRcNumberKycStatus(KYCStatus.PENDING_VERIFICATION);
            Kyc saved = kycRepository.save(kyc);

            //sendPendingVerificationKycEmail(saved, "NIN");
            isKycComplete(user);

            return "RC Number uploaded successfully";
        } catch (BranchlessBankingException pe) {
            throw new BranchlessBankingException(pe.getMessage());
        } catch (Exception e) {
            throw new BranchlessBankingException("RC Number upload failed");
        }
    }

    @Override
    public String uploadTin(User user, KycReqquest kycRequestModel) {
        try {

            if (kycRequestModel == null || StringUtils.isEmpty(kycRequestModel.getBvnOrNinOrRc())) {
                throw new BranchlessBankingException("Field cannot be blank");
            }

            if (user.getClassification().equals(Classification.INDIVIDUAL)) {
                throw new BranchlessBankingException("Invalid KYC Provided");
            }

            //Auto Verify
            Kyc kyc = kycRepository.findByUser(user);
            kyc.setTin(kycRequestModel.getBvnOrNinOrRc().trim());
            kyc.setTinUploadedDate(new Date());
            kyc.setTinKycStatus(KYCStatus.PENDING_VERIFICATION);
            Kyc saved = kycRepository.save(kyc);

            //sendPendingVerificationKycEmail(saved, "NIN");
            isKycComplete(user);

            return "Tin uploaded successfully";
        } catch (BranchlessBankingException pe) {
            throw new BranchlessBankingException(pe.getMessage());
        } catch (Exception e) {
            throw new BranchlessBankingException("Tin upload failed");
        }
    }

    @Override
    public String uploadContractNo(User user, KycReqquest kycRequestModel) {
        try {

            if (kycRequestModel == null || StringUtils.isEmpty(kycRequestModel.getBvnOrNinOrRc())) {
                throw new BranchlessBankingException("Field cannot be blank");
            }

            if (user.getClassification().equals(Classification.INDIVIDUAL)) {
                throw new BranchlessBankingException("Invalid KYC Provided");
            }

            //Auto Verify
            Kyc kyc = kycRepository.findByUser(user);
            kyc.setContractNo(kycRequestModel.getBvnOrNinOrRc().trim());
            kyc.setContractNoUploadedDate(new Date());
            kyc.setContractNoKycStatus(KYCStatus.PENDING_VERIFICATION);
            Kyc saved = kycRepository.save(kyc);

            //sendPendingVerificationKycEmail(saved, "NIN");
            isKycComplete(user);

            return "Contract No uploaded successfully";
        } catch (BranchlessBankingException pe) {
            throw new BranchlessBankingException(pe.getMessage());
        } catch (Exception e) {
            throw new BranchlessBankingException("Contract No upload failed");
        }
    }

    @Override
    public String uploadSelfie(User user, KycReqquest kycRequestModel) {
        try {

            if (kycRequestModel == null || StringUtils.isEmpty(kycRequestModel.getFileKey())) {
                throw new BranchlessBankingException("Field cannot be blank");
            }

            //Auto Verify

            Kyc kyc = kycRepository.findByUser(user);
            kyc.setSelfieBase64Img(kycRequestModel.getFileKey().trim());
            kyc.setSelfieUploadedDate(new Date());
            kyc.setSelfieKycStatus(KYCStatus.PENDING_VERIFICATION);
            Kyc saved = kycRepository.save(kyc);

            //sendPendingVerificationKycEmail(saved, "NIN");
            isKycComplete(user);

            return "Selfie uploaded successfully";
        } catch (BranchlessBankingException pe) {
            throw new BranchlessBankingException(pe.getMessage());
        } catch (Exception e) {
            throw new BranchlessBankingException("Selfie upload failed");
        }
    }

    public String validateSelfie(User user) {
        try {
            //Auto Verify
            Kyc kyc = kycRepository.findByUser(user);
            kyc.setSelfieKycStatus(KYCStatus.VERIFIED);
            Kyc saved = kycRepository.save(kyc);
            //sendPendingVerificationKycEmail(saved, "NIN");
            return "Selfie validated successfully";
        } catch (BranchlessBankingException pe) {
            throw new BranchlessBankingException(pe.getMessage());
        } catch (Exception e) {
            throw new BranchlessBankingException("Selfie upload failed");
        }
    }

    @Override
    public String uploadCac(User user, KycReqquest kycRequestModel) {
        try {

            if (kycRequestModel == null || StringUtils.isEmpty(kycRequestModel.getFileKey())) {
                throw new BranchlessBankingException("Field cannot be blank");
            }

            //Auto Verify

            Kyc kyc = kycRepository.findByUser(user);
            kyc.setCacBase64Img(kycRequestModel.getFileKey().trim());
            kyc.setCacUploadedDate(new Date());
            kyc.setCacKycStatus(KYCStatus.PENDING_VERIFICATION);
            Kyc saved = kycRepository.save(kyc);

            //sendPendingVerificationKycEmail(saved, "NIN");
            isKycComplete(user);

            return "CAC Cert. uploaded successfully";
        } catch (BranchlessBankingException pe) {
            throw new BranchlessBankingException(pe.getMessage());
        } catch (Exception e) {
            throw new BranchlessBankingException("CAC Cert. upload failed");
        }
    }

    @Override
    public String uploadIdCard(User user, KycReqquest kycRequestModel) {
        try {

            if (kycRequestModel == null || StringUtils.isEmpty(kycRequestModel.getFileKey())) {
                throw new BranchlessBankingException("Field cannot be blank");
            }

            //Auto Verify

            Kyc kyc = kycRepository.findByUser(user);
            kyc.setIdCardBase64Img(kycRequestModel.getFileKey().trim());
            kyc.setIdCardUploadedDate(new Date());
            kyc.setIdCardKycStatus(KYCStatus.PENDING_VERIFICATION);
            Kyc saved = kycRepository.save(kyc);

            //sendPendingVerificationKycEmail(saved, "NIN");
            isKycComplete(user);

            return "ID uploaded successfully";
        } catch (BranchlessBankingException pe) {
            throw new BranchlessBankingException(pe.getMessage());
        } catch (Exception e) {
            throw new BranchlessBankingException("ID upload failed");
        }
    }

    @Override
    public KycDto getUserKyc(User user) {
        Kyc kyc = kycRepository.findByUser(user);
        if (kyc == null) {
            KycDto kycResponseModel = new KycDto();
            kycResponseModel.setBvnKycStatus(KYCStatus.PENDING_UPLOAD);
            kycResponseModel.setNinKycStatus(KYCStatus.PENDING_UPLOAD);
            kycResponseModel.setCacKycStatus(KYCStatus.PENDING_UPLOAD);
            kycResponseModel.setRcNumberKycStatus(KYCStatus.PENDING_UPLOAD);
            kycResponseModel.setIdCardKycStatus(KYCStatus.PENDING_UPLOAD);
            kycResponseModel.setSelfieKycStatus(KYCStatus.PENDING_UPLOAD);
            kycResponseModel.setContractNoKycStatus(KYCStatus.PENDING_UPLOAD);
            kycResponseModel.setTinKycStatus(KYCStatus.PENDING_UPLOAD);
//            kycResponseModel.setUser(user);
            createKyc(user);
            return kycResponseModel;
        }

        KycDto kycResponseModel = convertEntityToDto(kyc);

        isKycComplete(user);

        return kycResponseModel;
    }

    @Override
    public Kyc getKycByUser(User user) {
        return kycRepository.findByUser_Id(user.getId());
    }

    @Override
    public KycDto getKyc(Long id) {
        Kyc kyc = kycRepository.getById(id);
        if (null == kyc.getId()) {
            KycDto kycResponseModel = new KycDto();
            kycResponseModel.setBvnKycStatus(KYCStatus.PENDING_UPLOAD);
            kycResponseModel.setNinKycStatus(KYCStatus.PENDING_UPLOAD);
            kycResponseModel.setCacKycStatus(KYCStatus.PENDING_UPLOAD);
            kycResponseModel.setRcNumberKycStatus(KYCStatus.PENDING_UPLOAD);
            kycResponseModel.setIdCardKycStatus(KYCStatus.PENDING_UPLOAD);
            kycResponseModel.setSelfieKycStatus(KYCStatus.PENDING_UPLOAD);
            kycResponseModel.setContractNoKycStatus(KYCStatus.PENDING_UPLOAD);
            kycResponseModel.setTinKycStatus(KYCStatus.PENDING_UPLOAD);
//            kycResponseModel.setUser(kyc.getUser());
            return kycResponseModel;
        }

        KycDto kycResponseModel = convertEntityToDto(kyc);

        isKycComplete(kyc.getUser());

        return kycResponseModel;
    }

    @Override
    public void checkBvn(String bvn) {
        Optional<Kyc> optionalKyc = kycRepository.findKycByBvn(bvn);

        if (!optionalKyc.isPresent()){
            return;
        }
        throw new DuplicateObjectException("BVN already exists");
    }

    @Override
    public void validateNin(String nin) {

        Kyc kyc = kycRepository.findFirstByNinIgnoreCase(nin);
        if (kyc != null) {
            throw new DuplicateObjectException(messageSource.getMessage("nin.exists", null, locale));
        }
//        return messageSource.getMessage("nin.not.exist", null, locale);
    }

    @Override
    public String checkNin(String nin) {

        Kyc kyc = kycRepository.findFirstByNinIgnoreCase(nin);
        if (kyc != null) {
            throw new DuplicateObjectException(messageSource.getMessage("nin.exists", null, locale));
        }
        return messageSource.getMessage("nin.not.exist", null, locale);
    }

    @Override
    public void checkRCNumber(String rcNumber) {

        Kyc kyc = kycRepository.findFirstByRcNumberIgnoreCase(rcNumber);
        if (kyc != null) {
            throw new DuplicateObjectException(messageSource.getMessage("rc.exists", null, locale));
        }
    }

    @Override
    public void checkTin(String tin) {

        Kyc kyc = kycRepository.findFirstByTinIgnoreCase(tin);
        if (kyc != null) {
            throw new DuplicateObjectException(messageSource.getMessage("tin.exists", null, locale));
        }
    }

    @Override
    public void checkContractNo(String cNo) {

        Kyc kyc = kycRepository.findFirstByContractNoIgnoreCase(cNo);
        if (kyc != null) {
            throw new DuplicateObjectException(messageSource.getMessage("cno.exists", null, locale));
        }
    }

    @Override
    public String createUserKyc(UserDto userDto) {
        return null;
    }

    private Kyc createKyc(User user) {
        Kyc kyc = new Kyc();
        kyc.setBvnKycStatus(KYCStatus.PENDING_UPLOAD);
        kyc.setNinKycStatus(KYCStatus.PENDING_UPLOAD);
        kyc.setCacKycStatus(KYCStatus.PENDING_UPLOAD);
        kyc.setRcNumberKycStatus(KYCStatus.PENDING_UPLOAD);
        kyc.setIdCardKycStatus(KYCStatus.PENDING_UPLOAD);
        kyc.setSelfieKycStatus(KYCStatus.PENDING_UPLOAD);
        kyc.setContractNoKycStatus(KYCStatus.PENDING_UPLOAD);
        kyc.setTinKycStatus(KYCStatus.PENDING_UPLOAD);
        kyc.setUser(user);
        return kycRepository.save(kyc);
    }

    @Override
    public String createUserKyc(co.payrail.attendance_srv.kyc_srv.dto.UserDto userDto) {
        User user = userRepository.getById(userDto.getId());
        Kyc kyc = kycRepository.findByUser(user);
        if (kyc == null) {
            kyc = new Kyc();
            kyc.setUser(user);
            kyc.setBvnKycStatus(KYCStatus.PENDING_UPLOAD);
            kyc.setNinKycStatus(KYCStatus.PENDING_UPLOAD);
            kyc.setCacKycStatus(KYCStatus.PENDING_UPLOAD);
            kyc.setRcNumberKycStatus(KYCStatus.PENDING_UPLOAD);
            kyc.setIdCardKycStatus(KYCStatus.PENDING_UPLOAD);
            kyc.setSelfieKycStatus(KYCStatus.PENDING_UPLOAD);
            kyc.setContractNoKycStatus(KYCStatus.PENDING_UPLOAD);
            kyc.setTinKycStatus(KYCStatus.PENDING_UPLOAD);
        }

        if (org.apache.commons.lang3.StringUtils.isNotBlank(userDto.getBvn())) {
            kyc.setBvn(userDto.getBvn());
            kyc.setBvnUploadedDate(new Date());
            kyc.setBvnKycStatus(KYCStatus.VERIFIED);
        }
        if (org.apache.commons.lang3.StringUtils.isNotBlank(userDto.getCACBase64Img())) {
            kyc.setCacBase64Img(userDto.getCACBase64Img());
            kyc.setCacUploadedDate(new Date());
            kyc.setCacKycStatus(KYCStatus.PENDING_VERIFICATION);
        }
        if (org.apache.commons.lang3.StringUtils.isNotBlank(userDto.getSelfieBase64Img())) {
            kyc.setSelfieBase64Img(userDto.getSelfieBase64Img());
            kyc.setSelfieUploadedDate(new Date());
            kyc.setSelfieKycStatus(KYCStatus.VERIFIED);
        }
        if (org.apache.commons.lang3.StringUtils.isNotBlank(userDto.getIdCardBase64Img())) {
            kyc.setIdCardBase64Img(userDto.getIdCardBase64Img());
            kyc.setIdCardUploadedDate(new Date());
            kyc.setIdCardKycStatus(KYCStatus.PENDING_VERIFICATION);
        }
        if (org.apache.commons.lang3.StringUtils.isNotBlank(userDto.getNin())) {
            kyc.setNin(userDto.getNin());
            kyc.setNinUploadedDate(new Date());
            kyc.setNinKycStatus(KYCStatus.VERIFIED);
        }

        if (org.apache.commons.lang3.StringUtils.isNotBlank(userDto.getRcNumber())) {
            kyc.setRcNumber(userDto.getRcNumber());
            kyc.setRcNumberUploadedDate(new Date());
            kyc.setRcNumberKycStatus(KYCStatus.PENDING_VERIFICATION);
        }
        if (org.apache.commons.lang3.StringUtils.isNotBlank(userDto.getTin())) {
            kyc.setTin(userDto.getTin());
            kyc.setTinUploadedDate(new Date());
            kyc.setTinKycStatus(KYCStatus.PENDING_VERIFICATION);
        }
        if (org.apache.commons.lang3.StringUtils.isNotBlank(userDto.getContractNo())) {
            kyc.setContractNo(userDto.getContractNo());
            kyc.setContractNoUploadedDate(new Date());
            kyc.setContractNoKycStatus(KYCStatus.PENDING_VERIFICATION);
        }

//        if (("AGENT".equals(user.getUserType()) || "FARMER".equals(user.getUserType())) && "VERIFIED".equals(kyc.getBvnKycStatus()) && "VERIFIED".equals(kyc.getNinKycStatus()) && "VERIFIED".equals(kyc.getSelfieKycStatus())){
//            user.setKycComplete(true);
//            userRepository.save(user);
//        }

        kycRepository.save(kyc);
        return "Successful";
    }

    @Override
    public String verifyBvnKyc(Long id) {
        try {
            if (id == null || id == 0) throw new BranchlessBankingException("Id cannot be null or 0");

            Kyc kyc = kycRepository.getById(id);

            if (kyc == null) {
                throw new BranchlessBankingException("BVN Kyc not found");
            }
            if (kyc.getBvnKycStatus() != KYCStatus.PENDING_VERIFICATION) {
                throw new BranchlessBankingException("Kyc not awaiting verification");
            }

            kyc.setBvnKycStatus(KYCStatus.VERIFIED);
            kyc.setVerifiedOrDeclinedDate(new Date());
            Kyc saved = kycRepository.save(kyc);
            //sendApprovedKycEmail(kyc, "BVN");

            isKycComplete(kyc.getUser());

            return "BVN Kyc successfully verified";
        } catch (BranchlessBankingException pe) {
            throw new BranchlessBankingException(pe.getMessage());
        } catch (Exception e) {
            log.error("EXCEPTION {}", e.getMessage());
            throw new BranchlessBankingException("Unable to Verify BVN");
        }
    }

    @Override
    public String verifyNinKyc(Long id) {
        try {
            if (id == null || id == 0) throw new BranchlessBankingException("Id cannot be null or 0");

            Kyc kyc = kycRepository.getById(id);

            if (kyc == null) {
                throw new BranchlessBankingException("NIN Kyc not found");
            }
            if (kyc.getNinKycStatus() != KYCStatus.PENDING_VERIFICATION) {
                throw new BranchlessBankingException("Kyc not awaiting verification");
            }
            ValidateVirtualNINRequest validateVirtualNINRequest = new ValidateVirtualNINRequest();
            validateVirtualNINRequest.setVirtualNin(kyc.getNin());
            ValidateVirtualNINResponse response = integrationService.validateVirtualNIN(validateVirtualNINRequest);
            if (!org.apache.commons.lang3.StringUtils.isEmpty(response.getError())) {
                throw new BranchlessBankingException(response.getError());
            }
            kyc.setNinKycStatus(KYCStatus.VERIFIED);
            kyc.setVerifiedOrDeclinedDate(new Date());
            Kyc saved = kycRepository.save(kyc);
            //sendApprovedKycEmail(kyc, "NIN");

            isKycComplete(kyc.getUser());

            return "NIN Kyc successfully verified";
        } catch (BranchlessBankingException pe) {
            throw new BranchlessBankingException(pe.getMessage());
        } catch (Exception e) {
            log.error("EXCEPTION {}", e.getMessage());
            throw new BranchlessBankingException("Unable to Verify NIN");
        }
    }

//    @Override
//    @RequireApproval(code="VERIFY_NIN_KYC",entityType= Kyc.class)
//    @PermissionScan(code="VERIFY_NIN_KYC",description="Verify Nin Kyc")
//    public String verifyVirtualNIN(Long id) {
//        try {
//            if (id == null || id == 0) throw new BranchlessBankingException("Id cannot be null or 0");
//
//            Kyc kyc = kycRepository.getById(id);
//
//            if (kyc == null) {
//                throw new BranchlessBankingException("NIN Kyc not found");
//            }
//            if (kyc.getNinKycStatus() != KYCStatus.PENDING_VERIFICATION) {
//                throw new BranchlessBankingException("Kyc not awaiting verification");
//            }
//            ValidateVirtualNINRequest validateVirtualNINRequest = new ValidateVirtualNINRequest();
//            validateVirtualNINRequest.setVirtualNin();
//            ValidateVirtualNINResponse response = integrationService.validateVirtualNIN(validateVirtualNINRequest);
//            if (!org.apache.commons.lang3.StringUtils.isEmpty(response.getError())) {
//                throw new BranchlessBankingException(response.getError());
//            }
//            kyc.setNinKycStatus(KYCStatus.VERIFIED);
//            kyc.setVerifiedOrDeclinedDate(new Date());
//            Kyc saved = kycRepository.save(kyc);
//            //sendApprovedKycEmail(kyc, "NIN");
//
//            isKycComplete(kyc.getUser());
//
//            return "NIN Kyc successfully verified";
//        } catch (BranchlessBankingException pe) {
//            throw new BranchlessBankingException(pe.getMessage());
//        } catch (Exception e) {
//            log.error("EXCEPTION {}", e.getMessage());
//            throw new BranchlessBankingException("Unable to Verify NIN");
//        }
//    }

    @Override
    public String verifyRcNumberKyc(Long id) {
        try {
            if (id == null || id == 0) throw new BranchlessBankingException("Id cannot be null or 0");

            Kyc kyc = kycRepository.getById(id);

            if (kyc == null) throw new BranchlessBankingException("RC Number Kyc not found");
            if (kyc.getRcNumberKycStatus() != KYCStatus.PENDING_VERIFICATION)
                throw new BranchlessBankingException("Kyc not awaiting verification");

            ValidateCACRequest validateCACRequest = new ValidateCACRequest();
            validateCACRequest.setRcNumber(kyc.getRcNumber());
            validateCACRequest.setCompanyName(kyc.getUser().getBusinessName());
            validateCACRequest.setType("BASIC");
            ValidateCacResponse response = integrationService.cac(validateCACRequest);
            if (!org.apache.commons.lang3.StringUtils.isEmpty(response.getError())) {
                throw new BranchlessBankingException(response.getError());
            }
            kyc.setRcNumberKycStatus(KYCStatus.VERIFIED);
            kyc.setVerifiedOrDeclinedDate(new Date());
            Kyc saved = kycRepository.save(kyc);
            //sendApprovedKycEmail(kyc, "RC NUMBER");

            isKycComplete(kyc.getUser());

            return "RC Number Kyc successfully verified";
        } catch (BranchlessBankingException pe) {
            throw new BranchlessBankingException(pe.getMessage());
        } catch (Exception e) {
            log.error("EXCEPTION {}", e.getMessage());
            throw new BranchlessBankingException("Unable to Verify RC Number");
        }
    }

    @Override
    public String verifySelfieKyc(Long id) {
        try {
            if (id == null || id == 0) throw new BranchlessBankingException("Id cannot be null or 0");

            Kyc kyc = kycRepository.getById(id);

            if (org.apache.commons.lang3.StringUtils.isBlank(kyc.getSelfieBase64Img()))
                throw new BranchlessBankingException("Selfie Image Kyc not found");
            if (kyc.getSelfieKycStatus() != KYCStatus.PENDING_VERIFICATION)
                throw new BranchlessBankingException("Kyc not awaiting verification");

//            ValidateImageRequest validateImageRequest = new ValidateImageRequest();
//            String image = ImageUtil.removeImagePrefix(kyc.getSelfieBase64Img());
//            validateImageRequest.setSelfieImage(image);
//            validateImageRequest.setBvn(kyc.getBvn());
//            ValidateBvnImageResponse response = integrationService.bvnSelfie(validateImageRequest);
//            if (!org.apache.commons.lang3.StringUtils.isEmpty(response.getError())) {
//                throw new BranchlessBankingException(response.getError());
//            }
//            if(!response.getSelfieVerification().isMatch()){
//                throw new BranchlessBankingException("SELFIE verification mismatch");
//            }

            kyc.setSelfieKycStatus(KYCStatus.VERIFIED);
            kyc.setVerifiedOrDeclinedDate(new Date());
            Kyc saved = kycRepository.save(kyc);
            //sendApprovedKycEmail(kyc, "SELFIE");

            isKycComplete(kyc.getUser());

            return "SELFIE Kyc successfully verified";
        } catch (BranchlessBankingException pe) {
            throw new BranchlessBankingException(pe.getMessage());
        } catch (Exception e) {
            log.error("EXCEPTION {}", e.getMessage());
            throw new BranchlessBankingException("Unable to Verify SELFIE");
        }
    }

    @Override
    public String verifyCacKyc(Long id) {
        try {
            if (id == null || id == 0) throw new BranchlessBankingException("Id cannot be null or 0");

            Kyc kyc = kycRepository.getById(id);

            if (Objects.isNull(kyc.getUser())) throw new BranchlessBankingException("CAC Cert. Kyc not found");
            if (kyc.getCacKycStatus() != KYCStatus.PENDING_VERIFICATION)
                throw new BranchlessBankingException("Kyc not awaiting verification");

            ValidateCACRequest validateCACRequest = new ValidateCACRequest();
            validateCACRequest.setRcNumber(kyc.getRcNumber());
            validateCACRequest.setCompanyName(kyc.getUser().getBusinessName());
            validateCACRequest.setType("BASIC");
            ValidateCacResponse response = integrationService.cac(validateCACRequest);
            if (!org.apache.commons.lang3.StringUtils.isEmpty(response.getError())) {
                throw new BranchlessBankingException(response.getError());
            }
            kyc.setCacKycStatus(KYCStatus.VERIFIED);
            kyc.setVerifiedOrDeclinedDate(new Date());
            Kyc saved = kycRepository.save(kyc);
            //sendApprovedKycEmail(kyc, "CAC CERT");

            isKycComplete(kyc.getUser());

            return "CAC Cert. Kyc successfully verified";
        } catch (BranchlessBankingException pe) {
            throw new BranchlessBankingException(pe.getMessage());
        } catch (Exception e) {
            log.error("EXCEPTION {}", e.getMessage());
            throw new BranchlessBankingException("Unable to Verify CAC Cert.");
        }
    }

    @Override
    public String verifyIdCardKyc(Long id) {
        try {
            if (id == null || id == 0) throw new BranchlessBankingException("Id cannot be null or 0");

            Kyc kyc = kycRepository.getById(id);

            if (Objects.isNull(kyc.getUser())) throw new BranchlessBankingException("ID Card Kyc not found");
            if (kyc.getIdCardKycStatus() != KYCStatus.PENDING_VERIFICATION)
                throw new BranchlessBankingException("Kyc not awaiting verification");

            kyc.setIdCardKycStatus(KYCStatus.VERIFIED);
            kyc.setVerifiedOrDeclinedDate(new Date());
            Kyc saved = kycRepository.save(kyc);
            //sendApprovedKycEmail(kyc, "ID CARD");

            isKycComplete(kyc.getUser());

            return "ID Card Kyc successfully verified";
        } catch (BranchlessBankingException pe) {
            throw new BranchlessBankingException(pe.getMessage());
        } catch (Exception e) {
            log.error("EXCEPTION {}", e.getMessage());
            throw new BranchlessBankingException("Unable to Verify ID Card");
        }
    }

    @Override
    public String verifyTinKyc(Long id) {
        try {
            if (id == null || id == 0) throw new BranchlessBankingException("Id cannot be null or 0");

            Kyc kyc = kycRepository.getById(id);

            if (Objects.isNull(kyc.getUser())) throw new BranchlessBankingException("TIN Kyc not found");
            if (kyc.getTinKycStatus() != KYCStatus.PENDING_VERIFICATION)
                throw new BranchlessBankingException("Kyc not awaiting verification");

            ValidateTinRequest validateTinRequest = new ValidateTinRequest();
            validateTinRequest.setTin(kyc.getTin());
            ValidateTinResponse response = integrationService.tin(validateTinRequest);
            if (!org.apache.commons.lang3.StringUtils.isEmpty(response.getError())) {
                throw new BranchlessBankingException(response.getError());
            }

            kyc.setTinKycStatus(KYCStatus.VERIFIED);
            kyc.setVerifiedOrDeclinedDate(new Date());
            Kyc saved = kycRepository.save(kyc);
            //sendApprovedKycEmail(kyc, "ID CARD");

            isKycComplete(kyc.getUser());

            return "TIN Kyc successfully verified";
        } catch (BranchlessBankingException pe) {
            throw new BranchlessBankingException(pe.getMessage());
        } catch (Exception e) {
            log.error("EXCEPTION {}", e.getMessage());
            throw new BranchlessBankingException("Unable to Verify TIN");
        }
    }

    @Override
    public String verifyContractNoKyc(Long id) {
        try {
            if (id == null || id == 0) throw new BranchlessBankingException("Id cannot be null or 0");

            Kyc kyc = kycRepository.getById(id);

            if (Objects.isNull(kyc.getUser())) throw new BranchlessBankingException("Contract No Kyc not found");
            if (kyc.getContractNoKycStatus() != KYCStatus.PENDING_VERIFICATION)
                throw new BranchlessBankingException("Contract No not awaiting verification");

            kyc.setContractNoKycStatus(KYCStatus.VERIFIED);
            kyc.setVerifiedOrDeclinedDate(new Date());
            Kyc saved = kycRepository.save(kyc);
            //sendApprovedKycEmail(kyc, "ID CARD");

            isKycComplete(kyc.getUser());

            return "Contract No Kyc successfully verified";
        } catch (BranchlessBankingException pe) {
            throw new BranchlessBankingException(pe.getMessage());
        } catch (Exception e) {
            log.error("EXCEPTION {}", e.getMessage());
            throw new BranchlessBankingException("Unable to Verify Contract No");
        }
    }

    @Override
    public String declineBvnKyc(Long id) {
        try {

            if (id == null || id == 0)
                throw new BranchlessBankingException("Id cannot be null or 0");


            Kyc kyc = kycRepository.getById(id);

            if (kyc == null) throw new BranchlessBankingException("BVN Kyc not found");
            if (kyc.getBvnKycStatus() != KYCStatus.PENDING_VERIFICATION)
                throw new BranchlessBankingException("BVN not awaiting verification");

            kyc.setBvnKycStatus(KYCStatus.DECLNED);
            kyc.setVerifiedOrDeclinedDate(new Date());
            Kyc saved = kycRepository.save(kyc);
            sendDeclinedKycEmail(saved, "BVN");

            return "BVN Kyc successfully declined";

        } catch (BranchlessBankingException pe) {
            throw new BranchlessBankingException(pe.getMessage());
        } catch (Exception e) {
            log.error("EXCEPTION {}", e.getMessage());
            throw new BranchlessBankingException("Unable to Verify BVN");
        }
    }

    @Override
    public String declineNinKyc(Long id) {
        try {

            if (id == null || id == 0)
                throw new BranchlessBankingException("Id cannot be null or 0");


            Kyc kyc = kycRepository.getById(id);

            if (kyc == null) throw new BranchlessBankingException("NIN Kyc not found");
            if (kyc.getNinKycStatus() != KYCStatus.PENDING_VERIFICATION)
                throw new BranchlessBankingException("NIN not awaiting verification");

            kyc.setNinKycStatus(KYCStatus.DECLNED);
            kyc.setVerifiedOrDeclinedDate(new Date());
            Kyc saved = kycRepository.save(kyc);
            sendDeclinedKycEmail(saved, "NIN");

            return "NIN Kyc successfully declined";

        } catch (BranchlessBankingException pe) {
            throw new BranchlessBankingException(pe.getMessage());
        } catch (Exception e) {
            log.error("EXCEPTION {}", e.getMessage());
            throw new BranchlessBankingException("Unable to Verify NIN");
        }
    }

    @Override
    public String declineRcNumberKyc(Long id) {
        try {

            if (id == null || id == 0)
                throw new BranchlessBankingException("Id cannot be null or 0");

            Kyc kyc = kycRepository.getById(id);

            if (Objects.isNull(kyc.getUser())) throw new BranchlessBankingException("RC Number Kyc not found");

            if (kyc.getRcNumberKycStatus() != KYCStatus.PENDING_VERIFICATION)
                throw new BranchlessBankingException("RC Number not awaiting verification");

            kyc.setRcNumberKycStatus(KYCStatus.DECLNED);
            kyc.setVerifiedOrDeclinedDate(new Date());
            Kyc saved = kycRepository.save(kyc);
            sendDeclinedKycEmail(saved, "RC NUMBER");

            return "RC Number Kyc successfully declined";

        } catch (BranchlessBankingException pe) {
            throw new BranchlessBankingException(pe.getMessage());
        } catch (Exception e) {
            log.error("EXCEPTION {}", e.getMessage());
            throw new BranchlessBankingException("Unable to Verify RC Number");
        }
    }

    @Override
    public String declineSelfieKyc(Long id) {
        try {

            if (id == null || id == 0)
                throw new BranchlessBankingException("Id cannot be null or 0");


            Kyc kyc = kycRepository.getById(id);

            if (Objects.isNull(kyc.getUser())) throw new BranchlessBankingException("Selfie Kyc not found");
            if (kyc.getSelfieKycStatus() != KYCStatus.PENDING_VERIFICATION)
                throw new BranchlessBankingException("Selfie not awaiting verification");

            kyc.setSelfieKycStatus(KYCStatus.DECLNED);
            kyc.setVerifiedOrDeclinedDate(new Date());
            Kyc saved = kycRepository.save(kyc);
            sendDeclinedKycEmail(saved, "SELFIE");

            return "Selfie Kyc successfully declined";

        } catch (BranchlessBankingException pe) {
            throw new BranchlessBankingException(pe.getMessage());
        } catch (Exception e) {
            log.error("EXCEPTION {}", e.getMessage());
            throw new BranchlessBankingException("Unable to Verify Selfie");
        }
    }

    @Override
    public String declineCacKyc(Long id) {
        try {

            if (id == null || id == 0)
                throw new BranchlessBankingException("Id cannot be null or 0");


            Kyc kyc = kycRepository.getById(id);

            if (Objects.isNull(kyc.getUser())) throw new BranchlessBankingException("CAC Cert. Kyc not found");
            if (kyc.getCacKycStatus() != KYCStatus.PENDING_VERIFICATION)
                throw new BranchlessBankingException("CAC Cert. not awaiting verification");

            kyc.setCacKycStatus(KYCStatus.DECLNED);
            kyc.setVerifiedOrDeclinedDate(new Date());
            Kyc saved = kycRepository.save(kyc);
            sendDeclinedKycEmail(saved, "CAC CERT");

            return "CAC Cert. Kyc successfully declined";

        } catch (BranchlessBankingException pe) {
            throw new BranchlessBankingException(pe.getMessage());
        } catch (Exception e) {
            log.error("EXCEPTION {}", e.getMessage());
            throw new BranchlessBankingException("Unable to Verify CAC Cert.");
        }
    }

    @Override
    public String declineIdCardKyc(Long id) {
        try {

            if (id == null || id == 0)
                throw new BranchlessBankingException("Id cannot be null or 0");


            Kyc kyc = kycRepository.getById(id);

            if (Objects.isNull(kyc.getUser())) throw new BranchlessBankingException("ID Card Kyc not found");
            if (kyc.getIdCardKycStatus() != KYCStatus.PENDING_VERIFICATION)
                throw new BranchlessBankingException("ID Card not awaiting verification");

            kyc.setIdCardKycStatus(KYCStatus.DECLNED);
            kyc.setVerifiedOrDeclinedDate(new Date());
            Kyc saved = kycRepository.save(kyc);
            sendDeclinedKycEmail(saved, "ID CARD");

            return "ID Card Kyc successfully declined";

        } catch (BranchlessBankingException pe) {
            throw new BranchlessBankingException(pe.getMessage());
        } catch (Exception e) {
            log.error("EXCEPTION {}", e.getMessage());
            throw new BranchlessBankingException("Unable to Verify ID Card");
        }
    }

    @Override
    public String declineTinKyc(Long id) {
        try {

            if (id == null || id == 0)
                throw new BranchlessBankingException("Id cannot be null or 0");


            Kyc kyc = kycRepository.getById(id);

            if (Objects.isNull(kyc.getUser())) throw new BranchlessBankingException("TIN Kyc not found");
            if (kyc.getTinKycStatus() != KYCStatus.PENDING_VERIFICATION)
                throw new BranchlessBankingException("TIN not awaiting verification");

            kyc.setTinKycStatus(KYCStatus.DECLNED);
            kyc.setVerifiedOrDeclinedDate(new Date());
            Kyc saved = kycRepository.save(kyc);
            sendDeclinedKycEmail(saved, "TIN");

            return "TIN Kyc successfully declined";

        } catch (BranchlessBankingException pe) {
            throw new BranchlessBankingException(pe.getMessage());
        } catch (Exception e) {
            log.error("EXCEPTION {}", e.getMessage());
            throw new BranchlessBankingException("Unable to Verify TIN");
        }
    }

    @Override
    public String declineContractNoKyc(Long id) {
        try {

            if (id == null || id == 0)
                throw new BranchlessBankingException("Id cannot be null or 0");


            Kyc kyc = kycRepository.getById(id);

            if (Objects.isNull(kyc.getUser())) throw new BranchlessBankingException("Contract No Kyc not found");
            if (kyc.getContractNoKycStatus() != KYCStatus.PENDING_VERIFICATION)
                throw new BranchlessBankingException("Contract No not awaiting verification");

            kyc.setContractNoKycStatus(KYCStatus.DECLNED);
            kyc.setVerifiedOrDeclinedDate(new Date());
            Kyc saved = kycRepository.save(kyc);
            sendDeclinedKycEmail(saved, "CONTRACT NUMBER");

            return "Contract No Kyc successfully declined";

        } catch (BranchlessBankingException pe) {
            throw new BranchlessBankingException(pe.getMessage());
        } catch (Exception e) {
            log.error("EXCEPTION {}", e.getMessage());
            throw new BranchlessBankingException("Unable to Verify Contract No");
        }
    }

    @Override
    public boolean isKycComplete(User user) {
        boolean status = true;

        if (user.isKycComplete()) {
            return true;
        }

        Kyc kyc = kycRepository.findByUser(user);

        if (null == user.getClassification()) {
            status = false;
        } else if (user.getClassification().equals(Classification.INDIVIDUAL)) {
            if (!kyc.getBvnKycStatus().equals(KYCStatus.VERIFIED)) {
                status = false;
            }
        } else {
                if (!kyc.getCacKycStatus().equals(KYCStatus.VERIFIED)) {
                    status = false;
                }

            if (Objects.isNull(user.getBusinessName()) || org.apache.commons.lang3.StringUtils.isBlank(user.getBusinessName())){
                status = false;
            }
        }

        if (status) {
            updateKycCompleteFlag(user, kyc);
        }

        return status;
    }

    @Override
    @Transactional
    public List<KycView> getPending(Pageable pageable) {
        List<String> pendingUserIds = kycRepository.findByTinKycStatusOrContractNoKycStatusOrRcNumberKycStatusOrCacKycStatusOrSelfieKycStatusOrIdCardKycStatusOrBvnKycStatusOrNinKycStatus(KYCStatus.PENDING_VERIFICATION, KYCStatus.PENDING_VERIFICATION, KYCStatus.PENDING_VERIFICATION, KYCStatus.PENDING_VERIFICATION, KYCStatus.PENDING_VERIFICATION, KYCStatus.PENDING_VERIFICATION, KYCStatus.PENDING_VERIFICATION, KYCStatus.PENDING_VERIFICATION).stream().limit(5).collect(Collectors.toList());
        List<KycView> views = new ArrayList<>();
        for (String id: pendingUserIds) {
            User user = userRepository.findById(Long.valueOf(id)).get();
            KycView kycView = convertToView(user);
            views.add(kycView);
        }
        return views;
    }

    private KycView convertToView(User user) {
        KycView view = new KycView();
        view.setUserName((user.getFirstName() != null && user.getLastName() != null) ?  user.getFirstName() + " " + user.getLastName() : "");
        view.setUserId(user.getId()!= null ? String.valueOf(user.getId()) : "");
        view.setUserClass(user.getClassification() != null ? user.getClassification().name() : "");

        return view;
    }

    @Override
    public void saveKyc(Kyc kyc) {
        kycRepository.save(kyc);
    }

//    @Override
//    public Page<KycDto> getPending(Pageable pageable) {
//        Page<Kyc> page = kycRepository.findByTinKycStatusOrContractNoKycStatusOrRcNumberKycStatusOrCacKycStatusOrSelfieKycStatusOrIdCardKycStatusOrBvnKycStatusOrNinKycStatus(KYCStatus.PENDING_VERIFICATION, KYCStatus.PENDING_VERIFICATION, KYCStatus.PENDING_VERIFICATION, KYCStatus.PENDING_VERIFICATION, KYCStatus.PENDING_VERIFICATION, KYCStatus.PENDING_VERIFICATION, KYCStatus.PENDING_VERIFICATION, KYCStatus.PENDING_VERIFICATION, pageable);
//        List<KycDto> dtOs = page.getContent().stream().map(this::convertEntityToDto).collect(Collectors.toList());
//        long t = page.getTotalElements();
//        Page<KycDto> pageImpl = new PageImpl<KycDto>(dtOs, pageable, t);
//        return pageImpl;

//        for (KycDto kycDto : kycDtos) {
//            KycPending pending = new KycPending();
//            kycDto.setUserIdentifier(kycDto.getUser().getUserIdentifier());
//            kycDto.setUserClass(kycDto.getUser().getClassification().toString());
//            String pendingItems = "";
//            if (kycDto.getUser().getClassification().equals(Classification.INDIVIDUAL)) {
//                if (kycDto.getBvnKycStatus().equals(KYCStatus.PENDING_VERIFICATION))
//                    pendingItems += "BVN, ";
//                if (kycDto.getNinKycStatus().equals(KYCStatus.PENDING_VERIFICATION))
//                    pendingItems += "NIN, ";
//                if (kycDto.getIdCardKycStatus().equals(KYCStatus.PENDING_VERIFICATION))
//                    pendingItems += "ID CARD, ";
//                if (kycDto.getSelfieKycStatus().equals(KYCStatus.PENDING_VERIFICATION))
//                    pendingItems += "SELFIE ";
//            } else {
//                if (kycDto.getRcNumberKycStatus().equals(KYCStatus.PENDING_VERIFICATION))
//                    pendingItems += "RC NUMBER, ";
//                if (kycDto.getTinKycStatus().equals(KYCStatus.PENDING_VERIFICATION))
//                    pendingItems += "TIN, ";
//                if (kycDto.getContractNoKycStatus().equals(KYCStatus.PENDING_VERIFICATION))
//                    pendingItems += "CONTRACT NO., ";
//                if (kycDto.getCacKycStatus().equals(KYCStatus.PENDING_VERIFICATION))
//                    pendingItems += "CAC DOC. ";
//            }
//            kycDto.setPendingItems(pendingItems);
//        }
//        return kycDtos;
//    }

    private void sendDeclinedKycEmail(Kyc kyc, String type) {
        try {

            Email email = new Email.Builder()
                    .setRecipient(kyc.getUser().getEmail())
                    .setSubject("KYC Declined - Payrail")
                    .setTemplate("mail/declinedkyc")
                    .build();

            String fullName = kyc.getUser().getFirstName() + " " + kyc.getUser().getLastName();

            Context context = new Context();
            context.setVariable("fullName", fullName);
            context.setVariable("type", type);
            mailService.sendGridMail(email, context);

//            NotificationHelper.publish(kyc.getUser(), "Dear customer, Your "+type+" Kyc has been unfortunately declined. Kindly re-upload valid kyc details to enable you enjoy our services.", "KYC", kyc.getId().toString());

        } catch (Exception e) {
            log.error("Error occurred declined kyc mail", e);
        }
    }


    public void updateKycCompleteFlag(User user, Kyc kyc) {
        user.setKycComplete(true);
        user.setNeedSetup(false);
        userRepository.save(user);

        try {

            String fullName = user.getFirstName() + " " + user.getLastName();


            Context context = new Context();
            context.setVariable("fullName", fullName);
            context.setVariable("username", user.getUserName());

            Email email = new Email.Builder()
                    .setRecipient(user.getEmail())
                    .setSubject(messageSource.getMessage("kyc.completed.email.subject", null, locale))
                    .setTemplate("mail/kyccompletion")
                    .build();

            mailService.sendGridMail(email, context);

        } catch (Exception e) {
            log.error("Error occurred sending kyc completion mail", e);
        }


    }

    private KycDto convertEntityToDto(Kyc kyc) {
        KycDto kycDto = modelMapper.map(kyc, KycDto.class);
//        if (kyc.getUser() != null) {
//            kycDto.setUserId(kyc.getUser().getId());
//            kycDto.setUserName(kyc.getUser().getFirstName() + " " + kyc.getUser().getLastName());
//            if (!Objects.isNull(kyc.getUser().getClassification())) {
//                kycDto.setUserClass(kyc.getUser().getClassification().toString());
//            }
//        }
        //log.info("KYC DTO >>> {}", kycDto);
        return kycDto;

    }
}
