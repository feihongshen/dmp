package cn.explink.test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class FinaceDAO {
	private Logger logger = LoggerFactory.getLogger(FinaceDAO.class);

	
	private final class CwbCountMapper implements RowMapper<Map<String,Long>> {

		@Override
		public Map<String,Long> mapRow(ResultSet rs, int rowNum) throws SQLException {
			Map<String,Long> ojb= new HashMap<String, Long>();
			ojb.put(rs.getString("cwb"),rs.getLong("count"));
			return ojb;
		}
	}
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	private final class CwbMapper implements RowMapper<String> {

		@Override
		public String mapRow(ResultSet rs, int rowNum) throws SQLException {
			return rs.getString("cwb");
		}
	}
	
	private final class AttrVo implements RowMapper<TypeVo> {

		@Override
		public TypeVo mapRow(ResultSet rs, int rowNum) throws SQLException {
			TypeVo vo=new TypeVo();
			vo.setNum(rs.getLong("num"));
			vo.setAmount(rs.getDouble("amount"));
			return vo;
		}
	}
	private final class DetailVo implements RowMapper<TypeVo> {

		@Override
		public TypeVo mapRow(ResultSet rs, int rowNum) throws SQLException {
			TypeVo vo=new TypeVo();
			vo.setCwb(rs.getString("cwb"));
			vo.setAmount(rs.getDouble("amount"));
			return vo;
		}
	}
	
	public String getDownloadDataSql(long maxCount,long deliverybranchid,long deliverystate){
		String sql="select cwb,businessfee from express_ops_delivery_state where state=1 and businessfee>0 and deliverystate="+deliverystate+" and deliverybranchid="+deliverybranchid;
		return sql;
	}
	
	public String getDeliveryStateByCwbsSql(String cwbs){
		String sql="select cwb,businessfee from express_ops_delivery_state where state=1 and cwb in ("+cwbs+")";
		return sql;
	}
	
	/**
	 * 查询成功、丢失 明细未扣款
	 * @param maxCount
	 * @param deliverybranchid
	 * @param deliverystate
	 * @return
	 */
	public String getDownloadDataDiffSql(long maxCount,long deliverybranchid,long deliverystate,String cwbs){
		String sql="SELECT cwb,businessfee FROM express_ops_delivery_state WHERE state=1  and businessfee>0  and deliverystate="+deliverystate+" AND deliverybranchid="+deliverybranchid
				+" AND cwb NOT IN ("+cwbs+")";
		return sql;
	}
	
	/**
	 * 查询 中转退货记录
	 * @param maxCount
	 * @param deliverybranchid
	 * @param flowordertype  扣款模式状态  
	 * @return
	 */
	public String getDownloadDataJuShouZhongZhuanSql(long maxCount,long deliverybranchid,long flowordertype){
		String sql="SELECT distinct cwb,fee as businessfee FROM ops_account_deduct_detail WHERE fee>0 AND branchid="+deliverybranchid+" AND flowordertype="+flowordertype;
				
		return sql;
	}
	
	
	
	/**
	 * 查询 中转退货重复
	 * @param maxCount
	 * @param deliverybranchid
	 * @param flowordertype  扣款模式状态  
	 * @return
	 */
	public String getDownloadDataJuShouZhongZhuanDiffSql(long maxCount,long deliverybranchid,long flowordertype){
		String sql="SELECT cwb,businessfee FROM express_ops_delivery_state WHERE state=1 AND cwb IN "
				+" (SELECT cwb FROM ops_account_deduct_detail WHERE branchid="+deliverybranchid+" AND flowordertype="+flowordertype
				+" GROUP BY cwb,branchid,flowordertype,createTime HAVING COUNT(1)>1)";
		return sql;
	}
	
	

	/**
	 * 查询未扣款并且 中转退货重复
	 * @param maxCount
	 * @param deliverybranchid
	 * @param flowordertype  扣款模式状态  
	 * @return
	 */
	public String getDownloadDataWeikoukuanJuShouZhongZhuanDiffSql(long maxCount,long deliverybranchid,long flowordertype){
		String sql="SELECT cwb,businessfee FROM express_ops_delivery_state WHERE state=1 AND cwb IN "
				+" (SELECT cwb FROM ops_account_deduct_detail WHERE branchid="+deliverybranchid+" AND flowordertype="+flowordertype
				+" GROUP BY cwb,branchid,flowordertype,createTime HAVING COUNT(1)>1)";
		return sql;
	}
	
	
	
	/**
	 * 查询 扣款表所有 已交易明细 ，根据类型查询
	 * @param maxCount
	 * @param deliverybranchid
	 * @param flowordertype  扣款模式状态  
	 * @return
	 */
	public List<String> getAccountDecuctByType(long maxCount,long deliverybranchid,long flowordertype){
		String sql="SELECT cwb FROM ops_account_deduct_detail WHERE fee>0 and  branchid="+deliverybranchid+" AND flowordertype="+flowordertype;
		List<String> list= jdbcTemplate.query(sql,new CwbMapper());
		return list;
	}
	
	
	/**
	 * 查询deliverystate表中所有成功和丢失的记录 不包含在cwbs中
	 * @param maxCount
	 * @param deliverybranchid
	 * @param flowordertype
	 * @return
	 */
	public List<String> getDeliveryStateNotInCwbs(String cwbs){
		String sql="SELECT cwb FROM express_ops_delivery_state WHERE deliverystate IN (1,8) and state=1 and cwb not in (cwbs)";
		List<String> list= jdbcTemplate.query(sql,new CwbMapper());
		return list;
	}
	
	
	
	/**
	 * 查询扣款表中所有重复 中转或者反馈的记录,根据订单查询
	 * @param deliverybranchid
	 * @param flowordertype
	 * @param cwbs  
	 * @return List<String>
	 */
	public List<String> getZhongzhongTuihuoChongfuCwbList(long deliverybranchid,String cwbs){
		String sql="SELECT cwb FROM ops_account_deduct_detail WHERE branchid="+deliverybranchid+" AND flowordertype IN (11,12) AND cwb in ("+cwbs+") "
					+" GROUP BY cwb,branchid,flowordertype,createTime HAVING COUNT(1)>1";
		List<String> list= jdbcTemplate.query(sql,new CwbMapper());
		return list;
	}
	
	/**
	 * 查询反馈表数量和金额
	 * @param deliveryState
	 * @param deliverybranchid
	 * @return
	 */
	public TypeVo getDeliveryByState(long deliverystate,long deliverybranchid,String cwbs){
		String sql="SELECT COUNT(1) as num,SUM(businessfee) as amount FROM express_ops_delivery_state "
				+ " WHERE state=1 AND businessfee>0  AND deliverystate IN ("+deliverystate+") AND deliverybranchid="+deliverybranchid;
		if(cwbs!=null&&cwbs.contains(",")){
			sql+=" AND cwb in ("+cwbs+")";
		}
		TypeVo typeVo= jdbcTemplate.queryForObject(sql,new AttrVo());
		return typeVo;
	}
	
	/**
	 * 查询妥投差异
	 * @param deliveryState
	 * @param deliverybranchid
	 * @return
	 */
	public TypeVo getDeliveryByStateDiff(long deliverystate,long deliverybranchid,String cwbs){
		String sql="SELECT COUNT(1) as num,SUM(businessfee) as amount FROM express_ops_delivery_state "
				+ " WHERE state=1 AND businessfee>0  AND deliverystate IN ("+deliverystate+") AND deliverybranchid="+deliverybranchid;
		if(cwbs!=null&&cwbs.contains(",")){
			sql+=" AND cwb NOT IN ("+cwbs+")";
		}
		TypeVo typeVo= jdbcTemplate.queryForObject(sql,new AttrVo());
		return typeVo;
	}
	
	
	/**
	 * 查询扣款返款(根据去重后的明细拿到不重复的汇总)
	 * @param deliveryState
	 * @param deliverybranchid
	 * @return
	 */
	public TypeVo getDeliveryZhongZhuanTuiHuo(long accountFlowType,long deliverybranchid){
		
		List<TypeVo> typeList = this.getDeductListByType(accountFlowType, deliverybranchid);
		double amount=0;
		if(typeList!=null&&typeList.size()>0){
			for(TypeVo vo:typeList){
				amount+=vo.getAmount();
			}
			TypeVo typeVo= new TypeVo(typeList.size(), amount);
			return typeVo;
		}
		
		return new TypeVo(0,0);
	}
	
	/**
	 * 查询扣返款表记录
	 * @param maxCount
	 * @param deliverybranchid
	 * @param deliverystate
	 * @return
	 */
	public List<String> getDeductByType(long deliverybranchid,long accountFlowType){
		String sql="SELECT cwb FROM ops_account_deduct_detail WHERE branchid="+deliverybranchid+" AND flowordertype="+accountFlowType+" AND fee>0";
		List<String> list=jdbcTemplate.query(sql, new CwbMapper());
		return list;
	}
	
	
	/**
	 * 查询退货未返款记录
	 * @param deliveryState
	 * @param deliverybranchid
	 * @return
	 */
	public TypeVo getDeliveryByStateNoBack(long deliverystate,long deliverybranchid,String cwbs){
		String sql="SELECT COUNT(1) as num,SUM(businessfee) as amount FROM express_ops_delivery_state "
				+ " WHERE state=1 AND businessfee>0  AND deliverystate IN ("+deliverystate+") AND deliverybranchid="+deliverybranchid;
		if(cwbs!=null&&cwbs.contains(",")){
			sql+=" AND cwb NOT IN ("+cwbs+")";
		}
		TypeVo typeVo= jdbcTemplate.queryForObject(sql,new AttrVo());
		return typeVo;
	}
	
	/**
	 * 查询退货未返款记录
	 * @param deliveryState
	 * @param deliverybranchid
	 * @return
	 */
	public List<TypeVo> getDeliveryDetailByStateNoBack(long deliverystate,long deliverybranchid,String cwbs){
		String sql="SELECT cwb,businessfee as amount FROM express_ops_delivery_state "
				+ " WHERE state=1 AND businessfee>0  AND deliverystate IN ("+deliverystate+") AND deliverybranchid="+deliverybranchid;
		if(cwbs!=null&&cwbs.contains(",")){
			sql+=" AND cwb NOT IN ("+cwbs+")";
		}
		List<TypeVo> typeVoList= jdbcTemplate.query(sql,new DetailVo());
		return typeVoList;
	}
	
	/**
	 * 查询detail表未完结订单
	 * @param deliveryState
	 * @param deliverybranchid
	 * @return
	 */
	public TypeVo getDetailWeiWanJie(long deliverybranchid){
		String sql1=" SELECT COUNT(1)AS num,SUM(receivablefee) AS amount FROM express_ops_cwb_detail WHERE state=1 " 
				  +" AND receivablefee>0   AND  (deliverystate=0 AND flowordertype  IN (6) AND nextbranchid="+deliverybranchid+")  ";
		String sql2=" SELECT COUNT(1)AS num,SUM(receivablefee) AS amount FROM express_ops_cwb_detail WHERE state=1 "
				  +" AND receivablefee>0  AND  (deliverystate=0 AND flowordertype  IN (7,8) AND currentbranchid="+deliverybranchid+")  ";
		
		String sql3=" SELECT COUNT(1)AS num,SUM(receivablefee) AS amount FROM express_ops_cwb_detail WHERE state=1  "
				  +" AND receivablefee>0  AND  (deliverystate=0 AND flowordertype  IN (9) AND startbranchid="+deliverybranchid+")  ";
		
		String sql4=" SELECT COUNT(1)AS num,SUM(receivablefee) AS amount FROM express_ops_cwb_detail WHERE state=1  "
				  +" AND receivablefee>0  AND  (deliverystate IN (6,9) AND flowordertype IN(35,36) AND currentbranchid="+deliverybranchid+")  ";
		String sqlAll=sql1+" UNION "+sql2+" UNION "+sql3+" UNION "+sql4;
		
		List<TypeVo> typeVoList= jdbcTemplate.query(sqlAll,new AttrVo());
		if(typeVoList==null||typeVoList.size()==0){
			return new TypeVo(0,0.00);
		}
		TypeVo alVo= new TypeVo();
		long nums=0;
		double amounts=0.00;
		for(TypeVo vo:typeVoList){
			nums+=vo.getNum();
			amounts+=vo.getAmount();
		}
		return new TypeVo(nums,amounts); 
		
	}
	
	
	/**
	 * 根据参数得到中转或退货的明细
	 * @param accountFlowType
	 * @param deliverybranchid
	 * @return
	 */
	public List<TypeVo> getDeductListByType(long accountFlowType,long deliverybranchid){
		String sql="SELECT distinct cwb,fee as amount FROM ops_account_deduct_detail WHERE branchid="+deliverybranchid
				+" AND flowordertype="+accountFlowType+" AND fee>0";
		List<TypeVo> typeVoList= jdbcTemplate.query(sql,new DetailVo());
		return typeVoList;
	}
	/**
	 * 根据参数得到中转或退货的明细
	 * @param accountFlowType
	 * @param deliverybranchid
	 * @return
	 */
	public List<TypeVo> getDeductsListByType(String accountFlowTypes,long deliverybranchid){
		String sql="SELECT distinct cwb,fee as amount FROM ops_account_deduct_detail WHERE branchid="+deliverybranchid
				+" AND flowordertype IN ("+accountFlowTypes+")  AND fee>0";
		List<TypeVo> typeVoList= jdbcTemplate.query(sql,new DetailVo());
		return typeVoList;
	}
	
	
	
	/**
	 * 根据参数得到妥投和丢失明细
	 * @param deliverystate
	 * @param deliverybranchid
	 * @return
	 */
	public List<TypeVo> getDeliveryListByType(long deliverystate,long deliverybranchid){
		String sql="SELECT cwb,businessfee as amount FROM express_ops_delivery_state WHERE state=1 AND deliverybranchid="+deliverybranchid
				+" AND deliverystate="+deliverystate+" AND businessfee>0";
		List<TypeVo> typeVoList= jdbcTemplate.query(sql,new DetailVo());
		return typeVoList;
	}
	
	/**
	 * 根据参数得到未完结订单明细
	 * @param deliverystate
	 * @param deliverybranchid
	 * @return
	 */
	public List<TypeVo> getDeliveryNoResultListByType(long deliverybranchid){
		
		String sql1=" SELECT  cwb,receivablefee as amount  FROM express_ops_cwb_detail WHERE state=1 " 
				  +" AND receivablefee>0   AND  (deliverystate=0 AND flowordertype  IN (6) AND nextbranchid="+deliverybranchid+")  ";
		String sql2=" SELECT  cwb,receivablefee as amount  FROM express_ops_cwb_detail WHERE state=1 "
				  +" AND receivablefee>0  AND  (deliverystate=0 AND flowordertype  IN (7,8) AND currentbranchid="+deliverybranchid+")  ";
		
		String sql3=" SELECT  cwb,receivablefee as amount  FROM express_ops_cwb_detail WHERE state=1  "
				  +" AND receivablefee>0  AND  (deliverystate=0 AND flowordertype  IN (9) AND startbranchid="+deliverybranchid+")  ";
		
		String sql4=" SELECT  cwb,receivablefee as amount  FROM express_ops_cwb_detail WHERE state=1  "
				  +" AND receivablefee>0  AND  (deliverystate IN (6,9) AND flowordertype IN(35,36) AND currentbranchid="+deliverybranchid+")  ";
		String sqlAll=sql1+" UNION "+sql2+" UNION "+sql3+" UNION "+sql4;
		
		List<TypeVo> typeVoList= jdbcTemplate.query(sqlAll,new DetailVo());
		return typeVoList;
	}

	
	/**
	 * 查询detail表拒收未退货出站订单
	 * @param deliveryState
	 * @param deliverybranchid
	 * @return
	 */
	public List<TypeVo> getDeliveryJuShouNoBackOut(long deliverybranchid){
		String sql="SELECT cwb,receivablefee as amount FROM express_ops_cwb_detail WHERE state=1 "
				+ " AND receivablefee>0 AND deliverystate=4 AND startbranchid="+deliverybranchid
				+ " AND  flowordertype IN(35,36) ";
		List<TypeVo> typeVoList= jdbcTemplate.query(sql,new DetailVo());
		return typeVoList;
	}
	
	/**
	 * 查询detail退货出站未入库数据明细
	 * @param deliveryState
	 * @param deliverybranchid
	 * @return
	 */
	public List<TypeVo> getDeliveryTuiHuoChuZhanWeiRuKu(long deliverybranchid){
		String sql="SELECT cwb,receivablefee as amount FROM express_ops_cwb_detail WHERE state=1 "
				+ " AND receivablefee>0 AND deliverystate=4 AND startbranchid="+deliverybranchid
				+ " AND  flowordertype=40 ";
		List<TypeVo> typeVoList= jdbcTemplate.query(sql,new DetailVo());
		return typeVoList;
	}
	
	/**
	 * 查询detail中转出站数据明细
	 * @param deliveryState
	 * @param deliverybranchid
	 * @return
	 */
	public List<TypeVo> getDetailZhongZhuanChuZhan(long deliverybranchid,String allzzBranchids){
		String sql="SELECT cwb,receivablefee as amount FROM express_ops_cwb_detail WHERE state=1 "
				+ " AND receivablefee>0  AND startbranchid="+deliverybranchid
				+ " AND  flowordertype=6 and nextbranchid in ("+allzzBranchids+") ";
		List<TypeVo> typeVoList= jdbcTemplate.query(sql,new DetailVo());
		return typeVoList;
	}
	
	
	
	/**
	 * 查询退货站入库未返款记录
	 * @param deliveryState
	 * @param deliverybranchid  
	 * @param flowordertype in (15,27,28,34)
	 * @param deliverid 操作人ID
	 * @param cwbstrs 反馈表中的这个站派送的所有数据
	 * @return
	 */
	public TypeVo getDetaiNoBackAmount(long deliverystate,long deliverybranchid,String cwbs,String cwbstrs){
		
		String sql1="SELECT COUNT(1) as num,SUM(receivablefee) as amount FROM express_ops_cwb_detail "
				+ " WHERE state=1 AND receivablefee>0  "
				+ " AND (flowordertype IN (15,45) AND startbranchid="+deliverybranchid+")  " ; // 退货站入库、退货再投
		
		String sql2="SELECT COUNT(1) as num,SUM(receivablefee) as amount FROM express_ops_cwb_detail "
				+ " WHERE state=1 AND receivablefee>0  "
				+ " AND (flowordertype IN (27,28,34) AND cwb IN ("+cwbstrs+") ) " ; // 退供货商出库、确认退货、拒收返库
			
		
		
		if(cwbs!=null&&cwbs.contains(",")){
			sql1+=" AND cwb NOT IN ("+cwbs+") ";
			sql2+=" AND cwb NOT IN ("+cwbs+") ";
		}
		
		String  sqlAll=sql1+" UNION "+sql2;
		
		List<TypeVo> typeVoList= jdbcTemplate.query(sqlAll,new AttrVo());
		if(typeVoList==null||typeVoList.size()==0){
			return new TypeVo(0,0.00);
		}
		long nums=0;
		double amounts=0;
		for(TypeVo vo:typeVoList){
			nums+=vo.getNum();
			amounts+=vo.getAmount();
		}
		
		return new TypeVo(nums,amounts);
	}
	
	
	/**
	 * 查询退货站入库未返款记录
	 * @param deliveryState
	 * @param deliverybranchid
	 * @return
	 */
	public List<TypeVo>getDetaiListNoBackAmount(long deliverystate,long deliverybranchid,String cwbs,String cwbstrs){
		
		String sql1="SELECT  cwb,receivablefee as amount  FROM express_ops_cwb_detail "
				+ " WHERE state=1 AND receivablefee>0  "
				+ " AND (flowordertype IN (15,45) AND startbranchid="+deliverybranchid+")  " ; // 退货站入库、退货再投
		
		String sql2="SELECT  cwb,receivablefee as amount  FROM express_ops_cwb_detail "
				+ " WHERE state=1 AND receivablefee>0  "
				+ " AND (flowordertype IN (27,28,34) AND cwb IN ("+cwbstrs+") ) " ; // 退供货商出库、确认退货、拒收返库
		
		if(cwbs!=null&&cwbs.contains(",")){
			sql1+=" AND cwb NOT IN ("+cwbs+") ";
			sql2+=" AND cwb NOT IN ("+cwbs+") ";
		}
		String  sqlAll=sql1+" UNION "+sql2;
		
		List<TypeVo> typeVoList= jdbcTemplate.query(sqlAll,new DetailVo());
		return typeVoList;
	}
	
	
	/**
	 * 查询中转站入库未返款记录
	 * @param deliveryState
	 * @param deliverybranchid
	 * @param zzBranchIds 所有中转站id
	 * @return
	 */
	public TypeVo getDetaiZhongZhuanNoBackAmount(long deliverybranchid,String cwbs,String zzBranchIds){
		String sql="SELECT COUNT(1) as num,SUM(receivablefee) as amount FROM express_ops_cwb_detail "
				+ " WHERE state=1 AND receivablefee>0 AND startbranchid="+deliverybranchid+"  AND flowordertype IN (4,12) "
				+ " AND currentbranchid IN ("+zzBranchIds+")";
		if(cwbs!=null&&cwbs.contains(",")){
			sql+=" AND cwb NOT IN ("+cwbs+")";
		}
		TypeVo typeVo= jdbcTemplate.queryForObject(sql,new AttrVo());
		return typeVo;
	}
	
	
	/**
	 * 查询中转站入库未返款记录List
	 * @param deliveryState
	 * @param deliverybranchid
	 * @param zzBranchIds 所有中转站id
	 * @return
	 */
	public List<TypeVo> getDetaiListZhongZhuanNoBackAmount(long deliverybranchid,String cwbs,String zzBranchIds){
		String sql="SELECT cwb,receivablefee as amount FROM express_ops_cwb_detail "
				+ " WHERE state=1 AND receivablefee>0 AND startbranchid="+deliverybranchid+"  AND flowordertype IN (4,12) "
				+ " AND currentbranchid in ("+zzBranchIds+")";
		if(cwbs!=null&&cwbs.contains(",")){
			sql+=" AND cwb NOT IN ("+cwbs+")";
		}
		List<TypeVo> typeVoList= jdbcTemplate.query(sql,new DetailVo());
		return typeVoList;
	}
	
	/**
	 * 查询deliverystate表中所有做过拒收的记录
	 * @param maxCount
	 * @param deliverybranchid
	 * @param flowordertype
	 * @return
	 */
	public List<String> getDeliveryListByJuShou(long deliverybranchid){
		String sql="SELECT cwb FROM express_ops_delivery_state WHERE state=1 AND deliverystate = 4 AND  deliverybranchid="+deliverybranchid
				;
		List<String> list= jdbcTemplate.query(sql,new CwbMapper());
		return list;
	}
	
	
	
	/**
	 * 查询扣款表所有状态为7，强制出库 重复的记录
	 * @param deliveryState
	 * @param deliverybranchid
	 * @return
	 */
	public List<Map<String,Long>> getQiangZhiChukuChongfu(long deliverybranchid){
		String sql="SELECT cwb,COUNT(1) as count FROM ops_account_deduct_detail WHERE  flowordertype=7 AND branchid="+deliverybranchid+"  GROUP BY cwb HAVING COUNT(1)>1";
		List<Map<String,Long>> typeVoList= jdbcTemplate.query(sql,new CwbCountMapper());
		return typeVoList;
	}
	
	/**
	 * 查询扣款表所有状态为7，强制出库 重复的记录
	 * @param deliveryState
	 * @param deliverybranchid
	 * @return
	 */
	public List<Map<String,Long>> getKouKuanByBranchidList(long deliverybranchid,String cwbs){
		String sql="SELECT cwb,COUNT(1) as count FROM ops_account_deduct_detail WHERE  flowordertype=8 AND branchid="+deliverybranchid+""
				+ " AND cwb in ("+cwbs+") "
				+ " GROUP BY cwb";
		List<Map<String,Long>> typeVoList= jdbcTemplate.query(sql,new CwbCountMapper());
		return typeVoList;
	}
	
	/**
	 * 根据参数得到妥投和丢失明细
	 * @param deliverystate
	 * @param deliverybranchid
	 * @return
	 */
	public TypeVo getDeliveryByType(long deliverystate,long deliverybranchid){
		String sql="SELECT cwb,businessfee as amount FROM express_ops_delivery_state WHERE state=1 AND deliverybranchid="+deliverybranchid
				+" AND deliverystate="+deliverystate+" AND businessfee>0";
		TypeVo vo= jdbcTemplate.queryForObject(sql,new DetailVo());
		return vo;
	}
	
	

	public TypeVo getDeliveryByCwb(String cwb){
		String sql="SELECT cwb,businessfee as amount FROM express_ops_delivery_state WHERE state=1 AND cwb='"+cwb+"' limit 1";
		TypeVo vo= jdbcTemplate.queryForObject(sql,new DetailVo());
		return vo;
	}
}

