package cn.explink.b2c.lefeng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.explink.ExplinkService;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.User;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.CwbFlowOrderTypeEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.service.CwbOrderWithDeliveryState;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.MD5.MD5Util;

@Service
public class LefengService {
	private Logger logger = LoggerFactory.getLogger(LefengService.class);

	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	ExplinkService explinkService;
	@Autowired
	UserDAO userDAO;

	protected static ObjectMapper jacksonmapper = JacksonMapper.getInstance();

	public String getObjectMethod(int key) {
		JointEntity obj = jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}

	public LefengT getLefengT(int key) {
		if (getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
		LefengT smile = (LefengT) JSONObject.toBean(jsonObj, LefengT.class);
		return smile;
	}

	public void edit(HttpServletRequest request, int joint_num) {
		LefengT lefeng = new LefengT();
		String customerids = request.getParameter("customerids");
		lefeng.setCustomerids(customerids);
		lefeng.setAppkey(request.getParameter("appkey"));
		lefeng.setCode(request.getParameter("code"));
		lefeng.setSearch_url(request.getParameter("search_url"));
		int issignflag = Integer.valueOf(request.getParameter("issignflag"));
		lefeng.setCompanyname(request.getParameter("companyname"));
		lefeng.setCompanyphone(request.getParameter("companyphone"));
		lefeng.setWebsite(request.getParameter("website"));

		lefeng.setIssignflag(issignflag);

		String oldCustomerids = "";

		JSONObject jsonObj = JSONObject.fromObject(lefeng);
		JointEntity jointEntity = jiontDAO.getJointEntity(joint_num);
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Create(jointEntity);
		} else {
			try {
				oldCustomerids = getLefengT(joint_num).getCustomerids();
			} catch (Exception e) {
			}
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Update(jointEntity);
		}
		// 保存 枚举到供货商表中
		customerDAO.updateB2cEnumByJoint_num(customerids, oldCustomerids, joint_num);
	}

	public void update(int joint_num, int state) {
		jiontDAO.UpdateState(joint_num, state);
	}

	ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * 乐蜂网请求接口
	 * 
	 * @return
	 */
	public String requestMethod(LefengT lefeng, String cwb, String code, String key) {
		try {
			logger.info("[乐蜂网]请求跟踪信息cwb={},code={},key=" + key, cwb, code);
			String local_key = MD5Util.md5(cwb + lefeng.getAppkey());
			ValidateBaseLogic(lefeng, code, key, local_key);

			LefengResponse resp = buildTrackInfoEntity(lefeng, cwb); // 构建实体
			if (resp == null) {
				throw new RuntimeException("未检索到数据");
			}
			resp.setResponseStatus("OK");
			String responseXML = objectMapper.writeValueAsString(resp);
			logger.info("查询[乐蜂网]订单跟踪返回XML={}", responseXML);
			return responseXML;

		} catch (Exception e) {
			logger.error("处理[乐蜂网]请求发送未知异常,cwb=" + cwb, e);
			return getResponseErrorMsg("未知异常" + e.getMessage(), "ERROR");
		}

	}

	public String getResponseErrorMsg(String errorMessage, String status) {
		LefengResponse lfw = new LefengResponse();
		lfw.setErrorMessage(errorMessage);
		lfw.setResponseStatus(status);
		lfw.setResult(null);
		try {
			return objectMapper.writeValueAsString(lfw);
		} catch (Exception e) {
			logger.error("返回[乐蜂网]转换JSON格式发生未知异常", e);
			return null;
		}
	}

	private void ValidateBaseLogic(LefengT lefeng, String code, String key, String local_key) {
		if (!lefeng.getCode().equals(code)) {
			logger.error("请求code={}不正确,正确code={}", code, lefeng.getCode());
			throw new RuntimeException("请求code不正确");
		}

		if (lefeng.getIssignflag() == 0) {
			return;
		}

		if (!local_key.equals(key)) {
			logger.error("请求签名验证失败,key={},local_key={}", key, local_key);
			throw new RuntimeException("请求签名验证失败");
		}

	}

	private LefengResponse buildTrackInfoEntity(LefengT lfw, String cwb) throws Exception {

		LefengResponse lfwresp = new LefengResponse();
		List<OrderFlow> tracklist = orderFlowDAO.getOrderFlowByCwb(cwb);
		if (tracklist != null && tracklist.size() > 0) {
			result rst = new result();
			OrderFlow orderFlow1 = tracklist.get(0);
			rst.setExpressNumber(orderFlow1.getCwb());
			rst.setStatus("Delivery");
			String status = "";
			List<TrackingItems> items = new ArrayList<TrackingItems>();
			for (OrderFlow orderFlow : tracklist) {

				int trackstatus = orderFlow.getFlowordertype();
				String trackevent = explinkService.getDetail(orderFlow);
				String trackdatetime = DateTimeUtil.formatDate(orderFlow.getCredate());
				if (getLefengStatus(trackstatus, orderFlow) == null) {
					continue;
				}
				TrackingItems lfw3 = new TrackingItems();
				lfw3.setInformation(trackevent);
				lfw3.setTime(trackdatetime);
				items.add(lfw3);
				status = getFinanceStatus(status, trackstatus); // 每次总是获取最大的status
			}
			rst.setStatus(status);
			rst.setTrackingItems(items);
			lfwresp.setResult(rst);
			return lfwresp;
		}
		return null;
	}

	public String getLefengStatus(int status, OrderFlow orderFlow) throws Exception {
		for (LefengTrackEnum e : LefengTrackEnum.values()) {
			if (orderFlow.getFlowordertype() == CwbFlowOrderTypeEnum.YiShenHe.getValue()) {
				CwbOrderWithDeliveryState cwbOrderWithDeliveryState = objectMapper.readValue(orderFlow.getFloworderdetail(), CwbOrderWithDeliveryState.class);
				int deliverystate = DeliveryStateEnum.getByValue((int) cwbOrderWithDeliveryState.getDeliveryState().getDeliverystate()).getValue();
				if (deliverystate == DeliveryStateEnum.PeiSongChengGong.getValue() || deliverystate == DeliveryStateEnum.ShangMenHuanChengGong.getValue()
						|| deliverystate == DeliveryStateEnum.ShangMenTuiChengGong.getValue()) {
					return LefengTrackEnum.Received.getLfw_code();
				}
				if (deliverystate == DeliveryStateEnum.JuShou.getValue() || deliverystate == DeliveryStateEnum.BuFenTuiHuo.getValue()) {
					return LefengTrackEnum.DeliveryFaild.getLfw_code();
				}
				if (deliverystate == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
					return LefengTrackEnum.DeliveryExpt.getLfw_code();
				}

				return null;
			}

			if (e.getOnwer_code() == status) {
				return e.getLfw_code();
			}
		}

		return null;
	}

	private String getFinanceStatus(String status, int trackstatus) {
		if (trackstatus == LefengTrackEnum.Deliverying.getOnwer_code() && status.equals("")) {
			return LefengTrackEnum.Deliverying.getLfw_code();
		}
		if (trackstatus == LefengTrackEnum.Received.getOnwer_code()) {
			return LefengTrackEnum.Received.getLfw_code();
		}
		if (trackstatus == LefengTrackEnum.DeliveryFaild.getOnwer_code()) {
			return LefengTrackEnum.DeliveryFaild.getLfw_code();
		}

		return null;
	}

	public LefengResponse getResponseEntity(String errorMessage, String status, result lfwR) {
		LefengResponse lfw = new LefengResponse();
		lfw.setErrorMessage(errorMessage);
		lfw.setResponseStatus(status);
		lfw.setResult(lfwR);
		return lfw;
	}

	/**
	 * 根据订单查询 跟踪信息 并返回乐蜂 需要的环节，过滤不必要的 封装为List<Map<String,String>>对象
	 * 
	 * @param cwb
	 * @return
	 */
	public String getCwbOrderFlowByLefeng(String cwb) {
		String returnMaps = null;
		try {
			List<Map<String, String>> maplist = new ArrayList<Map<String, String>>();
			List<OrderFlow> orderflowlist = orderFlowDAO.getOrderFlowByCwb(cwb);
			for (OrderFlow orderFlow : orderflowlist) {
				int trackstatus = orderFlow.getFlowordertype();
				if (getLefengStatus(trackstatus, orderFlow) == null) {
					continue;
				}
				CwbOrderWithDeliveryState cwbOrderWithDeliveryState = objectMapper.readValue(orderFlow.getFloworderdetail(), CwbOrderWithDeliveryState.class);
				long operatorid = 0;
				if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
					operatorid = cwbOrderWithDeliveryState.getDeliveryState().getDeliveryid();
				} else {
					operatorid = orderFlow.getUserid();
				}

				User users = userDAO.getUserByUserid(operatorid);

				String trackdatetime = DateTimeUtil.formatDate(orderFlow.getCredate());
				String flowordertype = String.valueOf(orderFlow.getFlowordertype());
				String deliverystate = "0";

				if (orderFlow.getFlowordertype() > FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
					deliverystate = String.valueOf(cwbOrderWithDeliveryState.getDeliveryState().getDeliverystate());
				}

				String orderflowdetail = explinkService.getDetail(orderFlow);
				String realname = users.getRealname();
				String usermobile = users.getUsermobile();
				Map<String, String> map = new HashMap<String, String>();
				map.put("flowordertype", flowordertype);
				map.put("trackdatetime", trackdatetime);
				map.put("deliverystate", deliverystate);
				map.put("orderflowdetail", orderflowdetail);
				map.put("realname", realname);
				map.put("usermobile", usermobile);
				map.put("cwb", cwb);

				maplist.add(map);
			}

			returnMaps = jacksonmapper.writeValueAsString(maplist);

		} catch (Exception e) {
			logger.error("构建乐蜂map对象异常", e);
		}

		return returnMaps;
	}

}
