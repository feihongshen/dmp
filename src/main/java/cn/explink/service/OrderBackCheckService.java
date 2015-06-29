package cn.explink.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.OrderBackCheckDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.OrderBackCheck;
import cn.explink.domain.User;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.CwbStateEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;

@Service
public class OrderBackCheckService {
	private Logger logger = LoggerFactory.getLogger(OrderBackCheckService.class);
	@Autowired
	OrderBackCheckDAO orderBackCheckDAO;
	@Autowired
	DataStatisticsService dataStatisticsService;
	@Autowired
	CwbRouteService cwbRouteService;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	List<CwbTranslator> cwbTranslators;

	public List<OrderBackCheck> getOrderBackCheckList(List<OrderBackCheck> orderbackList, List<Customer> customerList, List<User> userList) {
		List<OrderBackCheck> list = new ArrayList<OrderBackCheck>();

		if (orderbackList != null && !orderbackList.isEmpty()) {
			for (OrderBackCheck o : orderbackList) {
				o.setCustomername(dataStatisticsService.getQueryCustomerName(customerList, o.getCustomerid()));// 供货商的名称
				// o.setUsername(dataStatisticsService.getQueryUserName(userList,o.getUserid()));
				o.setFlowordertypename(FlowOrderTypeEnum.getText(o.getFlowordertype()).getText());
				o.setCwbordertypename(CwbOrderTypeIdEnum.getByValue(o.getCwbordertypeid()).getText());
				o.setCwbstatename(DeliveryStateEnum.getByValue((int) o.getCwbstate()).getText());
				list.add(o);
			}
		}
		return list;
	}

	public List<OrderBackCheck> getOrderBackCheckList2(List<OrderBackCheck> orderbackList, List<Customer> customerList, List<Branch> branchList) {
		List<OrderBackCheck> list = new ArrayList<OrderBackCheck>();

		if (orderbackList != null && !orderbackList.isEmpty()) {
			for (OrderBackCheck o : orderbackList) {
				o.setCustomername(dataStatisticsService.getQueryCustomerName(customerList, o.getCustomerid()));// 供货商的名称
				o.setFlowordertypename(FlowOrderTypeEnum.getText(o.getFlowordertype()).getText());
				o.setCwbordertypename(CwbOrderTypeIdEnum.getByValue(o.getCwbordertypeid()).getText());
				o.setCwbstatename(DeliveryStateEnum.getByValue((int) o.getCwbstate()).getText());
				o.setBranchname(dataStatisticsService.getQueryBranchName(branchList, o.getBranchid()));
				list.add(o);
			}
		}
		return list;
	}

	
	/**
	 * 退货出站审核为
	 * 退货确认
	 * @param ids
	 * @param user
	 */
	@Transactional
	public void save(String ids, User user,String dateStr) {
		if (!"".equals(ids)) {
			logger.info("===退货确认审核开始===");
			for (String id : ids.split(",")) {
				OrderBackCheck order = orderBackCheckDAO.getOrderBackCheckById(Long.parseLong(id));
				
				// 获得当前站点的退货站
				List<Branch> bList = new ArrayList<Branch>();
				for (long i : cwbRouteService.getNextPossibleBranch(order.getBranchid())) {
					bList.add(branchDAO.getBranchByBranchid(i));
				}
				Branch tuihuoNextBranch = null;
				for (Branch b : bList) {
					if (b.getSitetype() == BranchEnum.TuiHuo.getValue()) {
						tuihuoNextBranch = b;
					}
				}
				// 更改下一站为退货站
				cwbDAO.updateNextBranchid(order.getCwb(), tuihuoNextBranch.getBranchid());
				cwbDAO.updateCwbState(order.getCwb(), CwbStateEnum.TuiHuo);

				// 更新checkstate=1 并且更新确认状态为确认退货
				orderBackCheckDAO.updateOrderBackCheck1(1,Long.parseLong(id),user.getRealname(), dateStr);
				logger.info("用户:{},对订单:{},退货审核为确认退货状态", new Object[] { user.getRealname(), order.getCwb() });
			}
			logger.info("===退货审核确认结束===");
		}
	}

	/**
	 * 退货出站审核为
	 * 站点滞留
	 * @param ids
	 * @param user
	 */
	@Transactional
	public void rsPeiSong(List<OrderBackCheck> orderbackList,User user,String dateStr){
		if (orderbackList!=null) {
			logger.info("===退货站点配送开始===");
			
			StringBuffer sb = new StringBuffer("");
			for(OrderBackCheck obc:orderbackList){
				sb.append("'").append(obc.getCwb()).append("',");
			}
			String cwbs = "";
			if(sb.length()>0){
				cwbs = sb.substring(0, sb.toString().length()-1);
			}
			List<CwbOrder> coList = cwbDAO.getcwborderList(cwbs);
			
			for (CwbOrder cwbOrder : coList) {
				OrderBackCheck order = orderBackCheckDAO.getOrderBackCheckByCwb(cwbOrder.getCwb());
				cwbDAO.updateCwbState(order.getCwb(), CwbStateEnum.PeiShong);
				cwbDAO.updateNextbranch(cwbOrder);//修改下一站为当前站
				orderBackCheckDAO.updateOrderBackCheck2(2,cwbOrder.getCwb(),user.getRealname(),dateStr);//修改为站点滞留状态
			}
			logger.info("===退货站点配送结束===");
		}
	}
	
	
	public OrderBackCheck loadFormForOrderBackCheck(CwbOrder co, long branchid, long userid, long checkstate, long cwbstate) {
		OrderBackCheck orderBackCheck = new OrderBackCheck();
		orderBackCheck.setCheckstate(checkstate);
		orderBackCheck.setCwb(co.getCwb());
		orderBackCheck.setCustomerid(co.getCustomerid());
		orderBackCheck.setCwbordertypeid(co.getCwbordertypeid());
		orderBackCheck.setFlowordertype(co.getFlowordertype());
		orderBackCheck.setCwbstate(cwbstate);// 拒收 配送结果
		orderBackCheck.setConsigneename(co.getConsigneename());
		orderBackCheck.setConsigneephone(co.getConsigneephone());
		orderBackCheck.setConsigneeaddress(co.getConsigneeaddress());
		orderBackCheck.setBackreason(co.getBackreason());
		orderBackCheck.setBranchid(branchid);
		if (checkstate == 1) {
			orderBackCheck.setUserid(userid);
			orderBackCheck.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		} else {
			orderBackCheck.setCheckcreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		}
		return orderBackCheck;
	}

	public String translateCwb(String cwb) {
		for (CwbTranslator cwbTranslator : this.cwbTranslators) {
			String translateCwb = cwbTranslator.translate(cwb);
			if (StringUtils.hasLength(translateCwb)) {
				cwb = translateCwb;
			}
		}
		return cwb;
	}
	
}
