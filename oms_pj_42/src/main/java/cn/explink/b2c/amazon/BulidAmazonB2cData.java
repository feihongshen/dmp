package cn.explink.b2c.amazon;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.amazon.domain.AmazonInfo;
import cn.explink.b2c.tools.B2CCodDataDAO;
import cn.explink.b2c.tools.B2cDataOrderFlowDetail;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.b2c.tools.ExptReason;
import cn.explink.dao.GetDmpDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.B2CCodData;
import cn.explink.domain.Branch;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.jms.dto.DmpCwbOrder;
import cn.explink.jms.dto.DmpDeliveryState;
import cn.explink.jms.dto.DmpOrderFlow;
import cn.explink.util.DateTimeUtil;

@Service
public class BulidAmazonB2cData {
	private Logger logger = LoggerFactory.getLogger(BulidAmazonB2cData.class);
	@Autowired
	B2cDataOrderFlowDetail orderFlowDetail;
	@Autowired
	GetDmpDAO getDmpdao;
	@Autowired
	AmazonaService amazonaService;
	@Autowired
	UserDAO userDAO;
	@Autowired
	private B2cTools b2ctools;
	@Autowired
	private B2CCodDataDAO b2CCodDataDAO;
	@Autowired
	private AmazonZitiDAO amazonZitiDAO;

	public void buildAmazonCod(DmpOrderFlow orderFlow, long flowOrdertype, DmpDeliveryState deliveryState, DmpCwbOrder cwbOrder) {
		if ((flowOrdertype == FlowOrderTypeEnum.YiShenHe.getValue() && deliveryState != null && deliveryState.getBusinessfee().compareTo(BigDecimal.ZERO) > 0
				&& deliveryState.getReturnedfee().compareTo(BigDecimal.ZERO) == 0 && deliveryState.getDeliverystate() != DeliveryStateEnum.FenZhanZhiLiu.getValue())
				|| (flowOrdertype == FlowOrderTypeEnum.ShouGongdiushi.getValue() && cwbOrder.getReceivablefee().compareTo(BigDecimal.ZERO) > 0)) {// 存储cod对接表
			logger.info("满足cod存储，订单号：{}", orderFlow.getCwb());
			saveCodData(orderFlow, flowOrdertype, deliveryState, cwbOrder);
		}
	}

	public String BuildAmazonMethod(DmpOrderFlow orderFlow, long flowOrdertype, DmpCwbOrder cwbOrder, DmpDeliveryState deliveryState, long delivery_state, ObjectMapper objectMapper)
			throws IOException, JsonGenerationException, JsonMappingException {
		buildAmazonCod(orderFlow, flowOrdertype, deliveryState, cwbOrder);
		if ("亚马逊自提".equals(cwbOrder.getCartype())) {// 自提业务
			String amazonStatus2 = amazonaService.getAmazonZitiFlowEnum(flowOrdertype, delivery_state);
			if (amazonStatus2 == null) {
				logger.info("订单号：{} 不属于[亚马逊]所需要的json---,状态{}，return", cwbOrder.getCwb(), flowOrdertype);
				return null;
			}
			if (flowOrdertype == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() || flowOrdertype == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()
					|| flowOrdertype == FlowOrderTypeEnum.YiShenHe.getValue()) {
				try {
					if (flowOrdertype == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() || flowOrdertype == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()) {
						if (amazonZitiDAO.checkAmazonZitiCount(cwbOrder.getCwb()) == 0) {
							String endTime = new SimpleDateFormat("yyyy-MM-dd").format(orderFlow.getCredate()) + " 23:59:59";
							String daohuotime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate());
							SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							long createtime = 0;
							try {
								Date date1 = ft.parse(endTime);
								createtime = date1.getTime();
							} catch (ParseException e) {
								e.printStackTrace();
							}
							amazonZitiDAO.creAmazonZiti(cwbOrder.getCwb(), createtime, daohuotime, cwbOrder.getCustomerid(), orderFlow.getBranchid());
						}
					} else {
						amazonZitiDAO.delAmazonZiti(cwbOrder.getCwb());
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			if (flowOrdertype == FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue()) {// 做了退货站入库，删掉超期表
				amazonZitiDAO.delAmazonZiti(cwbOrder.getCwb());
			}
			return getZiti(orderFlow, flowOrdertype, cwbOrder, deliveryState, objectMapper, amazonStatus2);
		}
		String amazonStatus = amazonaService.getAmazonFlowEnum(flowOrdertype, delivery_state, cwbOrder.getCwbordertypeid());
		if (amazonStatus == null) {
			logger.info("订单号：{} 不属于[亚马逊]所需要的json---,状态{}，return", cwbOrder.getCwb(), flowOrdertype);
			return null;
		} else {
			AmazonInfo amazonInfo = new AmazonInfo();
			amazonInfo.setCarrierTrackingNum(cwbOrder.getCwb());
			amazonInfo.setDateTimePeriodValue(new SimpleDateFormat("yyyyMMddHHmmss").format(orderFlow.getCredate()));
			amazonInfo.setDateTime5PeriodValue(DateTimeUtil.getDate5min(orderFlow.getCredate(), 5));
			amazonInfo.setMessageReferenceNum(cwbOrder.getShipcwb());// 加密单号
			try {
				JSONObject json = JSONObject.fromObject(cwbOrder.getRemark5());
				amazonInfo.setSendCity(json.getString("city"));
				amazonInfo.setSendCountryCode(json.getString("countryCode"));
				amazonInfo.setSendLine1(json.getString("addressLine1"));
				amazonInfo.setSendPartyName(json.getString("name"));
				amazonInfo.setSendPostalCode(json.getString("zip"));
				amazonInfo.setSendStateProvinceCode(json.getJSONObject("stateChoice").getString("stateProvince"));
			} catch (Exception e) {
				amazonInfo.setSendCity("");
				amazonInfo.setSendCountryCode("");
				amazonInfo.setSendLine1("");
				amazonInfo.setSendPartyName("");
				amazonInfo.setSendPostalCode("");
				amazonInfo.setSendStateProvinceCode("");
			}
			amazonInfo.setSignCity(cwbOrder.getCwbcity());
			amazonInfo.setSignCountryCode("CN");
			amazonInfo.setSignLine1(cwbOrder.getConsigneeaddress());
			amazonInfo.setSignPartyName(cwbOrder.getConsigneename());
			amazonInfo.setSignPostalCode(cwbOrder.getConsigneeno());
			amazonInfo.setSignStateProvinceCode(cwbOrder.getCwbprovince());
			String rCode = getReasonCode(orderFlow, cwbOrder, deliveryState);// 异常码关联
			if (rCode.equals("yy")) {
				amazonStatus = "CA";
				rCode = "AQ";
			}
			if (rCode.equals("tt")) {
				amazonStatus = "CA";
				rCode = "BG";
			}
			if (rCode.equals("B2")) {
				amazonStatus = "CA";
			}
			if ((rCode.equals("BG") || rCode.equals("B2") || rCode.equals("AQ")) && amazonStatus.equals("RJ")) {
				amazonStatus = "CA";
			}
			if (flowOrdertype == FlowOrderTypeEnum.BaoGuoweiDao.getValue()) {
				amazonInfo.setStatus("SD");
				amazonInfo.setStatusReason("AY");
			} else if (flowOrdertype == FlowOrderTypeEnum.ZhongZhuanyanwu.getValue()) {
				if (cwbOrder.getCwbordertypeid().equals(CwbOrderTypeIdEnum.Peisong.getValue() + "")) {
					amazonInfo.setStatus("SD");
					amazonInfo.setStatusReason("BG");
				} else {
					amazonInfo.setStatus("SD");
					amazonInfo.setStatusReason("I2");
				}
			} else if (flowOrdertype == FlowOrderTypeEnum.ShouGongdiushi.getValue()) {
				amazonInfo.setStatus("CA");
				amazonInfo.setStatusReason("PL");
			} else {
				amazonInfo.setStatus(amazonStatus);
				amazonInfo.setStatusReason(rCode);
			}
			amazonInfo.setPmcr_CODE(getWeishuaka(cwbOrder)); // 未刷卡原因，获取关联
			if (deliveryState != null
					&& (deliveryState.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue() || deliveryState.getDeliverystate() == DeliveryStateEnum.ShangMenHuanChengGong.getValue() || deliveryState
							.getDeliverystate() == DeliveryStateEnum.ShangMenTuiChengGong.getValue()) && deliveryState.getCash().compareTo(BigDecimal.ZERO) > 0) {
				amazonInfo.setIsCach(1);
			}
			try {
				Branch b = getDmpdao.getNowBranch(orderFlow.getBranchid());
				amazonInfo.setBranchName(b.getBranchname());
				amazonInfo.setBranchPhone(b.getBranchphone());
				amazonInfo.setBranchAddress(b.getBranchaddress());
			} catch (Exception e) {
				logger.error("亚马逊封装站点信息异常:", e);
				e.printStackTrace();
			}

			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
				try {
					String deliveryStr = getDmpdao.getDeliverById(cwbOrder.getDeliverid());
					JSONObject json = JSONObject.fromObject(deliveryStr);
					amazonInfo.setDeliveryName(json.getString("realname"));
					amazonInfo.setDeliveryPhone(json.getString("usermobile"));
				} catch (Exception e) {
					logger.error("亚马逊封装小件员信息异常:", e);
					e.printStackTrace();
				}
			}
			amazonInfo.setIsTuotou(0);
			if (deliveryState != null && deliveryState.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue() && deliveryState.getReceivedfee().compareTo(BigDecimal.ZERO) > 0
					&& "D1".equals(amazonStatus) && "NS".equals(rCode)) {
				if (deliveryState.getPos().compareTo(BigDecimal.ZERO) > 0) {

					if (deliveryState.getPosremark() != null && deliveryState.getPosremark().contains("手机在线支付")) {
						amazonInfo.setPayType("Cash");
					} else {
						amazonInfo.setPayType("MPos");
					}

				} else {
					amazonInfo.setPayType("Cash");
				}
				amazonInfo.setIsTuotou(1);
			}
			logger.info("订单号：{}封装成[亚马逊]所需要的json----状态：{}", cwbOrder.getCwb(), flowOrdertype);
			return objectMapper.writeValueAsString(amazonInfo);
		}
	}

	private String getZiti(DmpOrderFlow orderFlow, long flowOrdertype, DmpCwbOrder cwbOrder, DmpDeliveryState deliveryState, ObjectMapper objectMapper, String amazonStatus)
			throws JsonGenerationException, JsonMappingException, IOException {
		AmazonInfo amazonInfo = new AmazonInfo();

		amazonInfo.setCarrierTrackingNum(cwbOrder.getCwb());
		amazonInfo.setDateTimePeriodValue(new SimpleDateFormat("yyyyMMddHHmmss").format(orderFlow.getCredate()));
		amazonInfo.setDateTime5PeriodValue(DateTimeUtil.getDate5min(orderFlow.getCredate(), 5));
		amazonInfo.setMessageReferenceNum(cwbOrder.getShipcwb());// 加密单号
		try {
			JSONObject json = JSONObject.fromObject(cwbOrder.getRemark5());
			amazonInfo.setSendCity(json.getString("city"));
			amazonInfo.setSendCountryCode(json.getString("countryCode"));
			amazonInfo.setSendLine1(json.getString("addressLine1"));
			amazonInfo.setSendPartyName(json.getString("name"));
			amazonInfo.setSendPostalCode(json.getString("zip"));
			amazonInfo.setSendStateProvinceCode(json.getJSONObject("stateChoice").getString("stateProvince"));
		} catch (Exception e) {
			amazonInfo.setSendCity("");
			amazonInfo.setSendCountryCode("");
			amazonInfo.setSendLine1("");
			amazonInfo.setSendPartyName("");
			amazonInfo.setSendPostalCode("");
			amazonInfo.setSendStateProvinceCode("");
		}
		amazonInfo.setSignCity(cwbOrder.getCwbcity());
		amazonInfo.setSignCountryCode("CN");
		amazonInfo.setSignLine1(cwbOrder.getConsigneeaddress());
		amazonInfo.setSignPartyName(cwbOrder.getConsigneename());
		amazonInfo.setSignPostalCode(cwbOrder.getConsigneeno());
		amazonInfo.setSignStateProvinceCode(cwbOrder.getCwbprovince());
		ExptReason expcode = null;
		if (flowOrdertype == FlowOrderTypeEnum.YiShenHe.getValue() && deliveryState != null) {
			expcode = b2ctools.getExptReasonByB2c(cwbOrder.getLeavedreasonid(), cwbOrder.getBackreasonid(), cwbOrder.getCustomerid() + "", deliveryState.getDeliverystate());
		}
		String rCode = "";
		rCode = expcode == null ? "-" : expcode.getExpt_code();
		// 异常码关联
		if (amazonStatus.indexOf("_") > -1) {
			amazonInfo.setStatus(amazonStatus.split("_")[0]);
			amazonInfo.setStatusReason(amazonStatus.split("_")[1]);
		} else if (flowOrdertype == FlowOrderTypeEnum.ShouGongdiushi.getValue()) {
			amazonInfo.setStatus("CA");
			amazonInfo.setStatusReason("PL");
		} else if (flowOrdertype == FlowOrderTypeEnum.YiShenHe.getValue() && deliveryState != null && (deliveryState.getDeliverystate() == DeliveryStateEnum.HuoWuDiuShi.getValue())) {
			if (!("ZH".equals(rCode) || "PL".equals(rCode))) {
				amazonInfo.setStatus("CA");
				amazonInfo.setStatusReason("PL");
			} else {
				amazonInfo.setStatus("CA");
				amazonInfo.setStatusReason(rCode);
			}
		} else if (flowOrdertype == FlowOrderTypeEnum.YiShenHe.getValue() && deliveryState != null && (deliveryState.getDeliverystate() == DeliveryStateEnum.QuanBuTuiHuo.getValue())) {
			String cod = "03,06,08,07,04,02,05";
			if ("".equals(rCode) || cod.indexOf(rCode) < 0) {
				amazonInfo.setStatus(amazonStatus);
				amazonInfo.setStatusReason("06");
			} else {
				amazonInfo.setStatus(amazonStatus);
				amazonInfo.setStatusReason(rCode);
			}
		} else {
			amazonInfo.setStatus(amazonStatus);
			amazonInfo.setStatusReason("06");
		}
		amazonInfo.setPmcr_CODE(getWeishuaka(cwbOrder)); // 未刷卡原因，获取关联
		if (deliveryState != null
				&& (deliveryState.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue() || deliveryState.getDeliverystate() == DeliveryStateEnum.ShangMenHuanChengGong.getValue() || deliveryState
						.getDeliverystate() == DeliveryStateEnum.ShangMenTuiChengGong.getValue()) && deliveryState.getCash().compareTo(BigDecimal.ZERO) > 0) {
			amazonInfo.setIsCach(1);
		}
		try {
			Branch b = getDmpdao.getNowBranch(orderFlow.getBranchid());
			amazonInfo.setBranchName(b.getBranchname());
			amazonInfo.setBranchPhone(b.getBranchphone());
			amazonInfo.setBranchAddress(b.getBranchaddress());
		} catch (Exception e) {
			logger.error("亚马逊封装站点信息异常:", e);
			e.printStackTrace();
		}

		if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
			try {
				String deliveryStr = getDmpdao.getDeliverById(cwbOrder.getDeliverid());
				JSONObject json = JSONObject.fromObject(deliveryStr);
				amazonInfo.setDeliveryName(json.getString("realname"));
				amazonInfo.setDeliveryPhone(json.getString("usermobile"));
			} catch (Exception e) {
				logger.error("亚马逊封装小件员信息异常:", e);
				e.printStackTrace();
			}
		}
		amazonInfo.setIsTuotou(0);
		if (deliveryState != null && deliveryState.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue() && deliveryState.getReceivedfee().compareTo(BigDecimal.ZERO) > 0
				&& "D1".equals(amazonStatus) && "NS".equals(rCode)) {
			if (deliveryState.getPos().compareTo(BigDecimal.ZERO) > 0) {
				amazonInfo.setPayType("MPos");
			} else {
				amazonInfo.setPayType("Cash");
			}
			amazonInfo.setIsTuotou(1);
		}
		logger.info("订单号：{}封装成[亚马逊]所需要的json----状态：{}", cwbOrder.getCwb(), flowOrdertype);
		return objectMapper.writeValueAsString(amazonInfo);
	}

	private String getReasonCode(DmpOrderFlow orderFlow, DmpCwbOrder cwbOrder, DmpDeliveryState deliveryState) {
		String reason_code = "NS";
		if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.RuKu.getValue() && cwbOrder.getCwbordertypeid().equals(CwbOrderTypeIdEnum.Shangmentui.getValue() + "")) {
			return "R7";
		}
		if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.FenZhanLingHuo.getValue() && cwbOrder.getCwbordertypeid().equals(CwbOrderTypeIdEnum.Shangmentui.getValue() + "")) {
			return "R8";
		}

		if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue()) {
			return "N4";
		}
		if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue()) {
			return "N5";
		}
		if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() || orderFlow.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()
				|| orderFlow.getFlowordertype() == FlowOrderTypeEnum.RuKu.getValue() || orderFlow.getFlowordertype() == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
			return "NS";
		}
		if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue() && deliveryState != null) {
			reason_code = b2ctools.getExptReasonByB2c(cwbOrder.getLeavedreasonid(), cwbOrder.getBackreasonid(), cwbOrder.getCustomerid() + "", deliveryState.getDeliverystate()).getExpt_code();
			if (deliveryState.getDeliverystate() == DeliveryStateEnum.HuoWuDiuShi.getValue()) {
				reason_code = b2ctools.getDiushiReasonByB2c(cwbOrder.getBackreasonid(), cwbOrder.getCustomerid() + "").getExpt_code();
				if (reason_code == null || "".equals(reason_code)) {
					return "ZH";
				} else {
					return reason_code;
				}
			}
			if (deliveryState.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue() || deliveryState.getDeliverystate() == DeliveryStateEnum.ShangMenHuanChengGong.getValue()
					|| deliveryState.getDeliverystate() == DeliveryStateEnum.ShangMenTuiChengGong.getValue()) {
				return "NS";
			}
			if (deliveryState.getDeliverystate() == DeliveryStateEnum.ShangMenJuTui.getValue()) {
				return "AJ";
			}
			if (reason_code == null || "".equals(reason_code)) {
				if ((Integer.parseInt(cwbOrder.getCwbordertypeid()) == CwbOrderTypeIdEnum.Shangmentui.getValue() && deliveryState.getDeliverystate() == DeliveryStateEnum.FenZhanZhiLiu.getValue())) {
					return "AR";
				}
				if ((Integer.parseInt(cwbOrder.getCwbordertypeid()) == CwbOrderTypeIdEnum.Shangmentui.getValue() && deliveryState.getDeliverystate() == DeliveryStateEnum.ShangMenJuTui.getValue())) {
					return "AJ";
				}
				if (deliveryState.getDeliverystate() == DeliveryStateEnum.FenZhanZhiLiu.getValue() && cwbOrder.getCwbordertypeid().equals(CwbOrderTypeIdEnum.Shangmentui.getValue() + "")) {
					return "AR";
				}
				if (deliveryState.getDeliverystate() == DeliveryStateEnum.FenZhanZhiLiu.getValue() && cwbOrder.getCwbordertypeid().equals(CwbOrderTypeIdEnum.Peisong.getValue() + "")) {
					return "AQ";
				}
				if (deliveryState.getDeliverystate() == DeliveryStateEnum.QuanBuTuiHuo.getValue()) {
					return "06";
				}
			} else {
				if (cwbOrder.getCwbordertypeid().equals(CwbOrderTypeIdEnum.Shangmentui.getValue() + "")) {
					String cod = "AJ,B4,RA,AO,J7,AR,A7,I2,R9,I5";
					if (cod.indexOf(reason_code) < 0) {
						String newcode = getCode(reason_code);
						if (cod.indexOf(newcode) < 0) {
							return "AR";
						} else {
							return newcode;
						}
					}
				}
				if (cwbOrder.getCwbordertypeid().equals(CwbOrderTypeIdEnum.Peisong.getValue() + "") && deliveryState.getDeliverystate() == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
					String cod = "B2,AQ,BG,AV,AO,S1,HB,AA,A5,H1,H2";
					if (cod.indexOf(reason_code) < 0) {
						String newcode = getCode(reason_code);
						if (cod.indexOf(newcode) < 0) {
							return "AQ";
						} else {
							return newcode;
						}
					}
				} else if (cwbOrder.getCwbordertypeid().equals(CwbOrderTypeIdEnum.Peisong.getValue() + "") && deliveryState.getDeliverystate() == DeliveryStateEnum.QuanBuTuiHuo.getValue()) {
					String cod = "yy,tt,03,06,08,07,04,02,05,AQ,BG,B2";
					if (cod.indexOf(reason_code) < 0) {
						return "06";
					}
				}
				return reason_code;
			}
		} else {
			return "NS";
		}
		return reason_code == null ? "NS" : reason_code;
	}

	private String getCode(String code) {
		String newCode = "";
		Map<String, String> map = new HashMap<String, String>();
		map.put("AV", "RA");
		map.put("S1", "J7");
		map.put("HB", "AR");
		map.put("AQ", "R9");
		map.put("AA", "I5");
		map.put("A5", "A7");
		map.put("H1", "AR");
		map.put("H2", "AR");

		map.put("RA", "AV");
		map.put("J7", "S1");
		map.put("AR", "HB");
		map.put("R9", "AQ");
		map.put("I5", "AA");
		map.put("A7", "A5");
		map.put("BG", "I2");

		newCode = map.get(code) == null ? code : map.get(code);
		return newCode;
	}

	private String getWeishuaka(DmpCwbOrder cwbOrder) {
		if (cwbOrder.getWeishuakareasonid() > 0) {
			String reason_code = b2ctools.getWeishuakaReasonByB2c(cwbOrder.getWeishuakareasonid(), cwbOrder.getCustomerid() + "").getExpt_code();
			if (reason_code == null || "".equals(reason_code)) {
				return "CHANGE_MIND";
			} else {
				return reason_code;
			}
		} else {
			return "";
		}
	}

	private void saveCodData(DmpOrderFlow orderFlow, long flowOrdertype, DmpDeliveryState deliveryState, DmpCwbOrder cwbOrder) {
		try {
			long isCunzai = b2CCodDataDAO.check(orderFlow.getCwb(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()));
			if (isCunzai > 0) {
				logger.info("亚马逊cod存储已重复,单号：{}", orderFlow.getCwb());
				return;
			}
			// 判断如果是支付宝app结算，则修改支付方式为cash，重置pos为 0
			if (deliveryState.getPosremark().contains("手机在线支付") || deliveryState.getPosremark().contains("手机")) {
				deliveryState.setCash(deliveryState.getPos());
				deliveryState.setPos(BigDecimal.ZERO);

				logger.info("亚马逊订单{}POS转化为现金,pos={},cash=" + deliveryState.getCash(), orderFlow.getCwb(), deliveryState.getPos());
			}

			long isState = b2CCodDataDAO.checkState(orderFlow.getCwb(), 0);// 当前状态是否是state=0,如果是旧覆盖
			if (isState > 0) {// 覆盖原来的

				JSONObject datajson = JSONObject.fromObject(deliveryState);
				b2CCodDataDAO.updateDatajsonByCwb(orderFlow.getCwb(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()), datajson.toString());
				logger.info("亚马逊cod存储覆盖state=0的订单,单号：{}", orderFlow.getCwb());
				return;
			}
			B2CCodData b2CData = new B2CCodData();
			if (flowOrdertype == FlowOrderTypeEnum.ShouGongdiushi.getValue()) {
				b2CData.setCwb(orderFlow.getCwb());
				b2CData.setCretime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				b2CData.setCustomerid(cwbOrder.getCustomerid());
				DmpDeliveryState deliveryState1 = new DmpDeliveryState();
				deliveryState1.setCwb(cwbOrder.getCwb());
				deliveryState1.setReceivedfee(BigDecimal.ZERO);
				deliveryState1.setBusinessfee(cwbOrder.getReceivablefee());
				deliveryState1.setDeliverystate(8);
				JSONObject datajson = JSONObject.fromObject(deliveryState1);
				b2CData.setDatajson(datajson.toString());
				b2CData.setPosttime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()));
				b2CData.setDeliverystate(8);
				b2CCodDataDAO.saveB2CData(b2CData);
			} else {
				b2CData.setCwb(orderFlow.getCwb());
				b2CData.setCretime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				b2CData.setCustomerid(deliveryState.getCustomerid());
				JSONObject datajson = JSONObject.fromObject(deliveryState);
				b2CData.setDatajson(datajson.toString());
				b2CData.setPosttime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()));
				b2CData.setDeliverystate(deliveryState.getDeliverystate());
				b2CCodDataDAO.saveB2CData(b2CData);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("亚马逊保存cod对接表异常", e);
		}
	}

}
