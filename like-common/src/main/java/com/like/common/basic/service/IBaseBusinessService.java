package com.like.common.basic.service;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.like.common.entity.BaseEntity;
import com.like.common.vo.LoginUser;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @ClassName IBaseBusinessService
 * @Description
 * @Author like
 * @Date 2020/10/12 18:19
 * @Version v1.0
 */


public interface IBaseBusinessService<T extends BaseEntity,M extends BaseMapper<T>, ID extends Serializable>  extends IService<T> {
    //查询分页
    public IPage findPage(Page page, T t, LoginUser loginUser, String... columns);
    //查询列表
    public List findList(T t, LoginUser loginUser, String... columns);
    //查询对象
    public Object findObj(ID id);
    //批量删除
    public void del(List<ID> reids, LoginUser loginUser);
    //下拉选择
    public JSONArray checkData(T t, LoginUser loginUser, String... column);
    //保存与修改
    public Boolean businessSaveOrUpdate(Object t);

    //将 vo  还原成 entity
    public T revertVoToEntity(Object t);
    //将 entity 还原成 vo
    public Object revertEntityToVo(Object entity);
}

