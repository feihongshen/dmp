package cn.explink.controller.express;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.explink.domain.express.ExpressCodBill;
import cn.explink.enumutil.express.ExpressBillTypeEnum;
import cn.explink.service.express.TransProvincialReceivedReconciliationService;
import cn.explink.util.Page;

/**
 * 跨省代收货款审核（应收）的controller
 *
 * @author songkaojun 2015年8月14日
 */
@Controller
@RequestMapping("/transProvincialAuditReconciliationController")
public class TransProvincialAuditReconciliationController extends ExpressCommonController {
	@Autowired
	private TransProvincialReceivedReconciliationService transProvincialReceivedReconciliationService;

	/**
	 * 跨省代收货款对账（应收）功能的初始化方法
	 *
	 * @param model
	 * @param billNo
	 * @param billState
	 * @param creatStart
	 * @param creatEnd
	 * @param cavStart
	 * @param cavEnd
	 * @param provinceId
	 * @param sequenceField
	 * @param ascOrDesc
	 * @param page
	 * @return
	 */
	@RequestMapping("/init")
	public String init(Model model) {
		model.addAttribute("infoList", new ArrayList<ExpressCodBill>());
		model.addAttribute("page_obj", new Page(0, 0, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", 0);
		model.addAttribute("provincelist", this.transProvincialReceivedReconciliationService.getAllProvince(1));
		model.addAttribute("provinceNonAllList", this.transProvincialReceivedReconciliationService.getAllProvince(0));
		model.addAttribute("billStatelist", this.transProvincialReceivedReconciliationService.getExpressBillState());
		return "express/codSettlement/transProvincialAuditReconciliationInit";
	}

	/**
	 *
	 * @Title: search
	 * @description 根据条件返回查看数据
	 * @author 刘武强
	 * @date  2015年8月19日上午10:11:01
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
		model.addAttribute("page_obj", new Page((Integer) map.get("count"), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		model.addAttribute("provincelist", this.transProvincialReceivedReconciliationService.getAllProvince(1));
		model.addAttribute("provinceNonAllList", this.transProvincialReceivedReconciliationService.getAllProvince(0));
		model.addAttribute("billStatelist", this.transProvincialReceivedReconciliationService.getExpressBillState());
		return "express/codSettlement/transProvincialAuditReconciliationInit";
	}

	/**
	 *
	 * @Title: audit
	 * @description 根据账单id,审核账单
	 * @author 刘武强
	 * @date  2015年8月19日上午11:39:29
	 * @param  @param model
	 * @param  @param id
	 * @param  @param auditOrCancalAuditOrVerificated: "audit":审核   "cancalAudit":取消审核
	 * @param  @return
	 * @return  String
	 * @throws
	 */
	@RequestMapping("/auditOrCancalAuditOrVerificated")
	public String auditOrCancalAuditOrVerificated(Model model, Long id, String auditOrCancalAuditOrVerificated) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = this.transProvincialReceivedReconciliationService
				.auditOrCancalAuditOrVerificated(id, ExpressBillTypeEnum.AcrossProvinceCodReceivableBill.getValue(), 1, Page.ONE_PAGE_NUMBER, auditOrCancalAuditOrVerificated);
		model.addAttribute("infoList", map.get("list"));
		model.addAttribute("page_obj", new Page((Integer) map.get("count"), 1, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", 1);
		model.addAttribute("provincelist", this.transProvincialReceivedReconciliationService.getAllProvince(1));
		model.addAttribute("provinceNonAllList", this.transProvincialReceivedReconciliationService.getAllProvince(0));
		model.addAttribute("billStatelist", this.transProvincialReceivedReconciliationService.getExpressBillState());
		model.addAttribute("Flag", map.get("flag"));
		return "express/codSettlement/transProvincialAuditReconciliationInit";
	}

	/**
	 *
	 * @Title: export
	 * @description TODO
	 * @author 刘武强
	 * @date  2015年8月19日下午2:46:36
	 * @param  @param provincereceivablecodbillid
	 * @param  @param page
	 * @param  @param request
	 * @param  @param response
	 * @param  @throws Exception
	 * @return  void
	 * @throws
	 */
	@RequestMapping("/exportBill")
	public void exportBill(Long provincereceivablecodbillid, Integer page, HttpServletRequest request, HttpServletResponse response) throws Exception {
		this.transProvincialReceivedReconciliationService.exportBill(provincereceivablecodbillid, page, request, response);
	}
}
