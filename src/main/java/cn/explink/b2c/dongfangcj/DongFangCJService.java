package cn.explink.b2c.dongfangcj;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.explink.ExplinkService;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.User;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.MD5.MD5Util;

@Service
public class DongFangCJService {
	private Logger logger = LoggerFactory.getLogger(DongFangCJService.class);

	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	DataImportService_B2c dataImportInterface;
	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	ExplinkService explinkService;
	@Autowired
	BranchDAO branchDAO;

	public String getObjectMethod(int key) {
		JointEntity obj = jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}

	public DongFangCJ getDongFangCJ(int key) {
		if (getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
		DongFangCJ smile = (DongFangCJ) JSONObject.toBean(jsonObj, DongFangCJ.class);
		return smile;
	}

	public void edit(HttpServletRequest request, int joint_num) {
		DongFangCJ cj = new DongFangCJ();
		String customerids = request.getParameter("customerids");
		cj.setCustomerids(customerids);
		cj.setFtp_host(request.getParameter("ftp_host"));
		cj.setFtp_username(request.getParameter("ftp_username"));
		cj.setFtp_password(request.getParameter("ftp_password"));
		cj.setFtp_port(Integer.valueOf(request.getParameter("ftp_port")));
		cj.setCharencode(request.getParameter("charencode"));
		cj.setPut_remotePath(request.getParameter("put_remotePath")); // 下载
		cj.setGet_remotePath(request.getParameter("get_remotePath")); // 上传
		cj.setDownloadPath(request.getParameter("downloadPath"));
		cj.setDownloadPath_bak(request.getParameter("downloadPath_bak"));
		cj.setUploadPath(request.getParameter("uploadPath"));
		cj.setUploadPath_bak(request.getParameter("uploadPath_bak"));
		cj.setMaxcount(request.getParameter("maxcount"));
		cj.setCustomerids(request.getParameter("customerids"));
		cj.setWarehouseid(Long.valueOf(request.getParameter("warehouseid")));
		cj.setPartener(request.getParameter("partener"));

		cj.setIsdelDirFlag(request.getParameter("isdelDirFlag").equals("1") ? true : false);

		cj.setSearch_url(request.getParameter("search_url"));
		cj.setCompany_num(request.getParameter("company_num"));
		cj.setPrivate_key(request.getParameter("private_key"));

		String oldCustomerids = "";

		JSONObject jsonObj = JSONObject.fromObject(cj);
		JointEntity jointEntity = jiontDAO.getJointEntity(joint_num);
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Create(jointEntity);
		} else {
			try {
				oldCustomerids = getDongFangCJ(joint_num).getCustomerids();
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

	public int getStateForYihaodian(int key) {
		JointEntity obj = null;
		int state = 0;
		try {
			obj = jiontDAO.getJointEntity(key);
			state = obj.getState();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return state;
	}

	/**
	 * 东方CJ webservice查询接口
	 */

	/**
	 * 京东请求接口开始
	 * 
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 */
	public String requestCwbSearchInterface(String wb_no, String sign, DongFangCJ cj) throws Exception {

		try {

			if (wb_no == null || wb_no.equals("")) {
				return "请求参数wb_no不能为空!";
			}
			if (sign == null || sign.equals("")) {
				return "请求参数sign不能为空!";
			}

			String local_sign = MD5Util.md5(wb_no.split("$")[0] + cj.getCompany_num() + cj.getPrivate_key());
			if (!local_sign.equalsIgnoreCase(sign)) {
				logger.warn("东方CJ请求签名验证失败,sign={},local_sign={}", sign, local_sign);
				return "请求签名验证失败!";
			}
			String cwb = wb_no.split("$")[0];
			CwbOrder cwborder = cwbDAO.getCwbByCwb(cwb);
			if (cwborder == null) {
				logger.warn("东方CJ请求未检索到数据cwb={}", cwb);
				return "查无此单" + cwb;
			}
			StringBuffer sub = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");

			sub.append("<ocj>");
			sub.append("<delivery_list>");

			sub = buildDongFangCJHeads(sub, cwb, cwborder);

			sub.append("</delivery_list>");
			sub.append("</ocj>");

			logger.info("查询返回东方CJ xml={}", sub.toString());
			return sub.toString();

		} catch (Exception e) {
			logger.error("东方CJ请求查询接口发生未知异常cwb=" + wb_no, e);
			return "未知异常";
		}

	}

	private StringBuffer buildDongFangCJHeads(StringBuffer sub, String cwb, CwbOrder cwborder) {

		sub.append("<wb_no>" + cwb + "</wb_no>");
		sub.append("<receiver>" + cwborder.getConsigneename() + "</receiver>");
		List<OrderFlow> orderlist = orderFlowDAO.getOrderFlowByCwb(cwb);
		if (orderlist == null || orderlist.size() == 0) {
			logger.info("0东方CJ0当前订单没有跟踪详情!cwb={}", cwb);
			return sub;
		}
		sub = buildDongFangCJFlowOrderList(sub, orderlist, cwborder);
		return sub;
	}

	private StringBuffer buildDongFangCJFlowOrderList(StringBuffer sub, List<OrderFlow> orderlist, CwbOrder cwborder) {
		int i = 0;
		for (OrderFlow orderFlow : orderlist) {

			DeliveryState deliveryState = deliveryStateDAO.getActiveDeliveryStateByCwb(orderFlow.getCwb());

			if (getDongFangCJFlowText(orderFlow.getFlowordertype(), orderFlow.getCwb(), deliveryState == null ? 0 : deliveryState.getDeliverystate()) == null) {
				continue;
			}

			User user = userDAO.getUserByUserid(orderFlow.getUserid());
			Branch branch = branchDAO.getBranchByBranchid(user.getBranchid());

			String step_code = String.valueOf(orderFlow.getFlowordertype());
			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue() && deliveryState != null && deliveryState.getDeliveryid() > 0) {
				step_code = "36_" + deliveryState.getDeliverystate();
				if (step_code.contains("_2") || step_code.contains("_3")) {
					step_code = "36_1"; // 所有配送成功 默认为 1
				}
			}

			sub.append("<wb_info_list>");
			sub.append("<wb_info_seq>" + (i + 1) + "</wb_info_seq>");
			sub.append("<wb_info_step_code>" + step_code + "</wb_info_step_code>");
			sub.append("<wb_info_step>" + getDongFangCJFlowText(orderFlow.getFlowordertype(), orderFlow.getCwb(), deliveryState == null ? 0 : deliveryState.getDeliverystate()) + "</wb_info_step>");
			sub.append("<wb_info_date>" + DateTimeUtil.formatDate(orderFlow.getCredate()) + "</wb_info_date>");
			sub.append("<wb_info_staff>" + user.getRealname() + "</wb_info_staff>");
			sub.append("<wb_info_tel>" + user.getUsermobile() + "</wb_info_tel>");
			sub.append("<wb_info_hp>" + user.getUsermobile() + "</wb_info_hp>");
			sub.append("<wb_info_photo>" + explinkService.getDetail(orderFlow) + "</wb_info_photo>");
			sub.append("<wb_info_addr>" + branch.getBranchname() + "</wb_info_addr>");
			sub.append("<wb_info_terminal></wb_info_terminal>");
			sub.append("<remark>"
					+ (orderFlow.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue() ? getExptReason(deliveryState == null ? 0 : deliveryState.getDeliverystate(), cwborder) : "")
					+ "</remark>");
			sub.append("</wb_info_list>");
			i++;
		}
		return sub;
	}

	public String getExptReason(long delivery_state, CwbOrder order) {
		if (delivery_state == DeliveryStateEnum.JuShou.getValue()) {
			return order.getBackreason();
		}
		if (delivery_state == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
			return order.getLeavedreason();
		}
		return "";

	}

	public String getDongFangCJFlowText(long flowordertype, String cwb, long delivery_state) {
		for (DongFangCJFlowEnum dd : DongFangCJFlowEnum.values()) {
			if (flowordertype != FlowOrderTypeEnum.YiShenHe.getValue() && flowordertype == dd.getFlowordertype()) {
				return dd.getText();
			}
			if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue()) {

				if (delivery_state == DeliveryStateEnum.PeiSongChengGong.getValue() || delivery_state == DeliveryStateEnum.ShangMenTuiChengGong.getValue()
						|| delivery_state == DeliveryStateEnum.ShangMenTuiChengGong.getValue()) {
					return "配送结束";
				}
				if (delivery_state == DeliveryStateEnum.JuShou.getValue()) {
					return "拒收";
				}
				if (delivery_state == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
					return "商品再配";
				}

			}

		}

		return null;

	}

}
