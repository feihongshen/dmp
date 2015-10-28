package cn.explink.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Service;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.OperationTimeDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.YpdjHandleRecordDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbDetailView;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.User;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.CwbStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.Page;

@Service
public class KfzdOrderService extends BaseOrderService{

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
	CustomerDAO customerDAO;
	
	@Autowired
	DataStatisticsService dataStatisticsService;
	
	@Autowired
	OperationTimeDAO operationTimeDAO;
	/**
	 * 获取<b>分拣中转</b>出库，<b>已出库</b>的订单分页列表
	 * @return
	 */
	public List<CwbDetailView> getSortAndChangeExportYiChuKuCwbViewList(long page, long nextbranchid){
		
		long kfBranchid = this.getSessionUser().getBranchid();
		long zzBranchid = this.getBranchIdFromUserBranchMapping(BranchEnum.ZhongZhuan);
		
		List<String> cwbyichukuList = new ArrayList<String>();
		
		
		//【中转已出库】的订单号列表
		List<String> cwbyichukuList_zz = this.operationTimeDAO.getOperationTimeByFlowordertypeAndBranchidAndNext(
				zzBranchid, nextbranchid, FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue());
		if(CollectionUtils.isNotEmpty(cwbyichukuList_zz)){
			cwbyichukuList.addAll(cwbyichukuList_zz);
		}
		
		//【分拣已出库】的订单号列表
		List<String> cwbyichukuList_fj = this.operationTimeDAO.getOperationTimeByFlowordertypeAndBranchidAndNext(
				kfBranchid, nextbranchid, FlowOrderTypeEnum.ChuKuSaoMiao.getValue());
		if(CollectionUtils.isNotEmpty(cwbyichukuList_fj)){
			cwbyichukuList.addAll(cwbyichukuList_fj);
		}
		
		
		String yicwbs = "";
		if (cwbyichukuList.size() > 0) {
			yicwbs = this.dataStatisticsService.getOrderFlowCwbs(cwbyichukuList);
		} else {
			yicwbs = "'--'";
		}
		
		// 已出库明细
		List<CwbOrder> cwbOrderList = this.cwbDAO.getCwbByCwbsPage(page, yicwbs, Page.DETAIL_PAGE_NUMBER);
		List<CwbDetailView> cwbOrderViewList = this.getCwbViewList(cwbOrderList);
		
		return cwbOrderViewList;
	}
	
	/**
	 * 获取分拣和中转出库扫描，已出库的数量数量
	 * @return
	 */
	public long getSortAndChangeYiChuKuCount(long nextbranchid){
		long kfBranchid = this.getSessionUser().getBranchid();
		long zzBranchid = this.getBranchIdFromUserBranchMapping(BranchEnum.ZhongZhuan);
		
		long yichukucount_fj = this.operationTimeDAO.getOperationTimeByFlowordertypeAndBranchidAndNextCount
				(kfBranchid, nextbranchid, FlowOrderTypeEnum.ChuKuSaoMiao.getValue());
	
		long yichukucount_zz = this.operationTimeDAO.getOperationTimeByFlowordertypeAndBranchidAndNextCount
				(zzBranchid, nextbranchid, FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue());
		
		return (yichukucount_fj + yichukucount_zz);
	}
	
	/**
	 * 获取<b>分拣中转</b>出库，<b>未出库</b>的订单分页View列表
	 * @return
	 */
	public List<CwbDetailView> getSortAndChangeExportWeiChuKuCwbViewList(long page, long nextbranchid){
		
		long kfBranchid = this.getSessionUser().getBranchid();
		long zzBranchid = this.getBranchIdFromUserBranchMapping(BranchEnum.ZhongZhuan);
		
		int cwbstate = CwbStateEnum.PeiShong.getValue();
		
		// 中转出库未出库明细
		List<CwbOrder> zzOrderList = this.cwbDAO.getZhongZhuanZhanChukuForCwbOrderByBranchid(zzBranchid, page, nextbranchid);
		
		// 分拣出库未出库明细
		List<CwbOrder> fjOrderList = this.cwbDAO.getChukuForCwbOrderByBranchid(kfBranchid, cwbstate, page, nextbranchid);
		
		zzOrderList.addAll(fjOrderList);
		
		List<CwbDetailView> weichukuViewList = this.getCwbViewList(zzOrderList);
		
		return weichukuViewList;
	}
	
	/**
	 * 获取<b>分拣中转</b>出库，<b>未出库</b>的订单分页View列表
	 * @return
	 */
	public List<CwbDetailView> getOrderedSortAndChangeExportWeiChuKuCwbViewList(long nextbranchid,String orderby, long asc){
		
		long kfBranchid = this.getSessionUser().getBranchid();
		long zzBranchid = this.getBranchIdFromUserBranchMapping(BranchEnum.ZhongZhuan);
		
		int cwbstate = CwbStateEnum.PeiShong.getValue();
		
		// 中转出库未出库明细
		List<CwbOrder> zzOrderList = this.cwbDAO.getZhongZhuanZhanChukuForCwbOrderByBranchid(zzBranchid, 1, nextbranchid);
		
		// 分拣出库未出库明细
		List<CwbOrder> fjOrderList = this.cwbDAO.getChukuForCwbOrderByBranchid(kfBranchid, cwbstate, orderby, nextbranchid,asc);
		
		zzOrderList.addAll(fjOrderList);
		
		List<CwbDetailView> weichukuViewList = this.getCwbViewList(zzOrderList);
		
		return weichukuViewList;
	}
	
	/**
	 * 获取订单分页转换成视图列表
	 * @return
	 */
	public List<CwbDetailView> getCwbViewList(List<CwbOrder> cwbOrders){
		List<Customer> customerList = this.customerDAO.getAllCustomers();
		// 系统设置是否显示订单备注
		JSONArray showCustomerjSONArray = getShowCustomerJsonArray();
		List<CwbDetailView> viewList = this.getcwbDetail(cwbOrders, customerList, showCustomerjSONArray, null, 0);
		return viewList;
	}
	
	
	/**
	 * 获取分拣和中转出库扫描，一票多件缺件的数量
	 * @return
	 */
	public List<JSONObject> getSortAndChangeYpdjLessCwbList(long nextbranchid,long page){
		long kfBranchid = this.getSessionUser().getBranchid();
		long zzBranchid = this.getBranchIdFromUserBranchMapping(BranchEnum.ZhongZhuan);
		
		List<JSONObject> quejianList_fj = this.ypdjHandleRecordDAO.
				getChukuQuejianbyBranchidList(kfBranchid, nextbranchid, page, FlowOrderTypeEnum.ChuKuSaoMiao.getValue());

		List<JSONObject> quejianList_zz = this.ypdjHandleRecordDAO.
				getChukuQuejianbyBranchidList(zzBranchid, nextbranchid, page, FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue());

		List<JSONObject> resultList = new ArrayList<JSONObject>();
		if(CollectionUtils.isNotEmpty(quejianList_fj)){
			resultList.addAll(quejianList_fj);
		}
		if(CollectionUtils.isNotEmpty(quejianList_zz)){
			resultList.addAll(quejianList_zz);
		}
		
		translateYpdjLessTranscwb(resultList);
		
		return resultList;
	}
	
	private void translateYpdjLessTranscwb(List<JSONObject> lessList){
		List<Customer> customerList = this.customerDAO.getAllCustomers();
		
		for (JSONObject obj : lessList) {
			String transcwb = "";
			if (obj.getString("transcwb").indexOf("explink") > -1) {

			} else if (obj.getString("transcwb").indexOf("havetranscwb") > -1) {
				transcwb = obj.getString("transcwb").split("_")[1];
			}
			obj.put("transcwb", transcwb);
			obj.put("customername", this.dataStatisticsService.getQueryCustomerName(customerList, obj.getLong("customerid")));
		}
	}
	
	
	/**
	 * 获取分拣和中转出库扫描，一票多件缺件的数量
	 * @return
	 */
	public long getSortAndChangeYpdjLessCwbNum(long nextbranchid){
		long kfBranchid = this.getSessionUser().getBranchid();
		long zzBranchid = this.getBranchIdFromUserBranchMapping(BranchEnum.ZhongZhuan);
		
		long lesscwbnum_fj = this.ypdjHandleRecordDAO.getChukuQuejianbyBranchid(kfBranchid, nextbranchid,FlowOrderTypeEnum.ChuKuSaoMiao.getValue());
		long lesscwbnum_zz = this.ypdjHandleRecordDAO.getChukuQuejianbyBranchid(zzBranchid, nextbranchid,FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue());
		long lesscwbnum = lesscwbnum_zz + lesscwbnum_fj;
		return lesscwbnum;
	}
}
