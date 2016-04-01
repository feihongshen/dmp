package cn.explink.dmp40;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.explink.dao.SystemInstallDAO;
import cn.explink.domain.SystemInstall;

public class Dmp40MenuTag extends TagSupport {

	private static Logger logger = LoggerFactory.getLogger(TagSupport.class);
	
	private static final long serialVersionUID = 1L;
	static PropertyPlaceholderHelper placeholderHelper = new PropertyPlaceholderHelper("${", "}");
	
	public int doStartTag() throws JspTagException {
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspTagException {
		try {
			JspWriter out = this.pageContext.getOut();
			out.print(end().toString());

		} catch (IOException e) {
			logger.error("", e);
		}
		return EVAL_PAGE;
	}

	public StringBuffer end() {
		
		String dmpid = "dmpid=" + this.pageContext.getSession().getId();
		String httppath = "http://" + this.pageContext.getRequest().getLocalAddr()+":"+this.pageContext.getRequest().getLocalPort();

		ServletContext sc = this.pageContext.getServletContext();
		ApplicationContext ac2 = WebApplicationContextUtils
				.getWebApplicationContext(sc);
		Dmp40FunctionDAO dmp40FunctionDAO = ac2.getBean(Dmp40FunctionDAO.class);
		Properties properties = getSystemInstallProperties();
		
		StringBuffer sb = new StringBuffer();
		sb.append("<div id=\"nav\" style=\"display:none;\" class=\"easyui-accordion\" fit=\"true\" border=\"false\">");
		sb.append(Dmp40MenuTag.getEasyuiMultistageTree(
				dmp40FunctionDAO.getFunctionMap(), dmpid,properties,httppath));
		sb.append("</div>");
		return sb;
	}

	public static String getEasyuiMultistageTree(
			Map<Integer, List<Dmp40Function>> map, String dmpid,Properties properties,String httppath) {
		if (map == null || map.size() == 0 || !map.containsKey(0)) {
			return "不具有任何权限,\n请找管理员分配权限";
		}
		StringBuffer menuString = new StringBuffer();
		List<Dmp40Function> list = map.get(0);
		for (Dmp40Function function : list) {
			menuString.append("<div title=\"&nbsp;&nbsp;"
					+ function.getFunctionName() + "\" iconCls=\""
					+ function.getFunctionUrl() + "\">");
			int submenusize = function.getDmp40FunctionList().size();
			if (submenusize == 0) {
				menuString.append("</div>");
			}
			if (submenusize > 0) {
				menuString
						.append("<ul class=\"easyui-tree\"  fit=\"false\" border=\"false\">");
			}
			menuString.append(getChildOfTree(function, 1, map, dmpid, properties,httppath));
			if (submenusize > 0) {
				menuString.append("</ul></div>");
			}
		}
		return menuString.toString();
	}

	private static String getChildOfTree(Dmp40Function parent, int level,
			Map<Integer, List<Dmp40Function>> map, String dmpid,Properties properties,String httppath) {
		StringBuffer menuString = new StringBuffer();
		List<Dmp40Function> list = map.get(level);
		for (Dmp40Function function : list) {
			if (function.getDmp40Function() != null && function.getDmp40Function().getId() != null && function.getDmp40Function().getId().equals(parent.getId())) {
				if (function.getDmp40FunctionList().size() == 0
						|| !map.containsKey(level + 1)) {
					menuString.append(getLeafOfTree(function, dmpid, properties,httppath));
				} else if (map.containsKey(level + 1)) {
					menuString.append("<li state=\"closed\" iconCls=\""
							+ "\" ><span>" + function.getFunctionName()
							+ "</span>");
					menuString.append("<ul >");
					menuString.append(getChildOfTree(function, level + 1, map,
							dmpid,properties,httppath));
					menuString.append("</ul></li>");
				}
			}
		}
		return menuString.toString();
	}
	
	private  Properties getSystemInstallProperties() {
		ServletContext sc = this.pageContext.getServletContext();
		ApplicationContext ac2 = WebApplicationContextUtils
				.getWebApplicationContext(sc);
		SystemInstallDAO systemInstallDAO = ac2.getBean(SystemInstallDAO.class);
		List<SystemInstall> systemInstalls = systemInstallDAO.getAllProperties();
		Properties properties = new Properties();
		for (SystemInstall systemInstall : systemInstalls) {
			properties.put(systemInstall.getName(), systemInstall.getValue());
		}
		return properties;
	}
	
	private static String getLeafOfTree(Dmp40Function function, String dmpid,Properties properties,String httppath) {
		String menuurl = function.getFunctionUrl();
		if(function.getFunctionUrl().indexOf("{") >-1){
//			menuurl =httppath + placeholderHelper.replacePlaceholders(function.getFunctionUrl(), properties);
			menuurl = placeholderHelper.replacePlaceholders(function.getFunctionUrl(), properties);
		}
		StringBuffer menuString = new StringBuffer();
		String icon = "folder";
		menuString.append("<li iconCls=\"");
		menuString.append(icon);
		menuString.append("\"> <a onclick=\"addTab(\'");
		menuString.append(function.getFunctionName());
		menuString.append("\',\'");
		menuString.append(menuurl + dmpid);
		menuString.append("&clickFunctionId=");
		menuString.append(function.getId());
		menuString.append("\',\'");
		menuString.append(icon);
		menuString.append("\')\"  title=\"");
		menuString.append(function.getFunctionName());
		menuString.append("\" url=\"");
		menuString.append(menuurl + dmpid);
		menuString.append("\" href=\"#\" ><span class=\"nav\" >");
		menuString.append(function.getFunctionName());
		menuString.append("</span></a></li>");
		return menuString.toString();
	}

}
