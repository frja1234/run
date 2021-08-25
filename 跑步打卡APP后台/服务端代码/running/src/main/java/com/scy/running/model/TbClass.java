package com.scy.running.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 *
 * @author scy
 * @since 2021-08-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class TbClass extends Model {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "班级id")
    @TableId(value = "class_id", type = IdType.AUTO)
    private Integer classId;

    @ApiModelProperty(value = "班级名称")
    private String className;

    @ApiModelProperty(value = "班级代码")
    private String classCode;

    private Integer collegeId;

    /*@ApiModelProperty(value = "学院id")
    private Integer collegeId;*/

    private TbCollege college; // 所属学院

}
