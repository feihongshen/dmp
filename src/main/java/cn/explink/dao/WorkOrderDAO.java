package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import cn.explink.domain.CsComplaintAccept;
import cn.explink.domain.CsComplaintAcceptVO;
import cn.explink.domain.CsConsigneeInfo;

@Repository
public class WorkOrderDAO {
	
	@Autowired
	private JdbcTemplate jt;
	
	private final class WorkOrderRowMapper implements RowMapper<CsConsigneeInfo>{

		@Override
		public CsConsigneeInfo mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			CsConsigneeInfo cci = new CsConsigneeInfo();
			cci.setId(rs.getInt("id"));
			cci.setPhoneonOne(rs.getString("phone_one"));
			cci.setConsigneeType(rs.getInt("consignee_type"));
			cci.setSex(rs.getInt("sex"));
			cci.setProvince(rs.getString("province"));
			cci.setCity(rs.getString("city"));
			cci.setName(rs.getString("name"));
			cci.setContactLastTime((String)rs.getString("contact_last_time"));
			cci.setContactNum(rs.getInt("contact_num"));
			
			return cci;
		}
		
	}
	
	public void save(CsConsigneeInfo cci){
		String sql="insert into cs_consignee_info(phone_one,consignee_type,sex,province,city,name,contact_last_time,contact_num) values(?,?,?,?,?,?,?,?)";
		jt.update(sql,cci.getPhoneonOne(),cci.getConsigneeType(),cci.getSex(),cci.getProvince(),cci.getCity(),cci.getName(),cci.getContactLastTime(),cci.getContactNum());
	}
	
	public void saveAllCsConsigneeInfo(CsConsigneeInfo cci){
		String sql="insert into cs_consignee_info(phone_one,phone_two,consignee_type,sex,province,city,name,contact_last_time,contact_num,mail_box) values(?,?,?,?,?,?,?,?,?,?)";
		jt.update(sql,cci.getPhoneonOne(),cci.getPhoneonTwo(),cci.getConsigneeType(),cci.getSex(),cci.getProvince(),cci.getCity(),cci.getName(),cci.getContactLastTime(),cci.getContactNum(),cci.getMailBox());
	}
	
	public void editCsConsigneeInfo(String phone){
		
		
	}
	
	public void remove(String phone){
		String sql="delete from cs_consignee_info where phone_one=?";
		jt.update(sql,phone);
	}
	
	public CsConsigneeInfo queryByPhoneNum(String phoneonOne)throws Exception{
		String sql="select * from cs_consignee_info where phone_one=?";
		CsConsigneeInfo cf=	null;
		if(this.jt.queryForObject(sql,new WorkOrderRowMapper(),phoneonOne)!=null){
			cf=this.jt.queryForObject(sql,new WorkOrderRowMapper(),phoneonOne);
		}
	
			return cf;							
	}
	public List<CsConsigneeInfo> queryAllCsConsigneeInfo(){
		String sql="select * from cs_consignee_info";
		
			return this.jt.query(sql,new WorkOrderRowMapper());							
	}
	
	public List<CsConsigneeInfo> queryByPhoneone(String phoneonOne)throws Exception{
		String sql="select * from cs_consignee_info where phone_one=?";
		List<CsConsigneeInfo> cf=null;
		if(this.jt.query(sql,new WorkOrderRowMapper(),phoneonOne)!=null){
			cf=this.jt.query(sql,new WorkOrderRowMapper(),phoneonOne);
		}
	
			return cf;							
	}
	
	
/*	public List<CsConsigneeInfo> queryprovinceByphone(String phone){
		List<CsConsigneeInfo> lcs= null;
		String sql="select * from cs_consignee_info where phone_one=?";
		this.jt.query(sql, new WorkOrderRowMapper(),phone);
		if(this.jt.query(sql, new WorkOrderRowMapper(),phone).size()>0){
			 lcs=this.jt.query(sql, new WorkOrderRowMapper(),phone);
		}else {return null;}
		return 	lcs;
	}*/
	
	/*public List<CsConsigneeInfo> findGoOnacceptWOOfAN(){
		
		return this.jt.query("select * from cs_consignee_info", rse)
	}*/
 	
/*	private final class QueryWorkOrderRowMapper implements RowMapper<CsComplaintAccept>{

		@Override
		public CsComplaintAccept mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			CsComplaintAccept cca =new CsComplaintAccept();
			return null;
		}
		
		
	}*/
	
	public void savequeryworkorderForm(CsComplaintAccept c){
		String sql="insert into cs_complaint_accept(complaint_type,accept_no,order_no,cwbstate,flowordertype,currentbrannch,content,complaint_state,accpet_time,phone_one,province) values(?,?,?,?,?,?,?,?,?,?,?)";
		this.jt.update(sql,c.getComplaintType(),c.getAcceptNo(),c.getOrderNo(),c.getCwbstate(),c.getFlowordertype(),c.getCurrentBranch(),c.getQueryContent(),c.getComplaintState(),c.getAcceptTime(),c.getPhoneOne(),c.getProvence());
	}
	
	public void saveComplainWorkOrderF(CsComplaintAccept c){
		String sql="insert into cs_complaint_accept(complaint_type,accept_no,order_no,cwbstate,flowordertype,currentbrannch,complaint_state,cod_org_id,complaint_user,complaint_one_level,complaint_two_level,complaint_result,content,accpet_time,phone_one,province) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		this.jt.update(sql,c.getComplaintType(),c.getAcceptNo(),c.getOrderNo(),c.getCwbstate(),c.getFlowordertype(),c.getCurrentBranch(),c.getComplaintState(),c.getCodOrgId(),c.getComplaintUser(),c.getComplaintOneLevel(),c.getComplaintTwoLevel(),c.getComplaintResult(),c.getContent(),c.getAcceptTime(),c.getPhoneOne(),c.getProvence());
	}
	
	
	public void updateComplainWorkOrderF(CsComplaintAccept c){
		String sql="update cs_complaint_accept set complaint_type=?,order_no=?,cwbstate=?,currentbrannch=?,complaint_state=?,cod_org_id=?,complaint_user=?,complaint_one_level=?,complaint_two_level=?,complaint_result=?,content=?,accpet_time=?,phone_one=?,province=? where accept_no=?";
		this.jt.update(sql,c.getComplaintType(),c.getOrderNo(),c.getCwbstate(),c.getCurrentBranch(),c.getComplaintState(),c.getCodOrgId(),c.getComplaintUser(),c.getComplaintOneLevel(),c.getComplaintTwoLevel(),c.getComplaintResult(),c.getContent(),c.getAcceptTime(),c.getPhoneOne(),c.getProvence(),c.getAcceptNo());
	}
	
	
	
	
	
	
	
	
	private final class CsComplaintAcceptRowMapper implements RowMapper<CsComplaintAccept>{

		@Override
		public CsComplaintAccept mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			CsComplaintAccept c= new CsComplaintAccept();
			c.setAcceptNo(rs.getString("accept_no"));
			c.setAcceptTime(rs.getString("accpet_time"));
			c.setComplaintState(rs.getInt("complaint_state"));
			c.setOrderNo(rs.getString("order_no"));
			c.setContent(rs.getString("content"));
			c.setComplaintType(rs.getInt("complaint_type"));
			c.setPhoneOne(rs.getString("phone_one"));
			c.setCurrentBranch(rs.getString("currentbrannch"));
			c.setCwbstate(rs.getInt("cwbstate"));
			c.setCodOrgId(rs.getInt("cod_org_id"));
			c.setComplaintUser(rs.getString("complaint_user"));
			c.setComplaintOneLevel(rs.getInt("complaint_one_level"));
			c.setComplaintTwoLevel(rs.getInt("complaint_two_level"));
			c.setComplaintResult(rs.getInt("complaint_result"));
			
			return c;
		}
		
	}
	
	/*public List<CsComplaintAccept> findGoOnacceptWO(String cwb){
		String sql="select * from cs_complaint_accept where order_no=?";
		return this.jt.query(sql, new CsComplaintAcceptRowMapper(),cwb);
		
		
	} 
	
	public List<CsComplaintAccept> findAllCs(){
		String sql="select * from cs_complaint_accept";
		return this.jt.query(sql, new CsComplaintAcceptRowMapper());
		
		
	} */
	
	public List<CsComplaintAccept> findGoOnacceptWO(String phone){
		String sql="select * from cs_complaint_accept where phone_one=?";
		return this.jt.query(sql, new CsComplaintAcceptRowMapper(),phone);		
	} 
	public List<CsComplaintAccept> findGoOnacceptWOByCWB(String cwb){
		String sql="select * from cs_complaint_accept where order_no=?";
		return this.jt.query(sql, new CsComplaintAcceptRowMapper(),cwb);		
	} 
	public List<CsComplaintAccept> findGoOnacceptWOByCWBs(String cwbs,CsComplaintAcceptVO cv){
		String sql="select * from cs_complaint_accept where order_no in("+cwbs+") or complaint_state=? or complaint_type=? or complaint_one_level=? or cod_org_id=? or complaint_result=? or complaint_two_level=? or accpet_time between ? and ?";
		return this.jt.query(sql, new CsComplaintAcceptRowMapper(),cv.getComplaintState(),cv.getComplaintType(),cv.getComplaintOneLevel(),cv.getCodOrgId(),cv.getComplaintResult(),cv.getComplaintTwoLevel(),cv.getBeginRangeTime(),cv.getEndRangeTime());		
	} 

	public CsComplaintAccept findGoOnacceptWOByWorkOrder(String workorder){
		String sql="select * from cs_complaint_accept where accept_no=?";
		return this.jt.queryForObject(sql, new CsComplaintAcceptRowMapper(),workorder);		
	} 
	
	public List<CsComplaintAccept> refreshWO(int somstate){
	String sql="select * from cs_complaint_accept  where complaint_state=?";
	return this.jt.query(sql,new CsComplaintAcceptRowMapper(),somstate);
	}
	
	public List<CsComplaintAccept> refreshWOFPage(){
	String sql="select * from cs_complaint_accept";
	return this.jt.query(sql,new CsComplaintAcceptRowMapper());
	}
	
}
