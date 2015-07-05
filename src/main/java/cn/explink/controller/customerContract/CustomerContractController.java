/**
 *
 */
package cn.explink.controller.customerContract;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.explink.dao.CustomerDAO;
import cn.explink.dao.contractManagement.CustomerContractDAO;
import cn.explink.domain.Customer;
import cn.explink.domain.customerCoutract.CustomerContractManagement;
import cn.explink.service.contractManagement.ContractManagementService;
import cn.explink.util.Page;
import cn.explink.util.ResourceBundleUtil;

/**
 *
 * @author wangqiang
 */
@Controller
@RequestMapping("/customerContract")
public class CustomerContractController {
	@Autowired
	private ContractManagementService contractManagementService;
	@Autowired
	private CustomerContractDAO customerContractDAO;
	@Autowired
	private CustomerDAO customerDao;

	/**
	 * 初始化查询合同表中的信息
	 *
	 * @param page
	 * @param model
	 * @param contractManagement
	 * @param createStatrtTime
	 * @param createEndTime
	 * @param overStartTime
	 * @param overEndTime
	 * @param sort
	 * @param method
	 * @return
	 */
	@RequestMapping("/customerContractList/{page}")
	public String getCustomerContractList(Model model, @PathVariable("page") long page, CustomerContractManagement contractManagement,
			@RequestParam(value = "createStatrtTime", required = false) String createStatrtTime, @RequestParam(value = "createEndTime", required = false, defaultValue = "") String createEndTime,
			@RequestParam(value = "overStartTime", required = false) String overStartTime, @RequestParam(value = "overEndTime", required = false) String overEndTime,
			@RequestParam(value = "sort", required = false) String sort, @RequestParam(value = "method", required = false) String method) {
		List<CustomerContractManagement> list = this.contractManagementService.getCustomerContractList(contractManagement, createStatrtTime, createEndTime, overStartTime, overEndTime, sort, method,
				page);
		model.addAttribute("customerContractList", list);
		int count = this.customerContractDAO.getCustomerContractcount(contractManagement, createStatrtTime, createEndTime, overStartTime, overEndTime, sort, method);
		// 客户信息
		Page page_obj = new Page(count, page, Page.ONE_PAGE_NUMBER);
		model.addAttribute("page", page);
		model.addAttribute("page_obj", page_obj);
		List<Customer> customerList = this.customerDao.getAllCustomerss();
		model.addAttribute("contractList", customerList);
		model.addAttribute("number", contractManagement.getNumber());
		model.addAttribute("contractstatus", contractManagement.getContractstatus());
		model.addAttribute("customerid", contractManagement.getCustomerid());
		model.addAttribute("partyaname", contractManagement.getPartyaname());
		model.addAttribute("marketingprincipal", contractManagement.getMarketingprincipal());
		model.addAttribute("othercontractors", contractManagement.getOthercontractors());
		model.addAttribute("contractdescription", contractManagement.getContractdescription());
		model.addAttribute("loansandsettlementway", contractManagement.getLoansandsettlementway());
		model.addAttribute("createStatrtTime", createStatrtTime);
		model.addAttribute("createEndTime", createEndTime);
		model.addAttribute("overStartTime", overStartTime);
		model.addAttribute("overEndTime", overEndTime);
		model.addAttribute("whetherhavedeposit", contractManagement.getWhetherhavedeposit());
		model.addAttribute("sort", sort);
		model.addAttribute("method", method);
		return "customerContract/customerContractList";
	}

	/**
	 * 创建合同(没有附件)
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/addCustomerContract")
	@ResponseBody
	public String add(Model model, CustomerContractManagement cm) {
		if ("[自动生成]".equals(cm.getNumber())) {
			String number = this.contractManagementService.getNumber();
			cm.setNumber(number);
		}
		this.customerContractDAO.createContract(cm);
		return "{\"errorCode\":0,\"error\":\"添加成功\"}";
	}

	/**
	 * 创建合同(有附件)
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/addCustomerContractfile")
	@ResponseBody
	public String addfile(@RequestParam(value = "Filedata", required = false) MultipartFile file, CustomerContractManagement cm) {
		if ("[自动生成]".equals(cm.getNumber())) {
			String number = this.contractManagementService.getNumber();
			cm.setNumber(number);
		}
		String filepath = this.contractManagementService.loadexceptfile(file);
		cm.setContractaccessory(filepath);
		this.customerContractDAO.createContract(cm);
		return "{\"errorCode\":0,\"error\":\"添加成功\"}";
	}

	/**
	 * 删除指定合同
	 */
	@RequestMapping("/deleteContract")
	@ResponseBody
	public String deleteContract(Long id) {
		this.customerContractDAO.deleteContract(id);
		return "{\"errorCode\":0,\"error\":\"删除成功\"}";
	}

	/**
	 * 下载合同
	 */
	@RequestMapping("/download")
	public void downloadContract(HttpServletRequest request, HttpServletResponse response) {
		try {
			String filePath = request.getParameter("filepathurl");
			String filePathaddress = ResourceBundleUtil.FILEPATH + filePath;
			File file = new File(filePathaddress);
			// 取得文件名。
			String filename = file.getName();
			// 以流的形式下载文件。
			InputStream fis;
			fis = new BufferedInputStream(new FileInputStream(filePathaddress));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			// 清空response
			response.reset();
			// 设置response的Header
			response.setContentType("application/ms-excel");
			response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
			response.addHeader("Content-Length", "" + file.length());
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 修改指定合同
	 */
	@RequestMapping("/update")
	@ResponseBody
	public String modificationContract(CustomerContractManagement cmContractManagement) {
		this.contractManagementService.updateContract(cmContractManagement);
		return "{\"errorCode\":0,\"error\":\"修改成功\"}";
	}

	/**
	 * 根据id查询合同信息
	 */
	@RequestMapping("/queryById")
	@ResponseBody
	public CustomerContractManagement queryById(@RequestParam(value = "id", required = false) Long id) {
		// List<CustomerContractManagement> list =
		// this.customerContractDAO.queryByid(id);
		return this.contractManagementService.getBranchContractVO(id);
	}
}
