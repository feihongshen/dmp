package cn.explink.dao.express;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Repository;

import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.User;
import cn.explink.domain.VO.express.DeliverSummary;
import cn.explink.domain.VO.express.EmbracedImportOrderVO;
import cn.explink.domain.VO.express.EmbracedOrderVO;
import cn.explink.domain.VO.express.NonEmbracedOrderVO;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.CwbStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.express.ExpressCombineTypeEnum;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.Page;
import cn.explink.util.SecurityUtil;
import cn.explink.util.StringUtil;
import cn.explink.util.Tools;

/**
 * 快递订单数据库访问操作
 *
 * @author songkaojun 2015年8月6日
 */
@Repository
public class ExpressOrderDao {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	private CwbDAO cwbDAO;
	@Autowired
	CustomerDAO customerDao;

	private final class ExpressOrderRowMapper implements RowMapper<CwbOrder> {
		@Override
		public CwbOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
			CwbOrder expressOrder = new CwbOrder();
			expressOrder.setOpscwbid(rs.getLong("opscwbid"));
			expressOrder.setCwb(rs.getString("cwb"));
			expressOrder.setSendcarnum(rs.getLong("sendcarnum"));
			expressOrder.setCollectorname(rs.getString("collectorname"));
			expressOrder.setInstationdatetime(rs.getString("instationdatetime"));
			expressOrder.setPaymethod(rs.getInt("paymethod"));
			expressOrder.setTotalfee(rs.getBigDecimal("totalfee"));
			expressOrder.setShouldfare(rs.getBigDecimal("shouldfare"));
			expressOrder.setPackagefee(rs.getBigDecimal("packagefee"));
			expressOrder.setInsuredfee(rs.getBigDecimal("insuredfee"));
			expressOrder.setNextbranchid(rs.getLong("nextbranchid"));
			// 收件人省id
			expressOrder.setRecprovinceid(rs.getInt("recprovinceid"));
			// 收件人市id
			expressOrder.setReccityid(rs.getInt("reccityid"));

			return expressOrder;
		}

	}

	/**
	 *
	 * @description 揽件补录录入界面的VO
	 * @author 刘武强
	 * @data 2015年8月10日
	 */
	private final class EmbrancedOrderInputRowMapper implements RowMapper<EmbracedOrderVO> {
		@Override
		public EmbracedOrderVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			EmbracedOrderVO embracedOrderVO = new EmbracedOrderVO();
			embracedOrderVO.setOrderNo(rs.getString("cwb"));
			embracedOrderVO.setDelivermanId(rs.getString("collectorid"));
			embracedOrderVO.setDelivermanName(StringUtil.nullConvertToEmptyString(rs.getString("collectorname")));
			embracedOrderVO.setSender_certificateNo(StringUtil.nullConvertToEmptyString(rs.getString("senderid")));

			embracedOrderVO.setSender_provinceid(rs.getInt("senderprovinceid") + "");
			embracedOrderVO.setSender_provinceName(StringUtil.nullConvertToEmptyString(rs.getString("senderprovince")));
			embracedOrderVO.setSender_cityid(rs.getInt("sendercityid") + "");
			embracedOrderVO.setSender_cityName(StringUtil.nullConvertToEmptyString(rs.getString("sendercity")));
			embracedOrderVO.setSender_countyid(rs.getInt("sendercountyid") + "");
			embracedOrderVO.setSender_countyName(StringUtil.nullConvertToEmptyString(rs.getString("sendercounty")));
			embracedOrderVO.setSender_townid(StringUtil.nullConvertToEmptyString(rs.getString("senderstreetid")));
			embracedOrderVO.setSender_townName(StringUtil.nullConvertToEmptyString(rs.getString("senderstreet")));
			embracedOrderVO.setSender_adress(StringUtil.nullConvertToEmptyString(rs.getString("senderaddress")));
			embracedOrderVO.setSender_cellphone(StringUtil.nullConvertToEmptyString(rs.getString("sendercellphone")));
			embracedOrderVO.setSender_telephone(StringUtil.nullConvertToEmptyString(rs.getString("sendertelephone")));
			embracedOrderVO.setSender_customerid(rs.getString("customerid") == null ? 0 : Long.parseLong(rs.getString("customerid")));
			embracedOrderVO.setSender_name(StringUtil.nullConvertToEmptyString(rs.getString("sendername")));

			embracedOrderVO.setConsignee_provinceid(rs.getInt("recprovinceid") + "");
			embracedOrderVO.setConsignee_provinceName(StringUtil.nullConvertToEmptyString(rs.getString("cwbprovince")));
			embracedOrderVO.setConsignee_cityid(rs.getInt("reccityid") + "");
			embracedOrderVO.setConsignee_cityName(StringUtil.nullConvertToEmptyString(rs.getString("cwbcity")));
			embracedOrderVO.setConsignee_countyid(rs.getInt("reccountyid") + "");
			embracedOrderVO.setConsignee_countyName(StringUtil.nullConvertToEmptyString(rs.getString("cwbcounty")));
			embracedOrderVO.setConsignee_townid(StringUtil.nullConvertToEmptyString(rs.getString("recstreetid")));
			embracedOrderVO.setConsignee_townName(StringUtil.nullConvertToEmptyString(rs.getString("recstreet")));
			embracedOrderVO.setConsignee_adress(StringUtil.nullConvertToEmptyString(rs.getString("consigneeaddress")));
			embracedOrderVO.setConsignee_cellphone(SecurityUtil.getInstance().decrypt(StringUtil.nullConvertToEmptyString(rs.getString("consigneemobile"))));
			embracedOrderVO.setConsignee_telephone(SecurityUtil.getInstance().decrypt(StringUtil.nullConvertToEmptyString(rs.getString("consigneephone"))));
			embracedOrderVO.setConsignee_customerid(rs.getString("reccustomerid") == null ? 0 : Long.parseLong(rs.getString("reccustomerid")));
			embracedOrderVO.setConsignee_name(StringUtil.nullConvertToEmptyString(rs.getString("consigneename")));

			embracedOrderVO.setNumber(rs.getInt("sendcarnum") + "");
			embracedOrderVO.setIsadditionflag(rs.getInt("isadditionflag") + "");
			embracedOrderVO.setDeliverid(rs.getLong("deliverid") + "");
			embracedOrderVO.setInstationid(rs.getInt("instationid"));
			embracedOrderVO.setInstationname(rs.getString("instationname"));
			embracedOrderVO.setInputdatetime(rs.getString("inputdatetime"));

			embracedOrderVO.setGoods_name(StringUtil.nullConvertToEmptyString(rs.getString("entrustname")));
			embracedOrderVO.setGoods_number(StringUtil.nullConvertToEmptyString(rs.getString("sendnum")));
			embracedOrderVO.setCharge_weight(StringUtil.nullConvertToEmptyString(rs.getString("chargeweight")));
			embracedOrderVO.setGoods_weight(StringUtil.nullConvertToEmptyString(rs.getString("carrealweight")));
			embracedOrderVO.setActual_weight(StringUtil.nullConvertToEmptyString(rs.getString("carrealweight")));
			embracedOrderVO.setGoods_other(StringUtil.nullConvertToEmptyString(rs.getString("other")));
			embracedOrderVO.setGoods_longth(StringUtil.nullConvertToEmptyString(rs.getString("length")));
			embracedOrderVO.setGoods_width(StringUtil.nullConvertToEmptyString(rs.getString("width")));
			embracedOrderVO.setGoods_height(StringUtil.nullConvertToEmptyString(rs.getString("height")));
			embracedOrderVO.setGoods_kgs(StringUtil.nullConvertToEmptyString(rs.getString("kgs")));

			embracedOrderVO.setFreight_total(StringUtil.nullConvertToEmptyString(rs.getString("totalfee")));
			embracedOrderVO.setFreight(StringUtil.nullConvertToEmptyString(rs.getString("shouldfare")));
			embracedOrderVO.setPacking_amount(StringUtil.nullConvertToEmptyString(rs.getString("packagefee")));
			embracedOrderVO.setInsured(StringUtil.nullConvertToEmptyString(rs.getString("hasinsurance")));
			embracedOrderVO.setInsured_amount(StringUtil.nullConvertToEmptyString(rs.getString("announcedvalue")));
			embracedOrderVO.setInsured_cost(StringUtil.nullConvertToEmptyString(rs.getString("insuredfee")));
			embracedOrderVO.setCollection(StringUtil.nullConvertToEmptyString(rs.getString("hascod")));
			embracedOrderVO.setCollection_amount(StringUtil.nullConvertToEmptyString(rs.getString("receivablefee")));

			embracedOrderVO.setPayment_method(rs.getInt("paymethod") + "");
			embracedOrderVO.setMonthly_account_number(StringUtil.nullConvertToEmptyString(rs.getString("monthsettleno")));
			embracedOrderVO.setRemarks(StringUtil.nullConvertToEmptyString(rs.getString("cwbremark")));

			embracedOrderVO.setFlowordertype(StringUtil.nullConvertToEmptyString(rs.getString("flowordertype")));
			embracedOrderVO.setInstationhandlerid(StringUtil.nullConvertToEmptyString(rs.getString("instationhandlerid")));
			embracedOrderVO.setInstationhandlername(StringUtil.nullConvertToEmptyString(rs.getString("instationhandlername")));
			embracedOrderVO.setInstationdatetime(StringUtil.nullConvertToEmptyString(rs.getString("instationdatetime")));
			embracedOrderVO.setInstationid(rs.getInt("instationid"));
			embracedOrderVO.setCarsize(StringUtil.nullConvertToEmptyString(rs.getString("carsize")));
			//快递二期补录新增字段 --周欢
			embracedOrderVO.setPaywayid(rs.getInt("paywayid"));
			embracedOrderVO.setExpress_product_type(rs.getInt("express_product_type"));
			return embracedOrderVO;
		}

	}

	/**
	 *
	 * @description 揽件补录未补录的VO
	 * @author 王志宇
	 * @data 2015年10月29日
	 */
	private final class ExportEmbrancedOrderRowMapper implements RowMapper<EmbracedImportOrderVO> {
		@Override
		public EmbracedImportOrderVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			EmbracedImportOrderVO embracedOrderVO = new EmbracedImportOrderVO();
			embracedOrderVO.setOrderNo(rs.getString("cwb"));
			embracedOrderVO.setDelivermanName(StringUtil.nullConvertToEmptyString(rs.getString("collectorname")));
			embracedOrderVO.setSender_companyName(ExpressOrderDao.this.customerDao.getCustomerById(rs.getLong("customerid")).getCompanyname());// 寄件人公司
			embracedOrderVO.setCharge_weight(rs.getBigDecimal("realweight") == null ? "0" : rs.getBigDecimal("realweight").toString());
			embracedOrderVO.setGoods_length("" + rs.getInt("length"));
			embracedOrderVO.setGoods_width("" + rs.getInt("width"));
			embracedOrderVO.setGoods_high("" + rs.getInt("height"));
			embracedOrderVO.setGoods_other(rs.getString("other"));
			embracedOrderVO.setInsured_value(rs.getBigDecimal("announcedvalue") == null ? "0" : rs.getBigDecimal("announcedvalue").toString());
			embracedOrderVO.setPacking_amount(rs.getBigDecimal("packagefee") == null ? "0" : rs.getBigDecimal("packagefee").toString());
			if (rs.getInt("paymethod") == 0) {
				embracedOrderVO.setYuejie(rs.getBigDecimal("shouldfare") == null ? "0" : rs.getBigDecimal("shouldfare").toString());
			} else if (rs.getInt("paymethod") == 1) {
				embracedOrderVO.setXianfu(rs.getBigDecimal("shouldfare") == null ? "0" : rs.getBigDecimal("shouldfare").toString());
			} else {
				embracedOrderVO.setDaofu(rs.getBigDecimal("shouldfare") == null ? "0" : rs.getBigDecimal("shouldfare").toString());
			}
			embracedOrderVO.setSender_provinceName(StringUtil.nullConvertToEmptyString(rs.getString("senderprovince")));
			embracedOrderVO.setSender_cityName(StringUtil.nullConvertToEmptyString(rs.getString("sendercity")));

			embracedOrderVO.setSender_countyName(StringUtil.nullConvertToEmptyString(rs.getString("sendercounty")));
			embracedOrderVO.setSender_townName(StringUtil.nullConvertToEmptyString(rs.getString("senderstreet")));
			embracedOrderVO.setSender_adress(StringUtil.nullConvertToEmptyString(rs.getString("senderaddress")));
			embracedOrderVO.setSender_cellphone(StringUtil.nullConvertToEmptyString(rs.getString("sendercellphone")));
			embracedOrderVO.setSender_telephone(StringUtil.nullConvertToEmptyString(rs.getString("sendertelephone")));

			embracedOrderVO.setConsignee_provinceName(StringUtil.nullConvertToEmptyString(rs.getString("cwbprovince")));

			embracedOrderVO.setConsignee_cityName(StringUtil.nullConvertToEmptyString(rs.getString("cwbcity")));

			embracedOrderVO.setConsignee_countyName(StringUtil.nullConvertToEmptyString(rs.getString("cwbcounty")));
			embracedOrderVO.setConsignee_townName(StringUtil.nullConvertToEmptyString(rs.getString("recstreet")));
			embracedOrderVO.setConsignee_adress(StringUtil.nullConvertToEmptyString(rs.getString("consigneeaddress")));
			embracedOrderVO.setConsignee_cellphone(StringUtil.nullConvertToEmptyString(rs.getString("consigneemobile")));
			embracedOrderVO.setConsignee_telephone(StringUtil.nullConvertToEmptyString(rs.getString("consigneephone")));

			embracedOrderVO.setFreight(rs.getBigDecimal("shouldfare") + "");

			embracedOrderVO.setSender_name(rs.getString("sendername"));

			embracedOrderVO.setConsignee_name(rs.getString("consigneename"));
			embracedOrderVO.setGoods_name(rs.getString("entrustname"));
			embracedOrderVO.setGoods_number(rs.getString("sendnum"));

			embracedOrderVO.setCollection_amount(rs.getString("receivablefee"));
			embracedOrderVO.setInsured_cost(rs.getString("insuredfee"));
			embracedOrderVO.setActual_weight(rs.getString("carrealweight"));
			embracedOrderVO.setFreight(rs.getString("shouldfare"));
			embracedOrderVO.setPayment_method(rs.getInt("paymethod") + "");
			embracedOrderVO.setMonthly_account_number(rs.getString("monthsettleno"));

			return embracedOrderVO;
		}
	}

	private final class OrderIdRowMapper implements RowMapper<Integer> {
		@Override
		public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
			Integer integer = new Integer(rs.getInt("opscwbid"));
			return integer;
		}

	}

	/**
	 *
	 * @description 揽件补录未补录查询界面的VO
	 * @author 刘武强
	 * @data 2015年8月10日
	 */
	private final class NonEmbrancedInputRowMapper implements RowMapper<NonEmbracedOrderVO> {
		@Override
		public NonEmbracedOrderVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			NonEmbracedOrderVO nonEmbracedOrderVO = new NonEmbracedOrderVO();
			nonEmbracedOrderVO.setOpscwbid(rs.getLong("opscwbid"));
			nonEmbracedOrderVO.setCwb(StringUtil.nullConvertToEmptyString(rs.getString("cwb")));
			nonEmbracedOrderVO.setCollectorname(StringUtil.nullConvertToEmptyString(rs.getString("collectorname")));
			nonEmbracedOrderVO.setSendcarnum(rs.getLong("sendcarnum"));
			nonEmbracedOrderVO.setInputdatetime(rs.getString("inputdatetime"));
			return nonEmbracedOrderVO;
		}

	}

	private final class DeliverSummaryRowMapper implements RowMapper<DeliverSummary> {
		@Override
		public DeliverSummary mapRow(ResultSet rs, int rowNum) throws SQLException {
			DeliverSummary summary = new DeliverSummary();
			summary.setDeliverid(rs.getLong("collectorid"));
			summary.setOrderSum(rs.getLong("order_sum"));
			summary.setTotalfeeSum(rs.getBigDecimal("totalfee_sum") == null ? new BigDecimal(0) : rs.getBigDecimal("totalfee_sum"));
			summary.setPaymethod(rs.getInt("paymethod"));
			return summary;
		}

	}

	public List<CwbOrder> getExpressOrderByPage(long page, Integer provinceId, List<Integer> cityIdList, List<String> excludeWaybillNoList, long currentBranchid, List<ExpressCombineTypeEnum> combineTypeList) {
		StringBuffer sql = new StringBuffer("select detail.opscwbid,detail.cwb,detail.sendcarnum,detail.collectorname,detail.instationdatetime,detail.paymethod," + "detail.totalfee,detail.shouldfare,detail.packagefee,detail.insuredfee, detail.nextbranchid,detail.recprovinceid,detail.reccityid from express_ops_cwb_detail detail where ");
		this.appendNeedCombineExpressWhereSql(provinceId, cityIdList, excludeWaybillNoList, sql, currentBranchid, combineTypeList);
		sql.append(" limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER);
		return this.jdbcTemplate.query(sql.toString(), new ExpressOrderRowMapper());
	}

	private void appendNeedCombineExpressWhereSql(Integer provinceId, List<Integer> cityIdList, List<String> excludeWaybillNoList, StringBuffer sql, long currentBranchid, List<ExpressCombineTypeEnum> combineTypeList) {
		sql.append("  detail.cwbordertypeid=" + CwbOrderTypeIdEnum.Express.getValue());
		if ((provinceId != null) && (provinceId != Integer.valueOf(-1))) {
			sql.append(" and detail.recprovinceid=" + provinceId);
		}
		if ((cityIdList != null) && !cityIdList.isEmpty()) {
			sql.append(" and detail.reccityid ");
			sql.append(Tools.assembleInByList(cityIdList));
		}
		sql.append(" and detail.currentbranchid=" + currentBranchid);
		if ((combineTypeList != null) && !combineTypeList.isEmpty()) {
			List<Integer> flowOrderTypeList = new ArrayList<Integer>();
			for (ExpressCombineTypeEnum combineType : combineTypeList) {
				if (combineType.getValue().equals(ExpressCombineTypeEnum.STATION_COMBINE.getValue())) {
					flowOrderTypeList.add(FlowOrderTypeEnum.LanJianRuZhan.getValue());
				}
				if (combineType.getValue().equals(ExpressCombineTypeEnum.WAREHOUSE_COMBINE.getValue())) {
					flowOrderTypeList.add(FlowOrderTypeEnum.RuKu.getValue());
				}
			}
			sql.append(" and detail.flowordertype ");
			sql.append(Tools.assembleInByList(flowOrderTypeList));
		}
		// 未合包
		//sql.append(" and detail.cwb not in(select bale_cwb.cwb from express_ops_bale_cwb bale_cwb inner join express_ops_bale bale on bale_cwb.baleid=bale.id where bale.balestate<>7)");
		//sql.append(" and not exists (select 1 from express_ops_bale_cwb bale_cwb, express_ops_bale bale where bale_cwb.baleid=bale.id and detail.cwb = bale_cwb.cwb and bale.balestate<>7)");
		sql.append(" and not exists (select 1 from express_ops_bale_cwb bale_cwb, express_ops_bale bale where bale_cwb.baleid=bale.id and bale.branchid=" + currentBranchid + " and bale.balestate<>7 and bale_cwb.cwb=detail.cwb)");
		
		if ((excludeWaybillNoList != null) && !excludeWaybillNoList.isEmpty()) {
			sql.append(" and detail.cwb not ");
			sql.append(Tools.assembleInByList(excludeWaybillNoList));
		}
	}

	public List<DeliverSummary> getDeliverSummary(String beginDate, String endDate, Set<Long> collectoridSet) {
		StringBuffer sql = new StringBuffer("select collectorid,count(opscwbid) order_sum,sum(totalfee) totalfee_sum,paymethod from express_ops_cwb_detail");
		sql.append(" where cwbordertypeid=" + CwbOrderTypeIdEnum.Express.getValue());
		sql.append(" and state =1 ");// add by jian_xie 2016-08-08,只统计有效的单
		if ((beginDate != null) && (endDate != null)) {
			sql.append(" and instationdatetime between '" + beginDate + "' and '" + endDate + "'");
		}
		sql.append(" and collectorid ").append(Tools.assembleInByList(new ArrayList<Long>(collectoridSet)));
		sql.append(" group by collectorid,paymethod");
		return this.jdbcTemplate.query(sql.toString(), new DeliverSummaryRowMapper());
	}

	/**
	 *
	 *
	 * @param page
	 * @param excuteStateList
	 * @param delivermanId
	 * @return
	 */
	public long getExpressOrderCount(long page, Integer provinceId, List<Integer> cityIdList, List<String> excludeWaybillNoList, long currentBranchid, List<ExpressCombineTypeEnum> combineTypeList) {
		StringBuffer sql = new StringBuffer("select count(1) from express_ops_cwb_detail detail where ");
		this.appendNeedCombineExpressWhereSql(provinceId, cityIdList, excludeWaybillNoList, sql, currentBranchid, combineTypeList);
		return this.jdbcTemplate.queryForInt(sql.toString());
	}

	public boolean isExpressOrderExist(String waybillNo, long currentBranchid) {
		StringBuffer sql = new StringBuffer("select opscwbid,cwb,sendcarnum,collectorname,instationdatetime,paymethod,detail.totalfee,shouldfare,packagefee,insuredfee,nextbranchid,recprovinceid,reccityid from express_ops_cwb_detail detail where detail.cwb='" + waybillNo + "' and ");
		sql.append("  detail.cwbordertypeid=" + CwbOrderTypeIdEnum.Express.getValue());
		sql.append(" and detail.currentbranchid=" + currentBranchid);
		CwbOrder cwbOrder = null;
		try {
			cwbOrder = this.jdbcTemplate.queryForObject(sql.toString(), new ExpressOrderRowMapper());
		} catch (DataAccessException e) {
			return false;
		}
		if (cwbOrder != null) {
			return true;
		} else {
			return false;
		}
	}

	public CwbOrder getExpressOrderByWaybillNo(String waybillNo, long currentBranchid, List<ExpressCombineTypeEnum> combineTypeList) {
		StringBuffer sql = new StringBuffer("select opscwbid,cwb,sendcarnum,collectorname,instationdatetime,paymethod,detail.totalfee,shouldfare,packagefee,insuredfee,nextbranchid,recprovinceid,reccityid from express_ops_cwb_detail detail where detail.cwb='" + waybillNo + "' and ");
		this.appendNeedCombineExpressWhereSql(null, null, null, sql, currentBranchid, combineTypeList);
		CwbOrder cwbOrder = null;
		try {
			cwbOrder = this.jdbcTemplate.queryForObject(sql.toString(), new ExpressOrderRowMapper());
		} catch (DataAccessException e) {
			return null;
		}
		return cwbOrder;
	}

	/**
	 *
	 * @Title: getCwbOrderByCwb
	 * @description 根据运单号查询订单
	 * @author 刘武强
	 * @date 2015年8月8日下午5:52:46
	 * @param @param cwb
	 * @param @return
	 * @return EmbracedOrderVO
	 * @throws
	 */
	public EmbracedOrderVO getCwbOrderByCwb(String cwb, Long branchid) {
		List<EmbracedOrderVO> infolist = new ArrayList<EmbracedOrderVO>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from express_ops_cwb_detail where state=1 and instationid=" + branchid);
		sql.append(" ").append("and cwb=?");
		infolist = this.jdbcTemplate.query(sql.toString(), new EmbrancedOrderInputRowMapper(), cwb);
		return infolist.size() > 0 ? infolist.get(0) : null;
	}

	/**
	 *
	 * @Title: getCwbOrderByCwb
	 * @description 根据运单号查询订单
	 * @author 刘武强
	 * @date 2015年8月8日下午5:52:46
	 * @param @param cwb
	 * @param @return
	 * @return EmbracedOrderVO
	 * @throws
	 */
	public EmbracedOrderVO getCwbOrderByCwb(String cwb) {
		List<EmbracedOrderVO> infolist = new ArrayList<EmbracedOrderVO>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from express_ops_cwb_detail where state=1 ");
		sql.append(" ").append("and cwb=?");
		infolist = this.jdbcTemplate.query(sql.toString(), new EmbrancedOrderInputRowMapper(), cwb);
		return infolist.size() > 0 ? infolist.get(0) : null;
	}

	/**
	 *
	 * @Title: getNonExtraInputOrder
	 * @description 获取尚未补录的订单信息
	 * @author 刘武强
	 * @date 2015年8月14日下午6:46:08
	 * @param @param page
	 * @param @param pageNumber
	 * @param @param expressType
	 * @param @return
	 * @return Map<String,Object>
	 * @throws
	 */
	public Map<String, Object> getNonExtraInputOrder(long page, int pageNumber, int expressType, Long branchid) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<NonEmbracedOrderVO> list = new ArrayList<NonEmbracedOrderVO>();
		StringBuffer sql = new StringBuffer();
		StringBuffer countsql = new StringBuffer();
		int count = 0;
		// limit " + ((page - 1) * pageNumber) + " ," + pageNumber
		sql.append("select * from express_ops_cwb_detail where state=1 and isadditionflag=0 and instationid=" + branchid + "  and cwbordertypeid =" + expressType + "  limit " + ((page - 1) * pageNumber) + " ," + pageNumber);
		countsql.append("select count(opscwbid) t from express_ops_cwb_detail where state=1 and instationid=" + branchid + "  and isadditionflag=0 and cwbordertypeid =" + expressType);

		// 查询页面数据
		list = this.jdbcTemplate.query(sql.toString(), new NonEmbrancedInputRowMapper());
		// 查询数据总量--前面参数已经绑定，所以不需要再次绑定
		count = this.jdbcTemplate.queryForInt(countsql.toString());
		map.put("list", list);
		map.put("count", count);
		return map;
	}

	/**
	 *
	 * @Title: getAllNotExtraInputOrder
	 * @description 获取所有尚未补录的订单信息（不带分页）
	 * @author 王志宇
	 * @date 2015年10月29日下午14:40
	 * @param @param expressType
	 * @param @return
	 * @return List<EmbracedImportOrderVO>
	 * @throws
	 */
	public List<EmbracedImportOrderVO> getAllNotExtraInputOrder(int expressType, Long branchid) {
		List<EmbracedImportOrderVO> list = new ArrayList<EmbracedImportOrderVO>();
		StringBuffer sql = new StringBuffer();
		StringBuffer countsql = new StringBuffer();
		sql.append("select * from express_ops_cwb_detail where state=1 and isadditionflag=0 and instationid=" + branchid + " and cwbordertypeid =" + expressType);

		// 查询页面数据
		list = this.jdbcTemplate.query(sql.toString(), new ExportEmbrancedOrderRowMapper());
		return list;
	}

	/**
	 *
	 * @Title: getOrderByProvincereceivablecodbillid
	 * @description 根据跨省应收货款账单id获得订单数据
	 * @author 刘武强
	 * @date 2015年8月14日下午6:50:31
	 * @param @param provincereceivablecodbillid
	 * @param @param page
	 * @param @param pageNumber
	 * @param @return
	 * @return Map<String,Object>
	 * @throws
	 */
	public Map<String, Object> getOrderByProvincereceivablecodbillid(Long provincereceivablecodbillid, long page, int pageNumber) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<EmbracedOrderVO> list = new ArrayList<EmbracedOrderVO>();
		StringBuffer sql = new StringBuffer();
		StringBuffer countsql = new StringBuffer();
		int count = 0;
		sql.append("select * from express_ops_cwb_detail where  provincereceivablecodbillid=" + provincereceivablecodbillid + "  limit " + ((page - 1) * pageNumber) + " ," + pageNumber);
		countsql.append("select count(opscwbid) t from express_ops_cwb_detail where provincereceivablecodbillid=" + provincereceivablecodbillid);

		// 查询页面数据
		list = this.jdbcTemplate.query(sql.toString(), new EmbrancedOrderInputRowMapper());
		count = this.jdbcTemplate.queryForInt(countsql.toString());
		map.put("list", list);
		map.put("count", count);
		return map;
	}

	/**
	 *
	 * @Title: getOrderByProvincereceivablecodbillidNonfenye
	 * @description 根据跨省应收货款账单id获得订单数据(不分页)
	 * @author 刘武强
	 * @date 2015年8月18日下午4:38:25
	 * @param @param provincereceivablecodbillid
	 * @param @return
	 * @return List<Integer>
	 * @throws
	 */
	public List<Integer> getOrderByProvincereceivablecodbillidNonfenye(Long provincereceivablecodbillid) {
		List<Integer> list = new ArrayList<Integer>();
		StringBuffer sql = new StringBuffer();
		sql.append("select opscwbid from express_ops_cwb_detail where  provincereceivablecodbillid=" + provincereceivablecodbillid);
		// 查询页面数据
		list = this.jdbcTemplate.query(sql.toString(), new OrderIdRowMapper());
		return list;
	}

	/**
	 *
	 * @Title: getOrderByConditions
	 * @description 通过传入条件，获取订单信息
	 * @author 刘武强
	 * @date 2015年8月15日下午5:07:57
	 * @param @param receivableProvinceId
	 * @param @param payableProvinceId
	 * @param @param closingDate
	 * @param @param deliveryState
	 * @param @param flowOrderType
	 * @param @param page
	 * @param @param pageNumber
	 * @param @return
	 * @return Map<String,Object>
	 * @throws
	 */
	public Map<String, Object> getOrderByConditions(Integer receivableProvinceId, Integer payableProvinceId, Date closingDate, int deliveryState, int flowOrderType, int CwbOrderType, long page, int pageNumber) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<EmbracedOrderVO> list = new ArrayList<EmbracedOrderVO>();
		StringBuffer sql = new StringBuffer();
		StringBuffer countsql = new StringBuffer();
		StringBuffer monneysql = new StringBuffer();
		StringBuffer where = new StringBuffer();
		int count = 0;
		double money = 0;
		sql.append("SELECT * FROM express_ops_cwb_detail t1 LEFT JOIN express_ops_delivery_state t2 on 	t1.cwb = t2.cwb");
		countsql.append("select count(opscwbid) FROM express_ops_cwb_detail t1 LEFT JOIN express_ops_delivery_state t2 on t1.cwb = t2.cwb");
		monneysql.append("select count(receivablefee) FROM express_ops_cwb_detail t1 LEFT JOIN express_ops_delivery_state t2 on t1.cwb = t2.cwb");
		where.append(" ").append(" where hascod=1").append(" ").append("and t2.auditingtime <?").append(" ").append("and t1.senderprovinceid=?").append(" ").append("and t1.recprovinceid =?")
				.append(" ").append("and t2.deliverystate=?").append(" ").append("and t1.flowordertype=?").append(" ").append("and t1.state=1").append(" ")
				.append("and t2.state=1 and provincereceivablecodbillid=0 and t1.cwbordertypeid =" + CwbOrderType);
		sql.append(where).append(" ").append("limit " + ((page - 1) * pageNumber) + " ," + pageNumber);
		countsql.append(where);
		monneysql.append(where);
		// 查询页面数据
		list = this.jdbcTemplate.query(sql.toString(), new EmbrancedOrderInputRowMapper(), closingDate, receivableProvinceId, payableProvinceId, deliveryState, flowOrderType);
		// 查询数据总量--前面参数已经绑定，所以不需要再次绑定
		count = this.jdbcTemplate.queryForInt(countsql.toString(), closingDate, receivableProvinceId, payableProvinceId, deliveryState, flowOrderType);
		money = this.jdbcTemplate.queryForInt(monneysql.toString(), closingDate, receivableProvinceId, payableProvinceId, deliveryState, flowOrderType);

		map.put("list", list);
		map.put("count", count);
		map.put("money", money);
		return map;
	}

	/**
	 *
	 * @Title: getOrderByConditionsNonfenye
	 * @description 根据条件查询全部订单数据
	 * @author 刘武强
	 * @date 2015年8月17日上午9:15:13
	 * @param @param receivableProvinceId
	 * @param @param payableProvinceId
	 * @param @param closingDate
	 * @param @param deliveryState
	 * @param @param flowOrderType
	 * @param @param page
	 * @param @param pageNumber
	 * @param @return
	 * @return List<EmbracedOrderVO>
	 * @throws
	 */
	public List<Integer> getOrderByConditionsNonfenye(Integer receivableProvinceId, Integer payableProvinceId, Date closingDate, int deliveryState, int flowOrderType, int CwbOrderType) {
		List<Integer> list = new ArrayList<Integer>();
		StringBuffer sql = new StringBuffer();
		StringBuffer where = new StringBuffer();

		sql.append("SELECT opscwbid FROM express_ops_cwb_detail t1 LEFT JOIN express_ops_delivery_state t2 on t1.cwb = t2.cwb");
		where.append(" ").append("where hascod=1").append(" ").append("and t2.auditingtime <?").append(" ").append("and t1.senderprovinceid=?").append(" ").append("and t1.recprovinceid =?")
				.append(" ").append("and t2.deliverystate=?").append(" ").append("and t1.flowordertype=?").append(" ").append("and t1.state=1").append(" ")
				.append("and t2.state=1 and provincereceivablecodbillid=0 and t1.cwbordertypeid =" + CwbOrderType);
		// 查询页面数据
		sql.append(where);
		list = this.jdbcTemplate.query(sql.toString(), new OrderIdRowMapper(), closingDate, receivableProvinceId, payableProvinceId, deliveryState, flowOrderType);
		// 查询数据总量--前面参数已经绑定，所以不需要再次绑定
		return list;
	}

	/**
	 *
	 * @Title: importEmbracedData
	 * @description 补录导入用到的批量插入方法
	 * @author 刘武强
	 * @date 2015年10月13日上午1:47:47
	 * @param @param list
	 * @return void
	 * @throws
	 */
	public void importEmbracedData(List<EmbracedOrderVO> list, User userparam, Branch branch) {
		final User user = userparam;
		final String sql = "insert into express_ops_cwb_detail (" + "cwb," + "flowordertype," + "cwbstate," + "collectorid," + "collectorname," + "currentbranchid," + "inputdatetime," + "cwbordertypeid," + "paymethod," + "customerid," + "transcwb," + "isadditionflag," + "senderprovinceid," + "senderprovince," + "sendercityid," + "sendercity," + "sendercellphone," + "sendertelephone," + "recprovinceid," + "cwbprovince," + "reccityid," + "cwbcity," + "consigneemobile," + "consigneephone," + "sendcarnum," + "inputhandlerid," + "inputhandlername," + "sendername," + "sendercountyid," + "sendercounty," + "senderstreetid," + "senderstreet," + "consigneename," + "reccountyid," + "cwbcounty," + "entrustname," + "sendnum," + "carrealweight," + "hascod," + "receivablefee," + "hasinsurance," + "insuredfee," + "realweight," + "monthsettleno," + "senderaddress," + "consigneeaddress," + "length," + "width," + "height," + "other," + "recstreetid," + "recstreet," + "announcedvalue," + "shouldfare," + "totalfee," + "packagefee," + "chargeweight," + "recareacode," + "sendareacode," + "kgs," + "emaildateid," + "instationhandlerid," + "instationhandlername," + "instationdatetime," + "instationid," + "instationname," + "carsize," + "credate)" + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		int circleTimes = (list.size() / Tools.DB_OPERATION_MAX) + 1;
		final String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		for (int i = 0; i < circleTimes; i++) {
			int startIndex = Tools.DB_OPERATION_MAX * i;
			int endIndex = Tools.DB_OPERATION_MAX * (i + 1);
			if (endIndex > list.size()) {
				endIndex = list.size();
			}
			if (endIndex <= 0) {
				break;
			}
			final List<EmbracedOrderVO> tempList = list.subList(startIndex, endIndex);
			if ((null != tempList) && !tempList.isEmpty()) {
				this.jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						int j = 1;
						EmbracedOrderVO embracedOrder = tempList.get(i);
						
						//Added by leoliao at 2016-07-26 输出日志
						logger.info("ExpressOrderDao.importEmbracedData:cwb={}", embracedOrder.getOrderNo());
						
						ps.setString(j++, embracedOrder.getOrderNo());
						ps.setInt(j++, FlowOrderTypeEnum.LanJianRuZhan.getValue());
						ps.setInt(j++, CwbStateEnum.PeiShong.getValue());
						ps.setInt(j++, Integer.parseInt(embracedOrder.getDelivermanId()));
						ps.setString(j++, embracedOrder.getDelivermanName());
						ps.setLong(j++, user.getBranchid());
						ps.setString(j++, date); //new java.sql.Date((System.currentTimeMillis()))只有年月日，但是时间不能只为年月日，要精确到时分秒 ---刘武强20160831
						ps.setLong(j++, CwbOrderTypeIdEnum.Express.getValue());
						ps.setString(j++, embracedOrder.getPayment_method());
						ps.setLong(j++, embracedOrder.getSender_customerid() == null ? 1000 : embracedOrder.getSender_customerid());// 如果客户id为空，这默认为1000
						ps.setString(j++, embracedOrder.getOrderNo());
						ps.setInt(j++, Integer.parseInt(embracedOrder.getIsadditionflag()));
						ps.setString(j++, embracedOrder.getSender_provinceid());
						ps.setString(j++, embracedOrder.getSender_provinceName());
						ps.setString(j++, embracedOrder.getSender_cityid());
						ps.setString(j++, embracedOrder.getSender_cityName());
						ps.setString(j++, embracedOrder.getSender_cellphone());
						ps.setString(j++, embracedOrder.getSender_telephone());
						ps.setString(j++, embracedOrder.getConsignee_provinceid());
						ps.setString(j++, embracedOrder.getConsignee_provinceName());
						ps.setString(j++, embracedOrder.getConsignee_cityid());
						ps.setString(j++, embracedOrder.getConsignee_cityName());
						ps.setString(j++, embracedOrder.getConsignee_cellphone());
						ps.setString(j++, embracedOrder.getConsignee_telephone());
						ps.setString(j++, embracedOrder.getNumber());
						ps.setString(j++, user.getUserid() + "");
						ps.setString(j++, user.getUsername());
						ps.setString(j++, embracedOrder.getSender_name());
						ps.setString(j++, embracedOrder.getSender_countyid());
						ps.setString(j++, embracedOrder.getSender_countyName());
						ps.setString(j++, embracedOrder.getSender_townid());
						ps.setString(j++, embracedOrder.getSender_townName());
						ps.setString(j++, embracedOrder.getConsignee_name());
						ps.setString(j++, embracedOrder.getConsignee_countyid());
						ps.setString(j++, embracedOrder.getConsignee_countyName());
						ps.setString(j++, embracedOrder.getGoods_name());
						ps.setString(j++, embracedOrder.getGoods_number());
						ps.setString(j++, embracedOrder.getActual_weight());
						ps.setString(j++, embracedOrder.getCollection());
						ps.setString(j++, embracedOrder.getCollection_amount());
						ps.setString(j++, embracedOrder.getInsured());
						ps.setString(j++, embracedOrder.getInsured_cost());
						ps.setString(j++, embracedOrder.getActual_weight());
						ps.setString(j++, embracedOrder.getMonthly_account_number());
						ps.setString(j++, StringUtil.nullConvertToEmptyString(embracedOrder.getSender_provinceName()) + StringUtil.nullConvertToEmptyString(embracedOrder.getSender_cityName()) + StringUtil
								.nullConvertToEmptyString(embracedOrder.getSender_countyName()) + StringUtil.nullConvertToEmptyString(embracedOrder.getSender_townName()) + StringUtil.nullConvertToEmptyString(embracedOrder
								.getSender_adress()));
						ps.setString(j++, StringUtil.nullConvertToEmptyString(embracedOrder.getConsignee_provinceName()) + StringUtil.nullConvertToEmptyString(embracedOrder.getConsignee_cityName()) + StringUtil
								.nullConvertToEmptyString(embracedOrder.getConsignee_countyName()) + StringUtil.nullConvertToEmptyString(embracedOrder.getConsignee_townName()) + StringUtil.nullConvertToEmptyString(embracedOrder
								.getConsignee_adress()));
						ps.setString(j++, embracedOrder.getGoods_longth());
						ps.setString(j++, embracedOrder.getGoods_width());
						ps.setString(j++, embracedOrder.getGoods_height());
						ps.setString(j++, embracedOrder.getGoods_other());
						ps.setString(j++, embracedOrder.getConsignee_townid());
						ps.setString(j++, embracedOrder.getConsignee_townName());
						ps.setString(j++, "".equals(embracedOrder.getInsured_amount()) ? "0" : embracedOrder.getInsured_amount());
						ps.setString(j++, embracedOrder.getFreight());
						ps.setString(j++, embracedOrder.getFreight_total());
						ps.setString(j++, embracedOrder.getPacking_amount());
						ps.setString(j++, embracedOrder.getCharge_weight());
						ps.setString(j++, embracedOrder.getDestination());
						ps.setString(j++, embracedOrder.getOrigin_adress());
						ps.setString(j++, embracedOrder.getGoods_kgs());
						ps.setLong(j++, -1);

						ps.setString(j++, embracedOrder.getInstationhandlerid());
						ps.setString(j++, embracedOrder.getInstationhandlername());
						ps.setString(j++, embracedOrder.getInstationdatetime());
						ps.setString(j++, embracedOrder.getInstationid() + "");
						ps.setString(j++, embracedOrder.getInstationname());
						ps.setString(j++, StringUtil.nullConvertToEmptyString(embracedOrder.getGoods_longth()) + "CM *" + StringUtil.nullConvertToEmptyString(embracedOrder.getGoods_width()) + "CM *" + StringUtil
								.nullConvertToEmptyString(embracedOrder.getGoods_height()) + "CM");
						ps.setTimestamp(j++, Timestamp.valueOf(DateTimeUtil.getNowTime()));
					}

					@Override
					public int getBatchSize() {

						return tempList.size();
					}
				});
			}

		}

	}

	/**
	 *
	 * @Title: importEmbracedData
	 * @description 补录导入用到的批量更新方法
	 * @author 王志宇
	 * @date 2015年10月13日上午1:47:47
	 * @param @param list
	 * @return void
	 * @throws
	 */
	public void updateImportEmbracedData(List<EmbracedOrderVO> list, User userparam, Branch branch) {
		final User user = userparam;
		final String sqlUpdate = "update  express_ops_cwb_detail set cwb=?,flowordertype=?,cwbstate=?,collectorid=?,collectorname=?,cwbordertypeid=?,paymethod=?,customerid=?,transcwb=?,isadditionflag=?,senderprovinceid=?,senderprovince=?,sendercityid=?,sendercity=?,sendercellphone=?,sendertelephone=?,recprovinceid=?,cwbprovince=?,reccityid=?,cwbcity=?,consigneemobile=?,consigneephone=?,sendcarnum=?,inputhandlerid=?,inputhandlername=?,sendername=?,sendercountyid=?,sendercounty=?,senderstreetid=?,senderstreet=?,consigneename=?,reccountyid=?,cwbcounty=?,entrustname=?,sendnum=?,carrealweight=?,hascod=?,receivablefee=?,hasinsurance=?,insuredfee=?,realweight=?,monthsettleno=?,senderaddress=?,consigneeaddress=?,length=?,width=?,height=?,other=?,recstreetid=?,recstreet=?,announcedvalue=?,shouldfare=?,totalfee=?,packagefee=?,chargeweight=?,recareacode=?,sendareacode=?,kgs=?,instationhandlerid=?,instationhandlername=?,instationdatetime=?,instationid=?,instationname=?,carsize=? where cwb =? and state=1";
		int circleTimes = (list.size() / Tools.DB_OPERATION_MAX) + 1;
//		final String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		for (int i = 0; i < circleTimes; i++) {
			int startIndex = Tools.DB_OPERATION_MAX * i;
			int endIndex = Tools.DB_OPERATION_MAX * (i + 1);
			if (endIndex > list.size()) {
				endIndex = list.size();
			}
			if (endIndex <= 0) {
				break;
			}
			final List<EmbracedOrderVO> tempList = list.subList(startIndex, endIndex);
			if ((null != tempList) && !tempList.isEmpty()) {
				this.jdbcTemplate.batchUpdate(sqlUpdate, new BatchPreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						int j = 1;
						EmbracedOrderVO embracedOrder = tempList.get(i);
						
						//Added by leoliao at 2016-07-26 输出日志
						logger.info("ExpressOrderDao.updateImportEmbracedData={}", embracedOrder.getOrderNo());
						
						ps.setString(j++, embracedOrder.getOrderNo());
						ps.setString(j++, embracedOrder.getFlowordertype());
						ps.setInt(j++, CwbStateEnum.PeiShong.getValue());
						ps.setInt(j++, Integer.parseInt(embracedOrder.getDelivermanId()));
						ps.setString(j++, embracedOrder.getDelivermanName());
						//如果是更新，那么当前站点不需要更新---刘武强20160616
						// ps.setLong(j++, user.getBranchid());
						//ps.setDate(j++, new java.sql.Date((System.currentTimeMillis())));
						//ps.setString(j++, date); //new java.sql.Date((System.currentTimeMillis()))只有年月日，但是时间不能只为年月日，要精确到时分秒     // ---更新的时候，不刷新揽件入站时间--刘武强20161010
						ps.setLong(j++, CwbOrderTypeIdEnum.Express.getValue());
						ps.setString(j++, embracedOrder.getPayment_method());
						ps.setLong(j++, embracedOrder.getSender_customerid() == null ? 1000 : embracedOrder.getSender_customerid());// 如果客户id为空，这默认为1000
						ps.setString(j++, embracedOrder.getOrderNo());
						ps.setInt(j++, Integer.parseInt(embracedOrder.getIsadditionflag()));
						ps.setString(j++, embracedOrder.getSender_provinceid());
						ps.setString(j++, embracedOrder.getSender_provinceName());
						ps.setString(j++, embracedOrder.getSender_cityid());
						ps.setString(j++, embracedOrder.getSender_cityName());
						ps.setString(j++, embracedOrder.getSender_cellphone());
						ps.setString(j++, embracedOrder.getSender_telephone());
						ps.setString(j++, embracedOrder.getConsignee_provinceid());
						ps.setString(j++, embracedOrder.getConsignee_provinceName());
						ps.setString(j++, embracedOrder.getConsignee_cityid());
						ps.setString(j++, embracedOrder.getConsignee_cityName());
						ps.setString(j++, embracedOrder.getConsignee_cellphone());
						ps.setString(j++, embracedOrder.getConsignee_telephone());
						ps.setString(j++, embracedOrder.getNumber());
						ps.setString(j++, user.getUserid() + "");
						ps.setString(j++, user.getUsername());
						ps.setString(j++, embracedOrder.getSender_name());
						ps.setString(j++, embracedOrder.getSender_countyid());
						ps.setString(j++, embracedOrder.getSender_countyName());
						ps.setString(j++, embracedOrder.getSender_townid());
						ps.setString(j++, embracedOrder.getSender_townName());
						ps.setString(j++, embracedOrder.getConsignee_name());
						ps.setString(j++, embracedOrder.getConsignee_countyid());
						ps.setString(j++, embracedOrder.getConsignee_countyName());
						ps.setString(j++, embracedOrder.getGoods_name());
						ps.setString(j++, embracedOrder.getGoods_number());
						ps.setString(j++, embracedOrder.getActual_weight());
						ps.setString(j++, embracedOrder.getCollection());
						ps.setString(j++, embracedOrder.getCollection_amount());
						ps.setString(j++, embracedOrder.getInsured());
						ps.setString(j++, embracedOrder.getInsured_cost());
						ps.setString(j++, embracedOrder.getActual_weight());
						ps.setString(j++, embracedOrder.getMonthly_account_number());
						ps.setString(j++, StringUtil.nullConvertToEmptyString(embracedOrder.getSender_provinceName()) + StringUtil.nullConvertToEmptyString(embracedOrder.getSender_cityName()) + StringUtil
								.nullConvertToEmptyString(embracedOrder.getSender_countyName()) + StringUtil.nullConvertToEmptyString(embracedOrder.getSender_townName()) + embracedOrder
								.getSender_adress());
						ps.setString(j++, StringUtil.nullConvertToEmptyString(embracedOrder.getConsignee_provinceName()) + StringUtil.nullConvertToEmptyString(embracedOrder.getConsignee_cityName()) + StringUtil
								.nullConvertToEmptyString(embracedOrder.getConsignee_countyName()) + StringUtil.nullConvertToEmptyString(embracedOrder.getConsignee_townName()) + embracedOrder
								.getConsignee_adress());
						ps.setString(j++, embracedOrder.getGoods_longth());
						ps.setString(j++, embracedOrder.getGoods_width());
						ps.setString(j++, embracedOrder.getGoods_height());
						ps.setString(j++, embracedOrder.getGoods_other());
						ps.setString(j++, embracedOrder.getConsignee_townid());
						ps.setString(j++, embracedOrder.getConsignee_townName());
						ps.setString(j++, "".equals(embracedOrder.getInsured_amount()) ? "0" : embracedOrder.getInsured_amount());
						ps.setString(j++, embracedOrder.getFreight());
						ps.setString(j++, embracedOrder.getFreight_total());
						ps.setString(j++, embracedOrder.getPacking_amount());
						ps.setString(j++, embracedOrder.getCharge_weight());
						ps.setString(j++, embracedOrder.getDestination());
						ps.setString(j++, embracedOrder.getOrigin_adress());
						ps.setString(j++, embracedOrder.getGoods_kgs());
						ps.setString(j++, embracedOrder.getInstationhandlerid());
						ps.setString(j++, embracedOrder.getInstationhandlername());
						ps.setString(j++, embracedOrder.getInstationdatetime());
						ps.setString(j++, embracedOrder.getInstationid() + "");
						ps.setString(j++, embracedOrder.getInstationname());
						ps.setString(j++, StringUtil.nullConvertToEmptyString(embracedOrder.getGoods_longth()) + "CM *" + StringUtil.nullConvertToEmptyString(embracedOrder.getGoods_width()) + "CM *" + StringUtil
								.nullConvertToEmptyString(embracedOrder.getGoods_height()) + "CM");
						ps.setString(j++, embracedOrder.getOrderNo());
					}

					@Override
					public int getBatchSize() {

						return tempList.size();
					}
				});
			}

		}

	}
	
	/**
	 * 快递单导入数据已经补录完成，则更新补录完成时间
	 * 刘武强
	 * 20160831
	 * @param list
	 */
	public void updateEmbracedDataCompleteTime(List<EmbracedOrderVO> list) {
		final String sqlUpdate = "update  express_ops_cwb_detail set completedatetime=? where cwb =?";
		int circleTimes = (list.size() / Tools.DB_OPERATION_MAX) + 1;
		final String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		for (int i = 0; i < circleTimes; i++) {
			int startIndex = Tools.DB_OPERATION_MAX * i;
			int endIndex = Tools.DB_OPERATION_MAX * (i + 1);
			if (endIndex > list.size()) {
				endIndex = list.size();
			}
			if (endIndex <= 0) {
				break;
			}
			final List<EmbracedOrderVO> tempList = list.subList(startIndex, endIndex);
			if ((null != tempList) && !tempList.isEmpty()) {
				this.jdbcTemplate.batchUpdate(sqlUpdate, new BatchPreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						int j = 1;
						EmbracedOrderVO embracedOrder = tempList.get(i);
						
						//Added by liuwuqiang at 2016-08-31 输出日志
						logger.info("ExpressOrderDao.updateImportEmbracedDataCompleteTime={}", embracedOrder.getOrderNo());
												
						ps.setString(j++, date); //new java.sql.Date((System.currentTimeMillis()))只有年月日，但是时间不能只为年月日，要精确到时分秒
						ps.setString(j++, embracedOrder.getOrderNo());
					}

					@Override
					public int getBatchSize() {

						return tempList.size();
					}
				});
			}

		}

	}

	/**
	 *
	 * @Title: getOrderBycwbs
	 * @description 通过运单号（字符串），查找在字符串里面的所有的运单
	 * @author 刘武强
	 * @date 2015年10月12日下午3:32:42
	 * @param @param cwbs
	 * @param @return
	 * @return List<EmbracedOrderVO>
	 * @throws
	 */
	public List<EmbracedOrderVO> getOrderBycwbs(String cwbs) {
		StringBuffer sql = new StringBuffer();
		List<EmbracedOrderVO> list = new ArrayList<EmbracedOrderVO>();
		sql.append("select * from express_ops_cwb_detail where cwb in " + cwbs + " and state=1 ");
		list = this.jdbcTemplate.query(sql.toString(), new EmbrancedOrderInputRowMapper());
		return list;
	}
	
	
	/**
	 * 根据订单号获取运单号 add by vic.liang@pjbest.com 2016-08-05
	 * @param cwbs
	 * @return
	 */
	public List<String> getTranscwbByCwbs(String cwbs) {
		StringBuffer sql = new StringBuffer();
		sql.append("select transcwb from express_ops_transcwb where transcwb in " + cwbs);
		return this.jdbcTemplate.queryForList(sql.toString() ,String.class); 
	}

	private final class changeAddrRowMapper implements RowMapper<CwbOrder> {
		@Override
		public CwbOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
			CwbOrder expressOrder = new CwbOrder();
			expressOrder.setCwb(rs.getString("trans_no"));
			expressOrder.setSenderaddress(rs.getString("senderAddr"));
			return expressOrder;
		}

	}

	public List<CwbOrder> changeAddr() {
		String sql = "SELECT trans_no,CONCAT(temp.aa,SUBSTRING(method_params,LOCATE('\"cnorAddr\" : \"',method_params)+14,LOCATE('\"cnorName\"',method_params)-LOCATE('\"cnorAddr\" : \"',method_params)-22)) AS senderAddr FROM express_ops_tps_interface_excep JOIN (   SELECT cwb,CONCAT(IFNULL(senderprovince,''),IFNULL(sendercity,''),IFNULL(sendercounty,''),IFNULL(senderstreet,'')) AS aa FROM express_ops_cwb_detail WHERE cwbordertypeid=6) AS temp   ON trans_no=cwb WHERE operation_type=3";
		List<CwbOrder> list = new ArrayList<CwbOrder>();
		list = this.jdbcTemplate.query(sql.toString(), new changeAddrRowMapper());
		return list;
	}

	public void updateSenderAddr(String cwb, String SenderAddr) {
		String sql = "update express_ops_cwb_detail set senderaddress=? where cwb=?";
		this.jdbcTemplate.update(sql, SenderAddr, cwb);
	}
}
