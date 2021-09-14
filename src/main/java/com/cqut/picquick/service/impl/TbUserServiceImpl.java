package com.cqut.picquick.service.impl;

import com.cqut.picquick.entity.TbUser;
import com.cqut.picquick.mapper.TbUserMapper;
import com.cqut.picquick.service.ITbUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author hk
 * @since 2021-09-14
 */
@Service
public class TbUserServiceImpl extends ServiceImpl<TbUserMapper, TbUser> implements ITbUserService {

}
