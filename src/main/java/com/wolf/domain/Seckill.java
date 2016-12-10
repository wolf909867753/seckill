package com.wolf.domain;

import java.util.Date;
/**
 * 秒杀实体类
 * @author wl
 *
 */
public class Seckill {
	private long secKillId;//商品库存id
	
	private String name;//商品名称
	
	private int number;//库存数量
	
	private Date startTime;//秒杀开启时间
	
	private Date endTime;//秒杀结束时间
	
	private Date createTime;//创建时间

	public long getSecKillId() {
		return secKillId;
	}

	public void setSecKillId(long secKillId) {
		this.secKillId = secKillId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "Seckill [secKillId=" + secKillId + ", name=" + name
				+ ", number=" + number + ", startTime=" + startTime
				+ ", endTime=" + endTime + ", createTime=" + createTime + "]";
	}
	
}
