package com.like.common.annotation;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @ClassName DelType
 * @Description
 * @Author like
 * @Date 2020/10/12 18:40
 * @Version v1.0
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DelType {
    //删除类型 0 默认 逻辑删除 1 物理删除
    int value() default 0;
}