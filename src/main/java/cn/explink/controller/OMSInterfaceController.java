package cn.explink.controller;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.explink.core_down.EpaiApiDAO;
import cn.explink.b2c.explink.core_up.CommenSendData;
import cn.explink.b2c.explink.core_up.CommonCoreService;
import cn.explink.b2c.explink.xmldto.OrderDto;
import cn.explink.b2c.maisike.stores.StoresDAO;
import cn.explink.b2c.tools.CommonExptDao;
import cn.explink.b2c.tools.ExptCodeJointDAO;
import cn.explink.b2c.tools.ExptReasonDAO;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.poscodeMapp.PoscodeMapp;
import cn.explink.b2c.tools.poscodeMapp.PoscodeMappDAO;
import cn.explink.b2c.weisuda.WeisudaService;
import cn.explink.dao.ApplyEditDeliverystateDAO;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CommonDAO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.EmailDateDAO;
import cn.explink.dao.ExportmouldDAO;
import cn.explink.dao.MenuDAO;
import cn.explink.dao.OrderGoodsDAO;
import cn.explink.dao.ReasonDao;
import cn.explink.dao.RemarkDAO;
import cn.explink.dao.RoleDAO;
import cn.explink.dao.SetExportFieldDAO;
import cn.explink.dao.StockResultDAO;
import cn.explink.dao.SwitchDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.TruckDAO;
import cn.explink.dao.UserDAO;
import cn.explink.dao.WarehouseToCommenDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.EmailDate;
import cn.explink.domain.User;
import cn.explink.enumutil.BranchEnum;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.pos.tools.PosEnum;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.util.Dom4jParseUtil;
import cn.explink.util.ResourceBundleUtil;

/**
 * 提供给OMS系统请求的接口
 * 
 * @author Administrator
 *
 */
@RequestMapping("/OMSInterface")
@Controller
public class OMSInterfaceController {
	String serverversion = "3.0";

	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	CommonExptDao commonExptDao;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	StockResultDAO stockResultDAO;
	@Autowired
	TruckDAO truckDAO;
	@Autowired
	MenuDAO menuDAO;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	CommonDAO commonDAO;
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	ExportmouldDAO exportmouldDAO;
	@Autowired
	SetExportFieldDAO setExportFieldDAO;
	@Autowired
	CustomWareHouseDAO customWareHouseDAO;
	@Autowired
	ExptCodeJointDAO exptCodeJointDAO;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	ReasonDao reasonDao;
	@Autowired
	SwitchDAO switchDAO;
	@Autowired
	RoleDAO roleDAO;
	@Autowired
	SystemInstallDAO systemInstallDAO;
	@Autowired
	RemarkDAO remarkDAO;

	@Autowired
	EpaiApiDAO epaiApiDAO;
	@Autowired
	ExptReasonDAO exptReasonDAO;

	@Autowired
	EmailDateDAO emailDateDAO;
	@Autowired
	CommonCoreService commonCoreService;
	@Autowired
	StoresDAO storesDAO;
	@Autowired
	WarehouseToCommenDAO warehouseToCommenDAO;
	@Autowired
	WeisudaService weisudaService;
	@Autowired
	OrderGoodsDAO orderGoodsDAO;
	@Autowired
	PoscodeMappDAO poscodeMappDAO;
	@Autowired
	ApplyEditDeliverystateDAO applyEditDeliverystateDAO;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping("/getNowBrancheId")
	public @ResponseBody String getNowBrancheId() {
		JSONObject reJson = new JSONObject();

		ExplinkUserDetail userDetail = securityContextHolderStrategy.getContext() == null ? null : (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		if (userDetail == null) {

			reJson.put("nowbranchid", "0");
		} else {
			reJson.put("nowbranchid", userDetail.getUser().getBranchid());
		}
		return reJson.toString();
	}

	@RequestMapping("/getNowRealname")
	public @ResponseBody String getNowRealname() {
		JSONObject reJson = new JSONObject();
		try {
			ExplinkUserDetail userDetail = securityContextHolderStrategy.getContext() == null ? null : (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication()
					.getPrincipal();
			if (userDetail == null) {

				reJson.put("nowRealname", "");
			} else {
				reJson.put("nowRealname", userDetail.getUser().getUsername());
			}
		} catch (Exception e) {
			reJson.put("nowRealname", "");
		}
		return reJson.toString();
	}

	@RequestMapping("/getNowUserId")
	public @ResponseBody String getNowUserId() {
		JSONObject reJson = new JSONObject();
		try {
			ExplinkUserDetail userDetail = securityContextHolderStrategy.getContext() == null ? null : (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication()
					.getPrincipal();
			if (userDetail == null) {

				reJson.put("nowUserId", "0");
			} else {
				reJson.put("nowUserId", userDetail.getUser().getUserid());
			}
		} catch (Exception e) {
			reJson.put("nowUserId", "");
		}
		return reJson.toString();
	}

	@RequestMapping("/getNowUserShowPhoneFlag")
	public @ResponseBody String getNowUserShowPhoneFlag() {
		JSONObject reJson = new JSONObject();

		ExplinkUserDetail userDetail = securityContextHolderStrategy.getContext() == null ? null : (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		if (userDetail == null) {

			reJson.put("nowShowPhoneFlag", "");
		} else {
			reJson.put("nowShowPhoneFlag", userDetail.getUser().getShowphoneflag());
		}
		return reJson.toString();
	}

	@RequestMapping("/getUserByid/{id}")
	public @ResponseBody String getUserByid(@PathVariable("id") long id) {
		if (id == 0) {
			try {
				ExplinkUserDetail userDetail = securityContextHolderStrategy.getContext() == null ? null : (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication()
						.getPrincipal();
				if (userDetail == null) {
					return "[]";
				} else {
					id = userDetail.getUser().getUserid();
				}
			} catch (Exception e) {
				return "[]";
			}
		}
		User u = userDAO.getUserByUserid(id);
		JSONArray jsonArray = new JSONArray();
		JSONObject us = new JSONObject();
		us.put("branchid", u.getBranchid());
		us.put("employeestatus", u.getEmployeestatus());
		us.put("employeestatusName", u.getEmployeestatusName());
		us.put("idcardno", u.getIdcardno());
		us.put("realname", u.getRealname());
		us.put("roleid", u.getRoleid());
		us.put("showphoneflag", u.getShowphoneflag());
		us.put("shownameflag",u.getShownameflag());
		us.put("userDeleteFlag", u.getUserDeleteFlag());
		us.put("useraddress", u.getUseraddress());
		us.put("usercustomerid", u.getUsercustomerid());
		us.put("useremail", u.getUseremail());
		us.put("userid", u.getUserid());
		us.put("usermobile", u.getUsermobile());
		us.put("username", u.getUsername());
		us.put("userphone", u.getUserphone());
		us.put("userremark", u.getUserremark());
		us.put("usersalary", u.getUsersalary());
		us.put("userwavfile", u.getUserwavfile());
		us.put("deliverManCode", u.getDeliverManCode());
		us.put("showmobileflag", u.getShowmobileflag());
		jsonArray.add(us);

		return jsonArray.toString();
	}

	@RequestMapping("/getBranchByAllZhanDian")
	public @ResponseBody String getBranchByAllZhanDian() {
		return JSONArray.fromObject(branchDAO.getBranchAllzhandian(BranchEnum.ZhanDian.getValue() + "")).toString();
	}
	
	@RequestMapping("/getAccessableBranch/{userId}")
	public @ResponseBody String getAccessableBranch(@PathVariable("userId") long userId) {
		return JSONArray.fromObject(branchDAO.getAccessableBranch(userId, BranchEnum.ZhanDian.getValue())).toString();
	}

	@RequestMapping("/getBranchByAllEffectZhanDian")
	public @ResponseBody String getBranchByAllEffectZhanDian() {
		return JSONArray.fromObject(branchDAO.getBranchEffectAllzhandian(BranchEnum.ZhanDian.getValue() + "")).toString();
	}

	@RequestMapping("/getBranchByZhanDian")
	public @ResponseBody String getBranchByZhanDian() {
		return JSONArray.fromObject(branchDAO.getBranchBySiteType(BranchEnum.ZhanDian.getValue())).toString();
	}
	
	@RequestMapping("/getNowCustomerPos/{customerid}")
	public @ResponseBody String getNowCustomerPos(@PathVariable("customerid") long customerid){
		PoscodeMapp codemapping = this.poscodeMappDAO.getPosCodeByKey(customerid, PosEnum.TongLianPos.getKey());
		if(codemapping == null){
			return "";
		}
		return codemapping.getCustomercode();
	}

	@RequestMapping("/getBranchByKufang")
	public @ResponseBody String getBranchByKufang() {
		return JSONArray.fromObject(branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue())).toString();
	}

	@RequestMapping("/getCustomWareHouseByid/{id}")
	public @ResponseBody String getCustomWareHouseByid(@PathVariable("id") String id) {

		try {
			return JSONObject.fromObject(customWareHouseDAO.getWarehouseId(Long.parseLong(id))).toString();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			return "{}";
		}
	}

	@RequestMapping("/getCustomWareHouse")
	public @ResponseBody String getCustomWareHouse() {
		return JSONArray.fromObject(customWareHouseDAO.getWarehouseAll()).toString();
	}

	@RequestMapping("/getCustomWareHouseByCustomid/{customid}")
	public @ResponseBody String getCustomWareHouseByCustomid(@PathVariable("customid") long customid) {
		return JSONArray.fromObject(customWareHouseDAO.getWarehouseAllByCustomid(customid)).toString();
	}

	@RequestMapping("/getBranchById/{id}")
	public @ResponseBody String getBranchById(@PathVariable("id") long id) {
		return JSONObject.fromObject(branchDAO.getBranchById(id)).toString();
	}

	@RequestMapping("/getBranchByName")
	public @ResponseBody String getBranchByName(@RequestParam(value = "name", required = false, defaultValue = "") String name) {
		return JSONArray.fromObject(branchDAO.getBranchByBranchnameMoHu(name)).toString();
	}

	@RequestMapping("/getBranchByBranchName")
	public @ResponseBody String getBranchByBranchName(@RequestParam(value = "branchname", required = false, defaultValue = "") String branchname) {
		return JSONObject.fromObject(branchDAO.getBranchByBranchname(branchname)).toString();
	}

	@RequestMapping("/getCustomer")
	public @ResponseBody String getCustomer() {
		return JSONArray.fromObject(customerDAO.getAllCustomers()).toString();
	}

	@RequestMapping("/getCustomerById/{id}")
	public @ResponseBody String getCustomerById(@PathVariable("id") long id) {
		return JSONObject.fromObject(customerDAO.getCustomerById(id)).toString();
	}

	@RequestMapping("/getCustomerByIds/{ids}")
	public @ResponseBody String getCustomerByIds(@PathVariable("ids") String customerids) {
		return JSONArray.fromObject(customerDAO.getCustomerByIds(customerids)).toString();
	}

	@RequestMapping("/getJointDaoByClientID/{id}")
	public @ResponseBody String getJointDaoByClientID(@PathVariable("id") String id) {
		return JSONObject.fromObject(jiontDAO.getCountByClientID(id)).toString();
	}

	@RequestMapping("/getTruck")
	public @ResponseBody String getTruck() {
		return JSONArray.fromObject(truckDAO.getAllTruck()).toString();
	}

	@RequestMapping("/getTruckById/{id}")
	public @ResponseBody String getTruckById(@PathVariable("id") long id) {
		return JSONObject.fromObject(truckDAO.getTruckByTruckid(id)).toString();
	}

	@RequestMapping("/getCwbDetailsByCwb/{cwb}")
	public @ResponseBody String getCwbDetailsByCwb(@PathVariable("cwb") String cwb) {
		JSONObject reJson = new JSONObject();
		reJson.put("cwb", cwbDAO.getCwbByCwb(cwb));
		reJson.put("deliveryState", deliveryStateDAO.getActiveDeliveryStateByCwb(cwb));
		return reJson.toString();
	}

	@RequestMapping("/getDeliverById/{id}")
	public @ResponseBody String getDeliverById(@PathVariable("id") long id) {
		User u = userDAO.getUserByUserid(id);

		JSONObject us = new JSONObject();
		us.put("branchid", u.getBranchid());
		us.put("employeestatus", u.getEmployeestatus());
		us.put("employeestatusName", u.getEmployeestatusName());
		us.put("idcardno", u.getIdcardno());
		us.put("realname", u.getRealname());
		us.put("roleid", u.getRoleid());
		us.put("showphoneflag", u.getShowphoneflag());
		us.put("userDeleteFlag", u.getUserDeleteFlag());
		us.put("useraddress", u.getUseraddress());
		us.put("usercustomerid", u.getUsercustomerid());
		us.put("useremail", u.getUseremail());
		us.put("userid", u.getUserid());
		us.put("usermobile", u.getUsermobile());
		us.put("username", u.getUsername());
		us.put("userphone", u.getUserphone());
		us.put("userremark", u.getUserremark());
		us.put("usersalary", u.getUsersalary());
		us.put("userwavfile", u.getUserwavfile());

		return us.toString();
	}

	@RequestMapping("/getCommon")
	public @ResponseBody String getAllCommon() {
		return JSONArray.fromObject(commonDAO.getAllCommons()).toString();
	}

	@RequestMapping("/getLogUser")
	public @ResponseBody String getLogUser() {
		ExplinkUserDetail userDetail = null;
		try {
			userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		} catch (Exception e) {
			return "[]";
		}

		JSONObject us = new JSONObject();
		if (userDetail.getUser() != null) {
			us.put("branchid", userDetail.getUser().getBranchid());
			us.put("employeestatus", userDetail.getUser().getEmployeestatus());
			us.put("employeestatusName", userDetail.getUser().getEmployeestatusName());
			us.put("idcardno", userDetail.getUser().getIdcardno());
			us.put("realname", userDetail.getUser().getRealname());
			us.put("roleid", userDetail.getUser().getRoleid());
			us.put("showphoneflag", userDetail.getUser().getShowphoneflag());
			us.put("userDeleteFlag", userDetail.getUser().getUserDeleteFlag());
			us.put("useraddress", userDetail.getUser().getUseraddress());
			us.put("usercustomerid", userDetail.getUser().getUsercustomerid());
			us.put("useremail", userDetail.getUser().getUseremail());
			us.put("userid", userDetail.getUser().getUserid());
			us.put("usermobile", userDetail.getUser().getUsermobile());
			us.put("username", userDetail.getUser().getUsername());
			us.put("userphone", userDetail.getUser().getUserphone());
			us.put("userremark", userDetail.getUser().getUserremark());
			us.put("usersalary", userDetail.getUser().getUsersalary());
			us.put("userwavfile", userDetail.getUser().getUserwavfile());
		}

		return us.toString();
	}

	@RequestMapping("/getCommonById/{id}")
	public @ResponseBody String getCommonById(@PathVariable("id") long id) {
		return JSONObject.fromObject(commonDAO.getCommonById(id)).toString();
	}

	@RequestMapping("/getJointEntity/{jointnum}")
	public @ResponseBody String getJointEntity(@PathVariable("jointnum") int jointnum) {
		return JSONObject.fromObject(jiontDAO.getJointEntity(jointnum)).toString();
	}

	@RequestMapping("/getJointEntityList")
	public @ResponseBody String getJointEntityList() {
		return JSONArray.fromObject(jiontDAO.getJointEntityList()).toString();
	}

	@RequestMapping("/getJointEntityByCompany/{companyname}")
	public @ResponseBody String getJointEntityByCompany(@PathVariable("companyname") String companyname) {
		return JSONObject.fromObject(jiontDAO.getJointEntityByCompanyname(companyname)).toString();
	}

	@RequestMapping("/getExportMoulds/{roleid}")
	public @ResponseBody String getExportMoulds(@PathVariable("roleid") long roleid) {
		return JSONArray.fromObject(exportmouldDAO.getAllExportmouldByUser(roleid)).toString();
	}

	@RequestMapping("/getSetExportFieldByStrs/{strs}")
	public @ResponseBody String getSetExportFieldByStrs(@PathVariable("strs") String strs) {
		return JSONArray.fromObject(exportmouldDAO.getSetExportFieldByStrs(strs)).toString();
	}

	@RequestMapping("/getAllBranch")
	public @ResponseBody String getAllBranch() {
		return JSONArray.fromObject(branchDAO.getAllBranches()).toString();
	}

	@RequestMapping("/getBranchByBranchids/{branchids}")
	public @ResponseBody String getBranchByBranchids(@PathVariable("branchids") String branchids) {
		return JSONArray.fromObject(branchDAO.getBranchByBranchids(branchids)).toString();
	}

	@RequestMapping("/getUserForALL")
	public @ResponseBody String getUserForALL() {
		List<User> ulist = userDAO.getUserForALL();
		JSONArray jsonArray = new JSONArray();
		for (User u : ulist) {
			JSONObject us = new JSONObject();
			us.put("branchid", u.getBranchid());
			us.put("employeestatus", u.getEmployeestatus());
			us.put("employeestatusName", u.getEmployeestatusName());
			us.put("idcardno", u.getIdcardno());
			us.put("realname", u.getRealname());
			us.put("roleid", u.getRoleid());
			us.put("showphoneflag", u.getShowphoneflag());
			us.put("userDeleteFlag", u.getUserDeleteFlag());
			us.put("useraddress", u.getUseraddress());
			us.put("usercustomerid", u.getUsercustomerid());
			us.put("useremail", u.getUseremail());
			us.put("userid", u.getUserid());
			us.put("usermobile", u.getUsermobile());
			us.put("username", u.getUsername());
			us.put("userphone", u.getUserphone());
			us.put("userremark", u.getUserremark());
			us.put("usersalary", u.getUsersalary());
			us.put("userwavfile", u.getUserwavfile());

			jsonArray.add(us);
		}

		return jsonArray.toString();
	}

	@RequestMapping("/getAllUsers")
	public @ResponseBody String getAllUsers() {
		List<User> ulist = userDAO.getAllUser();
		JSONArray jsonArray = new JSONArray();
		for (User u : ulist) {
			JSONObject us = new JSONObject();
			us.put("branchid", u.getBranchid());
			us.put("employeestatus", u.getEmployeestatus());
			us.put("employeestatusName", u.getEmployeestatusName());
			us.put("idcardno", u.getIdcardno());
			us.put("realname", u.getRealname());
			us.put("roleid", u.getRoleid());
			us.put("showphoneflag", u.getShowphoneflag());
			us.put("userDeleteFlag", u.getUserDeleteFlag());
			us.put("useraddress", u.getUseraddress());
			us.put("usercustomerid", u.getUsercustomerid());
			us.put("useremail", u.getUseremail());
			us.put("userid", u.getUserid());
			us.put("usermobile", u.getUsermobile());
			us.put("username", u.getUsername());
			us.put("userphone", u.getUserphone());
			us.put("userremark", u.getUserremark());
			us.put("usersalary", u.getUsersalary());
			us.put("userwavfile", u.getUserwavfile());

			jsonArray.add(us);
		}

		return jsonArray.toString();
	}

	@RequestMapping("/getDeliverListByCaiwu/{caiwubranchid}")
	// 查询 某个财务角色下 可见的小件员。
	public @ResponseBody String getDeliverListByCaiwu(@PathVariable("caiwubranchid") long caiwubranchid) {

		List<Branch> branchlist = branchDAO.getBranchBelowToCaiwu(caiwubranchid);
		String branchids = "";
		if (branchlist != null && branchlist.size() > 0) {
			for (Branch br : branchlist) {
				branchids += br.getBranchid() + ",";
			}
			branchids = branchids.substring(0, branchids.length() - 1);
		} else {
			branchids = "0";
		}

		List<User> ulist = userDAO.getUserByBranchIds(branchids);
		JSONArray jsonArray = new JSONArray();
		for (User u : ulist) {
			JSONObject us = new JSONObject();
			us.put("branchid", u.getBranchid());
			us.put("employeestatus", u.getEmployeestatus());
			us.put("employeestatusName", u.getEmployeestatusName());
			us.put("idcardno", u.getIdcardno());
			us.put("realname", u.getRealname());
			us.put("roleid", u.getRoleid());
			us.put("showphoneflag", u.getShowphoneflag());
			us.put("userDeleteFlag", u.getUserDeleteFlag());
			us.put("useraddress", u.getUseraddress());
			us.put("usercustomerid", u.getUsercustomerid());
			us.put("useremail", u.getUseremail());
			us.put("userid", u.getUserid());
			us.put("usermobile", u.getUsermobile());
			us.put("username", u.getUsername());
			us.put("userphone", u.getUserphone());
			us.put("userremark", u.getUserremark());
			us.put("usersalary", u.getUsersalary());
			us.put("userwavfile", u.getUserwavfile());

			jsonArray.add(us);
		}

		return jsonArray.toString();
	}

	@RequestMapping("/getBranchListByCaiwu/{caiwubranchid}")
	// 查询 某个财务角色下 可见的站点
	public @ResponseBody String getBranchListByCaiwu(@PathVariable("caiwubranchid") long caiwubranchid) {

		List<Branch> branchlist = branchDAO.getBranchBelowToCaiwu(caiwubranchid);
		return JSONArray.fromObject(branchlist).toString();
	}

	@RequestMapping("/getBranchListByCaiwuAndUser/{caiwubranchid}")
	// 查询 某个财务角色下 可见的站点连接区域权限设置
	public @ResponseBody String getBranchListByCaiwuAndUser(@PathVariable("caiwubranchid") long caiwubranchid, @RequestParam("userid") long userid) {

		List<Branch> branchlist = branchDAO.getBranchBelowToCaiwuAndUser(caiwubranchid, userid);
		return JSONArray.fromObject(branchlist).toString();
	}

	@RequestMapping("/getBranchListByTypeAndUser/{type}")
	// 查询 某个财务角色下 可见的站点连接区域权限设置a
	public @ResponseBody String getBranchListByTypeAndUser(@PathVariable("type") String type, @RequestParam("userid") long userid) {

		List<Branch> branchlist = branchDAO.getQueryBranchByBranchsiteAndUserid(userid, type);
		return JSONArray.fromObject(branchlist).toString();
	}

	@RequestMapping("/getBranchListByUser/{userid}")
	// 查询 某个财务角色下 可见的站点连接区域权限设置
	public @ResponseBody String getBranchListByUser(@PathVariable("userid") long userid) {

		List<Branch> branchlist = branchDAO.getBranchToUser(userid);
		return JSONArray.fromObject(branchlist).toString();
	}

	@RequestMapping("/getAllGruopNumWorker")
	// 查询 所有组织在岗员工。
	public @ResponseBody String getAllGruopNumWorker() {
		JSONObject us = userDAO.getAllGruopNumWorker();
		return us.toString();
	}

	@RequestMapping("/getDeliverListByBranch/{branchid}")
	// 查询 某个财务角色下 可见的小件员。
	public @ResponseBody String getDeliverListByBranch(@PathVariable("branchid") long branchid) {
		List<User> ulist = userDAO.getAllUserbybranchid(branchid);
		JSONArray jsonArray = new JSONArray();
		for (User u : ulist) {
			JSONObject us = new JSONObject();
			us.put("branchid", u.getBranchid());
			us.put("employeestatus", u.getEmployeestatus());
			us.put("employeestatusName", u.getEmployeestatusName());
			us.put("idcardno", u.getIdcardno());
			us.put("realname", u.getRealname());
			us.put("roleid", u.getRoleid());
			us.put("showphoneflag", u.getShowphoneflag());
			us.put("userDeleteFlag", u.getUserDeleteFlag());
			us.put("useraddress", u.getUseraddress());
			us.put("usercustomerid", u.getUsercustomerid());
			us.put("useremail", u.getUseremail());
			us.put("userid", u.getUserid());
			us.put("usermobile", u.getUsermobile());
			us.put("username", u.getUsername());
			us.put("userphone", u.getUserphone());
			us.put("userremark", u.getUserremark());
			us.put("usersalary", u.getUsersalary());
			us.put("userwavfile", u.getUserwavfile());

			jsonArray.add(us);
		}
		return jsonArray.toString();
	}

	@RequestMapping("/getAllUserByBranchIds/{branchid}")
	// 查询 站点下的小件员。
	public @ResponseBody String getAllUserByBranchIds(@PathVariable("branchid") String branchids) {
		List<User> ulist = userDAO.getAllUserByBranchIds(branchids);
		JSONArray jsonArray = new JSONArray();
		for (User u : ulist) {
			JSONObject us = new JSONObject();
			us.put("branchid", u.getBranchid());
			us.put("employeestatus", u.getEmployeestatus());
			us.put("employeestatusName", u.getEmployeestatusName());
			us.put("idcardno", u.getIdcardno());
			us.put("realname", u.getRealname());
			us.put("roleid", u.getRoleid());
			us.put("showphoneflag", u.getShowphoneflag());
			us.put("userDeleteFlag", u.getUserDeleteFlag());
			us.put("useraddress", u.getUseraddress());
			us.put("usercustomerid", u.getUsercustomerid());
			us.put("useremail", u.getUseremail());
			us.put("userid", u.getUserid());
			us.put("usermobile", u.getUsermobile());
			us.put("username", u.getUsername());
			us.put("userphone", u.getUserphone());
			us.put("userremark", u.getUserremark());
			us.put("usersalary", u.getUsersalary());
			us.put("userwavfile", u.getUserwavfile());

			jsonArray.add(us);
		}
		return jsonArray.toString();
	}

	@RequestMapping("/getRoles")
	public @ResponseBody String getRoles() {
		return JSONArray.fromObject(roleDAO.getRoles()).toString();
	}

	@RequestMapping("/getRoleByRoleid/{id}")
	public @ResponseBody String getRoleByRoleid(@PathVariable("id") long id) {
		return JSONObject.fromObject(roleDAO.getRolesByRoleid(id)).toString();
	}

	@RequestMapping("/getReason/{id}")
	public @ResponseBody String getReason(@PathVariable("id") long id) {
		if (reasonDao.getReasonByReasonid(id) != null) {

			return JSONObject.fromObject(reasonDao.getReasonByReasonid(id)).toString();
		} else {
			return "{}";
		}
	}

	@RequestMapping("/getSwitchBySwitchname/{switchname}")
	public @ResponseBody String getSwitchBySwitchname(@PathVariable("switchname") String switchname) {
		return JSONObject.fromObject(switchDAO.getSwitchBySwitchname(switchname)).toString();
	}

	@RequestMapping("/getExptCodeJointByKey/{reasonid}/{expt_type}/{support_key}")
	public @ResponseBody String getExptCodeJointByKey(@PathVariable("reasonid") long reasonid, @PathVariable("expt_type") int expt_type, @PathVariable("support_key") String support_key) {
		return JSONObject.fromObject(exptCodeJointDAO.getExpMatchByKey(reasonid, support_key, expt_type)).toString();
	}

	@RequestMapping("/getExptCodeJointByB2c/{reasonid}/{expt_type}/{customerid}")
	public @ResponseBody String getExptCodeJointByB2c(@PathVariable("reasonid") long reasonid, @PathVariable("expt_type") int expt_type, @PathVariable("customerid") long customerid) {
		return JSONObject.fromObject(exptCodeJointDAO.getExpMatchByKeyByOMS(reasonid, customerid, expt_type)).toString();
	}

	@RequestMapping("/getSystemInstallByName/{name}")
	public @ResponseBody String getSystemInstallByName(@PathVariable("name") String name) {
		return JSONObject.fromObject(systemInstallDAO.getSystemInstallByName(name)).toString();
	}

	@RequestMapping("/getQueryBranchByBranchsiteAndUserid/{userid}/{sitetype}")
	public @ResponseBody String getQueryBranchByBranchsiteAndUserid(@PathVariable("userid") long userid, @PathVariable("sitetype") String sitetype) {
		return JSONArray.fromObject(branchDAO.getQueryBranchByBranchsiteAndUserid(userid, sitetype)).toString();
	}

	@RequestMapping("/getAllReason")
	public @ResponseBody String getAllReason() {
		return JSONArray.fromObject(reasonDao.getAllReason()).toString();
	}

	@RequestMapping("/getAllUserbybranchid/{branchid}")
	public @ResponseBody String getAllUserbybranchid(@PathVariable("branchid") long branchid) {
		return JSONArray.fromObject(userDAO.getAllUserbybranchid(branchid)).toString();
	}

	@RequestMapping("/getUserByUserName/{userName}")
	public @ResponseBody String getUserByUserName(@PathVariable("userName") String userName) {
		User u = userDAO.getUserByUsername(userName);
		JSONObject us = new JSONObject();
		us.put("username", u.getUsername());
		us.put("password", u.getPassword());
		return us.toString();
	}

	@RequestMapping("/getAllRemark")
	public @ResponseBody String getAllRemark() {
		return JSONArray.fromObject(remarkDAO.getAllRemark()).toString();
	}

	@RequestMapping("/getFileUrl")
	public @ResponseBody String getFileUrl() {
		String fileUrl = ResourceBundleUtil.WAVPATH;
		JSONObject json = new JSONObject();
		json.put("fileUrl", fileUrl);

		return json.toString();
	}

	@RequestMapping("/getEmailDateByEmaildateAndCustomerid/{customerid}")
	public @ResponseBody String getEmailDateByEmaildateAndCustomerid(@PathVariable("customerid") long customerid, @RequestParam("starttime") String starttime, @RequestParam("endtime") String endtime) {
		List<EmailDate> emaildatelist = emailDateDAO.getEmailDateByEmaildateAndCustomerid(customerid, starttime, endtime);
		return JSONArray.fromObject(emaildatelist).toString();
	}

	@RequestMapping("/getEpaiAPI")
	public @ResponseBody String getEpaiAPI() {
		return JSONArray.fromObject(epaiApiDAO.getEpaiApiList()).toString();
	}

	/**
	 * 查询dmp异常码设置
	 * 
	 * @return
	 */
	@RequestMapping("/getExptB2cSetUp/{customerid}")
	public @ResponseBody String getExptB2cSetUp(@PathVariable("customerid") String customerid) {
		return JSONArray.fromObject(exptReasonDAO.getExptReasonListByPos(customerid)).toString();
	}

	/**
	 * 查询dmp异常码设置 根据类型查询
	 * 
	 * @return
	 */
	@RequestMapping("/getExptB2cSetUp/{customerid}/{type}")
	public @ResponseBody String getExptB2cSetUpByType(@PathVariable("customerid") String customerid, @PathVariable("type") String type) {
		return JSONArray.fromObject(exptReasonDAO.getExptReasonListByTypeAndCustomerid(customerid, type)).toString();
	}

	@RequestMapping("/getEmailDateByEmaildateid/{emaildateid}")
	public @ResponseBody String getEmailDateByEmaildateid(@PathVariable("emaildateid") long emaildateid) {
		String emaildatetime = emailDateDAO.getEmailDateByIdAndState(emaildateid);
		JSONObject json = new JSONObject();
		json.put("emaildatetime", emaildatetime);

		return json.toString();
	}

	/**
	 * 根据订单号查询订单列表 根据订单号JSONArry请求
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/getOrdersByJsonCwbArr")
	public @ResponseBody String getOrdersByJsonCwbArr(HttpServletRequest request) throws IOException {
		InputStream input = new BufferedInputStream(request.getInputStream());
		String jsoncwbarr = Dom4jParseUtil.getStringByInputStream(input); // 读取文件流，获得xml字符串
		logger.info("dmp获取的参数={}", jsoncwbarr);

		String cwbs = "";
		JSONArray jsonArray = JSONArray.fromObject(jsoncwbarr);
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonobj = (JSONObject) jsonArray.get(i);
			cwbs += "'" + jsonobj.getString("cwb") + "',";
		}
		cwbs = cwbs.length() > 0 ? cwbs.substring(0, cwbs.length() - 1) : "-1";
		List<CwbOrder> cwbOrderList = cwbDAO.getCwbByCwbs(cwbs);
		if (cwbOrderList == null || cwbOrderList.size() == 0) {
			return null;
		}

		List<OrderDto> orderDtoList = buildOrderListDtos(cwbOrderList);

		return JacksonMapper.getInstance().writeValueAsString(orderDtoList);

	}

	private List<OrderDto> buildOrderListDtos(List<CwbOrder> cwbOrderList) {

		List<OrderDto> orderDtoList = new ArrayList<OrderDto>();

		for (CwbOrder cwbOrder : cwbOrderList) {

			try {
				OrderDto orderDto = new OrderDto();

				orderDto.setCwb(cwbOrder.getCwb());
				orderDto.setTranscwb(cwbOrder.getTranscwb());
				orderDto.setConsigneename(cwbOrder.getConsigneename());
				orderDto.setConsigneeaddress(cwbOrder.getConsigneeaddress());
				orderDto.setConsigneepostcode(cwbOrder.getConsigneepostcode());
				orderDto.setConsigneephone(cwbOrder.getConsigneephone());
				orderDto.setConsigneemobile(cwbOrder.getConsigneemobile());
				orderDto.setSendcargoname(cwbOrder.getSendcarname());
				orderDto.setBackcargoname(cwbOrder.getBackcarname());
				orderDto.setReceivablefee(cwbOrder.getReceivablefee());
				orderDto.setPaybackfee(cwbOrder.getPaybackfee());
				orderDto.setCargorealweight(cwbOrder.getCarrealweight());
				orderDto.setCargoamount(cwbOrder.getCaramount());
				orderDto.setCargotype(cwbOrder.getCartype());
				orderDto.setCargosize(cwbOrder.getCarsize());
				orderDto.setSendcargonum(cwbOrder.getSendcarnum());
				orderDto.setBackcargonum(cwbOrder.getBackcarnum());
				orderDto.setCwbordertypeid(cwbOrder.getCwbordertypeid());
				orderDto.setCwbdelivertypeid(Integer.valueOf(cwbOrder.getCwbdelivertypeid()));
				// orderDto.setWarehousename(branchDAO.getBranchByBranchid(Long.valueOf(cwbOrder.getCustomerwarehouseid())).getBranchname());
				// orderDto.setSendtime(sendtime);
				// orderDto.setOuttobranch(outtobranch);
				orderDto.setCustomercommand(cwbOrder.getCustomercommand());
				orderDto.setCustomerwarehouseid(cwbOrder.getCustomerwarehouseid());
				orderDto.setPaywayid(cwbOrder.getPaywayid());
				// CustomWareHouse
				// customWareHouse=customWareHouseDAO.getWarehouseId(Long.valueOf(cwbOrder.getCustomerwarehouseid()));
				if(!StringUtils.isEmpty(cwbOrder.getCarwarehouse())){
					orderDto.setWarehousename(branchDAO.getBranchByBranchid(Long.valueOf(cwbOrder.getCarwarehouse())).getBranchname());
				}else{
					orderDto.setWarehousename("");
				}
				orderDto.setCwbprovince(cwbOrder.getCwbprovince());
				orderDto.setCwbcity(cwbOrder.getCwbcity());
				orderDto.setCwbcounty(cwbOrder.getCwbcounty());
				orderDto.setRemark1(cwbOrder.getRemark1());
				orderDto.setRemark2(cwbOrder.getRemark2());//广州通路对接需要的发货时间（限广州通路）
				
				orderDto.setRemark4(cwbOrder.getRemark4());//供货商名称（广州通路）
				orderDto.setPaybackfee(cwbOrder.getPaybackfee());//应退款（广州通路）
				orderDto.setReceivablefee(cwbOrder.getReceivablefee());//应收款（广州通路）
				orderDto.setConsigneemobile(cwbOrder.getConsigneemobile());
				orderDto.setConsigneephone(cwbOrder.getConsigneephone());//广州通路接口中我把电话或手机都放在了这个字段中
				orderDto.setCustomerid(cwbOrder.getCustomerid());
				//发货时间？？
				orderDto.setConsigneenameOfkf(cwbOrder.getConsigneenameOfkf());
				orderDto.setConsigneemobileOfkf(cwbOrder.getConsigneemobileOfkf());
				orderDto.setConsigneephoneOfkf(cwbOrder.getConsigneephoneOfkf());
				orderDto.setShouldfare(cwbOrder.getShouldfare());
				orderDtoList.add(orderDto);
			} catch (Exception e) {
				logger.error("环形对接-根据订单号获取订单详细信息发生错误" + cwbOrder.getCwb(), e);
			}

		}
		return orderDtoList;
	}

	/**
	 * 根据订单号查询订单列表 根据订单号JSONArry请求
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/requestDMPorderService_feedback")
	public @ResponseBody String requestDMPorderService_feedback(HttpServletRequest request) {

		try {

			InputStream input = new BufferedInputStream(request.getInputStream());
			String jsoncwbarr = Dom4jParseUtil.getStringByInputStream(input); // 读取文件流，获得xml字符串

			logger.info("上游-oms请求dmp-信息xml={}", jsoncwbarr);

			CommenSendData commenSendData = JacksonMapper.getInstance().readValue(jsoncwbarr, CommenSendData.class);

			commonCoreService.orderFlowFeedback(commenSendData);
			return "SUCCESS";
		} catch (Exception e) {
			logger.error("上游dmp接收状态回传发生异常", e);
			return "dmp接收反馈状态异常:" + e.getMessage();
		}

	}

	/**
	 * 唯速达签收结果反馈
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/weisudaFeedback")
	public @ResponseBody String weisudaFeedback(HttpServletRequest request) {

		try {

			InputStream input = new BufferedInputStream(request.getInputStream());
			String datajson = Dom4jParseUtil.getStringByInputStream(input); // 读取文件流，获得xml字符串

			logger.info("唯速达请求dmp-反馈信息={}", datajson);

			weisudaService.dealwith_fankui(datajson);
			return "SUCCESS";
		} catch (Exception e) {
			logger.error("处理唯速达反馈请求异常", e);
			return "处理唯速达反馈请求异常:" + e.getMessage();
		}

	}

	@RequestMapping("/getStoresById/{id}")
	public @ResponseBody String getStoresById(@PathVariable("id") int id) {
		return JSONObject.fromObject(storesDAO.getMaisiBranchById(id)).toString();
	}

	/*
	 * 承运商全线快递
	 */

	@RequestMapping("/getReasonidJointByB2c/{extpt_code}/{customerid}")
	public @ResponseBody String getReasonidJointByB2c(@PathVariable("extpt_code") long extpt_code, @PathVariable("customerid") String customerid) {
		String s = String.valueOf(extpt_code);
		int code = Integer.parseInt(s);
		return JSONObject.fromObject(commonExptDao.getExpMatchListByKeyEditAndName(code, customerid)).toString();
	}
	/**
	 * 广州通路需要添加的
	 * @param extpt_code
	 * @param customerid
	 * @return
	 */
	@RequestMapping("/getReasonidJointByB2cGztl/{customerid}")
	public @ResponseBody String getReasonid(
			@PathVariable("customerid") String customerid,
			@RequestParam(value = "code", required = false, defaultValue = "") String extpt_code) {
		try {
			 return JSONObject.fromObject(commonExptDao.getExceptReason(extpt_code, customerid)).toString();
		} catch (Exception e) {
			logger.error("", e);
			return null;
		}
	}
	@RequestMapping("/getCommonByCommonnumber/{commonnumber}")
	public @ResponseBody String UpdateRemarkbycwb(@PathVariable("commonnumber") String commonnumber) {
		return JSONObject.fromObject(commonDAO.getCommonByCommonnumber(commonnumber)).toString();
	}

	/**
	 * 查询单量统计接口
	 * 
	 * @param customerid
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 */
	@RequestMapping("/searchCwbStastics/")
	public @ResponseBody String searchCwbStastics(HttpServletRequest request) throws JsonGenerationException, JsonMappingException, IOException {
		String searchtime = request.getParameter("searchtime");

		List<Map<String, Object>> datalist = cwbDAO.getCwbStasticsByTime(searchtime);
		String resonseJson = JacksonMapper.getInstance().writeValueAsString(datalist);

		return resonseJson;
	}

	@RequestMapping("/getOrderGoods/{cwb}")
	public @ResponseBody String getOrderGoods(@PathVariable("cwb") String cwb) throws JsonGenerationException, JsonMappingException, IOException {
		return JacksonMapper.getInstance().writeValueAsString(orderGoodsDAO.getOrderGoodsList(cwb));
	}

	@RequestMapping("/getApplyEditDeliverystate/{cwb}")
	public @ResponseBody String getBranchById(@PathVariable("cwb") String cwb) {
		return JSONObject.fromObject(applyEditDeliverystateDAO.getApplyEditDeliverystateWithPass(cwb)).toString();//todo
	}
}
