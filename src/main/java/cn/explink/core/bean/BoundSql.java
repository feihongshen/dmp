package cn.explink.core.bean;

import java.util.List;

/**
 * sql语句对象
 * @author gaoll
 *
 */
public class BoundSql {

    /** 执行的sql */
    private String sql;

    /** 参数，对应sql中的?号 */
    private List<Object> params;

    /** 主键名称 */
    private String primaryKey;

    //TODO 空参构造是否必要？？？
    public BoundSql() {}

    /**
     * 带参构造
     * 
     * @param sql sql语句
     * @param primaryKey 主键名称
     * @param params sql中?占位符对应值
     */
    public BoundSql(String sql, String primaryKey, List<Object> params) {
        this.sql = sql;
        this.primaryKey = primaryKey;
        this.params = params;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public List<Object> getParams() {
        return params;
    }

    public void setParams(List<Object> params) {
        this.params = params;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }
}
