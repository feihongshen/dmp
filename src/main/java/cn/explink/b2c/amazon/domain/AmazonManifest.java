package cn.explink.b2c.amazon.domain;

import javax.xml.bind.annotation.XmlElement;

import cn.explink.b2c.amazon.domain.header.ManifestHeader;
import cn.explink.b2c.amazon.domain.manifestDetail.ManifestDetail;
import cn.explink.b2c.amazon.domain.manifestSummary.ManifestSummary;

public class AmazonManifest {
	private ManifestHeader manifestHeader;// 发货单头信息
	private ManifestDetail manifestDetail;// 发货单明细
	private ManifestSummary manifestSummary;// 发货单汇总

	@XmlElement(name = "manifestHeader")
	public ManifestHeader getManifestHeader() {
		return manifestHeader;
	}

	public void setManifestHeader(ManifestHeader manifestHeader) {
		this.manifestHeader = manifestHeader;
	}

	@XmlElement(name = "manifestDetail")
	public ManifestDetail getManifestDetail() {
		return manifestDetail;
	}

	public void setManifestDetail(ManifestDetail manifestDetail) {
		this.manifestDetail = manifestDetail;
	}

	@XmlElement(name = "manifestSummary")
	public ManifestSummary getManifestSummary() {
		return manifestSummary;
	}

	public void setManifestSummary(ManifestSummary manifestSummary) {
		this.manifestSummary = manifestSummary;
	}

}
