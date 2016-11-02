package cn.explink.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.OrderBackCheckDAO;
import cn.explink.dao.TransCwbDetailDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.OrderBackCheck;
import cn.explink.domain.User;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.CwbStateEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.ExceptionCwbErrorTypeEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.TransCwbStateEnum;
import cn.explink.exception.CwbException;

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
	@Autowired
	private TransCwbDetailDAO transCwbDetailDAO;

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

	public List<OrderBackCheck> getOrderBackCheckList2(List<OrderBackCheck> orderbackList, List<Customer> customerList, List<Branch> branchList) throws ParseException {
		if (orderbackList != null && !orderbackList.isEmpty()) {
			for (OrderBackCheck o : orderbackList) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String strcre = o.getCreatetime();
				Date date = sdf.parse(strcre);
				o.setCreatetime(sdf.format(date));
				o.setCustomername(dataStatisticsService.getQueryCustomerName(customerList, o.getCustomerid()));// 供货商的名称
				o.setFlowordertypename(FlowOrderTypeEnum.getText(o.getFlowordertype()).getText());
				o.setCwbordertypename(CwbOrderTypeIdEnum.getByValue(o.getCwbordertypeid()).getText());
				o.setCwbstatename(DeliveryStateEnum.getByValue((int) o.getCwbstate()).getText());
				o.setBranchname(dataStatisticsService.getQueryBranchName(branchList, o.getBranchid()));//配送站点
				o.setCheckstatename(getCheckstatename(o.getCheckstate()));//审核状态
				o.setCheckresultname(getCheckresultname(o.getCheckresult()));//审核结果
			}
		}
		return orderbackList;
	}

	public String getCheckstatename(long checkstate){
		if(checkstate==1){
			return "待审核";
		}else if(checkstate==2){
			return "已审核";
		}
		return "";
	}
	
	public String getCheckresultname(long checkresult){
		if(checkresult==0){
			return "未审核";
		}else if(checkresult==1){
			return "确认退货";
		}else if(checkresult==2){
			return "站点配送";
		}
		return "";
	}
	
	/**
	 * 退货出站审核为
	 * 退货确认
	 * @param ids
	 * @param user
	 */
	@Transactional
	public String save(String ids, User user,String dateStr) {
		StringBuffer wufazaicifankuiBuffer=new StringBuffer();
		wufazaicifankuiBuffer.append("审核完成，（已审核的订单无法再次反馈）其中已审核的订单号为:");
		if (!"".equals(ids)) {
			logger.info("===退货确认审核开始===");
			logger.info("退货确认审核ids{}",ids);
			List<OrderBackCheck> orderList = orderBackCheckDAO.getOrderBackCheckById(ids);
			if (orderList!=null&&!orderList.isEmpty()) {
				//OrderBackCheck order = orderBackCheckDAO.getOrderBackCheckById(Long.parseLong(id));
				for(OrderBackCheck order : orderList){
					if(order.getCheckstate()==2){
						wufazaicifankuiBuffer.append(order.getCwb()+",");
						//return "已审核的订单无法再次反馈！订单号:"+order.getCwb();
					}
					// 获得当前站点的退货站
					List<Branch> bList = new ArrayList<Branch>();
					for (long i : cwbRouteService.getNextPossibleBranch(order.getBranchid())) {
						bList.add(branchDAO.getBranchByBranchid(i));
					}
					Branch tuihuoNextBranch = new Branch();
					for (Branch b : bList) {
						if (b.getSitetype() == BranchEnum.TuiHuo.getValue()) {
							tuihuoNextBranch = b;
						}
					}
					// 更改下一站为退货站
					/*cwbDAO.updateNextBranchid(order.getCwb(), tuihuoNextBranch.getBranchid());
					cwbDAO.updateCwbState(order.getCwb(), CwbStateEnum.TuiHuo);*/
					this.cwbDAO.updateNextBranchidAndCwbstate(order.getCwb(),tuihuoNextBranch.getBranchid(),CwbStateEnum.TuiHuo);
	
					//更新运单状态
					CwbOrder cwborder = this.cwbDAO.getCwbByCwb(order.getCwb());
					for (String transcwb : cwborder.getTranscwb().split(",")) {
						this.transCwbDetailDAO.updateTransCwbDetailBytranscwb(transcwb, TransCwbStateEnum.TUIHUO.getValue());
					}
					
					// 更新checkstate=1 并且更新确认状态为确认退货
					orderBackCheckDAO.updateOrderBackCheck1(1,order.getId(),user.getRealname(), dateStr);
					logger.info("用户:{},对订单:{},退货审核为确认退货状态", new Object[] { user.getRealname(), order.getCwb() });
				}
				logger.info("===退货审核确认结束===");
			}
			if (wufazaicifankuiBuffer.indexOf(",")!=-1) {
				return wufazaicifankuiBuffer.substring(0, wufazaicifankuiBuffer.length()-1).toString();
			}else {
				return "审核为确认退货成功！";
			}
		}
		return "没有需要审核的订单!";
	}

	/**
	 * 退货出站审核为
	 * 站点滞留
	 * @param ids
	 * @param user
	 */
	@Transactional
	public String rsPeiSong(List<OrderBackCheck> orderbackList,User user,String dateStr){
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
				if(order==null){
					return "订单号不存在或者已审核过站点配送";
				}
				if(order!=null&&order.getCheckstate()==2){
					return "已审核的订单无法再次反馈!订单号:"+order.getCwb();
				}
				//更新订单状态
				cwbDAO.updateCwbState(order.getCwb(), CwbStateEnum.PeiShong);
				//更新运单状态
				CwbOrder cwborder = this.cwbDAO.getCwbByCwb(order.getCwb());
				for (String transcwb : cwborder.getTranscwb().split(",")) {
					this.transCwbDetailDAO.updateTransCwbDetailBytranscwb(transcwb, TransCwbStateEnum.PEISONG.getValue());
					this.transCwbDetailDAO.updateNextbranchByTranscwb(transcwb, cwbOrder.getCurrentbranchid());//更新运单下一站
				}
				cwbDAO.updateNextbranch(cwbOrder);//修改下一站为当前站
				orderBackCheckDAO.updateOrderBackCheck2(2,cwbOrder.getCwb(),user.getRealname(),dateStr);//修改为站点滞留状态
			}
			logger.info("===退货站点配送结束===");
			return "审核为站点配送成功!";
		}
		return "";
	}
	
	
	public OrderBackCheck loadFormForOrderBackCheck(CwbOrder co, long branchid, long userid, long checkstate, long cwbstate) {
		OrderBackCheck orderBackCheck = new OrderBackCheck();
		orderBackCheck.setCheckstate(checkstate);
		orderBackCheck.setCwb(co.getCwb());
		orderBackCheck.setCustomerid(co.getCustomerid());
		orderBackCheck.setCwbordertypeid(co.getCwbordertypeid());
		//审核时插入退货出站申请表
		orderBackCheck.setFlowordertype(FlowOrderTypeEnum.YiShenHe.getValue());
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
	
	/**
	 * 校验是否存在退货出站审核中的记录
	 * @param cwb 订单号
	 * @param flowOrderTypeEnum 操作类型
	 * @throws CwbException 如果存在待审核的记录，则会抛出异常
	 */
	public void validateCheckStateAuditing(String cwb, FlowOrderTypeEnum flowOrderTypeEnum) {
		final String logPrefix = "校验是否存在退货审核中的记录->";
		
		cwb = org.apache.commons.lang3.StringUtils.trimToEmpty(cwb);
		if (org.apache.commons.lang3.StringUtils.isEmpty(cwb)) {
			logger.info("{}cwb is empty", logPrefix);
			return;
		}
		
		if (flowOrderTypeEnum == null) {
			logger.info("{}flowOrderTypeEnum is null", logPrefix);
			return;
		}
		
		logger.info("{}cwb:{}, flowOrderTypeEnum:{}", logPrefix, cwb, flowOrderTypeEnum.getValue());
		
		List<OrderBackCheck> orderBackCheckList = orderBackCheckDAO.getOrderBackChecks("'" + cwb + "'", -1, -1, -1, -1, -1, "", "");
		if (CollectionUtils.isEmpty(orderBackCheckList)) {
			logger.info("{}orderBackCheckList is null or empty", logPrefix);
			return;
		}
		
		for (OrderBackCheck orderBackCheck : orderBackCheckList) {
			if (orderBackCheck == null) {
				continue;
			}
			
			if (orderBackCheck.getCheckstate() == 1) { //待审核
				throw new CwbException(cwb, flowOrderTypeEnum.getValue(), 
						ExceptionCwbErrorTypeEnum.TUI_HUO_CHU_ZHAN_SHEN_HE_ZHONG_LING_HUO, flowOrderTypeEnum.getText());
			}
		}
		
		logger.info("{}通过!", logPrefix);
	}
	
}
