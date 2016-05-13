package cn.explink.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 预约单 Controller
 * @date 2016年5月13日 上午11:04:29
 */
@Controller
@RequestMapping("/reserveOrder")
public class ReserveOrderController {
	
	/**
	 * 快递预约单查询
	 * @date 2016年5月13日 上午11:10:28
	 * @return
	 */
	@RequestMapping("/query")
	public String query() {
		return "reserveOrder/query";
	}
	
	/**
	 * 快递预约单处理
	 * @date 2016年5月13日 上午11:10:28
	 * @return
	 */
	@RequestMapping("/handle")
	public String handle() {
		return "reserveOrder/handle";
	}
	
	/**
	 * 快递预约单处理(站点)
	 * @date 2016年5月13日 上午11:10:28
	 * @return
	 */
	@RequestMapping("/warehouseHandle")
	public String warehouseHandle() {
		return "reserveOrder/warehouseHandle";
	}
}
