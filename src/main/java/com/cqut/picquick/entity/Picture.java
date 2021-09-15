package com.cqut.picquick.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.redis.core.types.Expiration;

/**
 * <p>
 * 图片
 * </p>
 *
 * @author hk
 * @since 2021-09-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_picture")
@ApiModel(value="TbPicture对象", description="图片")
public class Picture implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "图片名称,图片上传时原始名称")
    private String name;

    @ApiModelProperty(value = "图片在七牛云上存储的key")
    private String key ;

    @ApiModelProperty(value = "图片上传后返回的url链接")
    private String url;

    @ApiModelProperty(value = "图片的分类，默认没有分类")
    private Integer categoryId;

    @ApiModelProperty(value = "图片的私有，公开类型：1对应公开，0对应私有，默认私有")
    //@TableField(fill = FieldFill.INSERT)
    private Boolean type;

    @ApiModelProperty(value = "图片的文件大小")
    private Double size;

    @ApiModelProperty(value = "图片上传者用户ID")
    private String uploaderId;

    @ApiModelProperty(value = "图片在文件系统中保存的时间")
    private LocalDateTime saveTime;

    @ApiModelProperty(value = "图片的热度， 喜欢次数")
    private Integer likeCount;

    @TableLogic
    @ApiModelProperty(value = "逻辑删除：0未删除，1删除")
    private Boolean deleted;

    @ApiModelProperty(value = "图片过期时间")
    private Long expirationTime ;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "图片创建时间")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "图片修改，更新时间")
    private LocalDateTime updateTime;


}
