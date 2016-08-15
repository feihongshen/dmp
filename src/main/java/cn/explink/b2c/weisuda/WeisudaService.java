package cn.explink.b2c.weisuda;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.explink.core_up.CommonCoreService;
import cn.explink.b2c.explink.xmldto.OrderFlowDto;
import cn.explink.b2c.tools.ExptCodeJoint;
import cn.explink.b2c.tools.ExptCodeJointDAO;
import cn.explink.b2c.tools.ExptReasonDAO;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.tools.poscodeMapp.PoscodeMappDAO;
import cn.explink.b2c.weisuda.xml.Good;
import cn.explink.b2c.weisuda.xml.Goods;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryCashDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.ExceptionCwbDAO;
import cn.explink.dao.OrderGoodsDAO;
import cn.explink.dao.ReasonDao;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.OrderGoods;
import cn.explink.domain.SystemInstall;
import cn.explink.domain.User;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.ExceptionCwbErrorTypeEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.exception.CwbException;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.pos.tools.PosEnum;
import cn.explink.pos.tools.PosPayDAO;
import cn.explink.pos.tools.PosPayService;
import cn.explink.pos.tools.SignTypeEnum;
import cn.explink.service.CwbOrderService;
import cn.explink.service.OrderPartGoodsReturnService;
import cn.explink.util.StringUtil;

@Service
public class WeisudaService {
	// private Logger logger = LoggerFactory.getLogger(WeisudaService.class);
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	PosPayDAO posPayDAO;
	@Autowired
	PosPayService posPayService;
	@Autowired
	CwbOrderService cwbOrderService;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	ReasonDao reasonDao;
	@Autowired
	ExptReasonDAO exptReasonDAO;
	@Autowired
	ExptCodeJointDAO exptcodeJointDAO;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	ExceptionCwbDAO exceptionCwbDAO;
	@Autowired
	DeliveryCashDAO deliveryCashDAO;

	@Autowired
	PoscodeMappDAO poscodeMappDAO;
	@Autowired
	SystemInstallDAO systemInstallDAO;
	@Autowired
	ReasonDao reasonDAO;
	@Autowired
	OrderGoodsDAO orderGoodsDAO;
	@Autowired
	OrderPartGoodsReturnService orderPartGoodsReturnService;
	@Autowired
	CwbOrderService cwborderService;

	private Logger logger = LoggerFactory.getLogger(CommonCoreService.class);

	protected ObjectMapper jacksonmapper = JacksonMapper.getInstance();

	private String getObjectMethod(int key) {
		JointEntity obj = null;
		String posValue = "";
		try {
			obj = this.jiontDAO.getJointEntity(key);
			posValue = obj.getJoint_property();
		} catch (Exception e) {
		}
		return posValue;
	}

	public Weisuda getWeisudaSettingMethod(int key) {
		Weisuda weisuda = new Weisuda();
		if (!"".equals(this.getObjectMethod(key))) {
			JSONObject jsonObj = JSONObject.fromObject(this.getObjectMethod(key));
			weisuda = (Weisuda) JSONObject.toBean(jsonObj, Weisuda.class);
		} else {
			weisuda = new Weisuda();
		}

		return weisuda == null ? new Weisuda() : weisuda;
	}

	protected long getUserIdByUserName(String deliver_man) {
		long deliverid = 0;
		try {
			List<User> userlist = this.userDAO.getUsersByUsername(deliver_man);
			if ((userlist != null) && (userlist.size() > 0)) {
				deliverid = userlist.get(0).getUserid();
			}
		} catch (Exception e) {

		}
		return deliverid;
	}

	public void edit(HttpServletRequest request, int joint_num) {
		Weisuda weisuda = new Weisuda();
		String code = StringUtil.nullConvertToEmptyString(request.getParameter("code"));
		String v = StringUtil.nullConvertToEmptyString(request.getParameter("v"));
		String secret = StringUtil.nullConvertToEmptyString(request.getParameter("secret"));
		String pushOrders_URL = StringUtil.nullConvertToEmptyString(request.getParameter("pushOrders_URL"));
		String unVerifyOrders_URL = StringUtil.nullConvertToEmptyString(request.getParameter("UnVerifyOrders_URL"));
		String updateUnVerifyOrders_URL = StringUtil.nullConvertToEmptyString(request.getParameter("updateUnVerifyOrders_URL"));
		String nums = StringUtil.nullConvertToEmptyString(request.getParameter("nums"));
		String count = StringUtil.nullConvertToEmptyString(request.getParameter("count"));
		String updateOrders_URL = StringUtil.nullConvertToEmptyString(request.getParameter("updateOrders_URL"));
		String siteUpdate_URL = StringUtil.nullConvertToEmptyString(request.getParameter("siteUpdate_URL"));
		String siteDel_URL = StringUtil.nullConvertToEmptyString(request.getParameter("siteDel_URL"));
		String courierUpdate_URL = StringUtil.nullConvertToEmptyString(request.getParameter("courierUpdate_URL"));
		String carrierDel_URL = StringUtil.nullConvertToEmptyString(request.getParameter("carrierDel_URL"));
		String unboundOrders_URL = StringUtil.nullConvertToEmptyString(request.getParameter("unboundOrders_URL"));
		String getback_boundOrders_URL = StringUtil.nullConvertToEmptyString(request.getParameter("getback_boundOrders_URL"));
		String getback_confirmAppOrders_URL = StringUtil.nullConvertToEmptyString(request.getParameter("getback_confirmAppOrders_URL"));
		String getback_getAppOrders_URL = StringUtil.nullConvertToEmptyString(request.getParameter("getback_getAppOrders_URL"));
		String getback_updateOrders_URL = StringUtil.nullConvertToEmptyString(request.getParameter("getback_updateOrders_URL"));
		String customers = StringUtil.nullConvertToEmptyString(request.getParameter("customers"));
		String openbatchflag = request.getParameter("openbatchflag");
		String maxBoundCount = request.getParameter("maxBoundCount");
		String changeBranchcode = request.getParameter("changeBranchcode");
		String isSend = StringUtil.nullConvertToEmptyString(request.getParameter("isSend"));
		if(isSend==null||isSend.length()<1){
			isSend="0";
		}
		
		weisuda.setCode(code);
		weisuda.setV(v);
		weisuda.setSecret(secret);
		weisuda.setPushOrders_URL(pushOrders_URL);
		weisuda.setUnVerifyOrders_URL(unVerifyOrders_URL);
		weisuda.setUpdateUnVerifyOrders_URL(updateUnVerifyOrders_URL);
		weisuda.setUpdateOrders_URL(updateOrders_URL);
		weisuda.setSiteUpdate_URL(siteUpdate_URL);
		weisuda.setSiteDel_URL(siteDel_URL);
		weisuda.setCourierUpdate_URL(courierUpdate_URL);
		weisuda.setCarrierDel_URL(carrierDel_URL);
		weisuda.setUnboundOrders_URL(unboundOrders_URL);
		weisuda.setGetback_boundOrders_URL(getback_boundOrders_URL);
		weisuda.setGetback_confirmAppOrders_URL(getback_confirmAppOrders_URL);
		weisuda.setGetback_getAppOrders_URL(getback_getAppOrders_URL);
		weisuda.setGetback_updateOrders_URL(getback_updateOrders_URL);
		weisuda.setNums(nums);
		weisuda.setCount(count);
		weisuda.setOpenbatchflag(Integer.valueOf(openbatchflag));
		weisuda.setMaxBoundCount(Integer.valueOf(maxBoundCount));
		weisuda.setCustomers(customers);
		weisuda.setChangeBranchcode(Integer.valueOf(changeBranchcode));
		weisuda.setIsSend(Integer.valueOf(isSend));
		JSONObject jsonObj = JSONObject.fromObject(weisuda);
		JointEntity jointEntity = this.jiontDAO.getJointEntity(joint_num);
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			this.jiontDAO.Create(jointEntity);
		} else {
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			this.jiontDAO.Update(jointEntity);
		}

	}

	protected User getUser(long userid) {
		return this.userDAO.getUserByUserid(userid);
	}

	public void update(int joint_num, int state) {
		this.jiontDAO.UpdateState(joint_num, state);
	}

	/**
	 * 对接反馈接口
	 *
	 * @param commd
	 * @param cwb
	 * @param podresultid
	 * @param user
	 * @throws IOException
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 */
	public void dealwith_fankui(String datajson) throws IOException, JsonParseException, JsonMappingException {

		OrderFlowDto orderFlowDto = JacksonMapper.getInstance().readValue(datajson, OrderFlowDto.class);

		CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(orderFlowDto.getCwb());

		BigDecimal pos = BigDecimal.ZERO; //POS
		BigDecimal check = BigDecimal.ZERO; //支票
		BigDecimal cash = BigDecimal.ZERO; //现金
		BigDecimal other = BigDecimal.ZERO; //其他
		BigDecimal paybackedfee = BigDecimal.ZERO; //上门退货应退金额
		BigDecimal codpos = BigDecimal.ZERO; //COD扫码支付

		//获取配送状态
		long podresultid = Long.valueOf(orderFlowDto.getDeliverystate());
		// podresultid = this.getPodresultid(podresultid, cwbOrder);
		
		//获取配送状态对象
		DeliveryState deliverystate = this.deliveryStateDAO.getActiveDeliveryStateByCwb(orderFlowDto.getCwb());

		//配送状态描述
		String deliverstateremark = "系统对接" + "(" + orderFlowDto.getPayremark() + ")";

		//订单{}不满足支付条件，deliverystate表为空
		if (deliverystate == null) {
			this.logger.info("订单{}不满足支付条件，deliverystate表为空", deliverystate);
			return;
		}

		// 撤销
		if (orderFlowDto.getIsCancel() == 1) {
			// //////////////////////////执行撤销方法
			this.cwbOrderService.deliverStatePodCancel(orderFlowDto.getCwb(), deliverystate.getDeliverybranchid(), deliverystate.getDeliveryid(), "运单撤销", 0);
			return;
		}
		/**
		 * 如果系统订单当前的反馈结果是以下几种的（配送成功，拒收，上门退成功，上门拒退），不再处理品骏达反馈回来的信息。
		 * added by zhouguoting 2016/3/16 解决反馈状态重复问题
		 */
		if(deliverystate.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue() || 
				deliverystate.getDeliverystate() == DeliveryStateEnum.JuShou.getValue() || 
				deliverystate.getDeliverystate() == DeliveryStateEnum.ShangMenTuiChengGong.getValue() ||
				deliverystate.getDeliverystate() == DeliveryStateEnum.ShangMenJuTui.getValue()){
			this.logger.info("订单已是最终状态，不接收外部系统任何反馈信息，对接结果={},对接报文={}" , DeliveryStateEnum.getByValue((int)podresultid).getText(), datajson);
			return;
		}
		
		String remark5 = "";
		
		//配送状态：上门退成功 or 订单类型：上门退 ==>应退款
		if ((podresultid == DeliveryStateEnum.ShangMenTuiChengGong.getValue()) || (cwbOrder.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue())) { // 应退款
			pos = BigDecimal.ZERO;
			cash = BigDecimal.ZERO;
			check = BigDecimal.ZERO;
			other = BigDecimal.ZERO;
			codpos = BigDecimal.ZERO;
			
			//配送状态：上门换成功 or 配送状态：上门退成功 ==>设置应退款
			if ((podresultid == DeliveryStateEnum.ShangMenHuanChengGong.getValue()) || (podresultid == DeliveryStateEnum.ShangMenTuiChengGong.getValue())) {
				paybackedfee = deliverystate.getBusinessfee();
				remark5 = cwbOrder.getRemark5();
				if (remark5 != null) {
					if (remark5.trim().length() > 0) {
						remark5 += ",";
					}
				} else {
					remark5 = "";
				}
				remark5 += orderFlowDto.getPayremark();

			}

		//配送状态：配送成功 or 配送状态：上门换成功 ==>应收款
		} else if ((podresultid == DeliveryStateEnum.PeiSongChengGong.getValue()) || (podresultid == DeliveryStateEnum.ShangMenHuanChengGong.getValue())) { // 应收款
			SystemInstall isToCash = this.systemInstallDAO.getSystemInstallByName("isToCash");
			
			//转换到现金
			if ((isToCash != null) && isToCash.getValue().equals("yes")) {
				cash = deliverystate.getBusinessfee();
				
			} else { 

				// deliverstateremark+="-pos签收";
				if (orderFlowDto.getPaytype() == PaytypeEnum.Pos.getValue()) {
					pos = deliverystate.getBusinessfee();
				
				// deliverstateremark+="-现金签收";
				} else if (orderFlowDto.getPaytype() == PaytypeEnum.Xianjin.getValue()) {
					cash = deliverystate.getBusinessfee();
				
				// deliverstateremark+="-支票签收";
				} else if (orderFlowDto.getPaytype() == PaytypeEnum.Zhipiao.getValue()) {
					check = deliverystate.getBusinessfee();
				
				// deliverstateremark+="-其他方式签收";
				} else if (orderFlowDto.getPaytype() == PaytypeEnum.Qita.getValue()) {
					other = deliverystate.getBusinessfee();
				
				// deliverstateremark+="-支票签收";
				} else if (orderFlowDto.getPaytype() == PaytypeEnum.CodPos.getValue()) {
					codpos = deliverystate.getBusinessfee();
					
				} else {
					
					//原支付方式 
					//现金
					if (cwbOrder.getPaywayid() == PaytypeEnum.Xianjin.getValue()) {
						cash = deliverystate.getBusinessfee();
					
					//pos
					} else if (cwbOrder.getPaywayid() == PaytypeEnum.Pos.getValue()) {
						pos = deliverystate.getBusinessfee();
					
					//支票
					} else if (cwbOrder.getPaywayid() == PaytypeEnum.Zhipiao.getValue()) {
						check = deliverystate.getBusinessfee();
					
					//其他
					} else if (cwbOrder.getPaywayid() == PaytypeEnum.Qita.getValue()) {
						other = deliverystate.getBusinessfee();
					
					//COD扫码支付
					} else if (cwbOrder.getPaywayid() == PaytypeEnum.CodPos.getValue()) {
						codpos = deliverystate.getBusinessfee();
					}
				}
			}
		}

		long backedreasonid = 0; //退货原因id
		long leavedreasonid = 0; //滞留原因id
		long firstlevelreasonid = 0; //一级原因id

		//配送状态：拒收 or 配送状态：上门据退 or 配送状态：部分退货 ==>设置退货原因
		if ((podresultid == DeliveryStateEnum.JuShou.getValue()) || (podresultid == DeliveryStateEnum.ShangMenJuTui.getValue()) || (podresultid == DeliveryStateEnum.BuFenTuiHuo.getValue())) {
			deliverstateremark += "-" + orderFlowDto.getExptmsg();
			if ((orderFlowDto.getExptmsg() != null) && !orderFlowDto.getExptmsg().isEmpty()) {
				ExptCodeJoint exptCodeJoint = this.exptcodeJointDAO.getExpMatchListByPosCode(orderFlowDto.getExptmsg(), PosEnum.Weisuda.getKey());

				if ((exptCodeJoint != null) && (exptCodeJoint.getReasonid() != 0)) {
					backedreasonid = exptCodeJoint.getReasonid();
				}

			}
		}
		
		//配送状态：分站滞留 ==>设置滞留原因、一级原因
		if (podresultid == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {

			deliverstateremark += "-" + orderFlowDto.getStrandedrReason();
			if ((orderFlowDto.getStrandedrReason() != null) && !orderFlowDto.getStrandedrReason().isEmpty()) {
				try {
					ExptCodeJoint exptCodeJoint = this.exptcodeJointDAO.getExpMatchListByPosCode(orderFlowDto.getStrandedrReason(), PosEnum.Weisuda.getKey());

					if ((exptCodeJoint != null) && (exptCodeJoint.getReasonid() != 0)) {
						leavedreasonid = exptCodeJoint.getReasonid();
						firstlevelreasonid = this.reasonDao.getReasonByReasonid(leavedreasonid).getParentid();
					}
				} catch (Exception e) {
				}
			}
		}

		//小件员userid
		long deliverid = deliverystate.getDeliveryid();
		long infactDeliverid = 0; //实际小件员userid
		try {
			//通过姓名获取user
			User user = this.userDAO.getUserByUsernameToUpper(orderFlowDto.getDeliveryname().toUpperCase());
			if (user == null) {
				user = this.userDAO.getUserByUsername(orderFlowDto.getDeliveryname());
			}
			infactDeliverid = user.getUserid();
			//如果实际小件员userid 与 小件员userid不一致，更新配送状态为实际小件员userid
			if ((infactDeliverid != deliverid) && (infactDeliverid != 0)) {
				deliverid = infactDeliverid;
				this.deliveryStateDAO.updateDeliveryidByCwb(deliverid, user.getBranchid(), deliverystate.getCwb());
				//领货时变更了领货小件员要同时更改deliverycash表中的关于小件员的信息
				this.deliveryCashDAO.saveDeliveryCashByDeliverystateid(deliverid, user.getBranchid(), orderFlowDto.getRequestTime(), deliverystate.getId());
				deliverystate.setDeliverybranchid(user.getBranchid());
				deliverystate.setDeliveryid(deliverid);
			}
		} catch (Exception e1) {
			this.logger.error("唯速达_回传username=" + orderFlowDto.getDeliveryname() + "不存在，cwb=" + cwbOrder.getCwb());
			infactDeliverid = 0;
		}

		String posremark = pos.compareTo(BigDecimal.ZERO) > 0 ? "POS刷卡" : "";
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("deliverid", deliverid);
		parameters.put("podresultid", podresultid);
		parameters.put("backreasonid", backedreasonid);
		parameters.put("leavedreasonid", leavedreasonid);
		parameters.put("firstlevelreasonid", firstlevelreasonid);

		parameters.put("receivedfeecash", cash);
		parameters.put("receivedfeepos", pos);
		parameters.put("receivedfeecheque", check);
		parameters.put("receivedfeeother", other);
		// --lx--支付宝cod--
		parameters.put("receivedfeecodpos", codpos);

		parameters.put("paybackedfee", paybackedfee);
		parameters.put("podremarkid", (long) 0);
		parameters.put("posremark", posremark);
		parameters.put("checkremark", check.compareTo(BigDecimal.ZERO) > 0 ? "支票支付" : "");
		parameters.put("deliverstateremark", deliverstateremark);
		parameters.put("owgid", 0);
		parameters.put("sessionbranchid", deliverystate.getDeliverybranchid());
		parameters.put("sessionuserid", deliverystate.getDeliveryid());
		parameters.put("sign_typeid", 1);
		parameters.put("sign_man", orderFlowDto.getConsignee());
		parameters.put("sign_time", orderFlowDto.getRequestTime());

		String oldcwbremark = cwbOrder.getCwbremark().length() > 0 ? cwbOrder.getCwbremark() + "\n" : "";
		String newcwbremark = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + orderFlowDto.getCwbremark();
		newcwbremark = this.subCwbRemark(oldcwbremark + newcwbremark);
		try {
			//修改客户备注信息
			this.cwbDAO.updateCwbRemark(orderFlowDto.getCwb(), newcwbremark);
			cwbOrder.setCwbremark(newcwbremark);

		} catch (Exception e) {
			this.logger.error("唯速达接口反馈异常,cwb:" + cwbOrder.getCwb() + "cwbremark:" + newcwbremark, e);
			throw new CwbException(cwbOrder.getCwb(), FlowOrderTypeEnum.YiFanKui.getValue(), ExceptionCwbErrorTypeEnum.Bei_Zhu_Tai_Chang);
		}
		parameters.put("nosysyemflag", "1");//

		//通过小件员userid获取user
		User user = this.userDAO.getAllUserByid(deliverid);

		//订单类型：配送
		if (cwbOrder.getCwbordertypeid() == CwbOrderTypeIdEnum.Peisong.getValue()) {
			String Sign_Remark; //签收remark
			int Sign_Self_Flag; //签收flag
			
			//orderFlow的收件人 与 订单中的收件人一致 ==>本人签收
			if (orderFlowDto.getConsignee().trim().equals(cwbOrder.getConsigneename().trim())) {
				Sign_Self_Flag = SignTypeEnum.BenRenQianShou.getValue();
				Sign_Remark = SignTypeEnum.BenRenQianShou.getSign_text();
				
			} else {//orderFlow的收件人 与 订单中的收件人不一致 ==>他人签收
				Sign_Self_Flag = SignTypeEnum.TaRenDaiQianShou.getValue();
				Sign_Remark = SignTypeEnum.TaRenDaiQianShou.getSign_text();
			}
			
			//支付方式：pos
			if (orderFlowDto.getPaytype() == PaytypeEnum.Pos.getValue()) {
				
				//保存pos交易记录
				boolean flag = this.posPayDAO.save_PosTradeDetailRecord(cwbOrder.getCwb(), posremark, Double.valueOf(cwbOrder.getReceivablefee() + ""), deliverid, orderFlowDto.getPaytype(), "",
						orderFlowDto.getConsignee(), Sign_Self_Flag, Sign_Remark, 4, 1, "", PosEnum.Weisuda.getMethod(), 0, "");
				this.logger.info("唯速达签收交易-记录posPayDetail表" + flag + "! 订单号:{}", cwbOrder.getCwb());
				//更新正向订单的deliverystate, added by neo01.huang
				//cwborderService.updateForwardOrderDeliveryState(cwbOrder.getCwbordertypeid(), cwbOrder.getCwb(), DeliveryStateEnum.PeiSongChengGong.getValue());
			}
		
		//订单类型：上门退 ==>更新自动化分拣项目商品信息表
		} else if (cwbOrder.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue()) {
			List<OrderGoods> orderGoods = this.orderGoodsDAO.getOrderGoodsList(orderFlowDto.getCwb());
			if (orderFlowDto.getReamrk1() != null) {
				Goods goods = JacksonMapper.getInstance().readValue(orderFlowDto.getReamrk1(), Goods.class);
				List<Good> goodlist = goods.getGood();
				if ((orderGoods != null) && (orderGoods.size() > 0) && (goodlist != null) && (goodlist.size() > 0)) {
					for (Good good : goodlist) {
						for (OrderGoods orderGood : orderGoods) {
							if (orderGood.getGoods_code().equals(good.getCode())) {
								orderGood.setShituicount(good.getFetch_num());
								// add by jian_xie 2016-08-01 揽退部分退的
								int weituicount = 0;
								if(Integer.parseInt(orderGood.getGoods_num()) - good.getFetch_num() >= 0){
									weituicount = Integer.parseInt(orderGood.getGoods_num()) - good.getFetch_num();
								} 
								orderGood.setWeituicount(weituicount);
								orderGood.setTepituicount(good.getSpecial_num());
								orderGood.setRemark1(good.getRemark());
							}
						}
					}
					this.orderPartGoodsReturnService.updateOrderGoods(orderGoods);
				}
			}
		}
		//单票结果反馈
		this.cwborderService.deliverStatePod(user, orderFlowDto.getCwb(), orderFlowDto.getCwb(), parameters);
		if(!"".equals(remark5)){
			//更新订单remark5
			this.cwbDAO.updateCwbRemarkPaytype(orderFlowDto.getCwb(), remark5);
		}
	}

	/*
	 * private long getPodresultid(long podresultid, CwbOrder cwbOrder) { if
	 * (podresultid == DeliveryStateEnum.PeiSongChengGong.getValue()) { if
	 * (cwbOrder.getCwbordertypeid() ==
	 * CwbOrderTypeIdEnum.Shangmenhuan.getValue()) { podresultid =
	 * DeliveryStateEnum.ShangMenHuanChengGong.getValue(); } else if
	 * (cwbOrder.getCwbordertypeid() ==
	 * CwbOrderTypeIdEnum.Shangmentui.getValue()) { podresultid =
	 * DeliveryStateEnum.ShangMenTuiChengGong.getValue(); }
	 *
	 * } else if (podresultid == DeliveryStateEnum.JuShou.getValue()) { if
	 * (cwbOrder.getCwbordertypeid() ==
	 * CwbOrderTypeIdEnum.Shangmenhuan.getValue()) { podresultid =
	 * DeliveryStateEnum.JuShou.getValue(); } else if
	 * (cwbOrder.getCwbordertypeid() ==
	 * CwbOrderTypeIdEnum.Shangmentui.getValue()) { podresultid =
	 * DeliveryStateEnum.ShangMenJuTui.getValue(); } } return podresultid; }
	 */

	public String subCwbRemark(String newcwbremark) {
		while (newcwbremark.length() > 500) {
			if (newcwbremark.contains("\n")) {
				newcwbremark = newcwbremark.substring(newcwbremark.indexOf("\n") + 1);
			} else {
				newcwbremark = newcwbremark.substring(0, 500);
			}
		}
		return newcwbremark;
	}

}