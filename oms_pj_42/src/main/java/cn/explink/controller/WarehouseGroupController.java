package cn.explink.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.EmailDateDAO;
import cn.explink.dao.OutWarehouseGroupDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.OutWarehouseGroup;
import cn.explink.domain.User;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.OutWarehouseGroupEnum;
import cn.explink.enumutil.OutwarehousegroupOperateEnum;
import cn.explink.service.ExcuteService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.util.Page;

@Controller
@RequestMapping("/warehousegroup")
public class WarehouseGroupController {

	@Autowired
	OutWarehouseGroupDAO outwarehousegroupDao;
	@Autowired
	BranchDAO branchDao;
	@Autowired
	CwbDAO cwbDao;
	@Autowired
	ExcuteService excuteService;

	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	EmailDateDAO emailDateDao;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	/**
	 * 查询入库信息
	 * 
	 * @param page
	 * @param model
	 * @param userid
	 * @param beginemaildate
	 * @param endemaildate
	 * @return
	 */
	@RequestMapping("/inlist/{page}")
	public String inlist(@PathVariable("page") long page, Model model, @RequestParam(value = "userid", required = false, defaultValue = "0") long userid,
			@RequestParam(value = "beginemaildate", required = false, defaultValue = "") String beginemaildate,
			@RequestParam(value = "endemaildate", required = false, defaultValue = "") String endemaildate) {
		List<OutWarehouseGroup> owgAllList = null;
		// if(userid!=0){
		owgAllList = outwarehousegroupDao.getOutWarehouseGroupByPage(page, 0, beginemaildate, endemaildate, userid, OutwarehousegroupOperateEnum.RuKu.getValue());
		// }
		model.addAttribute("owgList", owgAllList);
		model.addAttribute("driverList", userDAO.getUserByRole(3));
		model.addAttribute("page_obj", new Page(outwarehousegroupDao.getOutWarehouseGroupCount(0, beginemaildate, endemaildate, userid, OutwarehousegroupOperateEnum.RuKu.getValue()), page,
				Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "warehousegroup/inlist";
	}

	/**
	 * 入库交接单信息打印和查询
	 * 
	 * @param model
	 * @param outwarehousegroupid
	 * @return
	 */
	@RequestMapping("/inlistprint/{outwarehousegroupid}")
	public String inlistprint(Model model, @PathVariable("outwarehousegroupid") long outwarehousegroupid) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String datetime = df.format(date);
		outwarehousegroupDao.savePrinttime(datetime, outwarehousegroupid);
		model.addAttribute("cwborderlist", cwbDao.getCwbByGroupid(outwarehousegroupid));
		model.addAttribute("branchname", branchDAO.getBranchByBranchid(getSessionUser().getBranchid()).getBranchname());
		return "warehousegroup/inbillprinting_default";
	}

	/**
	 * 出库信息查询
	 * 
	 * @param model
	 * @param page
	 * @param branchid
	 * @param beginemaildate
	 * @param endemaildate
	 * @return
	 */
	@RequestMapping("/outlist/{page}")
	public String outlist(Model model, @PathVariable("page") long page, @RequestParam(value = "branchid", required = false, defaultValue = "0") long branchid,
			@RequestParam(value = "beginemaildate", required = false, defaultValue = "") String beginemaildate,
			@RequestParam(value = "endemaildate", required = false, defaultValue = "") String endemaildate) {
		model.addAttribute("outwarehousegroupList", outwarehousegroupDao.getOutWarehouseGroupByPage(page, branchid, beginemaildate, endemaildate, 0, OutwarehousegroupOperateEnum.ChuKu.getValue()));
		model.addAttribute("branchList", branchDao.getAllBranches());
		model.addAttribute("page_obj", new Page(outwarehousegroupDao.getOutWarehouseGroupCount(branchid, beginemaildate, endemaildate, 0, OutwarehousegroupOperateEnum.ChuKu.getValue()), page,
				Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "warehousegroup/outlist";
	}

	/**
	 * 出库交接单信息打印和查询
	 * 
	 * @param model
	 * @param outwarehousegroupid
	 * @return
	 */
	@RequestMapping("/outbillprinting_default/{outwarehousegroupid}")
	public String outbillprinting_default(Model model, @PathVariable("outwarehousegroupid") long outwarehousegroupid) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String datetime = df.format(date);
		outwarehousegroupDao.savePrinttime(datetime, outwarehousegroupid);
		model.addAttribute("cwborderlist", cwbDao.getCwbByGroupid(outwarehousegroupid));
		model.addAttribute("branchname", branchDAO.getBranchByBranchid(outwarehousegroupDao.getOutWarehouseGroupByid(outwarehousegroupid).getBranchid()).getBranchname());
		return "warehousegroup/outbillprinting_default";
	}

	/**
	 * 分站到货信息查询
	 * 
	 * @param model
	 * @param page
	 * @param userid
	 * @return
	 */
	@RequestMapping("/inboxlist/{page}")
	public String inboxPrint(Model model, @PathVariable("page") long page, @RequestParam(value = "userid", required = false, defaultValue = "0") long userid) {
		List<OutWarehouseGroup> owgAllList = null;
		model.addAttribute("driverList", userDAO.getUserByRole(3));
		// if(userid!=0){
		owgAllList = outwarehousegroupDao.getOutWarehouseGroupByPage(page, getSessionUser().getBranchid(), "", "", userid, OutwarehousegroupOperateEnum.FenZhanDaoHuo.getValue());
		// }

		model.addAttribute("owgAllList", owgAllList);
		model.addAttribute("page_obj", new Page(outwarehousegroupDao.getOutWarehouseGroupCount(getSessionUser().getBranchid(), "", "", userid, OutwarehousegroupOperateEnum.FenZhanDaoHuo.getValue()),
				page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "warehousegroup/inboxlist";
	}

	/**
	 * 分站到货交接单信息打印和查询
	 * 
	 * @param outwarehousegroupid
	 * @param model
	 * @return
	 */
	@RequestMapping("/inboxsearchAndPrint/{outwarehousegroupid}")
	public String searchDetail(@PathVariable("outwarehousegroupid") long outwarehousegroupid, Model model) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String datetime = df.format(date);
		outwarehousegroupDao.savePrinttime(datetime, outwarehousegroupid);

		model.addAttribute("branchname", branchDAO.getBranchByBranchid(getSessionUser().getBranchid()).getBranchname());
		model.addAttribute("cwbAllDetail", cwbDao.getCwbByGroupid(outwarehousegroupid));
		return "warehousegroup/inboxbillprinting_default";
	}

	/**
	 * 小件员领货信息查询
	 * 
	 * @param model
	 * @param page
	 * @param userid
	 * @return
	 */
	@RequestMapping("/deliverlist/{page}")
	public String deliverList(Model model, @PathVariable("page") long page, @RequestParam(value = "userid", required = false, defaultValue = "0") long userid) {
		model.addAttribute("deliverList", userDAO.getUserByRole(2));

		List<OutWarehouseGroup> owgAllList = null;
		// if(userid!=0){
		owgAllList = outwarehousegroupDao.getOutWarehouseGroupByPage(page, getSessionUser().getBranchid(), "", "", userid, OutwarehousegroupOperateEnum.FenZhanLingHuo.getValue());
		// }
		model.addAttribute("owgAllList", owgAllList);
		model.addAttribute("page_obj", new Page(outwarehousegroupDao.getOutWarehouseGroupCount(getSessionUser().getBranchid(), "", "", userid, OutwarehousegroupOperateEnum.FenZhanLingHuo.getValue()),
				page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);

		return "warehousegroup/deliverlist";

	}

	/**
	 * 小件员领货交接单信息打印和查询
	 * 
	 * @param outwarehousegroupid
	 * @param model
	 * @return
	 */
	@RequestMapping("/searchAndPrintFordeliver/{outwarehousegroupid}")
	public String searchDetailToDeliver(@PathVariable("outwarehousegroupid") long outwarehousegroupid, Model model) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String datetime = df.format(date);
		outwarehousegroupDao.savePrinttimeAndState(datetime, OutWarehouseGroupEnum.FengBao.getValue(), outwarehousegroupid);

		model.addAttribute("branchname", branchDAO.getBranchByBranchid(getSessionUser().getBranchid()).getBranchname());
		model.addAttribute("cwbAllDetail", cwbDao.getCwbByGroupid(outwarehousegroupid));
		return "warehousegroup/deliverbillprinting_default";
	}

	/**
	 * 中转出库查询
	 * 
	 * @param model
	 * @param page
	 * @param branchid
	 * @param beginemaildate
	 * @param endemaildate
	 * @return
	 */
	@RequestMapping("/changelist/{page}")
	public String changelist(Model model, @PathVariable("page") long page, @RequestParam(value = "branchid", required = false, defaultValue = "0") long branchid,
			@RequestParam(value = "beginemaildate", required = false, defaultValue = "") String beginemaildate,
			@RequestParam(value = "endemaildate", required = false, defaultValue = "") String endemaildate) {
		model.addAttribute("outwarehousegroupList",
				outwarehousegroupDao.getOutWarehouseGroupByPage(page, branchid, beginemaildate, endemaildate, 0, OutwarehousegroupOperateEnum.ZhongZhuanChuKu.getValue()));
		model.addAttribute("branchList", branchDAO.getBranchBySiteType(BranchEnum.ZhongZhuan.getValue()));
		model.addAttribute("page_obj", new Page(outwarehousegroupDao.getOutWarehouseGroupCount(branchid, beginemaildate, endemaildate, 0, OutwarehousegroupOperateEnum.ZhongZhuanChuKu.getValue()),
				page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "warehousegroup/changelist";
	}

	/**
	 * 中转出库交接单打印和查询
	 * 
	 * @param model
	 * @param outwarehousegroupid
	 * @return
	 */
	@RequestMapping("/changebillprinting_default/{outwarehousegroupid}")
	public String changebillprinting_default(Model model, @PathVariable("outwarehousegroupid") long outwarehousegroupid) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String datetime = df.format(date);
		outwarehousegroupDao.savePrinttime(datetime, outwarehousegroupid);
		model.addAttribute("cwborderlist", cwbDao.getCwbByGroupid(outwarehousegroupid));
		model.addAttribute("branchname", branchDAO.getBranchByBranchid(outwarehousegroupDao.getOutWarehouseGroupByid(outwarehousegroupid).getBranchid()).getBranchname());
		return "warehousegroup/changebillprinting_default";
	}

	/**
	 * 退货出库信息查询
	 * 
	 * @param model
	 * @param page
	 * @param branchid
	 * @param beginemaildate
	 * @param endemaildate
	 * @return
	 */
	@RequestMapping("/returnlist/{page}")
	public String returnlist(Model model, @PathVariable("page") long page, @RequestParam(value = "branchid", required = false, defaultValue = "0") long branchid,
			@RequestParam(value = "beginemaildate", required = false, defaultValue = "") String beginemaildate,
			@RequestParam(value = "endemaildate", required = false, defaultValue = "") String endemaildate) {
		model.addAttribute("outwarehousegroupList",
				outwarehousegroupDao.getOutWarehouseGroupByPage(page, branchid, beginemaildate, endemaildate, 0, OutwarehousegroupOperateEnum.TuiHuoChuKu.getValue()));
		model.addAttribute("branchList", branchDAO.getBranchBySiteType(BranchEnum.TuiHuo.getValue()));
		model.addAttribute("page_obj", new Page(outwarehousegroupDao.getOutWarehouseGroupCount(branchid, beginemaildate, endemaildate, 0, OutwarehousegroupOperateEnum.TuiHuoChuKu.getValue()), page,
				Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "warehousegroup/returnlist";
	}

	/**
	 * 退货出库交接单打印和查询
	 * 
	 * @param model
	 * @param outwarehousegroupid
	 * @return
	 */
	@RequestMapping("/returnbillprinting_default/{outwarehousegroupid}")
	public String returnbillprinting_default(Model model, @PathVariable("outwarehousegroupid") long outwarehousegroupid) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String datetime = df.format(date);
		outwarehousegroupDao.savePrinttime(datetime, outwarehousegroupid);
		model.addAttribute("cwborderlist", cwbDao.getCwbByGroupid(outwarehousegroupid));
		model.addAttribute("branchname", branchDAO.getBranchByBranchid(outwarehousegroupDao.getOutWarehouseGroupByid(outwarehousegroupid).getBranchid()).getBranchname());
		return "warehousegroup/returnbillprinting_default";
	}

	/**
	 * 中转站入库查询
	 * 
	 * @param page
	 * @param model
	 * @param userid
	 * @param beginemaildate
	 * @param endemaildate
	 * @return
	 */
	@RequestMapping("/changeinlist/{page}")
	public String changeinlist(@PathVariable("page") long page, Model model, @RequestParam(value = "userid", required = false, defaultValue = "0") long userid,
			@RequestParam(value = "beginemaildate", required = false, defaultValue = "") String beginemaildate,
			@RequestParam(value = "endemaildate", required = false, defaultValue = "") String endemaildate) {
		List<OutWarehouseGroup> owgAllList = null;
		// if(userid!=0){
		owgAllList = outwarehousegroupDao.getOutWarehouseGroupByPage(page, 0, beginemaildate, endemaildate, userid, OutwarehousegroupOperateEnum.ZhongZhuanZhanRuKu.getValue());
		// }
		model.addAttribute("owgList", owgAllList);
		model.addAttribute("driverList", userDAO.getUserByRole(3));
		model.addAttribute("page_obj", new Page(outwarehousegroupDao.getOutWarehouseGroupCount(0, beginemaildate, endemaildate, userid, OutwarehousegroupOperateEnum.ZhongZhuanZhanRuKu.getValue()),
				page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "warehousegroup/changeinlist";
	}

	/**
	 * 中转站入库交接单打印和查询
	 * 
	 * @param model
	 * @param outwarehousegroupid
	 * @return
	 */
	@RequestMapping("/changeinlistprint/{outwarehousegroupid}")
	public String changeinlistprint(Model model, @PathVariable("outwarehousegroupid") long outwarehousegroupid) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String datetime = df.format(date);
		outwarehousegroupDao.savePrinttime(datetime, outwarehousegroupid);
		model.addAttribute("cwborderlist", cwbDao.getCwbByGroupid(outwarehousegroupid));
		model.addAttribute("branchname", branchDAO.getBranchByBranchid(getSessionUser().getBranchid()).getBranchname());
		return "warehousegroup/changeinbillprinting_default";
	}

	/**
	 * 中转站出库查询
	 * 
	 * @param model
	 * @param page
	 * @param branchid
	 * @param beginemaildate
	 * @param endemaildate
	 * @return
	 */
	@RequestMapping("/transbranchoutlist/{page}")
	public String transbranchoutlist(Model model, @PathVariable("page") long page, @RequestParam(value = "branchid", required = false, defaultValue = "0") long branchid,
			@RequestParam(value = "beginemaildate", required = false, defaultValue = "") String beginemaildate,
			@RequestParam(value = "endemaildate", required = false, defaultValue = "") String endemaildate) {
		model.addAttribute("outwarehousegroupList",
				outwarehousegroupDao.getOutWarehouseGroupByPage(page, branchid, beginemaildate, endemaildate, 0, OutwarehousegroupOperateEnum.ZhongZhuanZhanChuKu.getValue()));
		model.addAttribute("branchList", branchDao.getAllBranches());
		model.addAttribute("page_obj", new Page(outwarehousegroupDao.getOutWarehouseGroupCount(branchid, beginemaildate, endemaildate, 0, OutwarehousegroupOperateEnum.ZhongZhuanZhanChuKu.getValue()),
				page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "warehousegroup/transbranchoutlist";
	}

	/**
	 * 中转站出库信息交接单打印和查询
	 * 
	 * @param model
	 * @param outwarehousegroupid
	 * @return
	 */
	@RequestMapping("/transbranchoutbillprinting_default/{outwarehousegroupid}")
	public String transbranchoutbillprinting_default(Model model, @PathVariable("outwarehousegroupid") long outwarehousegroupid) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String datetime = df.format(date);
		outwarehousegroupDao.savePrinttime(datetime, outwarehousegroupid);
		model.addAttribute("cwborderlist", cwbDao.getCwbByGroupid(outwarehousegroupid));
		model.addAttribute("branchname", branchDAO.getBranchByBranchid(outwarehousegroupDao.getOutWarehouseGroupByid(outwarehousegroupid).getBranchid()).getBranchname());
		return "warehousegroup/transbranchoutbillprinting_default";
	}

	/**
	 * 退货站入库查询
	 * 
	 * @param page
	 * @param model
	 * @param userid
	 * @param beginemaildate
	 * @param endemaildate
	 * @return
	 */
	@RequestMapping("/backinlist/{page}")
	public String backinlist(@PathVariable("page") long page, Model model, @RequestParam(value = "userid", required = false, defaultValue = "0") long userid,
			@RequestParam(value = "beginemaildate", required = false, defaultValue = "") String beginemaildate,
			@RequestParam(value = "endemaildate", required = false, defaultValue = "") String endemaildate) {
		List<OutWarehouseGroup> owgAllList = null;
		// if(userid!=0){
		owgAllList = outwarehousegroupDao.getOutWarehouseGroupByPage(page, 0, beginemaildate, endemaildate, userid, OutwarehousegroupOperateEnum.TuiHuoZhanRuku.getValue());
		// }
		model.addAttribute("owgList", owgAllList);
		model.addAttribute("driverList", userDAO.getUserByRole(3));
		model.addAttribute("page_obj", new Page(outwarehousegroupDao.getOutWarehouseGroupCount(0, beginemaildate, endemaildate, userid, OutwarehousegroupOperateEnum.TuiHuoZhanRuku.getValue()), page,
				Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "warehousegroup/backinlist";
	}

	/**
	 * 退货站入库交接单打印和查询
	 * 
	 * @param model
	 * @param outwarehousegroupid
	 * @return
	 */
	@RequestMapping("/backinlistprint/{outwarehousegroupid}")
	public String backinlistprint(Model model, @PathVariable("outwarehousegroupid") long outwarehousegroupid) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String datetime = df.format(date);
		outwarehousegroupDao.savePrinttime(datetime, outwarehousegroupid);
		model.addAttribute("cwborderlist", cwbDao.getCwbByGroupid(outwarehousegroupid));
		model.addAttribute("branchname", branchDAO.getBranchByBranchid(getSessionUser().getBranchid()).getBranchname());
		return "warehousegroup/backinbillprinting_default";
	}

	/**
	 * 退货站再投查询
	 * 
	 * @param model
	 * @param page
	 * @param branchid
	 * @param beginemaildate
	 * @param endemaildate
	 * @return
	 */
	@RequestMapping("/returnbranchoutlist/{page}")
	public String returnbranchoutlist(Model model, @PathVariable("page") long page, @RequestParam(value = "branchid", required = false, defaultValue = "0") long branchid,
			@RequestParam(value = "beginemaildate", required = false, defaultValue = "") String beginemaildate,
			@RequestParam(value = "endemaildate", required = false, defaultValue = "") String endemaildate) {
		model.addAttribute("outwarehousegroupList",
				outwarehousegroupDao.getOutWarehouseGroupByPage(page, branchid, beginemaildate, endemaildate, 0, OutwarehousegroupOperateEnum.TuiHuoZhanZaiTou.getValue()));
		model.addAttribute("branchList", branchDao.getAllBranches());
		model.addAttribute("page_obj", new Page(outwarehousegroupDao.getOutWarehouseGroupCount(branchid, beginemaildate, endemaildate, 0, OutwarehousegroupOperateEnum.TuiHuoZhanZaiTou.getValue()),
				page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "warehousegroup/returnbranchoutlist";
	}

	/**
	 * 退货站再投信息交接单打印和查询
	 * 
	 * @param model
	 * @param outwarehousegroupid
	 * @return
	 */
	@RequestMapping("/returnbranchoutbillprinting_default/{outwarehousegroupid}")
	public String returnbranchoutbillprinting_default(Model model, @PathVariable("outwarehousegroupid") long outwarehousegroupid) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String datetime = df.format(date);
		outwarehousegroupDao.savePrinttime(datetime, outwarehousegroupid);
		model.addAttribute("cwborderlist", cwbDao.getCwbByGroupid(outwarehousegroupid));
		model.addAttribute("branchname", branchDAO.getBranchByBranchid(outwarehousegroupDao.getOutWarehouseGroupByid(outwarehousegroupid).getBranchid()).getBranchname());
		return "warehousegroup/returnbranchoutbillprinting_default";
	}
}
