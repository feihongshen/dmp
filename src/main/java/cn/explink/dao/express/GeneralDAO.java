package cn.explink.dao.express;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

/**
 *
 * @description 一个通用的DAO，能够适用一般的数据库操作(增、删、改，由于查里面的rowmap不好实现，目前尚没有抽象出查)
 * @author  刘武强
 * @data   2015年8月10日
 */

@Repository
public class GeneralDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 *
	 * @Title: insert
	 * @description 数据库插入操作,返回true或false
	 * @author 刘武强
	 * @date  2015年8月8日下午6:15:15
	 * @param  @param paramsMap 需要更新的字段和值的map，譬如：....name='liuw',则paramsMap.put("id",1222);
	 * @param  @param table 需要插入的表名称
	 * @param  @return
	 * @return  boolean 插入成功：true,否则false
	 * @throws
	 */
	public boolean insert(Map<String, Object> paramsMap, String table) {
		if (paramsMap.size() <= 0) {
			return false;
		}

		StringBuffer sql = new StringBuffer();
		StringBuffer values = new StringBuffer();
		StringBuffer culoms = new StringBuffer();
		Object[] params = new Object[paramsMap.size()];

		sql.append("insert into ").append(table);
		values.append("(");
		culoms.append("(");
		Set<String> keys = paramsMap.keySet();
		Iterator<String> it = keys.iterator();
		int i = 0;
		while (it.hasNext()) {
			String key = it.next().toString();
			culoms.append(key).append(",");
			values.append("?").append(",");
			params[i++] = paramsMap.get(key);
		}
		sql.append(culoms.substring(0, culoms.lastIndexOf(",")) + ") values");
		sql.append(values.substring(0, values.lastIndexOf(",")) + ")");

			this.jdbcTemplate.update(sql.toString(), params);
		
		return true;
	}

	/**
	 *
	 * @Title: insertReturnKey
	 * @description 给指定表插入数据，返回插入数据的id
	 * @author 刘武强
	 * @date  2015年8月20日下午4:07:07
	 * @param  @param params MapparamsMap 需要更新的字段和值的map，譬如：....name='liuw',则paramsMap.put("id",1222);
	 * @param  @param table 需要插入数据的表名
	 * @param  @return
	 * @return  Long
	 * @throws
	 */
	public Long insertReturnKey(Map<String, Object> paramsMap, String table) {

		final StringBuffer sql = new StringBuffer();
		StringBuffer values = new StringBuffer();
		StringBuffer culoms = new StringBuffer();
		final Object[] params = new Object[paramsMap.size()];

		sql.append("insert into ").append(table);
		values.append("(");
		culoms.append("(");
		Set<String> keys = paramsMap.keySet();
		Iterator<String> it = keys.iterator();
		int i = 0;
		while (it.hasNext()) {
			String key = it.next().toString();
			culoms.append(key).append(",");
			values.append("?").append(",");
			params[i++] = paramsMap.get(key);
		}
		sql.append(culoms.substring(0, culoms.lastIndexOf(",")) + ") values");
		sql.append(values.substring(0, values.lastIndexOf(",")) + ")");

		KeyHolder key = new GeneratedKeyHolder();
		this.jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
				for (int i = 0; i < params.length; i++) {
					ps.setObject(i + 1, params[i]);
				}
				return ps;
			}
		}, key);
		return key.getKey().longValue();
	}

	/**
	 *
	 * @Title: update
	 * @description  数据库跟新
	 * @author 刘武强
	 * @date  2015年8月11日上午8:05:10
	 * @param  @param paramsMap 需要更新的字段和值的map，譬如：....name='liuw',则paramsMap.put("id",1222);
	 * @param  @param table 需要更新的表名称
	 * @param  @param whereMap 更新条件的map，譬如 ：....where id=1222,则whereMap.put("id",1222);
	 * @param  @return
	 * @return  boolean 更新成功：true,否则false
	 * @throws
	 */
	public boolean update(Map<String, Object> paramsMap, String table, Map<String, Object> whereMap) {
		if (paramsMap.size() <= 0) {
			return false;
		}

		StringBuffer sql = new StringBuffer();
		StringBuffer culoms = new StringBuffer();
		StringBuffer where = new StringBuffer();
		final List<Object> paramslist = new ArrayList<Object>();

		sql.append("update ").append(table).append(" set");
		where.append("where 1=1");

		Set<String> keys = paramsMap.keySet();
		Iterator<String> it = keys.iterator();
		while (it.hasNext()) {
			String key = it.next().toString();
			culoms.append(" ").append(key).append("=?").append(",");
			paramslist.add(paramsMap.get(key));
		}
		sql.append(culoms.substring(0, culoms.lastIndexOf(",")));

		Set<String> wherekeys = whereMap.keySet();
		Iterator<String> whereit = wherekeys.iterator();
		while (whereit.hasNext()) {
			String wherekey = whereit.next().toString();
			where.append(" ").append("and ").append(wherekey).append("=?");
			paramslist.add(whereMap.get(wherekey));
		}

		sql.append(" ").append(where);
		this.jdbcTemplate.update(sql.toString(), new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				for (int i = 0; i < paramslist.size(); i++) {
					ps.setObject(i + 1, paramslist.get(i));
				}
			}
		});
		return true;
	}

	/**
	 *
	 * @Title: updateByIn
	 * @description 根据条件（where 字段  in ），来更新表格
	 * @author 刘武强
	 * @date  2015年8月17日上午9:34:49
	 * @param  @param paramsMap 需要更新的字段和值的map，譬如：....set name='liuw',则idparamsMap.put("name",'liuw');
	 * @param  @param table table 需要更新的表名称
	 * @param  @param whereMap 譬如 ：....where id=1222,则whereMap.put("id","('1222','2w32');
	 * @param  @return
	 * @return  boolean
	 * @throws
	 */
	public boolean updateByIn(Map<String, Object> paramsMap, String table, Map<String, Object> whereMap) {
		if (paramsMap.size() <= 0) {
			return false;
		}

		StringBuffer sql = new StringBuffer();
		StringBuffer culoms = new StringBuffer();
		StringBuffer where = new StringBuffer();
		final Object[] params = new Object[paramsMap.size()];

		sql.append("update ").append(table).append(" set");
		where.append("where 1=1");

		Set<String> keys = paramsMap.keySet();
		Iterator<String> it = keys.iterator();
		int i = 0;
		while (it.hasNext()) {
			String key = it.next().toString();
			culoms.append(" ").append(key).append("=?").append(",");
			params[i] = paramsMap.get(key);
			i++;
		}
		sql.append(culoms.substring(0, culoms.lastIndexOf(",")));

		Set<String> wherekeys = whereMap.keySet();
		Iterator<String> whereit = wherekeys.iterator();
		while (whereit.hasNext()) {
			String wherekey = whereit.next().toString();
			where.append(" ").append("and ").append(wherekey).append(" ").append("in " + whereMap.get(wherekey));
		}
		sql.append(" ").append(where);
		this.jdbcTemplate.update(sql.toString(), params);
		return true;
	}

	/**
	 *
	 * @Title: delete
	 * @description 根据map传过来的条件，删除记录
	 * @author 刘武强
	 * @date  2015年8月14日上午9:41:46
	 * @param  @param table 需要删除记录的表名称
	 * @param  @param paramsMap 需要删除的记录的条件map,譬如 ：where id=1222，则whereMap.put("id",1222);
	 * @param  @param value
	 * @return  void
	 * @throws
	 */
	public void deleteByKey(String table, Map<String, Object> paramsMap) {
		StringBuffer sql = new StringBuffer();
		StringBuffer where = new StringBuffer();
		sql.append("delete from ").append(table);
		where.append(" ").append("where 1=1");
		final List<Object> paramslist = new ArrayList<Object>();

		Set<String> keys = paramsMap.keySet();
		Iterator<String> it = keys.iterator();
		while (it.hasNext()) {
			String key = it.next().toString();
			where.append(" ").append("and ").append(key).append("=?");
			paramslist.add(paramsMap.get(key));
		}
		sql.append(where);
		this.jdbcTemplate.update(sql.toString(), new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				for (int i = 0; i < paramslist.size(); i++) {
					ps.setObject(i + 1, paramslist.get(i));
				}
			}
		});
	}

}
