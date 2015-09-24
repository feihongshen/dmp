package cn.explink.b2c.cachesynchro;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.tools.JiontDAO;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.UserDAO;

@Controller
@RequestMapping("/cacheSynchro")
public class CacheSynchroController {
	@Autowired
	UserDAO userDao;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	JiontDAO jiontDAO;
	@RequestMapping("/customercache")
	public @ResponseBody String cacheCustomerSynchroGet(HttpServletRequest request,HttpServletResponse response){
		customerDAO.updateCache();
		return "基础设置客户信息缓存设置同步成功";
	}
	@RequestMapping("/usercache")
	public @ResponseBody String cacheUser(){
		userDao.updateCache();
		return "基础设置用户信息缓存设置同步成功";
	}
	@RequestMapping("/branchcache")
	public @ResponseBody String cacheBranch(){
		branchDAO.updateCache();
		return "基础设置站点信息缓存设置同步成功";
	}
	@RequestMapping("/jointcache")
	public @ResponseBody String cacheJoint(){
		jiontDAO.updateCache();
		return "基础设置对接信息缓存设置同步成功";
	}
	@RequestMapping("/cacheClear")
	public @ResponseBody String cacheClear(){
		customerDAO.updateCache();
		userDao.updateCache();
		userDao.updateCache();
		jiontDAO.updateCache();
		return "基础设置缓存同步数据库成功";
	}
}
