package cn.explink.b2c.tools;

import java.text.MessageFormat;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.vipshop.BulidVipShopB2cData;
import cn.explink.b2c.yihaodian.YihaodianFlowEnum;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.GetDmpDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.CwbOrderCopyForDmp;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.User;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.jms.dto.CwbOrderWithDeliveryState;
import cn.explink.jms.dto.DmpDeliveryState;
import cn.explink.jms.dto.DmpOrderFlow;

/**
 * 订单流转详情，用于对接状态反馈
 * 
 * @author Administrator
 *
 */
@Service
public class B2cDataOrderFlowDetail {
	@Autowired
	GetDmpDAO getdmpDao;
	@Autowired
	DeliveryStateDAO deliveryDAO;
	@Autowired
	CacheBaseListener cacheBaseListener;

	private Logger logger = LoggerFactory.getLogger(BulidVipShopB2cData.class);

	private Branch getBranchById(long branchid) {
		return getdmpDao.getNowBranch(branchid);
	}

	private User getUserById(long userid) {
		return getdmpDao.getUserById(userid);
	}

	public String getDetail(DmpOrderFlow orderFlow) {
		try {
			User operatorUser = getUserById(orderFlow.getUserid());
			logger.info("====cwb={},flowordertype="+orderFlow.getFlowordertype()+",user={}==================",orderFlow.getCwb(),JacksonMapper.getInstance().writeValueAsString(operatorUser));
			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.DaoRuShuJu.getValue()) {
				return MessageFormat.format("从[{0}]导入数据", getBranchById(orderFlow.getBranchid()).getBranchname());
			}
			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.RuKu.getValue()) {
				return MessageFormat.format("从[{0}]入库", getBranchById(orderFlow.getBranchid()).getBranchname(), operatorUser.getUsermobile());
			}
			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) {
				CwbOrderWithDeliveryState cwbOrderWithDeliveryState = new ObjectMapper().readValue(orderFlow.getFloworderdetail().toString(), CwbOrderWithDeliveryState.class);
				Branch nextBranch = getBranchById(cwbOrderWithDeliveryState.getCwbOrder().getNextbranchid());
				return MessageFormat.format("从[{0}]出库，下一站[{1}]，联系电话[{2}]", getBranchById(orderFlow.getBranchid()).getBranchname(), nextBranch.getBranchname(), operatorUser.getUsermobile());
			}
			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.CheXiaoFanKui.getValue()) {
				return MessageFormat.format("货物由[{0}]撤销反馈", getBranchById(orderFlow.getBranchid()).getBranchname());
			}
			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.KuDuiKuTuiHuoChuKu.getValue()) {
				return MessageFormat.format("货物已由[{0}]进行库对库退货出库", getBranchById(orderFlow.getBranchid()).getBranchname());
			}
			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.ErJiFenBo.getValue()) {
				return MessageFormat.format("货物从[{0}]进行二级分拨操作", getBranchById(orderFlow.getBranchid()).getBranchname());
			}
			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.EeJiZhanTuiHuoChuZhan.getValue()) {
				return MessageFormat.format("货物已由二级站[{0}]退货出站", getBranchById(orderFlow.getBranchid()).getBranchname());
			}
			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()) {
				return MessageFormat.format("货物已到[{0}]，联系方式[{1}]", getBranchById(orderFlow.getBranchid()).getBranchname(), operatorUser.getUsermobile());
			}
			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()) {
				return MessageFormat.format("货物已到[{0}]", getBranchById(orderFlow.getBranchid()).getBranchname());
			}
			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
				CwbOrderWithDeliveryState cwbOrderWithDeliveryState = new ObjectMapper().readValue(orderFlow.getFloworderdetail().toString(), CwbOrderWithDeliveryState.class);
				User deliverUser = getUserById(cwbOrderWithDeliveryState.getDeliveryState().getDeliveryid());

				String comment = orderFlow.getComment();
				if (comment != null && !comment.isEmpty() && comment.contains("正在派件") && comment.contains("派件人")) {
					return MessageFormat.format("货物已从[{0}]" + comment, getBranchById(orderFlow.getBranchid()).getBranchname());
				} else {
					return MessageFormat.format("货物由[{0}]的派件员[{1}]正在派件..电话[{2}]", getBranchById(orderFlow.getBranchid()).getBranchname(), deliverUser.getRealname(), deliverUser.getUsermobile());
				}

			}
			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.YiFanKui.getValue()) {
				CwbOrderWithDeliveryState cwbOrderWithDeliveryState = new ObjectMapper().readValue(orderFlow.getFloworderdetail().toString(), CwbOrderWithDeliveryState.class);
				DmpDeliveryState deliverstate = cwbOrderWithDeliveryState.getDeliveryState();

				return MessageFormat.format("货物已由[{0}]的派送员[{1}]反馈为[{2}],电话[{3}]", getBranchById(orderFlow.getBranchid()).getBranchname(), getUserById(deliverstate.getDeliveryid()).getRealname(),
						DeliveryStateEnum.getByValue((int) deliverstate.getDeliverystate()).getText(), getUserById(deliverstate.getDeliveryid()).getUsermobile());

			}
			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.TuiHuoChuKuSaoMiao.getValue()) {
				return MessageFormat.format("货物已从[{0}]进行退货出库", getBranchById(orderFlow.getBranchid()).getBranchname());
			}
			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue()) {
				return MessageFormat.format("货物已到退货站[{0}]", getBranchById(orderFlow.getBranchid()).getBranchname());
			}
			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.TuiHuoZhanZaiTouSaoMiao.getValue()) {
				return MessageFormat.format("货物由[{0}]进行退货站再投", getBranchById(orderFlow.getBranchid()).getBranchname());
			}
			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue()) {
				return MessageFormat.format("货物已由[{0}]退供货商出库", getBranchById(orderFlow.getBranchid()).getBranchname());
			}
			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.GongHuoShangTuiHuoChenggong.getValue()) {
				return MessageFormat.format("货物已由[{0}]退供货商成功", getBranchById(orderFlow.getBranchid()).getBranchname());
			}
			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) {
				CwbOrderWithDeliveryState cwbOrderWithDeliveryState = new ObjectMapper().readValue(orderFlow.getFloworderdetail().toString(), CwbOrderWithDeliveryState.class);
				DmpDeliveryState deliverstate = cwbOrderWithDeliveryState.getDeliveryState();
				String exptMsg = "";
				if (deliverstate.getDeliverystate() == DeliveryStateEnum.QuanBuTuiHuo.getValue() || deliverstate.getDeliverystate() == DeliveryStateEnum.BuFenTuiHuo.getValue()
						|| deliverstate.getDeliverystate() == DeliveryStateEnum.FenZhanZhiLiu.getValue() || deliverstate.getDeliverystate() == DeliveryStateEnum.HuoWuDiuShi.getValue()) {
					exptMsg = ",原因:" + cwbOrderWithDeliveryState.getCwbOrder().getBackreason() + "" + cwbOrderWithDeliveryState.getCwbOrder().getLeavedreason();
				}
				return MessageFormat.format("货物已由[{0}]的小件员[{1}]反馈为[{2}],电话[{3}]" + exptMsg, getBranchById(orderFlow.getBranchid()).getBranchname(), getUserById(deliverstate.getDeliveryid())
						.getRealname(), DeliveryStateEnum.getByValue((int) deliverstate.getDeliverystate()).getText(), getUserById(deliverstate.getDeliveryid()).getUsermobile());
			}
			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.ZhongZhuanChuKuSaoMiao.getValue()) {
				return MessageFormat.format("货物已从[{0}]进行中转出库", getBranchById(orderFlow.getBranchid()).getBranchname());
			}
			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue()) {
				return MessageFormat.format("货物已到中转站[{0}]", getBranchById(orderFlow.getBranchid()).getBranchname());
			}
			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.ZhongZhuanZhanChuKuSaoMiao.getValue()) {
				return MessageFormat.format("货物已从[{0}]进行中转站出库", getBranchById(orderFlow.getBranchid()).getBranchname());
			}
		} catch (Exception e) {
			e.printStackTrace();

			return null;
		}
		return null;
	}

	public String getDetailByOrderFlow(OrderFlow orderFlow) {
		try {
			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.DaoRuShuJu.getValue()) {
				return MessageFormat.format("从[{0}]导入数据", getBranchById(orderFlow.getBranchid()).getBranchname());
			}
			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.RuKu.getValue()) {
				return MessageFormat.format("从[{0}]入库", getBranchById(orderFlow.getBranchid()).getBranchname());
			}
			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) {
				return MessageFormat.format("从[{0}]出库", getBranchById(orderFlow.getBranchid()).getBranchname());
			}
			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.CheXiaoFanKui.getValue()) {
				return MessageFormat.format("货物由[{0}]撤销反馈", getBranchById(orderFlow.getBranchid()).getBranchname());
			}
			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.KuDuiKuTuiHuoChuKu.getValue()) {
				return MessageFormat.format("货物已由[{0}]进行库对库退货出库", getBranchById(orderFlow.getBranchid()).getBranchname());
			}
			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.ErJiFenBo.getValue()) {
				return MessageFormat.format("货物从[{0}]进行二级分拨操作", getBranchById(orderFlow.getBranchid()).getBranchname());
			}
			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.EeJiZhanTuiHuoChuZhan.getValue()) {
				return MessageFormat.format("货物已由二级站[{0}]退货出站", getBranchById(orderFlow.getBranchid()).getBranchname());
			}
			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()) {
				return MessageFormat.format("货物已到[{0}]", getBranchById(orderFlow.getBranchid()).getBranchname());
			}
			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
				return MessageFormat.format("货物已从[{0}]的投递员分站领货", getBranchById(orderFlow.getBranchid()).getBranchname());
			}
			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.YiFanKui.getValue()) {
				DeliveryState delivery = deliveryDAO.getDeliveryStateByCwb("'" + orderFlow.getCwb() + "'");
				String podresult = getPodresultText(delivery.getDeliverystate());
				return MessageFormat.format("货物已由[{0}]的投递员反馈为[{1}]", getBranchById(orderFlow.getBranchid()).getBranchname(), podresult);
			}
			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.TuiHuoChuKuSaoMiao.getValue()) {
				return MessageFormat.format("货物已从[{0}]进行退货出库", getBranchById(orderFlow.getBranchid()).getBranchname());
			}
			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue()) {
				return MessageFormat.format("货物已到退货站[{0}]", getBranchById(orderFlow.getBranchid()).getBranchname());
			}
			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.TuiHuoZhanZaiTouSaoMiao.getValue()) {
				return MessageFormat.format("货物由[{0}]进行退货站再投", getBranchById(orderFlow.getBranchid()).getBranchname());
			}
			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue()) {
				return MessageFormat.format("货物已由[{0}]退供货商出库", getBranchById(orderFlow.getBranchid()).getBranchname());
			}
			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.GongHuoShangTuiHuoChenggong.getValue()) {
				return MessageFormat.format("货物已由[{0}]退供货商成功", getBranchById(orderFlow.getBranchid()).getBranchname());
			}
			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) {
				DeliveryState deliverstate = deliveryDAO.getDeliveryStateByCwb("'" + orderFlow.getCwb() + "'");
				String podresult = getPodresultText(deliverstate.getDeliverystate());
				CwbOrderCopyForDmp cwborder = getdmpDao.getCwbDetailsByCwb(orderFlow.getCwb());
				String exptMsg = "";
				if (deliverstate.getDeliverystate() == DeliveryStateEnum.QuanBuTuiHuo.getValue() || deliverstate.getDeliverystate() == DeliveryStateEnum.BuFenTuiHuo.getValue()
						|| deliverstate.getDeliverystate() == DeliveryStateEnum.FenZhanZhiLiu.getValue() || deliverstate.getDeliverystate() == DeliveryStateEnum.HuoWuDiuShi.getValue()) {
					exptMsg = ",原因:" + cwborder.getBackreason() + "" + cwborder.getLeavedreason();
				}
				return MessageFormat.format("货物已由[{0}]的小件员[{1}]反馈为[{2}]" + exptMsg, getBranchById(orderFlow.getBranchid()).getBranchname(), getUserById(deliverstate.getDeliveryid()).getRealname(),
						podresult);
			}
			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.ZhongZhuanChuKuSaoMiao.getValue()) {
				return MessageFormat.format("货物已从[{0}]进行中转出库", getBranchById(orderFlow.getBranchid()).getBranchname());
			}
			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue()) {
				return MessageFormat.format("货物已到中转站[{0}]", getBranchById(orderFlow.getBranchid()).getBranchname());
			}
			if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.ZhongZhuanZhanChuKuSaoMiao.getValue()) {
				return MessageFormat.format("货物已从[{0}]进行中转站出库", getBranchById(orderFlow.getBranchid()).getBranchname());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("oms 公共方法转换订单流程发生未知异常", e);
			return null;
		}
		return null;
	}

	public String getPodresultText(long delivery_state) {

		if (delivery_state == DeliveryStateEnum.PeiSongChengGong.getValue()) {
			return YihaodianFlowEnum.PeiSongChengGong.getText();
		}
		if (delivery_state == DeliveryStateEnum.QuanBuTuiHuo.getValue()) {
			return YihaodianFlowEnum.JuShou.getText();
		}
		if (delivery_state == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
			return YihaodianFlowEnum.FenZhanZhiLiu.getText();
		}
		if (delivery_state == DeliveryStateEnum.ShangMenHuanChengGong.getValue()) {
			return YihaodianFlowEnum.ShangMenHuanChengGong.getText();
		}
		if (delivery_state == DeliveryStateEnum.ShangMenTuiChengGong.getValue()) {
			return YihaodianFlowEnum.ShangMenTuiChengGong.getText();
		}
		if (delivery_state == DeliveryStateEnum.ShangMenJuTui.getValue()) {
			return YihaodianFlowEnum.JuShou.getText();
		}
		if (delivery_state == DeliveryStateEnum.BuFenTuiHuo.getValue()) {
			return YihaodianFlowEnum.BuFenShiBai.getText();
		}
		return "";

	}
}
