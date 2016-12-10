package com.wolf;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wolf.dao.ISeckillDao;
import com.wolf.dao.cache.RedisDao;
import com.wolf.domain.Seckill;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/spring/spring-dao.xml"})
public class TestRedisDao {
	
	@Autowired
	private RedisDao redisDao;
	
	@Autowired
	private ISeckillDao seckillDao;

	@Test
	public void testSETGET() throws Exception{
		long seckillId = 1000;
		Seckill seckill = redisDao.getSeckill(seckillId);
		System.err.println("1:seckill="+seckill);
		if(null == seckill){
			seckill = seckillDao.queryById(seckillId);
			System.err.println("2:seckill="+seckill);
			if(null != seckill){
				String result = redisDao.setSeckill(seckill);
				if("ok".equalsIgnoreCase(result)){
					seckill = redisDao.getSeckill(seckill.getSecKillId());
					System.err.println("3:seckill="+seckill);
				}
			}
		}
	}
}
