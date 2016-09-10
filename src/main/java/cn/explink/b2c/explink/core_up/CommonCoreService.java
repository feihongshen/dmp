package cn.explink.b2c.explink.core_up;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.explink.xmldto.OrderFlowDto;
import cn.explink.controller.DeliveryStateDTO;
import cn.explink.controller.DeliveryStateView;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.BranchRouteDAO;
import cn.explink.dao.CommonDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.ReasonDao;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.UserDAO;
import cn.explink.dao.WarehouseToCommenDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.BranchRoute;
import cn.explink.domain.Common;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.GotoClassOld;
import cn.explink.domain.Reason;
import cn.explink.domain.SystemInstall;
import cn.explink.domain.User;
import cn.explink.domain.WarehouseToCommen;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.DeliverPayuptypeEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.service.CwbOrderService;
import cn.explink.support.transcwb.TransCwbDao;
import cn.explink.support.transcwb.TranscwbView;
import cn.explink.util.B2cUtil;
import cn.explink.util.DateTimeUtil;

/**
 * 系统之间的对接（上游） 处理接收oms的部分，调用对应的service的辅助类
 * 
 * @author Administrator
 *
 */
@Service
public class CommonCoreService {
	private Logger logger = LoggerFactory.getLogger(CommonCoreService.class);

	@Autowired
	CustomerDAO customerDAO;

	@Autowired
	WarehouseToCommenDAO warehouseToCommenDAO;

	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	CwbOrderService cwborderService;
	@Autowired
	CommonDAO commonDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	TransCwbDao transCwbDao;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	SystemInstallDAO systemInstallDAO;
	@Autowired
	ReasonDao reasonDAO;

	@Autowired
	BranchRouteDAO branchRouteDAO;
	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	B2cUtil bcUtil;
	/**
	 * dmp 处理接收订单状态回传
	 * 
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public void orderFlowFeedback(CommenSendData commd) throws Exception {
		String cwb = commd.getCwb();
		long podresultid = commd.getDeliverystate();
		Common common = commonDAO.getCommonByCommonnumber(commd.getCommencode());
		if (common == null) {
			logger.info("指定common={}不存在,cwb={}", commd.getCommencode(), cwb);
			return;
		}
		WarehouseToCommen wareCommen = warehouseToCommenDAO.getCommenByCwb(cwb);

		Branch branch = branchDAO.getBranchByBranchid(wareCommen == null ? 1 : wareCommen.getNextbranchid());

		User user = null;
		if (branch.getBindmsksid() > 0) { // 不指定站点和操作人
			user = userDAO.getUserByBranchName(branch.getBranchid() == 0 ? 1 : branch.getBranchid());
		} else { // 承运商设置指定站点 和操作人
			user = userDAO.getUserByUserid(common.getUserid());
		}

		CwbOrder cwbOrder = cwbDAO.getCwbByCwb(cwb);

		long flowordertype = commd.getFlowordertype();

		if (flowordertype == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()) { // 对接分站到货

			dealWith_fenzhandaohuo(cwb, user, cwbOrder, flowordertype);

		} else if (flowordertype == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) { // 对接分站领货

			dealwith_fenzhanlinghuo(cwb, user, cwbOrder, flowordertype, commd);

		} else if (flowordertype == FlowOrderTypeEnum.YiFanKui.getValue()) { // 对接反馈

			dealwith_fankui(commd, cwb, podresultid, user, cwbOrder);

		} else if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue()) { // 对接审核
			dealwith_goclassConfirm(commd, cwb, common, user);
		}

		else if (flowordertype == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) { // 对接退货出站
																				// 迈思可-出库扫描流程

			dealWith_ChuKuSaoMiao(cwb, user, cwbOrder, flowordertype);
		}

		else if (flowordertype == FlowOrderTypeEnum.TuiHuoChuZhan.getValue()) { // 对接退货出站
																				// 系统环形对接-退货出站流程

			dealWith_TuiHuoChuZhan(cwb, user, cwbOrder, flowordertype);
		}

	}

	/**
	 * 处理分站领货
	 * 
	 * @param cwb
	 * @param user
	 * @param cwbOrder
	 * @param flowordertype
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	private void dealwith_fenzhanlinghuo(String cwb, User user, CwbOrder cwbOrder, long flowordertype, CommenSendData commd) throws JsonParseException, JsonMappingException, IOException {

		OrderFlowDto orderFlowDto = JacksonMapper.getInstance().readValue(commd.getDatajson(), OrderFlowDto.class);

		Customer customer = customerDAO.getCustomerById(cwbOrder.getCustomerid());
		if (customer.getIsypdjusetranscwb() == 1) { // 使用一票多件模式
		/*	List<TranscwbView> translist = transCwbDao.getTransCwbByCwb(cwb);
			if (translist == null || translist.size() == 0) {
				logger.info("一票多件查询transcwb表异常,cwb={},flowordertype={}", cwb, flowordertype);
				return;
			}*/
		/*	for (TranscwbView trans : translist) {
				cwborderService.receiveGoods(user, user, trans.getTranscwb(), trans.getTranscwb());

			}*/
			String transcwbs=cwbOrder.getTranscwb();
			if("".equals(transcwbs)){
				transcwbs=cwbOrder.getCwb();
			}
			String splitString = cwborderService.getSplitstring(transcwbs);
			String[] split = transcwbs.split(splitString);
			for (String transcwb : split) {
				cwborderService.receiveGoods(user, user, transcwb, transcwb);
			}

		} else {
			try {
				// 通用模板模板 ，需反馈真实派件人电话和名称推送至唯品会 ,暂时用作如风达
				if ((orderFlowDto.getDeliveryname() != null) && !orderFlowDto.getDeliveryname().isEmpty() && (orderFlowDto.getDeliverymobile() != null) && !orderFlowDto.getDeliverymobile().isEmpty()) {
					String remark5 = "信息：" + orderFlowDto.getDeliveryname() + "_" + orderFlowDto.getDeliverymobile();
					this.cwbDAO.updateCwbRemark5(cwb, remark5);

				}
			
			} catch (Exception e) {
				this.logger.error("处理备注5异常cwb=" + cwb, e);
			}
			cwborderService.receiveGoods(user, user, cwb, cwb);
			// 非一票多件模式更新此内容，目前主要是讯祥和顺丰需求
			String comment = orderFlowDto.getFloworderdetail();
			String remark1 = "";
			String remark2 = "";
			try {
				// 模板:正在派件..(派件人:李鹏飞,电话:13811579952)
				if (comment != null && !comment.isEmpty() && comment.contains("正在派件") && comment.contains("派件人")) {
					orderFlowDAO.updateCommentByCwb(cwb, orderFlowDto.getFloworderdetail());

					remark1 = comment.substring(comment.indexOf("派件人:") + 4, comment.indexOf(","));
					;
					// 派件员电话
					remark2 = comment.substring(comment.indexOf("电话:") + 3, comment.indexOf(")"));

					cwbDAO.updateCwbRemark1AndRemark2(cwb, remark1, remark2);
				}
			} catch (Exception e) {
				logger.error("处理顺丰追加备注未知异常,派件员不详细cwb=" + cwb, e);

			}
		}
	}

	/**
	 * 处理出库扫描
	 * 
	 * @param cwb
	 * @param user
	 * @param cwbOrder
	 * @param flowordertype
	 */
	private void dealWith_ChuKuSaoMiao(String cwb, User user, CwbOrder cwbOrder, long flowordertype) {
		Customer customer = customerDAO.getCustomerById(cwbOrder.getCustomerid());

		long startbranchid = cwbOrder.getStartbranchid();
		long nextbranchid = 0; // 这里只一级站
		Branch branch = branchDAO.getBranchByBranchid(startbranchid);
		if (branch.getContractflag().contains("2")) { // 当前站点是二级站
			List<BranchRoute> branchRouteList = branchRouteDAO.getBranchRouteByWheresql(0, startbranchid, 2);
			nextbranchid = branchRouteList.get(0).getFromBranchId();
		}

		if (customer.getIsypdjusetranscwb() == 1) {// 使用一票多件模式
			List<TranscwbView> translist = transCwbDao.getTransCwbByCwb(cwb);
			if (translist == null || translist.size() == 0) {
				logger.info("一票多件查询transcwb表异常,cwb={},flowordertype={}", cwb, flowordertype);
				return;
			}
			for (TranscwbView trans : translist) {
				cwborderService.outWarehous(user, trans.getTranscwb(), trans.getTranscwb(), 0, 0, nextbranchid, 0, true, "系统对接", "", 0, false, false);
			}

		} else {

			cwborderService.outWarehous(user, cwb, cwb, 0, 0, nextbranchid, 0, true, "系统对接", "", 0, false, false);
		}
	}

	/**
	 * 处理分站到货
	 * 
	 * @param cwb
	 * @param user
	 * @param cwbOrder
	 * @param flowordertype
	 */
	private void dealWith_fenzhandaohuo(String cwb, User user, CwbOrder cwbOrder, long flowordertype) {
		Customer customer = customerDAO.getCustomerById(cwbOrder.getCustomerid());
		if (customer.getIsypdjusetranscwb() == 1) {// 使用一票多件模式
	/*		List<TranscwbView> translist = transCwbDao.getTransCwbByCwb(cwb);
			if (translist == null || translist.size() == 0) {
				logger.info("一票多件查询transcwb表异常,cwb={},flowordertype={}", cwb, flowordertype);
				return;
			}*/
//			for (TranscwbView trans : translist) {
//				cwborderService.substationGoods(user, trans.getTranscwb(), trans.getTranscwb(), user.getUserid(), 0, "系统对接", "", false);
//			}
			String transcwbs=cwbOrder.getTranscwb();
			if("".equals(transcwbs)){
				transcwbs=cwbOrder.getCwb();
			}
			String splitString = cwborderService.getSplitstring(transcwbs);
			String[] split = transcwbs.split(splitString);
			for (String transcwb : split) {
				cwborderService.substationGoods(user, transcwb, transcwb, user.getUserid(), 0, "系统对接", "", false);
			}

		} else {
			cwborderService.substationGoods(user, cwb, cwb, user.getUserid(), 0, "系统对接", "", false);
		}
	}

	/**
	 * 系统对接-> 归班审核
	 * 
	 * @param commd
	 * @param cwb
	 * @param common
	 * @param user
	 * @throws IOException
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 */
	private void dealwith_goclassConfirm(CommenSendData commd, String cwb, Common common, User user) throws IOException, JsonParseException, JsonMappingException {

		OrderFlowDto orderFlowDto = JacksonMapper.getInstance().readValue(commd.getDatajson(), OrderFlowDto.class);
		DeliveryState deliveryState = deliveryStateDAO.getActiveDeliveryStateByCwb(cwb);

		int deliverpayuptype = 0;
		if (deliveryState.getPos().compareTo(BigDecimal.ZERO) > 0) {
			deliverpayuptype = DeliverPayuptypeEnum.Pos.getValue();
		} else if (deliveryState.getCash().compareTo(BigDecimal.ZERO) > 0) {
			deliverpayuptype = DeliverPayuptypeEnum.XianJin.getValue();
		}

		BigDecimal deliverpayupamount = deliveryState.getReceivedfee(); // 小件员交款金额
		String deliverpayupbanknum = ""; // 小件员交款小票号 暂时为空
		String payupaddress = common.getCommonname(); // 小件员交款地址
		BigDecimal deliverpayupamount_pos = deliveryState.getPos();
		BigDecimal deliverAccount = BigDecimal.ZERO; // 交款时小件员的余额
		BigDecimal deliverPosAccount = BigDecimal.ZERO; // 交款时小件员的POS余额

		String subAmount = "0"; // 非pos
		String subAmountPos = "0"; // POS

		if (deliveryState.getIsout() == 1) {
			if (deliveryState.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue() || deliveryState.getDeliverystate() == DeliveryStateEnum.ShangMenHuanChengGong.getValue()
					|| deliveryState.getDeliverystate() == DeliveryStateEnum.ShangMenTuiChengGong.getValue()) {

				subAmount = String.valueOf(BigDecimal.ZERO.subtract(deliveryState.getBusinessfee())); // 非pos
				subAmountPos = String.valueOf(deliveryState.getPos()); // POS
			}
		}

		if (deliveryState.getIsout() == 0) {
			if (deliveryState.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue() || deliveryState.getDeliverystate() == DeliveryStateEnum.ShangMenHuanChengGong.getValue()
					|| deliveryState.getDeliverystate() == DeliveryStateEnum.ShangMenTuiChengGong.getValue()) {

				subAmount = String.valueOf(deliveryState.getCash().add(deliveryState.getCheckfee()).add(deliveryState.getOtherfee()).subtract(deliveryState.getReturnedfee())); // 非pos
				subAmountPos = String.valueOf(deliveryState.getPos()); // POS
			}
		}

		GotoClassOld gotoClassOld = imitateLoadFormForGotoClass(cwb);

		cwborderService.deliverAuditok(user, "'" + cwb + "'", orderFlowDto.getOperatortime(), subAmount, subAmountPos, user.getUserid(), gotoClassOld, deliverpayuptype, deliverpayupamount,
				deliverpayupbanknum, payupaddress, deliverpayupamount_pos, deliverAccount, deliverPosAccount);
	}

	/**
	 * 模拟归班从页面获取的参数
	 * 
	 * @param request
	 * @return
	 */
	private GotoClassOld imitateLoadFormForGotoClass(String cwb) {

		DeliveryStateDTO dsDTO = new DeliveryStateDTO();

		List<DeliveryState> dlist = deliveryStateDAO.getDeliveryStateByCwbs("'" + cwb + "'");

		if (dlist != null) {
			List<DeliveryStateView> deliveryStateViews = getDeliveryStateViews(dlist, "'" + cwb + "'");
			dsDTO.analysisDeliveryStateList(deliveryStateViews, bcUtil, customerDAO);
		}

		GotoClassOld gco = new GotoClassOld();
		gco.setNownumber(dsDTO.getNowNumber()); // 今日领货
		gco.setYiliu(dsDTO.getYiliu());
		gco.setLishi_weishenhe(dsDTO.getLishi_weishenhe());
		gco.setZanbuchuli(dsDTO.getZanbuchuli());
		gco.setPeisong_chenggong(dsDTO.getFankui_peisong_chenggong() - dsDTO.getFankui_peisong_chenggong_zanbuchuli());
		gco.setPeisong_chenggong_amount(BigDecimal.valueOf(dsDTO.getAmountNotZanbuchuli(dsDTO.getFankui_peisong_chenggongList())));
		gco.setPeisong_chenggong_pos_amount(BigDecimal.valueOf(dsDTO.getPosAmountNotZanbuchuli(dsDTO.getFankui_peisong_chenggongList())));
		gco.setTuihuo(dsDTO.getFankui_tuihuo() - dsDTO.getFankui_tuihuo_zanbuchuli());
		gco.setTuihuo_amount(BigDecimal.valueOf(dsDTO.getAmountNotZanbuchuli(dsDTO.getFankui_tuihuoList())));
		gco.setBufentuihuo(dsDTO.getFankui_bufentuihuo() - dsDTO.getFankui_bufentuihuo_zanbuchuli());
		gco.setBufentuihuo_amount(BigDecimal.valueOf(dsDTO.getAmountNotZanbuchuli(dsDTO.getFankui_bufentuihuoList())));
		gco.setBufentuihuo_pos_amount(BigDecimal.valueOf(dsDTO.getPosAmountNotZanbuchuli(dsDTO.getFankui_bufentuihuoList())));
		gco.setZhiliu(dsDTO.getFankui_zhiliu() - dsDTO.getFankui_zhiliu_zanbuchuli());
		gco.setZhiliu_amount(BigDecimal.valueOf(dsDTO.getAmountNotZanbuchuli(dsDTO.getFankui_zhiliuList())));
		gco.setShangmentui_chenggong(dsDTO.getFankui_shangmentui_chenggong() - dsDTO.getFankui_shangmentui_chenggong_zanbuchuli());
		gco.setShangmentui_chenggong_amount(BigDecimal.valueOf(dsDTO.getAmountNotZanbuchuli(dsDTO.getFankui_shangmentui_chenggongList())));
		gco.setShangmentui_chenggong_fare(BigDecimal.valueOf(dsDTO.getSmtcgFareAmountNotZanbuchuli(dsDTO.getFankui_shangmentui_chenggongList())));
		gco.setShangmentui_jutui(dsDTO.getFankui_shangmentui_jutui() - dsDTO.getFankui_shangmentui_jutui_zanbuchuli());
		gco.setShangmentui_jutui_amount(BigDecimal.valueOf(dsDTO.getAmountNotZanbuchuli(dsDTO.getFankui_shangmentui_jutuiList())));
		gco.setShangmentui_jutui_fare(BigDecimal.valueOf(dsDTO.getSmtjtFareAmountNotZanbuchuli(dsDTO.getFankui_shangmentui_jutuiList())));
		gco.setShangmenhuan_chenggong(dsDTO.getFankui_shangmenhuan_chenggong() - dsDTO.getFankui_shangmenhuan_chenggong_zanbuchuli());
		gco.setShangmenhuan_chenggong_amount(BigDecimal.valueOf(dsDTO.getAmountNotZanbuchuli(dsDTO.getFankui_shangmenhuan_chenggongList())));
		gco.setShangmenhuan_chenggong_pos_amount(BigDecimal.valueOf(dsDTO.getPosAmountNotZanbuchuli(dsDTO.getFankui_shangmenhuan_chenggongList())));
		gco.setDiushi(dsDTO.getFankui_diushi() - dsDTO.getFankui_diushi_zanbuchuli());
		gco.setDiushi_amount(BigDecimal.valueOf(dsDTO.getAmountNotZanbuchuli(dsDTO.getFankui_diushiList())));
		return gco;
	}

	/**
	 * 获得前端需要的界面视图变量
	 * 
	 * @param dsList
	 * @param cwbs
	 *            'ABD123','BCD321' 如果为null 会自动从dsList中获取订单号
	 * @return
	 */
	public List<DeliveryStateView> getDeliveryStateViews(List<DeliveryState> dsList, String cwbs) {
		List<DeliveryStateView> deliveryStateViewList = new ArrayList<DeliveryStateView>();
		List<Customer> customerList = customerDAO.getAllCustomers();
		List<User> userList = userDAO.getAllUser();

		if (dsList.size() > 0) {
			if (cwbs == null || cwbs.equals("")) {
				StringBuffer cwbBuffer = new StringBuffer();
				for (DeliveryState ds : dsList) {
					cwbBuffer = cwbBuffer.append("'").append(ds.getCwb()).append("',");
				}
				cwbs = cwbBuffer.substring(0, cwbBuffer.length() - 1);
			}

			List<CwbOrder> clist = cwbDAO.getCwbByCwbs(cwbs);

			for (DeliveryState ds : dsList) {
				DeliveryStateView sdv = getDeliveryStateView(ds, customerList, userList, clist);
				if (sdv != null) { // 数据不正确时会返回null
					deliveryStateViewList.add(sdv);
				}
			}

		}

		return deliveryStateViewList;
	}

	private DeliveryStateView getDeliveryStateView(DeliveryState ds, List<Customer> customerList, List<User> userList, List<CwbOrder> clist) {
		DeliveryStateView sdv = new DeliveryStateView();
		sdv.setId(ds.getId());
		sdv.setCwb(ds.getCwb());
		sdv.setDeliveryid(ds.getDeliveryid());
		sdv.setReceivedfee(ds.getReceivedfee());
		sdv.setReturnedfee(ds.getReturnedfee());
		sdv.setBusinessfee(ds.getBusinessfee());
		sdv.setDeliverystate(ds.getDeliverystate());
		sdv.setCash(ds.getCash());
		sdv.setPos(ds.getPos());
		sdv.setPosremark(ds.getPosremark());
		sdv.setMobilepodtime(ds.getMobilepodtime());
		sdv.setCheckfee(ds.getCheckfee());
		sdv.setCheckremark(ds.getCheckremark());
		sdv.setReceivedfeeuser(ds.getReceivedfeeuser());
		sdv.setCreatetime(ds.getCreatetime());
		sdv.setOtherfee(ds.getOtherfee());
		sdv.setPodremarkid(ds.getPodremarkid());
		sdv.setDeliverstateremark(ds.getDeliverstateremark());
		sdv.setIsout(ds.getIsout());
		sdv.setPos_feedback_flag(ds.getPos_feedback_flag());
		sdv.setUserid(ds.getUserid());
		sdv.setGcaid(ds.getGcaid());
		sdv.setSign_typeid(ds.getSign_typeid());
		sdv.setSign_man_phone(ds.getSign_man_phone());
		sdv.setSign_man(ds.getSign_man());
		sdv.setSign_time(ds.getSign_time());
		sdv.setDeliverytime(ds.getDeliverytime());
		CwbOrder cwbOrder = null;
		if (clist == null) {
			cwbOrder = cwbDAO.getCwbByCwb(ds.getCwb());
		} else {
			for (CwbOrder c : clist) {
				if (c.getCwb().equals(ds.getCwb())) {
					cwbOrder = c;
					break;
				}
			}
		}
		if (cwbOrder == null) {
			logger.warn("cwborder {} not exist" + ds.getCwb());
			return null;
		}
		sdv.setCustomerid(cwbOrder.getCustomerid());
		for (Customer c : customerList) {
			if (cwbOrder.getCustomerid() == c.getCustomerid()) {
				sdv.setCustomername(c.getCustomername());
			}
		}
		sdv.setEmaildate(cwbOrder.getEmaildate());
		sdv.setConsigneename(cwbOrder.getConsigneename());
		sdv.setConsigneemobile(cwbOrder.getConsigneemobile());
		sdv.setConsigneephone(cwbOrder.getConsigneephone());
		sdv.setConsigneeaddress(cwbOrder.getConsigneeaddress());
		sdv.setBackcarname(cwbOrder.getBackcarname());
		sdv.setSendcarname(cwbOrder.getSendcarname());
		String realname = "";
		for (User u : userList) {
			if (u.getUserid() == ds.getDeliveryid()) {
				realname = u.getRealname();
			}
		}
		sdv.setDeliverealname(realname);
		sdv.setFlowordertype(cwbOrder.getFlowordertype());
		sdv.setCwbordertypeid(cwbOrder.getCwbordertypeid());
		sdv.setCwbremark(cwbOrder.getCwbremark());
		sdv.setBackreason(cwbOrder.getBackreason());
		sdv.setLeavedreason(cwbOrder.getLeavedreason());
		return sdv;
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
	private void dealwith_fankui(CommenSendData commd, String cwb, long podresultid, User user, CwbOrder cwbOrder) throws IOException, JsonParseException, JsonMappingException {
		OrderFlowDto orderFlowDto = JacksonMapper.getInstance().readValue(commd.getDatajson(), OrderFlowDto.class);

		logger.info("系统对接反馈:cwb=" + cwb + ",");

		BigDecimal pos = BigDecimal.ZERO;
		BigDecimal check = BigDecimal.ZERO;
		BigDecimal cash = BigDecimal.ZERO;
		BigDecimal paybackedfee = BigDecimal.ZERO;

		if (podresultid == DeliveryStateEnum.PeiSongChengGong.getValue()) {
			if (cwbOrder.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmenhuan.getValue()) {
				podresultid = DeliveryStateEnum.ShangMenHuanChengGong.getValue();
			} else if (cwbOrder.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue()) {
				podresultid = DeliveryStateEnum.ShangMenTuiChengGong.getValue();
			}

		} else if (podresultid == DeliveryStateEnum.JuShou.getValue()) {
			if (cwbOrder.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmenhuan.getValue()) {
				podresultid = DeliveryStateEnum.JuShou.getValue();
			} else if (cwbOrder.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue()) {
				podresultid = DeliveryStateEnum.ShangMenJuTui.getValue();
			}
		}

		String deliverremark = "";

		DeliveryState deliverystate = deliveryStateDAO.getActiveDeliveryStateByCwb(cwb);

		if (deliverystate != null) {
			if (deliverystate.getIsout() == 1) { // 应退款
				pos = BigDecimal.ZERO;
				cash = BigDecimal.ZERO;
				check = BigDecimal.ZERO;
				if (podresultid == DeliveryStateEnum.PeiSongChengGong.getValue() || podresultid == DeliveryStateEnum.ShangMenHuanChengGong.getValue()
						|| podresultid == DeliveryStateEnum.ShangMenTuiChengGong.getValue()) {
					paybackedfee = deliverystate.getBusinessfee();
					deliverremark = paybackedfee.compareTo(BigDecimal.ZERO) > 0 ? "现金退款" : "";
				}

			} else if (podresultid == DeliveryStateEnum.PeiSongChengGong.getValue() || podresultid == DeliveryStateEnum.ShangMenHuanChengGong.getValue()) { // 应收款
				SystemInstall isToCash = systemInstallDAO.getSystemInstallByName("isToCash");
				if (isToCash != null && isToCash.getValue().equals("yes")) {
					cash = deliverystate.getBusinessfee();
					deliverremark = "现金支付";
				} else {
					if (orderFlowDto.getPaytype() == PaytypeEnum.Pos.getValue()) {
						pos = deliverystate.getBusinessfee();
						deliverremark = "刷卡支付";
					} else if (orderFlowDto.getPaytype() == PaytypeEnum.Xianjin.getValue()) {
						cash = deliverystate.getBusinessfee();
						deliverremark = "现金支付";
					} else if (orderFlowDto.getPaytype() == PaytypeEnum.Zhipiao.getValue()) {
						check = deliverystate.getBusinessfee();
						deliverremark = "支票";
					} else {
						if (cwbOrder.getPaywayid() == PaytypeEnum.Xianjin.getValue()) {
							cash = deliverystate.getBusinessfee();
						} else if (cwbOrder.getPaywayid() == PaytypeEnum.Pos.getValue()) {
							pos = deliverystate.getBusinessfee();
						} else if (cwbOrder.getPaywayid() == PaytypeEnum.Zhipiao.getValue()) {
							check = deliverystate.getBusinessfee();
						}
					}
				}
			}

		}

		long backedreasonid = 0;
		long leavedreasonid = 0;

		String deliverstateremark = "系统对接";

		if (podresultid == DeliveryStateEnum.JuShou.getValue() || podresultid == DeliveryStateEnum.ShangMenJuTui.getValue() || podresultid == DeliveryStateEnum.BuFenTuiHuo.getValue()) {

			backedreasonid = Long.valueOf(orderFlowDto.getExptcode() == null || orderFlowDto.getExptcode().isEmpty() ? "0" : orderFlowDto.getExptcode());
			deliverremark = "";
		} else if (podresultid == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
			leavedreasonid = Long.valueOf(orderFlowDto.getExptcode() == null || orderFlowDto.getExptcode().isEmpty() ? "0" : orderFlowDto.getExptcode());
			deliverremark = "";
			deliverstateremark = deliverstateremark + "-" + orderFlowDto.getExptmsg();
		}

		Reason reason = reasonDAO.getReasonByReasonid(backedreasonid == 0 ? leavedreasonid : backedreasonid);
		if (reason == null) {
			backedreasonid = 0;
			leavedreasonid = 0;
		}

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("deliverid", user.getUserid());
		parameters.put("podresultid", podresultid);
		parameters.put("backreasonid", backedreasonid);
		parameters.put("leavedreasonid", leavedreasonid);
		parameters.put("receivedfeecash", cash);
		parameters.put("receivedfeepos", pos);
		parameters.put("receivedfeecheque", check);
		parameters.put("receivedfeeother", BigDecimal.ZERO);
		parameters.put("paybackedfee", paybackedfee);
		parameters.put("podremarkid", (long) 0);
		parameters.put("posremark", pos.compareTo(BigDecimal.ZERO) > 0 ? "POS刷卡" : "");
		parameters.put("checkremark", check.compareTo(BigDecimal.ZERO) > 0 ? "支票支付" : "");
		parameters.put("deliverstateremark", deliverstateremark);
		parameters.put("owgid", 0);
		parameters.put("sessionbranchid", user.getBranchid());
		parameters.put("sessionuserid", user.getUserid());
		parameters.put("sign_typeid", 1);
		parameters.put("sign_man", cwbOrder.getConsigneename());
		parameters.put("sign_time", DateTimeUtil.getNowTime());

		parameters.put("nosysyemflag", "1");//
		parameters.put("firstlevelreasonid", reason==null?0:reason.getParentid());
		cwborderService.deliverStatePod(user, cwb, cwb, parameters);
	}

	/**
	 * 处理站点退货出站
	 * 
	 * @param cwb
	 * @param user
	 * @param cwbOrder
	 * @param flowordertype
	 */
	private void dealWith_TuiHuoChuZhan(String cwb, User user, CwbOrder cwbOrder, long flowordertype) {
		Customer customer = customerDAO.getCustomerById(cwbOrder.getCustomerid());

		long nextbranchid = 0; // 这里退货站id
		List<BranchRoute> branchRouteList = branchRouteDAO.getBranchRouteByWheresql(cwbOrder.getStartbranchid(), 0, 2);

		for (BranchRoute rote : branchRouteList) {
			Branch branch = branchDAO.getBranchByBranchid(rote.getToBranchId());
			if (branch.getSitetype() == BranchEnum.TuiHuo.getValue()) {
				nextbranchid = branch.getBranchid();
				break;
			}
		}

		if (nextbranchid == 0) { // 如果下一站不是退货站类型
			logger.warn("下一站不是退货站类型或者没有指定", cwb);
			throw new RuntimeException("下一站不是退货站类型或者没有指定,cwb=" + cwb);
		}

		if (customer.getIsypdjusetranscwb() == 1) {// 使用一票多件模式
			List<TranscwbView> translist = transCwbDao.getTransCwbByCwb(cwb);
			if (translist == null || translist.size() == 0) {
				logger.info("一票多件查询transcwb表异常,cwb={},flowordertype={}", cwb, flowordertype);
				return;
			}
			for (TranscwbView trans : translist) {
				cwborderService.outUntreadWarehous(user, trans.getTranscwb(), trans.getTranscwb(), 0, 0, nextbranchid, 0, true, "系统对接", "", false);
			}

		} else {

			cwborderService.outUntreadWarehous(user, cwb, cwb, 0, 0, nextbranchid, 0, true, "系统对接", "", false);
		}
	}

}
