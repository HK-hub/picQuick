package com.cqut.picquick.controller;

import com.cqut.picquick.bean.MailBean;
import com.cqut.picquick.common.CustomToken;
import com.cqut.picquick.common.ResponseResult;
import com.cqut.picquick.common.constant.MessageConstant;
import com.cqut.picquick.common.constant.QQConstant;
import com.cqut.picquick.entity.User;
import com.cqut.picquick.entity.other.Me;
import com.cqut.picquick.enums.ResultCode;
import com.cqut.picquick.service.IUserService;
import com.cqut.picquick.service.RedisService;
import com.cqut.picquick.service.impl.UserServiceImpl;
import com.cqut.picquick.util.MailUtil;
import com.cqut.picquick.util.QQCommonUtil;
import com.cqut.picquick.util.RandomStringUtil;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;

import org.apache.solr.common.util.Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.HashMap;


/**
 * @author : HK意境
 * @ClassName : LoginController
 * @date : 2021/9/15 15:54
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@CrossOrigin
@RestController
@RequestMapping("/picquick/user")
public class LoginController {

    @Autowired
    private RedisService redisService ;
    @Autowired
    private IUserService userService ;
    @Autowired
    private MailUtil mailUtil;
    @Value("${email.code.expiration-time}")
    private long expirationTime ;
    private static final String loginSubject = "" ;
    private static final String LOGIN_CODE = "LOGIN_CODE-" ;


    /**
     * @methodName : 账号密码登录
     * @author : HK意境
     * @date : 2021/9/20 15:55
     * @description :
     * @Todo :
     * @params :
         * @param : null
     * @return : null
     * @throws:
     * @Bug :
     * @Modified :
     * @Version : 1.0
     */
    @ApiOperation("账号密码登录")
    @PostMapping("/accountLogin")
    public ResponseResult accountLogin(@RequestParam("account")String account , @RequestParam("password")String password){

        //构造返回对象
        ResponseResult responseResult = new ResponseResult(ResultCode.SUCCESS);
        HashMap<String, Object> resMap = new HashMap<>();
        System.out.println("account： " + account);
        System.out.println("password : " + password);
        User user = userService.accountLogin(account, password);
        if (user == null){
            resMap.put("err","账号或密码错误");
            responseResult.setResultCode(ResultCode.FAIL).setData(resMap);
        }else{
            resMap.put("data",user) ;
            responseResult.setData(resMap);
        }
        System.out.println(user.toString());
        System.out.println(responseResult.getData().toString());
        return responseResult;
    }



    /**
     * @methodName : 邮箱登录
     * @author : HK意境
     * @date : 2021/9/15 22:30
     * @description : qq, 网易邮箱登录
     * @Todo :
     * @params :
         * @param : null
     * @return : null
     * @throws:
     * @Bug :
     * @Modified :
     * @Version : 1.0
     */

    @GetMapping("/authCode")
    @ApiOperation(value = "获取邮箱验证码,AJAX 方法" )
    @ApiImplicitParams(
            @ApiImplicitParam(value = "邮箱")
    )
    public ResponseResult sendAuthCode(@RequestParam String emailAddress){
        MailBean mailBean = new MailBean();
        //设置接收者
        mailBean.setRecipient(emailAddress);
        //设置主题
        mailBean.setSubject(MessageConstant.loginSubject);
        //设置邮件内容: 登录用户， 验证码
        String code = MailUtil.generateVerCode();
        mailBean.setContent(MessageConstant.getLoginSubject(emailAddress, code));
        boolean res = mailUtil.sendSimpleMail(mailBean);

        ResponseResult responseResult = new ResponseResult(ResultCode.SUCCESS);
        HashMap<String, Object> resMap = new HashMap<>();
        resMap.put("sendResult", res) ;
        if (res == false){
            responseResult.setResultCode(ResultCode.FAIL).setData(resMap);
            //redisService.remove(LOGIN_CODE + emailAddress);
        }

        //设置 验证码 缓存，缓存时间
        redisService.set(LOGIN_CODE + emailAddress , code ,  expirationTime);
        resMap.put("code", code) ;
        return responseResult.setData(resMap);
    }

    @GetMapping("/emailLogin")
    @ApiOperation(value = "邮箱登录")
    public ResponseResult emailLogin(@RequestParam String email , @RequestParam String code){

        // 获取 redis 缓存数据
        String authCode = redisService.get(LOGIN_CODE + email);
        ResponseResult responseResult = new ResponseResult();
        HashMap<String, Object> resMap = new HashMap<>();
        // redis 没有缓存
        if(authCode == null || "".equals(authCode)){
            responseResult.setResultCode(ResultCode.FAIL).setData("验证码错误!");
            return responseResult ;
        }else if (!code.equals(authCode)){
            responseResult.setResultCode(ResultCode.FAIL).setData("验证码错误!");
            return responseResult ;
        }else {     //authCode 和 code 相等
            //判断是否是新用户
            User user = userService.createOrUpdate(email);
            resMap.put("userInfo", user) ;
            resMap.put("token",user.getToken());
            responseResult.setResultCode(ResultCode.SUCCESS);
            responseResult.setData(resMap) ;
        }
        return responseResult ;
    }



    /**
     * @methodName : toLogin()
     * @author : HK意境
     * @date : 2021/9/15 16:56
     * @description : 开始登录
     * @Todo :
     * @params :
         * @param : null
     * @return : null
     * @throws:
     * @Bug :
     * @Modified :
     * @Version : 1.0
     */
    @GetMapping("/qqLogin")
    public void toLogin(HttpServletRequest request , HttpServletResponse response) {

        String state = RandomStringUtil.generateLowerStr(10);
        //重定向
        String url = QQCommonUtil.getCode(QQConstant.APP_ID, QQConstant.CALLBACK_URL, state);
        try {
            response.sendRedirect(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping(value = "/qqCallback")
    public ResponseResult wiki(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        if (state == null || code == null) {
            request.setAttribute("msg","login failed");
            response.sendRedirect("/qqLogin");
        }
        ResponseResult responseResult = new ResponseResult(ResultCode.SUCCESS);

        //获取access_token
        CustomToken token = QQCommonUtil.getToken(QQConstant.APP_ID, QQConstant.APP_KEY , code, QQConstant.CALLBACK_URL);
        if (StringUtils.isNotBlank(token.getAccessToken())){
            //获取用户openid
            Me me = QQCommonUtil.getMe(token.getAccessToken());
            if(StringUtils.isNotBlank(me.getOpenId())){
                //获取用户信息
                String user_info = QQCommonUtil.getUserInfo(token.getAccessToken(), QQConstant.APP_ID, me.getOpenId());

                //获取到用户信息之后自己的处理逻辑.....
                String url = "/info/login?"+ URLEncoder.encode(Base64.getEncoder().encodeToString("openId".getBytes()),"UTF-8")+"="
                        +URLEncoder.encode(Base64.getEncoder().encodeToString(me.getOpenId().getBytes()),"UTF-8")+"&dis="
                        +URLEncoder.encode(Base64.getEncoder().encodeToString(RandomStringUtil.generateLowerStr(10).getBytes()),"UTF-8");

                responseResult.setData(user_info) ;
                System.out.println(url);
            }
        }

        return responseResult ;
    }


}
