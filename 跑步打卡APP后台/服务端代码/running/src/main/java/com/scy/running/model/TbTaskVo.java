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
public class TbTaskVo extends Model {

    private static final long serialVersionUID = 1L;

    private Integer ended;  // 完成人数
    private Integer notEnd; // 未完成人数
}
