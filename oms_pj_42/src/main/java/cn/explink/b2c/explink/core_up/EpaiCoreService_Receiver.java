package cn.explink.b2c.explink.core_up;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.explink.code_down.EpaiExpEmum;
import cn.explink.b2c.explink.xmldto.CoreMarchal;
import cn.explink.b2c.explink.xmldto.OrderFlowDto;
import cn.explink.b2c.explink.xmldto.ReturnDto;
import cn.explink.b2c.tools.JacksonMapper;
import cn.explink.dao.CommonSendDataDAO;
import cn.explink.dao.GetDmpDAO;
import cn.explink.dao.WarehouseCommenDAO;
import cn.explink.domain.Common;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.MD5.MD5Util;

/**
 * 系统之间的对接（上游） 接收下游状态回传
 * 
 * @author Administrator
 *
 */

@Service
public class EpaiCoreService_Receiver {
	private Logger logger = LoggerFactory.getLogger(EpaiCoreService_Receiver.class);

	@Autowired
	WarehouseCommenDAO warehouseCommenDAO;
	@Autowired
	GetDmpDAO getDmpDAO;
	@Autowired
	CommonSendDataDAO commonSendDataDAO;

	public static List<Common> commonList;

	public static Map<Long, Long> chackMap = new HashMap<Long, Long>();

	public void initCommonList() {
		commonList = getDmpDAO.getAllCommons();
	}

	public List<Common> getCommonList() {
		if (chackMap.get(1l) == null) {
			chackMap.put(1l, 1l);
			commonList = getDmpDAO.getAllCommons();
			return commonList;
		} else {
			return commonList;
		}
	}

	/**
	 * 接收 订单回传给dmp
	 * 
	 * @throws JAXBException
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	public String cwbOrdersFeedback_Receiver(OrderFlowDto orderFlowDto) throws JAXBException, UnsupportedEncodingException {
		ReturnDto returnDto = new ReturnDto();

		try {

			String userCode = orderFlowDto.getUserCode();
			String requestTime = orderFlowDto.getRequestTime();
			String sign = orderFlowDto.getSign();
			String cwb = orderFlowDto.getCwb();

			try {
				validatorRequestParams(userCode, requestTime, sign, returnDto);
			} catch (RuntimeException e) {
				logger.error("验证下游请求业务逻辑异常", e);
				return CoreMarchal.Marchal_rep_feedback(returnDto);// 通过jaxb状态对象对XML传输
																	// 回传至下游订单详细信息
			}

			long isexistscwbflag = warehouseCommenDAO.getCountByCwb(cwb);
			if (isexistscwbflag == 0) {
				returnDto.setErrCode(EpaiExpEmum.CanShuCuoWu.getErrCode());
				returnDto.setErrMsg("订单" + cwb + "不存在");
				return CoreMarchal.Marchal_rep_feedback(returnDto);// 通过jaxb状态对象对XML传输
																	// 回传至下游订单详细信息
			}
			long own_flowordertype = getCommenFlowOrdertype(orderFlowDto.getFlowordertype()); // 转义flowordertype
			long own_deliveryState = getCommenDeliveryState(orderFlowDto.getDeliverystate());// //转义deliverystate

			// 验证是否有重发订单插入
			long isrepeatFlag = commonSendDataDAO.isExistsCwbFlag(cwb, orderFlowDto.getUserCode(), orderFlowDto.getOperatortime(), String.valueOf(own_flowordertype));
			if (isrepeatFlag > 0) {
				returnDto.setErrCode(EpaiExpEmum.CanShuCuoWu.getErrCode());
				returnDto.setErrMsg("订单" + cwb + "流程相同时间段存在重复");
				return CoreMarchal.Marchal_rep_feedback(returnDto);// 通过jaxb状态对象对XML传输
																	// 回传至下游订单详细信息
			}

			long branchid = getDMPCommon(orderFlowDto.getUserCode()).getBranchid();

			orderFlowDto.setFlowordertype(String.valueOf(own_flowordertype));
			orderFlowDto.setDeliverystate(String.valueOf(own_deliveryState));

			String dataJson = JacksonMapper.getInstance().writeValueAsString(orderFlowDto);

			// 插入上游OMS临时表
			commonSendDataDAO.creCommenSendData(cwb, branchid, orderFlowDto.getUserCode(), DateTimeUtil.getNowTime(), orderFlowDto.getOperatortime(), dataJson, own_deliveryState, own_flowordertype,
					orderFlowDto.getCustid());

			returnDto.setErrCode(EpaiExpEmum.Success.getErrCode());
			returnDto.setErrMsg(EpaiExpEmum.Success.getErrMsg());

			return CoreMarchal.Marchal_rep_feedback(returnDto);// 通过jaxb状态对象对XML传输
																// 回传至下游订单详细信息

		} catch (Exception e) {

			logger.error("上游处理下载订单程序未知异常", e);
			returnDto.setErrCode(EpaiExpEmum.XiTongYiChang.getErrCode());
			returnDto.setErrMsg(e.getMessage());
			return CoreMarchal.Marchal_rep_feedback(returnDto);// 通过jaxb状态对象对XML传输
																// 回传至下游订单详细信息

		}

	}

	private long getCommenFlowOrdertype(String request_code) {
		for (CommenFlowtypeEnum em : CommenFlowtypeEnum.values()) {
			if (em.getRequest_code().equals(request_code)) {
				if (em.getRequest_code().equals(CommenFlowtypeEnum.KuFangRuKu.getRequest_code())) {
					return FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue();
				}
				return em.getOwn_code();
			}
		}
		return 0;
	}

	// private long getCommenDeliveryState(String request_code,long
	// cwbordertypeid) {
	// if(request_code.equals(CommenDeliveryEnum.ChengGong.getRequest_code())){
	// if(cwbordertypeid==CwbOrderTypeIdEnum.Peisong.getValue()){
	// return DeliveryStateEnum.PeiSongChengGong.getValue();
	// }else if(cwbordertypeid==CwbOrderTypeIdEnum.Shangmenhuan.getValue()){
	// return DeliveryStateEnum.ShangMenHuanChengGong.getValue();
	// }else if(cwbordertypeid==CwbOrderTypeIdEnum.Shangmentui.getValue()){
	// return DeliveryStateEnum.ShangMenTuiChengGong.getValue();
	// }
	// }else
	// if(request_code.equals(CommenDeliveryEnum.JuShou.getRequest_code())){
	// if(cwbordertypeid==CwbOrderTypeIdEnum.Peisong.getValue()){
	// return DeliveryStateEnum.QuanBuTuiHuo.getValue();
	// }else if(cwbordertypeid==CwbOrderTypeIdEnum.Shangmenhuan.getValue()){
	// return DeliveryStateEnum.QuanBuTuiHuo.getValue();
	// }else if(cwbordertypeid==CwbOrderTypeIdEnum.Shangmentui.getValue()){
	// return DeliveryStateEnum.ShangMenJuTui.getValue();
	// }
	// }else
	// if(request_code.equals(CommenDeliveryEnum.FenZhanZhiLiu.getRequest_code())){
	// return DeliveryStateEnum.FenZhanZhiLiu.getValue();
	// }
	//
	// return 0;
	// }
	private long getCommenDeliveryState(String request_code) {
		if (request_code.equals(CommenDeliveryEnum.ChengGong.getRequest_code())) {

			return DeliveryStateEnum.PeiSongChengGong.getValue();
		} else if (request_code.equals(CommenDeliveryEnum.JuShou.getRequest_code())) {
			return DeliveryStateEnum.QuanBuTuiHuo.getValue();
		} else if (request_code.equals(CommenDeliveryEnum.FenZhanZhiLiu.getRequest_code())) {
			return DeliveryStateEnum.FenZhanZhiLiu.getValue();
		}

		return 0;
	}

	private void validatorRequestParams(String userCode, String requestTime, String sign, ReturnDto returnDto) throws RuntimeException {
		if (userCode == null || userCode.isEmpty()) {

			returnDto.setErrCode(EpaiExpEmum.CanShuCuoWu.getErrCode());
			returnDto.setErrMsg("请求参数userCode不能为空");
			throw new RuntimeException("请求参数userCode不能为空");
		}
		if (requestTime == null || requestTime.isEmpty()) {
			returnDto.setErrCode(EpaiExpEmum.CanShuCuoWu.getErrCode());
			returnDto.setErrMsg("请求参数requestTime不能为空");
			throw new RuntimeException("请求参数requestTime不能为空");
		}
		if (sign == null || sign.isEmpty()) {
			returnDto.setErrCode(EpaiExpEmum.CanShuCuoWu.getErrCode());
			returnDto.setErrMsg("请求参数sign不能为空");
			throw new RuntimeException("请求参数sign不能为空");
		}

		Common common = getDMPCommon(userCode);
		if (common == null) {
			returnDto.setErrCode(EpaiExpEmum.YongHuBuCunZai.getErrCode());
			returnDto.setErrMsg("用户" + userCode + "不存在");
			throw new RuntimeException("用户" + userCode + "不存在");
		}

		String localsign = MD5Util.md5(userCode + requestTime + common.getPrivate_key());
		if (!localsign.equalsIgnoreCase(sign)) {
			returnDto.setErrCode(EpaiExpEmum.QianMingCuoWu.getErrCode());
			returnDto.setErrMsg("签名验证异常");
			throw new RuntimeException("签名验证异常");
		}
	}

	private Common getDMPCommon(String userCode) {
		List<Common> commonlist = getCommonList();
		if (commonlist == null || commonlist.size() == 0) {
			return new Common();
		}
		for (Common common : commonlist) {
			if (common.getCommonnumber().equals(userCode)) {
				return common;
			}
		}
		return new Common();
	}

}
