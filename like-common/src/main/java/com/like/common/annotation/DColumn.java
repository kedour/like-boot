package com.like.common.annotation;

/**
 * Created with IntelliJ IDEA.
 *
 * @ClassName DColumn
 * @Description
 * @Author like
 * @Date 2020/10/12 18:35
 * @Version v1.0
 */


import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DColumn {
    //属性名称
    String columnName() default "";

    //属性标题
    String columnTitle() default "";
    //下级
    SubDColumn[] subDcolumn() default  {};
    /**
     * 查询类型
     */
    OperaterType querytype() default OperaterType.like;
}