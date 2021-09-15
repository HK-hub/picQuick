package com.cqut.picquick.provider;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Client;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

/**
 * @author : HK意境
 * @ClassName : CloudStorageProvider
 * @date : 2021/9/14 23:13
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Component
public class CloudStorageProvider {



    @Value("${qiniu.oss.bucket}")
    private String bucket ;
    @Value("${qiniu.oss.domain}")
    private String domain;

    /**
     * 定义七牛云上传的相关策略
     */
    private StringMap putPolicy;



    @Autowired
    private Configuration c;
    @Autowired
    private UploadManager uploadManager;
    @Autowired
    private BucketManager bucketManager;
    @Autowired
    private Client client;
    @Autowired
    private Gson gson;
    // 密钥配置
    @Autowired
    private Auth auth;

    //简单上传模式的凭证
    public String getUpToken() {
        return auth.uploadToken(bucket);
    }
    //覆盖上传模式的凭证
    public String getUpToken(String fileKey) {
        return auth.uploadToken(bucket, fileKey);
    }

    //指定过期时间的上传模式


    /**
     * 将本地文件上传
     * @param filePath 本地文件路径
     * @param fileKey 上传到七牛后保存的文件路径名称
     * @return
     * @throws IOException
     */
    public DefaultPutRet upload(String filePath, String fileKey) throws IOException {
        Response res  = uploadManager.put(filePath, fileKey, getUpToken(fileKey));
        // 解析上传成功的结果
        DefaultPutRet putRet = gson.fromJson(res.bodyString(), DefaultPutRet.class);
        System.out.println(putRet.key);
        System.out.println(putRet.hash);
        return putRet;
    }

    /**
     * 上传二进制数据
     * @param data
     * @param fileKey
     * @return
     * @throws IOException
     */
    public DefaultPutRet upload(byte[] data, String fileKey) throws IOException {
        Response res = uploadManager.put(data, fileKey, getUpToken(fileKey));
        //上传成功
        if (res.isOK()){
            // 解析上传成功的结果
            DefaultPutRet putRet = gson.fromJson(res.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
            return putRet ;
        }
        return null;
    }

    /**
     * 上传输入流
     * @param inputStream
     * @param fileKey
     * @return
     * @throws IOException
     */
    public DefaultPutRet upload(InputStream inputStream, String fileKey) throws IOException {
        Response res = uploadManager .put(inputStream, fileKey, getUpToken(fileKey),null,null);

        // 解析上传成功的结果
        DefaultPutRet putRet = gson.fromJson(res.bodyString(), DefaultPutRet.class);
        System.out.println(putRet.key);
        System.out.println(putRet.hash);
        return putRet ;

    }


    public String delete(String key) throws QiniuException {
        Response response = bucketManager.delete(this.bucket, key);
        int retry = 0;
        while (response.needRetry() && retry++ < 3) {
            response = bucketManager.delete(bucket, key);
        }
        return response.statusCode == 200 ? "删除成功!" : "删除失败!";
    }


    /**
     * 获取公共空间文件
     * @param fileKey
     * @return
     */
    public String getFile(String fileKey) throws Exception{
        String encodedFileName = URLEncoder.encode(fileKey, "utf-8").replace("+", "%20");
        String url = String.format("%s/%s", domain, encodedFileName);
        return url;
    }

    /**
     * 获取私有空间文件
     * @param fileKey
     * @return
     */
    public String getPrivateFile(String fileKey, long exexpireInSeconds) throws Exception{
        String encodedFileName = URLEncoder.encode(fileKey, "utf-8").replace("+", "%20");
        String publicUrl = String.format("%s/%s", "http://"+domain, encodedFileName);
        //1小时，可以自定义链接过期时间
        //long expireInSeconds = 3600;
        String finalUrl = null ;
        if (exexpireInSeconds == -1){
            finalUrl = auth.privateDownloadUrl(publicUrl);
        }
        finalUrl = auth.privateDownloadUrl(publicUrl, exexpireInSeconds) ;
        return finalUrl;
    }

    public void afterPropertiesSet() throws Exception {
        this.putPolicy = new StringMap();
        putPolicy.put("returnBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"fname\":$(fname),\"width\":$(imageInfo.width), \"height\":${imageInfo.height}}");
    }


    /**
     * 获取上传凭证
     */
    private String getUploadToken() {
        return this.auth.uploadToken(bucket, null, 3600, putPolicy);
    }


    /***
     * 覆盖模式上传凭证
     *
     *
     */




}
