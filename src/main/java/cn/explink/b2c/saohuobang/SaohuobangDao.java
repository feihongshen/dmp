package cn.explink.b2c.saohuobang;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.b2c.saohuobang.xml.RequestOrder;

@Component
public class SaohuobangDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class infoMap implements RowMapper<Saohuobanginfo> {
		@Override
		public Saohuobanginfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			Saohuobanginfo en = new Saohuobanginfo();
			en.setCargoInfo(rs.getString("cargoInfo"));
			en.setCwb(rs.getString("cwb"));
			en.setDealTime(rs.getString("dealTime"));
			en.setId(rs.getInt("id"));
			en.setStoreAddress(rs.getString("storeAddress"));
			en.setStoreName(rs.getString("storeName"));
			en.setStorePhone(rs.getString("storePhone"));
			en.setTranscwb(rs.getString("transcwb"));
			en.setCommand(rs.getString("command"));
			en.setConsigneeaddress(rs.getString("consigneeaddress"));
			en.setConsigneemobile(rs.getString("consigneemobile"));
			en.setConsigneename(rs.getString("consigneename"));
			en.setMoney(rs.getBigDecimal("money"));
			return en;
		}
	}

	public void getInsertInfo(final RequestOrder rootnote, final String info) {

		jdbcTemplate.update("insert into express_saohuobang_information (cargoInfo,cwb,dealTime,"
				+ "storeAddress,storeName,transcwb,storePhone,consigneename,consigneeaddress,consigneemobile,command,money,state)" + "values(?,?,?,?,?,?,?,?,?,?,?,?,?)",
				new PreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, info);
						ps.setString(2, rootnote.getMailNo());
						ps.setString(3, rootnote.getOrderCreateTime());
						ps.setString(4, rootnote.getStoreAddress());
						ps.setString(5, rootnote.getStoreName());
						ps.setString(6, rootnote.getTxLogisticID());
						ps.setString(7, rootnote.getStoreTel());
						ps.setString(8, rootnote.getReceiver().getName());
						ps.setString(9, rootnote.getReceiver().getAddress());
						ps.setString(10, rootnote.getReceiver().getMobile() + " " + rootnote.getReceiver().getPhone());
						ps.setString(11, rootnote.getItemsSendDate());
						ps.setString(12, rootnote.getItemsTakePrice());
						ps.setInt(13, 1);
					}

				});

	}

	public void DeleteCwbAndTranscwbtodetail(String cwb, String oldcwb) {
		String sql = "update  express_saohuobang_information set cwb=?  where cwb =?";
		jdbcTemplate.update(sql, cwb, oldcwb);
	}

	public void getInsertAgain(final List<Saohuobanginfo> list_info, final String cwb) {

		jdbcTemplate.update("insert into express_saohuobang_information (cargoInfo,cwb,dealTime,"
				+ "storeAddress,storeName,transcwb,storePhone,consigneename,consigneeaddress,consigneemobile,command,money,state)" + "values(?,?,?,?,?,?,?,?,?,?,?,?,?)",
				new PreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, list_info.get(0).getCargoInfo());
						ps.setString(2, cwb);
						ps.setString(3, list_info.get(0).getDealTime());
						ps.setString(4, list_info.get(0).getStoreAddress());
						ps.setString(5, list_info.get(0).getStoreName());
						ps.setString(6, list_info.get(0).getTranscwb());
						ps.setString(7, list_info.get(0).getStorePhone());
						ps.setString(8, list_info.get(0).getConsigneename());
						ps.setString(9, list_info.get(0).getConsigneeaddress());
						ps.setString(10, list_info.get(0).getConsigneemobile());
						ps.setString(11, list_info.get(0).getCommand());
						ps.setLong(12, list_info.get(0).getMoney().longValue());
						ps.setInt(13, 1);

					}

				});

	}

	public void getInfoForinsert(String oldcwb, String string) {
		String sql = "update   express_saohuobang_information set state=" + string + "  where cwb ='" + oldcwb + "'";
		jdbcTemplate.update(sql);
	}

	/**
	 * 根据订单号 查询
	 * 
	 * @param cwbs
	 * @return
	 */
	public List<Saohuobanginfo> getSaohuobangOrderByCwbs(String cwbs) {
		String sql = "select * from  express_saohuobang_information where cwb in(" + cwbs + ") and state=1";
		return jdbcTemplate.query(sql, new infoMap());
	}

}
