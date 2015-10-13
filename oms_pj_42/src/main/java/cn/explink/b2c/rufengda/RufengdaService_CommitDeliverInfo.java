package cn.explink.b2c.rufengda;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2CDataDAO;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.dao.GetDmpDAO;
import cn.explink.domain.B2CData;
import cn.explink.domain.SystemInstall;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.WebServiceHandler;
import cn.explink.util.MD5.RSAUtils;

@Service
public class RufengdaService_CommitDeliverInfo extends RufengdaService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private B2cTools b2cTools;
	@Autowired
	private B2CDataDAO b2CDataDAO;
	@Autowired
	private GetDmpDAO getDmpDAO;

	/***
	 * 开始接口
	 * ======================================================================
	 * ============
	 */

	public long CommitDeliverInfo_interface() {
		SystemInstall system = getDmpDAO.getSystemInstallByName("rufengdaSendType");
		if (system != null && "yes".equals(system.getValue())) {// 系统设置走批量
			return commitDeliverInfo_interfaceAll();
		}
		long calcCount = 0;
		for (B2cEnum enums : B2cEnum.values()) { // 遍历凡客enum，可能有多个枚举
			if (enums.getMethod().contains("rufengda")) {
				int rfd_key = enums.getKey();
				CommitDeliverInfo(0, rfd_key); // 所有
				if (!b2cTools.isB2cOpen(rfd_key)) {
					logger.info("未开[如风达]状态反馈对接!rfd_key={}", rfd_key);
					continue;
				}
			}
		}
		return calcCount;
	}

	public long commitDeliverInfo_interfaceAll() {
		long calcCount = 0;
		for (B2cEnum enums : B2cEnum.values()) { // 遍历唯品会enum，可能有多个枚举
			if (enums.getMethod().contains("rufengda")) {
				int rfd_key = enums.getKey();
				if (!b2cTools.isB2cOpen(rfd_key)) {
					logger.info("未开[如风达]状态反馈对接!rfd_key={}", rfd_key);
					continue;
				}
				String posttime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
				commitDeliverInfoAll(RufengdaFlowEnum.RuKu.getOnwer_code(), rfd_key, posttime, 0); // 所有
				commitDeliverInfoAll(RufengdaFlowEnum.RuZhanFenZhan.getOnwer_code(), rfd_key, posttime, 0); // 所有
				commitDeliverInfoAll(RufengdaFlowEnum.FenPeiXiaoJianYuan.getOnwer_code(), rfd_key, posttime, 0); // 所有
				commitDeliverInfoAll(RufengdaFlowEnum.TuoTou.getOnwer_code(), rfd_key, posttime, 0); // 所有

				commitDeliverInfoAll(RufengdaFlowEnum.FenPeiXiaoJianYuan.getOnwer_code(), rfd_key, posttime, 3); // 所有
				commitDeliverInfoAll(RufengdaFlowEnum.TuoTou.getOnwer_code(), rfd_key, posttime, 3); // 所有
			}
		}
		return calcCount;
	}

	/**
	 * 按订单顺序，按单执行
	 * 
	 * @param flowordertype
	 * @param rfd_key
	 */
	public long CommitDeliverInfo(int flowordertype, int rfd_key) {
		long calcCount = 0;

		try {
			Rufengda rfd = super.getRufengdaSettingMethod(rfd_key);
			WebServiceHandler wshande = new WebServiceHandler();
			if (!b2cTools.isB2cOpen(rfd_key)) {
				logger.info("未开[如风达]状态反馈对接!rfd_key={}", rfd_key);
				return -1;
			}
			while (true) {
				List<B2CData> datalist = b2CDataDAO.getDataListByFlowStatus(rfd.getCustomerid(), rfd.getMaxCount());
				if (datalist == null || datalist.size() == 0) {
					logger.info("当前没有推送给[如风达]的订单数据,flowordertype={},rfd_key={}", flowordertype, rfd_key);
					return 0;
				} else {
					try {
						for (B2CData data : datalist) {
							DeliveryInfoSyn sendrfd = getDeliveryInfoSyn(data);
							List<DeliveryInfoSyn> sendList = new ArrayList<DeliveryInfoSyn>();
							sendList.add(sendrfd);
							String deliverInfos = new ObjectMapper().writeValueAsString(sendList); // 转化为json对象
							deliverInfos = deliverInfos.replaceAll("rps_", "");
							String deliverInfosSign = "";
							if (rfd.getIsopensignflag() == 1) {
								deliverInfosSign = RSAUtils.sign(deliverInfos, rfd);
							}
							String sendResult = deliverInfos + "," + deliverInfosSign;
							Object parms[] = { rfd.getLcId(), sendResult };
							long startTime = System.currentTimeMillis();
							logger.info("当前推送给如风达，开始：订单号-{}，id:{}", data.getCwb(), data.getB2cid());
							String returnValue = (String) wshande.invokeWs(rfd.getWs_url(), "CommitDeliverInfo", parms);
							String invokeReturn = returnValue.substring(0, returnValue.lastIndexOf(","));
							long endtime = System.currentTimeMillis();
							logger.info("当前推送给[如风达]flowordertype=[" + flowordertype + "]订单信息={},当前[如风达]返回 JSon={},json为空表示全部成功", sendResult, invokeReturn);
							logger.info("当前推送给如风达，结束：订单号-{},id:" + data.getB2cid() + ",耗时：{}", data.getCwb(), endtime - startTime);
							if (rfd.getIsopensignflag() == 1) {
								// 数字签名验证
								String crc = returnValue.substring(returnValue.lastIndexOf(",") + 1);
								boolean signflag = RSAUtils.verify(invokeReturn, crc, rfd);
								if (!signflag) {
									logger.error("CommitDeliverInfo签名验证失败!");
									return 0;
								}
							}
							dealWithSendFlagUpdateById(invokeReturn, data.getB2cid()); // 修改配送结果
						}
					} catch (Exception e) {

						String expt = "[如风达]状态反馈发生未知异,flowordertype=" + flowordertype;
						try {
							expt += ",datalist=[" + new ObjectMapper().writeValueAsString(datalist);
						} catch (Exception e1) {
							e1.printStackTrace();
						}

						logger.error("[如风达]状态反馈发生未知异,flowordertype=" + flowordertype + e.getMessage(), e);
						return 0;
					}
				}
				calcCount += datalist.size();
			}
		} catch (Exception e) {

		}

		return calcCount;

	}

	public long commitDeliverInfoAll(int flowordertype, int rfd_key, String posttime, int type) {
		long calcCount = 0;

		try {
			Rufengda rfd = super.getRufengdaSettingMethod(rfd_key);
			WebServiceHandler wshande = new WebServiceHandler();

			if (!b2cTools.isB2cOpen(rfd_key)) {
				logger.info("未开[如风达]状态反馈对接!rfd_key={}", rfd_key);
				return -1;
			}

			while (true) {
				List<B2CData> datalist = b2CDataDAO.getDataListByFlowStatusAndPosttime(flowordertype, rfd.getCustomerid(), rfd.getMaxCount(), posttime, type);
				if (datalist == null || datalist.size() == 0) {
					logger.info("当前没有推送给[如风达]的订单数据,flowordertype={},rfd_key={}", flowordertype, rfd_key);
					return 0;
				} else {
					try {
						List<DeliveryInfoSyn> sendrfdlist = getDeliveryInfoSynListByMap(datalist);
						String deliverInfos = new ObjectMapper().writeValueAsString(sendrfdlist); // 转化为json对象
						deliverInfos = deliverInfos.replaceAll("rps_", "");
						String deliverInfosSign = "";
						if (rfd.getIsopensignflag() == 1) {
							deliverInfosSign = RSAUtils.sign(deliverInfos, rfd);
						}

						String sendResult = deliverInfos + "," + deliverInfosSign;

						Object parms[] = { rfd.getLcId(), sendResult };
						String returnValue = (String) wshande.invokeWs(rfd.getWs_url(), "CommitDeliverInfo", parms);

						String invokeReturn = returnValue.substring(0, returnValue.lastIndexOf(","));

						logger.info("当前推送给[如风达]flowordertype=[" + flowordertype + "]订单信息={},当前[如风达]返回 JSon={},json为空表示全部成功", sendResult, invokeReturn);

						if (rfd.getIsopensignflag() == 1) {
							// 数字签名验证
							String crc = returnValue.substring(returnValue.lastIndexOf(",") + 1);
							boolean signflag = RSAUtils.verify(invokeReturn, crc, rfd);
							if (!signflag) {
								logger.error("CommitDeliverInfo签名验证失败!");
								return 0;
							}
						}

						dealWithSendFlagUpdate(invokeReturn, flowordertype, datalist); // 修改配送结果

					} catch (Exception e) {

						String expt = "[如风达]状态反馈发生未知异,flowordertype=" + flowordertype;
						try {
							expt += ",datalist=[" + new ObjectMapper().writeValueAsString(datalist);
						} catch (Exception e1) {
							e1.printStackTrace();
						}

						logger.error("[如风达]状态反馈发生未知异,flowordertype=" + flowordertype + e.getMessage(), e);
						return 0;
					}

				}
				calcCount += datalist.size();
			}
		} catch (Exception e) {

		}

		return calcCount;

	}

	// 修改处理结果的方法
	private void dealWithSendFlagUpdate(String responseJson, int flowordertype, List<B2CData> datalist) throws Exception {

		if (!responseJson.trim().equals("")) {
			JSONArray jsarr = JSONArray.fromObject(responseJson.equals("") ? "{}" : responseJson);
			if (jsarr != null && !jsarr.isEmpty()) {
				for (int i = 0; i < jsarr.size(); i++) {
					JSONObject obj = (JSONObject) jsarr.get(i);
					String cwb = obj.getString("OrderNO");
					String ErrorNO = obj.getString("ErrorNO");
					String ErrorMessage = obj.getString("ErrorMessage");

					if ("".equals(cwb.trim()) && "ERROR".equalsIgnoreCase(ErrorNO)) {
						throw new RuntimeException("推送如风达发生未知异常," + ErrorMessage);
					}
					long b2cid = getB2cIdByCwb(datalist, cwb);
					b2CDataDAO.updateB2cIdSQLResponseStatus(b2cid, 2, ErrorNO + ErrorMessage);

				}

			}
		}

		// 更新当前推送的所有状态[post_track_flag=0]为成功
		String b2cdataids = getB2cDataIds(datalist);
		b2CDataDAO.updateMultiB2cIdSQLResponseStatus_AllSuccess(b2cdataids);
		// 发送给dmp
		flowFromJMSB2cService.sendTodmp(b2cdataids);

	}

	/**
	 * 凡客订单反馈，使用一单单处理
	 * 
	 * @param responseJson
	 * @param b2cid
	 * @throws Exception
	 */
	private void dealWithSendFlagUpdateById(String responseJson, long b2cid) throws Exception {
		if (!responseJson.trim().equals("")) {
			JSONArray jsarr = JSONArray.fromObject(responseJson.equals("") ? "{}" : responseJson);
			if (jsarr != null && !jsarr.isEmpty()) {
				for (int i = 0; i < jsarr.size(); i++) {
					JSONObject obj = (JSONObject) jsarr.get(i);
					String cwb = obj.getString("OrderNO");
					String ErrorNO = obj.getString("ErrorNO");
					String ErrorMessage = obj.getString("ErrorMessage");

					if ("".equals(cwb.trim()) && "ERROR".equalsIgnoreCase(ErrorNO)) {
						b2CDataDAO.updateB2cIdSQLResponseStatus(b2cid, 2, ErrorNO + ErrorMessage);
						throw new RuntimeException("推送如风达发生未知异常," + ErrorMessage);
					}
					b2CDataDAO.updateB2cIdSQLResponseStatus(b2cid, 2, ErrorNO + ErrorMessage);
				}
			}
		} else {
			// 更新当前推送的所有状态[post_track_flag=0]为成功
			try {
				b2CDataDAO.updateTimebyId(b2cid, new SimpleDateFormat("MMddHHmmss").format(new Date()));
			} catch (Exception e) {
				b2CDataDAO.updateTimebyId(b2cid, "1");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 发送给dmp
			flowFromJMSB2cService.sendTodmp(b2cid + "");
		}
	}

	private long getB2cIdByCwb(List<B2CData> datalist, String cwb) {
		for (B2CData b2cdata : datalist) {
			if (b2cdata.getCwb().equals(cwb)) {
				return b2cdata.getB2cid();
			}
		}
		return 0;
	}

	private String getB2cDataIds(List<B2CData> datalist) {
		String b2cdataids = "";
		if (datalist != null && datalist.size() > 0) {
			for (B2CData b2cdata : datalist) {
				b2cdataids += b2cdata.getB2cid() + ",";
			}
			b2cdataids = b2cdataids.substring(0, b2cdataids.length() - 1);
		}

		return b2cdataids;
	}

	private List<DeliveryInfoSyn> getDeliveryInfoSynList(List<B2CData> datalist) throws Exception {
		List<DeliveryInfoSyn> sendrfdlist = new ArrayList<DeliveryInfoSyn>();
		for (B2CData b2cData : datalist) {

			DeliveryInfoSyn deliveryInfoSyn = new ObjectMapper().readValue(b2cData.getJsoncontent(), DeliveryInfoSyn.class); // 构建DeliveryInfoSyn对象
			sendrfdlist.add(deliveryInfoSyn);
		}
		return sendrfdlist;
	}

	/**
	 * 凡客对接的批量逻辑
	 * 
	 * @param datalist
	 * @return
	 * @throws Exception
	 */
	private List<DeliveryInfoSyn> getDeliveryInfoSynListByMap(List<B2CData> datalist) throws Exception {
		List<DeliveryInfoSyn> sendrfdlist = new ArrayList<DeliveryInfoSyn>();
		Map<String, String> cwbMap = new HashMap<String, String>();
		for (B2CData b2cData : datalist) {
			if (cwbMap.get(b2cData.getCwb()) == null) {// 如果已经存入就不存
				cwbMap.put(b2cData.getCwb(), b2cData.getJsoncontent());
			} else {
				b2CDataDAO.updateTimebyId(b2cData.getB2cid(), "3");
			}
		}
		if (cwbMap != null && !cwbMap.isEmpty()) {
			Iterator it = cwbMap.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next().toString();
				String value = cwbMap.get(key);
				DeliveryInfoSyn deliveryInfoSyn = new ObjectMapper().readValue(value, DeliveryInfoSyn.class); // 构建DeliveryInfoSyn对象
				sendrfdlist.add(deliveryInfoSyn);
			}
		}
		return sendrfdlist;
	}

	private DeliveryInfoSyn getDeliveryInfoSyn(B2CData data) throws Exception {

		DeliveryInfoSyn deliveryInfoSyn = new ObjectMapper().readValue(data.getJsoncontent(), DeliveryInfoSyn.class); // 构建DeliveryInfoSyn对象

		return deliveryInfoSyn;
	}

}
