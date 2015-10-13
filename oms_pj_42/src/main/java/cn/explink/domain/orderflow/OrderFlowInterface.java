package cn.explink.domain.orderflow;

import net.sf.json.JSONObject;

public interface OrderFlowInterface {

	/**
	 * @return 返回组织好的环节描述 将bean中的需要输出的内容组合在一起返回
	 */
	public String getBody(OrderFlow orderFlow);

	/**
	 * 加载数据库中的JSONObject的对象
	 * 
	 * @param floworderdetail
	 */
	public void loadFloworderdetailToProperty(JSONObject floworderdetail);
}
