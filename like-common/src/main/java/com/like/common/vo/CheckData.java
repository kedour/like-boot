package com.like.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author like
 * @Title: CheckData
 * @ProjectName yingfen
 * @Description: TODO
 * @date 2019-10-11 22:38
 */
@Getter
@Setter
public class CheckData {
    @ApiModelProperty(value = "id")
    String id;
    @ApiModelProperty(value = "显示名称")
    String name;
}
