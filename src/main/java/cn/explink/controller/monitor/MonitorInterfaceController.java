package cn.explink.controller.monitor;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.amazon.Amazon;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JointService;
import cn.explink.dao.BranchDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.User;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.SmsSendService;
import cn.explink.util.HttpUtil;

/**
 * 接口类（1.查询AS2目录下文件数的接口 2.发送短信接口）
 * 
 * @author liyy
 *
 */
@RequestMapping("/monitorInterface")
@Controller
public class MonitorInterfaceController {
	private Logger logger = LoggerFactory.getLogger(MonitorInterfaceController.class);

	@Autowired
	private BranchDAO branchDAO;
	@Autowired
	private SmsSendService smsSendService;

	@Autowired
	private JointService jointService;

	PropertyPlaceholderHelper placeholderHelper = new PropertyPlaceholderHelper("${", "}");

	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	/**
	 * 查询AS2目录下文件数的接口
	 * 
	 * @param response
	 * @param request
	 * @param filedir
	 *            AS2目录
	 * @return
	 */
	@RequestMapping("/getAS2DirectoryFileCount")
	public @ResponseBody JSONObject getAS2DirectoryFileCount(HttpServletResponse response, HttpServletRequest request) {
		JSONObject jo = new JSONObject();
		try {
			JSONObject jsonObj = JSONObject.fromObject(jointService.getObjectMethod(B2cEnum.Amazon.getKey()));
			Amazon amazon = (Amazon) JSONObject.toBean(jsonObj, Amazon.class);
			String filedir = amazon.getTnt_url();
			long filenum = 0;
			List arrayList = getListFiles(filedir, null, true);
			if (arrayList.isEmpty()) {
				filenum = 0;
			} else {
				filenum = arrayList.size();
			}
			jo.put("threshold", filenum);
			jo.put("resultCode", 0);
		} catch (Exception e) {
			jo.put("resultCode", 1);
			logger.error(e.getMessage());
		}
		return jo;
	}

	/**
	 * 发送短信接口
	 * 
	 * @param request
	 * @param name
	 *            用户名
	 * @param password
	 *            密码
	 * @param mobiles
	 *            手机号以逗号隔开
	 * @param message
	 *            短信内容
	 * @return
	 */
	@RequestMapping("/smssendMessage")
	public @ResponseBody JSONObject smssendMessage(HttpServletRequest request, @RequestParam(value = "name") String name, @RequestParam(value = "password") String password,
			@RequestParam(value = "mobiles") String mobiles, @RequestParam(value = "message") String message) {
		JSONObject jo = new JSONObject();
		try {
			try {
				User us = getSessionUser();
				Branch deliverybranch = branchDAO.getBranchByBranchid(us.getBranchid());
				logger.info("使用短信群发功能-接口：用户名：{},站点：{},手机号：" + mobiles + ",短信内容:" + message + "", us.getRealname(), deliverybranch.getBranchname());
			} catch (Exception e) {
				logger.error("使用短信群发功能-接口：获取用户名，站点异常");
			}
			JSONObject o = new JSONObject();
			String msg = "";
			int j = 0;
			int i = 0;
			String errorMsg = "";
			try {
				if (mobiles.trim().length() > 0) {
					logger.info("短信发送，单量：{}", mobiles.split(",").length);
					for (String mobile : mobiles.split(",")) {
						if (mobile.trim().length() == 0) {
							continue;
						}
						if (!isMobileNO(mobile)) {
							logger.info("短信发送，手机号：{}", mobile.trim());
							j++;
							errorMsg += "手机号：[" + mobile.trim() + "]:手机号格式不正确";
							continue;
						}
						if (mobile != null && !"".equals(mobile)) {
							logger.info("短信发送，手机号：{}", mobile.trim());
							try {
								msg = smsSendService.sendSmsInterface(mobile, message, 0, "未知", getSessionUser().getUserid(), HttpUtil.getUserIp(request), name, password);
								logger.info("短信发送，手机号：{}  结果：{}", mobile.trim(), msg);
								if ("发送短信成功".equals(msg)) {
									i++;
								} else {
									errorMsg += "手机号：[" + mobile.trim() + "]:" + msg;
									j++;
								}
							} catch (UnsupportedEncodingException e) {
								logger.error("短信发送，异常", e);
								j++;
								errorMsg += "手机号：[" + mobile.trim() + "]:短信发送网络异常";
							}
						} else {
							j++;
							errorMsg += "手机号：[" + mobile.trim() + "]:手机号为空";
						}
					}
				}
				logger.info("短信发送，成功单数：{}", i);
				logger.info("短信发送，失败单数：{}", j);

			} catch (Exception e) {
				logger.error("短信发送，异常", e);
				errorMsg += e.getMessage();
			}
			o.put("sussesCount", i);
			o.put("errorCount", j);
			o.put("errorMsg", errorMsg);

			jo.put("threshold", o.toString());
			jo.put("resultCode", 0);
		} catch (Exception e) {
			jo.put("resultCode", 1);
			logger.error(e.getMessage());
		}
		return jo;
	}

	/**
	 * 
	 * @param path
	 *            文件路径
	 * @param suffix
	 *            后缀名
	 * @param isdepth
	 *            是否遍历子目录
	 * @return
	 */
	public List getListFiles(String path, String suffix, boolean isdepth) {
		File file = new File(path);
		return listFile(file, suffix, isdepth);
	}

	public List<String> fileList = new ArrayList<String>();

	public List listFile(File f, String suffix, boolean isdepth) {
		// 是目录，同时需要遍历子目录
		if (f.isDirectory() && isdepth == true) {
			File[] t = f.listFiles();
			for (int i = 0; i < t.length; i++) {
				listFile(t[i], suffix, isdepth);
			}
		} else {
			String filePath = f.getAbsolutePath();

			if (suffix != null) {
				int begIndex = filePath.lastIndexOf(".");// 最后一个.(即后缀名前面的.)的索引
				String tempsuffix = "";

				if (begIndex != -1) {// 防止是文件但却没有后缀名结束的文件
					tempsuffix = filePath.substring(begIndex + 1, filePath.length());
				}

				if (tempsuffix.equals(suffix)) {
					fileList.add(filePath);
				}
			} else {
				// 后缀名为null则为所有文件
				fileList.add(filePath);
			}
		}
		return fileList;
	}

	// 验证手机号码的合法性
	private static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^1\\d{10}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}
}
