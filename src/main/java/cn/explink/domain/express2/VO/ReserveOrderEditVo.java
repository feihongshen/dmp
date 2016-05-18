package cn.explink.domain.express2.VO;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

/**
 * 预约单修改vo
 * @author neo01.huang
 * 2016-5-17
 */
public class ReserveOrderEditVo implements Serializable {

	private static final long serialVersionUID = -7870539868330607771L;

	/**
	 * 预约单号
	 */
	private String reserveOrderNo;
	/**
	 * 寄件人姓名
	 */
	private String cnorName4eidt;
	/**
	 * 市名称
	 */
	private String city4edit;
	/**
	 * 区名称
	 */
	private String county4edit;
	/**
	 * 寄件人详细地址
	 */
	private String cnorAddr4edit;
	/**
	 * 预约上门时间
	 */
	private String requireTimeStr4edit;
	
	//----------转换后的属性------------
	/**
	 * 预约上门时间
	 */
	private Date requireTime;
	/**
	 * 预约上门时间(Long)
	 */
	private Long requireTimeLong;
	//----------------------
	
	/**
	 * 校验
	 */
	public void validate() {
		reserveOrderNo = StringUtils.trimToEmpty(reserveOrderNo);
		cnorName4eidt = StringUtils.trimToEmpty(cnorName4eidt);
		city4edit = StringUtils.trimToEmpty(city4edit);
		county4edit = StringUtils.trimToEmpty(county4edit);
		cnorAddr4edit = StringUtils.trimToEmpty(cnorAddr4edit);
		requireTimeStr4edit = StringUtils.trimToEmpty(requireTimeStr4edit);
		
		if (reserveOrderNo.length() == 0) {
			throw new IllegalStateException("请输入预约单号");
		}
		if (cnorName4eidt.length() == 0) {
			throw new IllegalStateException("请输入寄件人");
		}
		if (city4edit.length() == 0) {
			throw new IllegalStateException("请输入市");
		}
		if (county4edit.length() == 0) {
			throw new IllegalStateException("请输入区");
		}
		if (cnorAddr4edit.length() == 0) {
			throw new IllegalStateException("请输入地址");
		}
		
		if (requireTimeStr4edit.length() != 0) {
			try {
				requireTime = DateUtils.parseDate(requireTimeStr4edit, "yyyy-MM-dd HH:mm:ss");
			} catch (ParseException e) {
				throw new IllegalStateException("请输入正确格式的预约上门时间");
			}
			requireTimeLong = requireTime.getTime();
		} else {
			requireTime = null;
			requireTimeLong = null;
		}
	}

	public String getReserveOrderNo() {
		return reserveOrderNo;
	}

	public void setReserveOrderNo(String reserveOrderNo) {
		this.reserveOrderNo = reserveOrderNo;
	}

	public String getCnorName4eidt() {
		return cnorName4eidt;
	}

	public void setCnorName4eidt(String cnorName4eidt) {
		this.cnorName4eidt = cnorName4eidt;
	}

	public String getCity4edit() {
		return city4edit;
	}

	public void setCity4edit(String city4edit) {
		this.city4edit = city4edit;
	}

	public String getCounty4edit() {
		return county4edit;
	}

	public void setCounty4edit(String county4edit) {
		this.county4edit = county4edit;
	}

	public String getCnorAddr4edit() {
		return cnorAddr4edit;
	}

	public void setCnorAddr4edit(String cnorAddr4edit) {
		this.cnorAddr4edit = cnorAddr4edit;
	}

	public String getRequireTimeStr4edit() {
		return requireTimeStr4edit;
	}

	public void setRequireTimeStr4edit(String requireTimeStr4edit) {
		this.requireTimeStr4edit = requireTimeStr4edit;
	}

	public Date getRequireTime() {
		return requireTime;
	}

	public void setRequireTime(Date requireTime) {
		this.requireTime = requireTime;
	}

	public Long getRequireTimeLong() {
		return requireTimeLong;
	}

	public void setRequireTimeLong(Long requireTimeLong) {
		this.requireTimeLong = requireTimeLong;
	}
	
}
