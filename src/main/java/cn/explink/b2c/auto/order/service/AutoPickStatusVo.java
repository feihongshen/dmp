package cn.explink.b2c.auto.order.service;

	/**
	 * 分拨状态推送DMP-vo
	 * <p>
	 * 类详细描述
	 * </p>
	 * @author vince.zhou
	 * @since 1.0
	 */
	public class AutoPickStatusVo {

	    private String order_sn;//订单号

	    private String cust_code;//客户编码

	    private String transport_no;//运单号

	    private String operate_type;//操作类型

	    private String operate_time;//操作时间

	    private String operator;//操作人

	    private String operate_org;//操作机构 (分拣机)

	    private String destination_org;//目的机构

	    private String original_weight;//重量

	    private String original_volume;//体积

	    private String package_no;//包号

	    public String getOrder_sn() {
	        return this.order_sn;
	    }

	    public void setOrder_sn(String order_sn) {
	        this.order_sn = order_sn;
	    }

	    public String getCust_code() {
	        return this.cust_code;
	    }

	    public void setCust_code(String cust_code) {
	        this.cust_code = cust_code;
	    }

	    public String getTransport_no() {
	        return this.transport_no;
	    }

	    public void setTransport_no(String transport_no) {
	        this.transport_no = transport_no;
	    }

	    public String getOperate_type() {
	        return this.operate_type;
	    }

	    public void setOperate_type(String operate_type) {
	        this.operate_type = operate_type;
	    }

	    public String getOperate_time() {
	        return this.operate_time;
	    }

	    public void setOperate_time(String operate_time) {
	        this.operate_time = operate_time;
	    }

	    public String getOperator() {
	        return this.operator;
	    }

	    public void setOperator(String operator) {
	        this.operator = operator;
	    }

	    public String getOperate_org() {
	        return this.operate_org;
	    }

	    public void setOperate_org(String operate_org) {
	        this.operate_org = operate_org;
	    }

	    public String getDestination_org() {
	        return this.destination_org;
	    }

	    public void setDestination_org(String destination_org) {
	        this.destination_org = destination_org;
	    }

	    public String getOriginal_weight() {
	        return this.original_weight;
	    }

	    public void setOriginal_weight(String original_weight) {
	        this.original_weight = original_weight;
	    }

	    public String getOriginal_volume() {
	        return this.original_volume;
	    }

	    public void setOriginal_volume(String original_volume) {
	        this.original_volume = original_volume;
	    }

	    public String getPackage_no() {
	        return this.package_no;
	    }

	    public void setPackage_no(String package_no) {
	        this.package_no = package_no;
	    }
}
