package cn.explink.b2c.tools;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.aspect.SystemInstallOperation;
import cn.explink.pos.tools.EmployeeInfo;

@Component
public class JiontDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class PosMapper implements RowMapper<JointEntity> {
		@Override
		public JointEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
			JointEntity en = new JointEntity();
			en.setJoint_num(rs.getInt("joint_num"));
			en.setJoint_property(rs.getString("joint_property"));
			en.setJoint_remark(rs.getString("joint_remark"));
			en.setState(rs.getInt("state"));
			return en;
		}
	}

	@Cacheable(value = "jointCache", key = "#key", condition = "#result ne null")
	public JointEntity getJointEntity(int key) {
		JointEntity jointEntity = null;
		try {
			String sql = "select * from express_set_joint where joint_num=?";
			jointEntity = jdbcTemplate.queryForObject(sql, new PosMapper(), key);
		} catch (Exception e) {
			 // e.printStackTrace();
		}
		return jointEntity;

	}

	public List<JointEntity> getJointEntityList() {
		List<JointEntity> list = null;
		try {
			String sql = "select * from express_set_joint where state=1  ";
			String jointnums = "";
			for (B2cEnum b2c : B2cEnum.values()) {
				if (b2c.getApi_type() == 2) {
					jointnums += b2c.getKey() + ",";
				}
			}
			sql += " and jointnum in (" + jointnums.substring(0, jointnums.length() - 1) + ")";

			list = jdbcTemplate.query(sql, new PosMapper());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list == null ? new ArrayList<JointEntity>() : list;

	}

	public JointEntity getJointEntityByCompanyname(String companyname) {
		JointEntity jointEntity = null;
		try {
			String strs = "\"companyname\":\"" + companyname + "\"";
			String sql = "select * from express_set_joint where joint_property like '%" + strs + "%' ";
			jointEntity = jdbcTemplate.queryForObject(sql, new PosMapper());
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return jointEntity;

	}

	public JointEntity getCountByClientID(String clientID) {
		try {
			String strs = "\"clientID\":\"" + clientID + "\"";
			String sql = "select * from express_set_joint where joint_property like '%" + strs + "%' ";
			return jdbcTemplate.queryForObject(sql, new PosMapper());
		} catch (Exception e) {
			// e.printStackTrace();
			return null;
		}

	}

	public JointEntity getJointEntityByKeys(String keys, String customerid) {
		JointEntity jointEntity = null;
		try {
			String strs = "\"customerids\":\"" + customerid + "\"";
			String sql = "select * from express_set_joint where joint_property like '%" + strs + "%' and joint_num in (" + keys + ") ";
			jointEntity = jdbcTemplate.queryForObject(sql, new PosMapper());
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return jointEntity;

	}

	public List<JointEntity> getJointEntityByB2c() {
		try {
			String sql = "select * from express_set_joint where joint_num>20000 and joint_num<30000";
			return jdbcTemplate.query(sql, new PosMapper());
		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * pos对接范围1000-2000
	 * 
	 * @return
	 */
	public List<JointEntity> getJointEntityByPos() {
		try {
			String sql = "select * from express_set_joint where joint_num>1000 and joint_num<2000";
			return jdbcTemplate.query(sql, new PosMapper());
		} catch (Exception e) {
			return null;
		}

	}
	@SystemInstallOperation
	@CacheEvict(value = "jointCache", key = "#joint_num")
	public void UpdateState(int joint_num, int state) {
		String sql = " update express_set_joint set state=? where joint_num=? ";
		jdbcTemplate.update(sql, state, joint_num);

	}
	@SystemInstallOperation
	@CacheEvict(value = "jointCache", key = "#jointEntity.joint_num")
	public void Update(JointEntity jointEntity) {
		String sql = " update express_set_joint set joint_property=? where joint_num=? ";
		jdbcTemplate.update(sql, jointEntity.getJoint_property(), jointEntity.getJoint_num());

	}
	@SystemInstallOperation
	@CacheEvict(value = "jointCache", key = "#jointEntity.joint_num")
	public void Create(final JointEntity jointEntity) {
		jdbcTemplate.update("insert into express_set_joint(joint_num,joint_property,state" + ") values(?,?,?)", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				// TODO Auto-generated method stub
				ps.setInt(1, jointEntity.getJoint_num());
				ps.setString(2, jointEntity.getJoint_property());
				ps.setInt(3, 1);
			}
		});

	}

	public EmployeeInfo getEmployeeInfo(String username) {
		try {
			String sql = " select u.userid,u.username,u.password,u.realname,b.branchname,b.branchid,b.branchaddress,b.branchphone,b.branchmobile,b.branchcode "
					+ " from express_set_branch b,express_set_user u where u.branchid=b.branchid and u.username=? and u.userdeleteflag=1 and employeestatus=1 ";
			return jdbcTemplate.queryForObject(sql, new EmployeeMapper(), username);
		} catch (Exception e) {
			return null;
		}

	}


	public EmployeeInfo getEmployeeByUserNameAndPassWord(String username, String password) {
		try {
			String sql = " select u.userid,u.username,u.password,u.realname,b.branchname,b.branchid,b.branchaddress,b.branchcode,b.branchphone,b.branchmobile "
					+ " from express_set_branch b,express_set_user u where u.branchid=b.branchid and u.username=? and u.password=? and u.userdeleteflag=1  and employeestatus=1   ";
			return jdbcTemplate.queryForObject(sql, new EmployeeMapper(), username, password);
		} catch (Exception e) {
			return null;
		}

	}
	

	private final class EmployeeMapper implements RowMapper<EmployeeInfo> {
		@Override
		public EmployeeInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			EmployeeInfo em = new EmployeeInfo();
			em.setUserid(rs.getInt("userid"));
			em.setUsername(rs.getString("username"));
			em.setPassword(rs.getString("password"));
			em.setRealname(rs.getString("realname"));
			em.setBranchid(rs.getString("branchid"));
			em.setBranchname(rs.getString("branchname"));
			em.setBranchphone(rs.getString("branchphone"));
			em.setBranchaddress(rs.getString("branchaddress"));
			em.setBranchmobile(rs.getString("branchmobile"));
			em.setBranchcode(rs.getString("branchcode"));
			return em;
		}
	}

	/**
	 * 判断对接的开关
	 * 
	 * @param key
	 * @return
	 */
	public int getStateByJointKey(int key) {
		try {
			return getJointEntity(key).getState();
		} catch (Exception e) {
			return 0;
		}

	}
	@CacheEvict(value = "jointCache", allEntries = true)
	public void updateCache(){
		
	}
	//根据承运上编码查询接口设置
	public JointEntity getJointEntityByShipperNo(String key, int joint_num, int isTpsSendFlag) {
		JointEntity jointEntity = null;
		try {
			String sql = "select * from express_set_joint where joint_property like '%\""+key+"\"%' "
					+ "and joint_property like '%\"isTpsSendFlag\":"+isTpsSendFlag+",%' and joint_num<>"+joint_num+" limit 0,1";
			jointEntity = jdbcTemplate.queryForObject(sql, new PosMapper());
		} catch (Exception e) {
			 // e.printStackTrace();
		}
		return jointEntity;
	}

	//根据承运商编码查询有效接口设置    【修改】去掉缓存注释【周欢】2016-07-15
	@Cacheable(value = "jointCache", key = "#key", condition = "#result ne null")
	public JointEntity getJointEntityByShipperNoForUse(String key) {
		JointEntity jointEntity = null;
		try {
			String sql = "select * from express_set_joint where joint_property like '%"+key+"%' "
					+ "and joint_property like '%\"isTpsSendFlag\":1,%' and state=1 limit 0,1";
			jointEntity = jdbcTemplate.queryForObject(sql, new PosMapper());
		} catch (Exception e) {
			 // e.printStackTrace();
		}
		return jointEntity;
	}

	//【新增】根据承运商编码和客户id查询接口设置【周欢】2016-07-13
	@Cacheable(value = "jointCache", key = "#shipperNo+#customerids", condition = "#result ne null")
	public JointEntity getDetialJointEntityByShipperNoForUse(String shipperNo,String customerids) {
		String sql = "select * from express_set_joint where joint_property like '%"+shipperNo+"%' "
				+ "and joint_property like '%\"isTpsSendFlag\":1,%' and joint_property like '%"+customerids+"%' and state=1 limit 0,1";
		return this.jdbcTemplate.queryForObject(sql,new PosMapper());
	}
}
