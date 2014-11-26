package cn.explink.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DeleteCwbDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void deleteCwbOrderByCwb(String cwb) {
		jdbcTemplate.update("delete from express_ops_cwb_detail where state=1 and cwb=? ", cwb);
	}

	public void deleteWarehouseToCommenByCwb(String cwb) {
		jdbcTemplate.update("delete from commen_cwb_order where cwb=? ", cwb);
	}

	public void deleteEditCwbByCwb(String cwb) {
		jdbcTemplate.update("delete from edit_delivery_state_detail where cwb=? ", cwb);
	}

	public void deleteShiXiaoByCwb(String cwb) {
		jdbcTemplate.update("delete from edit_shixiao where cwb=? ", cwb);
	}

	public void deleteAbnormalOrderByCwb(long opscwbid) {
		jdbcTemplate.update("delete from express_ops_abnormal_order where opscwbid=? ", opscwbid);
	}

	public void deleteAbnormalWriteBackByOpscwbid(long opscwbid) {
		jdbcTemplate.update("delete from express_ops_abnormal_write_back where opscwbid=? ", opscwbid);
	}

	public void deleteApplyEditDeliverystateByCwb(String cwb) {
		jdbcTemplate.update("delete from express_ops_applyeditdeliverystate where cwb=? ", cwb);
	}

	public void deleteBranchHisteryLogByCwb(String cwb) {
		jdbcTemplate.update("delete from express_ops_branch_histerylog where cwb=? ", cwb);
	}

	public void deleteComplaintByCwb(String cwb) {
		jdbcTemplate.update("delete from express_ops_complaint where cwb=? ", cwb);
	}

	public void deleteCwbOrderB2CByCwb(String cwb) {
		jdbcTemplate.update("delete from express_ops_cwb_detail_b2ctemp where state=1 and cwb=? ", cwb);
	}

	public void deleteCwbErrorByCwb(String cwb) {
		jdbcTemplate.update("delete from express_ops_cwb_error where cwb=? ", cwb);
	}

	public void deleteCwbTempByCwb(String cwb) {
		jdbcTemplate.update("delete from express_ops_cwb_temp where cwb=? ", cwb);
	}

	public void deleteDeliverCashByCwb(String cwb) {
		jdbcTemplate.update("delete from express_ops_deliver_cash where cwb=? ", cwb);
	}

	public void deleteDeliveryPercentByCwb(String cwb) {
		jdbcTemplate.update("delete from express_ops_delivery_percent where cwb=? ", cwb);
	}

	public void deleteDeliveryStateByCwb(String cwb) {
		jdbcTemplate.update("delete from express_ops_delivery_state where cwb=? ", cwb);
	}

	public void deleteExceptionCwbByCwb(String cwb) {
		jdbcTemplate.update("delete from express_ops_exception_cwb where cwb=? ", cwb);
	}

	public void deleteGroupDetailByCwb(String cwb) {
		jdbcTemplate.update("delete from express_ops_groupdetail where cwb=? ", cwb);
	}

	public void deleteOperationTimeByCwb(String cwb) {
		jdbcTemplate.update("delete from express_ops_operation_time where cwb=? ", cwb);
	}

	public void deleteOrderFlowByCwb(String cwb) {
		jdbcTemplate.update("delete from express_ops_order_flow where cwb=? ", cwb);
	}

	public void deletePosPaydetailByCwb(String cwb) {
		jdbcTemplate.update("delete from express_ops_pos_paydetail where cwb=? ", cwb);
	}

	public void deleteStockDetailByCwb(String cwb) {
		jdbcTemplate.update("delete from express_ops_stock_detail where cwb=? ", cwb);
	}

	public void deleteTransCwbByCwb(String cwb) {
		jdbcTemplate.update("delete from express_ops_transcwb where cwb=? ", cwb);
	}

	public void deleteTransCwbOrderflowByCwb(String cwb) {
		jdbcTemplate.update("delete from express_ops_transcwb_orderflow where cwb=? ", cwb);
	}

	public void deleteWarehouseToBranchByCwb(String cwb) {
		jdbcTemplate.update("delete from express_ops_warehouse_to_branch where cwb=? ", cwb);
	}

	public void deleteSaohuobangInformationByCwb(String cwb) {
		jdbcTemplate.update("delete from express_saohuobang_information where cwb=? ", cwb);
	}

	public void deleteB2CDataByCwb(String cwb) {
		jdbcTemplate.update("delete from express_save_b2cdata where cwb=? ", cwb);
	}

	public void deleteRemarkByCwb(String cwb) {
		jdbcTemplate.update("delete from express_set_remark where cwb=? ", cwb);
	}

	public void deleteSysScanByCwb(String cwb) {
		jdbcTemplate.update("delete from express_sys_scan where cwb=? ", cwb);
	}

	public void deleteFinanceAuditTempByCwb(String cwb) {
		jdbcTemplate.update("delete from finance_audit_temp where cwb=? ", cwb);
	}

	public void deleteReturnCwbsByCwb(String cwb) {
		jdbcTemplate.update("delete from ops_returncwbs where cwb=? ", cwb);
	}

	public void deleteTuihuoRecordByCwb(String cwb) {
		jdbcTemplate.update("delete from ops_tuihuorecord where cwb=? ", cwb);
	}

	public void deleteYpdjhandleRecordByCwb(String cwb) {
		jdbcTemplate.update("delete from ops_ypdjhandlerecord where cwb=? ", cwb);
	}

	public void deleteOnetranscwbToMorecwbsByCwb(String cwb) {
		jdbcTemplate.update("delete from set_onetranscwb_to_morecwbs where cwb=? ", cwb);
	}

	public void deleteSmsManageByCwb(String cwb) {
		jdbcTemplate.update("delete from sms_manage where cwb=? ", cwb);
	}

	public void deleteAccountCwbFareDetailByCwb(String cwb) {
		jdbcTemplate.update("delete from account_cwb_fare_detail where cwb=? ", cwb);
	}
}
