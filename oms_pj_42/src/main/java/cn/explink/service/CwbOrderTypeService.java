package cn.explink.service;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.explink.domain.ImportCwbOrderType;
import cn.explink.util.StringUtil;

@Service
public class CwbOrderTypeService {
	private Logger logger = LoggerFactory.getLogger(CwbOrderTypeService.class);

	//

	public ImportCwbOrderType loadFormForCwbOrderTypeToEdit(HttpServletRequest request, long importid) {
		ImportCwbOrderType importCwbOrderType = loadFormForCwbOrderType(request);
		importCwbOrderType.setImportid(importid);
		return importCwbOrderType;
	}

	public ImportCwbOrderType loadFormForCwbOrderType(HttpServletRequest request) {
		ImportCwbOrderType importCwbOrderType = new ImportCwbOrderType();
		importCwbOrderType.setImporttypeid(Integer.parseInt(request.getParameter("importtypeid")));
		importCwbOrderType.setImporttype(StringUtil.nullConvertToEmptyString(request.getParameter("importtype")));
		return importCwbOrderType;
	}

}
