package cn.explink.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "set_download_results")
public class DownloadResult {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "DOWNLOAD_ID", nullable = false)
	private Long downloadId;

	@Column(name = "RESULT")
	private String result;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDownloadId() {
		return downloadId;
	}

	public void setDownloadId(Long downloadId) {
		this.downloadId = downloadId;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}