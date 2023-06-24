package com.pxxy.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {
    public static String code(String str) {
        if (str == null) return null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[] byteDigest = md.digest();
            int i;
            StringBuilder buf = new StringBuilder("");
            for (byte b : byteDigest) {
                i = b;
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            //32位加密(小写)
            return buf.toString();
            //32位加密(大写)
            //return buf.toString().toUpperCase();
            // 16位的加密(小写)
            //return buf.toString().substring(8, 24);
            // 16位的加密(大写)
            //return buf.toString().substring(8, 24).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.exit(1);
            return "";
        }

    }

    public static void main(String[] args) {
    }
}