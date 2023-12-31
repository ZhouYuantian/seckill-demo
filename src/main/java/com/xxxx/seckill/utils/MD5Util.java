package com.xxxx.seckill.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

@Component
public class MD5Util {
    public static String md5(String src)
    {
        return DigestUtils.md5Hex(src);
    }

    private static final String salt="1a2b3c4d";

    public static String inputPassToFormPass(String inputPass)
    {
        String str=""+salt.charAt(0)+salt.charAt(2)+inputPass+salt.charAt(5)+salt.charAt(4);
        return md5(str);
    }

    public static String formPassToDbPass(String formPass,String salt)
    {
        String str=salt.charAt(2)+formPass+salt.charAt(5);
        return md5(str);
    }

    public static String inputPassToDbPass(String inputPass)
    {
        String formPass=inputPassToFormPass(inputPass);
        String dbPass=formPassToDbPass(formPass,salt);
        return dbPass;
    }
    //123456 -> d3b1294a61a07da9b49b6e22b2cbd7f9 -> 8327fb5098c33ec41899323ea8920162

}
