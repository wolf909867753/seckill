package com.wolf;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wolf.dao.ISuccessKilledDao;
import com.wolf.domain.SuccessKilled;
/**
 * 测试秒杀成功明细dao
 * @author wanglu-jf
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/spring/spring-dao.xml"})
public class TestISuccessKilledDao {

	@Resource
	private ISuccessKilledDao successKilledDao;
	
	/**
	 * 测试insertSuccessKilled
	 */
	@Test
	public void testInsertSuccessKilled() throws Exception{
		int num = successKilledDao.insertSuccessKilled(1000, Long.valueOf("13810881665"),new Short("0"));
		System.err.println(">>>>>num:"+num); 
	}
	
	
	/**
	 * 测试queryByIdWithSeckill
	 */
	@Test
	public void testQueryByIdWithSeckill() throws Exception{
		SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(1000,Long.valueOf("13810881665"));
		System.err.println(">>>>>successKilled:"+successKilled); 
	}
}
