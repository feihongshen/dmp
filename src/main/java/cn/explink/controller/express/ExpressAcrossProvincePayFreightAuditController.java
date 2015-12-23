package cn.explink.controller.express;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.domain.VO.express.ExportBillCwb;
import cn.explink.domain.VO.express.ExpressCwbOrderForTakeGoodsQueryVO;
import cn.explink.domain.VO.express.ExpressFreightAuditBillVO;
import cn.explink.domain.VO.express.SelectReturnVO;
import cn.explink.service.express.ExpressAcrossProvincePayFreightAuditService;
import cn.explink.util.ExportUtil4Order;
import cn.explink.util.Page;

/**
 * 跨省到付运费审核（应付）controller
 * @author wangzy
 *
 */
@Controller
@RequestMapping("/acrossProvincePayFreightAudit")
public class ExpressAcrossProvincePayFreightAuditController extends ExpressCommonController{

	@Autowired
	ExpressAcrossProvincePayFreightAuditService expressAcrossProvinceFreightAudit;
	
	
	/**
	 * 进入跨省到付运费审核页面
	 * @author 王志宇
	 * @return
	 */
	@RequestMapping("/toAcrossProvincePayFreightAudit/{page}")
	public String takeGoodsQuery(@PathVariable("page") long page,ExpressFreightAuditBillVO expressFreightAuditBillVO,Model model) {
		model.addAttribute("page_obj", new Page(expressAcrossProvinceFreightAudit.getBillCount(expressFreightAuditBillVO), page,Page.ONE_PAGE_NUMBER));
		model.addAttribute("expressFreightAuditBillVO",expressFreightAuditBillVO);
		model.addAttribute("billList",expressAcrossProvinceFreightAudit.queryBillForFreight(page,expressFreightAuditBillVO));
		return "express/acrossProvinceFreight/acrossProvincePayFreightAudit";
	}
	
	/**
	 * 编辑页面的数据回写
	 * @author 王志宇
	 */
	@RequestMapping("/edit")
	@ResponseBody
	public ExpressFreightAuditBillVO edit(Long id){
		return expressAcrossProvinceFreightAudit.edit(id);
	}

	/**
	 * 查询编辑页面的账单表条目的详细订单
	 * @author 王志宇
	 */
	@RequestMapping("/orderDetailsForBill")
	@ResponseBody
	public List<ExpressCwbOrderForTakeGoodsQueryVO>  orderDetailsForBill(Long id){
		return expressAcrossProvinceFreightAudit.orderDetailsForBill(id);
	}

	/**
	 * 获得编辑页面的账单条目的详细订单-----分页
	 * @author 王志宇
	 */
	@RequestMapping("/orderDetailsPageForBill")
	@ResponseBody
	public cn.explink.util.express.Page<ExpressCwbOrderForTakeGoodsQueryVO> orderDetailsPageForBill(Long id,Integer pageNo){
		cn.explink.util.express.Page<ExpressCwbOrderForTakeGoodsQueryVO> page = new cn.explink.util.express.Page<ExpressCwbOrderForTakeGoodsQueryVO>();
		List<ExpressCwbOrderForTakeGoodsQueryVO> list = expressAcrossProvinceFreightAudit.orderDetailsForBill(id);
		List<ExpressCwbOrderForTakeGoodsQueryVO> listNow = expressAcrossProvinceFreightAudit.orderDetailsForBillByPage(id,pageNo);
		page.setList(listNow);
		if(pageNo==null){
			page.setPageNo(1);
		}else{
			page.setPageNo(pageNo);			
		}
		page.setPageSize(Page.ONE_PAGE_NUMBER);
		page.setTotalCount(list.size());
		return page;
	}

	/**
	 * 获得省份（不包含自己的所有省份）
	 * @author 王志宇
	 */
	@RequestMapping("/getProvinces")
	@ResponseBody
	public List<SelectReturnVO> getProvinces(){
		return expressAcrossProvinceFreightAudit.getProvinces();
	}
	
	/**
	 * 审核
	 * @author 王志宇
	 */
	@RequestMapping("/audit")
	@ResponseBody
	public int audit(Long id,String state){
		return expressAcrossProvinceFreightAudit.audit(id,state);
	}
	
	/**
	 * 取消审核
	 * @author 王志宇
	 */
	@RequestMapping("/cancelAudit")
	@ResponseBody
	public int cancelAudit(Long id,String state){
		return expressAcrossProvinceFreightAudit.cancelAudit(id, state);
	}
	
	/**
	 * 核销
	 * @author 王志宇
	 */
	@RequestMapping("/checkAudit")
	@ResponseBody
	public int checkAudit(Long id,String state){
		return expressAcrossProvinceFreightAudit.checkAudit(id, state);
	}
	
	/**
	 * 导出
	 * @author 王志宇
	 */
	@RequestMapping("/exportBill")
	public void export(Long id,Integer pageNo,HttpServletRequest request, HttpServletResponse response) throws Exception{
		List<ExportBillCwb> listNow = expressAcrossProvinceFreightAudit.orderDetailsForBillByPageForExport(id,pageNo);
		String fileName = "账单明细";
		ExportUtil4Order.myExportXls(request, response, listNow, ExportBillCwb.class, fileName);
	}
}
