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

import cn.explink.domain.Branch;
import cn.explink.domain.CwbOrder;
import cn.explink.enumutil.CwbOrderAddressCodeEditTypeEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.CwbStateEnum;
import cn.explink.enumutil.EmailFinishFlagEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.YesOrNoStateEnum;
import cn.explink.util.DateTimeUtil;

/**
 * 快递单数据操作
 * @author jian.xie
 *
 */
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
			ExpressCwbOrderDTO order = new ExpressCwbOrderDTO();
			order.setId(rs.getString("tps_trans_id"));
			order.setTransportNo(rs.getString("transport_no"));
			order.setCustOrderNo(rs.getString("cust_order_no"));
			order.setCustCode(rs.getString("cust_code"));
			order.setAcceptDept(rs.getString("accept_dept"));
			order.setAcceptOperator(rs.getString("accept_operator"));
			order.setCnorProv(rs.getString("cnor_prov"));
			order.setCnorCity(rs.getString("cnor_city"));
			order.setCnorRegion(rs.getString("cnor_region"));
			order.setCnorTown(rs.getString("cnor_town"));
			order.setCnorAddr(rs.getString("cnor_addr"));
			order.setCnorName(rs.getString("cnor_name"));
			order.setCnorMobile(rs.getString("cnor_mobile"));
			order.setCnorTel(rs.getString("cnor_tel"));
			order.setCnorRemark(rs.getString("cnor_remark"));
			order.setCneeProv(rs.getString("cnee_prov"));
			order.setCneeCity(rs.getString("cnee_city"));
			order.setCneeRegion(rs.getString("cnee_region"));
			order.setCneeTown(rs.getString("cnee_town"));
			order.setCneeAddr(rs.getString("cnee_addr"));
			order.setCneeName(rs.getString("cnee_name"));
			order.setCneeMobile(rs.getString("cnee_mobile"));
			order.setCneeTel(rs.getString("cnee_tel"));
			order.setCneePeriod(rs.getInt("cnee_period"));
			order.setCneeRemark(rs.getString("cnee_remark"));
			order.setCneeCertificate(rs.getString("cnee_certificate"));
			order.setCneeNo(rs.getString("cnee_no"));
			order.setIsCod(rs.getInt("is_cod"));
			order.setCodAmount(rs.getBigDecimal("cod_amount"));
			order.setCarriage(rs.getBigDecimal("carriage"));
			order.setTotalNum(rs.getInt("total_num"));
			order.setTotalWeight(rs.getBigDecimal("total_weight"));
			order.setCalculateWeight(rs.getBigDecimal("calculate_weight"));
			order.setTotalVolume(rs.getBigDecimal("total_volume"));
			order.setTotalBox(rs.getInt("total_box"));
			order.setAssuranceValue(rs.getBigDecimal("assurance_value"));
			order.setAssuranceFee(rs.getBigDecimal("assurance_fee"));
			order.setPayType(rs.getInt("pay_type"));
			order.setPayment(rs.getInt("payment"));
			order.setDetails(rs.getString("details"));
			order.setCargoName(rs.getString("cargo_name"));
			order.setCount(rs.getInt("count"));
			order.setCargoLength(rs.getBigDecimal("cargo_length"));
			order.setCargoWidth(rs.getBigDecimal("cargo_width"));
			order.setCargoHeight(rs.getBigDecimal("cargo_height"));
			order.setWeight(rs.getBigDecimal("weight"));
			order.setVolume(rs.getBigDecimal("volume"));
			order.setCustPackNo(rs.getString("cust_pack_no"));
			order.setSizeSn(rs.getString("size_sn"));
			order.setPrice(rs.getBigDecimal("price"));
			order.setUnit(rs.getString("unit"));
			return order;
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
		sql.append("select * from express_ops_cwb_exprss_detail_temp where is_hand_over=0 and is_accept_prov =0 order by create_time limit 0,2000 ");
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
	public long insertCwbOrder(final ExpressCwbOrderDTO cwbOrderDTO, final Branch branch) {
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
		sql.append(" startbranchid,currentbranchid,nextbranchid,deliverybranchid,excelbranch,addresscodeedittype");
		sql.append(" ,totalfee,fnorgoffset,infactfare,paybackfee,isadditionflag,credate,customerid,paymethod) ");
		sql.append(" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

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

				//如果详细地址里面已经含省+市+区，则不再加入省市区
				// modify by jian_xie 2016-07-18,DMP地址拼接去重逻辑的修改何欣伟需求
//				String cneeProv = cwbOrderDTO.getCneeProv();
//				String cneeCity = cwbOrderDTO.getCneeCity();
//				String cneeRegion = cwbOrderDTO.getCneeRegion();
//				String cneeTown = cwbOrderDTO.getCneeTown();
				String cneeAddr = cwbOrderDTO.getCneeAddr();
//				if(null != cneeAddr){
//					if(null != cneeTown && cneeAddr.indexOf(cneeTown) < 0){//从地址小的开始处理
//						cneeAddr = cneeTown + cneeAddr;
//					}
//					if(null != cneeRegion && cneeAddr.indexOf(cneeRegion) < 0){
//						cneeAddr = cneeRegion + cneeAddr;
//					}
//					if(null != cneeCity && cneeAddr.indexOf(cneeCity) < 0){
//						cneeAddr = cneeCity + cneeAddr;
//					}
//					if(null != cneeProv && cneeAddr.indexOf(cneeProv) < 0){
//						cneeAddr = cneeProv + cneeAddr;
//					}
//				}
				ps.setString(++i, cneeAddr);
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
				ps.setBigDecimal(++i, cwbOrderDTO.getCargoLength());// 长
				ps.setBigDecimal(++i, cwbOrderDTO.getCargoWidth());// 宽
				ps.setBigDecimal(++i, cwbOrderDTO.getCargoHeight());// 高

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
				ps.setLong(++i, ((null != branch)?branch.getBranchid():0L));//配送站点ID
				ps.setString(++i, ((null != branch)?branch.getBranchname():""));//配送站点名称
				ps.setInt(++i, ((null != branch)?CwbOrderAddressCodeEditTypeEnum.DiZhiKu.getValue():CwbOrderAddressCodeEditTypeEnum.WeiPiPei.getValue()));//是否匹配状态位

				ps.setBigDecimal(++i, BigDecimal.ZERO);
				ps.setBigDecimal(++i, BigDecimal.ZERO);
				ps.setBigDecimal(++i, BigDecimal.ZERO);
				ps.setBigDecimal(++i, BigDecimal.ZERO);

				ps.setInt(++i, 1);//补录完成标识，标识=1
				ps.setTimestamp(++i, Timestamp.valueOf(DateTimeUtil.getNowTime()));
				//给快递一期转业务定时器doTime_expressTransOrder加上customerid和paymethod的转化
				ps.setInt(++i, 1000);// customerid
				ps.setInt(++i, cwbOrderDTO.getPayType());
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
	
	//根据快递单号查询图片url
	public String getExpressImageById(String transport_no) {
		try {
			return this.jdbcTemplate
					.queryForObject(
							"SELECT express_image from express_ops_cwb_exprss_detail_temp where transport_no=? limit 0,1",String.class,transport_no);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
}
