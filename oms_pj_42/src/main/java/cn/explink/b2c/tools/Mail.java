package cn.explink.b2c.tools;

import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.AuthenticationFailedException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Mail {

	private static final String smtpserver = "smtp.qq.com";
	private static final String sendemail = "1014790432@qq.com";
	private static final String sendusername = "352170177";
	private static final String sendpassword = "1103400035";
	private static final String senddisplayname = "Explink-OMS Send JMS Exception Monitoring Center ";

	// 定义发件人、收件人、SMTP服务器、用户名、密码、主题、内容等
	private String displayName;
	private String to;
	private String from;
	private String smtpServer;
	private String username;
	private String password;
	private String subject;
	private String content;
	private boolean ifAuth; // 服务器是否要身份认证
	private String filename = "";
	private Vector file = new Vector(); // 用于保存发送附件的文件名的集合
	private String filewithpath = ""; // 判断是否含有附件

	public void setSmtpServer(String smtpServer) {
		this.smtpServer = smtpServer;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public void setIfAuth(boolean ifAuth) {
		this.ifAuth = ifAuth;
	}

	public void setUserName(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void addAttachfile(String fname) {
		file.addElement(fname);
	}

	// 发送使用sina的邮箱 需要email身份验证 ifAuth=true
	public Mail(String to, String subject, String content, String filewithpath) throws Exception {
		this.addAttachfile(filewithpath); // 附件文件
		this.smtpServer = smtpserver;// smtp服务器地址
		this.from = sendemail;// 发送邮件
		this.displayName = new String((senddisplayname).getBytes("GBK"), "iso8859-1");// 发件人显示
		this.ifAuth = true;
		this.username = sendusername;// 发送用户名
		this.password = sendpassword;// 密码
		this.to = to;
		this.subject = subject;
		this.content = content;
		this.filewithpath = filewithpath;
	}

	public Map<String, Object> send() {
		if (filewithpath == null) { // 20111103 修改，判断是否带附件
			file = new Vector();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("state", "success");
		String message = "邮件发送成功";
		Session session = null;
		Properties props = System.getProperties();
		props.put("mail.smtp.host", smtpServer);
		if (ifAuth) { // 服务器需要身份认证
			props.put("mail.smtp.auth", "true");
			session = Session.getDefaultInstance(props);
		} else {
			props.put("mail.smtp.auth", "false");
			session = Session.getDefaultInstance(props, null);
		}
		session.setDebug(false);
		Transport trans = null;
		try {
			Message msg = new MimeMessage(session);
			try {
				Address from_address = new InternetAddress(from, displayName);
				msg.setFrom(from_address);
			} catch (java.io.UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			InternetAddress[] address = { new InternetAddress(to), new InternetAddress("lans@explink.cn"), };
			msg.setRecipients(Message.RecipientType.TO, address);
			msg.setSubject(subject);
			Multipart mp = new MimeMultipart();
			MimeBodyPart mbp = new MimeBodyPart();
			mbp.setContent(content.toString(), "text/html;charset=gb2312");
			mp.addBodyPart(mbp);
			ExtraAdjunctFile(mp);
			msg.setContent(mp); // Multipart加入到信件
			msg.setSentDate(new Date()); // 设置信件头的发送日期
			// 发送信件
			msg.saveChanges();
			trans = session.getTransport("smtp");
			trans.connect(smtpServer, username, password);
			trans.sendMessage(msg, msg.getAllRecipients());
			trans.close();
		} catch (AuthenticationFailedException e) {
			map.put("state", "failed");
			message = "邮件发送失败！错误原因：\n" + "身份验证错误!";
			e.printStackTrace();
		} catch (MessagingException e) {
			message = "邮件发送失败！错误原因：\n" + e.getMessage();
			map.put("state", "failed");
			e.printStackTrace();
			Exception ex = null;
			if ((ex = e.getNextException()) != null) {
				ex.printStackTrace();
			}
		}
		map.put("message", message);
		return map;
	}

	private void ExtraAdjunctFile(Multipart mp) {
		MimeBodyPart mbp;
		if (!file.isEmpty()) { // 有附件
			Enumeration efile = file.elements();
			while (efile.hasMoreElements()) {
				mbp = new MimeBodyPart();
				filename = efile.nextElement().toString(); // 选择出每一个附件名
				FileDataSource fds = new FileDataSource(filename); // 得到数据源
				try {
					mbp.setDataHandler(new DataHandler(fds));
					mbp.setFileName(new String(fds.getName().getBytes("GBK"), "iso8859-1")); // 得到文件名同样至入BodyPart
					mp.addBodyPart(mbp);
				} catch (Exception e) {
					e.printStackTrace();
				} // 得到附件本身并至入BodyPart
			}
			file.removeAllElements();
		}
	}

	/**
	 * 监听JMS对接出现的异常信息
	 * 
	 * @param exptreason
	 * @return
	 */
	public static Mail LoadingMessageParams(String exptContent) {
		Mail mm = null;
		String content = "您好，OMS处理对接异常，" + exptContent;
		try {
			mm = new Mail("zhangpk@explink.cn", "接收JMS存储异常", content, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mm;
	}

	/**
	 * 监听JMS对接出现的异常信息
	 * 
	 * @param exptreason
	 * @return
	 */
	public static void LoadingAndSendMessage(String exptContent) {
		Mail mm = null;
		String content = "您好，OMS处理对接异常，" + exptContent;
		try {
			mm = new Mail("zhangpk@explink.cn", "接收JMS存储异常", content, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		mm.send();
	}
}