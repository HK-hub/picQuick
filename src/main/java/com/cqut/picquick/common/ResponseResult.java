package com.cqut.picquick.common;


import com.cqut.picquick.enums.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.ResultSet;

/**
 * 数据响应对象
 * {
 *     success：成功,
 *     code   ：响应码,
 *     message:返回信息,
 *     //响应数据
 *     data:{
 *
 *     }
 *
 * }
 *
 * **/

@Data
@ToString
@NoArgsConstructor
public class ResponseResult {


    //是否成功
    private boolean success;
    //返回码
    private Integer code;
    //返回信息
    private String message;
    //返回数据
    private Object data;

    public ResponseResult(ResultCode code) {
        this.success = code.success();
        this.code = code.code();
        this.message = code.message();
    }

    public ResponseResult(ResultCode code, Object data) {
        this.success = code.success();
        this.code = code.code();
        this.message = code.message();
        this.data = data;
    }

    public ResponseResult(Integer code, String message, boolean success) {
        this.code = code;
        this.message = message;
        this.success = success;
    }

    public ResponseResult setResultCode(ResultCode code){
        this.success = code.success();
        this.code = code.code();
        this.message = code.message();
        return this;
    }

    public ResponseResult setData(Object data){
        this.data = data ;
        return this;
    }


    public static ResponseResult SUCCESS(){
        return new ResponseResult(ResultCode.SUCCESS);
    }

    public static ResponseResult ERROR(){
        return new ResponseResult(ResultCode.SERVER_ERROR);
    }

    public static ResponseResult FAIL(){
        return new ResponseResult(ResultCode.FAIL);
    }
}
