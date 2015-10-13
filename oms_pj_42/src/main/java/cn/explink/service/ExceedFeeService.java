package cn.explink.service;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.CwbOrderTypeDAO;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrderTypeBean;
import cn.explink.domain.ExceedFee;
import cn.explink.domain.ImportCwbOrderType;
import cn.explink.util.StringUtil;

@Service
public class ExceedFeeService {
	private Logger logger = LoggerFactory.getLogger(ExceedFeeService.class);

	//

	public ExceedFee loadFormForExceedFeeToEdit(HttpServletRequest request, long exceedid) {
		ExceedFee exceedFee = loadFormForCwbOrderType(request);
		exceedFee.setId(exceedid);
		return exceedFee;
	}

	public ExceedFee loadFormForCwbOrderType(HttpServletRequest request) {
		ExceedFee exceedFee = new ExceedFee();
		exceedFee.setExceedfee(Double.parseDouble(request.getParameter("exceedfee")));
		return exceedFee;
	}

}
