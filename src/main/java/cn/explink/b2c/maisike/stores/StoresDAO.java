package cn.explink.b2c.maisike.stores;

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

import cn.explink.b2c.maisike.branchsyn_json.Stores;

@Component
public class StoresDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class StoresMapper implements RowMapper<Stores> {
		@Override
		public Stores mapRow(ResultSet rs, int rowNum) throws SQLException {
			Stores stores = new Stores();
			stores.setId(rs.getLong("id"));
			stores.setSid(rs.getString("sid"));
			stores.setSaddress(rs.getString("saddress"));
			stores.setSarea(rs.getString("sarea"));
			stores.setSname(rs.getString("sname"));
			stores.setSphone(rs.getString("sphone"));
			stores.setB2cenum(rs.getString("b2cenum"));

			return stores;
		}
	}

	/**
	 * 查询是否含有合单的情况 运单号相同，订单号不同
	 * 
	 * @param xmlMap
	 * @param customer_id
	 *            true 超过24小时有重复订单提示
	 * @return
	 */
	public boolean isExistsBranchs(String sid) {
		String sql = "select count(1)  from maisike_set_branch where  sid='" + sid + "' ";
		int counts = jdbcTemplate.queryForInt(sql);
		return counts > 0;
	}

	/**
	 * 查询迈思可 表信息
	 * 
	 * @return
	 */
	public List<Stores> getMaisiBranchList() {
		String sql = "select * from maisike_set_branch";
		List<Stores> cwborderList = jdbcTemplate.query(sql, new StoresMapper());
		return cwborderList;
	}

	/**
	 * 查询迈思可 表信息
	 * 
	 * @return
	 */
	public Stores getMaisiBranchById(int id) {
		try {
			String sql = "select * from maisike_set_branch where id=?";
			return jdbcTemplate.queryForObject(sql, new StoresMapper(), id);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			return null;
		}

	}

	public void insertMaisikeStores(final Stores store, final int b2cenum) {

		jdbcTemplate.update("insert into maisike_set_branch (sid,sname,sarea,saddress,sphone,b2cenum) values(?,?,?,?,?,?)", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, store.getSid());
				ps.setString(2, store.getSname());
				ps.setString(3, store.getSarea());
				ps.setString(4, store.getSaddress());
				ps.setString(5, store.getSphone());
				ps.setString(6, String.valueOf(b2cenum));

			}
		});

	}

	public void updateById(long id, String b2cenum) {
		try {
			jdbcTemplate.update("update maisike_set_branch set b2cenum=? where id=?", b2cenum, id);
		} catch (DataAccessException e) {

		}

	}

	public void updateInfoById(String sid, String sname, String sarea, String saddress, String sphone) {
		try {
			jdbcTemplate.update("update maisike_set_branch set sname=?,sarea=?,saddress=?,sphone=?  where sid=?", sname, sarea, saddress, sphone, sid);
		} catch (DataAccessException e) {

		}

	}

}
