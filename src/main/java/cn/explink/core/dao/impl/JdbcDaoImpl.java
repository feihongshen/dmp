package cn.explink.core.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import cn.explink.core.bean.BoundSql;
import cn.explink.core.bean.Criteria;
import cn.explink.core.dao.JdbcDao;
import cn.explink.core.utils.ClassUtils;
import cn.explink.core.utils.DefaultNameHandler;
import cn.explink.core.utils.NameHandler;
import cn.explink.core.utils.NameUtils;
import cn.explink.core.utils.SqlAssembleUtils;

/**
 * 操作数据库Dao
 * @author gaoll
 *
 */
@Repository("jdbcDao")
public class JdbcDaoImpl implements JdbcDao {

	@Autowired
    protected JdbcOperations jdbcTemplate;

    /** 名称处理器，为空按默认执行 */
    protected NameHandler nameHandler;

    /** rowMapper，为空按默认执行 */
    protected String rowMapperClass;

    /** 数据库方言 */
    protected String dialect;

    /**
     * 插入数据
     *
     * @param entity 
     * @param criteria
     * @return long
     */
    private Long insert(Object entity, Criteria criteria) {
        Class<?> entityClass = SqlAssembleUtils.getEntityClass(entity, criteria);
        NameHandler handler = this.getNameHandler();
        String pkValue = handler.getPKValue(entityClass, this.dialect);
        if (StringUtils.isNotBlank(pkValue)) {
            String primaryName = handler.getPKName(entityClass);
            if (criteria == null) {
                criteria = Criteria.create(entityClass);
            }
            criteria.setPKValueName(NameUtils.getCamelName(primaryName), pkValue);
        }
        final BoundSql boundSql = SqlAssembleUtils.buildInsertSql(entity, criteria,this.getNameHandler());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(boundSql.getSql(),
                    new String[] { boundSql.getPrimaryKey() });
                int index = 0;
                for (Object param : boundSql.getParams()) {
                    index++;
                    ps.setObject(index, param);
                }
                return ps;
            }
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public Long insert(Object entity) {
        return this.insert(entity, null);
    }

    @Override
    public Long insert(Criteria criteria) {
        return this.insert(null, criteria);
    }

    @Override
    public void save(Object entity) {
        final BoundSql boundSql = SqlAssembleUtils.buildInsertSql(entity, null,this.getNameHandler());
        jdbcTemplate.update(boundSql.getSql(), boundSql.getParams().toArray());
    }

    @Override
    public void save(Criteria criteria) {
        final BoundSql boundSql = SqlAssembleUtils.buildInsertSql(null, criteria,this.getNameHandler());
        jdbcTemplate.update(boundSql.getSql(), boundSql.getParams().toArray());
    }

    @Override
    public void update(Criteria criteria) {
        BoundSql boundSql = SqlAssembleUtils.buildUpdateSql(null, criteria, this.getNameHandler());
        jdbcTemplate.update(boundSql.getSql(), boundSql.getParams().toArray());
    }

    @Override
    public void update(Object entity) {
        BoundSql boundSql = SqlAssembleUtils.buildUpdateSql(entity, null, this.getNameHandler());
        jdbcTemplate.update(boundSql.getSql(), boundSql.getParams().toArray());
    }

    @Override
    public void delete(Criteria criteria) {
        BoundSql boundSql = SqlAssembleUtils.buildDeleteSql(null, criteria, this.getNameHandler());
        jdbcTemplate.update(boundSql.getSql(), boundSql.getParams().toArray());
    }

    @Override
    public void delete(Object entity) {
        BoundSql boundSql = SqlAssembleUtils.buildDeleteSql(entity, null, this.getNameHandler());
        jdbcTemplate.update(boundSql.getSql(), boundSql.getParams().toArray());
    }

    @Override
    public void delete(Class<?> clazz, Long id) {
        BoundSql boundSql = SqlAssembleUtils.buildDeleteSql(clazz, id, this.getNameHandler());
        jdbcTemplate.update(boundSql.getSql(), boundSql.getParams().toArray());
    }

    @Override
    public void deleteAll(Class<?> clazz) {
        String tableName = this.getNameHandler().getTableName(clazz);
        String sql = "TRUNCATE TABLE " + tableName;
        jdbcTemplate.execute(sql);
    }

    @Override
    public <T> List<T> queryList(Criteria criteria) {
        BoundSql boundSql = SqlAssembleUtils.buildListSql(null, criteria, this.getNameHandler());
        List<?> list = jdbcTemplate.query(boundSql.getSql(), boundSql.getParams().toArray(),this.getRowMapper(criteria.getEntityClass()));
        return (List<T>) list;
    }

    @Override
    public <T> List<T> queryList(T entity) {
        BoundSql boundSql = SqlAssembleUtils.buildListSql(entity, null, this.getNameHandler());
        List<?> list = jdbcTemplate.query(boundSql.getSql(), boundSql.getParams().toArray(),this.getRowMapper(entity.getClass()));
        return (List<T>) list;
    }

    @Override
    public <T> List<T> queryList(T entity, Criteria criteria) {
        BoundSql boundSql = SqlAssembleUtils.buildListSql(entity, criteria, this.getNameHandler());
        List<?> list = jdbcTemplate.query(boundSql.getSql(), boundSql.getParams().toArray(),this.getRowMapper(entity.getClass()));
        return (List<T>) list;
    }

    @Override
    public int queryCount(Object entity, Criteria criteria) {
        BoundSql boundSql = SqlAssembleUtils.buildCountSql(entity, criteria, this.getNameHandler());
        return jdbcTemplate.queryForInt(boundSql.getSql(), boundSql.getParams().toArray());
    }

    @Override
    public int queryCount(Object entity) {
        BoundSql boundSql = SqlAssembleUtils.buildCountSql(entity, null, this.getNameHandler());
        return jdbcTemplate.queryForInt(boundSql.getSql(), boundSql.getParams().toArray());
    }

    @Override
    public int queryCount(Criteria criteria) {
        BoundSql boundSql = SqlAssembleUtils.buildCountSql(null, criteria, this.getNameHandler());
        return jdbcTemplate.queryForInt(boundSql.getSql(), boundSql.getParams().toArray());
    }

    @Override
    public <T> T get(Class<T> clazz, Long id) {
        BoundSql boundSql = SqlAssembleUtils.buildByIdSql(clazz, id, null, this.getNameHandler());

        //TODO  采用list方式查询，当记录不存在时返回null而不会抛出异常!!!
        List<T> list = jdbcTemplate.query(boundSql.getSql(), this.getRowMapper(clazz), id);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.iterator().next();
    }

    @Override
    public <T> T get(Criteria criteria, Long id) {
        BoundSql boundSql = SqlAssembleUtils.buildByIdSql(null, id, criteria, this.getNameHandler());

        //采用list方式查询，当记录不存在时返回null而不会抛出异常
        List<T> list = (List<T>) jdbcTemplate.query(boundSql.getSql(),this.getRowMapper(criteria.getEntityClass()), id);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.iterator().next();
    }

    @Override
    public <T> T querySingleResult(T entity) {
        BoundSql boundSql = SqlAssembleUtils.buildQuerySql(entity, null, this.getNameHandler());

        //采用list方式查询，当记录不存在时返回null而不会抛出异常
        List<?> list = jdbcTemplate.query(boundSql.getSql(), boundSql.getParams().toArray(),this.getRowMapper(entity.getClass()));
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return (T) list.iterator().next();
    }

    @Override
    public <T> T querySingleResult(Criteria criteria) {
        BoundSql boundSql = SqlAssembleUtils.buildQuerySql(null, criteria, this.getNameHandler());
        //采用list方式查询，当记录不存在时返回null而不会抛出异常
        List<?> list = jdbcTemplate.query(boundSql.getSql(), boundSql.getParams().toArray(),this.getRowMapper(criteria.getEntityClass()));
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return (T) list.iterator().next();
    }

    /**
     * 获取rowMapper对象
     *
     * @param clazz
     * @return
     */
    protected <T> RowMapper<T> getRowMapper(Class<T> clazz) {

        if (StringUtils.isBlank(rowMapperClass)) {
            return BeanPropertyRowMapper.newInstance(clazz);
        } else {
            return (RowMapper<T>) ClassUtils.newInstance(rowMapperClass);
        }
    }

    /**
     * 获取名称处理器
     *
     * @return
     */
    protected NameHandler getNameHandler() {

        if (this.nameHandler == null) {
            this.nameHandler = new DefaultNameHandler();
        }
        return this.nameHandler;
    }

    public void setJdbcTemplate(JdbcOperations jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setNameHandler(NameHandler nameHandler) {
        this.nameHandler = nameHandler;
    }

    public void setRowMapperClass(String rowMapperClass) {
        this.rowMapperClass = rowMapperClass;
    }

    public void setDialect(String dialect) {
        this.dialect = dialect;
    }

	@Override
	public JdbcOperations getJdbcTemplate() {
		return this.jdbcTemplate;
	}
}
