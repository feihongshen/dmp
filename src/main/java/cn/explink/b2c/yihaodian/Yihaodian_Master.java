package cn.explink.b2c.yihaodian;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Yihaodian_Master {

	@Autowired
	private Yihaodian_DownloadCwb yihaodian_DownloadCwb;
	@Autowired
	private Yihaodian_ExportCallBack yihaodian_ExportCallBack;

	public Yihaodian_DownloadCwb getYihaodian_DownloadCwb() {
		return yihaodian_DownloadCwb;
	}

	public void setYihaodian_DownloadCwb(Yihaodian_DownloadCwb yihaodian_DownloadCwb) {
		this.yihaodian_DownloadCwb = yihaodian_DownloadCwb;
	}

	public Yihaodian_ExportCallBack getYihaodian_ExportCallBack() {
		return yihaodian_ExportCallBack;
	}

	public void setYihaodian_ExportCallBack(Yihaodian_ExportCallBack yihaodian_ExportCallBack) {
		this.yihaodian_ExportCallBack = yihaodian_ExportCallBack;
	}
}
