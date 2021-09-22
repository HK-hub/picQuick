package com.cqut.picquick.controller;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.cqut.picquick.common.ResponseResult;
import com.cqut.picquick.entity.PictureType;
import com.cqut.picquick.enums.ResultCode;
import com.cqut.picquick.service.IPictureTypeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 图片类型表 前端控制器
 * </p>
 *
 * @author hk
 * @since 2021-09-14
 */
@RestController
@RequestMapping("/picquick/picture-type")
public class PictureTypeController {

    @Autowired
    private IPictureTypeService pictureTypeService ;

    /**
     * @methodName : 查询所有分类
     * @author : HK意境
     * @date : 2021/9/18 20:59
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
    @GetMapping("/all")
    @ApiOperation("获取所有分类")
    public ResponseResult getAllCategory(){

        //首先在redis 里面拿
        List<PictureType> pictureTypes = pictureTypeService.list();
        ResponseResult responseResult = new ResponseResult(ResultCode.SUCCESS);
        if (pictureTypes.size() <= 0){
            responseResult.setResultCode(ResultCode.FAIL);
        }
        return responseResult.setData(pictureTypes) ;
    }


    /**
     * @methodName : 修改分类，分类信息
     * @author : HK意境
     * @date : 2021/9/18 21:03
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
    @GetMapping("/modify")
    @ApiOperation("修改分类信息")
    public ResponseResult modifyCategoryInfo(@RequestParam("id")Integer id , @RequestParam("category")String categoryName, @RequestParam("info")String info){
        //PictureType byId = pictureTypeService.getById(id);
        boolean update = pictureTypeService.update(new LambdaUpdateWrapper<PictureType>().set(PictureType::getName, categoryName).set(PictureType::getDescription, info).eq(PictureType::getId, id));
        ResponseResult responseResult = new ResponseResult(ResultCode.SUCCESS);
        HashMap<String, String> resMap = new HashMap<>();
        //修改成功
        if (!update){
            resMap.put("err","修改图片分类失败");
            return responseResult.setResultCode(ResultCode.FAIL).setData(resMap);

        }
        resMap.put("category",categoryName);
        resMap.put("description",info) ;
        return responseResult.setData(resMap) ;
    }



}
