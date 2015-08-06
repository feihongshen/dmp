package cn.explink.domain;

public class PunishInsideOperationinfo {
	/**
	 * 主键
	 */
	private long id;
	/**
	 * express_ops_punishinside_detail表中的主键，两个表进行关联
	 */
	private long detailid;
	/**
	 * 操作人
	 */
	private long operationuserid;
	/**
	 * 操作人名字
	 */
	private String operationusername;
	/**
	 * 操作类型
	 */
	private long operationtype;
	/**
	 * 操作类型的名字
	 */
	private String operationtypename;
	/**
	 * 操作说明
	 */
	private String operationdescribe;
	/**
	 * 申诉的类型
	 */
	private long shensutype;
	/**
	 * 申诉时间
	 */
	private String shensudate;
	/**
	 * 获得申诉类型是减少扣罚还是撤销扣罚这两种1，撤销扣罚，2，减少扣罚
	 */
	private String shensutypeName;
	
	public String getShensutypeName() {
		return shensutypeName;
	}
	public void setShensutypeName(String shensutypeName) {
		this.shensutypeName = shensutypeName;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getDetailid() {
		return detailid;
	}
	public void setDetailid(long detailid) {
		this.detailid = detailid;
	}
	public long getOperationuserid() {
		return operationuserid;
	}
	public void setOperationuserid(long operationuserid) {
		this.operationuserid = operationuserid;
	}
	public String getOperationusername() {
		return operationusername;
	}
	public void setOperationusername(String operationusername) {
		this.operationusername = operationusername;
	}
	public String getOperationdescribe() {
		return operationdescribe;
	}
	public void setOperationdescribe(String operationdescribe) {
		this.operationdescribe = operationdescribe;
	}
	public long getOperationtype() {
		return operationtype;
	}
	public void setOperationtype(long operationtype) {
		this.operationtype = operationtype;
	}
	public long getShensutype() {
		return shensutype;
	}
	public void setShensutype(long shensutype) {
		this.shensutype = shensutype;
	}
	public String getOperationtypename() {
		return operationtypename;
	}
	public void setOperationtypename(String operationtypename) {
		this.operationtypename = operationtypename;
	}
	public String getShensudate() {
		return shensudate;
	}
	public void setShensudate(String shensudate) {
		this.shensudate = shensudate;
	}
	
	
}

