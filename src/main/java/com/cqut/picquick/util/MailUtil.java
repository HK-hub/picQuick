package com.cqut.picquick.util;

import com.cqut.picquick.bean.MailBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

/**
 * @author : HK意境
 * @ClassName : MailUtil
 * @date : 2021/9/15 22:19
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Component
public class MailUtil {
    @Value("${spring.mail.username}")
    private String MAIL_SENDER; //邮件发送者

    @Autowired
    private JavaMailSender javaMailSender;

    private Logger logger = LoggerFactory.getLogger(MailUtil.class);
    //验证码包含的字段，可自己设置
    private static final String SYMBOLS = "0123456789ABCDEFGHIGKLMNOPQRSTUVWXYZ";
    private static final Random RANDOM = new SecureRandom();
    /**
     * 发送文本邮件
     *
     * @param mailBean
     */
    public  boolean sendSimpleMail(MailBean mailBean) {
        try {
            SimpleMailMessage mailMessage= new SimpleMailMessage();
            mailMessage.setFrom(MAIL_SENDER);
            mailMessage.setTo(mailBean.getRecipient());
            mailMessage.setSubject(mailBean.getSubject());
            mailMessage.setText(mailBean.getContent());
            //mailMessage.copyTo(copyTo);
            javaMailSender.send(mailMessage);

        } catch (Exception e) {
            logger.error("send mail failed", e.getMessage());
            return false ;
        }
        return true ;
    }
    
    
    /**
     * @methodName : 发送邮箱验证码
     * @author : HK意境
     * @date : 2021/9/15 23:24
     * @description :生成 6 位数的随机数字
     * @Todo :
     * @params : 
         * @param : null 
     * @return : null
     * @throws: 
     * @Bug :
     * @Modified :
     * @Version : 1.0
     */
    public static String generateVerCode() {
        //	如果是六位，就生成大小为 6 的数组
        char[] numbers = new char[6];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
        }
        return new String(numbers);
    }
    
    
    
}