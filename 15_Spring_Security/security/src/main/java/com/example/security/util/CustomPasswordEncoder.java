package com.example.security.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

//Encoder with salt and hash
@Component
public class CustomPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        String salt = generateSalt();
        String hashedPassword = hash(rawPassword.toString(), salt);
        return salt + ":" + hashedPassword;
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String[] saltAndHash = encodedPassword.split(":");
        String salt = saltAndHash[0];
        String hashedPassword = hash(rawPassword.toString(), salt);
        return hashedPassword.equals(saltAndHash[1]);
    }

    private String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return byteToHex(salt);
    }

    private String hash(String password, String salt) {
        String saltedPassword = salt + password;
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password");
        }
        byte[] hashedPassword = digest.digest(saltedPassword.getBytes());
        return byteToHex(hashedPassword);
    }

    private String byteToHex(byte[] array) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : array) {
            hexString.append(String.format("%02X", b));
        }
        return hexString.toString();
    }


}
