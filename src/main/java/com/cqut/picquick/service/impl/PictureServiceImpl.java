package com.cqut.picquick.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqut.picquick.entity.Picture;
import com.cqut.picquick.entity.PictureType;
import com.cqut.picquick.entity.User;
import com.cqut.picquick.mapper.PictureMapper;
import com.cqut.picquick.mapper.UserMapper;
import com.cqut.picquick.provider.CloudStorageProvider;
import com.cqut.picquick.service.IPictureService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqut.picquick.service.IPictureTypeService;
import com.cqut.picquick.service.IUserService;
import com.cqut.picquick.util.ChineseUtil;
import com.cqut.picquick.util.JwtUtil;
import com.cqut.picquick.vo.PictureVo;
import com.qiniu.storage.model.DefaultPutRet;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
@Transactional
public class PictureServiceImpl extends ServiceImpl<PictureMapper, Picture> implements IPictureService {

    @Autowired
    private CloudStorageProvider cloudStorageProvider ;
    @Autowired
    private PictureMapper pictureMapper ;
    @Autowired
    private IPictureTypeService pictureTypeService ;
    @Autowired
    private JwtUtil jwtUtil ;
    @Autowired
    private UserMapper userMapper ;

    @Override
    public Picture createPicture(DefaultPutRet putRet, Map<String, Object> attributes) throws Exception {

        Picture picture = new Picture();
        picture.setPictureName((String) attributes.get("name"));
        picture.setFileKey(putRet.key);
        String url = cloudStorageProvider.getPrivateFile(putRet.key, 3600);
        picture.setUrl(url);
        //picture.setSize(Double.parseDouble(""+attributes.get("size")));

        //查询Token， 获取User
        String token = (String) attributes.get("token");
        //用户未登录
        if (token == null || token.isEmpty() || "".equals(token)){
            //设置过期时间
            picture.setExpirationTime(Long.parseLong((3600 * 10) + ""));
            picture.setUploaderId("0000000000");
        }else      //用户登录
        {
            //解析用户
            Claims claims = jwtUtil.parseJWT(token);
            //获取账号，密码,id
            String id = (String) claims.get("id");
            picture.setUploaderId(id);
            picture.setSaveTime(LocalDateTime.now());
            picture.setExpirationTime(Long.parseLong((3600 * 24 * 30 * 12) + ""));
            //保存picture
            //picture = this.addPicture(picture);
        }
        //保存picture
        picture = this.addPicture(picture);
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

    /**
     * @methodName : 获取所有公共图片
     * @author : HK意境
     * @date : 2021/9/19 13:08
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
    public Page<Picture> getPublicPictures(Integer current , Integer size) {

        //当前页面， 每页多少条
        Page<Picture> picturePage = new Page<>(current , size);

        pictureMapper.selectPage(picturePage, new LambdaQueryWrapper<Picture>().eq(Picture::getType, false).orderByAsc(Picture::getLikeCount));
        System.out.println("总页数: " + picturePage.getPages() );
        System.out.println("总条数：" + picturePage.getTotal());
        return picturePage;
    }

    //获取用户私有图片
    @Override
    public Page<Picture> getPrivatePictures(String userId, Integer current , Integer size) throws Exception {
        //获取用户图片 : 当前是第几页， 每页多少记录
        Page<Picture> picturePage = new Page<>(current, size);
        pictureMapper.selectPage(picturePage , new LambdaQueryWrapper<Picture>()
                .eq(Picture::getUploaderId, userId).orderByAsc(Picture::getLikeCount));

        //List<Picture> pictureList = pictureMapper.selectList(new LambdaQueryWrapper<Picture>().eq(Picture::getUploaderId, userId).orderByDesc(Picture::getUpdateTime));
        //获取图片url
        List<Picture> pictureUrlList = updatePictureUrl(picturePage.getRecords());
        picturePage.setRecords(pictureUrlList) ;
        return picturePage;
    }

    @Override
    public List<Picture> updatePictureUrl(List<Picture> pictureList) throws Exception {
        // 过期时间 -1 获取永不过期时间， 7200 获取2小时过期时间
        for (Picture picture : pictureList) {
            String url = cloudStorageProvider.getPrivateFile(picture.getFileKey(), 7200);
            picture.setUrl(url);
        }
        return pictureList;
    }

    /**
     * @methodName : 查询图片
     * @author : HK意境
     * @date : 2021/9/19 14:12
     * @description :更具分类和名字
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
    public List<Picture> searchPictureList(String keyword) {
        List<Picture> pictureList = new ArrayList<>();
        //解析关键字
        if (ChineseUtil.isEnglish(keyword)){
            //全中文
            PictureType one = pictureTypeService.getOne(new LambdaQueryWrapper<PictureType>()
                    .like(PictureType::getName, keyword).or()
                    .like(PictureType::getDescription, keyword));

            //查询分类名称
            pictureList = pictureMapper.selectList(new LambdaQueryWrapper<Picture>()
                    //图片名字模糊查询
                    .like(Picture::getPictureName, keyword).or()
                    //图片类型模糊查询
                    .eq(Picture::getCategoryId, one.getId()));

        }else if (ChineseUtil.checkNameChinese(keyword)){
            //全英文: 分割字符串已每个字符串为单位进行分别的查询
            String[] keywords = keyword.split("\\s+");
            //首先查询最像的
            List<Picture> pictureListAllKeywords = pictureMapper.selectList(new LambdaQueryWrapper<Picture>().like(Picture::getPictureName, keyword));
            boolean b = pictureList.addAll(pictureListAllKeywords);
            for (String key : keywords) {
                List<Picture> pictures = pictureMapper.selectList(new LambdaQueryWrapper<Picture>()
                        .like(Picture::getPictureName, key));
                pictureList.addAll(pictures) ;
            }

        }else{
            //中英文混合
            return null ;
        }

        return pictureList;
    }

    /**
     * @methodName : 获取图片的上传用户信息视图对象
     * @author : HK意境
     * @date : 2021/9/21 21:05
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
    public List<PictureVo> getPublicPicturesVO(List<Picture> records) {
        //获取用户
        List<PictureVo> pictureVoList = new ArrayList<>() ;
        for (Picture record : records) {
            User user = userMapper.selectById(record.getUploaderId());
            PictureVo pictureVo = new PictureVo();
            pictureVo.setPicture(record);
            pictureVo.setUser(user);
            pictureVoList.add(pictureVo) ;
        }
        return pictureVoList ;
    }
}
