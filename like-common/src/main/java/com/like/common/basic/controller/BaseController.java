package com.like.common.basic.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.like.common.basic.service.PermissionService;
import com.like.common.basic.service.IBaseBusinessService;
import com.like.common.basic.utils.ValidateUtil;
import com.like.common.entity.BaseEntity;
import com.like.common.utils.StringUtils;
import com.like.common.vo.CheckData;
import com.like.common.vo.JsonReturn;
import com.like.common.vo.LoginUser;
import com.like.common.vo.Param;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 *
 * @ClassName BaseController
 * @Description
 * @Author like
 * @Date 2020/10/12 17:34
 * @Version v1.0
 */

@Slf4j
public class BaseController<T extends BaseEntity, ID extends Serializable>{
    private Class<?> entityClass;
    public IBaseBusinessService businessService;
    @Autowired
    private Validator validator;
    @Autowired
    public PermissionService permissionService;
    public BaseController() {
        Class<?> c = this.getClass();
        Type t = c.getGenericSuperclass();
        if (t instanceof ParameterizedType) {
            this.entityClass = (Class<?>) ((ParameterizedType) t)
                    .getActualTypeArguments()[0];
        }
    }
    /**
     * 分页列表
     * @param
     * @return
     */
    @ApiOperation("分页列表")
    @RequestMapping(value = "/page",method = RequestMethod.POST)
    public JsonReturn page(@ApiIgnore Page page, @RequestHeader("token") String token, @RequestBody JSONObject request , @ApiIgnore T t){
        t =JSONObject.toJavaObject(request, (Class<T>) t.getClass());
        page =JSONObject.toJavaObject(request, page.getClass());
        JsonReturn jsonReturn=new JsonReturn();
        try {
            LoginUser loginUser= permissionService.findLoginUser(token);
            IPage pagedata = businessService.findPage(page,t,loginUser);
            jsonReturn.setData(pagedata);
        }
        catch (Exception e){
            e.printStackTrace();
            jsonReturn.ERROR(e);
        }
        return jsonReturn;
    }

    /**
     * 获取列表
     * @param
     * @return
     */
    @ApiOperation("获取列表")
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public JsonReturn list(@RequestHeader(value = "token") String token,@RequestBody T t){
        JsonReturn jsonReturn=new JsonReturn();
        try {
            LoginUser loginUser= permissionService.findLoginUser(token);
            List list = businessService.findList(t,loginUser);
            jsonReturn.setData(list);
        }catch (Exception e){
            e.printStackTrace();
            jsonReturn.ERROR(e);
        }
        return jsonReturn;
    }
    /**
     * 获取
     * @param
     * @return
     */
    @ApiOperation("获取")
    @RequestMapping(value = "/get/{id}",method = RequestMethod.GET)
    public JsonReturn get(@PathVariable(value = "id") String  id, @RequestHeader("token") String token){
        JsonReturn jsonReturn=new JsonReturn();
        LoginUser loginUser= permissionService.findLoginUser(token);
        jsonReturn = ValidateUtil.validateParam(new Param("id",String.valueOf(id)));
        if(!jsonReturn.isFlag()){
            return jsonReturn;
        }
        T t= (T) businessService.findObj((ID)id);
        jsonReturn.setData(t);
        return jsonReturn;
    }
    /**
     * 保存
     * @param t
     * @return
     */
    @ApiOperation(value = "保存")
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public JsonReturn save(@RequestBody T t, @RequestHeader(value = "token",required = false) String token){
        JsonReturn jsonReturn=new JsonReturn();
        try {
            boolean validateResult=true;
            //验证数据
            Set<ConstraintViolation<T>> validate = validator.validate(t);
            for (ConstraintViolation<T> mode : validate) {
                System.out.println(mode.getPropertyPath()+"--"+mode.getMessage());
                validateResult=false;
            }
            if(!validateResult){
                return jsonReturn.FALSE("参数校验失败");
            }

            if(!jsonReturn.isFlag()){
                return jsonReturn;
            }
            LoginUser loginUser= permissionService.findLoginUser(token);
            businessService.businessSaveOrUpdate(t);
            if(jsonReturn.isFlag()){
                jsonReturn.setData(String.valueOf(t.id));
            }
        }catch (Exception e){
            e.printStackTrace();
            jsonReturn.ERROR(e);
        }
        return jsonReturn;
    }

    /**
     * 删除
     * @return JsonReturn
     */
    @ApiOperation(value = "删除",response = JsonReturn.class, notes = "删除")
    @RequestMapping(value = "/del",method = RequestMethod.POST)
    public JsonReturn del(@RequestParam(value = "ids") String ids, @RequestHeader("token") String token){
        JsonReturn jsonReturn=new JsonReturn();
        try {
            LoginUser loginUser= permissionService.findLoginUser(token);
            List<Object> reids= StringUtils.sringToArray(ids,",","long");
            if(reids.size()==0){
                return jsonReturn.FALSE("请选择要删除的项");
            }
            businessService.del(reids,loginUser);
        }catch (Exception e){
            e.printStackTrace();
            jsonReturn.ERROR(e);
        }
        return jsonReturn;
    }
    @ApiOperation(value = "下拉数据",response = CheckData.class)
    @RequestMapping(value = "/checkData",method = RequestMethod.POST)
    public JsonReturn checkData(@RequestHeader("token") String token,@RequestBody T t){
        JsonReturn jsonReturn=new JsonReturn();
        try{
            LoginUser loginUser= permissionService.findLoginUser(token);
            JSONArray data= businessService.checkData(t,loginUser);
            jsonReturn.setData(data);
        }catch (Exception e){
            e.printStackTrace();
            jsonReturn.ERROR(e);
        }
        return jsonReturn;
    }

}
