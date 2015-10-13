package cn.explink.dao;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.explink.b2c.explink.code_down.EpaiApi;
import cn.explink.b2c.maisike.Stores;
import cn.explink.b2c.tools.ExptReason;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.domain.Branch;
import cn.explink.domain.Common;
import cn.explink.domain.CustomWareHouse;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrderCopyForDmp;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.DeliveryStateForDmp;
import cn.explink.domain.Exportmould;
import cn.explink.domain.ProxyConf;
import cn.explink.domain.Reason;
import cn.explink.domain.Remark;
import cn.explink.domain.Role;
import cn.explink.domain.SetExportField;
import cn.explink.domain.Switch;
import cn.explink.domain.SystemInstall;
import cn.explink.domain.User;
import cn.explink.service.ProxyService;
import cn.explink.util.Http;
import cn.explink.util.JSONReslutUtil;

@Component
public class GetDmpDAO {
	/*
	 * @Value("${godmp.dmpUrl}") String dmpUrl;
	 */
	@Autowired
	ProxyService proxyService;
	@Autowired
	ProxyConfDAO proxyConfDAO;
	private static ResourceBundle oms = ResourceBundle.getBundle("oms");
	private static String dmpUrl = GetDmpDAO.oms.getString("dmpUrl");
	private Logger logger = LoggerFactory.getLogger(GetDmpDAO.class);

	public String getDmpurl() {
		return GetDmpDAO.dmpUrl;
	}

	/**
	 * 获取当前站点id
	 *
	 * @param dmpid
	 * @return
	 */
	public long getNowBrancheId(String dmpid) {
		long branchid = 0;
		try {
			String branchStr = Http.post(GetDmpDAO.dmpUrl + "/OMSInterface/getNowBrancheId;jsessionid=" + dmpid, "");
			JSONObject jsonValue = JSONObject.fromObject(branchStr);
			branchid = jsonValue.getLong("nowbranchid");
		} catch (Exception e) {
			this.logger.error("获取当前机构id异常", e);
		}
		return branchid;
	}

	/**
	 * 获取当前用户真实姓名
	 *
	 * @param dmpid
	 * @return
	 */
	public String getNowRealname(String dmpid) {
		String realName = "";
		try {
			String branchStr = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getNowRealname;jsessionid=" + dmpid, "UTF-8", "POST").toString();
			this.logger.debug("now branchRealname");
			JSONObject jsonValue = JSONObject.fromObject(branchStr);
			realName = jsonValue.get("nowRealname").toString();
		} catch (Exception e) {
			this.logger.error("获取当前机构名称异常", e);
		}
		return realName;
	}

	/**
	 * 获取当前用户id
	 *
	 * @param dmpid
	 * @return
	 */
	public int getNowUserId(String dmpid) {
		int nowUserId = 0;
		try {
			String branchStr = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getNowUserId;jsessionid=" + dmpid, "UTF-8", "POST").toString();
			JSONObject jsonValue = JSONObject.fromObject(branchStr);
			nowUserId = Integer.parseInt(jsonValue.get("nowUserId").toString());
		} catch (Exception e) {
			this.logger.error("获取当前用户id异常", e);
		}
		return nowUserId;
	}

	/**
	 * 获取用户导出手机号权限
	 *
	 * @param dmpid
	 * @return
	 */
	public int getNowUserShowPhoneFlag(String dmpid) {
		int nowShowPhoneFlag = 0;
		try {
			String branchStr = Http.post(GetDmpDAO.dmpUrl + "/OMSInterface/getNowUserShowPhoneFlag;jsessionid=" + dmpid, "");
			JSONObject jsonValue = JSONObject.fromObject(branchStr);
			nowShowPhoneFlag = Integer.parseInt(jsonValue.get("nowShowPhoneFlag").toString());
		} catch (Exception e) {
			this.logger.error("获取当前用户可导出手机号权限异常", e);
		}
		return nowShowPhoneFlag;
	}

	/**
	 * 按userid获取用户
	 *
	 * @param id
	 * @return
	 */
	public User getUserById(long id) {
		User user = new User();
		JSONArray jSONArray = new JSONArray();
		try {
			String userStr = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getUserByid/" + id, "UTF-8", "POST").toString();
			jSONArray = JSONArray.fromObject(userStr);
			String delivermancode = jSONArray.getJSONObject(0).get("deliverManCode") == null ? "" : jSONArray.getJSONObject(0).getString("deliverManCode");
			user.setBranchid(jSONArray.getJSONObject(0).getLong("branchid"));
			user.setBranchid(jSONArray.getJSONObject(0).getLong("branchid"));
			user.setEmployeestatus(jSONArray.getJSONObject(0).getInt("employeestatus"));
			user.setIdcardno(jSONArray.getJSONObject(0).getString("idcardno"));
			user.setRealname(jSONArray.getJSONObject(0).getString("realname"));
			user.setRoleid(jSONArray.getJSONObject(0).getLong("roleid"));
			user.setShowphoneflag(jSONArray.getJSONObject(0).getLong("showphoneflag"));
			user.setShownameflag(jSONArray.getJSONObject(0).getLong("shownameflag"));
			user.setShowmobileflag(jSONArray.getJSONObject(0).getLong("showmobileflag"));
			user.setUserDeleteFlag(jSONArray.getJSONObject(0).getLong("userDeleteFlag"));
			user.setUseraddress(jSONArray.getJSONObject(0).getString("useraddress"));
			user.setUsercustomerid(jSONArray.getJSONObject(0).getLong("usercustomerid"));
			user.setUseremail(jSONArray.getJSONObject(0).getString("useremail"));
			user.setUserid(jSONArray.getJSONObject(0).getLong("userid"));
			user.setUsermobile(jSONArray.getJSONObject(0).getString("usermobile"));
			user.setUsername(jSONArray.getJSONObject(0).getString("username"));
			user.setUserphone(jSONArray.getJSONObject(0).getString("userphone"));
			user.setUserremark(jSONArray.getJSONObject(0).getString("userremark"));
			user.setUsersalary(new BigDecimal(jSONArray.getJSONObject(0).get("usersalary") == null ? "0.00" : jSONArray.getJSONObject(0).getString("usersalary")));
			user.setUserwavfile(jSONArray.getJSONObject(0).getString("userwavfile"));
			user.setDeliverManCode(delivermancode);
		} catch (Exception e) {
			this.logger.error("获取当前用户信息异常", e);
		}

		return user;
	}

	/**
	 * 查询所有站点类型的站点
	 *
	 * @return 返回站点名和站点id
	 */
	public List<Branch> getBranchByZhanDian() {
		JSONArray jSONArray = new JSONArray();
		List<Branch> list = null;
		String branchByZhanDian = "";
		try {
			branchByZhanDian = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getBranchByZhanDian", "UTF-8", "POST").toString();
			jSONArray = JSONArray.fromObject(branchByZhanDian);
			list = new ArrayList<Branch>();
			for (int i = 0; i < jSONArray.size(); i++) {
				Branch branch = new Branch();
				branch.setBranchid(jSONArray.getJSONObject(i).getLong("branchid"));
				branch.setBranchname(jSONArray.getJSONObject(i).getString("branchname"));
				branch.setBranchaddress(jSONArray.getJSONObject(i).getString("branchaddress"));
				branch.setSitetype(jSONArray.getJSONObject(i).getInt("sitetype"));
				branch.setBindmsksid(jSONArray.getJSONObject(i).getInt("bindmsksid"));
				list.add(branch);
			}

		} catch (Exception e) {
			list = null;
			this.logger.error("获取所有站点异常", e);
		}
		return list;
	}

	/**
	 * 查询所有站点类型的站点
	 *
	 * @return 返回站点名,站点id,站点类型
	 */
	public List<Branch> getBranchByAllZhanDian() {
		JSONArray jSONArray = new JSONArray();
		List<Branch> list = new ArrayList<Branch>();
		String branchByZhanDian = "";
		try {
			branchByZhanDian = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getBranchByAllZhanDian", "UTF-8", "POST").toString();
			jSONArray = JSONArray.fromObject(branchByZhanDian);
			list = new ArrayList<Branch>();
			for (int i = 0; i < jSONArray.size(); i++) {
				Branch branch = new Branch();
				branch.setBranchid(jSONArray.getJSONObject(i).getLong("branchid"));
				branch.setBranchname(jSONArray.getJSONObject(i).getString("branchname"));
				branch.setBranchaddress(jSONArray.getJSONObject(i).getString("branchaddress"));
				branch.setSitetype(jSONArray.getJSONObject(i).getInt("sitetype"));
				branch.setBindmsksid(jSONArray.getJSONObject(i).getInt("bindmsksid"));
				list.add(branch);
			}

		} catch (IOException e) {
			list = new ArrayList<Branch>();
			this.logger.error("查询所有站点类型的站点 返回站点名,站点id,站点类型", e);
		}
		return list;
	}

	/**
	 * 查询所有启用的站点类型机构
	 *
	 * @return
	 */
	public List<Branch> getBranchByAllEffectZhanDian() {
		JSONArray jSONArray = new JSONArray();
		List<Branch> list = new ArrayList<Branch>();
		String branchByZhanDian = "";
		try {
			branchByZhanDian = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getBranchByAllEffectZhanDian", "UTF-8", "POST").toString();
			jSONArray = JSONArray.fromObject(branchByZhanDian);
			list = new ArrayList<Branch>();
			for (int i = 0; i < jSONArray.size(); i++) {
				Branch branch = new Branch();
				branch.setBranchid(jSONArray.getJSONObject(i).getLong("branchid"));
				branch.setBranchname(jSONArray.getJSONObject(i).getString("branchname"));
				branch.setBranchaddress(jSONArray.getJSONObject(i).getString("branchaddress"));
				branch.setSitetype(jSONArray.getJSONObject(i).getInt("sitetype"));
				branch.setBindmsksid(jSONArray.getJSONObject(i).getInt("bindmsksid"));
				list.add(branch);
			}

		} catch (IOException e) {
			list = new ArrayList<Branch>();
			this.logger.error("查询所有站点类型的站点 返回站点名,站点id,站点类型", e);
		}
		return list;
	}

	/**
	 * 获得可访问的站点
	 *
	 * @param userId
	 * @return
	 */
	public List<Branch> getAccessableBranch(long userId) {
		List<Branch> list = new ArrayList<Branch>();
		try {
			String branchByZhanDian = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getAccessableBranch/" + userId, "UTF-8", "POST").toString();
			JSONArray jSONArray = JSONArray.fromObject(branchByZhanDian);
			for (int i = 0; i < jSONArray.size(); i++) {
				Branch branch = new Branch();
				branch.setBranchid(jSONArray.getJSONObject(i).getLong("branchid"));
				branch.setBranchname(jSONArray.getJSONObject(i).getString("branchname"));
				branch.setBranchaddress(jSONArray.getJSONObject(i).getString("branchaddress"));
				branch.setSitetype(jSONArray.getJSONObject(i).getInt("sitetype"));
				branch.setBindmsksid(jSONArray.getJSONObject(i).getInt("bindmsksid"));
				list.add(branch);
			}

		} catch (IOException e) {
			list = new ArrayList<Branch>();
			this.logger.error("查询所有站点类型的站点 返回站点名,站点id,站点类型", e);
		}
		return list;
	}

	/**
	 * 查询所有库房
	 *
	 * @return
	 */
	public List<Branch> getBranchByKufang() {
		JSONArray jSONArray = new JSONArray();
		List<Branch> list = null;
		String branchByZhanDian = "";
		try {
			branchByZhanDian = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getBranchByKufang", "UTF-8", "POST").toString();
			this.logger.debug("branchAll");
			jSONArray = JSONArray.fromObject(branchByZhanDian);
			list = new ArrayList<Branch>();
			for (int i = 0; i < jSONArray.size(); i++) {
				Branch branch = new Branch();
				branch.setBranchid(jSONArray.getJSONObject(i).getLong("branchid"));
				branch.setBranchname(jSONArray.getJSONObject(i).getString("branchname"));
				branch.setBranchaddress(jSONArray.getJSONObject(i).getString("branchaddress"));
				branch.setSitetype(jSONArray.getJSONObject(i).getInt("sitetype"));
				branch.setBindmsksid(jSONArray.getJSONObject(i).getInt("bindmsksid"));
				list.add(branch);
			}

		} catch (Exception e) {
			list = null;
			this.logger.error("获取所有库房异常", e);
		}
		return list;
	}

	/**
	 * 查询所有机构
	 *
	 * @return
	 */
	public List<Branch> getAllBranchs() {
		JSONArray jSONArray = new JSONArray();
		List<Branch> list = null;
		String branchList = "";
		try {
			branchList = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getAllBranch", "UTF-8", "POST").toString();
			this.logger.debug("branchAll");
			jSONArray = JSONArray.fromObject(branchList);
			list = new ArrayList<Branch>();
			for (int i = 0; i < jSONArray.size(); i++) {
				Branch branch = new Branch();
				branch.setBranchid(jSONArray.getJSONObject(i).getLong("branchid"));
				branch.setBranchname(jSONArray.getJSONObject(i).getString("branchname"));
				branch.setBranchaddress(jSONArray.getJSONObject(i).getString("branchaddress"));
				branch.setSitetype(jSONArray.getJSONObject(i).getInt("sitetype"));
				branch.setBindmsksid(jSONArray.getJSONObject(i).getInt("bindmsksid"));
				list.add(branch);
			}

		} catch (Exception e) {
			list = null;
			this.logger.error("获取所有站点异常", e);
		}
		return list;
	}

	public Branch getBranchByBranchName(String branchname) {
		Branch branch = new Branch();
		try {
			String branchStr = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getBranchByBranchName", "branchname=" + branchname, "POST").toString();
			JSONObject jsonObject = JSONObject.fromObject(branchStr);
			branch.setBranchid(jsonObject.getLong("branchid"));
			branch.setBranchname(jsonObject.getString("branchname"));
			branch.setBranchaddress(jsonObject.getString("branchaddress"));
			branch.setSitetype(jsonObject.getInt("sitetype"));
			branch.setBindmsksid(jsonObject.getInt("bindmsksid"));
		} catch (IOException e) {
			branch = null;
			this.logger.error("获取当前站点的详细信息异常", e);
		}
		return branch == null ? new Branch() : branch;
	}

	public List<Branch> getBranchByBranchids(String branchids) {
		JSONArray jSONArray = new JSONArray();
		List<Branch> list = null;
		String branchList = "";
		try {
			branchList = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getBranchByBranchids/" + branchids, "UTF-8", "POST").toString();
			this.logger.debug("branchAll");
			jSONArray = JSONArray.fromObject(branchList);
			list = new ArrayList<Branch>();
			for (int i = 0; i < jSONArray.size(); i++) {
				Branch branch = new Branch();
				branch.setBranchid(jSONArray.getJSONObject(i).getLong("branchid"));
				branch.setBranchname(jSONArray.getJSONObject(i).getString("branchname"));
				branch.setBranchaddress(jSONArray.getJSONObject(i).getString("branchaddress"));
				branch.setSitetype(jSONArray.getJSONObject(i).getInt("sitetype"));
				branch.setBindmsksid(jSONArray.getJSONObject(i).getInt("bindmsksid"));
				list.add(branch);
			}

		} catch (Exception e) {
			list = null;
			this.logger.error("获取所有站点异常", e);
		}
		return list;
	}

	public String getBranchById(long id) {

		String branchbyidTojson = "";
		try {
			branchbyidTojson = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getBranchById/" + id, "UTF-8", "POST").toString();
		} catch (IOException e) {
			this.logger.error("获取所有指点站点异常", e);
		}

		return branchbyidTojson;
	}

	/**
	 * 按beanchid 查询站点
	 *
	 * @param id
	 * @return
	 */
	public Branch getNowBranch(long id) {
		Branch branch = new Branch();
		try {
			String branchStr = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getBranchById/" + id, "UTF-8", "POST").toString();
			JSONObject jsonObject = JSONObject.fromObject(branchStr);
			branch.setBranchid(jsonObject.getLong("branchid"));
			branch.setBranchname(jsonObject.getString("branchname"));
			branch.setBranchaddress(jsonObject.getString("branchaddress"));
			branch.setSitetype(jsonObject.getInt("sitetype"));
			branch.setBindmsksid(jsonObject.getInt("bindmsksid"));
			branch.setBranchphone(jsonObject.getString("branchphone"));
			branch.setBranchmobile(jsonObject.getString("branchmobile"));
			branch.setBranchprovince(jsonObject.getString("branchprovince"));
			branch.setBranchcity(jsonObject.getString("branchcity"));
			branch.setBrancharea(jsonObject.getString("brancharea"));
		} catch (IOException e) {
			branch = null;
			this.logger.error("获取当前站点的详细信息异常", e);
		}
		return branch == null ? new Branch() : branch;
	}

	/**
	 * 按branchname查询站点
	 *
	 * @param name
	 * @return
	 */
	public List<Branch> getBranchByName(String name) {
		JSONArray jSONArray = new JSONArray();
		List<Branch> list = null;
		String branchByZhanDian = "";
		try {
			branchByZhanDian = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getBranchByName", "name=" + name + "", "POST").toString();
			this.logger.debug("branchAll");
			jSONArray = JSONArray.fromObject(branchByZhanDian);
			list = new ArrayList<Branch>();
			for (int i = 0; i < jSONArray.size(); i++) {
				Branch branch = new Branch();
				branch.setBranchid(jSONArray.getJSONObject(i).getLong("branchid"));
				branch.setBranchname(jSONArray.getJSONObject(i).getString("branchname"));
				branch.setBranchaddress(jSONArray.getJSONObject(i).getString("branchaddress"));
				branch.setSitetype(jSONArray.getJSONObject(i).getInt("sitetype"));
				branch.setBindmsksid(jSONArray.getJSONObject(i).getInt("bindmsksid"));
				list.add(branch);
			}
		} catch (Exception e) {
			list = null;
			this.logger.error("按站点名称获取站点列表异常");
		}
		return list;
	}

	/**
	 * 查询 所有供货商
	 *
	 * @return
	 */
	public List<Customer> getAllCustomers() {
		JSONArray jSONArray = new JSONArray();
		List<Customer> list = new ArrayList<Customer>();
		String customerAll = "";
		try {
			customerAll = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getCustomer", "UTF-8", "POST").toString();
			jSONArray = JSONArray.fromObject(customerAll);
			for (int i = 0; i < jSONArray.size(); i++) {
				Customer customer = new Customer();
				customer.setCustomerid(jSONArray.getJSONObject(i).getLong("customerid"));
				customer.setCustomername(jSONArray.getJSONObject(i).getString("customername"));
				customer.setB2cEnum(jSONArray.getJSONObject(i).getString("b2cEnum"));
				customer.setCustomercode(jSONArray.getJSONObject(i).getString("customercode"));
				list.add(customer);
			}
		} catch (Exception e) {
			this.logger.error("获取所有供货商异常", e);
		}
		return list;

	}

	/**
	 * 查询 所有供货商
	 *
	 * @return
	 */
	public static List<Customer> getStaticAllCustomers() {
		JSONArray jSONArray = new JSONArray();
		List<Customer> list = new ArrayList<Customer>();
		String customerAll = "";
		try {
			customerAll = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getCustomer", "UTF-8", "POST").toString();

			jSONArray = JSONArray.fromObject(customerAll);

			for (int i = 0; i < jSONArray.size(); i++) {
				Customer customer = new Customer();
				customer.setCustomerid(jSONArray.getJSONObject(i).getLong("customerid"));
				customer.setCustomername(jSONArray.getJSONObject(i).getString("customername"));
				customer.setB2cEnum(jSONArray.getJSONObject(i).getString("b2cEnum"));
				list.add(customer);
			}
		} catch (Exception e) {
			return null;
		}
		return list;

	}

	/**
	 * 查询 所有供货商发货库房
	 *
	 * @return
	 */
	public List<CustomWareHouse> getCustomWareHouse() {
		JSONArray jSONArray = new JSONArray();
		List<CustomWareHouse> list = new ArrayList();
		String customerAll = "";
		try {
			customerAll = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getCustomWareHouse", "UTF-8", "POST").toString();
			this.logger.debug("get CustomWareHouse");
			jSONArray = JSONArray.fromObject(customerAll);

			for (int i = 0; i < jSONArray.size(); i++) {
				CustomWareHouse customer = new CustomWareHouse();
				customer.setWarehouseid(jSONArray.getJSONObject(i).getLong("warehouseid"));
				customer.setCustomerwarehouse(jSONArray.getJSONObject(i).getString("customerwarehouse"));
				list.add(customer);
			}
		} catch (Exception e) {
			this.logger.error("获取所有供货商发货库房异常", e);
		}
		return list;

	}

	/**
	 *
	 * @param customid
	 * @return
	 */
	public List<CustomWareHouse> getCustomWareHouseByCustom(long customid) {
		JSONArray jSONArray = new JSONArray();
		List<CustomWareHouse> list = new ArrayList();
		String customerAll = "";
		try {
			customerAll = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getCustomWareHouseByCustomid/" + customid, "UTF-8", "POST").toString();
			this.logger.debug("get CustomWareHouse");
			jSONArray = JSONArray.fromObject(customerAll);

			for (int i = 0; i < jSONArray.size(); i++) {
				CustomWareHouse customer = new CustomWareHouse();
				customer.setWarehouseid(jSONArray.getJSONObject(i).getLong("warehouseid"));
				customer.setCustomerwarehouse(jSONArray.getJSONObject(i).getString("customerwarehouse"));
				list.add(customer);
			}
		} catch (Exception e) {
			this.logger.error("获取供货商发货库房异常", e);
		}
		return list;

	}

	public CustomWareHouse getCustomWareHouseByid(String customid) {
		CustomWareHouse customerH = new CustomWareHouse();
		try {
			String customerStr = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getCustomWareHouseByid/" + customid, "UTF-8", "POST").toString();
			this.logger.debug(" get CustomWareHouse");
			if (!JSONObject.fromObject(customerStr).isEmpty()) {
				customerH.setWarehouseid(JSONObject.fromObject(customerStr).getLong("warehouseid"));
				customerH.setCustomerwarehouse(JSONObject.fromObject(customerStr).getString("customerwarehouse"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			this.logger.error("获取供货商发货库房异常", e);
			customerH = null;
		}
		return customerH;
	}

	public List<Customer> getCustomerByIds(String ids) {

		JSONArray jSONArray = new JSONArray();
		List<Customer> list = new ArrayList<Customer>();
		String customerAll = "";
		try {
			customerAll = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getCustomerByIds/" + ids, "UTF-8", "POST").toString();
			this.logger.debug(" get customerAll");
			jSONArray = JSONArray.fromObject(customerAll);

			for (int i = 0; i < jSONArray.size(); i++) {
				Customer customer = new Customer();
				customer.setCustomerid(jSONArray.getJSONObject(i).getLong("customerid"));
				customer.setCustomername(jSONArray.getJSONObject(i).getString("customername"));
				customer.setB2cEnum(jSONArray.getJSONObject(i).getString("b2cEnum"));
				list.add(customer);
			}
		} catch (Exception e) {
			this.logger.error("获取所有供货商异常", e);
		}
		return list;
	}

	/**
	 * 按供货商id查询供货商
	 *
	 * @param id
	 * @return
	 */
	public Customer getCustomer(long id) {
		Customer customer = new Customer();
		try {
			String customerStr = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getCustomerById/" + id, "UTF-8", "POST").toString();
			JSONObject jsonObject = JSONObject.fromObject(customerStr);

			customer.setCustomerid(jsonObject.getLong("customerid"));
			customer.setCustomername(jsonObject.getString("customername"));
			customer.setB2cEnum(jsonObject.getString("b2cEnum"));
			customer.setCustomeraddress(jsonObject.getString("customeraddress"));
			customer.setCustomercontactman(jsonObject.getString("customercontactman"));
			customer.setCustomerphone(jsonObject.getString("customerphone"));
			customer.setIfeffectflag(jsonObject.getLong("ifeffectflag"));
			customer.setCustomercode(jsonObject.getString("customercode"));
		} catch (Exception e) {
			this.logger.error("获取供货商异常", e);
		}
		return customer;
	}

	public CwbOrderCopyForDmp getCwbDetailsByCwb(String cwb) {
		CwbOrderCopyForDmp cwborder = new CwbOrderCopyForDmp();
		String reJson = "";
		try {
			this.logger.debug("get getCwbDetailsByCwb");
			reJson = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getCwbDetailsByCwb/" + cwb, "UTF-8", "POST").toString();
			JSONObject json = JSONObject.fromObject(JSONObject.fromObject(reJson).get("cwb"));
			cwborder = (CwbOrderCopyForDmp) JSONObject.toBean(json, CwbOrderCopyForDmp.class);

			JSONObject deliverJson = JSONObject.fromObject(JSONObject.fromObject(reJson).get("deliveryState"));
			if (deliverJson != null) {
				DeliveryState deliverState = (DeliveryState) JSONObject.toBean(json, DeliveryState.class);
				cwborder.setDeliverstate(deliverState);
			}

		} catch (Exception e) {
			this.logger.error("获取dmp订单失败 RE: orderFlow -----------订单号：" + cwb + "", e);
			cwborder = null;
		}
		return cwborder;
	}

	public DeliveryStateForDmp getCwbDetailsStateByCwb(String cwb) {
		DeliveryStateForDmp delivery = new DeliveryStateForDmp();
		String reJson = "";
		try {
			reJson = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getCwbDetailsByCwb/" + cwb, "UTF-8", "POST").toString();
			JSONObject json = JSONObject.fromObject(JSONObject.fromObject(reJson).get("deliveryState"));
			json.remove("mobilepodtime");
			delivery = (DeliveryStateForDmp) JSONObject.toBean(json, DeliveryStateForDmp.class);
		} catch (Exception e) {
			this.logger.error("获取DeliveryStateForDmp异常RE: orderFlow -----------订单号：" + cwb + "", e);
		}
		return delivery;
	}

	public String getDeliverById(long id) {

		String deliverByIdToJson = "";
		try {
			deliverByIdToJson = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getDeliverById/" + id, "UTF-8", "POST").toString();
		} catch (Exception e) {
			this.logger.error("获取deliverByIdToJson异常");
		}
		return deliverByIdToJson;
	}

	public String getUserByUserName(String userName) {

		String password = "";
		try {
			String deliverByIdToJson = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getUserByUserName/" + userName, "UTF-8", "POST").toString();
			JSONObject json = JSONObject.fromObject(deliverByIdToJson);
			password = json.getString("password");
		} catch (Exception e) {
			this.logger.error("获取deliverByIdToJson异常");
		}
		return password;
	}

	public List<Common> getAllCommons() {
		JSONArray jSONArray = new JSONArray();
		List<Common> list = new ArrayList();
		String commonAll = "";
		try {
			commonAll = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getCommon", "UTF-8", "POST").toString();
			jSONArray = JSONArray.fromObject(commonAll);
			for (int i = 0; i < jSONArray.size(); i++) {
				Common common = new Common();
				common.setId(jSONArray.getJSONObject(i).getLong("id"));
				common.setCommonname(jSONArray.getJSONObject(i).getString("commonname"));
				common.setCommonnumber(jSONArray.getJSONObject(i).getString("commonnumber"));
				common.setPageSize(jSONArray.getJSONObject(i).getLong("pageSize"));
				common.setPrivate_key(jSONArray.getJSONObject(i).getString("private_key"));
				common.setIsopenflag(jSONArray.getJSONObject(i).getLong("isopenflag"));
				common.setFeedback_url(jSONArray.getJSONObject(i).getString("feedback_url"));
				common.setPhone(jSONArray.getJSONObject(i).getString("phone"));
				common.setBranchid(jSONArray.getJSONObject(i).getLong("branchid"));
				common.setLoopcount(jSONArray.getJSONObject(i).getLong("loopcount"));
				common.setIsasynchronous(jSONArray.getJSONObject(i).getLong("isasynchronous"));
				list.add(common);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static List<Common> getStaticAllCommons() {
		JSONArray jSONArray = new JSONArray();
		List<Common> list = new ArrayList();
		String commonAll = "";
		try {
			commonAll = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getCommon", "UTF-8", "POST").toString();
			jSONArray = JSONArray.fromObject(commonAll);
			for (int i = 0; i < jSONArray.size(); i++) {
				Common common = new Common();
				common.setId(jSONArray.getJSONObject(i).getLong("id"));
				common.setCommonname(jSONArray.getJSONObject(i).getString("commonname"));
				common.setCommonnumber(jSONArray.getJSONObject(i).getString("commonnumber"));
				common.setPageSize(jSONArray.getJSONObject(i).getLong("pageSize"));
				common.setPrivate_key(jSONArray.getJSONObject(i).getString("private_key"));
				common.setIsopenflag(jSONArray.getJSONObject(i).getLong("isopenflag"));
				list.add(common);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public User getLogUser(String dmpid) {
		JSONObject jsonObject = new JSONObject();
		User u = new User();
		String user = "";
		try {
			user = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getLogUser;jsessionid=" + dmpid, "UTF-8", "POST").toString();
			if ("[]".equals(user)) {
				this.logger.error("获取[]登录用户失败,登录失效了");
				return u;
			}
			jsonObject = JSONObject.fromObject(user);
			u.setUserid(jsonObject.getLong("userid"));
			u.setBranchid(jsonObject.getLong("branchid"));
			u.setUsername(jsonObject.getString("username"));
			u.setRealname(jsonObject.getString("realname"));
			u.setRoleid(jsonObject.getInt("roleid"));
		} catch (Exception e) {
			this.logger.error("获取登录用户失败,登录失效了");
		}
		return u;
	}

	public String getEMSType(String emsOrder) {
		ProxyConf defualtproxyConf = this.proxyConfDAO.getDefualtProxy();
		String emsType = "";// ET365852853CS
		SocketAddress socketAddress = null;
		try {
			if (defualtproxyConf != null) {
				Socket socket1 = new Socket(defualtproxyConf.getIp(), defualtproxyConf.getPort());
				socketAddress = socket1.getRemoteSocketAddress();
				socket1.close();
				this.logger.debug("获取数据库默认的代理ip:" + defualtproxyConf.getIp());
			} else {
				Socket socket1 = new Socket("161.139.195.98", 80);
				socketAddress = socket1.getRemoteSocketAddress();
				socket1.close();
				this.logger.debug("获取的程序写死默认代理ip:161.139.195.98");
			}
			emsType = JSONReslutUtil.getResultMessageShort(socketAddress,
					"http://www.kuaidi100.com/query?type=ems&postid=" + emsOrder + "&id=1&valicode=&temp=0.0015379865653812885&sessionid=&tmp=0.6559703338425606", "UTF-8", "POST").toString();
		} catch (IOException e) {
			this.logger.debug(("代理ip:" + defualtproxyConf) == null ? defualtproxyConf.getIp() : "161.139.195.98" + "  EMS抓取result:" + emsOrder + ":连接超时");
		}
		return emsType;
	}

	public String getEMSTypeByLong(String emsOrder) {
		SocketAddress socketAddress = this.proxyService.getNextProxy();
		String emsType = "";// ET365852853CS
		try {
			emsType = JSONReslutUtil.getResultMessageByProxy(socketAddress,
					"http://www.kuaidi100.com/query?type=ems&postid=" + emsOrder + "&id=1&valicode=&temp=0.0015379865653812885&sessionid=&tmp=0.6559703338425606", "UTF-8", "POST").toString();
		} catch (IOException e) {
			this.logger.debug("EMS抓取result:" + emsOrder + ":连接超时");
			ProxyConf p = this.proxyConfDAO.getProxyNowUse(1);
			this.proxyService.removProxy(p);
		}
		return emsType;
	}

	public String getCommonById(long id) {

		String commonByIdToJson = "";
		try {
			commonByIdToJson = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getCommonById/" + id, "UTF-8", "POST").toString();
		} catch (IOException e) {
			this.logger.error("获取指定承运商异常");
		}
		return commonByIdToJson;
	}

	public JointEntity getJointEntity(int jointnum) {

		JointEntity jointEntity = new JointEntity();
		try {
			String jointEntityStr = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getJointEntity/" + jointnum, "UTF-8", "POST").toString();
			JSONObject jsonObject = JSONObject.fromObject(jointEntityStr);
			if ((jsonObject == null) || jsonObject.isEmpty()) {
				return new JointEntity();
			}
			jointEntity.setJoint_num(jsonObject.getInt("joint_num"));
			jointEntity.setJoint_property(jsonObject.getString("joint_property"));
			jointEntity.setState(jsonObject.getInt("state"));
		} catch (Exception e) {
			this.logger.error("获取指定JointEntity异常", e);
		}
		return jointEntity == null ? new JointEntity() : jointEntity;
	}

	public List<JointEntity> getJointEntityList() {
		List<JointEntity> jointlist = new ArrayList<JointEntity>();
		try {
			String jointEntityStr = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getJointEntityList/", "UTF-8", "POST").toString();
			JSONArray jSONArray = JSONArray.fromObject(jointEntityStr);
			if ((jSONArray != null) && (jSONArray.size() > 0)) {
				for (int i = 0; i < jSONArray.size(); i++) {
					JointEntity jointEntity = new JointEntity();
					jointEntity.setJoint_num(jSONArray.getJSONObject(i).getInt("joint_num"));
					jointEntity.setJoint_property(jSONArray.getJSONObject(i).getString("joint_property"));
					jointEntity.setState(jSONArray.getJSONObject(i).getInt("state"));
					jointlist.add(jointEntity);
				}
			}
		} catch (Exception e) {
			this.logger.error("获取指定List<JointEntity>异常", e);
		}
		return jointlist;
	}

	/**
	 * 针对b2c对接中用到
	 *
	 * @param jointnum
	 * @return
	 */
	public JointEntity getJointEntityByCompanyname(String companyname) {

		JointEntity jointEntity = null;
		try {
			String jointEntityStr = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getJointEntityByCompany/" + companyname, "UTF-8", "POST").toString();
			JSONObject jsonObject = JSONObject.fromObject(jointEntityStr);
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(jsonObject.getInt("joint_num"));
			jointEntity.setJoint_property(jsonObject.getString("joint_property"));
			jointEntity.setState(jsonObject.getInt("state"));

		} catch (Exception e) {
			this.logger.error("获取指定getJointEntityByCompanyname异常");
		}
		return jointEntity;
	}

	public JointEntity getJointEntityByClientID(String ClientID) {

		JointEntity jointEntity = null;
		try {
			String jointEntityStr = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getJointDaoByClientID/" + ClientID, "UTF-8", "POST").toString();
			JSONObject jsonObject = JSONObject.fromObject(jointEntityStr);
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(jsonObject.getInt("joint_num"));
			jointEntity.setJoint_property(jsonObject.getString("joint_property"));
			jointEntity.setState(jsonObject.getInt("state"));
		} catch (Exception e) {
			this.logger.error("获取指定getJointDaoByClientID异常");
		}
		return jointEntity;
	}

	public List<Exportmould> getExportmoulds(User user, String dmpid) {
		JSONArray jSONArray = new JSONArray();
		List<Exportmould> list = new ArrayList<Exportmould>();
		String exportmouldAll = "";
		try {
			exportmouldAll = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getExportMoulds/" + user.getRoleid() + ";jsessionid=" + dmpid, "UTF-8", "POST").toString();
			jSONArray = JSONArray.fromObject(exportmouldAll);
			for (int i = 0; i < jSONArray.size(); i++) {
				Exportmould exportmould = new Exportmould();
				exportmould.setId(jSONArray.getJSONObject(i).getInt("id"));
				exportmould.setRolename(jSONArray.getJSONObject(i).getString("rolename"));
				exportmould.setRoleid(jSONArray.getJSONObject(i).getInt("roleid"));
				exportmould.setMouldname(jSONArray.getJSONObject(i).getString("mouldname"));
				exportmould.setMouldfieldids(jSONArray.getJSONObject(i).getString("mouldfieldids"));
				exportmould.setStatus(jSONArray.getJSONObject(i).getInt("status"));
				list.add(exportmould);
			}
		} catch (Exception e) {
			this.logger.error("获取指定getExportmoulds异常");
		}
		return list;
	}

	/**
	 * 根据参数获取导出excel列
	 *
	 * @param strs
	 *            逗号隔开
	 * @return
	 */
	public List<SetExportField> getSetExportFieldByExportstate(String strs) {
		JSONArray jSONArray = new JSONArray();
		List<SetExportField> list = new ArrayList<SetExportField>();
		String exportmouldAll = "";
		try {
			exportmouldAll = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getSetExportFieldByStrs/" + strs, "UTF-8", "POST").toString();
			jSONArray = JSONArray.fromObject(exportmouldAll);
			for (int i = 0; i < jSONArray.size(); i++) {
				SetExportField setExportField = new SetExportField();
				setExportField.setId(jSONArray.getJSONObject(i).getInt("id"));
				setExportField.setFieldname(jSONArray.getJSONObject(i).getString("fieldname"));
				setExportField.setExportstate(jSONArray.getJSONObject(i).getLong("exportstate"));
				setExportField.setFieldenglishname(jSONArray.getJSONObject(i).getString("fieldenglishname"));
				setExportField.setExportdatatype(jSONArray.getJSONObject(i).getString("exportdatatype"));
				list.add(setExportField);
			}
		} catch (Exception e) {
			this.logger.error("获取指定getSetExportFieldByExportstate异常");
		}
		return list;
	}

	public List<User> getAllUsers() {
		JSONArray jSONArray = new JSONArray();
		List<User> list = null;
		String userList = "";
		try {
			userList = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getAllUsers", "UTF-8", "POST").toString();
			this.logger.debug("userList :");
			jSONArray = JSONArray.fromObject(userList);
			list = new ArrayList<User>();
			for (int i = 0; i < jSONArray.size(); i++) {
				User user = new User();
				user.setUserid(jSONArray.getJSONObject(i).getLong("userid"));
				user.setUsername(jSONArray.getJSONObject(i).getString("username"));
				user.setRealname(jSONArray.getJSONObject(i).getString("realname"));
				user.setUsermobile(jSONArray.getJSONObject(i).getString("usermobile"));
				user.setUserphone(jSONArray.getJSONObject(i).getString("userphone"));
				list.add(user);
			}

		} catch (Exception e) {
			list = null;
			this.logger.error("获取指定getAllUsers异常");
		}
		return list;
	}

	public List<User> getAllDeliver(long branchid) {
		JSONArray jSONArray = new JSONArray();
		List<User> list = null;
		String userList = "";
		try {
			userList = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getDeliverListByBranch/" + branchid, "UTF-8", "POST").toString();
			this.logger.debug("userList :");
			jSONArray = JSONArray.fromObject(userList);
			list = new ArrayList<User>();
			for (int i = 0; i < jSONArray.size(); i++) {
				User user = new User();
				user.setUserid(jSONArray.getJSONObject(i).getLong("userid"));
				user.setUsername(jSONArray.getJSONObject(i).getString("username"));
				user.setRealname(jSONArray.getJSONObject(i).getString("realname"));
				list.add(user);
			}

		} catch (Exception e) {
			list = null;
			this.logger.error("获取指定getAllDeliver异常");
		}
		return list;
	}

	public List<User> getAllUserByBranchIds(String branchids) {
		JSONArray jSONArray = new JSONArray();
		List<User> list = null;
		String userList = "";
		try {
			userList = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getAllUserByBranchIds/" + branchids, "UTF-8", "POST").toString();
			this.logger.debug("userList :");
			jSONArray = JSONArray.fromObject(userList);
			list = new ArrayList<User>();
			for (int i = 0; i < jSONArray.size(); i++) {
				User user = new User();
				user.setUserid(jSONArray.getJSONObject(i).getLong("userid"));
				user.setUsername(jSONArray.getJSONObject(i).getString("username"));
				user.setRealname(jSONArray.getJSONObject(i).getString("realname"));
				list.add(user);
			}

		} catch (Exception e) {
			list = null;
			this.logger.error("获取指定getAllDeliver异常");
		}
		return list;
	}

	public SystemInstall getSystemInstallByName(String name) {

		SystemInstall systemIn = null;
		try {
			String switchStr = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getSystemInstallByName/" + name, "UTF-8", "POST").toString();
			JSONObject jsonObject = JSONObject.fromObject(switchStr);
			systemIn = new SystemInstall();
			systemIn.setName(jsonObject.getString("name"));
			systemIn.setChinesename(jsonObject.getString("chinesename"));
			systemIn.setValue(jsonObject.getString("value"));
		} catch (Exception e) {
			this.logger.error("获取指定getSystemInstallByName异常", e);
		}
		return systemIn;
	}

	public Switch getSwitchBySwitchname(String switchname) {

		Switch swith = new Switch();
		try {
			String switchStr = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getSwitchBySwitchname/" + switchname, "UTF-8", "POST").toString();
			JSONObject jsonObject = JSONObject.fromObject(switchStr);
			swith.setId(jsonObject.getLong("id"));
			swith.setSwitchname(jsonObject.getString("switchname"));
			swith.setState(jsonObject.getString("state"));
		} catch (Exception e) {
			this.logger.error("获取指定getSwitchBySwitchname异常");
		}
		return swith;
	}

	public List<User> getDeliverListByCaiWu(long caiwubranchid) {

		List<User> userlist = new ArrayList<User>();
		JSONArray jSONArray = new JSONArray();
		try {
			String userStr = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getDeliverListByCaiwu/" + caiwubranchid, "UTF-8", "POST").toString();
			this.logger.debug("get userlist by cwiwu!");
			jSONArray = JSONArray.fromObject(userStr);
			if ((jSONArray != null) && (jSONArray.size() > 0)) {
				for (int i = 0; i < jSONArray.size(); i++) {
					User user = new User();
					user.setUserid(jSONArray.getJSONObject(i).getLong("userid"));
					user.setUsername(jSONArray.getJSONObject(i).getString("username"));
					user.setRealname(jSONArray.getJSONObject(i).getString("realname"));
					userlist.add(user);
				}
			}

		} catch (Exception e) {
			this.logger.error("获取指定getDeliverListByCaiWu异常");
		}

		return userlist;
	}

	public List<Branch> getBranchListByCawWu(long caiwubranchid) {
		JSONArray jSONArray = new JSONArray();
		List<Branch> list = null;
		String branchByZhanDian = "";
		try {
			branchByZhanDian = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getBranchListByCaiwu/" + caiwubranchid, "UTF-8", "POST").toString();
			this.logger.debug("branchAll");
			jSONArray = JSONArray.fromObject(branchByZhanDian);
			list = new ArrayList<Branch>();
			for (int i = 0; i < jSONArray.size(); i++) {
				Branch branch = new Branch();
				branch.setBranchid(jSONArray.getJSONObject(i).getLong("branchid"));
				branch.setBranchname(jSONArray.getJSONObject(i).getString("branchname"));
				list.add(branch);
			}
		} catch (Exception e) {
			list = null;
			this.logger.error("获取指定getBranchListByCawWu异常");
		}
		return list;
	}

	public List<Branch> getBranchListByCawWuAndUser(long caiwubranchid, long userid) {
		JSONArray jSONArray = new JSONArray();
		List<Branch> list = null;
		String branchByZhanDian = "";
		try {
			branchByZhanDian = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getBranchListByCaiwuAndUser/" + caiwubranchid + "?userid=" + userid, "UTF-8", "POST").toString();
			this.logger.debug("branchAll");
			jSONArray = JSONArray.fromObject(branchByZhanDian);
			list = new ArrayList<Branch>();
			for (int i = 0; i < jSONArray.size(); i++) {
				Branch branch = new Branch();
				branch.setBranchid(jSONArray.getJSONObject(i).getLong("branchid"));
				branch.setBranchname(jSONArray.getJSONObject(i).getString("branchname"));
				branch.setSitetype(jSONArray.getJSONObject(i).getInt("sitetype"));
				list.add(branch);
			}
		} catch (Exception e) {
			list = null;
			this.logger.error("获取指定getBranchListByCawWuAndUser异常");
		}
		return list;
	}

	public List<Branch> getBranchListByTypeAndUser(String type, long userid) {
		JSONArray jSONArray = new JSONArray();
		List<Branch> list = null;
		String branchByZhanDian = "";
		try {
			branchByZhanDian = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getBranchListByTypeAndUser/" + type + "?userid=" + userid, "UTF-8", "POST").toString();
			this.logger.debug("branchAll");
			jSONArray = JSONArray.fromObject(branchByZhanDian);
			list = new ArrayList<Branch>();
			for (int i = 0; i < jSONArray.size(); i++) {
				Branch branch = new Branch();
				branch.setBranchid(jSONArray.getJSONObject(i).getLong("branchid"));
				branch.setBranchname(jSONArray.getJSONObject(i).getString("branchname"));
				branch.setSitetype(jSONArray.getJSONObject(i).getInt("sitetype"));
				list.add(branch);
			}
		} catch (Exception e) {
			list = null;
			this.logger.error("获取指定getBranchListByTypeAndUser异常");
		}
		return list;
	}

	public String getReason(long id) {
		String reason = "";
		try {
			String reasonStr = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getReason/" + id, "UTF-8", "POST").toString();
			JSONObject json = JSONObject.fromObject(reasonStr);
			if ((json != null) && !json.isEmpty()) {
				reason = json.getString("reasoncontent");
			}
		} catch (Exception e) {
			this.logger.error("获取指定getBranchListByCawWu异常");
		}
		return reason;
	}

	public List<Role> getRoleList() {
		JSONArray jSONArray = new JSONArray();
		List<Role> list = null;
		String roleList = "";
		try {
			roleList = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getRoles", "UTF-8", "POST").toString();
			this.logger.debug("rolelist :");
			jSONArray = JSONArray.fromObject(roleList);
			list = new ArrayList<Role>();
			for (int i = 0; i < jSONArray.size(); i++) {
				Role role = new Role();
				role.setRoleid(jSONArray.getJSONObject(i).getLong("roleid"));
				role.setRolename(jSONArray.getJSONObject(i).getString("rolename"));
				role.setType(jSONArray.getJSONObject(i).getInt("type"));
				list.add(role);
			}

		} catch (Exception e) {
			list = null;
			this.logger.error("获取指定getAllUsers异常");
		}
		return list;
	}

	public Role getRoleByRoleid(long id) {

		Role r = new Role();
		try {
			String roleStr = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getRoleByRoleid/" + id, "UTF-8", "POST").toString();
			this.logger.debug("role");
			r = (Role) JSONObject.toBean(JSONObject.fromObject(roleStr), Role.class);
		} catch (Exception e) {
			this.logger.error("获取指定getSwitchBySwitchname异常");
		}
		return r;
	}

	/**
	 * 异常反馈reasonid
	 *
	 * @param reasonid
	 * @param expt_type
	 * @param customerid
	 * @return
	 */
	public ExptReason getReasonidJointByB2c(long code, String customerid, String cwb) {
		ExptReason exptReason = new ExptReason();

		try {
			String jointEntityStr = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getReasonidJointByB2c/" + code + "/" + customerid, "UTF-8", "POST").toString();
			JSONObject jparm = JSONObject.fromObject(jointEntityStr);
			if (jparm.isEmpty()) {
				exptReason.setExpt_code("");
			} else {
				exptReason.setExpt_code((jparm.get("reasonid") != null) && !"".equals(jparm.getString("reasonid")) ? jparm.getString("reasonid") : "");

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			this.logger.error("获取指定getExptCodeJointByB2c异常");
		}
		return exptReason == null ? new ExptReason() : exptReason;
	}

	/**
	 * 异常反馈的时候查询该供货商对应的异常信息
	 *
	 * @param reasonid
	 * @param expt_type
	 * @param customerid
	 * @return
	 */
	public ExptReason getExptCodeJointByB2c(long reasonid, int expt_type, String customerid) {
		ExptReason exptReason = new ExptReason();

		try {
			String jointEntityStr = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getExptCodeJointByB2c/" + reasonid + "/" + expt_type + "/" + customerid, "UTF-8", "POST")
					.toString();
			JSONObject jparm = JSONObject.fromObject(jointEntityStr);

			exptReason.setExpt_code((jparm.get("expt_code") != null) && !"".equals(jparm.getString("expt_code")) ? jparm.getString("expt_code") : "");
			exptReason.setExpt_msg((jparm.get("expt_msg") != null) && !"".equals(jparm.getString("expt_msg")) ? jparm.getString("expt_msg") : "");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			this.logger.error("获取指定getExptCodeJointByB2c异常");
		}
		this.logger.debug("OMS请求接口:reasonid：" + reasonid + ";expt_type:" + expt_type + ";support_key:" + customerid);
		this.logger.debug("OMS对接请求接口路径:" + GetDmpDAO.dmpUrl + "/OMSInterface/getExptCodeJointByB2c/" + reasonid + "/" + expt_type + "/" + customerid);
		return exptReason == null ? new ExptReason() : exptReason;
	}

	/**
	 * 异常反馈reasonid
	 *
	 * @param reasonid
	 * @param expt_type
	 * @param customerid
	 * @return
	 */
	public ExptReason getReasonidJointByB2c(long code, String customerid) {
		ExptReason exptReason = new ExptReason();

		try {
			String jointEntityStr = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getReasonidJointByB2c/" + code + "/" + Long.valueOf(customerid), "UTF-8", "POST").toString();
			JSONObject jparm = JSONObject.fromObject(jointEntityStr);

			exptReason.setExpt_code((jparm.get("reasonid") != null) && !"".equals(jparm.getString("reasonid")) ? jparm.getString("reasonid") : "");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			this.logger.error("获取指定getExptCodeJointByB2c异常");
		}
		this.logger.debug("OMS请求接口:reasonid：" + code + ";expt_type:" + customerid);
		this.logger.debug("OMS对接请求接口路径:" + GetDmpDAO.dmpUrl + "/OMSInterface/getReasonidJointByB2c/" + code + "/" + customerid + "/" + customerid);
		return exptReason == null ? new ExptReason() : exptReason;
	}

	// 处理到这
	public List<Branch> getQueryBranchByBranchsiteAndUserid(long userid, String sitetype) {
		JSONArray jSONArray = new JSONArray();
		List<Branch> list = new ArrayList<Branch>();
		String resultList = "";
		try {
			resultList = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getQueryBranchByBranchsiteAndUserid/" + userid + "/" + sitetype + "", "UTF-8", "POST").toString();
			jSONArray = JSONArray.fromObject(resultList);
			for (int i = 0; i < jSONArray.size(); i++) {
				list.add((Branch) JSONObject.toBean(jSONArray.getJSONObject(i), Branch.class));
			}

		} catch (Exception e) {
			list = null;
			this.logger.error("获取指定getQueryBranchByBranchsiteAndUserid异常", e);
		}
		return list;
	}

	public List<Branch> getBranchListByUser(long userid) {
		JSONArray jSONArray = new JSONArray();
		List<Branch> list = new ArrayList<Branch>();
		String resultList = "";
		try {
			resultList = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getBranchListByUser/" + userid, "UTF-8", "POST").toString();
			jSONArray = JSONArray.fromObject(resultList);
			for (int i = 0; i < jSONArray.size(); i++) {
				list.add((Branch) JSONObject.toBean(jSONArray.getJSONObject(i), Branch.class));
			}
		} catch (Exception e) {
			list = null;
			this.logger.error("获取指定getBranchListByUser异常", e);
		}
		return list;
	}

	public List<CustomWareHouse> getCustomWareHouseByCustomerid(long customerid) {
		JSONArray jSONArray = new JSONArray();
		List<CustomWareHouse> list = new ArrayList<CustomWareHouse>();
		String resultList = "";
		try {
			resultList = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getCustomWareHouseByCustomerid/" + customerid + "", "UTF-8", "POST").toString();
			jSONArray = JSONArray.fromObject(resultList);
			for (int i = 0; i < jSONArray.size(); i++) {
				list.add((CustomWareHouse) JSONObject.toBean(jSONArray.getJSONObject(i), CustomWareHouse.class));
			}

		} catch (Exception e) {
			this.logger.error("获取指定getCustomWareHouseByCustomerid异常", e);
		}
		return list;

	}

	public List<Reason> getAllReason() {
		JSONArray jSONArray = new JSONArray();
		List<Reason> list = new ArrayList<Reason>();
		String resultList = "";
		try {
			resultList = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getAllReason", "UTF-8", "POST").toString();
			jSONArray = JSONArray.fromObject(resultList);
			for (int i = 0; i < jSONArray.size(); i++) {
				list.add((Reason) JSONObject.toBean(jSONArray.getJSONObject(i), Reason.class));
			}

		} catch (Exception e) {
			this.logger.error("获取指定getAllReason异常", e);
		}
		return list;
	}

	public List<User> getAllUserbybranchid(long branchid) {
		JSONArray jSONArray = new JSONArray();
		List<User> list = new ArrayList<User>();
		try {
			String userStr = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getAllUserbybranchid/" + branchid, "UTF-8", "POST").toString();
			jSONArray = JSONArray.fromObject(userStr);
			for (int i = 0; i < jSONArray.size(); i++) {
				list.add((User) JSONObject.toBean(jSONArray.getJSONObject(i), User.class));
			}
		} catch (Exception e) {
			this.logger.error("按站点获取用户List异常", e);
		}

		return list;
	}

	public Map<Long, Customer> getAllCustomersToMap() {
		Map<Long, Customer> customerMap = new HashMap<Long, Customer>();
		for (Customer customer : this.getAllCustomers()) {
			customerMap.put(customer.getCustomerid(), customer);
		}
		return customerMap;
	}

	public List<Remark> getAllRemark() {
		JSONArray jSONArray = new JSONArray();
		List<Remark> list = new ArrayList<Remark>();
		String remarkAll = "";
		try {
			remarkAll = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getAllRemark", "UTF-8", "POST").toString();
			jSONArray = JSONArray.fromObject(remarkAll);
			for (int i = 0; i < jSONArray.size(); i++) {
				Remark remark = new Remark();
				remark.setCwb(jSONArray.getJSONObject(i).getString("cwb"));
				remark.setRemark(jSONArray.getJSONObject(i).getString("remark"));
				remark.setCreatetime(jSONArray.getJSONObject(i).getString("createtime"));
				remark.setUsername(jSONArray.getJSONObject(i).getString("username"));
				remark.setRemarktype(jSONArray.getJSONObject(i).getString("remarktype"));

				list.add(remark);
			}
		} catch (Exception e) {
			this.logger.error("获取所有备注异常");
		}
		return list;
	}

	public String getFileUrl() {
		try {
			String jointEntityStr = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getFileUrl", "UTF-8", "POST").toString();
			JSONObject jparm = JSONObject.fromObject(jointEntityStr);
			return jparm.getString("fileUrl");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return "";
		}

	}

	/**
	 * 获取dmp 易派对接
	 *
	 * @param id
	 * @return
	 */
	public List<EpaiApi> getDMPEpaiAPI() {
		JSONArray jSONArray = new JSONArray();
		List<EpaiApi> list = new ArrayList<EpaiApi>();
		String resultList = "";
		try {
			resultList = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getEpaiAPI", "UTF-8", "POST").toString();
			jSONArray = JSONArray.fromObject(resultList);
			for (int i = 0; i < jSONArray.size(); i++) {
				list.add((EpaiApi) JSONObject.toBean(jSONArray.getJSONObject(i), EpaiApi.class));
			}

		} catch (Exception e) {
			this.logger.error("获取指定ExptReason异常", e);
		}
		return list;
	}

	/**
	 * 获取dmp 易派对接
	 *
	 * @param type
	 *            异常类型 2滞留, 3拒收
	 * @return
	 */
	public List<ExptReason> getDMPExptReason(String customerid, String type) {
		JSONArray jSONArray = new JSONArray();
		List<ExptReason> list = new ArrayList<ExptReason>();
		String resultList = "";
		try {
			resultList = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getExptB2cSetUp/" + customerid + "/" + type, "UTF-8", "POST").toString();
			jSONArray = JSONArray.fromObject(resultList);
			for (int i = 0; i < jSONArray.size(); i++) {
				list.add((ExptReason) JSONObject.toBean(jSONArray.getJSONObject(i), ExptReason.class));
			}

		} catch (Exception e) {
			this.logger.error("获取指定ExptReason异常", e);
		}
		return list;
	}

	/**
	 * 根据订单号集合返回订单详细信息
	 *
	 * @param
	 * @return
	 */
	public String getDMPOrdersByCwbs(String content) {

		try {

			return JSONReslutUtil.SendHttptoServer(content, GetDmpDAO.dmpUrl + "/OMSInterface/getOrdersByJsonCwbArr");

		} catch (Exception e) {
			this.logger.error("获取指定dmp上游信息异常", e);
		}
		return null;
	}

	/**
	 * 请求dmp反馈订单 分站到货，领货，反馈，审核
	 *
	 * @param
	 * @return
	 */
	public String requestDMPOrderService_feedback(String cwbJson) {

		try {
			// return JSONReslutUtil.getResultMessage( dmpUrl
			// +"/OMSInterface/requestDMPorderService_feedback",cwbJson,"POST").toString();
			return JSONReslutUtil.SendHttptoServer(cwbJson, GetDmpDAO.dmpUrl + "/OMSInterface/requestDMPorderService_feedback");
		} catch (Exception e) {
			this.logger.error("获取指定detail表异常", e);
		}
		return null;
	}

	public String requestDMPOrderService_Weisuda(String cwbJson) {

		try {
			// return JSONReslutUtil.getResultMessage( dmpUrl
			// +"/OMSInterface/requestDMPorderService_feedback",cwbJson,"POST").toString();
			return JSONReslutUtil.SendHttptoServer(cwbJson, GetDmpDAO.dmpUrl + "/OMSInterface/weisudaFeedback");
		} catch (Exception e) {
			this.logger.error("获取指定detail表异常", e);
		}
		return null;
	}

	public Stores getStoresById(long id) {

		Stores stores = new Stores();
		try {
			String branchStr = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getStoresById/" + id, "UTF-8", "POST").toString();
			stores = (Stores) JSONObject.toBean(JSONObject.fromObject(branchStr), Stores.class);
		} catch (IOException e) {
			this.logger.error("获取迈思可站点信息表的详细信息异常", e);
		}
		return stores;
	}

	public Common getCommonByCommonnumber(String Commonnumber) {
		try {
			String branchStr = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getCommonByCommonnumber/" + Commonnumber, "UTF-8", "POST").toString();
			Common common = (Common) JSONObject.toBean(JSONObject.fromObject(branchStr), Common.class);
			return common;
		} catch (IOException e) {
			this.logger.error("根据commonnumber获取承运商信息异常", e);
			return null;
		}
	}

	public List<User> getUserForALL() {
		JSONArray jSONArray = new JSONArray();
		List<User> list = null;
		String userList = "";
		try {
			userList = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getUserForALL", "UTF-8", "POST").toString();
			this.logger.debug("userList :");
			jSONArray = JSONArray.fromObject(userList);
			list = new ArrayList<User>();
			for (int i = 0; i < jSONArray.size(); i++) {
				User user = new User();
				user.setUserid(jSONArray.getJSONObject(i).getLong("userid"));
				user.setUsername(jSONArray.getJSONObject(i).getString("username"));
				user.setRealname(jSONArray.getJSONObject(i).getString("realname"));
				user.setUsermobile(jSONArray.getJSONObject(i).getString("usermobile"));
				user.setUserphone(jSONArray.getJSONObject(i).getString("userphone"));
				user.setEmployeestatus(jSONArray.getJSONObject(i).getInt("employeestatus"));
				list.add(user);
			}

		} catch (Exception e) {
			list = null;
			this.logger.error("获取指定getAllUsers异常");
		}
		return list;
	}

	public String getOrderGoods(String cwb) {
		try {
			String branchStr = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getOrderGoods/" + cwb, "UTF-8", "POST").toString();
			return branchStr;
		} catch (Exception e) {
			this.logger.error("获取唯品会商品列表异常" + cwb, e);
			return null;
		}
	}
/**
 * 广州通路对接添加
 */
	public ExptReason getExptCodeJointByB2cGztl(long customerid,String expect_code){
		ExptReason exptReason = new ExptReason();
		try {
			String jointEntityStr = JSONReslutUtil.getResultMessage(GetDmpDAO.dmpUrl + "/OMSInterface/getReasonidJointByB2cGztl/"+ customerid , "code=" + expect_code, "POST").toString();
			if(jointEntityStr != null ){
				JSONObject jparm = JSONObject.fromObject(jointEntityStr);
				exptReason.setExpt_code((jparm.get("expt_code") != null) && !"".equals(jparm.getString("expt_code")) ? jparm.getString("expt_code") : "");//广州通路对应我们系统中的原因
				exptReason.setExpt_msg((jparm.get("expt_type") != null) && !"".equals(jparm.getString("expt_type")) ? jparm.getString("expt_type") : "");//广州通路发来的异常类型在我们系统中的异常类型
				exptReason.setReasonid(jparm.get("reasonid")!=null&&!"".equals(jparm.getString("reasonid"))?jparm.getString("reasonid"):"");//在dmp里面查询出来的对应我们系统的原因所在表的id
			}
		} catch (Exception e) {
			this.logger.error("获取指定getExptCodeJointByB2c异常",e);
		}
		return exptReason;
	}
}
