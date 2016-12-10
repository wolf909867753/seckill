package com.wolf.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.wolf.domain.Seckill;
/**
 * 秒杀dao
 * @author wl
 *
 */
public interface ISeckillDao {
	/**
	 * 减库存
	 * @param seckillId
	 * @param killTime
	 * @return
	 * @throws Exception
	 */
	public int reduceNumber(@Param("seckillId")long seckillId,@Param("killTime")Date killTime) throws Exception;
	
	/**
	 * 根据id查询秒杀对象
	 * @param seckillId
	 * @return
	 * @throws Exception
	 */
	public Seckill queryById(@Param("seckillId")long seckillId) throws Exception;
	
	/**
	 * 分页查询秒杀商品列表
	 * @param offset
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public List<Seckill> queryAll(@Param("offset")int offset,@Param("limit")int limit) throws Exception;
	
	/**
	 * 根据存储过程执行秒杀操作
	 * @param paramMap
	 * @throws Exception
	 */
	public void executeSeckillByProcedure(Map<String,Object> paramMap) throws Exception;
}
