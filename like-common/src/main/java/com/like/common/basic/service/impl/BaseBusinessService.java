package com.like.common.basic.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.like.common.annotation.DelType;
import com.like.common.annotation.JoinFetchField;
import com.like.common.annotation.ParseAnnotation;
import com.like.common.basic.service.IBaseBusinessService;
import com.like.common.basic.utils.QueryUtil;
import com.like.common.basic.utils.SpringUtils;
import com.like.common.entity.BaseEntity;
import com.like.common.utils.BeanUtil;
import com.like.common.utils.Collections3;
import com.like.common.utils.StringUtils;
import com.like.common.vo.LoginUser;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @ClassName BaseBusinessService
 * @Description
 * @Author like
 * @Date 2020/10/12 18:22
 * @Version v1.0
 */


public class BaseBusinessService<T extends BaseEntity,M extends BaseMapper<T>, ID extends Serializable>   extends ServiceImpl<M, T> implements IBaseBusinessService<T,M,ID> {
    private Class<?> entityClass;
    public BaseBusinessService() {
        Class<?> c = this.getClass();
        Type t = c.getGenericSuperclass();
        if(((ParameterizedType) t)
                .getActualTypeArguments()[0].getTypeName().equals("T")){
            return;
        }
        if (t instanceof ParameterizedType && !c.getName().equals(BaseBusinessService.class.getName())) {
            this.entityClass = (Class<?>) ((ParameterizedType) t)
                    .getActualTypeArguments()[0];

        }
    }
    @Override
    public IPage findPage(Page page, T t, LoginUser loginUser, String ... columns) {
        Map<String, List> inQuery=  getRefQueryIn(t,loginUser);
        QueryWrapper queryWrapper= QueryUtil.createQueryWarapper(t);
        for(Map.Entry<String,List> entry:inQuery.entrySet()){
            String fieldName= StringUtils.camel2Underline(entry.getKey());
            if(StringUtils.isEmptyList(entry.getValue())){
                queryWrapper.eq(fieldName,"not exist");
            }else {
                queryWrapper.in(fieldName,entry.getValue());
            }
        }
        queryWrapper.select(columns);
        queryWrapper.orderByAsc("sort");
        queryWrapper.orderByDesc("create_time");
        queryWrapper.eq("del_flag",0);
        IPage iPage= this.page(page,queryWrapper);
        List<Object> vos=vos(iPage.getRecords());
        iPage.setRecords(vos);
        return iPage;
    }

    @Override
    public List findList(T t, LoginUser loginUser,String ... columns) {
        Map<String,List>  inQuery=  getRefQueryIn(t,loginUser);
        QueryWrapper queryWrapper= QueryUtil.createQueryWarapper(t);
        for(Map.Entry<String,List> entry:inQuery.entrySet()){
            String fieldName=StringUtils.camel2Underline(entry.getKey());
            if(StringUtils.isEmptyList(entry.getValue())){
                queryWrapper.eq(fieldName,"not exist");
            }else {
                queryWrapper.in(fieldName,entry.getValue());
            }

        }
        queryWrapper.select(columns);
        queryWrapper.orderByAsc("sort");
        queryWrapper.orderByDesc("create_time");
        queryWrapper.eq("del_flag",0);
        return vos(this.list(queryWrapper));
    }

    @Override
    public Object findObj(ID id) {
        T t= (T) this.getById(id);
        Object  vo= revertEntityToVo(t);
        return vo;
    }

    @Override
    public void del(List<ID> reids, LoginUser loginUser) {
        DelType delType= entityClass.getAnnotation(DelType.class);
        if(delType!=null&&delType.value()==1){
            this.removeByIds(reids);
        }else {
            try {
                T t= (T) entityClass.newInstance();
                t.setDelFlag(1);
                QueryWrapper queryWrapper=new QueryWrapper();
                queryWrapper.in("id",reids);
                this.update(t,queryWrapper);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }

    }


    @Override
    public JSONArray checkData(T t, LoginUser loginUser, String... column) {
        String nameCoumn="name";
        if(column==null || column.length==0){
            return checkDataForFeild(t,loginUser,nameCoumn);
        }
        return checkDataForFeild(t,loginUser,column);
    }

    public JSONArray checkDataForFeild(T t, LoginUser loginUser,String... column) {
        Map<String,List>  inQuery=  getRefQueryIn(t,loginUser);
        QueryWrapper queryWrapper= QueryUtil.createQueryWarapper(t);
        for(Map.Entry<String,List> entry:inQuery.entrySet()){
            String fieldName=StringUtils.camel2Underline(entry.getKey());
            if(StringUtils.isEmptyList(entry.getValue())){
                queryWrapper.eq(fieldName,"not exist");
            }else {
                queryWrapper.in(fieldName,entry.getValue());
            }

        }
        queryWrapper.orderByAsc("sort");
        queryWrapper.orderByDesc("create_time");
        queryWrapper.eq("del_flag",0);
        String columnStr=String.join(",", column);
        queryWrapper.select("id",this.underscoreName(columnStr));
        List<T> list=this.list(queryWrapper);
        JSONArray JSONArray=new JSONArray();
        for(T ta: list){
            JSONObject jsonObject=new JSONObject();
            String name="";
            try {
                for(int i=0,length=column.length; i<length; i++){
                    name +=  PropertyUtils.getProperty(ta,column[i]).toString();
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            jsonObject.put("id",ta.id);
            jsonObject.put("name",name);
            JSONArray.add(jsonObject);
        }
        return JSONArray;
    }


    /**
     * 获取join对象的 属性及对应的 in 可选值
     * @return
     */
    public  Map<String, List> getRefQueryIn(T t,LoginUser loginUser){
        t= (T) QueryUtil.addAreaFilter(t,loginUser);
        Map<String, QueryWrapper> queryWrapperList=QueryUtil.getRefQueryMap(t);
        Map<String,List> map=new HashMap<>();
        for(Map.Entry<String,QueryWrapper> entry:queryWrapperList.entrySet()){
            try {
                String fieldName=entry.getKey();
                QueryWrapper queryWrapper=entry.getValue();
                if(queryWrapper.getParamNameValuePairs().size()==0&&queryWrapper.getCustomSqlSegment().equals("")){
                    continue;
                }
                Field field=t.getClass().getDeclaredField(fieldName);
                JoinFetchField joinFetchFieldAno=field.getAnnotation(JoinFetchField.class);
                String mapby=joinFetchFieldAno.mappedBy();
                String joinOnField=joinFetchFieldAno.joinOnField();
                String refClassName= joinFetchFieldAno.targetEntity().getSimpleName();
                IService service= (IService) SpringUtils.getBean(refClassName+"Service");
                queryWrapper.select(StringUtils.camel2Underline(joinOnField));
                List<Object> refobj= service.list(queryWrapper);
                List<String> propertys= Collections3.extractToList(refobj,joinOnField);
                String joinOnfieldValues= Collections3.extractToString(refobj,joinOnField,",");
                System.out.println(joinOnfieldValues);
                map.put(mapby,propertys);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }

        }
        return map;
    }

    /**
     * 将驼峰式命名的字符串转换为下划线大写方式。如果转换前的驼峰式命名的字符串为空，则返回空字符串。</br>
     * 例如：HelloWorld->HELLO_WORLD
     * @param name 转换前的驼峰式命名的字符串
     * @return 转换后下划线大写方式命名的字符串
     */
    private String underscoreName(String name) {
        StringBuilder result = new StringBuilder();
        if (name != null && name.length() > 0) {
            // 将第一个字符处理成大写
            result.append(name.substring(0, 1));
            // 循环处理其余字符
            for (int i = 1; i < name.length(); i++) {
                String s = name.substring(i, i + 1);
                // 在大写字母前添加下划线
                if (s.equals(s.toUpperCase()) && !Character.isDigit(s.charAt(0)) && !s.equals(",")) {
                    result.append("_");
                }
                // 其他字符直接转成大写
                result.append(s.toLowerCase());
            }
        }
        return result.toString();
    }

    /**
     * 根据entity 返回 vo对象
     * @return
     */
    public List<Object> vos(List records){
        List<Object> vos=new ArrayList<>();
        for(Object entity: records){
            Object  vo= revertEntityToVo(entity);
            vos.add(vo);
        }
        return vos;
    }
    /**
     * 根据entity 返回 vo对象
     * @return
     */
    public <T>  List<T> vos(List records,Class<T> clazz){
        List<T> vos=new ArrayList<>();
        for(Object entity: records){
            T  vo= revertEntityToVo(entity,clazz);
            vos.add(vo);
        }
        return vos;
    }
    @Override
    public Object revertEntityToVo(Object entity){
        try {
            String entityName=entity.getClass().getName();
            String voName=entityName.replace(".entity",".vo");
            voName=voName.concat("VO");
            log.debug(voName);

            Object vo= null;
            try {
                vo = Class.forName(voName).newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            BeanUtils.copyProperties(entity,vo);
            //封装vo的关联对象 从entity的普通字段外键值取
            getRefObj(vo,entity);
            return vo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }
    public <T> T  revertEntityToVo(Object entity,Class<T> clazz){
        try {
            String entityName=entity.getClass().getName();
            String voName=entityName.replace(".entity",".vo");
            voName=voName.concat("VO");
            log.debug(voName);

            Object vo= null;
            try {
                vo = Class.forName(voName).newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            BeanUtils.copyProperties(entity,vo);
            //封装vo的关联对象 从entity的普通字段外键值取
            getRefObj(vo,entity);
            return (T)vo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  (T)entity;
    }
    public Object getRefObj(Object vo,Object entity){
        List<Field> joinFields= ParseAnnotation.getJoinFetchFields(vo.getClass());
        for(Field joinField:joinFields){
            JoinFetchField joinFetchField=joinField.getAnnotation(JoinFetchField.class);
            String property= joinFetchField.mappedBy();
            try {
                Object value= PropertyUtils.getProperty(entity,property);
                if(value!=null){
                    //属性的引用类型
                    String refType = BeanUtil.getRefType(joinField);
                    Object refvo=null;
                    // 如果是多对一 或者一对一
                    if (refType.equals("refObject")) {
                        refvo=getRefObject(joinField, value.toString());
                    } else if (refType.equals("collectionType")){
                        refvo=getRefArray(joinField,  value.toString());
                    }else {
                        continue;
                    }
                    PropertyUtils.setProperty(vo,joinField.getName(),refvo);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return vo;
    }

    /**
     * 获取集合应用对象
     * @return
     */
    public Object getRefArray(Field joinField,String values){
        String [] vals=values.split(",");
        List collection=new ArrayList();
        for(String value:vals){
            Object refvo= getRefObject(joinField, value);
            collection.add(refvo);
        }
        return collection;
    }
    /**
     * 获取集合应用对象
     * @return
     */
    public Object getRefObject(Field joinField,String value) {
        if (value != null) {
            JoinFetchField joinFetchField = joinField.getAnnotation(JoinFetchField.class);
            //通过属性值 查找对象
            String refClassName = joinFetchField.targetEntity().getSimpleName();
            String joinOnField = joinFetchField.joinOnField();
            IService service = (IService) SpringUtils.getBean(refClassName + "Service");
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq(StringUtils.camel2Underline(joinOnField), value.toString());
            Object refentity = service.getOne(queryWrapper);
          /*  Object refvo = null;
            try {
                refvo = joinField.getType().newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            BeanUtils.copyProperties(refentity, refvo);
            return refvo;*/
            return refentity;
        }
        return null;
    }
    @Override
    public Boolean businessSaveOrUpdate(Object t){
        T entity= (T) revertVoToEntity(t);
        entity.initTime();
        return   this.saveOrUpdate(entity);
    }
    @Override
    public boolean saveOrUpdate(T entity) {
        entity.initTime();
        return   super.saveOrUpdate(entity);
    }
    //将 vo  还原成 entity
    @Override
    public T revertVoToEntity(Object t){
        String voName=t.getClass().getName();
        String entityName=voName.replace(".vo",".entity");
        entityName=entityName.substring(0,entityName.length()-2);
        log.debug(entityName);

        Object entity= null;
        try {
            entity = Class.forName(entityName).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        BeanUtils.copyProperties(t,entity);

        List<Field> joinFields= ParseAnnotation.getJoinFetchFields(t.getClass());

        for(Field joinField:joinFields){
            try {
                JoinFetchField joinFetchField=joinField.getAnnotation(JoinFetchField.class);
                String property= joinFetchField.mappedBy();
                String joinOnField=joinFetchField.joinOnField();
                Object value = PropertyUtils.getProperty(t,joinField.getName()+"."+ joinOnField);
                PropertyUtils.setProperty(entity,property,value);
            } catch (Exception e) {
//                e.printStackTrace();
                log.error(e.getMessage());
                continue;
            }


        }
        return (T) entity;
    }
}
