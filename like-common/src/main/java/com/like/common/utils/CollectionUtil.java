package com.like.common.utils;

import java.util.List;

/**
 * @author shoubing zhang
 * @Title: CollectionUtil
 * @ProjectName electric-manager
 * @Description: TODO
 * @date 2019/1/8 16:43
 */
public class CollectionUtil {
    public static Object[] list2Arry(List list){
        Object[] strings = new Object[list.size()];
        return list.toArray(strings);
    }
    public static List arry2List(Object[] obj){
        return java.util.Arrays.asList(obj);
    }
}
