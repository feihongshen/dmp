package cn.explink.controller;

import java.io.File;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.explink.dao.SysConfigDAO;

@RequestMapping("/upload")
@Controller
public class UploadController {

	@Autowired
	SysConfigDAO sysConfigDAO;

	@RequestMapping("/file")
	public @ResponseBody String importExcel(@RequestParam("Filedata") MultipartFile file) throws Exception {
		String identifier = UUID.randomUUID().toString();
		file.transferTo(new File(sysConfigDAO.getConfig("uploadDir") + identifier));
		return identifier;
	}
}
