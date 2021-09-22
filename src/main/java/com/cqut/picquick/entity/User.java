package com.cqut.picquick.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.apache.solr.client.solrj.beans.Field;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author hk
 * @since 2021-09-14
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ToString
@ApiModel(value="TbUser对象", description="用户表")
@TableName(value = "tb_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Field("id")
    @TableId(type = IdType.ASSIGN_ID)
    @TableField(fill = FieldFill.INSERT)
    private String id ;

    @Field("user_username")
    @ApiModelProperty(value = "用户名称，有默认值，可以通过绑定QQ， Email ,手机号进行修改")
    private String username;

    @ApiModelProperty(value = "用户账户，为注册时候默认分配")
    //@TableField(fill = FieldFill.INSERT)
    @Field("user_account")
    private String account;

    @Field("user_password")
    @ApiModelProperty(value = "用户密码，有默认值")
    private String password;

    @Field("user_avatar_url")
    @ApiModelProperty(value = "用户头像地址，有默认值")
    private String avatarUrl;

    @Field("user_userinfo")
    @ApiModelProperty(value = "用户个人信息")
    //@TableField(fill = FieldFill.INSERT)
    private String userInfo;

    @Field("user_email")
    @ApiModelProperty(value = "用户邮箱，没有默认值，可以为空")
    private String email;

    @Field("user_qq")
    @ApiModelProperty(value = "用户QQ账号，可以使用QQ 进行登录")
    private String qq;

    @Field("user_telephone")
    @ApiModelProperty(value = "用户电话号码，注册时绑定")
    private String telephone;

    @Field("user_type")
    @ApiModelProperty(value = "用户权限类型：3普通用户，2vip用户，1管理员用户，0根用户")
    //@TableField(fill = FieldFill.INSERT)
    private Integer type;

    @Field("user_token")
    @ApiModelProperty(value = "用户token")
    private String token;

    @Field("user_deleted")
    @TableLogic
    @ApiModelProperty(value = "逻辑删除; 0未删除， 1 删除")
    private Boolean deleted;

    @Field("user_update_time")
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "用户修改时间，更新时间")
    private LocalDateTime updateTime;

    @Field("user_create_time")
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    @ApiModelProperty(value = "用户创建时间")
    private LocalDateTime createTime;


}
