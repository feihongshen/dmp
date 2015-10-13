package cn.explink.b2c.dangdang;

/**
 * DangDang 所有XML的节点
 * 
 * @author Administrator
 *
 */
public class DangDangXMLNote {

	private String cwb;
	private String in_storage_date;
	private String out_storage_date;
	private String express_operator_name;
	private String express_operator_tel;
	private long express_operator_id;
	private String sign_date;
	private String sign_person;
	private String order_status;
	private String reason;

	public String getSign_date() {
		return sign_date;
	}

	public void setSign_date(String sign_date) {
		this.sign_date = sign_date;
	}

	public String getSign_person() {
		return sign_person;
	}

	public void setSign_person(String sign_person) {
		this.sign_person = sign_person;
	}

	public String getOrder_status() {
		return order_status;
	}

	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getOut_storage_date() {
		return out_storage_date;
	}

	public void setOut_storage_date(String out_storage_date) {
		this.out_storage_date = out_storage_date;
	}

	public String getExpress_operator_name() {
		return express_operator_name;
	}

	public void setExpress_operator_name(String express_operator_name) {
		this.express_operator_name = express_operator_name;
	}

	public String getExpress_operator_tel() {
		return express_operator_tel;
	}

	public void setExpress_operator_tel(String express_operator_tel) {
		this.express_operator_tel = express_operator_tel;
	}

	public long getExpress_operator_id() {
		return express_operator_id;
	}

	public void setExpress_operator_id(long express_operator_id) {
		this.express_operator_id = express_operator_id;
	}

	private String operator;

	public String getCwb() {
		return cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public String getIn_storage_date() {
		return in_storage_date;
	}

	public void setIn_storage_date(String in_storage_date) {
		this.in_storage_date = in_storage_date;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

}
