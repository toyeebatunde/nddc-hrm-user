package co.payrail.attendance_srv.integration.dojah;


import co.payrail.attendance_srv.integration.dojah.exceptions.DojahExceptions;
import co.payrail.attendance_srv.integration.dojah.model.DojahReponse;
import co.payrail.attendance_srv.integration.dojah.model.age_verification.ValidateAgeRequest;
import co.payrail.attendance_srv.integration.dojah.model.age_verification.ValidateAgeResponse;
import co.payrail.attendance_srv.integration.dojah.model.international_passport.ValidateInternationalPassportRequest;
import co.payrail.attendance_srv.integration.dojah.model.international_passport.ValidateInternationalPassportResponse;
import co.payrail.attendance_srv.integration.dojah.model.lookupbvn.LookupBvnResponse;
import co.payrail.attendance_srv.integration.dojah.model.validateImage.ValidateImageRequest;
import co.payrail.attendance_srv.integration.dojah.model.validateImage.validatebvnimage.ValidateBvnImageResponse;
import co.payrail.attendance_srv.integration.dojah.model.validateImage.validateninimage.ValidateNinImageResponse;
import co.payrail.attendance_srv.integration.dojah.model.validateImage.validatephotoId.ValidatePhotoIdResponse;
import co.payrail.attendance_srv.integration.dojah.model.validatebvn.ValidateBvnRequest;
import co.payrail.attendance_srv.integration.dojah.model.validatebvn.ValidateBvnResponse;
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
import co.payrail.attendance_srv.integration.dojah.model.validatephonenumber.ValidatePhoneRequest;
import co.payrail.attendance_srv.integration.dojah.model.validatephonenumber.validatephoneadvance.ValidatePhoneAdvanceResponse;
import co.payrail.attendance_srv.integration.dojah.model.validatephonenumber.validatephonebasic.ValidatePhoneBasicResponse;
import co.payrail.attendance_srv.integration.dojah.model.validatetin.ValidateTinRequest;
import co.payrail.attendance_srv.integration.dojah.model.validatetin.ValidateTinResponse;
import co.payrail.attendance_srv.integration.dojah.model.validatevin.ValidateVinRequest;
import co.payrail.attendance_srv.integration.dojah.model.validatevin.ValidateVinResponse;
import co.payrail.attendance_srv.integration.dojah.model.virtual_nin.ValidateVirtualNINRequest;
import co.payrail.attendance_srv.integration.dojah.model.virtual_nin.ValidateVirtualNINResponse;
import co.payrail.attendance_srv.integration.dojah.prop.DojahProp;
import co.payrail.attendance_srv.integration.dojah.service.DojahApiService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import com.google.gson.JsonObject;

import static java.util.stream.Collectors.joining;

@Slf4j
@Service
public class DojahApi {

    @Autowired
    DojahApiService dojahApiService;

    @Autowired
    DojahProp dojahProp;

    @Autowired
    private Gson gson ;

    public ValidateBvnResponse validateBvn(ValidateBvnRequest validateBvnRequest) {
        String urlToCall = dojahProp.getUrl()+"bvn?";
        log.info("URL: " + urlToCall);
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("bvn", validateBvnRequest.getBvn());
        if(!StringUtils.isEmpty(validateBvnRequest.getFirstName())){
            requestParams.put("first_name", validateBvnRequest.getFirstName());
        }
        if(!StringUtils.isEmpty(validateBvnRequest.getLastName())){
            requestParams.put("last_name", validateBvnRequest.getLastName());
        }
        if(!StringUtils.isEmpty(validateBvnRequest.getDob())){
            requestParams.put("dob", validateBvnRequest.getDob());
        }

        String encodedURL = encodeUrl(urlToCall, requestParams);
        try  {
            String result =  dojahApiService.get(encodedURL);
            log.info("result  ......{}", result);
            DojahReponse dojahReponse = gson.fromJson(result , DojahReponse.class);
            JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
            if(!Objects.isNull(dojahReponse.getEntity())){
                return gson.fromJson(jsonObject.get("entity") , ValidateBvnResponse.class);
            }else{
                return gson.fromJson(result, ValidateBvnResponse.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new DojahExceptions(e.getMessage());
        }
    }


    public LookupBvnResponse lookupBvn(ValidateBvnRequest validateBvnRequest) throws Exception{
        String urlToCall = dojahProp.getUrl()+"bvn/full?";
        log.info("URL: " + urlToCall);
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("bvn", validateBvnRequest.getBvn());

        String encodedURL = encodeUrl(urlToCall, requestParams);
        try  {
            String result =  dojahApiService.get(encodedURL);
            //log.info("result  ......{}", result);
            DojahReponse dojahReponse = gson.fromJson(result , DojahReponse.class);
            JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
            if(!Objects.isNull(dojahReponse.getEntity())){
                return gson.fromJson(jsonObject.get("entity") , LookupBvnResponse.class);
            }else{
                return gson.fromJson(result, LookupBvnResponse.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new DojahExceptions(e.getMessage());
        }
    }

    public LookupBvnResponse lookupBvnFull(ValidateBvnRequest validateBvnRequest) throws Exception{
        String urlToCall = dojahProp.getUrl()+"bvn/advance?";
        log.info("URL: " + urlToCall);
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("bvn", validateBvnRequest.getBvn());
        String encodedURL = encodeUrl(urlToCall, requestParams);
        try  {
            String result =  dojahApiService.get(encodedURL);
            log.info("result  ......{}", result);
            DojahReponse dojahReponse = gson.fromJson(result , DojahReponse.class);
            JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
            if(!Objects.isNull(dojahReponse.getEntity())){
                return gson.fromJson(jsonObject.get("entity") , LookupBvnResponse.class);
            }else{
                return gson.fromJson(result, LookupBvnResponse.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new DojahExceptions(e.getMessage());
        }
    }

    public ValidateNubanResponse validateNuban(ValidateNubanRequest validateNubanRequest) throws Exception{
        String urlToCall = dojahProp.getUrl()+"nuban/status?";
        log.info("result  ......{}", urlToCall);
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("account_number", validateNubanRequest.getAccountNumber());
        requestParams.put("bank_code", validateNubanRequest.getBankCode());
        String encodedURL = encodeUrl(urlToCall, requestParams);
        log.info("++++encodedURL  ......{}", encodedURL);
        try  {
            String result =  dojahApiService.get(encodedURL);
            log.info("result  ......{}", result);
            DojahReponse dojahReponse = gson.fromJson(result , DojahReponse.class);
            JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
            if(!Objects.isNull(dojahReponse.getEntity())){
                return gson.fromJson(jsonObject.get("entity") , ValidateNubanResponse.class);
            }else{
                return gson.fromJson(result, ValidateNubanResponse.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new DojahExceptions(e.getMessage());
        }
    }



    public ValidateNinResponse validateNin(ValidateNinRequest validateNinRequest) throws Exception{
        String urlToCall = dojahProp.getUrl()+"nin?";
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("nin", validateNinRequest.getNin());
        String encodedURL = encodeUrl(urlToCall, requestParams);
        try  {
            String result =  dojahApiService.get(encodedURL);
            log.info("result  ......{}", result);
            DojahReponse dojahReponse = gson.fromJson(result , DojahReponse.class);
            JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
            if(!Objects.isNull(dojahReponse.getEntity())){
                return gson.fromJson(jsonObject.get("entity") , ValidateNinResponse.class);
            }else{
                return gson.fromJson(result, ValidateNinResponse.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new DojahExceptions(e.getMessage());
        }
    }

    public ValidateTinResponse validateTin(ValidateTinRequest validateTinRequest) throws Exception{
        String urlToCall = dojahProp.getUrl()+"tin?";
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("tin", validateTinRequest.getTin());
        String encodedURL = encodeUrl(urlToCall, requestParams);
        try  {
            String result =  dojahApiService.get(encodedURL);
            log.info("result  ......{}", result);
            DojahReponse dojahReponse = gson.fromJson(result , DojahReponse.class);
            JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
            if(!Objects.isNull(dojahReponse.getEntity())){
                return gson.fromJson(jsonObject.get("entity") , ValidateTinResponse.class);
            }else{
                return gson.fromJson(result, ValidateTinResponse.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new DojahExceptions(e.getMessage());
        }
    }

    public ValidateVirtualNINResponse validateVirtualNin(ValidateVirtualNINRequest validateVirtualNinRequest){
        String urlToCall = dojahProp.getUrl()+"vnin?";
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("vnin", validateVirtualNinRequest.getVirtualNin());
        String encodedURL = encodeUrl(urlToCall, requestParams);
        try  {
            String result =  dojahApiService.get(encodedURL);
            log.info("result  ......{}", result);
            DojahReponse dojahReponse = gson.fromJson(result , DojahReponse.class);
            JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
            if(!Objects.isNull(dojahReponse.getEntity())){
                return gson.fromJson(jsonObject.get("entity") , ValidateVirtualNINResponse.class);
            }else{
                return gson.fromJson(result, ValidateVirtualNINResponse.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new DojahExceptions(e.getMessage());
        }
    }
    public ValidateVinResponse validateVin(ValidateVinRequest validateVinRequest) throws Exception{
        String urlToCall = dojahProp.getUrl()+"vin?";
        Map<String, String> requestParams = new HashMap<>();
//        if(!StringUtils.isEmpty(validateVinRequest.getVin())){
//            requestParams.put("mode","vin");
            requestParams.put("vin", validateVinRequest.getVin());
//            requestParams.put("state", validateVinRequest.getState());
//            requestParams.put("lastname", validateVinRequest.getLastname());
//        }else{
//            requestParams.put("mode","dob");
//            requestParams.put("state", validateVinRequest.getState());
//            requestParams.put("lastname", validateVinRequest.getLastname());
//            requestParams.put("dob", validateVinRequest.getDob());
//            requestParams.put("firstname", validateVinRequest.getFirstname());
//        }
        String encodedURL = encodeUrl(urlToCall, requestParams);
        try  {
            String result =  dojahApiService.get(encodedURL);
            log.info("result  ......{}", result);
            DojahReponse dojahReponse = gson.fromJson(result , DojahReponse.class);
            JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
            if(!Objects.isNull(dojahReponse.getEntity())){
                return gson.fromJson(jsonObject.get("entity") , ValidateVinResponse.class);
            }else{
                return gson.fromJson(result, ValidateVinResponse.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new DojahExceptions(e.getMessage());
        }
    }

    public ValidateDlResponse validateDriverLicence(ValidateDlRequest validateDlRequest) throws Exception{
        String urlToCall = dojahProp.getUrl()+"dl?";
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("license_number", validateDlRequest.getLicenseNumber());
        requestParams.put("dob", validateDlRequest.getDob());
        String encodedURL = encodeUrl(urlToCall, requestParams);
        try  {
            String result =  dojahApiService.get(encodedURL);
            log.info("result  ......{}", result);
            DojahReponse dojahReponse = gson.fromJson(result , DojahReponse.class);
            JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
            if(!Objects.isNull(dojahReponse.getEntity())){
                return gson.fromJson(jsonObject.get("entity") , ValidateDlResponse.class);
            }else{
                return gson.fromJson(result, ValidateDlResponse.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new DojahExceptions(e.getMessage());
        }
    }

    public ValidateCacResponse validateCAC(ValidateCACRequest validateCACRequest) throws Exception{
        String urlToCall = dojahProp.getUrl()+"cac?";
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("rc_number", validateCACRequest.getRcNumber());
        requestParams.put("company_name", validateCACRequest.getCompanyName());

        String encodedURL = encodeUrl(urlToCall, requestParams);

        try  {
            String result =  dojahApiService.get(encodedURL);
            log.info("result  ......{}", result);
            DojahReponse dojahReponse = gson.fromJson(result , DojahReponse.class);
            JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
            if(!Objects.isNull(dojahReponse.getEntity())){
                return gson.fromJson(jsonObject.get("entity") , ValidateCacResponse.class);
            }else{
                return gson.fromJson(result, ValidateCacResponse.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new DojahExceptions(e.getMessage());
        }
    }

    public ValidateCacBasicResponse validateCACBasic(ValidateCACRequest validateCACRequest) throws Exception{
        String urlToCall = dojahProp.getUrl()+"cac/basic?";
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("rc_number", validateCACRequest.getRcNumber());
        String encodedURL = encodeUrl(urlToCall, requestParams);
        try  {
            String result =  dojahApiService.get(encodedURL);
            log.info("result  ......{}", result);
            DojahReponse dojahReponse = gson.fromJson(result , DojahReponse.class);
            JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
            if(!Objects.isNull(dojahReponse.getEntity())){
                return gson.fromJson(jsonObject.get("entity") , ValidateCacBasicResponse.class);
            }else{
                return gson.fromJson(result, ValidateCacBasicResponse.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new DojahExceptions(e.getMessage());
        }
    }


    public ValidateCacAdvanceResponse validateCACAdvance(ValidateCACRequest validateCACRequest) throws Exception{
        String urlToCall = dojahProp.getUrl()+"cac/advance?";
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("rc_number", validateCACRequest.getRcNumber());
        String encodedURL = encodeUrl(urlToCall, requestParams);
        try  {
            String result =  dojahApiService.get(encodedURL);
            log.info("result  ......{}", result);
            DojahReponse dojahReponse = gson.fromJson(result , DojahReponse.class);
            JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
            if(!Objects.isNull(dojahReponse.getEntity())){
                return gson.fromJson(jsonObject.get("entity") , ValidateCacAdvanceResponse.class);
            }else{
                return gson.fromJson(result, ValidateCacAdvanceResponse.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new DojahExceptions(e.getMessage());
        }
    }

    public ValidateAgeResponse validateAge(ValidateAgeRequest validateAgeRequest) throws Exception{
        String urlToCall = dojahProp.getUrl()+"age_verification?";
        Map<String, String> requestParams = new HashMap<>();
        if(!StringUtils.isEmpty(validateAgeRequest.getAccountNumber())){
            requestParams.put("account_number", validateAgeRequest.getAccountNumber());
        }else if(!StringUtils.isEmpty(validateAgeRequest.getBvn())){
            requestParams.put("bvn", validateAgeRequest.getBvn());
        }else if(!StringUtils.isEmpty(validateAgeRequest.getPhoneNumber())){
            requestParams.put("phone_number", validateAgeRequest.getPhoneNumber());
        }else {
            throw new DojahExceptions("Kindly provide a valid verification parameter");
        }
        String encodedURL = encodeUrl(urlToCall, requestParams);
        try  {
            String result =  dojahApiService.get(encodedURL);
            log.info("result  ......{}", result);
            DojahReponse dojahReponse = gson.fromJson(result , DojahReponse.class);
            JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
            if(!Objects.isNull(dojahReponse.getEntity())){
                return gson.fromJson(jsonObject.get("entity") , ValidateAgeResponse.class);
            }else{
                return gson.fromJson(result, ValidateAgeResponse.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new DojahExceptions(e.getMessage());
        }
    }

    public ValidatePhoneAdvanceResponse validatePhoneNumberAdvance(ValidatePhoneRequest validatePhoneRequest) throws Exception{
        String urlToCall = dojahProp.getUrl()+"phone_number?";
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("phone_number", validatePhoneRequest.getPhoneNumber());
        String encodedURL = encodeUrl(urlToCall, requestParams);
        try  {
            String result =  dojahApiService.get(encodedURL);
            log.info("result  ......{}", result);
            DojahReponse dojahReponse = gson.fromJson(result , DojahReponse.class);
            JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
            if(!Objects.isNull(dojahReponse.getEntity())){
                return gson.fromJson(jsonObject.get("entity") , ValidatePhoneAdvanceResponse.class);
            }else{
                return gson.fromJson(result, ValidatePhoneAdvanceResponse.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new DojahExceptions(e.getMessage());
        }
    }

    public ValidatePhoneBasicResponse validatePhoneNumberBasic(ValidatePhoneRequest validatePhoneRequest) throws Exception{
        String urlToCall = dojahProp.getUrl()+"phone_number/basic?";
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("phone_number", validatePhoneRequest.getPhoneNumber());
        String encodedURL = encodeUrl(urlToCall, requestParams);
        try  {
            String result =  dojahApiService.get(encodedURL);
            log.info("result  ......{}", result);
            DojahReponse dojahReponse = gson.fromJson(result , DojahReponse.class);
            JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
            if(!Objects.isNull(dojahReponse.getEntity())){
                return gson.fromJson(jsonObject.get("entity") , ValidatePhoneBasicResponse.class);
            }else{
                return gson.fromJson(result, ValidatePhoneBasicResponse.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new DojahExceptions(e.getMessage());
        }
    }


    public ValidateBvnImageResponse validateBvnWithSelfie(ValidateImageRequest validateImageRequest) throws Exception{
        if (validateImageRequest.getSelfieImage().startsWith("data:image/jpeg;base64")){
            validateImageRequest.setSelfieImage(validateImageRequest.getSelfieImage().replace("data:image/jpeg;base64", ""));
        }
        String urlToCall = dojahProp.getUrl()+"bvn/verify?";
        JSONObject object = new JSONObject();
        object.put("selfie_image",validateImageRequest.getSelfieImage());
        object.put("bvn",validateImageRequest.getBvn());
        try  {
            String result =  dojahApiService.post(urlToCall, object.toString());
            log.info("result  ......{}", result);
            DojahReponse dojahReponse = gson.fromJson(result , DojahReponse.class);
            JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
            if(!Objects.isNull(dojahReponse.getEntity())){
                return gson.fromJson(jsonObject.get("entity") , ValidateBvnImageResponse.class);
            }else{
                return gson.fromJson(result, ValidateBvnImageResponse.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new DojahExceptions(e.getMessage());
        }
    }

    public ValidateNinImageResponse validateNinWithSelfie(ValidateImageRequest validateImageRequest) throws Exception{
        String urlToCall = dojahProp.getUrl()+"nin/verify?";
        JSONObject object = new JSONObject();
        object.put("selfie_image",validateImageRequest.getSelfieImage());
        object.put("nin",validateImageRequest.getNin());
        try  {
            String result =  dojahApiService.post(urlToCall,object.toString());
            log.info("result  ......{}", result);
            DojahReponse dojahReponse = gson.fromJson(result , DojahReponse.class);
            JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
            if(!Objects.isNull(dojahReponse.getEntity())){
                return gson.fromJson(jsonObject.get("entity") , ValidateNinImageResponse.class);
            }else{
                return gson.fromJson(result, ValidateNinImageResponse.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new DojahExceptions(e.getMessage());
        }
    }

    public ValidatePhotoIdResponse validatePhotoIdWithSelfie(ValidateImageRequest validateImageRequest) throws Exception{
        String urlToCall = dojahProp.getUrl()+"photoid/verify?";
        JSONObject object = new JSONObject();
        if(!StringUtils.isEmpty(validateImageRequest.getFirstName())){
            object.put("first_name",validateImageRequest.getFirstName());
        }if(!StringUtils.isEmpty(validateImageRequest.getLastName())){
            object.put("last_name",validateImageRequest.getLastName());
        }
        object.put("selfie_image",validateImageRequest.getSelfieImage());
        object.put("photoid_image",validateImageRequest.getPhotoImage());
        try  {
            String result =  dojahApiService.post(urlToCall, object.toString());
            log.info("result  ......{}", result);
            DojahReponse dojahReponse = gson.fromJson(result , DojahReponse.class);
            JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
            if(!Objects.isNull(dojahReponse.getEntity())){
                return gson.fromJson(jsonObject.get("entity") , ValidatePhotoIdResponse.class);
            }else{
                return gson.fromJson(result, ValidatePhotoIdResponse.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new DojahExceptions(e.getMessage());
        }
    }






    private String encodeValue(String value) throws UnsupportedEncodingException {
        return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
    }

    private String encodeUrl(String baseUrl, Map<String, String> requestParams) {
        return requestParams.keySet().stream()
                .map(key -> {
                    try {
                        return key + "=" + encodeValue(requestParams.get(key));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .collect(joining("&", baseUrl, ""));
    }

    public ValidateInternationalPassportResponse validateInternationalPassport(ValidateInternationalPassportRequest validateInternationalPassportRequest) {
        String url = dojahProp.getUrl()+"/passport?";
        Map<String, String > requestParams = new HashMap<>();
        requestParams.put("passport_number",validateInternationalPassportRequest.getPassportNumber());
        requestParams.put("surname",validateInternationalPassportRequest.getLastName());
        String encodedURL = encodeUrl(url, requestParams);
        try  {
            String result =  dojahApiService.get(encodedURL);
            log.info("result  ......{}", result);
            DojahReponse dojahReponse = gson.fromJson(result , DojahReponse.class);
            JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
            if(!Objects.isNull(dojahReponse.getEntity())){
                return gson.fromJson(jsonObject.get("entity") , ValidateInternationalPassportResponse.class);
            }else{
                return gson.fromJson(result, ValidateInternationalPassportResponse.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new DojahExceptions(e.getMessage());
        }
    }
}
