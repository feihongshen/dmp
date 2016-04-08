package cn.explink.pos.mobileapp_dcb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import net.sf.json.JSONObject;

import org.apache.camel.CamelContext;
import org.apache.camel.Header;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.tools.RestHttpServiceHanlder;
import cn.explink.dao.MqExceptionDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.MqExceptionBuilder;
import cn.explink.domain.User;
import cn.explink.domain.MqExceptionBuilder.MessageSourceEnum;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.pos.tools.PosEnum;

@Service
public class MobiledcbService_SynUser {
	private Logger logger = LoggerFactory.getLogger(MobiledcbService_SynUser.class);

	@Autowired
	UserDAO userDAO;
	@Autowired
	JiontDAO jointDAO;
	@Autowired
	private CamelContext camelContext;
	
	@Autowired
	private MqExceptionDAO mqExceptionDAO;
	
	private static final String MQ_FROM_URI_USER_MONITOR = "jms:queue:VirtualTopicConsumers.oms1.userMonitor";
	private static final String MQ_HEADER_NAME_USER_MONITOR = "userMonitor";

	@PostConstruct
	public void init() throws Exception {
		camelContext.addRoutes(new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from("jms:queue:VirtualTopicConsumers.oms1.userMonitor?concurrentConsumers=5").to("bean:mobiledcbService_SynUser?method=userMonitor").routeId("userMonitorRoute");
			}
		});
	}

	public void userMonitor(@Header("userMonitor") String parm) { // 处理jms数据
		try {

			int isOpenFlag = jointDAO.getStateByJointKey(PosEnum.MobileApp_dcb.getKey());
			if (isOpenFlag == 0) {
				logger.warn("未开启新疆大晨报对接");
				return;
			}

			Mobiledcb mobiledbc = this.getMobiledcb(PosEnum.MobileApp_dcb.getKey());
			User user = JacksonMapper.getInstance().readValue(parm, User.class);
			String response = buildparamsAndSend(mobiledbc.getSender_usersyn(), user);
			logger.info("新疆大晨报派送员信息同步【修改】username={},response={}", user.getUsername(), response);

		} catch (Exception e) {
			logger.error("监听配送员信息同步发生未知异常", e);
			
			// 把未完成MQ插入到数据库中, start
			String functionName = "userMonitor";
			String fromUri = MQ_FROM_URI_USER_MONITOR;
			String body = null;
			String headerName = MQ_HEADER_NAME_USER_MONITOR;
			String headerValue = parm;
			String exceptionMessage = e.getMessage();
			
			//消费MQ异常表
			this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode(functionName)
					.buildExceptionInfo(exceptionMessage).buildTopic(fromUri)
					.buildMessageHeader(headerName, headerValue).buildMessageSource(MessageSourceEnum.receiver.getIndex()).getMqException());
			
			// 把未完成MQ插入到数据库中, end
		}
	}

	private String getObjectMethod(int key) {
		JointEntity obj = null;
		String posValue = "";
		try {
			obj = jointDAO.getJointEntity(key);
			posValue = obj.getJoint_property();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return posValue;
	}

	public Mobiledcb getMobiledcb(int key) {
		Mobiledcb alipay = new Mobiledcb();
		if (!"".equals(getObjectMethod(key))) {
			JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
			alipay = (Mobiledcb) JSONObject.toBean(jsonObj, Mobiledcb.class);
		} else {
			alipay = new Mobiledcb();
		}

		return alipay == null ? new Mobiledcb() : alipay;
	}

	/**
	 * 新疆大晨报客户端配送员信息同步接口
	 */
	public void UserInfo_synchronzed() {

		int isOpenFlag = jointDAO.getStateByJointKey(PosEnum.MobileApp_dcb.getKey());
		if (isOpenFlag == 0) {
			logger.warn("未开启新疆大晨报对接");
			return;
		}

		Mobiledcb mobiledbc = this.getMobiledcb(PosEnum.MobileApp_dcb.getKey());

		List<User> userList = userDAO.getAllUser();
		if (userList == null || userList.size() == 0) {
			return;
		}

		for (User user : userList) {

			try {
				String response = buildparamsAndSend(mobiledbc.getSender_usersyn(), user);
				logger.info("新疆大晨报派送员信息同步username={},response={}", user.getUsername(), response);
			} catch (Exception e) {
				logger.error("新疆大晨报派送员信息同步发生异常user=" + user.getUsername(), e);
			}

		}

	}

	/**
	 * 发送
	 * 
	 * @param mobiledbc
	 * @param user
	 * @return
	 */
	private String buildparamsAndSend(String url, User user) {
		Map<String, String> parmsMap = buildRequestParams(user);

		String response = RestHttpServiceHanlder.sendHttptoServer(parmsMap, url);

		return response;
	}

	private Map<String, String> buildRequestParams(User user) {
		Map<String, String> parmsMap = new HashMap<String, String>();

		parmsMap.put("userid", String.valueOf(user.getUserid()));
		parmsMap.put("username", user.getUsername());
		parmsMap.put("password", user.getPassword());
		parmsMap.put("realname", user.getRealname());
		parmsMap.put("idcardno", user.getIdcardno());
		parmsMap.put("employeestatus", String.valueOf(user.getEmployeestatus()));
		parmsMap.put("usermobile", user.getUsermobile());
		parmsMap.put("useremail", user.getUseremail());
		return parmsMap;
	}

}
