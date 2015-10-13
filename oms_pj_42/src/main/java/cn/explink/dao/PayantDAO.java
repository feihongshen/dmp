package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.controller.MonitorDTO;
import cn.explink.controller.PaymetDTO;
import cn.explink.dto.CarAmountStatiticsDTO;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;

@Component
public class PayantDAO {

	private final class MonMapper implements RowMapper<MonitorDTO> {
		@Override
		public MonitorDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			MonitorDTO monDto = new MonitorDTO();
			monDto.setCountsum(rs.getLong("countsum"));
			monDto.setCaramountsum(rs.getBigDecimal("caramountsum"));
			return monDto;
		}
	}

	private final class MonMapperDate implements RowMapper<PaymetDTO> {
		@Override
		public PaymetDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			PaymetDTO monDto = new PaymetDTO();
			monDto.setCwb(rs.getString("cwb"));
			return monDto;
		}
	}

	private String getMonitorWhereSql(String sql, String emailStartTime, String eamilEndTime, long customerid, long exportType) {

		if (emailStartTime.length() > 0 || eamilEndTime.length() > 0 || customerid > 0 || exportType > -1) {
			StringBuffer w = new StringBuffer();
			if (emailStartTime.length() > 0) {
				w.append(" AND emaildate >='" + emailStartTime + "' ");
			}
			if (eamilEndTime.length() > 0) {
				w.append(" AND emaildate <='" + eamilEndTime + "' ");
			}
			if (customerid > 0) {
				w.append(" AND customerid =" + customerid);
			}
			if (exportType > -1) {
				w.append(" AND exportType =" + exportType);
			}
			sql += w;
		}

		return sql;
	}

	private String getMonitorWhereSqlBack(String sql, String emailStartTime, String eamilEndTime, long customerid, long exportType) {

		if (emailStartTime.length() > 0 || eamilEndTime.length() > 0 || customerid > 0 || exportType > -1) {
			StringBuffer w = new StringBuffer();
			if (emailStartTime.length() > 0) {
				w.append(" AND emaildate >='" + emailStartTime + "' ");
			}
			if (eamilEndTime.length() > 0) {
				w.append(" AND emaildate <='" + eamilEndTime + "' ");
			}
			if (customerid > 0) {
				w.append(" AND customerid =" + customerid);
			}
			if (exportType > -1) {
				w.append(" AND exportBackType =" + exportType);
			}
			sql += w;
		}

		return sql;
	}

	private String getMonitorWhereSqlDate(String sql, String emailStartTime, String eamilEndTime, long customerid) {

		if (emailStartTime.length() > 0 || eamilEndTime.length() > 0 || customerid > 0) {
			StringBuffer w = new StringBuffer();
			if (emailStartTime.length() > 0) {
				w.append(" AND emaildate >='" + emailStartTime + "' ");
			}
			if (eamilEndTime.length() > 0) {
				w.append(" AND emaildate <='" + eamilEndTime + "' ");
			}
			if (customerid > 0) {
				w.append(" AND customerid =" + customerid);
			}

			sql += w;
		}

		return sql;
	}

	private String getisnow(String sql, String flowordertype, long isnow) {

		if (flowordertype.length() > 0 || isnow == 1) {
			StringBuffer w = new StringBuffer();

			if (flowordertype.length() > 0) {
				w.append(" AND p.flowordertype in(" + flowordertype + ")");
			}
			if (isnow == 1) {
				w.append(" AND p.isnow =1");
			}
			sql += w;
		}
		return sql;
	}

	private String getisnowExp(String sql, String flowordertype, long isnow) {

		if (flowordertype.length() > 0 || isnow == 1) {
			StringBuffer w = new StringBuffer();

			if (flowordertype.length() > 0) {
				w.append(" AND (p.flowordertype =1  OR p.flowordertype not in(" + flowordertype + ")) ");
			}
			if (isnow == 1) {
				w.append(" AND p.isnow =1");
			}
			sql += w;
		}
		return sql;
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public MonitorDTO getMon(String emailStartTime, String eamilEndTime, String flowordertype, long customerid, long isnow, long exportType) {
		String sql = "SELECT SUM(p.caramount) AS caramountsum,COUNT(1) countsum FROM ( SELECT caramount,flowordertype ,isnow FROM express_ops_order_flow where 1=1  ";
		sql = this.getMonitorWhereSql(sql, emailStartTime, eamilEndTime, customerid, exportType) + " GROUP BY cwb) p WHERE 1=1 ";
		sql = getisnow(sql, flowordertype, isnow);
		return jdbcTemplate.queryForObject(sql, new MonMapper());
	}

	public MonitorDTO getMonBack(String emailStartTime, String eamilEndTime, String flowordertype, long customerid, long isnow, long exportType) {
		String sql = "SELECT SUM(p.caramount) AS caramountsum,COUNT(1) countsum FROM ( SELECT caramount,flowordertype ,isnow FROM express_ops_order_flow where 1=1  ";
		sql = this.getMonitorWhereSqlBack(sql, emailStartTime, eamilEndTime, customerid, exportType) + " GROUP BY cwb) p WHERE 1=1 ";
		sql = getisnow(sql, flowordertype, isnow);
		return jdbcTemplate.queryForObject(sql, new MonMapper());
	}

	// 获取剩下的
	public MonitorDTO getMonExp(String emailStartTime, String eamilEndTime, String flowordertype, long customerid, long isnow, long exportType) {
		String sql = "SELECT SUM(p.caramount) AS caramountsum,COUNT(1) countsum FROM ( SELECT caramount,flowordertype ,isnow FROM express_ops_order_flow where 1=1  ";
		sql = this.getMonitorWhereSql(sql, emailStartTime, eamilEndTime, customerid, exportType) + " GROUP BY cwb) p WHERE 1=1 ";
		sql = getisnowExp(sql, flowordertype, isnow);
		return jdbcTemplate.queryForObject(sql, new MonMapper());
	}

	public List<PaymetDTO> getMonDate(String emailStartTime, String eamilEndTime, String flowordertype, long customerid) {
		String sql = "SELECT cwb FROM express_ops_order_flow where 1=1  ";
		sql = this.getMonitorWhereSqlDate(sql, emailStartTime, eamilEndTime, customerid);
		return jdbcTemplate.query(sql, new MonMapperDate());
	}

	/**
	 * 货款统计 结算货款
	 * 
	 * @param emailStartTime
	 * @param eamilEndTime
	 * @param flowordertype
	 * @param customerid
	 * @param isnow
	 * @param exportType
	 * @return
	 */
	public List<CarAmountStatiticsDTO> CargoAmountStatistics(String emailStartTime, String eamilEndTime, long customerid, long exportType) {
		String sql = " SELECT customername,customerid, COUNT(1) AS totalcount,SUM(receivablefee) AS totalamount," + " SUM(CASE WHEN orderResultType=" + DeliveryStateEnum.PeiSongChengGong.getValue()
				+ " THEN 1 ELSE 0 END) AS successcount, " + " SUM(CASE WHEN orderResultType=" + DeliveryStateEnum.PeiSongChengGong.getValue() + " THEN receivablefee ELSE 0 END) AS successamount,"
				+ " SUM(CASE WHEN orderResultType IN (" + DeliveryStateEnum.QuanBuTuiHuo.getValue() + ") THEN 1 ELSE 0 END) AS backcount," + " SUM(CASE WHEN orderResultType IN ("
				+ DeliveryStateEnum.QuanBuTuiHuo.getValue() + ") THEN receivablefee ELSE 0 END) AS backamount, " + " SUM(CASE WHEN orderResultType NOT IN ("
				+ DeliveryStateEnum.PeiSongChengGong.getValue() + "," + DeliveryStateEnum.QuanBuTuiHuo.getValue() + ") THEN 1 ELSE 0 END) AS kucuncount," + " SUM(CASE WHEN orderResultType NOT IN ("
				+ DeliveryStateEnum.PeiSongChengGong.getValue() + "," + DeliveryStateEnum.QuanBuTuiHuo.getValue() + ") THEN receivablefee ELSE 0 END) AS kucunamount "
				+ " FROM express_ops_cwb_detail WHERE state=1 AND customername<>'' ";
		if (customerid != -1) {
			sql += " AND customerid=" + customerid;
		}
		if (exportType != -1) {
			sql += " AND exportType=" + exportType;
		}
		if (emailStartTime != null && !"".equals(emailStartTime) && eamilEndTime != null && !"".equals(eamilEndTime)) {
			sql += " and emaildate between '" + emailStartTime + "' and '" + eamilEndTime + "' ";
		}
		sql += " GROUP BY customername,customerid ";

		return jdbcTemplate.query(sql, new CarAmountMapper());
	}

	/**
	 * 货款统计 结算 退货款
	 * 
	 * @param emailStartTime
	 * @param eamilEndTime
	 * @param flowordertype
	 * @param customerid
	 * @param isnow
	 * @param exportType
	 * @return
	 */
	public List<CarAmountStatiticsDTO> CargoAmountStatisticsBack(String emailStartTime, String eamilEndTime, long customerid, long exportType, String startaudittime, String endaudittime) {
		String sql = " SELECT customername,customerid," + " SUM(CASE WHEN  1=1 " + ((emailStartTime != null && !"".equals(emailStartTime)) ? " and emaildate >= '" + emailStartTime + "'" : " ")
				+ ((eamilEndTime != null && !"".equals(eamilEndTime)) ? " and emaildate <= '" + eamilEndTime + "' " : " ") + "  THEN 1 ELSE 0 END) AS totalcount, " + " SUM(CASE WHEN  1=1 "
				+ ((emailStartTime != null && !"".equals(emailStartTime)) ? " and emaildate >= '" + emailStartTime + "'" : " ")
				+ ((eamilEndTime != null && !"".equals(eamilEndTime)) ? " and emaildate <= '" + eamilEndTime + "' " : " ") + "  THEN receivablefee ELSE 0 END) AS totalamount, "
				+ " SUM(CASE WHEN orderResultType=" + DeliveryStateEnum.PeiSongChengGong.getValue() + " THEN 1 ELSE 0 END) AS successcount," + " SUM(CASE WHEN orderResultType="
				+ DeliveryStateEnum.PeiSongChengGong.getValue() + " THEN receivablefee ELSE 0 END) AS successamount," + " SUM(CASE WHEN orderResultType IN ("
				+ DeliveryStateEnum.QuanBuTuiHuo.getValue() + "," + DeliveryStateEnum.ShangMenTuiChengGong.getValue() + "," + FlowOrderTypeEnum.TuiHuoZhanYouHuoWuDanRuKu.getValue() + ","
				+ FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue() + "," + FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue() + ") "
				+ ((startaudittime != null && !"".equals(startaudittime)) ? " and audittime >= '" + startaudittime + "'" : " ")
				+ ((endaudittime != null && !"".equals(endaudittime)) ? " and audittime <= '" + endaudittime + "' " : " ") + "  THEN 1 ELSE 0 END) AS backcount, "
				+ " SUM(CASE WHEN orderResultType IN (" + DeliveryStateEnum.QuanBuTuiHuo.getValue() + "," + DeliveryStateEnum.ShangMenTuiChengGong.getValue() + ") AND flowordertype in ("
				+ FlowOrderTypeEnum.TuiHuoZhanYouHuoWuDanRuKu.getValue() + "," + FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue() + "," + FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue() + ") "
				+ ((startaudittime != null && !"".equals(startaudittime)) ? " and audittime >= '" + startaudittime + "'" : " ")
				+ ((endaudittime != null && !"".equals(endaudittime)) ? " and audittime <= '" + endaudittime + "' " : "  ") + "  THEN receivablefee ELSE 0 END) AS backamount, "

				+ " SUM(CASE WHEN orderResultType NOT IN (" + DeliveryStateEnum.PeiSongChengGong.getValue() + "," + DeliveryStateEnum.QuanBuTuiHuo.getValue() + ") THEN 1 ELSE 0 END) AS kucuncount,"
				+ " SUM(CASE WHEN orderResultType NOT IN (" + DeliveryStateEnum.PeiSongChengGong.getValue() + "," + DeliveryStateEnum.QuanBuTuiHuo.getValue()
				+ ") THEN receivablefee ELSE 0 END) AS kucunamount " + " FROM express_ops_cwb_detail WHERE state=1 AND customername<>'' ";
		if (customerid != -1) {
			sql += " AND customerid=" + customerid;
		}
		if (exportType != -1) {
			sql += " AND exportbackType=" + exportType;
		}
		if (emailStartTime != null && !"".equals(emailStartTime)) {
			sql += " and emaildate >= '" + emailStartTime + "'";
		}
		if (eamilEndTime != null && !"".equals(eamilEndTime)) {
			sql += " and emaildate <= '" + eamilEndTime + "' ";
		}
		sql += " GROUP BY customername,customerid ";

		return jdbcTemplate.query(sql, new CarAmountMapper());
	}

	/**
	 * 修改导出状态
	 * 
	 * @param emailStartTime
	 * @param eamilEndTime
	 * @param customerid
	 * @param exportType
	 * @return
	 */
	public int CargoAmountStatistics_update(String emailStartTime, String eamilEndTime, long customerid, long exportType) {
		String sql = "update express_ops_cwb_detail set exportType=" + exportType + " where  1=1 AND customername<>'' ";
		if (customerid != -1) {
			sql += " AND customerid=" + customerid;
		}
		if (emailStartTime != null && !"".equals(emailStartTime) && eamilEndTime != null && !"".equals(eamilEndTime)) {
			sql += " and emaildate between '" + emailStartTime + "' and '" + eamilEndTime + "' ";
		}
		try {
			return jdbcTemplate.update(sql);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 修改导出状态 退货
	 * 
	 * @param emailStartTime
	 * @param eamilEndTime
	 * @param customerid
	 * @param exportType
	 * @return
	 */
	public int CargoAmountStatisticsBack_update(String emailStartTime, String eamilEndTime, long customerid, long exportType) {
		String sql = "update express_ops_cwb_detail set exportbackType=" + exportType + " where  1=1 AND customername<>'' ";
		if (customerid != -1) {
			sql += " AND customerid=" + customerid;
		}
		if (emailStartTime != null && !"".equals(emailStartTime) && eamilEndTime != null && !"".equals(eamilEndTime)) {
			sql += " and emaildate between '" + emailStartTime + "' and '" + eamilEndTime + "' ";
		}
		try {
			return jdbcTemplate.update(sql);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}

	private final class CarAmountMapper implements RowMapper<CarAmountStatiticsDTO> {
		@Override
		public CarAmountStatiticsDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			CarAmountStatiticsDTO monDto = new CarAmountStatiticsDTO();
			monDto.setTotalcount(rs.getLong("totalcount"));
			monDto.setTotalamount(rs.getDouble("totalamount"));
			monDto.setSuccesscount(rs.getLong("successcount"));
			monDto.setSuccessamount(rs.getDouble("successamount"));
			monDto.setBackcount(rs.getLong("backcount"));
			monDto.setBackamount(rs.getDouble("backamount"));
			monDto.setCustomername(rs.getString("customername"));
			monDto.setKucuncount(rs.getLong("kucuncount"));
			monDto.setKucunamount(rs.getDouble("kucunamount"));
			monDto.setCustomerid(rs.getLong("customerid"));
			return monDto;
		}
	}

}
