package cn.explink.b2c.explink.core_down;

import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.explink.xmldto.OrderEntity;
import cn.explink.b2c.tools.JointService;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.enumutil.BranchEnum;
import cn.explink.util.XmlUtil;
import cn.explink.util.MD5.MD5Util;

@Controller
@RequestMapping("/epaiApi")
public class EpaiApiController {
	private Logger logger = LoggerFactory.getLogger(EpaiApiController.class);
	@Autowired
	EpaiApiService epaiApiService;

	@Autowired
	BranchDAO branchDAO;
	@Autowired
	JointService jointService;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	EpaiApiDAO epaiApiDAO;
	@Autowired
	EpaiApiService_Download epaiApiService_Download;
	@Autowired
	EpaiApiService_ExportCallBack epaiApiService_ExportCallBack;
	@Autowired
	EpaiInsertCwbDetailTimmer epaiInsertCwbDetailTimmer;
	@Autowired
	AcquisitionOrderService acquisitionOrderService;

	@RequestMapping("/add")
	public String add(Model model) throws Exception {

		model.addAttribute("customerlist", customerDAO.getAllCustomers());
		model.addAttribute("warehouselist", branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));
		return "jointmanage/epaiApi_down/add";
	}

	@RequestMapping("/create")
	public @ResponseBody String create(HttpServletRequest request, Model model,
			@RequestParam(value = "customerid", required = false, defaultValue = "0") long customerid,
			@RequestParam(value = "userCode", required = false, defaultValue = "") String usercode) {

		if (request.getParameter("password") != null && "explink".equals(request.getParameter("password"))) {
			boolean isExistsFlag = epaiApiDAO.IsExistsPosCodeFlag(usercode, customerid);

			if (isExistsFlag) {
				return "{\"errorCode\":1,\"error\":\"userCode或者供货商已存在！\"}";
			} else {
				EpaiApi pc = epaiApiService.loadingEpaiApiEntity(request, usercode);
				epaiApiDAO.createEpaiApi(pc);
				return "{\"errorCode\":0,\"error\":\"新建成功\"}";
			}
		} else {
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}

	}

	@RequestMapping("/list")
	public String poscodeMapplist(HttpServletRequest request, Model model) {

		model.addAttribute("epailist", epaiApiDAO.getEpaiApiList());
		model.addAttribute("warehouselist", branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));
		model.addAttribute("customerlist", customerDAO.getAllCustomers());
		return "jointmanage/epaiApi_down/epaiapi";
	}

	@RequestMapping("/del/{id}/{pwd}")
	public @ResponseBody String del(Model model, @PathVariable("id") long b2cid, @PathVariable("pwd") String pwd) {
		if (pwd != null && pwd.equals("explink")) {
			epaiApiDAO.exptReasonDel(b2cid);
			return "{\"errorCode\":0,\"error\":\"操作成功\"}";
		} else {

			return "{\"errorCode\":0,\"error\":\"操作失败，密码错误\"}";
		}

	}

	@RequestMapping("/edit/{id}")
	public String edit(@PathVariable("id") long b2cid, Model model, HttpServletRequest request) {
		model.addAttribute("customerlist", customerDAO.getAllCustomers());
		model.addAttribute("epaiapi", epaiApiDAO.getEpaiApi(b2cid));
		model.addAttribute("warehouselist", branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));

		return "jointmanage/epaiApi_down/edit";

	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String save(@PathVariable("id") long b2cid, Model model, HttpServletRequest request) {
		if (request.getParameter("password") != null && "explink".equals(request.getParameter("password"))) {
			String usercode = request.getParameter("userCode");
			String customerid = request.getParameter("customerid");
			boolean isExistsFlag = epaiApiDAO.IsExistsPosCodeFlag(usercode, customerid, b2cid);
			if (!isExistsFlag) {
				EpaiApi pc = epaiApiService.loadingEpaiApiEntity(request, "");
				epaiApiDAO.update(pc, b2cid);
				return "{\"errorCode\":0,\"error\":\"修改成功\"}";
			} else {
				return "{\"errorCode\":0,\"error\":\"重复设置\"}";
			}

		} else {
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}

	}

	/**
	 * 
	 * @Description: 接收推送过来的订单信息 @param @param
	 * request @param @param response @param @return @return String @throws
	 */
	@RequestMapping("/sendCwbDetail")
	public @ResponseBody String sendCwbDetail(HttpServletRequest request, HttpServletResponse response) {

		String content = request.getParameter("content"); // xml正文
		String userCode = request.getParameter("userCode");// 承运商标识
		String requestTime = request.getParameter("requestTime"); // 请求时间
		String sign = request.getParameter("sign");// 签名
		this.logger.info("请求参数:content={},userCode={},requestTime={},sign={}}",
				new Object[] { content, userCode, requestTime, sign });
		
		try {
			
			if(content == null || content == "" || userCode == null || userCode == "" || requestTime == null || requestTime ==""
				||	sign == null || sign == ""){
				this.logger.info("请求参数为空:content={},userCode={},requestTime={},sign={}}",
				new Object[] { content, userCode, requestTime, sign });
				return this.acquisitionOrderService.sendXml("99", "请求参数为空");
			}
			
			content = URLDecoder.decode(content, "UTF-8");// 实际xml信息
			userCode = URLDecoder.decode(userCode, "UTF-8");
			requestTime = URLDecoder.decode(requestTime, "UTF-8");
			
			//非空判断
			if (content == null || content.equals("")) {
				logger.info(userCode+"解析xml之后的数据集为空content={},userCode={}", content, userCode);
				return this.acquisitionOrderService.sendXml("99", "content为空");
			}
			
			if(userCode == null || userCode.equals("")){
				logger.info(userCode+"承运商标识为空userCode={}",userCode);
				return this.acquisitionOrderService.sendXml("99", "userCode为空");
			}
			
			EpaiApi epaiApi = epaiApiDAO.getEpaiApiByUserCode(userCode);
			
			if (epaiApi == null) {
				logger.info("本地没有对应的承运商标识userCode={}",userCode);
				return this.acquisitionOrderService.sendXml("99", "本地没有对应的承运商标识");
			}
			//判断是否开启对接
			if(epaiApi.getIsopenflag() != 1){
				logger.info("当前客户没有开启对接！userCode={}",userCode);
				return this.acquisitionOrderService.sendXml("99", "未开启对接");
			}
			
			// 校验加密是否符合约定
			String MD5Sign = MD5Util.md5(userCode + requestTime + epaiApi.getPrivate_key());
			if (!sign.equalsIgnoreCase(MD5Sign)) {
				this.logger.info(userCode+"MD5验证失败sign={},MD5Sign={}",sign,MD5Sign);
				return this.acquisitionOrderService.sendXml("02", "签名错误");
			}
			
			
			OrderEntity order = XmlUtil.toObject(OrderEntity.class, content);
			/*
			 * 处理xml中的信息
			 */
			return this.acquisitionOrderService.orderDetailExportInterface(order, epaiApi);
		} catch (Exception e) {
			this.logger.error(userCode+"处理异常，原因:", e);
			return acquisitionOrderService.sendXml("99", "未知异常");
		}

	}

	/**
	 * 手动执行推送测试类
	 * 
	 * @Description: 接收推送过来的订单信息 @param @param
	 * request @param @param response @param @return @return String @throws
	 */
	/*
	 * @RequestMapping("/test") public @ResponseBody String epai_test() {
	 * epaiApiService_ExportCallBack.exportCallBack_controllers();
	 * epaiInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail(); return
	 * "系统之间对接-下游电商手动下载数据完成";
	 * 
	 * }
	 */

	/**
	 * 手动执行推送测试类
	 * 下单
	 * @return
	 */

	
	 @RequestMapping("/test") 
	 public @ResponseBody String epai_test() {
		 
		 acquisitionOrderService.passiveReceptionOrActiveAcquisition();
		 epaiApiService_ExportCallBack.exportCallBack_controllers();
		 epaiInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail(); 
	 return "系统之间对接-下游电商手动下载数据完成";
	 
	 }

	 
	 /**
	 * 手动执行推送测试类
	 * 下单确认
	 * @return
	 */

	
	 @RequestMapping("/orderExportCallback") 
	 public @ResponseBody String orderExportCallback() {
		 this.epaiApiService_ExportCallBack.exportCallBack_controllers();
		 return "系统之间对接-下游电商手动下载数据完成（下单确认）";
	  }
	 
	 /**
	 * 手动执行推送测试类
	 * 导入主表
	 * @return
	 */
	
	 @RequestMapping("/timmer") 
	 public @ResponseBody String timmer() {
		 this.epaiInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail(); 
		 return "系统之间对接-下游电商手动下载数据完成（导入主表）";
	  }


}
