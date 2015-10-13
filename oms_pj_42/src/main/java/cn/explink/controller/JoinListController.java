package cn.explink.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import cn.explink.util.ServiceUtil;

@RequestMapping("joinlist")
@Controller
public class JoinListController {

	@RequestMapping("/upload")
	public String upload(Model model, @RequestParam("txt") MultipartFile file, HttpServletRequest request, @RequestParam("showMessage") String showMessage) {
		if (!file.isEmpty()) {
			String filePath = request.getSession().getServletContext().getRealPath("/");
			String name = ServiceUtil.jspPath + "InBillPrinting.jsp";
			ServiceUtil.uploadtxtFile(file, filePath, name);
		}
		model.addAttribute("showMessage", "上传成功");
		return "/joinlist/list";
	}

}
