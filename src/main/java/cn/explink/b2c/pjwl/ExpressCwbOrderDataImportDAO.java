package cn.explink.b2c.pjwl;

import java.math.BigDecimal;
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
import org.springframework.stereotype.Repository;

import cn.explink.domain.CwbOrder;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.CwbStateEnum;
import cn.explink.enumutil.EmailFinishFlagEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.YesOrNoStateEnum;

@Repository
public class ExpressCwbOrderDataImportDAO {

	@Autowired
	JdbcTemplate jdbcTemplate;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private final class ExpressCwbOrderDTOSingleMapper implements RowMapper<ExpressCwbOrderDTO> {
		@Override
		public ExpressCwbOrderDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			ExpressCwbOrderDTO tempPreOrder = new ExpressCwbOrderDTO();
			tempPreOrder.setId(rs.getLong("id") + "");
			tempPreOrder.setTransportNo(rs.getString("transport_no"));
			return tempPreOrder;
		}
	}

	private final class ExpressCwbOrderDTOMapper implements RowMapper<ExpressCwbOrderDTO> {
		@Override
		public ExpressCwbOrderDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			ExpressCwbOrderDTO tempPreOrder = new ExpressCwbOrderDTO();
			tempPreOrder.setId(rs.getString("tps_trans_id"));
			tempPreOrder.setTransportNo(rs.getString("transport_no"));
			tempPreOrder.setCustOrderNo(rs.getString("cust_order_no"));
			tempPreOrder.setCustCode(rs.getString("cust_code"));
			tempPreOrder.setAcceptDept(rs.getString("accept_dept"));
			tempPreOrder.setAcceptOperator(rs.getString("accept_operator"));
			tempPreOrder.setCnorProv(rs.getString("cnor_prov"));
			tempPreOrder.setCnorCity(rs.getString("cnor_city"));
			tempPreOrder.setCnorRegion(rs.getString("cnor_region"));
			tempPreOrder.setCnorTown(rs.getString("cnor_town"));
			tempPreOrder.setCnorAddr(rs.getString("cnor_addr"));
			tempPreOrder.setCnorName(rs.getString("cnor_name"));
			tempPreOrder.setCnorMobile(rs.getString("cnor_mobile"));
			tempPreOrder.setCnorTel(rs.getString("cnor_tel"));
			tempPreOrder.setCnorRemark(rs.getString("cnor_remark"));
			tempPreOrder.setCneeProv(rs.getString("cnee_prov"));
			tempPreOrder.setCneeCity(rs.getString("cnee_city"));
			tempPreOrder.setCneeRegion(rs.getString("cnee_region"));
			tempPreOrder.setCneeTown(rs.getString("cnee_town"));
			tempPreOrder.setCneeAddr(rs.getString("cnee_addr"));
			tempPreOrder.setCneeName(rs.getString("cnee_name"));
			tempPreOrder.setCneeMobile(rs.getString("cnee_mobile"));
			tempPreOrder.setCneeTel(rs.getString("cnee_tel"));
			tempPreOrder.setCneePeriod(rs.getInt("cnee_period"));
			tempPreOrder.setCneeRemark(rs.getString("cnee_remark"));
			tempPreOrder.setCneeCertificate(rs.getString("cnee_certificate"));
			tempPreOrder.setCneeNo(rs.getString("cnee_no"));
			tempPreOrder.setIsCod(rs.getInt("is_cod"));
			tempPreOrder.setCodAmount(rs.getBigDecimal("cod_amount"));
			tempPreOrder.setCarriage(rs.getBigDecimal("carriage"));
			tempPreOrder.setTotalNum(rs.getInt("total_num"));
			tempPreOrder.setTotalWeight(rs.getBigDecimal("total_weight"));
			tempPreOrder.setCalculateWeight(rs.getBigDecimal("calculate_weight"));
			tempPreOrder.setTotalVolume(rs.getBigDecimal("total_volume"));
			tempPreOrder.setTotalBox(rs.getInt("total_box"));
			tempPreOrder.setAssuranceValue(rs.getBigDecimal("assurance_value"));
			tempPreOrder.setAssuranceFee(rs.getBigDecimal("assurance_fee"));
			tempPreOrder.setPayType(rs.getInt("pay_type"));
			tempPreOrder.setPayment(rs.getInt("payment"));
			tempPreOrder.setDetails(rs.getString("details"));
			tempPreOrder.setCargoName(rs.getString("cargo_name"));
			tempPreOrder.setCount(rs.getInt("count"));
			tempPreOrder.setCargoLength(rs.getBigDecimal("cargo_length"));
			tempPreOrder.setCargoWidth(rs.getBigDecimal("cargo_width"));
			tempPreOrder.setCargoHeight(rs.getBigDecimal("cargo_height"));
			tempPreOrder.setWeight(rs.getBigDecimal("weight"));
			tempPreOrder.setVolume(rs.getBigDecimal("volume"));
			tempPreOrder.setCustPackNo(rs.getString("cust_pack_no"));
			tempPreOrder.setSizeSn(rs.getString("size_sn"));
			tempPreOrder.setPrice(rs.getBigDecimal("price"));
			tempPreOrder.setUnit(rs.getString("unit"));
			return tempPreOrder;
		}
	}

	/**
	 * 通过id查询数据
	 * @param id
	 * @return
	 */
	public ExpressCwbOrderDTO getCwbOrderExpresstemp(String tpsTranId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT id,transport_no from express_ops_cwb_exprss_detail_temp where tps_trans_id=? and is_hand_over=0 limit 0,1");
		try {
			return this.jdbcTemplate.queryForObject(sql.toString(), new ExpressCwbOrderDTOSingleMapper(), tpsTranId);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	/**
	 * 插入临时记录的表
	 * @param transOrderDto
	 */
	public long insertTransOrder_toTempTable(final ExpressCwbOrderDTO dto) {
		final StringBuffer sql = new StringBuffer();
		sql.append(" insert into express_ops_cwb_exprss_detail_temp ");
		sql.append(" (transport_no,cust_order_no,cust_code,accept_dept,");
		sql.append(" accept_operator,cnor_prov,cnor_city,cnor_region,");
		sql.append(" cnor_town,cnor_addr,cnor_name,cnor_mobile,");
		sql.append(" cnor_tel,cnor_remark,cnee_prov,cnee_city,");
		sql.append(" cnee_region,cnee_town,cnee_addr,cnee_name,");
		sql.append(" cnee_mobile,cnee_tel,cnee_period,cnee_remark,");
		sql.append(" cnee_certificate,cnee_no,is_cod,cod_amount,");
		sql.append(" carriage,total_num,total_weight,calculate_weight,");
		sql.append(" total_volume,total_box,assurance_value,assurance_fee,");
		sql.append(" pay_type,payment,details,cargo_name,");
		sql.append(" count,cargo_length,cargo_width,cargo_height,");
		sql.append(" weight,volume,cust_pack_no,size_sn,");
		sql.append(" price,unit,tps_trans_id,create_time,");
		sql.append(" is_hand_over )");
		sql.append(" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		KeyHolder key = new GeneratedKeyHolder();
		this.jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
				int i = 0;

				ps.setString(++i, dto.getTransportNo());
				ps.setString(++i, dto.getCustOrderNo());
				ps.setString(++i, dto.getCustCode());
				ps.setString(++i, dto.getAcceptDept());

				ps.setString(++i, dto.getAcceptOperator());
				ps.setString(++i, dto.getCnorProv());
				ps.setString(++i, dto.getCnorCity());
				ps.setString(++i, dto.getCnorRegion());

				ps.setString(++i, dto.getCnorTown());
				ps.setString(++i, dto.getCnorAddr());
				ps.setString(++i, dto.getCnorName());
				ps.setString(++i, dto.getCnorMobile());

				ps.setString(++i, dto.getCnorTel());
				ps.setString(++i, dto.getCnorRemark());
				ps.setString(++i, dto.getCneeProv());
				ps.setString(++i, dto.getCneeCity());

				ps.setString(++i, dto.getCneeRegion());
				ps.setString(++i, dto.getCneeTown());
				ps.setString(++i, dto.getCneeAddr());
				ps.setString(++i, dto.getCneeName());

				ps.setString(++i, dto.getCneeMobile());
				ps.setString(++i, dto.getCneeTel());
				ps.setInt(++i, dto.getCneePeriod());
				ps.setString(++i, dto.getCneeRemark());

				ps.setString(++i, dto.getCneeCertificate());
				ps.setString(++i, dto.getCneeNo());
				ps.setInt(++i, dto.getIsCod());
				ps.setBigDecimal(++i, dto.getCodAmount());

				ps.setBigDecimal(++i, dto.getCarriage());
				ps.setInt(++i, dto.getTotalNum());
				ps.setBigDecimal(++i, dto.getTotalWeight());
				ps.setBigDecimal(++i, dto.getCalculateWeight());

				ps.setBigDecimal(++i, dto.getTotalVolume());
				ps.setInt(++i, dto.getTotalBox());
				ps.setBigDecimal(++i, dto.getAssuranceValue());
				ps.setBigDecimal(++i, dto.getAssuranceFee());

				ps.setInt(++i, dto.getPayType());
				ps.setInt(++i, dto.getPayment());
				ps.setString(++i, dto.getDetails());
				ps.setString(++i, dto.getCargoName());

				ps.setInt(++i, dto.getCount());
				ps.setBigDecimal(++i, dto.getCargoLength());
				ps.setBigDecimal(++i, dto.getCargoWidth());
				ps.setBigDecimal(++i, dto.getCargoHeight());

				ps.setBigDecimal(++i, dto.getWeight());
				ps.setBigDecimal(++i, dto.getVolume());
				ps.setString(++i, dto.getCustPackNo());
				ps.setString(++i, dto.getSizeSn());

				ps.setBigDecimal(++i, dto.getPrice());
				ps.setString(++i, dto.getUnit());
				ps.setString(++i, dto.getId());
				//				ps.setDate(++i, new Date(System.currentTimeMillis()));
				ps.setTimestamp(++i, new Timestamp(System.currentTimeMillis()));

				ps.setInt(++i, YesOrNoStateEnum.No.getValue());

				return ps;
			}
		}, key);
		return key.getKey().longValue();
	}

	/**
	 * 查询临时表中的记录
	 * @return
	 */
	public List<ExpressCwbOrderDTO> getTransOrderTempByKeys() {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from express_ops_cwb_exprss_detail_temp where is_hand_over=0 order by create_time limit 0,2000 ");
		List<ExpressCwbOrderDTO> transOrderList = this.jdbcTemplate.query(sql.toString(), new ExpressCwbOrderDTOMapper());
		return transOrderList;
	}

	private final class QueryRecordMapper implements RowMapper<CwbOrder> {
		@Override
		public CwbOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
			CwbOrder obj = new CwbOrder();
			obj.setOpscwbid(rs.getLong("opscwbid"));
			obj.setCwb(rs.getString("cwb"));
			return obj;
		}
	}

	/**
	 * 查询记录
	 * @param cwb
	 * @return
	 */
	public CwbOrder getCwbOrderRecordByReserveOrderNo(String cwb) {
		StringBuffer sql = new StringBuffer();
		sql.append("select opscwbid,cwb from express_ops_cwb_detail where state = 1 and cwb=? limit 0,1 ");
		try {
			return this.jdbcTemplate.queryForObject(sql.toString(), new QueryRecordMapper(), cwb);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}

	}

	/**
	 * 插入订单记录
	 * @param deliverBranchId
	 * @param transOrder
	 */
	public long insertCwbOrder(final ExpressCwbOrderDTO cwbOrderDTO, final Long deliverBranchId) {
		final StringBuffer sql = new StringBuffer();
		sql.append(" insert into express_ops_cwb_detail ( ");
		sql.append(" cwb,transcwb,collectorname,senderprovince,");//sendercustomcode,
		sql.append(" sendercity,sendercounty,senderstreet,sendercellphone,");
		sql.append(" sendertelephone,senderaddress,sendername,consigneename,");
		sql.append(" cwbprovince,cwbcity,cwbcounty,recstreet,");
		sql.append(" consigneeaddress,consigneemobile,consigneephone,cwbremark,");
		sql.append(" receivablefee,shouldfare,sendnum,hascod,");
		sql.append(" realweight,chargeweight,announcedvalue,insuredfee,");
		sql.append(" paywayid,length,width,height,");
		sql.append(" cwbordertypeid,orderflowid,flowordertype,");
		sql.append(" cargovolume,cwbstate,instationname,state,");
		sql.append(" startbranchid,currentbranchid,nextbranchid,deliverybranchid");
		sql.append(" ,totalfee,fnorgoffset,infactfare,paybackfee,isadditionflag) ");
		sql.append(" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

		KeyHolder key = new GeneratedKeyHolder();
		this.jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
				int i = 0;

				ps.setString(++i, cwbOrderDTO.getTransportNo());
				ps.setString(++i, cwbOrderDTO.getTransportNo());
				//				ps.setString(++i, cwbOrderDTO.getCustCode());
				ps.setString(++i, cwbOrderDTO.getAcceptOperator());
				ps.setString(++i, cwbOrderDTO.getCnorProv());

				ps.setString(++i, cwbOrderDTO.getCnorCity());
				ps.setString(++i, cwbOrderDTO.getCnorRegion());
				ps.setString(++i, cwbOrderDTO.getCnorTown());
				ps.setString(++i, cwbOrderDTO.getCnorMobile());

				ps.setString(++i, cwbOrderDTO.getCnorTel());
				ps.setString(++i, cwbOrderDTO.getCnorProv() + cwbOrderDTO.getCnorCity() + cwbOrderDTO.getCnorRegion() + cwbOrderDTO.getCnorTown() + cwbOrderDTO.getCnorAddr());
				ps.setString(++i, cwbOrderDTO.getCnorName());
				ps.setString(++i, cwbOrderDTO.getCneeName());

				ps.setString(++i, cwbOrderDTO.getCneeProv());
				ps.setString(++i, cwbOrderDTO.getCneeCity());
				ps.setString(++i, cwbOrderDTO.getCneeRegion());
				ps.setString(++i, cwbOrderDTO.getCneeTown());

				ps.setString(++i, cwbOrderDTO.getCneeProv() + cwbOrderDTO.getCneeCity() + cwbOrderDTO.getCneeRegion() + cwbOrderDTO.getCneeTown() + cwbOrderDTO.getCneeAddr());
				ps.setString(++i, cwbOrderDTO.getCneeMobile());
				ps.setString(++i, cwbOrderDTO.getCneeTel());
				ps.setString(++i, cwbOrderDTO.getCnorRemark());

				ps.setBigDecimal(++i, cwbOrderDTO.getCodAmount());
				ps.setBigDecimal(++i, cwbOrderDTO.getCarriage());
				ps.setInt(++i, cwbOrderDTO.getTotalNum());
				ps.setInt(++i, cwbOrderDTO.getIsCod());

				ps.setBigDecimal(++i, cwbOrderDTO.getTotalWeight());
				ps.setBigDecimal(++i, cwbOrderDTO.getCalculateWeight());
				ps.setBigDecimal(++i, cwbOrderDTO.getAssuranceValue());
				ps.setBigDecimal(++i, cwbOrderDTO.getAssuranceFee());

				ps.setLong(++i, cwbOrderDTO.getPayment());
				ps.setInt(++i, cwbOrderDTO.getCargoLength().intValue());
				ps.setInt(++i, cwbOrderDTO.getCargoWidth().intValue());
				ps.setInt(++i, cwbOrderDTO.getCargoHeight().intValue());

				ps.setLong(++i, CwbOrderTypeIdEnum.Express.getValue());
				ps.setInt(++i, FlowOrderTypeEnum.DaoRuShuJu.getValue());
				ps.setInt(++i, EmailFinishFlagEnum.WeiDaoHuo.getValue());

				ps.setFloat(++i, cwbOrderDTO.getTotalVolume().floatValue());
				ps.setLong(++i, CwbStateEnum.PeiShong.getValue());
				ps.setString(++i, cwbOrderDTO.getAcceptDept());
				ps.setInt(++i, 1);

				ps.setLong(++i, 0);//上一个机构id
				ps.setLong(++i, 0);//当前机构
				ps.setLong(++i, 0);//下一站目的机构id
				ps.setLong(++i, deliverBranchId);//配送站点

				ps.setBigDecimal(++i, BigDecimal.ZERO);
				ps.setBigDecimal(++i, BigDecimal.ZERO);
				ps.setBigDecimal(++i, BigDecimal.ZERO);
				ps.setBigDecimal(++i, BigDecimal.ZERO);

				ps.setInt(++i, 1);//补录完成标识，标识=1
				return ps;
			}
		}, key);
		return key.getKey().longValue();
	}

	/**
	 * 更新临时表中的记录
	 * @param tpsTransId
	 */
	public void updateTransDataTempRecord(String tpsTransId) {
		StringBuffer sql = new StringBuffer();
		sql.append("update express_ops_cwb_exprss_detail_temp set is_hand_over=1 where tps_trans_id=?");
		try {
			this.jdbcTemplate.update(sql.toString(), tpsTransId);
		} catch (DataAccessException e) {
			this.logger.error("修改运单号临时表数据 is_hand_over失败！transOrderId=" + tpsTransId, e);
		}
	}
}
