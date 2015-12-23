package cn.explink.b2c.pjwl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import cn.explink.util.StringUtil;

@Service
public class PreOrderDataImportDAO_Express {

	private Logger logger = LoggerFactory.getLogger(PreOrderDataImportDAO_Express.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	
	private final class ExpressPreOrderDTOMapper implements RowMapper<ExpressPreOrderDTO> {
		@Override
		public ExpressPreOrderDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			ExpressPreOrderDTO tempPreOrder = new ExpressPreOrderDTO();
			tempPreOrder.setId(rs.getLong("id"));
			tempPreOrder.setReserveOrderNo(rs.getString("reverse_order_no"));
			tempPreOrder.setOrderType(rs.getInt("order_type"));
			tempPreOrder.setCustCode(StringUtil.nullConvertToEmptyString(rs.getString("cust_code")));
			tempPreOrder.setCnorProv(StringUtil.nullConvertToEmptyString(rs.getString("cnor_prov")));
			tempPreOrder.setCnorCity(StringUtil.nullConvertToEmptyString(rs.getString("cnor_city")));
			tempPreOrder.setCnorRegion(StringUtil.nullConvertToEmptyString(rs.getString("cnor_region")));
			tempPreOrder.setCnorTown(StringUtil.nullConvertToEmptyString(rs.getString("cnor_town")));
			tempPreOrder.setCnorAddr(StringUtil.nullConvertToEmptyString(rs.getString("cnor_addr")));
			tempPreOrder.setCnorName(StringUtil.nullConvertToEmptyString(rs.getString("cnor_name")));
			tempPreOrder.setCnorMobile(StringUtil.nullConvertToEmptyString(rs.getString("cnor_mobile")));
			tempPreOrder.setCnorTel(StringUtil.nullConvertToEmptyString(rs.getString("cnor_tel")));
			tempPreOrder.setCnorRemark(StringUtil.nullConvertToEmptyString(rs.getString("cnor_remark")));
			tempPreOrder.setCarrierCode(StringUtil.nullConvertToEmptyString(rs.getString("carrier_code")));
			tempPreOrder.setReserveOrderStatus(rs.getInt("reserve_order_status"));
			tempPreOrder.setState(rs.getInt("state"));
			tempPreOrder.setIsExist(rs.getInt("is_exist"));
			tempPreOrder.setCreateTime(rs.getTimestamp("create_time"));
			return tempPreOrder;
		}
	}
	
	/**
	 * 查询临时表中是否存在预订单
	 * 
	 * @param reserveOrderNo
	 * @return
	 */
	public ExpressPreOrderDTO getPreOrderExpresstemp(String reserveOrderNo) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * from express_ops_preorder_temp where reverse_order_no=? and state=1 limit 0,1");
		try {
			return jdbcTemplate.queryForObject(sql.toString(), new ExpressPreOrderDTOMapper(), reserveOrderNo);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	/**
	 * 插入记录到临时表
	 * 
	 * @param preOrderDto
	 */
	public long insertPreOrder_toTempTable(final ExpressPreOrderDTO dto) {
		final StringBuffer sql = new StringBuffer();
		sql.append(" insert into express_ops_preorder_temp ");
		sql.append(" (reverse_order_no,order_type,cust_code,cnor_prov,");
		sql.append(" cnor_city,cnor_region,cnor_town,cnor_addr,");
		sql.append(" cnor_name,cnor_mobile,cnor_tel,cnor_remark,");
		sql.append(" carrier_code,reserve_order_status,state,is_exist,");
		sql.append(" create_time)");
		sql.append(" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		KeyHolder key = new GeneratedKeyHolder();
		this.jdbcTemplate.update(new PreparedStatementCreator() {
			
			public PreparedStatement createPreparedStatement(
					java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement(sql.toString(),Statement.RETURN_GENERATED_KEYS);
				int i = 0;

				ps.setString(++i, dto.getReserveOrderNo());
				ps.setInt(++i, dto.getOrderType());
				ps.setString(++i, dto.getCustCode());
				ps.setString(++i, dto.getCnorProv());
				ps.setString(++i, dto.getCnorCity());
				ps.setString(++i, dto.getCnorRegion());
				ps.setString(++i, dto.getCnorTown());
				ps.setString(++i, dto.getCnorAddr());
				ps.setString(++i, dto.getCnorName());
				ps.setString(++i, dto.getCnorMobile());
				ps.setString(++i, dto.getCnorTel());
				ps.setString(++i, dto.getCnorRegion());
				ps.setString(++i, dto.getCarrierCode());
				ps.setInt(++i, dto.getReserveOrderStatus());
				ps.setInt(++i, dto.getState());
				ps.setInt(++i, dto.getIsExist());
				ps.setTimestamp(++i, new Timestamp(System.currentTimeMillis()));
				return ps;
			}
		}, key);
		return key.getKey().longValue();
	}

	/**
	 * 查询预订单列表
	 * 
	 * @return
	 */
	public List<ExpressPreOrderDTO> getPreOrderTempByKeys() {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from express_ops_preorder_temp where is_exist=0 order by create_time limit 0,2000 ");
		List<ExpressPreOrderDTO> preOrderList = jdbcTemplate.query(sql.toString(), new ExpressPreOrderDTOMapper());
		return preOrderList;
	}

	/**
	 * 更新临时表中的记录的状态
	 * 
	 * @param preOrderNo
	 */
	public void update_preOrderDetailTempByPreOrderNo(String preOrderNo) {
		try {
			jdbcTemplate.update("update express_ops_preorder_temp set is_exist=1 where reverse_order_no='" + preOrderNo + "'");
		} catch (DataAccessException e) {
			logger.error("修改临时表数据 is_exist失败！preOrderNo=" + preOrderNo, e);
		}
	}

}
