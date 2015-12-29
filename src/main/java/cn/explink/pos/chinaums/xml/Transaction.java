package cn.explink.pos.chinaums.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "transaction")
public class Transaction {
	private Transaction_Header transaction_header;
	private Transaction_Body transaction_body;

	@XmlElement(name = "transaction_header")
	public Transaction_Header getTransaction_Header() {
		return transaction_header;
	}

	public void setTransaction_Header(Transaction_Header transaction_Header) {
		this.transaction_header = transaction_Header;
	}

	@XmlElement(name = "transaction_body")
	public Transaction_Body getTransaction_Body() {
		return transaction_body;
	}

	public void setTransaction_Body(Transaction_Body transaction_Body) {
		this.transaction_body = transaction_Body;
	}
}
