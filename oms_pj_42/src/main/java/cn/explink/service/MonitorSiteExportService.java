package cn.explink.service;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import cn.explink.dao.ExportmouldDAO;
import cn.explink.dao.GetDmpDAO;
import cn.explink.dao.MonitorHouseDAO;

@Service
public class MonitorSiteExportService {

	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	MonitorHouseDAO monitorDAO;
	@Autowired
	GetDmpDAO getDmpDAO;
	@Autowired
	MonitorSiteService monitorService;
	@Autowired
	ExportService exportService;
	@Autowired
	ExportmouldDAO exportmouldDAO;

	public void MonitorSitExport(String crateStartdate, String crateEnddate, String customerid, long branchid, String flowType, String startinSitetime, String endinSitetime, String exportmould2,
			HttpServletResponse response) {

	}

}
