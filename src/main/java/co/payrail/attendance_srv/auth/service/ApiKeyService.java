package co.payrail.attendance_srv.auth.service;


import co.payrail.attendance_srv.auth.entity.ApiKey;

public interface ApiKeyService {
    ApiKey generateApiKey(String username);

    ApiKey findByKey(String key);

    String generateNewKey();
}
