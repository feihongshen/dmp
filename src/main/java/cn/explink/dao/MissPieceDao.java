package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.MissPiece;
import cn.explink.util.Page;

@Component
public class MissPieceDao {
	private final class MissPieceMapper implements RowMapper<MissPiece> {
		@Override
		public MissPiece mapRow(ResultSet rs, int rowNum) throws SQLException {
			MissPiece missPiece=new MissPiece();
			missPiece.setCreatetime(rs.getString("createtime"));
			missPiece.setCreuserid(rs.getLong("creuserid"));
			missPiece.setCustomerid(rs.getLong("customerid"));
			missPiece.setCwb(rs.getString("cwb"));
			missPiece.setCwbordertypeid(rs.getLong("cwbordertypeid"));
			missPiece.setDescribeinfo(rs.getString("describeinfo"));
			missPiece.setFlowordertype(rs.getLong("flowordertype"));
			missPiece.setId(rs.getLong("id"));
			missPiece.setCallbackbranchid(rs.getLong("callbackbranchid"));
			missPiece.setQuestionno(rs.getString("questionno"));
			missPiece.setFilepath(rs.getString("filepath"));
			missPiece.setState(rs.getString("state"));
			return missPiece;
		}
	}
	@Autowired
	JdbcTemplate jdbcTemplate;
	//根据条件查询丢失(分页)
	public List<MissPiece>  findMissPieces(long page,String cwbs,long customerid,long cwbordertypeid,long callbackbranchid,String begindate,String enddate){
		String sql="select * from express_ops_lose_back where 1=1 and state=1 ";
		if (cwbs.length()>0) {
			sql+=" and cwb IN("+cwbs+")";
		}
		if (customerid>0) {
			sql+=" and customerid="+customerid;
		}
		if (cwbordertypeid>0) {
			sql+="  and cwbordertypeid="+cwbordertypeid;
		}
		if (callbackbranchid>0) {
			sql+="  and callbackbranchid="+callbackbranchid;
		}
		if (begindate!="") {
			sql+=" and createtime>='"+begindate+"'";
		}
		System.out.println(enddate=="");
		System.out.println(enddate);
		if (enddate!="") {
			sql+=" and createtime<='"+enddate+"'";
		}
		if (page>0) {
			sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		}
		try {
			List<MissPiece> missPieces=this.jdbcTemplate.query(sql, new MissPieceMapper());
			return missPieces;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	} 
	//根据订单号查询丢失
	public List<MissPiece> findMissPieceByCwb(String cwb){
		String sql="select * from express_ops_lose_back where cwb=?";
		
			try {
				return this.jdbcTemplate.query(sql, new MissPieceMapper(), cwb);
			} catch (DataAccessException e) {
				return null;
			}
			

		
	}
	//根据id号查询丢失
	public List<MissPiece> findMissPieceById(long id){
		String sql="select * from express_ops_lose_back where id=?";
		
		try {
			return this.jdbcTemplate.query(sql, new MissPieceMapper(), id);
		} catch (DataAccessException e) {
			return null;
		}
		
		
		
	}
	//向表里面插入数据
	public void insertintoMissPiece(final String cwb,final long callbackbranchid,final String describe,final String filepath,final String questionno,final String createtime,final long customerid,final long cwbtypeid,final long flowordertype,final long userid
){
		String sql="insert into express_ops_lose_back(cwb,callbackbranchid,describeinfo,filepath,questionno,createtime,customerid,cwbordertypeid,flowordertype,creuserid,state) values(?,?,?,?,?,?,?,?,?,?,?)";
		try {
			this.jdbcTemplate.update(sql,new PreparedStatementSetter() {
				
				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setString(1, cwb);
					ps.setLong(2, callbackbranchid);
					ps.setString(3, describe);
					ps.setString(4, filepath);
					ps.setString(5, questionno);
					ps.setString(6, createtime);
					ps.setLong(7,customerid );
					ps.setLong(8,cwbtypeid );
					ps.setLong(9,flowordertype );
					ps.setLong(10,userid );
					ps.setLong(11, 1);
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
/*	//修改丢失件的状态1为有效，0为无效
	public long updateState(String cwbs){
		String sql="update express_ops_lose_back set state=0 where cwb IN('"+cwbs+"')";
		try {
			int k=this.jdbcTemplate.update(sql);
			return k;
		} catch (Exception e) {
			return 0;
		}
	}*/
	//修改丢失件的状态1为有效，0为无效
	public long updateStateAdd(String cwbs){
		String sql="delete  from  express_ops_lose_back  where id=?";
		try {
			int k=this.jdbcTemplate.update(sql, cwbs);
			return k;
		} catch (Exception e) {
			return 0;
		}
	}
	//问题件创建的时候如果已经有丢失件的话更新丢失件里面的问题件号
	public void updateQuestionNo(String questionNo,String cwb){
		String sql="update express_ops_lose_back set questionno=? where cwb=?";
			this.jdbcTemplate.update(sql, questionNo,cwb);
	}
}
