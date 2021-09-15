package com.cqut.picquick.mapper;

import com.cqut.picquick.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author hk
 * @since 2021-09-14
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
