package cn.explink.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.GotoClassOld;

@Component
public class GotoClassOldDAO {

	private final class GotoClassOldRowMapper implements RowMapper<GotoClassOld> {
		@Override
		public GotoClassOld mapRow(ResultSet rs, int rowNum) throws SQLException {
			GotoClassOld gco = new GotoClassOld();
			gco.setId(rs.getLong("id"));
			gco.setGotoclassauditingid(rs.getLong("gotoclassauditingid"));
			gco.setNownumber(rs.getLong("nownumber"));
			gco.setYiliu(rs.getLong("yiliu"));
			gco.setLishi_weishenhe(rs.getLong("lishiweishenhe"));
			gco.setZanbuchuli(rs.getLong("zanbuchuli"));
			gco.setPeisong_chenggong(rs.getLong("peisongchenggong"));
			gco.setPeisong_chenggong_amount(rs.getBigDecimal("peisongchenggongamount"));
			gco.setPeisong_chenggong_pos_amount(rs.getBigDecimal("peisongchenggongposamount"));
			gco.setTuihuo(rs.getLong("tuihuo"));
			gco.setTuihuo_amount(rs.getBigDecimal("tuihuoamount"));
			gco.setBufentuihuo(rs.getLong("bufentuihuo"));
			gco.setBufentuihuo_amount(rs.getBigDecimal("bufentuihuoamount"));
			gco.setBufentuihuo_pos_amount(rs.getBigDecimal("bufentuihuoposamount"));
			gco.setZhiliu(rs.getLong("zhiliu"));
			gco.setZhiliu_amount(rs.getBigDecimal("zhiliuamount"));
			gco.setShangmentui_chenggong(rs.getLong("shangmentuichenggong"));
			gco.setShangmentui_chenggong_amount(rs.getBigDecimal("shangmentuichenggongamount"));
			gco.setShangmentui_jutui(rs.getLong("shangmentuijutui"));
			gco.setShangmentui_jutui_amount(rs.getBigDecimal("shangmentuijutuiamount"));
			gco.setShangmenhuan_chenggong(rs.getLong("shangmenhuanchenggong"));
			gco.setShangmenhuan_chenggong_amount(rs.getBigDecimal("shangmenhuanchenggongamount"));
			gco.setShangmenhuan_chenggong_pos_amount(rs.getBigDecimal("shangmenhuanchenggongposamount"));
			gco.setDiushi(rs.getLong("diushi"));
			gco.setDiushi_amount(rs.getBigDecimal("diushiamount"));
			gco.setShangmentui_chenggong_fare(rs.getBigDecimal("shangmentuichenggongfare") == null ? BigDecimal.ZERO : rs.getBigDecimal("shangmentuichenggongfare"));
			gco.setShangmentui_jutui_fare(rs.getBigDecimal("shangmentuijutuifare") == null ? BigDecimal.ZERO : rs.getBigDecimal("shangmentuijutuifare"));
			gco.setZhongzhuan(rs.getInt("zhongzhuan"));
			gco.setZhongzhuan_amount(rs.getBigDecimal("zhongzhuanamount") == null ? BigDecimal.ZERO : rs.getBigDecimal("zhongzhuanamount"));
			return gco;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void creGotoClassOld(GotoClassOld gco) {
		jdbcTemplate
				.update("insert into express_ops_goto_class_old (gotoclassauditingid,nownumber,"
						+ "yiliu,lishiweishenhe,zanbuchuli,peisongchenggong,peisongchenggongamount,peisongchenggongposamount,tuihuo,"
						+ "tuihuoamount,bufentuihuo,bufentuihuoamount,bufentuihuoposamount,zhiliu,zhiliuamount,shangmentuichenggong,"
						+ "shangmentuichenggongamount,shangmentuijutui,shangmentuijutuiamount,shangmenhuanchenggong,"
						+ "shangmenhuanchenggongamount,shangmenhuanchenggongposamount,diushi,diushiamount,shangmentuichenggongfare,shangmentuijutuifare,zhongzhuan,zhongzhuanamount) values (?,?,?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?,?)",
						gco.getGotoclassauditingid(), gco.getNownumber(), gco.getYiliu(), gco.getLishi_weishenhe(), gco.getZanbuchuli(), gco.getPeisong_chenggong(), gco.getPeisong_chenggong_amount(),
						gco.getPeisong_chenggong_pos_amount(), gco.getTuihuo(), gco.getTuihuo_amount(), gco.getBufentuihuo(), gco.getBufentuihuo_amount(), gco.getBufentuihuo_pos_amount(),
						gco.getZhiliu(), gco.getZhiliu_amount(), gco.getShangmentui_chenggong(), gco.getShangmentui_chenggong_amount(), gco.getShangmentui_jutui(), gco.getShangmentui_jutui_amount(),
						gco.getShangmenhuan_chenggong(), gco.getShangmenhuan_chenggong_amount(), gco.getShangmenhuan_chenggong_pos_amount(), gco.getDiushi(), gco.getDiushi_amount(),
						gco.getShangmentui_chenggong_fare(), gco.getShangmentui_jutui_fare(),gco.getZhongzhuan(),gco.getZhongzhuan_amount());
	}

	public List<GotoClassOld> getGotoClassOld(long id) {
		return jdbcTemplate.query("SELECT * FROM express_ops_goto_class_old where gotoclassauditingid=?", new GotoClassOldRowMapper(), id);
	}
}
