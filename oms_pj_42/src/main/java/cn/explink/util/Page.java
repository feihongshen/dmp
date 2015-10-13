package cn.explink.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class Page {

	public static final int ONE_PAGE_NUMBER = 10;
	public static final int DOWN_PAGE_NUMBER = 20;
	public static final int EXCEL_PAGE_NUMBER = 150000;
	private long previous;
	private long next;
	private long maxpage;
	private long total;

	/**
	 * @return 上一页页码
	 */
	public long getPrevious() {
		return previous;
	}

	/**
	 * @return 下一页页码
	 */
	public long getNext() {
		return next;
	}

	/**
	 * @return 最后一页 页码
	 */
	public long getMaxpage() {
		return maxpage;
	}

	/**
	 * @return 记录总数
	 */
	public long getTotal() {
		return total;
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * @param sql
	 *            必须带有 limit的sql语句string 要求语句全小写
	 * @return 返回一个组装好的div标签
	 */
	public Page() {

	}

	public Page(String sql) {

		if (sql.indexOf("limit") > -1) {
			// 总记录数
			total = jdbcTemplate.queryForLong("select count(1) " + sql.split("limit")[0].substring(sql.split("limit")[0].indexOf("from")));
			String limit = sql.split("limit")[1];
			long currentLine = Long.parseLong(limit.split(",")[0].trim());
			long pagenum = Long.parseLong(limit.split(",")[1].trim());

			// 上一页
			previous = currentLine / pagenum;
			// 下一页
			next = (currentLine / pagenum) + 2;
			// 总页数
			maxpage = (total + (pagenum - 1)) / pagenum;

		}
	}

	/**
	 * 
	 * @param total
	 *            总数
	 * @param currentPage
	 *            当前开始行数
	 * @param pagenum
	 *            每页记录数
	 */
	public Page(long total, long currentPage, long pagenum) {

		// 总页数
		maxpage = (total + (pagenum - 1)) / pagenum;
		// 上一页
		previous = (currentPage - 1) < 1 ? 1 : (currentPage - 1);
		// 下一页
		next = (currentPage + 1) > maxpage ? maxpage : (currentPage + 1);
		// 总记录数
		this.total = total;
	}
}
