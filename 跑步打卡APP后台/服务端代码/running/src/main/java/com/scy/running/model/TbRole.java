package com.scy.running.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * InnoDB free: 11264 kB
 * </p>
 *
 * @author scy
 * @since 2021-07-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class TbRole extends Model {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "角色id")
    @TableId(value = "role_id", type = IdType.AUTO)
    private Integer roleId;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "角色创建时间")
    private Date createTime;

    @ApiModelProperty(value = "角色创建人")
    private Integer createUser;

    @ApiModelProperty(value = "角色最近一次修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "角色最近一次修改人")
    private Integer updateUser;

    @ApiModelProperty(value = "角色代码")
    private String roleCode;

    @ApiModelProperty(value = "角色备注")
    private String remarks;

    @ApiModelProperty(value = "角色token")
    private String token;

    @ApiModelProperty(value = "角色激活0/注销1标志", example = "0")
    private Integer defFlag; // 删除标志，0代表激活，1代表注销

    private List<TbPermission> permissionList; // 一对多，角色权限
}
