package com.expense.utils;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * I defined this util class as a Spring Bean to make it easier to Mock it in tests
 * Default Cipher: AES-128
 * Default Cipher mode: ECB
 * PKCS5Padding is also applied by default
 */
@Setter
@Component
public class EncryptionUtil {

    @Value("${login.encryption.key}")
    private String encryptionKey;

    @Value("${login.encryption.cipher}")
    private String encryptionCipher;

    public String encrypt(String plainText) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(encryptionKey.getBytes(), encryptionCipher);
        Cipher cipher = Cipher.getInstance(encryptionCipher);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

        byte[] encrypted = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public String decrypt(String encryptedText) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(encryptionKey.getBytes(), encryptionCipher);
        Cipher cipher = Cipher.getInstance(encryptionCipher);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decrypted);
    }
}
