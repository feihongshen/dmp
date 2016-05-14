package cn.explink.controller.express2;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.controller.ExplinkResponse;
import cn.explink.controller.express.ExpressCommonController;
import cn.explink.core.common.model.json.DataGridReturn;
import cn.explink.core.utils.JsonUtil;
import cn.explink.domain.Branch;
import cn.explink.domain.User;
import cn.explink.domain.VO.express.AdressVO;
import cn.explink.domain.express2.VO.ReserveOrderLogVo;
import cn.explink.domain.express2.VO.ReserveOrderVo;
import cn.explink.service.express2.ReserveOrderService;
import cn.explink.util.Tools;
import net.sf.json.JSONObject;

/**
 * 预约单 Controller
 * @date 2016年5月13日 上午11:04:29
 */
@Controller
@RequestMapping("/express2/reserveOrder")
public class ReserveOrderController extends ExpressCommonController {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
	private ReserveOrderService reserveOrderService;
	
	/**
	 * 快递预约单查询
	 * @date 2016年5月13日 上午11:10:28
	 * @return
	 */
	@RequestMapping("/query")
	public String query() {
		return "express2/reserveOrder/query";
	}
	
	/**
	 * 快递预约单处理
	 * @date 2016年5月13日 上午11:10:28
	 * @return
	 */
	@RequestMapping("/handle")
	public String handle(Model model) {

        //查找本省的所有城市
        List<AdressVO> cities = this.reserveOrderService.getCities();
        model.addAttribute("cityList",cities);

        //查找本省的所有站点
        List<Branch> branches = this.reserveOrderService.getBranches();
        model.addAttribute("branchList",branches);

        return "express2/reserveOrder/handle";
	}
	
	/**
	 * 快递预约单处理(站点)
	 * @date 2016年5月13日 上午11:10:28
	 * @return
	 */
	@RequestMapping("/handleWarehouse")
	public String warehouseHandle() {
		return "express2/reserveOrder/warehouseHandle";
	}
	
	/**
	 * 查询列表
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonGenerationException 
	 * @date 2016年5月13日 下午5:39:59
	 */
	@ResponseBody
	@RequestMapping("/queryList")
	public void queryList(HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException {
		List<ReserveOrderVo> reserveOrderVoList = this.reserveOrderService.getReserveOrderVoList();
		int count = reserveOrderVoList.size();
		DataGridReturn dg = new DataGridReturn();
		dg.setRows(reserveOrderVoList);
		dg.setTotal(count);
		Tools.outData2Page(Tools.obj2json(dg), response);
	}
	
    @RequestMapping("/getCountyByCity")
    @ResponseBody
    public JSONObject getCountyByCity(int cityId) {
        JSONObject obj = new JSONObject();
        List<AdressVO> countyList = this.reserveOrderService.getRegionsByCity(cityId);
        obj.put("countyList", countyList);
        return obj;
    }

    @RequestMapping("/getCourierByBranch")
    @ResponseBody
    public JSONObject getCourierByBranch(int branchId) {
        JSONObject obj = new JSONObject();
        List<User> kdyList = this.reserveOrderService.getCourierByBranch(branchId);
        obj.put("kdyList", kdyList);
        return obj;
    }
	
	/**
	 * 查询预约单日志
	 * @param reserveOrderNo 预约单号
	 * @return
	 */
	@ResponseBody
	@RequestMapping("queryReserveOrderLog")
	public String queryReserveOrderLog(String reserveOrderNo) {
		reserveOrderNo = StringUtils.trimToEmpty(reserveOrderNo);
		
		ExplinkResponse explinkResponse = new ExplinkResponse();
		
		if (reserveOrderNo.length() == 0) {
			explinkResponse.setStatuscode(Boolean.FALSE.toString());
			explinkResponse.setErrorinfo("预约单号为空");
			return JsonUtil.translateToJson(explinkResponse);
		}
		
		Map<String, Object> responseBody = new HashMap<String, Object>();
		explinkResponse.setBody(responseBody);
		
		List<ReserveOrderLogVo> reserveOrderLogVoList = Collections.emptyList();
		try {
			//查询预约单日志
			reserveOrderLogVoList = reserveOrderService.queryReserveOrderLog(reserveOrderNo);
			if (reserveOrderLogVoList == null) {
				reserveOrderLogVoList = Collections.emptyList();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		responseBody.put("reserveOrderLogVoList", reserveOrderLogVoList);
		return JsonUtil.translateToJson(explinkResponse);
	}
	
}
