package cn.explink.b2c.sfxhm;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2cDataOrderFlowDetail;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.dao.GetDmpDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.User;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.jms.dto.DmpCwbOrder;
import cn.explink.jms.dto.DmpDeliveryState;
import cn.explink.jms.dto.DmpOrderFlow;
import cn.explink.util.DateTimeUtil;

@Service
public class BuildSfxhmB2cData {
	private Logger logger = LoggerFactory.getLogger(BuildSfxhmB2cData.class);
	@Autowired
	B2cDataOrderFlowDetail orderFlowDetail;

	@Autowired
	GetDmpDAO getDmpdao;
	@Autowired
	SfxhmService sfxhmService;

	public String buildHomegobjMethod(DmpOrderFlow orderFlow, long flowOrdertype, DmpCwbOrder cwbOrder, long delivery_state, DmpDeliveryState dmpDeliveryState, ObjectMapper objectMapper)
			throws IOException, JsonGenerationException, JsonMappingException {

		String dmsReceiveStatus = sfxhmService.getFlowEnum(flowOrdertype, delivery_state);
		if (dmsReceiveStatus == null) {
			logger.info("订单号：{} 不属于[顺风-小红帽]所需要的json---,状态{}，return", cwbOrder.getCwb(), flowOrdertype);
			return null;
		}

		Sfxhm sm = sfxhmService.getSfxhm(B2cEnum.SFexpressXHM.getKey());
		User user = getDmpdao.getUserById(orderFlow.getUserid());
		Branch branch = getDmpdao.getNowBranch(orderFlow.getBranchid());
		String shipper = "";
		User deliveryUser = null;
		if (flowOrdertype == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
			deliveryUser = getDmpdao.getUserById(dmpDeliveryState.getDeliveryid());
			shipper = deliveryUser.getRealname();
		}

		String desc = getDesc(orderFlow, cwbOrder, dmsReceiveStatus, sm, user, branch, deliveryUser);

		SfxhmNote note = new SfxhmNote();
		note.setMailno(orderFlow.getCwb());
		note.setNote(desc);
		note.setOperator(user.getRealname());
		note.setScan_time(DateTimeUtil.formatDate(orderFlow.getCredate()));
		note.setScan_type(dmsReceiveStatus);
		note.setScan_zone(branch.getBranchname());
		note.setShipper(shipper);
		note.setUpload_time(DateTimeUtil.getNowTime());

		return objectMapper.writeValueAsString(note);

	}

	private String getDesc(DmpOrderFlow orderFlow, DmpCwbOrder cwbOrder, String dmsReceiveStatus, Sfxhm sm, User user, Branch branch, User deliveryUser) {
		String desc = ""; // 描述
		if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.DaoRuShuJu.getValue()) {
			desc = "快件顺丰XX集散中心装车，发往" + sm.getCompanyname();
		} else if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.RuKu.getValue()) {
			desc = "快件到达" + sm.getCompanyname() + "正在 卸车";
		} else if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) {
			Branch nextBranch = getDmpdao.getNowBranch(cwbOrder.getNextbranchid());
			desc = "快件由" + sm.getCompanyname() + "装车发往" + nextBranch.getBranchname();
		} else if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()) {
			desc = "快件到达" + branch.getBranchname() + "正在卸车";
		} else if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
			desc = "快件由" + user.getRealname() + "正在出仓,派件员:" + deliveryUser.getRealname() + ",电话：" + deliveryUser.getUsermobile();
		} else if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) {
			if (dmsReceiveStatus.equals(SfxhmTrackEnum.ZhiLiu.getState())) {
				desc = "快件由于原因 " + cwbOrder.getLeavedreason() + "派送不成功";
			} else if (dmsReceiveStatus.equals(SfxhmTrackEnum.QianShou.getState())) {
				if (deliveryUser != null) {
					desc = "快件由" + deliveryUser.getRealname() + "上门派件,签收成功";
				} else {
					desc = "快件由" + user.getRealname() + "上门派件,签收成功";
				}
			}
		}
		return desc;
	}

}
