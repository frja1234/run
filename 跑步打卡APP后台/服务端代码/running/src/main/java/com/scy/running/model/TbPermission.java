package com.scy.running.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2021-07-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class TbPermission extends Model {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "权限id")
    @TableId(value = "permission_id", type = IdType.AUTO)
    private Integer permissionId;

    @ApiModelProperty(value = "父级权限id")
    private Integer parentPermissionId; // 父级权限的id

    @ApiModelProperty(value = "权限名称")
    private String permissionName;

    @ApiModelProperty(value = "权限路径")
    private String permissionPath;

    @ApiModelProperty(value = "权限创建时间")
    private Date createTime;

    @ApiModelProperty(value = "权限创建人")
    private Integer createUser;

    @ApiModelProperty(value = "权限最近一次修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "权限最近一次修改人")
    private Integer updateUser;


    private TbPermission parentPermission; // 父级权限

    @ApiModelProperty(value = "权限激活0/注销1标志", example = "0")
    private Integer defFlag; // 删除标志，0代表激活，1代表注销

}
