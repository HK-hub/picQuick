package com.cqut.picquick.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqut.picquick.entity.Picture;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cqut.picquick.vo.PictureVo;
import com.qiniu.storage.model.DefaultPutRet;

import java.util.List;
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

    Picture createPicture(DefaultPutRet putRet, Map<String, Object> attributes) throws Exception;

    Picture addPicture(Picture picture) ;

    Page<Picture> getPublicPictures(Integer current, Integer size);

    Page<Picture> getPrivatePictures(String userId, Integer current , Integer size) throws Exception;

    List<Picture> updatePictureUrl(List<Picture> pictureList) throws Exception;

    List<Picture> searchPictureList(String keyword) ;

    List<PictureVo> getPublicPicturesVO(List<Picture> records) ;
}
