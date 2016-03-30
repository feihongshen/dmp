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
import org.springframework.transaction.annotation.Transactional;

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
import cn.explink.domain.User;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.service.CustomerService;
import cn.explink.util.DateTimeUtil;

@Service
public class DongFangCJService_search {
	private Logger logger = LoggerFactory.getLogger(DongFangCJService_search.class);

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
	@Autowired
	CustomerService customerService;

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

	@Transactional
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
		this.customerService.initCustomerList();

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
	public String requestCwbSearchInterface(String wb_no, DongFangCJ cj) throws Exception {

		try {

			if (wb_no == null || wb_no.equals("")) {
				return "请求参数billcode不能为空";
			}
			StringBuffer sub = new StringBuffer("<?xml version=\"1.0\" encoding=\"gb2312\" ?>");

			sub.append("<ocj>");
			sub.append("<delivery_list>");
			for (String cwb : wb_no.split("$")) {
				sub = buildDongFangCJHeads(sub, cwb);
			}
			sub.append("</delivery_list>");
			sub.append("<sign>fhsuejbhgfgw7sy75sf77s7d76fjaj</sign>");
			sub.append("</ocj>");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	private StringBuffer buildDongFangCJHeads(StringBuffer sub, String cwb) {
		CwbOrder cwborder = cwbDAO.getCwbByCwb(cwb);
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
			long delivery_state = deliveryStateDAO.getActiveDeliveryStateByCwb(orderFlow.getCwb()).getDeliverystate();
			if (getDongFangCJFlowText(orderFlow.getFlowordertype(), orderFlow.getCwb(), delivery_state) == null) {
				continue;
			}

			User user = userDAO.getUserByUserid(orderFlow.getUserid());
			Branch branch = branchDAO.getBranchByBranchid(user.getBranchid());
			sub.append("<wb_info_list>");
			sub.append("<wb_info_seq>" + (i + 1) + "</wb_info_seq>");
			sub.append("<wb_info_step_code>" + orderFlow.getFlowordertype() + "</wb_info_step_code>");
			sub.append("<wb_info_step>" + getDongFangCJFlowText(orderFlow.getFlowordertype(), orderFlow.getCwb(), delivery_state) + "</wb_info_step>");
			sub.append("<wb_info_date>" + DateTimeUtil.formatDate(orderFlow.getCredate()) + "</wb_info_date>");
			sub.append("<wb_info_staff>" + user.getRealname() + "</wb_info_staff>");
			sub.append("<wb_info_tel>" + user.getUsermobile() + "</wb_info_tel>");
			sub.append("<wb_info_hp>" + user.getUsermobile() + "</wb_info_hp>");
			sub.append("<wb_info_photo>" + explinkService.getDetail(orderFlow) + "</wb_info_photo>");
			sub.append("<wb_info_addr>" + branch.getBranchname() + "</wb_info_addr>");
			sub.append("<wb_info_terminal></wb_info_terminal>");
			sub.append("<remark>" + getExptReason(delivery_state, cwborder) + "</remark>");
			sub.append("<wb_info_list>");
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
