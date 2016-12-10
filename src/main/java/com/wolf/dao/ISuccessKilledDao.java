package com.wolf.dao;

import org.apache.ibatis.annotations.Param;

import com.wolf.domain.SuccessKilled;

/**
 * 秒杀成功记录dao
 * @author wl
 *
 */
public interface ISuccessKilledDao{

	/**
	 * 插入购买明细，可过滤重复
	 * @param seckillId
	 * @param userPhone
	 * @return
	 * @throws Exception
	 */
	public int insertSuccessKilled(@Param("seckillId")long seckillId,@Param("userPhone")long userPhone,@Param("state")short state) throws Exception;
	
	/**
	 *  根据Id查询SuccessKilled并携带秒杀产品对象实体
	 * @param seckillId
	 * @return
	 * @throws Exception
	 */
	public SuccessKilled queryByIdWithSeckill(@Param("seckillId")long seckillId,@Param("userPhone")long userPhone) throws Exception;
}
