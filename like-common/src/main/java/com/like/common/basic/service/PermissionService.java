package com.like.common.basic.service;

import com.like.common.vo.LoginUser;

/**
 * Created with IntelliJ IDEA.
 *
 * @ClassName PermissionService
 * @Description
 * @Author like
 * @Date 2020/10/12 18:53
 * @Version v1.0
 */
public interface PermissionService {
    /**
     * 查询登录用户
     * @param token
     * @return
     */
    LoginUser findLoginUser(String token);

    /**
     * 查询登录服务人员
     * @param token
     * @return
     */
//    LoginWaiter findLoginWaiter(String token);

}
