package com.scy.running.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

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
public class TbTask extends Model {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "任务id")
    @TableId(value = "task_id", type = IdType.AUTO)
    private Integer taskId;

    @ApiModelProperty(value = "任务名称")
    private String taskName;

    @ApiModelProperty(value = "要求路程")
    private Integer taskRequireDistance;

    @ApiModelProperty(value = "要求时间")
    private Integer taskRequireTime;

    @ApiModelProperty(value = "截止时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date taskDeadline;

    @ApiModelProperty(value = "超时标志，0代表ing，1代表已过期")
    private Integer isOvertime;

    @ApiModelProperty(value = "任务创建时间")
    private Date createTime;

    @ApiModelProperty(value = "任务创建人")
    private Integer createUser;

    @ApiModelProperty(value = "任务最近一次修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "任务最近一次修改人")
    private Integer updateUser;


    private String selectClasses; // 展示选择的班级
}
