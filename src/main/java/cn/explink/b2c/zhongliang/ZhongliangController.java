package cn.explink.b2c.zhongliang;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointService;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.domain.Branch;
import cn.explink.enumutil.BranchEnum;

@Controller
@RequestMapping("/zhongliang")
public class ZhongliangController {

	private Logger logger = LoggerFactory.getLogger(ZhongliangController.class);

	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	JointService jointService;
	@Autowired
	ZhongliangService zhongliangService;
	@Autowired
	ZhongliangBackOrderServices zhongliangBackOrderServices;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	ZhongliangInsertCwbDetailTimmer timmer;

	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		model.addAttribute("zhongliang", this.zhongliangService.getZhongliang(key) == null ? new Zhongliang() : this.zhongliangService.getZhongliang(key));
		List<Branch> warehouselist = this.branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue());
		model.addAttribute("warehouselist", warehouselist);
		model.addAttribute("customerList", this.customerDAO.getAllCustomers());
		model.addAttribute("joint_num", key);
		return "jointmanage/zhongliang/edit";
	}

	@RequestMapping("/save/{id}")
	public @ResponseBody
	String smileSave(Model model, @PathVariable("id") int key, HttpServletRequest request) {
		if ((request.getParameter("password") != null) && "explink".equals(request.getParameter("password"))) {
			try{
				this.zhongliangService.edit(request, key);
				this.logger.info("修改中粮B2c信息");
				return "{\"errorCode\":0,\"error\":\"修改成功\"}";
			}catch(Exception e){
				return "{\"errorCode\":1,\"error\":\""+ e.getMessage() +"\"}";
			}

		} else {
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody
	String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		this.zhongliangService.update(key, state);
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	@RequestMapping("/waitOrder")
	public @ResponseBody
	String waitOrder() {
		this.zhongliangService.waitOrder();
		return "已经执行中粮订单下载";
	}

	@RequestMapping("/waitOrdercounts")
	public @ResponseBody
	String waitOrdercounts() {
		this.zhongliangService.waitOrdercounts();
		return "已经执行中粮多次订单下载";
	}

	@RequestMapping("/cancelOrder")
	public @ResponseBody
	String cancelOrder() {

		return this.zhongliangService.CancelOrders();
	}

	@RequestMapping("/backOrder")
	public @ResponseBody
	String backOrder() {
		this.zhongliangBackOrderServices.backOrder();
		return "已经执行中粮[意向单]订单下载";
	}

	@RequestMapping("/backOrdercounts")
	public @ResponseBody
	String backOrdercounts() {
		this.zhongliangBackOrderServices.BackOrdercounts();
		return "已经执行中粮[意向单]订单多次下载";
	}

	@RequestMapping("/backCancelOrder")
	public @ResponseBody
	String CancelOrders() {

		this.zhongliangBackOrderServices.CancelOrders();
		return "已经执行中粮[意向单]订单取消";
	}

	@RequestMapping("/timmer")
	public @ResponseBody
	String timmer() {
		this.timmer.selectTempAndInsertToCwbDetail();
		return "手动执行了中粮的插入正式表定时器";
	}
}
