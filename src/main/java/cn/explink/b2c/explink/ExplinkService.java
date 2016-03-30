package cn.explink.b2c.explink;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.User;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.service.CwbOrderService;
import cn.explink.service.CwbOrderWithDeliveryState;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.StringUtil;
import cn.explink.util.MD5.MD5Util;

@Service
public class ExplinkService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	CwbOrderService cwbOrderService;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;

	public String getObjectMethod(int key) {
		JointEntity obj = jiontDAO.getJointEntity(key);
		if (obj != null) {
			String posValue = obj.getJoint_property();
			return posValue;
		} else {
			return null;
		}
	}

	public Explink getExplink(int key) {
		if (getObjectMethod(key) != null) {
			JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
			Explink liebo = (Explink) JSONObject.toBean(jsonObj, Explink.class);
			return liebo;
		} else {
			return null;
		}
	}

	public void edit(HttpServletRequest request, int joint_num) {
		Explink explink = new Explink();
		explink.setCompanyname(StringUtil.nullConvertToEmptyString(request.getParameter("companyname")));
		explink.setCount(Integer.parseInt(StringUtil.nullConvertToEmptyString(request.getParameter("count"))));
		explink.setKey(StringUtil.nullConvertToEmptyString(request.getParameter("key")));
		explink.setUrl(StringUtil.nullConvertToEmptyString(request.getParameter("url")));
		explink.setVersion(StringUtil.nullConvertToEmptyString(request.getParameter("version")));
		explink.setNewSignMethod(Integer.valueOf(request.getParameter("newSignMethod")));
		
		
		JSONObject jsonObj = JSONObject.fromObject(explink);

		JointEntity jointEntity = jiontDAO.getJointEntity(joint_num);
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Create(jointEntity);
		} else {
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Update(jointEntity);
		}
	}

	public void update(int joint_num, int state) {
		jiontDAO.UpdateState(joint_num, state);
	}

	public JointEntity getJointEntity(String companyname) {
		return jiontDAO.getJointEntityByCompanyname(companyname);
	}

	private Explink getExplinkByCompany(String companyname) {
		JointEntity joint = getJointEntity(companyname);
		if (joint != null && joint.getJoint_property() != null && !joint.getJoint_property().equals("")) {
			JSONObject jsonO = JSONObject.fromObject(joint.getJoint_property());
			return (Explink) JSONObject.toBean(jsonO, Explink.class);
		}
		return null;
	}

	public String getCwbStatusInterface(String cwbs, String companyname, String sign) throws JsonParseException, JsonMappingException, IOException {
		JointEntity joint = getJointEntity(companyname);
		if (joint != null && joint.getState() == 0) {
			logger.warn("未开启explink对接，joint_num=" + joint.getJoint_num());
			return respExptMsg("未开启此对接");
		}

		Explink explink = getExplinkByCompany(companyname);

		try {
			validatorBusinessMethod(cwbs, companyname, sign, explink);
		} catch (RuntimeException e) {
			logger.error("请求explink发生异常,订单号=" + cwbs + "," + e.getMessage(), e);
			return respExptMsg(e.getMessage());
		}
		String cwbTransCwb = cwbOrderService.translateCwb(cwbs); // 可能是订单号也可能是运单号

		List<OrderFlow> list = orderFlowDAO.getOrderFlowByCwb(cwbTransCwb);
		if (list == null || list.size() == 0) {
			return respExptMsg("没有检索到数据");
		}

		return parseCwbStatusXML(list, cwbs, explink);

	}

	public String getExplinkFlowEnum(long flowordertype) {
		for (ExplinkFlowEnum dd : ExplinkFlowEnum.values()) {
			if (dd.getStatuscode().equals(flowordertype + "") && flowordertype != FlowOrderTypeEnum.YiFanKui.getValue() && flowordertype != FlowOrderTypeEnum.YiShenHe.getValue()) {
				return dd.getStatuscode();
			}
			if (flowordertype == FlowOrderTypeEnum.YiFanKui.getValue() || flowordertype == FlowOrderTypeEnum.YiShenHe.getValue()) {
				return "有结果";
			}
		}

		return null;

	}

	private String parseCwbStatusXML(List<OrderFlow> list, String ordcwb, Explink explink) throws JsonParseException, JsonMappingException, IOException {
		StringBuffer responesXML = new StringBuffer();
		responesXML.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		responesXML.append("<Result>");
		for (int i = 0; i < list.size(); i++) {
			OrderFlow orderflow = list.get(i);

			String credatetime = DateTimeUtil.formatDate(orderflow.getCredate());
			String updatetime = "2013-01-11 05:00:00";
			double diffHours = DateTimeUtil.getHourFromToTime(credatetime, updatetime);

			if (getExplinkFlowEnum(orderflow.getFlowordertype()) == null) {
				if (diffHours < 0) {
					continue;
				}

			}

			if ("0".equals(explink.getVersion())) { // 旧版
				if (orderflow.getFlowordertype() == FlowOrderTypeEnum.YiFanKui.getValue()) {
					continue;
				}
				String podreultname = getFlowOrderTextById(orderflow.getFlowordertype());
				if (podreultname.equals("审核")) {
					podreultname = "反馈";
				}
				responesXML.append("<row resultcount=\"" + (i + 1) + "\">");
				responesXML.append("<cwb>" + ordcwb + "</cwb>");// <!--订单号 -->
				responesXML.append("<trackdatetime>" + DateTimeUtil.formatDate(orderflow.getCredate()) + "</trackdatetime>");// <!--操作时间
																																// -->
				responesXML.append("<branchname>" + branchDAO.getBranchByBranchid(orderflow.getBranchid()).getBranchname() + "</branchname>");// <!--站点
																																				// -->
				responesXML.append("<trackevent>" + (diffHours < 0 ? getDetail(orderflow) : getFloworderTrackInfo(orderflow)) + "</trackevent>");// <!--跟踪信息
																																					// -->
				responesXML.append("<podresultname>" + podreultname + "</podresultname>");// <!--订单状态
																							// -->
			} else {
				ExplinkFlowEnum crossEnum = getFlowOrdertypeEnum(orderflow.getFlowordertype(), orderflow.getCwb(), orderflow.getFloworderdetail());
				if (crossEnum == null) {
					continue;
				}
				responesXML.append("<row resultcount=\"" + (i + 1) + "\">");
				responesXML.append("<cwb>" + ordcwb + "</cwb>");// <!--订单号 -->
				responesXML.append("<trackdatetime>" + DateTimeUtil.formatDate(orderflow.getCredate()) + "</trackdatetime>");// <!--操作时间
																																// -->
				responesXML.append("<branchname>" + branchDAO.getBranchByBranchid(orderflow.getBranchid()).getBranchname() + "</branchname>");// <!--站点
																																				// -->
				responesXML.append("<trackevent>" + (diffHours < 0 ? getDetail(orderflow) : getFloworderTrackInfo(orderflow)) + "</trackevent>");// <!--跟踪信息
																																					// -->
				responesXML.append("<podresultname>" + crossEnum.getText() + "</podresultname>");// <!--订单状态
																									// -->
				responesXML.append("<statuscode>" + crossEnum.getStatuscode() + "</statuscode>");// <!--订单状态
																									// code-->
			}

			responesXML.append("</row>");
		}
		responesXML.append("</Result>");
		return responesXML.toString();

	}

	private ExplinkFlowEnum getFlowOrdertypeEnum(long flowordertype, String cwb, String flowdetail) throws JsonParseException, JsonMappingException, IOException {
		for (ExplinkFlowEnum em : ExplinkFlowEnum.values()) {
			if (flowordertype != FlowOrderTypeEnum.YiFanKui.getValue() && flowordertype != FlowOrderTypeEnum.YiShenHe.getValue()) {
				if (String.valueOf(flowordertype).equals(em.getStatuscode())) {
					return em;
				}

			}
			CwbOrderWithDeliveryState deliveryState = objectMapper.readValue(flowdetail, CwbOrderWithDeliveryState.class);

			if (flowordertype == FlowOrderTypeEnum.YiFanKui.getValue()) {
				long delivery_state = deliveryState.getDeliveryState().getDeliverystate();
				if (delivery_state == DeliveryStateEnum.PeiSongChengGong.getValue()) {
					return ExplinkFlowEnum.PeiSongChengGong;
				}
				if (delivery_state == DeliveryStateEnum.ShangMenTuiChengGong.getValue()) {
					return ExplinkFlowEnum.ShangMenTuiChengGong;
				}
				if (delivery_state == DeliveryStateEnum.ShangMenHuanChengGong.getValue()) {
					return ExplinkFlowEnum.ShangMenHuanChengGong;
				}
				if (delivery_state == DeliveryStateEnum.JuShou.getValue() || delivery_state == DeliveryStateEnum.ShangMenJuTui.getValue()) {
					return ExplinkFlowEnum.JuShou;
				}
				if (delivery_state == DeliveryStateEnum.BuFenTuiHuo.getValue()) {
					return ExplinkFlowEnum.BuFenTuiHuo;
				}
				if (delivery_state == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
					return null;
				}
				if (delivery_state == DeliveryStateEnum.HuoWuDiuShi.getValue()) {
					return ExplinkFlowEnum.HuoWuDiuShi;
				}
			}

			if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue()) {
				long delivery_state = deliveryState.getDeliveryState().getDeliverystate();
				if (delivery_state == DeliveryStateEnum.PeiSongChengGong.getValue()) {
					return ExplinkFlowEnum.PeiSongChengGong_v;
				}
				if (delivery_state == DeliveryStateEnum.ShangMenTuiChengGong.getValue()) {
					return ExplinkFlowEnum.ShangMenTuiChengGong_v;
				}
				if (delivery_state == DeliveryStateEnum.ShangMenHuanChengGong.getValue()) {
					return ExplinkFlowEnum.ShangMenHuanChengGong_v;
				}
				if (delivery_state == DeliveryStateEnum.JuShou.getValue() || delivery_state == DeliveryStateEnum.ShangMenJuTui.getValue()) {
					return ExplinkFlowEnum.JuShou_v;
				}
				if (delivery_state == DeliveryStateEnum.BuFenTuiHuo.getValue()) {
					return ExplinkFlowEnum.BuFenTuiHuo_v;
				}
				if (delivery_state == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
					return ExplinkFlowEnum.FenZhanZhiLiu_v;
				}
				if (delivery_state == DeliveryStateEnum.HuoWuDiuShi.getValue()) {
					return ExplinkFlowEnum.HuoWuDiuShi_v;
				}
			}

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

	/**
	 * 获取订单的跟踪详情
	 * 
	 * @param orderFlowAll
	 * @return
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
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) {
				// return
				// MessageFormat.format("货物已由[{0}]的[{1}]审核;联系电话：[{2}];备注：[{3}]",currentbranchname,
				// userDAO.getUserByUserid(orderFlowAll.getUserid()).getRealname(),
				// phone,
				// comment);
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
			logger.error("", e);
			return null;
		}
		return null;
	}

	private void validatorBusinessMethod(String cwbs, String companyname, String sign, Explink explink) {
		if (explink == null) {
			throw new RuntimeException("配置信息有误");
		}
		if (companyname == null) {
			throw new RuntimeException("缺少参数companyname");
		}
		if (cwbs == null) {
			throw new RuntimeException("缺少参数cwb");
		}
		if (sign == null) {
			throw new RuntimeException("缺少参数sign");
		}
		if (cwbs.split(",").length > explink.getCount()) {
			throw new RuntimeException("订单数不能超过" + explink.getCount() + "个");
		}
		String sigstr = MD5Util.md5(explink.getCompanyname() + explink.getKey());
		if(explink.getNewSignMethod()==1){
			sigstr=MD5Util.md5(cwbs+explink.getCompanyname() + explink.getKey());
		}
		
		if (!sign.equals(sigstr)) {
			logger.info("签名验证失败,本地签名=" + sigstr);
			throw new RuntimeException("签名验证失败");
		}

	}

	private String getFlowOrderTextById(int flowordertype) {
		for (FlowOrderTypeEnum e : FlowOrderTypeEnum.values()) {
			if (e.getValue() == flowordertype) {
				return e.getText();
			}
			if (flowordertype == 18 || flowordertype == 19 || flowordertype == 20 || flowordertype == 21 || flowordertype == 22 || flowordertype == 23 || flowordertype == 24 || flowordertype == 25) {
				return "反馈";
			}
		}
		return "";

	}

	public String respExptMsg(String msg) {
		return "<Result><msg>" + msg + "</msg></Result>";
	}

	/**
	 * 根据订单状态查询原始数据结果的数据 查询1月10号的数据
	 * 
	 * @param user
	 * @return
	 */
	public String getFloworderTrackInfo(OrderFlow orderFlowAll) {
		StringBuffer returnStrs = new StringBuffer();
		long flowordertype = orderFlowAll.getFlowordertype();
		Branch branch = branchDAO.getBranchByBranchid(orderFlowAll.getBranchid());
		if (flowordertype == FlowOrderTypeEnum.RuKu.getValue()) {
			returnStrs.append("从" + branch.getBranchname() + FlowOrderTypeEnum.RuKu.getText());
		} else if (flowordertype == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) {
			returnStrs.append("货物从[" + branch.getBranchname() + "]出库");
		} else if (flowordertype == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()) {
			returnStrs.append("货物已送达[" + branch.getBranchname() + "]");
		} else if (flowordertype == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
			returnStrs.append("货物由[" + branch.getBranchname() + "]的派送员开始派送");
		} else if (flowordertype == 18 || flowordertype == 19 || flowordertype == 20 || flowordertype == 21 || flowordertype == 22 || flowordertype == 23 || flowordertype == 24 || flowordertype == 25) {
			DeliveryState delivery = deliveryStateDAO.getDeliveryByCwb(orderFlowAll.getCwb());
			returnStrs.append("在" + branch.getBranchname() + "反馈:" + getOrderDeliverStateByStatus(delivery.getDeliverystate()));
		}
		return returnStrs.toString();
	}

	public String getOrderDeliverStateByStatus(long deliverystate) {
		for (DeliveryStateEnum en : DeliveryStateEnum.values()) {
			if (deliverystate == en.getValue()) {
				return en.getText();
			}
		}
		return "";
	}

}
