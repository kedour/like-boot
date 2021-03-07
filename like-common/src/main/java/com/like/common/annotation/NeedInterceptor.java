package com.like.common.annotation;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @ClassName NeedInterceptor
 * @Description
 * @Author like
 * @Date 2020/10/12 18:46
 * @Version v1.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface NeedInterceptor {
    String value() default "no";
}

