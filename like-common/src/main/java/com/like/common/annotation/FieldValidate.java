package com.like.common.annotation;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @ClassName FieldValidate
 * @Description
 * @Author like
 * @Date 2020/10/12 18:45
 * @Version v1.0
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
/**
 * 验证类型
 */
public @interface FieldValidate {
    //类选择器 用于验证匹配 如果无值 取Java 类型
    String className() default "";

    //是否必填
    boolean required() default true;

    //最长字符
    int maxlength() default 255;

    //最大值 当验证类型为int 时
    int max() default 2147483647;

}
