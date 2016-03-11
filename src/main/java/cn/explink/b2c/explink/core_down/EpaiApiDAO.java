package cn.explink.b2c.explink.core_down;

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

@Component
public class EpaiApiDAO {

	@Autowired
	JdbcTemplate jdbcTemplate;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private final class EpaiApier implements RowMapper<EpaiApi> {
		@Override
		public EpaiApi mapRow(ResultSet rs, int rowNum) throws SQLException {
			EpaiApi code = new EpaiApi();
			code.setB2cid(rs.getInt("b2cid"));
			code.setCustomerid(rs.getLong("customerid"));
			code.setFeedback_url(rs.getString("feedback_url"));
			code.setGetOrder_url(rs.getString("getorder_url"));
			code.setPageSize(rs.getInt("pageSize"));
			code.setPrivate_key(rs.getString("private_key"));
			code.setUserCode(rs.getString("usercode"));
			code.setWarehouseid(rs.getLong("warehouseid"));
			code.setState(rs.getInt("state"));
			code.setCallBack_url(rs.getString("callBack_url"));
			code.setIsopenflag(rs.getInt("isopenflag"));
			code.setIsfeedbackflag(rs.getInt("isfeedbackflag"));
			code.setIspostflag(rs.getInt("ispostflag"));
			code.setIsPassiveReception(rs.getInt("isPassiveReception"));
			code.setOpen_ordertrackflag(rs.getInt("open_ordertrackflag"));
			code.setOrdertrack_url(rs.getString("ordertrack_url"));
			return code;
		}
	}

	public List<EpaiApi> getEpaiApiList() // 是否b2c 标示 1:是，0:不是(pos)。
	{
		List<EpaiApi> list = null;
		try {
			String sql = "select * from b2c_set_epaiapi ";

			list = jdbcTemplate.query(sql, new EpaiApier());
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		return list;

	}

	public boolean IsExistsPosCodeFlag(String usercode, long customerid) {
		int counts = 0;
		try {
			String sql = "select count(1)  from b2c_set_epaiapi where  customerid=" + customerid + " and usercode='" + usercode + "'";

			counts = jdbcTemplate.queryForInt(sql);
		} catch (Exception e) {
		}

		return counts > 0;

	}

	public boolean IsExistsPosCodeFlag(String usercode, String customerid, long b2cid) {
		int counts = 0;
		try {
			String sql = "select count(1)  from b2c_set_epaiapi where  customerid=" + customerid + " and usercode='" + usercode + "' and b2cid<>" + b2cid;

			counts = jdbcTemplate.queryForInt(sql);
		} catch (Exception e) {
		}

		return counts > 0;

	}

	public void createEpaiApi(EpaiApi pc) {
		try {
			String sql = "insert into b2c_set_epaiapi(usercode,customerid,getorder_url,callback_url,feedback_url,private_key,warehouseid,pagesize,isopenflag,ispostflag,isPassiveReception,open_ordertrackflag,ordertrack_url)  values(?,?,?,?,?,?,?,?,?,?,?,?,?) ";
			jdbcTemplate.update(sql, pc.getUserCode(), pc.getCustomerid(), pc.getGetOrder_url(), pc.getCallBack_url(), pc.getFeedback_url(), pc.getPrivate_key(), pc.getWarehouseid(),
					pc.getPageSize(), pc.getIsopenflag(), pc.getIspostflag(),pc.getIsPassiveReception(),pc.getOpen_ordertrackflag(),pc.getOrdertrack_url());
		} catch (Exception e) {
			logger.info(e.getMessage());
		}

	}

	public void exptReasonDel(long b2cid) {
		try {
			String sql = "delete from b2c_set_epaiapi where b2cid=" + b2cid;
			jdbcTemplate.update(sql);
		} catch (Exception e) {
			logger.info(e.getMessage());
		}

	}

	public EpaiApi getEpaiApi(long b2cid) {
		EpaiApi expt = null;
		try {
			String sql = "select * from b2c_set_epaiapi where b2cid=" + b2cid;
			expt = jdbcTemplate.queryForObject(sql, new EpaiApier());
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		return expt;

	}
	
	public EpaiApi getEpaiApiByUserCode(String userCode){
		EpaiApi expt = null;
		try {
			String sql = "select * from b2c_set_epaiapi where usercode='"+userCode+"'";
			expt = jdbcTemplate.queryForObject(sql, new EpaiApier());
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		return expt;
	}
	
	

	public void update(EpaiApi pc, long b2cid) {
		try {
			String sql = "update b2c_set_epaiapi set usercode=?,customerid=?,getorder_url=?,callback_url=?,feedback_url=?,private_key=?,warehouseid=?,pagesize=?,isopenflag=?,isfeedbackflag=?,ispostflag=?,isPassiveReception=?,open_ordertrackflag=?,ordertrack_url=?  where b2cid=? ";
			jdbcTemplate.update(sql, pc.getUserCode(), pc.getCustomerid(), pc.getGetOrder_url(), pc.getCallBack_url(), pc.getFeedback_url(), pc.getPrivate_key(), pc.getWarehouseid(),
					pc.getPageSize(), pc.getIsopenflag(), pc.getIsfeedbackflag(), pc.getIspostflag(),pc.getIsPassiveReception(),
					pc.getOpen_ordertrackflag(),pc.getOrdertrack_url(),b2cid);
		} catch (DataAccessException e) {
			logger.info(e.getMessage());
		}

	}
	
	
	public EpaiApi getEpaiApiByKey(long customerid) {
		EpaiApi expt = null;
		try {
			String sql = "select * from b2c_set_epaiapi where customerid=? limit 1";
			expt = jdbcTemplate.queryForObject(sql, new EpaiApier(),customerid);
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		return expt;

	}

}
