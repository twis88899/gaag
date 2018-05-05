package com.twis.common.utils;

import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;  
  
/** 
 * Redis 池化 工具类 
 */  
@PropertySource(value="classpath:configs.properties", ignoreResourceNotFound=true)
public class RedisPoolUtils {  
  
    protected static ReentrantLock lockPool = new ReentrantLock();  
    protected static ReentrantLock lockJedis = new ReentrantLock();  
  
    protected static  Logger log = LoggerFactory.getLogger(RedisPoolUtils.class);
  
    //Redis服务器IP  
    @Value("${redis.ip}")  
    private static String ADDR_ARRAY;  
  
    //Redis的端口号  
    @Value("${redis.port}") 
    private static int PORT;  
  
    //访问密码  
    private static String AUTH = "";  
    //可用连接实例的最大数目，默认值为8；  
    //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。  
    private static int MAX_ACTIVE = 8;  
  
    //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。  
    private static int MAX_IDLE = 8;  
  
    //等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；  
    private static int MAX_WAIT = 3000;  
  
    //超时时间  
    private static int TIMEOUT = 10000;  
  
    //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；  
    private static boolean TEST_ON_BORROW = false;  
  
    private static JedisPool jedisPool = null;  
  
    /** 
     * 初始化Redis连接池 
     */  
    private static void initialPool() {  
        try {  
            JedisPoolConfig config = new JedisPoolConfig();  
            config.setMaxTotal(MAX_ACTIVE);  
            config.setMaxIdle(MAX_IDLE);  
            config.setMaxWaitMillis(MAX_WAIT);  
            config.setTestOnBorrow(TEST_ON_BORROW);  
            jedisPool = new JedisPool(config, ADDR_ARRAY.split(",")[0], PORT, TIMEOUT, AUTH);  
        } catch (Exception e) {  
            log.error("First create JedisPool error : " + e);  
            try {  
                //如果第一个IP异常，则访问第二个IP  
                JedisPoolConfig config = new JedisPoolConfig();  
                config.setMaxTotal(MAX_ACTIVE);  
                config.setMaxIdle(MAX_IDLE);  
                config.setMaxWaitMillis(MAX_WAIT);  
                config.setTestOnBorrow(TEST_ON_BORROW);  
                jedisPool = new JedisPool(config, ADDR_ARRAY.split(",")[1], PORT, TIMEOUT, AUTH);  
            } catch (Exception e2) {  
                log.error("Second create JedisPool error : " + e2);  
            }  
        }  
    }  
  
    /** 
     * 在多线程环境同步初始化 
     */  
    private static void poolInit() {  
        lockPool.lock();  
        try {  
            if (jedisPool == null) {  
                initialPool();  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            lockPool.unlock();  
        }  
    }  
  
    public static Jedis getJedis() {  
        lockJedis.lock();  
        if (jedisPool == null) {  
            poolInit();  
        }  
        Jedis jedis = null;  
        try {  
            if (jedisPool != null) {  
                jedis = jedisPool.getResource();  
            }  
        } catch (Exception e) {  
            log.error("Get jedis error : " + e);  
        } finally {  
            returnResource(jedis);  
            lockJedis.unlock();  
        }  
        return jedis;  
    }  
  
    /** 
     * 释放jedis资源 
     * 
     * @param jedis 
     */  
    public static void returnResource(final Jedis jedis) {  
        if (jedis != null && jedisPool != null) {  
            jedisPool.returnResource(jedis);  
        }  
    }  
  
    /** 
     * 设置 String 
     * 
     * @param key 
     * @param value 
     */  
    public synchronized static void setString(String key, String value) {  
        try {  
            value = StringUtils.isEmpty(value) ? "" : value;  
            getJedis().set(key, value);  
        } catch (Exception e) {  
            log.error("Set key error : " + e);  
        }  
    }  
  
    /** 
     * 设置 过期时间 
     * 
     * @param key 
     * @param seconds 以秒为单位 
     * @param value 
     */  
    public synchronized static void setString(String key, int seconds, String value) {  
        try {  
            value = StringUtils.isEmpty(value) ? "" : value;  
            getJedis().setex(key, seconds, value);  
        } catch (Exception e) {  
            log.error("Set keyex error : " + e);  
        }  
    }  
  
    /** 
     * 获取String值 
     * 
     * @param key 
     * @return value 
     */  
    public synchronized static String getString(String key) {  
        if (getJedis() == null || !getJedis().exists(key)) {  
            return null;  
        }  
        return getJedis().get(key);  
    }  
}  
