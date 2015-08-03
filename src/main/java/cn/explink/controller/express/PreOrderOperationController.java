package cn.explink.controller.express;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.explink.dao.BranchDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.User;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.express.ExcuteTypeEnum;
import cn.explink.service.ExplinkUserDetail;

@Controller
@RequestMapping("/preOrderOperationController")
public class PreOrderOperationController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	BranchDAO branchDAO;

	/**
	 *
	 * @Title: CallerArchivalRepository
	 * @description 预订单查询的主方法
	 * @author 刘武强
	 * @date  2015年7月30日下午4:37:27
	 * @param  @param model
	 * @param  @param start:查询不需要time，只要date
	 * @param  @param end:查询不需要time，只要date
	 * @param  @param advanceOrdercode
	 * @param  @param excuteType
	 * @param  @param sender
	 * @param  @param mobile
	 * @param  @param station：如果是0的话，就代表的是全部；如果不为0，则是对应的站点id
	 * @param  @param page
	 * @param  @return
	 * @return  String
	 * @throws
	 */
	@RequestMapping("/query/{page}")
	public String CallerArchivalRepository(Model model, String start_name, String end_name, String advanceordercode_name, String excuteType_name, String sender_name, String mobile_name,
			String station_name, @PathVariable(value = "page") long page) {
		long branchid = this.getSessionUser().getBranchid();
		//branchid是1的话，那么就是省公司，否则是站点
		model.addAttribute("userType", branchid);
		model.addAttribute("page", page);
		model.addAttribute("excuteTypelist", this.getExcuteType());
		model.addAttribute("stationlist", this.getBranchStations(branchid));

		//model.addAttribute("page_obj", new Page(workorderdao.getCsConsigneeInfocount(cci.getName(),cci.getPhoneonOne(),cci.getConsigneeType()), page, Page.ONE_PAGE_NUMBER));
		return "express/preorderquery/preOrderQuery";
	}

	/**
	 *
	 * @Title: getSessionUser
	 * @description 获取登录用户信息
	 * @author 刘武强
	 * @date  2015年7月30日下午4:13:05
	 * @param  @return
	 * @return  User
	 * @throws
	 */
	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	/**
	 *
	 * @Title: getDateSelectType
	 * @description 获取执行状态（不包括“未分配站点”这个状态）
	 * @author 刘武强
	 * @date  2015年7月30日下午4:13:22
	 * @param  @return
	 * @return  List
	 * @throws
	 */
	public List<Map<String, Object>> getExcuteType() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<ExcuteTypeEnum> infolist = ExcuteTypeEnum.getAllStatus();
		for (int i = 0; i < infolist.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			ExcuteTypeEnum enmu = infolist.get(i);
			//将“未分配站点”去掉
			if (enmu.getValue() == 1) {
				continue;
			}
			map.put("key", enmu.getValue());
			map.put("value", enmu.getText());
			list.add(map);
		}
		return list;
	}

	/**
	 *
	 * @Title: getBranchStations
	 * @description 获取站点
	 * @author 刘武强
	 * @date  2015年7月30日下午4:17:57
	 * @param  branchid=1时，获取所有的站点；否则获取当前branchid对应的站点信息
	 * @return  List
	 * @throws
	 */
	public List<Map<String, String>> getBranchStations(long branchid) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		if (branchid == 1) {
			List<Branch> branchListinfo = this.branchDAO.getAllBranches();
			//把“全部”拼接进去
			Map<String, String> mapall = new HashMap<String, String>();
			mapall.put("key", "0");
			mapall.put("value", "全部");
			list.add(mapall);
			for (Branch temp : branchListinfo) {
				Map<String, String> map = new HashMap<String, String>();
				if (temp.getSitetype() == BranchEnum.ZhanDian.getValue()) {
					map.put("key", temp.getBranchid() + "");
					map.put("value", temp.getBranchname());
					list.add(map);
				}
			}
		} else {
			Branch branch = this.branchDAO.getBranchByBranchid(branchid);
			Map<String, String> map = new HashMap<String, String>();
			if (branch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
				map.put("key", branch.getBranchid() + "");
				map.put("value", branch.getBranchname());
				list.add(map);
			}
		}
		return list;
	}

	//public List getTableInfo(){

	//}
}
