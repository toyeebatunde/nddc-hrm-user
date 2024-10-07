package co.payrail.attendance_srv.kyc_srv.controller;

import co.payrail.attendance_srv.kyc_srv.dto.BvnVerificationRequest;
import co.payrail.attendance_srv.kyc_srv.dto.CACVerificationRequest;
import co.payrail.attendance_srv.kyc_srv.dto.VNINVerificationRequest;
import co.payrail.attendance_srv.kyc_srv.service.KycService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/kyc")
public class KycController {
    public final KycService kycService;

    public KycController(KycService kycService) {
        this.kycService = kycService;
    }

    @PostMapping("/verify-bvn")
    public ResponseEntity<?> verifyBVN(@RequestBody  BvnVerificationRequest bvnVerificationRequest){

        Map<String, String> response = kycService.verifyBvnAndUpdateUserDetails(bvnVerificationRequest.getBvn(), bvnVerificationRequest.getUserName());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/verify-vnin")
    public ResponseEntity<?> verifyVirtualNIN( @RequestBody VNINVerificationRequest vninVerificationRequest){

        Map<String, String> response = kycService.verifyVirtualNINAndUpdateUserDetails(vninVerificationRequest.getVnin(), vninVerificationRequest.getUserName());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/verify-cac")
    public ResponseEntity<?> verifyCac(@RequestBody CACVerificationRequest dto ) throws Exception {
        String response = kycService.verifyCac(dto.getRcNumber(), dto.getCompanyName(), dto.getUserName());
        if (response.equalsIgnoreCase("Licence found!")){
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }


}
