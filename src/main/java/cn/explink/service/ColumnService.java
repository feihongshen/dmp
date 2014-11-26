package cn.explink.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.SetExcelColumnDAO;
import cn.explink.domain.ExcelColumnSet;

@Service
public class ColumnService {

	@Autowired
	SetExcelColumnDAO setExcelColumnDAO;

	public void addColumn(ExcelColumnSet excelColumnSet) {
		setExcelColumnDAO.creColumn(excelColumnSet);
	}

	public void editColumn(ExcelColumnSet excelColumnSer) {
		setExcelColumnDAO.saveColumn(excelColumnSer);
	}

}
