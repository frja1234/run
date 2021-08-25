package com.scy.running.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * InnoDB free: 11264 kB; (`role_id`) REFER `scy_running_app/tb_role`(`role_id`)
 * </p>
 *
 * @author scy
 * @since 2021-07-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class TbUser extends Model {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id")
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    private Integer roleId;
    private String roleToken;

    @ApiModelProperty(value = "用户昵称")
    private String userNickName;

    @ApiModelProperty(value = "用户qq号")
    private String userQq;

    @ApiModelProperty(value = "用户微信号")
    private String userVx;

    @ApiModelProperty(value = "用户电话")
    private String userCellPhone;

    @ApiModelProperty(value = "用户账号密码")
    private String userPassword;

    @ApiModelProperty(value = "用户创建时间")
    private Date createTime;

    @ApiModelProperty(value = "用户创建人")
    private Integer createUser;

    @ApiModelProperty(value = "用户最近一次修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "用户最近一次修改人")
    private Integer updateUser;

    @ApiModelProperty(value = "用户最近一次登录时间")
    private Date lastLoginTime;

    @ApiModelProperty(value = "用户最近一次登录ip")
    private String lastLoginIp;

    @ApiModelProperty(value = "用户最近一次登录方式")
    private Integer lastLoginMode;

    private TbRole role;

    @ApiModelProperty(value = "用户激活0 / 注销1 标志", example = "0")
    private Integer defFlag; // 删除标志，0代表激活，1代表注销

    @ApiModelProperty(value = "学号 / 教工号")
    private String userNum; // 学号 / 教工号， 取决于角色是学生还是教师

    @ApiModelProperty(value = "用户真实姓名")
    private String userRealName; // 用户真实姓名

//    @ApiModelProperty(value = "所属教师")
    private Integer teacherId; // 如果是学生，会有所属教师

    private TbUser teacher;  // 如果是学生，会有所属教师

    private String img; // 照片的oss路径

    private String collegeName; // 学院名称
    private String className; // 班级名称

    private Integer classId; // 所属班级

}
