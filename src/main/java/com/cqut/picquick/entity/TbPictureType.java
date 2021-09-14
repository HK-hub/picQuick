package com.cqut.picquick.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 图片类型表
 * </p>
 *
 * @author hk
 * @since 2021-09-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_picture_type")
@ApiModel(value="TbPictureType对象", description="图片类型表")
public class TbPictureType implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "图片类型名称")
    private String name;

    @ApiModelProperty(value = "图片类型说明")
    private String description;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
