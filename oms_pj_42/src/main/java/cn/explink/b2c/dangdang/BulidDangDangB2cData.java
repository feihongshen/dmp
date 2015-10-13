package cn.explink.b2c.dangdang;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2CDataDAO;
import cn.explink.b2c.tools.B2cDataOrderFlowDetail;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.dao.GetDmpDAO;
import cn.explink.domain.B2CData;
import cn.explink.domain.Branch;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.jms.dto.DmpCwbOrder;
import cn.explink.jms.dto.DmpDeliveryState;
import cn.explink.jms.dto.DmpOrderFlow;
import cn.explink.util.DateTimeUtil;

@Service
public class BulidDangDangB2cData {
	private Logger logger = LoggerFactory.getLogger(BulidDangDangB2cData.class);
	@Autowired
	B2cDataOrderFlowDetail orderFlowDetail;
	@Autowired
	private B2cTools b2ctools;
	@Autowired
	DangDangService dangdangService;
	@Autowired
	GetDmpDAO getDmpdao;
	@Autowired
	B2CDataDAO b2CDataDAO;

	public String BuildDangDangMethod(DmpOrderFlow orderFlow, long flowOrdertype, DmpCwbOrder cwbOrder, DmpDeliveryState deliverstate, ObjectMapper objectMapper) throws Exception {

		dangdangService.feedBackToDangDangFlowStatus(orderFlow); // 反馈给当当跟踪日志
		String DangDangReceiveStatus = dangdangService.getDangDangFlowEnum(flowOrdertype);
		if (DangDangReceiveStatus == null) {
			logger.info("订单号：{} 不属于 当当所需要的json---,状态{}，return", cwbOrder.getCwb(), flowOrdertype);
			return null;
		}
		// 各个节点的反馈信息
		DangDangXMLNote xmlnote = new DangDangXMLNote();
		if (flowOrdertype == FlowOrderTypeEnum.RuKu.getValue() && CwbOrderTypeIdEnum.Shangmentui.getValue() != Integer.valueOf(cwbOrder.getCwbordertypeid())) {
			xmlnote.setCwb(cwbOrder.getCwb());
			xmlnote.setIn_storage_date(DateTimeUtil.formatDate(orderFlow.getCredate()));
			xmlnote.setOperator(getDmpdao.getUserById(orderFlow.getUserid()).getRealname());

		} else if ((flowOrdertype == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() || flowOrdertype == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue())
				&& CwbOrderTypeIdEnum.Shangmentui.getValue() == Integer.valueOf(cwbOrder.getCwbordertypeid())) {
			xmlnote.setCwb(cwbOrder.getCwb());
			xmlnote.setIn_storage_date(DateTimeUtil.formatDate(orderFlow.getCredate()));
			xmlnote.setOperator(getDmpdao.getUserById(orderFlow.getUserid()).getRealname());

		} else if (flowOrdertype == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
			xmlnote.setCwb(cwbOrder.getCwb());
			xmlnote.setOut_storage_date(DateTimeUtil.formatDate(orderFlow.getCredate()));
			xmlnote.setOperator(getDmpdao.getUserById(orderFlow.getUserid()).getRealname());

			// 分站名称+分站电话 =express_operator_name
			Branch nowbranch = getDmpdao.getNowBranch(orderFlow.getBranchid());
			String branch_name = nowbranch.getBranchname();
			String branch_tel = nowbranch.getBranchphone();
			String express_operator_name = "【" + branch_name + "】【" + branch_tel + "】";
			xmlnote.setExpress_operator_name(express_operator_name);

			// 派送员名称+派送员电话 =express_operator_name
			String deliver_name = getDmpdao.getUserById(deliverstate.getDeliveryid()).getRealname();
			String deliver_tel = getDmpdao.getUserById(deliverstate.getDeliveryid()).getUsermobile();
			String express_operator_tel = "【" + deliver_name + "】【" + deliver_tel + "】";
			xmlnote.setExpress_operator_tel(express_operator_tel);

			xmlnote.setExpress_operator_id(cwbOrder.getDeliverid());
		} else if (flowOrdertype == FlowOrderTypeEnum.YiShenHe.getValue()) { // 投递反馈

			try {
				if (flowOrdertype == FlowOrderTypeEnum.YiShenHe.getValue() || flowOrdertype == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
					B2CData b2CData = b2CDataDAO.getB2cDataByKeys(orderFlow.getCwb(), FlowOrderTypeEnum.YiShenHe.getValue(), 0);
					if (b2CData != null) {
						logger.warn("系统自动屏蔽状态={},订单号={}", FlowOrderTypeEnum.YiShenHe.getValue(), cwbOrder.getCwb());
						b2CDataDAO.updateB2cIdSQLResponseStatus(b2CData.getB2cid(), 3, "系统自动屏蔽此状态");
					}
				}

			} catch (Exception e) {
				logger.error("如风达修改短时间内未推送36状态发生未知异常", e);
			}

			xmlnote.setCwb(cwbOrder.getCwb());
			xmlnote.setSign_date(DateTimeUtil.formatDate(orderFlow.getCredate()));
			xmlnote.setSign_person(cwbOrder.getConsigneenameSpecial());
			xmlnote.setOperator(getDmpdao.getUserById(orderFlow.getUserid()).getRealname());
			xmlnote.setOrder_status(dangdangService.getOrderDeliverStateByStatus(deliverstate.getDeliverystate()));
			String reason_code = b2ctools.getExptReasonByB2c(cwbOrder.getLeavedreasonid(), cwbOrder.getBackreasonid(), cwbOrder.getCustomerid() + "", deliverstate.getDeliverystate()).getExpt_code();

			String dd_deliveryState = dangdangService.getOrderDeliverStateByStatus(deliverstate.getDeliverystate()); // 返回当当的异常码
			if (reason_code == null || "".equals(reason_code)) {

				if ("102".equals(dd_deliveryState)) {
					reason_code = "299";
				}
				if ("103".equals(dd_deliveryState)) { // 103为配送失败
					if (deliverstate.getDeliverystate() == DeliveryStateEnum.HuoWuDiuShi.getValue()) { // 如果为订单丢失，则默认为101异常原因
						reason_code = "101";
					} else {
						reason_code = "199";
					}

				}
			}

			xmlnote.setReason("101".equals(dd_deliveryState) ? "0" : reason_code);
		} else {
			return null;
		}
		logger.info("订单号：{}封装成[当当]所需要的json数据成功,状态：{}", cwbOrder.getCwb(), DangDangReceiveStatus);

		return objectMapper.writeValueAsString(xmlnote);

	}

}
