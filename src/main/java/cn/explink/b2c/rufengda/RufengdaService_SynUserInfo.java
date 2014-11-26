package cn.explink.b2c.rufengda;

import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.dao.UserDAO;
import cn.explink.domain.User;
import cn.explink.util.WebServiceHandler;

/**
 * 如风达 配送员信息同步
 * 
 * @author Administrator
 *
 */
@Service
public class RufengdaService_SynUserInfo extends RufengdaService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	ObjectMapper objectMapper = new ObjectMapper();

	/***
	 * 配送员信息同步的方法 定时器1周启动一次。
	 * ====================================================
	 * ==============================
	 * 
	 * @throws Exception
	 */

	public long SynUserInfo(int rfd_key) {

		Rufengda rfd = super.getRufengda(rfd_key);

		try {
			List<User> userList = userDAO.getAllUser();
			for (User user : userList) {

				SynUserInfo userInfo = new SynUserInfo();
				userInfo.setRps_Name(user.getRealname());
				userInfo.setRps_Code(user.getUserid() + "");
				userInfo.setRps_Sex("男");
				userInfo.setRps_IdentifyCode(user.getIdcardno());
				userInfo.setRps_EmailAddress(user.getUseremail() == null ? "未设置" : user.getUseremail());
				userInfo.setRps_Phone(user.getUsermobile());
				String objectValue = objectMapper.writeValueAsString(userInfo);
				objectValue = objectValue.replaceAll("rps_", "");

				Object parms[] = { rfd.getLcId(), objectValue };
				String deliverManCode = (String) WebServiceHandler.invokeWs(rfd.getWs_url(), "SynUserInfo", parms);
				logger.info("发送配送员信息同步接口，请求parms={},returnValue={}", objectValue, deliverManCode);

				userDAO.updateUserCode(deliverManCode, user.getUserid());

			}
			return userList.size();

		} catch (Exception e) {
			logger.error("发送配送员信息同步接口发生不可预知的异常", e);
			return 0;
		}

	}

}
