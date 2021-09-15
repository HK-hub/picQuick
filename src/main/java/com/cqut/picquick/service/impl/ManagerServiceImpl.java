package com.cqut.picquick.service.impl;

import com.cqut.picquick.entity.Manager;
import com.cqut.picquick.mapper.ManagerMapper;
import com.cqut.picquick.service.IManagerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 管理员表 服务实现类
 * </p>
 *
 * @author hk
 * @since 2021-09-14
 */
@Service
public class ManagerServiceImpl extends ServiceImpl<ManagerMapper, Manager> implements IManagerService {

}
