package cn.explink.domain.express;

import java.util.Date;

public class ExpressPreOrder {
	/*
	 * 主键  (not null)
	 */
	private int id;
	/*
	 * 预订单编号(not null)
	 */
	private String pre_order_no;
	/*
	 *预订单状态（0：正常，1：关闭，2：退回）
	 */
	private int status;
	/*
	 * 执行状态(0未匹配站点、1已匹配站点、2已分配小件员、3延迟揽件、4揽件失败、5站点超区、6揽件超区、7揽件成功)
	 */
	private int excute_state;
	/*
	 *
	 */
	private String send_person;
	/*
	 * 手机号码
	 */
	private String cellphone;
	/*
	 * 固定电话
	 */
	private String telephone;
	/*
	 * 取件地址
	 */
	private String collect_address;
	/*
	 * 原因
	 */
	private String reason;
	/*
	 * 分配站点id
	 */
	private int branch_id;
	/*
	 * 分配站点名称
	 */
	private String branch_name;
	/*
	 * 省公司处理预订单时间
	 */
	private Date handle_time;
	/*
	 * 处理人id
	 */
	private int handle_user_id;
	/*
	 * 处理人姓名
	 */
	private String handle_user_name;
	/*
	 * 分配小件员的时间
	 */
	private Date distribute_deliverman_time;
	/*
	 * 生成时间
	 */
	private Date create_time;
	/*
	 * 预约时间
	 */
	private Date arrange_time;
	/*
	 * 小件员id
	 */
	private int deliverman_id;
	/*
	 * 小件员姓名
	 */
	private String deliverman_name;
	/*
	 * 分配小件员的操作人id
	 */
	private int distribute_user_id;
	/*
	 * 分配小件员的操作人姓名
	 */
	private String distribute_user_name;
	/*
	 * 快递单号（也是订单表中的订单号）
	 */
	private String order_no;
	/*
	 * 一级原因id
	 */
	private int feedback_first_reason_id;
	/*
	 * 一级原因
	 */
	private String feedback_first_reason;
	/*
	 * 二级原因id
	 */
	private int feedback_second_reason_id;
	/*
	 * 二级原因
	 */
	private String feedback_second_reason;
	/*
	 * 反馈备注
	 */
	private String feedback_remark;
	/*
	 * 反馈人id
	 */
	private int feedback_user_id;
	/*
	 * 反馈人姓名
	 */
	private String feedback_user_name;
	/*
	 * 反馈时间
	 */
	private Date feedback_time;

	public ExpressPreOrder() {
		super();
	}

	public ExpressPreOrder(int id, String pre_order_no, int status, int excute_state, String send_person, String cellphone, String telephone, String collect_address, String reason, int branch_id,
			String branch_name, Date handle_time, int handle_user_id, String handle_user_name, Date distribute_deliverman_time, Date create_time, Date arrange_time, int deliverman_id,
			String deliverman_name, int distribute_user_id, String distribute_user_name, String order_no, int feedback_first_reason_id, String feedback_first_reason, int feedback_second_reason_id,
			String feedback_second_reason, String feedback_remark, int feedback_user_id, String feedback_user_name, Date feedback_time) {
		super();
		this.id = id;
		this.pre_order_no = pre_order_no;
		this.status = status;
		this.excute_state = excute_state;
		this.send_person = send_person;
		this.cellphone = cellphone;
		this.telephone = telephone;
		this.collect_address = collect_address;
		this.reason = reason;
		this.branch_id = branch_id;
		this.branch_name = branch_name;
		this.handle_time = handle_time;
		this.handle_user_id = handle_user_id;
		this.handle_user_name = handle_user_name;
		this.distribute_deliverman_time = distribute_deliverman_time;
		this.create_time = create_time;
		this.arrange_time = arrange_time;
		this.deliverman_id = deliverman_id;
		this.deliverman_name = deliverman_name;
		this.distribute_user_id = distribute_user_id;
		this.distribute_user_name = distribute_user_name;
		this.order_no = order_no;
		this.feedback_first_reason_id = feedback_first_reason_id;
		this.feedback_first_reason = feedback_first_reason;
		this.feedback_second_reason_id = feedback_second_reason_id;
		this.feedback_second_reason = feedback_second_reason;
		this.feedback_remark = feedback_remark;
		this.feedback_user_id = feedback_user_id;
		this.feedback_user_name = feedback_user_name;
		this.feedback_time = feedback_time;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPre_order_no() {
		return this.pre_order_no;
	}

	public void setPre_order_no(String pre_order_no) {
		this.pre_order_no = pre_order_no;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getExcute_state() {
		return this.excute_state;
	}

	public void setExcute_state(int excute_state) {
		this.excute_state = excute_state;
	}

	public String getSend_person() {
		return this.send_person;
	}

	public void setSend_person(String send_person) {
		this.send_person = send_person;
	}

	public String getCellphone() {
		return this.cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getCollect_address() {
		return this.collect_address;
	}

	public void setCollect_address(String collect_address) {
		this.collect_address = collect_address;
	}

	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public int getBranch_id() {
		return this.branch_id;
	}

	public void setBranch_id(int branch_id) {
		this.branch_id = branch_id;
	}

	public String getBranch_name() {
		return this.branch_name;
	}

	public void setBranch_name(String branch_name) {
		this.branch_name = branch_name;
	}

	public Date getHandle_time() {
		return this.handle_time;
	}

	public void setHandle_time(Date handle_time) {
		this.handle_time = handle_time;
	}

	public int getHandle_user_id() {
		return this.handle_user_id;
	}

	public void setHandle_user_id(int handle_user_id) {
		this.handle_user_id = handle_user_id;
	}

	public String getHandle_user_name() {
		return this.handle_user_name;
	}

	public void setHandle_user_name(String handle_user_name) {
		this.handle_user_name = handle_user_name;
	}

	public Date getDistribute_deliverman_time() {
		return this.distribute_deliverman_time;
	}

	public void setDistribute_deliverman_time(Date distribute_deliverman_time) {
		this.distribute_deliverman_time = distribute_deliverman_time;
	}

	public Date getCreate_time() {
		return this.create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public Date getArrange_time() {
		return this.arrange_time;
	}

	public void setArrange_time(Date arrange_time) {
		this.arrange_time = arrange_time;
	}

	public int getDeliverman_id() {
		return this.deliverman_id;
	}

	public void setDeliverman_id(int deliverman_id) {
		this.deliverman_id = deliverman_id;
	}

	public String getDeliverman_name() {
		return this.deliverman_name;
	}

	public void setDeliverman_name(String deliverman_name) {
		this.deliverman_name = deliverman_name;
	}

	public int getDistribute_user_id() {
		return this.distribute_user_id;
	}

	public void setDistribute_user_id(int distribute_user_id) {
		this.distribute_user_id = distribute_user_id;
	}

	public String getDistribute_user_name() {
		return this.distribute_user_name;
	}

	public void setDistribute_user_name(String distribute_user_name) {
		this.distribute_user_name = distribute_user_name;
	}

	public String getOrder_no() {
		return this.order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	public int getFeedback_first_reason_id() {
		return this.feedback_first_reason_id;
	}

	public void setFeedback_first_reason_id(int feedback_first_reason_id) {
		this.feedback_first_reason_id = feedback_first_reason_id;
	}

	public String getFeedback_first_reason() {
		return this.feedback_first_reason;
	}

	public void setFeedback_first_reason(String feedback_first_reason) {
		this.feedback_first_reason = feedback_first_reason;
	}

	public int getFeedback_second_reason_id() {
		return this.feedback_second_reason_id;
	}

	public void setFeedback_second_reason_id(int feedback_second_reason_id) {
		this.feedback_second_reason_id = feedback_second_reason_id;
	}

	public String getFeedback_second_reason() {
		return this.feedback_second_reason;
	}

	public void setFeedback_second_reason(String feedback_second_reason) {
		this.feedback_second_reason = feedback_second_reason;
	}

	public String getFeedback_remark() {
		return this.feedback_remark;
	}

	public void setFeedback_remark(String feedback_remark) {
		this.feedback_remark = feedback_remark;
	}

	public int getFeedback_user_id() {
		return this.feedback_user_id;
	}

	public void setFeedback_user_id(int feedback_user_id) {
		this.feedback_user_id = feedback_user_id;
	}

	public String getFeedback_user_name() {
		return this.feedback_user_name;
	}

	public void setFeedback_user_name(String feedback_user_name) {
		this.feedback_user_name = feedback_user_name;
	}

	public Date getFeedback_time() {
		return this.feedback_time;
	}

	public void setFeedback_time(Date feedback_time) {
		this.feedback_time = feedback_time;
	}

}
