/*
 * Copyright 2019-2029 geekidea(https://github.com/geekidea)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.like.common.shiro.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.like.common.utils.IpUtil;
import lombok.Data;
import org.apache.shiro.authc.HostAuthenticationToken;

import java.util.Date;

/**
 * Shiro JwtToken对象
 *
 * @author geekidea
 * @date 2019-09-27
 * @since 1.3.0.RELEASE
 **/
@Data
public class JwtToken implements HostAuthenticationToken {
    private static final long serialVersionUID = 5101247566043093405L;

    /**
     * 登录ip
     */
    private String host;
    /**
     * 登录用户名称
     */
    private String username;
    /**
     * 登录盐值
     */
    private String salt;
    /**
     * 登录token
     */
    private String token;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 多长时间过期，默认一小时
     */
    private long expireSecond;
    /**
     * 过期日期
     */
    private Date expireDate;

    private String principal;

    private String credentials;

   /* @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }*/

    public static com.like.common.shiro.jwt.JwtToken build(String token, String username, String salt, long expireSecond) {
        DecodedJWT decodedJwt = JwtUtil.getJwtInfo(token);
        Date createDate = decodedJwt.getIssuedAt();
        Date expireDate = decodedJwt.getExpiresAt();
        com.like.common.shiro.jwt.JwtToken jwtToken= new com.like.common.shiro.jwt.JwtToken();
        jwtToken.setUsername(username);
        jwtToken.setHost(IpUtil.getRequestIp());
        jwtToken.setSalt(salt);
        jwtToken.setCreateDate(createDate);
        jwtToken.setExpireSecond(expireSecond);
        jwtToken.setExpireDate(expireDate);
        jwtToken.setToken(token);
        return jwtToken;
    }
}