package cn.explink.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.explink.enumutil.FlowOrderTypeEnum;

@Repository
public class GoodsStatusNumDao {
	
	@Autowired
	private JdbcTemplate jt;

	public int findGoodsStatusNum(int flowordertype,int branchid){
		String append="";
		if(flowordertype==FlowOrderTypeEnum.ChuKuSaoMiao.getValue()){
			append=" and nextbranchid=?";
		}
		if(flowordertype==FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()||flowordertype==FlowOrderTypeEnum.FenZhanLingHuo.getValue()){
			append=" and branchid=?";
		}
		
		String sql="select count(1) from express_ops_operation_time where flowordertype=? "+append;
		
		return jt.queryForInt(sql,flowordertype,branchid);
	
	}
}
