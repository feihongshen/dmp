package cn.explink.dao.express;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import cn.explink.domain.VO.express.CollectorName;
import cn.explink.domain.VO.express.ExpressIntoStationCountVO;
import cn.explink.domain.VO.express.ExpressIntoStationVO;
import cn.explink.domain.dto.ExpressIntoDto;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.express.ExpressProcessStateEnum;
import cn.explink.enumutil.express.ExpressSettleWayEnum;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;
import cn.explink.util.Tools;

/**
 * 揽件入站
 * 
 * @author jiangyu 2015年8月4日
 *
 */
@Component
public class ExpressIntoStationDAO {
	@Autowired
	JdbcTemplate jdbcTemplate;

	private final class CwbInfoForExpressIntoStation implements RowMapper<ExpressIntoStationVO> {
		//小件员信息
		private Map<Long, String> deliverManMap;
		
		private Map<String, Object> addressInfo;
		
		private Map<Integer, String> provinceMap;
		
		private Map<Integer, String> cityMap;
		
		
		@SuppressWarnings("unchecked")
		public CwbInfoForExpressIntoStation(Map<Long, String> deliverManMap, Map<String, Object> addressInfo) {
			super();
			this.deliverManMap = deliverManMap;
			this.addressInfo = addressInfo;
			if (addressInfo!=null) {
				if(addressInfo.get("province")!=null){
					provinceMap = (Map<Integer, String>) addressInfo.get("province");
				}else {
					provinceMap = new HashMap<Integer, String>();
				}
				if(addressInfo.get("city")!=null){
					cityMap = (Map<Integer, String>) addressInfo.get("city");
				}else {
					cityMap = new HashMap<Integer, String>();
				}
			}
		}

		@Override
		public ExpressIntoStationVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			ExpressIntoStationVO intoStationVo = new ExpressIntoStationVO();
			intoStationVo.setId(rs.getInt("opscwbid"));
			intoStationVo.setTransNo(rs.getString("cwb"));//运单号
			intoStationVo.setOrderCount(rs.getLong("sendcarnum"));//件数
			intoStationVo.setDeliveryMan(StringUtil.nullConvertToEmptyString(deliverManMap.get(rs.getLong("collectorid"))));//小件员
			intoStationVo.setPackFee(rs.getBigDecimal("packagefee"));//包装费
			String pickTimeStr = StringUtil.nullConvertToEmptyString(rs.getString("instationdatetime"));
			if (!"".equals(pickTimeStr)&&pickTimeStr.length()>2) {
				intoStationVo.setPickTime(pickTimeStr.substring(0,pickTimeStr.length()-2));//揽件时间
			}else {
				intoStationVo.setPickTime(pickTimeStr);//揽件时间
			}
			intoStationVo.setSaveFee(rs.getBigDecimal("insuredfee"));//保价费
			intoStationVo.setTransportFee(rs.getBigDecimal("shouldfare"));//应收运费
			intoStationVo.setTransportFeeTotal(rs.getBigDecimal("totalfee"));//总的费用
			intoStationVo.setPayType(ExpressSettleWayEnum.getByValue(rs.getInt("paymethod")).getText());//付款方式
			intoStationVo.setProvince(StringUtil.nullConvertToEmptyString(provinceMap.get(rs.getInt("recprovinceid"))));//收件人省【还要做转换】
			intoStationVo.setCity(StringUtil.nullConvertToEmptyString(cityMap.get(rs.getInt("reccityid"))));//收件人市
			//处理状态的转变
			int flowOrderType = rs.getInt("flowordertype");
			if (FlowOrderTypeEnum.DaoRuShuJu.getValue()==flowOrderType) {
				intoStationVo.setProcessState(ExpressProcessStateEnum.UnProcess.getText());
			}else if(FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()==flowOrderType){
				intoStationVo.setProcessState(ExpressProcessStateEnum.Processed.getText());
			}
			return intoStationVo;
		}
	}
	/**
	 * pda 读取字段
	 * @author Administrator
	 *
	 */
	private final class ExpressIntoDtoMapper implements RowMapper<ExpressIntoDto>{

		@Override
		public ExpressIntoDto mapRow(ResultSet rs, int rowNum) throws SQLException {
			String pickTimeStr = StringUtil.nullConvertToEmptyString(rs.getString("instationdatetime"));
			ExpressIntoDto expressIntoDto=new ExpressIntoDto();
			expressIntoDto.setCwb(rs.getString("cwb"));
			expressIntoDto.setSendername(rs.getString("sendername"));
			expressIntoDto.setSendercellphone(rs.getString("sendercellphone"));
			expressIntoDto.setSendertelephone(rs.getString("sendertelephone"));
			expressIntoDto.setSenderaddress(rs.getString("senderaddress"));
			expressIntoDto.setRealweight(rs.getDouble("realweight"));
			if (!"".equals(pickTimeStr)&&pickTimeStr.length()>2) {
				expressIntoDto.setPickTime(pickTimeStr.substring(0,pickTimeStr.length()-2));//揽件时间
			}else {
				expressIntoDto.setPickTime(pickTimeStr);//揽件时间
			}
			expressIntoDto.setCwbremark(rs.getString("cwbremark"));
			return expressIntoDto;
		}
		
	}

	// 揽件入站查询
	public List<ExpressIntoStationVO> getRecordListByPage(long page, Long deliveryId, Integer processState, Integer payType, Long branchId, Map<Long, String> deliverManMap, Map<String, Object> addressInfo) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select c.opscwbid,c.cwb,c.sendcarnum,c.collectorid,c.instationdatetime,c.shouldfare,c.packagefee,c.insuredfee,c.paymethod");
		sql.append(",c.totalfee,c.recprovinceid,c.reccityid,c.flowordertype ");
		sql.append(" from express_ops_cwb_detail c ");
		sql.append(" where 1=1 ");
		if (!Tools.isEmpty(deliveryId)) {
			if (deliveryId.intValue()!=-1) {
				sql.append(" and collectorid=" + deliveryId);
			}
		}
		if(payType!=null){
			
			sql.append(" and paymethod=" + payType);
		}
		//是否交接标示
		if (ExpressProcessStateEnum.UnProcess.getValue().equals(processState)) {
			sql.append(" and ishandover=0");
		}else {
			sql.append(" and ishandover=1");
		}
		// 当前站点
		sql.append(" and instationid="+branchId);
		// sql.append(" and c.paymethod="+payType);
		//快递类型
		sql.append(" and c.cwbordertypeid="+CwbOrderTypeIdEnum.Express.getValue());
		sql.append(" and c.state=1 limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER);
		return this.jdbcTemplate.query(sql.toString(), new CwbInfoForExpressIntoStation(deliverManMap,addressInfo));
	}
	// pda 揽件入站确认查询
	public List<ExpressIntoDto> getRecordListByPage( String deliveryId, Integer processState, Integer payType, Long branchId) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select c.cwb,c.sendername,c.sendercellphone,c.sendertelephone,c.senderaddress,c.realweight,c.instationdatetime,c.cwbremark ");
		sql.append(" from express_ops_cwb_detail c ");
		sql.append(" where 1=1 ");
		if (!Tools.isEmpty(deliveryId)) {
			if (StringUtils.hasText(deliveryId)) {
				sql.append(" and collectorid in (" + deliveryId+") ");
			}
		}
		if(payType!=null){
			
			sql.append(" and paymethod=" + payType);
		}
		//是否交接标示
		if (ExpressProcessStateEnum.UnProcess.getValue().equals(processState)) {
			sql.append(" and ishandover=0");
		}else {
			sql.append(" and ishandover=1");
		}
		// 当前站点
		sql.append(" and instationid="+branchId);
		// sql.append(" and c.paymethod="+payType);
		//快递类型
		sql.append(" and c.cwbordertypeid="+CwbOrderTypeIdEnum.Express.getValue());
		sql.append(" and c.state=1");
		return this.jdbcTemplate.query(sql.toString(), new ExpressIntoDtoMapper());
	}

	// 揽件入站查询记录数
	public Long getExpressRecordCount(Long deliveryId, Integer processState, Integer payType, Long branchId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT COUNT(1) count FROM express_ops_cwb_detail c ");
		sql.append(" where 1=1 ");
		if (!Tools.isEmpty(deliveryId)) {
			if (deliveryId.intValue()!=-1) {
				sql.append(" and collectorid=" + deliveryId);
			}
		}
		if(payType!=null){
			
			sql.append(" and paymethod=" + payType);
		}
		//是否交接标示
		if (ExpressProcessStateEnum.UnProcess.getValue().equals(processState)) {
			sql.append(" and ishandover=0");
		}else {
			sql.append(" and ishandover=1");
		}
		// 当前站点
		sql.append(" and instationid="+branchId);
//		sql.append(" and c.paymethod="+payType);
		//快递类型
		sql.append(" and c.cwbordertypeid="+CwbOrderTypeIdEnum.Express.getValue());
		sql.append(" and c.state=1 ");
		return this.jdbcTemplate.queryForLong(sql.toString());
	}

	
	private final class CountInfoForExpressIntoStation implements RowMapper<ExpressIntoStationCountVO> {
		@Override
		public ExpressIntoStationCountVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			ExpressIntoStationCountVO record = new ExpressIntoStationCountVO();
			record.setPayType(rs.getInt("paymethod"));//到付  现付  月结
			record.setCount(rs.getLong("totalCount"));
			record.setSumFee(rs.getBigDecimal("sumFee"));
			return record;
		}
	}
	
	private final class CollectorInfoForExpressIntoStation implements RowMapper<CollectorName> {
		@Override
		public CollectorName mapRow(ResultSet rs, int rowNum) throws SQLException {
			CollectorName record = new CollectorName();
			record.setCollectorName(rs.getString("collectorname"));
			return record;
		}
	}

	/**
	 * 查询汇总记录
	 * 
	 * @param ids
	 * @return
	 */
	public List<ExpressIntoStationCountVO> getExpressOrderSummaryCountBefore(String ids) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select c.paymethod,COUNT(1) as totalCount,sum(c.totalfee) as sumFee");//SUM(IFNULL(c.packagefee,0)+IFNULL(c.insuredfee,0)+IFNULL(c.shouldfare,0))
		sql.append(" from express_ops_cwb_detail c ");
		sql.append(" where 1=1 ");
		sql.append(" and c.opscwbid in("+ids+")");
		sql.append(" and c.state=1 ");
		sql.append(" and c.cwbordertypeid="+CwbOrderTypeIdEnum.Express.getValue());
		sql.append(" group by c.paymethod");
		return this.jdbcTemplate.query(sql.toString(), new CountInfoForExpressIntoStation());
	}
	
	public ExpressIntoStationCountVO getExpressOrderSummaryCount(String ids) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select COUNT(1) as totalCount,sum(c.totalfee) as sumFee");//c.paymethod,
		sql.append(" from express_ops_cwb_detail c ");
		sql.append(" where 1=1 ");
		sql.append(" and c.opscwbid in("+ids+")");
		sql.append(" and c.state=1 ");
		sql.append(" and c.cwbordertypeid="+CwbOrderTypeIdEnum.Express.getValue());
//		sql.append(" group by c.paymethod");
//		return this.jdbcTemplate.query(sql.toString(), new CountInfoForExpressIntoStation());
		return this.jdbcTemplate.queryForObject(sql.toString(), new CountInfoForExpressIntoStation());
	}
	/**
	 * 查询小件员
	 * @param ids
	 * @return
	 */
	public List<String> deliveryManList(String ids){
		StringBuffer sql = new StringBuffer();
		sql.append(" select DISTINCT(c.collectorname) as collectorname ");//c.paymethod,
		sql.append(" from express_ops_cwb_detail c ");
		sql.append(" where 1=1 ");
		sql.append(" and c.opscwbid in("+ids+")");
		sql.append(" and c.state=1 ");
		sql.append(" and c.cwbordertypeid="+CwbOrderTypeIdEnum.Express.getValue());
		sql.append(" and c.collectorname is not NULL");
		return this.jdbcTemplate.queryForList(sql.toString(),String.class);
//		return this.jdbcTemplate.query(sql.toString(),new CollectorInfoForExpressIntoStation());
	}
	
	/**
	 * 操作入站操作更新订单相关字段[更新订单的上一站下一站当前站]
	 * @param ids
	 * @param pickExpressTime 揽件入站时间的修改
	 * @param deliverId
	 * @return
	 */
	public Integer executeIntoStationOpe(String ids, Long currentBranchId,Long nextBranchId, String pickExpressTime,String intoStationName) {
		StringBuffer sql = new StringBuffer();
		sql.append(" update express_ops_cwb_detail c ");
//		sql.append(" set instationid=?,instationname=?,flowordertype=?,startbranchid=?,currentbranchid=?,nextbranchid=?,instationdatetime='"+pickExpressTime+"' where c.opscwbid in("+ids+") and state = 1");
		sql.append(" set instationid=?,instationname=?,flowordertype=?,startbranchid=?,currentbranchid=?,nextbranchid=?,instationdatetime='"+pickExpressTime+"',ishandover=1,instationhandoverid=?,instationhandovername=?,instationhandovertime='"+pickExpressTime+"' where c.opscwbid in("+ids+") and state = 1");
		//状态修改为:揽件入站
//		return this.jdbcTemplate.update(sql.toString(),currentBranchId,intoStationName,FlowOrderTypeEnum.LanJianRuZhan.getValue(),currentBranchId,currentBranchId,nextBranchId);
		return this.jdbcTemplate.update(sql.toString(),currentBranchId,intoStationName,FlowOrderTypeEnum.LanJianRuZhan.getValue(),currentBranchId,currentBranchId,nextBranchId,currentBranchId,intoStationName);
	}
	
	/**
	 * @param ids
	 * 运单录入和揽件入站之外的操作做交接操作
	 */
	public Integer executeIntoStationForOth(String ids,Long currentBranchId, String pickExpressTime,String intoStationName) {
		StringBuffer sql = new StringBuffer();
		sql.append(" update express_ops_cwb_detail c ");
		sql.append(" set ishandover=1, instationhandoverid=?,instationhandovername=?,instationhandovertime='"+pickExpressTime+"'  where c.opscwbid in("+ids+") and state = 1");
		return this.jdbcTemplate.update(sql.toString(),currentBranchId,intoStationName);
	}
}
