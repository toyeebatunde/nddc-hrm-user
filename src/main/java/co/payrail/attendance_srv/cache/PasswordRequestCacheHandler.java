package co.payrail.attendance_srv.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@Component
public class  PasswordRequestCacheHandler {

    private final Map<String, String> resetCache = new HashMap<>();

    public void addToCache(String key,String value){
        resetCache.put(key,value);
    }

    public void removeCache(String key) {
        resetCache.remove(key);
    }

    public String getFromCache(String key){
        return resetCache.get(key);
    }
}
