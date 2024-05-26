package com.example.onetimemessage.onetimemessage.service;

import com.example.onetimemessage.onetimemessage.config.Config;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class EncryptionService {
    private final Config config;
    private final String AES_ALGORITHM = "AES";
    private final String AES_TRANSFORMATION = "AES/GCM/NoPadding";
    private final int KEY_SIZE_BITS = 128;
    private final int GCM_NONCE_LENGTH_BYTES = 12;
    private final String CHARSET_NAME = "UTF-8";
    private byte[] SECRET_KEY_MESSAGE_BODY_SALT_AS_BYTES;

    public EncryptionService(Config config) {
        this.config = config;
        this.SECRET_KEY_MESSAGE_BODY_SALT_AS_BYTES = Base64.getDecoder().decode(this.config.getMessageBodyEncryptionSalt());
    }

    public String encrypt(String textForEncryption, SecretKey secretKey) throws Exception {
        var cipher = Cipher.getInstance(AES_TRANSFORMATION);
        var nonce = this.generateNonce();

        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new GCMParameterSpec(KEY_SIZE_BITS, nonce));

        var encryptedBytes = cipher.doFinal(textForEncryption.getBytes(CHARSET_NAME));
        var combined = new byte[GCM_NONCE_LENGTH_BYTES + encryptedBytes.length];

        System.arraycopy(nonce, 0, combined, 0, GCM_NONCE_LENGTH_BYTES);
        System.arraycopy(encryptedBytes, 0, combined, GCM_NONCE_LENGTH_BYTES, encryptedBytes.length);

        return Base64.getEncoder().encodeToString(combined);
    }

    public String decrypt(String textForDecryption, SecretKey secretKey) throws Exception {
        var combined = Base64.getDecoder().decode(textForDecryption);
        var nonce = new byte[GCM_NONCE_LENGTH_BYTES];
        var encryptedBytes = new byte[combined.length - GCM_NONCE_LENGTH_BYTES];

        System.arraycopy(combined, 0, nonce, 0, GCM_NONCE_LENGTH_BYTES);
        System.arraycopy(combined, GCM_NONCE_LENGTH_BYTES, encryptedBytes, 0, combined.length - GCM_NONCE_LENGTH_BYTES);

        var cipher = Cipher.getInstance(AES_TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(KEY_SIZE_BITS, nonce));

        var decryptedBytes = cipher.doFinal(encryptedBytes);

        return new String(decryptedBytes, CHARSET_NAME);
    }

    private byte[] generateNonce() {
        var secureRandom = new SecureRandom();
        var nonce = new byte[GCM_NONCE_LENGTH_BYTES];

        secureRandom.nextBytes(nonce);

        return nonce;
    }

    public SecretKey generateSecretKey() throws Exception {
        var keyGenerator = KeyGenerator.getInstance(this.AES_ALGORITHM);

        keyGenerator.init(KEY_SIZE_BITS);

        return keyGenerator.generateKey();
    }

    public SecretKey getSecretKeyWithSalt(SecretKey secretKey) {
        var secretKeyInBytes = secretKey.getEncoded();
        var finalKey = new byte[SECRET_KEY_MESSAGE_BODY_SALT_AS_BYTES.length + secretKeyInBytes.length];

        System.arraycopy(secretKeyInBytes, 0, finalKey, 0, secretKeyInBytes.length);
        System.arraycopy(SECRET_KEY_MESSAGE_BODY_SALT_AS_BYTES, 0, finalKey, secretKeyInBytes.length, SECRET_KEY_MESSAGE_BODY_SALT_AS_BYTES.length);

        return new SecretKeySpec(finalKey, 0, finalKey.length, this.AES_ALGORITHM);
    }
}
