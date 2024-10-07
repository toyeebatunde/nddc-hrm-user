package co.payrail.attendance_srv.integration.service;

import co.payrail.attendance_srv.integration.dojah.model.age_verification.ValidateAgeRequest;
import co.payrail.attendance_srv.integration.dojah.model.age_verification.ValidateAgeResponse;
import co.payrail.attendance_srv.integration.dojah.model.lookupbvn.LookupBvnResponse;
import co.payrail.attendance_srv.integration.dojah.model.validateImage.ValidateImageRequest;
import co.payrail.attendance_srv.integration.dojah.model.validateImage.validatebvnimage.ValidateBvnImageResponse;
import co.payrail.attendance_srv.integration.dojah.model.validateImage.validateninimage.ValidateNinImageResponse;
import co.payrail.attendance_srv.integration.dojah.model.validateImage.validatephotoId.ValidatePhotoIdResponse;
import co.payrail.attendance_srv.integration.dojah.model.validatecac.ValidateCACRequest;
import co.payrail.attendance_srv.integration.dojah.model.validatecac.ValidateCacResponse;
import co.payrail.attendance_srv.integration.dojah.model.validatedl.ValidateDlRequest;
import co.payrail.attendance_srv.integration.dojah.model.validatedl.ValidateDlResponse;
import co.payrail.attendance_srv.integration.dojah.model.validatenin.ValidateNinRequest;
import co.payrail.attendance_srv.integration.dojah.model.validatenin.ValidateNinResponse;
import co.payrail.attendance_srv.integration.dojah.model.validatetin.ValidateTinRequest;
import co.payrail.attendance_srv.integration.dojah.model.validatetin.ValidateTinResponse;
import co.payrail.attendance_srv.integration.dojah.model.validatevin.ValidateVinRequest;
import co.payrail.attendance_srv.integration.dojah.model.validatevin.ValidateVinResponse;
import co.payrail.attendance_srv.integration.dojah.model.virtual_nin.ValidateVirtualNINRequest;
import co.payrail.attendance_srv.integration.dojah.model.virtual_nin.ValidateVirtualNINResponse;
import co.payrail.attendance_srv.integration.models.request.DepositRequest;
import co.payrail.attendance_srv.integration.service.dto.kyc.BranchlessBvnEnquiryRequest;
import co.payrail.attendance_srv.integration.service.dto.kyc.BranchlessNameEnquiryRequest;
import co.payrail.attendance_srv.integration.service.dto.kyc.BranchlessNameEnquiryResponse;


import java.util.List;

public interface IntegrationService {

    BranchlessNameEnquiryResponse nameEnquiry(BranchlessNameEnquiryRequest request);

    LookupBvnResponse bvn(BranchlessBvnEnquiryRequest request);

    ValidateNinResponse nin(ValidateNinRequest request);

    ValidateVirtualNINResponse validateVirtualNIN(ValidateVirtualNINRequest request);

    ValidateTinResponse tin(ValidateTinRequest request);

    ValidateVinResponse vin(ValidateVinRequest request);

    ValidateDlResponse drl(ValidateDlRequest request);

    ValidateCacResponse cac(ValidateCACRequest request);

    ValidateAgeResponse age(ValidateAgeRequest request);

    ValidateBvnImageResponse bvnSelfie(ValidateImageRequest request);

    ValidateNinImageResponse ninSelfie(ValidateImageRequest request);

    ValidatePhotoIdResponse photoId(ValidateImageRequest request);

    void sendSms(String phoneNumber , String message) throws  NullPointerException;


}
