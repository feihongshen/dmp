package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import cn.explink.domain.CsComplaintAccept;
import cn.explink.domain.CsComplaintAcceptVO;
import cn.explink.domain.CsConsigneeInfo;
import cn.explink.domain.CsShenSuChat;
import cn.explink.util.Page;

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
			cci.setPhoneonTwo(rs.getString("phone_two"));
			cci.setMailBox(rs.getString("mail_box"));
			cci.setConsigneeType(rs.getInt("consignee_type"));
			cci.setSex(rs.getInt("sex"));
			cci.setProvince(rs.getString("province"));
			cci.setCity(rs.getString("city"));
			cci.setName(rs.getString("name"));
			cci.setContactLastTime(rs.getString("contact_last_time"));
			cci.setContactNum(rs.getInt("contact_num"));
			cci.setCallerremark(rs.getString("callerremark"));

			return cci;
		}

	}

	public void save(CsConsigneeInfo cci){
		String sql="insert into cs_consignee_info(phone_one,consignee_type,sex,province,city,name,contact_last_time,contact_num) values(?,?,?,?,?,?,?,?)";
		this.jt.update(sql,cci.getPhoneonOne(),cci.getConsigneeType(),cci.getSex(),cci.getProvince(),cci.getCity(),cci.getName(),cci.getContactLastTime(),cci.getContactNum());
	}

	public void saveAllCsConsigneeInfo(CsConsigneeInfo cci){
		String sql="insert into cs_consignee_info(phone_one,phone_two,consignee_type,sex,province,city,name,contact_last_time,contact_num,mail_box,callerremark) values(?,?,?,?,?,?,?,?,?,?,?)";
		this.jt.update(sql,cci.getPhoneonOne(),cci.getPhoneonTwo(),cci.getConsigneeType(),cci.getSex(),cci.getProvince(),cci.getCity(),cci.getName(),cci.getContactLastTime(),cci.getContactNum(),cci.getMailBox(),cci.getCallerremark());
	}


	public void editAllCsConsigneeInfo(CsConsigneeInfo cci){
		String sql="update cs_consignee_info set";
		StringBuffer sb = new StringBuffer();
		if((cci.getPhoneonOne()!=null)&&(cci.getPhoneonOne().length()>0)){
			sb.append(" phone_one='"+cci.getPhoneonOne()+"',");
		}
		if((cci.getPhoneonTwo()!=null)&&(cci.getPhoneonTwo().length()>0)){
			sb.append(" phone_two='"+cci.getPhoneonTwo()+"',");
		}
		if(cci.getConsigneeType()>0){
			sb.append(" consignee_type="+cci.getConsigneeType()+",");
		}
		if(cci.getSex()>0){
			sb.append(" sex="+cci.getSex()+",");
		}
		if((cci.getProvince()!=null)&&(cci.getProvince().length()>0)){
			sb.append(" province='"+cci.getProvince()+"',");
		}
		if((cci.getCity()!=null)&&(cci.getCity().length()>0)){
			sb.append(" city='"+cci.getCity()+"',");
		}

		if((cci.getContactLastTime()!=null)&&(cci.getContactLastTime().length()>0)){
			sb.append(" contact_last_time='"+cci.getContactLastTime()+"',");
		}
		if(cci.getContactNum()>0){
			sb.append(" contact_num="+cci.getContactNum()+",");
		}
		if((cci.getMailBox()!=null)&&(cci.getMailBox().length()>0)){
			sb.append(" mail_box='"+cci.getMailBox()+"',");
		}
		if((null!=cci.getCallerremark())&&!cci.getCallerremark().equals("")){
			sb.append(" callerremark='"+cci.getCallerremark()+"',");
		}
		if((cci.getName()!=null)&&(cci.getName().length()>0)){
			sb.append(" name='"+cci.getName()+"'");
		}


		sb.append(" where id="+cci.getId());

		sql+=sb.toString();
		this.jt.update(sql);
	}

	public List<CsConsigneeInfo> queryListCsConsigneeInfo(String phone){
		String sql="select * from cs_consignee_info where phone_one=?";
		return this.jt.query(sql, new WorkOrderRowMapper(),phone);

	}

	public void remove(String phone){
		String sql="delete from cs_consignee_info where phone_one=?";
		this.jt.update(sql,phone);
	}

	public CsConsigneeInfo queryByPhoneNum(String phoneonOne){
		String sql="select * from cs_consignee_info where phone_one=?";
		CsConsigneeInfo cf=null;
		List<CsConsigneeInfo> c=this.jt.query(sql,new WorkOrderRowMapper(),phoneonOne);
		if((c.size()>0)&&(c!=null)){
			cf=c.get(0);
		}
		return cf;

	}
	public CsConsigneeInfo queryById(int id){
		String sql="select * from cs_consignee_info where id=?";
		CsConsigneeInfo	cf=this.jt.queryForObject(sql,new WorkOrderRowMapper(),id);
			return cf;
	}
	public List<CsConsigneeInfo> queryAllCsConsigneeInfo(long page ,String name,String phone,int type){
		String sql="select * from cs_consignee_info where id>0";
		StringBuilder sb = new StringBuilder();
		if(type>=0){
			sb.append(" and consignee_type="+type);
		}
		if((name!=null)&&(name.length()>0)){
			sb.append(" and name='"+name+"'");
		}
		if((phone!=null)&&(phone.length()>0)){
				sb.append(" and phone_one='"+phone+"'");
		}
		sql+=sb.toString();
		sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		return this.jt.query(sql, new WorkOrderRowMapper());
	}

	public List<CsConsigneeInfo> queryAllCsConsigneeInfo1(String name,String phone,int type){
		String sql="select * from cs_consignee_info where id>"+0;
		StringBuilder sb = new StringBuilder();
		if(type>=0){
			sb.append(" and consignee_type="+type);
		}
		if((name!=null)&&(name.length()>0)){
			sb.append(" and name='"+name+"'");
		}
		if((phone!=null)&&(phone.length()>0)){
				sb.append(" and phone_one='"+phone+"'");
		}
		sql+=sb.toString();
			return this.jt.query(sql, new WorkOrderRowMapper());
	}

	public long getCsConsigneeInfocount(String name,String phone,int type){
		String sql="select count(*) from cs_consignee_info where id>"+0;
		StringBuilder sb = new StringBuilder();
		if(type>=0){
			sb.append(" and consignee_type="+type);
		}
		if((name!=null)&&(name.length()>0)){
			sb.append(" and name='"+name+"'");
		}
		if((phone!=null)&&(phone.length()>0)){
				sb.append(" and phone_one='"+phone+"'");
		}
		sql+=sb.toString();
		return this.jt.queryForLong(sql);

	}
	public CsConsigneeInfo queryByPhoneone(String phoneonOne){
		CsConsigneeInfo cf =null;
		try {
			String sql="select * from cs_consignee_info where phone_one=? limit 1";
			cf = this.jt.queryForObject(sql,new WorkOrderRowMapper(),phoneonOne);
		} catch (Exception e) {
			return null;
		}
		return cf;


	}



	public void savequeryworkorderForm(CsComplaintAccept c){
		String sql="insert into cs_complaint_accept(handle_user,complaint_type,accept_no,order_no,cwbstate,flowordertype,currentbrannch,querycontent,complaint_state,accpet_time,phone_one,province,customerid,cuijian_num) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		this.jt.update(sql,c.getHandleUser(),c.getComplaintType(),c.getAcceptNo(),c.getOrderNo(),c.getCwbstate(),c.getFlowordertype(),c.getCurrentBranch(),c.getQueryContent(),c.getComplaintState(),c.getAcceptTime(),c.getPhoneOne(),c.getProvence(),c.getCustomerid(),c.getCuijianNum());
	}

	public void saveComplainWorkOrderF(CsComplaintAccept c){
		String sql="insert into cs_complaint_accept(handle_user,accept_no,order_no,cwbstate,flowordertype,currentbrannch,complaint_state,cod_org_id,complaint_user,complaint_one_level,complaint_two_level,complaint_result,content,accpet_time,phone_one,province,customerid,cuijian_num) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		this.jt.update(sql,c.getHandleUser(),c.getAcceptNo(),c.getOrderNo(),c.getCwbstate(),c.getFlowordertype(),c.getCurrentBranch(),c.getComplaintState(),c.getCodOrgId(),c.getComplaintUser(),c.getComplaintOneLevel(),c.getComplaintTwoLevel(),c.getComplaintResult(),c.getContent(),c.getAcceptTime(),c.getPhoneOne(),c.getProvence(),c.getCustomerid(),c.getCuijianNum());
	}
	/**
	 * 批量导入新增/处理工单
	 * @param c
	 */
	public void saveComplainWorkOrderForImport(CsComplaintAccept c){     
		String sql="insert into cs_complaint_accept(handle_user,accept_no,order_no,cwbstate,flowordertype,currentbrannch,complaint_state,cod_org_id,complaint_user,complaint_one_level,complaint_two_level,complaint_result,content,accpet_time,phone_one,province,customerid,cuijian_num,jiean_user,heshi_user,remark,heshi_time,jiean_remark,jiean_time) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?  ,?,?,?,?,?,?)";
		this.jt.update(sql,c.getHandleUser(),c.getAcceptNo(),c.getOrderNo(),c.getCwbstate(),c.getFlowordertype(),c.getCurrentBranch(),c.getComplaintState(),c.getCodOrgId(),c.getComplaintUser(),c.getComplaintOneLevel(),c.getComplaintTwoLevel(),c.getComplaintResult(),c.getContent(),c.getAcceptTime(),c.getPhoneOne(),c.getProvence(),c.getCustomerid(),c.getCuijianNum()  ,c.getJieanUser(),c.getHeshiUser(),c.getRemark(),c.getHeshiTime(),c.getJieanremark(),c.getJieanTime());
	}

	public void updateComplainWorkOrderF(CsComplaintAccept c){
		String sql="update cs_complaint_accept set";
		StringBuilder sb = new StringBuilder();
			if(c.getComplaintState()>=0){
				sb.append(" complaint_state="+c.getComplaintState());
			}
			if(c.getComplaintResult()>=0){
				sb.append(" ,complaint_result="+c.getComplaintResult());
			}
			if((c.getContent()!=null)&&(c.getContent().length()>0)){
				sb.append(" ,content='"+c.getContent()+"'");
			}

			if((c.getAcceptNo()!=null)&&(c.getAcceptNo().length()>0)){
				sb.append(" where accept_no='"+c.getAcceptNo()+"'");
			}
			sql=sql+sb.toString();
			this.jt.update(sql);
	}








	private final class CsComplaintAcceptRowMapper implements RowMapper<CsComplaintAccept>{

		@Override
		public CsComplaintAccept mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			CsComplaintAccept c= new CsComplaintAccept();
			c.setId(rs.getInt("id"));
			c.setAcceptNo(rs.getString("accept_no"));
			c.setAcceptTime(rs.getString("accpet_time"));
			c.setComplaintState(rs.getInt("complaint_state"));
			c.setOrderNo(rs.getString("order_no"));
			c.setContent(rs.getString("content"));
			c.setProvence(rs.getString("province"));
			c.setComplaintType(rs.getInt("complaint_type"));
			c.setPhoneOne(rs.getString("phone_one"));
			c.setCurrentBranch(rs.getString("currentbrannch"));
			c.setCwbstate(rs.getInt("cwbstate"));
			c.setCodOrgId(rs.getInt("cod_org_id"));
			c.setComplaintUser(rs.getString("complaint_user"));
			c.setComplaintOneLevel(rs.getInt("complaint_one_level"));
			c.setComplaintTwoLevel(rs.getInt("complaint_two_level"));
			c.setComplaintResult(rs.getInt("complaint_result"));
			c.setRemark(rs.getString("remark"));
			c.setCustomerid(rs.getLong("customerid"));
			c.setHeshiTime(rs.getString("heshi_time"));
			c.setCustomerid(rs.getLong("customerid"));
			c.setHandleUser(rs.getString("handle_user"));
			c.setShensuremark(rs.getString("shensu_remark"));
			c.setJieanchongshenremark(rs.getString("jieanchongshen_remark"));
			c.setJieanTime(rs.getString("jiean_time"));
			c.setComplaintTime(rs.getString("complaint_time"));
			c.setJieanremark(rs.getString("jiean_remark"));
			c.setJieanchongshenTime(rs.getString("jieanchongshen_time"));
			c.setHeshiUser(rs.getString("heshi_user"));
			c.setJieanUser(rs.getString("jiean_user"));
			c.setShensuUser(rs.getString("shensu_user"));
			c.setChongshenUser(rs.getString("chongshen_user"));
			c.setIfpunish(rs.getInt("if_punish"));
			c.setCuijianNum(rs.getInt("cuijian_num"));
			c.setDownloadchongshenpath(rs.getString("downloadchongshenpath"));
			c.setDownloadheshipath(rs.getString("downloadheshipath"));
			c.setDownloadjieanpath(rs.getString("downloadjieanpath"));
			c.setDownloadshensupath(rs.getString("downloadshensupath"));
			c.setQueryContent(rs.getString("querycontent"));


			return c;
		}

	}

	@SuppressWarnings("unused")
	private final class HandleUserRowMapper implements RowMapper<CsComplaintAccept>{

		@Override
		public CsComplaintAccept mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			CsComplaintAccept c= new CsComplaintAccept();
			c.setHandleUser(rs.getString("handle_user"));
			return c;
		}

	}

	public List<CsComplaintAccept> findGoOnacceptWO(String phone,long page){
		String sql="select * from cs_complaint_accept where phone_one=? order by complaint_state,accpet_time asc";
		sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		return this.jt.query(sql, new CsComplaintAcceptRowMapper(),phone);
	}
	public long findGoOnacceptWOCount(String phone){
		String sql="select count(1) from cs_complaint_accept where phone_one=?";
		return this.jt.queryForLong(sql,phone);
	}
	public List<CsComplaintAccept> findGoOnacceptWOByCWB(String cwb,long page){
		String sql="select * from cs_complaint_accept where order_no=? order by complaint_state,accpet_time asc";
		sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		return this.jt.query(sql, new CsComplaintAcceptRowMapper(),cwb);
	}

	public long findGoOnacceptWOByCWBCount(String cwb){
		String sql="select count(1) from cs_complaint_accept where order_no=?";
		return this.jt.queryForLong(sql,cwb);
	}

public List<CsComplaintAccept> findGoOnacceptWOByCWBs(long page,String cwbs,CsComplaintAcceptVO cv,String workorders){
		String sql="select * from cs_complaint_accept where id>0";
		StringBuilder sb = new StringBuilder();
		if(!cwbs.equals("")){
			sb.append(" and order_no in("+cwbs+")");
		}
		if(!workorders.equals("")){
			sb.append(" and accept_no in("+workorders+")");
		}
		if(cv.getComplaintState()!=-1){
			sb.append(" and complaint_state="+cv.getComplaintState());
		}
		/*if(cv.getComplaintType()!=-1){
			sb.append(" and complaint_type="+cv.getComplaintType());
		}*/
		if(cv.getComplaintOneLevel()!=-1){
			sb.append(" and complaint_one_level="+cv.getComplaintOneLevel());
		}
		if(cv.getCodOrgId()!=-1){
			sb.append(" and cod_org_id="+cv.getCodOrgId());
		}if(cv.getComplaintResult()!=-1){
			sb.append(" and complaint_result="+cv.getComplaintResult());
		}
		if(cv.getIfpunish()>0){
			sb.append(" and if_punish="+cv.getIfpunish());
		}
		if((cv.getHandleUser()!=null)&&(cv.getHandleUser().length()>0)){
			sb.append(" and handle_user='"+cv.getHandleUser()+"'");
		}
		if(cv.getComplaintTwoLevel()>0){
			sb.append(" and complaint_two_level="+cv.getComplaintTwoLevel());
		}
		if((cv.getBeginRangeTime()!=null)&&(cv.getBeginRangeTime().length()>0)&&(cv.getEndRangeTime()!=null)&&(cv.getEndRangeTime().length()>0)){
			sb.append(" and accpet_time between '"+cv.getBeginRangeTime()+"'" +" and '"+cv.getEndRangeTime()+"'");
		}
		if (cv.getCustomerIds() != null && !cv.getCustomerIds().isEmpty()) {
			sb.append(" and customerid in (" + cv.getCustomerIds() + ")");
		}

		sql+=sb.toString();
		sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		System.out.println(sql);
		return this.jt.query(sql, new CsComplaintAcceptRowMapper());
	}

public List<CsComplaintAccept> findGoOnacceptWOByCWBs1(String cwbs,CsComplaintAcceptVO cv,String workorders){
	String sql="select * from cs_complaint_accept where id>0";
	StringBuilder sb = new StringBuilder();
	if(!cwbs.equals("")){
		sb.append(" and order_no in("+cwbs+")");
	}
	if(!workorders.equals("")){
		sb.append(" and accept_no in("+workorders+")");
	}
	if(cv.getComplaintState()!=-1){
		sb.append(" and complaint_state="+cv.getComplaintState());
	}
	/*if(cv.getComplaintType()!=-1){
		sb.append(" and complaint_type="+cv.getComplaintType());
	}*/
	if(cv.getComplaintOneLevel()!=-1){
		sb.append(" and complaint_one_level="+cv.getComplaintOneLevel());
	}
	if(cv.getCodOrgId()!=-1){
		sb.append(" and cod_org_id="+cv.getCodOrgId());
	}if(cv.getComplaintResult()!=-1){
		sb.append(" and complaint_result="+cv.getComplaintResult());
	}
	if(cv.getIfpunish()>0){
		sb.append(" and if_punish="+cv.getIfpunish());
	}
	if((cv.getHandleUser()!=null)&&(cv.getHandleUser().length()>0)){
		sb.append(" and handle_user='"+cv.getHandleUser()+"'");
	}
	if(cv.getComplaintTwoLevel()>0){
		sb.append(" and complaint_two_level="+cv.getComplaintTwoLevel());
	}
	if((cv.getBeginRangeTime()!=null)&&(cv.getBeginRangeTime().length()>0)&&(cv.getEndRangeTime()!=null)&&(cv.getEndRangeTime().length()>0)){
		sb.append(" and accpet_time between '"+cv.getBeginRangeTime()+"'" +" and '"+cv.getEndRangeTime()+"'");
	}

	sql+=sb.toString();
	return this.jt.query(sql, new CsComplaintAcceptRowMapper());
}


public long findGoOnacceptWOByCWBsCount(String cwbs,CsComplaintAcceptVO cv,String workorders){
	String sql="select count(*) from cs_complaint_accept where id>0";
	StringBuilder sb = new StringBuilder();
	if(!cwbs.equals("")){
		sb.append(" and order_no in("+cwbs+")");
	}
	if(!workorders.equals("")){
		sb.append(" and accept_no in("+workorders+")");
	}
	if(cv.getComplaintState()!=-1){
		sb.append(" and complaint_state="+cv.getComplaintState());
	}
	/*if(cv.getComplaintType()!=-1){
		sb.append(" and complaint_type="+cv.getComplaintType());
	}*/
	if(cv.getComplaintOneLevel()!=-1){
		sb.append(" and complaint_one_level="+cv.getComplaintOneLevel());
	}
	if(cv.getCodOrgId()!=-1){
		sb.append(" and cod_org_id="+cv.getCodOrgId());
	}if(cv.getComplaintResult()!=-1){
		sb.append(" and complaint_result="+cv.getComplaintResult());
	}
	if(cv.getIfpunish()>0){
		sb.append(" and if_punish="+cv.getIfpunish());
	}
	if((cv.getHandleUser()!=null)&&(cv.getHandleUser().length()>0)){
		sb.append(" and handle_user='"+cv.getHandleUser()+"'");
	}
	if(cv.getComplaintTwoLevel()>0){
		sb.append(" and complaint_two_level="+cv.getComplaintTwoLevel());
	}
	if((cv.getBeginRangeTime()!=null)&&(cv.getBeginRangeTime().length()>0)&&(cv.getEndRangeTime()!=null)&&(cv.getEndRangeTime().length()>0)){
		sb.append(" and accpet_time between '"+cv.getBeginRangeTime()+"'" +" and '"+cv.getEndRangeTime()+"'");
	}
	if (cv.getCustomerIds() != null && !cv.getCustomerIds().isEmpty()) {
		sb.append(" and customerid in (" + cv.getCustomerIds() + ")");
	}

	sql+=sb.toString();
	System.out.println(sql);
	return this.jt.queryForLong(sql);
}
public List<CsComplaintAccept> findGoOnacceptWOByCWBsAdd(String ncwbs,String gongdancwb,long gongdantype,long gongdanstate,long complainresultcontent,long complainedmechanism,String begindateh,String enddateh,long tousuonesort,long tousutwosort){
	String sql="select * from cs_complaint_accept where id>0  ";
	StringBuilder sb = new StringBuilder();
	if((ncwbs.trim()!=null)&&(ncwbs.trim().length()>0)){
		sb.append(" and order_no in(").append(ncwbs).append(")");
	}
	if (!gongdancwb.equals("")) {
		sb.append("  and accept_no='"+gongdancwb+"'");
	}
	if(gongdanstate>0){
		sb.append(" and complaint_state="+gongdanstate);
	}
	if(gongdantype>0){
		sb.append(" and complaint_type="+gongdantype);
	}
	if(tousuonesort>0){
		sb.append(" and complaint_one_level="+tousuonesort);
	}
	if(complainedmechanism>0){
		sb.append(" and cod_org_id="+complainedmechanism);
	}if(complainresultcontent>0){
		sb.append(" and complaint_result="+complainresultcontent);
	}
	if(tousutwosort>0){
		sb.append(" and complaint_two_level="+tousutwosort);
	}
	if((begindateh!=null)&&(begindateh.length()>0)&&(enddateh!=null)&&(enddateh.length()>0)){
		sb.append(" and accpet_time between '"+begindateh+"'" +" and '"+enddateh+"'");
	}

	sql+=sb.toString();

	return this.jt.query(sql, new CsComplaintAcceptRowMapper());
}

	public CsComplaintAccept findGoOnacceptWOByWorkOrder(String workorder){
		CsComplaintAccept lcs;
		try {
			String sql="select * from cs_complaint_accept where accept_no='"+workorder+"'";
			lcs = this.jt.queryForObject(sql, new CsComplaintAcceptRowMapper());
		} catch (DataAccessException e) {

			return null;
		}

		return 	lcs;
	}

	public List<CsComplaintAccept> refreshWO(int somstate){
	String sql="select * from cs_complaint_accept  where complaint_state=?";
	return this.jt.query(sql,new CsComplaintAcceptRowMapper(),somstate);
	}

	public List<CsComplaintAccept> refreshWOFPage(){
	String sql="select distinct handle_user from cs_complaint_accept";
	return this.jt.query(sql,new HandleUserRowMapper());
	}


	public void ChangecomplaintState(CsComplaintAccept cca){
		String sql="update cs_complaint_accept set";
		StringBuilder sb = new StringBuilder();
		if((null != cca.getComplaintTime()) && (cca.getComplaintTime().length()>0)){
			sb.append(" complaint_time='"+cca.getComplaintTime()+"',");
		}
		if((cca.getJieanTime()!=null)&&(cca.getJieanTime().length()>0)){
			sb.append(" jiean_time='"+cca.getJieanTime()+"',");
		}
		if((cca.getJieanchongshenremark()!=null)&&(cca.getJieanchongshenremark().length()>0)){
			sb.append(" jieanchongshen_remark='"+cca.getJieanchongshenremark()+"',");
		}
		if((cca.getShensuremark()!=null)&&(cca.getShensuremark().length()>0)){
			sb.append(" shensu_remark='"+cca.getShensuremark()+"',");
		}
		if((cca.getJieanremark()!=null)&&(cca.getJieanremark().length()>0)){
			sb.append(" jiean_remark='"+cca.getJieanremark()+"',");
		}
		if((cca.getRemark()!=null)&&(cca.getRemark().length()>0)){
			sb.append(" remark='"+cca.getRemark()+"',");
		}
		if(cca.getComplaintState()>-1){
			sb.append(" complaint_state="+cca.getComplaintState()+",");
		}
		if(cca.getComplaintResult()>-1){
			sb.append(" complaint_result="+cca.getComplaintResult()+",");
		}
		if((cca.getHeshiTime()!=null)&&(cca.getHeshiTime().length()>0)){
			sb.append(" heshi_time='"+cca.getHeshiTime()+"',");
		}
		if((cca.getJieanchongshenTime()!=null)&&(cca.getJieanchongshenTime().length()>0)){
			sb.append(" jieanchongshen_time='"+cca.getJieanchongshenTime()+"',");
		}
		if((cca.getDownloadchongshenpath()!=null)&&(cca.getDownloadchongshenpath().length()>0)){
			sb.append(" downloadchongshenpath='"+cca.getDownloadchongshenpath()+"',");
		}
		if((cca.getDownloadheshipath()!=null)&&(cca.getDownloadheshipath().length()>0)){
			sb.append(" downloadheshipath='"+cca.getDownloadheshipath()+"',");
		}
		if((cca.getDownloadshensupath()!=null)&&(cca.getDownloadshensupath().length()>0)){
			sb.append(" downloadshensupath='"+cca.getDownloadshensupath()+"',");
		}
		if((cca.getDownloadjieanpath()!=null)&&(cca.getDownloadjieanpath().length()>0)){
			sb.append(" downloadjieanpath='"+cca.getDownloadjieanpath()+"',");
		}
		if((cca.getHandleUser()!=null)&&(cca.getHandleUser().length()>0)){
			sb.append(" handle_user='"+cca.getHandleUser()+"'");
		}
		if((cca.getHeshiUser()!=null)&&(cca.getHeshiUser().length()>0)){
			sb.append(" heshi_user='"+cca.getHeshiUser()+"'");
		}
		if((cca.getJieanUser()!=null)&&(cca.getJieanUser().length()>0)){
			sb.append(" jiean_user='"+cca.getJieanUser()+"'");
		}
		if((cca.getShensuUser()!=null)&&(cca.getShensuUser().length()>0)){
			sb.append(" shensu_user='"+cca.getShensuUser()+"'");
		}
		if((cca.getChongshenUser()!=null)&&(cca.getChongshenUser().length()>0)){
			sb.append(" chongshen_user='"+cca.getChongshenUser()+"'");
		}
		if(cca.getId()>=0){
			sb.append(" where id="+cca.getId());
		}
		sql+=sb.toString();

		this.jt.update(sql);
	}

	public CsComplaintAccept getCsComplaintAccept(int id){
		String sql="select * from cs_complaint_accept where id=?";
		return this.jt.queryForObject(sql, new CsComplaintAcceptRowMapper(),id);
	}

	public void ChangeGoOnAcceptcomplaintState(CsComplaintAccept cca){
		String sql="update cs_complaint_accept set complaint_state=? where id=?";
		this.jt.update(sql,cca.getComplaintState(),cca.getId());

	}

	/**
	 * 更新工单催件次数
	 * @param workOrderNo
	 */
	public void updateMsgNum(String workOrderNo) {
		String sql = "UPDATE `cs_complaint_accept` SET cuijian_num = cuijian_num + 1 WHERE accept_no=?";
		this.jt.update(sql,workOrderNo);
	}

	/**
	 * 根据工单号，查询工单记录
	 * @param workOrderNo
	 * @return
	 */
	public CsComplaintAccept getMsgByWorkOrderNo(String workOrderNo){
		String sql = "SELECT * FROM cs_complaint_accept cca WHERE cca.`accept_no`=?";
		return this.jt.queryForObject(sql,new CsComplaintAcceptRowMapper(),workOrderNo);
	}

	//机构核实，机构申诉专用
	public void OrgChangecomplaintState(CsComplaintAccept cca){
		String sql="update cs_complaint_accept set";
		StringBuilder sb = new StringBuilder();
		if((null != cca.getComplaintTime()) && (cca.getComplaintTime().length()>0)){
			sb.append(" complaint_time='"+cca.getComplaintTime()+"',");
		}
		if((cca.getJieanTime()!=null)&&(cca.getJieanTime().length()>0)){
			sb.append(" jiean_time='"+cca.getJieanTime()+"',");
		}
		if((cca.getJieanchongshenremark()!=null)&&(cca.getJieanchongshenremark().length()>0)){
			sb.append(" jieanchongshen_remark='"+cca.getJieanchongshenremark()+"',");
		}
		if((cca.getShensuremark()!=null)&&(cca.getShensuremark().length()>0)){
			sb.append(" shensu_remark='"+cca.getShensuremark()+"',");
		}
		if((cca.getJieanremark()!=null)&&(cca.getJieanremark().length()>0)){
			sb.append(" jiean_remark='"+cca.getJieanremark()+"',");
		}
		if((cca.getRemark()!=null)&&(cca.getRemark().length()>0)){
			sb.append(" remark='"+cca.getRemark()+"',");
		}
		if(cca.getComplaintState()>0){
			sb.append(" complaint_state="+cca.getComplaintState()+",");
		}
		/*	if(cca.getComplaintResult()>-1){
			sb.append(" complaint_result="+cca.getComplaintResult()+",");
		}*/
		if((cca.getHeshiTime()!=null)&&(cca.getHeshiTime().length()>0)){
			sb.append(" heshi_time='"+cca.getHeshiTime()+"',");
		}
		if((cca.getJieanchongshenTime()!=null)&&(cca.getJieanchongshenTime().length()>0)){
			sb.append(" jieanchongshen_time='"+cca.getJieanchongshenTime()+"',");
		}
		if((cca.getDownloadchongshenpath()!=null)&&(cca.getDownloadchongshenpath().length()>0)){
			sb.append(" downloadchongshenpath='"+cca.getDownloadchongshenpath()+"',");
		}
		if((cca.getDownloadheshipath()!=null)&&(cca.getDownloadheshipath().length()>0)){
			sb.append(" downloadheshipath='"+cca.getDownloadheshipath()+"',");
		}
		if((cca.getDownloadshensupath()!=null)&&(cca.getDownloadshensupath().length()>0)){
			sb.append(" downloadshensupath='"+cca.getDownloadshensupath()+"',");
		}
		if((cca.getDownloadjieanpath()!=null)&&(cca.getDownloadjieanpath().length()>0)){
			sb.append(" downloadjieanpath='"+cca.getDownloadjieanpath()+"',");
		}
		if((cca.getHandleUser()!=null)&&(cca.getHandleUser().length()>0)){
			sb.append(" handle_user='"+cca.getHandleUser()+"'");
		}
		if((cca.getHeshiUser()!=null)&&(cca.getHeshiUser().length()>0)){
			sb.append(" heshi_user='"+cca.getHeshiUser()+"'");
		}
		if((cca.getJieanUser()!=null)&&(cca.getJieanUser().length()>0)){
			sb.append(" jiean_user='"+cca.getJieanUser()+"'");
		}
		if((cca.getShensuUser()!=null)&&(cca.getShensuUser().length()>0)){
			sb.append(" shensu_user='"+cca.getShensuUser()+"'");
		}
		if((cca.getChongshenUser()!=null)&&(cca.getChongshenUser().length()>0)){
			sb.append(" chongshen_user='"+cca.getChongshenUser()+"'");
		}
		if(cca.getId()>=0){
			sb.append(" where id="+cca.getId());
		}
		sql+=sb.toString();

		this.jt.update(sql);
	}
	public void updateIsfine(String order_no,long isfine){
		try {
			String sql="update cs_complaint_accept set if_punish=? where accept_no=?";
			this.jt.update(sql,isfine,order_no);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
	}

	public void updateConnum(int newcnnum,String phone){
		this.jt.update("update cs_consignee_info set contact_num="+newcnnum+" where phone_one='"+phone+"'");
	}

	public List<CsComplaintAccept> findGoOnacceptWOByCWBsnew(long page,
			String cwbs, CsComplaintAcceptVO cv, String workorders,
			long branchid) {
		String sql="select * from cs_complaint_accept where id>0 and cod_org_id="+branchid;
		StringBuilder sb = new StringBuilder();
		if(!cwbs.equals("")){
			sb.append(" and order_no in("+cwbs+")");
		}
		if(!workorders.equals("")){
			sb.append(" and accept_no in("+workorders+")");
		}
		if(cv.getComplaintState()!=-1){
			sb.append(" and complaint_state="+cv.getComplaintState());
		}
		/*if(cv.getComplaintType()!=-1){
			sb.append(" and complaint_type="+cv.getComplaintType());
		}*/
		if(cv.getComplaintOneLevel()!=-1){
			sb.append(" and complaint_one_level="+cv.getComplaintOneLevel());
		}
		/*if(cv.getCodOrgId()!=-1){
			sb.append(" and cod_org_id="+cv.getCodOrgId());
		}*/if(cv.getComplaintResult()!=-1){
			sb.append(" and complaint_result="+cv.getComplaintResult());
		}
		if(cv.getIfpunish()>0){
			sb.append(" and if_punish="+cv.getIfpunish());
		}
		if((cv.getHandleUser()!=null)&&(cv.getHandleUser().length()>0)){
			sb.append(" and handle_user='"+cv.getHandleUser()+"'");
		}
		if(cv.getComplaintTwoLevel()>0){
			sb.append(" and complaint_two_level="+cv.getComplaintTwoLevel());
		}
		if((cv.getBeginRangeTime()!=null)&&(cv.getBeginRangeTime().length()>0)&&(cv.getEndRangeTime()!=null)&&(cv.getEndRangeTime().length()>0)){
			sb.append(" and accpet_time between '"+cv.getBeginRangeTime()+"'" +" and '"+cv.getEndRangeTime()+"'");
		}
		if (cv.getCustomerIds() != null && !cv.getCustomerIds().isEmpty()) {
			sb.append(" and customerid in (" + cv.getCustomerIds() + ")");
		}

		sql+=sb.toString();
		sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		return this.jt.query(sql, new CsComplaintAcceptRowMapper());
	}


	public List<CsComplaintAccept> findGoOnacceptWOByCWBsnewNoPage(
			String cwbs, CsComplaintAcceptVO cv, String workorders,
			long branchid) {
		String sql="select * from cs_complaint_accept where id>0 and cod_org_id="+branchid;
		StringBuilder sb = new StringBuilder();
		if(!cwbs.equals("")){
			sb.append(" and order_no in("+cwbs+")");
		}
		if(!workorders.equals("")){
			sb.append(" and accept_no in("+workorders+")");
		}
		if(cv.getComplaintState()!=-1){
			sb.append(" and complaint_state="+cv.getComplaintState());
		}
		/*if(cv.getComplaintType()!=-1){
			sb.append(" and complaint_type="+cv.getComplaintType());
		}*/
		if(cv.getComplaintOneLevel()!=-1){
			sb.append(" and complaint_one_level="+cv.getComplaintOneLevel());
		}
		/*if(cv.getCodOrgId()!=-1){
			sb.append(" and cod_org_id="+cv.getCodOrgId());
		}*/if(cv.getComplaintResult()!=-1){
			sb.append(" and complaint_result="+cv.getComplaintResult());
		}
		if(cv.getIfpunish()>0){
			sb.append(" and if_punish="+cv.getIfpunish());
		}
		if((cv.getHandleUser()!=null)&&(cv.getHandleUser().length()>0)){
			sb.append(" and handle_user='"+cv.getHandleUser()+"'");
		}
		if(cv.getComplaintTwoLevel()>0){
			sb.append(" and complaint_two_level="+cv.getComplaintTwoLevel());
		}
		if((cv.getBeginRangeTime()!=null)&&(cv.getBeginRangeTime().length()>0)&&(cv.getEndRangeTime()!=null)&&(cv.getEndRangeTime().length()>0)){
			sb.append(" and accpet_time between '"+cv.getBeginRangeTime()+"'" +" and '"+cv.getEndRangeTime()+"'");
		}

		sql+=sb.toString();
		return this.jt.query(sql, new CsComplaintAcceptRowMapper());
	}



	public long findGoOnacceptWOByCWBsCountnew(String cwbs,
			CsComplaintAcceptVO cv, String workorders, long branchid) {
		String sql="select count(*) from cs_complaint_accept where id>0 and cod_org_id="+branchid;
		StringBuilder sb = new StringBuilder();
		if(!cwbs.equals("")){
			sb.append(" and order_no in("+cwbs+")");
		}
		if(!workorders.equals("")){
			sb.append(" and accept_no in("+workorders+")");
		}
		if(cv.getComplaintState()!=-1){
			sb.append(" and complaint_state="+cv.getComplaintState());
		}
		/*if(cv.getComplaintType()!=-1){
			sb.append(" and complaint_type="+cv.getComplaintType());
		}*/
		if(cv.getComplaintOneLevel()!=-1){
			sb.append(" and complaint_one_level="+cv.getComplaintOneLevel());
		}
		/*if(cv.getCodOrgId()!=-1){
			sb.append(" and cod_org_id="+cv.getCodOrgId());
		}*/if(cv.getComplaintResult()!=-1){
			sb.append(" and complaint_result="+cv.getComplaintResult());
		}
		if(cv.getIfpunish()>0){
			sb.append(" and if_punish="+cv.getIfpunish());
		}
		if((cv.getHandleUser()!=null)&&(cv.getHandleUser().length()>0)){
			sb.append(" and handle_user='"+cv.getHandleUser()+"'");
		}
		if(cv.getComplaintTwoLevel()>0){
			sb.append(" and complaint_two_level="+cv.getComplaintTwoLevel());
		}
		if((cv.getBeginRangeTime()!=null)&&(cv.getBeginRangeTime().length()>0)&&(cv.getEndRangeTime()!=null)&&(cv.getEndRangeTime().length()>0)){
			sb.append(" and accpet_time between '"+cv.getBeginRangeTime()+"'" +" and '"+cv.getEndRangeTime()+"'");
		}
		if (cv.getCustomerIds() != null && !cv.getCustomerIds().isEmpty()) {
			sb.append(" and customerid in (" + cv.getCustomerIds() + ")");
		}

		sql+=sb.toString();

		return this.jt.queryForLong(sql);
	}
	
	private  class CsShenSuChatClass implements RowMapper<CsShenSuChat>{

		@Override
		public CsShenSuChat mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			CsShenSuChat cs = new CsShenSuChat();
			cs.setCreTime(rs.getString("cre_time"));
			cs.setCreChatContent(rs.getString("cre_chat_content"));
			cs.setCreUser(rs.getLong("cre_user"));
			cs.setAcceptNo(rs.getString("accept_no"));
			return cs;
		}
	}		
	public List<CsShenSuChat> getCsShenSuChatContentByAcceptNo(String acceptNo){
		String sql="select * from cs_shensu_chat where accept_no='"+acceptNo+"'";
		List<CsShenSuChat> cslist=jt.query(sql, new CsShenSuChatClass());
		return cslist;
		
	}
	
	public void huifuChat(String creTime,String creContent,long creUser,String acceptNo){
		String sql="insert into cs_shensu_chat(cre_time,cre_chat_content,cre_user,accept_no) values(?,?,?,?)";
		this.jt.update(sql,creTime,creContent,creUser,acceptNo);
	}
	
	public CsComplaintAccept getCsComplaintAcceptByAcceptNo(String workOrderNo){
		String sql = "SELECT * FROM cs_complaint_accept WHERE accept_no='"+workOrderNo+"'";
		CsComplaintAccept cc=null;
		try {
			 cc= this.jt.queryForObject(sql,new CsComplaintAcceptRowMapper());
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			return null;
		}
		return cc;
	}
}
