package cn.explink.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Service;

import cn.explink.dao.AppearWindowDao;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.MiddleAppearDao;
import cn.explink.dao.OperationSetTimeDAO;
import cn.explink.dao.OperationTimeDAO;
import cn.explink.domain.OperationSetTime;
import cn.explink.domain.OperationTime;
import cn.explink.domain.User;
import cn.explink.domain.WindowShow;
import cn.explink.enumutil.FrameEnumExcp;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.util.DateTimeUtil;

/**
 * 即时消息Service
 *
 */
@Service
public class AppearWindowService {
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	@Autowired
	OperationSetTimeDAO operationSetTimeDAO;
	@Autowired
	OperationTimeDAO operationTimeDAO;
	@Autowired
	MiddleAppearDao middleAppearDao;
	@Autowired
	AppearWindowDao appearWindowDao;
	private Logger logger = LoggerFactory.getLogger(AppearWindowService.class);
	protected static ObjectMapper jacksonmapper = JacksonMapper.getInstance();

	/**
	 * 得到当前用户
	 *
	 * @return
	 */
	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	/**
	 * 进入消息列表主要功能
	 */
	public void getAppearInfoShow() {
		this.logger.info("进入消息列表......");

		// 1.查询得到用户监听表
		List<OperationSetTime> slist = this.operationSetTimeDAO.getOperationSetTime();
		if (slist.isEmpty()) {
			this.logger.info("没有用户定制消息列表功能");
			return;
		}
		// 2.轮偱用户监听表
		StringBuffer sb = new StringBuffer();
		for (OperationSetTime s : slist) {
			this.logger.info("用户:{},监控时间名称：{}", s.getUserid(), s.getName());
			long countYicunzai = this.appearWindowDao.getStateAndUserid(s.getUserid(), "1");
			if (countYicunzai > 0) {
				continue;
			}
			String timeShow = s.getValue();// 得到时间规则
			// 3.声明一个map，用来存储时间规则
			Map<String, Integer> timmerMap = new HashMap<String, Integer>();
			try {
				// 4.遍历每个客户的时间规定
				timmerMap = AppearWindowService.jacksonmapper.readValue(timeShow, Map.class);
				long A1 = timmerMap.get("A1") == null ? 0 : timmerMap.get("A1");
				long A2 = timmerMap.get("A2") == null ? 0 : timmerMap.get("A2");
				long A3 = timmerMap.get("A3") == null ? 0 : timmerMap.get("A3");
				long A4 = timmerMap.get("A4") == null ? 0 : timmerMap.get("A4");
				long A5 = timmerMap.get("A5") == null ? 0 : timmerMap.get("A5");
				long A6 = timmerMap.get("A6") == null ? 0 : timmerMap.get("A6");
				long A7 = timmerMap.get("A7") == null ? 0 : timmerMap.get("A7");
				long A8 = timmerMap.get("A8") == null ? 0 : timmerMap.get("A8");
				long A9 = timmerMap.get("A9") == null ? 0 : timmerMap.get("A9");
				long A10 = timmerMap.get("A10") == null ? 0 : timmerMap.get("A10");

				A1 = System.currentTimeMillis() - (A1 * 60 * 60 * 1000);
				A2 = System.currentTimeMillis() - (A2 * 60 * 60 * 1000);
				A3 = System.currentTimeMillis() - (A3 * 60 * 60 * 1000);
				A4 = System.currentTimeMillis() - (A4 * 60 * 60 * 1000);
				A5 = System.currentTimeMillis() - (A5 * 60 * 60 * 1000);
				A6 = System.currentTimeMillis() - (A6 * 60 * 60 * 1000);
				A7 = System.currentTimeMillis() - (A7 * 60 * 60 * 1000);
				A8 = System.currentTimeMillis() - (A8 * 60 * 60 * 1000);
				A9 = System.currentTimeMillis() - (A9 * 60 * 60 * 1000);
				A10 = System.currentTimeMillis() - (A10 * 60 * 60 * 1000);

				List list = new ArrayList();
				list.add(0, A1);
				list.add(1, A2);
				list.add(2, A3);
				list.add(3, A4);
				list.add(4, A5);
				list.add(5, A6);
				list.add(6, A7);
				list.add(7, A8);
				list.add(8, A9);
				list.add(9, A10);

				// 声明一个list存储符合用户制定 的时间规则的订单的cwbs.
				List<OperationTime> oplist = new ArrayList<OperationTime>();

				for (int i = 0; i < 10; i++) {
					List<OperationTime> excpCount = this.operationTimeDAO.countUserException((Long) list.get(i), FrameEnumExcp.getText(i), i);
					oplist.addAll(excpCount);
				}
				for (OperationTime op : oplist) {
					long count = this.middleAppearDao.getInfoCount(1, s.getUserid(), "1", op.getCwb());// 未发送
					if (count == 0) {
						this.middleAppearDao.creMiddleTime(op.getCwb(), "1", s.getUserid(), "1");// 新加入中间表一条数据
					}
					sb.append("'");
					sb.append(op.getCwb());
					sb.append("',");
				}

				// 中间表插入消息表
				long count = this.middleAppearDao.getInfoCount(1, s.getUserid(), "1", "");
				if (count > 0) {
					this.logger.info("监控" + count);
					long a = this.appearWindowDao.getCountByStateAndUserid(s.getUserid(), "1");
					String jsonInfo = "Sum:" + count;
					if (a > 0) {// 存在,update
						this.appearWindowDao.updateByStateAndUserid(jsonInfo, s.getUserid());
					} else {
						this.appearWindowDao.creWindowTime(jsonInfo, "1", s.getUserid(), "1");
					}
				}

			} catch (Exception e) {
				this.logger.info("消息列表出现异常" + e);
				e.printStackTrace();
			}
		}
		if (sb.toString().length() > 0) {
			this.logger.info("执行update 字段isupdate.......");
			String time = DateTimeUtil.getNowTime();
			this.operationTimeDAO.updateUserException(time, sb.toString().equals("") ? "" : sb.substring(0, sb.length() - 1).toString());
		}
	}

	/**
	 * 查询消息表符合state=1的该用户，弹出
	 *
	 * @return
	 */
	public String getAppearNewinfo() {
		long userid = this.getSessionUser().getUserid();
		List<WindowShow> winList = this.appearWindowDao.getCountWindowByState(userid);
		List<WindowShow> winListKf = null;
		if (this.getSessionUser().getRoleid() == 1) {
			winListKf = this.appearWindowDao.getCountWindowByType("'5'");
		}
		if ((winListKf != null) && (winListKf.size() > 0)) {
			winList.addAll(winListKf);
		}
		if (winList.isEmpty()) {
			return "";
		}
		String json;
		try {
			json = JacksonMapper.getInstance().writeValueAsString(winList);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			this.logger.info(" 查询消息出现异常" + e);
			return "";
		}
	}

	/**
	 * 点击消息则失效消息表中该条数据
	 *
	 * @param type
	 * @return
	 */

	public String getchangeState(String type) {
		long userid = this.getSessionUser().getUserid();
		this.logger.info("点击{}......删除此消息", userid);
		String a = DateTimeUtil.getNowTime();
		long resulta = this.appearWindowDao.updateByUserid(a, userid);
		long resultb = this.middleAppearDao.updateByUserid(a, userid, type);
		if ((resulta == 1) && (resultb == 1)) {
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		} else {
			return "{\"errorCode\":1,\"error\":\"系统错误\"}";
		}

	}

}
