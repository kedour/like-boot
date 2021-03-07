package com.like.common.basic.service.impl;

import com.like.common.basic.service.PermissionService;
import com.like.common.shiro.jwt.JwtUtil;
import com.like.common.vo.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @ClassName PermissionServiceImpl
 * @Description
 * @Author like
 * @Date 2020/10/12 18:52
 * @Version v1.0
 */
@Service
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    RestTemplate restTemplate;
    @Override
    public LoginUser findLoginUser(String token){
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", JwtUtil.getApplicationToken());
        LoginUser loginUser= restTemplate.getForObject("http://AUTH-SERVER/permission/getOeratorPermission/"+token+"?token={token}",LoginUser.class,params);
        return loginUser;
    }

}

