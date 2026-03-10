package com.itwray.iw.auth.utils;

import java.security.SecureRandom;

/**
 * 家庭组工具类
 *
 * @author wray
 * @since 2024-03-10
 */
public class FamilyGroupUtils {

    /**
     * 邀请码字符集（排除易混淆字符：0,O,1,I,L）
     */
    private static final String INVITE_CODE_CHARS = "23456789ABCDEFGHJKMNPQRSTUVWXYZ";

    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * 生成8位邀请码
     *
     * @return 邀请码
     */
    public static String generateInviteCode() {
        StringBuilder code = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            int index = RANDOM.nextInt(INVITE_CODE_CHARS.length());
            code.append(INVITE_CODE_CHARS.charAt(index));
        }
        return code.toString();
    }

    /**
     * 验证邀请码格式是否正确
     *
     * @param inviteCode 邀请码
     * @return true-格式正确
     */
    public static boolean isValidInviteCodeFormat(String inviteCode) {
        if (inviteCode == null || inviteCode.length() != 8) {
            return false;
        }
        for (char c : inviteCode.toCharArray()) {
            if (INVITE_CODE_CHARS.indexOf(c) == -1) {
                return false;
            }
        }
        return true;
    }
}
