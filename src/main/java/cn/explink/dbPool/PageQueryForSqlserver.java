package cn.explink.dbPool;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * SQL SERVER 分页类
 * <p>
 * Title:
 * </p>
 *
 * <p>
 * Description:
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 *
 * <p>
 * Company:
 * </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class PageQueryForSqlserver {
	int intPageSize = 20; // 一页显示的记录数
	int intRowCount; // 记录总数
	int intPageCount; // 总页数
	int intPage; // 待显示页码

	ResultSet rs = null;
	String urlname = "";

	public PageQueryForSqlserver(String urlname) {
		this.urlname = urlname;
	}

	public List PageQuery(HttpServletRequest request, String sql, Object[] para) throws Exception {
		List datalist = new ArrayList();

		String strPage = request.getParameter("page"); // 取得待显示页码
		if (strPage == null) {
			intPage = 1; // 表明在QueryString中没有page这一个参数，此时显示第一页数据
		} else {
			intPage = Integer.parseInt(strPage); // 将字符串转换成整型
			if (intPage < 1)
				intPage = 1;
		}
		DBOperator db = new DBOperator(this.urlname);
		try {
			rs = db.selectForPages(sql, para);
			if (rs.next()) {
				rs.last(); // 获取记录总数
				intRowCount = rs.getRow();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		intPageCount = (intRowCount + intPageSize - 1) / intPageSize; // 记算总页数
		if (intPage > intPageCount)
			intPage = intPageCount; // 调整待显示的页码
		if (intPageCount > 0) {
			rs.absolute((intPage - 1) * intPageSize + 1); // 将记录指针定位到待显示页的第一条记录上
			ResultSetMetaData rsmd = rs.getMetaData();
			int t = 0;
			while (t < intPageSize && !rs.isAfterLast()) {
				Map map = new HashMap();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					map.put(rsmd.getColumnName(i).toLowerCase(), rs.getObject(i) == null ? "" : rs.getObject(i));
					// 取出字段名称,字段名称全部小写字母,null值转化为空字符串
				}
				datalist.add(map);
				rs.next();
				t++;
			}

		}

		try {
			db.freeConn(); // rs使用完毕，释放数据库链接资源，特别重要！！！！
			if (rs != null)
				rs.close();
		} catch (SQLException se) {
			se.printStackTrace();
		}

		return datalist;

	}

	public List PageQueryForOneTable(HttpServletRequest request, String[] collist, String[] conditionlist, String ordersql, Object[] para, String table) throws Exception {
		List datalist = new ArrayList();

		String strPage = request.getParameter("page"); // 取得待显示页码
		if (strPage == null) {
			intPage = 1; // 表明在QueryString中没有page这一个参数，此时显示第一页数据
		} else {
			intPage = Integer.parseInt(strPage); // 将字符串转换成整型
			if (intPage < 1)
				intPage = 1;
		}
		DBOperator db = new DBOperator(this.urlname);
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
		sql += " from " + table;
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

		try {
			rs = db.selectForPages(sql, para);
			if (rs.next()) {
				rs.last(); // 获取记录总数
				intRowCount = rs.getRow();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		intPageCount = (intRowCount + intPageSize - 1) / intPageSize; // 记算总页数
		if (intPage > intPageCount)
			intPage = intPageCount; // 调整待显示的页码
		if (intPageCount > 0) {
			rs.absolute((intPage - 1) * intPageSize + 1); // 将记录指针定位到待显示页的第一条记录上

			ResultSetMetaData rsmd = rs.getMetaData();
			int t = 0;
			while (t < intPageSize && !rs.isAfterLast()) {
				Map map = new HashMap();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					map.put(rsmd.getColumnName(i).toLowerCase(), rs.getObject(i) == null ? "" : rs.getObject(i));
					// 取出字段名称,字段名称全部小写字母 null值转化为空字符串
				}
				datalist.add(map);
				rs.next();
				t++;
			}

		}

		try {
			db.freeConn(); // rs使用完毕，释放数据库链接资源，特别重要！！！！
			if (rs != null)
				rs.close();
		} catch (SQLException se) {
			se.printStackTrace();
		}

		return datalist;

	}

	// 页面显示信息
	public String PageLegend(String para) {

		String str = "";
		if (intPageCount > 1) { // 总页数大于1页，显示翻页信息
			str += "<a href=\"" + para + "&page=1\">[首页]</a>";
			if (intPage > 1)
				str += "<a href=\"" + para + "&page=" + (intPage - 1) + "\">[上一页]</a>";

			str += "第" + intPage + "页  共" + intPageCount + "页";

			if (intPage < intPageCount)
				str += "<a href=\"" + para + "&page=" + (intPage + 1) + "\">[下一页]</a>";

			str += "<a  href=\"" + para + "&page=" + intPageCount + "\">[末页]</a>";
			str += "&nbsp;&nbsp;&nbsp;&nbsp;转到";
			str += "<select name=\"page\" onchange=document.location=\"" + para + "&page=\"" + "+document.getElementById('page').value>";
			for (int i = 1; i <= intPageCount; i++) {
				if (i == intPage) {
					str += "<option value=\"" + i + "\" selected >" + "第" + i + "页" + "</option>";
				} else {
					str += "<option value=\"" + i + "\">" + "第" + i + "页" + "</option>";
				}
			}
			str += "</select>";

		}
		return str;
	}

}
