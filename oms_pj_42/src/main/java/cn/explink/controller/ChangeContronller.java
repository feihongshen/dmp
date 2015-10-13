package cn.explink.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.CwbDAO;
import cn.explink.dao.GetDmpDAO;
import cn.explink.domain.Branch;
import cn.explink.service.FlowFromJMSService;

@Controller
@RequestMapping("/OMSChange")
public class ChangeContronller {

	@Autowired
	FlowFromJMSService flowFromJMSService;
	@Autowired
	GetDmpDAO getDmpDAO;
	@Autowired
	CwbDAO cwbDAO;

	/**
	 * 迁移妥投
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/tuotou")
	public @ResponseBody String tuotouChange(Model model, HttpServletRequest request) {
		try {
			String pram = request.getParameter("pram");
			long functionid = request.getParameter("functionid") == null ? 0 : Long.parseLong(request.getParameter("functionid").toString());
			long issycTime = request.getParameter("issycTime") == null ? 0 : Long.parseLong(request.getParameter("issycTime").toString());
			if (functionid != 0) {
				flowFromJMSService.saveAllForChange(pram, 0, functionid, issycTime);
			} else {
				flowFromJMSService.saveAll(pram, 0);
			}
			return "{msg:\"00\"}";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "{msg:\"50\"}";
		}

	}

	/**
	 * 存储branch Map
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/pushBranchMap")
	public @ResponseBody String pushBranchMap(Model model, HttpServletRequest request) {
		try {
			List<Branch> branchList = getDmpDAO.getAllBranchs();
			if (branchList != null && branchList.size() > 0) {
				flowFromJMSService.putBranchMap(branchList);
				return "{msg:\"00\"}";
			} else {
				return "{msg:\"01\"}";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "{msg:\"50\"}";
		}

	}

	/**
	 * 订单修改
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/editcwb")
	public @ResponseBody String editcwb(Model model, HttpServletRequest request) {
		try {
			flowFromJMSService.editcwb(request);
			return "{msg:\"00\"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{msg:\"50\"}";
		}
	}

}
