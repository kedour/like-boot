package com.like.test;

import java.lang.annotation.Documented;

/**
 * Created with IntelliJ IDEA.
 *
 * @ClassName Annotation
 * @Description
 * @Author like
 * @Date 2020/10/12 23:06
 * @Version v1.0
 */
@Documented
public @interface Annotation {
    String value() default "22";
}
