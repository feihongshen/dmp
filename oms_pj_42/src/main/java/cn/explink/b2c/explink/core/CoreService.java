package cn.explink.b2c.explink.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.JacksonMapper;
import cn.explink.dao.CommonSendDataDAO;
import cn.explink.dao.GetDmpDAO;
import cn.explink.dao.WarehouseCommenDAO;
import cn.explink.domain.CommenSendData;
import cn.explink.domain.Common;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.JSONReslutUtil;

/**
 * 系统之间的对接（上游） 接收下游状态回传
 * 
 * @author Administrator
 *
 */
@Service
public class CoreService {
	private Logger logger = LoggerFactory.getLogger(CoreService.class);

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
	 * 上游- oms定时回传至dmp，反馈
	 */
	public long selectOMStemp_feedback() {
		long calcCount = 0;
		int check = 0;
		List<Common> commonlist = getCommonList();

		if (commonlist == null || commonlist.size() == 0) {
			logger.warn("上游-未开启系统对接");
			return -1;
		}
		for (Common com : commonlist) {
			if (com.getIsopenflag() == 0) {
				logger.info("上游对接未开启name={}", com.getCommonname());
				continue;
			}
			check++;
			try {
				calcCount += excute_SelectOMStemp_feedback(com);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (check == 0) {
			return -1;
		}
		return calcCount;
	}

	private long excute_SelectOMStemp_feedback(Common com) throws JsonGenerationException, JsonMappingException, IOException {
		long calcCount = 0;

		try {
			int i = 0;
			while (true) {
				i++;
				if (i > 100) {
					logger.warn("上游OMS临时表反馈DMP循环超过100次,怀疑程序未知bug,return");
					return 0;
				}

				long loopcount = com.getLoopcount() == 0 ? 3 : com.getLoopcount();

				List<CommenSendData> datalist = commonSendDataDAO.getCommenCwbListBycommoncode(com.getCommonnumber(), com.getPageSize(), loopcount);
				if (datalist == null || datalist.size() == 0) {
					logger.info("上游-oms目前没有待发送dmp的订单,commonCode={},pagesize={}", com.getCommonnumber(), com.getPageSize());
					return 0;
				}

				for (CommenSendData csdata : datalist) {
					String sendcontent = JacksonMapper.getInstance().writeValueAsString(csdata);

					logger.info("上游oms请求dmp处理订单={},flowordertype={}", csdata.getCwb(), csdata.getFlowordertype());

					String response = getDmpDAO.requestDMPOrderService_feedback(sendcontent);

					logger.info("上游-oms返回信息={}", response);
					if (response.equals("SUCCESS")) { // 处理成功 存储时间格式
						commonSendDataDAO.updateCommenSendDataById(csdata.getId(), DateTimeUtil.getNowTime(), "SUCCESS");
					} else { // 失败存储2

						// 当捕获到异常 ,包含 状态为已审核的订单不允许进行 关键字的，则新增一条领货记录，并且
						if (csdata.getDeliverystate() > 0 && response != null && response.contains("状态为已审核的订单不允许进行反馈")) {
							// 插入一条领货 插入上游OMS临时表
							long isexitsflag = commonSendDataDAO.isExistsCwbFlag(csdata.getCwb(), csdata.getCommencode(), csdata.getStatetime(), FlowOrderTypeEnum.FenZhanLingHuo.getValue() + "");
							if (isexitsflag == 0) {
								commonSendDataDAO.creCommenSendData(csdata.getCwb(), csdata.getStartbranchid(), csdata.getCommencode(), csdata.getPosttime(), csdata.getStatetime(),
										csdata.getDatajson(), 0, FlowOrderTypeEnum.FenZhanLingHuo.getValue(), "0");
							} else {
								commonSendDataDAO.updateCommenSendDataById(csdata.getId(), "2", response.length() > 280 ? response.substring(0, 280) : response);
							}

							logger.info("重置审核状态自动补领货cwb={}", csdata.getCwb());

						} else {
							commonSendDataDAO.updateCommenSendDataById(csdata.getId(), "2", response.length() > 280 ? response.substring(0, 280) : response);
						}

					}

					String custid = csdata.getCustid(); // 发送下游信息

					feedbackLastCwbOrderStatus(com.getFeedback_url(), custid, response, com);
				}
				calcCount = datalist.size();

			}
		} catch (Exception e) {

		}

		return calcCount;

	}

	/**
	 * 上游异步回传最终状态给下游
	 **/
	private void feedbackLastCwbOrderStatus(String url, String custid, String response, Common common) {

		if (common.getIsasynchronous() == 0) {
			logger.info("不开启异步回传");
			return;
		}

		try {
			String success = "SUCCESS".equals(response) ? "T" : "F";

			Map<String, String> parmsMap = new HashMap<String, String>();
			parmsMap.put("custid", custid);
			parmsMap.put("success", success);
			parmsMap.put("msg", success.equals("T") ? "" : response);

			logger.info("异步回传下游url={},custid={},success=" + success + ",msg=" + (success.equals("T") ? "" : response), url, custid);

			JSONReslutUtil.sendHTTPServerByParms(parmsMap, url);
		} catch (Exception e) {
			logger.error("异步回传下游发生未知异常", e);
		}
	}

}
