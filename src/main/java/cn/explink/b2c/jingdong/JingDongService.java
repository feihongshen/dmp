package cn.explink.b2c.jingdong;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.explink.ExplinkService;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.service.CustomerService;
import cn.explink.service.CwbOrderService;
import cn.explink.util.DateTimeUtil;

@Service
public class JingDongService {
	private Logger logger = LoggerFactory.getLogger(JingDongService.class);

	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	ExplinkService explinkService;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	CwbOrderService cwbOrderService;

	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	CustomerService customerService;

	public String getObjectMethod(int key) {
		JointEntity obj = jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}

	public JingDong getJingDong(int key) {
		if (getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
		JingDong smile = (JingDong) JSONObject.toBean(jsonObj, JingDong.class);
		return smile;
	}

	@Transactional
	public void edit(HttpServletRequest request, int joint_num) {
		JingDong jingdong = new JingDong();
		String customerids = request.getParameter("customerids");
		jingdong.setCustomerids(customerids);
		jingdong.setSearch_url(request.getParameter("search_url"));
		int maxcount = Integer.valueOf(request.getParameter("maxcount"));
		jingdong.setMaxcount(maxcount);

		String oldCustomerids = "";

		JSONObject jsonObj = JSONObject.fromObject(jingdong);
		JointEntity jointEntity = jiontDAO.getJointEntity(joint_num);
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Create(jointEntity);
		} else {
			try {
				oldCustomerids = getJingDong(joint_num).getCustomerids();
			} catch (Exception e) {
			}
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Update(jointEntity);
		}
		// 保存 枚举到供货商表中
		customerDAO.updateB2cEnumByJoint_num(customerids, oldCustomerids, joint_num);
		this.customerService.initCustomerList();
	}

	public void update(int joint_num, int state) {
		jiontDAO.UpdateState(joint_num, state);
	}

	/**
	 * 京东请求接口开始
	 * 
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 */

	public String requestCwbSearchInterface(String billcode, JingDong jingdong) throws Exception {

		try {

			if (billcode == null || billcode.equals("")) {
				return "请求参数billcode不能为空";
			}
			if (billcode.split(",").length > jingdong.getMaxcount()) {
				return "请求订单数量不可超过[" + jingdong.getMaxcount() + "]个";
			}

			int effect_count = 0; // 计数存在的订单
			for (String cwb : billcode.split(",")) {
				String cwbTransCwb = cwbOrderService.translateCwb(cwb); // 可能是订单号也可能是运单号
				CwbOrder cwbOrder = cwbDAO.getCwbByCwb(cwbTransCwb);
				if (cwbOrder != null) {
					effect_count++;
				}
			}
			if (effect_count == 0) {
				return "未检索到数据";
			}

			logger.debug("[京东]请求订单列表cwbs={}", billcode);

			// 构建Xml
			String responseXML = BuildTrackInfoXML(billcode);
			logger.debug("[京东]查询跟踪返回XML=[{}]", responseXML);

			return responseXML;

		} catch (Exception e) {
			String error = "处理[京东]查询请求发生未知异常:" + e.getMessage();
			logger.error(error, e);
			return error;
		}

	}

	private String BuildTrackInfoXML(String billcode) {
		StringBuffer sub = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sub.append("<root>");
		for (String cwb : billcode.split(",")) {
			String cwbTransCwb = cwbOrderService.translateCwb(cwb); // 可能是订单号也可能是运单号

			List<OrderFlow> orderlist = orderFlowDAO.getOrderFlowByCwb(cwbTransCwb);
			if (orderlist == null || orderlist.size() == 0) {
				continue;
			}
			sub.append("<track>");
			sub.append("<billcode>" + cwb + "</billcode>");
			for (OrderFlow orderFlow : orderlist) {
				if (getJingDongFlowEnum(orderFlow.getFlowordertype()) == null) {
					continue;
				}

				sub.append("<detail>");
				sub.append("<time>" + DateTimeUtil.formatDate(orderFlow.getCredate()) + "</time>");
				sub.append("<scantype>" + getFlowOrdertypeText(orderFlow.getFlowordertype(), cwbTransCwb) + "</scantype>");
				sub.append("<memo>" + explinkService.getDetail(orderFlow) + "</memo>");
				sub.append("</detail>");
			}
			sub.append("</track>");
		}
		sub.append("</root>");
		return sub.toString();
	}

	private String getFlowOrdertypeText(long flowordertype, String cwb) {
		for (FlowOrderTypeEnum em : FlowOrderTypeEnum.values()) {
			if (em.getValue() == flowordertype) {
				if (em.getValue() == FlowOrderTypeEnum.YiFanKui.getValue()) {
					long delivery_state = deliveryStateDAO.getActiveDeliveryStateByCwb(cwb).getDeliverystate();
					if (delivery_state == DeliveryStateEnum.PeiSongChengGong.getValue()) {
						return "妥投";
					}
					if (delivery_state == DeliveryStateEnum.ShangMenTuiChengGong.getValue()) {
						return "上门退成功";
					}
					if (delivery_state == DeliveryStateEnum.ShangMenHuanChengGong.getValue()) {
						return "上门换成功";
					}
					if (delivery_state == DeliveryStateEnum.JuShou.getValue()) {
						return "拒收";
					}
					if (delivery_state == DeliveryStateEnum.BuFenTuiHuo.getValue()) {
						return "拒收";
					}
					if (delivery_state == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
						return "分站滞留";
					}
					if (delivery_state == DeliveryStateEnum.HuoWuDiuShi.getValue()) {
						return "货物丢失";
					}

				} else {
					return em.getText();
				}

			}
		}
		return "";
	}

	public String getJingDongFlowEnum(long flowordertype) {
		for (JingDongFlowEnum dd : JingDongFlowEnum.values()) {
			if (flowordertype == dd.getFlowordertype()) {
				return dd.getFlowordertype() + "";
			}
		}

		return null;

	}

}
