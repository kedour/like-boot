package com.like.common.annotation;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @ClassName SortField
 * @Description
 * @Author like
 * @Date 2020/10/12 18:44
 * @Version v1.0
 */
@Documented
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SortField {
    //排序类型 1 升序  0 倒叙
    String sortType() default "desc";
}