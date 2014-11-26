package cn.explink.b2c.yonghuics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Yonghui_Master {

	@Autowired
	private YonghuiService_download yonghuiService_download;
	@Autowired
	private YonghuiService_callBack yonghuiService_callBack;

	public YonghuiService_download getYonghuiService_download() {
		return yonghuiService_download;
	}

	public void setYonghuiService_download(YonghuiService_download yonghuiService_download) {
		this.yonghuiService_download = yonghuiService_download;
	}

	public YonghuiService_callBack getYonghuiService_callBack() {
		return yonghuiService_callBack;
	}

	public void setYonghuiService_callBack(YonghuiService_callBack yonghuiService_callBack) {
		this.yonghuiService_callBack = yonghuiService_callBack;
	}

}
