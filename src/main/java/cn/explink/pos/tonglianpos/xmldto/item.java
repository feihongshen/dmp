package cn.explink.pos.tonglianpos.xmldto;

import javax.xml.bind.annotation.XmlElement;

public class item {
	private merchant_code merchant_code;

	@XmlElement(name = "merchant_code")
	public merchant_code getMerchant_code() {
		return merchant_code;
	}

	public void setMerchant_code(merchant_code merchant_code) {
		this.merchant_code = merchant_code;
	}

}
