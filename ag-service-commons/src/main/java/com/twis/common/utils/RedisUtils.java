package com.twis.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import redis.clients.jedis.BinaryJedisCluster;
import redis.clients.jedis.HostAndPort;
/**
 * @author EverWang
 * @since 2015/6/10 16:53
 */
public class RedisUtils {
//	private static ShardedJedisPool pool;
//	private static List<JedisShardInfo> jdsInfoList = new ArrayList<JedisShardInfo>();
	private static Boolean isCrateJC=false;
	private static  Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();  
	private static BinaryJedisCluster jc;  
	public RedisUtils() {
		if (jc == null) {
//			JedisPoolConfig config = new JedisPoolConfig();// Jedis池配置
//			config.setMaxActive(500);// 最大活动的对象个数
//			config.setMaxIdle(1000 * 60);// 对象最大空闲时间
//			config.setMaxWait(1000 * 10);// 获取对象时最大等待时间
//			config.setTestOnBorrow(true);
//			config.setBlockWhenExhausted(true);
//			config.setFairness(true);
//			config.setJmxEnabled(true);
//			config.setLifo(true);
//			config.setTestOnCreate(true);
//			config.setTestOnReturn(true);
//			config.setTestWhileIdle(true);
//			pool = new ShardedJedisPool(config, jdsInfoList, Hashing.MURMUR_HASH,Sharded.DEFAULT_KEY_TAG_PATTERN);
			if(jedisClusterNodes.size()>0&&!isCrateJC){
				jc = new BinaryJedisCluster(jedisClusterNodes); 
			}
		}
	}
	
	
	
	public static void setIsCrateJC(Boolean isCrateJC) {
		RedisUtils.isCrateJC = isCrateJC;
	}



	public static void addJedisShardInfo(String ip,Integer port){
		jedisClusterNodes.add(new HostAndPort(ip, port));  
//		JedisShardInfo info = new JedisShardInfo(ip, port);
//		jdsInfoList.add(info);
	}

	/************************************
	 * Svae token code into reids. The code will be used to control access
	 * authority.
	 *
	 ************************************/
	public void save(String key, String value, Integer seconds) {
		try {
			jc.set(key.getBytes(), value.getBytes("gbk"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (seconds != null) {
			jc.expire(key.getBytes(), seconds);
		}
	}
	
	public void saveObject(String key, byte[] bytes, Integer seconds) {
		jc.set(key.getBytes(), bytes);
		if (seconds != null) {
			jc.expire(key.getBytes(), seconds);
		}
	}
	
	public void append(String key, String value) {
		try {
			jc.append(key.getBytes(), value.getBytes("gbk"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public void hdel(String key, String field) {
		jc.hdel(key.getBytes(), field.getBytes());
	}
	
	public boolean hexists(String key, String field){
		return jc.hexists(key.getBytes(), field.getBytes());
	}
	
	public String hget(String key, String field){

		byte[] bt = jc.hget(key.getBytes(), field.getBytes());
		if(bt ==null){
			return null;
		}else{
			try {
				return new String(bt,"gbk");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public Object hGetObject(String key, String field){
		if (field == null) return null;
		byte[] bt = jc.hget(key.getBytes(), field.getBytes());
		if(bt ==null){
			return null;
		}else{
			Object object = unserizlize(bt);
			if(object ==null){
				return null;
			}else{
				return object;
			}
		}
	}
	
	public void hset(String key, String field, String value){
		try {
			jc.hset(key.getBytes(), field.getBytes(), value.getBytes("gbk"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public void hSetObect(String key, String field, Object value){
		try {
			jc.hset(key.getBytes(), field.getBytes(), serialize(value));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void expire(String key, Integer seconds) {
		if (seconds != null) {
			jc.expire(key.getBytes(), seconds);
		}
	}

	public void save(String key, String value) {
		save(key, value, null);
	}
	
	public void saveObject(String key, Object value) {
		saveObject(key, serialize(value), null);
	}

	public void delete(String... keys) {
		if (keys != null) {
			for(String key :keys){
				jc.del(key.getBytes());
			}
		}
	}

	/**************************************************
	 * If user relogin,we update the relative token to make sure only existing a
	 * instance.
	 *
	 * @param key
	 * @param seconds
	 *************************************************/
	public boolean update(String key, String value, Integer seconds) {
		if (jc.exists(key.getBytes())) {
			save(key, value,seconds);
			return true;
		} else {
			return false;
		}
	}

	public String getValue(String key) {
		byte[] bt = jc.get(key.getBytes());
		if(bt ==null){
			return null;
		}else{
			try {
				return new String(bt,"gbk");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public Object getObject(String key) {
		byte[] bt = jc.get(key.getBytes());
		Object object = unserizlize(bt);
		if(object ==null){
			return null;
		}else{
			return object;
		}
	}
	
	public Long getIncr(String key) {
		return jc.incr(key.getBytes());
	}
	
	public Long getIncrByStep(String key, long iStep) {
		return jc.incrBy(key.getBytes(), iStep);
	}

	/**
	 * 判断KEY是否存在
	 *
	 * @param key
	 *            需要判断的KEY
	 * @return 存在返回true 不存在返回false
	 * @author wangyi
	 */
	public boolean isExists(String key) {
		return jc.exists(key.getBytes());
	}

	public void lpush(String key, String... strings) {
		for(String str : strings){
			jc.lpush(key.getBytes(), str.getBytes());
		}	
	}
	
	public String lpop(String key) {
		byte[] bt =jc.lpop(key.getBytes());
		if(bt ==null){
			return null;
		}else{
			return new String(bt);
		}
	}

	public List<String> blpop(String key) {
		List<byte[]> list = jc.blpop(10000, key.getBytes());
		ArrayList<String> returnList= new ArrayList<String> ();
		for(byte[] obj :list){
			returnList.add(new String(obj));
		}
		return returnList;
	}

	public List<String> lrange(String key) {
		List<byte[]> list = jc.lrange( key.getBytes() ,0l, -1l);
		ArrayList<String> returnList= new ArrayList<String> ();
		for(byte[] obj :list){
			returnList.add(new String(obj));
		}
		return returnList;
		
	}
	
	public void sadd(String key, String... strings) {
		for(String str : strings){
			jc.sadd(key.getBytes(), str.getBytes());
		}	
	}
	
	public void srem(String key, String smebers) {
		srem(key, smebers);
	}
	
	public boolean sismember(String key, String smebers){
		return jc.sismember(key.getBytes(), smebers.getBytes());
	}
	
	public void srem(String key, byte[] smebers) {
		jc.srem(key.getBytes(), smebers);
	}
	
	public Set<byte[]> smembers(String key) {
		return jc.smembers(key.getBytes());
	}
	
	public long scard(String key) {
		return jc.scard(key.getBytes());
	}
	
	public byte [] serialize(Object obj){
        ObjectOutputStream obi=null;
        ByteArrayOutputStream bai=null;
        try {
            bai=new ByteArrayOutputStream();
            obi=new ObjectOutputStream(bai);
            obi.writeObject(obj);
            byte[] byt=bai.toByteArray();
            return byt;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public Object unserizlize(byte[] byt){
        ObjectInputStream oii=null;
        ByteArrayInputStream bis=null;
        bis=new ByteArrayInputStream(byt);
        try {
            oii=new ObjectInputStream(bis);
            Object obj=oii.readObject();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
