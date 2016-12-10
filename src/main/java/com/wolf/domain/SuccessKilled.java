package com.wolf.domain;

import java.util.Date;
/**
 * 秒杀成功明细表实体类
 * @author wanglu-jf
 *
 */
public class SuccessKilled {
	private long seckillId;//秒杀商品ID
	
	private long userPhone;//用户手机号
	
	private short state;//状态标示:-1:无效 0：成功 1：已付款
	
	private Date createTime;//创建时间
	
	private Seckill seckill;//many2one
	
	public Seckill getSeckill() {
		return seckill;
	}
	public void setSeckill(Seckill seckill) {
		this.seckill = seckill;
	}
	public long getSeckillId() {
		return seckillId;
	}
	public void setSeckillId(long seckillId) {
		this.seckillId = seckillId;
	}
	public long getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(long userPhone) {
		this.userPhone = userPhone;
	}
	public short getState() {
		return state;
	}
	public void setState(short state) {
		this.state = state;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Override
	public String toString() {
		return "SuccessKilled [seckillId=" + seckillId + ", userPhone="
				+ userPhone + ", state=" + state + ", createTime=" + createTime
				+ ", seckill=" + seckill + "]";
	}

	
}
