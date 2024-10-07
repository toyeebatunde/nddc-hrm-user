package co.payrail.attendance_srv.integration.service.implementation;

import co.payrail.attendance_srv.exception.BranchlessBankingException;
import co.payrail.attendance_srv.integration.dojah.DojahApi;
import co.payrail.attendance_srv.integration.dojah.model.age_verification.ValidateAgeRequest;
import co.payrail.attendance_srv.integration.dojah.model.age_verification.ValidateAgeResponse;
import co.payrail.attendance_srv.integration.dojah.model.lookupbvn.LookupBvnResponse;
import co.payrail.attendance_srv.integration.dojah.model.validateImage.ValidateImageRequest;
import co.payrail.attendance_srv.integration.dojah.model.validateImage.validatebvnimage.ValidateBvnImageResponse;
import co.payrail.attendance_srv.integration.dojah.model.validateImage.validateninimage.ValidateNinImageResponse;
import co.payrail.attendance_srv.integration.dojah.model.validateImage.validatephotoId.ValidatePhotoIdResponse;
import co.payrail.attendance_srv.integration.dojah.model.validatebvn.ValidateBvnRequest;
import co.payrail.attendance_srv.integration.dojah.model.validatecac.ValidateCACRequest;
import co.payrail.attendance_srv.integration.dojah.model.validatecac.ValidateCacResponse;
import co.payrail.attendance_srv.integration.dojah.model.validatecac.validatecacadvance.ValidateCacAdvanceResponse;
import co.payrail.attendance_srv.integration.dojah.model.validatecac.validatecacbasic.ValidateCacBasicResponse;
import co.payrail.attendance_srv.integration.dojah.model.validatedl.ValidateDlRequest;
import co.payrail.attendance_srv.integration.dojah.model.validatedl.ValidateDlResponse;
import co.payrail.attendance_srv.integration.dojah.model.validatenin.ValidateNinRequest;
import co.payrail.attendance_srv.integration.dojah.model.validatenin.ValidateNinResponse;
import co.payrail.attendance_srv.integration.dojah.model.validatenuban.ValidateNubanRequest;
import co.payrail.attendance_srv.integration.dojah.model.validatenuban.ValidateNubanResponse;
import co.payrail.attendance_srv.integration.dojah.model.validatetin.ValidateTinRequest;
import co.payrail.attendance_srv.integration.dojah.model.validatetin.ValidateTinResponse;
import co.payrail.attendance_srv.integration.dojah.model.validatevin.ValidateVinRequest;
import co.payrail.attendance_srv.integration.dojah.model.validatevin.ValidateVinResponse;
import co.payrail.attendance_srv.integration.dojah.model.virtual_nin.ValidateVirtualNINRequest;
import co.payrail.attendance_srv.integration.dojah.model.virtual_nin.ValidateVirtualNINResponse;
import co.payrail.attendance_srv.integration.service.IntegrationService;
import co.payrail.attendance_srv.integration.service.dto.kyc.BranchlessBvnEnquiryRequest;
import co.payrail.attendance_srv.integration.service.dto.kyc.BranchlessNameEnquiryRequest;
import co.payrail.attendance_srv.integration.service.dto.kyc.BranchlessNameEnquiryResponse;
import co.payrail.attendance_srv.integration.termii.model.TermiiSmsRequest;
import co.payrail.attendance_srv.integration.termii.model.TermiiSmsResponse;
import co.payrail.attendance_srv.integration.termii.service.Termii;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Slf4j
@Service
public class IntegrationServiceImpl implements IntegrationService {

    private static final int PHONE_NO_LENGTH = 14;
    @Autowired
    private Termii termii;


    @Autowired
    private DojahApi dojahApi;
    @Autowired
    private MessageSource messageSource ;

    private final Locale locale = LocaleContextHolder.getLocale();
    @Autowired
    @Value("banking.nip")
    private  String nipBankName;

    @Autowired
    @Value("banking.vas")
    private  String vasBankName;

    @Override
    public BranchlessNameEnquiryResponse nameEnquiry(BranchlessNameEnquiryRequest request) {
        try {

            ValidateNubanRequest validateNubanRequest = new ValidateNubanRequest();
            validateNubanRequest.setAccountNumber(request.getAccountNumber());
            validateNubanRequest.setBankCode(request.getBankCode());
            ValidateNubanResponse validateNubanResponse = dojahApi.validateNuban(validateNubanRequest);
            log.info("Name enquiry details ..{}",validateNubanResponse);
            if(!StringUtils.isEmpty(validateNubanResponse.getAccountName())){
                return BranchlessNameEnquiryResponse.builder()
                        .accountName(validateNubanResponse.getAccountName())
                        .build();
            }else {
                throw new BranchlessBankingException(messageSource.getMessage("account.name.validation.failed", null, locale));
            }
        }catch ( Exception ex){
            log.error("exception ..{}",ex);
            ex.printStackTrace();
            throw new BranchlessBankingException(messageSource.getMessage("account.name.validation.failed", null, locale));
        }
    }
    @Override
    public LookupBvnResponse bvn(BranchlessBvnEnquiryRequest request) {
        try {
            log.info("BVN enquiry request ..{}",request.toString());
            ValidateBvnRequest validateBvnRequest = new ValidateBvnRequest();
            validateBvnRequest.setBvn(request.getBvn());
//            if(!StringUtils.isEmpty(request.getFirstName())){
//                validateBvnRequest.setFirstName(request.getFirstName());
//            }
//            if(!StringUtils.isEmpty(request.getLastName())){
//                validateBvnRequest.setLastName(request.getLastName());
//            }
            LookupBvnResponse response = dojahApi.lookupBvnFull(validateBvnRequest);
            log.info("BVN enquiry details ..{}",response.toString());
            return response;
        }catch (BranchlessBankingException e){
            log.info("Exception ..{}", e.getMessage());
            throw new BranchlessBankingException(e.getMessage());
        }catch ( Exception ex){
            log.error("exception ..{}",ex);
            ex.printStackTrace();
            throw  new BranchlessBankingException(messageSource.getMessage("bvn.validation.failed", null, locale));
        }
    }

    @Override
    public ValidateNinResponse nin(ValidateNinRequest request) {
        try {
            log.info("NIN enquiry request ..{}",request.toString());
            ValidateNinResponse response = dojahApi.validateNin(request);
            log.info("NIN enquiry details ..{}",response.toString());
            return response;

        }catch (BranchlessBankingException e){
            log.info("Exception ..{}", e.getMessage());
            throw new BranchlessBankingException(e.getMessage());
        }catch ( Exception ex){
            log.error("exception ..{}",ex);
            ex.printStackTrace();
            throw  new BranchlessBankingException(messageSource.getMessage("nin.validation.failed", null, locale));
        }
    }

    @Override
    public ValidateVirtualNINResponse validateVirtualNIN(ValidateVirtualNINRequest request) {
        try {
            log.info("NIN enquiry request ..{}",request.toString());
            ValidateVirtualNINResponse response = dojahApi.validateVirtualNin(request);
            log.info("NIN enquiry details ..{}",response.toString());
            return response;

        }catch (BranchlessBankingException e){
            log.info("Exception ..{}", e.getMessage());
            throw new BranchlessBankingException(e.getMessage());
        }catch ( Exception ex){
            log.error("exception ..{}",ex);
            ex.printStackTrace();
            throw  new BranchlessBankingException(messageSource.getMessage("nin.validation.failed", null, locale));
        }
    }

    @Override
    public ValidateTinResponse tin(ValidateTinRequest request) {
        try {
            log.info("TIN enquiry request ..{}",request.toString());
            ValidateTinResponse response = dojahApi.validateTin(request);
            log.info("TIN enquiry details ..{}",response.toString());
            return response;

        }catch (BranchlessBankingException e){
            log.info("Exception ..{}", e.getMessage());
            throw new BranchlessBankingException(e.getMessage());
        }catch ( Exception ex){
            log.error("exception ..{}",ex);
            ex.printStackTrace();
            throw  new BranchlessBankingException(messageSource.getMessage("validation.exist.error", null, locale));
        }
    }

    @Override
    public ValidateVinResponse vin(ValidateVinRequest request) {
        try {
            log.info("VIN enquiry request ..{}",request.toString());
            ValidateVinResponse response = dojahApi.validateVin(request);
            log.info("VIN enquiry details ..{}",response.toString());
            return response;
        }catch (BranchlessBankingException e){
            log.info("Exception ..{}", e.getMessage());
            throw new BranchlessBankingException(e.getMessage());
        }catch ( Exception ex){
            log.error("exception ..{}",ex);
            ex.printStackTrace();
            throw  new BranchlessBankingException(messageSource.getMessage("validation.exist.error", null, locale));
        }
    }

    @Override
    public ValidateDlResponse drl(ValidateDlRequest request) {
        try {
            log.info("Driver licence enquiry request ..{}",request.toString());
            ValidateDlResponse response = dojahApi.validateDriverLicence(request);
            log.info("Driver licence enquiry details ..{}",response.toString());
            return response;
        }catch (BranchlessBankingException e){
            log.info("Exception ..{}", e.getMessage());
            throw new BranchlessBankingException(e.getMessage());
        }catch ( Exception ex){
            log.error("exception ..{}",ex);
            ex.printStackTrace();
            throw  new BranchlessBankingException(messageSource.getMessage("validation.exist.error", null, locale));
        }
    }

    @Override
    public ValidateCacResponse cac(ValidateCACRequest request) {
        try {
            log.info("CAC enquiry request ..{}",request.toString());
            ValidateCacResponse response = new ValidateCacResponse();
            if(StringUtils.isEmpty(request.getType())){
                response = dojahApi.validateCAC(request);
                if(!StringUtils.isEmpty(response.getError())){
                    return response;
                }
            }else if(request.getType().equalsIgnoreCase("BASIC")){
                ValidateCacBasicResponse validateCacBasicResponse = dojahApi.validateCACBasic(request);
                if(!StringUtils.isEmpty(validateCacBasicResponse.getError())){
                    return response;
                }
                response.setRcNumber(validateCacBasicResponse.getRcNumber());
                response.setAddress(validateCacBasicResponse.getAddress());
                response.setCompanyName(validateCacBasicResponse.getCompanyName());
                response.setDateOfRegistration(validateCacBasicResponse.getRegDate());
            }else if(request.getType().equalsIgnoreCase("ADVANCE")){
                ValidateCacAdvanceResponse validateCacAdvanceResponse = dojahApi.validateCACAdvance(request);
                if(!StringUtils.isEmpty(validateCacAdvanceResponse.getError())){
                    return response;
                }
                response.setRcNumber(validateCacAdvanceResponse.getRCNumber());
                response.setAddress(validateCacAdvanceResponse.getHeadOfficeAddress());
                response.setCompanyName(validateCacAdvanceResponse.getNameOfCompany());
                response.setDateOfRegistration(validateCacAdvanceResponse.getDateOfRegistration());
            }
            log.info("CAC enquiry details ..{}", response);
            return response;
        }catch (BranchlessBankingException e){
            log.info("Exception ..{}", e.getMessage());
            throw new BranchlessBankingException(e.getMessage());
        }catch ( Exception ex){
            log.error("exception ..{}",ex);
            ex.printStackTrace();
            throw  new BranchlessBankingException(messageSource.getMessage("validation.exist.error", null, locale));
        }
    }

    @Override
    public ValidateAgeResponse age(ValidateAgeRequest request) {
        try {
            log.info("Age enquiry request ..{}",request.toString());
            ValidateAgeResponse response = dojahApi.validateAge(request);
            log.info("Age enquiry details ..{}",response.toString());
            return response;
        }catch (BranchlessBankingException e){
            log.info("Exception ..{}", e.getMessage());
            throw new BranchlessBankingException(e.getMessage());
        }catch ( Exception ex){
            log.error("exception ..{}",ex);
            ex.printStackTrace();
            throw  new BranchlessBankingException(messageSource.getMessage("validation.exist.error", null, locale));
        }
    }

    @Override
    public ValidateBvnImageResponse bvnSelfie(ValidateImageRequest request) {
        try {
            ValidateBvnImageResponse response = dojahApi.validateBvnWithSelfie(request);
            log.info("BVN with Selfie enquiry details ..{}",response.toString());
            return response;
        }catch (BranchlessBankingException e){
            log.info("Exception ..{}", e.getMessage());
            throw new BranchlessBankingException(e.getMessage());
        }catch ( Exception ex){
            log.error("exception ..{}",ex);
            ex.printStackTrace();
            throw  new BranchlessBankingException(messageSource.getMessage("validation.exist.error", null, locale));
        }
    }

    @Override
    public ValidateNinImageResponse ninSelfie(ValidateImageRequest request) {
        try {
            log.info("NIN with Selfie enquiry request ..{}",request.toString());
            ValidateNinImageResponse response = dojahApi.validateNinWithSelfie(request);
            log.info("NIN with Selfie enquiry details ..{}",response.toString());
            return response;
        }catch (BranchlessBankingException e){
            log.info("Exception ..{}", e.getMessage());
            throw new BranchlessBankingException(e.getMessage());
        }catch ( Exception ex){
            log.error("exception ..{}",ex);
            ex.printStackTrace();
            throw  new BranchlessBankingException(messageSource.getMessage("validation.exist.error", null, locale));
        }
    }

    @Override
    public ValidatePhotoIdResponse photoId(ValidateImageRequest request) {
        try {
            log.info("PhotoId enquiry request ..{}",request.toString());
            ValidatePhotoIdResponse response = dojahApi.validatePhotoIdWithSelfie(request);
            log.info("PhotoId enquiry details ..{}",response.toString());
            return response;
        }catch (BranchlessBankingException e){
            log.info("Exception ..{}", e.getMessage());
            throw new BranchlessBankingException(e.getMessage());
        }catch ( Exception ex){
            log.error("exception ..{}",ex);
            ex.printStackTrace();
            throw  new BranchlessBankingException(messageSource.getMessage("validation.exist.error", null, locale));
        }
    }


    @Override
    public void sendSms(String phoneNumber, String message) throws NullPointerException  {
        String formattedPhone = validateNumber(phoneNumber);
        TermiiSmsRequest request = new TermiiSmsRequest();
        request.setTo(formattedPhone);
        request.setSms(message);

        try {
            TermiiSmsResponse response = termii.sendSms(request);
            log.info("+++ SMS response ..{}",response);
        }   catch (Exception e){
            e.printStackTrace();
        }

    }




    /**
     * This validates a phone number against some specified rules
     * returns the correct number or null if the number failed a rule
     */
    public String validateNumber(String phone) throws NullPointerException {
        String returned = null;
        String phoneLocal = phone.replaceAll("[()]", "").trim();
        //check if it is in international format
        if (phoneLocal.contains("+")) {
            //Only send to mobile numbers
            //check if this is a nigerian number
            if (phoneLocal.contains("+234")) {
                if (phoneLocal.length() == PHONE_NO_LENGTH) {
                    returned = phoneLocal;
                } else {
                    //this a landline or invalid number hence no alert will be sent
                    log.error("This is an invalid number or a landline hence no alert will be sent: %s",
                            phoneLocal.replaceAll("[()]", "").trim());
                    log.info("Accepted formats; +234********** and 0**********");
                    throw new NullPointerException();
                }
            } else {
                //a non nigerian number
                returned = phoneLocal;
            }

            //check for more than one + sign
            if (phoneLocal.lastIndexOf('+') > 0) {
                //the phone number contains more than one + therefore it is invalid
                log.error(String.format("The phone number %s has more than one plus", phoneLocal));
                log.info("Accepted formats; +234********** and 0**********");
                throw new NullPointerException("Invalid phone number");
            }
        }else if (phoneLocal.contains("234")){
            //check if it is a landline
            if (phoneLocal.length() < 13) {
                log.error("This is a landline hence no alert will be sent");
                throw new NullPointerException("");
            } else {
                //It is a mobile number
                //convert it to international format
                returned = "+" + phoneLocal.trim();
            }
        } else {
            //check if it is a landline
            if (phoneLocal.length() < 10) {
                log.error("This is a landline hence no alert will be sent");
                throw new NullPointerException("");
            } else if (StringUtils.remove(phoneLocal,'+').length() > 11) {
                log.error("Invalid number: " + phoneLocal);
                log.info("Accepted formats; +234********** and 0**********");
                throw new NullPointerException();
            } else {
                //It is a mobile number
                //convert it to international format
                returned = "+234" + phoneLocal.substring(1).replaceAll("[()]", "").trim();
            }
        }
        //check if the number is invalid
        if (returned.length() < PHONE_NO_LENGTH) {
            log.error(String.format("Phone Number %s: is invalid", phoneLocal));
            log.info("Accepted formats; +234********** and 0**********");
            throw new NullPointerException("");
        } else if (returned.equals("+2348000000000")) {
            log.error("Invalid number: 08000000000");
            throw new NullPointerException("");
        }

        return returned.substring(1);
    }

}
