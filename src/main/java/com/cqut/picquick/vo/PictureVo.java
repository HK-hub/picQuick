package com.cqut.picquick.vo;

import com.cqut.picquick.entity.Picture;
import com.cqut.picquick.entity.User;
import com.qiniu.util.Auth;
import com.qiniu.util.Base64;
import com.qiniu.util.UrlSafeBase64;
import lombok.Data;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : HK意境
 * @ClassName : PictureVo
 * @date : 2021/9/19 13:04
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
public class PictureVo {

    private Picture picture ;
    private User user ;

}


