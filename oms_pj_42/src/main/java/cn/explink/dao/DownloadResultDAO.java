package cn.explink.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.explink.dao.support.BasicJdbcTemplateDaoSupport;
import cn.explink.domain.DownloadResult;

@Repository("downloadResultDAO")
public class DownloadResultDAO extends BasicJdbcTemplateDaoSupport<DownloadResult, Long> {

	public DownloadResultDAO() {
		super(DownloadResult.class);
	}

	public DownloadResult getDownloadResultByDownloadRequestId(Integer downloadRequestId) {
		String sql = "select * from set_download_results where download_id = ?";
		List<DownloadResult> list = getJdbcTemplate().query(sql, createRowMapper(), downloadRequestId);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

}
