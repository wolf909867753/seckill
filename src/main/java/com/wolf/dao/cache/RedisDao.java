package com.wolf.dao.cache;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.wolf.domain.Seckill;

public class RedisDao {
	
	private JedisPool jedisPool;//jedis池
	
	private RuntimeSchema<Seckill> sechema = RuntimeSchema.createFrom(Seckill.class);
	
	public RedisDao(String ip,int port){
		jedisPool = new JedisPool(ip, port);
	}
	
	/**
	 * 从jedis获取数据
	 * @param seckillId
	 * @return
	 */
	public Seckill getSeckill(long seckillId){
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String key = "seckill:"+seckillId;
			byte[] bytes = jedis.get(key.getBytes());
			//自定义序列化 get ->byte[]->反序列化 ->object(seckill)
			if(null != bytes){
				Seckill seckill = sechema.newMessage();
				ProtostuffIOUtil.mergeFrom(bytes, seckill, sechema);
				return seckill;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			jedis.close();
		}
		return null;
	}
	
	
	/**
	 * set seckill 2 redis
	 * @param seckill
	 * @return
	 */
	public String setSeckill(Seckill seckill){
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String key = "seckill:"+seckill.getSecKillId();
			//set  object(seckill) --> 序列化-->byte[]
			byte[] bytes = ProtostuffIOUtil.toByteArray(seckill, sechema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
			String result = jedis.setex(key.getBytes(), 60*60, bytes);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
