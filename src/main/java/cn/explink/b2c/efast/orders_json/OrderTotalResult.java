package cn.explink.b2c.efast.orders_json;

import java.util.List;

/**
 * 获取订单列表接口返回总页数 实体
 * 
 * @author Administrator
 *
 */
public class OrderTotalResult {

	private long total_results; // 总数目
	private long page_no; // 当前页
	private long page_size; // 每页数量
	private List<OrderInfo> list;

	public long getTotal_results() {
		return total_results;
	}

	public void setTotal_results(long total_results) {
		this.total_results = total_results;
	}

	public long getPage_no() {
		return page_no;
	}

	public void setPage_no(long page_no) {
		this.page_no = page_no;
	}

	public long getPage_size() {
		return page_size;
	}

	public void setPage_size(long page_size) {
		this.page_size = page_size;
	}

	public List<OrderInfo> getList() {
		return list;
	}

	public void setList(List<OrderInfo> list) {
		this.list = list;
	}

}
