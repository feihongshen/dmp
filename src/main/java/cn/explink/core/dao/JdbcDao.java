package cn.explink.core.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcOperations;

import cn.explink.core.bean.Criteria;

/**
 * 操作数据库Dao
 * @author gaoll
 *
 */
public interface JdbcDao {

    /**
     * 插入一条记录 自动处理主键
     *
     * @param entity
     * @return
     */
    public Long insert(Object entity);

    /**
     * 插入一条记录 自动处理主键
     *
     * @param criteria
     * @return long long
     */
    public Long insert(Criteria criteria);

    /**
     * 保存一条记录，不主动处理主键(手动指定主键)
     *
     * @param entity
     */
    public void save(Object entity);

    /**
     * 保存一条记录，不主动处理主键(手动指定主键)
     *
     * @param criteria
     */
    public void save(Criteria criteria);

    /**
     * 根据Criteria更新
     *
     * @param criteria
     */
    public void update(Criteria criteria);

    /**
     * 根据实体更新
     *
     * @param entity
     */
    public void update(Object entity);

    /**
     * 根据Criteria删除
     *
     * @param criteria
     */
    public void delete(Criteria criteria);

    /**
     * 根据entity删除  此方法会以实体中不为空的字段为条件
     *
     * @param entity
     */
    public void delete(Object entity);

    /**
     * 根据主键删除记录
     *
     * @param clazz
     * @param id
     */
    public void delete(Class<?> clazz, Long id);

    /**
     * 删除对应实体所有记录(TRUNCATE ddl权限)
     *
     * @param clazz
     */
    public void deleteAll(Class<?> clazz);

    /**
     * 按设置的条件查询
     *
     * @param <T>  
     * @param criteria 
     * @return list
     */
    public <T> List<T> queryList(Criteria criteria);

    /**
     * 查询列表
     *
     * @param entity
     * @return list
     */
    public <T> List<T> queryList(T entity);

    /**
     * 查询列表
     *
     * @param <T>  
     * @param entity 
     * @param criteria 
     * @return list
     */
    public <T> List<T> queryList(T entity, Criteria criteria);

    /**
     * 查询记录数
     *
     * @param entity
     * @return
     */
    public int queryCount(Object entity);

    /**
     * 查询记录数
     *
     * @param criteria
     * @return int
     */
    public int queryCount(Criteria criteria);

    /**
     * 查询记录数
     *
     * @param entity
     * @param criteria
     * @return int
     */
    public int queryCount(Object entity, Criteria criteria);

    /**
     * 根据主键得到记录
     *
     * @param <T>  
     * @param clazz
     * @param id 
     * @return t
     */
    public <T> T get(Class<T> clazz, Long id);

    /**
     * 根据主键得到记录
     *
     * @param <T> 
     * @param criteria
     * @param id
     * @return t
     */
    public <T> T get(Criteria criteria, Long id);

    /**
     * 查询单个记录
     *
     * @param <T>   
     * @param entity
     * @return t
     */
    public <T> T querySingleResult(T entity);

    /**
     * 查询单个记录
     *
     * @param <T> 
     * @param criteria
     * @return t
     */
    public <T> T querySingleResult(Criteria criteria);
    
    /**
     * 获取原生jdbcTemplate
     * @return
     */
    public JdbcOperations getJdbcTemplate();
}
