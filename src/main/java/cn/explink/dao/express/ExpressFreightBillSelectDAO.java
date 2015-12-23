package cn.explink.dao.express;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import cn.explink.domain.VO.express.SelectReturnVO;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.express.ExpressSettleWayEnum;
import cn.explink.util.StringUtil;

@Repository
public class ExpressFreightBillSelectDAO {
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	private final class SelectVOMapper implements RowMapper<SelectReturnVO> {
		private String hiddenFlag;
		private String displayFlag;
		public SelectVOMapper(String hiddenFlag, String displayFlag) {
			super();
			this.hiddenFlag = hiddenFlag;
			this.displayFlag = displayFlag;
		}

		@Override
		public SelectReturnVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			SelectReturnVO selectVo = new SelectReturnVO();
			selectVo.setHiddenValue(rs.getLong(hiddenFlag));
			selectVo.setDisplayValue(StringUtil.nullConvertToEmptyString(rs.getString(displayFlag)));
			return selectVo;
		}
	}
	
	/**
	 * 查询客户
	 * @return
	 */
	public List<SelectReturnVO> getCustomerSelectInfo() {
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct(c.customerid) as customerId, u.customername ");
		sql.append("from express_ops_cwb_detail c left join express_set_customer_info u ");
		sql.append(" on c.customerid = u.customerid");
		sql.append(" where 1=1 ");
		sql.append(" and c.cwbordertypeid=" + CwbOrderTypeIdEnum.Express.getValue());
		sql.append(" and c.paymethod="+ExpressSettleWayEnum.MonthPay.getValue());//过滤掉非月结的订单
		sql.append(" and c.state=1 ");
		
		return this.jdbcTemplate.query(sql.toString(), new SelectVOMapper("customerId","customername"));
	}
	
	/**
	 * 查询所有的站点信息
	 * @return
	 */
	public List<SelectReturnVO> getBranchData() {
		StringBuffer sql = new StringBuffer();
		sql.append("select branchid,branchname from express_set_branch where sitetype=? and brancheffectflag='1' ORDER BY CONVERT( branchname USING gbk ) COLLATE gbk_chinese_ci ASC ");
		return jdbcTemplate.query(sql.toString(), new SelectVOMapper("branchid","branchname"),BranchEnum.ZhanDian.getValue());
	}
	/**
	 * 查询所有的省份
	 * @param currentBranchId 
	 * @param currentBranchId
	 * @return
	 */
	public List<SelectReturnVO> getProvinceData(Long currentProvinceId) {
		StringBuffer sql = new StringBuffer();
		sql.append("select id, name from express_set_province where id<>"+currentProvinceId);
		return this.jdbcTemplate.query(sql.toString(), new SelectVOMapper("id","name"));
	}
	
	/**
	 * 查询当前登录人的省份
	 * @param user 
	 * @return
	 */
	public SelectReturnVO getCurrentUserInfo(Long currentProvinceId) {
		StringBuffer sql = new StringBuffer();
		sql.append("select id, name from express_set_province where id="+currentProvinceId);
		return this.jdbcTemplate.queryForObject(sql.toString(), new SelectVOMapper("id","name"));
	}
}
