package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.CommonModel;

@Component
public class CommonModelDAO {

	private final class CommonModelRowMapper implements RowMapper<CommonModel> {
		@Override
		public CommonModel mapRow(ResultSet rs, int rowNum) throws SQLException {
			CommonModel commonModel = new CommonModel();
			commonModel.setId(rs.getLong("id"));
			commonModel.setModelname(rs.getString("modelname"));
			commonModel.setCoordinate(rs.getString("coordinate"));
			commonModel.setImageurl(rs.getString("imageurl"));
			return commonModel;
		}
	}

	private final class CommonModelJsonRowMapper implements RowMapper<JSONObject> {

		@Override
		public JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
			JSONObject json = new JSONObject();
			json.put("id", rs.getLong("id"));
			json.put("modelname", rs.getString("modelname"));
			json.put("coordinate", rs.getString("coordinate"));
			json.put("imageurl", rs.getString("imageurl"));
			return json;
		}

	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void CreateCommonModel(String modelname) {
		jdbcTemplate.update("insert into express_set_commonmodel(modelname) values(?)", modelname);
	}

	public List<CommonModel> getAllCommonModel() {
		try {
			return jdbcTemplate.query("select * from express_set_commonmodel", new CommonModelRowMapper());
		} catch (EmptyResultDataAccessException ee) {
			return null;
		}
	}

	public CommonModel getCommonModelByModelname(String modelname) {
		try {
			return jdbcTemplate.queryForObject("select * from express_set_commonmodel where modelname =?", new CommonModelRowMapper(), modelname);
		} catch (EmptyResultDataAccessException ee) {
			return null;
		}
	}

	public JSONObject getCommonModelJsonByModelname(String modelname) {
		try {
			return jdbcTemplate.queryForObject("select * from express_set_commonmodel where modelname =?", new CommonModelJsonRowMapper(), modelname);
		} catch (EmptyResultDataAccessException ee) {
			return null;
		}
	}

	public CommonModel getCommonModelById(long id) {
		try {
			return jdbcTemplate.queryForObject("select * from express_set_commonmodel where id = " + id, new CommonModelRowMapper());
		} catch (EmptyResultDataAccessException ee) {
			return null;
		}

	}

	public void saveCommonModelById(String coordinate, long id) {
		jdbcTemplate.update("update express_set_commonmodel set coordinate=? where id =?", coordinate, id);
	}

	public void saveCommonModelImageurlById(String imageurl, long id) {
		jdbcTemplate.update("update express_set_commonmodel set imageurl=? where id =?", imageurl, id);
	}

}
