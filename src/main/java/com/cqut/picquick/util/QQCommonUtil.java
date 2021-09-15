package com.cqut.picquick.util;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.cqut.picquick.common.CustomToken;
import com.cqut.picquick.entity.other.Me;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;

/**
 * @author : HK意境
 * @ClassName : QQCommonUtil
 * @date : 2021/9/15 15:57
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class QQCommonUtil {


    private static Logger log = LoggerFactory.getLogger(QQCommonUtil.class);

    //获取Authorization Code
    public final static String auth_url = "https://graph.qq.com/oauth2.0/authorize?response_type=code&client_id=APPID&redirect_uri=REDIRECTURL&&state=STATE";
    // 凭证获取（GET）
    public final static String token_url = "https://graph.qq.com/oauth2.0/token?grant_type=authorization_code&client_id=APPID&client_secret=APPSECRET&code=CODE&redirect_uri=REDIRECTURL";
    //权限自动续期，获取Access Token
    public final static String refresh_token_url = "https://graph.qq.com/oauth2.0/token?grant_type=refresh_token&client_id=APPID&client_secret=APPSECRET&refresh_token=REFRESHTOKEN";
    //获取用户OpenID_OAuth2.0
    public final static String oauth_url = "https://graph.qq.com/oauth2.0/me?access_token=ACCESSTOKEN";
    //获取登录用户的昵称、头像、性别
    public final static String user_info_url = "https://graph.qq.com/user/get_user_info?access_token=ACCESSTOKEN&oauth_consumer_key=APPID&openid=OPENID";


    /**
     * 发送https请求
     *
     * @param requestUrl 请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr 提交的数据
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
     */
    public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
        String result = null;
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = { new CustomX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(ssf);

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            conn.setRequestMethod(requestMethod);

            // 当outputStr不为null时向输出流写数据
            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // 从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }

            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            conn.disconnect();
            result = buffer.toString();
        } catch (ConnectException ce) {
            log.error("连接超时：{}", ce);
        } catch (Exception e) {
            log.error("https请求异常：{}", e);
        }
        return result;
    }

    /**
     *  获取Authorization Code
     * @param appid
     * @param redirect_url
     * @param state
     * @return
     */
    public static String getCode(String appid, String redirect_url, String state){
        String resUrl = auth_url.replace("APPID",appid).replace("REDIRECTURL",redirect_url).replace("STATE",state);
        return resUrl;
    }

    /**
     * 获取接口访问凭证
     * @param appid
     * @param appsecret
     * @param code
     * @param redirect_url
     * @return
     */
    public static CustomToken getToken(String appid, String appsecret, String code, String redirect_url) {
        CustomToken token = null;
        String requestUrl = token_url.replace("APPID", appid).replace("APPSECRET", appsecret).replace("CODE",code).replace("REDIRECTURL",redirect_url);
        // 发起GET请求获取凭证
        String content = httpsRequest(requestUrl, "GET", null);
        if (StringUtils.isNotBlank(content)){
            content = content.replace("=","\":\"");
            content = content.replace("&","\",\"");
            content = "{\"" + content +"\"}";
            JSONObject jsonObject = JSONObject.parseObject(content);

            if (null != jsonObject) {
                try {
                    token = new CustomToken();
                    token.setAccessToken(jsonObject.getString("access_token"));
                    token.setExpiresIn(jsonObject.getInteger("expires_in"));
                    token.setRefreshToken(jsonObject.getString("refresh_token"));
                } catch (JSONException e) {
                    token = null;
                    // 获取token失败
                    log.error("获取token失败 errcode:{} errmsg:{}", jsonObject.getInteger("code"), jsonObject.getString("msg"));
                }
            }
        }
        return token;
    }

    /**
     * 权限自动续期，获取Access Token
     * @param appid
     * @param appsecret
     * @param refresh_token
     * @return
     */
    public static CustomToken getRefreshToken(String appid, String appsecret,String refresh_token) {
        CustomToken token = null;
        String requestUrl = refresh_token_url.replace("APPID", appid).replace("APPSECRET", appsecret).replace("REFRESHTOKEN",refresh_token);
        // 发起GET请求获取凭证
        String content = httpsRequest(requestUrl, "GET", null);
        if (StringUtils.isNotBlank(content)){
            content = content.replace("=","\":\"");
            content = content.replace("&","\",\"");
            content = "{\"" + content +"\"}";
            JSONObject jsonObject = JSONObject.parseObject(content);

            if (null != jsonObject) {
                try {
                    token = new CustomToken();
                    token.setAccessToken(jsonObject.getString("access_token"));
                    token.setExpiresIn(jsonObject.getInteger("expires_in"));
                    token.setRefreshToken(jsonObject.getString("refresh_token"));
                } catch (JSONException e) {
                    token = null;
                    // 获取token失败
                    log.error("获取token失败 errcode:{} errmsg:{}", jsonObject.getInteger("code"), jsonObject.getString("msg"));
                }
            }
        }
        return token;
    }

    /**
     * 获取用户OpenID_OAuth2.0
     * @param access_token
     * @return
     */
    public static Me getMe(String access_token){
        Me me = null;
        String requestUrl = oauth_url.replace("ACCESSTOKEN",access_token);

        // 发起GET请求获取凭证
        String result = httpsRequest(requestUrl, "GET", null);
        if (StringUtils.isNotBlank(result)) {
            try {
                me = new Me();
                me.setOpenId(StringUtils.substringBetween(result, "\"openid\":\"", "\"}"));
            } catch (JSONException e){
                me = null;
                log.error("获取用户信息失败 ");
            }
        }
        return me;
    }

    /**
     * 获取登录用户的昵称、头像、性别
     * @param access_token
     * @param appid
     * @param openid
     * @return
     */
    public static String getUserInfo(String access_token, String appid, String openid){
        String result = null;
        String requestUrl = user_info_url.replace("ACCESSTOKEN",access_token).replace("APPID",appid).replace("OPENID",openid);

        // 发起GET请求获取凭证
        result= httpsRequest(requestUrl, "GET", null);
        if (StringUtils.isNotBlank(result)) {
            return result;
        }
        return result;
    }


}
