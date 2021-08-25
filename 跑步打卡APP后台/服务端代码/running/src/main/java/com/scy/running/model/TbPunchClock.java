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
public class TbPunchClock extends Model {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "打卡id")
    @TableId(value = "punch_clock_id", type = IdType.AUTO)
    private Integer punchClockId;

    @ApiModelProperty(value = "打卡人(学生id)")
    private Integer userId;

//    private TbUser student;

    /*@ApiModelProperty(value = "所属任务id")
    private Integer taskId;*/

    private TbTask task;

    private Integer taskId;

    @ApiModelProperty(value = "累积总路程")
    private Integer runTotalLength;

    @ApiModelProperty(value = "打卡状态")
    private Integer punchClockState;

    @ApiModelProperty(value = "累积总时长")
    private Integer runTotalTime;

    @ApiModelProperty(value = "打卡开始时间")
    private Date punchClockStartTime;

    @ApiModelProperty(value = "打卡结束时间")
    private Date punchClockEndTime;

    @ApiModelProperty(value = "任务创建时间")
    private Date createTime;

    @ApiModelProperty(value = "任务创建人")
    private Integer createUser;

    @ApiModelProperty(value = "任务最近一次修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "任务最近一次修改人")
    private Integer updateUser;

}
