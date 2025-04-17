package com.shoppingmall.demo.utils;

import java.security.MessageDigest;
import java.util.Objects;
import java.util.Random;

@SuppressWarnings("ALL")
public class MD5Util {
    public MD5Util() {
    }
    public static String generate(String password) {
        Random r = new Random();
        StringBuilder strBu = new StringBuilder(16);
        strBu.append(r.nextInt(99999999)).append(r.nextInt(99999999));
        int len = strBu.length();
        if (len < 16) {
            for (int i = 0; i < 16 - len; ++i) {
                strBu.append("0");
            }
        }

        String salt = strBu.toString();
        password = md5Hex(password + salt);
        char[] cs = new char[48];

        for (int i = 0; i < 48; i += 3) {
            cs[i] = password.charAt(i / 3 * 2);

            char c = salt.charAt(i / 3);
            cs[i + 1] = c;
            cs[i + 2] = password.charAt(i / 3 * 2 + 1);
        }

        return new String(cs);
    }

    public static boolean verify(String password, String md5) {
        char[] cs1 = new char[32];
        char[] cs2 = new char[16];

        for (int i = 0; i < 48; i += 3) {
            cs1[i / 3 * 2] = md5.charAt(i);
            cs1[i / 3 * 2 + 1] = md5.charAt(i + 2);
            cs2[i / 3] = md5.charAt(i + 1);
        }

        String salt = new String(cs2);
        return Objects.equals(md5Hex(password + salt), new String(cs1));
    }

    private static String md5Hex(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(str.getBytes());
            return hex(digest);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
            return "";
        }
    }

    private static String hex(byte[] arr) {
        StringBuffer strBuf = new StringBuffer();
        for (byte b : arr) {
            strBuf.append(Integer.toHexString((b & 0xFF) | 0x100), 1, 3);
        }
        return strBuf.toString();
    }
}
