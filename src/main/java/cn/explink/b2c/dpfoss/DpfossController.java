package cn.explink.b2c.dpfoss;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointService;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.domain.Customer;
import cn.explink.enumutil.BranchEnum;
import cn.explink.util.DateTimeUtil;

@Controller
@RequestMapping("/dpfoss")
public class DpfossController {
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	JointService jointService;
	@Autowired
	DpfossService dpfossService;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	DpfossInsertCwbDetailTimmer dpfossInsertCwbDetailTimmer;
	@Autowired
	CustomerDAO customerDAO;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		model.addAttribute("dpfossobject", dpfossService.getDpfoss(key));
		model.addAttribute("warehouselist", branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));
		model.addAttribute("joint_num", key);
		return "b2cdj/dpfoss/dpfoss";
	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String smileSave(Model model, @PathVariable("id") int key, HttpServletRequest request) {
		if (request.getParameter("password") != null && "explink".equals(request.getParameter("password"))) {
			try{
				dpfossService.edit(request, key);
				return "{\"errorCode\":0,\"error\":\"修改成功\"}";
			}catch(Exception e){
				return "{\"errorCode\":1,\"error\":\""+ e.getMessage() +"\"}";
			}
		} else {
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		dpfossService.update(key, state);
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	@RequestMapping("/")
	public String enterpage(Model model) {
		List<Customer> newcustomerlist = getDPCustomerList();

		model.addAttribute("customerlist", newcustomerlist);
		return "b2cdj/dpfoss/dpfossOrder_download";
	}

	private List<Customer> getDPCustomerList() {
		List<Customer> newcustomerlist = new ArrayList<Customer>();
		List<Customer> customerlist = customerDAO.getAllCustomers();
		for (Customer customer : customerlist) {
			if (customer.getB2cEnum().isEmpty()) {
				continue;
			}
			if ("0".equals(customer.getB2cEnum())) {
				continue;
			}
			if (getDpfossEnumsKey().contains(customer.getB2cEnum())) {
				newcustomerlist.add(customer);
			}
		}
		return newcustomerlist;
	}

	private String getDpfossEnumsKey() {
		String dpfoss_keys = "";
		for (B2cEnum em : B2cEnum.values()) {
			if (em.getMethod().contains("DPFoss")) {
				dpfoss_keys += em.getKey() + ",";
			}
		}
		return dpfoss_keys.isEmpty() ? "" : dpfoss_keys.substring(0, dpfoss_keys.length() - 1);
	}

	/**
	 * 手动执行交接单下载功能
	 */
	@RequestMapping("/download")
	public String requestByGuangZhouABC(HttpServletRequest request, HttpServletResponse response) {
		String responsemsg = "";
		String customerid = "";
		String rdotype = "";
		try {

			customerid = request.getParameter("customerid");
			String dpfossEnums = getDpfossEnumsKey();
			String handOverNo = request.getParameter("handOverNo"); // 页面录入交接单号
			String waybillNo = request.getParameter("handOverNo"); // 页面录入交接单号
			rdotype = request.getParameter("rdotype");

			if (rdotype.equals("1")) {
				waybillNo = "";
			} else {
				handOverNo = "";
			}

			Customer customer = customerDAO.getCustomerById(Long.valueOf(customerid));

			ValidateDpfoss(request, handOverNo, waybillNo, rdotype, dpfossEnums, customer);

			int waybillNo_count = dpfossService.cwbOrdersDownloading(handOverNo, waybillNo, Integer.valueOf(customer.getB2cEnum()));
			dpfossInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail(Integer.valueOf(customer.getB2cEnum()));
			responsemsg = "目前已成功下载交接单号=[" + handOverNo + "]的全部数据，订单数量=[" + waybillNo_count + "]，当前时间=" + DateTimeUtil.getNowTime();

		} catch (Exception e) {
			logger.error("0德邦物流0处理业务逻辑异常！", e);
			responsemsg = e.getMessage();
		} finally {
			request.setAttribute("responsemsg", responsemsg);
			request.setAttribute("customerlist", getDPCustomerList());
			request.setAttribute("customerid", customerid);
			request.setAttribute("rdotype", rdotype);
		}
		return "b2cdj/dpfoss/dpfossOrder_download";
	}

	private void ValidateDpfoss(HttpServletRequest request, String handOverNo, String waybillNo, String rdotype, String dpfossEnums, Customer customer) {

		int isOpenFlag = dpfossService.getStateForJoints(dpfossEnums, String.valueOf(customer.getCustomerid()));
		if (isOpenFlag == 0) {
			throw new RuntimeException("未开启0德邦物流0查询接口," + customer.getCustomername());
		}

		if (rdotype.equals("1")) {
			if (handOverNo == null || handOverNo.equals("")) {
				throw new RuntimeException("请求输入的交接单号为空!");
			}
		} else {
			if (waybillNo == null || waybillNo.equals("")) {
				throw new RuntimeException("请求输入的运单号为空!");
			}
		}

	}

	@RequestMapping("/timmer")
	public @ResponseBody void ExcuteTimmerMethod_tmall(HttpServletRequest request, HttpServletResponse response) {

		for (B2cEnum enums : B2cEnum.values()) { // 遍历唯品会enum，可能有多个枚举
			if (enums.getMethod().contains("DPFoss")) {

				dpfossInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail(enums.getKey());
				logger.info("执行了德邦物流查询临时表的定时器!");
			}
		}

	}

}
