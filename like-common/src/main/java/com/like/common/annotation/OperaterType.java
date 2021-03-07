package com.like.common.annotation;

/**
 * Created with IntelliJ IDEA.
 *
 * @ClassName OperaterType
 * @Description
 * @Author like
 * @Date 2020/10/12 18:37
 * @Version v1.0
 */
public enum OperaterType {
    //等于
    eq,
    //模糊查询
    like,
    //大于
    gt,
    //小于
    lt,
    //范围查询
    range,
    //不在范围
    notin,
    // 在范围
    in,
    //is
    is,
    //not is
    isnot;

    private OperaterType() {
    }
}
