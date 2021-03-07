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

import javax.servlet.http.HttpServletResponse;

/**
 * 公共常量
 *
 * @author geekidea
 * @date 2018-11-08
 */
public interface HttpStatusConstant extends HttpServletResponse {
    /**
     * token参数为空
     */
  int SC_Token_empty =600;
    /**
     * token非法
     */
    int SC_Token_illegal =601;
    /**
     * token过期
     */
    int SC_Token_expired =602;
    /**
     * token不存在
     */
    int SC_Token_redis_exists=603;
}
