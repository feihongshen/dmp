package cn.explink.controller.express;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.domain.VO.express.ExpressCwbOrderForTakeGoodsQueryVO;
import cn.explink.domain.VO.express.ExpressFreightAuditBillVO;
import cn.explink.domain.VO.express.SelectReturnVO;
import cn.explink.domain.express.ExpressFreightBill;
import cn.explink.service.express.ExpressStationFreightAuditService;
import cn.explink.util.Page;

/**
 * 站点运费审核controller
 * @author wangzy
 *
 */
@Controller
@RequestMapping("/stationFreightAudit")
public class ExpressStationFreightAuditController extends ExpressCommonController{

	@Autowired
	ExpressStationFreightAuditService expressStationFreightAudit;
	
	
	/**
	 * 进入站点运费审核页面
	 * @author 王志宇
	 * @return
	 */
	@RequestMapping("/toStationFreightAudit/{page}")
	public String takeGoodsQuery(@PathVariable("page") long page,ExpressFreightAuditBillVO expressFreightAuditBillVO,Model model) {
		model.addAttribute("page_obj", new Page(expressStationFreightAudit.getBillCount(expressFreightAuditBillVO), page,Page.ONE_PAGE_NUMBER));
		model.addAttribute("expressFreightAuditBillVO",expressFreightAuditBillVO);
		model.addAttribute("billList",expressStationFreightAudit.queryBillForFreight(page,expressFreightAuditBillVO));
		return "express/stationfreight/stationfreightAudit";
	}
	
	/**
	 * 编辑页面的数据回写
	 * @author 王志宇
	 */
	@RequestMapping("/edit")
	@ResponseBody
	public ExpressFreightAuditBillVO edit(Long id){
		return expressStationFreightAudit.edit(id);
	}
	
	/**
	 * 查询编辑页面的账单表条目的详细订单
	 * @author 王志宇
	 */
	@RequestMapping("/orderDetailsForBill")
	@ResponseBody
	public List<ExpressCwbOrderForTakeGoodsQueryVO>  orderDetailsForBill(Long id){
		return expressStationFreightAudit.orderDetailsForBill(id);
	}

	/**
	 * 获得编辑页面的账单条目的详细订单-----分页
	 * @author 王志宇
	 */
	@RequestMapping("/orderDetailsPageForBill")
	@ResponseBody
	public cn.explink.util.express.Page<ExpressCwbOrderForTakeGoodsQueryVO> orderDetailsPageForBill(Long id,Integer pageNo){
		cn.explink.util.express.Page<ExpressCwbOrderForTakeGoodsQueryVO> page = new cn.explink.util.express.Page<ExpressCwbOrderForTakeGoodsQueryVO>();
		List<ExpressCwbOrderForTakeGoodsQueryVO> list = expressStationFreightAudit.orderDetailsForBill(id);
		List<ExpressCwbOrderForTakeGoodsQueryVO> listNow = expressStationFreightAudit.orderDetailsForBillByPage(id,pageNo);
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
	 * 获得所有站点（站点下的所有省份）
	 * @author 王志宇
	 */
	@RequestMapping("/getStations")
	@ResponseBody
	public List<SelectReturnVO> getStations(){
		return expressStationFreightAudit.getStations();
	}
	
	/**
	 * 审核
	 * @author 王志宇
	 */
	@RequestMapping("/audit")
	@ResponseBody
	public int audit(Long id,String state){
		return expressStationFreightAudit.audit(id,state);
	}
	
	/**
	 * 取消审核
	 * @author 王志宇
	 */
	@RequestMapping("/cancelAudit")
	@ResponseBody
	public int cancelAudit(Long id,String state){
		return expressStationFreightAudit.cancelAudit(id, state);

	}
	
	/**
	 * 核销
	 * @author 王志宇
	 */
	@RequestMapping("/checkAudit")
	@ResponseBody
	public int checkAudit(Long id,String state){
		return expressStationFreightAudit.checkAudit(id, state);
	}
	
}
