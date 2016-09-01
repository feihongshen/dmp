package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.core.utils.StringUtils;
import cn.explink.domain.ApplyEditCartypeVO;
import cn.explink.util.express.Page;

/**
 * 货物类型更改审核操作DAO类
 * @author zhili01.liang
 *
 */
@Component
public class ApplyEditCartypeDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class ApplyMapper implements RowMapper<ApplyEditCartypeVO> {
		@Override
		public ApplyEditCartypeVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			ApplyEditCartypeVO vo = new ApplyEditCartypeVO();
			vo.setId(rs.getLong("id"));
			vo.setCwb(rs.getString("cwb"));
			vo.setTranscwb(rs.getString("transcwb"));
			vo.setCustomerid(rs.getLong("customerid"));
			vo.setCustomername(rs.getString("customername"));
			vo.setDoType(rs.getInt("do_type"));
			vo.setOriginalCartype(rs.getString("original_cartype"));
			vo.setApplyCartype(rs.getString("apply_cartype"));
			vo.setCarrealweight(rs.getBigDecimal("carrealweight"));
			vo.setCarsize(rs.getString("carsize"));
			vo.setApplyBranchid(rs.getLong("apply_branchid"));
			vo.setApplyBranchname(rs.getString("apply_branchname"));
			vo.setApplyUserid(rs.getLong("apply_userid"));
			vo.setApplyUsername(rs.getString("apply_username"));
			vo.setApplyTime(rs.getString("apply_time"));
			vo.setReviewUserid(rs.getLong("review_userid"));
			vo.setReviewUsername(rs.getString("review_username"));
			vo.setReviewTime(rs.getString("review_time"));
			vo.setReviewStatus(rs.getInt("Review_Status"));
			vo.setRemark(rs.getString("remark"));
			return vo;
		}
	}

	/**
	 * 创建修改申请记录
	 * @param applyEditCartype
	 */
	public void createApplyEditCartype(ApplyEditCartypeVO applyEditCartype) {
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into EXPRESS_OPS_APPLYEDITCARTYPE (");
		sql.append(
				"   CWB, TRANSCWB, CUSTOMERID, CUSTOMERNAME, DO_TYPE, ORIGINAL_CARTYPE, APPLY_CARTYPE, CARREALWEIGHT,");
		sql.append("   CARSIZE, APPLY_BRANCHID, APPLY_BRANCHNAME, APPLY_USERID, APPLY_USERNAME, APPLY_TIME, ");
		sql.append("   REVIEW_USERID, REVIEW_USERNAME, REVIEW_TIME, REVIEW_STATUS,REMARK");
		sql.append("  ) values (");
		sql.append("  ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,? ");
		sql.append("  ) ");
		params.add(applyEditCartype.getCwb());
		params.add(applyEditCartype.getTranscwb());
		params.add(applyEditCartype.getCustomerid());
		params.add(applyEditCartype.getCustomername());
		params.add(applyEditCartype.getDoType());
		params.add(applyEditCartype.getOriginalCartype());
		params.add(applyEditCartype.getApplyCartype());
		params.add(applyEditCartype.getCarrealweight());
		params.add(applyEditCartype.getCarsize());
		params.add(applyEditCartype.getApplyBranchid());
		params.add(applyEditCartype.getApplyBranchname());
		params.add(applyEditCartype.getApplyUserid());
		params.add(applyEditCartype.getApplyUsername());
		params.add(applyEditCartype.getApplyTime());
		params.add(applyEditCartype.getReviewUserid());
		params.add(applyEditCartype.getReviewUsername());
		params.add(applyEditCartype.getReviewTime());
		params.add(applyEditCartype.getReviewStatus());
		params.add(applyEditCartype.getRemark());

		jdbcTemplate.update(sql.toString(), params.toArray());

	}

	public ApplyEditCartypeVO getApplyEditCartypeVOById(long id) {
		String sql = " select * from EXPRESS_OPS_APPLYEDITCARTYPE where id = ? limit 0,1 ";
		return jdbcTemplate.queryForObject(sql, new Object[] { id }, new ApplyMapper());
	}

	public List<ApplyEditCartypeVO> queryApplyEditCartype(String cwb, Integer reviewStatus, int begin, int interval) {
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from EXPRESS_OPS_APPLYEDITCARTYPE ");
		sql.append(" where 1=1 ");
		if (!StringUtils.isEmpty(cwb)) {
			sql.append(" and cwb = ? ");
			params.add(cwb);
		}
		if (reviewStatus != null) {
			sql.append(" and review_status = ? ");
			params.add(reviewStatus);
		}

		sql.append(" limit ? , ? ");
		params.add(begin);
		params.add(interval);
		return jdbcTemplate.query(sql.toString(), params.toArray(), new ApplyMapper());

	}

	public Page<ApplyEditCartypeVO> queryApplyEditCartypeByPage(String cwbs, String branchid,
			String applyUserId, Boolean isReview, Integer reviewStatus, String startApplyTime, String endApplyTime,
			int begin, int interval) {
		Page<ApplyEditCartypeVO> page = new Page<ApplyEditCartypeVO>();
		List<Object> params = new ArrayList<Object>();
		List<ApplyEditCartypeVO> list = new ArrayList<ApplyEditCartypeVO>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from EXPRESS_OPS_APPLYEDITCARTYPE ");
		sql.append(" where 1=1 ");
		if (!StringUtils.isEmpty(cwbs)) {
			sql.append(" and cwb in ( ");
			sql.append(cwbs);
			sql.append(" ) ");
		}
		if (!StringUtils.isEmpty(branchid)) {
			sql.append(" and apply_branchid in ( ");
			sql.append(branchid);
			sql.append(" ) ");
		}
		if (!StringUtils.isEmpty(applyUserId)) {
			sql.append(" and apply_userid in ( ");
			sql.append(applyUserId);
			sql.append(" ) ");
		}
		if (isReview != null) {
			if (isReview) {
				sql.append(" and review_status > 0 ");
			} else {
				sql.append(" and review_status = 0 ");
			}
		}
		if (reviewStatus != null) {
			sql.append(" and review_status = ? ");
			params.add(reviewStatus);
		}
		if (!StringUtils.isEmpty(startApplyTime)) {
			sql.append(" and apply_time >= ? ");
			params.add(startApplyTime);
		}
		if (!StringUtils.isEmpty(endApplyTime)) {
			sql.append(" and apply_time <= ? ");
			params.add(endApplyTime);
		}
		// 先查出总数
		String countSql = getCountSql(sql.toString());
		Long count = jdbcTemplate.queryForLong(countSql, params.toArray());
		page.setTotalCount(count);
		// 查出结果列表
		sql.append(getPageSql());
		params.add(begin);
		params.add(interval);
		list = jdbcTemplate.query(sql.toString(), params.toArray(), new ApplyMapper());
		page.setList(list);

		return page;

	}

	public List<ApplyEditCartypeVO> queryApplyEditCartypeByList(String cwbs, String branchid,
			String applyUserId, Boolean isReview, Integer reviewStatus, String startApplyTime, String endApplyTime) {
		List<Object> params = new ArrayList<Object>();
		List<ApplyEditCartypeVO> list = new ArrayList<ApplyEditCartypeVO>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from EXPRESS_OPS_APPLYEDITCARTYPE ");
		sql.append(" where 1=1 ");
		if (!StringUtils.isEmpty(cwbs)) {
			sql.append(" and cwb in ( ");
			sql.append(cwbs);
			sql.append(" ) ");
		}
		if (!StringUtils.isEmpty(branchid)) {
			sql.append(" and apply_branchid in ( ");
			sql.append(branchid);
			sql.append(" ) ");
		}
		if (!StringUtils.isEmpty(applyUserId)) {
			sql.append(" and apply_userid in ( ");
			sql.append(applyUserId);
			sql.append(" ) ");
		}
		if (isReview != null) {
			if (isReview) {
				sql.append(" and review_status > 0 ");
			} else {
				sql.append(" and review_status = 0 ");
			}
		}
		if (reviewStatus != null) {
			sql.append(" and review_status = ? ");
			params.add(reviewStatus);
		}
		if (!StringUtils.isEmpty(startApplyTime)) {
			sql.append(" and apply_time >= ? ");
			params.add(startApplyTime);
		}
		if (!StringUtils.isEmpty(endApplyTime)) {
			sql.append(" and apply_time <= ? ");
			params.add(endApplyTime);
		}
		list = jdbcTemplate.query(sql.toString(), params.toArray(), new ApplyMapper());

		return list;

	}

	public String getCountSql(String sql) {
		return "select count(1) from (" + sql + " ) temp ";
	}

	public String getPageSql() {
		return " limit ?, ? ";
	}

	public void updateReview(ApplyEditCartypeVO applyEditCartype) {
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" update  EXPRESS_OPS_APPLYEDITCARTYPE set ");
		sql.append("   REVIEW_USERID = ?, REVIEW_USERNAME = ?, REVIEW_TIME = ?, REVIEW_STATUS = ?");
		sql.append("  where id = ? ");
		params.add(applyEditCartype.getReviewUserid());
		params.add(applyEditCartype.getReviewUsername());
		params.add(applyEditCartype.getReviewTime());
		params.add(applyEditCartype.getReviewStatus());
		params.add(applyEditCartype.getId());

		jdbcTemplate.update(sql.toString(), params.toArray());
	}

	public static void main(String[] args) {

	}
}
