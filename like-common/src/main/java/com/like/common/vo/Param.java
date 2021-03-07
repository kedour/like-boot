package com.like.common.vo;

public class Param {
    //键
    String name;
    //值
    String value;
    //中文说明
    String remark;

    public Param(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public Param(String name, String value, String remark) {
        this.name = name;
        this.value = value;
        this.remark = remark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
