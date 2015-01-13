package cn.explink.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.explink.controller.CwbOrderDTO;
import cn.explink.domain.OverdueExMoVO;
import cn.explink.domain.PrintcwbDetail;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.DateTimeUtil;

@Repository
public class OverdueExMoDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate = null;

	private Set<FlowOrderTypeEnum> allowUpdateFlowSet = null;

	@Autowired
	private CwbDAO cwbDAO = null;

	public void updateSmtOverdueTime(OrderFlow orderFlow, String strCreateDate) {
		int cwbOrderType = this.getCwbOrderType(orderFlow);
		if (cwbOrderType != 2) {
			return;
		}
		this.updateTime(orderFlow, strCreateDate);
	}

	public void updateDeliverState(String cwb, int deliverState, BigDecimal receivedFeed) {
		String updateSql = this.getUpdateDeliverStateSql();
		this.getJdbcTemplate().update(updateSql, deliverState, receivedFeed, cwb);
	}

	public void updateDeliverState(String cwb, int deliverState) {
		String sql = this.getUpdateDeliverStateSql();
		String time = DateTimeUtil.getNowTime();
		this.getJdbcTemplate().update(sql, deliverState, time, cwb);
	}

	public void insertSmtOrder(CwbOrderDTO dto) {
		// 删除存在数据.
		this.deleteExistData(dto);
		// 插入数据.
		this.insertData(dto);
	}

	public void updatePrintTime(PrintcwbDetail printDetail) {
		String detail = printDetail.getPrintdetail();
		String[] cwbs = detail.split(",");
		if (cwbs.length == 0) {
			return;
		}
		String inPara = this.getCwbInPara(cwbs);
		String sql = this.getUpdatePrintTimeSql(inPara);
		this.getJdbcTemplate().execute(sql);
	}

	public void updateDeliverInfo(String cwb, long deliverId, String dispatchTime) {
		String sql = this.getUpdateDeliverInfoSql();
		this.getJdbcTemplate().update(sql, deliverId, dispatchTime, cwb);
	}

	public void updateOutAreaTime(String[] cwbs, String strTime) {
		String inPara = this.getCwbInPara(cwbs);
		String sql = this.getupdateOutAreaTimeSql(inPara);
		this.getJdbcTemplate().update(sql, strTime);
	}

	public void updateEMHTime(String cwb, String strTime) {
		String sql = this.getUpdateEMHTimeSql();
		this.getJdbcTemplate().update(sql, strTime, cwb);
	}

	private String getUpdateEMHTimeSql() {
		return "update express_ops_smt_cwb_opt_time set exception_match_time = ? where cwb = ?";
	}

	private String getupdateOutAreaTimeSql(String inPara) {
		return "update express_ops_smt_cwb_opt_time set report_outarea_time = ? where cwb in(" + inPara + ")";
	}

	private String getUpdateDeliverInfoSql() {
		return "update express_ops_smt_cwb_opt_time set deliver_id = ? , dispatch_time = ? where cwb = ?";
	}

	private String getUpdatePrintTimeSql(String inPara) {
		return "update express_ops_smt_cwb_opt_time set print_time = ? where cwb in (" + inPara + ")";
	}

	private String getCwbInPara(String[] cwbs) {
		StringBuilder inPara = new StringBuilder();
		for (String cwb : cwbs) {
			inPara.append("'");
			inPara.append(cwb);
			inPara.append("'");
		}
		return inPara.toString();
	}

	private String getUpdateDeliverStateSql() {
		return "update express_ops_smt_cwb_opt_time set deliver_state = ?, received_fee = ? where cwb = ?";
	}

	private void insertData(CwbOrderDTO dto) {
		String insertSql = this.getInsertSql();
		String cwb = dto.getCwb();
		long venderId = dto.getCustomerid();
		String createTime = dto.getRemark2();
		int payType = (int) dto.getPaywayid();
		BigDecimal shouleFee = dto.getShouldfare();
		this.getJdbcTemplate().update(insertSql, cwb, venderId, createTime, payType, shouleFee);
	}

	private void deleteExistData(CwbOrderDTO dto) {
		this.getJdbcTemplate().update(this.getDeleteSql(), dto.getCwb(), dto.getCustomerid());
	}

	private String getDeleteSql() {
		return "delete from express_ops_smt_cwb_opt_time where cwb = ? and vender_id = ?";
	}

	private String getInsertSql() {
		StringBuilder sql = new StringBuilder();
		sql.append("insert into express_ops_smt_cwb_opt_time");
		sql.append("(cwb , vender_id , create_time , pay_type , should_fee)");
		sql.append("values(?,?,?,?,?)");

		return sql.toString();
	}

	private void updateTime(OrderFlow orderFlow, String strCreateDate) {
		FlowOrderTypeEnum flowOrderType = FlowOrderTypeEnum.getText(orderFlow.getFlowordertype());
		if (!this.getAllowUpdateFlowSet().contains(flowOrderType)) {
			return;
		}
		BaseAction action = this.getUpdateFieldAction(orderFlow, flowOrderType);
		action.update();
	}

	private BaseAction getUpdateFieldAction(OrderFlow orderFlow, FlowOrderTypeEnum flowOrderType) {
		if (FlowOrderTypeEnum.DaoRuShuJu.equals(flowOrderType)) {
			String[] fields = new String[] { OverdueExMoVO.SYSTEM_ACCEPT_TIME };
			Object[] paras = new Object[] { this.formateDate(orderFlow.getCredate()), orderFlow.getCwb() };
			return new BaseAction(fields, paras);
		} else if (FlowOrderTypeEnum.RuKu.equals(flowOrderType)) {
			String[] fields = new String[] { OverdueExMoVO.WAREHOUSE_ID };
			Object[] paras = new Object[] { orderFlow.getBranchid(), orderFlow.getCwb() };
			return new BaseAction(fields, paras);
		} else if (FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.equals(flowOrderType)) {
			String[] fields = new String[] { OverdueExMoVO.DELIVER_STATION_ID, OverdueExMoVO.STATION_ACCEPT_TIME };
			Object[] paras = new Object[] { orderFlow.getBranchid(), this.formateDate(orderFlow.getCredate()), orderFlow.getCwb() };
			return new BaseAction(fields, paras);
		} else if (FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.equals(flowOrderType)) {
			String[] fields = new String[] { OverdueExMoVO.DELIVER_STATION_ID, OverdueExMoVO.STATION_ACCEPT_TIME };
			Object[] paras = new Object[] { orderFlow.getBranchid(), this.formateDate(orderFlow.getCredate()), orderFlow.getCwb() };
			return new BaseAction(fields, paras);
		} else if (FlowOrderTypeEnum.ChaoQu.equals(flowOrderType)) {
			String[] fields = new String[] { OverdueExMoVO.REPORT_OUTAREA_TIME };
			Object[] paras = new Object[] { this.formateDate(orderFlow.getCredate()), orderFlow.getCwb() };
			return new BaseAction(fields, paras);
		} else if (FlowOrderTypeEnum.YiChangPiPeiYiChuLi.equals(flowOrderType)) {
			String[] fields = new String[] { OverdueExMoVO.EXCEPTION_MATCH_TIME };
			Object[] paras = new Object[] { this.formateDate(orderFlow.getCredate()), orderFlow.getCwb() };
			return new BaseAction(fields, paras);
		} else {
			return null;
		}
	}

	private String formateDate(Date date) {
		return DateTimeUtil.formatDate(date);
	}

	private int getCwbOrderType(OrderFlow orderFlow) {
		return this.getCwbDAO().getCwbOrderType(orderFlow.getCwb());
	}

	private JdbcTemplate getJdbcTemplate() {
		return this.jdbcTemplate;
	}

	private CwbDAO getCwbDAO() {
		return this.cwbDAO;
	}

	private Set<FlowOrderTypeEnum> getAllowUpdateFlowSet() {
		if (this.allowUpdateFlowSet == null) {
			this.initAllowUpdateFlowSet();
		}
		return this.allowUpdateFlowSet;
	}

	private void initAllowUpdateFlowSet() {
		Set<FlowOrderTypeEnum> flowSet = new HashSet<FlowOrderTypeEnum>();
		flowSet.add(FlowOrderTypeEnum.DaoRuShuJu);
		flowSet.add(FlowOrderTypeEnum.RuKu);
		flowSet.add(FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao);
		flowSet.add(FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao);
		flowSet.add(FlowOrderTypeEnum.FenZhanLingHuo);
		flowSet.add(FlowOrderTypeEnum.ChaoQu);
		flowSet.add(FlowOrderTypeEnum.YiChangPiPeiYiChuLi);

		this.allowUpdateFlowSet = flowSet;
	}

	private abstract class UpdateAction {
		protected abstract String[] getUpdateFields();

		protected abstract Object[] getUpdateParas();

		public void update() {
			String sql = this.getUpdateSql();
			this.getJdbcTemplate().update(sql, this.getUpdateParas());
		}

		private String getUpdateSql() {
			StringBuilder sql = new StringBuilder();
			sql.append("update express_ops_smt_cwb_opt_time set ");
			int length = this.getUpdateFields().length;
			for (int i = 0; i < length; i++) {
				if (i == (length - 1)) {
					sql.append(this.getUpdateFields()[i] + " = ? ");
				} else {
					sql.append(this.getUpdateFields()[i] + " = ? , ");
				}
			}
			sql.append("where cwb = ? ");

			return sql.toString();
		}

		private JdbcTemplate getJdbcTemplate() {
			return OverdueExMoDAO.this.jdbcTemplate;
		}
	}

	private class BaseAction extends UpdateAction {

		private String[] fields = null;

		private Object[] paras = null;

		public BaseAction(String[] fields, Object[] paras) {
			this.fields = fields;
			this.paras = paras;
		}

		@Override
		protected String[] getUpdateFields() {
			return this.getFields();
		}

		@Override
		protected Object[] getUpdateParas() {
			return this.getParas();
		}

		private String[] getFields() {
			return this.fields;
		}

		private Object[] getParas() {
			return this.paras;
		}

	}

}
