package cn.explink.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.explink.b2c.tools.ExptCodeJoint;
import cn.explink.b2c.tools.ExptCodeJointDAO;
import cn.explink.b2c.tools.ExptReasonDAO;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.tools.RestHttpServiceHanlder;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliverPaymentPrintVo;
import cn.explink.domain.DeliverPaymentReportVo;
import cn.explink.domain.DeliveryPayment;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.User;
import cn.explink.domain.VO.DeliverServerParamVO;
import cn.explink.domain.VO.DeliverServerPullVO;
import cn.explink.domain.VO.DeliveryPosNotifyVO;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.DeliverServerPayEnum;
import cn.explink.enumutil.DeliverServerResultEnum;
import cn.explink.enumutil.DeliveryPaymentPatternEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.pos.tools.PosEnum;
import cn.explink.pos.tools.SignTypeEnum;
import cn.explink.util.DigestsEncoder;
import cn.explink.util.JsonUtil;
import cn.explink.util.StringUtil;

@Service
public class DeliverService {
	private Logger logger =LoggerFactory.getLogger(this.getClass());
	@Autowired
	private JiontDAO jiontDAO;
	@Autowired
	private ExptReasonDAO exptReasonDAO;
	@Autowired
	ExptCodeJointDAO exptcodeJointDAO;
	
	@Autowired
	private DeliveryStateDAO deliveryStateDAO;
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private CustomerDAO customerDAO;
	
	@Autowired
	private CwbDAO cwbDAO;
	
	/**
	 * 构建派件服务参数VO
	 * @param key
	 * @return
	 */
	public DeliverServerParamVO getDeliverServerParamVO(int key) {
		JointEntity obj = jiontDAO.getJointEntity(key);
		if (obj == null || obj.getJoint_property() == null || obj.getState() == 0 ) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(obj.getJoint_property());
		DeliverServerParamVO deliverServerParamVO = (DeliverServerParamVO) JSONObject.toBean(jsonObj, DeliverServerParamVO.class);
		return deliverServerParamVO;
	}
	

	public Object getDeliverServerParamVOForSet(int key) {
		JointEntity obj = jiontDAO.getJointEntity(key);
		if (obj == null || obj.getJoint_property() == null ) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(obj.getJoint_property());
		DeliverServerParamVO deliverServerParamVO = (DeliverServerParamVO) JSONObject.toBean(jsonObj, DeliverServerParamVO.class);
		return deliverServerParamVO;
	}

	public void update(int joint_num, int state) {
		jiontDAO.UpdateState(joint_num, state);
	}
	
	/**
	 * 修改派件服务paramVO对象
	 * @param request
	 * @param joint_num
	 */
	public void edit(HttpServletRequest request, int joint_num) {

		DeliverServerParamVO deliverServerParamVO = new DeliverServerParamVO();
		deliverServerParamVO.setCode(request.getParameter("code"));
		deliverServerParamVO.setDeliverServerPushUrl(request.getParameter("deliverServerPushUrl"));
		deliverServerParamVO.setDeliverSyncPushUrl(request.getParameter("deliverSyncPushUrl"));
		deliverServerParamVO.setToken(request.getParameter("token"));
		deliverServerParamVO.setTradeNum(StringUtil.isEmpty(request.getParameter("tradeNum"))?1:Integer.valueOf(request.getParameter("tradeNum")));
		deliverServerParamVO.setDelivery_company_code(request.getParameter("delivery_company_code"));
		deliverServerParamVO.setDeliverPosSynUrl( request.getParameter("deliverPosSynUrl"));
		
		JSONObject jsonObj = JSONObject.fromObject(deliverServerParamVO);
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
	
	/**
	 * 更新 paramVO
	 * @param paramVO
	 * @param joint_num
	 */
	public void updateTradeNum( DeliverServerParamVO paramVO , int joint_num){
		
		DeliverServerParamVO deliverServerParamVO = new DeliverServerParamVO();
		deliverServerParamVO.setCode(paramVO.getCode());
		deliverServerParamVO.setDeliverServerPushUrl(paramVO.getDeliverServerPushUrl());
		deliverServerParamVO.setDeliverSyncPushUrl(paramVO.getDeliverSyncPushUrl());
		deliverServerParamVO.setToken(paramVO.getToken());
		deliverServerParamVO.setTradeNum(paramVO.getTradeNum()+1);
		deliverServerParamVO.setDelivery_company_code(paramVO.getDelivery_company_code());
		deliverServerParamVO.setDeliverPosSynUrl(paramVO.getDeliverPosSynUrl());
		
		JSONObject jsonObj = JSONObject.fromObject(deliverServerParamVO);
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
	
	/**
	 * 构建派送反馈结果参数Map
	 * @param user 
	 * @param co 
	 * @param pullVO
	 * @param paramVO
	 * @return
	 */
	public Map<String,Object> buildBusinessParam(User user, CwbOrder co, DeliverServerPullVO pullVO , DeliverServerParamVO paramVO){
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		//参数构建
		Long deliveryid = user.getUserid();//派送员id
		Long podresultid = this.getDeliveryState(pullVO,co);//配送结果
		Long podremarkid = Long.valueOf(0);//配送结果备注id，非必填
		Map<String, BigDecimal> feeMap = new HashMap<String, BigDecimal>();
		feeMap.put("returnedfee", BigDecimal.ZERO);//退换现金
		feeMap.put("receivedfeecash", BigDecimal.ZERO);//现金实收
		feeMap.put("receivedfeepos", BigDecimal.ZERO);//POS刷卡实收
		feeMap.put("receivedfeecodpos", BigDecimal.ZERO);//支付宝实收
		feeMap.put("receivedfeecheque", BigDecimal.ZERO);//支票实收
		feeMap.put("receivedfeeother", BigDecimal.ZERO);//其他实收
		String posremark = "";//POS备注
		String checkremark = "";//支票号备注
		Long backreasonid = 0l;//拒收原因
		Long leavedreasonid = 0l;//滞留原因
		if(podresultid==DeliveryStateEnum.FenZhanZhiLiu.getValue()){
			leavedreasonid=Long.valueOf(pullVO.getFail_code());
		}
		if(podresultid==DeliveryStateEnum.JuShou.getValue()||podresultid==DeliveryStateEnum.ShangMenJuTui.getValue()){
			backreasonid= Long.valueOf(pullVO.getFail_code());
		}
		
		Long weishuakareasonid = Long.valueOf(0); //未刷卡原因
		Long losereasonid = Long.valueOf(0);//货物丢失原因
		String deliverytime_now = pullVO.getTime_stamp();//真实反馈时间
		String sign_man = co.getConsigneename();//实际签收人姓名
		String sign_man_phone = StringUtil.isEmpty(co.getConsigneemobile())?(StringUtil.isEmpty(co.getConsigneephone())?"***********":co.getConsigneephone()):co.getConsigneemobile();//实际签收人手机
		String sign_time = pullVO.getTime_stamp();//签收时间
		Integer sign_typeid = pullVO.getSign_code().intValue() == 1?SignTypeEnum.BenRenQianShou.getValue():SignTypeEnum.TaRenDaiQianShou.getValue();
		String deliverstateremark = "【棒棒糖派件服务APP-推送派送结果】";//反馈备注输入内容
		
		this.buildReceivedFee(feeMap,pullVO,co);
		
		parameters.put("deliverid", deliveryid);
		parameters.put("podresultid", podresultid);
		parameters.put("backreasonid", backreasonid);
		parameters.put("leavedreasonid", leavedreasonid);
		parameters.put("receivedfeecash", feeMap.get("receivedfeecash"));
		parameters.put("receivedfeepos", feeMap.get("receivedfeepos"));
		parameters.put("receivedfeecodpos",feeMap.get("receivedfeecodpos"));
		parameters.put("receivedfeecheque", feeMap.get("receivedfeecheque"));
		parameters.put("receivedfeeother", feeMap.get("receivedfeeother"));
		parameters.put("paybackedfee", feeMap.get("returnedfee"));
		parameters.put("podremarkid", podremarkid);
		parameters.put("posremark", posremark);
		parameters.put("checkremark", checkremark);
		parameters.put("deliverstateremark", deliverstateremark);
		parameters.put("owgid", 0);
		parameters.put("sessionbranchid", user.getBranchid());//机构？？？
		parameters.put("sessionuserid", user.getUserid());//操作人？？？
		parameters.put("sign_typeid",sign_typeid);//本人他人签收？？？
		parameters.put("sign_man", sign_man);//签收人？？？
		parameters.put("sign_time", sign_time);
		parameters.put("weishuakareasonid", weishuakareasonid);
		parameters.put("losereasonid", losereasonid);
		parameters.put("deliverytime_now", deliverytime_now);
		parameters.put("sign_man_phone", sign_man_phone);
		parameters.put("resendtime", pullVO.getD_time());
		parameters.put("zhiliuremark", pullVO.getD_address());
		return parameters;
	}

	/**
	 * 构建应收金额
	 * @param receivedfeecash
	 * @param receivedfeepos
	 * @param receivedfeecodpos
	 * @param receivedfeecheque
	 * @param receivedfeeother
	 * @param pullVO
	 */
	private void buildReceivedFee(Map<String,BigDecimal> feeMap, DeliverServerPullVO pullVO, CwbOrder co) {
		
		if( CwbOrderTypeIdEnum.Peisong.getValue() == co.getCwbordertypeid()){
			if(pullVO.getCod().compareTo(Integer.valueOf(0)) == -1){
				return;
			}else if( DeliverServerPayEnum.ALIPAY.getIndex().equals(pullVO.getPayment_means())){
				feeMap.put("receivedfeecodpos", this.transitFee(pullVO.getCod()));
			}else if( DeliverServerPayEnum.CASH.getIndex().equals(pullVO.getPayment_means())){
				feeMap.put("receivedfeecash", this.transitFee(pullVO.getCod()));
			}else if( DeliverServerPayEnum.POS.getIndex().equals(pullVO.getPayment_means())){
				feeMap.put("receivedfeepos", this.transitFee(pullVO.getCod()));
			}else if( DeliverServerPayEnum.PAID.getIndex().equals(pullVO.getPayment_means())){
				feeMap.put("returnedfee", BigDecimal.ZERO);
				feeMap.put("receivedfeecash", BigDecimal.ZERO);
				feeMap.put("receivedfeepos", BigDecimal.ZERO);
				feeMap.put("receivedfeecheque", BigDecimal.ZERO);
				feeMap.put("receivedfeeother", BigDecimal.ZERO);
				feeMap.put("receivedfeecodpos", BigDecimal.ZERO);
			}
		}else{
			if(pullVO.getCod().compareTo(Integer.valueOf(0)) == -1){
				feeMap.put("returnedfee", this.transitFee(pullVO.getCod()));
			}else{
				if( DeliverServerPayEnum.ALIPAY.getIndex().equals(pullVO.getPayment_means())){
					feeMap.put("receivedfeecodpos", this.transitFee(pullVO.getCod()));
				}else if( DeliverServerPayEnum.CASH.getIndex().equals(pullVO.getPayment_means())){
					feeMap.put("receivedfeecash", this.transitFee(pullVO.getCod()));
				}else if( DeliverServerPayEnum.POS.getIndex().equals(pullVO.getPayment_means())){
					feeMap.put("receivedfeepos", this.transitFee(pullVO.getCod()));
				}else if( DeliverServerPayEnum.PAID.getIndex().equals(pullVO.getPayment_means())){
					//TODO已付款 不合理
					feeMap.put("receivedfeecash", this.transitFee(pullVO.getCod()));
				}
			}
		}	
	}

	/**
	 * 构建滞留原因
	 * @param pullVO
	 * @return
	 */
	private Long getLeavedReasonId(DeliverServerPullVO pullVO) {

		if(DeliverServerResultEnum.HOLDUP.getIndex().equals(pullVO.getDelivery_result())){
			ExptCodeJoint targetReason = exptcodeJointDAO.getExpMatchListByPosCode(pullVO.getFail_code(),PosEnum.DeliverServerAPP.getKey());
			if(targetReason != null){
				return targetReason.getReasonid(); 
			}
		}
		return Long.valueOf(0);
	}

	/**
	 * 构建拒收原因
	 * @param pullVO
	 * @return
	 */
	private Long getBackReasonId(DeliverServerPullVO pullVO) {
		
		if(DeliverServerResultEnum.REFUSE.getIndex().equals(pullVO.getDelivery_result())){
			
			ExptCodeJoint targetReason = exptcodeJointDAO.getExpMatchListByPosCode(pullVO.getFail_code(),PosEnum.DeliverServerAPP.getKey());
			if(targetReason != null){
				return targetReason.getReasonid(); 
			}
		}
		return Long.valueOf(0);
	}

	/**
	 * 构建配送结果
	 * @param co 
	 * @param delivery_result
	 */
	private Long getDeliveryState(DeliverServerPullVO pullVO, CwbOrder co) {
		
		//成功
		if(DeliverServerResultEnum.SUCCESS.getIndex().equals(pullVO.getDelivery_result())){
			if( CwbOrderTypeIdEnum.Peisong.getValue() == co.getCwbordertypeid()){
				return Long.valueOf(DeliveryStateEnum.PeiSongChengGong.getValue());
			}else if( CwbOrderTypeIdEnum.Shangmentui.getValue() == co.getCwbordertypeid()){
				return Long.valueOf(DeliveryStateEnum.ShangMenTuiChengGong.getValue());
			}else if( CwbOrderTypeIdEnum.Shangmenhuan.getValue() == co.getCwbordertypeid()){
				return Long.valueOf(DeliveryStateEnum.ShangMenHuanChengGong.getValue());
			}
		}else
		//收件人拒收
		if(DeliverServerResultEnum.REFUSE.getIndex().equals(pullVO.getDelivery_result())){
			if( CwbOrderTypeIdEnum.Peisong.getValue() == co.getCwbordertypeid() || CwbOrderTypeIdEnum.Shangmenhuan.getValue() == co.getCwbordertypeid() ){
				return Long.valueOf(DeliveryStateEnum.JuShou.getValue());
			}else if( CwbOrderTypeIdEnum.Shangmentui.getValue() == co.getCwbordertypeid() ){
				return Long.valueOf(DeliveryStateEnum.ShangMenJuTui.getValue());
			}
		}else
		//回复归班前状态
		if(DeliverServerResultEnum.REVERSE.getIndex().equals(pullVO.getDelivery_result())){
			return Long.valueOf(DeliveryStateEnum.ZhiLiuZiDongLingHuo.getValue());
		}else
		//滞留
		if(DeliverServerResultEnum.HOLDUP.getIndex().equals(pullVO.getDelivery_result())){
			return Long.valueOf(DeliveryStateEnum.FenZhanZhiLiu.getValue());
		}
		return Long.valueOf(DeliveryStateEnum.WeiFanKui.getValue());
	}

	/**
	 * 金额 分 ——> 元 转换
	 * @param fee
	 * @return
	 */
	private BigDecimal transitFee(Integer fee){
		BigDecimal result = BigDecimal.ZERO;
		if(null != fee){
			result = new BigDecimal(fee).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
		}
		return result;
	}
	
	
	
	/**
	 * POS机刷卡的订单需要通知棒棒糖App
	 */
	public void posFeedbackNotifyApp(String notifyCwb){
		try {
			DeliverServerParamVO dVo = (DeliverServerParamVO)this.getDeliverServerParamVOForSet(PosEnum.DeliverServerAPP.getKey());
			DeliveryPosNotifyVO notifyVO = new DeliveryPosNotifyVO();
			notifyVO.setCode(dVo.getCode());
			notifyVO.setDelivery_company_code(dVo.getDelivery_company_code());
			notifyVO.setMail_num(notifyCwb);
			notifyVO.setSign(createSign(dVo, notifyVO));
	
			String jsonStr = JsonUtil.translateToJson(notifyVO);
			logger.info("派件服务-POS刷卡通知：{}，cwb={}",jsonStr,notifyCwb);
			
			String responseJson = RestHttpServiceHanlder.sendHttptoServer_Json(jsonStr, dVo.getDeliverPosSynUrl());
			
			logger.info("派件服务-POS刷卡通知返回：{}，cwb={}",responseJson,notifyCwb);
			

		} catch (Exception e) {
			logger.error("POS支付结果通知App异常,cwb={}"+notifyCwb,e);
		} 
		
		
	}


	private String createSign(DeliverServerParamVO dVo,
			DeliveryPosNotifyVO notifyVO) {
		Map<String,String> paramMap=new HashMap<String, String>();
		paramMap.put("delivery_company_code",notifyVO.getDelivery_company_code());
		paramMap.put("mail_num",notifyVO.getMail_num());
		
		String linkString = createLinkString(paramMap);
		logger.info("pos通知-签名字符串:{},完整内容：{}",linkString,(linkString + "&" + dVo.getToken()));
		String sign = DigestsEncoder.encode("SHA1", linkString + "&" + dVo.getToken());
		return sign;
	}
	
	/** 
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params) {

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        String prestr = "";

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);

            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }

        return prestr;
    }
    @SuppressWarnings("rawtypes")
	private boolean isSuccessPush(String result) throws JsonParseException, JsonMappingException, IOException {
		boolean flag = false;
		Map resultMap = JsonUtil.readValue(result,Map.class);
		if("ok".equals(resultMap.get("result"))){
			flag = true;
		}
		return flag;
	}
    
    /**
     * 查询交款单
     * @author chunlei05.li
     * @date 2016年8月29日 下午3:31:47
     * @param deliveryId
     * @param auditingtimeStart
     * @param auditingtimeEnd
     * @param paymentType
     * @return
     */
    public List<DeliverPaymentReportVo> getDeliverPaymentQueryList(long deliveryId, String auditingtimeStart,
			String auditingtimeEnd, int paymentType) {
    	// 查询归班反馈
    	List<DeliveryPayment> deliveryPaymentList = this.deliveryStateDAO.getDeliveryPaymentList(deliveryId, auditingtimeStart, auditingtimeEnd, paymentType);
    	return this.getDeliverPaymentPrintVoList(deliveryId, deliveryPaymentList);
    }
    
    /**
     * 查询交款单打印信息
     * @author chunlei05.li
     * @date 2016年8月29日 下午3:34:26
     * @param customerIds
     * @param cwbOrderTypeIds
     * @param deliveryPaymentPatternIds
     * @return
     */
	public List<DeliverPaymentPrintVo> getDeliverPaymentPrintList(long deliveryId, long[] customerIds,
			int[] cwbOrderTypeIds, int[] deliveryPaymentPatternIds, String auditingtimeStart, String auditingtimeEnd) {
		// 查询归班反馈
		List<DeliveryPayment> deliveryPaymentList = this.deliveryStateDAO.getDeliveryPaymentList(deliveryId,
				customerIds, cwbOrderTypeIds, deliveryPaymentPatternIds, auditingtimeStart, auditingtimeEnd);
		List<DeliverPaymentReportVo> deliverPaymentPrintVoList = this.getDeliverPaymentPrintVoList(deliveryId, deliveryPaymentList);
		// 打印Map，保证排序
		// deliverPaymentPrintVoList已排序
		Map<Integer, DeliverPaymentPrintVo> printMap = new LinkedHashMap<Integer, DeliverPaymentPrintVo>();
		for (DeliverPaymentReportVo reportVo : deliverPaymentPrintVoList) {
			DeliveryPaymentPatternEnum paymentPattern = DeliveryPaymentPatternEnum .getByPayno(reportVo.getDeliveryPaymentPatternId());
			DeliverPaymentPrintVo printVo = printMap.get(paymentPattern.getPayno());
			if (printVo == null) {
				printVo = new DeliverPaymentPrintVo();
				printVo.setDeliveryPayment(paymentPattern);
				printVo.setDeliverPaymentReportVoList(new ArrayList<DeliverPaymentReportVo>());
			}
			printVo.getDeliverPaymentReportVoList().add(reportVo);
			printVo.setOrderCount(printVo.getOrderCount() + reportVo.getOrderCount());
			printVo.setRealTotal(printVo.getRealTotal().add(reportVo.getRealTotal()));
			printVo.setShouldTotal(printVo.getShouldTotal().add(reportVo.getShouldTotal()));
			printMap.put(paymentPattern.getPayno(), printVo);
		}
		List<DeliverPaymentPrintVo> printVoList = new ArrayList<DeliverPaymentPrintVo>(printMap.values());
		return printVoList;
	}
	
    /**
     * 交款单转VO
     * @author chunlei05.li
     * @date 2016年8月29日 下午3:31:59
     * @param deliveryId
     * @param deliveryPaymentList
     * @return
     */
	public List<DeliverPaymentReportVo> getDeliverPaymentPrintVoList(long deliveryId,
			List<DeliveryPayment> deliveryPaymentList) {
		// 获取小件员信息
    	User delivery = this.userDAO.getUserByUserid(deliveryId);
		// 查询供货商，转Map
		List<Customer> customerList = this.customerDAO.getAllCustomers();
		Map<Long, String> customernameMap = new HashMap<Long, String>();
		for (Customer customer : customerList) {
			customernameMap.put(customer.getCustomerid(), customer.getCustomername());
		}
    	// 归类
		Map<String, DeliverPaymentReportVo> paymentVoMap = new HashMap<String, DeliverPaymentReportVo>();
		for (DeliveryPayment dp : deliveryPaymentList) {
			DeliveryState ds = dp.getDeliveryState();
			CwbOrder cwb = dp.getCwbOrder();
			// 归类支付类型，根据产品确认，武汉不存在一个订单只存在一种支付类型的订单
			// 若存在一个订单存在多种支付类型，则统计表报会有误差
			DeliveryPaymentPatternEnum paymentPattern;
			if (ds.getCash().compareTo(BigDecimal.ZERO) > 0) {
				paymentPattern = DeliveryPaymentPatternEnum.CASH;
			} else if (ds.getPos().compareTo(BigDecimal.ZERO) > 0) {
				paymentPattern = DeliveryPaymentPatternEnum.POS;
			} else if (ds.getCheckfee().compareTo(BigDecimal.ZERO) > 0) {
				paymentPattern = DeliveryPaymentPatternEnum.CHECKFEE;
			} else if (ds.getCodpos().compareTo(BigDecimal.ZERO) > 0) {
				paymentPattern = DeliveryPaymentPatternEnum.CODPOS;
			} else if (ds.getOtherfee().compareTo(BigDecimal.ZERO) > 0) {
				paymentPattern = DeliveryPaymentPatternEnum.OTHERFEE;
			} else {
				paymentPattern = DeliveryPaymentPatternEnum.CASH;
			}
			// 组建key，根据支付类型 + 客户 + 订单类型分类
			String key = paymentPattern.getPayno() + "_" + ds.getCustomerid() + "_" + ds.getCwbordertypeid();
			DeliverPaymentReportVo paymentVo = paymentVoMap.get(key);
			if (paymentVo == null) {
				paymentVo = new DeliverPaymentReportVo();
				paymentVo.setDeliveryId(delivery.getUserid());
				paymentVo.setDeliveryName(delivery.getRealname());
				paymentVo.setDeliveryPaymentPatternId(paymentPattern.getPayno());
				paymentVo.setDeliveryPaymentPattern(paymentPattern.getPayname());
				paymentVo.setCustomerId(ds.getCustomerid());
				paymentVo.setCustomerName(customernameMap.get(ds.getCustomerid()));
				CwbOrderTypeIdEnum cwbOrderTypeIdEnum = CwbOrderTypeIdEnum.getByValue(ds.getCwbordertypeid());
				paymentVo.setCwbOrderTypeId(cwbOrderTypeIdEnum == null ? 0 : cwbOrderTypeIdEnum.getValue());
				paymentVo.setCwbOrderType(cwbOrderTypeIdEnum == null ? "" : cwbOrderTypeIdEnum.getText());
			}
			// 目前系统不支持快递到付，所以不统计快递的应收运费
			if (ds.getCwbordertypeid() == CwbOrderTypeIdEnum.Express.getValue()) {
				ds.setShouldfare(BigDecimal.ZERO);
			}
			// 订单数量
			paymentVo.setOrderCount(paymentVo.getOrderCount() + 1);
			// 应收金额
			paymentVo.setShouldReceivedfee(paymentVo.getShouldReceivedfee().add(cwb.getReceivablefee()));
			// 应退金额
			paymentVo.setShouldPaybackfee(paymentVo.getShouldPaybackfee().add(cwb.getPaybackfee()));
			// 应收运费
			paymentVo.setShouldfare(paymentVo.getShouldfare().add(ds.getShouldfare()));
			// 应收合计 = 应收金额 - 应退金额 + 应收运费
			BigDecimal shouldTotal = paymentVo.getShouldReceivedfee().subtract(paymentVo.getShouldPaybackfee()).add(paymentVo.getShouldfare());
			paymentVo.setShouldTotal(shouldTotal);
			// 实收合计 = 实收金额 - 实退金额 + 实收运费
			BigDecimal realTotal = ds.getReceivedfee().subtract(ds.getReturnedfee()).add(ds.getInfactfare());
			paymentVo.setRealTotal(paymentVo.getRealTotal().add(realTotal));
			paymentVoMap.put(key, paymentVo);
		}
		List<DeliverPaymentReportVo> voList = new ArrayList<DeliverPaymentReportVo>(paymentVoMap.values());
		// 排序
		Collections.sort(voList, new Comparator<DeliverPaymentReportVo>() {
			@Override
			public int compare(DeliverPaymentReportVo dp1, DeliverPaymentReportVo dp2) {
				// 先按支付方式排序
				if (dp1.getDeliveryPaymentPatternId() > dp2.getDeliveryPaymentPatternId()) {
					return 1;
				} else if (dp1.getDeliveryPaymentPatternId() < dp2.getDeliveryPaymentPatternId()) {
					return -1;
				}
				// 按订单类型排序
				if (dp1.getCwbOrderTypeId() > dp2.getCwbOrderTypeId()) {
					return 1;
				} else if (dp1.getCwbOrderTypeId() < dp2.getCwbOrderTypeId()) {
					return -1;
				}
				// 按客户排序
				if (dp1.getCustomerId() > dp2.getCustomerId()) {
					return 1;
				} else if (dp1.getCustomerId() < dp2.getCustomerId()) {
					return -1;
				}
				return 0;
			}
		});
		return voList;
    }
}
