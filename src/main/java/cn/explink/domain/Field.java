package cn.explink.domain;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


public class Field implements Serializable {
	static final String EMPTY = "";
	private static final long serialVersionUID = 6437587423945233376L;
	public static final String CONDITION_EQ = "eq";
	public static final String CONDITION_NEQ = "neq";
	public static final String CONDITION_EQIC = "eqic";
	public static final String CONDITION_LIKE_END = "likee";
	public static final String CONDITION_LIKE_END_IC = "likeeic";
	public static final String CONDITION_LIKE_START = "likes";
	public static final String CONDITION_LIKE_START_IC = "likesic";
	public static final String CONDITION_LIKE_ANYWHERE = "likea";
	public static final String CONDITION_LIKE_ANYWHERE_IC = "likeaic";
	public static final String CONDITION_GT = "gt";
	public static final String CONDITION_GE = "ge";
	public static final String CONDITION_LT = "lt";
	public static final String CONDITION_LE = "le";
	public static final String CONDITION_ISNULL = "is null";
	public static final String CONDITION_ISNOTNULL = "is not null";
	public static final String CONDITION_IN = "in";
	public static final String CONDITION_NOTIN = "not in";
	public static final String CONJUNCTIONTYPE_AND = "and";
	public static final String CONJUNCTIONTYPE_OR = "or";
	public static final String CONJUNCTIONTYPE_EXIST = "exist";
	public static final String CONJUNCTIONTYPE_NOT = "not";
	private String fieldName = "";
	private String fieldParam = "";
	private Object fieldValue = "";
	private String fieldValueString = "";
	private String condition = "eq";
	private String fromAssociateObject = "";
	private String fromAssociateIdObject = "";
	private String fromAssociateCollection = "";
	private String conjunctionType = "and";
	private String isOrderBy = "";

	public Field() {
	}

	public Field(String paramString) {
		this.fieldName = paramString;
		this.fieldParam = paramString;
	}

	public Field(String paramString, Object paramObject) {
		this.fieldName = paramString;
		this.fieldParam = paramString;
		this.fieldValue = paramObject;
	}

	public Field(String paramString1, String paramString2) {
		this.fieldName = paramString1;
		this.fieldParam = paramString1;
		this.fieldValueString = paramString2;
		this.fieldValue = paramString2;
	}

	public Field(String paramString1, String paramString2, Object paramObject) {
		this.fieldName = paramString1;
		this.fieldParam = paramString1;
		this.condition = paramString2;
		this.fieldValue = paramObject;
	}

	public Field(String paramString1, String paramString2, String paramString3) {
		this.fieldName = paramString1;
		this.fieldParam = paramString1;
		this.condition = paramString2;
		this.fieldValueString = paramString3;
		this.fieldValue = paramString3;
	}

	public Field(String paramString1, Object paramObject, String paramString2) {
		this.fieldName = paramString1;
		this.fieldParam = paramString2;
		this.fieldValue = paramObject;
	}

	public Field(String paramString1, String paramString2, Object paramObject, String paramString3) {
		this.fieldName = paramString1;
		this.fieldParam = paramString3;
		this.condition = paramString2;
		this.fieldValue = paramObject;
	}

	public Field(String paramString1, String paramString2, String paramString3, String paramString4) {
		this.fieldName = paramString1;
		this.fieldParam = paramString4;
		this.condition = paramString2;
		this.fieldValueString = paramString3;
		this.fieldValue = paramString3;
	}

	public String getFieldName() {
		return this.fieldName;
	}

	public Object getFieldValue() {
		if ((this.fieldValue != null) && (!"".equals(this.fieldValue)))
			return this.fieldValue;
		return this.fieldValueString;
	}

	public String getCondition() {
		return this.condition;
	}

	@Deprecated
	public String getFromAssociateObj() {
		return this.fromAssociateObject;
	}

	public String getFromAssociateCollection() {
		return this.fromAssociateCollection;
	}

	public String getConjunctionType() {
		return this.conjunctionType;
	}

	public String getIsOrderBy() {
		return this.isOrderBy;
	}

	public void setFieldName(String paramString) {
		this.fieldName = paramString;
		if ((this.fieldParam == null) || (this.fieldParam.trim().length() == 0))
			this.fieldParam = paramString;
	}

	public void setFieldValue(Object paramObject) {
		this.fieldValue = paramObject;
	}

	public void setCondition(String paramString) {
		this.condition = paramString;
	}

	@Deprecated
	public void setFromAssociateObj(String paramString) {
		setFromAssociateObject(paramString);
	}

	public void setFromAssociateCollection(String paramString) {
		this.fromAssociateCollection = paramString;
	}

	public void setConjunctionType(String paramString) {
		this.conjunctionType = paramString;
	}

	public void setIsOrderBy(String paramString) {
		this.isOrderBy = paramString;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public String getFieldParam() {
		return this.fieldParam;
	}

	public void setFieldParam(String paramString) {
		this.fieldParam = paramString;
		if ((paramString == null) || (paramString.trim().length() == 0))
			this.fieldParam = this.fieldName;
	}

	public String getFieldValueString() {
		return this.fieldValueString;
	}

	public void setFieldValueString(String paramString) {
		this.fieldValueString = paramString;
	}

	public String getFromAssociateObject() {
		return this.fromAssociateObject;
	}

	public void setFromAssociateObject(String paramString) {
		this.fromAssociateObject = paramString;
	}

	public String getFromAssociateIdObject() {
		return this.fromAssociateIdObject;
	}

	public void setFromAssociateIdObject(String paramString) {
		this.fromAssociateIdObject = paramString;
	}

	public String toSql() {
		if (condition.equals(Field.CONDITION_EQ)) {
			return this.fieldName + "=:" + this.fieldParam;
		} else if (condition.equals(Field.CONDITION_GE)) {
			return this.fieldName + ">=:" + this.fieldParam;
		} else if (condition.equals(Field.CONDITION_LE)) {
			return this.fieldName + "<=:" + this.fieldParam;
		} else if (condition.equals(Field.CONDITION_GT)) {
			return this.fieldName + ">:" + this.fieldParam;
		} else if (condition.equals(Field.CONDITION_LT)) {
			return this.fieldName + "<:" + this.fieldParam;
		} else if (condition.equals(Field.CONDITION_NEQ)) {
			return this.fieldName + "<>:" + this.fieldParam;
		} else if (condition.equals(Field.CONDITION_IN)) {
			return this.fieldName + " in (:" + this.fieldParam + ")";
		} else if (condition.equals(Field.CONDITION_NOTIN)) {
			return this.fieldName + " not in (:" + this.fieldParam + ")";
		} else if (condition.equals(Field.CONDITION_LIKE_ANYWHERE)) {
			this.fieldValue = "%" + this.fieldValue + "%";
			return this.fieldName + " like :" + this.fieldParam; 
		} else if (condition.equals(Field.CONDITION_LIKE_START)) {
			this.fieldValue = this.fieldValue + "%";
			return this.fieldName + " like :" + this.fieldParam; 
		} else if (condition.equals(Field.CONDITION_LIKE_END)) {
			this.fieldValue = "%" + this.fieldValue;
			return this.fieldName + " like :" + this.fieldParam; 
		} else {
			return "";
		}
	}
	
	public String toSqlWithoutParam() {
		if (condition.equals(Field.CONDITION_EQ)) {
			return this.fieldName + "=" + this.getFieldValueString();
		} else if (condition.equals(Field.CONDITION_GE)) {
			return this.fieldName + ">=" + this.getFieldValueString();
		} else if (condition.equals(Field.CONDITION_LE)) {
			return this.fieldName + "<=" + this.getFieldValueString();
		} else if (condition.equals(Field.CONDITION_GT)) {
			return this.fieldName + ">" + this.getFieldValueString();
		} else if (condition.equals(Field.CONDITION_LT)) {
			return this.fieldName + "<" + this.getFieldValueString();
		} else if (condition.equals(Field.CONDITION_NEQ)) {
			return this.fieldName + "<>" + this.getFieldValueString();
		} else if (condition.equals(Field.CONDITION_IN)) {
			return this.fieldName + " in (" + this.getFieldValueString() + ")";
		} else if (condition.equals(Field.CONDITION_NOTIN)) {
			return this.fieldName + " not in (" + this.getFieldValueString() + ")";
		} else if (condition.equals(Field.CONDITION_LIKE_ANYWHERE)) {
			this.fieldValue = "%" + this.fieldValue + "%";
			return this.fieldName + " like " + this.getFieldValueString(); 
		} else if (condition.equals(Field.CONDITION_LIKE_START)) {
			this.fieldValue = this.fieldValue + "%";
			return this.fieldName + " like " + this.getFieldValueString(); 
		} else if (condition.equals(Field.CONDITION_LIKE_END)) {
			this.fieldValue = "%" + this.fieldValue;
			return this.fieldName + " like " + this.getFieldValueString(); 
		} else {
			return "";
		}
	}
}
