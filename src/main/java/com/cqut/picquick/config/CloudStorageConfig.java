package com.cqut.picquick.config;

/**
 * @author : HK意境
 * @ClassName : CloudStorageConfig
 * @date : 2021/9/15 0:50
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */

import com.google.gson.Gson;
import com.qiniu.http.Client;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * @author : HK意境
 * @ClassName : CloudStorageConfig
 * @date : 2021/9/14 22:41
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Configuration
@ConfigurationProperties(prefix = "qiniu.oss")
public class CloudStorageConfig {

    @Value("${qiniu.oss.access-key}")
    private String accessKey ;
    @Value("${qiniu.oss.secret-key}")
    private String secretKey ;
    @Value("${qiniu.oss.region}")
    private String region;


    /**
     * 华南机房,配置自己空间所在的区域
     */
    @Bean
    public com.qiniu.storage.Configuration qiNiuConfig()
            throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Field[] fields = QiNiuRegion.class.getDeclaredFields();
        for(Field field:fields){
            String r = (String)field.get(QiNiuRegion.class);
            //System.out.println(r);
            if(r.equals(region)){
                com.qiniu.storage.Configuration c = new com.qiniu.storage.Configuration(
                        (Region)Region.class
                                .getMethod(r,null)
                                .invoke(null,null));
                return c;

            }
        }
        return new com.qiniu.storage.Configuration(Region.huanan());
    }

    /**
     * 构建一个七牛上传工具实例
     */
    @Bean
    public UploadManager uploadManager(com.qiniu.storage.Configuration c) {
        return new UploadManager(c);
    }


    /**
     * 认证信息实例
     * @return
     */
    @Bean
    public Auth auth() {
        return Auth.create(accessKey, secretKey);
    }

    /**
     *
     * @param c com.qiniu.storage.Configuration
     * @return
     */
    @Bean
    public Client client(com.qiniu.storage.Configuration c){
        return new Client(c);
    }

    /**
     * 构建七牛空间管理实例
     * @param auth 认证信息
     * @param c com.qiniu.storage.Configuration
     * @return
     */
    @Bean
    public BucketManager bucketManager(Auth auth, com.qiniu.storage.Configuration c) {
        return new BucketManager(auth, c);
    }

    /**
     * Gson
     * @return
     */
    @Bean
    public Gson gson() {
        return new Gson() ;
    }


    interface QiNiuRegion{
        String REGION_0 = "region0";
        String REGION_1 = "region1";
        String REGION_2 = "region2";
        String REGION_Na = "regionNa";
        String REGION_AS = "regionAs";
        String HUA_DONG = "huadong";
        String HUA_BEI = "huabei";
        String HUA_NAN = "huanan";
        String BRI_MEI = "beimei";
        String XIN_JIA_PO = "xinijipo";
    }

}








