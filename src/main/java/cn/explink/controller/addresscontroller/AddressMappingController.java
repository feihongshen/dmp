package cn.explink.controller.addresscontroller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.UserDAO;
import cn.explink.domain.User;
import cn.explink.domain.addressvo.AddressQueryResult;
import cn.explink.domain.addressvo.AddressSyncServiceResult;
import cn.explink.domain.addressvo.AddressVo;
import cn.explink.domain.addressvo.ApplicationVo;
import cn.explink.domain.addressvo.DelivererRuleVo;
import cn.explink.domain.addressvo.ResultCodeEnum;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.addressmatch.AddressQueryService;
import cn.explink.service.addressmatch.AddressSyncService;
import cn.explink.util.ResourceBundleUtil;

@Controller
@RequestMapping("/addressdelivertostation")
public class AddressMappingController {

	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	AddressQueryService addressQueryService;
	@Autowired
	AddressSyncService addressSyncService;
	@Autowired
	UserDAO userDao;

	private static Logger logger = LoggerFactory.getLogger(AddressMappingController.class);

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	/**
	 * 初始化----用户点击菜单后加载0级树
	 * 
	 * @param addressId
	 * @return
	 */
	@RequestMapping("/inittree")
	public String initTree(Model model) {
		try {
			List<User> delivererList = qureyDeliverListByStationId();
			model.addAttribute("delivererList", delivererList);
		} catch (Exception e) {
			logger.error("", e);
		}
		return "/address/deliveryDelivererRule";
	}

	/**
	 * 获服树结构子结点 TODO
	 * 合并getAddressQueryResult与getAddressDelivererRuleResult方法，同一次点击只能调地址库接口
	 * 
	 * @param model
	 * @param addressId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/loadsubtree")
	public @ResponseBody List<AddressVo> getAddressQueryResult(Model model, @RequestParam(value = "addressId", defaultValue = "", required = false) Long addressId) throws Exception {
		ApplicationVo applicationVo = getApplicationVo();
		User user = getSessionUser();
		long branchid = user.getBranchid();
		AddressQueryResult addressQuestResult = addressQueryService.getAddress(applicationVo, addressId, branchid);
		ResultCodeEnum resultCode = addressQuestResult.getResultCode();
		String message = addressQuestResult.getMessage();
		if (resultCode.getCode() != 0) {
			logger.error("调用WEBSERVICE查询地址失败，原因：" + message);
			return null;
		}
		List<AddressVo> addressVoList = addressQuestResult.getAddressVoList();
		/*
		 * if(addressVoList == null){ return null; }
		 */
		for (AddressVo a : addressVoList) {
			a.setpId(String.valueOf(a.getParentId()));
			if (a.getpId() != null) {
				a.setIsParent(true);
				a.setOpen(true);
			}
		}
		return addressVoList;
	}

	@RequestMapping("/loaddelivererrule")
	public @ResponseBody List<DelivererRuleVo> getAddressDelivererRuleResult(@RequestParam(value = "addressId", defaultValue = "", required = false) Long addressId) throws Exception {
		ApplicationVo applicationVo = getApplicationVo();
		User user = getSessionUser();
		long branchid = user.getBranchid();
		AddressQueryResult addressQuestResult = addressQueryService.getAddress(applicationVo, addressId, branchid);
		ResultCodeEnum resultCode = addressQuestResult.getResultCode();
		String message = addressQuestResult.getMessage();

		List<DelivererRuleVo> delivererRuleVoList = addressQuestResult.getDelivererRuleVoList();
		if (resultCode.getCode() != 0) {
			logger.error("调用WEBSERVICE查询地址失败，原因：" + message);
			return null;
		}
		List<User> delivererList = qureyDeliverListByStationId();

		/*
		 * List<AddressVo> addressVoList =
		 * addressQuestResult.getAddressVoList();
		 */
		if (delivererRuleVoList == null) {
			return null;
		}

		List<DelivererRuleVo> delivererRuleToRemove = null;
		for (DelivererRuleVo a : delivererRuleVoList) {
			boolean foundDeliverer = false;
			// TODO 把delivererList转换成id为key的map，避免多次循环
			for (User b : delivererList) {
				if (a.getDelivererId() == b.getUserid()) {
					a.setDelivererName(b.getRealname());
					foundDeliverer = true;
					break;
				}
			}
			if (!foundDeliverer) {
				if (delivererRuleToRemove == null) {
					delivererRuleToRemove = new ArrayList<DelivererRuleVo>();
				}
				delivererRuleToRemove.add(a);
			}
		}

		if (delivererRuleToRemove != null) {
			delivererRuleVoList.removeAll(delivererRuleToRemove);
		}
		return delivererRuleVoList;
	}

	/**
	 * 生成applicationVo
	 */
	private ApplicationVo getApplicationVo() {
		ApplicationVo applicationVo = new ApplicationVo();
		applicationVo.setCustomerId(Long.parseLong(ResourceBundleUtil.addresscustomerid));
		applicationVo.setId(Long.parseLong(ResourceBundleUtil.addressid));
		applicationVo.setPassword(ResourceBundleUtil.addresspassword);
		return applicationVo;
	}

	/**
	 * 小件员规则设置
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/savedelivererrule")
	public @ResponseBody String saveDelivererRule(Model model, @RequestParam(value = "deliveryDeilvererRule", defaultValue = "", required = false) String deliveryDeilvererRule) throws Exception {
		ApplicationVo applicationVo = getApplicationVo();

		List<DelivererRuleVo> delivererRuleVoList = new ArrayList<DelivererRuleVo>();
		String[] dsrkey = deliveryDeilvererRule.split("&");
		for (int i = 0; i < dsrkey.length; i++) {
			DelivererRuleVo tmpdrvo = new DelivererRuleVo();
			tmpdrvo.setAddressId(Long.parseLong(dsrkey[i].split("#")[2]));
			tmpdrvo.setDelivererId(Long.parseLong(dsrkey[i].split("#")[0]));
			tmpdrvo.setRule(dsrkey[i].split("#")[1]);
			delivererRuleVoList.add(tmpdrvo);
		}
		AddressSyncServiceResult asr = addressSyncService.createDelivererRule(applicationVo, delivererRuleVoList);
		if (asr.getResultCode().getCode() == 0) {
			logger.info("小件员设置规则成功");
			return "{\"errorCode\":0,\"error\":\"创建成功\"}";
		} else {
			logger.info("小件员设置规则失败" + asr.getMessage());
			return "{\"errorCode\":1,\"error\":\"创建失败\"}";
		}
	}

	/**
	 * 小件员规则删除
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/deletedelivererrule")
	public @ResponseBody String deleDelivererRule(Model model, @RequestParam(value = "deilvererRuleId", defaultValue = "", required = false) Long deilvererRuleId) throws Exception {
		ApplicationVo applicationVo = getApplicationVo();
		AddressSyncServiceResult asr = addressSyncService.deleteDelivererRule(applicationVo, deilvererRuleId);
		if (asr.getResultCode().getCode() == 0) {
			logger.info("小件员规则删除成功");
			return "{\"errorCode\":0,\"error\":\"删除成功\"}";
		} else {
			logger.info("小件员规则删除失败" + asr.getMessage());
			return "{\"errorCode\":1,\"error\":\"删除失败\"}";
		}
	}

	/**
	 * 站长登陆，获取该站所有工作状态小件员
	 * 
	 * @return
	 */
	public List<User> qureyDeliverListByStationId() throws Exception {
		List<User> delivererList = new ArrayList<User>();
		User user = getSessionUser();
		long branchid = user.getBranchid();
		try {
			delivererList = userDao.getUserByRolesAndBranchid("2", branchid);
		} catch (Exception e) {
			logger.error("", e);
		}

		return delivererList;
	}
}
