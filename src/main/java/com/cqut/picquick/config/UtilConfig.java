package com.cqut.picquick.config;

import com.cqut.picquick.util.BcryptUtil;
import com.cqut.picquick.util.IdWorker;
import com.cqut.picquick.util.ImgStringUtil;
import com.cqut.picquick.util.RandomStringUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : HK意境
 * @ClassName : UtilConfig
 * @date : 2021/9/14 13:11
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Configuration
public class UtilConfig {

    @Bean
    public IdWorker getIdWorker(){
        return new IdWorker();
    }

    @Bean
    public ImgStringUtil getImgStringUtil(){

        return new ImgStringUtil() ;
    }


}
