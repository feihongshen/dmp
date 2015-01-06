package cn.explink.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.explink.domain.OverdueExMoVO;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.FlowOrderTypeEnum;

@Repository
public class OverdueExMoDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate = null;

	private Map<FlowOrderTypeEnum, String> flowFieldMap = null;

	@Autowired
	private CwbDAO cwbDAO = null;

	public void updateSmtOverdueTime(OrderFlow orderFlow, String strCreateDate) {
		int cwbOrderType = this.getCwbOrderType(orderFlow);
		if (cwbOrderType != 2) {
			return;
		}
		this.updateTime(orderFlow, strCreateDate);
	}

	private void updateTime(OrderFlow orderFlow, String strCreateDate) {
		FlowOrderTypeEnum flowOrderType = FlowOrderTypeEnum.getText(orderFlow.getFlowordertype());
		if (!this.getFlowFieldMap().containsKey(flowOrderType)) {
			return;
		}
		String field = this.getFlowFieldMap().get(flowOrderType);
		String sql = this.getFlowUpdateSql(field);
		this.getJdbcTemplate().update(sql, strCreateDate, strCreateDate);
	}

	private String getFlowUpdateSql(String field) {
		return "update express_ops_smt_cwb_opt_time set " + field + " = ? where cwb = ?";
	}

	private int getCwbOrderType(OrderFlow orderFlow) {
		return this.getCwbDAO().getCwbOrderType(orderFlow.getCwb());
	}

	private boolean isCwbExist(OrderFlow orderFlow) {
		String sql = "select count(cwb) from express_ops_smt_cwb_opt_time where cwb = ?";
		int count = this.getJdbcTemplate().queryForInt(sql, orderFlow.getCwb());

		return count != 0;
	}

	private JdbcTemplate getJdbcTemplate() {
		return this.jdbcTemplate;
	}

	private CwbDAO getCwbDAO() {
		return this.cwbDAO;
	}

	private Map<FlowOrderTypeEnum, String> getFlowFieldMap() {
		if (this.flowFieldMap == null) {
			this.initFlowFieldMap();
		}
		return this.flowFieldMap;
	}

	private void initFlowFieldMap() {
		Map<FlowOrderTypeEnum, String> flowFieldMap = new HashMap<FlowOrderTypeEnum, String>();
		flowFieldMap.put(FlowOrderTypeEnum.DaoRuShuJu, OverdueExMoVO.SYSTEM_ACCEPT_TIME);
		flowFieldMap.put(FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao, OverdueExMoVO.STATION_ACCEPT_TIME);
		flowFieldMap.put(FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao, OverdueExMoVO.STATION_ACCEPT_TIME);
		flowFieldMap.put(FlowOrderTypeEnum.FenZhanLingHuo, OverdueExMoVO.DISPATCH_TIME);
		flowFieldMap.put(FlowOrderTypeEnum.ChaoQu, OverdueExMoVO.REPORT_OUTAREA_TIME);
		flowFieldMap.put(FlowOrderTypeEnum.YiChangPiPeiYiChuLi, OverdueExMoVO.EXCEPTION_MATCH_TIME);
		flowFieldMap.put(FlowOrderTypeEnum.YiFanKui, OverdueExMoVO.FEEDBACK_TIME);
	}
}
