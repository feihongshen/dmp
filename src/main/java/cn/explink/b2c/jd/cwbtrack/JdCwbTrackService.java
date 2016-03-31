package cn.explink.b2c.jd.cwbtrack;

import java.text.MessageFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.codehaus.jackson.map.ObjectMapper;
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
import cn.explink.domain.Branch;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.User;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.service.CwbOrderService;
import cn.explink.service.CwbOrderWithDeliveryState;
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
				sub.append("<memo>" + getDetail(orderFlow) + "</memo>");
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
	
	
	/**
	 * 获取订单的跟踪详情
	 */
	ObjectMapper objectMapper = new ObjectMapper();
	public String getDetail(OrderFlow orderFlowAll) {
		try {
			CwbOrderWithDeliveryState cwbOrderWithDeliveryState = objectMapper.readValue(orderFlowAll.getFloworderdetail(), CwbOrderWithDeliveryState.class);
			CwbOrder cwbOrder = cwbOrderWithDeliveryState.getCwbOrder();

			String nextbranchname = this.getNextBranchName(cwbOrder);

			User user = userDAO.getUserByUserid(orderFlowAll.getUserid());
			String phone = user.getUsermobile();
			String comment = orderFlowAll.getComment();
			String currentbranchname = branchDAO.getBranchByBranchid(orderFlowAll.getBranchid()).getBranchname();

			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.DaoRuShuJu.getValue()) {
				return MessageFormat.format("从[{0}]导入数据", currentbranchname);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.RuKu.getValue()) {
				return MessageFormat.format("从[{0}]入库;联系电话：[{1}]", currentbranchname, phone);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()) {
				return MessageFormat.format("从[{0}]到错货入库;联系电话：[{1}];备注:[{2}]", currentbranchname, phone, comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.DaoCuoHuoChuLi.getValue()) {
				return MessageFormat.format("从[{0}]到错货处理;联系电话：[{1}];备注:[{2}]", currentbranchname, phone, comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) {
				return MessageFormat.format("从[{0}]出库,下一站[{1}]联系电话[{2}]", currentbranchname, nextbranchname, phone);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.CheXiaoFanKui.getValue()) {
				return MessageFormat.format("货物由[{0}]撤销反馈;联系电话：[{1}]", currentbranchname, phone);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue()) {
				return MessageFormat.format("从[{0}]>库对库出库；下一站[{1}]，联系电话[{2}]", currentbranchname, nextbranchname, phone);
			}

			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()) {
				return MessageFormat.format("从[{0}]到货;联系电话：[{1}]", currentbranchname, phone);
			}
			//中转站入库-->到件
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue()) {
				return MessageFormat.format("从[{0}]中转站入库;联系电话：[{1}]", currentbranchname, phone);
			}
			//中转站出库-->发件
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue()) {
				return MessageFormat.format("从[{0}中转站出库,下一站[{1}]联系电话[{2}]", currentbranchname, nextbranchname, phone);
			}
			
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
				User users = userDAO.getUserByUserid(cwbOrderWithDeliveryState.getDeliveryState().getDeliveryid());
				String deliverphone = users.getUsermobile();
				return MessageFormat.format("货物由[{0}]的派件员[{1}]正在派件..小件员电话:[{2}]", currentbranchname, users.getRealname(), deliverphone);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.YiFanKui.getValue()) {
				User users = userDAO.getUserByUserid(cwbOrderWithDeliveryState.getDeliveryState().getDeliveryid());
				String deliverphone = users.getUsermobile();
				return MessageFormat.format("货物已由[{0}]的派件员[{1}]反馈为[{2}];小件员电话[{3}],备注:[{4}]", currentbranchname, users.getRealname(),
						DeliveryStateEnum.getByValue((int) cwbOrderWithDeliveryState.getDeliveryState().getDeliverystate()).getText(), deliverphone, comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.TuiHuoChuZhan.getValue()) {
				return MessageFormat.format("货物已从[{0}]进行退货出库;联系电话：[{1}];备注：[{2}]", currentbranchname, phone, comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue()) {
				return MessageFormat.format("货物已到退货站[{0}];联系电话：[{1}];备注：[{2}]", currentbranchname, phone, comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue()) {
				return MessageFormat.format("货物已由[{0}]退供货商出库;联系电话：[{1}];备注：[{2}]", currentbranchname, phone, comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.GongHuoShangTuiHuoChenggong.getValue()) {
				return MessageFormat.format("货物已由[{0}]退供货商成功;联系电话：[{1}];备注：[{2}]", currentbranchname, phone, comment);
			}
			//已审核
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) {
				long deliverystate = cwbOrderWithDeliveryState.getDeliveryState().getDeliverystate();

				if (deliverystate == DeliveryStateEnum.PeiSongChengGong.getValue() || deliverystate == DeliveryStateEnum.ShangMenHuanChengGong.getValue()
						|| deliverystate == DeliveryStateEnum.ShangMenTuiChengGong.getValue()) {
					String signman = cwbOrderWithDeliveryState.getDeliveryState().getSign_man();
					if (signman == null || signman.isEmpty()) {
						signman = cwbOrder.getConsigneename();
					}

					return MessageFormat.format("订单审核为已签收,签收人是：{0}", signman);
				} else if (deliverystate == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
					return MessageFormat.format("订单审核为站点滞留,原因:{0}", cwbOrder.getLeavedreason());
				} else if (deliverystate == DeliveryStateEnum.JuShou.getValue() || deliverystate == DeliveryStateEnum.BuFenTuiHuo.getValue()
						|| deliverystate == DeliveryStateEnum.ShangMenJuTui.getValue()) {
					return MessageFormat.format("订单审核为拒收,原因:{0}", cwbOrder.getBackreason());
				} else if (deliverystate == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
					return MessageFormat.format("订单{0}审核为丢失,原因:{1}", cwbOrder.getCwb(), cwbOrder.getLosereason());
				} else {
					return MessageFormat.format("订单{0}已审核", cwbOrder.getCwb());
				}

			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.UpdateDeliveryBranch.getValue()) {
				return MessageFormat.format("货物配送站点变更为[{0}];操作人：[{1}];联系电话：[{2}]", branchDAO.getBranchByBranchid(cwbOrder.getDeliverybranchid()).getBranchname(),
						userDAO.getUserByUserid(orderFlowAll.getUserid()).getRealname(), phone);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue()) {
				return MessageFormat.format("货物已由[{0}]退供货商拒收返库入库;联系电话：[{1}];备注：[{2}]", currentbranchname, phone, comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.BeiZhu.getValue()) {
				return MessageFormat.format("货物被[{0}]添加了备注;联系电话：[{1}];备注：[{2}]", userDAO.getUserByUserid(orderFlowAll.getUserid()).getRealname(), phone, comment);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	private String getNextBranchName(CwbOrder cwborder) {
		Branch nextBranch = branchDAO.getBranchByBranchid(cwborder.getNextbranchid());
		if (nextBranch == null) {
			return "";
		}
		return nextBranch.getBranchname();
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
