package cn.explink.pos.chinaums.searchpos;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.RestHttpServiceHanlder;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.PosPayMoneyDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.pos.chinaums.ChinaUms;
import cn.explink.pos.chinaums.ChinaUmsService;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.pos.tools.PosEnum;
import cn.explink.pos.tools.PosTradeDetail;
import cn.explink.util.MD5.MD5Util;

/**
 * 根据订单号查询支付交易结果
 * 
 * @author Administrator
 *
 */
@Service
public class ChinaUmsService_searchPos extends ChinaUmsService {

	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	JiontDAO jointDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	PosPayMoneyDAO posPayMoneyDAO;

	private Logger logger = LoggerFactory.getLogger(ChinaUmsService_searchPos.class);

	/**
	 * 根据订单号查询北京银联交易结果
	 * 
	 * @return
	 */
	public String cwbSearchtoPos(String cwb) {
		try {
			String returnCode = "";

			int isOpenFlag = jointDAO.getStateByJointKey(PosEnum.ChinaUms.getKey());
			if (isOpenFlag == 0) {
				returnCode = "未开启ChinaUms对接";
			}

			ChinaUms chinaums = getChinaUmsSettingMethod(PosEnum.ChinaUms.getKey());
			CwbOrder cwbOrder = cwbDAO.getCwbByCwb(cwb);
			if (cwbOrder == null) {
				returnCode = "订单号" + cwb + "在系统中不存在";
			}

			DeliveryState deliveryState = deliveryStateDAO.getActiveDeliveryStateByCwb(cwb);
			long branchid = deliveryState.getDeliverybranchid();
			long userid = deliveryState.getDeliveryid();

			String transcwb = cwbOrder.getTranscwb();
			String employ_no = userDAO.getAllUserByid(userid).getUsername();
			String net_code = String.valueOf(branchid);
			String mer_id = String.valueOf(cwbOrder.getCustomerid());

			String term_id = ""; // //???终端号这块可能无法提供，因为是支付不成功，终端号不会传输到这里
			String cod = cwbOrder.getReceivablefee() + "";

			Map<String, String> params = buildRequestParams(cwb, chinaums, transcwb, employ_no, net_code, mer_id, term_id, cod);

			String responseJson = RestHttpServiceHanlder.sendHttptoServer(params, chinaums.getRequest_url());

			ChinaUmsJsonBean jsonBean = JacksonMapper.getInstance().readValue(responseJson, ChinaUmsJsonBean.class);

		} catch (Exception e) {
			logger.error("请求chinaums查询POS未知异常", e);
		}

		return null;
	}

	private Map<String, String> buildRequestParams(String cwb, ChinaUms chinaums, String transcwb, String employ_no, String net_code, String mer_id, String term_id, String cod) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("order_no", cwb); // 订单号
		params.put("dsorder_no", transcwb); // 运单号
		params.put("employ_no", employ_no); // 操作员编号
		params.put("net_code", net_code); // 寄件网点编号
		params.put("mer_id", chinaums.getMer_id()); // 商户号
		params.put("mer_name", ""); // 商户名称，暂时为空
		params.put("term_id", term_id); // 终端号 暂时默认为""
		params.put("cod", cod); // 金额
		String signStr = cwb + transcwb + employ_no + net_code + mer_id + term_id + cod + chinaums.getPrivate_key();
		String mac = MD5Util.md5(signStr);
		params.put("mac", mac);
		return params;
	}

}
