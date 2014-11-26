package cn.explink.b2c.wenxuan.jdto;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * 文轩的json bean
 * 
 * @author Administrator
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WinxuanOrders {

	private List<Orderlist> orderlist;
	private Pagination pagination;
	private State state;

	public List<Orderlist> getOrderlist() {
		return orderlist;
	}

	public void setOrderlist(List<Orderlist> orderlist) {
		this.orderlist = orderlist;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Pagination getPagination() {
		return pagination;
	}

	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}

}
