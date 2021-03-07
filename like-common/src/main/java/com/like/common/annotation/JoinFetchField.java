package com.like.common.annotation;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @ClassName JoinFetchField
 * @Description
 * @Author like
 * @Date 2020/10/12 18:38
 * @Version v1.0
 */
@Documented
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JoinFetchField {
    Class targetEntity() default void.class;
    String mappedBy() default "";
    String joinOnField() default "";
}
