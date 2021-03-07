package com.like.common.utils;

/**
 * Created with IntelliJ IDEA.
 *
 * @ClassName BeanUtil
 * @Description
 * @Author like
 * @Date 2020/10/12 18:25
 * @Version v1.0
 */

import com.google.common.collect.Lists;
import com.like.common.annotation.ExcelField;
import com.like.common.annotation.ListExcelField;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BeanUtil {
    //获取一个类的所有字段  包括从父类继承的字段

    public static List<Field> getFields(Class clas, List list) {
        Field[] fields = clas.getDeclaredFields();
        for (Field filed : fields) {
            if(filed.getName().equals("serialVersionUID")){
                continue;
            }
            list.add(filed);
        }
        Class supperclass = clas.getSuperclass();
        if (supperclass != null  && !supperclass.getName().startsWith("com.baomidou.mybatisplus")) {
            getFields(supperclass, list);
        }
        return list;
    }
    //获取一个类的所有字段  包括从父类继承的字段

    public static Field getField(Class clas,String name) {
        List<Field> filds= getFields(clas,new ArrayList());
        for(Field field: filds){
            if(field.getName().equals(name)){
                return field;
            }
        }
        return null;
    }
    // 获取引用类型 collectionType 集合类型 or singleType 基础数据类型或者多对一
    public static String getRefType(Field field) {
        if (field == null) {
            return null;
        }
        Type type = field.getGenericType();
        String typeString = type.toString();
        if (typeString.contains("java.util.Set")
                || typeString.contains("java.util.List")
                || typeString.contains("java.util.Collection")) {
            // 集合字段
            return "collectionType";
        } else {
            if (typeString.contains("java.lang")
                    || typeString.contains("java.utils") || typeString.contains("java.math") || typeString.equals("int") || typeString.equals("long") || typeString.equals("boolean") || typeString.equals("double")) {
                // 普通字段
                return "singleField";
            } else {
                // 引用对象
                return "refObject";
            }
        }
    }
    // 获取引用类型 collectionType 集合类型 or singleType 基础数据类型或者多对一
    public static String getFieldType(Field field) {
        if (field == null) {
            return null;
        }
        Type type = field.getGenericType();
        String typeString = type.toString();
        return typeString;
    }

    /**
     * 解析获取所有excelFields ListExcelFields的字段
     * type 0 导出 1 导入
     * @return
     */
    public static List<Object[]> getExcelFields(Class cls,int type){
        /**
         * 注解列表（Object[]{ ExcelField, Field/Method }）
         */
        List<Object[]> annotationList = Lists.newArrayList();
        List<Field> fs = BeanUtil.getFields(cls,new ArrayList());
        for (Field f : fs) {
            ExcelField ef = f.getAnnotation(ExcelField.class);
            ListExcelField listef = f.getAnnotation(ListExcelField.class);
            if(type==0){
                if ((ef != null&&ef.type()!=2)|(listef!=null&&listef.type()!=2)) {
                    if(ef != null){
                        annotationList.add(new Object[]{ef, f});
                    }else {
                        annotationList.add(new Object[]{listef, f});
                    }
                }
            }else {
                if ((ef != null&&ef.type()!=1)|(listef!=null&&listef.type()!=1)) {
                    if(ef != null){
                        annotationList.add(new Object[]{ef, f});
                    }else {
                        annotationList.add(new Object[]{listef, f});
                    }
                }
            }


        }
        return annotationList;

    }


    /**
     * 判断字段名称 是否配置excel注解
     * @param fileName
     * @param className
     * @return
     */
    public static boolean contailExcelField(String fileName,Class className){
        Field field=Reflections.getField(className,fileName);
        //判断当前属性是否具有excelField 或者ListExcelField属性
        ExcelField columnsAno =  field.getAnnotation(ExcelField.class);
        ListExcelField listcolumnsAno =  field.getAnnotation(ListExcelField.class);
        if(columnsAno!=null||listcolumnsAno!=null){
            return true;
        }
        return false;
    }

}

