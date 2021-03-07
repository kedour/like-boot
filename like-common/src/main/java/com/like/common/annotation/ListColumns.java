package com.like.common.annotation;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @ClassName ListColumns
 * @Description
 * @Author like
 * @Date 2020/10/12 18:46
 * @Version v1.0
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ListColumns {
    DColumn[] columns();

    //列表类型(分页或者列表 0 表示分页 1 表示不分页)
    int listType() default 0;
}

