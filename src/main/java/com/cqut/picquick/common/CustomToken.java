package com.cqut.picquick.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : HK意境
 * @ClassName : CustomToken
 * @date : 2021/9/15 16:04
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
public class CustomToken {

    private String accessToken;

    private Integer expiresIn;

    private String refreshToken;

}
