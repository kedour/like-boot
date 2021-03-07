package com.like.common.constant;

/**
 * @author shoubing zhang
 * @Title: Regular
 * @ProjectName electric-manager
 * @Description: 常用的正则表达式
 * @date 2018/12/269:55
 */
public class Regular {
    /**
     * 验证邮箱
     */
    public static final String EMAIL="\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";
    /**
     * 验证手机
     */
    public static final String PHONE="0?(13|14|15|17|18|19)[0-9]{9}$";
    /**
     * 验证64位
     */
    public static final String MAX_LENGTH_64="^.{0,64}$";
    /**
     * 验证32位
     */
    public static final String MAX_LENGTH_32="^.{0,32}$";
    /**
     * 验证128位
     */
    public static final String MAX_LENGTH_128="^.{0,128}$";
    /**
     * 验证16位
     */
    public static final String MAX_LENGTH_16="^.{0,16}$";
    /**
     *验证纯数字
     */
    public static final String number="^[0-9]*$";

    /**
     *验证带小数位
     */
    public static final String doubleNumber="^-?[0-9]*\\.?[0-9]{1,4}$";
    /**
     * 最大10w
     */
    public static final int MAX_10 = 100000;
    /**
     * 最大100w
     */
    public static final int MAX_100 = 1000000;
    /**
     * 最大10000
     */
    public static final int MAX_1 = 10000;
    /**
     * 最大12
     */
    public static final int MAX_12 = 12;
    /**
     * 最大12
     */
    public static final int MAX_36 = 36;
}
