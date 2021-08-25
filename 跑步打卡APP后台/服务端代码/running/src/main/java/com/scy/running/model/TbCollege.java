package com.scy.running.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 *
 * @author scy
 * @since 2021-08-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class TbCollege extends Model {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "学院id")
    @TableId(value = "college_id", type = IdType.AUTO)
    private Integer collegeId;

    @ApiModelProperty(value = "学院名称")
    private String collegeName;

    @ApiModelProperty(value = "学院代码")
    private String collegeCode;

    @ApiModelProperty(value = "学院简介")
    private String collegeIntroduction;


}
