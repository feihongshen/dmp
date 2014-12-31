package cn.explink.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.domain.OverdueExMoCondVO;
import cn.explink.domain.TimeTypeEnum;

/**
 * 超期异常监控控制器.
 *
 * @author zhaoshb
 * @since DMP3.0
 */
@Controller
@RequestMapping("/overdueexmo")
public class OverdueExMoController {

	@Autowired
	private CustomerDAO customerDAO = null;

	@Autowired
	private BranchDAO branchDAO = null;

	@RequestMapping("/{page}")
	public ModelAndView list(@PathVariable("page") int page, OverdueExMoCondVO condVO) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("time_type_map", this.getTimeTypeMap());
		mav.addObject("time_type", TimeTypeEnum.SystemAcceptTime.ordinal());
		mav.addObject("vender_map", this.getVenderMap());
		mav.addObject("org_map", this.getOrgMap());
		mav.addObject("show_col_map", this.getShowColMap());
		mav.addObject("cond", condVO);

		mav.setViewName("/orverdueexmo/overdueexmo");

		return mav;
	}

	private Map<Integer, String> getTimeTypeMap() {
		Map<Integer, String> timeTypeMap = new LinkedHashMap<Integer, String>();
		for (TimeTypeEnum timeType : TimeTypeEnum.values()) {
			timeTypeMap.put(timeType.ordinal(), timeType.getName());
		}
		return timeTypeMap;
	}

	private Map<Long, String> getVenderMap() {
		Map<Long, String> venderMap = new LinkedHashMap<Long, String>();
		List<Customer> customerList = this.getCustomerDAO().getAllCustomers();
		for (Customer customer : customerList) {
			venderMap.put(customer.getCustomerid(), customer.getCustomername());
		}
		return venderMap;
	}

	private Map<Long, String> getOrgMap() {
		List<Branch> branchList = this.getBranchDAO().getAllBranchBySiteType(1L);
		Map<Long, String> branchMap = new LinkedHashMap<Long, String>();
		for (Branch branch : branchList) {
			branchMap.put(branch.getBranchid(), branch.getBranchname());
		}
		return branchMap;
	}

	private Map<Integer, String> getShowColMap() {
		Map<Integer, String> showColMap = new LinkedHashMap<Integer, String>();
		for (ShowColEnum showCol : ShowColEnum.values()) {
			showColMap.put(showCol.ordinal(), showCol.getColName());
		}
		return showColMap;
	}

	private CustomerDAO getCustomerDAO() {
		return this.customerDAO;
	}

	private BranchDAO getBranchDAO() {
		return this.branchDAO;
	}

	private enum ShowColEnum {
		SystemAccept("系统接收"), OutAreaTransfer("超区转单"), NotMatched("未匹配"), StationAccept("站点接收"), Print("打印"), Dispatch("分派"), RptOutArea("上报超区"), GetBack("揽退");

		String colName;

		ShowColEnum(String colName) {
			this.colName = colName;
		}

		public String getColName() {
			return this.colName;
		}

	}

}
