package cn.explink.b2c.explink;

import java.util.List;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2cDataOrderFlowDetail;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.GetDmpDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.MD5.MD5Util;

/**
 * 提供给供方查询订单跟踪日志的接口，仅支持单个订单查询.
 * 
 * @author Administrator
 *
 */
@Service
public class ExplinkService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	GetDmpDAO getDmpDAO;
	@Autowired
	B2cDataOrderFlowDetail orderFlowDetail;

	private Explink getExplinkByCompany(String companyname) {
		JointEntity joint = getDmpDAO.getJointEntityByCompanyname(companyname);
		if (joint != null && joint.getJoint_property() != null && !joint.getJoint_property().equals("")) {
			JSONObject jsonO = JSONObject.fromObject(joint.getJoint_property());
			return (Explink) JSONObject.toBean(jsonO, Explink.class);
		}
		return null;
	}

	public String getCwbStatusInterface(String cwbs, String companyname, String sign) {
		Explink explink = getExplinkByCompany(companyname);

		try {
			ValidatorBusinessMethod(cwbs, companyname, sign, explink);
		} catch (RuntimeException e) {
			logger.error("请求explink发生异常,订单号=" + cwbs + "," + e.getMessage());
			return respExptMsg(e.getMessage());
		}

		List<OrderFlow> list = orderFlowDAO.getOrderFlowByCwb(cwbs);
		if (list == null || list.size() == 0) {
			return respExptMsg("没有检索到数据");
		}

		return parseCwbStatusXML(list);

	}

	public String getExplinkFlowEnum(long flowordertype) {
		for (ExplinkFlowEnum dd : ExplinkFlowEnum.values()) {
			if (flowordertype == dd.getFlowordertype()) {
				return dd.getFlowordertype() + "";
			}
		}

		return null;

	}

	private String parseCwbStatusXML(List<OrderFlow> list) {
		StringBuffer responesXML = new StringBuffer();
		responesXML.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		responesXML.append("<Result>");
		for (int i = 0; i < list.size(); i++) {
			OrderFlow orderflow = list.get(i);
			if (getExplinkFlowEnum(orderflow.getFlowordertype()) == null) {
				continue;
			}
			responesXML.append("<row resultcount=\"" + (i + 1) + "\">");
			responesXML.append("<cwb>" + orderflow.getCwb() + "</cwb>");// <!--订单号
																		// -->
			responesXML.append("<trackdatetime>" + DateTimeUtil.formatDate(orderflow.getCredate()) + "</trackdatetime>");// <!--操作时间
																															// -->
			responesXML.append("<branchname>" + getDmpDAO.getNowBranch(orderflow.getBranchid()).getBranchname() + "</branchname>");// <!--站点
																																	// -->
			responesXML.append("<trackevent>" + orderFlowDetail.getDetailByOrderFlow(orderflow) + "</trackevent>");// <!--跟踪信息
																													// -->
			responesXML.append("<podresultname>" + getFlowOrderTextById(orderflow.getFlowordertype()) + "</podresultname>");// <!--订单状态
																															// -->
			responesXML.append("</row>");
		}
		responesXML.append("</Result>");
		return responesXML.toString();

	}

	// private String ConvertCwbStrs(String cwbs) {
	// StringBuffer cwbStr = new StringBuffer();
	// if(cwbs.trim().length()>0){
	// for(String cwb:cwbs.split(",")){
	// cwbStr.append("'"+cwb.trim());
	// cwbStr.append("',");
	// }
	// String u = cwbStr.substring(0, cwbStr.length()-1);
	// cwbStr = new StringBuffer(u);
	// }
	// return cwbStr.toString();
	// }

	private void ValidatorBusinessMethod(String cwbs, String companyname, String sign, Explink explink) {
		if (explink == null) {
			throw new RuntimeException("配置信息有误");
		}
		if (companyname == null) {
			throw new RuntimeException("缺少参数缺少参数");
		}
		if (cwbs == null) {
			throw new RuntimeException("缺少参数cwb");
		}
		if (sign == null) {
			throw new RuntimeException("缺少参数sign");
		}
		if (cwbs.split(",").length > explink.getCount()) {
			throw new RuntimeException("订单数不能超过" + explink.getCount() + "个");
		}
		String sigstr = MD5Util.md5(explink.getCompanyname() + explink.getKey());
		if (!sign.equals(sigstr)) {
			throw new RuntimeException("签名验证失败,本地签名=" + sigstr);
		}

	}

	private String getFlowOrderTextById(int flowordertype) {
		for (FlowOrderTypeEnum e : FlowOrderTypeEnum.values()) {
			if (e.getValue() == flowordertype) {
				return e.getText();
			}
		}
		return null;
	}

	public String respExptMsg(String msg) {
		return "<Result><msg>" + msg + "</msg></Result>";
	}

}
