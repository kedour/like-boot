package com.like.common.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Excel注解定义
 *
 * @author ThinkGem
 * @version 2013-03-10
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ListExcelField {
    String titleOne() default "";
    /**
     * 如果是对象支持配置导出子项
     */
    ExcelField[] subFields();
    /**
     * 字段类型（0：导出导入；1：仅导出；2：仅导入）
     */
    int type() default 0;
    /**
     * 导出字段字段排序（升序）
     */
    int sort() default 0;
}