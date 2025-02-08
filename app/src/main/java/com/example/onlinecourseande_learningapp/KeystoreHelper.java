package com.example.onlinecourseande_learningapp;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.security.KeyStore;
import java.util.Arrays;

public class KeystoreHelper {

    private static final String KEY_ALIAS = "my_key_alias";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";

    private KeyStore keyStore;

    public KeystoreHelper() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Generate and store the key in Keystore
    public void generateKey() throws Exception {
        if (!keyStore.containsAlias(KEY_ALIAS)) {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            keyGenerator.init(
                    new KeyGenParameterSpec.Builder(KEY_ALIAS, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                            .build());
            keyGenerator.generateKey();
        }
    }

    // Encrypt the password using Keystore
    public String encryptPassword(String password) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey());

        byte[] iv = cipher.getIV();
        byte[] encryption = cipher.doFinal(password.getBytes());

        // Combine the IV and encryption result, then encode it in Base64
        byte[] ivAndEncryptedPassword = new byte[iv.length + encryption.length];
        System.arraycopy(iv, 0, ivAndEncryptedPassword, 0, iv.length);
        System.arraycopy(encryption, 0, ivAndEncryptedPassword, iv.length, encryption.length);

        return Base64.encodeToString(ivAndEncryptedPassword, Base64.DEFAULT);
    }

    // Decrypt the password using Keystore
    public String decryptPassword(String encryptedPassword) throws Exception {
        byte[] ivAndEncryptedPassword = Base64.decode(encryptedPassword, Base64.DEFAULT);

        byte[] iv = Arrays.copyOfRange(ivAndEncryptedPassword, 0, 12); // 12 bytes for IV
        byte[] encryptedBytes = Arrays.copyOfRange(ivAndEncryptedPassword, 12, ivAndEncryptedPassword.length);

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), new GCMParameterSpec(128, iv));

        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        return new String(decryptedBytes);
    }

    // Get the secret key from Keystore
    private SecretKey getSecretKey() throws Exception {
        return (SecretKey) keyStore.getKey(KEY_ALIAS, null);
    }
}
