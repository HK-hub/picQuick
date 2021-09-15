package com.cqut.picquick.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cqut.picquick.entity.Picture;
import com.cqut.picquick.entity.User;
import com.cqut.picquick.mapper.PictureMapper;
import com.cqut.picquick.service.IPictureService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqut.picquick.service.IUserService;
import com.cqut.picquick.util.JwtUtil;
import com.qiniu.storage.model.DefaultPutRet;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 图片 服务实现类
 * </p>
 *
 * @author hk
 * @since 2021-09-14
 */
@Service
public class PictureServiceImpl extends ServiceImpl<PictureMapper, Picture> implements IPictureService {

    @Autowired
    private PictureMapper pictureMapper ;
    @Autowired
    private IUserService userService ;
    @Autowired
    private JwtUtil jwtUtil ;


    @Override
    public Picture createPicture(DefaultPutRet putRet, Map<String, Object> attributes) {

        Picture picture = new Picture();
        picture.setName((String) attributes.get("name"));
        picture.setKey(putRet.key);
        picture.setUrl(putRet.hash);
        picture.setSize((Double) attributes.get("size"));

        //查询Token， 获取User
        String token = (String) attributes.get("token");
        //用户未登录
        if (token == null || token.isEmpty() || "".equals(token)){
            //设置过期时间
            picture.setExpirationTime(Long.parseLong((3600 * 2) + ""));
            picture.setUploaderId(null);
        }else      //用户登录
        {
            //解析用户
            Claims claims = jwtUtil.parseJWT(token);
            //获取账号，密码,id
            String id = (String) claims.get("id");
            picture.setUploaderId(id);
            picture.setSaveTime(LocalDateTime.now());
            //保存picture
            picture = this.addPicture(picture);
        }
        return picture;
    }

    @Override
    public Picture addPicture(Picture picture) {
        int insert = pictureMapper.insert(picture);

        //插入图片成功
        if (picture.getId() != null || !picture.getId().isEmpty()
                || "".equals(picture.getId())){
            return picture ;
        }
        return null;
    }
}
