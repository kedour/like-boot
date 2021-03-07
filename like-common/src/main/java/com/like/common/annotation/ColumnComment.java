package com.like.common.annotation;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @ClassName ColumnComment
 * @Description
 * @Author like
 * @Date 2020/10/12 18:41
 * @Version v1.0
 */
@Documented
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnComment {
    String comment() default "";
    //输入提示
    String tips() default "";
    //是否必填
    boolean required() default false;
    //排序
    int sort() default 0;
}