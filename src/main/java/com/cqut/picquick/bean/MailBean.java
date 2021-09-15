package com.cqut.picquick.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : HK意境
 * @ClassName : MailBean
 * @date : 2021/9/15 22:17
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
public class MailBean implements Serializable {

    private static final long serialVersionUID = -2116367492649751914L;
    //邮件接收人
    private String recipient;
    //邮件主题
    private String subject;
    //邮件内容
    private String content;


}
