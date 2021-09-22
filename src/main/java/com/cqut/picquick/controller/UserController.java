package com.cqut.picquick.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.cqut.picquick.common.ResponseResult;
import com.cqut.picquick.common.constant.KeyValueConstant;
import com.cqut.picquick.entity.User;
import com.cqut.picquick.enums.ResultCode;
import com.cqut.picquick.provider.CloudStorageProvider;
import com.cqut.picquick.service.IUserService;
import com.cqut.picquick.service.RedisService;
import com.cqut.picquick.util.JwtUtil;
import com.qiniu.storage.model.DefaultPutRet;
import io.swagger.annotations.*;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.util.HashMap;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author hk
 * @since 2021-09-14
 */
@Api(tags = "用户Controller")
@CrossOrigin()
@RestController
@RequestMapping("/picquick/user")
public class UserController {

    @Autowired()
    private IUserService userService ;
    @Autowired
    private RedisService redisService ;
    @Autowired
    private CloudStorageProvider cloudStorageProvider ;
    @Autowired
    private JwtUtil jwtUtil ;

    /**
     * @methodName : 用户上传头像
     * @author : HK意境
     * @date : 2021/9/20 20:29
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
    @ApiOperation("修改用户头像")
    @PostMapping("/avatar")
    public ResponseResult updateAvatar(@RequestParam("token")String token, @RequestParam("file") String file) throws Exception {

        //解析Token
        String account = (String) jwtUtil.parseJWT(token).get("account");

        //构造返回对象
        ResponseResult responseResult = new ResponseResult(ResultCode.SUCCESS);
        HashMap<String, Object> resMap = new HashMap<>();

        //头像地址不存在或认证失败
        if (account == null){
            System.out.println("认证不通过");
            resMap.put("err", "头像地址不存在或认证失败");
            responseResult.setResultCode(ResultCode.FAIL) ;
        }else {
            //认证通过
            //置换头像地址
            System.out.println("认证通过");
            //DefaultPutRet putRet = cloudStorageProvider.upload(Base64.decodeBase64(file), "avatar_"+account+".png");
            cloudStorageProvider.put64image(Base64.decodeBase64(file),"avatar_"+account+".png");
            String avatarUrl = cloudStorageProvider.getPrivateFile("avatar_"+account, -1);
            System.out.println("新的头像地址：" + avatarUrl);
            boolean update = userService.update(new LambdaUpdateWrapper<User>()
                    .set(User::getAvatarUrl, avatarUrl)
                    .eq(User::getAccount, account));
            //更新失败
            if (!update) {
                resMap.put("err", "头像地址存在,认证成功,但更新头像失败");
                responseResult.setResultCode(ResultCode.FAIL);
            } else {
                System.out.println("更新成功");
                User user = userService.getById((String) jwtUtil.parseJWT(token).get("id"));
                resMap.put("data", user);
                responseResult.setResultCode(ResultCode.SUCCESS);
            }
        }
        return responseResult.setData(resMap);

    }

    @GetMapping("/ava")
    public ResponseResult getAvatarInfo(){
        HashMap<String, Object> resMap = new HashMap<>();
        resMap.put("url", "http://"+ "qzeut4n5e.hn-bkt.clouddn.com/");
        resMap.put("Authorization", cloudStorageProvider.getUpToken()) ;

        ResponseResult responseResult = new ResponseResult(ResultCode.SUCCESS,resMap);
        return  responseResult.setData(resMap) ;
    }


    @PostMapping("/login")
    @ApiOperation(value = "一般方法用户登录",notes = "使用username,password")
    public ResponseResult commonLogin(@RequestBody User loginUser){

        User login = userService.login(loginUser);
        ResponseResult responseResult = new ResponseResult(ResultCode.SUCCESS);

        //用户不存在
        if (login == null){
            return responseResult.setResultCode(ResultCode.FAIL).setData(null);
        }else if (login.getUserInfo().equals("user has exits")){
            //用户名已经存在
            return responseResult.setResultCode(ResultCode.USER_HAS_EXITS).setData(null);
        }
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("user",login) ;
        map.put("token",login.getToken()) ;
        return responseResult.setData(map);
    }


    @PostMapping("/register")
    @ApiOperation("用户邮箱注册")
    @ApiImplicitParams(
            @ApiImplicitParam(value = "username")
    )
    public ResponseResult userRegister(@RequestParam(value = "username" , required = false) String username , @RequestParam("account")String account ,
                                       @RequestParam("type")String type , @RequestParam("password")String password, @RequestParam("authCode")String authCode){

        HashMap<String, Object> resMap = new HashMap<>();
        ResponseResult responseResult = new ResponseResult();

        User one = userService.getOne(new LambdaUpdateWrapper<User>().eq(User::getEmail, account));

        //账号存在
        if (one != null){
            resMap.put("err","账号名已存在");
            responseResult.setResultCode(ResultCode.FAIL).setData(resMap) ;
            return responseResult;
        }
        String code = redisService.get(KeyValueConstant.LOGIN_CODE + account);
        //验证码错误
        if (code == null || !code.equals(authCode)){
            resMap.put("err","验证码过期或错误");
            responseResult.setResultCode(ResultCode.FAIL).setData(resMap) ;
            return responseResult;
        }
        one  = new User();
        one.setUsername(username).setAccount(account).setEmail(account).setPassword(password);
        userService.addUser(one);

        resMap.put("user",one) ;
        return responseResult.setResultCode(ResultCode.SUCCESS).setData(resMap);
    }



    /**
     * @methodName : getUserById
     * @author : HK意境
     * @date : 2021/9/14 11:00
     * @description :
     * @Todo :
     * @params :
     * @param : id
     * @return : ResponseResult
     * @throws:
     * @Bug :
     * @Modified :
     * @Version : 1.0
     */
    @GetMapping()
    @ApiOperation(value = "根据ID获取User")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "id",dataType = "String")
    )
    public ResponseResult getUserById(@RequestParam String id){
        User user = userService.getById(id);

        //用户不存在
        if (user == null){
            return new ResponseResult(ResultCode.FAIL,"this user does not exist");
        }
        //返回请求获取的User
        return new ResponseResult(ResultCode.SUCCESS,user) ;
    }


    /**
     * @methodName : createUser
     * @author : HK意境
     * @date : 2021/9/14 11:01
     * @description :
     * @Todo :
     * @params :
     * @param : user
     * @return : ResponseResult
     * @throws:
     * @Bug :
     * @Modified :
     * @Version : 1.0
     */
    @PostMapping()
    @ApiOperation(value = "创建用户")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "User对象",dataTypeClass = User.class)
    )
    public ResponseResult createUser(@RequestBody User user){
        User addUser = userService.addUser(user);

        ResponseResult responseResult = new ResponseResult(ResultCode.SUCCESS_CREATE);
        if (addUser == null){
            responseResult.setResultCode(ResultCode.FAIL);
        }
        return responseResult.setData(addUser);
    }

    /**
     * @methodName : updateUser
     * @author : HK意境
     * @date : 2021/9/14 11:01
     * @description :
     * @Todo :
     * @params :
     * @param : user
     * @return : ResponseResult
     * @throws:
     * @Bug :
     * @Modified :
     * @Version : 1.0
     */
    @PutMapping()
    @ApiOperation(value = "修改用户信息")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "待修改用户", dataTypeClass = User.class)
    )
    public ResponseResult updateUser(@RequestBody User user){

        boolean update = userService.update(user, new LambdaUpdateWrapper<User>().eq(User::getAccount, user.getAccount()));

        return new ResponseResult(ResultCode.SUCCESS,update) ;
    }


    /**
     * @methodName : deleteUserById
     * @author : HK意境
     * @date : 2021/9/14 11:02
     * @description :
     * @Todo :
     * @params :
     * @param : id
     * @return : ResponseResult
     * @throws:
     * @Bug :
     * @Modified :
     * @Version : 1.0
     */
    @DeleteMapping()
    @ApiOperation(value = "删除用户")
    public ResponseResult deleteUserById(@RequestBody String id) {

        //查询是否存在：
        User user = userService.getById(id);
        if (user == null){
            return new ResponseResult(ResultCode.FAIL, "this user does not exist");
        }
        boolean res = userService.removeById(id);

        return new ResponseResult(ResultCode.SUCCESS, res);

    }


}
