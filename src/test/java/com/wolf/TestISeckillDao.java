package com.wolf;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wolf.dao.ISeckillDao;
import com.wolf.domain.Seckill;
/**
 * 测试秒杀dao
 * @author wanglu-jf
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/spring/spring-dao.xml"})
public class TestISeckillDao {
	
	@Resource
	private ISeckillDao seckillDao;

	/**
	 * 测试reduceNumber
	 */
	@Test
	public void testReduceNumber() throws Exception{
		int num = seckillDao.reduceNumber(1000, new Date());
		System.err.println(">>>>>num:"+num);
	}
	
	/**
	 * 测试queryById
	 */
	@Test
	public void testQueryById() throws Exception{
		Seckill seckill = seckillDao.queryById(1000);
		System.err.println(">>>>>seckill:"+seckill);
	}
	
	/**
	 * 测试queryAll
	 */
	@Test
	public void testQueryAll() throws Exception{
		List<Seckill> seckills = seckillDao.queryAll(0,10);
		System.err.println(">>>>>seckills:"+seckills);
	}
}
