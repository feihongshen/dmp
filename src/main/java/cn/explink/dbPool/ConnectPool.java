package cn.explink.dbPool;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.InetAddress;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.StringTokenizer;
import java.io.*;

/**
 * 管理类DBConnectionManager支持对一个或多个由属性文件定义的数据库连接
 * 池的访问.客户程序可以调用getInstance()方法访问本类的唯一实例.
 */
public class ConnectPool {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	static public ConnectPool instance; // 唯一实例
	static public int clients;
	public Vector drivers = new Vector(); // 驱动
	public PrintWriter log;
	public Hashtable pools = new Hashtable(); // 连接

	/**
	 * 返回唯一实例.如果是第一次调用此方法,则创建实例
	 *
	 * @return DBConnectionManager 唯一实例
	 */
	static synchronized public ConnectPool getInstance() {
		if (instance == null) {
			instance = new ConnectPool();
		}

		clients++;

		return instance;
	}

	/**
	 * 建构函数私有以防止其它对象创建本类实例
	 */
	public ConnectPool() {
		init();
	}

	/**
	 * 将连接对象返回给由名字指定的连接池
	 *
	 * @param name
	 *            在属性文件中定义的连接池名字
	 * @param con
	 *            连接对象
	 */
	public void freeConnection(String name, Connection con) {
		DBConnectionPool pool = (DBConnectionPool) pools.get(name);
		if (pool != null) {
			pool.freeConnection(con);
		} else {
			logger.info("pool ==null");
		}
		clients--;
	}

	/**
	 * 获得一个可用的(空闲的)连接.如果没有可用连接,且已有连接数小于最大连接数 限制,则创建并返回新连接
	 *
	 * @param name
	 *            在属性文件中定义的连接池名字
	 * @return Connection 可用连接或null
	 */
	public Connection getConnection(String name) {
		DBConnectionPool pool = (DBConnectionPool) pools.get(name);
		if (pool != null) {

			return pool.returnConnection();
		}
		return null;
	}

	/**
	 * 获得一个可用连接.若没有可用连接,且已有连接数小于最大连接数限制, 则创建并返回新连接.否则,在指定的时间内等待其它线程释放连接.
	 *
	 * @param name
	 *            连接池名字
	 * @param time
	 *            以毫秒计的等待时间
	 * @return Connection 可用连接或null
	 */
	public Connection getConnection(String name, long time) {
		DBConnectionPool pool = (DBConnectionPool) pools.get(name);
		if (pool != null) {
			return pool.getConnection(time);
		}
		return null;
	}

	/**
	 * 关闭所有连接,撤销驱动程序的注册
	 */
	public synchronized void release() {
		// 等待直到最后一个客户程序调用
		if (--clients != 0) {
			return;
		}

		Enumeration allPools = pools.elements();
		while (allPools.hasMoreElements()) {
			DBConnectionPool pool = (DBConnectionPool) allPools.nextElement();
			pool.release();
		}
		Enumeration allDrivers = drivers.elements();
		while (allDrivers.hasMoreElements()) {
			Driver driver = (Driver) allDrivers.nextElement();
			try {
				DriverManager.deregisterDriver(driver);

				// log("撤销JDBC驱动程序 " + driver.getClass().getName() + "的注册");
				logger.info("撤销JDBC驱动程序 " + driver.getClass().getName() + "的注册");
			} catch (SQLException e) {
				// log(e, "无法撤销下列JDBC驱动程序的注册: " + driver.getClass().getName());

				logger.info("无法撤销下列JDBC驱动程序的注册: " + driver.getClass().getName() + ", 错误: " + e);
			}
		}
	}

	/**
	 * 根据指定属性创建连接池实例.
	 *
	 * @param props
	 *            连接池属性
	 */
	private void createPools(Properties props) {
		Enumeration propNames = props.propertyNames();
		while (propNames.hasMoreElements()) {
			String name = (String) propNames.nextElement();
			if (name.endsWith(".url")) {

				String poolName = name.substring(0, name.lastIndexOf("."));

				String url = props.getProperty(poolName + ".url"); // db.properties配置文件

				logger.info("连接池URL是：" + url);
				if (url == null) {
					// log("没有为连接池" + poolName + "指定URL");
					logger.info("没有为连接池" + poolName + "指定URL");
					continue;
				}
				String user = props.getProperty(poolName + ".user"); // db.properties配置文件
				String password = props.getProperty(poolName + ".password"); // db.properties配置文件
				String maxconn = props.getProperty(poolName + ".maxconn", "100"); // db.properties配置文件

				int max;
				try {
					max = Integer.valueOf(maxconn).intValue();
				} catch (NumberFormatException e) {
					// log("错误的最大连接数限制: " + maxconn + " .连接池: " + poolName);
					logger.info("错误的最大连接数限制: " + maxconn + " .连接池: " + poolName + ", 错误: " + e);
					max = 0;
				}
				DBConnectionPool pool = new DBConnectionPool(poolName, url, user, password, max);
				pools.put(poolName, pool);
				// log("成功创建连接池" + poolName);
				logger.info("成功创建连接池" + poolName + " URL:" + url);

			}
		}
	}

	// 登录IP地址判断
	public boolean checkServerIP() throws Exception {
		boolean flag = false;
		InetAddress inet = InetAddress.getLocalHost();
		String hostAddress = inet.getHostAddress(); // 获取JSP服务器IP地址
		logger.info("IP address = [" + hostAddress + "]");
		if (hostAddress.equals("124.42.120.61")) {// 飞远117.36.73.254
													// //誉诚59.108.63.4//福建飞远61.154.11.184
			flag = true;
		}
		return flag;
	}

	// 登录MAC地址判断
	public boolean checkServerMac() throws Exception {
		boolean flag = false;
		String MACAddr = "";
		try {
			Process process = Runtime.getRuntime().exec("ipconfig /all");
			InputStreamReader ir = new InputStreamReader(process.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);
			String line;
			while ((line = input.readLine()) != null)
				if (line.indexOf("Physical Address") > 0) {
					MACAddr = line.substring(line.indexOf("-") - 2);
					logger.info("MAC address = [" + MACAddr + "]");
					break;

				}
		} catch (java.io.IOException e) {
			System.err.println("IOException " + e.getMessage());
		}
		// 获取服务器MAC地址 龙腾新地址 0019B92AA246 00-1D-60-1E-E5-A2
		if (MACAddr.equals("00-15-17-44-08-D0") || MACAddr.equals("00-E0-4C-61-E2-DE")) {
			// 成都VPN 00-53-45-00-00-00 00-1D-60-1E-E5-A2 成都 00-0D-60-76-9B-80 本地
			// 陕西2块网卡00-1E-C9-55-48-F7 00-E0-4C-61-E2-DE
			// 福建飞远00-0D-61-76-60-FE
			// 经管信成00-1F-3C-6B-E2-19 00-0A-E4-44-5D-A6 00-0B-2F-13-19-08
			// 上海财大 00-1F-C6-4C-D0-F8
			// 武汉 00-15-17-44-08-D0
			flag = true;
		}
		return flag;
	}

	/*
	 * //获取windwos或者linux下mac地址 private final static String getMacAddress()
	 * throws IOException { String os = System.getProperty("os.name");
	 * 
	 * try { if (os.startsWith("Windows")) { return
	 * windowsParseMacAddress(windowsRunIpConfigCommand()); } else if
	 * (os.startsWith("Linux")) { return
	 * linuxParseMacAddress(linuxRunIfConfigCommand()); } else { throw new
	 * IOException("unknown operating system: " + os); } } catch (ParseException
	 * ex) { ex.printStackTrace(); throw new IOException(ex.getMessage()); } }
	 * 
	 * private final static String windowsRunIpConfigCommand() throws
	 * IOException { Process p = Runtime.getRuntime().exec("ipconfig /all");
	 * InputStream stdoutStream = new BufferedInputStream(p.getInputStream());
	 * 
	 * StringBuffer buffer = new StringBuffer(); for (; ; ) { int c =
	 * stdoutStream.read(); if (c == -1)break; buffer.append((char) c); } String
	 * outputText = buffer.toString();
	 * 
	 * stdoutStream.close();
	 * 
	 * return outputText; }
	 * 
	 * private final static String windowsParseMacAddress(String
	 * ipConfigResponse) throws ParseException { String localHost = null; try {
	 * localHost = InetAddress.getLocalHost().getHostAddress(); } catch
	 * (java.net.UnknownHostException ex) { ex.printStackTrace(); throw new
	 * ParseException(ex.getMessage(), 0); }
	 * 
	 * StringTokenizer tokenizer = new StringTokenizer(ipConfigResponse, "\n");
	 * String lastMacAddress = null;
	 * 
	 * while (tokenizer.hasMoreTokens()) { String line =
	 * tokenizer.nextToken().trim();
	 * 
	 * // see if line contains IP address if (line.endsWith(localHost) &&
	 * lastMacAddress != null) { return lastMacAddress; }
	 * 
	 * // see if line contains MAC address int macAddressPosition =
	 * line.indexOf(":"); if (macAddressPosition <= 0)continue;
	 * 
	 * String macAddressCandidate = line.substring(macAddressPosition +
	 * 1).trim(); if (windowsIsMacAddress(macAddressCandidate)) { lastMacAddress
	 * = macAddressCandidate; continue; } }
	 * 
	 * ParseException ex = new ParseException("cannot read MAC address from [" +
	 * ipConfigResponse + "]", 0); ex.printStackTrace(); throw ex; }
	 * 
	 * 
	 * private final static boolean windowsIsMacAddress(String
	 * macAddressCandidate) { // TODO: use a smart regular expression if
	 * (macAddressCandidate.length() != 17)return false;
	 * 
	 * return true; }
	 * 
	 * private final static String linuxParseMacAddress(String ipConfigResponse)
	 * throws ParseException { String localHost = null; try { localHost =
	 * InetAddress.getLocalHost().getHostAddress(); } catch
	 * (java.net.UnknownHostException ex) { ex.printStackTrace(); throw new
	 * ParseException(ex.getMessage(), 0); }
	 * 
	 * StringTokenizer tokenizer = new StringTokenizer(ipConfigResponse, "\n");
	 * String lastMacAddress = null;
	 * 
	 * while (tokenizer.hasMoreTokens()) { String line =
	 * tokenizer.nextToken().trim(); boolean containsLocalHost =
	 * line.indexOf(localHost) >= 0;
	 * 
	 * // see if line contains IP address if (containsLocalHost &&
	 * lastMacAddress != null) { return lastMacAddress; }
	 * 
	 * // see if line contains MAC address int macAddressPosition =
	 * line.indexOf("HWaddr"); if (macAddressPosition <= 0)continue;
	 * 
	 * String macAddressCandidate = line.substring(macAddressPosition +
	 * 6).trim(); if (linuxIsMacAddress(macAddressCandidate)) { lastMacAddress =
	 * macAddressCandidate; continue; } }
	 * 
	 * ParseException ex = new ParseException ("cannot read MAC address for " +
	 * localHost + " from [" + ipConfigResponse + "]", 0); ex.printStackTrace();
	 * throw ex; }
	 * 
	 * 
	 * private final static boolean linuxIsMacAddress(String
	 * macAddressCandidate) { // TODO: use a smart regular expression if
	 * (macAddressCandidate.length() != 17)return false; return true; }
	 * 
	 * 
	 * private final static String linuxRunIfConfigCommand() throws IOException
	 * { Process p = Runtime.getRuntime().exec("ifconfig"); InputStream
	 * stdoutStream = new BufferedInputStream(p.getInputStream());
	 * 
	 * StringBuffer buffer = new StringBuffer(); for (; ; ) { int c =
	 * stdoutStream.read(); if (c == -1)break; buffer.append((char) c); } String
	 * outputText = buffer.toString();
	 * 
	 * stdoutStream.close();
	 * 
	 * return outputText; }
	 */

	// 配置连接属性文件
	private void createPools() {
		String poolName = "expressurl";
		String url = "jdbc:jtds:sqlserver://localhost:1433/expressdata2";
		String user = "sa";
		String password = "";
		String maxconn = "20";
		int max;
		try {
			max = Integer.valueOf(maxconn).intValue();
		} catch (NumberFormatException e) {
			logger.info("错误的最大连接数限制: " + maxconn + " .连接池: " + poolName + ", 错误: " + e);
			max = 0;
		}
		DBConnectionPool pool = new DBConnectionPool(poolName, url, user, password, max);
		pools.put(poolName, pool);
		logger.info("成功创建连接池" + poolName);
	}

	// 配置驱动程序
	private void loadDrivers() {

		String driverClasses = "net.sourceforge.jtds.jdbc.Driver";
		String driverClassName = driverClasses;
		try {
			Driver driver = (Driver) Class.forName(driverClassName).newInstance();
			DriverManager.registerDriver(driver);
			drivers.addElement(driver);
			logger.info(driverClassName);
			logger.info("成功注册JDBC驱动程序" + driverClassName);
		} catch (Exception e) {
			logger.info("无法注册JDBC驱动程序: " + driverClassName + ", 错误: " + e);
		}

	}

	/**
	 * 读取属性完成初始化
	 */
	private void init() {
		try {
			// Properties p = new Properties();
			// InputStream is = new FileInputStream(getClass().getResource("/" +
			// "db.properties").getPath());
			// System.out.println("configs file local at " +
			// getClass().getResource("/" + "db.properties").getPath());
			InputStream is = (getClass().getClassLoader().getResourceAsStream("/" + "dbsqlserver.properties"));
			logger.info("configs file local at " + getClass().getClassLoader().getResourceAsStream("/" + "dbsqlserver.properties"));

			Properties dbProps = new Properties();
			try {
				dbProps.load(is);
			} catch (Exception e) {
				System.err.println("不能读取属性文件. " + "请确保dbsqlserver.properties在CLASSPATH指定的路径中");
				return;
			}

			loadDrivers(dbProps); // 使用db.properties
			createPools(dbProps); // 使用db.properties
			// loadDrivers();
			// createPools();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 171 * 装载和注册所有JDBC驱动程序 172 * 173 * @param props 属性 174
	 */
	private void loadDrivers(Properties props) {
		String driverClasses = props.getProperty("drivers"); // db.properties配置文件

		StringTokenizer st = new StringTokenizer(driverClasses);
		while (st.hasMoreElements()) {
			String driverClassName = st.nextToken().trim();
			try {
				Driver driver = (Driver) Class.forName(driverClassName).newInstance();
				DriverManager.registerDriver(driver);
				drivers.addElement(driver);
				logger.info(driverClassName);
				// log("成功注册JDBC驱动程序" + driverClassName);
				logger.info("成功注册JDBC驱动程序" + driverClassName);
			} catch (Exception e) {
				// log("无法注册JDBC驱动程序: " + driverClassName + ", 错误: " + e);

				logger.info("无法注册JDBC驱动程序: " + driverClassName + ", 错误: " + e);
			}
		}
	}

	/**
	 * 将文本信息写入日志文件
	 */
	/*
	 * private void log(String msg) { log.println(new Date() + ": " + msg); }
	 */

	/**
	 * 将文本信息与异常写入日志文件
	 */
	/*
	 * private void log(Throwable e, String msg) { log.println(new Date() + ": "
	 * + msg); e.printStackTrace(log); }
	 */

	/**
	 * 此内部类定义了一个连接池.它能够根据要求创建新连接,直到预定的最 大连接数为止.在返回连接给客户程序之前,它能够验证连接的有效性.
	 */

	class DBConnectionPool {
		// private int checkedOut;
		private Vector freeConnections = new Vector();
		private int maxConn;
		private String name;
		private String password;
		private String URL;
		private String user;
		public PrintWriter log;

		/**
		 * 创建新的连接池
		 *
		 * @param name
		 *            连接池名字
		 * @param URL
		 *            数据库的JDBC URL
		 * @param user
		 *            数据库帐号,或 null
		 * @param password
		 *            密码,或 null
		 * @param maxConn
		 *            此连接池允许建立的最大连接数
		 */
		public DBConnectionPool(String name, String URL, String user, String password, int maxConn) {
			this.name = name;
			this.URL = URL;
			this.user = user;
			this.password = password;
			this.maxConn = maxConn;
		}

		/**
		 * 将不再使用的连接返回给连接池
		 *
		 * @param con
		 *            客户程序释放的连接
		 */
		public synchronized void freeConnection(Connection con) {
			// 将指定连接加入到向量末尾
			try {
				if (con.isClosed()) {
					logger.info("before freeConnection con is closed");
				}
				freeConnections.addElement(con);
				Connection contest = (Connection) freeConnections.lastElement();
				if (contest.isClosed()) {
					logger.info("after freeConnection contest is closed");
				}
				notifyAll();
			} catch (SQLException e) {
				logger.error("客户程序释放的连接", e);
			}
		}

		/**
		 * 从连接池获得一个可用连接.如没有空闲的连接且当前连接数小于最大连接 数限制,则创建新连接.如原来登记为可用的连接不再有效,则从向量删除之,
		 * 然后递归调用自己以尝试新的可用连接.
		 */
		public synchronized Connection getConnection() {
			Connection con = null;
			if (freeConnections.size() > 0) {
				// 获取向量中第一个可用连接
				con = (Connection) freeConnections.firstElement();
				freeConnections.removeElementAt(0);
				try {
					if (con.isClosed()) {
						// log("从连接池" + name + "删除一个无效连接");
						logger.info("从连接池" + name + "删除一个无效连接");
						// 递归调用自己,尝试再次获取可用连接
						con = getConnection();
					}
				} catch (SQLException e) {

					// log("从连接池" + name + "删除一个无效连接时错误");
					logger.info("从连接池" + name + "删除一个无效连接出错" + ", 错误: " + e);
					// 递归调用自己,尝试再次获取可用连接
					con = getConnection();
				}
				if (freeConnections.size() > maxConn) {
					logger.info(" 删除一个溢出连接 ");
					releaseOne();
				}
			}

			else if ((maxConn == 0) || (freeConnections.size() < maxConn)) {
				con = newConnection();
			}

			return con;
		}

		public synchronized Connection returnConnection() {
			Connection con = null;
			// 如果闲置小于最大连接,返回一个新连接
			if (freeConnections.size() < maxConn) {
				con = newConnection();
			}
			// 如果闲置大于最大连接，返回一个可用的旧连接
			else if (freeConnections.size() >= maxConn) {

				con = (Connection) freeConnections.firstElement();
				logger.info(" [a 连接池可用连接数 ] : " + "[ " + freeConnections.size() + " ] URL:" + URL);
				freeConnections.removeElementAt(0);
				logger.info(" [b 连接池可用连接数 ] : " + "[ " + freeConnections.size() + " ] URL:" + URL);
				try {
					if (con.isClosed()) {
						// log("从连接池" + name + "删除一个无效连接");
						logger.info("从连接池" + name + "删除一个无效连接 URL:" + URL);
						returnConnection();
					}
				} catch (SQLException e) {

					// log("从连接池" + name + "删除一个无效连接时错误");
					logger.info("从连接池" + name + "删除一个无效连接出错" + ", 错误: " + e);
					returnConnection();
				}
			}
			return con;
		}

		/**
		 * 从连接池获取可用连接.可以指定客户程序能够等待的最长时间 参见前一个getConnection()方法.
		 *
		 * @param timeout
		 *            以毫秒计的等待时间限制
		 */
		public synchronized Connection getConnection(long timeout) {
			long startTime = new Date().getTime();
			Connection con;
			while ((con = getConnection()) == null) {
				try {
					wait(timeout);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if ((new Date().getTime() - startTime) >= timeout) {
					// wait()返回的原因是超时
					return null;
				}
			}
			return con;
		}

		/**
		 * 关闭所有连接
		 */
		public synchronized void release() {
			Enumeration allConnections = freeConnections.elements();
			while (allConnections.hasMoreElements()) {
				Connection con = (Connection) allConnections.nextElement();
				try {
					con.close();
					// log("关闭连接池" + name + "中的一个连接");
					logger.info("关闭连接池" + name + "中的一个连接");
				} catch (SQLException e) {
					// log(e, "无法关闭连接池" + name + "中的连接");

					logger.info("无法关闭连接池" + name + "中的连接" + ", 错误: " + e);
				}
			}
			freeConnections.removeAllElements();
		}

		/**
		 * 关闭一个连接
		 */
		public synchronized void releaseOne() {
			if (freeConnections.firstElement() != null) {
				Connection con = (Connection) freeConnections.firstElement();
				try {
					con.close();
					logger.info("关闭连接池" + name + "中的一个连接");
					// log("关闭连接池" + name + "中的一个连接");
				} catch (SQLException e) {

					logger.info("无法关闭连接池" + name + "中的一个连接" + ", 错误: " + e);
					// log(e, "无法关闭连接池" + name + "中的连接");
				}
			} else {
				logger.info("releaseOne() bug.......................................................");

			}
		}

		/**
		 * 创建新的连接
		 */
		private Connection newConnection() {
			Connection con = null;
			try {
				if (user == null) {
					con = DriverManager.getConnection(URL);
				} else {
					con = DriverManager.getConnection(URL, user, password);
				}
				// log("连接池" + name + "创建一个新的连接");
				logger.info("连接池" + name + "创建一个新的连接 URL:" + URL);
			} catch (SQLException e) {
				// log(e, "无法创建下列URL的连接: " + URL);

				logger.info("无法创建下列URL的连接: " + URL + ", 错误: " + e);
				return null;
			}
			return con;
		}

	}

}
