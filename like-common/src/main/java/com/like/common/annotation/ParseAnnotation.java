package com.like.common.annotation;

import com.like.common.utils.BeanUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @ClassName ParseAnnotation
 * @Description
 * @Author like
 * @Date 2020/10/12 18:44
 * @Version v1.0
 */

@Slf4j
public class ParseAnnotation {
    public String parseNeedInterceptor(Method method) {
        NeedInterceptor needInterceptor = method.getAnnotation(NeedInterceptor.class);
        if (needInterceptor == null) {
            return null;
        }
        String value = "";
        if (needInterceptor != null) {
            value = needInterceptor.value();
        }
        return value;
    }

    public static List<Object> parseListColumns(Class className) {
        ListColumns listColumnsAno = (ListColumns) className.getAnnotation(ListColumns.class);
        if (listColumnsAno == null) {
            return new ArrayList<>();
        }
        DColumn[] listColumns = listColumnsAno.columns();
        List<Object> list = new ArrayList<>();
        for (DColumn column : listColumns) {
            Map<String, Object> map = new HashMap<>();
            map.put("columnName", column.columnName());
            map.put("columnTitle", column.columnTitle());
            if(column.subDcolumn().length>0){
                List<Map> maps=new ArrayList<>();
                for(SubDColumn subColumn:column.subDcolumn()){
                    Map<String, Object> submap = new HashMap<>();
                    submap.put("columnName", subColumn.columnName());
                    submap.put("columnTitle", subColumn.columnTitle());
                    maps.add(submap);
                }
                map.put("sub",maps);
            }
            list.add(map);
        }
        return list;
    }

    //获取列表的显示形式 带分页或者不带分页
    public static int parselistType(Class className) {
        int listType = 0;
        ListColumns listColumnsAno = (ListColumns) className.getAnnotation(ListColumns.class);
        if (listColumnsAno == null) {
            return listType;
        }
        listType = listColumnsAno.listType();
        return listType;
    }

    //获取表注释
    public static int getDelType(Class className) {
        TableComment tableColumnsAno = (TableComment) className.getAnnotation(TableComment.class);
        int delType=0;
        if(tableColumnsAno!=null){
            delType=tableColumnsAno.delType();
        }
        return delType;
    }
    //获取属性深度
    public static int getDepth(Class className) {
        TableComment tableColumnsAno = (TableComment) className.getAnnotation(TableComment.class);
        int depth=2;
        if(tableColumnsAno!=null){
            depth=tableColumnsAno.depth();
        }
        return depth;
    }
    //获取最后一级别的field
    public static Field getFields(String queryColumn, Class clz) {
        String[] qcs = queryColumn.split("\\.");
        String sc = qcs[0];
        try {
            //getFields()：获得某个类的所有的公共（public）的字段，包括父类中的字段。
            //getDeclaredFields()：获得某个类的所有声明的字段，即包括public、private和proteced，但是不包括父类的申明字段。
            Field field=null;
            try{
                field = clz.getDeclaredField(sc);
            }catch (Exception e){
                field =clz.getField(sc);
            }

            while (qcs.length > 1) {
                Class declaringclass = field.getType();
                return getFields(queryColumn.replace(sc + ".", ""), declaringclass);
            }
            return field;
        } catch (NoSuchFieldException e) {
            // e.printStackTrace();
            //取父类的方法
            try {
                Field field = clz.getField(sc);
                return field;
            } catch (NoSuchFieldException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }

    //获取一个类的所有验证字段
    public static List<Field> getValidateFields(Class entityClass) {
        List<Field> fields = BeanUtil.getFields(entityClass, new ArrayList());
        List validateFields = new ArrayList();
        for (Field field : fields) {
            FieldValidate fieldValidate = field.getAnnotation(FieldValidate.class);
            if (fieldValidate != null) {
                validateFields.add(field);
            }
        }
        return validateFields;
    }


    public static ExcelField  getExcelField(Field field){
        ExcelField excelField=field.getAnnotation(ExcelField.class);
        if(excelField==null){
            return null;
        }
        return excelField;
    }

    public static List<Field> getSortField(Class classname){
        List<Field> fields=BeanUtil.getFields(classname,new ArrayList());
        List<Field> uniqueField=new ArrayList<>();
        for(Field field:fields){
            SortField column =field.getAnnotation(SortField.class);
            if(column!=null){
                uniqueField.add(field);
            }
        }
        return uniqueField;
    }
    public static List<Field> getJoinFetchFields(Class classname){
        List<Field> fields= BeanUtil.getFields(classname,new ArrayList());
        List<Field> joinFetchFieldField=new ArrayList<>();
        for(Field field:fields){
            JoinFetchField column =field.getAnnotation(JoinFetchField.class);
            if(column!=null){
                joinFetchFieldField.add(field);
            }
        }
        return joinFetchFieldField;
    }

}

