package cn.explink.b2c.jd.cwbtrack;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.explink.ExplinkService;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.service.CwbOrderService;
import cn.explink.util.DateTimeUtil;

@Service
public class JdCwbTrackService {
	private Logger logger = LoggerFactory.getLogger(JdCwbTrackService.class);

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
	BranchDAO branchDAO;
	@Autowired
	UserDAO userDAO;

	@Autowired
	DeliveryStateDAO deliveryStateDAO;

    /**
     * 编辑接口配置信息
     */
	public void edit(HttpServletRequest request, int joint_num) {
		JdCwbTrackConfig config = new JdCwbTrackConfig();
		String maxCount =request.getParameter("maxCount").trim();
		String customerId=request.getParameter("customerId").trim();		
		String privateKey=request.getParameter("privateKey").trim();		
		
		config.setMaxCount(maxCount==""?50:Integer.valueOf(maxCount));
		config.setCustomerId(customerId==""?0:Long.parseLong(customerId));
		config.setPrivateKey(privateKey);
		
		JSONObject jsonObj = JSONObject.fromObject(config);
		JointEntity jointEntity = jiontDAO.getJointEntity(joint_num);
		if (jointEntity == null) {//新增
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Create(jointEntity);
		} else {//修改
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Update(jointEntity);
		}
	}
	
	/**
	 * 京东请求接口开始
	 */
	public String requestCwbSearchInterface(String billcode, JdCwbTrackConfig config) throws Exception {
		try {
			if (billcode == null || billcode.equals("")) {
				return "请求参数billcode不能为空";
			}
			if (billcode.split(",").length > config.getMaxCount()) {
				return "请求订单数量不可超过[" + config.getMaxCount() + "]个";
			}

			int effect_count = 0; // 计数存在的订单
			for (String cwb : billcode.split(",")) {
				String cwbTransCwb = cwbOrderService.translateCwb(cwb); // 可能是订单号也可能是运单号
				CwbOrder cwbOrder = cwbDAO.getCwbByCwb(cwbTransCwb);
				if (cwbOrder != null && cwbOrder.getCustomerid()==config.getCustomerId()) {
					effect_count++;
				}
			}
			if (effect_count == 0) {
				return "未检索到订单数据";
			}

			logger.debug("[京东_订单跟踪]]请求订单列表cwbs={}", billcode);

			// 构建Xml
			String responseXML = BuildTrackInfoXML(billcode,config);
			logger.debug("[京东_订单跟踪]查询跟踪返回XML=[{}]", responseXML);
			return responseXML;

		} catch (Exception e) {
			String error = "处理[京东_订单跟踪]查询请求发生未知异常:" + e.getMessage();
			logger.error(error, e);
			return error;
		}
	}
	
	/**
	 * 构建响应报文
	 */
	private String BuildTrackInfoXML(String billcode,JdCwbTrackConfig config) {
		StringBuffer sub = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sub.append("<root>");
		String scantype="";//扫描类型
		for (String cwb : billcode.split(",")) {
			String cwbTransCwb = cwbOrderService.translateCwb(cwb); // 可能是订单号也可能是运单号

			//List<OrderFlow> orderlist = orderFlowDAO.getOrderFlowByCwb(cwbTransCwb);
			List<OrderFlow> orderlist = orderFlowDAO.getOrderFlowByCwbAndCustomerid(cwbTransCwb,config.getCustomerId());
			if (orderlist == null || orderlist.size() == 0) {
				continue;
			}
			sub.append("<track>");
			sub.append("<billcode>" + cwb + "</billcode>");
			
			for (OrderFlow orderFlow : orderlist) {
				scantype=getScantype(orderFlow.getFlowordertype(),cwbTransCwb);
				
				if ("".equals(scantype)) {//跳过扫描类型为""的轨迹
					continue;
				}

				sub.append("<detail>");
				sub.append("<time>" + DateTimeUtil.formatDate(orderFlow.getCredate()) + "</time>");
				sub.append("<scantype>" +scantype+ "</scantype>");
				sub.append("<memo>" + explinkService.getDetail(orderFlow) + "</memo>");
				sub.append("</detail>");
			}
			sub.append("</track>");
		}
		sub.append("</root>");
		return sub.toString();
	}
	
	
	/**
	 * 获取扫描类型
	 * @param flowordertype  
	 * @param cwb 订单号
	 */
	private String getScantype(int flowordertype,String cwb){
		String jdTrackFlowText="";
		if(flowordertype==FlowOrderTypeEnum.ChuKuSaoMiao.getValue()
				||flowordertype==FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue()){
			//出库扫描(出库、退货再投、站点出站) 中转站出库 对应 发件
			jdTrackFlowText="发件";
		}
		else if(flowordertype==FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()
				||flowordertype==FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()//到错货
				||flowordertype==FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue()
				||flowordertype==FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue()){
			//分站到货 倒错货 退货站入库 中转站入库 对应 到件
			jdTrackFlowText="到件";
		}
		else if(flowordertype==FlowOrderTypeEnum.FenZhanLingHuo.getValue()){
			//分站领货 对应 派件
			jdTrackFlowText="派件";
		}
		else if(flowordertype==FlowOrderTypeEnum.YiFanKui.getValue()){//已反馈
			long delivery_state = deliveryStateDAO.getActiveDeliveryStateByCwb(cwb).getDeliverystate();
			//分站滞留 待中转 拒收 对应 问题件扫描
			if (delivery_state == DeliveryStateEnum.FenZhanZhiLiu.getValue()
					||delivery_state ==DeliveryStateEnum.DaiZhongZhuan.getValue()
					||delivery_state ==DeliveryStateEnum.JuShou.getValue()) {
				jdTrackFlowText="问题件扫描";
			}
		}
		return jdTrackFlowText;
	}
	

	//获取接口配置信息
	public JdCwbTrackConfig getJdCwbTrackConfig(int key) {
		if (getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
		JdCwbTrackConfig smile = (JdCwbTrackConfig) JSONObject.toBean(jsonObj, JdCwbTrackConfig.class);
		return smile;
	}
	
	public String getObjectMethod(int key) {
		JointEntity obj = jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}
	
	/**
	 * 更新接口状态
	 */
	public void update(int joint_num, int state) {
		jiontDAO.UpdateState(joint_num, state);
	}

}
