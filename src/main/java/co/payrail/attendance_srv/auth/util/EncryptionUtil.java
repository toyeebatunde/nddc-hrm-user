package co.payrail.attendance_srv.auth.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class EncryptionUtil {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";

    // Generate a new AES key (use this method once and save the key securely)
    public static String generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
        keyGen.init(256); // for AES-256
        SecretKey secretKey = keyGen.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    // Encrypt the plain text using the provided key
    public static String encrypt(String plainText, String encryptionKey) throws Exception {
        try {
            System.out.println("Encryption Key: " + encryptionKey);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            SecretKeySpec keySpec = new SecretKeySpec(Base64.getDecoder().decode(encryptionKey), ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid Base64 encryption key: " + encryptionKey);
            throw e;
        }
    }


    // Decrypt the cipher text using the provided key
    public static String decrypt(String cipherText, String encryptionKey) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        SecretKeySpec keySpec = new SecretKeySpec(Base64.getDecoder().decode(encryptionKey), ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(cipherText));
        return new String(decryptedBytes);
    }
}
