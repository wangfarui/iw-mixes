package com.itwray.iw.common.utils;

import cn.hutool.crypto.KeyUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import com.itwray.iw.common.IwException;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * AES 加密工具类
 *
 * @author wray
 * @since 2025/3/11
 */
public abstract class AESUtils {

    /**
     * 生成密钥 SecretKey
     */
    public static SecretKey generateSecretKey(String aesKey) {
        String fullAesKey = StringUtils.rightPad(aesKey, 32, "*");
        byte[] secretByte = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue(), fullAesKey.getBytes(StandardCharsets.UTF_8)).getEncoded();
        return KeyUtil.generateKey(SymmetricAlgorithm.AES.getValue(), secretByte);
    }

    /**
     * AES-GCM 加密（IV 不需要存储）
     * <p>该算法加密后的数据会在明文上额外增加28个字节长度</p>
     */
    public static String encryptAESGCM(SecretKey key, String plaintext) {
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            byte[] iv = new byte[12]; // 12 字节 IV（GCM 推荐）
            new SecureRandom().nextBytes(iv);
            GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);

            cipher.init(Cipher.ENCRYPT_MODE, key, gcmSpec);
            byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes());

            byte[] combined = new byte[iv.length + encryptedBytes.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(encryptedBytes, 0, combined, iv.length, encryptedBytes.length);

            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception e) {
            // ignore
            // 加密异常
            throw new IwException(e);
        }
    }

    /**
     * AES-GCM 解密
     */
    public static String decryptAESGCM(SecretKey key, String encryptedData) {
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            byte[] combined = Base64.getDecoder().decode(encryptedData);

            byte[] iv = new byte[12]; // 取前 12 字节 IV
            byte[] ciphertext = new byte[combined.length - 12];
            System.arraycopy(combined, 0, iv, 0, iv.length);
            System.arraycopy(combined, iv.length, ciphertext, 0, ciphertext.length);

            GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);
            cipher.init(Cipher.DECRYPT_MODE, key, gcmSpec);

            byte[] decryptedBytes = cipher.doFinal(ciphertext);
            return new String(decryptedBytes);
        } catch (Exception e) {
            // ignore
            // 解密异常
            throw new IwException(e);
        }
    }

    /**
     * 判断加密数据是否为 AesGcm 加密后的数据
     */
    public static boolean isAesGcmEncrypted(SecretKey key, String encryptedData) {
        try {
            byte[] combined = Base64.getDecoder().decode(encryptedData);
            byte[] iv = new byte[12]; // 取前 12 字节 IV
            byte[] ciphertext = new byte[combined.length - 12];
            System.arraycopy(combined, 0, iv, 0, iv.length);
            System.arraycopy(combined, iv.length, ciphertext, 0, ciphertext.length);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);
            cipher.init(Cipher.DECRYPT_MODE, key, gcmSpec);
            cipher.doFinal(ciphertext); // 试图解密

            // 解密成功，说明是有效 AES-GCM 密文
            return true;
        } catch (Exception e) {
            // 解密失败，说明不是 AES-GCM 加密数据
            return false;
        }
    }
}
