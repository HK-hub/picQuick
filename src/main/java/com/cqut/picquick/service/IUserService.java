package com.cqut.picquick.service;

import com.cqut.picquick.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author hk
 * @since 2021-09-14
 */
public interface IUserService extends IService<User> {
    User addUser(User user) ;
    User login(User user) ;
    User createOrUpdate(String email) ;
    User accountLogin(String account, String password) ;
}
