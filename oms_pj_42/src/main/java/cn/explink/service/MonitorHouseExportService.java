package cn.explink.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.rowset.ResultSetWrappingSqlRowSet;
import org.springframework.stereotype.Service;

import cn.explink.dao.ExportmouldDAO;
import cn.explink.dao.GetDmpDAO;
import cn.explink.dao.MonitorHouseDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.SetExportField;
import cn.explink.enumutil.BranchEnum;
import cn.explink.util.ExcelUtils;

@Service
public class MonitorHouseExportService {

	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	MonitorHouseDAO monitorDAO;
	@Autowired
	GetDmpDAO getDmpDAO;
	@Autowired
	MonitorHouseService monitorService;
	@Autowired
	ExportService exportService;
	@Autowired
	ExportmouldDAO exportmouldDAO;

	public void MonitorHouseExport(String valueSql, String exportmould2, HttpServletResponse response) {
	}

}
