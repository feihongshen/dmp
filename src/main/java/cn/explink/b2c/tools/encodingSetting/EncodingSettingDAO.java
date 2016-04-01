package cn.explink.b2c.tools.encodingSetting;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.util.Page;

@Component
public class EncodingSettingDAO {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	private static Logger logger =LoggerFactory.getLogger(EncodingSettingDAO.class);

	private final class EncodingSet implements RowMapper<EncodingSetting> {
		@Override
		public EncodingSetting mapRow(ResultSet rs, int rowNum) throws SQLException {
			EncodingSetting code = new EncodingSetting();
			code.setPoscodeid(rs.getInt("poscodeid"));
			code.setCustomerid(rs.getInt("customerid"));
			code.setCustomercode(rs.getString("customercode"));
			code.setRemark(rs.getString("remark"));
			return code;
		}
	}

	public List<EncodingSetting> getEncodingSettingList(long customerid, long page) // 是否b2c
																			// 标示
																			// 1:是，0:不是(pos)。
	{
		List<EncodingSetting> list = null;
		try {
			String sql = "select * from encodingsetting_customer_mapping where 1=1 ";
			if (customerid != -1) {
				sql += " and customerid=" + customerid;
			}

			sql += " order by customerid  limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
			list = jdbcTemplate.query(sql, new EncodingSet());
		} catch (Exception e) {
			logger.error("", e);
		}
		return list;

	}

	public int getEncodingSettingCount(long customerid) {
		int counts = 0;
		try {
			String sql = "select count(1) from encodingsetting_customer_mapping where 1=1 ";
			if (customerid != -1) {
				sql += " and customerid=" + customerid;
			}

			counts = jdbcTemplate.queryForInt(sql);
		} catch (Exception e) {
		}
		return counts;

	}

	public boolean IsExistsPosCodeFlag( long customerid) {
		boolean flag = false;
		int counts = 0;
		try {
			String sql = "select count(1)  from encodingsetting_customer_mapping ";
			sql += "where  customerid=" + customerid ;

			counts = jdbcTemplate.queryForInt(sql);
		} catch (Exception e) {
		}
		if (counts > 0) {
			flag = true;
		}
		return flag;

	}

	public void createExptReason(EncodingSetting pc) {
		try {
			String sql = "insert into encodingsetting_customer_mapping(customerid,customercode,remark)" + " values(?,?,?) ";
			jdbcTemplate.update(sql,  pc.getCustomerid(), pc.getCustomercode(), pc.getRemark());
		} catch (Exception e) {
		}

	}
	
	

	public void exptReasonDel(long poscodeid) {
		try {
			String sql = "delete from encodingsetting_customer_mapping where poscodeid=" + poscodeid;
			jdbcTemplate.update(sql);
		} catch (Exception e) {
			logger.error("", e);
		}

	}

	public EncodingSetting getExptReasonEntityByKey(long poscodeid) {
		EncodingSetting expt = null;
		try {
			String sql = "select * from encodingsetting_customer_mapping where poscodeid=" + poscodeid;
			expt = jdbcTemplate.queryForObject(sql, new EncodingSet());
		} catch (Exception e) {
			logger.error("", e);
		}
		return expt;

	}

	public EncodingSetting getPosCodeByKey(long customerid) {
		EncodingSetting expt = null;
		try {
			String sql = "select * from encodingsetting_customer_mapping where customerid=" + customerid  + " limit 1 ";
			expt = jdbcTemplate.queryForObject(sql, new EncodingSet());
		} catch (Exception e) {

		}
		return expt;

	}

	public void SaveExptReason(EncodingSetting pc, long poscodeid) {
		try {
			String sql = "update encodingsetting_customer_mapping set remark='" + pc.getRemark() + "',customercode='" + pc.getCustomercode() + "' " + " where poscodeid=" + poscodeid;
			jdbcTemplate.update(sql);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}

	}

	public boolean IsExistsPosCodeExceptOwnFlag( long customerid, long poscodeid) {
		boolean flag = false;
		int counts = 0;
		try {
			String sql = "select count(1)  from encodingsetting_customer_mapping where  customerid='" + customerid +  " and poscodeid<>" + poscodeid;

			counts = jdbcTemplate.queryForInt(sql);
		} catch (Exception e) {
		}
		if (counts > 0) {
			flag = true;
		}
		return flag;

	}

}
