package com.cqut.picquick.util;

import cn.hutool.crypto.digest.BCrypt;

/**
 * @author : HK意境
 * @ClassName : BcryptUtil
 * @date : 2021/9/14 12:44
 * @description : 加密工具类
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class BcryptUtil {
    public static String encode(String password){
        //对明文密码进行加密,并返回加密后的密码
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean match(String password, String encodePassword){
        //将明文密码跟加密后的密码进行匹配，如果一致返回true,否则返回false
        return BCrypt.checkpw(password,encodePassword);
    }

}
