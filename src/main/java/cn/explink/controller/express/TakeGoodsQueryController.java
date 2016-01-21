package cn.explink.controller.express;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.explink.domain.User;
import cn.explink.domain.VO.express.ExpressCwbOrderForTakeGoodsQueryVO;
import cn.explink.domain.express.ExpressCwb4TakeGoodsQuery;
import cn.explink.service.express.PreOrderService;
import cn.explink.service.express.StationOperationService;
import cn.explink.service.express.TakeGoodsQueryService;
import cn.explink.util.ExportUtil4Express;
import cn.explink.util.Page;

/**
 * 揽件查询
 * @author wangzy
 *
 */
@Controller
@RequestMapping("/takeGoodsQuery")
public class TakeGoodsQueryController extends ExpressCommonController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	TakeGoodsQueryService takeGoodsQueryService;
	@Autowired
	PreOrderService preOrderService;
	@Autowired
	StationOperationService stationOperationService;

	/**
	 * 进入揽件查询页面
	 * @author 王志宇
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("/toTakeGoodsQuery/{page}")
	public String takeGoodsQuery(HttpServletRequest request, HttpServletResponse response, @PathVariable("page") long page, ExpressCwb4TakeGoodsQuery cwb4TakeGoodsQuery, Model model) throws ParseException {
		long startTime = System.currentTimeMillis();
		this.logger.info("进入揽件查询页面的时间为：" + startTime);
		User user = this.getSessionUser();
		cwb4TakeGoodsQuery.setStation("" + user.getBranchid());
		long getUserTime = System.currentTimeMillis();
		List<User> listUser = this.takeGoodsQueryService.getAllRoleUsers(user);//通过branchid获取所有状态下的小件员 -- 刘武强16.01.20
		this.logger.info("揽件查询页面中查询用户（takeGoodsQueryService.getRoleUsers(user)）操作的时间共：" + (System.currentTimeMillis() - getUserTime));
		String userIds = "";
		for (User usera : listUser) {
			userIds += "," + usera.getUserid();
		}
		if (userIds.length() > 0) {

			userIds = userIds.substring(1, userIds.length());
		}

		long getOrderListTime = System.currentTimeMillis();
		List<ExpressCwbOrderForTakeGoodsQueryVO> listOrderfdsdsadfsdf = this.takeGoodsQueryService.getcwbOrderByPage(page, cwb4TakeGoodsQuery, user, userIds);
		this.logger.info("揽件查询页面中获得订单（takeGoodsQueryService.getcwbOrderByPage(page,cwb4TakeGoodsQuery,user,userIds)）操作的时间共：" + (System.currentTimeMillis() - getOrderListTime));
		model.addAttribute("cwborderList", listOrderfdsdsadfsdf);

		model.addAttribute("deliveryManList", listUser);

		long getCountAllTime = System.currentTimeMillis();
		Long countAll = this.takeGoodsQueryService.getcwborderCount(page, cwb4TakeGoodsQuery, user, listOrderfdsdsadfsdf, userIds);
		this.logger.info("揽件查询页面中获得用户总数量（takeGoodsQueryService.getcwborderCount(page,cwb4TakeGoodsQuery,user,listOrderfdsdsadfsdf,userIds)）操作的时间共：" + (System.currentTimeMillis() - getCountAllTime));
		model.addAttribute("page_obj", new Page(countAll, page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		long getShouldfareAndCountTime = System.currentTimeMillis();
		model.addAttribute("cwb4TakeGoodsQuery", this.takeGoodsQueryService.getShouldfareAndCount(page, cwb4TakeGoodsQuery, user, userIds));
		this.logger.info("揽件查询页面中获得所用汇总费用（takeGoodsQueryService.getShouldfareAndCount(page,cwb4TakeGoodsQuery,user,userIds)）操作的时间共：" + (System.currentTimeMillis() - getShouldfareAndCountTime));
		cwb4TakeGoodsQuery.setCountAll(countAll.intValue());
		//导出标志为真则导出
		if ("true".equals(cwb4TakeGoodsQuery.getExportFlag())) {
			ExportUtil4Express.exportXls(request, response, this.takeGoodsQueryService.getcwbOrders(cwb4TakeGoodsQuery, userIds), ExpressCwbOrderForTakeGoodsQueryVO.class, "运单信息");
		}
		long getBranchTime = System.currentTimeMillis();
		cwb4TakeGoodsQuery.setStation(this.preOrderService.getBranchStations(user.getBranchid(), false).get(0).get("value"));
		this.logger.info("揽件查询页面中获得所有站点（preOrderService.getBranchStations(user.getBranchid(), false)）操作的时间共：" + (System.currentTimeMillis() - getBranchTime));
		this.logger.info("揽件查询页面初始化页面完毕的时间为：" + System.currentTimeMillis());
		this.logger.info("进入揽件查询页面的时间共：" + (System.currentTimeMillis() - startTime));
		return "express/takeGoodsQuery/TakeGoodsQuery";
	}

}
