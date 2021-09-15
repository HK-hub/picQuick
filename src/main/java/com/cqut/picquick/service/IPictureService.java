package com.cqut.picquick.service;

import com.cqut.picquick.entity.Picture;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qiniu.storage.model.DefaultPutRet;

import java.util.Map;

/**
 * <p>
 * 图片 服务类
 * </p>
 *
 * @author hk
 * @since 2021-09-14
 */
public interface IPictureService extends IService<Picture> {

    Picture createPicture(DefaultPutRet putRet, Map<String, Object> attributes) ;

    Picture addPicture(Picture picture) ;

}
