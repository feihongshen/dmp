package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.SearcheditInfo;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.Page;

@Component
public class searchEditCwbInfoDao {

	private final class EditInfoRowMapper implements RowMapper<SearcheditInfo> {
		@Override
		public SearcheditInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			SearcheditInfo info = new SearcheditInfo();
			info.setCrename(rs.getLong("crename"));
			info.setCretime(rs.getString("cretime"));
			info.setCwb(rs.getString("cwb"));
			info.setId(rs.getLong("id"));
			info.setState(rs.getString("state"));

			info.setNewcommand(rs.getString("newcommand"));// 供货商要求
			info.setNewremark(rs.getString("newremark"));// 备注
			info.setNewconsigneeaddress(rs.getString("newconsigneeaddress"));// 地址
			info.setOldconsigneeaddress(rs.getString("oldconsigneeaddress"));// 地址
			info.setNewconsigneemobile(rs.getString("newconsigneemobile"));// 电话
			info.setNewResendtime(rs.getString("newResendtime"));// 发货时间
			info.setNewconsigneename(rs.getString("newconsigneename"));// 姓名
			info.setDeliverybranchid(rs.getLong("deliverybranchid"));
			info.setOldResendtime(rs.getString("oldResendtime"));// 发货时间
			info.setOldconsigneemobile(rs.getString("oldconsigneemobile"));// 电话
			info.setOldcommand(rs.getString("oldcommand"));// 供货商要求
			info.setOldconsigneename(rs.getString("oldconsigneename"));// 姓名
			info.setOldremark(rs.getString("oldremark"));// 备注
			info.setNewbranchid(rs.getLong("newbranchid"));//修改的配送站点
			info.setOldexceldeliverid(rs.getLong("oldexceldeliverid")); //匹配小件员ID
			info.setOldexceldeliver(rs.getString("oldexceldeliver")); //匹配小件员
			info.setNewexceldeliverid(rs.getLong("newexceldeliverid")); //匹配小件员ID
			info.setNewexceldeliver(rs.getString("newexceldeliver")); //匹配小件员
			return info;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 查询订单信息修改时间范围内的订单详情
	 * 
	 * @param startime
	 * @param endtime
	 * @return
	 */
	public List<SearcheditInfo> getInfoByCretime(long page, String startime, String endtime) {
		try {
			String sql = "SELECT * from express_ops_editcwbinfo where cretime>=? and cretime<=?  ";
			sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
			return jdbcTemplate.query(sql, new EditInfoRowMapper(), startime, endtime);
		} catch (Exception ee) {
			return null;
		}
	}

	/**
	 * 插入订单信息修改表
	 * 
	 * @param info
	 * @param editname
	 * @param editmobile
	 * @param editcommand
	 * @param editaddress
	 * @param resendtime
	 * @param userid
	 */
	public void createEditInfo(CwbOrder info,long newbranchid, String editname, String editmobile, String editcommand, String editaddress, String resendtime, long userid, String remark, long newexceldeliverid, String newexceldeliver) {
		jdbcTemplate
				.update("insert into express_ops_editcwbinfo(cwb,deliverybranchid,oldconsigneename,newconsigneename,oldconsigneemobile,newconsigneemobile,oldconsigneeaddress,newconsigneeaddress,oldResendtime,newResendtime,oldcommand,newcommand,cretime,crename,oldremark,newremark,newbranchid,oldexceldeliverid,oldexceldeliver,newexceldeliverid,newexceldeliver) "
						+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", info.getCwb(), info.getDeliverybranchid() == 0 ? info.getNextbranchid() : info.getDeliverybranchid(), info.getConsigneename(),
						editname, info.getConsigneemobile(), editmobile, info.getConsigneeaddress(), editaddress, info.getResendtime(), resendtime, info.getCustomercommand(), editcommand,
						DateTimeUtil.getNowTime(), userid, info.getCwbremark(), remark,newbranchid,info.getExceldeliverid(), info.getExceldeliver(), newexceldeliverid, newexceldeliver);
	}

	/**
	 * 查询重复
	 * 
	 * @param cwb
	 * @return
	 */
	public long countEditInfoBycwb(String cwb) {
		return jdbcTemplate.queryForLong("SELECT count(1) from express_ops_editcwbinfo where cwb=?  ", cwb);
	}

	/**
	 * 查询总数
	 * 
	 * @param cwb
	 * @return
	 */
	public long countEditInfo(String startime, String endtime) {
		String sql = "SELECT count(1) from express_ops_editcwbinfo where cretime>=? and cretime<=?";
		return jdbcTemplate.queryForLong(sql, startime, endtime);
	}

	/**
	 * 删除订单
	 * 
	 * @param cwb
	 * @return
	 */
	public long deleteEditInfo(String cwb) {
		return jdbcTemplate.update("delete from  express_ops_editcwbinfo where cwb=?  ", cwb);
	}

	public List<SearcheditInfo> getAllInfoByCretime(String startime, String endtime) {
		try {
			String sql = "SELECT * from express_ops_editcwbinfo where cretime>=? and cretime<=?  ";
			return jdbcTemplate.query(sql, new EditInfoRowMapper(), startime, endtime);
		} catch (Exception ee) {
			return null;
		}
	}
	
	
	public List<SearcheditInfo> getEditInfoByCwbs (String cwbs) {
		String sql = "SELECT * FROM express_ops_editcwbinfo WHERE cwb in ("+cwbs+")";
		return this.jdbcTemplate.query(sql, new EditInfoRowMapper());
	}
}
