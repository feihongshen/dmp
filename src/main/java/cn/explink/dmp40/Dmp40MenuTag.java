package cn.explink.dmp40;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class Dmp40MenuTag extends TagSupport {

	private static final long serialVersionUID = 1L;

	public int doStartTag() throws JspTagException {
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspTagException {
		try {
			JspWriter out = this.pageContext.getOut();
			out.print(end().toString());

		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

	public StringBuffer end() {

		ServletContext sc = this.pageContext.getServletContext();
		ApplicationContext ac2 = WebApplicationContextUtils
				.getWebApplicationContext(sc);
		Dmp40FunctionDAO dmp40FunctionDAO = ac2.getBean(Dmp40FunctionDAO.class);

		StringBuffer sb = new StringBuffer();
		sb.append("<div id=\"nav\" style=\"display:none;\" class=\"easyui-accordion\" fit=\"true\" border=\"false\">");
		sb.append(Dmp40MenuTag.getEasyuiMultistageTree(dmp40FunctionDAO
				.getFunctionMap()));
		sb.append("</div>");
		return sb;
	}

	public static String getEasyuiMultistageTree(
			Map<Integer, List<Dmp40Function>> map) {
		if (map == null || map.size() == 0 || !map.containsKey(0)) {
			return "不具有任何权限,\n请找管理员分配权限";
		}
		StringBuffer menuString = new StringBuffer();
		List<Dmp40Function> list = map.get(0);
		for (Dmp40Function function : list) {
			menuString.append("<div title=\"&nbsp;&nbsp;" + function.getFunctionName()
					+ "\" iconCls=\"" + function.getFunctionUrl() + "\">");
			int submenusize = function.getDmp40FunctionList().size();
			if (submenusize == 0) {
				menuString.append("</div>");
			}
			if (submenusize > 0) {
				menuString
						.append("<ul class=\"easyui-tree\"  fit=\"false\" border=\"false\">");
			}
			menuString.append(getChildOfTree(function, 1, map));
			if (submenusize > 0) {
				menuString.append("</ul></div>");
			}
		}
		return menuString.toString();
	}

	private static String getChildOfTree(Dmp40Function parent, int level,
			Map<Integer, List<Dmp40Function>> map) {
		StringBuffer menuString = new StringBuffer();
		List<Dmp40Function> list = map.get(level);
		for (Dmp40Function function : list) {
			if (function.getDmp40Function().getId().equals(parent.getId())) {
				if (function.getDmp40FunctionList().size() == 0
						|| !map.containsKey(level + 1)) {
					menuString.append(getLeafOfTree(function));
				} else if (map.containsKey(level + 1)) {
					menuString.append("<li state=\"closed\" iconCls=\""
							+ "\" ><span>" + function.getFunctionName()
							+ "</span>");
					menuString.append("<ul >");
					menuString.append(getChildOfTree(function, level + 1, map));
					menuString.append("</ul></li>");
				}
			}
		}
		return menuString.toString();
	}

	private static String getLeafOfTree(Dmp40Function function) {

		StringBuffer menuString = new StringBuffer();
		String icon = "folder";
		menuString.append("<li iconCls=\"");
		menuString.append(icon);
		menuString.append("\"> <a onclick=\"addTab(\'");
		menuString.append(function.getFunctionName());
		menuString.append("\',\'");
		menuString.append(function.getFunctionUrl());
		menuString.append("&clickFunctionId=");
		menuString.append(function.getId());
		menuString.append("\',\'");
		menuString.append(icon);
		menuString.append("\')\"  title=\"");
		menuString.append(function.getFunctionName());
		menuString.append("\" url=\"");
		menuString.append(function.getFunctionUrl());
		menuString.append("\" href=\"#\" ><span class=\"nav\" >");
		menuString.append(function.getFunctionName());
		menuString.append("</span></a></li>");
		return menuString.toString();
	}

}
