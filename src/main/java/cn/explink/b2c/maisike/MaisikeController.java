package cn.explink.b2c.maisike;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointService;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.WarehouseToCommenDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.WarehouseToCommen;
import cn.explink.enumutil.BranchEnum;
import cn.explink.service.CwbOrderService;
import cn.explink.service.WhareHouseToCommonService;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.MD5.MD5Util;

/**
 * 迈思可
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/maisike")
public class MaisikeController {
	private Logger logger = LoggerFactory.getLogger(MaisikeController.class);
	@Autowired
	MaisikeService maisikeService;
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	JointService jointService;

	@Autowired
	BranchDAO branchDAO;
	@Autowired
	MaisikeService_branchSyn maisikeService_branchSyn;

	@Autowired
	MaisikeService_Search maisikeService_Search;
	@Autowired
	WhareHouseToCommonService whareHouseToCommonService;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	WarehouseToCommenDAO warehouseToCommenDAO;
	@Autowired
	CwbOrderService cwbOrderService;

	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {

		String editJsp = "";
		for (B2cEnum fote : B2cEnum.values()) {
			if (fote.getKey() == key) {
				editJsp = fote.getMethod();
				break;
			}
		}
		model.addAttribute("maisikeObject", maisikeService.getMaisike(key));
		model.addAttribute("warehouselist", branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));
		model.addAttribute("joint_num", key);
		return "b2cdj/maisike";

	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String save(Model model, @PathVariable("id") int key, HttpServletRequest request) {

		if (request.getParameter("password") != null && "explink".equals(request.getParameter("password"))) {

			maisikeService.edit(request, key);
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		} else {
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
		// 保存

	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		maisikeService.update(key, state);
		// 保存
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";

	}

	@RequestMapping("/branchsyn_hander")
	public @ResponseBody String branchsyn_hander() {
		maisikeService_branchSyn.syn_maisikeBranchs();
		// 保存
		return "迈思可站点手动同步完成";
	}

	@RequestMapping("/search")
	public @ResponseBody String searchMaisike(HttpServletRequest request) {
		try {
			String cwb = request.getParameter("cwb");
			String requesttime = request.getParameter("requestTime");
			String sign = request.getParameter("sign");
			if (cwb == null || requesttime == null || sign == null) {
				return "参数不完整";
			}

			logger.info("迈思可请求信息cwb={},requesttime={},sign={}" + sign, cwb, requesttime);

			int b2cenum = B2cEnum.Maisike.getKey();
			Maisike mInfo = maisikeService_Search.getMaisike(b2cenum);

			String password = MD5Util.md5(cwb + requesttime + mInfo.getSearch_key());
			if (!sign.equalsIgnoreCase(password)) {
				logger.info("签名异常local_sign={},sign={}", password, sign);
				return "签名异常";
			}

			String cwbTransCwb = cwbOrderService.translateCwb(cwb); // 可能是订单号也可能是运单号
			if (cwbTransCwb == null || cwbTransCwb.isEmpty()) {
				return "未检索到数据";
			}

			imidateCommonOuttoBranch(String.valueOf(b2cenum), cwbTransCwb); // 模拟承运商确认出库

			String response = maisikeService_Search.Search_maisike_info(mInfo, cwbTransCwb, sign, requesttime);

			logger.info("返回迈思可日志={}", response);

			return response;
		} catch (Exception e) {
			return "未知异常" + e.getMessage();
		}

	}

	/**
	 * 模拟承运商确认出库
	 */
	private void imidateCommonOuttoBranch(String commoncode, String cwb) {
		try {
			CwbOrder cwbOrder = cwbDAO.getCwbByCwb(cwb);
			if (cwbOrder == null) {
				logger.info("模拟承运商确认出库未检索到此单号={}", cwb);
				return;
			}

			long count = warehouseToCommenDAO.getCommonCountByCwb(cwbOrder.getCwb());// 查询是否存在

			if (count > 0) {
				logger.info("承运商确认出库已存在该数据,不需要模拟重发={}", cwb);
				return;
			}

			WarehouseToCommen warehtoCommen = new WarehouseToCommen();
			warehtoCommen.setCommencode(commoncode);
			warehtoCommen.setCredate(DateTimeUtil.getNowTime());
			warehtoCommen.setCustomerid(cwbOrder.getCustomerid());
			warehtoCommen.setCwb(cwb);
			warehtoCommen.setStatetime(DateTimeUtil.getNowTime());
			warehtoCommen.setNextbranchid(cwbOrder.getNextbranchid());
			warehtoCommen.setStartbranchid(cwbOrder.getStartbranchid());
			int outbranchflag = 1; // 站点出库类型

			warehouseToCommenDAO.creWarehouseToCommen(cwb, warehtoCommen.getCustomerid(), warehtoCommen.getStartbranchid(), warehtoCommen.getNextbranchid(), warehtoCommen.getCommencode(),
					warehtoCommen.getCredate(), warehtoCommen.getStatetime(), outbranchflag);
			warehtoCommen = warehouseToCommenDAO.getCommenByCwb(cwb);
			whareHouseToCommonService.auditCommen_imitate(commoncode, warehtoCommen, cwbOrder.getStartbranchid(), outbranchflag);

		} catch (Exception e) {
			logger.error("模拟承运商确认出库异常", e);
		}
	}

}
