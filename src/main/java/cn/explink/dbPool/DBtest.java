package cn.explink.dbPool;

import java.util.*;

public class DBtest {
	String urlname = "";

	public DBtest(String urlname) {
		this.urlname = urlname;
	}

	public void main(String[] args) throws Exception {
		String sql = "select * from userinfo";
		String[] collist = { "companyno", "company" };
		// Object[] para={"string","6",
		// "string","ӯ֮����",};
		// int t=insert(collist,para,"companyinfo");

		String[] updcollist = { "companyno", "company" };
		String key = "companyid";
		Object[] updpara = { "string", "5", "string", "�������", "int", "5" };
		update(updcollist, key, updpara, "companyinfo");

		List list = select(collist, null, null, null, "companyinfo");
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			System.out.println(map.get("companyno"));
			System.out.println(map.get("company").toString());
		}

	}

	// ���������Ĳ�ѯ���г�ȫ���ֶ�collist==null
	/**
	 *
	 * @param collist
	 *            String[]
	 * @param table
	 *            String
	 * @param conditionlist
	 *            String[]
	 * @return List
	 * @throws Exception
	 */
	public List select(String[] collist, String[] conditionlist, String ordersql, Object[] para, String table) throws Exception {
		String sql = "select ";
		if (collist == null) {
			sql += " * ";
		} else {
			for (int i = 0; i < collist.length; i++) {
				sql += collist[i];
				if (i < collist.length - 1) {
					sql += ",";
				}
			}
		}
		sql += " from " + table + " ";
		if (conditionlist != null) {
			sql += " where ";
			for (int i = 0; i < conditionlist.length; i++) {
				sql += conditionlist[i] + "=? ";
				if (i < conditionlist.length - 1) {
					sql += " and ";
				}
			}

		}
		if (ordersql != null)
			sql += ordersql;
		Database db = new Database(this.urlname);
		// System.out.println("sql====="+sql);
		return db.selectDataList(sql, para);
	}

	// ����
	/**
	 *
	 * @param collist
	 *            String[] �������ֶ��б?ȫ������ʱcollist����null
	 * @param para
	 *            Object[] �����б�
	 * @param table
	 *            String ��������
	 * @return int
	 * @throws Exception
	 */
	public int insert(String[] collist, Object[] para, String table) throws Exception {
		String sql = "insert into " + table;
		if (collist != null) {
			sql += "( ";
			for (int i = 0; i < collist.length; i++) {
				sql += collist[i];
				if (i < collist.length - 1) {
					sql += ", ";
				}
			}
			sql += ")";
		}
		sql += " values( ";
		for (int i = 0; i < collist.length; i++) {
			sql += "?";
			if (i < collist.length - 1) {
				sql += ", ";
			}
		}
		sql += ")";
		Database db = new Database(this.urlname);
		return db.updateData(sql, para);
	}

	// ɾ��,ɾ��ȫ��collist==null,para==null
	/**
	 *
	 * @param collist
	 *            String[] ɾ������ֶ��б�
	 * @param para
	 *            Object[] �����б�
	 * @param table
	 *            String ��ɾ�����
	 * @return int
	 * @throws Exception
	 */
	public int delete(String[] collist, Object[] para, String table) throws Exception {
		String sql = "delete from " + table;
		if (collist != null) {
			sql += " where ";
			for (int i = 0; i < collist.length; i++) {
				sql += collist[i] + "=? ";
				if (i < collist.length - 1) {
					sql += " and ";
				}
			}
		}
		Database db = new Database(this.urlname);
		return db.updateData(sql, para);
	}

	// �޸�
	/**
	 *
	 * @param collist
	 *            String[] �޸��ֶ��б�
	 * @param key
	 *            String ����ֶ�,ȫ������key==null
	 * @param para
	 *            Object[] �����ֶ�
	 * @param table
	 *            String ���޸ı���
	 * @return int
	 * @throws Exception
	 */
	public int update(String[] collist, String key, Object[] para, String table) throws Exception {
		String sql = "update " + table + " set ";
		for (int i = 0; i < collist.length; i++) {
			sql += collist[i] + "=? ";
			if (i < collist.length - 1) {
				sql += ", ";
			}
		}
		if (key != null) {
			sql += " where " + key + "=? ";
		}
		Database db = new Database(this.urlname);
		return db.updateData(sql, para);
	}

}
