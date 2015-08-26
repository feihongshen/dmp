package cn.explink.manager;

import java.util.List;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.GotoClassAuditingDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.PayUpDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.GotoClassAuditing;
import cn.explink.domain.PayUp;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.service.CwbOrderService;
import cn.explink.service.PayUpService;

@Controller
@RequestMapping("/manage")
public class ManagerController {

	private Logger logger = LoggerFactory.getLogger(ManagerController.class);

	@Autowired
	OrderFlowDAO orderFlowDAO;

	@Autowired
	PayUpDAO payUpDAO;

	@Autowired
	PayUpService payUpService;

	@Autowired
	UserDAO userDAO;

	@Autowired
	GotoClassAuditingDAO gcaDAO;

	@Autowired
	CwbOrderService cwbOrderService;

	@Autowired
	GotoClassAuditingDAO gotoClassAuditingDAO;

	@RequestMapping("/")
	public String main(String cwb) {
		return "manage";
	}

	@RequestMapping("/resendFlowJms")
	public @ResponseBody String resendFlowJms(@RequestParam("cwbs") String cwbs) {
		String[] split = cwbs.split("\n");
		for (String cwb : split) {
			if (cwb.trim().length() == 0) {
				continue;
			}
			logger.info("resending flow for {} ", cwb);
			List<OrderFlow> orderflows = orderFlowDAO.getOrderFlowListByCwb(cwb.trim());
			for (OrderFlow orderFlow : orderflows) {
				logger.info("resending flow for {} with state {} ", cwb, orderFlow.getFloworderdetail());
				cwbOrderService.send(orderFlow);
			}
		}

		return "ok" + split.length;
	}
	@RequestMapping("/resendFlowJmsEnd")
	public @ResponseBody String resendFlowJmsEnd(@RequestParam("cwbs") String cwbs) {
		String[] split = cwbs.split("\n");
		for (String cwb : split) {
			if (cwb.trim().length() == 0) {
				continue;
			}
			logger.info("resending flow for {} ", cwb);
			OrderFlow orderFlow = orderFlowDAO.getOrderCurrentFlowByCwb(cwb.trim());
			if(orderFlow == null ){
				continue;
			}	
			cwbOrderService.send(orderFlow);
		}
		
		return "ok" + split.length;
	}

	@RequestMapping("/resendPayup")
	public @ResponseBody String resendPayup(@RequestParam("ids") String ids) {
		String[] split = ids.split(",");
		for (String id : split) {
			if (id.trim().length() == 0) {
				continue;
			}
			logger.info("resending Payup for {} ", id);
			PayUp pu = payUpDAO.getPayUpById(Long.parseLong(id.trim()));

			List<GotoClassAuditing> gcaList = gcaDAO.getGotoClassAuditingByPayUpId(pu.getId());
			String gcaids = "";
			for (GotoClassAuditing gca : gcaList) {
				gcaids += "'" + gca.getId() + "',";
			}
			gcaids = gcaids.substring(0, gcaids.length() - 1);

			JSONObject sendJson = new JSONObject();
			sendJson.put("payupid", pu.getId());
			sendJson.put("should_amount", pu.getAmount().doubleValue());// 应交款
			sendJson.put("payup_amount", pu.getAmount().doubleValue());// 实交款
			sendJson.put("payup_amount_pos", pu.getAmountPos().doubleValue());// POS刷卡金额
			sendJson.put("arrearage_huo_amount", 0);// 默认交款欠款
			sendJson.put("arrearage_fa_amount", 0);// 默认罚款欠款
			sendJson.put("payup_type", pu.getType());// 交款类型1货款 2罚款
			sendJson.put("gcaids", gcaids);// 交款对应的当前的归班记录的id
			sendJson.put("remark", pu.getRemark());
			sendJson.put("credatetime", pu.getCredatetime());
			sendJson.put("branchid", pu.getBranchid());
			try {
				sendJson.put("payup_realname", userDAO.getUserByid(pu.getUserid()).get(0).getRealname());
			} catch (Exception ee) {
				sendJson.put("payup_realname", pu.getUserid());
			}
			sendJson.put("upbranchid", pu.getUpbranchid());// 上交至哪个财务
			sendJson.put("way", pu.getWay());// 缴款方式
			logger.info("resending Payup for {} with state {} ", pu.getId(), sendJson.toString());
			try {
				/*
				 * {"should_amount":25481.8,"payup_amount":0,"payup_amount_pos":0
				 * , "arrearage_huo_amount":25481.8,"arrearage_fa_amount":0,
				 * "payup_type":0, "gcaids":
				 * "'10073','10079','10107','10110','10114','10115','10116','10139','10145'"
				 * ,
				 * "remark":"000096","credatetime":"2012-11-14 09:46:39","branchid"
				 * :228,
				 * "payup_realname":"刘小悦","upbranchid":189,"way":0,"payupid"
				 * :917}
				 */
				payUpService.sendPayUp(sendJson.toString());
			} catch (Exception ee) {
				logger.error("ERROR Payup for {} ", pu.getId());
			}
		}
		return "ok" + split.length;
	}

	@RequestMapping("/resendGotoClassJms")
	public @ResponseBody String resendGotoClassJms(@RequestParam("gcaids") String gcaids) {
		String[] split = gcaids.split("\n");
		for (String gcaid : split) {
			if (gcaid.trim().length() == 0) {
				continue;
			}
			logger.info("resending gotoclass message for {} ", gcaid);
			cwbOrderService.okJMS(gotoClassAuditingDAO.getGotoClassAuditingByGcaid(Integer.parseInt(gcaid)));
		}

		return "ok" + split.length;
	}

}
