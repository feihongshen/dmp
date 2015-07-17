/**
 *
 */
package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.PFoverarea;

/**
 * @author Administrator
 *
 */
@Component
public class PFoverareaDAO {
	final class PFoverareaRowMapper implements RowMapper<PFoverarea> {

		@Override
		public PFoverarea mapRow(ResultSet rs, int rowNum) throws SQLException {
			PFoverarea pf = new PFoverarea();
			pf.setId(rs.getLong("id"));
			pf.setState(rs.getInt("state"));
			pf.setPfruleid(rs.getLong("pfruleid"));
			pf.setTypeid(rs.getInt("typeid"));
			pf.setTabid(rs.getInt("tabid"));
			return pf;
		}
	}
}
