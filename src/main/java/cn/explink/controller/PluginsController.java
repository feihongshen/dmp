package cn.explink.controller;


import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.tools.ant.types.FileList.FileName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.explink.util.ContextHolderUtils;

@RequestMapping("/plugins")
@Controller
public class PluginsController {

	private static Logger logger = LoggerFactory.getLogger(PluginsController.class);
	 
	@RequestMapping("/getWeightPluginStatus")
	public String getWeightPluginStatus(Model model){
		
		return "plugins/pluginsmessage";
	}
	
	private static String fileName_exe = "FireFoxPlugin4ScaleWeight.exe";
	private static String fileName_zip = "FireFoxPlugin4ScaleWeight.zip";
	@RequestMapping("/pluginsdownload")
	public static void pluginsdownload(HttpServletRequest request,HttpServletResponse response) 
	{
		String fileName = fileName_exe;
		//如果页面选择下载zip
		if ("zip".equals(request.getParameter("fileType"))) 
		{			
			fileName = fileName_zip;
		}
		
		String path = ContextHolderUtils.getRequest().getSession().getServletContext().getRealPath(File.separator) ;
		
		path = path +"firefoxplugins"+File.separator+fileName;
		
		File file = new File(path);
		OutputStream out = null; 
		try { 
			response.reset(); 
			response.setContentType("application/octet-stream; charset=utf-8"); 
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName); 
			out = response.getOutputStream(); 
			out.write(FileUtils.readFileToByteArray(file)); 
			out.flush(); 
		} catch (IOException e) { 
			e.printStackTrace(); 
			} finally { 
				if (out != null) { 
					try { 
						out.close(); 
					} catch (IOException e) 
					{ 
						e.printStackTrace(); 
					} 
				} 
			} 
		}
}
