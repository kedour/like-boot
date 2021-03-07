package com.like.common.annotation;
import java.lang.annotation.*;
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited

/**
 * Created with IntelliJ IDEA.
 *
 * @ClassName SubDColumn
 * @Description
 * @Author like
 * @Date 2020/10/12 18:35
 * @Version v1.0
 */
public @interface SubDColumn {
    //属性名称
    String columnName() default "";

    //属性标题
    String columnTitle() default "";
}
