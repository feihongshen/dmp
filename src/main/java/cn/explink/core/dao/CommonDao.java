package cn.explink.core.dao;

import java.util.List;
import java.util.Set;

import org.springframework.jdbc.core.JdbcOperations;

/**
 * 
 * @author gaoll
 *
 */
public interface CommonDao {
	/**
	 * 新增
	 * @param t
	 */
	public <T> void sava(T t);

	/**
	 * 更新
	 * @param t
	 */
	public <T> void update(T t);
	
	/**
	 * 批量更新
	 * @param ts
	 */
	public <T> void update(Set<T> ts);
	
	/**
	 * 根据Id查询
	 * @param id
	 * @param t
	 * @return
	 */
	public <T> T loadById(Long id,Class<T> tClazz);
	
	/**
	 * 根据Id删除
	 * @param id
	 * @param t
	 */
	public <T> void deleteById(Long id,Class<T> tClazz);

	/**
	 * 查询所有
	 * @param t
	 * @return
	 */
	public <T> List<T> loadAll(Class<T> tClazz);
	
	/**
	 * 获取原生jdbcTemplate
	 * @return
	 */
	public JdbcOperations getJdbcTemplate();
	
	/**
	 * 获取公共jdbcDao
	 * @return
	 */
	public JdbcDao getJdbcDao();
}
