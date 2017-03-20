package cn.explink.pos.chinaums;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.ExptCodeJoint;
import cn.explink.b2c.tools.ExptReason;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.User;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.ExceptionCwbErrorTypeEnum;
import cn.explink.enumutil.ReasonTypeEnum;
import cn.explink.exception.CwbException;
import cn.explink.pos.chinaums.xml.Transaction;
import cn.explink.pos.tools.EmployeeInfo;
import cn.explink.pos.tools.PosEnum;
import cn.explink.util.DateTimeUtil;

@Service
public class ChinaUmsService_toExptFeedBack extends ChinaUmsService {
	private Logger logger = LoggerFactory.getLogger(ChinaUmsService_toExptFeedBack.class);
	/**
	 * 异常反馈通知接口
	 * @param request
	 * @param service_code
	 * @param jobject
	 * @param yeePay
	 * @return
	 */
	public String toExceptionFeedBack(Transaction rootnote, ChinaUms chinaUms){
		ChinaUmsRespNote chinaUmsRespNote=new ChinaUmsRespNote();
		try {
		    EmployeeInfo employee=new EmployeeInfo();
		    employee=jiontDAO.getEmployeeInfo(rootnote.getTransaction_Header().getEmployno());
			if (employee == null) {
			        rootnote.getTransaction_Header().setResponse_code( ChinaUmsExptMessageEnum.NoUserName.getResp_code());
			        rootnote.getTransaction_Header().setResponse_msg(ChinaUmsExptMessageEnum.NoUserName.getResp_msg());
				return chinaUmsService_public.createXML_toExptFeedBack(chinaUms, rootnote);
			}
			chinaUmsRespNote=super.BuildChinaumsRespClassAndSign(rootnote);
			long deliverystate=getDeliveryByReasonType(rootnote.getTransaction_Body().getBadtype(),rootnote.getTransaction_Body().getErrorcode(),chinaUms); //根据异常码判断
			
			if(chinaUmsRespNote.getCwbOrder()==null){
				chinaUmsRespNote.setResp_code(ChinaUmsExptMessageEnum.ChaXunYiChang.getResp_code());
				chinaUmsRespNote.setResp_msg(ChinaUmsExptMessageEnum.ChaXunYiChang.getResp_msg());
				logger.error("chinaums运单异常反馈,没有检索到数据"+chinaUmsRespNote.getOrder_no()+",小件员："+chinaUmsRespNote.getDelivery_man());
			}
			else if(chinaUmsRespNote.getDeliverstate()!=null&&chinaUmsRespNote.getDeliverstate().getDeliverystate()!=0){
				chinaUmsRespNote.setResp_code(ChinaUmsExptMessageEnum.QiTaShiBai.getResp_code());
				chinaUmsRespNote.setResp_msg(ChinaUmsExptMessageEnum.QiTaShiBai.getResp_msg()+",不可重复反馈");
				logger.error("chinaums运单异常反馈,订单反馈过，不可重复反馈,单号："+chinaUmsRespNote.getOrder_no()+",小件员："+chinaUmsRespNote.getDelivery_man());
			}else if(deliverystate==-1){
				chinaUmsRespNote.setResp_code(ChinaUmsExptMessageEnum.QiTaShiBai.getResp_code());
				chinaUmsRespNote.setResp_msg(ChinaUmsExptMessageEnum.QiTaShiBai.getResp_msg()+"无法识别此异常码");
				logger.error("chinaums异常反馈失败,无法识别此编码:["+rootnote.getTransaction_Body().getErrorcode()+"]，单号："+chinaUmsRespNote.getOrder_no()+",小件员："+chinaUmsRespNote.getDelivery_man());
			}else{
			    chinaUmsRespNote.setEx_code(rootnote.getTransaction_Body().getErrorcode());
				chinaUmsRespNote.setEx_desc(rootnote.getTransaction_Body().getMemo());
				chinaUmsRespNote.setDelivery_man(rootnote.getTransaction_Header().getEmployno());
				chinaUmsRespNote=ExcuteCwbExptFeedBackHandler(chinaUmsRespNote,deliverystate,rootnote,chinaUms);
			}
			
		}catch(Exception e){
			logger.error("异常反馈-系统遇到不可预知的异常!异常原因:",e);
			e.printStackTrace();
		}


	
		//生成返回的xml字符串
		Map<String,String> respMap=convertMapType_toExptFeedBack(chinaUmsRespNote, chinaUms,rootnote);
		String responseXml = ChinaUmsXMLHandler.createXMLMessage_toExptFeedBack(respMap);
		logger.info("返回chinaums数据成功。-异常反馈-业务编码={},返回XML={}",rootnote.getTransaction_Header().getTranstype(),responseXml);

		return responseXml;
	}


	private ChinaUmsRespNote ExcuteCwbExptFeedBackHandler(ChinaUmsRespNote chinaUmsRespNote,long delivery_state,Transaction rootnote,ChinaUms chinaums) {
			long deliverystate=delivery_state;
			long backreasonid=0;
			long leavedreasonid=0;
			long firstlevelreasonid=0;
			
			
			 if (((rootnote.getTransaction_Body().getBadtype() != null) && (!rootnote.getTransaction_Body().getBadtype().isEmpty())) || (chinaums.getVersion() == 1))
			 {
				ExptCodeJoint exptCodeJoint=exptcodeJointDAO.getExpMatchListByPosCode(chinaUmsRespNote.getEx_code(),PosEnum.ChinaUms.getKey());
				if(deliverystate==DeliveryStateEnum.FenZhanZhiLiu.getValue()){
					
					if(exptCodeJoint!=null&&exptCodeJoint.getReasonid()!=0){
						leavedreasonid=(exptcodeJointDAO.getExpMatchListByPosCode(chinaUmsRespNote.getEx_code(),PosEnum.ChinaUms.getKey())).getReasonid();
						firstlevelreasonid=this.reasonDao.getReasonByReasonid(leavedreasonid).getParentid();
					}else{
						chinaUmsRespNote.setResp_code(ChinaUmsExptMessageEnum.QiTaShiBai.getResp_code());
						chinaUmsRespNote.setResp_msg(ChinaUmsExptMessageEnum.QiTaShiBai.getResp_msg()+"无法识别此异常码");
						return chinaUmsRespNote;
					}
				}else if(deliverystate==DeliveryStateEnum.JuShou.getValue()){
					if(exptCodeJoint!=null&&exptCodeJoint.getReasonid()!=0){
						backreasonid=(exptcodeJointDAO.getExpMatchListByPosCode(chinaUmsRespNote.getEx_code(),PosEnum.ChinaUms.getKey())).getReasonid();
					}else{
						chinaUmsRespNote.setResp_code(ChinaUmsExptMessageEnum.QiTaShiBai.getResp_code());
						chinaUmsRespNote.setResp_msg(ChinaUmsExptMessageEnum.QiTaShiBai.getResp_msg()+"无法识别此异常码");
						return chinaUmsRespNote;
						
					}
				}
			}
			
		
			
			
			
			try {
				chinaUmsRespNote.setBranchid(userDAO.getUserByUsername(chinaUmsRespNote.getDelivery_man()).getBranchid());
				Map<String,Object> parameters = new HashMap<String,Object>();
				parameters.put("deliverid",chinaUmsRespNote.getDeliverid());
				parameters.put("podresultid", deliverystate);
				parameters.put("backreasonid",backreasonid);
				parameters.put("leavedreasonid",leavedreasonid);
				parameters.put("firstlevelreasonid",leavedreasonid);
				parameters.put("receivedfeecash",BigDecimal.ZERO);
				parameters.put("receivedfeepos",BigDecimal.ZERO);
				parameters.put("receivedfeecheque",BigDecimal.ZERO);
				parameters.put("receivedfeeother",BigDecimal.ZERO);
				parameters.put("paybackedfee",BigDecimal.ZERO);
				parameters.put("podremarkid",0l);
				parameters.put("posremark","POS反馈");
				parameters.put("checkremark","");
				parameters.put("deliverstateremark",chinaUmsRespNote.getEx_desc());
				parameters.put("owgid",0);
				parameters.put("sessionbranchid",chinaUmsRespNote.getBranchid());
				parameters.put("sessionuserid", chinaUmsRespNote.getDeliverid());
				parameters.put("sign_typeid",0);
				parameters.put("sign_man","");
				parameters.put("sign_time","");
				cwbOrderService.deliverStatePod(getUser(chinaUmsRespNote.getDeliverid()), chinaUmsRespNote.getOrder_no(),chinaUmsRespNote.getOrder_no(),parameters);
				chinaUmsRespNote.setResp_code(ChinaUmsExptMessageEnum.Success.getResp_code());
				chinaUmsRespNote.setResp_msg(ChinaUmsExptMessageEnum.Success.getResp_msg());
			} catch (CwbException e1) {
				CwbOrder cwbOrder = cwbDAO.getCwbByCwb(chinaUmsRespNote.getOrder_no());
				User user = getUser(chinaUmsRespNote.getDeliverid());
				exceptionCwbDAO.createExceptionCwb(chinaUmsRespNote.getOrder_no(), e1.getFlowordertye(), e1.getMessage(), user.getBranchid(), user.getUserid(), cwbOrder==null?0:cwbOrder.getCustomerid(), 0, 0, 0, "");
				
				if(e1.getError().getValue()==ExceptionCwbErrorTypeEnum.YI_CHANG_DAN_HAO.getValue()){
					logger.error("chinaUms异常反馈异常,没有检索到数据"+chinaUmsRespNote.getOrder_no()+",小件员："+chinaUmsRespNote.getDelivery_man(),e1);
					chinaUmsRespNote.setResp_code("30");
					chinaUmsRespNote.setResp_msg("没有检索到数据");
				}else if(e1.getError().getValue()==ExceptionCwbErrorTypeEnum.BU_SHI_ZHE_GE_XIAO_JIAN_YUAN_DE_HUO.getValue()){
					logger.error("chinaUms异常反馈异常,不是此小件员的货"+chinaUmsRespNote.getOrder_no()+",当前小件员："+chinaUmsRespNote.getDelivery_man()+e1);
					chinaUmsRespNote.setResp_code("40");
					chinaUmsRespNote.setResp_msg("不是当前小件员的货不能登记快件");
				}
			}

		return chinaUmsRespNote;
	}


	private Map<String, String> convertMapType_toExptFeedBack(ChinaUmsRespNote chinaUmsRespNote,ChinaUms chinaUms,Transaction rootnote)
	{
	    Map<String, String> retMap = new HashMap<String, String>();
	    // 放入map
	    retMap.put("version", rootnote.getTransaction_Header().getVersion());
	    retMap.put("transtype", rootnote.getTransaction_Header().getTranstype());
	    retMap.put("employno", rootnote.getTransaction_Header().getEmployno());
	    retMap.put("termid", rootnote.getTransaction_Header().getTermid());
	    retMap.put("response_time", DateTimeUtil.getNowTimeNo());
	    retMap.put("response_code", chinaUmsRespNote.getResp_code());
	    retMap.put("response_msg", chinaUmsRespNote.getResp_msg());

		// 生成待加密的字符串
		String str = ChinaUmsXMLHandler.createMACXML_cwbSign(retMap);
		String r=CreateRespSign(chinaUms, str);
		retMap.put("mac", r.toUpperCase());
		return retMap;
	}


	/**
	 * badtype 异常类型
	 * errcode 异常码
	 * 保证兼容银联POS机
	 * @param errcode
	 * @return
	 */
	private int getDeliveryByReasonType(String badtype,String errcode, ChinaUms chinaUms){
		int deliverystate=-1;
		if(chinaUms.getVersion()==2){ //安达信版本，直接使用errcode判断是滞留还是拒收
			ExptReason exptReason =exptReasonDAO.getExptReasonCodeByPos(errcode, PosEnum.ChinaUms.getKey());
			if(exptReason!=null){
				int type = exptReason.getExpt_type();
				if(ReasonTypeEnum.BeHelpUp.getValue()==type){
					return DeliveryStateEnum.FenZhanZhiLiu.getValue();
				}
				if(ReasonTypeEnum.ReturnGoods.getValue()==type){
					return DeliveryStateEnum.JuShou.getValue();
				}
			}
		}
		
		if (chinaUms.getVersion() == 1) {
		      return DeliveryStateEnum.FenZhanZhiLiu.getValue();
		}
		
		if(badtype==null||badtype.isEmpty()){
			if("01".equals(errcode)){
				deliverystate=DeliveryStateEnum.JuShou.getValue();
			}else if("02".equals(errcode)){
				deliverystate=DeliveryStateEnum.FenZhanZhiLiu.getValue();
			}
		}else{
			if("01".equals(badtype)){
				deliverystate=DeliveryStateEnum.JuShou.getValue();
			}else if("02".equals(badtype)){
				deliverystate=DeliveryStateEnum.FenZhanZhiLiu.getValue();
			}
		}
		
		

		return deliverystate;
	}	
	

	
	

}
