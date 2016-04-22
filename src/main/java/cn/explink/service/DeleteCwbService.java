package cn.explink.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.dao.AccountCwbFareDAO;
import cn.explink.dao.AccountCwbFareDetailDAO;
import cn.explink.dao.DeleteCwbDAO;
import cn.explink.dao.DeleteCwbRecordDAO;
import cn.explink.dao.EmailDateDAO;
import cn.explink.dao.MqExceptionDAO;
import cn.explink.dao.OrderArriveTimeDAO;
import cn.explink.domain.AccountCwbFare;
import cn.explink.domain.AccountCwbFareDetail;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeleteCwbRecord;
import cn.explink.domain.MqExceptionBuilder;
import cn.explink.domain.User;

@Service
public class DeleteCwbService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	DeleteCwbDAO deleteCwbDAO;
	@Autowired
	EmailDateDAO emailDateDAO;
	@Autowired
	DeleteCwbRecordDAO deleteCwbRecordDAO;
	@Autowired
	OrderArriveTimeDAO orderArriveTimeDAO;
	@Autowired
	AccountCwbFareDetailDAO accountCwbFareDetailDAO;
	@Autowired
	AccountCwbFareDAO accountCwbFareDAO;

	@Produce(uri = "jms:topic:losecwb")
	ProducerTemplate losecwbProducerTemplate;
	
	@Autowired
	private MqExceptionDAO mqExceptionDAO;

	/**
	 * 数据删除功能
	 * 
	 * @param co
	 * @param user
	 */
	@Transactional
	public void deletecwb(CwbOrder co, User user) {
		try {
			// 删除问题件相关的表
			deleteCwbDAO.deleteAbnormalOrderByCwb(co.getOpscwbid());
			deleteCwbDAO.deleteAbnormalWriteBackByOpscwbid(co.getOpscwbid());

			// 删除修改配送结果功能表
			deleteCwbDAO.deleteApplyEditDeliverystateByCwb(co.getCwb());
			// 删除投诉功能表
			deleteCwbDAO.deleteComplaintByCwb(co.getCwb());
			// 删除导入数据失败订单记录
			deleteCwbDAO.deleteCwbErrorByCwb(co.getCwb());
			// 删除异常单记录表
			deleteCwbDAO.deleteExceptionCwbByCwb(co.getCwb());
			// 删除打印批次中间表
			deleteCwbDAO.deleteGroupDetailByCwb(co.getCwb());
			// 删除超期异常监控表
			deleteCwbDAO.deleteOperationTimeByCwb(co.getCwb());
			// 删除orderflow操作表
			deleteCwbDAO.deleteOrderFlowByCwb(co.getCwb());
			// 删除备注表
			deleteCwbDAO.deleteRemarkByCwb(co.getCwb());
			// 删除返单表
			deleteCwbDAO.deleteReturnCwbsByCwb(co.getCwb());
			// 删除“数据失效”功能表
			deleteCwbDAO.deleteShiXiaoByCwb(co.getCwb());
			// 删除库存表
			deleteCwbDAO.deleteStockDetailByCwb(co.getCwb());
			// 删除运单号和订单号关联表
			deleteCwbDAO.deleteTransCwbByCwb(co.getCwb());
			// 删除一票多件操作记录表
			deleteCwbDAO.deleteTransCwbOrderflowByCwb(co.getCwb());
			// 删除退货记录表
			deleteCwbDAO.deleteTuihuoRecordByCwb(co.getCwb());
			// 删除一票多件缺件表
			deleteCwbDAO.deleteYpdjhandleRecordByCwb(co.getCwb());
			// 删除
			deleteCwbDAO.deleteB2CDataByCwb(co.getCwb());
			// 删除
			deleteCwbDAO.deleteBranchHisteryLogByCwb(co.getCwb());
			// 删除
			deleteCwbDAO.deleteCwbOrderB2CByCwb(co.getCwb());
			// 删除
			deleteCwbDAO.deleteCwbTempByCwb(co.getCwb());
			// 删除
			deleteCwbDAO.deleteDeliverCashByCwb(co.getCwb());
			// 删除
			deleteCwbDAO.deleteDeliveryPercentByCwb(co.getCwb());
			// 删除
			deleteCwbDAO.deleteDeliveryStateByCwb(co.getCwb());
			// 删除
			deleteCwbDAO.deleteEditCwbByCwb(co.getCwb());
			// 删除
			deleteCwbDAO.deleteFinanceAuditTempByCwb(co.getCwb());
			// 删除
			deleteCwbDAO.deleteOnetranscwbToMorecwbsByCwb(co.getCwb());
			// 删除
			deleteCwbDAO.deletePosPaydetailByCwb(co.getCwb());
			// 删除
			deleteCwbDAO.deleteSaohuobangInformationByCwb(co.getCwb());
			// 删除
			deleteCwbDAO.deleteSmsManageByCwb(co.getCwb());
			// 删除
			deleteCwbDAO.deleteSysScanByCwb(co.getCwb());
			// 删除
			deleteCwbDAO.deleteWarehouseToBranchByCwb(co.getCwb());
			// 删除
			deleteCwbDAO.deleteWarehouseToCommenByCwb(co.getCwb());
			// 删除到车时间表
			orderArriveTimeDAO.deleteOrderArriveTimeByCwb(co.getCwb());
			// 删除上门退运费表
			deleteCwbDAO.deleteAccountCwbFareDetailByCwb(co.getCwb());

			List<AccountCwbFareDetail> accountCwbFareDetailList = accountCwbFareDetailDAO.getAccountCwbFareDetailByCwb(co.getCwb());
			AccountCwbFare accountCwbFare = accountCwbFareDAO.getAccountCwbFareLocKById(accountCwbFareDetailList.size() > 0 ? accountCwbFareDetailList.get(0).getFareid() : 0);
			if (accountCwbFare != null) {
				if (accountCwbFare.getCashfee().compareTo(co.getInfactfare()) > 0) {
					// 如果现金金额大于该单应收运费，则从现金中扣除金额，反之从转账金额中去扣除
					BigDecimal nowFee = accountCwbFare.getCashfee().divide(co.getInfactfare());
					accountCwbFareDAO.saveAccountCwbFareById(nowFee, accountCwbFare.getGirofee(), accountCwbFare.getId());
				} else {
					BigDecimal nowFee = accountCwbFare.getGirofee().divide(co.getInfactfare());
					accountCwbFareDAO.saveAccountCwbFareById(accountCwbFare.getCashfee(), nowFee, accountCwbFare.getId());
				}
			}

			// 订单删除后减掉相应批次中的订单数
			long cwbcount = emailDateDAO.getEmailDateById(co.getEmaildateid()).getCwbcount() - 1;
			emailDateDAO.editEditEmaildateForCwbcount(cwbcount, co.getEmaildateid());

			// 产生删除记录
			DeleteCwbRecord deleteCwbRecord = new DeleteCwbRecord();
			deleteCwbRecord.setCwb(co.getCwb());
			deleteCwbRecord.setCustomerid(co.getCustomerid());
			deleteCwbRecord.setFlowordertype(co.getFlowordertype());
			deleteCwbRecord.setUserid(user.getUserid());
			deleteCwbRecord.setDeletetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

			deleteCwbRecordDAO.creDeleteCwbRecord(deleteCwbRecord);
			// 处理全部处理完毕后，删除express_ops_cwb_detail表中的订单信息
			deleteCwbDAO.deleteCwbOrderByCwb(co.getCwb());

			// 发送JMS
			try {
				loseCwbSendJMS(co.getCwb(), user.getUserid());
			} catch (Exception e) {
				logger.info("数据删除功能send JMS，cwb：{},error:{}", co.getCwb(), e);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("删除数据失败：" + e.getMessage());
		}
	}

	/**
	 * 数据删除发送JMS
	 * 
	 * @param cwb
	 * @param userid
	 */
	public void loseCwbSendJMS(String cwb, long userid) {
		try {
			logger.info("消息发送端：losecwbProducerTemplate, cwbAndUserid={}", cwb + "," + userid);
			losecwbProducerTemplate.sendBodyAndHeader(null, "cwbAndUserid", cwb + "," + userid);
		} catch (Exception ee) {
			logger.info("数据删除功能，cwb：{},error:{}", cwb, ee);
			//写MQ异常表
			this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode(this.getClass().getSimpleName() + ".loseCwbSendJMS")
					.buildExceptionInfo(ee.toString()).buildTopic(this.losecwbProducerTemplate.getDefaultEndpoint().getEndpointUri())
					.buildMessageHeader("cwbAndUserid", cwb + "," + userid).getMqException());
	
		}
	}
}
