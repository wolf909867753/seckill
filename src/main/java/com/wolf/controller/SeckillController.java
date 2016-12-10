package com.wolf.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wolf.domain.Seckill;
import com.wolf.dto.Exposer;
import com.wolf.dto.SeckillExecution;
import com.wolf.dto.SeckillResult;
import com.wolf.service.ISeckillService;

/**
 * Created by wolf on 16/11/28.
 */
@Component
@RequestMapping("/seckill")  // url:模块/资源/{}/细分
public class SeckillController {
	@Autowired
	private ISeckillService seckillService;

	/**
	 * 秒杀列表页
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Model model) {
		// list.jsp+mode=ModelAndView
		// 获取列表页
		try {
			List<Seckill> list = seckillService.getSeckillList();
			model.addAttribute("list", list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "list";
	}

	/**
	 * 秒杀详情页
	 * @param seckillId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
	public String detail(@PathVariable("seckillId") Long seckillId, Model model) {
		try {
			if (seckillId == null) {
				return "redirect:/seckill/list";
			}

			Seckill seckill = seckillService.getById(seckillId);
			if (seckill == null) {
				return "forward:/seckill/list";
			}

			model.addAttribute("seckill", seckill);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "detail";
	}

	/**
	 * 暴露秒杀接口的方法
	 * @param seckillId
	 * @return
	 */
	// ajax ,json暴露秒杀接口的方法
	@RequestMapping(value = "/{seckillId}/exposer", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public SeckillResult<Exposer> exposer(Long seckillId) {
		SeckillResult<Exposer> result;
		try {
			Exposer exposer = seckillService.exportSeckillUrl(seckillId);
			result = new SeckillResult<Exposer>(true, exposer);
		} catch (Exception e) {
			e.printStackTrace();
			result = new SeckillResult<Exposer>(false, e.getMessage());
		}

		return result;
	}

	/**
	 * 执行秒杀接口的方法
	 * @param seckillId
	 * @return
	 */
	@RequestMapping(value = "/{seckillId}/{md5}/execution", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public SeckillResult<SeckillExecution> execute(
			@PathVariable("seckillId") Long seckillId,
			@PathVariable("md5") String md5,
			@CookieValue(value = "killPhone", required = false) Long phone) {
		if (phone == null) {
			return new SeckillResult<SeckillExecution>(false, "未注册");
		}

//		try {
//			SeckillExecution execution = seckillService.executeSeckill(seckillId, phone, md5);
//			return new SeckillResult<SeckillExecution>(true, execution);
//		} catch (RepeatKillException e1) {
//			SeckillExecution execution = new SeckillExecution(seckillId,SeckillStatEnum.REPEAT_KILL);
//			return new SeckillResult<SeckillExecution>(true, execution);
//		} catch (SeckillCloseException e2) {
//			SeckillExecution execution = new SeckillExecution(seckillId,SeckillStatEnum.END);
//			return new SeckillResult<SeckillExecution>(true, execution);
//		} catch (Exception e) {
//			SeckillExecution execution = new SeckillExecution(seckillId,SeckillStatEnum.INNER_ERROR);
//			return new SeckillResult<SeckillExecution>(true, execution);
//		}
		
		//根据存储过程执行秒杀  好处：在数据库端执行事务，减少行级锁的持有时间
		SeckillExecution execution = seckillService.executeSeckillByProcedure(seckillId, phone, md5);
		return new SeckillResult<SeckillExecution>(true, execution);
	}

	/**
	 * 获取系统时间
	 * @return
	 */
	@RequestMapping(value = "/time/now", method = RequestMethod.GET)
	@ResponseBody
	public SeckillResult<Long> time() {
		Date now = new Date();
		return new SeckillResult<Long>(true, now.getTime());
	}
}
