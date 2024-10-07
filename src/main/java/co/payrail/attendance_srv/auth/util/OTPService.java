package co.payrail.attendance_srv.auth.util;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.timestreamwrite.model.TimeUnit;

import java.time.Duration;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.*;
import java.util.concurrent.ExecutionException;


@Service
@AllArgsConstructor
@Slf4j
public class OTPService {
    //TODO save this into a configuration file
    private String expirationTime = "240";
    private LoadingCache<String, Integer> otpCache;
    public OTPService(){
        super();
        otpCache = CacheBuilder.newBuilder()
//                .expireAfterWrite(Duration.ofDays(Integer.parseInt(expirationTime)))
                .maximumSize(1000L)
                .build(new CacheLoader<String, Integer>() {
                    public @NonNull Integer load(@NonNull String key) {
                        return getOtp(key);
                    }
                });
    }

    public int generateOTP(String key){
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        otpCache.put(key, otp);
        return otp;
    }

    public int getOtp(String key){
        try{
            otpCache.refresh(key);
            return otpCache.get(key);
        }catch (Exception e){
            return 0;
        }
    }

    public void clearOTP(String key){
        otpCache.invalidate(key);
    }

    public boolean validateOtp(String key, String otp) {
        if (otpCache == null) {
            System.out.println("Cache is not initialized.");
        }


        String cachedOtp = null;
        try {
            cachedOtp = String.valueOf(otpCache.get(key));

            return otp.equals(cachedOtp);
        } catch (ExecutionException e) {

            log.info("Exception --> {}", e.getMessage());
            return false;
        }
    }
}