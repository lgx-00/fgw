package com.pxxy.utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.math.BigInteger;
import java.security.MessageDigest;

public class MD5 {

    public static String md5(String dateString) throws Exception {
        MessageDigest md5 = null;
        byte[] digest = MessageDigest.getInstance("md5").digest(dateString.getBytes("utf-8"));
        String md5code = new BigInteger(1, digest).toString(16);
        // 如果生成数字未满32位，需要前面补0
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }

    public static String md5PlusSalt(String keyword)
    {
		//md5加密
        String md5 = DigestUtils.md5Hex(keyword);
		//md5+盐
        char[] cArray = md5.toCharArray();
        for(int i = 0;i < cArray.length; i++)
        {
            if(cArray[i] >= 48 &&cArray[i] <= 57)
            {
                cArray[i] = (char)(105-cArray[i]);

            }
        }
        return String.valueOf(cArray);

    }
	//解密+盐
    public static String md5MinusSalt(String md5)
    {
        char[] cArray=md5.toCharArray();
        for(int i=0;i<cArray.length;i++)
        {
            if(cArray[i]>=48&&cArray[i]<=57)
            {
                cArray[i]=(char)(105-cArray[i]);
            }
        }
        return String.valueOf(cArray);
    }
}