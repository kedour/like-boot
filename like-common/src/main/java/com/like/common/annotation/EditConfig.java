package com.like.common.annotation;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @ClassName EditConfig
 * @Description
 * @Author like
 * @Date 2020/10/12 18:41
 * @Version v1.0
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface EditConfig {
    boolean enableAdd() default true;
    boolean enableEdit() default true;
}

