package com.cqut.picquick;

/**
 * @author : HK意境
 * @ClassName : JavaMailSendTest
 * @date : 2021/9/15 22:21
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */


import com.cqut.picquick.bean.MailBean;
import com.cqut.picquick.util.MailUtil;

import org.apache.http.client.utils.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringJUnitConfig
@SpringBootTest
public class JavaMailSendTest {

    @Autowired
    private MailUtil mailUtil;

    //@Autowired
    //private TemplateEngine templateEngine;

    //接收人
    private static final String RECIPINET = "hk3161880795@126.com";

    /**
     * 发送文本邮件
     */
    @Test
    public void sendSimpleMail() {
        MailBean mailBean = new MailBean();
        mailBean.setRecipient(RECIPINET);
        mailBean.setSubject("SpringBootMail之这是一封文本的邮件");
        mailBean.setContent("SpringBootMail发送一个简单格式的邮件，时间为：" + DateUtils.formatDate(new Date()));

        mailUtil.sendSimpleMail(mailBean);
    }




}
