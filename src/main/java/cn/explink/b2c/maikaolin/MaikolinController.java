package cn.explink.b2c.maikaolin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JointService;
import cn.explink.dao.BranchDAO;
import cn.explink.enumutil.BranchEnum;

@Controller
@RequestMapping("/maikaolin")
public class MaikolinController {
	@Autowired
	MaikolinService maikolinService;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	JointService jointService;
	@Autowired
	MaikaolinInsertCwbDetailTimmer maikaolinInsertCwbDetailTimmer;

	/*
	 * 发送xml给快递公司 快递返回xml给我们 我们把xml信息再发送给麦思科 麦思科请求我们 要求去接收快递公司数据 PackageTodoList
	 * 包裹清单列表查询（快递公司请求查询） PackageCheck 包裹复核（快递公司 扫描入库） PackageCourier
	 * 包裹分配（快递公司分站出库） PackageConfirm 包裹回单（快递公司 提交派送结果） PackageOperation 包裹操作详情信息
	 * PackageException 异常信息
	 */
	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		model.addAttribute("warehouselist", branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));
		model.addAttribute("maikaolinObject", maikolinService.getMaikaolin(key));
		model.addAttribute("joint_num", key);
		return "/b2cdj/maikaolin";

	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		maikolinService.update(key, state);
		// 保存
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String dangdangSave(Model model, @PathVariable("id") int key, HttpServletRequest request) {

		if (request.getParameter("password") != null && "explink".equals(request.getParameter("password"))) {
			try{
				maikolinService.edit(request, key);
				return "{\"errorCode\":0,\"error\":\"修改成功\"}";
			}catch(Exception e){
				return "{\"errorCode\":1,\"error\":\""+ e.getMessage() +"\"}";
			}
		} else {
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
		// 保存
	}

	@RequestMapping("/")
	public @ResponseBody String requestByDangDang(HttpServletRequest request, HttpServletResponse response) {
		try {
			Maikolin mkl = maikolinService.getMaikaolin(B2cEnum.Maikaolin.getKey());

			int isOpenFlag = jointService.getStateForJoint(B2cEnum.Maikaolin.getKey());
			if (isOpenFlag == 0) {
				return "未开启麦考林订单下载接口";
			}
		} catch (Exception e) {
		}
		return maikolinService.retrun("true");
	}

	// 手动触发
	@RequestMapping("/hander")
	public @ResponseBody String hander(HttpServletRequest request, HttpServletResponse response) {
		try {

			maikolinService.getOrdersByMaikolin(B2cEnum.Maikaolin.getKey());
			maikolinService.getMaikaolinDataCallBack();
			maikaolinInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail(B2cEnum.Maikaolin.getKey());
			return "success";

		} catch (Exception e) {
			return "error" + e.getMessage();

		}

	}

	// 测试用——反馈
	@RequestMapping("/te")
	public @ResponseBody String ffff(HttpServletRequest request, HttpServletResponse response) {
		try {
			Maikolin dangdangsyn = maikolinService.getMaikaolin(B2cEnum.Maikaolin.getKey());
			String charcode = dangdangsyn.getUserCode();
			response.setContentType("text/xml;charset=" + charcode);
			response.setCharacterEncoding(charcode);
			int isOpenFlag = jointService.getStateForJoint(B2cEnum.Maikaolin.getKey());
			if (isOpenFlag == 0) {
				return maikolinService.retrun("false");
			}
		} catch (Exception e) {
		}
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?><tms>" + "<response_header>" + "<user_id>快递公司</user_id>" + "<user_key>" + 4242 + "</user_key>" + "<method>PackageCourier</method>"
				+ "<response_time>20090911121212</response_time>" + "</response_header>" + "<response_body>" + "<operation_result>" + "<package_id>5345345</package_id> " + "<success>true</success>"
				+ "<reason>成功</reason> " + "</operation_result>" + "</response_body>" + "</tms>";
	}
}
