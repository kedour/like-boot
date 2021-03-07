package com.like.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.like.common.entity.BaseEntity;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.like.entity.UserInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 用户信息表
 * </p>
 *
 * @author like
 * @since 2020-10-18
 */
@Data
@ToString
@Getter
@Setter
@ApiModel(value = "UserInfo对象", description = "用户信息表")
public class UserInfoVO extends BaseEntity<UserInfo> {


    @ApiModelProperty(value = "用户名称")
            private String userName;

    @ApiModelProperty(value = "手机号码")
            private String phone;

    @ApiModelProperty(value = "性别")
            private Integer sex;

    @ApiModelProperty(value = "年龄")
            private Integer age;

    @ApiModelProperty(value = "密码")
            private String password;

    @ApiModelProperty(value = "备注")
            private String remark;


    @Override
    protected Serializable pkVal(){
            return null;
        }

        }
