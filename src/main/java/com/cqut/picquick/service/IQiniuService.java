package com.cqut.picquick.service;

import com.cqut.picquick.common.ResponseResult;
import com.qiniu.common.QiniuException;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @author : HK意境
 * @ClassName : IQiniuService
 * @date : 2021/9/14 23:41
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public interface IQiniuService {




    /**
     * @methodName : 上传多个文件
     * @author : HK意境
     * @date : 2021/9/15 10:28
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
    ResponseResult uploadMultipleFile(MultipartFile[] files, HttpServletRequest request, boolean override);



    /**
     * 以文件的形式上传
     *
     * @param file
     * @param fileName:
     * @return: java.lang.String
     */
    Map<String,Object> uploadFile(File file, String fileName) throws QiniuException;

    /**
     * 以流的形式上传
     *
     * @param inputStream
     * @param fileName:
     * @return: java.lang.String
     */
    Map<String,Object> uploadFile(InputStream inputStream, String fileName, HttpServletRequest request) throws IOException;

    /**
     * @methodName : 上传本地文件
     * @author : HK意境
     * @date : 2021/9/15 10:28
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


    /**
     * 删除文件
     *
     * @param key:
     * @return: java.lang.String
     */
    Map<String,Object> delete(String key) throws QiniuException;


    Map<String,Object> getFile(String fileKey) throws Exception;

    String getPrivateFile(String fileKey,HttpServletRequest request) throws Exception;





}
