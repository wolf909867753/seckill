package com.wolf.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import com.wolf.dao.ISeckillDao;
import com.wolf.dao.ISuccessKilledDao;
import com.wolf.dao.cache.RedisDao;
import com.wolf.domain.Seckill;
import com.wolf.domain.SuccessKilled;
import com.wolf.dto.Exposer;
import com.wolf.dto.SeckillExecution;
import com.wolf.enums.SeckillStatEnum;
import com.wolf.exception.RepeatKillException;
import com.wolf.exception.SeckillCloseException;
import com.wolf.exception.SeckillException;
import com.wolf.service.ISeckillService;

/**
 * Created by wolf on 16/11/28.
 */
//@Component @Service @Dao @Controller
@Service
public class SeckillServiceImpl implements ISeckillService
{
    //日志对象
    private Logger logger= LoggerFactory.getLogger(this.getClass());

    //加入一个混淆字符串(秒杀接口)的salt，为了我避免用户猜出我们的md5值，值任意给，越复杂越好
    private final String salt="abcdefghijklmnopqrstuvwsyzABCDEFGHIJKLMNOPQRSTUVWSYZ123456789,./';!@#$%^&*()_+ABCDEFGHIJKLMNOPQRSTUVWSYZabcdefghijklmnopqrstuvwsyz";

    //注入Service依赖
    @Autowired //@Resource
    private ISeckillDao seckillDao;

    @Autowired //@Resource
    private ISuccessKilledDao successKilledDao;
    
    @Autowired
    private RedisDao redisDao;

    /**
     * 查询全部的秒杀记录
     * @return
     */
    public List<Seckill> getSeckillList() throws Exception {
        return seckillDao.queryAll(0,4);//暂时写死，分页操作
    }

    /**
     *查询单个秒杀记录
     * @param seckillId
     * @return
     */
    public Seckill getById(long seckillId) throws Exception {
        return seckillDao.queryById(seckillId);
    }

    /**
     * 在秒杀开启时输出秒杀接口的地址，否则输出系统时间和秒杀时间
     * @param seckillId
     */
    public Exposer exportSeckillUrl(long seckillId) throws Exception{
    	Seckill seckill = redisDao.getSeckill(seckillId);
    	if(null == seckill){
    		seckill = seckillDao.queryById(seckillId);
    		if (null ==seckill) {//说明查不到这个秒杀产品的记录
                return new Exposer(false,seckillId);
            }else{
            	redisDao.setSeckill(seckill);//放入到缓存中
            }
    	}
    	
        //若是秒杀未开启
        Date startTime=seckill.getStartTime();
        Date endTime=seckill.getEndTime();
        //系统当前时间
        Date nowTime=new Date();
        if (startTime.getTime()>nowTime.getTime() || endTime.getTime()<nowTime.getTime()){
            return new Exposer(false,seckillId,nowTime.getTime(),startTime.getTime(),endTime.getTime());
        }

        //秒杀开启，返回秒杀商品的id、用给接口加密的md5
        String md5=getMD5(seckillId);
        return new Exposer(true,md5,seckillId);
    }
    /**
     * 加密算法MD5
     * @param seckillId
     * @return
     */
    private String getMD5(long seckillId){
        String base=seckillId+"/"+salt;
        String md5= DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    //秒杀是否成功，成功:减库存，增加明细；失败:抛出异常，事务回滚
    /**
     * 使用注解控制事务方法的优点:
     * 1.开发团队达成一致约定，明确标注事务方法的编程风格
     * 2.保证事务方法的执行时间尽可能短，不要穿插其他网络操作RPC/HTTP请求或者剥离到事务方法外部
     * 3.不是所有的方法都需要事务，如只有一条修改操作、只读操作不要事务控制
     */
    @Transactional
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws SeckillException, RepeatKillException, SeckillCloseException {

        if (md5==null||!md5.equals(getMD5(seckillId))){
            throw new SeckillException("seckill data rewrite");//秒杀数据被重写了
        }
        //执行秒杀逻辑:减库存+增加购买明细
        Date nowTime=new Date();

        try{
        	//秒杀成功,增加明细
            int insertCount=successKilledDao.insertSuccessKilled(seckillId,userPhone,new Short("0"));
            //看是否该明细被重复插入，即用户是否重复秒杀
            if (insertCount<=0){
                throw new RepeatKillException("seckill repeated");
            }else {
            	 //减库存
                int updateCount=seckillDao.reduceNumber(seckillId,nowTime);
                if (updateCount<=0){
                    //没有更新库存记录，说明秒杀结束
                    throw new SeckillCloseException("seckill is closed");
                }
            	
                //秒杀成功,得到成功插入的明细记录,并返回成功秒杀的信息
                SuccessKilled successKilled=successKilledDao.queryByIdWithSeckill(seckillId,userPhone);
                return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS,successKilled);
            }
        }catch (SeckillCloseException e1){
            throw e1;
        }catch (RepeatKillException e2){
            throw e2;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            //所以编译期异常转化为运行期异常
            throw new SeckillException("seckill inner error :"+e.getMessage());
        }

    }
    
    /**
     * 根据存储过程执行秒杀操作
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return
     */
    public SeckillExecution executeSeckillByProcedure(long seckillId,long userPhone,String md5){
    	 try {
			if (md5 == null || !md5.equals(getMD5(seckillId))){
			     return new SeckillExecution(seckillId,SeckillStatEnum.DATE_REWRITE);//秒杀数据被重写了
			 }
			 Map<String, Object> paramMap = new HashMap<String,Object>();
			 paramMap.put("seckillId", seckillId);
			 paramMap.put("userPhone", userPhone);
			 paramMap.put("state", 0);
			 paramMap.put("killTime", new Date());
			 paramMap.put("result", null);
			 seckillDao.executeSeckillByProcedure(paramMap);
			 int result = MapUtils.getInteger(paramMap, "result",-2);
			 if(1 == result){//秒杀成功
				 SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
				 return new SeckillExecution(seckillId,SeckillStatEnum.SUCCESS,successKilled);
			 }else{//秒杀失败
				 return new SeckillExecution(seckillId,SeckillStatEnum.stateOf(result));
			 }
		} catch (Exception e) {
			return new SeckillExecution(seckillId,SeckillStatEnum.INNER_ERROR);
		}
    }
}