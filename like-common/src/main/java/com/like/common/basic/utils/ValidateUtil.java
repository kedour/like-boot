package com.like.common.basic.utils;


import com.like.common.annotation.FieldValidate;
import com.like.common.annotation.ParseAnnotation;
import com.like.common.utils.StringUtils;
import com.like.common.vo.JsonReturn;
import com.like.common.vo.Param;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 数据校验
 */
public class ValidateUtil {
    //保存时对实体对象的数据校验
    public static JsonReturn validate(Object obj){
        JsonReturn jsonReturn=new JsonReturn();
        List<Field> validates=  ParseAnnotation.getValidateFields(obj.getClass());
        for(Field fieldValidate:validates){
            //校验注解
            FieldValidate validate=fieldValidate.getAnnotation(FieldValidate.class);
           if(validate.required()){
              //验证数据是否非空
               try {
                Object value= PropertyUtils.getProperty(obj,fieldValidate.getName());
                if(value==null|| StringUtils.isEmpty(String.valueOf(value))){
                    jsonReturn.setFlag(false);
                    jsonReturn.setMessage(fieldValidate.getName()+"--非空校验不通过");
                    return jsonReturn;
                }
               } catch (Exception e) {
                   e.printStackTrace();
               }
           };

        }
        return jsonReturn;
    }
    //保存时对实体对象的数据校验
    public static JsonReturn validateParam(Param...params){
        JsonReturn jsonReturn=new JsonReturn();
        for(Param param:params){
                //验证数据是否非空
            if(StringUtils.isEmpty(param.getValue())||param.getValue().equals("0")){
                jsonReturn.setFlag(false);
                jsonReturn.setMessage(param.getName()+"--非空校验不通过");
                return jsonReturn;
            }
        }
        return jsonReturn;
    }
}
