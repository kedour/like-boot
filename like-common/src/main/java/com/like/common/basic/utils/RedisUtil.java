package com.like.common.basic.utils;

import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 *
 * @ClassName BaseBusinessService
 * @Description
 * @Author like
 * @Date 2020/10/12 18:22
 * @Version v1.0
 */


public class RedisUtil {

    public static final int TIMEOUT = 1000;

    private StringRedisTemplate stringRedisTemplate;

    public RedisUtil(StringRedisTemplate stringRedisTemplate){
        this.stringRedisTemplate=stringRedisTemplate;
    }
    public void push(String key, String value) {
        stringRedisTemplate.opsForList().leftPush(key, value);
    }

    public String pop(String key) {
        return stringRedisTemplate.opsForList().rightPop(key);
    }


    public void set(String key, String value, Integer time,TimeUnit ... timeUnits) {
        if (time == null || time == -1){
            stringRedisTemplate.opsForValue().set(key, value);
        }else {
            TimeUnit timeUnit=TimeUnit.SECONDS;
            if(timeUnits!=null&&timeUnits.length>0){
                timeUnit=timeUnits[0];
            }
        	stringRedisTemplate.opsForValue().set(key, value, time,timeUnit);
        }
    }
    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public void delete(String key) {
        stringRedisTemplate.opsForValue().getOperations().delete(key);
    }

    public boolean hasKey(String key) {
        return stringRedisTemplate.hasKey(key);
    }

}
