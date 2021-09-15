package com.cqut.picquick.common.constant;

/**
 * @author : HK意境
 * @ClassName : QQConstant
 * @date : 2021/9/15 15:48
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class QQConstant {

    /**
     * 登录处理回调地址，可自定义，此时是为了演示方便，实际开发不应该放在常量类中，应放在配置文件
     */
    public static final String CALLBACK_URL ="http://127.0.0.1:8080/picquick/user/qqCallback";

    /**
     * 自己的appID和appKey
     */
    public static final String APP_ID = "自己的APP-ID";
    public static final String APP_KEY = "自己的APP-KEY";

    /**
     * 获取Authorization Code
     */
    public static final String GET_AUTHORIZATION_CODE = "https://graph.qq.com/oauth2.0/authorize";

    /**
     * 通过Authorization Code获取Access Token
     */
    public static final String GET_ACCESS_TOKEN = "https://graph.qq.com/oauth2.0/token";

    /**
     * 获取用户openId
     */
    public static final String GET_OPEN_ID = "https://graph.qq.com/oauth2.0/me";

    /**
     * 获取用户信息
     */
    public static final String GET_USER_INFO = "https://graph.qq.com/user/get_user_info";
}



