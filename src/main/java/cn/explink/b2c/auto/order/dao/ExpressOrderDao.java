package cn.explink.b2c.auto.order.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import cn.explink.b2c.auto.order.domain.ExpressDetailTemp;
import cn.explink.b2c.auto.order.util.MqOrderBusinessUtil;
import cn.explink.dao.express.CityDAO;
import cn.explink.dao.express.CountyDAO;
import cn.explink.dao.express.ProvinceDAO;
import cn.explink.dao.express.TownDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.User;
import cn.explink.domain.VO.express.AdressVO;
import cn.explink.enumutil.CwbOrderAddressCodeEditTypeEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.CwbStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
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
	
	@Autowired
	private ProvinceDAO provinceDAO;
	@Autowired
	private CityDAO cityDAO;
	@Autowired
    private CountyDAO countyDAO;	
	@Autowired
	private TownDAO townDAO;

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
			expressDetailTemp.setReturnCredit(rs.getBigDecimal("return_credit"));
			expressDetailTemp.setOrderSource(rs.getInt("order_source"));
			expressDetailTemp.setDoCreateTime(rs.getTimestamp("do_create_time")!=null?rs.getTimestamp("do_create_time").getTime():0);
			expressDetailTemp.setIsHandOver(rs.getInt("is_hand_over"));
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
		sql.append(" cnor_corp_no, cnor_corp_name,freight,account_id,packing_fee,express_image,cnee_corp_name,is_accept_prov,express_product_type,return_credit,order_source,do_create_time)");
		sql.append(" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
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
				ps.setString(++i, expressDetailTemp.getCnorProv());// 寄件省
				ps.setString(++i, expressDetailTemp.getCnorCity());// 市
				ps.setString(++i, expressDetailTemp.getCnorRegion());// 区

				ps.setString(++i, expressDetailTemp.getCnorTown());// 街道
				ps.setString(++i, expressDetailTemp.getCnorAddr());
				ps.setString(++i, expressDetailTemp.getCnorName());
				ps.setString(++i, expressDetailTemp.getCnorMobile());

				ps.setString(++i, expressDetailTemp.getCnorTel());
				ps.setString(++i, expressDetailTemp.getCnorRemark());
				ps.setString(++i, expressDetailTemp.getCneeProv());// 收件省
				ps.setString(++i, expressDetailTemp.getCneeCity());// 收件市

				ps.setString(++i, expressDetailTemp.getCneeRegion());// 收件区
				ps.setString(++i, expressDetailTemp.getCneeTown());// 街道
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
				ps.setBigDecimal(++i, expressDetailTemp.getReturnCredit());// 应退金额
				ps.setInt(++i, expressDetailTemp.getOrderSource());// 订单来源
				ps.setTimestamp(++i, new Timestamp(expressDetailTemp.getDoCreateTime()));// 订单来源
				return ps;
			}
		}, key);
		return key.getKey().longValue();
	}
	
	public static void main(String[] args){
		System.out.println(new Timestamp(System.currentTimeMillis()));
		System.out.println(new Timestamp(1477987144925l));
	}

	/**
	 * 查询临时表中未转业务的记录----揽件省
	 * @param provinceType 0、派件省，1、揽件省
	 * @return
	 */
	public List<ExpressDetailTemp> getExpressDetailTempListNotOver(String provinceType) {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from express_ops_cwb_exprss_detail_temp where is_hand_over=0 and is_accept_prov in (" + provinceType + ") order by create_time limit 0,2000 ");
		//sql.append("select * from express_ops_cwb_exprss_detail_temp where is_hand_over=0 and cust_order_no='201611011551' order by create_time limit 0,2000 ");
		logger.info("getExpressDetailTempListNotOver,sql:{}", sql.toString());
		
		List<ExpressDetailTemp> transOrderList = this.jdbcTemplate.query(sql.toString(), new ExpressDetailTempMapper());
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
		sql.append(" receivablefee,shouldfare,sendcarnum,hascod,");
		sql.append(" realweight,chargeweight,announcedvalue,insuredfee,");
		sql.append(" paywayid,length,width,height,");
		sql.append(" cwbordertypeid,orderflowid,flowordertype,");
		sql.append(" cargovolume,cwbstate,instationid,instationname,state,");
		sql.append(" startbranchid,currentbranchid,nextbranchid,deliverybranchid,excelbranch,addresscodeedittype");
		sql.append(" ,totalfee,fnorgoffset,infactfare,paybackfee,isadditionflag,credate ");
		sql.append(" , cnor_corp_no,cnor_corp_name,account_id,packagefee,express_image,cnee_corp_name,express_product_type,customerid,hasinsurance,paymethod,newpaywayid,monthsettleno");
		sql.append(" , tpstranscwb,senderprovinceid,sendercityid,sendercountyid,senderstreetid,recprovinceid,reccityid,reccountyid,recstreetid,order_source,inputdatetime)");
		sql.append(" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

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
				if(expressDetailTemp.getIsAcceptProv() == 1){
					ps.setString(++i, user != null ? user.getUserid() + "":"0");// 小件员id
					ps.setString(++i, user != null ? user.getRealname() : "");// 小件员名称
				} else{
					ps.setString(++i, "0");// 小件员id
					ps.setString(++i, "");// 小件员名称
				}
				ps.setString(++i, expressDetailTemp.getCnorProv());// 寄件人省

				ps.setString(++i, expressDetailTemp.getCnorCity());// 市
				ps.setString(++i, expressDetailTemp.getCnorRegion());// 区
				ps.setString(++i, expressDetailTemp.getCnorTown());// 街道
				ps.setString(++i, expressDetailTemp.getCnorMobile());

				ps.setString(++i, expressDetailTemp.getCnorTel());
				ps.setString(++i, expressDetailTemp.getCnorAddr());
				ps.setString(++i, expressDetailTemp.getCnorName());
				ps.setString(++i, expressDetailTemp.getCneeName());

				ps.setString(++i, expressDetailTemp.getCneeProv());// 收件人省
				ps.setString(++i, expressDetailTemp.getCneeCity());// 市
				ps.setString(++i, expressDetailTemp.getCneeRegion());// 区
				ps.setString(++i, expressDetailTemp.getCneeTown());// 街道

				//如果详细地址里面已经含省+市+区，则不再加入省市区
				String cneeAddr = expressDetailTemp.getCneeAddr();
				
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

				ps.setInt(++i, MqOrderBusinessUtil.getPayTypeValue(expressDetailTemp.getPayment()));//原支付方式
				ps.setBigDecimal(++i, expressDetailTemp.getCargoLength());// 长
				ps.setBigDecimal(++i, expressDetailTemp.getCargoWidth());// 宽
				ps.setBigDecimal(++i, expressDetailTemp.getCargoHeight());// 高

				ps.setLong(++i, CwbOrderTypeIdEnum.Express.getValue());
				ps.setInt(++i, FlowOrderTypeEnum.DaoRuShuJu.getValue());
				
				ps.setInt(++i, 1001);// 状态

				ps.setFloat(++i, expressDetailTemp.getTotalVolume().floatValue());
				ps.setLong(++i, CwbStateEnum.PeiShong.getValue());
				if(expressDetailTemp.getIsAcceptProv() == 1){
					ps.setInt(++i, acceptBranch != null ? (int)acceptBranch.getBranchid() : 0);
					ps.setString(++i, acceptBranch != null ? acceptBranch.getBranchname() : "");//站点
				}else{
					ps.setInt(++i, 0);
					ps.setString(++i, "");//站点
				}
				ps.setInt(++i, 1);

				ps.setLong(++i, 0);//上一个机构id
				if(expressDetailTemp.getIsAcceptProv() == 1){
					ps.setLong(++i, acceptBranch != null ? acceptBranch.getBranchid() : 0L);//当前机构
				} else {
					ps.setLong(++i, 0L);//当前机构
				}
				ps.setLong(++i, 0);//下一站目的机构id
				ps.setLong(++i, ((null != branch)?branch.getBranchid():0L));//配送站点ID
				ps.setString(++i, ((null != branch)?branch.getBranchname():""));//配送站点名称
				ps.setInt(++i, ((null != branch)?CwbOrderAddressCodeEditTypeEnum.DiZhiKu.getValue():CwbOrderAddressCodeEditTypeEnum.WeiPiPei.getValue()));//是否匹配状态位

				ps.setBigDecimal(++i, expressDetailTemp.getCarriage());// 费用合计
				ps.setBigDecimal(++i, BigDecimal.ZERO);
				ps.setBigDecimal(++i, BigDecimal.ZERO);
				ps.setBigDecimal(++i, expressDetailTemp.getReturnCredit());// 应退金额
				if(expressDetailTemp.getIsAcceptProv() == 1){
					ps.setInt(++i, 0);//补录完成标识，
				}else{
					ps.setInt(++i, 1);
				}
				ps.setTimestamp(++i, Timestamp.valueOf(DateTimeUtil.getNowTime()));
				ps.setString(++i, expressDetailTemp.getCnorCorpNo());
				ps.setString(++i, expressDetailTemp.getCnorCorpName());
				ps.setString(++i, expressDetailTemp.getAccountId());// 月结账号
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
				// 结算方式
				ps.setInt(++i, expressDetailTemp.getPayType());
				ps.setString(++i, MqOrderBusinessUtil.getPayTypeValue(expressDetailTemp.getPayment()) + "");// 现在支付方式 
				ps.setString(++i, expressDetailTemp.getAccountId());// 月结账号
				ps.setString(++i, expressDetailTemp.getTransportNo()); // tps运单号
				// 需要把省市区的id带出
				// 寄件人省市区街道id
				AdressVO vo = provinceDAO.getProviceByName(expressDetailTemp.getCnorProv());
				if(vo !=null){
					ps.setInt(++i, vo.getId());
				}else{
					ps.setInt(++i, 0);
				}
				vo = cityDAO.getCityByNameAndProvice(expressDetailTemp.getCnorCity(), vo);
				if(vo !=null){
					ps.setInt(++i, vo.getId());
				}else{
					ps.setInt(++i, 0);
				}
				vo = countyDAO.getCountyByNameAndCity(expressDetailTemp.getCnorRegion(), vo);
				if(vo !=null){
					ps.setInt(++i, vo.getId());
				}else{
					ps.setInt(++i, 0);
				}
				vo = townDAO.getTownByNameAndCounty(expressDetailTemp.getCnorTown(), vo);
				if(vo !=null){
					ps.setInt(++i, vo.getId());
				}else{
					ps.setInt(++i, 0);
				}
				// 收寄人省市区街道id
				vo = provinceDAO.getProviceByName(expressDetailTemp.getCneeProv());
				if(vo !=null){
					ps.setInt(++i, vo.getId());
				}else{
					ps.setInt(++i, 0);
				}
				vo = cityDAO.getCityByNameAndProvice(expressDetailTemp.getCneeCity(), vo);
				if(vo !=null){
					ps.setInt(++i, vo.getId());
				}else{
					ps.setInt(++i, 0);
				}
				vo = countyDAO.getCountyByNameAndCity(expressDetailTemp.getCneeRegion(), vo);
				if(vo !=null){
					ps.setInt(++i, vo.getId());
				}else{
					ps.setInt(++i, 0);
				}
				vo = townDAO.getTownByNameAndCounty(expressDetailTemp.getCneeTown(), vo);
				if(vo !=null){
					ps.setInt(++i, vo.getId());
				}else{
					ps.setInt(++i, 0);
				}
				ps.setInt(++i, expressDetailTemp.getOrderSource());// 订单来源
				if(expressDetailTemp.getIsAcceptProv() == 1){
					//如果是接收省份，则填写 inputdatetime
					ps.setTimestamp(++i, Timestamp.valueOf(DateTimeUtil.getNowTime()));
				}else{
					ps.setNull(++i, Types.TIMESTAMP);
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
	 * 更新临时表中的特定字段
	 * @param tpsTransId
	 */
	public void updateExpressDetailTemp(final ExpressDetailTemp expressDetailTemp, final Branch branch, final Branch acceptBranch, final User user) {
	    final StringBuffer sql = new StringBuffer();
		sql.append(" update express_ops_cwb_detail set ");
		sql.append(" transcwb=?,collectorid=?,collectorname=?,senderprovince=?,sendercity=?,");//sendercustomcode,
		sql.append(" sendercounty=?,senderstreet=?,sendercellphone=?,sendertelephone=?,senderaddress=?,");
		sql.append(" sendername=?,consigneename=?,cwbprovince=?,cwbcity=?,cwbcounty=?,");
		sql.append(" recstreet=?,consigneeaddress=?,consigneemobile=?,consigneephone=?,cwbremark=?,");
		sql.append(" receivablefee=?,shouldfare=?,sendnum=?,hascod=?,realweight=?,");
		sql.append(" chargeweight=?,announcedvalue=?,insuredfee=?,paywayid=?,length=?,");
		sql.append(" width=?,height=?,cwbordertypeid=?,orderflowid=?,inputdatetime=?,");
		sql.append(" cargovolume=?,cwbstate=?,instationid=?,instationname=?,state=?,");
		sql.append(" deliverybranchid=?,excelbranch=?,addresscodeedittype=?,totalfee=?,fnorgoffset=?,");
		sql.append(" infactfare=?,paybackfee=?,isadditionflag=?,credate=?,cnor_corp_no=?,");
		sql.append(" cnor_corp_name=?,account_id=?,packagefee=?,express_image=?,cnee_corp_name=?,");
		sql.append(" express_product_type=?,hasinsurance=?,paymethod=?,newpaywayid=?,monthsettleno=?,");
		sql.append(" tpstranscwb=?,senderprovinceid=?,sendercityid=?,sendercountyid=?,senderstreetid=?,");
		sql.append(" recprovinceid=?,reccityid=?,reccountyid=?,recstreetid=?");
		sql.append(" where cwb=?");

		this.jdbcTemplate.update(sql.toString(), new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				int i = 0;
				ps.setString(++i, !expressDetailTemp.getCustPackNo().isEmpty()?expressDetailTemp.getCustPackNo():expressDetailTemp.getTransportNo());
				//				ps.setString(++i, cwbOrderDTO.getCustCode());
				if(expressDetailTemp.getIsAcceptProv() == 1){
					ps.setString(++i, user != null ? user.getUserid() + "":"0");// 小件员id
					ps.setString(++i, user != null ? user.getRealname() : "");// 小件员名称
				} else{
					ps.setString(++i, "0");// 小件员id
					ps.setString(++i, "");// 小件员名称
				}
				ps.setString(++i, expressDetailTemp.getCnorProv());// 寄件人省

				ps.setString(++i, expressDetailTemp.getCnorCity());// 市  5
				ps.setString(++i, expressDetailTemp.getCnorRegion());// 区
				ps.setString(++i, expressDetailTemp.getCnorTown());// 街道
				ps.setString(++i, expressDetailTemp.getCnorMobile());

				ps.setString(++i, expressDetailTemp.getCnorTel());
				ps.setString(++i, expressDetailTemp.getCnorAddr()); //10
				ps.setString(++i, expressDetailTemp.getCnorName());
				ps.setString(++i, expressDetailTemp.getCneeName());

				ps.setString(++i, expressDetailTemp.getCneeProv());// 收件人省
				ps.setString(++i, expressDetailTemp.getCneeCity());// 市
				ps.setString(++i, expressDetailTemp.getCneeRegion());// 区 15
				ps.setString(++i, expressDetailTemp.getCneeTown());// 街道

				//如果详细地址里面已经含省+市+区，则不再加入省市区
				String cneeAddr = expressDetailTemp.getCneeAddr();
				
				ps.setString(++i, cneeAddr);
				ps.setString(++i, expressDetailTemp.getCneeMobile());
				ps.setString(++i, expressDetailTemp.getCneeTel()); 
				ps.setString(++i, expressDetailTemp.getCnorRemark());//20

				ps.setBigDecimal(++i, expressDetailTemp.getCodAmount());
				ps.setBigDecimal(++i, expressDetailTemp.getFreight());// 运费
				ps.setInt(++i, expressDetailTemp.getTotalNum());
				if(expressDetailTemp.getCodAmount().compareTo(new BigDecimal(0)) > 0){
					ps.setInt(++i, 1);// 是否有代收货款
				}else{
					ps.setInt(++i, 0);
				}

				ps.setBigDecimal(++i, expressDetailTemp.getTotalWeight());  //25
				ps.setBigDecimal(++i, expressDetailTemp.getCalculateWeight());
				ps.setBigDecimal(++i, expressDetailTemp.getAssuranceValue());
				ps.setBigDecimal(++i, expressDetailTemp.getAssuranceFee());

				ps.setInt(++i, MqOrderBusinessUtil.getPayTypeValue(expressDetailTemp.getPayment()));//原支付方式
				ps.setBigDecimal(++i, expressDetailTemp.getCargoLength());// 长  30
				ps.setBigDecimal(++i, expressDetailTemp.getCargoWidth());// 宽
				ps.setBigDecimal(++i, expressDetailTemp.getCargoHeight());// 高

				ps.setLong(++i, CwbOrderTypeIdEnum.Express.getValue());
				ps.setInt(++i, FlowOrderTypeEnum.DaoRuShuJu.getValue());
				
				if(expressDetailTemp.getIsAcceptProv() == 1){
					//如果是接收省份，则填写 inputdatetime
					ps.setTimestamp(++i, Timestamp.valueOf(DateTimeUtil.getNowTime()));
				}else{
					ps.setNull(++i, Types.TIMESTAMP);
				}// 状态   35

				ps.setFloat(++i, expressDetailTemp.getTotalVolume().floatValue());
				ps.setLong(++i, CwbStateEnum.PeiShong.getValue());
				if(expressDetailTemp.getIsAcceptProv() == 1){
					ps.setInt(++i, acceptBranch != null ? (int)acceptBranch.getBranchid() : 0);
					ps.setString(++i, acceptBranch != null ? acceptBranch.getBranchname() : "");//站点
				}else{
					ps.setInt(++i, 0);
					ps.setString(++i, "");//站点
				}
				ps.setInt(++i, 1);  //40

				ps.setLong(++i, ((null != branch)?branch.getBranchid():0L));//配送站点ID
				ps.setString(++i, ((null != branch)?branch.getBranchname():""));//配送站点名称
				ps.setInt(++i, ((null != branch)?CwbOrderAddressCodeEditTypeEnum.DiZhiKu.getValue():CwbOrderAddressCodeEditTypeEnum.WeiPiPei.getValue()));//是否匹配状态位

				ps.setBigDecimal(++i, expressDetailTemp.getCarriage());// 费用合计
				ps.setBigDecimal(++i, BigDecimal.ZERO);//  45
				ps.setBigDecimal(++i, BigDecimal.ZERO);
				ps.setBigDecimal(++i, expressDetailTemp.getReturnCredit());// 应退金额  
				if(expressDetailTemp.getIsAcceptProv() == 1){
					ps.setInt(++i, 0);//补录完成标识，
				}else{
					ps.setInt(++i, 1);
				}
				ps.setTimestamp(++i, Timestamp.valueOf(DateTimeUtil.getNowTime()));
				ps.setString(++i, expressDetailTemp.getCnorCorpNo());//50
				ps.setString(++i, expressDetailTemp.getCnorCorpName());
				ps.setString(++i, expressDetailTemp.getAccountId());// 月结账号  
				ps.setBigDecimal(++i, expressDetailTemp.getPackingFee());
				ps.setString(++i, expressDetailTemp.getExpressImage());
				ps.setString(++i, expressDetailTemp.getCneeCorpName());//55
				ps.setInt(++i, expressDetailTemp.getExpressProductType());
				// 是否有保价
				if (expressDetailTemp.getAssuranceValue().compareTo(new BigDecimal(0)) > 0) {
					ps.setInt(++i, 1);
				} else {
					ps.setInt(++i, 0);
				}
				// 结算方式
				ps.setInt(++i, expressDetailTemp.getPayType());
				ps.setString(++i, MqOrderBusinessUtil.getPayTypeValue(expressDetailTemp.getPayment()) + "");// 现在支付方式 
				ps.setString(++i, expressDetailTemp.getAccountId());// 月结账号 60
				ps.setString(++i, expressDetailTemp.getTransportNo()); // tps运单号
				// 需要把省市区的id带出
				// 寄件人省市区街道id
				AdressVO vo = provinceDAO.getProviceByName(expressDetailTemp.getCnorProv());
				if(vo !=null){
					ps.setInt(++i, vo.getId());
				}else{
					ps.setInt(++i, 0);
				}
				vo = cityDAO.getCityByNameAndProvice(expressDetailTemp.getCnorCity(), vo);
				if(vo !=null){
					ps.setInt(++i, vo.getId());
				}else{
					ps.setInt(++i, 0);
				}
				vo = countyDAO.getCountyByNameAndCity(expressDetailTemp.getCnorRegion(), vo);
				if(vo !=null){
					ps.setInt(++i, vo.getId());
				}else{
					ps.setInt(++i, 0);
				}
				vo = townDAO.getTownByNameAndCounty(expressDetailTemp.getCnorTown(), vo);
				if(vo !=null){
					ps.setInt(++i, vo.getId());
				}else{
					ps.setInt(++i, 0);
				}//65
				// 收寄人省市区街道id
				vo = provinceDAO.getProviceByName(expressDetailTemp.getCneeProv());
				if(vo !=null){
					ps.setInt(++i, vo.getId());
				}else{
					ps.setInt(++i, 0);
				}
				vo = cityDAO.getCityByNameAndProvice(expressDetailTemp.getCneeCity(), vo);
				if(vo !=null){
					ps.setInt(++i, vo.getId());
				}else{
					ps.setInt(++i, 0);
				}
				vo = countyDAO.getCountyByNameAndCity(expressDetailTemp.getCneeRegion(), vo);
				if(vo !=null){
					ps.setInt(++i, vo.getId());
				}else{
					ps.setInt(++i, 0);
				}
				vo = townDAO.getTownByNameAndCounty(expressDetailTemp.getCneeTown(), vo);
				if(vo !=null){
					ps.setInt(++i, vo.getId());
				}else{
					ps.setInt(++i, 0);
				}
				ps.setString(++i, expressDetailTemp.getTransportNo()); // tps运单号
			}
		});
	}

	/**
	 * 根据 tpsTranId删除临时表记录，
	 * @param tpsTranId
	 */
	public void deleteCwbOrderExpresstemp(String cwb) {
		try {
			if(cwb.isEmpty()){
				return;
			}
			String sql = "delete from express_ops_cwb_exprss_detail_temp where cust_order_no=?";
			this.jdbcTemplate.update(sql, cwb.trim());
		}catch(Exception ex){}
	}
	
	/**
	 * 通过id查询数据
	 * @param id
	 * @return
	 */
	public ExpressDetailTemp getCwbOrderExpresstempByCwb(String transportNo) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * from express_ops_cwb_exprss_detail_temp where transport_no = ? limit 0,1");
		List<ExpressDetailTemp> list = this.jdbcTemplate.query(sql.toString(), new ExpressDetailTempMapper(), transportNo);
		if(CollectionUtils.isEmpty(list)){
			return null;
		}else {
			return list.get(0);
		}
	}

	public void updateExpressDetailTempDoCreateTime(String transportNo,
			long createTimeNew) {
		String sql= "update express_ops_cwb_exprss_detail_temp set do_create_time=? where transport_no = ?";
		this.jdbcTemplate.update(sql, new Timestamp(createTimeNew), transportNo);
	}
}
