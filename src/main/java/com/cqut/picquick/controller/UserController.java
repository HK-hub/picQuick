package com.cqut.picquick.controller;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.cqut.picquick.common.ResponseResult;
import com.cqut.picquick.entity.User;
import com.cqut.picquick.enums.ResultCode;
import com.cqut.picquick.service.IUserService;
import com.cqut.picquick.service.impl.UserServiceImpl;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
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
