package com.cqut.picquick.controller;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqut.picquick.common.ResponseResult;
import com.cqut.picquick.entity.Picture;
import com.cqut.picquick.entity.PictureType;
import com.cqut.picquick.enums.ResultCode;
import com.cqut.picquick.mapper.UserMapper;
import com.cqut.picquick.service.IPictureService;
import com.cqut.picquick.service.IPictureTypeService;
import com.cqut.picquick.vo.PictureVo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 图片 前端控制器
 * </p>
 *
 * @author hk
 * @since 2021-09-14
 */
@RestController
@RequestMapping("/picquick/picture")
public class PictureController {

    @Autowired
    private IPictureService pictureService;
    @Autowired
    private IPictureTypeService pictureTypeService ;
    @Autowired
    private UserMapper userMapper ;

    /**
     * @methodName : 点赞图片
     * @author : HK意境
     * @date : 2021/9/21 16:44
     * @description : 给喜欢的图片点赞
     * @Todo :
     * @params :
         * @param : null
     * @return : null
     * @throws:
     * @Bug :
     * @Modified :
     * @Version : 1.0
     */
    @Transactional()
    @PutMapping("/like/{pictureId}")
    @ApiOperation("点赞图片")
    public ResponseResult likePicture(@PathVariable("pictureId")String pictureId){
        Picture byId = pictureService.getById(pictureId);
        boolean update = pictureService.update(new LambdaUpdateWrapper<Picture>().set(Picture::getLikeCount, byId.getLikeCount() + 1)
                .eq(Picture::getId, byId.getId()));

        ResponseResult responseResult = new ResponseResult(ResultCode.SUCCESS);
        HashMap<String, Object> resMap = new HashMap<>();
        //点赞成功
        if (update){
            resMap.put("msg", "点赞成功");
        }else{
            resMap.put("msg","点赞失败");
        }
        return responseResult.setData(resMap) ;
    }



    /**
     * @methodName : 搜索图片
     * @author : HK意境
     * @date : 2021/9/19 13:53
     * @description : 根据图片类型或者名字搜索图片
     * @Todo :
     * @params :
         * @param : null
     * @return : null
     * @throws:
     * @Bug :
     * @Modified :
     * @Version : 1.0
     */
    @ApiOperation("根据图片名字或者类型搜索图片")
    @GetMapping("/search")
    @ApiImplicitParams(
            @ApiImplicitParam(value = "keyword", name = "关键字")
    )
    public ResponseResult searchPicture(@RequestParam("keyword")String keyword){

        List<Picture> pictureList = pictureService.searchPictureList(keyword);
        ResponseResult responseResult = new ResponseResult(ResultCode.SUCCESS,pictureList);
        return responseResult ;
    }


    /**
     * @methodName : 修改图片分类
     * @author : HK意境
     * @date : 2021/9/18 20:49
     * @description :限制只能修改图片分类
     * @Todo :
     * @params :
         * @param : null
     * @return : null
     * @throws:
     * @Bug :
     * @Modified :
     * @Version : 1.0
     */
    @ApiOperation("修改图片分类")
    @PutMapping("/modify")
    @ApiImplicitParams(
            @ApiImplicitParam(value = "newCategory" ,name = "新的图片分类")
    )
    public ResponseResult updatePicture(@RequestParam("id")String id ,@RequestParam("newCategory")String newCategory){
        //返回数据
        boolean update = false ;
        ResponseResult responseResult = new ResponseResult();
        //查询所有图片分类
        List<PictureType> pictureTypeList = pictureTypeService.list();
        //匹配分类
        for (PictureType pictureType : pictureTypeList) {
            //匹配上对应分类
            if (pictureType.getName().equals(newCategory)){
                //修改图片分类
                update = pictureService.update(new LambdaUpdateWrapper<Picture>().set(Picture::getCategoryId, pictureType.getId()));
                break;
            }
        }
        //更新成功
        if (update){
            return responseResult.setResultCode(ResultCode.SUCCESS).setData(newCategory);
        }else{
            return responseResult.setResultCode(ResultCode.FAIL).setData("error");
        }

    }


    /**
     * @methodName : 获取所有公共图片
     * @author : HK意境
     * @date : 2021/9/18 21:36
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

    @ApiOperation("返回所有公开图片")
    @GetMapping("/public/all")
    public ResponseResult getAllPublicPicture(@RequestParam("pages")Integer current ,
                                              @RequestParam(value = "size" , defaultValue = "25")Integer size) throws Exception {
        //获取所有图片
        Page<Picture> picturePage = pictureService.getPublicPictures(current, size);
        //获取分页Records
        List<Picture> records = picturePage.getRecords();
        //更新url 链接
        pictureService.updatePictureUrl(records);
        List<PictureVo> publicPicturesVO = pictureService.getPublicPicturesVO(records);
        //返回结果
        HashMap<String, Object> resMap = new HashMap<>();
        resMap.put("List", publicPicturesVO) ;
        resMap.put("pages", picturePage.getPages());
        resMap.put("current", current);
        resMap.put("size", size) ;
        resMap.put("total", picturePage.getTotal());
        return new ResponseResult(ResultCode.SUCCESS,resMap);
    }


    /**
     * @methodName : 获取用户的上传图片
     * @author : HK意境
     * @date : 2021/9/19 13:44
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
    @GetMapping("/public/user")
    @ApiOperation("获取用户上传图片")
    public ResponseResult getUserPicture(@RequestParam("id")String userId, HttpServletRequest request, Integer current , Integer size) throws Exception {

        //获取用户
        Page<Picture> privatePicturesPage = pictureService.getPrivatePictures(userId, current, size);
        //返回对象
        HashMap<String, Object> resMap = new HashMap<>();
        resMap.put("data", privatePicturesPage.getRecords()) ;
        resMap.put("current", current) ;
        resMap.put("size", size) ;
        resMap.put("total", privatePicturesPage.getTotal());
        ResponseResult responseResult = new ResponseResult(ResultCode.SUCCESS, resMap);
        return responseResult ;
    }


}
