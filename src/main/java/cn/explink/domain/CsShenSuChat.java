package cn.explink.domain;

public class CsShenSuChat {
	
	private String creTime;
	private long creUser;
	private String creChatContent;
	private String acceptNo;
	
	public String getAcceptNo() {
		return acceptNo;
	}
	public void setAcceptNo(String acceptNo) {
		this.acceptNo = acceptNo;
	}
	public String getCreTime() {
		return creTime;
	}
	public void setCreTime(String creTime) {
		this.creTime = creTime;
	}
	
	public long getCreUser() {
		return creUser;
	}
	public void setCreUser(long creUser) {
		this.creUser = creUser;
	}
	public String getCreChatContent() {
		return creChatContent;
	}
	public void setCreChatContent(String creChatContent) {
		this.creChatContent = creChatContent;
	}
	
	

}
