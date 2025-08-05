package com.itwray.iw.common.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * 签名工具
 */
public class SignatureUtil {

    public static void main(String[] args) {
        String key = generateSignature("1", "2", "3", "4");
        System.out.println(key);
    }

    public static String generateSignature(String appKey, String timestamp, String path, String secret) {
        String signString = appKey + timestamp + path;
        return hmacSHA256(signString, secret);
    }

    private static String hmacSHA256(String data, String secret) {
        try {
            Mac sha256Hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256Hmac.init(secretKey);
            byte[] hash = sha256Hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (Exception e) {
            throw new RuntimeException("HMAC SHA256 calculation error", e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        for (byte b : bytes) {
            hex.append(String.format("%02x", b));
        }
        return hex.toString();
    }
}
