package cn.explink.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolderStrategy;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.OperationTimeDAO;
import cn.explink.dao.OrderBackCheckDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.UserDAO;
import cn.explink.dao.YpdjHandleRecordDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbDetailView;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.OrderBackCheck;
import cn.explink.domain.User;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.CwbFlowOrderTypeEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.CwbStateEnum;
import cn.explink.enumutil.DeliveryStateEnum;

public class BaseOrderService {

	@Autowired
	BranchDAO branchDAO;
	
	@Autowired
	YpdjHandleRecordDAO ypdjHandleRecordDAO;
	
	@Autowired
	SystemInstallDAO systemInstallDAO;
	
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	
	@Autowired
	CwbDAO cwbDAO;
	
	@Autowired
	OrderBackCheckDAO orderBackCheckDao;
	
	@Autowired
	CustomerDAO customerDAO;
	
	@Autowired
	DataStatisticsService dataStatisticsService;
	
	@Autowired
	OperationTimeDAO operationTimeDAO;
	@Autowired
	UserDAO userDAO;
	
	private static Logger logger = LoggerFactory.getLogger(BaseOrderService.class);
	
	/**
	 * 从【区域权限设置】获取中转类型的站点id
	 * @return 0 如果当前登录用户没有相关
	 *
	 * @author jinghui.pan@pjbest.com
	 */
	public long getBranchIdFromUserBranchMapping(BranchEnum branchEnum){
		long branchid = 0;
		Branch zzBranch = null;
		List<Branch> zzBranchlist = this.branchDAO.getQueryBranchByBranchidAndUserid(this.getSessionUser().getUserid(), branchEnum.getValue());
		if(zzBranchlist!= null && !zzBranchlist.isEmpty()){
			zzBranch = zzBranchlist.get(0);
			branchid = zzBranch.getBranchid();
		}
		
		return branchid;
	}
	
	public boolean isShowCustomerSign(){
		
		JSONArray showCustomerjSONArray = getShowCustomerJsonArray();
		boolean showCustomerSign = ((showCustomerjSONArray.size() > 0) && !showCustomerjSONArray.getJSONObject(0).getString("customerid").equals("0")) ? true : false;
		return showCustomerSign;
	}
	
	public JSONArray getShowCustomerJsonArray(){
		
		// 系统设置是否显示订单备注
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");
		
		return showCustomerjSONArray;
	}
	
	public List<CwbDetailView> getcwbDetail(List<CwbOrder> cwbList, List<Customer> customerList, JSONArray showCustomerjSONArray, List<Branch> branchList, long sign) {
		//订单集合 站点集合
		Map<String, Map<String, String>> allTime = this.dataStatisticsService.getOrderFlowByCredateForDetailAndExportAllTime(
				this.getcwbs(cwbList), branchList);
		List<CwbDetailView> cwbViewlist = new ArrayList<CwbDetailView>();
		if (cwbList.size() > 0) {
			for (CwbOrder wco : cwbList) {
				CwbDetailView view = new CwbDetailView();
				Map<String, String> cwbMap = allTime.isEmpty() ? new HashMap<String, String>() : (allTime.get(wco.getCwb()));

				
				view.setOpscwbid(wco.getOpscwbid());
				view.setCwb(wco.getCwb());
				view.setCwbordertypeid(wco.getCwbordertypeid());
				view.setPackagecode(wco.getPackagecode());
				view.setConsigneeaddress(wco.getConsigneeaddress());
				view.setConsigneename(wco.getConsigneename());
				view.setReceivablefee(wco.getReceivablefee());
				view.setEmaildate(wco.getEmaildate());
				view.setTranscwb(wco.getTranscwb());
				view.setCustomerid(wco.getCustomerid());
				view.setNextbranchid(wco.getNextbranchid());
				view.setDeliverid(wco.getDeliverid());
				view.setExceldliverid(wco.getExceldeliverid());
				//***Hps_Concerto create 2016年6月27日 11:17:07
				 for (CwbStateEnum  cwb   : CwbStateEnum.values()){
					 if(cwb.getValue()==wco.getCwbstate()){
						 view.setCwbstatetext(cwb.getText());
					 }
				 } 
				 long flowordertype = wco.getFlowordertype();
				 int deliverystate = wco.getDeliverystate();
				 try{
						if(CwbFlowOrderTypeEnum.getText(flowordertype)!=null){
							if(CwbFlowOrderTypeEnum.getText(flowordertype).getText()=="已审核"){
									view.setFlowordertypetext("审核为："+DeliveryStateEnum.getByValue(deliverystate).getText());
							 }else if(CwbFlowOrderTypeEnum.getText(flowordertype).getText()=="已反馈") {
								 view.setFlowordertypetext(DeliveryStateEnum.getByValue(deliverystate).getText()); 
								/*hps*/ }else{
									view.setFlowordertypetext(CwbFlowOrderTypeEnum.getText(flowordertype).getText());
							 }
						}else{
							view.setFlowordertypetext("");
						}
					}catch(Exception e){
						view.setFlowordertypetext("");
					}
				//*************
				//****Hps_Concerto create 2016年5月26日16:24:30
				view.setFlowordertype(wco.getFlowordertype());
				view.setCwbstate(wco.getCwbstate());
				OrderBackCheck oc = orderBackCheckDao.getOrderBackCheckOnlyCwb(wco.getCwb());
				if(oc==null){ 
					view.setCheckstateresultname("");
				}else{
					view.setCheckstateresultname(oc.getCheckresult()==0?"未审核":(oc.getCheckresult()==1?"确认退货":"站点配送"));
				}
				view.setDeliverystate(wco.getDeliverystate());
				//******************
				view.setRemarkView(this.getShowCustomer(showCustomerjSONArray, wco));
				view.setCustomername(this.dataStatisticsService.getQueryCustomerName(customerList, wco.getCustomerid()));
				view.setInSitetime(cwbMap == null ? "" : (cwbMap.get("InSitetime") == null ? "" : cwbMap.get("InSitetime")));
				view.setPickGoodstime(cwbMap == null ? "" : (cwbMap.get("PickGoodstime") == null ? "" : cwbMap.get("PickGoodstime")));
				view.setOutstoreroomtime(cwbMap == null ? "" : (cwbMap.get("Outstoreroomtime") == null ? "" : cwbMap.get("Outstoreroomtime")));

				
				view.setCwbordertype(CwbOrderTypeIdEnum.getTextByValue(wco.getCwbordertypeid()));
				view.setPickaddress(wco.getRemark4());

				cwbViewlist.add(view);
			}
		}

		List<CwbDetailView> views = new ArrayList<CwbDetailView>();
		Map<String, CwbDetailView> map = new HashMap<String, CwbDetailView>();
		for (CwbDetailView weirukuView : cwbViewlist) {
			if (sign == 1) {// 按出库时间排序
				map.put(weirukuView.getOutstoreroomtime() + weirukuView.getOpscwbid() + "_" + cwbViewlist.indexOf(weirukuView), weirukuView);
			} else if (sign == 2) {// 按到货时间排序
				map.put(weirukuView.getInSitetime() + weirukuView.getOpscwbid() + "_" + cwbViewlist.indexOf(weirukuView), weirukuView);
			} else if (sign == 3) {// 按领货时间排序
				map.put(weirukuView.getPickGoodstime() + weirukuView.getOpscwbid() + "_" + cwbViewlist.indexOf(weirukuView), weirukuView);
			}
		}
		List<String> keys = new ArrayList<String>(map.keySet());
		Collections.sort(keys, Collections.reverseOrder());
		for (int i = 0; i < keys.size(); i++) {
			views.add(map.get(keys.get(i)));
		}
		return sign == 0 ? cwbViewlist : views;
	}
	
	public Object getShowCustomer(JSONArray jSONArray, CwbOrder co) {
		Object remark = "";
		try {
			for (int i = 0; i < jSONArray.size(); i++) {
				String a = jSONArray.getJSONObject(i).getString("customerid");
				String b[] = a.split(",");
				for (String s : b) {
					if (String.valueOf(co.getCustomerid()).equals(s)) {
						remark = co.getClass().getMethod("get" + jSONArray.getJSONObject(i).getString("remark")).invoke(co);
						break;
					}
				}
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		return remark;
	}
	
	public List<String> getcwbs(List<CwbOrder> cwbList) {
		List<String> cwbsList = new ArrayList<String>();
		if (cwbList.size() > 0) {
			for (CwbOrder wco : cwbList) {
				cwbsList.add(wco.getCwb());
			}
		}
		return cwbsList;
	}

	protected User getSessionUser() {
		Authentication authen = this.securityContextHolderStrategy.getContext().getAuthentication();
		if(authen==null){
			User user = userDAO.getUserByUsername("admin");
			return user;
		}else{
			ExplinkUserDetail userDetail = (ExplinkUserDetail) authen.getPrincipal();
			return userDetail.getUser();
		}
	}
}
