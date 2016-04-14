package cn.explink.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.AccountDeducDetailDAO;
import cn.explink.domain.AccountDeducDetail;
import cn.explink.domain.AccountDeducDetailView;
import cn.explink.domain.Branch;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.User;
import cn.explink.enumutil.AccountFlowOrderTypeEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.util.StringUtil;

@Service
public class AccountDeducDetailService {
	private Logger logger = LoggerFactory.getLogger(AccountDeducDetailService.class);

	@Autowired
	AccountDeducDetailDAO accountDeducDetailDAO;
	@Autowired
	DataStatisticsService dataStatisticsService;

	/**
	 * 导出 excel字段匹配
	 * 
	 * @param clist
	 * @return List<AccountCwbDetailView>
	 */
	public List<AccountDeducDetailView> getViewByAccountDeducDetail(List<AccountDeducDetail> clist) {
		List<AccountDeducDetailView> viewList = new ArrayList<AccountDeducDetailView>();
		if (clist != null && !clist.isEmpty()) {
			for (AccountDeducDetail acd : clist) {
				AccountDeducDetailView o = new AccountDeducDetailView();
				o.setBranchname(acd.getBranchname());
				o.setCwb(acd.getCwb());
				o.setFee(acd.getFee());
				o.setMemo(acd.getMemo());
				o.setCreatetime(acd.getCreatetime());
				o.setUsername(acd.getUsername());
				// 类型
				for (AccountFlowOrderTypeEnum ft : AccountFlowOrderTypeEnum.values()) {
					if (acd.getFlowordertype() == ft.getValue()) {
						if (acd.getFlowordertype() == AccountFlowOrderTypeEnum.GaiZhanChongKuan.getValue()) {
							o.setRecordtype("中转");
						} else {
							o.setRecordtype(ft.getText());
						}
					}
				}
				viewList.add(o);
			}
		}
		return viewList;
	}

	/**
	 * 此方法慎用，adList最好不要超过1000条
	 * 
	 * @param adList
	 * @param branchList
	 * @param userList
	 * @return
	 */
	public List<AccountDeducDetail> getAccountDeducDetailListByBranchAndUser(List<AccountDeducDetail> adList, List<Branch> branchList, List<User> userList) {
		try {
			// 处理列表页关联的站点名称
			List<AccountDeducDetail> list = new ArrayList<AccountDeducDetail>();
			if (adList != null && !adList.isEmpty()) {
				for (AccountDeducDetail o : adList) {
					o.setBranchname(dataStatisticsService.getQueryBranchName(branchList, o.getBranchid()));
					o.setUsername(dataStatisticsService.getQueryUserName(userList, o.getUserid()));
					list.add(o);
				}
			}
			return list;
		} catch (Exception e) {
			logger.info("分页查找扣款结算列表异常：", e);
			return null;
		}
	}

	public AccountDeducDetail loadFormForAccountDeducDetail(CwbOrder co, long branchid, long flowordertype, BigDecimal fee, long userid, String memo, long recordid, long recordidvirt) {
		String nowtimesecond = String.valueOf(System.currentTimeMillis());
		AccountDeducDetail accountDeducDetail = new AccountDeducDetail();
		accountDeducDetail.setBranchid(branchid);
		accountDeducDetail.setCwb(StringUtil.nullConvertToEmptyString(co.getCwb()));
		accountDeducDetail.setFee(fee);// 代收货款
		accountDeducDetail.setFlowordertype(flowordertype);
		accountDeducDetail.setMemo(memo);
		accountDeducDetail.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		accountDeducDetail.setCreatetimesecond(nowtimesecond);
		accountDeducDetail.setUserid(userid);
		accountDeducDetail.setRecordid(recordid);
		accountDeducDetail.setRecordidvirt(recordidvirt);
		return accountDeducDetail;
	}

	public BigDecimal getTHPaybackfee(long cwbordertypeid, long deliverystate, BigDecimal receivablefee, BigDecimal paybackfee) {
		BigDecimal fee = BigDecimal.ZERO;// 应退金额
		if (cwbordertypeid == CwbOrderTypeIdEnum.Shangmentui.getValue() && deliverystate == DeliveryStateEnum.ShangMenTuiChengGong.getValue()) {
			fee = paybackfee;
		} else if (cwbordertypeid == CwbOrderTypeIdEnum.Shangmenhuan.getValue() && deliverystate == DeliveryStateEnum.ShangMenHuanChengGong.getValue()) {
			fee = paybackfee;
		} else {
			fee = receivablefee;
		}
		return fee;
	}
}
