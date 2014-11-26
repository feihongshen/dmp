package cn.explink.b2c.wenxuan;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import cn.explink.domain.Customer;
import cn.explink.enumutil.BranchEnum;
import cn.explink.util.Dom4jParseUtil;

/**
 * 文轩接口
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/wenxuan")
public class WenxuanController {
	private Logger logger = LoggerFactory.getLogger(WenxuanController.class);
	@Autowired
	WenxuanService wenxuanService;
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	JointService jointService;

	@Autowired
	BranchDAO branchDAO;
	@Autowired
	WenxuanInsertCwbDetailTimmer wenxuanInsertCwbDetailTimmer;

	@Autowired
	WenxuanService_getOrder wenxuanService_getOrder;

	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {

		model.addAttribute("wenxuanObject", wenxuanService.getWenxuan(key));
		model.addAttribute("warehouselist", branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));
		model.addAttribute("joint_num", key);
		return "b2cdj/wenxuan/wenxuan";

	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String save(Model model, @PathVariable("id") int key, HttpServletRequest request) {

		if (request.getParameter("password") != null && "explink".equals(request.getParameter("password"))) {

			wenxuanService.edit(request, key);
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		} else {
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
		// 保存

	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		wenxuanService.update(key, state);
		// 保存
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";

	}

	@RequestMapping("/")
	public String enterpage(Model model) {
		return "b2cdj/wenxuan/OrderDownWx";
	}

	@RequestMapping("/download")
	public @ResponseBody String test(Model model) {
		wenxuanService_getOrder.excutorGetOrdersByTimes();
		return "SUCCESS";
	}

	@RequestMapping("/waybill")
	public String waybill(HttpServletRequest request) {
		String waybill = request.getParameter("handOverNo");
		String responsemsg = "订单" + waybill + "下载成功";

		try {

			wenxuanService_getOrder.excutorGetOrdersByWayBill(waybill);
			wenxuanInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();

		} catch (Exception e) {
			responsemsg = e.getMessage();
		} finally {
			request.setAttribute("responsemsg", responsemsg);
		}

		return "b2cdj/wenxuan/OrderDownWx";
	}

	@RequestMapping("/timmer")
	public @ResponseBody String timmer(HttpServletRequest request, HttpServletResponse response) {
		long starttime = System.currentTimeMillis();
		long endtime = System.currentTimeMillis();

		wenxuanInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();

		logger.info("执行了临时表-获取[家有购物北京]订单的定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));
		return "家有购物北京ERP执行临时表成功";
	}

}
