package cn.explink.b2c.tools.poscodeMapp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.util.Page;

@Component
public class PoscodeMappDAO {

	@Autowired
	JdbcTemplate jdbcTemplate;

	private final class PoscodeMapper implements RowMapper<PoscodeMapp> {
		@Override
		public PoscodeMapp mapRow(ResultSet rs, int rowNum) throws SQLException {
			PoscodeMapp code = new PoscodeMapp();
			code.setPoscodeid(rs.getInt("poscodeid"));
			code.setPosenum(rs.getInt("posenum"));
			code.setCustomercode(rs.getString("customercode"));
			code.setCustomerid(rs.getInt("customerid"));
			code.setRemark(rs.getString("remark"));
			return code;
		}
	}

	public List<PoscodeMapp> getPoscodeMappList(long posenum, long page) // 是否b2c
																			// 标示
																			// 1:是，0:不是(pos)。
	{
		List<PoscodeMapp> list = null;
		try {
			String sql = "select * from poscode_customer_mapping where 1=1 ";
			if (posenum != -1) {
				sql += " and posenum=" + posenum;
			}

			sql += " order by posenum  limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
			list = jdbcTemplate.query(sql, new PoscodeMapper());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;

	}

	public int getPoscodeMappCount(long posenum) {
		int counts = 0;
		try {
			String sql = "select count(1) from poscode_customer_mapping where 1=1 ";
			if (posenum != -1) {
				sql += " and posenum=" + posenum;
			}

			counts = jdbcTemplate.queryForInt(sql);
		} catch (Exception e) {
		}
		return counts;

	}

	public boolean IsExistsPosCodeFlag(long posenum, long customerid) {
		boolean flag = false;
		int counts = 0;
		try {
			String sql = "select count(1)  from poscode_customer_mapping ";
			sql += "where  customerid=" + customerid + " and posenum=" + posenum;

			counts = jdbcTemplate.queryForInt(sql);
		} catch (Exception e) {
		}
		if (counts > 0) {
			flag = true;
		}
		return flag;

	}

	public void createExptReason(PoscodeMapp pc) {
		try {
			String sql = "insert into poscode_customer_mapping(posenum,customerid,customercode,remark)" + " values(?,?,?,?) ";
			jdbcTemplate.update(sql, pc.getPosenum(), pc.getCustomerid(), pc.getCustomercode(), pc.getRemark());
		} catch (Exception e) {
		}

	}

	public void exptReasonDel(long poscodeid) {
		try {
			String sql = "delete from poscode_customer_mapping where poscodeid=" + poscodeid;
			jdbcTemplate.update(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public PoscodeMapp getExptReasonEntityByKey(long poscodeid) {
		PoscodeMapp expt = null;
		try {
			String sql = "select * from poscode_customer_mapping where poscodeid=" + poscodeid;
			expt = jdbcTemplate.queryForObject(sql, new PoscodeMapper());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return expt;

	}

	public PoscodeMapp getPosCodeByKey(long customerid, long posenum) {
		PoscodeMapp expt = null;
		try {
			String sql = "select * from poscode_customer_mapping where customerid=" + customerid + " and posenum=" + posenum + " limit 1 ";
			expt = jdbcTemplate.queryForObject(sql, new PoscodeMapper());
		} catch (Exception e) {

		}
		return expt;

	}

	public void SaveExptReason(PoscodeMapp pc, long poscodeid) {
		try {
			String sql = "update poscode_customer_mapping set remark='" + pc.getRemark() + "',customercode='" + pc.getCustomercode() + "' " + " where poscodeid=" + poscodeid;
			jdbcTemplate.update(sql);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}

	}

	public boolean IsExistsPosCodeExceptOwnFlag(long posenum, long customerid, long poscodeid) {
		boolean flag = false;
		int counts = 0;
		try {
			String sql = "select count(1)  from poscode_customer_mapping where  customerid='" + customerid + "' and posenum=" + posenum + " and poscodeid<>" + poscodeid;

			counts = jdbcTemplate.queryForInt(sql);
		} catch (Exception e) {
		}
		if (counts > 0) {
			flag = true;
		}
		return flag;

	}

}
