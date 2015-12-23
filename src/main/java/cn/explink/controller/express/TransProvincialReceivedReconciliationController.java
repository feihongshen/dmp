package cn.explink.controller.express;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import cn.explink.enumutil.express.ExpressBillTypeEnum;
import cn.explink.service.express.TransProvincialReceivedReconciliationService;
import cn.explink.util.Page;

/**
 *
 * @description 跨省代收货款对账（应收）的controller
 * @author  刘武强
 * @data   2015年8月12日
 */
@Controller
@RequestMapping("/transProvincialReceivedReconciliationController")
public class TransProvincialReceivedReconciliationController extends ExpressCommonController {
	@Autowired
	private TransProvincialReceivedReconciliationService transProvincialReceivedReconciliationService;

	/**
	 *
	 * @Title: init
	 * @description 跨省代收货款对账（应收）功能的初始化方法
	 * @author 刘武强
	 * @date  2015年8月12日下午4:51:50
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
		return "express/codSettlement/transProvincialReceivedReconciliationInit";
	}

	/**
	 *
	 * @Title: search
	 * @description 根据条件返回查看数据
	 * @author 刘武强
	 * @date  2015年8月19日上午10:10:25
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
				.getCodeBillInfo(billNo, billState, creatStart, creatEnd, cavStart, cavEnd, provinceId, sequenceField, ascOrDesc, ExpressBillTypeEnum.AcrossProvinceCodReceivableBill.getValue(), page, Page.ONE_PAGE_NUMBER);
		model.addAttribute("infoList", map.get("list"));
		model.addAttribute("BranchprovinceId", this.transProvincialReceivedReconciliationService.getProvinceId());
		model.addAttribute("page_obj", new Page((Integer) map.get("count"), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		model.addAttribute("provincelist", this.transProvincialReceivedReconciliationService.getAllProvince(1));
		model.addAttribute("provinceNonAllList", this.transProvincialReceivedReconciliationService.getAllProvince(0));
		model.addAttribute("billStatelist", this.transProvincialReceivedReconciliationService.getExpressBillState());
		return "express/codSettlement/transProvincialReceivedReconciliationInit";
	}

	/**
	 *
	 * @Title: delete
	 * @description 删除指定id的express_ops_cod_bill数据，并重新查询刷新页面
	 * @author 刘武强
	 * @date  2015年8月14日上午9:51:55
	 * @param  @param model
	 * @param  @param id
	 * @param  @param billNo 不会传参过来，只是为了方便方法的调用
	 * @param  @param billState 不会传参过来，只是为了方便方法的调用
	 * @param  @param creatStart 不会传参过来，只是为了方便方法的调用
	 * @param  @param creatEnd 不会传参过来，只是为了方便方法的调用
	 * @param  @param cavStart 不会传参过来，只是为了方便方法的调用
	 * @param  @param cavEnd 不会传参过来，只是为了方便方法的调用
	 * @param  @param provinceId 不会传参过来，只是为了方便方法的调用
	 * @param  @param sequenceField 不会传参过来，只是为了方便方法的调用
	 * @param  @param ascOrDesc 不会传参过来，只是为了方便方法的调用
	 * @param  @return
	 * @return  String
	 * @throws
	 */
	@RequestMapping("/delete")
	public String delete(Model model, Long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = this.transProvincialReceivedReconciliationService.delete(id, ExpressBillTypeEnum.AcrossProvinceCodReceivableBill.getValue(), 1, Page.ONE_PAGE_NUMBER);
		model.addAttribute("infoList", map.get("list"));
		model.addAttribute("BranchprovinceId", this.transProvincialReceivedReconciliationService.getProvinceId());
		model.addAttribute("page_obj", new Page((Integer) map.get("count"), 1, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", 1);
		model.addAttribute("provincelist", this.transProvincialReceivedReconciliationService.getAllProvince(1));
		model.addAttribute("provinceNonAllList", this.transProvincialReceivedReconciliationService.getAllProvince(0));
		model.addAttribute("billStatelist", this.transProvincialReceivedReconciliationService.getExpressBillState());
		model.addAttribute("deleteFlag", map.get("flag"));
		return "express/codSettlement/transProvincialReceivedReconciliationInit";
	}

	/**
	 *
	 * @Title: update
	 * @description 更新方法
	 * @author 刘武强
	 * @date  2015年8月20日下午3:21:55
	 * @param  @param model
	 * @param  @param expressCodBill 需要更新的内容
	 * @param  @return
	 * @return  String
	 * @throws
	 */
	@RequestMapping("/update")
	public String update(Model model, ExpressCodBill expressCodBill) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = this.transProvincialReceivedReconciliationService.update(expressCodBill, ExpressBillTypeEnum.AcrossProvinceCodReceivableBill.getValue(), 1, Page.ONE_PAGE_NUMBER);
		model.addAttribute("infoList", map.get("list"));
		model.addAttribute("BranchprovinceId", this.transProvincialReceivedReconciliationService.getProvinceId());
		model.addAttribute("page_obj", new Page((Integer) map.get("count"), 1, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", 1);
		model.addAttribute("provincelist", this.transProvincialReceivedReconciliationService.getAllProvince(1));
		model.addAttribute("provinceNonAllList", this.transProvincialReceivedReconciliationService.getAllProvince(0));
		model.addAttribute("billStatelist", this.transProvincialReceivedReconciliationService.getExpressBillState());
		model.addAttribute("updateFlag", map.get("flag"));
		return "express/codSettlement/transProvincialReceivedReconciliationInit";
	}

	/**
	 *
	 * @Title: getOrderByProvincereceivablecodbillid
	 * @description 通过应收账单的id，查找订单数据(订单表上有个字段记录了应付账单id)
	 * @author 刘武强
	 * @date  2015年8月20日下午3:22:35
	 * @param  @param provincereceivablecodbillid 账单id
	 * @param  @param page
	 * @param  @return
	 * @return  JSONObject
	 * @throws
	 */
	@RequestMapping("/getOrderByProvincereceivablecodbillid/{page}")
	@ResponseBody
	public JSONObject getOrderByProvincereceivablecodbillid(Long provincereceivablecodbillid, @PathVariable(value = "page") long page) {
		JSONObject obj = new JSONObject();
		Map<String, Object> map = this.transProvincialReceivedReconciliationService
				.getOrderByProvincereceivablecodbillid(provincereceivablecodbillid, ExpressBillTypeEnum.AcrossProvinceCodReceivableBill.getValue(), 1, Page.ONE_PAGE_NUMBER);
		obj.put("alert_infoMap", map.get("list"));
		obj.put("alert_page_obj", new Page((Integer) map.get("count"), page, Page.ONE_PAGE_NUMBER));
		obj.put("alert_page", 1);
		return obj;
	}

	/**
	 *
	 * @Title: insertCondition
	 * @description 根据创建账单的信息，查询满足条件的订单数据
	 * @author 刘武强
	 * @date  2015年8月20日下午3:23:34
	 * @param  @param expressCodBill 创建账单的信息，
	 * @param  @param page
	 * @param  @return
	 * @return  JSONObject
	 * @throws
	 */
	@RequestMapping("/insertInfo/{page}")
	@ResponseBody
	public JSONObject insertInfo(ExpressCodBill expressCodBill, @PathVariable(value = "page") long page) {
		JSONObject obj = new JSONObject();
		Map<String, Object> map = this.transProvincialReceivedReconciliationService
				.getOrderByConditions(expressCodBill, ExpressBillTypeEnum.AcrossProvinceCodReceivableBill.getValue(), 1, Page.ONE_PAGE_NUMBER);
		obj.put("alert_infoMap", map.get("list"));
		obj.put("alert_page_obj", new Page((Integer) map.get("count"), page, Page.ONE_PAGE_NUMBER));
		obj.put("alert_page", 1);
		obj.put("money", map.get("money"));
		obj.put("count", map.get("count"));
		return obj;
	}

	/**
	 *
	 * @Title: insert
	 * @description 新增账单的方法
	 * @author 刘武强
	 * @date  2015年8月20日下午3:25:07
	 * @param  @param model
	 * @param  @param expressCodBill 新增账单信息
	 * @param  @return
	 * @return  String
	 * @throws
	 */
	@RequestMapping("/insert")
	public String insert(Model model, ExpressCodBill expressCodBill) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = this.transProvincialReceivedReconciliationService.insert(expressCodBill, ExpressBillTypeEnum.AcrossProvinceCodReceivableBill.getValue(), 1, Page.ONE_PAGE_NUMBER);
		model.addAttribute("infoList", map.get("list"));
		model.addAttribute("BranchprovinceId", this.transProvincialReceivedReconciliationService.getProvinceId());
		model.addAttribute("page_obj", new Page((Integer) map.get("count"), 1, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", 1);
		model.addAttribute("provincelist", this.transProvincialReceivedReconciliationService.getAllProvince(1));
		model.addAttribute("provinceNonAllList", this.transProvincialReceivedReconciliationService.getAllProvince(0));
		model.addAttribute("billStatelist", this.transProvincialReceivedReconciliationService.getExpressBillState());
		model.addAttribute("insertFlag", map.get("flag"));
		model.addAttribute("id", expressCodBill.getId());
		return "express/codSettlement/transProvincialReceivedReconciliationInit";
	}

}
