package cn.explink.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.controller.ComplaintView;
import cn.explink.dao.ComplaintDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.MqExceptionDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Complaint;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.MqExceptionBuilder;
import cn.explink.domain.User;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.Page;

@Service
public class ComplaintService {

	@Autowired
	ComplaintDAO complaintDAO;
	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	CwbOrderService cwborderService;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;

	@Produce(uri = "jms:topic:complaint")
	ProducerTemplate complaintTemplate;
	private ObjectMapper om = new ObjectMapper();
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	private MqExceptionDAO mqExceptionDAO;

	public List<Complaint> getListByCwbsAndWhere(String cwbs, long type, long auditType, String starteTime, String endTime, long page) {
		List<Complaint> list = new ArrayList<Complaint>();
		if (cwbs.length() > 0) {
			String[] cwbstr = cwbs.split("\r\n");
			for (int i = ((int) page - 1) * Page.ONE_PAGE_NUMBER, j = ((int) page - 1) * Page.ONE_PAGE_NUMBER; j < ((int) page) * Page.ONE_PAGE_NUMBER; i++, j++) {
				if (i == cwbstr.length) {
					break;
				}
				if (cwbstr[i].trim().length() == 0) {
					continue;
				}
				String cwb = cwborderService.translateCwb(cwbstr[i].trim());
				List<Complaint> oList = complaintDAO.getComplaintByCwb(cwb);
				if (oList != null && oList.size() > 0) {
					list.addAll(oList);
				} else {
					j--;
				}
			}
		} else {
			starteTime = "".equals(starteTime) ? "2012-01-01 00:00:00" : starteTime;
			endTime = "".equals(endTime) ? sdf.format(new Date()) : endTime;
			List<Complaint> oList = complaintDAO.getComplaintByWhere(type, auditType, starteTime, endTime, page);
			list = oList;
		}
		return list;
	}

	public List<Complaint> getListByCwbsAndWhereNoPage(String cwbs, long type, long auditType, String starteTime, String endTime) {
		List<Complaint> list = new ArrayList<Complaint>();
		if (cwbs.length() > 0 && !cwbs.contains(",")) {
			String[] cwbstr = cwbs.split("\r\n");
			for (int i = 0; i < cwbstr.length; i++) {
				if (cwbstr[i].trim().length() == 0) {
					continue;
				}
				String cwb = cwborderService.translateCwb(cwbstr[i].trim());
				List<Complaint> oList = complaintDAO.getComplaintByCwb(cwb);
				if (oList != null && oList.size() > 0) {
					list.addAll(oList);
				}
			}
		} else {
			starteTime = "".equals(starteTime) ? "2012-01-01 00:00:00" : starteTime;
			endTime = "".equals(endTime) ? sdf.format(new Date()) : endTime;
			List<Complaint> oList = complaintDAO.getComplaintByWhereNoPage(type, auditType, starteTime, endTime);
			list = oList;
		}
		return list;
	}

	public long getCountByCwbsAndWhere(String cwbs, long type, long auditType, String starteTime, String endTime) {
		long count = 0;
		if (cwbs.length() > 0) {
			List<Complaint> list = new ArrayList<Complaint>();
			for (String cwb : cwbs.split("\r\n")) {
				if (cwb.trim().length() == 0) {
					continue;
				}
				String cwb1 = cwborderService.translateCwb(cwb.trim());
				List<Complaint> oList = complaintDAO.getComplaintByCwb(cwb1);
				if (oList != null && oList.size() > 0) {
					list.addAll(oList);
				}
			}
			count = list.size();
		} else {
			starteTime = "".equals(starteTime) ? "2012-01-01 00:00:00" : starteTime;
			endTime = "".equals(endTime) ? sdf.format(new Date()) : endTime;
			count = complaintDAO.getComplaintCount(type, auditType, starteTime, endTime);
		}
		return count;
	}

	public List<ComplaintView> getComplaintView(List<Complaint> clist, List<Customer> customerList, List<User> userList, List<Branch> branchList) {
		List<ComplaintView> complaintViewList = new ArrayList<ComplaintView>();
		if (clist.size() > 0) {
			for (Complaint comp : clist) {
				ComplaintView complaintView = new ComplaintView();
				complaintView.setCwb(comp.getCwb());
				CwbOrder order = cwbDAO.getCwbByCwb(comp.getCwb());
				if (order != null) {
					complaintView.setCwbdelivername(this.getQueryUserName(userList, order.getDeliverid()));// 订单本身的小件员
					complaintView.setCustomername(this.getQueryCustomerName(customerList, order.getCustomerid()));
					Date ruku = this.getOrderFlowByCwbAndType(order.getCwb(), FlowOrderTypeEnum.RuKu.getValue()).getCredate();
					Date chukusaomiao = this.getOrderFlowByCwbAndType(order.getCwb(), FlowOrderTypeEnum.ChuKuSaoMiao.getValue()).getCredate();
					Date daohuosaomiao = this.getOrderFlowByCwbAndType(order.getCwb(), FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()).getCredate();
					Date yishenhe = this.getOrderFlowByCwbAndType(order.getCwb(), FlowOrderTypeEnum.YiShenHe.getValue()).getCredate();
					complaintView.setInstoreroomtime(ruku != null ? sdf.format(ruku) : "");// 入库时间
					complaintView.setOutstoreroomtime(chukusaomiao != null ? sdf.format(chukusaomiao) : "");// 出库时间
					complaintView.setInSitetime(daohuosaomiao != null ? sdf.format(daohuosaomiao) : "");// 到站时间
					complaintView.setGoclasstime(yishenhe == null ? "" : sdf.format(yishenhe));// 归班时间
					DeliveryState deliverystate = this.getDeliveryByCwb(order.getCwb());
					if (deliverystate != null) {
						complaintView.setDeliverystate(deliverystate.getDeliverystate());
					}
					complaintView.setEmaildate(order.getEmaildate());
					complaintView.setConsigneename(order.getConsigneename());
					complaintView.setOrderflowtype(order.getFlowordertype());
				} else {
					continue;
				}
				complaintView.setDelivername(this.getQueryUserName(userList, comp.getDeliveryid()));// 投诉的小件员
				complaintView.setBranchname(this.getQueryBranchName(branchList, comp.getBranchid()));// 投诉站点

				complaintView.setId(comp.getId());
				complaintView.setType(comp.getType());
				complaintView.setAuditType(comp.getAuditType());
				complaintView.setContent(comp.getContent());
				complaintView.setAuditRemark(comp.getAuditRemark());
				complaintView.setCreateTime(comp.getCreateTime());
				complaintView.setBranchid(comp.getBranchid());
				complaintView.setDeliveryid(comp.getDeliveryid());
				complaintView.setCreateUser(getQueryUserName(userList, comp.getCreateUser()));
				complaintView.setConsigneeaddress(order.getConsigneeaddress());
				String phone = order.getConsigneephone();
				if (order.getConsigneemobile() != null && order.getConsigneemobile().length() > 0) {
					phone = phone + "/" + order.getConsigneemobile();
				} else {
					phone = order.getConsigneemobile();
				}
				complaintView.setConsigneephone(phone);
				complaintView.setAuditUser(comp.getAuditUser());
				complaintView.setAuditTime(comp.getAuditTime());
				complaintViewList.add(complaintView);

			}
		}
		return complaintViewList;
	}

	public ComplaintView getComplaintViewByid(long id, List<Customer> customerList, List<User> userList, List<Branch> branchList) {
		ComplaintView complaintView = new ComplaintView();
		Complaint complaint = complaintDAO.getComplaintById(id);
		if (complaint != null) {
			complaintView.setCwb(complaint.getCwb());
			CwbOrder order = cwbDAO.getCwbByCwb(complaint.getCwb());
			if (order != null) {
				complaintView.setCwbdelivername(this.getQueryUserName(userList, order.getDeliverid()));// 订单本身的小件员
				complaintView.setCustomername(this.getQueryCustomerName(customerList, order.getCustomerid()));// 供货商
				Date ruku = this.getOrderFlowByCwbAndType(order.getCwb(), FlowOrderTypeEnum.RuKu.getValue()).getCredate();
				Date chukusaomiao = this.getOrderFlowByCwbAndType(order.getCwb(), FlowOrderTypeEnum.ChuKuSaoMiao.getValue()).getCredate();
				Date daohuosaomiao = this.getOrderFlowByCwbAndType(order.getCwb(), FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()).getCredate();
				Date yishenhe = this.getOrderFlowByCwbAndType(order.getCwb(), FlowOrderTypeEnum.YiShenHe.getValue()).getCredate();
				complaintView.setInstoreroomtime(ruku != null ? sdf.format(ruku) : "");// 入库时间
				complaintView.setOutstoreroomtime(chukusaomiao != null ? sdf.format(chukusaomiao) : "");// 出库时间
				complaintView.setInSitetime(daohuosaomiao != null ? sdf.format(daohuosaomiao) : "");// 到站时间
				complaintView.setGoclasstime(yishenhe == null ? "" : sdf.format(yishenhe));// 归班时间
				DeliveryState deliverystate = this.getDeliveryByCwb(order.getCwb());
				if (deliverystate != null) {
					complaintView.setDeliverystate(deliverystate.getDeliverystate());
				}
				complaintView.setEmaildate(order.getEmaildate());
				complaintView.setConsigneename(order.getConsigneename());
				complaintView.setOrderflowtype(order.getFlowordertype());
			}
			complaintView.setDelivername(this.getQueryUserName(userList, complaint.getDeliveryid()));// 投诉小件员
			complaintView.setBranchname(this.getQueryBranchName(branchList, complaint.getBranchid()));// 投诉站点
			complaintView.setId(complaint.getId());
			complaintView.setType(complaint.getType());
			complaintView.setAuditType(complaint.getAuditType());
			complaintView.setContent(complaint.getContent());
			complaintView.setAuditRemark(complaint.getAuditRemark());
			complaintView.setCreateTime(complaint.getCreateTime());
			complaintView.setBranchid(complaint.getBranchid());
			complaintView.setDeliveryid(complaint.getDeliveryid());
		}
		return complaintView;
	}

	public long updateComplaint(Complaint complaint) {
		try {
			complaintDAO.updateComplaintById(complaint);
			send(complaint);
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public String getQueryBranchName(List<Branch> branchList, long branchid) {
		String branchname = "";
		for (Branch b : branchList) {
			if (b.getBranchid() == branchid) {
				branchname = b.getBranchname();
				break;
			}
		}
		return branchname;
	}

	public String getQueryUserName(List<User> userList, long userid) {
		String username = "";
		for (User u : userList) {
			if (u.getUserid() == userid) {
				username = u.getRealname();
				break;
			}
		}
		return username;
	}

	public String getQueryCustomerName(List<Customer> customerList, long customerid) {
		String customername = "";
		for (Customer c : customerList) {
			if (c.getCustomerid() == customerid) {
				customername = c.getCustomername();
				break;
			}
		}
		return customername;
	}

	public OrderFlow getOrderFlowByCwbAndType(String cwb, long flowordertype) {
		List<OrderFlow> orderflowList = new ArrayList<OrderFlow>();
		orderflowList = orderFlowDAO.getOrderFlowByCwbAndFlowordertype(cwb, flowordertype, "", "");
		OrderFlow orderflow = orderflowList.size() > 0 ? orderflowList.get(orderflowList.size() - 1) : new OrderFlow();
		return orderflow;
	}

	public DeliveryState getDeliveryByCwb(String cwb) {
		List<DeliveryState> delvieryList = deliveryStateDAO.getDeliveryStateByCwb(cwb);
		return delvieryList.size() > 0 ? delvieryList.get(delvieryList.size() - 1) : new DeliveryState();
	}

	public List<CwbOrder> getListByCwbs(String cwbs, String begindate, String enddate, String consigneename, String consigneemobile, String consigneeaddress, long page) {
		List<CwbOrder> list = new ArrayList<CwbOrder>();
		if (cwbs.length() > 0) {
			String[] cwbstr = cwbs.split("\r\n");
			for (int i = ((int) page - 1) * Page.ONE_PAGE_NUMBER; i < ((int) page) * Page.ONE_PAGE_NUMBER; i++) {
				if (i == cwbstr.length) {
					break;
				}
				if (cwbstr[i].trim().length() == 0) {
					continue;
				}
				String cwb = cwborderService.translateCwb(cwbstr[i].trim());
				List<CwbOrder> oList = cwbDAO.getListByCwb(cwb);
				list.addAll(oList);
			}
		} else {
			list = cwbDAO.getListByCwb(begindate, enddate, 0, consigneename, consigneemobile, consigneeaddress, page);
		}
		return list;
	}

	public long getCountByCwbs(String cwbs, String begindate, String enddate, String consigneename, String consigneemobile, String consigneeaddress) {
		long count = 0;
		if (cwbs.length() > 0) {
			List<CwbOrder> list = new ArrayList<CwbOrder>();
			for (String cwb : cwbs.split("\r\n")) {
				if (cwb.trim().length() == 0) {
					continue;
				}
				String cwb1 = cwborderService.translateCwb(cwb.trim());
				List<CwbOrder> oList = cwbDAO.getListByCwb(cwb1);
				list.addAll(oList);
			}
			count = list.size();
		} else {
			count = cwbDAO.getCountByCwb(begindate, enddate, 0, consigneename, consigneemobile, consigneeaddress);
		}
		return count;
	}

	public void send(Complaint complaint) {
		try {
			complaintTemplate.sendBodyAndHeader(null, "complaint", om.writeValueAsString(complaint));
		} catch (Exception ee) {
			logger.error("send flow complaint error", ee);
			//写MQ异常表
			try {
				this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode("send")
						.buildExceptionInfo(ee.getMessage()).buildTopic(this.complaintTemplate.getDefaultEndpoint().getEndpointUri())
						.buildMessageHeader("complaint", om.writeValueAsString(complaint)).getMqException());
			} catch (IOException e) {
				logger.error("转换错误", e);
			}
		}

	}

}
