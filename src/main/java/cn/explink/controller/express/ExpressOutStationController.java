package cn.explink.controller.express;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.RoleDAO;
import cn.explink.dao.TruckDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Bale;
import cn.explink.domain.Branch;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.Truck;
import cn.explink.domain.User;
import cn.explink.domain.VO.express.ExpressOpeAjaxResult;
import cn.explink.domain.VO.express.ExpressOutStationParamsVO;
import cn.explink.enumutil.CwbOrderPDAEnum;
import cn.explink.enumutil.express.AddressMatchEnum;
import cn.explink.enumutil.express.ExpressOutStationFlagEnum;
import cn.explink.service.CwbOrderService;
import cn.explink.service.express.ExpressOutStationService;
import cn.explink.service.express.TpsInterfaceExecutor;
import cn.explink.service.express.tps.enums.FeedbackOperateTypeEnum;
import cn.explink.util.ServiceUtil;
import cn.explink.util.Tools;

/**
 * 揽件出站
 *
 * @author jiangyu 2015年8月4日
 *
 */
@RequestMapping("/expressOutStation")
@Controller
public class ExpressOutStationController extends ExpressCommonController {

	@Autowired
	UserDAO userDAO;
	@Autowired
	RoleDAO roleDAO;
	@Autowired
	HttpSession session;
	@Autowired
	TruckDAO trunkDAO;
	@Autowired
	CwbOrderService cwborderService;

	@Autowired
	ExpressOutStationService expressOutStationService;
	@Autowired
	BranchDAO branchDAO;

	@Autowired
	private TpsInterfaceExecutor tpsInterfaceExecutor;

	/**
	 * 进入揽件出站的功能页面
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/expressOutStationIndex")
	public String branchexportwarhouse(Model model, @RequestParam(value = "branchid", defaultValue = "0") long branchid) {
		long startTime = System.currentTimeMillis();
		User user = this.getSessionUser();
		// add by wangzhiyu 获取下一站集合
		List<Branch> nextBranchList = this.expressOutStationService.getNextBranchList(user.getBranchid(), user);

		List<User> userList = this.userDAO.getUserByRole(FeedbackOperateTypeEnum.OutboundScan.getValue());
		List<Truck> trunkList = this.trunkDAO.getAllTruck();
		model.addAttribute("branchlist", nextBranchList);
		model.addAttribute("userList", userList);
		model.addAttribute("truckList", trunkList);
		model.addAttribute("branchid", branchid);
		this.logger.info("进入揽件出站页面的时间共：" + (System.currentTimeMillis() - startTime) + "毫秒");
		return "express/outStation/expressOutStation";
	}

	/**
	 * 揽件出站操作
	 *
	 * @param model
	 * @param params
	 * @return
	 */
	@RequestMapping("/executeOutStation")
	@ResponseBody
	public ExpressOpeAjaxResult expressOutStationExecute(Model model, HttpServletRequest request, HttpServletResponse response, ExpressOutStationParamsVO params) {
		//当前请求时间
		long startTime = System.currentTimeMillis();
		//返回数据
		ExpressOpeAjaxResult res = new ExpressOpeAjaxResult();
		//获取请求路径
		String contextPath = request.getContextPath();
		Map<String, Object> checkResMap = new HashMap<String, Object>();
		try {
			//校验订单号是否为空
			if (!Tools.isEmpty(params.getScanNo())) {
				//赋值当前路径
				params.setContextPath(contextPath);
				//快递单号
				String scanNo = params.getScanNo();
				scanNo = this.cwborderService.translateCwb(scanNo);// 订单号和运单号的转换
				checkResMap = this.expressOutStationService.checkIsOrderOrBaleOperation(scanNo, params);
				//声音文件路径
				String wavPath = null;
				if (ExpressOutStationFlagEnum.OrderNo.getValue().equals(checkResMap.get("opeFlag"))) {
					// 订单号操作
					res = this.expressOutStationService.executeOutStationOpeOrderNo(this.getSessionUser(), scanNo, params);
					if (res.getStatus()) {// 成功
						// 匹配地址库
						CwbOrder cwbOrder = this.cwborderService.getCwbByCwb(scanNo);
						this.matchStation(scanNo, this.getSessionUser().getUserid(), cwbOrder.getConsigneeaddress());
						wavPath = this.getErrorWavFullPath(request, CwbOrderPDAEnum.OK.getVediourl());
					} else {
						wavPath = this.getErrorWavFullPath(request, CwbOrderPDAEnum.SYS_ERROR.getVediourl());
					}

				} else if (ExpressOutStationFlagEnum.BaleNo.getValue().equals(checkResMap.get("opeFlag"))) {
					// 包号操作
					res = this.expressOutStationService.executeOutStationOpeBaleNo(this.getSessionUser(), scanNo, params);
					if (res.getStatus()) {
						// 匹配地址库
						Bale bale=(Bale) checkResMap.get("bale");
						List<String> cwbList = this.cwborderService.getCwbsByBale(bale.getId());
						for (String cwb : cwbList) {
							CwbOrder cwbOrder = this.cwborderService.getCwbByCwb(cwb);
							this.matchStation(cwb, this.getSessionUser().getUserid(), cwbOrder.getConsigneeaddress());
						}
						wavPath = request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl();
					} else {
						wavPath = request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.Feng_Bao.getVediourl();
					}
				}
				res.addLongWav(wavPath);
			}
		} catch (Exception e) {
			checkResMap.put("opeFlag", ExpressOutStationFlagEnum.OrderNo.getValue());
			res.setAttributes(checkResMap);
			res.setStatus(false);
			res.setMsg(e.getMessage());
			String wavPath = this.getErrorWavFullPath(request, CwbOrderPDAEnum.SYS_ERROR.getVediourl());
			res.addLongWav(wavPath);
		}
		this.logger.info("进入揽件出站页面的时间共：" + (System.currentTimeMillis() - startTime) + "毫秒");
		return res;
	}

	private void matchStation(String cwb, long userid, String address) {
		Boolean matchFlag = this.tpsInterfaceExecutor.autoMatch(cwb, userid, address, AddressMatchEnum.ExpressOutStation.getValue());
		if (matchFlag) {
			this.logger.info("揽件出站调用地址库jms消息发送成功");

		} else {
			this.logger.info("揽件出站调用地址库jms消息发送失败");
		}
	}

	private String getErrorWavFullPath(HttpServletRequest request, String fillName) {
		return request.getContextPath() + ServiceUtil.waverrorPath + fillName;
	}

	private String getWavFullPath(HttpServletRequest request, String fillName) {
		return request.getContextPath() + ServiceUtil.wavPath + fillName;
	}

}
