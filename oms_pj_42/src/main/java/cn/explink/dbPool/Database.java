package cn.explink.dbPool;

import java.util.List;

public class Database {
	String urlname = "";

	public Database(String urlname) {
		this.urlname = urlname;
	}

	// 数据查询
	public List selectDataList(String sql, Object[] para) throws Exception {
		DBOperator db = new DBOperator(this.urlname);
		return db.select(sql, para);
	}

	// 带全部条件的数据查询
	public List selectDataListByMoreKey(String sql, Object[] para) throws Exception {
		DBOperator db = new DBOperator(this.urlname);
		return db.selectbymore(sql, para);
	}

	// 数据修改删除
	public int updateData(String sql, Object[] para) throws Exception {
		DBOperator db = new DBOperator(this.urlname);
		return db.update(sql, para);

	}

	/**
	 * 获取字段名称columnname，类型columntype，描述信息comments
	 * 
	 * @param table
	 *            String
	 * @param databasetype
	 *            String
	 * @return List
	 * @throws Exception
	 */

}
