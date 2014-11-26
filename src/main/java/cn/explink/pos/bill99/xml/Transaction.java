package cn.explink.pos.bill99.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Transaction")
public class Transaction {
	private Transaction_Header transaction_Header;
	private Transaction_Body transaction_Body;

	@XmlElement(name = "Transaction_Header")
	public Transaction_Header getTransaction_Header() {
		return transaction_Header;
	}

	public void setTransaction_Header(Transaction_Header transaction_Header) {
		this.transaction_Header = transaction_Header;
	}

	@XmlElement(name = "Transaction_Body")
	public Transaction_Body getTransaction_Body() {
		return transaction_Body;
	}

	public void setTransaction_Body(Transaction_Body transaction_Body) {
		this.transaction_Body = transaction_Body;
	}
}
