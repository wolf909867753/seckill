-- 创建数据库
CREATE DATABASE seckill;

use seckill;

CREATE TABLE seckill(
  seckill_id  bigint NOT NULL AUTO_INCREMENT COMMENT '商品库存id',
  name VARCHAR(120) NOT NULL COMMENT '商品名称',
  number int NOT NULL COMMENT '库存数量',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  start_time TIMESTAMP NOT NULL COMMENT'秒杀开启时间',
  end_time TIMESTAMP NOT NULL COMMENT '秒杀结束时间',
  PRIMARY KEY (seckill_id),
  KEY idx_create_time(create_time),
  KEY idx_start_time(start_time),
  KEY idx_end_time(end_time)
)ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='秒杀数据库';
-- mysql引擎配置为innordb，以便支持事务

-- 初始化数据库
INSERT INTO
  seckill(name,number,start_time,end_time)
VALUES
  ('1000元秒杀iphone6',100,'2016-11-23 00:00:00','2016-12-24 00:00:00'),
  ('500元秒杀ipad2',200,'2016-11-23 00:00:00','2016-12-24 00:00:00'),
  ('300元秒杀小米4',300,'2016-11-23 00:00:00','2016-12-24 00:00:00'),
  ('2000元秒杀iphone6s',400,'2016-11-23 00:00:00','2016-12-24 00:00:00');
  
-- 秒杀成功明细表
-- 用户登录认证相关的信息
CREATE TABLE success_killed(
  seckill_id bigint NOT NULL COMMENT '秒杀商品ID',
  user_phone bigint NOT NULL COMMENT '用户手机号',
  state tinyint NOT NULL DEFAULT -1 COMMENT '状态标示:-1:无效 0：成功 1：已付款',
  create_time TIMESTAMP NOT NULL COMMENT '创建时间',
  PRIMARY KEY(seckill_id,user_phone),-- 联合主键
  KEY idx_create_time(create_time)
)ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='秒杀成功明细表';



-- 秒杀执行存储过程 优化秒杀
DELIMITER $$ -- console ;转化为;
-- 定义存储过程
-- 参数 :  in-输入参数        out-输出参数
-- row_count();返回上一条修改类型sql(delete ,insert,update)的影响行数
-- row_count(): 0:未修改数据; >0:表示修改的行数; <0:sql错误/未执行修改sql
CREATE PROCEDURE seckill.execute_seckill
	(
		in v_seckill_id bigint,
		in v_phone bigint,
		in v_state tinyint,
		in v_kill_time timestamp,
		out r_result int
	)
	BEGIN
		DECLARE insert_count int DEFAULT 0;
		START TRANSACTION;
		insert ignore into success_killed(seckill_id,user_phone,state,create_time)
		values(v_seckill_id,v_phone,v_state,v_kill_time);
		select row_count() into insert_count;
		IF(insert_count = 0) THEN
			ROLLBACK;
			set r_result = -1;
		ELSEIF(insert_count < 0) THEN
			ROLLBACK;
			set r_result = -2;
		ELSE
			update seckill set number = number -1 
			where seckill_id = v_seckill_id and end_time > v_kill_time and start_time  < v_kill_time and number > 0;
			select row_count() into insert_count;
			IF(insert_count = 0) THEN
				ROLLBACK;
				set r_result = 0;
			ELSEIF(insert_count < 0) THEN
				ROLLBACK;
				set r_result = -2;
			ELSE
				COMMIT;
				set r_result = 1;
			END IF;
		END IF;
	END;
$$
-- 秒杀存储过程定义结束
DELIMITER ;
SET @r_result = -3;
-- 执行存储过程
call execute_seckill(1003,13810881668,0,now(),@r_result);
--获取结果
select  @r_result;

-- 总结:不作为重点,但秒杀必用,原因如下:
-- 1:存储过程优化: 事务行级锁的持有时间
-- 2:不要过度依赖存储过程
-- 3:简单的逻辑可以应用存储过程
-- 4:QPS:一个秒杀单6000/QPS

--  删除存储过程

drop PROCEDURE execute_seckill
