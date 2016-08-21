package cn.explink.b2c.ems;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import cn.explink.b2c.tools.ExptCodeJoint;
import cn.explink.b2c.tools.ExptCodeJointDAO;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.tools.JointService;
import cn.explink.controller.DeliveryStateDTO;
import cn.explink.controller.DeliveryStateView;
import cn.explink.core.utils.StringUtils;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.CwbStateControlDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.ExceptionCwbDAO;
import cn.explink.dao.OrderBackCheckDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.TransCwbDetailDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.CwbStateControl;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.GotoClassOld;
import cn.explink.domain.OrderBackCheck;
import cn.explink.domain.User;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.BranchTypeEnum;
import cn.explink.enumutil.CwbFlowOrderTypeEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.EMSTraceDataEnum;
import cn.explink.enumutil.ExceptionCwbErrorTypeEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.exception.CwbException;
import cn.explink.exception.ExplinkException;
import cn.explink.pos.tools.SignTypeEnum;
import cn.explink.service.CustomerService;
import cn.explink.service.CwbAutoHandleService;
import cn.explink.service.CwbOrderService;
import cn.explink.service.OrderBackCheckService;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.StringUtil;
import cn.explink.util.Tools;

@Service
public class EMSService {
	private Logger logger = LoggerFactory.getLogger(EMSService.class);
	
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	CustomerService customerService;
	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	CwbAutoHandleService cwbAutoHandleService;
	@Autowired
	CwbOrderService cwbOrderService;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	CwbStateControlDAO cwbStateControlDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	OrderBackCheckService orderBackCheckService;
	@Autowired
	ExceptionCwbDAO exceptionCwbDAO;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	JointService jointService;
	@Autowired
	ExptCodeJointDAO exptCodeJointDAO;
	@Autowired
	EMSDAO eMSDAO;
	@Autowired
	OrderBackCheckDAO orderBackCheckDAO;
	@Autowired
	TransCwbDetailDAO transCwbDetailDAO;
	
	@Transactional
	public void edit(HttpServletRequest request, int joint_num) {
		EMS ems = new EMS();
		ems.setSysAccount(request.getParameter("sysAccount"));
		ems.setEmsStateUrl(request.getParameter("emsStateUrl"));
		ems.setEmsTranscwbUrl(request.getParameter("emsTranscwbUrl"));
		ems.setEncodeKey(request.getParameter("encodeKey"));
		ems.setOrderSendUrl(request.getParameter("orderSendUrl"));
		ems.setSendOrderCount(Integer.parseInt(request.getParameter("sendOrderCount")));
		ems.setSupportKey(Integer.parseInt(request.getParameter("supportKey")));
		ems.setAppKey(request.getParameter("appKey"));
		ems.setEmsDiliveryid(Long.parseLong(request.getParameter("emsDiliveryid")));
		ems.setEmsBranchid(Long.parseLong(request.getParameter("emsBranchid")));

		JSONObject jsonObj = JSONObject.fromObject(ems);
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

	public String getObjectMethod(int key){
		JointEntity obj=jiontDAO.getJointEntity(key);
		return obj==null?null:obj.getJoint_property();
	}
	public EMS getEMS(int key){
		if(getObjectMethod(key)==null){
			return null;
		}
	    JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key)); 
	    EMS ems = (EMS)JSONObject.toBean(jsonObj,EMS.class);
		return ems;
	}
	
	public void update(int joint_num,int state){
		jiontDAO.UpdateState(joint_num, state);
	}

	public EMS getEmsObject(int key) {
		if (getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
		EMS ems = (EMS) JSONObject.toBean(jsonObj, EMS.class);
		return ems;
	}
	
	
	//处理数据逻辑，构建返回报文
	public int checkEMSData(ExpressMail expressMail, String listexpressmail)throws Exception {
		 //
		 String mailnum = expressMail.getMailnum();
		 //从ems运单对照表获取dmp运单号
	     String transcwb = eMSDAO.getTranscwbByEmailNo(mailnum);
	     
        if (transcwb.trim().length() == 0) {
			this.logger.info("dmp运单号不存在！");
			throw new CwbException("","落地配运单号不存在！");
		}
        long emsFlowordertype = 0;
    	transcwb = transcwb.trim();
    	String cwb = this.cwbOrderService.translateCwb(transcwb);
    	CwbOrder order = this.cwbDAO.getCwbByCwb(cwb);
    	if(order==null){
    		this.logger.info("dmp订单不存在！订单号["+cwb+"]");
			throw new CwbException("","落地配订单不存在！订单号["+cwb+"]");
    	}
    	String action = expressMail.getAction();
    	String properdelivery = expressMail.getProperdelivery();
    	String notproperdelivery = expressMail.getNotproperdelivery();
    	OrderFlow orderFlow = orderFlowDAO.getOrderCurrentFlowByCwb(cwb);
    	if(orderFlow==null){
    		this.logger.info("dmp轨迹不存在！订单号["+cwb+"]");
			throw new CwbException("","落地配轨迹不存在！订单号["+cwb+"]");
    	}
		if(action.equals("10") && StringUtil.isEmpty(expressMail.getProperdelivery())){
			this.logger.info("EMS的签收信息异常,action=10(妥投)时，properdelivery为空。运单号：[" + transcwb + "]");
			throw new CwbException("","EMS的签收信息异常,action=10(妥投)时，properdelivery为空。运单号：[" + transcwb + "]");
		}else if(action.equals("10")&&!(properdelivery.equals("10")||properdelivery.equals("13")||properdelivery.equals("14")||properdelivery.equals("11"))){
			this.logger.info("EMS的签收信息异常,action=10(妥投)时，properdelivery["+properdelivery+"],落地配无法识别。运单号：[" + transcwb + "]");
			throw new CwbException("","EMS的签收信息异常,action=10(妥投)时，properdelivery["+properdelivery+"],落地配无法识别。运单号：[" + transcwb + "]");
		}
		
		if(action.equals("20")&&StringUtil.isEmpty(expressMail.getNotproperdelivery())){
			this.logger.info("EMS的签收信息异常,action=20(未妥投)时，notproperdelivery为空。运单号：[" + transcwb + "]");
			throw new CwbException("","EMS的签收信息异常,action=20(未妥投)时，notproperdelivery为空。运单号：[" + transcwb + "]");
		}
		
		if(!StringUtils.isEmpty(action)){
			if(action.equals("00")||action.equals("30")||action.equals("60")||action.equals("41")){
				//不做处理
				this.logger.info("EMS的action为00,30,41,60 时不做处理，运单号：[" + transcwb + "]");
				throw new CwbException("","EMS的action为00,30,41,60 时不做处理，运单号：[" + transcwb + "]");
			}else if(action.equals("40")){
				//if(flow==36&&order.getDeliverystate()==4){
					emsFlowordertype = 36;
				/*}else{
					//不做处理
					this.logger.info("EMS的action为40，且订单状态不是为审核为拒 时不做处理，运单号：[" + transcwb + "]");
					throw new CwbException("","EMS的action为40，且订单状态不是为审核为拒 时不做处理，运单号：[" + transcwb + "]");
				}*/
			}else if(action.equals("10")){//妥投
				emsFlowordertype=9;
			}else if(action.equals("20")){//配送失败
				emsFlowordertype=9;
			}else if(action.equals("50")){
				emsFlowordertype = 6;
			}else if(action.equals("51")){
				emsFlowordertype=7;
			}else {
				this.logger.info("EMS，action值不合理，dmp无法操作，action=["+action+"]! 运单号：[" + transcwb + "]");
				throw new CwbException("","EMS，action值不合理，dmp无法操作，action=["+action+"]! 运单号：[" + transcwb + "]");
			}
		}else{
			this.logger.info("EMS，action值为空，dmp无法操作，action=["+action+"]! 运单号：[" + transcwb + "]");
			throw new CwbException("","EMS，action值为空，dmp无法操作，action=["+action+"]! 运单号：[" + transcwb + "]");
		}
		String credate = Tools.getCurrentTime("yyyy-MM-dd HH:mm:ss");
		//根据订单号、运单号、操作状去除重复数据
		List<EMSFlowEntity> flowList = eMSDAO.getFlowByCondition(transcwb,mailnum,emsFlowordertype,action, notproperdelivery);
		if(flowList.size()!=0){
			//eMSDAO.updateFlowInfoByCondition(transcwb,emsFlowordertype,listexpressmail,credate,expressMail);
			throw new CwbException("","轨迹数据重复！运单号["+transcwb+"]");
		}
		 //保存获取的ems运单轨迹报文 eidt by zhouhuan 2016-07-21
        eMSDAO.saveEMSFlowInfo(transcwb,mailnum,listexpressmail,action,emsFlowordertype, properdelivery,notproperdelivery,credate,0l);
        return 1;
	}
	

	//处理ems轨迹逻辑
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public int handleTranscwbFlowResult(EMS ems,EMSFlowEntity emsFlowEntity)throws Exception {
		 //从ems运单对照表获取dmp运单号
	     String transcwb = emsFlowEntity.getTranscwb();
	    Branch emsbranch = branchDAO.getBranchByBranchid(ems.getEmsBranchid());
        if (transcwb.trim().length() == 0) {
			this.logger.info("dmp运单号不存在！");
			//return 2;
			throw new CwbException("","落地配运单号不存在！");
		}
        
        transcwb = transcwb.trim();
		String cwb = this.cwbOrderService.translateCwb(transcwb);
		CwbOrder order = this.cwbDAO.getCwbByCwb(cwb);
		/*String cwbByAddTrans = eMSDAO.getCwbByAddTranscwb(transcwb);
		if(!StringUtils.isEmpty(cwbByAddTrans)){
			order = this.cwbDAO.getCwbByCwb(cwbByAddTrans);
		}*/
		if(order==null){
			this.logger.info("EMS运单号，对应的DMP订单号不存在！EMS运单号：[" + emsFlowEntity.getEmailnum() + "]");
			throw new CwbException("","EMS运单号，对应的DMP订单号不存在！EMS运单号：[" + emsFlowEntity.getEmailnum() + "]");
		}
		//获取EMS退货出站下一站信息
		long tuihuoid = emsbranch.getTuihuoid();
		Branch emsTuihuoBranch = branchDAO.getBranchByBranchid(tuihuoid);
		User emsDelivery = this.userDAO.getUserByUserid(ems.getEmsDiliveryid());
		String action = emsFlowEntity.getEmsAction();
		String properdelivery = emsFlowEntity.getProperdelivery();
		OrderFlow orderFlow = orderFlowDAO.getOrderCurrentFlowByCwb(cwb);
		Date d = new Date();
		BigDecimal bd = BigDecimal.ZERO;
		Map<String, Object> parameters = null; 
		
		List<DeliveryState> deliveryStateList = this.deliveryStateDAO.getDeliveryStateByDeliver(ems.getEmsDiliveryid());
		List<DeliveryStateView> cwbOrderWithDeliveryState = this.getDeliveryStateViews(deliveryStateList, null);
		DeliveryStateDTO dsDTO = new DeliveryStateDTO();
		dsDTO.analysisDeliveryStateList(cwbOrderWithDeliveryState);
		
		Integer sign_typeid = 0;
		if(action.equals("10")&&!StringUtil.isEmpty(properdelivery)){
			if(properdelivery.equals("10")||properdelivery.equals("13")||properdelivery.equals("14")){
				sign_typeid = SignTypeEnum.BenRenQianShou.getValue();
			}else if(properdelivery.equals("11")){
				sign_typeid = SignTypeEnum.TaRenDaiQianShou.getValue();
			}else{
				this.logger.info("EMS的签收信息异常，运单号：[" + transcwb + "]");
				throw new CwbException("","EMS的签收信息异常，运单号：[" + transcwb + "]");
			}
		}
		
		int flow = orderFlow.getFlowordertype();
		//校验ems轨迹顺序
		int validateFlag = validateEMSOrder(emsFlowEntity.getEmsFlowordertype(),flow,order);
		if(validateFlag==0){
			throw new CwbException("","EMS运单轨迹顺序异常！当前订单操作状态flowordertype="+flow+", EMS的action为："+emsFlowEntity.getEmsAction());
		}
		//订单当前操作状态
		DeliveryState deliveryState = this.deliveryStateDAO.getActiveDeliveryStateByCwb(order.getCwb());
		
		if(!StringUtils.isEmpty(action)){
			if(action.equals("00")||action.equals("30")||action.equals("41")||action.equals("60")){
				//不做处理
				this.logger.info("EMS的action为00,30,41,60 时不做处理，运单号：[" + transcwb + "]");
				throw new CwbException("","EMS的action为00,30,41,60 时不做处理，运单号：[" + transcwb + "]");
			}else if(action.equals("50")){
				//如果此时订单状态不是出库状态，则需补环节
				if(flow==FlowOrderTypeEnum.DaoRuShuJu.getValue()
				   ||flow==FlowOrderTypeEnum.TiHuo.getValue()
				   ||flow==FlowOrderTypeEnum.TiHuoYouHuoWuDan.getValue()
				   ||flow==FlowOrderTypeEnum.RuKu.getValue()){
					this.logger.info("订单状态不是出库状态，需补环节，运单号：[" + transcwb + "]");
					order = cwbAutoHandleService.autoSupplyLink(emsDelivery, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), order, 0, transcwb, false);
				}
				//模拟站点到货
				this.subStationScan(transcwb,ems);
			}else if(action.equals("51")){
				//模拟小件员领货
				this.cwbbranchdeliver(transcwb,emsDelivery);
			}else if(action.equals("40")){
				if(flow==36&&order.getDeliverystate()==4){
					Customer customer = this.customerDAO.getCustomerById(this.cwbDAO.getCwbByCwb(cwb).getCustomerid());
					boolean chechFlag = customer.getNeedchecked() == 1 ? true : false;
					// chechFlag (退货是否需要审核的标识 0：否 ,1：是)
					if (chechFlag) {
						OrderBackCheck back = orderBackCheckDAO.getOrderBackCheckByCwb(order.getCwb());
						this.clientConfirm(back.getId()+"",emsDelivery);
					}
					//模拟退货出站（依据该客户是否要退货出站审核，如果要审核，模拟退货出站审核+退货出站；否则，直接模拟退货出站）
					this.imitateCwbexportUntreadWarhouse(transcwb, emsDelivery,emsTuihuoBranch);
				}else if(flow==FlowOrderTypeEnum.TuiHuoChuZhan.getValue()){
					//模拟退货出站
					this.imitateCwbexportUntreadWarhouse(transcwb, emsDelivery,emsTuihuoBranch);
				}else{
					//不做处理
					this.logger.info("EMS的action为40，且订单状态不是为审核为拒收 时不做处理，运单号：[" + transcwb + "]");
					throw new CwbException("","EMS的action为40，且订单状态不是为审核为拒 时不做处理，运单号：[" + transcwb + "]");
				}
			}else if(action.equals("10")&&!StringUtils.isEmpty(properdelivery)){
				// 已反馈订单不允许批量反馈
				if (((order.getFlowordertype() == FlowOrderTypeEnum.YiFanKui.getValue()) || (order.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue())) && (deliveryState.getDeliverystate() != DeliveryStateEnum.WeiFanKui
						.getValue())) {
					throw new CwbException(cwb, FlowOrderTypeEnum.YiFanKui.getValue(), ExceptionCwbErrorTypeEnum.YI_FAN_KUI_BU_NENG_PI_LIANG_ZAI_FAN_KUI);
				}
				/*
				 * 模拟归班反馈为配送成功，且审核通过
				 * 配送结果podresultid=1,podremarkid=58（已经配送），receivedfeecash:订单对应的应收金额
				 */
				parameters = this.getDeliveryResult(emsDelivery,1l,0l,58l,bd,order.getReceivablefee(),bd,bd,bd,"","","","",sign_typeid,0l,0l,d.toString(),bd,0,0l,0l,0l,transcwb);
				if(parameters!=null){
					try {
						this.cwbOrderService.deliverStatePod(emsDelivery, cwb, "", parameters);
					} catch (CwbException ce) {
						this.logger.info("EMS订单模拟归班反馈为配送成功异常："+ce.getMessage()+"! 运单号：[" + transcwb + "]");
						throw ce;
					}
				}else{
					this.logger.info("EMS订单模拟归班反馈数据异常! 运单号：[" + transcwb + "]");
					throw new CwbException("","EMS订单模拟归班反馈数据异常! 运单号：[" + transcwb + "]");
				}
				/*
				 * deliverpayuptype=1,deliverpayupamount=0,deliverpayupaddress="",deliverpayupbanknum="",
				 * deliverPosAccount=0,
				 */
				this.imitateOk(cwb,"",dsDTO,1,bd,"","",bd,bd,bd,emsDelivery);
			}else if(action.equals("20")){
				
				Long notproperdelivery = Long.parseLong(emsFlowEntity.getNotproperdelivery());
				ExptCodeJoint expt = null;
				if(notproperdelivery!=100){
					expt = exptCodeJointDAO.getExpListByCodeandid(notproperdelivery, ems.getSupportKey());
					if(expt==null){
						 this.logger.info("EMS，拒收原因映射设置异常，EMS拒收原因编号为：[" + notproperdelivery + "]");
						 throw new CwbException("","EMS，拒收原因映射设置异常，EMS拒收原因编号为：[" + notproperdelivery + "]"); 
					}
				}
				 
				// 已反馈订单不允许批量反馈
				if (((order.getFlowordertype() == FlowOrderTypeEnum.YiFanKui.getValue()) || (order.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue())) && (deliveryState.getDeliverystate() != DeliveryStateEnum.WeiFanKui
						.getValue())) {
					throw new CwbException(cwb, FlowOrderTypeEnum.YiFanKui.getValue(), ExceptionCwbErrorTypeEnum.YI_FAN_KUI_BU_NENG_PI_LIANG_ZAI_FAN_KUI);
				}
				 
				 if(notproperdelivery==100){
					/* 模拟归班反馈并审核为滞留自动领货
					 * podresultid=9（滞留自动领货）,firstlevelreasonid（114配送延），leavedreasonid（122天气原因）
					 */
					parameters = this.getDeliveryResult(emsDelivery,9l,0l,0l,bd,bd,bd,bd,bd,"","","","",sign_typeid,0l,0l,d.toString(),bd,114,122,0l,0l,transcwb);
					if(parameters!=null){
						try {
							this.cwbOrderService.deliverStatePod(emsDelivery, cwb, "", parameters);
						} catch (CwbException ce) {
							this.logger.info("EMS订单模拟归班反馈为【滞留自动领货】异常："+ce.getMessage()+"! 运单号：[" + transcwb + "]");
							throw ce;
						}
					}else{
						this.logger.info("EMS订单模拟归班反馈数据异常! 运单号：[" + transcwb + "]");
						throw new CwbException("","EMS订单模拟归班反馈数据异常! 运单号：[" + transcwb + "]");
					}
					/*
					 * deliverpayuptype=1,deliverpayupamount=0,deliverpayupaddress="",deliverpayupbanknum="",
					 * deliverPosAccount=0,
					 */
					this.imitateOk(cwb,"",dsDTO,1,bd,"","",bd,bd,bd,emsDelivery);
				}else if(notproperdelivery==117){
					/*
					 * 模拟归班反馈并审核为货物丢失
					 * 配送结果podresultid=8(货物丢失),podremarkid=58（已经配送）
					 */
					parameters = this.getDeliveryResult(emsDelivery,8l,0l,58l,bd,bd,bd,bd,bd,"","","","",sign_typeid,0l,expt.getReasonid(),d.toString(),bd,0l,0l,0l,0l,transcwb);
					if(parameters!=null){
						try {
							this.cwbOrderService.deliverStatePod(emsDelivery, cwb, "", parameters);
						} catch (CwbException ce) {
							this.logger.info("EMS订单模拟归班反馈为【货物丢失】异常："+ce.getMessage()+"! 运单号：[" + transcwb + "]");
							throw new CwbException("","EMS订单模拟归班反馈为【货物丢失】异常："+ce.getMessage()+"! 运单号：[" + transcwb + "]");
						}
					}else{
						this.logger.info("EMS订单模拟归班反馈数据异常! 运单号：[" + transcwb + "]");
						throw new CwbException("","EMS订单模拟归班反馈数据异常! 运单号：[" + transcwb + "]");
					}
					/*
					 * deliverpayuptype=1,deliverpayupamount=0,deliverpayupaddress="",deliverpayupbanknum="",
					 * deliverPosAccount=0,
					 */
					this.imitateOk(cwb,"",dsDTO,1,bd,"","",bd,bd,bd,emsDelivery);
				}else{
					//模拟归班反馈并审核为拒收
					parameters = this.getDeliveryResult(emsDelivery,4l,expt.getReasonid(),0l,bd,bd,bd,bd,bd,"","","","",sign_typeid,0l,0l,d.toString(),bd,expt.getReasonid(),0l,0l,0l,transcwb);
					if(parameters!=null){
						try {
							this.cwbOrderService.deliverStatePod(emsDelivery, cwb, "", parameters);
						} catch (CwbException ce) {
							this.logger.info("EMS订单模拟归班反馈为【拒收】异常："+ce.getMessage()+"! 运单号：[" + transcwb + "]");
						}
					}else{
						this.logger.info("EMS订单模拟归班反馈数据异常! 运单号：[" + transcwb + "]");
						throw new CwbException("","EMS订单模拟归班反馈数据异常! 运单号：[" + transcwb + "]");
					}
					/*
					 * deliverpayuptype=1,deliverpayupamount=0,deliverpayupaddress="",deliverpayupbanknum="",
					 * deliverPosAccount=0,
					 */
					this.imitateOk(cwb,"",dsDTO,1,bd,"","",bd,bd,bd,emsDelivery);
				}
				
			}else{
				this.logger.info("EMS，action值不合理，dmp无法操作，action=["+action+"]! 运单号：[" + transcwb + "]");
				throw new CwbException("","EMS，action值不合理，dmp无法操作，action=["+action+"]! 运单号：[" + transcwb + "]");
			}
		}else{
			this.logger.info("EMS，action值为空，dmp无法操作，action=["+action+"]! 运单号：[" + transcwb + "]");
			throw new CwbException("","EMS，action值为空，dmp无法操作，action=["+action+"]! 运单号：[" + transcwb + "]");
		}
		//eMSDAO.changeEmsTraceDataState(emsFlowEntity.getId(),EMSTraceDataEnum.chulichenggong.getValue(),"处理成功");
		return 1;
	}

    
    //模拟（非合包）站点到货,分站到货
    public void subStationScan(String transcwb,EMS ems) throws Exception{
    	User emsDelivery = this.userDAO.getUserByUserid(ems.getEmsDiliveryid());
		String cwb = this.cwbOrderService.translateCwb(transcwb);
		CwbOrder cwbOrderOld = this.cwbDAO.getCwbByCwb(cwb);
		try{
			if (cwbOrderOld == null) {
				this.logger.info("EMS分站到货扫描：无此单号/包号! 运单号：[" + cwb + "]");
				throw new CwbException(cwb, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
			}

			/**
			 * 快递分站到货跨环节逻辑，如果一个订单当前站不为空，并且该订单的当前环节的下一环节包括入库环节，则此次分战到货为跨环节分战到货
			 * 如果是跨环节分战到货则需补上订单出站轨迹并回传tps
			 */
			List<CwbStateControl> cwbStateControlList = new ArrayList<CwbStateControl>();
			if (cwbOrderOld.getFlowordertype() > 0) {
				cwbStateControlList = this.cwbStateControlDAO.getCwbStateControlByFromstate(Integer.parseInt(cwbOrderOld.getFlowordertype() + ""));
			}
			int kuahuanjieFlag = 0;

			// 判断是否为快递单，并且是当前站是否为0或者空
			if (cwbOrderOld.getCwbordertypeid() == CwbOrderTypeIdEnum.Express.getValue()) {
				// 判断当前站
				if ((cwbOrderOld.getCurrentbranchid() != 0) && (cwbOrderOld.getCurrentbranchid() != emsDelivery.getBranchid())) {
					for (int a = 0; a < cwbStateControlList.size(); a++) {
						// 判断订单当前环节的下一环节是否包含入分战到货 //如果到错货就不需要补这个流程信息 --刘武强 11.25
						if ((cwbStateControlList.get(a).getTostate() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue())) {
							if (cwbOrderOld.getFlowordertype() != FlowOrderTypeEnum.DaoCuoHuoChuLi.getValue()) {
								// 创建流程跟踪信息表
								this.cwbOrderService.createFloworder(emsDelivery, cwbOrderOld.getCurrentbranchid(), cwbOrderOld, FlowOrderTypeEnum.LanJianChuZhan, "", System.currentTimeMillis(), transcwb, false);
							}
							// 状态回传tps
							// expressOutStationService.executeTpsTransInterface4TransFeedBack(cwbOrderOld.getCwb(),
							// user, cwbOrderOld.getNextbranchid());
							kuahuanjieFlag = 1;
							break;
						}
					}
					if (kuahuanjieFlag == 0) {
						this.logger.info("EMS分站到货扫描：快递单揽件入站状态不允许做中转出站操作! 运单号：[" + transcwb + "]");
						throw new CwbException(cwb, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.BuNengKuaHuanJieRuZhan);
					}
				} else if ((cwbOrderOld.getCurrentbranchid() != 0) && (cwbOrderOld.getCurrentbranchid() == emsDelivery.getBranchid())) {
					this.logger.info("EMS分站到货扫描：重复扫描! 运单号：[" + transcwb + "]");
					throw new CwbException(cwb, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.Operation_Repeat);
				}
			} else {
				Branch branchStart = this.branchDAO.getBranchByBranchid(cwbOrderOld.getStartbranchid());
				// 如果上一站是二级站
				if ((branchStart != null) && (branchStart.getContractflag() != null)) {
					if (Integer.parseInt(branchStart.getContractflag()) == BranchTypeEnum.ErJiZhan.getValue()) {
						cwbOrderOld.setFlowordertype(FlowOrderTypeEnum.LanJianRuZhan.getValue());
						cwbOrderOld.setNextbranchid(0);
					}
				}

			}
			// ===================快递逻辑结束==============================
			CwbOrder cwbOrder = this.cwbOrderService.substationGoods(emsDelivery, cwb, transcwb, 0, 0, "", "", false);
		}catch(Exception e){
			this.logger.info("EMS模拟站点到货异常! 运单号：[" + transcwb + "]");
			throw e;
			//修改临时表对应数据的状态
			/*eMSDAO.changeEmsTraceDataState(id, state, remark);*/
		}
    }
    
    //模拟小件员领货
    public void cwbbranchdeliver(String transcwb,User emsDelivery) throws Exception {
		String cwb = this.cwbOrderService.translateCwb(transcwb);
		CwbOrder cwbOrder = this.cwbOrderService.receiveGoods(emsDelivery, emsDelivery, cwb, transcwb);
		if (cwbOrder.getSendcarnum() > 1) {
			if (cwbOrder.getScannum() == cwbOrder.getSendcarnum()) {
				this.cwbOrderService.delYpdjFlowordertypeMethod(cwb);
			}
		}
    }
    
    //模拟退货出站
    public void imitateCwbexportUntreadWarhouse(String transcwb,User emsDelivery,Branch nextBranch)throws Exception{
		String cwb = this.cwbOrderService.translateCwb(transcwb);
		try{
			//CwbOrder cwbOrder = this.cwbOrderService.outUntreadWarehous(this.getSessionUser(), cwb, scancwb, driverid, truckid, branchid, requestbatchno, confirmflag == 1, comment, baleno, false);// 为包号修改
			CwbOrder cwbOrder = this.cwbOrderService.outUntreadWarehous(emsDelivery, cwb, transcwb, emsDelivery.getUserid(), 0, nextBranch.getBranchid(), 0, false, "", "", false);
			transCwbDetailDAO.updatePreviousbranchidByCwb(cwb, emsDelivery.getBranchid());
		}catch(Exception e){
			this.logger.info("EMS模拟退货出站异常! 运单号：[" + transcwb + "]");
			System.out.println("e");
			throw e;
		}
		
	}
    
    //模拟客服退货审核
	public void clientConfirm(String cwb, User emsDelivery) {
		try {
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dateStr = sdf.format(date);
			this.orderBackCheckService.save(cwb, emsDelivery,dateStr);
		} catch (CwbException e) {
			this.logger.info("EMS模拟客服审核异常! 订单号：[" + cwb + "]");
		}
	}
	
	/*
	 * 模拟归班审核
	 * zanbuchuliTrStr： 暂不处理订单号：“”
	 * nocwbs：未反馈订单号： 
	 * subTrStr 交款订单号   '20160406102732'
	 * okTime 审核时间     2016-04-15 09:29:51
	 * subAmount 审核金额  dsDTO.getUpPayAmount() 
	 * subAmountPos 审核POS金额   dsDTO.getPos_amount()
	 * deliverealuser 小件员   4378
	 * gotoClassOld 存储历史明细对象    
	 * deliverpayuptype 小件员交款类型   0
	 * deliverpayupamount 小件员交款金额  0
	 * deliverpayupbanknum 小件员交款小票号 POS类型才有   ‘’
	 * deliverpayupaddress 小件员交款地址 现金类型才有 “”
	 * deliverpayupamount_pos 小件员交用户刷pos金额  0
	 * deliverAccount 小件员当前帐户金额   0
	 * deliverPosAccount 小件员当前pos帐户金额   0
	 */
	public void imitateOk(String subTrStr,String zanbuchuliTrStr,DeliveryStateDTO dsDTO, 
			int deliverpayuptype,BigDecimal deliverpayupamount,String deliverpayupaddress,String deliverpayupbanknum,
			BigDecimal deliverpayupamount_pos,BigDecimal deliverAccount,BigDecimal deliverPosAccount, User emsDelivery) throws Exception{
		String okTime = Tools.getCurrentTime("yyyy-MM-dd HH:mm:ss");
		BigDecimal subAmount = BigDecimal.ZERO;
		BigDecimal subAmountPos = BigDecimal.ZERO;
		try {
			if (subTrStr.trim().length() == 0) {
				this.logger.info("没有关联订单!");
				return ;
			} 
			String[] cwbs = subTrStr.trim().split(",");
			for (String cwb : cwbs) {
				cwb = cwb.replaceAll("'", "");
				CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwb);
				DeliveryState deliverystate = this.deliveryStateDAO.getActiveDeliveryStateByCwb(co.getCwb());
				subAmount = subAmount.add(deliverystate.getCash()).add(deliverystate.getCheckfee()).add(deliverystate.getOtherfee()).subtract(deliverystate.getReturnedfee()).add(deliverystate.getInfactfare());
				subAmountPos = subAmountPos.add(deliverystate.getPos()).add(deliverystate.getCodpos());
			}
			
			// 将支付宝COD扫码的支付合并到pos上作为归班
			//BigDecimal subAmountPosAndCodPos = dsDTO.getPos_amount().add(dsDTO.getCodpos_amount());
			deliverpayupamount_pos = deliverpayupamount_pos.add(dsDTO.getCodpos_amount());
			// 将支付宝COD扫码的支付合并到pos上作为归班
			//deliverpayupamount_pos = deliverpayupamount_pos.add(deliverystate.getCodpos());
			GotoClassOld gotoClassOld = this.loadFormForGotoClass(dsDTO);
		    this.cwbOrderService.deliverAuditok(emsDelivery, subTrStr, okTime, subAmount+"", subAmountPos+"", emsDelivery.getUserid(), gotoClassOld, deliverpayuptype, deliverpayupamount,
					deliverpayupbanknum, deliverpayupaddress, deliverpayupamount_pos, deliverAccount, deliverPosAccount);
		} catch (CwbException e) {
			String[] cwbs = subTrStr.split(",");
			for (String cwb : cwbs) {
				if (cwb.trim().length() == 0) {
					continue;
				}
				cwb = cwb.replaceAll("'", "");
				CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
				this.exceptionCwbDAO.createExceptionCwbScan(cwb, e.getFlowordertye(), e.getMessage(), emsDelivery.getBranchid(), emsDelivery.getUserid(), cwbOrder == null ? 0
						: cwbOrder.getCustomerid(), 0, 0, 0, "", cwb);
			}

			this.logger.error("", e);
			throw e;
		} catch (ExplinkException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}
	
	/*
	 * 获取反馈结果内容
	 * podresultid：配送结果，backreasonid：退货原因 ，podremarkid：配送结果备注，returnedfee：退还现金，receivedfeecash：实收现金，
	 * receivedfeepos:POS刷卡实收,receivedfeecheque:支票实收,receivedfeeother:其他实收,posremark:POS备注,checkremark:支票号备注,
	 * deliverstateremark:反馈备注输入内容,signmanphone:实际签收人手机,signman:实际签收人,weishuakareasonid:未刷卡原因
	 * losereasonid:货物丢失原因,deliverytime：真实反馈时间，infactfare：实收运费，firstlevelreasonid：一级原因，
	 * leavedreasonid：二级原因，changereasonid：二级原因，firstchangereasonid：一级原因
	 */
	public Map<String, Object> getDeliveryResult(User emsDelivery,long podresultid,long backreasonid,long podremarkid,
			BigDecimal returnedfee,BigDecimal receivedfeecash,BigDecimal receivedfeepos,BigDecimal receivedfeecheque,
			BigDecimal receivedfeeother,String posremark,String checkremark,String deliverstateremark,String signmanphone,
			Integer signTypeid,long weishuakareasonid,long losereasonid,String deliverytime,BigDecimal infactfare,
			long firstlevelreasonid,long leavedreasonid,long changereasonid,long firstchangereasonid,String transcwb){
		//this.logger.info("web-editDeliveryState-进入EMS单票反馈,cwb={}", scancwb);
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("deliverid", emsDelivery.getUserid());// 小件员
		parameters.put("podresultid", podresultid);
		parameters.put("backreasonid", backreasonid);
		parameters.put("leavedreasonid", leavedreasonid);
		parameters.put("receivedfeecash", receivedfeecash);
		parameters.put("receivedfeepos", receivedfeepos);
		parameters.put("receivedfeecheque", receivedfeecheque);
		parameters.put("receivedfeeother", receivedfeeother);
		parameters.put("paybackedfee", returnedfee);
		parameters.put("podremarkid", podremarkid);
		parameters.put("posremark", posremark);
		parameters.put("checkremark", checkremark);
		parameters.put("deliverstateremark", deliverstateremark);
		parameters.put("owgid", 0);
		parameters.put("sessionbranchid", emsDelivery.getBranchid());
		parameters.put("sessionuserid", emsDelivery.getUserid());
		parameters.put("sign_typeid", signTypeid);
		parameters.put("sign_man_phone", signmanphone);
		if(signTypeid==1){
			parameters.put("sign_man", "本人签收");
		}else if(signTypeid==2){
			parameters.put("sign_man", "他人签收");
		}else{
			parameters.put("sign_man", "");
		}
		parameters.put("sign_time", DateTimeUtil.getNowTime());
		parameters.put("weishuakareasonid", weishuakareasonid);
		parameters.put("losereasonid", losereasonid);
		parameters.put("deliverytime_now", deliverytime);
		parameters.put("infactfare", infactfare);
		parameters.put("firstlevelreasonid", firstlevelreasonid);
		parameters.put("changereasonid", changereasonid);
		parameters.put("firstchangereasonid", firstchangereasonid);
		parameters.put("transcwb", transcwb);
		return parameters;
	}
	
	private GotoClassOld loadFormForGotoClass(DeliveryStateDTO dsDTO) {
		GotoClassOld gco = new GotoClassOld();
		gco.setNownumber(dsDTO.getNowNumber());
		gco.setYiliu(dsDTO.getYiliu());
		gco.setLishi_weishenhe(dsDTO.getLishi_weishenhe());
		gco.setZanbuchuli(dsDTO.getZanbuchuli());
		gco.setPeisong_chenggong(dsDTO.getFankui_peisong_chenggong()-dsDTO.getFankui_peisong_chenggong_zanbuchuli());
		gco.setPeisong_chenggong_amount(BigDecimal.valueOf(dsDTO.getAmountNotZanbuchuli(dsDTO.getFankui_peisong_chenggongList())));
		gco.setPeisong_chenggong_pos_amount(BigDecimal.valueOf(dsDTO.getPosAmountNotZanbuchuli(dsDTO.getFankui_peisong_chenggongList())));
		gco.setTuihuo(dsDTO.getFankui_tuihuo()-dsDTO.getFankui_tuihuo_zanbuchuli());
		gco.setTuihuo_amount(BigDecimal.valueOf(dsDTO.getAmountNotZanbuchuli(dsDTO.getFankui_tuihuoList())));
		gco.setBufentuihuo(dsDTO.getFankui_bufentuihuo()-dsDTO.getFankui_bufentuihuo_zanbuchuli());
		gco.setBufentuihuo_amount(BigDecimal.valueOf(dsDTO.getAmountNotZanbuchuli(dsDTO.getFankui_bufentuihuoList())));
		gco.setBufentuihuo_pos_amount(BigDecimal.valueOf(dsDTO.getPosAmountNotZanbuchuli(dsDTO.getFankui_bufentuihuoList())));
		gco.setZhiliu(dsDTO.getFankui_zhiliu()-dsDTO.getFankui_zhiliu_zanbuchuli());
		gco.setZhiliu_amount(BigDecimal.valueOf(dsDTO.getAmountNotZanbuchuli(dsDTO.getFankui_zhiliuList())));
		gco.setShangmentui_chenggong(dsDTO.getFankui_shangmentui_chenggong()-dsDTO.getFankui_shangmentui_chenggong_zanbuchuli());
		gco.setShangmentui_chenggong_amount(BigDecimal.valueOf(dsDTO.getAmountNotZanbuchuli(dsDTO.getFankui_shangmentui_chenggongList())));
		gco.setShangmentui_chenggong_fare(BigDecimal.valueOf(dsDTO.getSmtcgFareAmountNotZanbuchuli(dsDTO.getFankui_shangmentui_chenggongList())));
		gco.setShangmentui_jutui(dsDTO.getFankui_shangmentui_jutui()-dsDTO.getFankui_shangmentui_jutui_zanbuchuli());
		gco.setShangmentui_jutui_amount(BigDecimal.valueOf(dsDTO.getAmountNotZanbuchuli(dsDTO.getFankui_shangmentui_jutuiList()) ));
		gco.setShangmentui_jutui_fare(BigDecimal.valueOf(dsDTO.getSmtjtFareAmountNotZanbuchuli(dsDTO.getFankui_shangmentui_jutuiList())));
		gco.setShangmenhuan_chenggong(dsDTO.getFankui_shangmenhuan_chenggong()-dsDTO.getFankui_shangmenhuan_chenggong_zanbuchuli());
		gco.setShangmenhuan_chenggong_amount(BigDecimal.valueOf(dsDTO.getAmountNotZanbuchuli(dsDTO.getFankui_shangmenhuan_chenggongList())));
		gco.setShangmenhuan_chenggong_pos_amount(BigDecimal.valueOf(dsDTO.getPosAmountNotZanbuchuli(dsDTO.getFankui_shangmenhuan_chenggongList())));
		gco.setDiushi(dsDTO.getFankui_diushi()-dsDTO.getFankui_diushi_zanbuchuli());
		gco.setDiushi_amount(BigDecimal.valueOf(dsDTO.getAmountNotZanbuchuli(dsDTO.getFankui_diushiList())));
		gco.setZhongzhuan(dsDTO.getFankui_zhongzhuan()-dsDTO.getFankui_zhongzhuan_zanbuchuli());
		gco.setZhongzhuan_amount(BigDecimal.valueOf(dsDTO.getAmountNotZanbuchuli(dsDTO.getFankui_zhongzhuanList())));
		return gco;
	}
	
	public List<DeliveryStateView> getDeliveryStateViews(List<DeliveryState> dsList, String cwbs) {
		List<DeliveryStateView> deliveryStateViewList = new ArrayList<DeliveryStateView>();
		List<Customer> customerList = this.customerDAO.getAllCustomersNew();
		List<User> userList = this.userDAO.getAllUser();

		if (dsList.size() > 0) {
			if ((cwbs == null) || cwbs.equals("")) {
				StringBuffer cwbBuffer = new StringBuffer();
				for (DeliveryState ds : dsList) {
					cwbBuffer = cwbBuffer.append("'").append(ds.getCwb()).append("',");
				}
				cwbs = cwbBuffer.substring(0, cwbBuffer.length() - 1);
			}

			List<CwbOrder> clist = this.cwbDAO.getCwbByCwbs(cwbs);

			for (DeliveryState ds : dsList) {
				DeliveryStateView sdv = this.getDeliveryStateView(ds, customerList, userList, clist);
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
		sdv.setCodpos(ds.getCodpos());
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
		sdv.setSign_man(StringUtil.nullConvertToEmptyString(ds.getSign_man()));
		sdv.setSign_time(StringUtil.nullConvertToEmptyString(ds.getSign_time()));
		sdv.setDeliverytime(ds.getDeliverytime());

		CwbOrder cwbOrder = null;
		if (clist == null) {
			cwbOrder = this.cwbDAO.getCwbByCwb(ds.getCwb());
		} else {
			for (CwbOrder c : clist) {
				if (c.getCwb().equals(ds.getCwb())) {
					cwbOrder = c;
					break;
				}
			}
		}
		if (cwbOrder == null) {
			this.logger.warn("cwborder {} not exist" + ds.getCwb());
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
		sdv.setChangereason(cwbOrder.getChangereason());
		sdv.setShouldfare(ds.getShouldfare());
		sdv.setInfactfare(ds.getInfactfare());
		return sdv;
	}

	//远程获取ems运单号，并保存到运单对照表
	@Transactional
	public void getEMSTranscwb(String transcwb,EMS ems) throws Exception{
		String emsTranscwbUrl = ems.getEmsTranscwbUrl();
		//对接授权码
		String appKey = ems.getAppKey();
		BufferedReader in = null;
		String response_XML = "";
		
		try {
			//1.拼接requestXML是发送给EMS的xml信息 2.response_XML发送给EMS
			String requestXML = TranscwbRequestStr(ems.getSysAccount(), ems.getEncodeKey(),appKey, transcwb);
			
			logger.info("请求[EMS]运单号XML={}", requestXML);
			//将发送给ems的订单信息字符串进行base64加密
			String base64Sendstr = new BASE64Encoder().encode(requestXML.getBytes());
			base64Sendstr = URLEncoder.encode(base64Sendstr);
			// 创建HttpClient实例     
	        HttpClient httpclient = new DefaultHttpClient();  
			HttpPost httpposts = new HttpPost(emsTranscwbUrl + base64Sendstr);    
	        HttpResponse response = httpclient.execute(httpposts); 
	        in = new BufferedReader(new InputStreamReader(response.getEntity()  
	                .getContent()));  
	        StringBuffer sb = new StringBuffer("");  
	        String line = "";  
	        String NL = System.getProperty("line.separator");  
	        while ((line = in.readLine()) != null) {  
	            sb.append(line + NL);  
	        }  
	        in.close();  
	        String result = sb.toString();  
			
			//将返回给dmp的订单信息字符串进行base64解密
			response_XML = new String(new BASE64Decoder().decodeBuffer(result));
			//将返回给dmp的订单信息字符串进行base64解密
			logger.info("请求[EMS]运单号接口返回xml{}", response_XML);
			
			if (response_XML.contains("<result>0</result>")) {
				String errorDesc = response_XML.substring(response_XML.indexOf("<errorDesc>") + 11, response_XML.indexOf("</errorDesc>"));
				String errorCode = response_XML.substring(response_XML.indexOf("<errorCode>") + 11, response_XML.indexOf("</errorCode>"));
				logger.info("请求[EMS]运单号异常<result>0</result>,errorCode={}，errorDesc={}",errorCode,errorDesc);
				return;
			}

			// 3.成功了,解析xml
			EMSTranscwb eMSTranscwb = EMSUnmarchal.Unmarchal(response_XML);
			EmsAndDmpTranscwb emsAndDmpTranscwb = eMSTranscwb.getQryData();
			
			if (emsAndDmpTranscwb == null) {
				logger.info("请求[EMS]没有获取到EMS对应运单号!");
				return;
			} 
			
			transcwb = transcwb.trim();
	    	String cwb = this.cwbOrderService.translateCwb(transcwb);
	    	
	    	long existTranscwb = eMSDAO.getListByTranscwb(transcwb);
	    	if(existTranscwb!=0){
	    		logger.info("该运单号已获取对应的ems运单号！dmp运单号为：{}",transcwb);
				return;
	    	}
	    	String bingTime = DateTimeUtil.getNowTime();
	    	//解析并将获取的运单号信息存储到dmp与ems运单对照关系表
			eMSDAO.saveEMSEmailnoAndDMPTranscwb(cwb,transcwb,emsAndDmpTranscwb.getBillno(), bingTime);
			
			//更新订单临时表"获取运单状态字段"值
			List<SendToEMSOrder> orderList = eMSDAO.getSendOrderByTranscwb(transcwb);
			if(orderList.size()==0){
				throw new CwbException("","EMS订单下发临时表中没有对应运单记录!运单号为：["+transcwb+"]"); 
			}else{
				eMSDAO.updateGetTranscwbStateByTranscwb(transcwb);
			}
			logger.info("请求[EMS]获取到对应运单号成功!");
			return;

		} catch (Exception e) {
			logger.error("处理请求[EMS]运单号异常！返回信息：" + response_XML + ",异常原因：" + e.getMessage(), e);
			throw e;
		}
	}

	//拼接运单对照关系请求报文
	public String TranscwbRequestStr(String userid, String userkey, String appKey, String bigAccountDataId) {
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><XMLInfo>" + "<sysAccount>"+userid+"</sysAccount>" + "<passWord>" + userkey + "</passWord>" + "<appKey>" + appKey + "</appKey>"
				+ "<bigAccountDataId>" +bigAccountDataId+ "</bigAccountDataId>" + "</XMLInfo>";
		return xml;
	}
	
	
	//校验ems轨迹顺序
	public int validateEMSOrder(long emsFlowordertype,long currentOrderType,CwbOrder order){
		int flag = 1;
		if(order.getSendcarnum()==1){
			if(emsFlowordertype==0){
				flag = 0;
			}else if(emsFlowordertype==6 && !(currentOrderType==1||currentOrderType==2||currentOrderType==3||currentOrderType==4||currentOrderType==6)){
				flag = 0;
			}else if(emsFlowordertype==7 && currentOrderType!=7){
				flag = 0;
			}else if(emsFlowordertype==9 && currentOrderType!=9){
				flag = 0;
			}else if(emsFlowordertype==36 && currentOrderType!=36){
				flag = 0;
			}
		}else if((order.getSendcarnum() > 1) || (order.getBackcarnum() > 1)){
			OrderFlow orderFlow = null;
			String flowOrderStr = "";
			if(emsFlowordertype==6){
				flowOrderStr = CwbFlowOrderTypeEnum.WeiDaoHuo.getValue()+","+
						CwbFlowOrderTypeEnum.TiHuo.getValue()+","+
						CwbFlowOrderTypeEnum.TiHuoYouHuoWuDan.getValue()+","+
						CwbFlowOrderTypeEnum.RuKu.getValue()+","+
						CwbFlowOrderTypeEnum.ChuKuSaoMiao.getValue()+","+
						CwbFlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()+","+
						CwbFlowOrderTypeEnum.DaoCuoHuoChuLi.getValue()+","+
						CwbFlowOrderTypeEnum.FenZhanLingHuo.getValue()+","+
						CwbFlowOrderTypeEnum.YiShenHe.getValue();
				orderFlow = orderFlowDAO.getOrderFlowByCwbAndFlowtype(order.getCwb(),flowOrderStr);
			}else if(emsFlowordertype==7){
				flowOrderStr = CwbFlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()+","+
						CwbFlowOrderTypeEnum.DaoCuoHuoChuLi.getValue()+","+
						CwbFlowOrderTypeEnum.FenZhanLingHuo.getValue()+","+
						CwbFlowOrderTypeEnum.YiShenHe.getValue();
				orderFlow = orderFlowDAO.getOrderFlowByCwbAndFlowtype(order.getCwb(),flowOrderStr);
			}else if(emsFlowordertype==9){
				flowOrderStr = CwbFlowOrderTypeEnum.FenZhanLingHuo.getValue()+","+
						CwbFlowOrderTypeEnum.YiShenHe.getValue();
				orderFlow = orderFlowDAO.getOrderFlowByCwbAndFlowtype(order.getCwb(),flowOrderStr);
			}else if(emsFlowordertype==36){
				flowOrderStr = CwbFlowOrderTypeEnum.YiShenHe.getValue()+"";
				orderFlow = orderFlowDAO.getOrderFlowByCwbAndFlowtype(order.getCwb(),flowOrderStr);
			}
			if(orderFlow!=null){
				flag = 1;
			}else{
				flag = 0;
			}
		}
		
		return flag;
	}
	
	//拼接发送给ems订单信息字符串
	public String getObjectToXmlStr(List<SendToEMSOrder> list,String sysAccount,String passWord,
			String printKind,String appKey,String encodeKey) { 
		StringBuffer objstr = new StringBuffer();
		for(SendToEMSOrder orderInfo : list){
			objstr.append("<printData>");
			objstr.append(orderInfo.getData());
			objstr.append("</printData>");
		}
		StringBuffer stringBuffer = new StringBuffer(); 
		stringBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><XMLInfo>");
		stringBuffer.append("<sysAccount>"+sysAccount+"</sysAccount>");
		stringBuffer.append("<passWord>"+passWord+"</passWord>");
		stringBuffer.append("<printKind>"+printKind+"</printKind>");
		stringBuffer.append("<appKey>"+appKey+"</appKey>");
		stringBuffer.append("<encodeKey>"+encodeKey+"</encodeKey>");
		stringBuffer.append("<printDatas>");
		stringBuffer.append(objstr);
		stringBuffer.append("</printDatas>");
		stringBuffer.append("</XMLInfo>");
		return stringBuffer.toString(); 
	}
	
	//处理发送订单信息给ems逻辑
	@SuppressWarnings("restriction")
	@Transactional
	public void handleSendOrderToEMS(EMS ems,List<SendToEMSOrder> subList){
		String sysAccount=ems.getSysAccount();
		String passWord=ems.getEncodeKey();
		String printKind=2+"";
		String appKey=ems.getAppKey();
		String encodeKey=ems.getEncodeKey();
		String sendOrderUrl = ems.getOrderSendUrl();
		String sendstr = this.getObjectToXmlStr(subList,sysAccount,passWord,printKind,appKey,encodeKey);
		//String sendOrderUrl = "http://os.ems.com.cn:8081/zkweb/bigaccount/getBigAccountDataAction.do?method=getPrintDatas&xml=";
		List<SendToEMSOrder> currentList = subList;
		BufferedReader in = null;
		try {
			logger.info("[EMS]订单下发接口,发送报文xml{}", sendstr);
			//将发送给ems的订单信息字符串进行base64加密
			String base64Sendstr = new BASE64Encoder().encode(sendstr.getBytes());
			base64Sendstr = URLEncoder.encode(base64Sendstr);
			//String base64Sendstr="http://os.ems.com.cn:8081/zkweb/bigaccount/getBigAccountDataAction.do?method=getPrintDatas&xml=PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiID8%2BCjxYTUxJbmZvPgo8c3lzQWNjb3VudD5BMTIzNDU2Nzg5MFo8L3N5c0FjY291bnQ%2BCjxwYXNzV29yZD5lMTBhZGMzOTQ5YmE1OWFiYmU1NmUwNTdmMjBmODgzZTwvcGFzc1dvcmQ%2BCjxhcHBLZXk%2BU0E3MTI1MTQ0QjNhNkJCMDA8L2FwcEtleT4KPHByaW50S2luZD4yPC9wcmludEtpbmQ%2BCjxwcmludERhdGFzPgo8cHJpbnREYXRhPgo8YmlnQWNjb3VudERhdGFJZD4yMDE1MTIwOTE3Mjc0MDIwPC9iaWdBY2NvdW50RGF0YUlkPgo8YnVzaW5lc3NUeXBlPjQ8L2J1c2luZXNzVHlwZT4KPGJpbGxubz5LREJHMTAwNjE4MjIxPC9iaWxsbm8%2BCjxzY29udGFjdG9yPmFhYTwvc2NvbnRhY3Rvcj4KPHNjdXN0TW9iaWxlPjEzMzMzMzMzMzMzPC9zY3VzdE1vYmlsZT4KPHNjdXN0Q29tcD5iYmI8L3NjdXN0Q29tcD4KPHRjb250YWN0b3I%2BY2NjPC90Y29udGFjdG9yPgo8dGN1c3RNb2JpbGU%2BMTU4NTY1OTg2MDY8L3RjdXN0TW9iaWxlPgo8dGN1c3RBZGRyPmVlZWVlZWVlZWVlPC90Y3VzdEFkZHI%2BCjx0Y3VzdFByb3ZpbmNlPuWuieW%2BveecgTwvdGN1c3RQcm92aW5jZT4KPHRjdXN0Q2l0eT7lronluobluII8L3RjdXN0Q2l0eT4KPHRjdXN0Q291bnR5PuWuv%2BadvuWOvzwvdGN1c3RDb3VudHk%2BCjwvcHJpbnREYXRhPgo8L3ByaW50RGF0YXM%2BCjwvWE1MSW5mbz4K";
			// 创建HttpClient实例     
	        HttpClient httpclient = new DefaultHttpClient();  
			HttpPost httpposts = new HttpPost(sendOrderUrl + base64Sendstr);    
	        HttpResponse response = httpclient.execute(httpposts); 
	        in = new BufferedReader(new InputStreamReader(response.getEntity()  
                    .getContent()));  
            StringBuffer sb = new StringBuffer("");  
            String line = "";  
            String NL = System.getProperty("line.separator");  
            while ((line = in.readLine()) != null) {  
                sb.append(line + NL);  
            }  
            in.close();  
            String result = sb.toString();  
			
			//String response_XML = this.HTTPInvokeWs(base64Sendstr, sendOrderUrl);
			//将返回给dmp的订单信息字符串进行base64解密
			String response_XML = new String(new BASE64Decoder().decodeBuffer(result));
			logger.info("[EMS]订单下发接口返回xml{}", response_XML);
			
			// 3.成功了,解析xml
			EMSOrderResultBack eMSOrderResultBack = EMSUnmarchal.UnmarchalOrder(response_XML);
			List<ErrorDetail> errorDetail = eMSOrderResultBack.getErrorDetail();
			
			if (eMSOrderResultBack!=null && eMSOrderResultBack.getResult().equals("0")) {
				logger.info("EMS订单下发失败,外层错误代码:{},外层错误描述:{}",eMSOrderResultBack.getErrorCode(),eMSOrderResultBack.getErrorDesc());
				//更改订单临时表，发送状态为2：发送失败
				if(errorDetail!=null){
					for(ErrorDetail detail : errorDetail){
						eMSDAO.updateOrderTemp(detail.getDataID(),detail.getDataError(),2);
						logger.info("EMS订单下发失败,订单号为：{}",detail.getDataID());
						SendToEMSOrder errOder = eMSDAO.getSendOrderByTranscwb(detail.getDataID()).get(0);
						currentList.remove(errOder);
					}
				}else{
					for(SendToEMSOrder order : subList){
						eMSDAO.updateOrderTemp(order.getTranscwb(),eMSOrderResultBack.getErrorCode()+":"+eMSOrderResultBack.getErrorDesc(),2);
						logger.info("EMS订单下发失败,运单号为：{}",order.getTranscwb());
						SendToEMSOrder errOder = eMSDAO.getSendOrderByTranscwb(order.getTranscwb()).get(0);
						currentList.remove(errOder);
					}
				}
				return;
			}
			
			//更改订单临时表，发送状态为1：发送成功
			for(SendToEMSOrder order : currentList){
				eMSDAO.updateOrderTemp(order.getTranscwb(),"发送成功！",1);
				logger.info("EMS订单下发成功，运单号为：{}",order.getTranscwb());
			}
		} catch (Exception e) {
			logger.info("EMS订单下发信息返回异常,异常信息为：{}！异常报文为：{}",e.getMessage(),sendstr);
		} 
		
	}

	public void saveEmsFlowInfo(ExpressMail expressMail, String listexpressmail) {
		String mailnum = expressMail.getMailnum();
		String action = expressMail.getAction();
		String credate = Tools.getCurrentTime("yyyy-MM-dd HH:mm:ss");
		//保存获取的ems运单轨迹报文
        eMSDAO.saveEMSFlowInfoToTemp(mailnum,listexpressmail,credate,action);
	}

	@Transactional
	public void initialHandleEMSFlow(EMS ems, List<EMSFlowObjInitial> subList) {
		for (EMSFlowObjInitial eMSFlowObjInitial : subList) {
			int state=EMSTraceDataEnum.weichuli.getValue();
			String remark = "";
			try {
				JSONObject jsonObject=JSONObject.fromObject(eMSFlowObjInitial.getFlowContent());
		        String listexpressmail=jsonObject.getString("listexpressmail");
		        
		        JSONArray jsonarray = JSONArray.fromObject(listexpressmail);  
			    List<ExpressMail> dataList = (List<ExpressMail>)JSONArray.toCollection(jsonarray,ExpressMail.class);
			    
			    if(dataList.size()!=0){
	            	 for(int i=0;i<dataList.size();i++){
	            		 String transcwb = "";
	            		 state = this.checkEMSData(dataList.get(i),eMSFlowObjInitial.getFlowContent());
	                 }
	            }
			} catch (Exception e) {
				e.printStackTrace();
				remark = e.getMessage(); 
				state=EMSTraceDataEnum.chulishibai.getValue();
				logger.error("EMS定时器查询临时表，模拟dmp相关操作执行异常!异常原因={}", e);
			}finally{
				eMSDAO.changeEmsFlowState(eMSFlowObjInitial.getId(),state,remark);
			}
		}
	}

}
