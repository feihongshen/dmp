package cn.explink.b2c.explink.core;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.CommonEmaildateDAO;
import cn.explink.dao.GetDmpDAO;
import cn.explink.dao.WarehouseCommenDAO;
import cn.explink.domain.CommonEmaildate;
import cn.explink.domain.WarehouseToCommen;
import cn.explink.service.FlowFromJMSService;

@Controller
@RequestMapping("/OMSExplink")
public class CoreContronller {

	@Autowired
	FlowFromJMSService flowFromJMSService;
	@Autowired
	GetDmpDAO getDmpDAO;
	@Autowired
	CommonEmaildateDAO commonEmaildateDAO;
	@Autowired
	WarehouseCommenDAO warehouseCommenDAO;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 接受dmp发的出库确认数据
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/postdata")
	public @ResponseBody String getpostdata(Model model, HttpServletRequest request) {
		try {
			String pram = request.getParameter("pram");
			logger.info("postdata pram:{}", pram);
			JSONObject json = JSONObject.fromObject(pram);
			long id = json.getLong("id");
			String cwb = json.getString("cwb");
			long customerid = json.getLong("customerid");
			long startbranchid = json.getLong("startbranchid");
			String commencode = json.getString("commencode");
			String credate = json.getString("credate");
			String statetime = json.getString("statetime");
			long emaildateid = json.getLong("emaildateid");
			long nextbranchid = json.getLong("nextbranchid");
			WarehouseToCommen comm = warehouseCommenDAO.getCountByid(id);
			if (comm == null) {
				warehouseCommenDAO.creWarehouseToCommen(id, cwb, customerid, startbranchid, nextbranchid, commencode, credate, statetime, emaildateid);
				commonEmaildateDAO.updateCommonEmaildateCount(emaildateid);// 新的emaildateid
																			// 数量加1
			} else {
				warehouseCommenDAO.updateWarehouseToCommen(id, cwb, startbranchid, nextbranchid, commencode, credate, emaildateid);
				if (comm.getEmaildateid() != emaildateid) {
					commonEmaildateDAO.updateCommonEmaildateCount(emaildateid);// 新的emaildateid
																				// 数量加1
					commonEmaildateDAO.updateCommonEmaildateJianCount(comm.getEmaildateid());// 原来的emaildateid
																								// 数量减1
				}
			}
			return "{msg:\"00\"}";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "{msg:\"50\"}";
		}

	}

	@RequestMapping("/emaildate")
	public @ResponseBody String getemaildate(Model model, HttpServletRequest request) {
		try {
			String pram = request.getParameter("pram");
			logger.info("emaildate pram:{}", pram);
			JSONObject json = JSONObject.fromObject(pram);
			CommonEmaildate comm = new CommonEmaildate();
			comm.setCommoncode(json.getString("commencode"));
			comm.setEmaildate(json.getString("emaildate"));
			long emaildateid = commonEmaildateDAO.creCommonEmaildate(comm);
			return "{emaildateid:" + emaildateid + "}";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "{emaildateid:-1}";
		}

	}

}
