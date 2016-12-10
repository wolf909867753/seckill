package com.wolf;

import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wolf.dao.ISeckillDao;
import com.wolf.dao.ISuccessKilledDao;
import com.wolf.domain.Seckill;
import com.wolf.domain.SuccessKilled;

public class TestDao {
	
	private static ApplicationContext context = null;
	
	static{
		context = new ClassPathXmlApplicationContext("spring/spring-dao.xml");
	}
	
	/**
	 * 测试DataSource
	 */
	@Test
	public void testDataSource(){
		DataSource dataSource = (DataSource) context.getBean("dataSource");
		System.err.println(">>>>>>dataSource:"+dataSource);
	}
	
	/**
	 * 测试ISeckillDao
	 */
	@Test
	public void testReduceNumber(){
		try {
			ISeckillDao seckillDao = context.getBean(ISeckillDao.class);
			int num = seckillDao.reduceNumber(1000, new Date());
			System.err.println(">>>>>num:"+num);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	
	/**
	 * 测试queryById
	 */
	@Test
	public void testQueryById(){
		try {
			ISeckillDao seckillDao = context.getBean(ISeckillDao.class);
			Seckill seckill = seckillDao.queryById(1000);
			System.err.println(">>>>>seckill:"+seckill);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * 测试queryAll
	 */
	@Test
	public void testQueryAll(){
		try {
			ISeckillDao seckillDao = context.getBean(ISeckillDao.class);
			List<Seckill> seckills = seckillDao.queryAll(0,10);
			System.err.println(">>>>>seckills:"+seckills);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	
	/**
	 * 测试insertSuccessKilled
	 */
	@Test
	public void testInsertSuccessKilled(){
		try {
			ISuccessKilledDao successKilledDao = context.getBean(ISuccessKilledDao.class);
			int num = successKilledDao.insertSuccessKilled(1000, Long.valueOf("13810881665"),new Short("0"));
			System.err.println(">>>>>num:"+num);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	
	/**
	 * 测试queryByIdWithSeckill
	 */
	@Test
	public void testQueryByIdWithSeckill(){
		try {
			ISuccessKilledDao successKilledDao = context.getBean(ISuccessKilledDao.class);
			SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(1000,Long.valueOf("13810881665"));
			System.err.println(">>>>>successKilled:"+successKilled);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	
}
