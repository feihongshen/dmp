package cn.explink.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.domain.MatchExceptionOrder;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.SqlBuilder;

@Controller
@RequestMapping("/meh")
public class MatchExceptionController {

	@Autowired
	private CwbDAO cwbDAO;

	@Autowired
	private BranchDAO branchDAO;

	@RequestMapping("/matchexceptionhandle")
	public String matchExpectionHandle(Model model) {
		return "meh/matchexceptionhandle";
	}

	private int queryTransferOrderCount(boolean today, boolean transfer) {
		return this.cwbDAO.queryMatchExceptionOrderCount(this.getQueryTransferCountSql(today, transfer));
	}

	private List<MatchExceptionOrder> queryTransferOrder(boolean today, boolean transfer, int page) {
		return this.cwbDAO.queryMatchExceptionOrder(this.getQueryTransferOrderSql(today, transfer, page));
	}

	private int queryMatchOrderCount(boolean today, boolean match, int page) {
		return this.cwbDAO.queryMatchExceptionOrderCount(this.getMatchOrderCountSql(today, match));
	}

	private List<MatchExceptionOrder> queryMatchOrder(boolean today, boolean match, int page) {
		return this.cwbDAO.queryMatchExceptionOrder(this.getMatchOrderSql(today, match, page));
	}

	private List<MatchExceptionOrder> queryWaitHandleOrder(boolean today) {
		String sql = this.getQuerywaitHandleOrderSql(today, 1);

		return this.cwbDAO.queryMatchExceptionOrder(sql);
	}

	private String getQuerywaitHandleOrderSql(boolean today, int page) {
		StringBuilder sql = new StringBuilder();
		sql.append(this.getQueryTransferOrderSql(today, false, -1));
		sql.append(" union ");
		sql.append(this.getMatchOrderSql(today, false, -1));
		sql.append(" ");
		sql.append(this.getLimitSql(page));

		return sql.toString();
	}

	private String getQueryTransferOrderSql(boolean today, boolean transfer, int page) {
		SqlBuilder sql = new SqlBuilder();
		sql.appendSelectPart(this.getSelectOrderPart());
		this.appendTransferSqlWhereCond(sql, today, transfer);
		sql.appendExtraPart(this.getLimitSql(page));

		return sql.toString();
	}

	private String getQueryTransferCountSql(boolean today, boolean transfer) {
		SqlBuilder sql = new SqlBuilder();
		sql.appendSelectPart(this.getSelectCountPart());
		this.appendTransferSqlWhereCond(sql, today, transfer);

		return sql.toString();
	}

	private String getMatchOrderCountSql(boolean today, boolean match) {
		SqlBuilder sql = new SqlBuilder();
		sql.appendSelectPart(this.getSelectCountPart());
		this.appendMatchSqlWhereCond(sql, today, match);

		return sql.toString();

	}

	private String getMatchOrderSql(boolean today, boolean match, int page) {
		SqlBuilder sql = new SqlBuilder();
		sql.appendSelectPart(this.getSelectOrderPart());
		this.appendMatchSqlWhereCond(sql, today, match);
		sql.appendExtraPart(this.getLimitSql(page));

		return sql.toString();
	}

	private String getLimitSql(int page) {
		if (page == -1) {
			return "";
		}
		int start = (page - 1) * 100;
		int end = page * 100;

		return "limit " + start + "," + end;
	}

	private void appendMatchSqlWhereCond(SqlBuilder sql, boolean today, boolean match) {
		// 增加前导列的命中率.
		if (match) {
			sql.appendCondition(this.getTimeWhereCond(today));
			sql.appendCondition(this.getOrderStatusWhereCond());
			sql.appendCondition(this.getMatchWhereCond(match));
		} else {
			sql.appendCondition(this.getMatchWhereCond(match));
			sql.appendCondition(this.getOrderStatusWhereCond());
			sql.appendCondition(this.getTimeWhereCond(today));
		}
	}

	private void appendTransferSqlWhereCond(SqlBuilder sql, boolean today, boolean transfer) {
		// 转单条件.
		sql.appendCondition(this.getTransferWhereCond(transfer));
		// 时间条件.
		sql.appendCondition(this.getTimeWhereCond(today));
		// 订单状态条件.
		sql.appendCondition(this.getOrderStatusWhereCond());
	}

	private String getTransferWhereCond(boolean transfer) {
		return "";
	}

	private String getOrderStatusWhereCond() {
		StringBuilder cond = new StringBuilder();
		int fzdh = FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue();
		cond.append("f.flowordertype = ");
		cond.append(fzdh);

		return cond.toString();
	}

	private String getMatchWhereCond(boolean match) {
		StringBuilder cond = new StringBuilder();
		if (match) {
			cond.append("d.nextbranchid != 0 ");
		} else {
			cond.append("d.nextbranchid = 0 ");
		}
		return cond.toString();
	}

	private String getTimeWhereCond(boolean today) {
		StringBuilder whereCond = new StringBuilder();
		whereCond.append("f.credate ");
		if (today) {
			whereCond.append(" >= ");
		} else {
			whereCond.append(" < ");
		}
		whereCond.append("'" + this.getTodayZeroTimeString() + "' ");

		return whereCond.toString();
	}

	private String getSelectOrderPart() {
		return "select " + this.getQueryFields() + " from express_ops_cwb_detail d inner join express_ops_order_flow f on d.cwb = f.cwb where ";
	}

	private String getSelectCountPart() {
		return "select count(1) from express_ops_cwb_detail d inner join express_ops_order_flow f on d.cwb = f.cwb where ";
	}

	private String getQueryFields() {
		return "d.nextbranchid,d.consigneename,d.consigneephone,d.shouldfare,d.consigneeaddress";
	}

	private String getTodayZeroTimeString() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);

		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());
	}
}
