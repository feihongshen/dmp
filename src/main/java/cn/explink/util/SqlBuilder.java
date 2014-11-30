package cn.explink.util;

public class SqlBuilder {

	private StringBuilder builder = null;

	private boolean firstAddWhereCond = true;

	public SqlBuilder() {
		this.setSql(new StringBuilder());
	}

	public void appendSelectPart(String selectPart) {
		this.getBuiler().append(selectPart);
	}

	public void appendCondition(String condition) {
		if ((condition == null) || condition.isEmpty()) {
			return;
		}
		if (this.isFirstAddWhereCond()) {
			this.getBuiler().append(" where ");
			this.setFirstAddWhereCond(false);
		} else {
			this.getBuiler().append(" and ");
		}
		this.getBuiler().append(condition);
	}

	public void appendExtraPart(String part) {
		this.getBuiler().append(" ");
		this.getBuiler().append(part);
	}

	public String getSql() {
		return this.getBuiler().toString();
	}

	private StringBuilder getBuiler() {
		return this.builder;
	}

	private void setSql(StringBuilder sql) {
		this.builder = sql;
	}

	private boolean isFirstAddWhereCond() {
		return this.firstAddWhereCond;
	}

	private void setFirstAddWhereCond(boolean firstAddWhereCond) {
		this.firstAddWhereCond = firstAddWhereCond;
	}

}
