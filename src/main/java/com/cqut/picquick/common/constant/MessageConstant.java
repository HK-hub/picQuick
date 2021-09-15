package com.cqut.picquick.common.constant;

import org.apache.http.client.utils.DateUtils;

import java.util.Date;

/**
 * @author : HK意境
 * @ClassName : MessageConstant
 * @date : 2021/9/15 23:03
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class MessageConstant {

    public static final String loginSubject = "picQuick欢迎您登录,已恭候多时" ;


    public static String getLoginSubject(String username, String authCode){
        StringBuilder sb = new StringBuilder();
        sb.append("<h2>picQuick 图床系统登录验证:</h2>")
                .append("<p style='text-align:left'>"+"尊敬的: " + username +"! 以下是你的登录验证码: ")
                .append("<span style='color:skyblue'>" + authCode)
                .append("</span>")
                .append("   本验证码 5 分钟内效，请及时输入。（请勿泄露此验证码）如非本人操作，请忽略该邮件。(这是一封通过自动发送的邮件，请不要直接回复）")
                .append(" </p>")
                .append("<p> 时间为："+ DateUtils.formatDate(new Date()) +"</p>");

        return sb.toString() ;
    }


}
