package com.cqut.picquick.service;

/**
 * @author : HK意境
 * @ClassName : RedisService
 * @date : 2021/9/16 0:08
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public interface RedisService {

    /**
     * 存储数据
     */
    void set(String key, String value);

    void set(String key, String value, long expire);

    /**
     * 获取数据
     */
    String get(String key);

    /**
     * 设置超期时间
     */
    boolean expire(String key, long expire);

    /**
     * 删除数据
     */
    void remove(String key);

    /**
     * 自增操作
     * @param delta 自增步长
     */
    Long increment(String key, long delta);

}
