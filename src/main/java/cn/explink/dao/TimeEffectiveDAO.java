package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import cn.explink.domain.TimeEffectiveVO;
import cn.explink.domain.TimeTypeEnum;

@Repository
public class TimeEffectiveDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate = null;

	public List<TimeEffectiveVO> getAllTimeEffectiveVO() {
		String sql = "select * from express_set_time_effective where dr = 0";

		return this.getJdbcTemplate().query(sql, new TimeEffectiveVOMapper());
	}

	public void update(List<TimeEffectiveVO> teVOList) {
		String sql = this.getBatchUpdateSql();
		this.getJdbcTemplate().batchUpdate(sql, this.getBatchUpdateParaList(teVOList));
	}

	private List<Object[]> getBatchUpdateParaList(List<TimeEffectiveVO> teVOList) {
		List<Object[]> paraList = new ArrayList<Object[]>();
		for (TimeEffectiveVO teVO : teVOList) {
			paraList.add(this.getBatchUpdateParas(teVO));
		}
		return paraList;
	}

	private Object[] getBatchUpdateParas(TimeEffectiveVO teVO) {
		Object[] paras = new Object[3];
		paras[0] = teVO.getTimeType().ordinal();
		paras[1] = teVO.getScope();
		paras[2] = teVO.getId();

		return paras;
	}

	private String getBatchUpdateSql() {
		return "update express_set_time_effective set time_type=?,scope=? where id=? ";
	}

	private JdbcTemplate getJdbcTemplate() {
		return this.jdbcTemplate;
	}

	private class TimeEffectiveVOMapper implements RowMapper<TimeEffectiveVO> {

		@Override
		public TimeEffectiveVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			TimeEffectiveVO vo = new TimeEffectiveVO();
			vo.setId(rs.getInt(TimeEffectiveVO.ID));
			vo.setCode(rs.getString(TimeEffectiveVO.CODE));
			vo.setName(rs.getString(TimeEffectiveVO.NAME));
			vo.setTimeType(TimeTypeEnum.values()[rs.getInt(TimeEffectiveVO.TIME_TYPE)]);
			vo.setScope(rs.getLong(TimeEffectiveVO.SCOPE));
			vo.setTs(rs.getTimestamp(TimeEffectiveVO.TS));

			return vo;
		}

	}

}
