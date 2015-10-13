package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import cn.explink.domain.Truck;
import cn.explink.util.Page;

@Component
public class TruckDAO {

	private final class TruckRowMapper implements RowMapper<Truck> {

		@Override
		public Truck mapRow(ResultSet rs, int rowNum) throws SQLException {
			// TODO Auto-generated method stub
			Truck truck = new Truck();
			truck.setTruckid(rs.getInt("truckid"));
			truck.setTruckno(rs.getString("truckno"));
			truck.setTrucktype(rs.getString("trucktype"));
			truck.setTruckoil(rs.getFloat("truckoil"));
			truck.setTruckway(rs.getString("truckway"));
			truck.setTruckkm(rs.getFloat("truckkm"));
			truck.setTruckstartkm(rs.getFloat("truckstartkm"));
			truck.setTruckdriver(rs.getInt("truckdriver"));
			truck.setTruckflag(rs.getInt("truckflag"));
			return truck;
		}

	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Truck> getAllTruck() {
		return jdbcTemplate.query("select * from express_set_truck", new TruckRowMapper());
	}

	private String getTruckByPageWhereSql(String sql, String truckno, String trucktype) {

		if (truckno.length() > 0 && trucktype.length() > 0) {
			sql += " where truckno like '%" + truckno + "%' and trucktype like '%" + trucktype + "%'";
		} else if (truckno.length() > 0) {
			sql += " where truckno like '%" + truckno + "%'";
		} else if (trucktype.length() > 0) {
			sql += " where trucktype like '%" + trucktype + "%' ";
		}

		return sql;
	}

	public List<Truck> getTruckByPage(long page, String truckno, String trucktype) {
		String sql = "select * from express_set_truck";
		sql = this.getTruckByPageWhereSql(sql, truckno, trucktype);
		sql += " order by truckflag desc limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		List<Truck> truckList = jdbcTemplate.query(sql, new TruckRowMapper());
		return truckList;
	}

	public long getTruckCount(String truckno, String trucktype) {
		String sql = "select count(1) from express_set_truck";
		sql = this.getTruckByPageWhereSql(sql, truckno, trucktype);
		return jdbcTemplate.queryForInt(sql);
	}

	public List<Truck> getTruckByTrucknoCheck(String truckno) {
		List<Truck> truckList = jdbcTemplate.query("SELECT * from express_set_truck where truckno=?", new TruckRowMapper(), truckno);
		return truckList;
	}

	public Truck getTruckByTruckid(long truckid) {
		return jdbcTemplate.queryForObject("select * from express_set_truck where truckid =?", new TruckRowMapper(), truckid);
	}

	public List<Truck> getTruckByTruckname(String truckname) {
		return jdbcTemplate.query("select * from express_set_truck where truckno =?", new TruckRowMapper(), truckname);
	}

	public void creTruck(final Truck truck) {

		jdbcTemplate.update("insert into express_set_truck(truckno,trucktype,truckoil,truckway,truckkm,truckstartkm,truckdriver," + "truckflag) values(?,?,?,?,?,?,?,?)",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						// TODO Auto-generated method stub
						ps.setString(1, truck.getTruckno());
						ps.setString(2, truck.getTrucktype());
						ps.setFloat(3, truck.getTruckoil());
						ps.setString(4, truck.getTruckway());
						ps.setFloat(5, truck.getTruckkm());
						ps.setFloat(6, truck.getTruckstartkm());
						ps.setInt(7, truck.getTruckdriver());
						ps.setInt(8, truck.getTruckflag());
					}
				});
	}

	public void saveTruck(final Truck truck) {

		jdbcTemplate.update("update express_set_truck set truckno=?,trucktype=?,truckoil=?,truckway=?,truckkm=?,truckstartkm=?,truckdriver=?,truckflag=? where truckid=?",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						// TODO Auto-generated method stub
						ps.setString(1, truck.getTruckno());
						ps.setString(2, truck.getTrucktype());
						ps.setFloat(3, truck.getTruckoil());
						ps.setString(4, truck.getTruckway());
						ps.setFloat(5, truck.getTruckkm());
						ps.setFloat(6, truck.getTruckstartkm());
						ps.setInt(7, truck.getTruckdriver());
						ps.setInt(8, truck.getTruckflag());
						ps.setLong(9, truck.getTruckid());
					}
				});

	}

	public void delTruck(long truckid) {
		jdbcTemplate.update("update express_set_truck set truckflag=(truckflag+1)%2 where truckid=" + truckid);
	}

}
