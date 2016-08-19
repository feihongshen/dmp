package cn.explink.b2c.auto.order.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.explink.b2c.vipshop.VipShopGetCwbDataService;
import cn.explink.dao.BranchDAO;
import cn.explink.enumutil.BranchEnum;

@Controller
@RequestMapping("/qiLeKang")
public class QiLeKangController {

	@Autowired
	VipShopGetCwbDataService vipshopService;
	@Autowired
	BranchDAO branchDAO;

	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		model.addAttribute("vipshopObject", this.vipshopService.getVipShop(key));
		model.addAttribute("warehouselist", this.branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));
		model.addAttribute("joint_num", key);
		return "b2cdj/qilekang";

	}

}
