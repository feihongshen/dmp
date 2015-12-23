package cn.explink.domain.VO.express;

public class AdressInfoDetailVO {
	/*
	 * 省id
	 */
	private Long provinceId;
	/*
	 * 省名称
	 */
	private String provinceName;
	/*
	 * 市id
	 */
	private Long cityId;
	/*
	 * 市名称
	 */
	private String cityName;
	/*
	 * 区/县id
	 */
	private Long countyId;
	/*
	 * 区/县名称
	 */
	private String countyName;
	/*
	 * 街道id
	 */
	private Long townId;
	/*
	 * 街道名称
	 */
	private String townName;

	public Long getProvinceId() {
		return this.provinceId;
	}

	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}

	public String getProvinceName() {
		return this.provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public Long getCityId() {
		return this.cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return this.cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public Long getCountyId() {
		return this.countyId;
	}

	public void setCountyId(Long countyId) {
		this.countyId = countyId;
	}

	public String getCountyName() {
		return this.countyName;
	}

	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}

	public Long getTownId() {
		return this.townId;
	}

	public void setTownId(Long townId) {
		this.townId = townId;
	}

	public String getTownName() {
		return this.townName;
	}

	public void setTownName(String townName) {
		this.townName = townName;
	}

}
