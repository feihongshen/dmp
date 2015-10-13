package cn.explink.b2c.explink.core.threadpool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 *  显示
 * @author Administrator
 *
 */

public class SubExcuteCoreTask implements Runnable{
	
	
	private Logger logger = LoggerFactory.getLogger(CoreExcutorService.class);
	
	private Common common;
	private GetDmpDAO getDmpDAO;
	private CommonSendDataDAO commonSendDataDAO;
	private List<CommenSendData> tasklist;
	public SubExcuteCoreTask(List<CommenSendData> list,Common common,GetDmpDAO getDmpDAO,CommonSendDataDAO commonSendDataDAO){
		this.tasklist=list;
		this.common=common;
		this.getDmpDAO=getDmpDAO;
		this.commonSendDataDAO=commonSendDataDAO;
	}
	
	
	@Override
	public void run() {
		if(tasklist==null || tasklist.size() == 0){
			return ;
		}
		for (CommenSendData csdata : tasklist) {
			excuteService(common, csdata);
		}
		
	}
	
	private void excuteService(Common com, CommenSendData csdata) {
		
		String sendcontent;
		try {
			sendcontent = JacksonMapper.getInstance().writeValueAsString(csdata);
		

			logger.info("oms请求dmp处理订单={},flowordertype={}", csdata.getCwb(), csdata.getFlowordertype());
	
			String response = getDmpDAO.requestDMPOrderService_feedback(sendcontent);
	
			logger.info("上游-oms返回{}信息={}",csdata.getCwb(), response);
			
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
	
			orderStatusNotify(com.getFeedback_url(), csdata.getCustid(), response, com);
		
		} catch (Exception e) {
			logger.error("oms请求dmp处理订单"+csdata.getCwb()+"异常",e);
		} 
	}

	/**
	 * 上游异步回传最终状态给下游
	 **/
	private void orderStatusNotify(String url, String custid, String response, Common common) {

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
