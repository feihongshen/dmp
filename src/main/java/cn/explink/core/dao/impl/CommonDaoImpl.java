package cn.explink.core.dao.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;

import cn.explink.core.dao.CommonDao;
import cn.explink.core.dao.JdbcDao;

@Repository 
public class CommonDaoImpl implements CommonDao {
	
	@Autowired
	protected JdbcDao jdbcDao;
	
	@Override
	public <T> void sava(T t) {
		jdbcDao.insert(t);
	}

	@Override
	public <T> void update(T t) {
		jdbcDao.update(t);
	}

	@Override
	public <T> void update(Set<T> ts) {
		if(null != ts && ts.size() > 0){
			for(T tempT : ts){
				this.update(tempT);
			}
		}
	}

	@Override
	public <T> T loadById(Long id, Class<T> tClazz) {
		T result = jdbcDao.get(tClazz, id);
		return result;
	}

	@Override
	public <T> void deleteById(Long id, Class<T> tClazz) {
		jdbcDao.delete(tClazz, id);
	}

	@Override
	public <T> List<T> loadAll(Class<T> tClazz) {
		List<T> resultList = null;
		try {
			resultList = jdbcDao.queryList(tClazz.newInstance());
		} catch (InstantiationException e) {
			//TODO 异常处理
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return resultList;
	}

	@Override
	public JdbcDao getJdbcDao() {
		return this.jdbcDao;
	}
	
	@Override
	public JdbcOperations getJdbcTemplate() {
		return this.jdbcDao.getJdbcTemplate();
	}

}
