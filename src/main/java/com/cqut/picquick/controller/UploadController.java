package com.cqut.picquick.controller;

import com.cqut.picquick.common.ResponseResult;
import com.cqut.picquick.enums.ResultCode;
import com.cqut.picquick.provider.CloudStorageProvider;
import com.cqut.picquick.service.IPictureService;
import com.cqut.picquick.service.IQiniuService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qiniu.storage.model.DefaultPutRet;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : HK意境
 * @ClassName : UploadController
 * @date : 2021/9/15 0:23
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@RestController
@RequestMapping(value = "/cloud")
public class UploadController {

    @Autowired
    private IPictureService pictureService ;
    @Autowired
    private IQiniuService qiniuService;
    @Autowired
    private CloudStorageProvider cloudStorageProvider ;
    @Autowired
    private ObjectMapper objectMapper;


    /**
     * @methodName :
     * @author : HK意境
     * @date : 2021/9/15 0:27
     * @description : 多个文件上床
     * @Todo :
     * @params :
         * @param : null
     * @return : null
     * @throws:
     * @Bug :
     * @Modified :
     * @Version : 1.0
     */
    @ApiOperation("上传文件")
    @PostMapping("/multiple")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "待上传文件")
    )
    public ResponseResult uploadMultipleFiles(MultipartFile[] files , HttpServletRequest request){

        System.out.println(files.length);
        for (MultipartFile file : files) {
            System.out.println(file.isEmpty());
        }

        ResponseResult responseResult = qiniuService.uploadMultipleFile(files, request, true);

        //获取返回结果
        Map resultData = (HashMap<String, Object>) responseResult.getData();
        String err = (String) resultData.get("err");

        //上传文件失败
        if (err != null || !err.isEmpty() || "".equals(err)){
            return responseResult.setResultCode(ResultCode.SC_EXPECTATION_FAILED) ;
        }
        return responseResult.setResultCode(ResultCode.SUCCESS_CREATE);

    }



    @ApiOperation("单个文件上传")
    @PostMapping("/single")
    public ResponseResult uploadSingleFile(MultipartFile file, HttpServletRequest request) throws IOException {

        Map<String, Object> resMap = qiniuService.uploadFile(file.getInputStream(), file.getOriginalFilename(), request);

        //获取返回结果
        ResponseResult responseResult = new ResponseResult();
        responseResult.setData(resMap) ;
        String err = (String) resMap.get("err");

        //上传文件失败
        if (err != null || !err.isEmpty() || "".equals(err)){
            return responseResult.setResultCode(ResultCode.SC_EXPECTATION_FAILED);
        }
        return responseResult.setResultCode(ResultCode.SUCCESS_CREATE);

    }






}
