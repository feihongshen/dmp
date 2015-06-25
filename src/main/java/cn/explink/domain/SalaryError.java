/**
 *
 */
package cn.explink.domain;

/**
 * @author Administrator
 *
 */
public class SalaryError {
	private long id;
	private String realname;
	private String idcard;
	private String text;
	private long importflag;

	/**
	 * @return the id
	 */
	public long getId() {
		return this.id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the realname
	 */
	public String getRealname() {
		return this.realname;
	}

	/**
	 * @param realname
	 *            the realname to set
	 */
	public void setRealname(String realname) {
		this.realname = realname;
	}

	/**
	 * @return the idcard
	 */
	public String getIdcard() {
		return this.idcard;
	}

	/**
	 * @param idcard
	 *            the idcard to set
	 */
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return this.text;
	}

	/**
	 * @param text
	 *            the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the improtflag
	 */
	public long getImportflag() {
		return this.importflag;
	}

	/**
	 * @param improtflag
	 *            the improtflag to set
	 */
	public void setImportflag(long importflag) {
		this.importflag = importflag;
	}

}
