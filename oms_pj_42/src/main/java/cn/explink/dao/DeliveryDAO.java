package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.controller.DeliveryDTO;
import cn.explink.controller.EmaildateTDO;
import cn.explink.dto.DeliveryNewDTO;

@Component
public class DeliveryDAO {

	private final class DeliveryMapper implements RowMapper<DeliveryDTO> {
		@Override
		public DeliveryDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			DeliveryDTO delivery = new DeliveryDTO();
			delivery.setCustomerid(rs.getLong("customerid"));
			delivery.setBranchid(rs.getLong("branchid"));
			delivery.setDeliverycount(rs.getLong("deliverycount"));
			delivery.setMonth(rs.getLong("month"));
			return delivery;
		}
	}

	private final class DeliveryNewMapper implements RowMapper<DeliveryNewDTO> {
		@Override
		public DeliveryNewDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			DeliveryNewDTO delivery = new DeliveryNewDTO();
			// delivery.setCustomerid(rs.getLong("customerid"));
			// delivery.setBranchid(rs.getLong("branchid"));
			// delivery.setCustomername(rs.getString("customername"));
			delivery.setRuKucount(rs.getLong("ruKucount"));
			delivery.setTuoToucount(rs.getLong("tuoToucount"));
			delivery.setYoujiekucount(rs.getLong("youjiekucount"));
			return delivery;
		}
	}

	private final class EmaildateMapper implements RowMapper<EmaildateTDO> {
		@Override
		public EmaildateTDO mapRow(ResultSet rs, int rowNum) throws SQLException {
			EmaildateTDO emaildate = new EmaildateTDO();
			emaildate.setEmaildate(rs.getString("emaildate"));
			emaildate.setEmaildateid(rs.getLong("emaildateid"));
			emaildate.setCustomername(rs.getString("customername"));

			return emaildate;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// 确认需求后的
	private String getDeliverWhereSql(String sql, String startdate, String enddate, String emaildateid, long timeLong, long dateType, int isTuotou) {
		if (startdate.length() > 0 || enddate.length() > 0 || emaildateid.length() > 0 || timeLong > 0 || dateType > 0 || isTuotou > 0) {
			StringBuffer w = new StringBuffer();

			if (dateType == 1) {// 按发货时间查询
				if (startdate.length() > 0) {
					w.append(" AND emaildate>='" + startdate + " 00:00:00' ");
				}
				if (enddate.length() > 0) {
					w.append(" AND emaildate<='" + enddate + " 23:59:59' ");
				}
				if (timeLong > 0 && isTuotou > 0) {
					if (isTuotou == 1) {// 妥投
						w.append(" AND tuotouTime <=" + timeLong);
					} else {
						w.append(" AND youjieguoTime <=" + timeLong);
					}
				}
			}
			if (dateType == 2) {// 按入库时间查询
				if (startdate.length() > 0) {
					w.append(" AND instoreroomtime>='" + startdate + " 00:00:00' ");
				}
				if (enddate.length() > 0) {
					w.append(" AND instoreroomtime<='" + enddate + " 23:59:59' ");
				}
				if (timeLong > 0 && isTuotou > 0) {
					if (isTuotou == 1) {// 妥投
						w.append(" AND rukutuotouTime <=" + timeLong);
					} else {
						w.append(" AND rukuyoujieguoTime <=" + timeLong);
					}
				}
			}
			if (dateType == 3) {// 按到站时间查询
				if (startdate.length() > 0) {
					w.append(" AND inSitetime>='" + startdate + " 00:00:00' ");
				}
				if (enddate.length() > 0) {
					w.append(" AND inSitetime<='" + enddate + " 23:59:59' ");
				}
				if (timeLong > 0 && isTuotou > 0) {
					if (isTuotou == 1) {// 妥投
						w.append(" AND daozhantuotouTime <=" + timeLong);
					} else {
						w.append(" AND daozhanyoujieguoTime <=" + timeLong);
					}
				}
			}

			if (emaildateid.length() > 0) {
				w.append(" AND emaildateid IN(" + emaildateid + ")");
			}
			sql += w;
		}
		return sql;
	}

	private String getDeliverAndBrangchidWhereSql(String sql, String startdate, String enddate, String emaildate, long branchid, long customerid, long timeLong, long dateType, long isTuotou) {
		if (startdate.length() > 0 || enddate.length() > 0 || emaildate.length() > 0 || customerid > 0) {
			StringBuffer w = new StringBuffer();
			if (dateType == 1) {// 按发货时间查询
				if (startdate.length() > 0) {
					w.append(" AND emaildate>='" + startdate + " 00:00:00' ");
				}
				if (enddate.length() > 0) {
					w.append(" AND emaildate<='" + enddate + " 23:59:59' ");
				}
				if (timeLong > 0 && isTuotou > 0) {
					if (isTuotou == 1) {// 妥投
						w.append(" AND tuotouTime <=" + timeLong);
					} else {
						w.append(" AND youjieguoTime <=" + timeLong);
					}
				}
			}

			if (dateType == 2) {// 按到站时间查询
				if (startdate.length() > 0) {
					w.append(" AND inSitetime>='" + startdate + " 00:00:00' ");
				}
				if (enddate.length() > 0) {
					w.append(" AND inSitetime<='" + enddate + " 23:59:59' ");
				}
				if (timeLong > 0 && isTuotou > 0) {
					if (isTuotou == 1) {// 妥投
						w.append(" AND daozhantuotouTime <=" + timeLong);
					} else {
						w.append(" AND daozhanyoujieguoTime <=" + timeLong);
					}
				}
			}

			if (branchid > -1) {
				w.append(" AND branchid =" + branchid);
			}
			if (customerid > -1) {
				w.append(" AND customerid =" + customerid);
			}

			sql += w;
		}
		return sql;
	}

	/**
	 * PeiSongChengGong(18,"配送成功","Success"),
	 * ShangMenTuiChengGong(19,"上门退成功","BackToDoorSuccess"),
	 * ShangMenHuanChengGong(20,"上门换成功","ChangeToDoorSuccess"),
	 * QuanBuTuiHuo(21,"全部退货","ReturnGoods"),
	 * BuFenTuiHuo(22,"部分退货","SomeReturnGoods"),
	 * FenZhanZhiLiu(23,"分站滞留","StayGoods"),
	 * ShangMenJuTui(24,"上门拒退","BackToDoorFail"),
	 * HuoWuDiuShi(25,"货物丢失","GoodsLose"), 获取公司所有的投递率
	 * 
	 * @param customerid
	 * @param startdate
	 * @param enddate
	 * @param emaildateid
	 * @return
	 */
	public List<DeliveryNewDTO> getAllBydayNew(long customerid, String startdate, String enddate, String emaildateid, long timeLong, long dateType, int isTuotou) {
		String sql = "SELECT " + "SUM(CASE WHEN flowordertype>1 THEN 1 ELSE 0 END) AS ruKucount," + "SUM(CASE WHEN flowordertype in(18,19,20,22) THEN 1 ELSE 0 END) AS tuoToucount,"
				+ "SUM(CASE WHEN flowordertype in(18,19,20,21,22,23,24,25) THEN 1 ELSE 0 END) AS youjiekucount " + " FROM express_ops_cwb_detail " + "WHERE state=1 "
				+ (customerid > 0 ? " and customerid=" + customerid : "");
		sql = this.getDeliverWhereSql(sql, startdate, enddate, emaildateid, timeLong, dateType, isTuotou);
		return jdbcTemplate.query(sql, new DeliveryNewMapper());
	}

	/**
	 * FenZhanDaoHuoSaoMiao(7,"分站到货扫描","SubstationGoods"),用来查询到站的
	 * 
	 * PeiSongChengGong(18,"配送成功","Success"),
	 * ShangMenTuiChengGong(19,"上门退成功","BackToDoorSuccess"),
	 * ShangMenHuanChengGong(20,"上门换成功","ChangeToDoorSuccess"),
	 * QuanBuTuiHuo(21,"全部退货","ReturnGoods"),
	 * BuFenTuiHuo(22,"部分退货","SomeReturnGoods"),
	 * FenZhanZhiLiu(23,"分站滞留","StayGoods"),
	 * ShangMenJuTui(24,"上门拒退","BackToDoorFail"),
	 * HuoWuDiuShi(25,"货物丢失","GoodsLose"), 获取各站投递率
	 * 
	 * @param customerid
	 * @param startdate
	 * @param enddate
	 * @param emaildate
	 * @param branchid
	 * @param flowordertype
	 * @return
	 */
	public List<DeliveryNewDTO> getAllBydayAndBrandid(long customerid, String startdate, String enddate, String emaildate, long branchid, long timeLong, long dateType, long isTuotou) {
		String sql = "SELECT " + "SUM(CASE WHEN flowordertype>1 THEN 1 ELSE 0 END) AS ruKucount," + "SUM(CASE WHEN flowordertype in(18,19,20,22) THEN 1 ELSE 0 END) AS tuoToucount,"
				+ "SUM(CASE WHEN flowordertype in(18,19,20,21,22,23,24,25) THEN 1 ELSE 0 END) AS youjiekucount " + " FROM express_ops_cwb_detail " + "WHERE state=1 ";
		sql = this.getDeliverAndBrangchidWhereSql(sql, startdate, enddate, emaildate, branchid, customerid, timeLong, dateType, isTuotou);
		return jdbcTemplate.query(sql, new DeliveryNewMapper());
	}

	/**
	 * 查询所有有操作过订单的供货商，没有任何数据的供货商不在此列表中
	 * 
	 * @return
	 */
	public List<DeliveryDTO> getDeliveryCutomer() {
		String sql = "SELECT customerid,branchid,COUNT(1) AS deliverycount,MONTH(credate) AS 'month' " + "FROM   express_ops_order_flow   GROUP   BY  customerid";
		return jdbcTemplate.query(sql, new DeliveryMapper());
	}

	/**
	 * 查询所选时间内的所有批次号
	 * 
	 * @param startdate
	 * @param enddate
	 * @return
	 */
	public List<EmaildateTDO> getEmailDate(String startdate, String enddate) {
		String sql = "SELECT emaildate,emaildateid,customername FROM express_ops_order_flow WHERE emaildate >='" + startdate + " 00:00:00' AND emaildate <= '" + enddate
				+ " 23:59:59'  AND emaildate is not null AND state=1 GROUP BY emaildateid";
		return jdbcTemplate.query(sql, new EmaildateMapper());
	}

	/**
	 * 按站点查询所选时间内的所有批次号
	 * 
	 * @param startdate
	 * @param enddate
	 * @param branchid
	 * @return
	 */
	public List<EmaildateTDO> getEmailDateAndBrandId(String startdate, String enddate, long branchid) {
		String sql = "SELECT emaildate,emaildateid,customername FROM express_ops_order_flow WHERE credate >='" + startdate + "' AND credate <= '" + enddate
				+ "'  AND emaildate is not null AND emaildateid>0 AND state=1 GROUP BY emaildateid ORDER BY emaildate DESC";
		if (branchid > 0) {
			sql = "SELECT emaildate,emaildateid,customername FROM express_ops_order_flow WHERE credate >='" + startdate + "' AND credate <= '" + enddate
					+ "'  AND emaildate is not null AND emaildateid>0 AND state=1   AND branchid=" + branchid + " GROUP BY emaildateid ORDER BY emaildate DESC";
		}
		return jdbcTemplate.query(sql, new EmaildateMapper());
	}
}
