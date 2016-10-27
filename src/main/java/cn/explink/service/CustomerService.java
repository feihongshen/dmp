package cn.explink.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import cn.explink.b2c.weisuda.CourierService;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.domain.Customer;
import cn.explink.domain.SystemInstall;
import cn.explink.util.JSONReslutUtil;
import cn.explink.util.ResourceBundleUtil;
import cn.explink.util.ServiceUtil;
import cn.explink.util.StringUtil;

@Service
public class CustomerService {

	@Autowired
	CourierService courierService;
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	SystemInstallDAO systemInstallDAO;
	@Autowired
	CustomerDAO customerdao;

	public Customer loadFormForCustomer(HttpServletRequest request, MultipartFile file, long customerid) {
		Customer customer = this.loadFormForCustomer(request, file);
		customer.setCustomerid(customerid);
		return customer;
	}




	public Customer loadFormForCustomer(HttpServletRequest request, MultipartFile file) {
		Customer customer = new Customer();
		customer.setCustomername(StringUtil.nullConvertToEmptyString(request.getParameter("customername")));
		customer.setCompanyname(StringUtil.nullConvertToEmptyString(request.getParameter("companyname")));
		customer.setCustomercode(StringUtil.nullConvertToEmptyString(request.getParameter("customercode")));
		customer.setCustomeraddress(StringUtil.nullConvertToEmptyString(request.getParameter("customeraddress")));
		customer.setCustomercontactman(StringUtil.nullConvertToEmptyString(request.getParameter("customercontactman")));
		customer.setCustomerphone(StringUtil.nullConvertToEmptyString(request.getParameter("customerphone")));
		customer.setPaytype(Long.parseLong(request.getParameter("paytype")));
		customer.setIsypdjusetranscwb(Long.parseLong(request.getParameter("isypdjusetranscwb")));
		customer.setIsUsetranscwb(Long.parseLong(request.getParameter("isUsetranscwb")));
		customer.setIsAutoProductcwb(Long.parseLong(request.getParameter("isAutoProductcwb")));
		customer.setAutoProductcwbpre(StringUtil.nullConvertToEmptyString(request.getParameter("autoProductcwbpre")));
		customer.setIsFeedbackcwb(Integer.parseInt(request.getParameter("isFeedbackcwb")));
		customer.setNeedchecked(Integer.parseInt(request.getParameter("needchecked")));
		customer.setSmschannel(Integer.parseInt(request.getParameter("smschannel")));
		customer.setIsqufendaxiaoxie(Long.parseLong(request.getParameter("isqufendaxiaoxie")));
		customer.setPfruleid(Long.parseLong(request.getParameter("pfruleid")));
		customer.setWavFilePath(this.saveFile(request, file));
		customer.setAutoArrivalBranchFlag(Integer.parseInt(request.getParameter("autoArrivalBranchFlag")));
		
		String isJiDan = request.getParameter("ifjidan");
		int mpsswitch = Integer.parseInt(request.getParameter("mpsswitch") == null ? "0" : request.getParameter("mpsswitch"));
		if (isJiDan == null || "0".equals(isJiDan)) { //是否集单：否
			customer.setMpsswitch(0);
		} else if ("1".equals(isJiDan)) {//是否集单：是
			customer.setMpsswitch(mpsswitch);
		}
		return customer;
	}

	private String saveFile(HttpServletRequest request, MultipartFile file) {
		if ((file != null) && !file.isEmpty()) {
			String filePath = ResourceBundleUtil.WAVPATH;
			String name = file.getOriginalFilename();
			ServiceUtil.uploadWavFile(file, filePath, name);
			return name;
		} else {
			return StringUtil.nullConvertToEmptyString(request.getParameter("wavText"));
		}
	}

	public void initCustomerList() {
		try {
			//SystemInstall omsPathUrl = this.systemInstallDAO.getSystemInstallByName("omsPathUrl");
			SystemInstall omsUrl = this.systemInstallDAO.getSystemInstallByName("omsUrl");
			String url1 = "";
			if (omsUrl != null) {
				url1 = omsUrl.getValue();
//				url1 = omsUrl.getValue();
			} else {
				url1 = "http://127.0.0.1:8080/oms/";
			}
			final String url = url1;

			String success = JSONReslutUtil.getResultMessage(url + "/handfeedback/init_customerlist", "flag=1", "POST").toString();

			this.logger.info("供货商设置修改同步oms成功，success={},url={}", success, url + "/handfeedback/init_customerlist");

			this.courierService.customerUpdate();
		} catch (Exception e) {
			this.logger.error("dmp供货商修改调用oms重置异常", e);
		}

	}

	public List<Customer> getPageCash() {

		return this.customerdao.getAllCustomers();
	}

}
