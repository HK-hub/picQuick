package com.cqut.picquick.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.cqut.picquick.common.ResponseResult;
import com.cqut.picquick.entity.Picture;
import com.cqut.picquick.entity.User;
import com.cqut.picquick.enums.ResultCode;
import com.cqut.picquick.provider.CloudStorageProvider;
import com.cqut.picquick.service.IPictureService;
import com.cqut.picquick.service.IQiniuService;
import com.cqut.picquick.service.IUserService;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : HK意境
 * @ClassName : QiniuServiceImpl
 * @date : 2021/9/14 23:41
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Service
public class QiniuServiceImpl implements IQiniuService, InitializingBean {

    @Autowired
    private CloudStorageProvider cloudStorageProvider ;
    @Autowired
    private IUserService userService ;
    @Autowired
    private IPictureService pictureService ;

    @Value("${qiniu.oss.bucket}")
    private String bucket;

    @Value("${qiniu.oss.domain}")
    private String domain;

    /**
     * 定义七牛云上传的相关策略
     */
    private StringMap putPolicy;

    /**
     * @methodName :
     * @author : HK意境
     * @date : 2021/9/15 9:17
     * @description : 多文件上传
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
    public ResponseResult uploadMultipleFile(MultipartFile[] files, HttpServletRequest request, boolean override) {

        //结果Map 集合
        HashMap<String, Object> resultMap = new HashMap<>();
        //上传成功集合
        ArrayList<Picture> pictureSuccessList = new ArrayList<>();
        //上传失败集合
        ArrayList<String> pictureFailList = new ArrayList<>();

        ResponseResult responseResult = new ResponseResult(ResultCode.SUCCESS_CREATE);
        //上传文件为空
        if (files.length <= 0 ) {
            responseResult.setResultCode(ResultCode.SC_EXPECTATION_FAILED) ;
            resultMap.put("err","file is empty") ;
            responseResult.setData(resultMap) ;
            return responseResult ;
        }
        //默认上传接口回复对象
        DefaultPutRet putRet;

        //把文件转化为字节数组
        InputStream is = null;
        ByteArrayOutputStream bos = null;
        String fileKey  = null ;

        for (int i = 0 ; i < files.length ; ++i){

            MultipartFile file = files[i];
            try {
                //该文件为空
                if (files[i].isEmpty() || files[i].getSize() == 0){
                    pictureFailList.add(file.getOriginalFilename()) ;
                    continue;
                }

                //文件不为空
                fileKey = file.getOriginalFilename() ;
                is = file.getInputStream();
                bos = new ByteArrayOutputStream();
                byte[] b = new byte[1024];
                int len = -1;
                while ((len = is.read(b)) != -1){
                    bos.write(b, 0, len);
                }
                byte[] uploadBytes= bos.toByteArray();

                String upToken;
                if(override){
                    upToken = cloudStorageProvider.getUpToken(fileKey);
                }else{
                    //覆盖上传凭证
                    upToken = cloudStorageProvider.getUpToken();
                }

                //进行上传操作，传入文件的字节数组，文件名，上传空间，得到回复对象
                DefaultPutRet defaultPutRet = cloudStorageProvider.upload(uploadBytes, fileKey);
                //上传成功
                if (defaultPutRet != null) {
                    HashMap<String, Object> attr = new HashMap<>();
                    attr.put("name",file.getOriginalFilename());
                    attr.put("key",defaultPutRet.key) ;
                    attr.put("hash",defaultPutRet.hash) ;
                    attr.put("size",file.getSize());
                    attr.put("token",request.getHeader("Authorization")) ;

                    Picture picture = pictureService.createPicture(defaultPutRet, attr);
                    pictureSuccessList.add(picture) ;

                } else {    //上传失败
                    pictureFailList.add(file.getOriginalFilename());
                }


            }catch (QiniuException exception) {
                exception.printStackTrace();

            } catch (IOException e) {

                e.printStackTrace();
            } finally {
                resultMap.put("successList", pictureSuccessList) ;
                resultMap.put("failList", pictureFailList) ;
                responseResult.setData(resultMap) ;
            }
        }


        return null;
    }

    @Override
    public Map<String, Object> uploadFile(File file, String fileName) throws QiniuException {
        return null;
    }

    /**
     * 上传输入流
     * @param inputStream
     * @param
     * @return
     * @throws IOException
     */
    @Override
    public Map<String, Object> uploadFile(InputStream inputStream, String fileName, HttpServletRequest request) {

        HashMap<String, Object> resMap = new HashMap<>();
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        try {
            DefaultPutRet defaultPutRet = cloudStorageProvider.upload(bufferedInputStream, fileName);

            String token = request.getHeader("Authorization");

            HashMap<String, Object> attr = new HashMap<>();
            attr.put("name",fileName);
            attr.put("key",defaultPutRet.key) ;
            attr.put("hash",defaultPutRet.hash) ;
            attr.put("size",0);
            attr.put("token",token) ;
            Picture picture = pictureService.createPicture(defaultPutRet, attr);
            attr.put("data",picture) ;

        }catch (Exception e){
            e.printStackTrace();

        }finally {
            return resMap ;
        }

    }

    @Override
    public Map<String, Object> delete(String key) throws QiniuException {

        return null;
    }

    @Override
    public Map<String, Object> getFile(String fileKey) throws Exception {
        return null;
    }


    @Override
    public String getPrivateFile(String fileKey, HttpServletRequest request) throws Exception {

        String encodedFileName = URLEncoder.encode(fileKey, "utf-8");
        String publicUrl = String.format("%s/%s", domain, encodedFileName);


        long expireInSeconds = -1;
        String token = request.getHeader("Authorization");
        if (token != null){
            User one = userService.getOne(new LambdaUpdateWrapper<User>().eq(User::getToken, token));
            if (one == null){
                expireInSeconds = 3600;
            }else {
                expireInSeconds = -1 ;
            }
        }
        String privateFile = cloudStorageProvider.getPrivateFile(fileKey, expireInSeconds);

        return privateFile;
    }


    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
