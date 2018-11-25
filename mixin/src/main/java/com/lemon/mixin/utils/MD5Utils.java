package com.lemon.mixin.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @ Author     : AnxiangLemon
 * @ Date       : Created in 0:15 2018/10/24
 * @ Description: this is class
 */
public class MD5Utils {
    private static final String MD5_KEY = "lemon";
    /**
     * MD5方法
     *
     * @param text 明文
     * @return 密文
     * @throws Exception
     */
    public static String md5(String text) {
        //加密后的字符串
        String encodeStr= DigestUtils.md5Hex(text + MD5_KEY);
        System.out.println("MD5加密后的字符串为:encodeStr="+encodeStr);
        return encodeStr;
    }

    /**
     * MD5验证方法
     *
     * @param text 明文
     * @param md5 密文
     * @return true/false
     * @throws Exception
     */
    public static boolean verify(String text, String md5) {
        //根据传入的密钥进行验证
        String md5Text = md5(text);
        if(md5Text.equalsIgnoreCase(md5))
        {
            System.out.println("MD5验证通过");
            return true;
        }

        return false;
    }
}
