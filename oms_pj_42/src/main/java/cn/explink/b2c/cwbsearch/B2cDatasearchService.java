package cn.explink.b2c.cwbsearch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.GetDmpDAO;
import cn.explink.domain.Customer;
import cn.explink.domain.User;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.jms.dto.CwbOrderWithDeliveryState;
import cn.explink.jms.dto.DmpCwbOrder;
import cn.explink.jms.dto.DmpDeliveryState;
import cn.explink.jms.dto.DmpOrderFlow;
import cn.explink.util.DateTimeUtil;

@Service
public class B2cDatasearchService {

	@Autowired
	B2cDatasearchDAO b2cDatasearchDAO;
	@Autowired
	GetDmpDAO getDmpdao;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public void buildB2cData(CwbOrderWithDeliveryState cwbOrderWothDeliverystate, DmpOrderFlow orderFlow, long flowOrdertype, long delivery_state, Customer customer) {
		DmpCwbOrder dmpCwbOrder = cwbOrderWothDeliverystate.getCwbOrder();
		DmpDeliveryState deliveryState = cwbOrderWothDeliverystate.getDeliveryState();

		String desc = "";
		if (flowOrdertype == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {

			desc = "配送中";
		}
		if (delivery_state == DeliveryStateEnum.PeiSongChengGong.getValue() || delivery_state == DeliveryStateEnum.ShangMenHuanChengGong.getValue()
				|| delivery_state == DeliveryStateEnum.ShangMenTuiChengGong.getValue()) {
			desc = "配送成功";
		} else if (delivery_state == DeliveryStateEnum.QuanBuTuiHuo.getValue() || delivery_state == DeliveryStateEnum.ShangMenJuTui.getValue()
				|| delivery_state == DeliveryStateEnum.BuFenTuiHuo.getValue()) {
			desc = "客户拒收";
		} else if (delivery_state == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
			desc = dmpCwbOrder.getLeavedreason();
		}

		if (!desc.isEmpty()) {

			long deliverid = deliveryState != null ? deliveryState.getDeliveryid() : orderFlow.getUserid();
			User user = getDmpdao.getUserById(deliverid);
			String operatorname = user.getRealname();
			String mobilephone = user.getUsermobile();
			String nowtime = DateTimeUtil.getNowTime();

			save(orderFlow.getCwb(), customer.getCustomerid(), DateTimeUtil.formatDate(orderFlow.getCredate()), flowOrdertype, delivery_state, desc, operatorname, dmpCwbOrder.getConsigneename(),
					mobilephone, "", nowtime);
		}
	}

	/**
	 * 存储 b2c数据，目前支持梦芭莎
	 */
	public void save(String cwb, long customerid, String cretime, long flowordertype, long deliverystate, String desc, String operator, String signname, String mobilephone, String remark,
			String nowtime) {
		B2cDatasearch b2cdata = b2cDatasearchDAO.getDataByKeys(cwb, flowordertype);

		if (b2cdata == null) {
			b2cDatasearchDAO.save(cwb, customerid, cretime, flowordertype, deliverystate, desc, operator, signname, mobilephone, remark, nowtime);
		} else {
			// 如果存在 并且等于配送或者拒收
			if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue()) {
				b2cDatasearchDAO.updateState(b2cdata.getB2cid(), 0);
			}
			b2cDatasearchDAO.save(cwb, customerid, cretime, flowordertype, deliverystate, desc, operator, signname, mobilephone, remark, nowtime);
		}

	}

}
