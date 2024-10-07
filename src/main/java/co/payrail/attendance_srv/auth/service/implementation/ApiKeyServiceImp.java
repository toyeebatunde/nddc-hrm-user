package co.payrail.attendance_srv.auth.service.implementation;

import co.payrail.attendance_srv.auth.entity.ApiKey;
import co.payrail.attendance_srv.auth.entity.User;
import co.payrail.attendance_srv.auth.repository.ApiKeyRepository;
import co.payrail.attendance_srv.auth.service.UserService;
import co.payrail.attendance_srv.auth.service.ApiKeyService;
import co.payrail.attendance_srv.auth.util.EncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.UUID;

@Service
public class ApiKeyServiceImp implements ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;

    private final UserService userService;

    private final String encryptionKey = "XtQElQCdY9Hed1o1z/SG5yLC5R/hLIlZ49giFSpT9Is="; // Securely store and retrieve this key

    @Autowired
    public ApiKeyServiceImp(ApiKeyRepository apiKeyRepository, UserService userService) {
        this.apiKeyRepository = apiKeyRepository;
        this.userService = userService;
    }

    @Override
    public ApiKey generateApiKey(String username) {
        Optional<User> user =  userService.findByUserName(username);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }

        // Deactivate existing API keys for the user
        apiKeyRepository.deactivateApiKeys(user.get().getId());

        // Create new API key
        ApiKey newApiKey = new ApiKey();
        String plainApiKey = generateNewKey();
        try {
            String encryptedApiKey = EncryptionUtil.encrypt(plainApiKey, encryptionKey);
            newApiKey.setKey(encryptedApiKey);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting API key", e);
        }
        newApiKey.setUser((User) user.get());
        newApiKey.setDeleted(false);

        // Save new API key to the repository
        ApiKey apiKeyObject = apiKeyRepository.save(newApiKey);

        // Return response with the new API key (plain text)
        apiKeyObject.setKey(plainApiKey); // Ensure the returned object has the plain API key
        return apiKeyObject;
    }

    @Override
    public ApiKey findByKey(String key) {
        try {
            String encryptedKey = EncryptionUtil.encrypt(key, encryptionKey);
            return apiKeyRepository.findByKeyAndDeletedFalse(encryptedKey);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting API key for search", e);
        }
    }

    public void deleteByKey(Long key) {
        apiKeyRepository.deactivateApiKeys(key);
    }

    public String generateNewKey() {
        SecureRandom random = new SecureRandom();
        return new UUID(random.nextLong(), random.nextLong()).toString();
    }
}
