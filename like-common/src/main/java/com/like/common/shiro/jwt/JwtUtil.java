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

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.like.common.basic.properties.JwtProperties;
import com.like.common.constant.CommonConstant;
import com.like.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 * https://github.com/auth0/java-jwt
 *
 * @author geekidea
 * @date 2019-09-30
 * @since 1.3.0.RELEASE
 **/
@Slf4j
@Component
public class JwtUtil {

    private static JwtProperties jwtProperties;
    @Value("${spring.application.name}")
    public  String application;
    public  static String APPLICATION;
    private static Map<String,String> applicationMap=new HashMap<>();
    public JwtUtil(JwtProperties jwtProperties) {
        com.like.common.shiro.jwt.JwtUtil.jwtProperties = jwtProperties;
        APPLICATION=application;
        log.info(JSON.toJSONString(com.like.common.shiro.jwt.JwtUtil.jwtProperties));
    }
    @PostConstruct
    public void readspringProfile(){
        com.like.common.shiro.jwt.JwtUtil.APPLICATION=this.application;
    }
    //生成系统交互的token
    public static String generateApplicationToken(){
        String token = com.like.common.shiro.jwt.JwtUtil.generateToken(com.like.common.shiro.jwt.JwtUtil.APPLICATION, "cloud_server","",Duration.ofSeconds(jwtProperties.getExpireSecond()));
        return token;
    }
    //获取系统交互的token
    public static String getApplicationToken(){
        String token=applicationMap.get(com.like.common.shiro.jwt.JwtUtil.APPLICATION);
        if(token!=null){
            //取出用户信息
            String username = com.like.common.shiro.jwt.JwtUtil.getUsername(token);
            //判断token 是否过期
           if(com.like.common.shiro.jwt.JwtUtil.isExpired(token)){
               //重新生成token
               token = com.like.common.shiro.jwt.JwtUtil.generateToken(com.like.common.shiro.jwt.JwtUtil.APPLICATION,  "cloud_server","",Duration.ofSeconds(jwtProperties.getExpireSecond()));
               applicationMap.put(com.like.common.shiro.jwt.JwtUtil.APPLICATION,token);
           }
        }else {
            token = com.like.common.shiro.jwt.JwtUtil.generateToken(com.like.common.shiro.jwt.JwtUtil.APPLICATION, "cloud_server","",Duration.ofSeconds(jwtProperties.getExpireSecond()));
            applicationMap.put(com.like.common.shiro.jwt.JwtUtil.APPLICATION,token);
        }
        return token;
    }
    /**
     * 生成JWT Token
     *
     * @param username       用户名
     * @param roleName       角色名称
     * @param salt           盐值
     * @param expireDuration 过期时间和单位
     * @return token
     */
    public static String generateToken(String username,String roleName, String salt, Duration expireDuration) {
        try {
            if (StringUtils.isBlank(username)) {
                log.error("username不能为空");
                return null;
            }
            log.debug("username:{}", username);

            // 如果盐值为空，则使用默认值：666666
            if (com.like.common.utils.StringUtils.isBlank(salt)) {
                salt = jwtProperties.getSecret();
            }
            log.debug("salt:{}", salt);

            // 过期时间，单位：秒
            Long expireSecond;
            // 默认过期时间为1小时
            if (expireDuration == null) {
                expireSecond = jwtProperties.getExpireSecond();
            } else {
                expireSecond = expireDuration.getSeconds();
            }
            log.debug("expireSecond:{}", expireSecond);
            Date expireDate = DateUtils.addSeconds(new Date(), expireSecond.intValue());
            log.debug("expireDate:{}", expireDate);

            // 生成token
            Algorithm algorithm = Algorithm.HMAC256(salt);
            String token = JWT.create()
                    .withClaim(CommonConstant.JWT_USERNAME, username).withClaim(CommonConstant.ADMIN_ROLE_NAME,roleName)
                    // jwt唯一id
                    .withJWTId(StringUtils.getUUID())
                    // 签发人
                    .withIssuer(jwtProperties.getIssuer())
                    // 主题
                    .withSubject(jwtProperties.getSubject())
                    // 签发的目标
                    .withAudience(jwtProperties.getAudience())
                    // 签名时间
                    .withIssuedAt(new Date())
                    // token过期时间
                    .withExpiresAt(expireDate)
                    // 签名
                    .sign(algorithm);
            return token;
        } catch (Exception e) {
            log.error("generateToken exception", e);
        }
        return null;
    }

    public static boolean verifyToken(String token, String salt) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(salt);
            JWTVerifier verifier = JWT.require(algorithm)
                    // 签发人
                    .withIssuer(jwtProperties.getIssuer())
                    // 主题
                    .withSubject(jwtProperties.getSubject())
                    // 签发的目标
                    .withAudience(jwtProperties.getAudience())
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            if (jwt != null) {
                return true;
            }
        } catch (Exception e) {
            log.error("Verify Token Exception", e);
        }
        return false;
    }

    /**
     * 解析token，获取token数据
     *
     * @param token
     * @return
     */
    public static DecodedJWT getJwtInfo(String token) {
        return JWT.decode(token);
    }

    /**
     * 获取用户名
     *
     * @param token
     * @return
     */
    public static String getUsername(String token) {
        if (StringUtils.isBlank(token)){
            return null;
        }
        DecodedJWT decodedJwt = getJwtInfo(token);
        if (decodedJwt == null) {
            return null;
        }
        String username = decodedJwt.getClaim(CommonConstant.JWT_USERNAME).asString();
        return username;
    }
    /**
     * 获取角色
     *
     * @param token
     * @return
     */
    public static String getRoleName(String token) {
        if (StringUtils.isBlank(token)){
            return null;
        }
        DecodedJWT decodedJwt = getJwtInfo(token);
        if (decodedJwt == null) {
            return null;
        }
        String username = decodedJwt.getClaim(CommonConstant.ADMIN_ROLE_NAME).asString();
        return username;
    }
    /**
     * 获取创建时间
     *
     * @param token
     * @return
     */
    public static Date getIssuedAt(String token) {
        DecodedJWT decodedJwt = getJwtInfo(token);
        if (decodedJwt == null) {
            return null;
        }
        return decodedJwt.getIssuedAt();
    }

    /**
     * 获取过期时间
     *
     * @param token
     * @return
     */
    public static Date getExpireDate(String token) {
        DecodedJWT decodedJwt = getJwtInfo(token);
        if (decodedJwt == null) {
            return null;
        }
        return decodedJwt.getExpiresAt();
    }

    /**
     * 判断token是否已过期
     *
     * @param token
     * @return
     */
    public static boolean isExpired(String token) {
        Date expireDate = getExpireDate(token);
        if (expireDate == null) {
            return true;
        }
        return expireDate.before(new Date());
    }
    /**
     * 判断token是否非法
     *
     * @param token
     * @return
     */
    public static boolean isIllegal(String token) {
        if(token.split("\\.").length!=3){
            return true;
        }
        return false;
    }
}
