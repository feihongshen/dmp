package cn.explink.b2c.gome;

import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2CDataDAO;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.b2c.tools.ExceptionTrace;
import cn.explink.b2c.tools.Mail;
import cn.explink.domain.B2CData;
import cn.explink.util.WebServiceHandler;

@Service
public class GomeService_CommitDeliverInfo extends GuomeiService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private B2cTools b2cTools;
	@Autowired
	private B2CDataDAO b2CDataDAO;

	/***
	 * 开始接口
	 * ======================================================================
	 * ============
	 */

	public long commitDeliverInfo_interface() {
		long calcCount = 0;
		calcCount += CommitDeliverInfo(GomeFlowEnum.RuKu.getOnwer_code()); // 入库
		calcCount += CommitDeliverInfo(GomeFlowEnum.FenzhanDaohuoSaomiao.getOnwer_code()); // 分配小件员
		calcCount += CommitDeliverInfo(GomeFlowEnum.FenPeiXiaoJianYuan.getOnwer_code()); // 分配小件员
		calcCount += CommitDeliverInfo(GomeFlowEnum.TuoTou.getOnwer_code()); // 妥投、滞留、拒收、丢失、上门退成功，上门退拒退

		calcCount += CommitDeliverInfo(GomeFlowEnum.Tuihuozhanruku.getOnwer_code()); // 退货站入库
		calcCount += CommitDeliverInfo(GomeFlowEnum.Tuigongyingshangchuku.getOnwer_code()); // 退供货商出库

		calcCount += CommitDeliverInfo(GomeFlowEnum.ShangmenTuilinghuo.getOnwer_code()); // 退货订单取货途中
		calcCount += CommitDeliverInfo(GomeFlowEnum.TuiHuoChuZhan.getOnwer_code()); // 退货离开分拨点

		calcCount += CommitDeliverInfo(GomeFlowEnum.Yichangchuli.getOnwer_code()); // 手工维护的破损

		return calcCount;
	}

	public long CommitDeliverInfo(int flowordertype) {
		long calcCount = 0;
		try {
			Gome gome = super.getGomeSettingMethod(B2cEnum.Gome.getKey());
			if (!b2cTools.isB2cOpen(B2cEnum.Gome.getKey())) {
				logger.info("未开[国美]状态反馈对接!");
				return -1;
			}

			while (true) {
				List<B2CData> datalist = b2CDataDAO.getDataListByFlowStatus(flowordertype, gome.getCustomerid(), gome.getMaxCount());
				if (datalist == null || datalist.size() == 0) {
					logger.info("当前没有推送给[国美]的订单数据,flowordertype={}", flowordertype);
					return 0;
				} else {
					try {
						for (B2CData b2cData : datalist) {
							DeliveryInfo deliveryInfo = new ObjectMapper().readValue(b2cData.getJsoncontent(), DeliveryInfo.class); // 构建DeliveryInfoSyn对象
							String xml = getXml(deliveryInfo, gome);
							String method = "setTntInformationsByXmlStr";
							String pram = xml.replaceAll("null", "");
							logger.info("当前推送给[国美]的订单数据,flowordertype={},pram：{}", flowordertype, pram);
							Object o = WebServiceHandler.invokeWsByNameAndPassWord(gome.getTnt_url(), method, pram, gome.getUsername(), gome.getPassword());
							logger.info("当前推送给[国美]flowordertype=[" + flowordertype + "]订单信息={},当前[国美]返回 xml={},xml为空表示全部成功", deliveryInfo.getOrderNumber(), o);
							String returnValue = "";
							if (o != null) {
								returnValue = (String) o;
							}
							dealWithSendFlagUpdate(returnValue, flowordertype, b2cData.getB2cid()); // 修改配送结果
						}
						calcCount += datalist.size();

					} catch (Exception e) {

						// String
						// expt="[国美]状态反馈发生未知异,flowordertype="+flowordertype;
						// try {
						// expt += ",datalist=["+new
						// ObjectMapper().writeValueAsString(datalist);
						// } catch (Exception e1) {
						// e1.printStackTrace();
						// }
						// expt=ExceptionTrace.getExceptionTrace(e,expt);
						// Mail.LoadingAndSendMessage(expt);
						logger.error("[国美]状态反馈发生未知异,flowordertype=" + flowordertype + e.getMessage(), e);
						return 0;
					}

				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return calcCount;

	}

	public int sendByCwbs(String cwbs, long send_b2c_flag) {
		Gome gome = super.getGomeSettingMethod(B2cEnum.Gome.getKey());
		if (!b2cTools.isB2cOpen(B2cEnum.Gome.getKey())) {
			logger.info("未开[国美]状态反馈对接!");
			return 0;
		}
		List<B2CData> datalist = b2CDataDAO.getDataListByCwb(cwbs, send_b2c_flag);
		if (datalist == null || datalist.size() == 0) {
			logger.info("当前没有推送给[国美]的订单数据");
			return 0;
		} else {
			try {
				int i = 0;
				for (B2CData b2cData : datalist) {
					DeliveryInfo deliveryInfo = new ObjectMapper().readValue(b2cData.getJsoncontent(), DeliveryInfo.class); // 构建DeliveryInfoSyn对象
					String xml = getXml(deliveryInfo, gome);
					String method = "setTntInformationsByXmlStr";
					String pram = xml.replaceAll("null", "");
					logger.info("当前推送给[国美]的订单数据,pram：{}", pram);
					Object o = WebServiceHandler.invokeWsByNameAndPassWord(gome.getTnt_url(), method, pram, gome.getUsername(), gome.getPassword());
					logger.info("当前推送给[国美]订单信息={},当前[国美]返回 xml={},xml为空表示全部成功", deliveryInfo.getOrderNumber(), o);
					String returnValue = "";
					if (o != null) {
						returnValue = (String) o;
					}
					dealWithSendFlagUpdateByHand(returnValue, 0, b2cData.getB2cid()); // 修改配送结果
					i++;
				}
				return i;
			} catch (Exception e) {
				logger.error("[国美]状态反馈发生未知异", e);
				return 0;
			}
		}

	}

	private String getXml(DeliveryInfo deliveryInfo, Gome gome) {
		if (deliveryInfo.getStatusCode().equals(GomeFlowEnum.RuKu.getGome_code()) || deliveryInfo.getStatusCode().equals(GomeFlowEnum.FenzhanDaohuoSaomiao.getGome_code())
				|| deliveryInfo.getStatusCode().equals(GomeFlowEnum.FenPeiXiaoJianYuan.getGome_code()) || deliveryInfo.getStatusCode().equals(GomeFlowEnum.TuoTou.getGome_code())
				|| deliveryInfo.getStatusCode().equals(GomeFlowEnum.ShangmenTuilinghuo.getGome_code()) || deliveryInfo.getStatusCode().equals(GomeFlowEnum.ShangMentuiChengGong.getGome_code())
				|| deliveryInfo.getStatusCode().equals(GomeFlowEnum.TuiHuoChuZhan.getGome_code())) {
			return setOrderToXml(deliveryInfo, gome);
		} else {// 异常订单推送
			return setOrderToXml(deliveryInfo, gome);
		}
	}

	private String setOrderToXml(DeliveryInfo deliveryInfo, Gome gome) {
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<tntInformationEvnt><conditions>" + "<tntVo>" + "<buid>" + gome.getBuid() + "</buid>" + "<lspCode>" + gome.getLspCode()
				+ "</lspCode>" + "<orderNumber>" + deliveryInfo.getOrderNumber() + "</orderNumber>" + "<businessCode>" + gome.getBusinessCode() + "</businessCode>" + "<lspAbbr>" + gome.getLspAbbr()
				+ "</lspAbbr>" + "<waybillCode>" + deliveryInfo.getOrderNumber() + "</waybillCode>" + "<statusCode>" + deliveryInfo.getStatusCode() + "</statusCode>" + "<statusTime>"
				+ deliveryInfo.getStatusTime() + "</statusTime>" + "<reasonCode>" + deliveryInfo.getReasonCode() + "</reasonCode>" + "<reasonDesc>" + deliveryInfo.getReasonDesc() + "</reasonDesc>"
				+ "<driverId>" + deliveryInfo.getDriverId() + "</driverId>" + "<driverName>" + deliveryInfo.getDriverName() + "</driverName>" + "<driverPhoneNumber>"
				+ deliveryInfo.getDriverPhoneNumber() + "</driverPhoneNumber>" + "<truckId>" + deliveryInfo.getTruckId() + "</truckId>" + "<sortingCenter>" + deliveryInfo.getSortingCenter()
				+ "</sortingCenter>" + "</tntVo></conditions></tntInformationEvnt>";
		return xml;
	}

	// 修改处理结果的方法
	private void dealWithSendFlagUpdate(String responseJson, int flowordertype, long b2cid) throws Exception {
		if (responseJson != null && !responseJson.trim().equals("")) {
			b2CDataDAO.updateB2cIdSQLResponseStatus(b2cid, 2, responseJson.trim().length() > 500 ? responseJson.trim().substring(0, 499) : responseJson.trim());
		} else {
			b2CDataDAO.updateB2cIdSQLResponseStatus(b2cid, 1, "");
		}

	}

	// 修改处理结果的方法
	private void dealWithSendFlagUpdateByHand(String responseJson, int flowordertype, long b2cid) throws Exception {
		if (responseJson != null && !responseJson.trim().equals("")) {
			b2CDataDAO.updateB2cIdSQLResponseStatus(b2cid, 3, responseJson.trim().length() > 500 ? responseJson.trim().substring(0, 499) : responseJson.trim());
		} else {
			b2CDataDAO.updateB2cIdSQLResponseStatus(b2cid, 1, "");
		}

	}

}
