/**
 * 
 */
package cn.explink.service.docking;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.dao.AutoIntowarehouseMessageDAO;
import cn.explink.dao.EntranceDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.domain.AutoIntowarehouseMessage;
import cn.explink.domain.Entrance;
import cn.explink.param.AutoAllocationParam;
import cn.explink.schedule.Constants;
import cn.explink.util.CurrentUserHelper;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.StringUtil;
import cn.explink.util.Tools;
import cn.explink.util.Tongxing.SocketClient;

/**
 * 与武汉飞远自动化设备的中间件对接
 * 
 * @author mali 2016年3月24日
 *
 */
@Service
public class AutoAllocationService {
	@Autowired
	EntranceDAO entranceDAO;
	@Autowired
	SystemInstallDAO systemInstallDAO;
	@Autowired
	AutoIntowarehouseMessageDAO autoIntowarehouseMessageDAO;

	// 模拟多个客户端，每个客户端的socketclient存放在map中，key是上货口ip
	private Map<String, SocketClient> socketMap = new HashMap<String, SocketClient>();
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * @return the socketMap
	 */
	public Map<String, SocketClient> getSocketMap() {
		return socketMap;
	}

	/**
	 * @param socketMap
	 *            the socketMap to set
	 */
	public void setSocketMap(Map<String, SocketClient> socketMap) {
		this.socketMap = socketMap;
	}

	/**
	 * 初始化授权
	 */
	public void init() {
		// 分拨入口查询
		List<Entrance> eList = this.entranceDAO.getAllEnableEntrances();
		// 是否开启自动分拣设置
		String autoAllocatingSwitch = this.systemInstallDAO.getSystemInstall(
				"AutoAllocating").getValue();

		if (autoAllocatingSwitch != null && autoAllocatingSwitch.equals("1")) {
			// 获得初始化参数
			AutoAllocationParam params = AutoAllocationParam.getInitParams();
			for (Entrance entrance : eList) {
				this.sendMsg(entrance.getEntranceip(), params);

			}
		}
	}

	/**
	 * 在队列中增加一个元素
	 */
	@Transactional
	public void addQueue(String ip, AutoAllocationParam param) {
		param.setAct(AutoAllocationParam.ADDQUEUE);
		this.sendMsg(ip, param);
	}

	/**
	 * 向中间件发送指令
	 * 
	 * @param IP
	 * @param params
	 */
	private void sendMsg(String IP, AutoAllocationParam param) {
		String sendMsg = Tools.getCurrentTime(null) + " 向中间件发送消息" + ",序列号:"
				+ param.getSerialNo() + ",订单号:" + param.getOrderid() + ",动作："
				+ AutoAllocationParam.getActName(param.getAct()) + ",出货口："
				+ param.getExport() + ",方向：" + param.getDirection() + ",配送站点："
				+ param.getStation();
		this.logger.info(sendMsg);
		SocketClient sc = this.socketMap.get(IP);

		// added by wangwei, 20160713
		/* 因为发送报文与接收报文是在两个线程中处理，为确保先保存sendContent再保存receiveContent，需要在发送前就把sendContent存入数据库。
		 * 此处使用了事务，如果 发送失败，sendContent将撤销保存。
		 */
		updateMsgLogAfterSend(param);
		sc.sendMessage(param.toString());
		this.logger.info("发送自动分拨报文：" + param.toString());
	}

	// added by wangwei, 20160713, start
	public void createEmptyMsgLog(AutoAllocationParam param, String cwb,
			String scancwb, byte intowarehouseType, String entranceIP) {
		AutoIntowarehouseMessage autoIntowarehouseMessage = new AutoIntowarehouseMessage();
		autoIntowarehouseMessage.setSerialNo(param.getSerialNo());
		autoIntowarehouseMessage.setIntowarehouseType(intowarehouseType);
		autoIntowarehouseMessage.setScancwb(scancwb);
		autoIntowarehouseMessage.setCwb(cwb);
		autoIntowarehouseMessage.setEntranceIP(entranceIP);;
		autoIntowarehouseMessage.setSendContent("");
		autoIntowarehouseMessage.setSendTime("0000-00-00 00:00:00");
		autoIntowarehouseMessage.setReceiveContent("");
		autoIntowarehouseMessage.setReceiveTime("0000-00-00 00:00:00");
		autoIntowarehouseMessage
				.setHandleStatus(Constants.AUTO_ALLOC_STATUS_UNSENT);
		autoIntowarehouseMessage.setCreatedByUser(CurrentUserHelper
				.getInstance().getUserName());
		autoIntowarehouseMessage.setCreatedDtmLoc(new Date());
		autoIntowarehouseMessageDAO
				.creAutoIntowarehouseMessage(autoIntowarehouseMessage);
	}

	private void updateMsgLogAfterSend(AutoAllocationParam param) {
		AutoIntowarehouseMessage autoIntowarehouseMessage = autoIntowarehouseMessageDAO
				.getAutoIntowarehouseMessageBySerialNo(param.getSerialNo());
		autoIntowarehouseMessage.setCwb(param.getOrderid());
		autoIntowarehouseMessage.setSendContent(param.toString());
		autoIntowarehouseMessage.setSendTime(DateTimeUtil.getNowTime());
		autoIntowarehouseMessage
				.setHandleStatus(Constants.AUTO_ALLOC_STATUS_SENT);
		autoIntowarehouseMessage.setUpdatedByUser(CurrentUserHelper
				.getInstance().getUserName());
		autoIntowarehouseMessage.setUpdatedDtmLoc(new Date());
		autoIntowarehouseMessageDAO
				.saveAutoIntowarehouseMessage(autoIntowarehouseMessage);
	}

	// added by wangwei, 20160713, end

	/**
	 * 处理返回结果 成功响应举例 < root >
	 * <state>0</state><message>已添加队列</message><queue>2</queue></ root > 失败响应举例
	 * < root
	 * ><state>-3</state><message>队列处理中，无法处理！！！</message><queue>2</queue></ root
	 * >
	 * 
	 * @param result
	 * @param params
	 */
	// private void handleResult(String result,Map<String, String> params){
	// //打印日志信息
	// String logMessage=Tools.getCurrentTime(null);
	// if(null==result||result.isEmpty()){
	// logMessage+=" 向中间件发送指令【失败】";
	// this.logger.info(logMessage);
	// return;
	// }
	// int stateBeginIndex=result.indexOf("<state>");
	// int stateEndIndex=result.indexOf("</state>");
	// if(stateBeginIndex>-1&&stateEndIndex>-1){
	// int msgBeginIndex=result.indexOf("<message>");
	// int msgEndIndex=result.indexOf("</message>");
	// //返回状态码
	// String resState=result.substring(stateBeginIndex, stateEndIndex);
	// //返回信息
	// String resMessage=result.substring(msgBeginIndex, msgEndIndex);
	//
	// if(resState.equals("0")){
	// logMessage+=" 向中间件发送指令【成功】"+",返回信息："+resMessage;
	//
	// }else{
	// logMessage+=" 向中间件发送指令【失败】"+",返回信息："+resMessage;
	// }
	// this.logger.info(logMessage);
	// }
	//
	//
	// }
	// added by wangwei, 20160713, start
	/**
	 * 处理返回结果 成功响应举例 < root >
	 * <state>0</state><message>已添加队列</message><queue>2</queue></ root > 失败响应举例
	 * < root
	 * ><state>-3</state><message>队列处理中，无法处理！！！</message><queue>2</queue></ root
	 * >
	 * 
	 * @param result
	 */
	public void handleResultWhenReceiveReply(String result) {
		this.logger.info("接收自动分拨返回报文：" + result);
		String serialNo = "";
		String state = "";
		try {
			Document doc = DocumentHelper.parseText(result);
			Element rootElt = doc.getRootElement();
			serialNo = rootElt.elementTextTrim("serialno");
			state = rootElt.elementTextTrim("state");
		} catch (DocumentException e) {
			logger.error("解析自动分拨返回报文错误：" + result);
			return;
		}

		if (StringUtil.isEmpty(serialNo)) {
			return;
		}
		AutoIntowarehouseMessage autoIntowarehouseMessage = autoIntowarehouseMessageDAO
				.getAutoIntowarehouseMessageBySerialNo(serialNo);
		if (autoIntowarehouseMessage != null) {
			autoIntowarehouseMessage.setReceiveContent(result);
			autoIntowarehouseMessage.setReceiveTime(DateTimeUtil.getNowTime());
			byte handleStatus = Constants.AUTO_ALLOC_STATUS_HANDLE_FAIL;
			if (state.equals("0")) {
				handleStatus = Constants.AUTO_ALLOC_STATUS_HANDLE_SUCCESS;
			} else {
				handleStatus = Constants.AUTO_ALLOC_STATUS_HANDLE_FAIL;
			}
			autoIntowarehouseMessage.setHandleStatus(handleStatus);
			autoIntowarehouseMessage.setUpdatedByUser(autoIntowarehouseMessage
					.getCreatedByUser());
			autoIntowarehouseMessage.setUpdatedDtmLoc(new Date());
			autoIntowarehouseMessageDAO
					.saveAutoIntowarehouseMessage(autoIntowarehouseMessage);
		}
	}

	// added by wangwei, 20160713, end

	/**
	 * 启动与服务器的通信
	 */
	public SocketClient startConnect(String IPAndPort) {
		SocketClient sc = new SocketClient();
		String[] items = IPAndPort.split(":");
		if (items.length == 2) {
			String realIP = items[0];
			int port = Integer.valueOf(items[1]);
			sc.StartEngine(realIP, port);
		}
		return sc;
	}

	/**
	 * 清空队列
	 * 
	 * @param ip
	 * @param param
	 */
	public void flushQueue(String ip, AutoAllocationParam param) {
		param.setAct(AutoAllocationParam.CLEARQUEUE);
		this.sendMsg(ip, param);

	}

	// added by wangwei, 20160713, start
	public void updateStatus(String serialNo, byte handleStatus) {
		AutoIntowarehouseMessage autoIntowarehouseMessage = autoIntowarehouseMessageDAO
				.getAutoIntowarehouseMessageBySerialNo(serialNo);
		autoIntowarehouseMessage.setHandleStatus(handleStatus);
		autoIntowarehouseMessage.setUpdatedByUser(autoIntowarehouseMessage
				.getCreatedByUser());
		autoIntowarehouseMessageDAO
				.saveAutoIntowarehouseMessage(autoIntowarehouseMessage);
	}
	// added by wangwei, 20160713, end
}
