package com.scy.running.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class MyVo {

    @ApiModelProperty(value = "每页大小", example = "10")
    private long size = 10; // 每页大小

    @ApiModelProperty(value = "当前页码", example = "1")
    private long current = 1; // 当前页码

}
