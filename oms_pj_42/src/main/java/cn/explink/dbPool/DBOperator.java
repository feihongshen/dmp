package cn.explink.dbPool;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数据库通用操作类 查询select方法,参数为sql和参数para[],返回list 更新update方法,参数为sql和参数para[],返回int
 * 数据库连接,sql操作,数据库连接释放全部在此类中完成
 *
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */

public class DBOperator {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private java.sql.PreparedStatement prep = null;
	private java.sql.ResultSet rs = null;
	private Connection conn = null;
	private ConnectPool connpool = null;
	private String urlname = "";
	private String showflag = "1";
	private static Configuration config;
	private java.sql.CallableStatement callstm = null;
	private int batchnum;

	public DBOperator(String urlname) {
		// logger.info("=========dboperator urlname="+urlname);
		if (urlname.indexOf("#") != -1) { // 含有# 取出前一个作为urlname 后一个用于判断是否显示电话 手机
			String[] urls = urlname.split("#");
			this.urlname = urls[0];
			this.showflag = urls[1];
		} else {
			this.urlname = urlname;
		}

		initialize(this.urlname);
	}

	private void initialize(String urlname) {
		try {
			connpool = ConnectPool.getInstance();
			conn = connpool.getConnection(urlname); // urlname为db.properties文件中.url前的名称
			this.config = Configuration.getInstance("dbsqlserver.properties");

		} catch (Exception e) {
			logger.error("初始化sqlserver", e.getMessage());
		}
	}

	/**
	 * *获取字段类型与字段名称 columntype, columnname * @@param sql sql字符串 * @@param params
	 * * @@return
	 */
	public List selectColumnsList(String table) throws Exception {
		List list = new ArrayList(); // updated by wuje at 2007-01-07 改为通用方法
										// 返回值为List类型
		rs = null;
		try {
			prep = conn.prepareStatement("select * from " + table);

			rs = prep.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();

			// 字段中文描述信息
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				Map map = new HashMap();
				// String coltype = rsmd.getColumnTypeName(i);
				map.put("tablename", table);
				map.put("columnname", rsmd.getColumnName(i).toLowerCase()); // 取出字段名称,字段名称全部小写字母
				map.put("columntype", rsmd.getColumnTypeName(i));
				list.add(map);
			}
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
			System.err.println(ex.getMessage());
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.err.println(ex.getMessage());
		}
		freeConn(); // 数据库操作完毕,释放数据库连接资源
		return list;
	}

	/**
	 *
	 * @param sqlStrArray
	 *            String 执行存储过程的的sql语句 示例： test(?,?,?)
	 * @param params
	 *            Object[] 参数列表4个参数一组，分别是: 参数类型，
	 *            参数值（out参数值为空串），输入输出（in/out），参数名称（主要用于获取out参数返回值） 示例：
	 *            params={"String","2","in","inname1",
	 *            "String","2","in","inname2", "String","2","in","inname3",
	 *            "String","","out","outname1", "String","","out","outname2",}
	 * 
	 * 
	 *            存储过程参数类型与java数据类型对应关系： VARCHAR---- string INTEGER---- int
	 *            DOUBLE---- double FLOAT---- float DECIMAL---- double
	 *
	 *
	 * @return Map 返回值中有两个key值
	 *         return_para_list，输出参数的list，封装一个map，key值为参数名称，value为字符串，没有输出参数
	 *         此list为null
	 *         return_table_list，返回数据查询结果集的list，封装一组map，key值为存储过程执行的sql的字段
	 *         ，value为查询结果值，没有输出结果集，此list为null 调用示例 executeProcedure()
	 */

	public Map<String, Object> executeProcedure(String sqlStrArray, Object[] params) {

		ArrayList return_para_list = new ArrayList(); // string的封装 按out参数的顺序排列
		ArrayList return_table_list = new ArrayList();
		Map returnmap = new HashMap();

		if (sqlStrArray == null)
			return null;
		try {
			/**
			 * 用于执行 SQL 存储过程的接口
			 */
			CallableStatement proc = null;
			logger.info("-------{call " + sqlStrArray + " }");
			proc = conn.prepareCall("{call " + sqlStrArray + " }");

			// proc.registerOutParameter(1,Types.VARCHAR);
			/**
			 * 参数传递
			 */
			int index = 1;
			if (params != null) {
				int n = params.length;
				if (n < 4 || n % 4 != 0 || (n / 4) != this.getStrNum(sqlStrArray, "?")) {
					throw new IllegalArgumentException("存储过程参数为不是4的倍数或者小于4,或者是参数的个数不一致");
				}

				for (int i = 0; i < params.length; i += 4) {
					// params[i] = this.pareObjToStr(params[i]); //第一个参数为数据类型
					// params[i + 1] = this.pareObjToStr(params[i + 1]);
					// //第二个参数为数据值
					// params[i + 2] = this.pareObjToStr(params[i + 2]);
					// //第三个参数为输入输出

					/**
					 * 输入参数
					 */
					if (params[i + 2].toString().equalsIgnoreCase("in")) {
						logger.info("---------输入字段---------------" + params[i + 1]);
						if (((String) params[i]).toLowerCase().equals("string")) {
							proc.setString(index++, getRealString((String) params[i + 1]));
						} else if (((String) params[i]).toLowerCase().equals("long")) {
							proc.setLong(index++, Long.parseLong((String) params[i + 1]));
						} else if (((String) params[i]).toLowerCase().equals("int")) {
							proc.setInt(index++, Integer.parseInt((String) params[i + 1]));
						} else if (((String) params[i]).toLowerCase().equals("date")) {
							proc.setString(index++, (String) params[i + 1]);
						} else if (((String) params[i]).toLowerCase().equals("float")) {
							proc.setFloat(index++, Float.parseFloat((String) params[i + 1]));
						} else if (((String) params[i]).toLowerCase().equals("double")) {
							proc.setDouble(index++, Double.parseDouble((String) params[i + 1]));
						} else if (((String) params[i]).toLowerCase().equals("image")) {
							proc.setBytes(index++, (byte[]) params[i + 1]);
						} else if (((String) params[i]).toLowerCase().equals("t_cod_route")) { // 小红帽和顺丰表类型
							proc.setObject(index++, params[i + 1]);

						}

						else {
							proc.setString(index++, getRealString((String) params[i + 1]));

						}

					}
					/**
					 * 设置out参数
					 */
					else if (params[i + 2].toString().equalsIgnoreCase("out")) { // 第三个参数为输入输出
						logger.info("---------输出字段---------------");
						if (((String) params[i]).toLowerCase().equals("string")) {
							proc.registerOutParameter(index++, Types.VARCHAR);
						} else if (((String) params[i]).toLowerCase().equals("int")) {
							proc.registerOutParameter(index++, Types.INTEGER);
						} else if (((String) params[i]).toLowerCase().equals("datetime")) {
							proc.registerOutParameter(index++, Types.DATE);
						} else if (((String) params[i]).toLowerCase().equals("float")) {
							proc.registerOutParameter(index++, Types.FLOAT);
						} else if (((String) params[i]).toLowerCase().equals("double")) {
							proc.registerOutParameter(index++, Types.DOUBLE);
						} else if (((String) params[i]).toLowerCase().equals("char")) {
							proc.registerOutParameter(index++, Types.CHAR);
						} else {
							proc.registerOutParameter(index++, Types.VARCHAR);
						}
					} else {
						throw new IllegalArgumentException("存储过程传参异常,第" + (i + 2 + 1) + "个参数应该为in或out");
					}
				}
			}

			// 提交存储过程执行
			boolean bool = proc.execute(); // 根据返回 bool=true,来判断是否有结果集.
			/**
			 * 根据参数顺序获取out输出参数字段
			 */
			if (params != null) {
				Map paramap = new HashMap();
				for (int i = 0; i < params.length; i += 4) {
					params[i] = this.pareObjToStr(params[i]); // 第一个参数为数据类型
					params[i + 1] = this.pareObjToStr(params[i + 1]); // 第二个参数为数据类型
					params[i + 2] = this.pareObjToStr(params[i + 2]); // 第三个参数为输入输出
					params[i + 3] = this.pareObjToStr(params[i + 3]); // 第四个参数为参数名称
					String reStr = "";
					if (params[i + 2].toString().equalsIgnoreCase("out")) {
						if (params[i].toString().equalsIgnoreCase("string")) {
							reStr = proc.getString(i / 4 + 1);
							paramap.put(params[i + 3].toString(), reStr == null ? "" : reStr.trim());
							return_para_list.add(paramap);
						} else if (params[i].toString().equalsIgnoreCase("float")) {
							float number = proc.getFloat(i / 4 + 1);
							try {
								reStr = String.valueOf(number);
							} catch (Exception ev) {
								logger.info("----FLOAT ERROR");
								ev.printStackTrace();
							}
							paramap.put(params[i + 3].toString(), reStr);
							return_para_list.add(paramap);
						} else if (params[i].toString().equalsIgnoreCase("double")) {
							double number = proc.getDouble(i / 4 + 1);
							try {
								reStr = String.valueOf(number);
							} catch (Exception ev) {
								logger.info("----DOUBLE ERROR");
								ev.printStackTrace();
							}
							paramap.put(params[i + 3].toString(), reStr);
							return_para_list.add(paramap);
						} else if (params[i].toString().equalsIgnoreCase("int")) {
							int number = proc.getInt(i / 4 + 1);
							try {
								reStr = String.valueOf(number);
							} catch (Exception ev) {
								logger.info("----INT ERROR");
								ev.printStackTrace();
							}
							paramap.put(params[i + 3].toString(), reStr);
							return_para_list.add(paramap);
						} else {
							reStr = proc.getString(i / 4 + 1);
							logger.info("----强制转换成getString()-----");
							paramap.put(params[i + 3].toString(), reStr);
							return_para_list.add(paramap);
						}
					}
				}

			}
			returnmap.put("return_para_list", return_para_list);// out 参数list

			/**
			 * 结果集合....取结果集..放入list_rs
			 */

			if (bool) {
				ResultSet rs = (ResultSet) proc.getResultSet();
				ResultSetMetaData rmd = rs.getMetaData();
				int Count = rmd.getColumnCount();
				logger.info("---------------------存储过程查询结果集列数-----" + Count);
				while (rs.next()) {
					String reS = "";
					Map map = new HashMap();

					for (int c = 0; c < Count; c++) {
						String columnName = rmd.getColumnName(c + 1);
						reS = rs.getString(c + 1);
						// logger.info("---reS=-----" + reS + "  = " +
						// columnName);
						map.put(columnName, reS == null ? "" : reS.toString().trim());
					}
					return_table_list.add(map);
				}
				logger.info("-------------------存储过程查询结果记录行数------" + return_table_list.size());
				rs.close();
			}
			returnmap.put("return_table_list", return_table_list); // 查询结果集
			/*
			 * 取结果集..
			 */
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
		} catch (SQLException ex) {

			try { // 更新出错，回滚，抛出异常
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			ex.printStackTrace();

		} finally {
			freeConn(); // 数据库操作完毕,释放数据库连接资源
		}
		return returnmap;

	}

	/**
	 * *获取字段描述信息，适用于oracle与sql server 没有描述信息的字段不能列出 字段名 column_name 描述 comments
	 * * @@param sql sql字符串 * @@param params * @@return
	 */
	public List selectColumnCommentsList(String table, String databasetype) throws Exception {
		List list = new ArrayList(); // updated by wuje at 2007-01-07 改为通用方法
										// 返回值为List类型
		rs = null;
		try {
			String sql = "";
			if (databasetype.equals("sql server")) {
				sql = " SELECT c.name as column_name, CONVERT(VARCHAR, p.[value]) AS comments " + " FROM sysproperties p INNER JOIN " + " sysobjects o ON p.id = o.id INNER JOIN "
						+ " syscolumns c ON p.id = c.id AND p.smallid = c.colid" + " WHERE (p.name = 'MS_Description') AND (o.name = '" + table + "') ";
				// 注意，sql server的存储的表名都是小写，而且大小写不区分
			} else if (databasetype.equals("oracle")) {
				sql = " select column_name,comments from  ALL_COL_COMMENTS where table_name='" + table.toUpperCase() + "'";
				// 注意，oracle 的存储的表名都是大写，而且大小写区分，不能使用小写
			}
			prep = conn.prepareStatement(sql);
			rs = prep.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();

			while (rs.next()) {
				Map map = new HashMap();

				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					String coltype = rsmd.getColumnTypeName(i);
					if (coltype.equals("text")) { // text类型字段必须经过转化string后读取
						map.put(rsmd.getColumnName(i).toLowerCase(), getClobString(rs, i));
						// 取出字段名称,字段名称全部小写字母
					} else {
						map.put(rsmd.getColumnName(i).toLowerCase(), rs.getObject(i) == null ? "" : rs.getObject(i));
						// 取出字段名称,字段名称全部小写字母 null值转化为空字符串
					}

				}

				list.add(map);
			}

		} catch (NumberFormatException ex) {
			ex.printStackTrace();
			System.err.println(ex.getMessage());
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.err.println(ex.getMessage());
		}
		freeConn(); // 数据库操作完毕,释放数据库连接资源
		return list;
	}

	/**
	 * *所有对数据库操作中需要返回RestsultSet的字符串 for example select * @@param sql sql字符串 * @@param
	 * params * @@return
	 */
	public List select(String sql, Object[] params) throws Exception {
		// conn.setAutoCommit(true);
		List list = new ArrayList(); // updated by wuje at 2007-01-07 改为通用方法
										// 返回值为List类型
		rs = null;
		try {
			prep = conn.prepareStatement(sql);
			int index = 1;
			if (params != null) {
				int n = params.length;
				if (n < 2 || n % 2 != 0 || (n / 2) != this.getStrNum(sql, "?")) {
					throw new IllegalArgumentException("参数为奇数或者是小于2,或者是参数的个数不一致");
				}
				for (int i = 0; i < params.length; i += 2) {
					params[i + 1] = this.pareObjToStr(params[i + 1]); // 把第二个参数对象转换为字符串类型
					if (((String) params[i]).toLowerCase().equals("string")) {
						prep.setString(index++, getRealString((String) params[i + 1]));
					}
					if (((String) params[i]).toLowerCase().equals("long")) {
						prep.setLong(index++, Long.parseLong((String) params[i + 1]));
					}
					if (((String) params[i]).toLowerCase().equals("int")) {
						prep.setInt(index++, Integer.parseInt((String) params[i + 1]));
					}
					if (((String) params[i]).toLowerCase().equals("date")) {
						prep.setString(index++, (String) params[i + 1]);
					}
					if (((String) params[i]).toLowerCase().equals("float")) {
						prep.setFloat(index++, Float.parseFloat((String) params[i + 1]));
					}
					if (((String) params[i]).toLowerCase().equals("double")) {
						prep.setDouble(index++, Double.parseDouble((String) params[i + 1]));
					}
					if (((String) params[i]).toLowerCase().equals("image")) {
						prep.setBytes(index++, (byte[]) params[i + 1]);
					}
				}
			}
			rs = prep.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();

			while (rs.next()) {
				Map map = new HashMap();

				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					// 取出字段名称,字段名称全部小写字母,null值转化为空字符串
					// 电话不可见时控制
					if (!showflag.equals("1") && (rsmd.getColumnName(i).toLowerCase().equals("consigneephone") || rsmd.getColumnName(i).toLowerCase().equals("consigneemobile"))) { // 电话
																																													// 手机隐藏
						map.put(rsmd.getColumnName(i).toLowerCase(), "[不可见]");
					} else {
						String coltype = rsmd.getColumnTypeName(i);
						if (coltype.equals("text") || coltype.equals("ntext")) { // text类型字段必须经过转化string后读取
							map.put(rsmd.getColumnName(i).toLowerCase(), getClobString(rs, i));
							// 取出字段名称,字段名称全部小写字母
						} else {
							map.put(rsmd.getColumnName(i).toLowerCase(), rs.getObject(i) == null ? "" : rs.getObject(i));
							// 取出字段名称,字段名称全部小写字母 null值转化为空字符串
						}
					}
				}
				list.add(map);
			}
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
			System.err.println(ex.getMessage());
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.err.println(ex.getMessage());
		} finally {
			freeConn(); // 数据库操作完毕,释放数据库连接资源
		}
		return list;
	}

	/**
	 * *分页处理查询类 包括拼串查询和一般查询 所有对数据库操作中需要返回RestsultSet的字符串 for example select
	 * 使用rs完毕之后，注意调用freeconn()方法 * @@param sql sql字符串 * @@param params * @@return
	 */
	public ResultSet selectForPages(String sql, Object[] params) throws Exception {

		rs = null;
		try {
			prep = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			// ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE
			// 此参数支持翻页类中的rs.first(),rs.last(),rs.absolute()等方法的使用
			int index = 1;
			if (params != null) {
				int n = params.length;
				if (n < 2 || n % 2 != 0) { // || (n / 2) != this.getStrNum(sql,
											// "?")) {
					throw new IllegalArgumentException("参数为奇数或者是小于2,或者是参数的个数不一致");
				}
				for (int i = 0; i < params.length; i += 2) {
					params[i + 1] = this.pareObjToStr(params[i + 1]); // 把第二个参数对象转换为字符串类型
					if (((String) params[i]).toLowerCase().equals("string")) {
						prep.setString(index++, (String) params[i + 1]);
					}
					if (((String) params[i]).toLowerCase().equals("long")) {
						prep.setLong(index++, Long.parseLong((String) params[i + 1]));
					}
					if (((String) params[i]).toLowerCase().equals("int")) {
						prep.setInt(index++, Integer.parseInt((String) params[i + 1]));
					}
					if (((String) params[i]).toLowerCase().equals("date")) {
						prep.setString(index++, (String) params[i + 1]);
					}
					if (((String) params[i]).toLowerCase().equals("float")) {
						prep.setFloat(index++, Float.parseFloat((String) params[i + 1]));
					}
					if (((String) params[i]).toLowerCase().equals("double")) {
						prep.setDouble(index++, Double.parseDouble((String) params[i + 1]));
					}
					if (((String) params[i]).toLowerCase().equals("image")) {
						prep.setBytes(index++, (byte[]) params[i + 1]);
					}
				}
			}
			rs = prep.executeQuery();

		} catch (NumberFormatException ex) {
			ex.printStackTrace();
			System.err.println(ex.getMessage());
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.err.println(ex.getMessage());
		}
		// freeConn();//数据库操作完毕,释放数据库连接资源
		return rs;
	}

	// 组合条件查询时,拼串后使用的查询方法
	public List selectbymore(String sql, Object[] params) throws Exception {
		List list = new ArrayList(); // updated by wuje at 2007-01-07 改为通用方法
										// 返回值为List类型
		rs = null;
		try {
			prep = conn.prepareStatement(sql);
			int index = 1;
			if (params != null) {
				int n = params.length;
				if (n < 2 || n % 2 != 0) { // || (n / 2) != this.getStrNum(sql,
											// "?")) {
					throw new IllegalArgumentException("参数为奇数或者是小于2,或者是参数的个数不一致");
				}
				for (int i = 0; i < params.length; i += 2) {
					params[i + 1] = this.pareObjToStr(params[i + 1]); // 把第二个参数对象转换为字符串类型
					if (((String) params[i]).toLowerCase().equals("string")) {
						prep.setString(index++, getRealString((String) params[i + 1]));
					}
					if (((String) params[i]).toLowerCase().equals("long")) {
						prep.setLong(index++, Long.parseLong((String) params[i + 1]));
					}
					if (((String) params[i]).toLowerCase().equals("int")) {
						prep.setInt(index++, Integer.parseInt((String) params[i + 1]));
					}
					if (((String) params[i]).toLowerCase().equals("date")) {
						prep.setString(index++, (String) params[i + 1]);
					}
					if (((String) params[i]).toLowerCase().equals("float")) {
						prep.setFloat(index++, Float.parseFloat((String) params[i + 1]));
					}
					if (((String) params[i]).toLowerCase().equals("double")) {
						prep.setDouble(index++, Double.parseDouble((String) params[i + 1]));
					}
					if (((String) params[i]).toLowerCase().equals("image")) {
						prep.setBytes(index++, (byte[]) params[i + 1]);
					}
				}
			}
			rs = prep.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();

			while (rs.next()) {
				Map map = new HashMap();

				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					// 取出字段名称,字段名称全部小写字母,null值转化为空字符串
					// 电话不可见时控制
					// logger.info("showflag="+showflag+"====coluname="+rsmd.getColumnName(i).toLowerCase());
					if (!showflag.equals("1") && (rsmd.getColumnName(i).toLowerCase().equals("consigneephone") || rsmd.getColumnName(i).toLowerCase().equals("consigneemobile"))) { // 电话
																																													// 手机隐藏
						map.put(rsmd.getColumnName(i).toLowerCase(), "[不可见]");
					} else {
						// map.put(rsmd.getColumnName(i).toLowerCase(),
						// rs.getObject(i)==null?"":rs.getObject(i));
						String coltype = rsmd.getColumnTypeName(i);
						if (coltype.equals("text") || coltype.equals("ntext")) { // text类型字段必须经过转化string后读取
							map.put(rsmd.getColumnName(i).toLowerCase(), getClobString(rs, i));
							// 取出字段名称,字段名称全部小写字母
						} else {
							map.put(rsmd.getColumnName(i).toLowerCase(), rs.getObject(i) == null ? "" : rs.getObject(i));
							// 取出字段名称,字段名称全部小写字母 null值转化为空字符串
						}

					}

				}

				list.add(map);
			}

		} catch (NumberFormatException ex) {
			ex.printStackTrace();
			System.err.println(ex.getMessage());
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.err.println(ex.getMessage());
		} finally {
			freeConn(); // 数据库操作完毕,释放数据库连接资源
		}
		return list;
	}

	/**
	 * *所有对数据库的更新操作 contains(insert update delete)
	 * 
	 * @@param sql 带参数的sql语句 * @@param params 参数数组 * @@return
	 */

	public int update(String sql, Object[] params) throws Exception {
		int num = 0;
		try {
			// conn.setAutoCommit(true);
			prep = conn.prepareStatement(sql);
			logger.info("sql" + sql);
			int index = 1;
			if (params != null) {
				int n = params.length;
				if (n < 2 || n % 2 != 0 || (n / 2) != this.getStrNum(sql, "?")) {
					throw new IllegalArgumentException("参数为奇数或者是小于2,或者是参数的个数不一致");
				}
				for (int i = 0; i < params.length; i += 2) {
					params[i + 1] = this.pareObjToStr(params[i + 1]);
					// 第二个参数对象转换为字符串类型
					if (((String) params[i]).toLowerCase().equals("string")) {
						prep.setString(index++, getRealString((String) params[i + 1]));
					}
					if (((String) params[i]).toLowerCase().equals("long")) {
						prep.setLong(index++, Long.parseLong((String) params[i + 1]));
					}
					if (((String) params[i]).toLowerCase().equals("int")) {
						prep.setInt(index++, Integer.parseInt((String) params[i + 1]));
					}
					if (((String) params[i]).toLowerCase().equals("date")) {
						prep.setString(index++, (String) params[i + 1]);
					}
					if (((String) params[i]).toLowerCase().equals("float")) {
						prep.setFloat(index++, Float.parseFloat((String) params[i + 1]));
					}
					if (((String) params[i]).toLowerCase().equals("double")) {
						prep.setDouble(index++, Double.parseDouble((String) params[i + 1]));
					}
					if (((String) params[i]).toLowerCase().equals("image")) {
						prep.setBytes(index++, (byte[]) params[i + 1]);
					}
				}
			}
			num = prep.executeUpdate();
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
		} catch (SQLException ex) {

			try { // 更新出错，回滚，抛出异常
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				// throw new logisException(e1.getMessage());
			}
			ex.printStackTrace();
			// throw new logisException(ex.getMessage());
		} finally {
			freeConn(); // 数据库操作完毕,释放数据库连接资源
		}
		return num;
	}

	/**
	 * 批量更新操作
	 * 
	 * @param sql
	 *            String
	 * @param params
	 *            Object[]
	 * @return int
	 * @throws Exception
	 */

	public int[] updateBatch(String sql, List paramslist) throws Exception {
		int[] num = null;
		try {
			// conn.setAutoCommit(false);
			// 有可能同时有多个用户对同一个表进行批量操作 避免造成死锁 不应设置为false
			prep = conn.prepareStatement(sql);

			long beginTime = System.currentTimeMillis();

			for (int p = 0; p < paramslist.size(); p++) {
				Object[] params = (Object[]) paramslist.get(p);
				int index = 1;
				if (params != null) {
					int n = params.length;
					if (n < 2 || n % 2 != 0 || (n / 2) != this.getStrNum(sql, "?")) {
						throw new IllegalArgumentException("参数为奇数或者是小于2,或者是参数的个数不一致");
					}
					for (int i = 0; i < params.length; i += 2) {
						params[i + 1] = this.pareObjToStr(params[i + 1]);
						// 第二个参数对象转换为字符串类型
						if (((String) params[i]).toLowerCase().equals("string")) {
							prep.setString(index++, getRealString((String) params[i + 1]));
						}
						if (((String) params[i]).toLowerCase().equals("long")) {
							prep.setLong(index++, Long.parseLong((String) params[i + 1]));
						}
						if (((String) params[i]).toLowerCase().equals("int")) {
							prep.setInt(index++, Integer.parseInt((String) params[i + 1]));
						}
						if (((String) params[i]).toLowerCase().equals("date")) {
							prep.setString(index++, (String) params[i + 1]);
						}
						if (((String) params[i]).toLowerCase().equals("float")) {
							prep.setFloat(index++, Float.parseFloat((String) params[i + 1]));
						}
						if (((String) params[i]).toLowerCase().equals("double")) {
							prep.setDouble(index++, Double.parseDouble((String) params[i + 1]));
						}
						if (((String) params[i]).toLowerCase().equals("image")) {
							prep.setBytes(index++, (byte[]) params[i + 1]);
						}
					}
				}
				prep.addBatch();
				this.batchnum = Integer.parseInt(config.getValue("BATCHNUM"));
				if (batchnum > 0) { // pst+batch
					if (p % batchnum == 0) { // 可以设置不同的大小；如50，100，500，1000等等
						long startTime = System.currentTimeMillis();
						prep.executeBatch();
						// conn.commit();
						prep.clearBatch();
						prep.clearParameters();
						long endTime = System.currentTimeMillis();
						logger.info("本批次到" + p + " pst+batch 耗时 " + (endTime - startTime) / 1000 + "秒");
					}
				}
			}

			num = prep.executeBatch();
			// conn.commit();
			long endTime = System.currentTimeMillis();
			logger.info("==========sql======");
			logger.info(sql);
			logger.info("==========sql======");
			logger.info((batchnum > 0 ? "全部" + paramslist.size() + " pst+batch+batchnum 耗时 " : "全部" + paramslist.size() + " pst+batch 耗时 ") + (endTime - beginTime) / 1000 + "秒");

		} catch (NumberFormatException ex) {
			ex.printStackTrace();
		} catch (SQLException ex) {

			try { // 更新出错，回滚，抛出异常
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				// throw new logisException(e1.getMessage());
			}
			ex.printStackTrace();
			// throw new logisException(ex.getMessage());
		} finally {
			freeConn(); // 数据库操作完毕,释放数据库连接资源
		}
		return num;
	}

	/**
	 * * 把Object类型对象按照相应的类型进行转换，返回String类型 * @@param obj * @@return 返回String类型
	 */
	private String pareObjToStr(Object obj) {
		if (obj == null) {
			return null;
		}
		if (obj instanceof String) {
			return obj.toString();
		}
		if (obj instanceof Integer) {
			return ((Integer) obj).toString();
		}
		if (obj instanceof Long) {
			return ((Long) obj).toString();
		}
		if (obj instanceof Float) {
			return ((Float) obj).toString();
		}
		if (obj instanceof Double) {
			return ((Double) obj).toString();
		}
		if (obj instanceof java.util.Date) {
			return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((java.util.Date) obj);
		}
		return obj.toString();
	}

	/**
	 * *统计原来的字符串中包含的实际字符串的个数 * @@param sql 原来的字符串 * @@param str 其中包含的字符串 * @@return
	 * 原字符从串中包含的字符串的个数
	 */
	private int getStrNum(String sql, String str) {
		int num = 0;
		int index = sql.indexOf(str);
		while (index != -1) {
			num++;
			index = sql.indexOf(str, index + str.length());
		}
		return num;
	}

	// public int getNextId(String sequenceName) throws
	// Exception{
	// return Sequence.getNextId(conn,sequenceName);
	// }

	/** * 释放数据库连接资源 */
	public void freeConn() {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}

		}
		if (prep != null) {
			try {
				prep.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}

		}
		if (conn != null) {
			try {
				connpool.freeConnection(this.urlname, conn);
				logger.info(" [coonpool 释放一个连接 ] ");
			} catch (Exception se) {
				se.printStackTrace();
			}

		}

	}

	// 获取Sql server中的ntext或者text类型数据，转化为string
	public static String getClobString(ResultSet rs, int col) {
		try {
			java.io.Reader reader = rs.getCharacterStream(col);
			if (reader == null) {
				return "";
			}
			StringBuffer sb = new StringBuffer();
			char[] charbuf = new char[4096];
			for (int i = reader.read(charbuf); i > 0; i = reader.read(charbuf)) {
				sb.append(charbuf, 0, i);
			}
			return sb.toString();
		} catch (Exception e) {
			return "";
		}
	}

	// 替换看不见的ASC字符 后的参数
	private static String getRealString(String parameter) {
		if (parameter != null) {
			return parameter.replaceAll(String.valueOf((char) (127)), "").replaceAll(String.valueOf((char) (28)), "").replaceAll(String.valueOf((char) (29)), "")
					.replaceAll(String.valueOf((char) (31)), "");
		}
		return null;
	}

	// 一个CWB一个费用类型存储过程方法
	public void callsqlProc(String cwb, String feetype) throws Exception {
		try {
			String spName = "{call clearcwbfee(?,?)}";
			callstm = conn.prepareCall(spName);
			callstm.setString(1, cwb);
			callstm.setString(2, feetype);
			callstm.execute();
		} catch (SQLException e) {
			conn.rollback();
		}
	}

	// 执行计算站点费用的存储过程
	public void callbranchProc(String feetype, String sendtimestart, String sendtimeend, int branchid) throws Exception {
		try {
			String spName = "{call branchclearfeecount(?,?,?,?)}";
			callstm = conn.prepareCall(spName);
			callstm.setString(1, feetype);
			callstm.setString(2, sendtimestart);
			callstm.setString(3, sendtimeend);
			callstm.setInt(4, branchid);
			callstm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			freeConn();
		}
	}

	// 执行计算员工费用的存储过程
	public void calldeliverProc(String feetype, int deliverid, String delivertimestart, String delivertimeend) throws Exception {
		try {
			String spName = "{call deliverclearfeecount(?,?,?,?)}";
			callstm = conn.prepareCall(spName);
			callstm.setString(1, feetype);
			callstm.setInt(2, deliverid);
			callstm.setString(3, delivertimestart);
			callstm.setString(4, delivertimeend);
			callstm.execute();
		} catch (SQLException e) {
			conn.rollback();
		}
	}

	// 20110219 批量插入单表
	public void insertData_OneTable_ForBatch(String tablename, // 标名称
			List insertcolumnlist, // 插入字段名称
			List insertparaslist // 一组参数paralist数组
	) throws Exception {

		String insertsql = " insert into " + tablename + "( ";
		String valuessql = " values(";
		for (int i = 0; i < insertcolumnlist.size(); i++) {
			String column = (String) insertcolumnlist.get(i);
			insertsql += "" + column + ",";
			valuessql += "?,";
		}
		insertsql = insertsql.substring(0, insertsql.length() - 1) + ")"; // 去掉最后一个逗号,
		valuessql = valuessql.substring(0, valuessql.length() - 1) + ")"; // 去掉最后一个逗号,

		insertsql = insertsql + valuessql;
		updateBatch(insertsql, insertparaslist);
	}

	// 20110219 批量更新单表 每个SQL执行的更新值 条件值都不一样
	public void updateData_OneTable_ForBatch(String tablename, // 表名称
			List updatecolumnlist, // 更新字段名称
			List condtioncolumnlist, // 条件字段名称
			List updateparaslist, // 一组参数paralist数组 包括字段和条件的值
			String otherupdatecolumnsetsql, // 其他更新字段sql，更新到常量 无其他字段为""
			String otherconditionsql // 其他条件sql ，需要带上and 无其他其他条件为""
	) throws Exception {

		String updatesetsql = "";
		if (updatecolumnlist != null) {
			for (int i = 0; i < updatecolumnlist.size(); i++) {
				String column = (String) updatecolumnlist.get(i);
				if (i < updatecolumnlist.size() - 1) {
					updatesetsql += column + "=?,";
				} else {
					updatesetsql += column + "=?";
				}
			}
		}
		String condtioncolumnsql = " where 1=1 ";
		if (condtioncolumnlist != null) {
			for (int i = 0; i < condtioncolumnlist.size(); i++) {
				String column = (String) condtioncolumnlist.get(i);
				condtioncolumnsql += " and " + column + "=? ";
			}
		}

		String updatesql = " update " + tablename + " set " + updatesetsql
				+ (otherupdatecolumnsetsql != null && !otherupdatecolumnsetsql.equals("") ? (updatecolumnlist != null ? "," + otherupdatecolumnsetsql : otherupdatecolumnsetsql) : "")
				+ condtioncolumnsql + otherconditionsql;
		logger.info("======BATCHSQL=====" + updatesql);
		updateBatch(updatesql, updateparaslist);

	}

}
