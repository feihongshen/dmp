package cn.explink.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.CwbOrderTailDao;
import cn.explink.domain.Branch;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.FlowOrderTypeZongheEnum;
import cn.explink.jms.dto.DmpCwbOrder;
import cn.explink.jms.dto.DmpDeliveryState;
import cn.explink.jms.dto.DmpOrderFlow;

@Service
public class CwbOrderTailService {

	@Autowired
	private CwbOrderTailDao cwbOrderTaildao;

	private Logger logger = LoggerFactory.getLogger(CwbOrderTailService.class);

	/**
	 * 保存当前订单操作与时间
	 * 
	 * @param orderFlow
	 * @param dmpCwbOrder
	 * @param deliverystate
	 */
	public void saveCwbOrderCurHandle(DmpOrderFlow orderFlow, DmpCwbOrder dmpCwbOrder, DmpDeliveryState deliverystate, Branch branch, Branch nextbranch) {
		try {
			long flowordertype = getFloworderType(branch, nextbranch, dmpCwbOrder.getFlowordertype());
			dmpCwbOrder.setFlowordertype(flowordertype);
			orderFlow.setFlowordertype((int) flowordertype);
			String timeName = getFlowTimeName((int) flowordertype);
			logger.info("综合查询：cwb:" + orderFlow.getCwb() + "，flowordertype:" + flowordertype + "，timeName：" + timeName);
			cwbOrderTaildao.saveOrUpdateOrderTail(orderFlow, dmpCwbOrder, deliverystate, timeName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private long getFloworderType(Branch branch, Branch nextbranch, long flowordertype) {
		if (branch.getSitetype() == BranchEnum.ZhanDian.getValue() && nextbranch.getSitetype() == BranchEnum.ZhongZhuan.getValue() && flowordertype == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) {
			return FlowOrderTypeZongheEnum.ZhongZhuanChuKuSaoMiao.getValue();
		}
		if (branch.getSitetype() == BranchEnum.ZhongZhuan.getValue() && flowordertype == FlowOrderTypeEnum.RuKu.getValue()) {
			return FlowOrderTypeZongheEnum.ZhongZhuanZhanRuKu.getValue();
		}
		if (branch.getSitetype() == BranchEnum.ZhongZhuan.getValue() && flowordertype == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) {
			return FlowOrderTypeZongheEnum.ZhongZhuanZhanChuKuSaoMiao.getValue();
		}
		if (branch.getSitetype() == BranchEnum.TuiHuo.getValue() && flowordertype == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) {
			return FlowOrderTypeZongheEnum.TuiHuoZhanZaiTouSaoMiao.getValue();
		}
		return flowordertype;
	}

	private String getFlowTimeName(int flowordertype) {
		for (FlowOrderTypeZongheEnum f : FlowOrderTypeZongheEnum.values()) {
			if (f.getValue() == flowordertype) {
				if (flowordertype == 1) {
					return "";
				}
				return f.getMethod().indexOf("time") > -1 ? f.getMethod() : "";
			}
		}
		return "";
	}
}
