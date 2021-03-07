package com.like.test;

/**
 * Created with IntelliJ IDEA.
 *
 * @ClassName Test01
 * @Description
 * @Author like
 * @Date 2020/10/12 23:07
 * @Version v1.0
 */
@Annotation(value = "11")
public class Test01 {
//    @AnnotationTest1("world")
    public void method() {
        System.out.println("usage of annotation");
    }
    public static void main(String[] args) {
        Test01 test01 = new Test01();
        test01.method();
    }

}
