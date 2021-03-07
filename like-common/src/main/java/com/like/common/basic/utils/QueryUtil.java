package com.like.common.basic.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.like.common.annotation.DColumn;
import com.like.common.annotation.OperaterType;
import com.like.common.utils.BeanUtil;
import com.like.common.utils.StringUtils;
import com.like.common.vo.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @ClassName QueryUtil
 * @Description
 * @Author like
 * @Date 2020/10/12 18:23
 * @Version v1.0
 */


@Slf4j
public class QueryUtil {
    /**
     * 将查询参数构造like查询
     *
     * @return QueryWrapper
     */
    public static QueryWrapper createQueryWarapper(Object t) {
        //获取不为null的查询参数
        Map<String, Map<String,Object>> queryParam = getQueryMap(t);

        for (String key: queryParam.keySet()) {
            log.debug("封装查询参数[key:{}, value:{}]", key, queryParam.get(key).get("value"));
        }


        //没有查询参数
        if (queryParam.isEmpty()) {
            return new QueryWrapper();
        }

        //有查询参数
        QueryWrapper queryWrapper = new QueryWrapper();
        for (String key : queryParam.keySet()) {
            //获取key 对应的Field 找到 查询类型 。如果没有查询类型配置 默认like
            String value=queryParam.get(key).get("value").toString();
            Field field=(Field)queryParam.get(key).get("field");
            DColumn d= field.getAnnotation(DColumn.class);
            if(key.toLowerCase().equals("seq_no")){
                //如果是全国 就不查询了
                if(value.equals(".307239438969348097.")){
                    continue;
                }
                String[] seqNos=value.split("\\,");
                StringBuffer sqlBuffer=new StringBuffer();
                for(String seqNo: seqNos){
                    if(StringUtils.isBlank(seqNo)){
                        continue;
                    }
                    sqlBuffer.append(key+" like "+"'"+seqNo+"%"+"'");
                    sqlBuffer.append(" or ");
                }
                if(sqlBuffer.toString().endsWith(" or ")){
                    String fseql=sqlBuffer.toString().substring(0,sqlBuffer.length()-4);
                    queryWrapper.apply(fseql);
                }
                // queryWrapper.apply(key+" like {0} or like {1}", seqNos[0]+"%",seqNos[1]);

            }else if(key.toLowerCase().equals("id")){
                queryWrapper.eq(key, value.trim());
            }else {
                if(d!=null&&d.querytype()== OperaterType.eq){
                    queryWrapper.eq(key, value.trim());
                }else {
                    queryWrapper.like(key, value.trim());
                }
            }

        }
        log.info("查询条件个数-"+queryWrapper.getSqlSet());
        log.info("查询条件个数-"+queryWrapper.getSqlSelect());
        log.info("查询条件个数-"+queryWrapper.getCustomSqlSegment());
        log.info("查询条件个数-"+queryWrapper.getParamNameValuePairs());
        log.info("查询条件个数-"+queryWrapper.getParamNameValuePairs().size());
        return queryWrapper;
    }
    /**
     * 通过反射获取对象的字段名及值
     *
     * @param o page对象
     * @return 键值对
     */
    private static Map<String, Map<String,Object>> getQueryMap(Object o) {
        Map<String, Map<String,Object>> map = new HashMap<>(16);

        Class clazz = o.getClass();
        List<Field> fields = BeanUtil.getFields(clazz,new ArrayList());
        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            field.setAccessible(true);
            try {
                Object val = field.get(o);
                if (val != null&&!val.toString().equals("")) {
                    String fieldType= BeanUtil.getRefType(field);
                    if(fieldType.equals("refObject")){
                        continue;
                    }
                    String fieldName=  StringUtils.camel2Underline(field.getName());
                    Map<String,Object> fieldMap=new HashMap<>();
                    fieldMap.put("value",val);
                    fieldMap.put("field",field);
                    map.put(fieldName, fieldMap);
                }

            } catch (IllegalAccessException e) {

                log.error("转换page查询参数异常[filedName:{}, errorMessage:{}]", field.getName(), e.getMessage());
                e.printStackTrace();
            }
        }
        return map;
    }

    /**
     * 添加区域选项
     * @return
     */
    public static   Object addAreaFilter(Object t, LoginUser loginUser){
        try {
            Field areaField=BeanUtil.getField(t.getClass(),"area");
            if(areaField==null){
                return t;
            }
            Object area= PropertyUtils.getProperty(t,areaField.getName());
            if(loginUser!=null){
//                if(area==null){
//                    SysArea sysArea=new SysArea();
//                    String mainAreaSeqNo=loginUser.getMainAreaSeqNo();
//                    if(StringUtils.isBlank(mainAreaSeqNo)){
//                        mainAreaSeqNo=loginUser.getAreaSeqNo();
//                    }
//                    sysArea.setSeqNo(mainAreaSeqNo);
//                    PropertyUtils.setProperty(t,areaField.getName(),sysArea);
//                }else {
//                    Object seqNo=PropertyUtils.getProperty(area,"seqNo");
//                    if(seqNo==null||seqNo.equals("")){
//                        String mainAreaSeqNo=loginUser.getMainAreaSeqNo();
//                        if(StringUtils.isBlank(mainAreaSeqNo)){
//                            mainAreaSeqNo=loginUser.getAreaSeqNo();
//                        }
//                        PropertyUtils.setProperty(area,"seqNo",mainAreaSeqNo);
//                    }
//
//                }
            }

        }catch (Exception e){
            e.printStackTrace();
            log.info("not exist area property");
        }
        return t;
    }


    public static Map<String, QueryWrapper> getRefQueryMap(Object o){
        Map<String, QueryWrapper> map = new HashMap<>(16);
        Class clazz = o.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            try {
                Object val = field.get(o);
                if (val != null) {
                    String fieldType= BeanUtil.getRefType(field);
                    if(fieldType.equals("refObject")){
                        //如果是对象 先把对象查询出来
                        QueryWrapper queryWrapper= createQueryWarapper(val);
                        map.put(field.getName(),queryWrapper);

                    }

                }

            } catch (IllegalAccessException e) {

                log.error("转换page查询参数异常[filedName:{}, errorMessage:{}]", field.getName(), e.getMessage());
                e.printStackTrace();
            }
        }
        return  map;
    }



}

