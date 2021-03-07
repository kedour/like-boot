package com.like.common.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 *
 * @ClassName LoginUser
 * @Description
 * @Author like
 * @Date 2020/10/12 18:20
 * @Version v1.0
 */
@Getter
@Setter
public class LoginUser implements Serializable {

    private static final long serialVersionUID = 4507869346123296527L;

    /**
     * 	登录成功ID
     */

    private String userId;
    /**
     * 	登录成功用户名
     */

    private String account;
    /**
     * 	登录成功后的token
     */

    @Override
    public boolean equals(Object obj) {
        if (this == obj){
            return true;
        }

        if (obj == null){
            return false;
        }

        if (getClass() != obj.getClass()){
            return false;
        }

        LoginUser other = (LoginUser) obj;
        if (userId == null) {
            if (other.userId != null){
                return false;
            }

        }
        else if (!userId.equals(other.userId)){
            return false;
        }
        return true;
    }
}
