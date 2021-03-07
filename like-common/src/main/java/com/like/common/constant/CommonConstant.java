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

package com.like.common.constant;

/**
 * 公共常量
 *
 * @author geekidea
 * @date 2018-11-08
 */
public interface CommonConstant {
    /**
     * 登录用户
     */
    String LOGIN_SYS_USER = "loginSysUser";

    /**
     * 登录token
     */
    String JWT_DEFAULT_TOKEN_NAME = "token";

    /**
     * JWT用户名
     */
    String JWT_USERNAME = "username";

    /**
     * JWT刷新新token响应状态码
     */
    int JWT_REFRESH_TOKEN_CODE = 460;

    /**
     * JWT刷新新token响应状态码，
     * Redis中不存在，但jwt未过期，不生成新的token，返回361状态码
     */
    int JWT_INVALID_TOKEN_CODE = 461;

    /**
     * JWT Token默认密钥
     */
    String JWT_DEFAULT_SECRET = "666666";

    /**
     * JWT 默认过期时间，3600L，单位秒
     */
    Long JWT_DEFAULT_EXPIRE_SECOND = 3600L;


    /**
     * 管理员角色名称
     */
    String ADMIN_ROLE_NAME = "roleName";

    String ADMIN_LOGIN = "adminLogin";

    /**
     * 验证码token
     */
    String VERIFY_TOKEN = "verifyToken";

    /**
     * base64前缀
     */
    String BASE64_PREFIX = "data:image/png;base64,";
}
