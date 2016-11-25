package cn.explink.b2c.tools;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import cn.explink.controller.CwbOrderDTO;
import cn.explink.controller.MQCwbOrderDTO;
import cn.explink.domain.EmailDate;
import cn.explink.domain.User;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.EmailFinishFlagEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.StringUtil;

@Service
public class DataImportDAO_B2c {
	private Logger logger = LoggerFactory.getLogger(DataImportDAO_B2c.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class CwbDTOMapper implements RowMapper<CwbOrderDTO> {
		@Override
		public CwbOrderDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			CwbOrderDTO cwbOrder = new CwbOrderDTO();
			cwbOrder.setStartbranchid(rs.getLong("startbranchid"));
			cwbOrder.setCustomerwarehouseid(rs.getLong("customerwarehouseid"));
			cwbOrder.setEmaildate(StringUtil.nullConvertToEmptyString(rs.getString("emaildate")));
			cwbOrder.setEmaildateid(rs.getLong("emaildateid"));
			cwbOrder.setServiceareaid(rs.getLong("serviceareaid"));
			cwbOrder.setCustomerid(rs.getLong("customerid"));
			cwbOrder.setShipcwb(StringUtil.nullConvertToEmptyString(rs.getString("shipcwb")));
			cwbOrder.setConsigneeno(StringUtil.nullConvertToEmptyString(rs.getString("consigneeno")));
			cwbOrder.setConsigneename(StringUtil.nullConvertToEmptyString(rs.getString("consigneename")));
			cwbOrder.setConsigneeaddress(StringUtil.nullConvertToEmptyString(rs.getString("consigneeaddress")));
			cwbOrder.setConsigneepostcode(StringUtil.nullConvertToEmptyString(rs.getString("consigneepostcode")));
			cwbOrder.setConsigneephone(StringUtil.nullConvertToEmptyString(rs.getString("consigneephone")));
			cwbOrder.setCwbremark(StringUtil.nullConvertToEmptyString(rs.getString("cwbremark")));
			cwbOrder.setCustomercommand(StringUtil.nullConvertToEmptyString(rs.getString("customercommand")));
			cwbOrder.setTransway(StringUtil.nullConvertToEmptyString(rs.getString("transway")));
			cwbOrder.setCwbprovince(StringUtil.nullConvertToEmptyString(rs.getString("cwbprovince")));
			cwbOrder.setCwbcity(StringUtil.nullConvertToEmptyString(rs.getString("cwbcity")));
			cwbOrder.setCwbcounty(StringUtil.nullConvertToEmptyString(rs.getString("cwbcounty")));
			cwbOrder.setReceivablefee(rs.getBigDecimal("receivablefee"));
			cwbOrder.setPaybackfee(rs.getBigDecimal("paybackfee"));
			cwbOrder.setCwb(StringUtil.nullConvertToEmptyString(rs.getString("cwb")));
			cwbOrder.setShipperid(rs.getLong("shipperid"));
			cwbOrder.setCwbordertypeid(rs.getLong("cwbordertypeid"));
			cwbOrder.setConsigneemobile(StringUtil.nullConvertToEmptyString(rs.getString("consigneemobile")));
			cwbOrder.setTranscwb(StringUtil.nullConvertToEmptyString(rs.getString("transcwb")));
			cwbOrder.setDestination(StringUtil.nullConvertToEmptyString(rs.getString("destination")));
			cwbOrder.setCwbdelivertypeid(rs.getLong("cwbdelivertypeid"));
			cwbOrder.setExceldeliver(StringUtil.nullConvertToEmptyString(rs.getString("exceldeliver")));
			cwbOrder.setExceldeliverid(rs.getLong("exceldeliverid"));
			cwbOrder.setExcelbranch(StringUtil.nullConvertToEmptyString(rs.getString("excelbranch")));
			cwbOrder.setCargorealweight(rs.getBigDecimal("carrealweight"));
			cwbOrder.setModelname(rs.getString("modelname"));

			cwbOrder.setPaywayid(rs.getLong("paywayid"));
			cwbOrder.setNewpaywayid(rs.getString("newpaywayid"));
			cwbOrder.setMulti_shipcwb(rs.getString("multi_shipcwb"));
			cwbOrder.setCargovolume(rs.getBigDecimal("cargovolume"));
			cwbOrder.setConsignoraddress(rs.getString("consignoraddress"));
			cwbOrder.setCargoamount(rs.getBigDecimal("caramount"));
			cwbOrder.setSendcargoname(rs.getString("sendcarname"));
			cwbOrder.setBackcargoname(rs.getString("backcarname"));
			cwbOrder.setOpscwbid(rs.getLong("opscwbid"));
			cwbOrder.setSendcargonum(rs.getInt("sendcarnum"));
			cwbOrder.setBackcargonum(rs.getInt("backcarnum"));
			cwbOrder.setRemark1(rs.getString("remark1"));
			cwbOrder.setRemark2(rs.getString("remark2"));
			cwbOrder.setRemark3(rs.getString("remark3"));
			cwbOrder.setRemark4(rs.getString("remark4"));
			cwbOrder.setRemark5(rs.getString("remark5"));
			cwbOrder.setCargotype(rs.getString("cartype"));
			cwbOrder.setCargosize(rs.getString("carsize"));
			cwbOrder.setCommoncwb(StringUtil.nullConvertToEmptyString(rs.getString("commoncwb")));
			cwbOrder.setShouldfare(rs.getBigDecimal("shouldfare"));
			cwbOrder.setInfactfare(rs.getBigDecimal("infactfare"));
			cwbOrder.setResendtime(rs.getString("resendtime"));
			cwbOrder.setIsmpsflag(rs.getInt("ismpsflag"));
			cwbOrder.setMpsallarrivedflag(rs.getInt("mpsallarrivedflag"));
			cwbOrder.setVipclub(rs.getInt("vipclub"));
			
			return cwbOrder;
		}
	}

	private final class CwbDTO4TempMapper implements RowMapper<CwbOrderDTO> {
		@Override
		public CwbOrderDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			CwbOrderDTO cwbOrder = new CwbOrderDTO();
			cwbOrder.setStartbranchid(rs.getLong("startbranchid"));
			cwbOrder.setCustomerwarehouseid(rs.getLong("customerwarehouseid"));
			cwbOrder.setEmaildate(StringUtil.nullConvertToEmptyString(rs.getString("emaildate")));
			cwbOrder.setEmaildateid(rs.getLong("emaildateid"));
			cwbOrder.setServiceareaid(rs.getLong("serviceareaid"));
			cwbOrder.setCustomerid(rs.getLong("customerid"));
			cwbOrder.setShipcwb(StringUtil.nullConvertToEmptyString(rs.getString("shipcwb")));
			cwbOrder.setConsigneeno(StringUtil.nullConvertToEmptyString(rs.getString("consigneeno")));
			cwbOrder.setConsigneename(StringUtil.nullConvertToEmptyString(rs.getString("consigneename")));
			cwbOrder.setConsigneeaddress(StringUtil.nullConvertToEmptyString(rs.getString("consigneeaddress")));
			cwbOrder.setConsigneepostcode(StringUtil.nullConvertToEmptyString(rs.getString("consigneepostcode")));
			cwbOrder.setConsigneephone(StringUtil.nullConvertToEmptyString(rs.getString("consigneephone")));
			cwbOrder.setCwbremark(StringUtil.nullConvertToEmptyString(rs.getString("cwbremark")));
			cwbOrder.setCustomercommand(StringUtil.nullConvertToEmptyString(rs.getString("customercommand")));
			cwbOrder.setTransway(StringUtil.nullConvertToEmptyString(rs.getString("transway")));
			cwbOrder.setCwbprovince(StringUtil.nullConvertToEmptyString(rs.getString("cwbprovince")));
			cwbOrder.setCwbcity(StringUtil.nullConvertToEmptyString(rs.getString("cwbcity")));
			cwbOrder.setCwbcounty(StringUtil.nullConvertToEmptyString(rs.getString("cwbcounty")));
			cwbOrder.setReceivablefee(rs.getBigDecimal("receivablefee"));
			cwbOrder.setPaybackfee(rs.getBigDecimal("paybackfee"));
			cwbOrder.setCwb(StringUtil.nullConvertToEmptyString(rs.getString("cwb")));
			cwbOrder.setShipperid(rs.getLong("shipperid"));
			cwbOrder.setCwbordertypeid(rs.getLong("cwbordertypeid"));
			cwbOrder.setConsigneemobile(StringUtil.nullConvertToEmptyString(rs.getString("consigneemobile")));
			cwbOrder.setTranscwb(StringUtil.nullConvertToEmptyString(rs.getString("transcwb")));
			cwbOrder.setDestination(StringUtil.nullConvertToEmptyString(rs.getString("destination")));
			cwbOrder.setCwbdelivertypeid(rs.getLong("cwbdelivertypeid"));
			cwbOrder.setExceldeliver(StringUtil.nullConvertToEmptyString(rs.getString("exceldeliver")));
			cwbOrder.setExcelbranch(StringUtil.nullConvertToEmptyString(rs.getString("excelbranch")));
			cwbOrder.setCargorealweight(rs.getBigDecimal("carrealweight"));
			cwbOrder.setModelname(rs.getString("modelname"));

			cwbOrder.setPaywayid(rs.getLong("paywayid"));
			cwbOrder.setNewpaywayid(rs.getString("newpaywayid"));
			cwbOrder.setMulti_shipcwb(rs.getString("multi_shipcwb"));
			cwbOrder.setCargovolume(rs.getBigDecimal("cargovolume"));
			cwbOrder.setConsignoraddress(rs.getString("consignoraddress"));
			cwbOrder.setCargoamount(rs.getBigDecimal("caramount"));
			cwbOrder.setSendcargoname(rs.getString("sendcarname"));
			cwbOrder.setBackcargoname(rs.getString("backcarname"));
			cwbOrder.setOpscwbid(rs.getLong("opscwbid"));
			cwbOrder.setSendcargonum(rs.getInt("sendcarnum"));
			cwbOrder.setBackcargonum(rs.getInt("backcarnum"));
			cwbOrder.setRemark1(rs.getString("remark1"));
			cwbOrder.setRemark2(rs.getString("remark2"));
			cwbOrder.setRemark3(rs.getString("remark3"));
			cwbOrder.setRemark4(rs.getString("remark4"));
			cwbOrder.setRemark5(rs.getString("remark5"));
			cwbOrder.setCargotype(rs.getString("cartype"));
			cwbOrder.setCargosize(rs.getString("carsize"));
			cwbOrder.setCommoncwb(StringUtil.nullConvertToEmptyString(rs.getString("commoncwb")));
			cwbOrder.setShouldfare(rs.getBigDecimal("shouldfare"));
			cwbOrder.setInfactfare(rs.getBigDecimal("infactfare"));
			cwbOrder.setResendtime(rs.getString("resendtime"));
			cwbOrder.setGetDataFlag(rs.getLong("getDataFlag"));
			
			cwbOrder.setIsmpsflag(rs.getInt("ismpsflag"));
			cwbOrder.setMpsallarrivedflag(rs.getInt("mpsallarrivedflag"));
			cwbOrder.setVipclub(rs.getInt("vipclub"));
			cwbOrder.setTpsTranscwb(rs.getString("tpsTranscwb"));
			cwbOrder.setDoType(rs.getInt("do_type"));
			cwbOrder.setAnnouncedvalue(rs.getBigDecimal("announcedvalue"));
			cwbOrder.setOrderSource(rs.getInt("order_source"));
			cwbOrder.setExchangeflag(rs.getInt("exchange_flag"));
			cwbOrder.setExchangecwb(rs.getString("exchange_cwb"));
			return cwbOrder;
		}
	}

	/**
	 * 插入数据到临时表 然后定时器插入detail表
	 *
	 * @param cwbOrderDTO
	 * @param customerid
	 * @param branchid
	 * @param user
	 * @param ed
	 */
	public void insertCwbOrder_toTempTable(final CwbOrderDTO cwbOrderDTO, final long customerid, final long branchid, final User user, final EmailDate ed) {
		this.logger.info("导入临时表一条新的订单，订单号为{}", cwbOrderDTO.getCwb());
		this.jdbcTemplate.update(
				"insert into express_ops_cwb_detail_b2ctemp (cwb,consigneename,consigneeaddress,consigneepostcode,consigneephone,sendcarname,backcarname,receivablefee,paybackfee,carrealweight,cwbremark,"
						+ "customerid,emaildate,consigneemobile,startbranchid,exceldeliver,consigneeno,excelbranch,caramount,customercommand,cartype,carsize,backcaramount,"
						+ "destination,transway,shipperid,sendcarnum,backcarnum,excelimportuserid,cwbordertypeid,cwbdelivertypeid,customerwarehouseid,cwbprovince,"
						+ "cwbcity,cwbcounty,shipcwb,transcwb,serviceareaid,nextbranchid,orderflowid,flowordertype,emailfinishflag,commonid,modelname,emaildateid,carwarehouse,"
						+ "paywayid,newpaywayid,multi_shipcwb,cargovolume,consignoraddress,tmall_notify_id,remark1,remark2,remark3,remark4,remark5,commoncwb,shouldfare,ismpsflag,mpsallarrivedflag,vipclub,tpstranscwb,exchange_flag,exchange_cwb) "
						+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?,?,? ,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )", new PreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, cwbOrderDTO.getCwb());
						ps.setString(2, cwbOrderDTO.getConsigneename());
						ps.setString(3, cwbOrderDTO.getConsigneeaddress());
						ps.setString(4, cwbOrderDTO.getConsigneepostcode());
						ps.setString(5, cwbOrderDTO.getConsigneephone());
						ps.setString(6, cwbOrderDTO.getSendcargoname());
						ps.setString(7, cwbOrderDTO.getBackcargoname());
						ps.setFloat(8, cwbOrderDTO.getReceivablefee().floatValue());
						ps.setFloat(9, cwbOrderDTO.getPaybackfee().floatValue());
						ps.setFloat(10, cwbOrderDTO.getCargorealweight().floatValue());
						ps.setString(11, cwbOrderDTO.getCwbremark());
						ps.setLong(12, customerid);
						ps.setString(13, ed.getEmaildatetime());
						ps.setString(14, cwbOrderDTO.getConsigneemobile());
						ps.setLong(15, branchid);
						ps.setString(16, cwbOrderDTO.getExceldeliver());
						ps.setString(17, cwbOrderDTO.getConsigneeno());
						ps.setString(18, cwbOrderDTO.getExcelbranch());
						ps.setFloat(19, cwbOrderDTO.getCargoamount().floatValue());
						ps.setString(20, cwbOrderDTO.getCustomercommand());
						ps.setString(21, cwbOrderDTO.getCargotype());
						ps.setString(22, cwbOrderDTO.getCargosize());
						ps.setFloat(23, cwbOrderDTO.getBackcargoamount().floatValue());
						ps.setString(24, cwbOrderDTO.getDestination());
						ps.setString(25, cwbOrderDTO.getTransway());
						ps.setLong(26, cwbOrderDTO.getShipperid());
						ps.setInt(27, cwbOrderDTO.getSendcargonum());
						ps.setInt(28, cwbOrderDTO.getBackcargonum());
						ps.setLong(29, user.getUserid());
						ps.setLong(30, cwbOrderDTO.getCwbordertypeid());
						ps.setLong(31, cwbOrderDTO.getCwbdelivertypeid());
						ps.setLong(32, cwbOrderDTO.getCustomerwarehouseid());
						ps.setString(33, cwbOrderDTO.getCwbprovince());
						ps.setString(34, cwbOrderDTO.getCwbcity());
						ps.setString(35, cwbOrderDTO.getCwbcounty());
						ps.setString(36, cwbOrderDTO.getShipcwb());
						ps.setString(37, cwbOrderDTO.getTranscwb());
						ps.setLong(38, cwbOrderDTO.getServiceareaid());
						ps.setLong(39, cwbOrderDTO.getDeliverybranchid());
						ps.setLong(40, 0);
						ps.setInt(41, FlowOrderTypeEnum.DaoRuShuJu.getValue());
						ps.setInt(42, EmailFinishFlagEnum.WeiDaoHuo.getValue());
						ps.setLong(43, (cwbOrderDTO.getCommon() == null ? 0 : cwbOrderDTO.getCommon().getId()));
						ps.setString(44, cwbOrderDTO.getModelname());
						ps.setLong(45, ed.getEmaildateid());
						ps.setLong(46, branchid);
						ps.setLong(47, cwbOrderDTO.getPaywayid());
						ps.setString(48, cwbOrderDTO.getNewpaywayid() + "");
						ps.setString(49, cwbOrderDTO.getMulti_shipcwb());
						ps.setFloat(50, cwbOrderDTO.getCargovolume().floatValue());
						ps.setString(51, cwbOrderDTO.getConsignoraddress());
						ps.setString(52, cwbOrderDTO.getTmall_notifyid());
						ps.setString(53, cwbOrderDTO.getRemark1());
						ps.setString(54, cwbOrderDTO.getRemark2());
						ps.setString(55, cwbOrderDTO.getRemark3());
						ps.setString(56, cwbOrderDTO.getRemark4());
						ps.setString(57, cwbOrderDTO.getRemark5());
						ps.setString(58, cwbOrderDTO.getCommoncwb());
						ps.setFloat(59, cwbOrderDTO.getShouldfare().floatValue());
						ps.setInt(60, cwbOrderDTO.getIsmpsflag());
						ps.setInt(61, cwbOrderDTO.getMpsallarrivedflag());
						ps.setInt(62, cwbOrderDTO.getVipclub());
						ps.setString(63, cwbOrderDTO.getTpsTranscwb());
						ps.setInt(64, cwbOrderDTO.getExchangeflag());
						ps.setString(65, cwbOrderDTO.getExchangecwb());
					}
				});
	}

	/**
	 * 查询临时表 未插入到detail表的数据 getDataFlag表示是否插入到detail中
	 *
	 * @return
	 */
	public List<CwbOrderDTO> getCwbOrderTempByKeys(String customerids) {
		String sql = "select * from express_ops_cwb_detail_b2ctemp where customerid in (" + customerids + ") and getDataFlag=0 order by credate limit 0,2000 ";
		List<CwbOrderDTO> cwborderList = this.jdbcTemplate.query(sql, new CwbDTO4TempMapper());
		return cwborderList;
	}

	/*
	 * 本来网新增
	 */
	public CwbOrderDTO getCwbOrderTempByCwb(String cwb, String customerid) {
		String sql = "select * from express_ops_cwb_detail_b2ctemp where cwb ='" + cwb + "' and customerid='" + customerid + "' order by credate  ";
		CwbOrderDTO d = new CwbOrderDTO();
		List<CwbOrderDTO> list = this.jdbcTemplate.query(sql, new CwbDTO4TempMapper());
		if (list.size() > 0) {
			return d;
		}
		return null;
	}

	/**
	 * 修改临时表为[插入detail表]成功 getDataFlag=1表示 已经插入到detail中
	 */
	public void update_CwbDetailTempByCwb(long opscwbid) {
		try {
			this.jdbcTemplate.update("update express_ops_cwb_detail_b2ctemp set getDataFlag=" + opscwbid + " where opscwbid=" + opscwbid);

		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			this.logger.error("修改临时表数据 getDataFlag失败！opscwbid=" + opscwbid, e);
		}

	}

	/**
	 * 修改临时表为[插入detail表]成功 getDataFlag=1表示 已经插入到detail中
	 */
	public void update_CwbDetailTempByCwb(String cwb) {
		try {
			this.jdbcTemplate.update("update express_ops_cwb_detail_b2ctemp set getDataFlag=1 where cwb=" + cwb);

		} catch (DataAccessException e) {
			this.logger.error("修改临时表数据 getDataFlag失败！cwb=" + cwb, e);
		}

	}

	/**
	 * 查询 未反馈给B2C状态的数据(如：告知一号店这些数据已经下载成功)
	 *
	 * @param customerid
	 * @param MaxCount
	 * @return
	 */
	public List<CwbOrderDTO> getCwbOrderByCustomerIdAndPageCount(long customerid, long MaxCount) {
		String beforeTime = DateTimeUtil.getDateBefore(5);
		String sql = "select * from express_ops_cwb_detail_b2ctemp where state=1 and customerid=" + customerid + " and isB2cSuccessFlag=0 and emaildate>='" + beforeTime + "' limit 0," + MaxCount;
		return this.jdbcTemplate.query(sql, new CwbDTO4TempMapper());
	}

	public List<CwbOrderDTO> getCwbOrderByCustomerIdsAndPageCount(String customerids, long MaxCount) {
		String beforeTime = DateTimeUtil.getDateBefore(5);
		String sql = "select * from express_ops_cwb_detail_b2ctemp where state=1" + " and customerid in (" + customerids + ")" + " and isB2cSuccessFlag=0 and emaildate>='" + beforeTime + "' limit 0,"
				+ MaxCount;
		return this.jdbcTemplate.query(sql, new CwbDTO4TempMapper());
	}
	
	/**
	 * 修改为未反馈给b2c(改成未成功)
	 * @author jian.xie
	 */
	public void updateIsB2cSuccessFlagFalseByCwbs(String cwb){
		String sql = "update express_ops_cwb_detail_b2ctemp set isB2cSuccessFlag=0 where cwb =? and state=1  and isB2cSuccessFlag=1";
		this.jdbcTemplate.update(sql, cwb);
	}

	/**
	 * 修改为已 反馈给B2C（已经成功）
	 *
	 * @param cwbs
	 */
	public void updateIsB2cSuccessFlagByCwbs(String cwb) {
		String sql = "update express_ops_cwb_detail_b2ctemp set isB2cSuccessFlag=1 where cwb =? and state=1  and isB2cSuccessFlag=0";
		this.jdbcTemplate.update(sql, cwb);
	}

	/**
	 * 修改为已 反馈给B2C（已经成功）
	 *
	 * @param cwbs
	 */
	public void updateIsB2cSuccessFlagByIds(String opscwbis) {
		String sql = "update express_ops_cwb_detail_b2ctemp set isB2cSuccessFlag=1 where opscwbid in (" + opscwbis + ") and state=1  and isB2cSuccessFlag=0";
		this.jdbcTemplate.update(sql);
	}

	public void updateCarrealweightAndTranscwb(String cwb, float carrealweight, String transcwb) {
		String sql = "update express_ops_cwb_detail_b2ctemp set carrealweight=carrealweight+" + carrealweight
				+ ",sendcarnum=sendcarnum+1,transcwb=CONCAT((CASE  WHEN transcwb IS NULL THEN  '' ELSE CONCAT(transcwb,',')  END),'" + transcwb + "')  where cwb =? and state=1  ";
		this.jdbcTemplate.update(sql, cwb);
		String sql2 = "update express_ops_cwb_detail set carrealweight=carrealweight+" + carrealweight
				+ " ,sendcarnum=sendcarnum+1,transcwb=CONCAT((CASE  WHEN transcwb IS NULL THEN  '' ELSE CONCAT(transcwb,',')  END),'" + transcwb + "')  where cwb =? and state=1  ";
		this.jdbcTemplate.update(sql2, cwb);

	}

	public void updateCarrealweightAndTranscwb(String cwb) {
		String sql = "update express_ops_cwb_detail_b2ctemp set sendcarnum=0 ,transcwb='',carrealweight=0 where cwb =? and state=1  ";
		this.jdbcTemplate.update(sql, cwb);
		String sql2 = "update express_ops_cwb_detail set sendcarnum=0 ,transcwb='',carrealweight=0 where cwb =? and state=1  ";
		this.jdbcTemplate.update(sql2, cwb);

	}

	public void updateCwbAndTranscwb(String transcwb, String cwb, String history) {
		String sql = "update express_ops_cwb_detail_b2ctemp set cwb=?,cwbremark=?,state=1 where transcwb =? ";
		this.jdbcTemplate.update(sql, cwb, history, transcwb);
		String sql2 = "update express_ops_cwb_detail set cwb=?,cwbremark=? ,state=1 where transcwb =?";
		this.jdbcTemplate.update(sql2, cwb, history, transcwb);

	}

	public void DeleteCwbAndTranscwbtob2ctemp(String cwb) {
		String sql = "delete from express_ops_cwb_detail_b2ctemp  where cwb ='" + cwb + "'";
		this.jdbcTemplate.update(sql);

	}

	public void DeleteCwbAndTranscwbtodetail(String cwb) {
		String sql = "update  express_ops_cwb_detail set state=0 where cwb =?";
		this.jdbcTemplate.update(sql, cwb);
	}

	public CwbOrderDTO getCwbByCwbB2ctemp(String cwb) {
		try {
			return this.jdbcTemplate.queryForObject("SELECT * from express_ops_cwb_detail_b2ctemp where cwb=? and state=1 limit 0,1", new CwbDTO4TempMapper(), cwb);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	/**
	 * 返回临时表数据
	 * @param cwb
	 * @return
	 */
	public CwbOrderDTO getCwbB2ctempByCwb(String cwb) {
		try {
			return this.jdbcTemplate.queryForObject("SELECT * from express_ops_cwb_detail_b2ctemp where cwb=? limit 0,1", new CwbDTO4TempMapper(), cwb);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public CwbOrderDTO getTranscwbtemp(String cwb) {
		try {
			return this.jdbcTemplate.queryForObject("SELECT * FROM express_ops_cwb_detail WHERE flowordertype=1 and state=1 and cwb=?  ", new CwbDTOMapper(), cwb);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public void updateRemark5Bycwb(String cwb, String remark5, String consigneMobile) {
		String sql = "update express_ops_cwb_detail set remark5=?,consigneeMobile=?  where cwb =? ";
		this.jdbcTemplate.update(sql, remark5, consigneMobile, cwb);
	}

	// ===============快乐购临时表新增===================
	/**
	 * 查询关联快乐购的订单
	 */
	public CwbOrderDTO getbranchByCwbandtranscwb(long cwb, String transcwb) {
		try {
			return this.jdbcTemplate.queryForObject("SELECT * from express_ops_cwb_detail_toHappyGo where cwb=? and commoncwb=? and state=1 and cwbordertypeid=3 limit 0,1", new CwbDTOMapper(), cwb,
					transcwb);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 插入数据到临时表 然后定时器插入detail表
	 *
	 * @param cwbOrderDTO
	 * @param customerid
	 * @param branchid
	 * @param user
	 * @param ed
	 */
	public void inserHappyGo(final CwbOrderDTO cwbOrderDTO, final long customerid, final long branchid, final User user, final EmailDate ed) {
		this.logger.info("导入快乐购临时表一条新的订单，订单号为{}", cwbOrderDTO.getCwb());
		this.jdbcTemplate.update(
				"insert into express_ops_cwb_detail_toHappyGo (cwb,consigneename,consigneeaddress,consigneepostcode,consigneephone,sendcarname,backcarname,receivablefee,paybackfee,carrealweight,cwbremark,"
						+ "customerid,emaildate,consigneemobile,startbranchid,exceldeliver,consigneeno,excelbranch,caramount,customercommand,cartype,carsize,backcaramount,"
						+ "destination,transcwb,shipperid,sendcarnum,backcarnum,excelimportuserid,cwbordertypeid,cwbdelivertypeid,customerwarehouseid,cwbprovince,"
						+ "cwbcity,cwbcounty,shipcwb,commoncwb,serviceareaid,nextbranchid,orderflowid,flowordertype,emailfinishflag,commonid,modelname,emaildateid,carwarehouse,"
						+ "paywayid,newpaywayid,isaudit,cargovolume,consignoraddress,tmall_notify_id,remark1,remark2,remark3,remark4,remark5) "
						+ "values(?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?,?,? ,?,?,?,?,?,?,? )", new PreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, cwbOrderDTO.getCwb());
						ps.setString(2, cwbOrderDTO.getConsigneename());
						ps.setString(3, cwbOrderDTO.getConsigneeaddress());
						ps.setString(4, cwbOrderDTO.getConsigneepostcode());
						ps.setString(5, cwbOrderDTO.getConsigneephone());
						ps.setString(6, cwbOrderDTO.getSendcargoname());
						ps.setString(7, cwbOrderDTO.getBackcargoname());
						ps.setFloat(8, cwbOrderDTO.getReceivablefee().floatValue());
						ps.setFloat(9, cwbOrderDTO.getPaybackfee().floatValue());
						ps.setFloat(10, cwbOrderDTO.getCargorealweight().floatValue());
						ps.setString(11, cwbOrderDTO.getCwbremark());
						ps.setLong(12, customerid);
						ps.setString(13, ed.getEmaildatetime());
						ps.setString(14, cwbOrderDTO.getConsigneemobile());
						ps.setLong(15, branchid);
						ps.setString(16, cwbOrderDTO.getExceldeliver());
						ps.setString(17, cwbOrderDTO.getConsigneeno());
						ps.setString(18, cwbOrderDTO.getExcelbranch());
						ps.setFloat(19, cwbOrderDTO.getCargoamount().floatValue());
						ps.setString(20, cwbOrderDTO.getCustomercommand());
						ps.setString(21, cwbOrderDTO.getCargotype());
						ps.setString(22, cwbOrderDTO.getCargosize());
						ps.setFloat(23, cwbOrderDTO.getBackcargoamount().floatValue());
						ps.setString(24, cwbOrderDTO.getDestination());
						ps.setString(25, cwbOrderDTO.getTranscwb());
						ps.setLong(26, cwbOrderDTO.getShipperid());
						ps.setInt(27, cwbOrderDTO.getSendcargonum());
						ps.setInt(28, cwbOrderDTO.getBackcargonum());
						ps.setLong(29, user.getUserid());
						ps.setLong(30, cwbOrderDTO.getCwbordertypeid());
						ps.setLong(31, cwbOrderDTO.getCwbdelivertypeid());
						ps.setLong(32, cwbOrderDTO.getCustomerwarehouseid());
						ps.setString(33, cwbOrderDTO.getCwbprovince());
						ps.setString(34, cwbOrderDTO.getCwbcity());
						ps.setString(35, cwbOrderDTO.getCwbcounty());
						ps.setString(36, cwbOrderDTO.getShipcwb());
						ps.setString(37, cwbOrderDTO.getCommoncwb());
						ps.setLong(38, cwbOrderDTO.getServiceareaid());
						ps.setLong(39, cwbOrderDTO.getDeliverybranchid());
						ps.setLong(40, 0);
						ps.setInt(41, FlowOrderTypeEnum.DaoRuShuJu.getValue());
						ps.setInt(42, EmailFinishFlagEnum.WeiDaoHuo.getValue());
						ps.setLong(43, (cwbOrderDTO.getCommon() == null ? 0 : cwbOrderDTO.getCommon().getId()));
						ps.setString(44, cwbOrderDTO.getModelname());
						ps.setLong(45, ed.getEmaildateid());
						ps.setLong(46, branchid);
						ps.setLong(47, cwbOrderDTO.getPaywayid());
						ps.setString(48, cwbOrderDTO.getNewpaywayid() + "");
						ps.setLong(49, cwbOrderDTO.getIsaudit());
						ps.setFloat(50, cwbOrderDTO.getCargovolume().floatValue());
						ps.setString(51, cwbOrderDTO.getConsignoraddress());
						ps.setString(52, cwbOrderDTO.getTmall_notifyid());
						ps.setString(53, cwbOrderDTO.getRemark1());
						ps.setString(54, cwbOrderDTO.getRemark2());
						ps.setString(55, cwbOrderDTO.getRemark3());
						ps.setString(56, cwbOrderDTO.getRemark4());
						ps.setString(57, cwbOrderDTO.getRemark5());
					}
				});
	}

	/**
	 * 关联 两个换货订单（）
	 *
	 * @param cwbs
	 */
	public void updateTransCwbByCwbs(String transcwb, String commoncwb) {
		String sql = "update express_ops_cwb_detail_toHappyGo set transcwb=? ,isaudit=2 where commoncwb =? and state=1  and isB2cSuccessFlag=0";
		this.jdbcTemplate.update(sql, transcwb, commoncwb);
	}

	/**
	 * 查询快乐购临时表 未插入到detail表的数据 getDataFlag表示是否插入到detail中
	 *
	 * @return
	 */
	public List<CwbOrderDTO> getHappyGoByKeys(String customerids) {
		String sql = "select * from express_ops_cwb_detail_toHappyGo where customerid in (" + customerids + ") and getDataFlag=0 and isaudit=2 order by credate limit 0,2000 ";
		List<CwbOrderDTO> cwborderList = this.jdbcTemplate.query(sql, new CwbDTOMapper());
		return cwborderList;
	}

	public CwbOrderDTO getCwbByHappyCwbB2ctemp(String cwb) {
		try {
			return this.jdbcTemplate.queryForObject("SELECT * from express_ops_cwb_detail_toHappyGo where cwb=? and state=1 limit 0,1", new CwbDTOMapper(), cwb);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<CwbOrderDTO> getHappyGoInfoByIsaudit(String customerids, String isaudit) {
		String sql = "select * from express_ops_cwb_detail_toHappyGo where customerid in (" + customerids + ") and getDataFlag=0 and isaudit=1 and cwbordertypeid=3 order by credate limit 0,2000 ";
		List<CwbOrderDTO> cwborderList = this.jdbcTemplate.query(sql, new CwbDTOMapper());
		return cwborderList;
	}

	public List<CwbOrderDTO> getHappyGoDetailByIsaudit(String commoncwb) {
		String sql = "select * from express_ops_cwb_detail_toHappyGo where  getDataFlag=0 and isaudit=1 and cwbordertypeid=3 and commoncwb=? order by credate limit 0,2000 ";
		List<CwbOrderDTO> cwborderList = this.jdbcTemplate.query(sql, new CwbDTOMapper(), commoncwb);
		return cwborderList;
	}

	/**
	 * 修改临时表为[插入detail表]成功 getDataFlag=1表示 已经插入到detail中
	 */
	public void updateHappyGoByCwb(long opscwbid) {
		try {
			this.jdbcTemplate.update("update express_ops_cwb_detail_toHappyGo set getDataFlag=" + opscwbid + " where opscwbid=" + opscwbid);

		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			this.logger.error("修改临时表数据 getDataFlag失败！opscwbid=" + opscwbid, e);
		}

	}

	public void updateHappyGoDetailByIsaudit(String transcwb, String cwb) {
		String sql = "update express_ops_cwb_detail_toHappyGo set transcwb=" + transcwb + " , isaudit=2 where cwb=" + cwb;
		this.jdbcTemplate.update(sql);
	}

	public void updateIsaudit(String cwb) {
		String sql = "update express_ops_cwb_detail_toHappyGo set  isaudit=3 where cwb=" + cwb;
		this.jdbcTemplate.update(sql);
	}

	// ===============快乐购临时表新增结束===================

	// 根据订单号失效
	public void dataLoseB2ctempByCwb(String cwb) {
		this.jdbcTemplate.update("update express_ops_cwb_detail_b2ctemp set state=0  where state =1 and cwb=? ", cwb);
	}

	/**
	 * 订单信息修改
	 */
	public CwbOrderDTO getCwbFromCwborder(String cwb) {
		try {
			return this.jdbcTemplate.queryForObject("SELECT * from express_ops_cwb_detail where cwb=? and state=1 limit 0,1", new CwbDTOMapper(), cwb);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public void updateBycwb(final Map<String, String> order) {
		String sql = "update express_ops_cwb_detail_b2ctemp set consigneename=? ,sendcarnum=?,consigneemobile=?,consigneephone=?,consigneepostcode=?,"
				+ "consigneeaddress=?,receivablefee=?,customercommand=?,remark1=?,remark2=?,remark3=?,remark4=?,remark5=?,carrealweight=?,paywayid=?," + "cartype=?,cwbordertypeid=?,shouldfare=? "
				+ " where cwb =? and state=1  ";
		this.jdbcTemplate.update(sql, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, order.get("consigneename").toString());
				ps.setString(2, order.get("sendcarnum").toString());
				ps.setString(3, order.get("consigneemobile").toString());
				ps.setString(4, order.get("consigneephone").toString());
				ps.setString(5, order.get("consigneepostcode").toString());

				ps.setString(6, order.get("consigneeaddress").toString());
				ps.setString(7, order.get("receivablefee").toString());
				ps.setString(8, order.get("customercommand").toString());
				ps.setString(9, order.get("remark1").toString());
				ps.setString(10, order.get("remark2").toString());
				ps.setString(11, order.get("remark3").toString());
				ps.setString(12, order.get("remark4").toString());
				ps.setString(13, order.get("remark5").toString());
				ps.setString(14, order.get("cargorealweight").toString());
				ps.setString(15, order.get("paywayid").toString());

				ps.setString(16, order.get("cargotype").toString());
				ps.setString(17, order.get("cwbordertypeid").toString());
				ps.setString(18, order.get("shouldfare").toString());
				ps.setString(19, order.get("cwb").toString());
			}
		});

	}

	/**
	 * 获取未反馈给TPS的OXOJIT订单号
	 *
	 * @param count
	 *            获取的条数
	 * @param customerid
	 *            客户id
	 * @return
	 */
	public List<String> getUnfeedbackOXOJITOrders(int count, long customerid) {
		String sql = "select transcwb from express_ops_cwb_detail_b2ctemp where customerid=? and cwbordertypeid=? and getDataFlag<>0 and oxojitfeedbackflag=0 and state=1 order by credate limit ?";
		return this.jdbcTemplate.query(sql, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("transcwb");
			}

		}, customerid, CwbOrderTypeIdEnum.OXO_JIT.getValue(), count);

	}

	/**
	 * 批量更新oxojitfeedbackflag的值为1
	 *
	 * @param cwbs
	 * @param customerid
	 */
	public void updateOXOJITfeedbackflag(String transcwbs, long customerid) {
		String sql = "update express_ops_cwb_detail_b2ctemp set oxojitfeedbackflag=1 where transcwb in(" + transcwbs + ") and customerid=? and cwbordertypeid=? and state=1";
		this.jdbcTemplate.update(sql, customerid, CwbOrderTypeIdEnum.OXO_JIT.getValue());

	}

	public List<CwbOrderDTO> getCwbOrderTempByKeysExtends(String customerids, String cwbordertypeids) {
		String sql = "select * from express_ops_cwb_detail_b2ctemp where state=1 and customerid in (" + customerids + ") and getDataFlag=0 and cwbordertypeid in (" + cwbordertypeids
				+ ") order by credate limit 0,500 ";
		List<CwbOrderDTO> cwborderList = this.jdbcTemplate.query(sql, new CwbDTO4TempMapper());
		return cwborderList;
	}
	/**
	 * 根据remark4来查询【苏宁易购】业务
	 */
	public CwbOrderDTO getCwbByremark4(String remark4) {
		try {
			return jdbcTemplate.queryForObject("SELECT * from express_ops_cwb_detail_b2ctemp where remark4=? and state=1 limit 0,1", new CwbDTOMapper(), remark4);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	/**
	 * 【玫琳凯】业务需要
	 * @param cwb
	 */
	public void updateGetdataflagByCWB(String cwb) {
		String sql = "update express_ops_cwb_detail_b2ctemp set getDataFlag=1 where cwb=?";
		this.jdbcTemplate.update(sql,cwb);
	}
	
	public void updateTmsPackageCondition(String cwb, String  transcwb,int sendcarnum,int mpsallarrivedflag,int ismpsflag) {
		this.jdbcTemplate.update("update express_ops_cwb_detail_b2ctemp set transcwb=?,sendcarnum=?,mpsallarrivedflag=?,ismpsflag=? where cwb=? and state = 1  ", transcwb,sendcarnum,mpsallarrivedflag,ismpsflag,cwb);
	}
	
	/**
	 * 获取临时表中未转业务的记录id
	 * @author leo01.liao
	 * @param customerids
	 * @param cwbordertypeids
	 * @param maxCount
	 * @return
	 */
	public List<Long> getCwbOrderTempOpscwbidByKeysExtends(String customerids, String cwbordertypeids, int maxCount) {
		if(maxCount <= 0){
			maxCount = 2000;
		}
		
		String sql = "select opscwbid from express_ops_cwb_detail_b2ctemp where state=1 and customerid in (" + customerids + ") " +
					 " and getDataFlag=0 and cwbordertypeid in (" + cwbordertypeids + ") order by credate limit 0, " + maxCount;
		return this.jdbcTemplate.queryForList(sql, Long.class);
	}
	
	/**
	 * 使用行级锁以防止并发修改
	 * @author leo01.liao
	 * @param opscwbid
	 * @return
	 */
	public CwbOrderDTO getCwbByCwbB2ctempOpscwbidLock(long opscwbid) {
		try {
			//取消锁定，改为使用乐观锁方式。leoliao at 2016-03-29
			return this.jdbcTemplate.queryForObject("SELECT * from express_ops_cwb_detail_b2ctemp where opscwbid=? ", new CwbDTO4TempMapper(), opscwbid);
			//return this.jdbcTemplate.queryForObject("SELECT * from express_ops_cwb_detail_b2ctemp where opscwbid=? for update", new CwbDTO4TempMapper(), opscwbid);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	/**
	 * 使用id来进行更新以便行级锁生效
	 * @author leo01.liao
	 * @param opscwbid
	 * @param transcwb
	 * @param sendcarnum
	 * @param mpsallarrivedflag
	 * @param ismpsflag
	 */
	public void updateTmsPackageCondition(long opscwbid, String  transcwb, int sendcarnum, int mpsallarrivedflag, int ismpsflag) {
		String sql = "update express_ops_cwb_detail_b2ctemp set transcwb=?,sendcarnum=?,mpsallarrivedflag=?,ismpsflag=? where opscwbid=? ";
		this.jdbcTemplate.update(sql, transcwb, sendcarnum, mpsallarrivedflag, ismpsflag, opscwbid);
	}
	
	/**
	 * 修改临时表为[插入detail表]成功 getDataFlag != 0表示已经插入到detail中
	 * @author leo01.liao
	 * @param getDataFlag
	 * @param opscwbid
	 */
	public void update_CwbDetailTempByCwb(long getDataFlag, long opscwbid) {
		try {
			this.jdbcTemplate.update("update express_ops_cwb_detail_b2ctemp set getDataFlag=" + getDataFlag + " where opscwbid=" + opscwbid);
		} catch (DataAccessException e) {
			this.logger.error("[update_CwbDetailTempByCwb(long getDataFlag, long opscwbid)]修改临时表数据 getDataFlag失败！opscwbid=" + opscwbid, e);
		}
	}
	
	/**
	 * 修改临时表为[插入detail表]成功 getDataFlag != 0表示已经插入到detail中
	 * @author leo01.liao
	 * @param cwbOrderDto
	 */
	public void update_CwbDetailTempByCwb(CwbOrderDTO cwbOrderDto) {
		if(cwbOrderDto == null){
			return;
		}
		
		String emaildate    = cwbOrderDto.getEmaildate(); //发货时间
		long opscwbid       = cwbOrderDto.getOpscwbid();
		int  sendCarnum     = cwbOrderDto.getSendcargonum(); //发货数量
		int  mpsAllArrivedFlag = cwbOrderDto.getMpsallarrivedflag(); //是否集齐标识
		
		try {
			String sqlUpdate = "update express_ops_cwb_detail_b2ctemp set getDataFlag=" + opscwbid + 
							   " where opscwbid=" + opscwbid + " and mpsallarrivedflag=" + mpsAllArrivedFlag + 
							   " and sendcarnum=" + sendCarnum + " and emaildate='" + emaildate + "'";
			
			this.jdbcTemplate.update(sqlUpdate);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			this.logger.error("[update_CwbDetailTempByCwb(CwbOrderDTO cwbOrderDTO)]修改临时表数据 getDataFlag失败！opscwbid=" + opscwbid, e);
		}

	}
	
	/**
	 * 更新临时表的emaildate(发货时间)和getDataFlag为0(重新转业务)
	 * @param cwb
	 * @param emaildate
	 */
	public void update_CwbDetailTempEmaildateByCwb(String cwb, String emaildate) {
		this.jdbcTemplate.update("update express_ops_cwb_detail_b2ctemp set emaildate=?, getDataFlag=0 where cwb=? and state = 1 ", emaildate, cwb);
	}
	
	/**
	 * MQ订单数据插入数据到临时表 然后定时器插入detail表
	 * @param cwbOrderDTO
	 * @param customerid
	 * @param branchid
	 * @param user
	 * @param ed
	 */
	public void insertCwbOrderToTempTable(final MQCwbOrderDTO cwbOrderDTO, final long customerid, final long branchid, final User user, final EmailDate ed) {
		this.logger.info("导入临时表一条新的订单，订单号为{}", cwbOrderDTO.getCwb());
		this.jdbcTemplate.update(
				"insert into express_ops_cwb_detail_b2ctemp ("
				+ "cwb,consigneename,consigneeaddress,consigneepostcode,consigneephone,sendcarname,backcarname,receivablefee,paybackfee,carrealweight,"
				+ "cwbremark,customerid,emaildate,consigneemobile,startbranchid,exceldeliver,consigneeno,excelbranch,caramount,customercommand,"
				+ "cartype,carsize,backcaramount,destination,transway,shipperid,sendcarnum,backcarnum,excelimportuserid,cwbordertypeid,"
				+ "cwbdelivertypeid,customerwarehouseid,cwbprovince,cwbcity,cwbcounty,shipcwb,transcwb,serviceareaid,nextbranchid,orderflowid,"
				+ "flowordertype,emailfinishflag,commonid,modelname,emaildateid,carwarehouse,paywayid,newpaywayid,multi_shipcwb,cargovolume,"
				+ "consignoraddress,tmall_notify_id,remark1,remark2,remark3,remark4,remark5,commoncwb,shouldfare,ismpsflag,"
				+ "mpsallarrivedflag,vipclub,tpstranscwb,do_type,paymethod,order_source,announcedvalue) "
						+ "values("
				+ "?,?,?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,?,?, "
				+ "?,?,?,?,?,?,?,?,?,?, "
				+ "?,?,?,?,?,?,?,?,?,?, "
				+ "?,?,?,?,?,?,?,?,?,?, "
				+ "?,?,?,?,?,?,?,?,?,?, "
				+ "?,?,?,?,?,?,? )", new PreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, cwbOrderDTO.getCwb());
						ps.setString(2, cwbOrderDTO.getConsigneename());
						ps.setString(3, cwbOrderDTO.getConsigneeaddress());
						ps.setString(4, cwbOrderDTO.getConsigneepostcode());
						ps.setString(5, cwbOrderDTO.getConsigneephone());
						ps.setString(6, cwbOrderDTO.getSendcargoname());
						ps.setString(7, cwbOrderDTO.getBackcargoname());
						ps.setFloat(8, cwbOrderDTO.getReceivablefee().floatValue());
						ps.setFloat(9, cwbOrderDTO.getPaybackfee().floatValue());
						ps.setFloat(10, cwbOrderDTO.getCargorealweight().floatValue());
						ps.setString(11, cwbOrderDTO.getCwbremark());
						ps.setLong(12, customerid);
						ps.setString(13, ed.getEmaildatetime());
						ps.setString(14, cwbOrderDTO.getConsigneemobile());
						ps.setLong(15, branchid);
						ps.setString(16, cwbOrderDTO.getExceldeliver());
						ps.setString(17, cwbOrderDTO.getConsigneeno());
						ps.setString(18, cwbOrderDTO.getExcelbranch());
						ps.setFloat(19, cwbOrderDTO.getCargoamount().floatValue());
						ps.setString(20, cwbOrderDTO.getCustomercommand());
						ps.setString(21, cwbOrderDTO.getCargotype());
						ps.setString(22, cwbOrderDTO.getCargosize());
						ps.setFloat(23, cwbOrderDTO.getBackcargoamount().floatValue());
						ps.setString(24, cwbOrderDTO.getDestination());
						ps.setString(25, cwbOrderDTO.getTransway());
						ps.setLong(26, cwbOrderDTO.getShipperid());
						ps.setInt(27, cwbOrderDTO.getSendcargonum());
						ps.setInt(28, cwbOrderDTO.getBackcargonum());
						ps.setLong(29, user.getUserid());
						ps.setLong(30, cwbOrderDTO.getCwbordertypeid());
						ps.setLong(31, cwbOrderDTO.getCwbdelivertypeid());
						ps.setLong(32, cwbOrderDTO.getCustomerwarehouseid());
						ps.setString(33, cwbOrderDTO.getCwbprovince());
						ps.setString(34, cwbOrderDTO.getCwbcity());
						ps.setString(35, cwbOrderDTO.getCwbcounty());
						ps.setString(36, cwbOrderDTO.getShipcwb());
						ps.setString(37, cwbOrderDTO.getTranscwb());
						ps.setLong(38, cwbOrderDTO.getServiceareaid());
						ps.setLong(39, cwbOrderDTO.getDeliverybranchid());
						ps.setLong(40, 0);
						ps.setInt(41, FlowOrderTypeEnum.DaoRuShuJu.getValue());
						ps.setInt(42, EmailFinishFlagEnum.WeiDaoHuo.getValue());
						ps.setLong(43, (cwbOrderDTO.getCommon() == null ? 0 : cwbOrderDTO.getCommon().getId()));
						ps.setString(44, cwbOrderDTO.getModelname());
						ps.setLong(45, ed.getEmaildateid());
						ps.setLong(46, branchid);
						ps.setLong(47, cwbOrderDTO.getPaywayid());
						ps.setString(48, cwbOrderDTO.getNewpaywayid() + "");
						ps.setString(49, cwbOrderDTO.getMulti_shipcwb());
						ps.setFloat(50, cwbOrderDTO.getCargovolume().floatValue());
						ps.setString(51, cwbOrderDTO.getConsignoraddress());
						ps.setString(52, cwbOrderDTO.getTmall_notifyid());
						ps.setString(53, cwbOrderDTO.getRemark1());
						ps.setString(54, cwbOrderDTO.getRemark2());
						ps.setString(55, cwbOrderDTO.getRemark3());
						ps.setString(56, cwbOrderDTO.getRemark4());
						ps.setString(57, cwbOrderDTO.getRemark5());
						ps.setString(58, cwbOrderDTO.getCommoncwb());
						ps.setFloat(59, cwbOrderDTO.getShouldfare().floatValue());
						ps.setInt(60, cwbOrderDTO.getIsmpsflag());
						ps.setInt(61, cwbOrderDTO.getMpsallarrivedflag());
						ps.setInt(62, cwbOrderDTO.getVipclub());
						ps.setString(63, cwbOrderDTO.getTpsTranscwb());
						ps.setInt(64, cwbOrderDTO.getDoType());//订单类型
						ps.setInt(65, cwbOrderDTO.getPaymethod());//付款方式
						ps.setInt(66, cwbOrderDTO.getOrder_source());//付款方式
						ps.setFloat(67, cwbOrderDTO.getAnnouncedvalue() == null ? 0 : cwbOrderDTO.getAnnouncedvalue().floatValue());//保价价值
					}
				});
	}
	
	

	/**
	 * 使用id来进行更新以便行级锁生效--MQ下发订单新增
	 * @param opscwbid
	 * @param transcwb
	 * @param sendcarnum
	 * @param mpsallarrivedflag
	 * @param ismpsflag
	 */
	public void updateTmsCollectionInfo(long opscwbid, String  transcwb, int sendcarnum, int mpsallarrivedflag, int ismpsflag, String remark1) {
		String sql = "update express_ops_cwb_detail_b2ctemp set transcwb=?,sendcarnum=?,mpsallarrivedflag=?,ismpsflag=?,remark1=? where opscwbid=? ";
		this.jdbcTemplate.update(sql, transcwb, sendcarnum, mpsallarrivedflag, ismpsflag,remark1, opscwbid);
	}
	
	/**
	 * 修改临时表为[插入detail表]成功 getDataFlag != 0表示已经插入到detail中--MQ下发订单新增
	 * @param getDataFlag
	 * @param remark1
	 * @param opscwbid
	 */
	public void updateCwbDetailTempByCwb(long getDataFlag, long opscwbid, String remark1) {
		try {
			this.jdbcTemplate.update("update express_ops_cwb_detail_b2ctemp set getDataFlag=" + getDataFlag + ",remark1="+remark1+" where opscwbid=" + opscwbid);
		} catch (DataAccessException e) {
			this.logger.error("[update_CwbDetailTempByCwb(long getDataFlag, long opscwbid, String remark1)]修改临时表数据 getDataFlag失败！opscwbid=" + opscwbid, e);
		}
	}
	
	// 根据订单号删除临时表中对应的订单信息
	public void deleteB2ctempByCwb(String cwb) {
		this.jdbcTemplate.update("delete from express_ops_cwb_detail_b2ctemp where state =1 and cwb=? ", cwb);
	}
}
