package cn.explink.controller.express;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.domain.express.ExpressCodBill;
import cn.explink.domain.express.ExpressCodBillDetail;
import cn.explink.enumutil.express.ExpressBillTypeEnum;
import cn.explink.service.express.TransProvincialReceivedReconciliationService;
import cn.explink.util.Page;

/**
 *
 * @description 跨省代收货款对账（应付）的controller
 * @author  刘武强
 * @data   2015年8月19日
 */
@Controller
@RequestMapping("/transProvincialPayableReconciliationController")
public class TransProvincialPayableReconciliationController {
	@Autowired
	private TransProvincialReceivedReconciliationService transProvincialReceivedReconciliationService;

	/**
	 *
	 * @Title: init
	 * @description 跨省代收货款对账（应付）功能的初始化方法
	 * @author 刘武强
	 * @date  2015年8月20日上午9:15:39
	 * @param  @param model
	 * @param  @return
	 * @return  String
	 * @throws
	 */
	@RequestMapping("/init")
	public String init(Model model) {
		model.addAttribute("infoList", new ArrayList<ExpressCodBill>());
		model.addAttribute("BranchprovinceId", this.transProvincialReceivedReconciliationService.getProvinceId());
		model.addAttribute("page_obj", new Page(0, 0, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", 0);
		model.addAttribute("provincelist", this.transProvincialReceivedReconciliationService.getAllProvince(1));
		model.addAttribute("provinceNonAllList", this.transProvincialReceivedReconciliationService.getAllProvince(0));
		model.addAttribute("billStatelist", this.transProvincialReceivedReconciliationService.getExpressBillState());
		return "express/codSettlement/transProvincialReceivedReconciliationPayInit";
	}

	/**
	 *
	 * @Title: search
	 * @description 根据条件返回查看数据
	 * @author 刘武强
	 * @date  2015年8月20日上午9:21:20
	 * @param  @param model
	 * @param  @param billNo
	 * @param  @param billState
	 * @param  @param creatStart
	 * @param  @param creatEnd
	 * @param  @param cavStart
	 * @param  @param cavEnd
	 * @param  @param provinceId
	 * @param  @param sequenceField
	 * @param  @param ascOrDesc
	 * @param  @param page
	 * @param  @return
	 * @return  String
	 * @throws
	 */
	@RequestMapping("/search/{page}")
	public String search(Model model, String billNo, @RequestParam(value = "billState", defaultValue = "-1") int billState, @DateTimeFormat(pattern = "yyyy-MM-dd") Date creatStart, @DateTimeFormat(pattern = "yyyy-MM-dd") Date creatEnd, @DateTimeFormat(pattern = "yyyy-MM-dd") Date cavStart, @DateTimeFormat(pattern = "yyyy-MM-dd") Date cavEnd, @RequestParam(value = "provinceId", defaultValue = "-1") int provinceId, String sequenceField, String ascOrDesc, @PathVariable(value = "page") long page) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = this.transProvincialReceivedReconciliationService
				.getCodeBillInfo(billNo, billState, creatStart, creatEnd, cavStart, cavEnd, provinceId, sequenceField, ascOrDesc, ExpressBillTypeEnum.AcrossProvinceCodPayableBill.getValue(), page, Page.ONE_PAGE_NUMBER);
		model.addAttribute("infoList", map.get("list"));
		model.addAttribute("BranchprovinceId", this.transProvincialReceivedReconciliationService.getProvinceId());
		model.addAttribute("page_obj", new Page((Integer) map.get("count"), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		model.addAttribute("provincelist", this.transProvincialReceivedReconciliationService.getAllProvince(1));
		model.addAttribute("provinceNonAllList", this.transProvincialReceivedReconciliationService.getAllProvince(0));
		model.addAttribute("billStatelist", this.transProvincialReceivedReconciliationService.getExpressBillState());
		return "express/codSettlement/transProvincialReceivedReconciliationPayInit";
	}

	/**
	 *
	 * @Title: delete
	 * @description TODO
	 * @author 刘武强
	 * @date  2015年8月20日下午3:04:32
	 * @param  @param model
	 * @param  @param id
	 * @param  @return
	 * @return  String
	 * @throws
	 */
	@RequestMapping("/delete")
	public String delete(Model model, Long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = this.transProvincialReceivedReconciliationService.delete(id, ExpressBillTypeEnum.AcrossProvinceCodPayableBill.getValue(), 1, Page.ONE_PAGE_NUMBER);
		model.addAttribute("infoList", map.get("list"));
		model.addAttribute("BranchprovinceId", this.transProvincialReceivedReconciliationService.getProvinceId());
		model.addAttribute("page_obj", new Page((Integer) map.get("count"), 1, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", 1);
		model.addAttribute("provincelist", this.transProvincialReceivedReconciliationService.getAllProvince(1));
		model.addAttribute("provinceNonAllList", this.transProvincialReceivedReconciliationService.getAllProvince(0));
		model.addAttribute("billStatelist", this.transProvincialReceivedReconciliationService.getExpressBillState());
		model.addAttribute("deleteFlag", map.get("flag"));
		return "express/codSettlement/transProvincialReceivedReconciliationPayInit";
	}

	/**
	 *
	 * @Title: update
	 * @description 根据前台修改的备注，更新到账单表
	 * @author 刘武强
	 * @date  2015年8月20日下午4:00:06
	 * @param  @param model
	 * @param  @param expressCodBill 记录了备注信息的vo
	 * @param  @return
	 * @return  String
	 * @throws
	 */
	@RequestMapping("/update")
	public String update(Model model, ExpressCodBill expressCodBill) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = this.transProvincialReceivedReconciliationService.update(expressCodBill, ExpressBillTypeEnum.AcrossProvinceCodPayableBill.getValue(), 1, Page.ONE_PAGE_NUMBER);
		model.addAttribute("infoList", map.get("list"));
		model.addAttribute("BranchprovinceId", this.transProvincialReceivedReconciliationService.getProvinceId());
		model.addAttribute("page_obj", new Page((Integer) map.get("count"), 1, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", 1);
		model.addAttribute("provincelist", this.transProvincialReceivedReconciliationService.getAllProvince(1));
		model.addAttribute("provinceNonAllList", this.transProvincialReceivedReconciliationService.getAllProvince(0));
		model.addAttribute("billStatelist", this.transProvincialReceivedReconciliationService.getExpressBillState());
		model.addAttribute("updateFlag", map.get("flag"));
		return "express/codSettlement/transProvincialReceivedReconciliationPayInit";
	}

	@RequestMapping("/getOrderByProvincereceivablecodbillid/{page}")
	@ResponseBody
	public JSONObject getOrderByProvincereceivablecodbillid(Long provincereceivablecodbillid, @PathVariable(value = "page") long page) {
		JSONObject obj = new JSONObject();
		Map<String, Object> map = this.transProvincialReceivedReconciliationService.getOrderByProvincereceivablecodbillid(provincereceivablecodbillid, ExpressBillTypeEnum.AcrossProvinceCodPayableBill
				.getValue(), 1, Page.ONE_PAGE_NUMBER);
		obj.put("alert_infoMap", map.get("list"));
		obj.put("alert_page_obj", new Page((Integer) map.get("count"), page, Page.ONE_PAGE_NUMBER));
		obj.put("alert_page", 1);
		return obj;
	}

	@RequestMapping("/alertInfo/{page}")
	public String alertInfo(Model model, @PathVariable(value = "page") long page) {
		//TODO 查询导入的数据，用于显示
		List<ExpressCodBillDetail> list = new ArrayList<ExpressCodBillDetail>();
		model.addAttribute("infoList", list);//导入的数据
		model.addAttribute("page", page);//当前页面
		model.addAttribute("page_obj", new Page(2, 1, Page.ONE_PAGE_NUMBER));//页面实体
		model.addAttribute("money", 0);//代收货款
		model.addAttribute("count", 0);//运单数量
		return "express/codSettlement/transProvincialPayAlert";
	}

	/**
	 *
	 * @Title: exportInfo
	 * @description 导入数据
	 * @author 刘武强
	 * @date  2015年8月24日上午10:43:05
	 * @param  @return
	 * @return  JSONObject
	 * @throws
	 */
	public JSONObject exportInfo() {
		JSONObject obj = new JSONObject();
		//TODO 导入数据，放回flag(true、flase)和errInfo(错误信息)
		obj.put("flag", true);
		obj.put("errInfo", null);
		return obj;
	}

	@RequestMapping("/insert")
	public String insert(Model model, ExpressCodBill expressCodBill) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = this.transProvincialReceivedReconciliationService.insert(expressCodBill, ExpressBillTypeEnum.AcrossProvinceCodPayableBill.getValue(), 1, Page.ONE_PAGE_NUMBER);
		model.addAttribute("infoList", map.get("list"));
		model.addAttribute("BranchprovinceId", this.transProvincialReceivedReconciliationService.getProvinceId());
		model.addAttribute("page_obj", new Page((Integer) map.get("count"), 1, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", 1);
		model.addAttribute("provincelist", this.transProvincialReceivedReconciliationService.getAllProvince(1));
		model.addAttribute("provinceNonAllList", this.transProvincialReceivedReconciliationService.getAllProvince(0));
		model.addAttribute("billStatelist", this.transProvincialReceivedReconciliationService.getExpressBillState());
		model.addAttribute("insertFlag", map.get("flag"));
		model.addAttribute("id", expressCodBill.getId());
		return "express/codSettlement/transProvincialReceivedReconciliationPayInit";
	}
}
