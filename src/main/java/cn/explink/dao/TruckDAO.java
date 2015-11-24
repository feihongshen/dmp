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
			truck.setTruckdriver(rs.getInt("truckdriver"));
			truck.setTruckflag(rs.getInt("truckflag"));
			truck.setTruckTerminalId(rs.getString("truck_terminal_id"));
			truck.setTruckSimNum(rs.getString("truck_sim_num"));
			return truck;
		}

	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Truck> getAllTruck() {
		return jdbcTemplate.query("select * from express_set_truck where truckflag =1 ", new TruckRowMapper());
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
		Truck truck=new Truck();
		truck.setTruckno("_________");
		try {
			return jdbcTemplate.queryForObject("select * from express_set_truck where truckid =?", new TruckRowMapper(), truckid);
		} catch (Exception e) {
			return truck;
		}
	}

	public List<Truck> getTruckByTruckname(String truckname) {
		return jdbcTemplate.query("select * from express_set_truck where truckno =?", new TruckRowMapper(), truckname);
	}

	public void creTruck(final Truck truck) {

		jdbcTemplate.update("insert into express_set_truck(truckno,trucktype,truckdriver,truckflag,truck_terminal_id,truck_sim_num) values(?,?,?,?,?,?)", new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				// TODO Auto-generated method stub
				ps.setString(1, truck.getTruckno());
				ps.setString(2, truck.getTrucktype());
				ps.setInt(3, truck.getTruckdriver());
				ps.setInt(4, truck.getTruckflag());
				ps.setString(5, truck.getTruckTerminalId());
				ps.setString(6, truck.getTruckSimNum());
			}
		});
	}

	public void saveTruck(final Truck truck) {
		String sql="update express_set_truck set truckno=?,trucktype=?,truckdriver=?,truckflag=?,truck_terminal_id=?,truck_sim_num=? where truckid=?";
		jdbcTemplate.update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				// TODO Auto-generated method stub
				ps.setString(1, truck.getTruckno());
				ps.setString(2, truck.getTrucktype());
				ps.setInt(3, truck.getTruckdriver());
				ps.setInt(4, truck.getTruckflag());
				ps.setString(5, truck.getTruckTerminalId());
				ps.setString(6, truck.getTruckSimNum());
				ps.setLong(7, truck.getTruckid());
			}
		});
		
		System.out.println(sql);

	}

	public void delTruck(long truckid) {
		jdbcTemplate.update("update express_set_truck set truckflag=(truckflag+1)%2 where truckid=?", truckid);
	}

}
