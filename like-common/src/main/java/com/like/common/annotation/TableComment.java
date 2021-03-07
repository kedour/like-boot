package com.like.common.annotation;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @ClassName TableComment
 * @Description
 * @Author like
 * @Date 2020/10/12 18:47
 * @Version v1.0
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface TableComment {
    //注释
    String comment() default "";
    //0 物理删除 1 逻辑删除
    int  delType() default 1;
    /**
     * 控制返回到前台的属性深度。1 - code 2 - staion.code 3 - station.area.name
     */
    int depth() default 2;
}