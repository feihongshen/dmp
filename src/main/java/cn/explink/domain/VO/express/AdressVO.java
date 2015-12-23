package cn.explink.domain.VO.express;

/**
 *
 * @description 存储省市区县级联地址的VO
 * @author  刘武强
 * @data   2015年8月7日
 */
public class AdressVO {
	/**
	 * id
	 */
	private int id;
	/**
	 * 地址的代码
	 */
	private String code;
	/**
	 * 地址的名字
	 */
	private String name;
	/**
	 * 父地址的代码
	 */
	private String parentCode;


	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParentCode() {
		return this.parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
	public AdressVO() {
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
