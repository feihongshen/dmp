package cn.explink.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import scala.Array;
import cn.explink.dao.ReasonDao;
import cn.explink.domain.Reason;
import cn.explink.domain.User;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.util.Page;

@Controller
@RequestMapping("/reason")
public class ReasonController {

	@Autowired
	ReasonDao reasonDao;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	private Logger logger = LoggerFactory.getLogger(this.getClass());


	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy
				.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	@RequestMapping("/list/{page}")
	public String list(
			Model model,
			@PathVariable("page") long page,
			@RequestParam(value = "reasontype", required = false, defaultValue = "0") long reasontype) {
		model.addAttribute("reasonList",
				reasonDao.getReasonByPage(page, reasontype));
		model.addAttribute("page_obj",
				new Page(reasonDao.getReasonCount(reasontype), page,
						Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "reason/list";
	}

	@RequestMapping("/edit/{id}")
	public String edit(@PathVariable("id") long reasonid, Model model) {
		Reason reason = reasonDao.getReasonByReasonid(reasonid);
		if(reason.getParentid()>0){
			String firstreason = reasonDao.getReasonByReasonid(reason.getParentid()).getReasoncontent();
			model.addAttribute("firstreason",firstreason);
		}
		model.addAttribute("reason", reasonDao.getReasonByReasonid(reasonid));
		
		
		return "reason/edit";
	}

	@RequestMapping("/add")
	public String add(HttpServletRequest request, Model model) {
		request.setAttribute("reasonList", reasonDao.add());
		return "reason/add";
	}

	@RequestMapping("/save/{id}")
	public @ResponseBody
	String save(
			Model model,
			HttpServletRequest request,
			@PathVariable("id") long reasonid,
			@RequestParam(value = "reasoncontent", defaultValue = "", required = false) String reasoncontent,
			@RequestParam(value="changealowflag",defaultValue="0",required=false) int changealowflag
			) {
		List<Reason> list = reasonDao.getReasonByReasoncontent(reasoncontent);
		if (list.size() > 0) {
			if(validateReason(changealowflag, list)){
				return "{\"errorCode\":1,\"error\":\"该文字已存在\"}";
			}
			
		}
		Reason reason = new Reason();
		reason.setReasoncontent(reasoncontent);
		reason.setReasonid(reasonid);
		reason.setChangealowflag(changealowflag);
		reasonDao.saveReason(reason);
		logger.info("operatorUser={},常用语管理->save", getSessionUser()
				.getUsername());
		return "{\"errorCode\":0,\"error\":\"修改成功\"}";
	}

	private Boolean validateReason(int changealowflag, List<Reason> list) {
		for(Reason r:list){
			if(r.getChangealowflag()==changealowflag){
				return true;
			}
		}
		return false;
	}

	@RequestMapping("/create")
	public @ResponseBody
	String create(
			Model model,
			HttpServletRequest request,
			@RequestParam(value = "reasoncontent", defaultValue = "", required = false) String reasoncontent,
			@RequestParam(value = "reasontype", defaultValue = "0", required = false) long reasontype,
			@RequestParam(value = "whichreason", defaultValue = "0", required = false) int whichreason,
			@RequestParam(value = "parentid", defaultValue = "0", required = false) int parentid,
			@RequestParam(value = "changealowflag", defaultValue = "0", required = false) int changealowflag) {

		List<Reason> list = reasonDao.getReasonByReasoncontentAndParentid(reasoncontent,parentid);
		if (list.size() > 0) {
			if(validateReasonType(reasontype, list)){
				return "{\"errorCode\":1,\"error\":\"该文字已存在\"}";
			}
			
		}
		Reason  parentReason1=reasonDao.getReasonByReasonid(parentid);;
		Reason reason = new Reason();
		reason.setReasoncontent(reasoncontent);
		reason.setReasontype(reasontype);
		reason.setWhichreason(whichreason);
		reason.setChangealowflag(changealowflag);
		if (whichreason==2) {
			if (changealowflag==1) {
				if (parentReason1.getChangealowflag()!=1) {
					reasonDao.saveReasonAdd(parentid,1);
					List<Reason> reasons=reasonDao.getAllSecondLevelReason(parentid);
					if (reasons.size()>0) {
						for (Reason reason2 : reasons) {
							reasonDao.saveReasonAdd(reason2.getReasonid(), 1);
						}
					}
				}
			}else {
				if (parentReason1.getChangealowflag()==1) {
					reason.setChangealowflag(1);
				}
			}
		}
		if (reasontype == 1||reasontype == 2||reasontype == 13) {
			reason.setWhichreason(1);
			if (whichreason == 2) {
				reason.setParentid(parentid);
				reason.setWhichreason(2);
			}
		}

		reasonDao.creReason(reason);
		logger.info("operatorUser={},常用语管理->create", getSessionUser()
				.getUsername());
		return "{\"errorCode\":0,\"error\":\"新建成功\"}";
	}

	private Boolean validateReasonType(long reasontype, List<Reason> list) {
		for(Reason r:list){
			if(r.getReasontype()==reasontype){
				return true;
			}
		}
		return false;
	}
	@RequestMapping("/getfirstreason")
	public @ResponseBody String getfirstreason( @RequestParam(value = "reasontype", defaultValue = "0", required = false) long reasontype){

		if (reasontype > 0) {
			List<Reason> leveltwolist = this.reasonDao.getFirstReasonByType(reasontype);
			return JSONArray.fromObject(leveltwolist).toString();
		} else {
			return "[]";
		}
	}
	
	@RequestMapping("/getSecondreason")
	public @ResponseBody String getSecondreason( @RequestParam(value = "firstreasonid", defaultValue = "0", required = false) long firstreasonid){

		if (firstreasonid > 0) {
			List<Reason> leveltwolist = this.reasonDao.getAllSecondLevelReason(firstreasonid);
			return JSONArray.fromObject(leveltwolist).toString();
		} else {
			return "[]";
		}
	}
	
	
}
