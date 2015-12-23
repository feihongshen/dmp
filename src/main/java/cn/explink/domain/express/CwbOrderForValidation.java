/**
 *
 */
package cn.explink.domain.express;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.NotEmpty;

import cn.explink.domain.CwbOrder;
import cn.explink.util.exception.ValidationExeception;

/**
 * @author songkaojun 2015年8月21日
 */
public class CwbOrderForValidation extends CwbOrder {

	private String deliverName;

	/**
	 * 运单号
	 */
	@NotEmpty
	@Override
	public String getTranscwb() {
		return super.getTranscwb();
	}

	/**
	 * 件数
	 */
	@Override
	public long getSendcarnum() {
		return super.getSendcarnum();
	}

	/**
	 * 揽件员
	 */
	@Override
	public String getCollectorname() {
		return super.getCollectorname();
	}

	/**
	 * 派件员
	 *
	 * @return
	 */
	public String getDeliverName() {
		return this.deliverName;
	}

	public void setDeliverName(String deliverName) {
		this.deliverName = deliverName;
	}

	/**
	 * 代收货款
	 */
	@Override
	public BigDecimal getReceivablefee() {
		return super.getReceivablefee();
	}

	public void setValues(String[] values) throws Exception {
		this.assertEquals(values, 5);

		this.setTranscwb(values[0]);
		this.setSendcarnum(values[1].equals("") ? 0 : Long.parseLong(values[1]));
		this.setCollectorname(values[2]);
		this.setDeliverName(values[3]);
		this.setReceivablefee(values[4].equals("") ? BigDecimal.ZERO : new BigDecimal(values[4]));
	}

	private void assertEquals(String[] values, int size) throws ValidationExeception {
		if (values.length != size) {
			throw new ValidationExeception("配置文件提供" + values.length + "个字段" + ",而导入数据库需要提供" + size + "个字段");
		}
	}

}
