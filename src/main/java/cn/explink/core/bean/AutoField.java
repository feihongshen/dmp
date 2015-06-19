package cn.explink.core.bean;

/**
 * 组装sql时的字段信息
 *
 * @author gaoll
 */
public class AutoField {
    
	/*=========================字段组合类型++++++++++++++++++ */
	/** sql中的操作字段  (update/insert/delete/select)*/
    public static final int UPDATE_FIELD = 1;

    /** sql中的where 字段  */
    public static final int WHERE_FIELD  = 2;

    /** sql中的排序字段     */
    public static final int ORDER_BY_FIELD = 3;

    /** 主键值名称 例如oracle的序列名，非直接主键值   */
    public static final int PK_VALUE_NAME = 4;
    /*=========================字段组合类型++++++++++++++++++ */

    

    /*
     * 字段名
     */
    private String name;

    /*
     * 条件符 and or 等
     */
    private String sqlOperator;

    /*
     * 处理符 字段值 （<、>、<>、<=、>=、!=、=、in）等
     */
    private String fieldOperator;

    /*
     * 字段操作对应值
     */
    private Object[] values;

    /*
     * 字段操作类型（类中常量定义）
     */
    private int type;

    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object[] getValues() {
        return values;
    }

    public void setValues(Object[] values) {
        this.values = values;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSqlOperator() {
        return sqlOperator;
    }

    public void setSqlOperator(String sqlOperator) {
        this.sqlOperator = sqlOperator;
    }

    public String getFieldOperator() {
        return fieldOperator;
    }

    public void setFieldOperator(String fieldOperator) {
        this.fieldOperator = fieldOperator;
    }
}
