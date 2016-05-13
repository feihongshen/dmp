package cn.explink.controller;

import cn.explink.controller.express.ExpressCommonController;
import cn.explink.domain.Branch;
import cn.explink.domain.User;
import cn.explink.domain.VO.express.AdressVO;
import cn.explink.service.express.EmbracedOrderInputService;
import cn.explink.service.express.PreOrderService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 预约单 Controller
 * @date 2016年5月13日 上午11:04:29
 */
@Controller
@RequestMapping("/preorder")
public class PreOrderController extends ExpressCommonController {

    @Autowired
    private PreOrderService preOrderService;

    /**
	 * 快递预约单查询
	 * @date 2016年5月13日 上午11:10:28
	 * @return
	 */
	@RequestMapping("/query")
	public String query() {
		return "preorder/query";
	}
	
	/**
	 * 快递预约单处理
	 * @date 2016年5月13日 上午11:10:28
	 * @return
	 */
	@RequestMapping("/handle")
	public String handle(Model model) {

        //查找本省的所有城市
        List<AdressVO> cities = this.preOrderService.getCities();
        model.addAttribute("cityList",cities);

        //查找本省的所有站点
        List<Branch> branches = this.preOrderService.getBranches();
        model.addAttribute("branchList",branches);


        return "preorder/handle";
	}
	
	/**
	 * 快递预约单处理(站点)
	 * @date 2016年5月13日 上午11:10:28
	 * @return
	 */
	@RequestMapping("/handleWarehouse")
	public String warehouseHandle() {
		return "preorder/handleWarehouse";
	}


    @RequestMapping("/getCountyByCity")
    @ResponseBody
    public JSONObject getCountyByCity(int cityId) {
        JSONObject obj = new JSONObject();
        List<AdressVO> countyList = this.preOrderService.getRegionsByCity(cityId);
        obj.put("countyList", countyList);
        return obj;
    }

    @RequestMapping("/getKDYByBranch")
    @ResponseBody
    public JSONObject getKDYByBranch(int branchId) {
        JSONObject obj = new JSONObject();
        List<User> kdyList = this.preOrderService.getKDYByBranch(branchId);
        obj.put("kdyList", kdyList);
        return obj;
    }


}
