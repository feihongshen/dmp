package cn.explink.core.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.explink.core.exception.AssistantException;

/**
 * sql 操作基类
 * @author gaoll
 *
 */
public class Criteria {

	/*
	 * 操作实体类class
	 */
    private Class<?> entityClass;

    /*
     * 操作字段list
     */
    private List<AutoField> autoFields;

    /*
     * 排序字段list
     */
    private List<AutoField> orderByFields;

    /*
     * 白名单字段list
     */
    private List<String> includeFields;

    /*
     * 黑名单字段list
     */
    private List<String> excludeFields;

    /* 
     * 单条sql语句where标识 
     */
    private boolean isWhere = false;

    private Criteria(Class<?> clazz) {
        this.entityClass = clazz;
        this.autoFields = new ArrayList<AutoField>();
        this.orderByFields = new ArrayList<AutoField>();
    }

    /**
     * 创建sql操作基类
     * 
     * @param clazz
     * @return
     */
    public static Criteria create(Class<?> clazz) {
        return new Criteria(clazz);
    }

    /**
     * 添加白名单字段
     *
     * @param field
     * @return
     */
    public Criteria include(String... field) {
        if (this.includeFields == null) {
            this.includeFields = new ArrayList<String>();
        }
        this.includeFields.addAll(Arrays.asList(field));
        return this;
    }

    /**
     * 添加黑名单字段
     *
     * @param field
     * @return
     */
    public Criteria exclude(String... field) {
        if (this.excludeFields == null) {
            this.excludeFields = new ArrayList<String>();
        }
        this.excludeFields.addAll(Arrays.asList(field));
        return this;
    }

    /**
     * asc 设置asc排序字段
     *
     * @param field
     * @return
     */
    public Criteria asc(String... field) {
        for (String f : field) {
            AutoField autoField = this.buildAutoFields(f, null, "ASC", AutoField.ORDER_BY_FIELD,null);
            this.orderByFields.add(autoField);
        }
        return this;
    }

    /**
     * desc 设置desc排序字段
     *
     * @param field
     * @return
     */
    public Criteria desc(String... field) {
        for (String f : field) {
            AutoField autoField = this.buildAutoFields(f, null, "DESC", AutoField.ORDER_BY_FIELD,null);
            this.orderByFields.add(autoField);
        }
        return this;
    }

    /**
     * 设置操作属性
     *
     * @param fieldName
     * @param value
     * @return
     */
    public Criteria set(String fieldName, Object value) {
        AutoField autoField = this.buildAutoFields(fieldName, null, "=", AutoField.UPDATE_FIELD,value);
        this.autoFields.add(autoField);
        return this;
    }

    /**
     * 设置主键值名称，如oracle序列名，非直接的值
     * 
     * @param pkName
     * @param valueName
     * @return
     */
    public Criteria setPKValueName(String pkName, String valueName) {
        AutoField autoField = this.buildAutoFields(pkName, null, "=", AutoField.PK_VALUE_NAME,valueName);
        this.autoFields.add(autoField);
        return this;
    }

    /**
     * 设置and条件-简易方法（=）
     *
     * @param fieldName
     * @param values
     * @return
     */
    public Criteria and(String fieldName, Object[] values) {
        this.and(fieldName, "=", values);
        return this;
    }

    /**
     * 设置and条件
     *
     * @param fieldName
     * @param fieldOperator
     * @param values
     * @return
     */
    public Criteria and(String fieldName, String fieldOperator, Object[] values) {
        AutoField autoField = this.buildAutoFields(fieldName, "and", fieldOperator,AutoField.WHERE_FIELD, values);
        this.autoFields.add(autoField);
        return this;
    }

    /**
     * 设置or条件-简易方法（=）
     *
     * @param fieldName
     * @param values
     * @return
     */
    public Criteria or(String fieldName, Object[] values) {
        this.or(fieldName, "=", values);
        return this;
    }

    /**
     * 设置or条件
     *
     * @param fieldName
     * @param fieldOperator
     * @param values
     * @return
     */
    public Criteria or(String fieldName, String fieldOperator, Object[] values) {
        AutoField autoField = this.buildAutoFields(fieldName, "or", fieldOperator,AutoField.WHERE_FIELD, values);
        this.autoFields.add(autoField);
        return this;
    }

    /**
     * 设置where条件属性-简易方法（=）
     *
     * @param fieldName
     * @param values
     * @return
     */
    public Criteria where(String fieldName, Object[] values) {
        this.where(fieldName, "=", values);
        return this;
    }

    /**
     * 设置where条件属性
     *
     * @param fieldName the field name
     * @param fieldOperator the operator
     * @param values the values
     * @return
     */
    public Criteria where(String fieldName, String fieldOperator, Object[] values) {
        if (this.isWhere) {
            throw new AssistantException("There can be only one 'where'!");
        }
        AutoField autoField = this.buildAutoFields(fieldName, "and", fieldOperator,AutoField.WHERE_FIELD, values);
        this.autoFields.add(autoField);
        this.isWhere = true;
        return this;
    }

    /**
     * 组装操作字段
     * @param fieldName 字段名
     * @param sqlOperator 条件符
     * @param fieldOperator 字段处理符号
     * @param type 字段操作类型
     * @param values 字段操作对应值
     * @return
     */
    private AutoField buildAutoFields(String fieldName, String sqlOperator, String fieldOperator,int type, Object... values) {
        AutoField autoField = new AutoField();
        autoField.setName(fieldName);
        autoField.setSqlOperator(sqlOperator);
        autoField.setFieldOperator(fieldOperator);
        autoField.setValues(values);
        autoField.setType(type);

        return autoField;
    }


    public Class<?> getEntityClass() {
        return entityClass;
    }

    public List<AutoField> getAutoFields() {
        return autoFields;
    }

    public List<String> getIncludeFields() {
        return includeFields;
    }

    public List<String> getExcludeFields() {
        return excludeFields;
    }

    public List<AutoField> getOrderByFields() {
        return orderByFields;
    }
}
