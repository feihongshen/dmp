package cn.explink.util;

import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailUtil {

	private static Logger logger = LoggerFactory.getLogger(EmailUtil.class);
	
	private static final String SMTP_HOST_NAME = "smtp.exmail.qq.com";
	private static final String SMTP_PORT = "25";
	// TODO 配置发件邮箱
	private static final String emailFromAddress1 = "service1@ruyicai.com";
	private static final String emailFromPassword1 = "ruyicai1234";

	// private static final String emailFromAddress2 = "service2@ruyicai.com";
	// private static final String emailFromPassword2 = "ruyicai1234";

	/**
	 * 发送邮件接口 testEmail.utilSend(recipients,subject,message)
	 * 
	 * @param recipients
	 *            收件人
	 * @param subject
	 *            标题
	 * @param message
	 *            内容
	 * @throws MessagingException
	 *             发送失败抛出异常
	 */
	public static void sendMail(String recipients[], String subject, String message) throws MessagingException {
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		Properties props = new Properties();
		props.put("mail.smtp.host", SMTP_HOST_NAME);
		props.put("mail.smtp.auth", "true");
		// props.put("mail.debug", "false");
		props.put("mail.smtp.port", SMTP_PORT);
		props.put("mail.smtp.socketFactory.port", SMTP_PORT);
		// props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
		// props.put("mail.smtp.socketFactory.fallback", "false");

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {

			protected PasswordAuthentication getPasswordAuthentication() {
				String from = emailFromAddress1;
				String password = emailFromPassword1;
				return new PasswordAuthentication(from, password);
			}
		});

		Message msg = new MimeMessage(session);
		String from = emailFromAddress1;
		InternetAddress addressFrom = new InternetAddress(from);
		try {
			addressFrom.setPersonal("易普联科");
		} catch (UnsupportedEncodingException e) {
			logger.error("", e);
		}
		msg.setFrom(addressFrom);

		InternetAddress[] addressTo = new InternetAddress[recipients.length];
		for (int i = 0; i < recipients.length; i++) {
			addressTo[i] = new InternetAddress(recipients[i]);
		}
		msg.setRecipients(Message.RecipientType.TO, addressTo);

		// Setting the Subject and Content Type
		msg.setSubject(subject);
		msg.setContent(message, "text/html;charset=utf-8");
		Transport.send(msg);
	}
}
