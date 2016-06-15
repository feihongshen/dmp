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
	private String cnorName4edit;
	/**
	 * 市编码
	 */
	private String city4edit;
	/**
	 * 市名称
	 */
	private String cityName4edit;
	/**
	 * 区编码
	 */
	private String county4edit;
	/**
	 * 区名称
	 */
	private String countyName4edit;
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
	 * 市编码
	 */
	private Integer city4editInt;
	/**
	 * 区编码
	 */
	private Integer county4editInt;
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
     * 乐观锁
     */
    private Long recordVersion;

	/**
	 * 校验
	 */
	public void validate() {
		reserveOrderNo = StringUtils.trimToEmpty(reserveOrderNo);
		cnorName4edit = StringUtils.trimToEmpty(cnorName4edit);
		city4edit = StringUtils.trimToEmpty(city4edit);
		county4edit = StringUtils.trimToEmpty(county4edit);
		cnorAddr4edit = StringUtils.trimToEmpty(cnorAddr4edit);
		requireTimeStr4edit = StringUtils.trimToEmpty(requireTimeStr4edit);
		
		if (reserveOrderNo.length() == 0) {
			throw new IllegalStateException("请输入预约单号");
		}
		if (cnorName4edit.length() == 0) {
			throw new IllegalStateException("请输入寄件人");
		}
		if (cnorName4edit.length() > 64) {
			throw new IllegalStateException("寄件人最大长度为64");
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
		if (cnorAddr4edit.length() > 256) {
			throw new IllegalStateException("地址最大长度为256");
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
		
		try {
			city4editInt = Integer.parseInt(city4edit);
		} catch (NumberFormatException e) {
			throw new IllegalStateException("市的编码不是数字");
		}
		
		try {
			county4editInt = Integer.parseInt(county4edit);
		} catch (NumberFormatException e) {
			throw new IllegalStateException("区的编码不是数字");
		}
		
	}

	public String getReserveOrderNo() {
		return reserveOrderNo;
	}

	public void setReserveOrderNo(String reserveOrderNo) {
		this.reserveOrderNo = reserveOrderNo;
	}

	public String getCnorName4edit() {
		return cnorName4edit;
	}

	public void setCnorName4edit(String cnorName4edit) {
		this.cnorName4edit = cnorName4edit;
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

	public String getCityName4edit() {
		return cityName4edit;
	}

	public void setCityName4edit(String cityName4edit) {
		this.cityName4edit = cityName4edit;
	}

	public String getCountyName4edit() {
		return countyName4edit;
	}

	public void setCountyName4edit(String countyName4edit) {
		this.countyName4edit = countyName4edit;
	}

	public Integer getCity4editInt() {
		return city4editInt;
	}

	public void setCity4editInt(Integer city4editInt) {
		this.city4editInt = city4editInt;
	}

	public Integer getCounty4editInt() {
		return county4editInt;
	}

	public void setCounty4editInt(Integer county4editInt) {
		this.county4editInt = county4editInt;
	}


    public Long getRecordVersion() {
        return recordVersion;
    }

    public void setRecordVersion(Long recordVersion) {
        this.recordVersion = recordVersion;
    }
}
