package cn.explink.b2c.yonghui;

import java.io.BufferedInputStream;
import java.io.InputStream;
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
import cn.explink.b2c.yonghui.domain.Content;
import cn.explink.b2c.yonghui.domain.OrderBack;
import cn.explink.b2c.yonghui.domain.YongHui;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.enumutil.BranchEnum;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.util.Dom4jParseUtil;

@Controller
@RequestMapping("/yonghui")
public class YongHuiController {

	private Logger logger = LoggerFactory.getLogger(YongHuiController.class);

	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	JointService jointService;
	@Autowired
	YongHuiServices yonghuiServices;

	@Autowired
	BranchDAO branchDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	YongHuiInsertCwbDetailTimmer timmer;

	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		model.addAttribute("yonghui", this.yonghuiServices.getYonghui(key) == null ? new YongHui() : this.yonghuiServices.getYonghui(key));
		List<Branch> warehouselist = this.branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue());
		List<Customer> customers = this.customerDAO.getAllCustomers();
		model.addAttribute("customerlist", customers);
		model.addAttribute("warehouselist", warehouselist);
		model.addAttribute("joint_num", key);
		return "jointmanage/yonghui/edit";
	}

	@RequestMapping("/save/{id}")
	public @ResponseBody
	String smileSave(Model model, @PathVariable("id") int key, HttpServletRequest request) {
		if ((request.getParameter("password") != null) && "explink".equals(request.getParameter("password"))) {
			try{
				this.yonghuiServices.edit(request, key);
				this.logger.info("修改-永辉-信息");
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
		this.yonghuiServices.update(key, state);
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	@RequestMapping("/timmer")
	public @ResponseBody
	String timmer() {
		this.timmer.selectTempAndInsertToCwbDetail();
		return "手动执行了-永辉-的插入正式表定时器";
	}

	@RequestMapping("/orderdown")
	public @ResponseBody
	String orderdown(HttpServletRequest request) {
		// String content_str = request.getParameter("Content");
		String content_str = null;
		//BufferedReader br;
		OrderBack back = new OrderBack();
		try {
			request.setCharacterEncoding("utf-8");
			//br = new BufferedReader(new InputStreamReader(request.getInputStream()));
			InputStream input = new BufferedInputStream(request.getInputStream());
			content_str = Dom4jParseUtil.getStringByInputStream(input); // 读取文件流，获得xml字符串
			
			this.logger.info("-永辉-订单下载json={}", content_str);
			/*if (content_str != null) {
				content_str = new String(content_str.getBytes("utf-8"));
			} else {
				return "{\"errCode\":\"05\",\"errMsg\":\"系统异常\"}";
			}*/
			if(content_str == null){
				return "{\"errCode\":\"05\",\"errMsg\":\"系统异常\"}";
			}
			
		} catch (Exception e1) {
			this.logger.error("-永辉-订单下载异常", e1);
			return "{\"errCode\":\"05\",\"errMsg\":\"系统异常\"}";
		}
		Content con = null;

		try {
			if ((content_str != null) && (content_str.length() > 0)) {
				con = JacksonMapper.getInstance().readValue(content_str, Content.class);
				back = this.yonghuiServices.orderBack(con);
			} else {
				back.setErrCode(YongHuiExpEmum.CanShuCuoWu.getErrCode());
				back.setErrMsg(YongHuiExpEmum.CanShuCuoWu.getErrMsg());
				return JacksonMapper.getInstance().writeValueAsString(back);

			}
			return JacksonMapper.getInstance().writeValueAsString(back);
		} catch (Exception e) {
			back.setErrCode(YongHuiExpEmum.XiTongYiChang.getErrCode());
			back.setErrMsg(YongHuiExpEmum.XiTongYiChang.getErrMsg());
			logger.error("永辉超市接口异常",e);
			return "{\"errCode\":\"05\",\"errMsg\":\"系统异常\"}";
		}
	}

	@RequestMapping("/test")
	public String test(Model model) {

		return "jointmanage/yonghui/test";
	}
}
