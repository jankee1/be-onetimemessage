package com.example.onetimemessage.onetimemessage.service;

import com.example.onetimemessage.onetimemessage.Config;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class EncryptionService {
    private static final Config CONFIG = new Config();
    private static final String AES_ALGORITHM = "AES";
    private static final String AES_TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int KEY_SIZE_BITS = 128;
    private static final int GCM_NONCE_LENGTH_BYTES = 12;
    private static final String CHARSET_NAME = "UTF-8";
    private static final byte[] SECRET_KEY_MESSAGE_BODY_SALT_AS_BYTES = Base64.getDecoder().decode(CONFIG.getMESSAGE_BODY_ENCRYPTION_SALT());

    public static String encrypt(String textForEncryption, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
        byte[] nonce = generateNonce();

        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new GCMParameterSpec(KEY_SIZE_BITS, nonce));

        byte[] encryptedBytes = cipher.doFinal(textForEncryption.getBytes(CHARSET_NAME));
        byte[] combined = new byte[GCM_NONCE_LENGTH_BYTES + encryptedBytes.length];

        System.arraycopy(nonce, 0, combined, 0, GCM_NONCE_LENGTH_BYTES);
        System.arraycopy(encryptedBytes, 0, combined, GCM_NONCE_LENGTH_BYTES, encryptedBytes.length);

        return Base64.getEncoder().encodeToString(combined);
    }

    public static String decrypt(String textForDecryption, SecretKey secretKey) throws Exception {
        byte[] combined = Base64.getDecoder().decode(textForDecryption);
        byte[] nonce = new byte[GCM_NONCE_LENGTH_BYTES];
        byte[] encryptedBytes = new byte[combined.length - GCM_NONCE_LENGTH_BYTES];

        System.arraycopy(combined, 0, nonce, 0, GCM_NONCE_LENGTH_BYTES);
        System.arraycopy(combined, GCM_NONCE_LENGTH_BYTES, encryptedBytes, 0, combined.length - GCM_NONCE_LENGTH_BYTES);

        Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(KEY_SIZE_BITS, nonce));

        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        return new String(decryptedBytes, CHARSET_NAME);
    }

    private static byte[] generateNonce() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] nonce = new byte[GCM_NONCE_LENGTH_BYTES];
        secureRandom.nextBytes(nonce);
        return nonce;
    }

    static SecretKey generateSecretKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(AES_ALGORITHM);
        keyGenerator.init(KEY_SIZE_BITS);
        return keyGenerator.generateKey();
    }

    static SecretKey getSecretKeyWithSalt(SecretKey secretKey) {
        byte[] secretKeyInBytes = secretKey.getEncoded();
        byte[] finalKey = new byte[SECRET_KEY_MESSAGE_BODY_SALT_AS_BYTES.length + secretKeyInBytes.length];
        System.arraycopy(secretKeyInBytes, 0, finalKey, 0, secretKeyInBytes.length);
        System.arraycopy(SECRET_KEY_MESSAGE_BODY_SALT_AS_BYTES, 0, finalKey, secretKeyInBytes.length, SECRET_KEY_MESSAGE_BODY_SALT_AS_BYTES.length);
        return new SecretKeySpec(finalKey, 0, finalKey.length, AES_ALGORITHM);
    }
}
