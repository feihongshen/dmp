/**
 *
 */
package cn.explink.domain;

/**
 * @author Administrator
 *
 */
public class PenalizeOutImportErrorRecord {
	private long id;
	private long importFlag;
	private String cwb;
	private String error;
	/**
	 * @return the id
	 */
	public long getId() {
		return this.id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * @return the importFlag
	 */
	public long getImportFlag() {
		return this.importFlag;
	}
	/**
	 * @param importFlag the importFlag to set
	 */
	public void setImportFlag(long importFlag) {
		this.importFlag = importFlag;
	}
	/**
	 * @return the cwb
	 */
	public String getCwb() {
		return this.cwb;
	}
	/**
	 * @param cwb the cwb to set
	 */
	public void setCwb(String cwb) {
		this.cwb = cwb;
	}
	/**
	 * @return the error
	 */
	public String getError() {
		return this.error;
	}
	/**
	 * @param error the error to set
	 */
	public void setError(String error) {
		this.error = error;
	}

}
