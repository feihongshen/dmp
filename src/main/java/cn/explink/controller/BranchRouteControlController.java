package cn.explink.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.BranchRouteDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.BranchRoute;
import cn.explink.domain.User;
import cn.explink.enumutil.BranchRouteEnum;
import cn.explink.service.CwbRouteService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.util.Page;

@Controller
@RequestMapping("/branchRouteControl")
public class BranchRouteControlController {

	@Autowired
	BranchDAO branchDAO;
	@Autowired
	BranchRouteDAO branchRouteDAO;
	@Autowired
	CwbRouteService cwbRouteService;
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	@RequestMapping("/add")
	public String add(Model model) throws Exception {
		model.addAttribute("branchlist", branchDAO.getAllEffectBranches());
		return "branchroute/add";
	}

	@RequestMapping("/create")
	public @ResponseBody String create(@RequestParam(value = "fromBranchId", required = false, defaultValue = "0") long fromBranchId,
			@RequestParam(value = "toBranchId", required = false, defaultValue = "") String[] toBranchId, @RequestParam(value = "type", required = false, defaultValue = "0") int type) throws Exception {
		int flag=0;
		StringBuilder sb = new StringBuilder();
		String msg=null;
		for( String tempToBranchId : toBranchId){
			List<BranchRoute> brlist = branchRouteDAO.getBranchRouteByWheresql(fromBranchId, Long.valueOf(tempToBranchId), type);
			if (brlist.size() > 0) {				
				Branch branch=branchDAO.getBranchByBranchid(Long.valueOf(tempToBranchId));
				/*msg+=branch.getBranchname()+",";*/
				sb.append(branch.getBranchname()+",");
				msg=sb.substring(0,sb.length()-1);
				flag=1;
				continue;
			} else {
				branchRouteDAO.creBranchRoute(fromBranchId, Long.valueOf(tempToBranchId), type);
				cwbRouteService.reload();
				logger.info("operatorUser={},货物流向设置->create", getSessionUser().getUsername());
			}
			
		}
		if(flag==1){
			return "{\"errorCode\":0,\"error\":\"操作成功,已过滤重复数据 "+msg+"\"}"; 
		}
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	@RequestMapping("/list/{page}")
	public String list(@PathVariable("page") long page, Model model, @RequestParam(value = "fromBranchId", required = false, defaultValue = "0") long fromBranchId,
			@RequestParam(value = "toBranchId", required = false, defaultValue = "0") long toBranchId, @RequestParam(value = "type", required = false, defaultValue = "0") int type) {
		model.addAttribute("branchlist", branchDAO.getAllBranches());
		model.addAttribute("brList", branchRouteDAO.getBranchRouteByWhere(page, fromBranchId, toBranchId, type));
		model.addAttribute("page_obj", new Page(branchRouteDAO.getBranchRouteCount(fromBranchId, toBranchId, type), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "branchroute/list";
	}

	@RequestMapping("/edit/{fromBranchId}/{toBranchId}/{type}")
	public String edit(Model model, @PathVariable("fromBranchId") int fromBranchId, @PathVariable("toBranchId") int toBranchId, @PathVariable("type") int type) {
		model.addAttribute("branchlist", branchDAO.getAllEffectBranches());
		model.addAttribute("branchroute", branchRouteDAO.getBranchRouteByWheresql(fromBranchId, toBranchId, type).get(0));
		return "branchroute/edit";
	}

	@RequestMapping("/save/{oldfromBranchId}/{oldtoBranchId}/{oldtype}")
	public @ResponseBody String save(@PathVariable("oldfromBranchId") int oldfromBranchId, @PathVariable("oldtoBranchId") int oldtoBranchId, @PathVariable("oldtype") int oldtype,
			@RequestParam("fromBranchId") int fromBranchId, @RequestParam("toBranchId") int toBranchId, @RequestParam("type") int type) throws Exception {
		List<BranchRoute> brlist = branchRouteDAO.getBranchRouteByWheresql(fromBranchId, toBranchId, type);

		if (brlist.size() > 0) {
			return "{\"errorCode\":0,\"error\":\"该货物流向设置已存在\"}";
		} else {
			branchRouteDAO.saveBranchRouteByWhere(oldfromBranchId, oldtoBranchId, oldtype, fromBranchId, toBranchId, type);
			cwbRouteService.reload();
			logger.info("operatorUser={},货物流向设置->save", getSessionUser().getUsername());
			return "{\"errorCode\":0,\"error\":\"保存成功\"}";
		}
	}

	@RequestMapping("/del/{fromBranchId}/{toBranchId}/{type}")
	public @ResponseBody String del(@PathVariable("fromBranchId") int fromBranchId, @PathVariable("toBranchId") int toBranchId, @PathVariable("type") int type) throws Exception {

		branchRouteDAO.deleteBranchRouteByWhere(fromBranchId, toBranchId, type);
		cwbRouteService.reload();
		logger.info("operatorUser={},货物流向设置->del", getSessionUser().getUsername());
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	@RequestMapping("/toNextStopPage")
	public String toNextStopPage(Model model, @RequestParam(value = "branchid", required = false, defaultValue = "-1") long branchid, HttpServletRequest request) {
		model.addAttribute("branches", branchDAO.getBranchesByKuFangAndZhanDian());
		List<Long> nextbranches = new ArrayList<Long>();
		if (branchid > -1) {
			cwbRouteService.reload();
			nextbranches = cwbRouteService.getNextPossibleBranch(branchid);
		}
		model.addAttribute("nextbranches", nextbranches);
		return "branchroute/nextStop";
	}

	@RequestMapping("/gettoNextStopPage")
	public @ResponseBody JSONObject gettoNextStopPage(Model model, @RequestParam(value = "branchid", required = false, defaultValue = "-1") long branchid, HttpServletRequest request) {
		model.addAttribute("branches", branchDAO.getBranchesByKuFangAndZhanDian());
		List<Long> nextbranches = new ArrayList<Long>();
		if (branchid > -1) {
			cwbRouteService.reload();
			nextbranches = cwbRouteService.getNextPossibleBranch(branchid);
		}
		JSONObject obj = new JSONObject();
		obj.put("nextbranches", nextbranches);

		return obj;
	}

	@RequestMapping("/gettoNextbackStopPage")
	public @ResponseBody JSONObject gettoNextbackStopPage(Model model, @RequestParam(value = "branchid", required = false, defaultValue = "-1") long branchid, HttpServletRequest request) {
		model.addAttribute("branches", branchDAO.getBranchesByKuFangAndZhanDian());
		List<Long> nextbranches = new ArrayList<Long>();
		if (branchid > -1) {
			cwbRouteService.reload();
			nextbranches = cwbRouteService.getNextPossibleBackBranch(branchid);
		}
		JSONObject obj = new JSONObject();
		obj.put("nextbranches", nextbranches);

		return obj;
	}

	@RequestMapping("/saveNextStop")
	public String save(Model model, @RequestParam("branchid") long branchid, @RequestParam("cwbtobranchid") String cwbtobranchid, @RequestParam("type") int type) throws Exception {
		List<Long> possibleBranchIds = new ArrayList<Long>();

		if (cwbtobranchid.length() > 0) {
			for (int i = 0; i < cwbtobranchid.split(",").length; i++) {
				long id = Long.parseLong(cwbtobranchid.split(",")[i]);
				possibleBranchIds.add(id);
			}
		}

		if (type == BranchRouteEnum.JinZhengXiang.getValue()) {
			cwbRouteService.saveNextPossileBranch(branchid, possibleBranchIds);
		} else if (type == BranchRouteEnum.JinDaoXiang.getValue()) {
			cwbRouteService.saveNextPossileBackBranch(branchid, possibleBranchIds);
		}

		List<Long> nextbranches = new ArrayList<Long>();
		if (branchid > -1) {
			cwbRouteService.reload();
			nextbranches = cwbRouteService.getNextPossibleBranch(branchid);
		}
		model.addAttribute("nextbranches", nextbranches);
		model.addAttribute("branches", branchDAO.getBranchesByKuFangAndZhanDian());
		logger.info("operatorUser={},货物流向设置->saveNextStop", getSessionUser().getUsername());
		model.addAttribute("errorState", "保存成功");
		return "branchroute/nextStop";
	}

}