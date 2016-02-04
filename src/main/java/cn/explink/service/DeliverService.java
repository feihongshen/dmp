package cn.explink.service;

import java.io.IOException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
import cn.explink.domain.CwbOrder;
import cn.explink.domain.User;
import cn.explink.domain.VO.DeliverServerParamVO;
import cn.explink.domain.VO.DeliverServerPullVO;
import cn.explink.domain.VO.DeliveryPosNotifyVO;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.DeliverServerPayEnum;
import cn.explink.enumutil.DeliverServerResultEnum;
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
	
}
