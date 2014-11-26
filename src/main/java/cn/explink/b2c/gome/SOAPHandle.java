package cn.explink.b2c.gome;

import java.util.Hashtable;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;

import org.apache.axis.AxisFault;
import org.apache.axis.Handler;
import org.apache.axis.MessageContext;
import org.apache.axis.message.MessageElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SOAPHandle implements Handler {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private String userName;
	private String passWord;

	public SOAPHandle(String userName, String passWord) {
		this.userName = userName;
		this.passWord = passWord;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public boolean canHandleBlock(QName qname) {
		// TODO Auto-generated method stub
		return false;
	}

	public void cleanup() {
		// TODO Auto-generated method stub

	}

	public void generateWSDL(MessageContext messagecontext) throws AxisFault {
		// TODO Auto-generated method stub

	}

	public Element getDeploymentData(Document document) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getOption(String s) {
		// TODO Auto-generated method stub
		return null;
	}

	public Hashtable getOptions() {
		// TODO Auto-generated method stub
		return null;
	}

	public List getUnderstoodHeaders() {
		// TODO Auto-generated method stub
		return null;
	}

	public void init() {
		// TODO Auto-generated method stub

	}

	public void onFault(MessageContext messagecontext) {
		// TODO Auto-generated method stub

	}

	public void setName(String s) {
		// TODO Auto-generated method stub

	}

	public void setOption(String s, Object obj) {
		// TODO Auto-generated method stub

	}

	public void setOptions(Hashtable hashtable) {
		// TODO Auto-generated method stub

	}

	@Override
	public void invoke(MessageContext messagecontext) throws AxisFault {
		// TODO Auto-generated method stub
		SOAPEnvelope envelope;
		try {

			String GUOMEIUSERNAME_WEB = this.getUserName(); // 用户帐号
			String GUOMEIPASSWORD_WEB = this.getPassWord(); // 用户密码
			logger.info("请求国美用户名：{}", GUOMEIUSERNAME_WEB);
			logger.info("请求国美密码：{}", GUOMEIPASSWORD_WEB);
			envelope = messagecontext.getMessage().getSOAPPart().getEnvelope();
			SOAPHeader header = envelope.getHeader();
			SOAPElement security = header.addChildElement("Security", "wsse", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
			MessageElement usernameToken = (MessageElement) security.addChildElement("UsernameToken", "wsse");
			usernameToken.setAttribute("xmlns:wsu", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");
			SOAPElement username = usernameToken.addChildElement("Username", "wsse");
			username.addTextNode(GUOMEIUSERNAME_WEB); // 用户帐号
			SOAPElement password = usernameToken.addChildElement("Password", "wsse");
			password.setAttribute("Type", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText");
			password.addTextNode(GUOMEIPASSWORD_WEB); // 用户密码
			SOAPMessage message = messagecontext.getMessage();
		} catch (SOAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
