package cn.explink.b2c.yangguang;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import cn.explink.enumutil.BranchEnum;
import cn.explink.util.StringUtil;

@Controller
@RequestMapping("/yangguang")
public class YangGuangController {
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	JointService jointService;
	@Autowired
	YangGuangService yangGuangService;
	@Autowired
	YangGuangService_download yangGuangService_download;
	@Autowired
	YangGuangInsertCwbDetailTimmer yangGuangInsertCwbDetailTimmer;
	@Autowired
	BranchDAO branchDAO;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		model.addAttribute("yangguangObject", yangGuangService.getYangGuang(key));
		model.addAttribute("multidifflist", yangGuangService.getYangGuangDiffs(key));

		model.addAttribute("warehouselist", branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));
		model.addAttribute("joint_num", key);
		return "b2cdj/yangguang";
	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String smileSave(Model model, @PathVariable("id") int key, HttpServletRequest request) throws Exception {
		if (request.getParameter("password") != null && "explink".equals(request.getParameter("password"))) {
			yangGuangService.edit(request, key);
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		} else {
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		yangGuangService.update(key, state);
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	/**
	 * 提供手动导入接口 和插入主表
	 */
	@RequestMapping("/test")
	public @ResponseBody String requestCwbSearch(HttpServletRequest request, HttpServletResponse response) {
		yangGuangService_download.getOrderDetailToYangGuangFTPServer();
		yangGuangService_download.AnalyzTxtFileSaveB2cTemp();
		yangGuangInsertCwbDetailTimmer.selectTempAndInsertToCwbDetailByMultipDiff(B2cEnum.YangGuang.getKey());
		yangGuangService_download.MoveTxtFileToBakFile(); // 移动文件

		return "success";
	}

	/**
	 * 手动删除
	 */
	@RequestMapping("/delete")
	public @ResponseBody String delete(HttpServletRequest request, HttpServletResponse response) {
		yangGuangService_download.MoveTxtFileToBakFile(); // 移动文件
		return "success";
	}

	/**
	 * 手动插入主表
	 */
	@RequestMapping("/timmer")
	public @ResponseBody String requestCwbSearchTimmer(HttpServletRequest request, HttpServletResponse response) {
		yangGuangInsertCwbDetailTimmer.selectTempAndInsertToCwbDetailByMultipDiff(B2cEnum.YangGuang.getKey());
		return "success";
	}

	@RequestMapping("/test1")
	public @ResponseBody void test(HttpServletRequest request, HttpServletResponse response) {
		String line = "D000001105881000984365 120130805637898001001001陈在贵              0000-000000000000-00000000056510河北省 邯郸市 复兴区 邯钢百家村生活区4区85栋1单元6号                                                1064532013诺肯全铜淋浴花洒超值买赠组                        001固态                                              00010000000998                                                                                                    2013080600000004469264000000";

		logger.info(line);

		String SerialNo = StringUtil.subString(line, YgSE1Enum.SerialNo.getbIx(), YgSE1Enum.SerialNo.geteIx());
		String WarehouseCode = StringUtil.subString(line, YgSE1Enum.WarehouseCode.getbIx(), YgSE1Enum.WarehouseCode.geteIx());

		String ShipperNo = StringUtil.subString(line, YgSE1Enum.ShipperNo.getbIx(), YgSE1Enum.ShipperNo.geteIx()); // 1
		String KuChuQuFen = StringUtil.subString(line, YgSE1Enum.KuChuQuFen.getbIx(), YgSE1Enum.KuChuQuFen.geteIx());
		String CustName = StringUtil.subString(line, YgSE1Enum.CustName.getbIx(), YgSE1Enum.CustName.geteIx());
		String TelephoneNo = StringUtil.subString(line, YgSE1Enum.TelephoneNo.getbIx(), YgSE1Enum.TelephoneNo.geteIx());
		String MobilephoneNo = StringUtil.subString(line, YgSE1Enum.MobilephoneNo.getbIx(), YgSE1Enum.MobilephoneNo.geteIx());
		String ZipCode = StringUtil.subString(line, YgSE1Enum.ZipCode.getbIx(), YgSE1Enum.ZipCode.geteIx());
		String OrderNo = StringUtil.subString(line, YgSE1Enum.OrderNo.getbIx(), YgSE1Enum.OrderNo.geteIx()); // 存入transcwb
		String Address = StringUtil.subString(line, YgSE1Enum.Address.getbIx(), YgSE1Enum.Address.geteIx());

		logger.info("SerialNo:{}", SerialNo);
		logger.info("WarehouseCode:{}", WarehouseCode);
		logger.info("ShipperNo:{}", ShipperNo);
		logger.info("KuChuQuFen:{}", KuChuQuFen);
		logger.info("CustName:{}", CustName);
		logger.info("TelephoneNo:{}", TelephoneNo);
		logger.info("MobilephoneNo:{}", MobilephoneNo);
		logger.info("ZipCode:{}", ZipCode);
		logger.info("OrderNo:{}", OrderNo);
		logger.info("Address:{}", Address);

	}

}
