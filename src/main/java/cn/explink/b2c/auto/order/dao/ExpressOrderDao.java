package cn.explink.b2c.auto.order.dao;

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
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import cn.explink.b2c.auto.order.domain.ExpressDetailTemp;
import cn.explink.domain.Branch;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.User;
import cn.explink.enumutil.CwbOrderAddressCodeEditTypeEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.CwbStateEnum;
import cn.explink.enumutil.EmailFinishFlagEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.enumutil.YesOrNoStateEnum;
import cn.explink.util.DateTimeUtil;


/**
 * 快递订单数据库处理
 * @author jian.xie
 *
 */
@Repository("inter.expressOrderDao")
public class ExpressOrderDao {

	@Autowired
	JdbcTemplate jdbcTemplate;

	private Logger logger = LoggerFactory.getLogger(ExpressOrderDao.class);

	/**
	 * 查询临时记录完整记录
	 * @author jian.xie
	 *
	 */
	private final class ExpressDetailTempMapper implements RowMapper<ExpressDetailTemp> {
		@Override
		public ExpressDetailTemp mapRow(ResultSet rs, int rowNum) throws SQLException {
			ExpressDetailTemp expressDetailTemp = new ExpressDetailTemp();
			expressDetailTemp.setTpsTransId(rs.getString("tps_trans_id"));
			expressDetailTemp.setTransportNo(rs.getString("transport_no"));
			expressDetailTemp.setCustOrderNo(rs.getString("cust_order_no"));
			expressDetailTemp.setCustCode(rs.getString("cust_code"));
			expressDetailTemp.setAcceptDept(rs.getString("accept_dept"));
			expressDetailTemp.setAcceptOperator(rs.getString("accept_operator"));
			expressDetailTemp.setCnorProv(rs.getString("cnor_prov"));
			expressDetailTemp.setCnorCity(rs.getString("cnor_city"));
			expressDetailTemp.setCnorRegion(rs.getString("cnor_region"));
			expressDetailTemp.setCnorTown(rs.getString("cnor_town"));
			expressDetailTemp.setCnorAddr(rs.getString("cnor_addr"));
			expressDetailTemp.setCnorName(rs.getString("cnor_name"));
			expressDetailTemp.setCnorMobile(rs.getString("cnor_mobile"));
			expressDetailTemp.setCnorTel(rs.getString("cnor_tel"));
			expressDetailTemp.setCnorRemark(rs.getString("cnor_remark"));
			expressDetailTemp.setCneeProv(rs.getString("cnee_prov"));
			expressDetailTemp.setCneeCity(rs.getString("cnee_city"));
			expressDetailTemp.setCneeRegion(rs.getString("cnee_region"));
			expressDetailTemp.setCneeTown(rs.getString("cnee_town"));
			expressDetailTemp.setCneeAddr(rs.getString("cnee_addr"));
			expressDetailTemp.setCneeName(rs.getString("cnee_name"));
			expressDetailTemp.setCneeMobile(rs.getString("cnee_mobile"));
			expressDetailTemp.setCneeTel(rs.getString("cnee_tel"));
			expressDetailTemp.setCneePeriod(rs.getInt("cnee_period"));
			expressDetailTemp.setCneeRemark(rs.getString("cnee_remark"));
			expressDetailTemp.setCneeCertificate(rs.getString("cnee_certificate"));
			expressDetailTemp.setCneeNo(rs.getString("cnee_no"));
			expressDetailTemp.setIsCod(rs.getInt("is_cod"));
			expressDetailTemp.setCodAmount(rs.getBigDecimal("cod_amount"));
			expressDetailTemp.setCarriage(rs.getBigDecimal("carriage"));
			expressDetailTemp.setTotalNum(rs.getInt("total_num"));
			expressDetailTemp.setTotalWeight(rs.getBigDecimal("total_weight"));
			expressDetailTemp.setCalculateWeight(rs.getBigDecimal("calculate_weight"));
			expressDetailTemp.setTotalVolume(rs.getBigDecimal("total_volume"));
			expressDetailTemp.setTotalBox(rs.getInt("total_box"));
			expressDetailTemp.setAssuranceValue(rs.getBigDecimal("assurance_value"));
			expressDetailTemp.setAssuranceFee(rs.getBigDecimal("assurance_fee"));
			expressDetailTemp.setPayType(rs.getInt("pay_type"));
			expressDetailTemp.setPayment(rs.getInt("payment"));
			expressDetailTemp.setDetails(rs.getString("details"));
			expressDetailTemp.setCargoName(rs.getString("cargo_name"));
			expressDetailTemp.setCount(rs.getInt("count"));
			expressDetailTemp.setCargoLength(rs.getBigDecimal("cargo_length"));
			expressDetailTemp.setCargoWidth(rs.getBigDecimal("cargo_width"));
			expressDetailTemp.setCargoHeight(rs.getBigDecimal("cargo_height"));
			expressDetailTemp.setWeight(rs.getBigDecimal("weight"));
			expressDetailTemp.setVolume(rs.getBigDecimal("volume"));
			expressDetailTemp.setCustPackNo(rs.getString("cust_pack_no"));
			expressDetailTemp.setSizeSn(rs.getString("size_sn"));
			expressDetailTemp.setPrice(rs.getBigDecimal("price"));
			expressDetailTemp.setUnit(rs.getString("unit"));
			expressDetailTemp.setCnorCorpNo(rs.getString("cnor_corp_no"));
			expressDetailTemp.setCnorCorpName(rs.getString("cnor_corp_name"));
			expressDetailTemp.setFreight(rs.getBigDecimal("freight"));
			expressDetailTemp.setAccountId(rs.getString("account_id"));
			expressDetailTemp.setPackingFee(rs.getBigDecimal("packing_fee"));
			expressDetailTemp.setExpressImage(rs.getString("express_image"));
			expressDetailTemp.setCneeCorpName(rs.getString("cnee_corp_name"));
			expressDetailTemp.setExpressProductType(rs.getInt("express_product_type"));
			expressDetailTemp.setIsAcceptProv(rs.getInt("is_accept_prov"));
			return expressDetailTemp;
		}
	}

	/**
	 * 通过id查询数据
	 * @param id
	 * @return
	 */
	public ExpressDetailTemp getCwbOrderExpresstemp(String tpsTranId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * from express_ops_cwb_exprss_detail_temp where tps_trans_id= ? limit 0,1");
		List<ExpressDetailTemp> list = this.jdbcTemplate.query(sql.toString(), new ExpressDetailTempMapper(), tpsTranId);
		if(CollectionUtils.isEmpty(list)){
			return null;
		}else {
			return list.get(0);
		}
	}

	/**
	 * 插入临时记录的表
	 * @param transOrderDto
	 */
	public long insertExpressDetailTemp(final ExpressDetailTemp expressDetailTemp) {
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
		sql.append(" price,unit,tps_trans_id,create_time,is_hand_over,");
		sql.append(" cnor_corp_no, cnor_corp_name,freight,account_id,packing_fee,express_image,cnee_corp_name,is_accept_prov,express_product_type)");
		sql.append(" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		KeyHolder key = new GeneratedKeyHolder();
		this.jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
				int i = 0;

				ps.setString(++i, expressDetailTemp.getTransportNo());
				ps.setString(++i, expressDetailTemp.getCustOrderNo());
				ps.setString(++i, expressDetailTemp.getCustCode());
				ps.setString(++i, expressDetailTemp.getAcceptDept());

				ps.setString(++i, expressDetailTemp.getAcceptOperator());
				ps.setString(++i, expressDetailTemp.getCnorProv());
				ps.setString(++i, expressDetailTemp.getCnorCity());
				ps.setString(++i, expressDetailTemp.getCnorRegion());

				ps.setString(++i, expressDetailTemp.getCnorTown());
				ps.setString(++i, expressDetailTemp.getCnorAddr());
				ps.setString(++i, expressDetailTemp.getCnorName());
				ps.setString(++i, expressDetailTemp.getCnorMobile());

				ps.setString(++i, expressDetailTemp.getCnorTel());
				ps.setString(++i, expressDetailTemp.getCnorRemark());
				ps.setString(++i, expressDetailTemp.getCneeProv());
				ps.setString(++i, expressDetailTemp.getCneeCity());

				ps.setString(++i, expressDetailTemp.getCneeRegion());
				ps.setString(++i, expressDetailTemp.getCneeTown());
				ps.setString(++i, expressDetailTemp.getCneeAddr());
				ps.setString(++i, expressDetailTemp.getCneeName());

				ps.setString(++i, expressDetailTemp.getCneeMobile());
				ps.setString(++i, expressDetailTemp.getCneeTel());
				ps.setInt(++i, expressDetailTemp.getCneePeriod());
				ps.setString(++i, expressDetailTemp.getCneeRemark());

				ps.setString(++i, expressDetailTemp.getCneeCertificate());
				ps.setString(++i, expressDetailTemp.getCneeNo());
				ps.setInt(++i, expressDetailTemp.getIsCod());
				ps.setBigDecimal(++i, expressDetailTemp.getCodAmount());

				ps.setBigDecimal(++i, expressDetailTemp.getCarriage());
				ps.setInt(++i, expressDetailTemp.getTotalNum());
				ps.setBigDecimal(++i, expressDetailTemp.getTotalWeight());
				ps.setBigDecimal(++i, expressDetailTemp.getCalculateWeight());

				ps.setBigDecimal(++i, expressDetailTemp.getTotalVolume());
				ps.setInt(++i, expressDetailTemp.getTotalBox());
				ps.setBigDecimal(++i, expressDetailTemp.getAssuranceValue());
				ps.setBigDecimal(++i, expressDetailTemp.getAssuranceFee());

				ps.setInt(++i, expressDetailTemp.getPayType());
				ps.setInt(++i, expressDetailTemp.getPayment());
				ps.setString(++i, expressDetailTemp.getDetails());
				ps.setString(++i, expressDetailTemp.getCargoName());

				ps.setInt(++i, expressDetailTemp.getCount());
				ps.setBigDecimal(++i, expressDetailTemp.getCargoLength());
				ps.setBigDecimal(++i, expressDetailTemp.getCargoWidth());
				ps.setBigDecimal(++i, expressDetailTemp.getCargoHeight());

				ps.setBigDecimal(++i, expressDetailTemp.getWeight());
				ps.setBigDecimal(++i, expressDetailTemp.getVolume());
				ps.setString(++i, expressDetailTemp.getCustPackNo());
				ps.setString(++i, expressDetailTemp.getSizeSn());

				ps.setBigDecimal(++i, expressDetailTemp.getPrice());
				ps.setString(++i, expressDetailTemp.getUnit());
				ps.setString(++i, expressDetailTemp.getTpsTransId());
				//				ps.setDate(++i, new Date(System.currentTimeMillis()));
				ps.setTimestamp(++i, new Timestamp(System.currentTimeMillis()));

				ps.setInt(++i, YesOrNoStateEnum.No.getValue());
				
				ps.setString(++i, expressDetailTemp.getCnorCorpNo());
				ps.setString(++i, expressDetailTemp.getCnorCorpName());
				ps.setBigDecimal(++i, expressDetailTemp.getFreight());
				ps.setString(++i, expressDetailTemp.getAccountId());
				ps.setBigDecimal(++i, expressDetailTemp.getPackingFee());
				ps.setString(++i, expressDetailTemp.getExpressImage());
				ps.setString(++i, expressDetailTemp.getCneeCorpName());
				ps.setInt(++i, expressDetailTemp.getIsAcceptProv());
				ps.setInt(++i, expressDetailTemp.getExpressProductType());
				return ps;
			}
		}, key);
		return key.getKey().longValue();
	}

	/**
	 * 查询临时表中未转业务的记录----揽件省
	 * @param provinceType 0、派件省，1、揽件省
	 * @return
	 */
	public List<ExpressDetailTemp> getExpressDetailTempListNotOver(int provinceType) {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from express_ops_cwb_exprss_detail_temp where is_hand_over=0 and is_accept_prov=? order by create_time limit 0,2000 ");
		List<ExpressDetailTemp> transOrderList = this.jdbcTemplate.query(sql.toString(), new ExpressDetailTempMapper(), provinceType);
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
	public long insertCwbOrder(final ExpressDetailTemp expressDetailTemp, final Branch branch, final Branch acceptBranch, final User user) {
		final StringBuffer sql = new StringBuffer();
		sql.append(" insert into express_ops_cwb_detail ( ");
		sql.append(" cwb,transcwb,collectorid,collectorname,senderprovince,");//sendercustomcode,
		sql.append(" sendercity,sendercounty,senderstreet,sendercellphone,");
		sql.append(" sendertelephone,senderaddress,sendername,consigneename,");
		sql.append(" cwbprovince,cwbcity,cwbcounty,recstreet,");
		sql.append(" consigneeaddress,consigneemobile,consigneephone,cwbremark,");
		sql.append(" receivablefee,shouldfare,sendnum,hascod,");
		sql.append(" realweight,chargeweight,announcedvalue,insuredfee,");
		sql.append(" paywayid,length,width,height,");
		sql.append(" cwbordertypeid,orderflowid,flowordertype,");
		sql.append(" cargovolume,cwbstate,instationid,instationname,state,");
		sql.append(" startbranchid,currentbranchid,nextbranchid,deliverybranchid,excelbranch,addresscodeedittype");
		sql.append(" ,totalfee,fnorgoffset,infactfare,paybackfee,isadditionflag,credate ");
		sql.append(" , cnor_corp_no,cnor_corp_name,account_id,packagefee,express_image,cnee_corp_name,express_product_type,customerid,hasinsurance)");
		sql.append(" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

		KeyHolder key = new GeneratedKeyHolder();
		this.jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
				int i = 0;

				ps.setString(++i, expressDetailTemp.getTransportNo());
				ps.setString(++i, expressDetailTemp.getTransportNo());
				//				ps.setString(++i, cwbOrderDTO.getCustCode());
				ps.setString(++i, user.getUserid() + "");// 小件员id
				ps.setString(++i, user.getRealname());// 小件员名称
				ps.setString(++i, expressDetailTemp.getCnorProv());

				ps.setString(++i, expressDetailTemp.getCnorCity());
				ps.setString(++i, expressDetailTemp.getCnorRegion());
				ps.setString(++i, expressDetailTemp.getCnorTown());
				ps.setString(++i, expressDetailTemp.getCnorMobile());

				ps.setString(++i, expressDetailTemp.getCnorTel());
				ps.setString(++i, expressDetailTemp.getCnorProv() + expressDetailTemp.getCnorCity() + expressDetailTemp.getCnorRegion() + expressDetailTemp.getCnorTown() + expressDetailTemp.getCnorAddr());
				ps.setString(++i, expressDetailTemp.getCnorName());
				ps.setString(++i, expressDetailTemp.getCneeName());

				ps.setString(++i, expressDetailTemp.getCneeProv());
				ps.setString(++i, expressDetailTemp.getCneeCity());
				ps.setString(++i, expressDetailTemp.getCneeRegion());
				ps.setString(++i, expressDetailTemp.getCneeTown());

				//如果详细地址里面已经含省+市+区，则不再加入省市区
				String cneeProv = expressDetailTemp.getCneeProv();
				String cneeCity = expressDetailTemp.getCneeCity();
				String cneeRegion = expressDetailTemp.getCneeRegion();
				String cneeTown = expressDetailTemp.getCneeTown();
				String cneeAddr = expressDetailTemp.getCneeAddr();
				if(null != cneeAddr){
					if(null != cneeTown && cneeAddr.indexOf(cneeTown) < 0){//从地址小的开始处理
						cneeAddr = cneeTown + cneeAddr;
					}
					if(null != cneeRegion && cneeAddr.indexOf(cneeRegion) < 0){
						cneeAddr = cneeRegion + cneeAddr;
					}
					if(null != cneeCity && cneeAddr.indexOf(cneeCity) < 0){
						cneeAddr = cneeCity + cneeAddr;
					}
					if(null != cneeProv && cneeAddr.indexOf(cneeProv) < 0){
						cneeAddr = cneeProv + cneeAddr;
					}
				}
				
				ps.setString(++i, cneeAddr);
				ps.setString(++i, expressDetailTemp.getCneeMobile());
				ps.setString(++i, expressDetailTemp.getCneeTel());
				ps.setString(++i, expressDetailTemp.getCnorRemark());

				ps.setBigDecimal(++i, expressDetailTemp.getCodAmount());
				ps.setBigDecimal(++i, expressDetailTemp.getFreight());// 运费
				ps.setInt(++i, expressDetailTemp.getTotalNum());
				if(expressDetailTemp.getCodAmount().compareTo(new BigDecimal(0)) > 0){
					ps.setInt(++i, 1);// 是否有代收货款
				}else{
					ps.setInt(++i, 0);
				}

				ps.setBigDecimal(++i, expressDetailTemp.getTotalWeight());
				ps.setBigDecimal(++i, expressDetailTemp.getCalculateWeight());
				ps.setBigDecimal(++i, expressDetailTemp.getAssuranceValue());
				ps.setBigDecimal(++i, expressDetailTemp.getAssuranceFee());

				ps.setLong(++i, getPayTypeValue(expressDetailTemp.getPayment()));//支付方式
				ps.setInt(++i, expressDetailTemp.getCargoLength().intValue());
				ps.setInt(++i, expressDetailTemp.getCargoWidth().intValue());
				ps.setInt(++i, expressDetailTemp.getCargoHeight().intValue());

				ps.setLong(++i, CwbOrderTypeIdEnum.Express.getValue());
				ps.setInt(++i, FlowOrderTypeEnum.DaoRuShuJu.getValue());
				
				ps.setInt(++i, 1001);// 状态

				ps.setFloat(++i, expressDetailTemp.getTotalVolume().floatValue());
				ps.setLong(++i, CwbStateEnum.PeiShong.getValue());
				ps.setInt(++i, (int)acceptBranch.getBranchid());
				ps.setString(++i, acceptBranch.getBranchname());//站点
				ps.setInt(++i, 1);

				ps.setLong(++i, 0);//上一个机构id
				ps.setLong(++i, 0);//当前机构
				ps.setLong(++i, 0);//下一站目的机构id
				ps.setLong(++i, ((null != branch)?branch.getBranchid():0L));//配送站点ID
				ps.setString(++i, ((null != branch)?branch.getBranchname():""));//配送站点名称
				ps.setInt(++i, ((null != branch)?CwbOrderAddressCodeEditTypeEnum.DiZhiKu.getValue():CwbOrderAddressCodeEditTypeEnum.WeiPiPei.getValue()));//是否匹配状态位

				ps.setBigDecimal(++i, expressDetailTemp.getCarriage());// 费用合计
				ps.setBigDecimal(++i, BigDecimal.ZERO);
				ps.setBigDecimal(++i, BigDecimal.ZERO);
				ps.setBigDecimal(++i, BigDecimal.ZERO);
				if(expressDetailTemp.getIsAcceptProv() == 1){
					ps.setInt(++i, 0);//补录完成标识，
				}else{
					ps.setInt(++i, 1);
				}
				ps.setTimestamp(++i, Timestamp.valueOf(DateTimeUtil.getNowTime()));
				ps.setString(++i, expressDetailTemp.getCnorCorpNo());
				ps.setString(++i, expressDetailTemp.getCnorCorpName());
				ps.setString(++i, expressDetailTemp.getAccountId());
				ps.setBigDecimal(++i, expressDetailTemp.getPackingFee());
				ps.setString(++i, expressDetailTemp.getExpressImage());
				ps.setString(++i, expressDetailTemp.getCneeCorpName());
				ps.setInt(++i, expressDetailTemp.getExpressProductType());
				ps.setInt(++i, 1000);// customerid
				// 是否有保价
				if (expressDetailTemp.getAssuranceValue().compareTo(new BigDecimal(0)) > 0) {
					ps.setInt(++i, 1);
				} else {
					ps.setInt(++i, 0);
				}
				return ps;
			}
		}, key);
		return key.getKey().longValue();
	}

	/**
	 * 更新临时表中的已转业务记录
	 * @param tpsTransId
	 */
	public void updateExpressDetailTempForOver(String tpsTransId) {
		StringBuffer sql = new StringBuffer();
		sql.append("update express_ops_cwb_exprss_detail_temp set is_hand_over=1 where tps_trans_id=?");
		this.jdbcTemplate.update(sql.toString(), tpsTransId);
	}
	
	
	/**
	 * 根据tps的支付方式，返回dmp的支付方式
	 * @param payment
	 * @return
	 */
	private int getPayTypeValue(int payment){
		int pay = 0;
		switch (payment) {
		case 0:
			pay = PaytypeEnum.Xianjin.getValue();
			break;
		case 1:
			pay = PaytypeEnum.Pos.getValue();
			break;
		case 2:
			pay = PaytypeEnum.CodPos.getValue();
			break;
		case 3:
			pay = PaytypeEnum.CodPos.getValue();
			break;
		case 4:
			pay = PaytypeEnum.CodPos.getValue();
			break;
		case 5:
			pay = PaytypeEnum.Pos.getValue();
			break;
		case 6:
			pay = PaytypeEnum.Pos.getValue();
			break;
		case 7:
			pay = PaytypeEnum.Pos.getValue();
			break;
		case 8:
			pay = PaytypeEnum.Pos.getValue();
			break;
		case 9:
			pay = PaytypeEnum.CodPos.getValue();
			break;
		case 10:
			pay = PaytypeEnum.CodPos.getValue();
			break;
		case 11:
			pay = PaytypeEnum.CodPos.getValue();
			break;
		case 12:
			pay = PaytypeEnum.Pos.getValue();
			break;
		case 13:
			pay = PaytypeEnum.Zhipiao.getValue();
			break;
		case 14:
			pay = PaytypeEnum.Qita.getValue();
			break;
		}
		
		return pay;
	}

	/**
	 * 更新临时表中的特定字段
	 * @param tpsTransId
	 */
	public void updateExpressDetailTemp(ExpressDetailTemp expressDetailTemp) {
		// TODO
		
	}
}
