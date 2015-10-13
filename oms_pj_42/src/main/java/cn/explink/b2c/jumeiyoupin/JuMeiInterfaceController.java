package cn.explink.b2c.jumeiyoupin;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.GetDmpDAO;
import cn.explink.dao.OrderSelectDAO;
import cn.explink.util.MD5.MD5Util;

@Controller
@RequestMapping("/explink")
public class JuMeiInterfaceController {

	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	GetDmpDAO getDmpDAO;
	@Autowired
	OrderSelectDAO orderSelectDao;
	@Autowired
	JumeiInterfaceService jumeiInterfaceService;

	private Logger logger = LoggerFactory.getLogger(JuMeiInterfaceController.class);

	@RequestMapping("/jumei")
	public @ResponseBody String login(HttpServletResponse response, HttpServletRequest request) {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		JuMeiYouPin juMeiYouPin = getJuMeiYouPinSettingMethod(B2cEnum.JuMeiYouPin.getKey()); // 获取配置信息
		if (!isJuMeiOpen(B2cEnum.JuMeiYouPin.getKey())) {
			logger.info("[聚美优品推送]聚美对接并未开启！");
			return null;
		}
		String postXml = request.getParameter("exceptionReply");
		logger.info("[聚美优品推送]推送异常订单处理数据：{}", postXml);
		String xmlstr = "";
		try {
			xmlstr = new String(postXml.getBytes("iso-8859-1"), "GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 新增三个参数
		String cwbinfo = request.getParameter("cwb");
		String unixstamp = request.getParameter("unixstamp");
		String sign = request.getParameter("sign");
		logger.info("[聚美优品推送]接收的xml信息：" + xmlstr);
		logger.info("[聚美优品推送]接收的cwb：" + cwbinfo);
		logger.info("[聚美优品推送]接收的unixstamp：" + unixstamp);
		logger.info("[聚美优品推送]接收的sign：" + sign);
		// logResult(xmlstr);
		// 加密
		String checkStr = cwbinfo + unixstamp;
		String MD5String = MD5Util.md5(checkStr);
		String MD5Data = MD5Util.md5(MD5String + juMeiYouPin.getPrivate_key());
		logger.info("接收的MD5：" + MD5Data);
		return jumeiInterfaceService.returnJumeiXml(xmlstr, cwbinfo, unixstamp, MD5Data, sign);

	}

	public JuMeiYouPin getJuMeiYouPinSettingMethod(int key) {
		JuMeiYouPin juMeiYouPin = null;
		if (this.getObjectMethod(key) != null) {
			JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
			juMeiYouPin = (JuMeiYouPin) JSONObject.toBean(jsonObj, JuMeiYouPin.class);
		} else {
			juMeiYouPin = new JuMeiYouPin();
		}

		return juMeiYouPin;
	}

	public String getObjectMethod(int key) {
		JointEntity obj = new JointEntity();
		String propertity = "";
		try {
			obj = getDmpDAO.getJointEntity(key);
			propertity = obj.getJoint_property();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return propertity;
	}

	private boolean isJuMeiOpen(int key) {
		try {
			JointEntity obj = getDmpDAO.getJointEntity(key);
			return obj.getState() != 0;
		} catch (Exception e) {
			logger.warn("error while getting jumei status with key {}, will defualt false", key);
			e.printStackTrace();
		}
		return false;
	}

}