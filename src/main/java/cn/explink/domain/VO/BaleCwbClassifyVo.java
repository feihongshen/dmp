package cn.explink.domain.VO;

import java.util.ArrayList;
import java.util.List;

public class BaleCwbClassifyVo {
	
	/**
	 * 包号
	 */
	private String baleno;
	
	/**
	 * 运单号
	 */
	private List<String> transcwbList = new ArrayList<String>();

	public String getBaleno() {
		return baleno;
	}

	public void setBaleno(String baleno) {
		this.baleno = baleno;
	}

	public List<String> getTranscwbList() {
		return transcwbList;
	}

	public void setTranscwbList(List<String> transcwbList) {
		this.transcwbList = transcwbList;
	}

	@Override
	public String toString() {
		return "BaleCwbClassifyVo [baleno=" + baleno + ", transcwbList=" + transcwbList + "]";
	}
}
