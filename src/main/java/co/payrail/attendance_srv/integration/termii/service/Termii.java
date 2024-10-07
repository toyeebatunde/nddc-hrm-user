package co.payrail.attendance_srv.integration.termii.service;

import co.payrail.attendance_srv.integration.termii.model.TermiiSmsRequest;
import co.payrail.attendance_srv.integration.termii.model.TermiiSmsResponse;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class Termii {


    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();

    @Value("${termii.apiKey}")
    private String apiKey;
    @Value("${termii.From}")
    private String from;


    public TermiiSmsResponse sendSms(TermiiSmsRequest request) throws IOException{

        String urlToCall = "https://api.ng.termii.com/api/sms/send" ;

        request.setFrom(from);
        request.setApi_key(apiKey);
        String json = gson.toJson(request);
        log.info("request body for termii  ...{}", json);
        RequestBody body = RequestBody.create( MediaType.get("application/json; charset=utf-8"), json);
        Request smsRequest = new Request.Builder()
                .url(urlToCall )
                .post(body)
                .build();

        log.debug("url ......{}",urlToCall);

        try (Response response = client.newCall(smsRequest).execute()) {
            String result =  response.body().string();
            log.info("result  ......{}", result);
            return gson.fromJson(result , TermiiSmsResponse.class);
        }

    }

    @Async
    public void sendSmsTo(String phoneNumber, String otpCode) throws IOException {
        TermiiSmsRequest request = new TermiiSmsRequest();
        request.setTo(phoneNumber);
        request.setSms(String.format("Your OTP code is %s. It expires in 4 mins. Please do not disclose it to anyone",otpCode));
        sendSms(request);
    }

//    public static TermiiSmsResponse testSms(TermiiSmsRequest request) throws IOException{
//
//        String urlToCall = "https://termii.com/api/sms/send" ;
//        request.setFrom("N-Alert");
//        request.setApi_key("TLCDzx9vjrlfeCU6i4hMG0glQYN7E4pqUyQRyOmtBQwEe3b4nOskRTAnyTr8Yy");
//        OkHttpClient client = new OkHttpClient();
//        Gson gson = new Gson();
//
//
//        String json = gson.toJson(request);
//        log.info("request body for termii  ...{}", json);
//        RequestBody body = RequestBody.create( MediaType.get("application/json; charset=utf-8"), json);
//        Request smsRequest = new Request.Builder()
//                .url(urlToCall )
//                .post(body)
//                .build();
//
//        log.debug("url ......{}",urlToCall);
//
//        try (Response response = client.newCall(smsRequest).execute()) {
//            String result =  response.body().string();
//            log.info("result  ......{}", result);
//            return gson.fromJson(result , TermiiSmsResponse.class);
//        }
//
//    }
//
//    public static void main(String[] args) {
//        TermiiSmsRequest request = new TermiiSmsRequest();
//        request.setSms("Welcome to Angala Agency banking! use this code 000000 to complete your registration ");
//        request.setTo("+2348036277233");
//        try {
//            TermiiSmsResponse termiiSmsResponse = testSms(request);
//            System.out.println("response:" + termiiSmsResponse);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
