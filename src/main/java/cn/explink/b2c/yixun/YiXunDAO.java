package cn.explink.b2c.yixun;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.domain.b2cdj.CwbOrderTemp;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.EmailFinishFlagEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.StringUtil;

@Component
public class YiXunDAO {
	private Logger logger = LoggerFactory.getLogger(YiXunService.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	CustomWareHouseDAO customWarehouseDAO;
	@Autowired
	OrderFlowDAO orderFlowDAO;

	private final class CwbTempMapper implements RowMapper<CwbOrderTemp> {
		@Override
		public CwbOrderTemp mapRow(ResultSet rs, int rowNum) throws SQLException {
			CwbOrderTemp cwbOrder = new CwbOrderTemp();
			cwbOrder.setOpscwbid(rs.getLong("opscwbid"));
			cwbOrder.setStartbranchid(rs.getLong("startbranchid"));
			cwbOrder.setNextbranchid(rs.getLong("nextbranchid"));
			cwbOrder.setBacktocustomer_awb(StringUtil.nullConvertToEmptyString(rs.getString("backtocustomer_awb")));
			cwbOrder.setCwbflowflag(StringUtil.nullConvertToEmptyString(rs.getString("cwbflowflag")));
			cwbOrder.setCarrealweight(rs.getBigDecimal("carrealweight"));
			cwbOrder.setCartype(StringUtil.nullConvertToEmptyString(rs.getString("cartype")));
			cwbOrder.setCarwarehouse(rs.getLong("carwarehouse"));
			cwbOrder.setCarsize(StringUtil.nullConvertToEmptyString(rs.getString("carsize")));
			cwbOrder.setBackcaramount(rs.getBigDecimal("backcaramount"));
			cwbOrder.setSendcarnum(rs.getLong("sendcarnum"));
			cwbOrder.setBackcarnum(rs.getLong("backcarnum"));
			cwbOrder.setCaramount(rs.getBigDecimal("caramount"));
			cwbOrder.setBackcarname(StringUtil.nullConvertToEmptyString(rs.getString("backcarname")));
			cwbOrder.setSendcarname(StringUtil.nullConvertToEmptyString(rs.getString("sendcarname")));
			cwbOrder.setDeliverid(rs.getLong("deliverid"));
			cwbOrder.setEmailfinishflag(rs.getInt("emailfinishflag"));
			cwbOrder.setReacherrorflag(rs.getInt("reacherrorflag"));
			cwbOrder.setOrderflowid(rs.getLong("orderflowid"));
			cwbOrder.setFlowordertype(rs.getLong("flowordertype"));
			cwbOrder.setCwbreachbranchid(rs.getLong("cwbreachbranchid"));
			cwbOrder.setCwbreachdeliverbranchid(rs.getLong("cwbreachdeliverbranchid"));
			cwbOrder.setPodfeetoheadflag(StringUtil.nullConvertToEmptyString(rs.getString("podfeetoheadflag")));
			cwbOrder.setPodfeetoheadtime(StringUtil.nullConvertToEmptyString(rs.getString("podfeetoheadtime")));
			cwbOrder.setPodfeetoheadchecktime(StringUtil.nullConvertToEmptyString(rs.getString("podfeetoheadchecktime")));
			cwbOrder.setPodfeetoheadcheckflag(StringUtil.nullConvertToEmptyString(rs.getString("podfeetoheadcheckflag")));
			cwbOrder.setLeavedreasonid(rs.getLong("leavedreasonid"));
			cwbOrder.setDeliversubscribeday(StringUtil.nullConvertToEmptyString(rs.getString("deliversubscribeday")));
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
			cwbOrder.setExcelimportuserid(rs.getLong("excelimportuserid"));
			cwbOrder.setState(rs.getLong("state"));
			cwbOrder.setPrinttime(rs.getString("printtime"));
			cwbOrder.setCommonid(rs.getLong("commonid"));
			cwbOrder.setCommoncwb(rs.getString("commoncwb"));
			cwbOrder.setSigntypeid(rs.getLong("signtypeid"));
			cwbOrder.setPodrealname(rs.getString("podrealname"));
			cwbOrder.setPodtime(rs.getString("podtime"));
			cwbOrder.setPodsignremark(rs.getString("podsignremark"));
			cwbOrder.setModelname(rs.getString("modelname"));
			cwbOrder.setScannum(rs.getLong("scannum"));
			cwbOrder.setIsaudit(rs.getLong("isaudit"));
			cwbOrder.setBackreason(rs.getString("backreason"));
			cwbOrder.setLeavedreason(rs.getString("leavedreason"));
			cwbOrder.setPaywayid(rs.getLong("paywayid"));
			cwbOrder.setNewpaywayid(rs.getString("newpaywayid"));
			cwbOrder.setTargetcarwarehouse(rs.getLong("targetcarwarehouse"));
			cwbOrder.setMulti_shipcwb(rs.getString("multi_shipcwb"));
			cwbOrder.setCargovolume(rs.getBigDecimal("cargovolume"));
			cwbOrder.setConsignoraddress(rs.getString("consignoraddress"));

			return cwbOrder;
		}
	}

	// 查询是否有相同的notify_id（tmall判断重复是跟踪notify_id判断的）
	public boolean isRepeatNotify_id(String customer_id, String notify_id) {
		int counts = 0;
		if (notify_id != null && !"".equals(notify_id)) {
			String sql = "insert into express_ops_cwb_detail_b2ctemp  where  tmall_notify_id=? ";
			counts = jdbcTemplate.queryForInt(sql, notify_id);
		}
		return counts > 0;
	}

	// 是否存在仓库
	public boolean isExistsWarehouFlag(Map<String, String> xmlMap, String customer_id) {
		boolean flag = false;
		String wms_code = xmlMap.get("wms_code") != null && !"".equals(xmlMap.get("wms_code").toString()) ? xmlMap.get("wms_code").toString() : "";
		if (xmlMap != null && xmlMap.size() > 0) {
			int counts = 0;
			try {
				String sql = "select count(1) from express_set_customer_warehouse where ifeffectflag=1 and customerid in (" + customer_id + ") and warehouse_no='" + wms_code + "' ";
				counts = jdbcTemplate.queryForInt(sql);
			} catch (DataAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (counts > 0) {
				flag = true;
			}
		}
		return flag;
	}

	// 是否已入库
	public boolean isIntoStorageFlag(Map<String, String> xmlMap, String customer_id) {
		boolean flag = false;
		String cwb = xmlMap.get("tms_order_code") == null ? null : xmlMap.get("tms_order_code").toString();
		if (xmlMap != null && xmlMap.size() > 0) {
			String sql = "select count(1) from express_ops_cwb_detail where state=1  and customerid in (" + customer_id + ") and cwb='" + cwb + "' and flowordertype>=4 ";
			int counts = jdbcTemplate.queryForInt(sql);
			if (counts > 0) {
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 
	 * 运单号相同，订单号不同,未入库，并且未超过24小时。
	 * 
	 * @param xmlMap
	 * @param customer_id
	 *            true 超过24小时有重复订单提示
	 * @return
	 */
	public boolean isExistsOver24HCwbInfo(String cwb, String customer_id) {
		String nowtime = DateTimeUtil.getNowTime();
		String sql = "select credate  from express_ops_cwb_detail_b2ctemp where state=1 and cwb='" + cwb + "' and flowordertype=1  ";
		Map<String, Object> map = null;
		try {
			map = jdbcTemplate.queryForMap(sql);
		} catch (DataAccessException e) {
			logger.info("订单号为：{}在数据库中不存在，不需要合单！{}", cwb, e);
			return false;
		}
		String cretime = "";
		if (map != null && map.size() > 0) {
			cretime = map.get("credate").toString();
		}
		double diffHours = DateTimeUtil.getHourFromToTime(cretime, nowtime);

		return diffHours > 24;
	}

	/**
	 * 查询是否含有合单的情况 运单号相同，订单号不同
	 * 
	 * @param xmlMap
	 * @param customer_id
	 *            true 超过24小时有重复订单提示
	 * @return
	 */
	public boolean isExistsTogetherCwbInfo(String cwb, String customer_id) {
		boolean flag = false;

		String nowtime = DateTimeUtil.getNowTime();
		String sql = "select count(1)  from express_ops_cwb_detail where state=1 and cwb='" + cwb + "' and flowordertype=1  ";
		// +" and  (TIMEDIFF('"+nowtime+"',credate)<24)>0 ";
		int counts = jdbcTemplate.queryForInt(sql);
		if (counts > 0) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 合单修改multi_shipcwb
	 * 
	 */
	public boolean update_CwbTogetherInfo(String cwb, String shipcwb) {
		boolean flag = false;
		// 追加
		if (!"".equals(shipcwb)) {
			int update = 0;
			try {
				String sql = "update express_ops_cwb_detail set multi_shipcwb=CONCAT(IF(multi_shipcwb IS NULL,'',multi_shipcwb),'," + shipcwb + "') where cwb='" + cwb + "' ";
				update = jdbcTemplate.update(sql);
				logger.info("tmall订单[一件多票]追加成功，存储系统中为普通单子");
			} catch (DataAccessException e) {
				// TODO Auto-generated catch block
				logger.info("tmall订单[一件多票]追加失败");
				e.printStackTrace();
			}
			if (update > 0) {
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 查询临时表 YIxun反馈
	 * 
	 * @return
	 */
	public List<CwbOrderTemp> getcwbOrderTempByKeys(String customerid) {
		String sql = "select * from express_ops_cwb_detail_b2ctemp where customerid=" + customerid + " and getDataFlag=0 order by credate limit 0,1000 ";
		List<CwbOrderTemp> cwborderList = jdbcTemplate.query(sql, new CwbTempMapper());
		return cwborderList;
	}

	/*
	 * public void insertCwbOrderMethod(final CwbOrderTemp cwborder) {
	 * logger.info("定时器主动插入一条新的订单，订单号为{}",cwborder.getCwb());
	 * //保存操作记录并返回对应的操作记录的id 将id保存到express_ops_cwb_detail记录中 用作双向1对1 JSONObject
	 * orderdetail = new JSONObject(); orderdetail.put("cwbOrder",
	 * JSONObject.fromObject(cwborder).toString());
	 * orderdetail.put("deliveryState", null);
	 * 
	 * // {"cwbOrder":{"opscwbid":5308,"startbranchid":0,"currentbranchid":0,
	 * "nextbranchid"
	 * :189,"deliverybranchid":0,"backtocustomer_awb":"","cwbflowflag"
	 * :"1","carrealweight"
	 * :25.000,"cartype":"家电","carwarehouse":"189","carsize":
	 * "","backcaramount":0.00
	 * ,"sendcarnum":3,"backcarnum":1,"caramount":59.30,"backcarname"
	 * :"","sendcarname"
	 * :"","deliverid":0,"deliverystate":0,"emailfinishflag":0,"reacherrorflag"
	 * :0,"orderflowid":0,"flowordertype":1,"cwbreachbranchid":0,
	 * "cwbreachdeliverbranchid"
	 * :0,"podfeetoheadflag":"0","podfeetoheadtime":null
	 * ,"podfeetoheadchecktime":
	 * null,"podfeetoheadcheckflag":"0","leavedreasonid"
	 * :0,"deliversubscribeday":
	 * null,"customerwarehouseid":"0","emaildateid":11,"emaildate"
	 * :"2013-03-01 10:26:51"
	 * ,"serviceareaid":0,"customerid":126,"shipcwb":"","consigneeno"
	 * :"","consigneename"
	 * :"杨利萍","consigneeaddress":"四川省成都市金牛区杨高南路428号由由世纪广场3号楼7楼"
	 * ,"consigneepostcode"
	 * :"","consigneephone":"","cwbremark":"","customercommand"
	 * :"","transway":"",
	 * "cwbprovince":"","cwbcity":"","cwbcounty":"","receivablefee"
	 * :25.48,"paybackfee"
	 * :0.00,"cwb":"PS99001","shipperid":0,"cwbordertypeid":1,
	 * "consigneemobile":"13263145179"
	 * ,"transcwb":"","destination":"","cwbdelivertypeid"
	 * :"1","exceldeliver":"","excelbranch"
	 * :"","excelimportuserid":1001,"state":1
	 * ,"printtime":"","commonid":0,"commoncwb"
	 * :"","signtypeid":0,"podrealname":""
	 * ,"podtime":"","podsignremark":"","modelname"
	 * :null,"scannum":0,"isaudit":0,
	 * "backreason":"","leavedreason":"","paywayid"
	 * :1,"newpaywayid":"1","tuihuoid"
	 * :189,"cwbstate":1,"remark1":"","remark2":""
	 * ,"remark3":"","remark4":"","remark5"
	 * :"","backreasonid":0,"multi_shipcwb":null
	 * ,"packagecode":"","backreturnreasonid"
	 * :0,"backreturnreason":"","handleresult"
	 * :0,"handleperson":0,"handlereason":
	 * "","addresscodeedittype":0},"deliveryState":null}
	 * 
	 * final long orderflowid = orderFlowDAO.creOrderFlow( new OrderFlow(0,
	 * cwborder.getCwb(), cwborder.getCarwarehouse(), new
	 * Timestamp(System.currentTimeMillis()), cwborder.getExcelimportuserid(),
	 * orderdetail.toString(), FlowOrderTypeEnum.DaoRuShuJu.getValue(),""));
	 * 
	 * jdbcTemplate.update(
	 * "insert into express_ops_cwb_detail (cwb,consigneename,consigneeaddress,consigneepostcode,consigneephone,sendcarname,backcarname,receivablefee,paybackfee,carrealweight,cwbremark,"
	 * +
	 * "customerid,emaildate,consigneemobile,startbranchid,exceldeliver,consigneeno,excelbranch,caramount,customercommand,cartype,carsize,backcaramount,"
	 * +
	 * "destination,transway,shipperid,sendcarnum,backcarnum,excelimportuserid,cwbordertypeid,cwbdelivertypeid,customerwarehouseid,cwbprovince,"
	 * +
	 * "cwbcity,cwbcounty,shipcwb,transcwb,serviceareaid,nextbranchid,orderflowid,flowordertype,emailfinishflag,modelname,emaildateid,carwarehouse,"
	 * + "paywayid,multi_shipcwb,cargovolume,consignoraddress) " +
	 * "values(?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?,?,? ,?,?,?,?,?,?,?,?,?)"
	 * , new PreparedStatementSetter() {
	 * 
	 * @Override public void setValues(PreparedStatement ps) throws SQLException
	 * { ps.setString(1, cwborder.getCwb()); ps.setString(2,
	 * cwborder.getConsigneename()); ps.setString(3,
	 * cwborder.getConsigneeaddress()); ps.setString(4,
	 * cwborder.getConsigneepostcode()); ps.setString(5,
	 * cwborder.getConsigneephone()); ps.setString(6,
	 * cwborder.getSendcargoname()); ps.setString(7,
	 * cwborder.getBackcargoname()); ps.setFloat(8,
	 * cwborder.getReceivablefee().floatValue()); ps.setFloat(9,
	 * cwborder.getPaybackfee().floatValue()); ps.setFloat(10,
	 * cwborder.getCarrealweight().floatValue()); ps.setString(11,
	 * cwborder.getCwbremark()); ps.setLong(12,cwborder.getCustomerid());
	 * ps.setString(13, cwborder.getEmaildate()); ps.setString(14,
	 * cwborder.getConsigneemobile()); ps.setLong(15,
	 * cwborder.getStartbranchid()); ps.setString(16,
	 * cwborder.getExceldeliver()); ps.setString(17, cwborder.getConsigneeno());
	 * ps.setString(18, cwborder.getExcelbranch()); ps.setFloat(19,
	 * cwborder.getCargoamount().floatValue()); ps.setString(20,
	 * cwborder.getCustomercommand()); ps.setString(21,
	 * cwborder.getCargotype()); ps.setString(22, cwborder.getCargosize());
	 * ps.setFloat(23, cwborder.getBackcargoamount().floatValue());
	 * ps.setString(24, cwborder.getDestination()); ps.setString(25,
	 * cwborder.getTransway()); ps.setLong(26, cwborder.getShipperid());
	 * ps.setLong(27, cwborder.getSendcarnum()); ps.setInt(28,
	 * cwborder.getBackcargonum()); ps.setLong(29,
	 * cwborder.getExcelimportuserid()); ps.setLong(30,
	 * cwborder.getCwbordertypeid()); ps.setLong(31,
	 * cwborder.getCwbdelivertypeid()); ps.setLong(32,
	 * cwborder.getCustomerwarehouseid()); ps.setString(33,
	 * cwborder.getCwbprovince()); ps.setString(34, cwborder.getCwbcity());
	 * ps.setString(35, cwborder.getCwbcounty()); ps.setString(36,
	 * cwborder.getShipcwb()); ps.setString(37, cwborder.getTranscwb());
	 * ps.setLong(38, cwborder.getServiceareaid()); ps.setLong(39,
	 * cwborder.getNextbranchid());
	 * 
	 * ps.setLong(40, orderflowid); ps.setInt(41,
	 * FlowOrderTypeEnum.DaoRuShuJu.getValue()); ps.setInt(42,
	 * EmailFinishFlagEnum.WeiDaoHuo.getValue());
	 * 
	 * 
	 * 
	 * ps.setString(43, cwborder.getModelname()); ps.setLong(44,
	 * cwborder.getEmaildateid()); ps.setLong(45, cwborder.getCarwarehouse());
	 * 
	 * ps.setLong(46, cwborder.getPaywayid()); ps.setString(47,
	 * cwborder.getMulti_shipcwb()); ps.setFloat(48,
	 * cwborder.getCargovolume().floatValue()); ps.setString(49,
	 * cwborder.getConsignoraddress()); }
	 * 
	 * });
	 * 
	 * }
	 */

	/**
	 * 修改临时表为获取成功 getDataFlag=1表示下次查询不再获取
	 */
	public void update_CwbDetailTempByCwb(long opscwbid) {
		try {
			jdbcTemplate.update("update express_ops_cwb_detail_b2ctemp set getDataFlag=1 where opscwbid=" + opscwbid);
			logger.info("修改tmall临时表数据 getDataFlag状态为1,success!opscwbid={}", opscwbid);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			logger.info("修改tmall临时表数据 getDataFlag失败！原因：" + e);
		}

	}

}
