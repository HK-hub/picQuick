package com.cqut.picquick.service.impl;

import cn.hutool.jwt.JWTUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.cqut.picquick.common.ResponseResult;
import com.cqut.picquick.common.constant.KeyValueConstant;
import com.cqut.picquick.entity.User;
import com.cqut.picquick.enums.ResultCode;
import com.cqut.picquick.mapper.UserMapper;
import com.cqut.picquick.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqut.picquick.service.RedisService;
import com.cqut.picquick.util.BcryptUtil;
import com.cqut.picquick.util.IdWorker;
import com.cqut.picquick.util.JwtToken;
import com.cqut.picquick.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author hk
 * @since 2021-09-14
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private IdWorker idWorker ;
    @Autowired
    private UserMapper userMapper ;
    @Autowired
    private JwtUtil jwtUtil ;
    @Autowired
    private RedisService redisService;


    @Override
    public User addUser(User user) {

        //设置加密密码
        String encode = BcryptUtil.encode(user.getPassword());
        user.setPassword(encode);
        //设置账户
        user.setAccount(idWorker.nextIdString());
        //设置ID
        //user.setId(idWorker.nextIdString());
        //设置token
        String token = jwtUtil.createJWT(user);
        user.setToken(token);

        //插入User
        int insert = userMapper.insert(user);

        //插入失败
        if (user.getId() == null) {
            return null;
        }
        return user ;
    }

    @Override
    public User login(User user) {

        List<User> userList = userMapper.selectList(new LambdaQueryWrapper<User>().eq(User::getUsername, user.getUsername()));

        if (userList.size() >= 2){
            User res = new User();
            res.setUserInfo("user has exits");
            return res ;
        }
        else if (userList.size()==0){
            return null;
        }
        //查询匹配：
        User selectUser = userList.get(0);
        //将明文密码跟加密后的密码进行匹配，如果一致返回true,否则返回false
        boolean match = BcryptUtil.match(user.getPassword(), selectUser.getPassword());
        if (match){
            //获取更新token
            String token = jwtUtil.createJWT(user);
            int update = userMapper.update(selectUser, new LambdaUpdateWrapper<User>().set(User::getToken, token).eq(User::getAccount, selectUser.getAccount()));
            return selectUser;
        }

        return null;
    }



    /**
     * @methodName : 邮箱登录的用户
     * @author : HK意境
     * @date : 2021/9/16 0:40
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
    @Override
    public User createOrUpdate(String email) {

        User user = userMapper.selectOne(new LambdaUpdateWrapper<User>().eq(User::getEmail, email));
        String token ;
        //新用户
        if (user == null){
            user = new User();
            user.setEmail(email).setAccount(email).setUsername(email);
            userMapper.insert(user) ;
            //换取新的 token
            token = jwtUtil.createJWT(user);
            user.setToken(token);
        }else{
            //换取新的 token
            token = jwtUtil.createJWT(user);
            user.setToken(token);
            userMapper.updateById(user) ;

        }

        // redis 缓存
        redisService.set(KeyValueConstant.userToken + user.getAccount() , token, 3600);

        return user;
    }
}
