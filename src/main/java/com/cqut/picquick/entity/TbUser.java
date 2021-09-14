package com.cqut.picquick.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author hk
 * @since 2021-09-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="TbUser对象", description="用户表")
public class TbUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户名称，有默认值，可以通过绑定QQ， Email ,手机号进行修改")
    private String username;

    @ApiModelProperty(value = "用户账户，为注册时候默认分配")
    private String account;

    @ApiModelProperty(value = "用户密码，有默认值")
    private String password;

    @ApiModelProperty(value = "用户头像地址，有默认值")
    private String avatarUrl;

    @ApiModelProperty(value = "用户个人信息")
    private String userInfo;

    @ApiModelProperty(value = "用户邮箱，没有默认值，可以为空")
    private String email;

    @ApiModelProperty(value = "用户QQ账号，可以使用QQ 进行登录")
    private String qq;

    @ApiModelProperty(value = "用户电话号码，注册时绑定")
    private String telephone;

    @ApiModelProperty(value = "用户权限类型：3普通用户，2vip用户，1管理员用户，0根用户")
    private Integer type;

    @ApiModelProperty(value = "用户token")
    private String token;

    @ApiModelProperty(value = "逻辑删除; 0未删除， 1 删除")
    private Boolean deleted;

    @ApiModelProperty(value = "用户修改时间，更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "用户创建时间")
    private LocalDateTime createTime;


}
