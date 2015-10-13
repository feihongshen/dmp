package cn.explink.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.amazon.AmazonaService;
import cn.explink.b2c.amazon.domain.Amazon;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliverySuccessfulDAO;
import cn.explink.dao.DownloadManagerDAO;
import cn.explink.dao.GetDmpDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DownloadManager;
import cn.explink.domain.Exportmould;
import cn.explink.domain.SystemInstall;
import cn.explink.domain.User;
import cn.explink.enumutil.ModelEnum;
import cn.explink.service.DataStatisticService;
import cn.explink.service.DownloadManagerService;
import cn.explink.util.Page;

@RequestMapping("/download")
@Controller
public class DownloadController {

	@Autowired
	GetDmpDAO getDmpDAO;
	@Autowired
	DataStatisticService dataStatisticService;
	@Autowired
	DeliverySuccessfulDAO deliverySuccessfulDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	DownloadManagerDAO downloadManagerDAO;
	@Autowired
	DownloadManagerService downloadManagerService;
	@Autowired
	AmazonaService amazonaService;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping("/list/{page}")
	public String download(Model model, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate,
			@RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, @RequestParam(value = "state", required = false, defaultValue = "-2") int state,
			@PathVariable(value = "page") long page, HttpServletRequest request) {

		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		User user = getDmpDAO.getLogUser(dmpid);
		if (user == null || user.getUserid() == 0) {// 如果登录失效，提示登录失败
			Page pageparm = new Page();
			pageparm = new Page(0, page, Page.DOWN_PAGE_NUMBER);
			model.addAttribute("downloadList", new ArrayList<DownloadManager>());
			model.addAttribute("page_obj", pageparm);
			model.addAttribute("page", page);
			model.addAttribute("fileDay", 0);
			model.addAttribute("nouser", 0);
			return "download/downloadlist";
		}
		List<DownloadManager> downloadList = downloadManagerDAO.getDownloadManagerListByUserId(user.getUserid(), begindate, enddate, state, page);
		long count = downloadManagerDAO.getDownloadManagerCountByUserId(user.getUserid(), begindate, enddate, state);
		Page pageparm = new Page();
		pageparm = new Page(count, page, Page.DOWN_PAGE_NUMBER);
		model.addAttribute("downloadList", downloadList);
		model.addAttribute("page_obj", pageparm);
		model.addAttribute("page", page);
		SystemInstall sysInstall = getDmpDAO.getSystemInstallByName("fileEffectiveDay");
		int day = 7;
		if (sysInstall != null) {
			try {
				day = Integer.parseInt(sysInstall.getValue());
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				day = 7;
			}
		}
		model.addAttribute("fileDay", day);
		return "download/downloadlist";

	}

	@RequestMapping("/xiazai/{id}")
	public void download(Model model, @PathVariable(value = "id") long id, HttpServletResponse response, HttpServletRequest request) {

		try {
			String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
			User user = getDmpDAO.getLogUser(dmpid);
			if (user == null || user.getUserid() == 0) {// 如果登录失效，提示登录失败
				return;
			}
			DownloadManager down = downloadManagerDAO.getDownloadManagerByid(user.getUserid(), id);

			if (down == null) {
				return;
			}
			if (down.getModelid() == ModelEnum.KuFangChuKuHuiZong.getValue()) {
				// 库房出库统计（云）导出报表
				downloadManagerService.KuFangChuKuHuiZongExport(down);
			}
			// path是指欲下载的文件的路径。
			File file = new File(down.getFileurl() + down.getFilename());
			// 取得文件名。
			String filename = file.getName();
			// 以流的形式下载文件。
			InputStream fis = new BufferedInputStream(new FileInputStream(down.getFileurl() + down.getFilename()));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			// 清空response
			response.reset();
			// 设置response的Header
			response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
			response.addHeader("Content-Length", "" + file.length());
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream");
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
			logger.info("下载文件完成");
			downloadManagerDAO.updateStateById(3, id, "");
		} catch (IOException ex) {
			logger.error("下载文件异常", ex);
			ex.printStackTrace();
		}

	}

	@RequestMapping("/zhongzhi/{id}")
	public @ResponseBody String zhongzhi(Model model, @PathVariable(value = "id") long id, HttpServletResponse response, HttpServletRequest request) {

		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		User user = getDmpDAO.getLogUser(dmpid);
		JSONObject json = new JSONObject();
		if (user == null || user.getUserid() == 0) {// 如果登录失效，提示登录失败
			json.put("errorCode", 1);
			json.put("remark", "登录失效");
			return json.toString();
		}
		DownloadManager down = downloadManagerDAO.getDownloadManagerByid(user.getUserid(), id);

		if (down == null) {
			json.put("errorCode", 1);
			json.put("remark", "没有查询到改记录");
			return json.toString();
		}

		if (down.getState() == 1) {
			json.put("errorCode", 2);
			json.put("remark", "已经完成导出，不能终止导出");
			json.put("endtime", down.getEndtime());
			return json.toString();
		}
		downloadManagerDAO.updateStateById(2, id, "");
		downloadManagerService.saveXiazaitoMap(id, 2);
		json.put("errorCode", 0);
		json.put("remark", "已终止导出");
		return json.toString();
	}

	@RequestMapping("/del/{id}")
	public @ResponseBody String del(Model model, @PathVariable(value = "id") long id, HttpServletResponse response, HttpServletRequest request) {

		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		User user = getDmpDAO.getLogUser(dmpid);
		JSONObject json = new JSONObject();
		if (user == null || user.getUserid() == 0) {// 如果登录失效，提示登录失败
			json.put("errorCode", 1);
			json.put("remark", "登录失效");
			return json.toString();
		}
		DownloadManager down = downloadManagerDAO.getDownloadManagerByid(user.getUserid(), id);
		if (down == null) {
			json.put("errorCode", 1);
			json.put("remark", "没有查询到改记录");
			return json.toString();
		}
		if (down.getState() == 1 || down.getState() == 3 || down.getState() == 0) {// 如果导出了文件，将文件删掉
			try {
				File myFilePath = new File(down.getFileurl() + down.getFilename());
				if (myFilePath.exists()) {// 文件是否存在
					myFilePath.delete(); // 删除文件
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		downloadManagerDAO.delStateById(id);
		downloadManagerService.removeXiazaitoMap(id);
		json.put("errorCode", 0);
		json.put("remark", "删除成功");
		return json.toString();

	}

	@RequestMapping("/chongxinxiazaiCheck/{id}")
	public @ResponseBody String exportExcle(Model model, HttpServletResponse response, HttpServletRequest request, @PathVariable(value = "id") long id) {
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		User user = getDmpDAO.getLogUser(dmpid);
		JSONObject json = new JSONObject();
		if (user == null || user.getUserid() == 0) {// 如果登录失效，提示登录失败
			json.put("errorCode", 1);
			json.put("remark", "登录失效");
			return json.toString();
		}
		DownloadManager down = downloadManagerDAO.getDownloadManagerByid(user.getUserid(), id);

		if (down == null) {
			json.put("errorCode", 1);
			json.put("remark", "没有查询到改记录");
			return json.toString();
		}
		downloadManagerDAO.updateStateById(-1, id, "");
		downloadManagerService.saveXiazaitoMap(id, -1);
		json.put("errorCode", 0);
		json.put("remark", "已进入重新下载");
		return json.toString();

	}

	@RequestMapping("/getDownCount")
	public @ResponseBody String getDownCount(HttpServletRequest request) {
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		User user = getDmpDAO.getLogUser(dmpid);
		JSONObject json = new JSONObject();
		if (user == null || user.getUserid() == 0) {// 如果登录失效，提示登录失败
			json.put("errorCode", 1);
			json.put("remark", "登录失效");
			return json.toString();
		}
		long beingDownCount = downloadManagerDAO.getDownloadCountByUseridAndState(user.getUserid(), 0);
		long finishCount = downloadManagerDAO.getDownloadCountByUseridAndState(user.getUserid(), 1);
		long downCount = beingDownCount + finishCount;
		json.put("errorCode", 0);
		json.put("beingDownCount", beingDownCount);
		json.put("finishCount", finishCount);
		json.put("downCount", downCount);
		return json.toString();

	}

	@RequestMapping("/chongxinxiazai/{id}")
	public void chongxinxiazai(Model model, @PathVariable(value = "id") long id, HttpServletResponse response, HttpServletRequest request) {

		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		User user = getDmpDAO.getLogUser(dmpid);
		if (user == null || user.getUserid() == 0) {// 如果登录失效，提示登录失败
			return;
		}
		DownloadManager down = downloadManagerDAO.getDownloadManagerByid(user.getUserid(), id);

		if (down == null) {
			return;
		}
		downloadManagerService.down_task();

	}

	@RequestMapping("/show/{id}")
	public String show(Model model, @PathVariable(value = "id") long id, HttpServletRequest request) {

		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		User user = getDmpDAO.getLogUser(dmpid);
		if (user == null || user.getUserid() == 0) {// 如果登录失效，提示登录失败
			return "download/show";
		}
		DownloadManager down = downloadManagerDAO.getDownloadManagerByid(user.getUserid(), id);

		if (down == null) {
			return "download/show";
		}
		model.addAttribute("down", down);

		return "download/tuotoushow";//

	}

	@RequestMapping("/getZitiCount")
	public @ResponseBody String getZitiCount(HttpServletRequest request) {
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		User user = getDmpDAO.getLogUser(dmpid);
		JSONObject json = new JSONObject();
		if (user == null || user.getUserid() == 0) {// 如果登录失效，提示登录失败
			json.put("errorCode", 1);
			json.put("remark", "登录失效");
			return json.toString();
		}
		Amazon amazon = amazonaService.getAmazonSettingMethod(B2cEnum.Amazon.getKey());
		long zitiCount = cwbDAO.getAmazonZitiByBranchid(new Date().getTime() - (1000 * 60 * 24 * (amazon.getDelay() <= 0 ? 5 : amazon.getDelay())), user.getBranchid());

		json.put("errorCode", 0);
		json.put("zitiCount", zitiCount);

		return json.toString();

	}

	@RequestMapping("/zitilist/{page}")
	public String zitiList(Model model, @PathVariable(value = "page") long page, HttpServletRequest request) {
		Amazon amazon = amazonaService.getAmazonSettingMethod(B2cEnum.Amazon.getKey());
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		User user = getDmpDAO.getLogUser(dmpid);
		if (user == null || user.getUserid() == 0) {// 如果登录失效，提示登录失败
			Page pageparm = new Page();
			pageparm = new Page(0, page, Page.DOWN_PAGE_NUMBER);
			model.addAttribute("zitiList", new ArrayList<CwbOrder>());
			model.addAttribute("page_obj", pageparm);
			model.addAttribute("page", page);
			model.addAttribute("fileDay", 0);
			model.addAttribute("nouser", 0);
			return "download/zitiList";
		}
		List<CwbOrder> zitiList = cwbDAO.getAmazonZitiListByBranchid(new Date().getTime() - (1000 * 60 * 24 * (amazon.getDelay() <= 0 ? 5 : amazon.getDelay())), user.getBranchid(), page);
		long count = cwbDAO.getAmazonZitiByBranchid(new Date().getTime() - (1000 * 60 * 24 * (amazon.getDelay() <= 0 ? 5 : amazon.getDelay())), user.getBranchid());
		Page pageparm = new Page();
		pageparm = new Page(count, page, Page.DOWN_PAGE_NUMBER);
		model.addAttribute("zitiList", zitiList);
		model.addAttribute("page_obj", pageparm);
		model.addAttribute("page", page);
		model.addAttribute("count", count);
		// 加载导出模板
		List<Exportmould> exportmouldlist = getDmpDAO.getExportmoulds(user, dmpid);
		model.addAttribute("exportmouldlist", exportmouldlist);
		return "download/zitiList";

	}

}
