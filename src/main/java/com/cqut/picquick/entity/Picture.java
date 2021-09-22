package com.cqut.picquick.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.relational.core.mapping.Column;

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
    @Column(value = "id")
    private String id;

    @ApiModelProperty(value = "图片名称,图片上传时原始名称")
    @Column(value = "picture_name")
    private String pictureName;

    @ApiModelProperty(value = "图片在七牛云上存储的key")
    @Column(value = "file_key")
    private String fileKey ;


    @ApiModelProperty(value = "图片上传后返回的url链接")
    @Column(value = "url")
    private String url;

    @ApiModelProperty(value = "图片的分类，默认没有分类")
    @Column(value = "category_id")
    private Integer categoryId;

    @ApiModelProperty(value = "图片的私有，公开类型：1对应公开，0对应私有，默认私有")
    //@TableField(fill = FieldFill.INSERT)
    @Column(value = "type")
    private Boolean type;

    @ApiModelProperty(value = "图片的文件大小")
    @Column(value = "size")
    private Double size;

    @ApiModelProperty(value = "图片上传者用户ID")
    @Column(value = "uploadId")
    private String uploaderId;

    @ApiModelProperty(value = "图片在文件系统中保存的时间")
    @Column(value = "save_time")
    private LocalDateTime saveTime;

    @ApiModelProperty(value = "图片的热度， 喜欢次数")
    @Column(value = "like_count")
    private Integer likeCount;

    @TableLogic
    @ApiModelProperty(value = "逻辑删除：0未删除，1删除")
    @Column(value = "deleted")
    private Boolean deleted;

    @ApiModelProperty(value = "图片过期时间")
    @Column(value = "expiration_time")
    private Long expirationTime ;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "图片创建时间")
    @Column(value = "create_time")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "图片修改，更新时间")
    @Column(value = "update_time")
    private LocalDateTime updateTime;


}
