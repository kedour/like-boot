package com.like.common.vo;

import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 *
 * @ClassName JsonReturn
 * @Description
 * @Author like
 * @Date 2020/10/12 19:06
 * @Version v1.0
 */

public class JsonReturn<T> implements java.io.Serializable {
    //执行结果
    boolean flag;
    //状态码
    int code;
    //提示信息
    String message;
    //错误信息
    String errorMessage;
    //数据
    T data;
    //单位毫秒
    long time;
    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Object getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public JsonReturn() {
        this.setCode(HttpServletResponse.SC_OK);
        this.flag=true;
        this.message="成功";
    }
    public JsonReturn(boolean flag, String message, T data) {
        this.flag = flag;
        this.message = message;
        this.setCode(HttpServletResponse.SC_OK);
        this.data = data;
    }
    public JsonReturn(boolean flag, String message,int code, T data) {
        this.flag = flag;
        this.message = message;
        this.setCode(code);
        this.data = data;
    }
    public  JsonReturn FALSE(String message){
        this.flag = false;
        this.message = message;
        this.setCode(HttpServletResponse.SC_OK);
        return this;
    }
    public  JsonReturn FALSE(int code,String message){
        this.flag = false;
        this.code=code;
        this.message = message;
        this.setCode(code);
        return this;
    }
    public  JsonReturn ERROR(Exception e){
        e.printStackTrace();
        this.setFlag(false);
        this.setCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        this.setErrorMessage(e.getMessage());
        this.setMessage("系统故障！请稍后重试");
        return this;
    }
}

