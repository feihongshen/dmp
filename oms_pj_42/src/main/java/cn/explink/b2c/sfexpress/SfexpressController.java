package cn.explink.b2c.sfexpress;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.sfexpress.wsdl.ISfexpressService;
import cn.explink.dao.BranchDAO;

/**
 * 迈思可
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/sfexpress")
public class SfexpressController {

	@Autowired
	BranchDAO branchDAO;

	@Autowired
	SfexpressService_sendOrder sfexpressService_sendOrder;
	@Autowired
	SfexpressService_searchOrderStatus sfexpressService_searchOrderStatus;

	/**
	 * 手动下单发送至顺丰系统
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/sendOrder")
	public @ResponseBody String sendTwoLeavelBranch(HttpServletRequest request) throws Exception {
		sfexpressService_sendOrder.sendCwbOrdersToSFexpress();
		return "手动调用数据下单至顺丰成功";
	}

	/**
	 * 查询顺丰系统订单跟踪，路由查询
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/search")
	public @ResponseBody String search(HttpServletRequest request) throws Exception {
		sfexpressService_searchOrderStatus.getWSReturnJson();
		return "手动调用查询顺丰系统成功";
	}

}
