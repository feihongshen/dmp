package cn.explink.controller.express;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.express.ExpressFreightBillSelectDAO;
import cn.explink.domain.VO.express.ExpressCwbOrderForTakeGoodsQueryVO;
import cn.explink.domain.VO.express.ExpressFreightAuditBillVO;
import cn.explink.domain.VO.express.SelectReturnVO;
import cn.explink.service.express.ExpressCustomerFreightAuditService;
import cn.explink.util.Page;

/**
 * 客户运费审核controller
 * @author wangzy
 *
 */
@Controller
@RequestMapping("/customerFreightAudit")
public class ExpressCustomerFreightAuditController extends ExpressCommonController{

	@Autowired
	ExpressCustomerFreightAuditService expressCustomerFreightAudit;
	
	
	/**
	 * 进入客户运费审核页面
	 * @author 王志宇
	 * @return
	 */
	@RequestMapping("/toCustomerFreightAudit/{page}")
	public String takeGoodsQuery(@PathVariable("page") long page,ExpressFreightAuditBillVO expressFreightAuditBillVO,Model model) {
		model.addAttribute("page_obj", new Page(expressCustomerFreightAudit.getBillCount(expressFreightAuditBillVO), page,Page.ONE_PAGE_NUMBER));
		model.addAttribute("expressFreightAuditBillVO",expressFreightAuditBillVO);
		model.addAttribute("billList",expressCustomerFreightAudit.queryBillForFreight(page,expressFreightAuditBillVO));
		return "express/customerfreight/customerfreightAudit";
	}
	
	/**
	 * 编辑页面的数据回写
	 * @author 王志宇
	 */
	@RequestMapping("/edit")
	@ResponseBody
	public ExpressFreightAuditBillVO edit(Long id){
		return expressCustomerFreightAudit.edit(id);
	}
	
	/**
	 * 查询编辑页面的账单表条目的详细订单
	 * @author 王志宇
	 */
	@RequestMapping("/orderDetailsForBill")
	@ResponseBody
	public List<ExpressCwbOrderForTakeGoodsQueryVO>  orderDetailsForBill(Long id){
		return expressCustomerFreightAudit.orderDetailsForBill(id);
	}

	/**
	 * 获得编辑页面的账单条目的详细订单-----分页
	 * @author 王志宇
	 */
	@RequestMapping("/orderDetailsPageForBill")
	@ResponseBody
	public cn.explink.util.express.Page<ExpressCwbOrderForTakeGoodsQueryVO> orderDetailsPageForBill(Long id,Integer pageNo){
		cn.explink.util.express.Page<ExpressCwbOrderForTakeGoodsQueryVO> page = new cn.explink.util.express.Page<ExpressCwbOrderForTakeGoodsQueryVO>();
		List<ExpressCwbOrderForTakeGoodsQueryVO> list = expressCustomerFreightAudit.orderDetailsForBill(id);
		List<ExpressCwbOrderForTakeGoodsQueryVO> listNow = expressCustomerFreightAudit.orderDetailsForBillByPage(id,pageNo);
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
	 * 获得客户（非月结的客户）
	 * @author 王志宇
	 */
	@RequestMapping("/getCustomers")
	@ResponseBody
	public List<SelectReturnVO> getCustomers(){
		return expressCustomerFreightAudit.getCustomers();
	}
	
	/**
	 * 审核
	 * @author 王志宇
	 */
	@RequestMapping("/audit")
	@ResponseBody
	public int audit(Long id,String state){
		return expressCustomerFreightAudit.audit(id,state);
	}
	
	/**
	 * 取消审核
	 * @author 王志宇
	 */
	@RequestMapping("/cancelAudit")
	@ResponseBody
	public int cancelAudit(Long id,String state){
		return expressCustomerFreightAudit.cancelAudit(id, state);
	}
	
	/**
	 * 核销
	 * @author 王志宇
	 */
	@RequestMapping("/checkAudit")
	@ResponseBody
	public int checkAudit(Long id,String state){
		return expressCustomerFreightAudit.checkAudit(id, state);
	}
	
}
