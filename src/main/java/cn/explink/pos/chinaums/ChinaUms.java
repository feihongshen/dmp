package cn.explink.pos.chinaums;

public class ChinaUms {

	private String private_key;
	private String request_url; // 请求URL
	private int isotherdeliveroper; // 他人刷卡限制 0 关闭 (可刷) 1 开启（不可刷）
	private String mer_id; // 商户号
	private int isAutoSupplementaryProcess; // 是否自动补充流程 ,默认 0关闭， 1 开启
											// (到货和领货都可以自动补充)

	public int getIsAutoSupplementaryProcess() {
		return this.isAutoSupplementaryProcess;
	}

	public void setIsAutoSupplementaryProcess(int isAutoSupplementaryProcess) {
		this.isAutoSupplementaryProcess = isAutoSupplementaryProcess;
	}

	private String forward_url; // 转发URL,用于新老系统公用
	private int isForward; // 是否允许转发

	private int version; // 版本 0 易普默认版本 1 陕西城联个性化版本 注:目前暂时使用这俩个版本.

	public String getPrivate_key() {
		return this.private_key;
	}

	public int getVersion() {
		return this.version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public void setPrivate_key(String private_key) {
		this.private_key = private_key;
	}

	public String getRequest_url() {
		return this.request_url;
	}

	public void setRequest_url(String request_url) {
		this.request_url = request_url;
	}

	public int getIsotherdeliveroper() {
		return this.isotherdeliveroper;
	}

	public void setIsotherdeliveroper(int isotherdeliveroper) {
		this.isotherdeliveroper = isotherdeliveroper;
	}

	public String getMer_id() {
		return this.mer_id;
	}

	public void setMer_id(String mer_id) {
		this.mer_id = mer_id;
	}

	public String getForward_url() {
		return this.forward_url;
	}

	public void setForward_url(String forward_url) {
		this.forward_url = forward_url;
	}

	public int getIsForward() {
		return this.isForward;
	}

	public void setIsForward(int isForward) {
		this.isForward = isForward;
	}

}
