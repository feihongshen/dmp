/**
 * 
 */
package cn.explink.service.docking;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.EntranceDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.domain.Entrance;
import cn.explink.param.AutoAllocationParam;
import cn.explink.util.Tools;
import cn.explink.util.Tongxing.SocketClient;

/**
 * 与武汉飞远自动化设备的中间件对接
 * 
 * @author mali  2016年3月24日
 *
 */
@Service
public class AutoAllocationService  {
	@Autowired
	EntranceDAO entranceDAO;
	@Autowired
	SystemInstallDAO systemInstallDAO;
	
	//模拟多个客户端，每个客户端的socketclient存放在map中，key是上货口ip
	private Map<String,SocketClient> socketMap =new HashMap<String,SocketClient>();
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	/**
	 * @return the socketMap
	 */
	public Map<String, SocketClient> getSocketMap() {
		return socketMap;
	}


	/**
	 * @param socketMap the socketMap to set
	 */
	public void setSocketMap(Map<String, SocketClient> socketMap) {
		this.socketMap = socketMap;
	}
	
	
	/**
	 * 初始化授权
	 */
	public void init(){
		//分拨入口查询
		List<Entrance> eList=this.entranceDAO.getAllEnableEntrances();
		//是否开启自动分拣设置
		String autoAllocatingSwitch = this.systemInstallDAO.getSystemInstall("AutoAllocating").getValue();
		
		if(autoAllocatingSwitch!=null&&autoAllocatingSwitch.equals("1")){
			//获得初始化参数
			AutoAllocationParam params=AutoAllocationParam.getInitParams();
			for(Entrance entrance :eList){
				this.sendMsg(entrance.getEntranceip(),params);
				
			}
		}
	}
	

	/**
	 * 在队列中增加一个元素
	 */
	public void addQueue(String ip,AutoAllocationParam param){
		param.setAct(AutoAllocationParam.ADDQUEUE);
		this.sendMsg(ip, param);
	}
	
	
	
	/**
	 * 向中间件发送指令
	 * @param IP
	 * @param params
	 */
	private void sendMsg(String IP, AutoAllocationParam param) {
		String sendMsg=Tools.getCurrentTime(null)+" 向中间件发送消息"
							+",订单号:"+param.getOrderid()
							+",动作："+AutoAllocationParam.getActName(param.getAct())
							+",出货口："+param.getExport()
							+",方向："+param.getDirection()
							+",配送站点："+param.getStation();
		this.logger.info(sendMsg);
		SocketClient sc=this.socketMap.get(IP);
		sc.sendMessage(param.toString());
	}
	
	/**
	 *  处理返回结果
	 *  成功响应举例
	 *< root > <state>0</state><message>已添加队列</message><queue>2</queue></ root >
	 *  失败响应举例
	 *< root ><state>-3</state><message>队列处理中，无法处理！！！</message><queue>2</queue></ root >

	 * @param result
	 * @param params
	 */
//	private void handleResult(String result,Map<String, String> params){
//		//打印日志信息
//    	String logMessage=Tools.getCurrentTime(null);
//		if(null==result||result.isEmpty()){
//			logMessage+=" 向中间件发送指令【失败】";
//			this.logger.info(logMessage);
//			return;
//		}
//		int stateBeginIndex=result.indexOf("<state>");
//		int stateEndIndex=result.indexOf("</state>");
//        if(stateBeginIndex>-1&&stateEndIndex>-1){
//        	int msgBeginIndex=result.indexOf("<message>");
//    		int msgEndIndex=result.indexOf("</message>");
//    		//返回状态码
//        	String resState=result.substring(stateBeginIndex, stateEndIndex);
//        	//返回信息
//        	String resMessage=result.substring(msgBeginIndex, msgEndIndex);
//        	
//        	if(resState.equals("0")){
//        		logMessage+=" 向中间件发送指令【成功】"+",返回信息："+resMessage;
//        		
//        	}else{
//        		logMessage+=" 向中间件发送指令【失败】"+",返回信息："+resMessage;
//        	}
//        	this.logger.info(logMessage);
//        }
//        
//        
//	}
	
	/**
	 * 启动与服务器的通信
	 */
	public SocketClient startConnect(String IP,int port)
	{
		SocketClient sc=new SocketClient();
		sc.StartEngine(IP, port);
		return sc;
	}


	/**
	 * 清空队列
	 * @param ip
	 * @param param
	 */
	public void flushQueue(String ip,AutoAllocationParam param) {
		param.setAct(AutoAllocationParam.CLEARQUEUE);
		this.sendMsg(ip, param);
		
	}


	

}
