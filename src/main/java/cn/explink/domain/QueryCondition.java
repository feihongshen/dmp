package cn.explink.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryCondition {
	String orderBy = "";
	int pageSize = 10;
	int currentPage = 1;
	int pageTotal = 0;
	int total = 0;
	List<Field> conditions = new ArrayList<Field>();
	Map<String, Object> parameters = new HashMap<String, Object>();
	
	private Map<String, Object> extendMaps = new HashMap<String, Object>();
	String whereSql = null;

	public void addCondition(Field field) {
		conditions.add(field);
	}

	public void eq(String name, Object value) {
		Field field = new Field(name, value);
		addCondition(field);
	}

	public void eq(String name, Object value, String param) {
		Field field = new Field(name, Field.CONDITION_EQ, value, param);
		addCondition(field);
	}

	public void le(String name, Object value) {
		Field field = new Field(name, Field.CONDITION_LE, value);
		addCondition(field);
	}

	public void le(String name, Object value, String param) {
		Field field = new Field(name, Field.CONDITION_LE, value, param);
		addCondition(field);
	}

	public void ge(String name, Object value) {
		Field field = new Field(name, Field.CONDITION_GE, value);
		addCondition(field);
	}

	public void ge(String name, Object value, String param) {
		Field field = new Field(name, Field.CONDITION_GE, value, param);
		addCondition(field);
	}

	public void lt(String name, Object value) {
		Field field = new Field(name, Field.CONDITION_LT, value);
		addCondition(field);
	}

	public void lt(String name, Object value, String param) {
		Field field = new Field(name, Field.CONDITION_LT, value, param);
		addCondition(field);
	}

	public void gt(String name, Object value) {
		Field field = new Field(name, Field.CONDITION_GT, value);
		addCondition(field);
	}

	public void gt(String name, Object value, String param) {
		Field field = new Field(name, Field.CONDITION_GT, value, param);
		addCondition(field);
	}

	public void in(String name, Object value) {
		Field field = new Field(name, Field.CONDITION_IN, value);
		addCondition(field);
	}

	public void in(String name, Object value, String param) {
		Field field = new Field(name, Field.CONDITION_IN, value, param);
		addCondition(field);
	}

	public void like(String name, Object value) {
		Field field = new Field(name, Field.CONDITION_LIKE_ANYWHERE, value);
		addCondition(field);
	}

	public void like(String name, Object value, String param) {
		Field field = new Field(name, Field.CONDITION_LIKE_ANYWHERE, value, param);
		addCondition(field);
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCurrentPage() {
		if (pageTotal <= 0 || currentPage <= 0) {
			return 1;
		} else if (currentPage > pageTotal) {
			return pageTotal;
		}
		return currentPage;
	}

	public int getRealPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<Field> getConditions() {
		return conditions;
	}

	public void setConditions(List<Field> conditions) {
		this.conditions = conditions;
	}

	public String getWhereSql() {
		if (whereSql != null) {
			return whereSql;
		}
		whereSql = "1=1";
		for (int i = 0; i < conditions.size(); i++) {
			Field f = conditions.get(i);
			whereSql += " and " + f.toSql();
			parameters.put(f.getFieldParam(), f.getFieldValue());
		}
		return whereSql;
	}

	public Map<String, Object> getParameters() {
		return parameters;
	}

	public int getPageTotal() {
		return pageTotal;
	}

	public void setPageTotal(int pageTotal) {
		this.pageTotal = pageTotal;
	}

	public Map<String, Object> getExtendMaps() {
		return extendMaps;
	}

	public void setExtendMaps(Map<String, Object> extendMaps) {
		this.extendMaps = extendMaps;
	}

	public void setWhereSql(String whereSql) {
		this.whereSql = whereSql;
	}
	
}
