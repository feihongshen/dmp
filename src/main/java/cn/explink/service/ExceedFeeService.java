package cn.explink.service;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import cn.explink.domain.ExceedFee;

@Service
public class ExceedFeeService {

	public ExceedFee loadFormForExceedFeeToEdit(HttpServletRequest request, long exceedid) {
		ExceedFee exceedFee = loadFormForCwbOrderType(request);
		exceedFee.setId(exceedid);
		return exceedFee;
	}

	public ExceedFee loadFormForCwbOrderType(HttpServletRequest request) {
		ExceedFee exceedFee = new ExceedFee();
		exceedFee.setExceedfee(BigDecimal.valueOf(Double.parseDouble(request.getParameter("exceedfee"))));
		return exceedFee;
	}

}
