# seckill
秒杀

秒杀项目: 
  1:架构:Spring3.2.0 + SpringMVC + MyBaits3.2.7 + BootStarp3.3.0 +jQuery2.0 + MySql5.1.8 + redis 2.6.2
  2:开发环境web容器为:Tomcat7 
  3:项目优点: 
    3-1:使用存储过程,减少事务行级锁的持有时间 
    3-2:秒杀列表页使用页面静态化技术(freemarker)--未实现 
    3-3:使用redis缓存商品信息
    3-4:秒杀按钮请求连接采用MD5加密算法,数据存储使用联合主键,防止非法操作及重复秒杀 
  4:项目所涉及的数据库、表结构及存储过程参考项目的sql.sql文件。
