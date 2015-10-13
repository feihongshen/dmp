package cn.explink.filter;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import cn.explink.util.JSONReslutUtil;

public class DmpFilter implements Filter {
	private static ResourceBundle oms = ResourceBundle.getBundle("oms");
	private static String dmpUrl = oms.getString("dmpUrl");

	public void destroy() {

	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");

		String dmpid = request.getParameter("dmpid") == null ? "" : (String) request.getParameter("dmpid");
		if (!"".equals(dmpid)) {
			/** 如果线上不行，就删除掉或者注释此段 ↓↓↓↓↓↓↓↓↓↓↓↓↓------------开始 */
			// String branchStr = JSONReslutUtil.getResultMessage( dmpUrl
			// +"/OMSInterface/getNowUserId;jsessionid="+dmpid,"UTF-8","POST").toString();
			// if(branchStr == null || branchStr.equals("")){
			// response.sendRedirect("/dmp/login");
			// return;
			// }
			// JSONObject jsonValue = JSONObject.fromObject(branchStr);
			// if(jsonValue == null || jsonValue.isEmpty()){
			// response.sendRedirect("/dmp/login");
			// return;
			// }else if(jsonValue.get("nowUserId").toString().equals("")){
			// response.sendRedirect("/dmp/login");
			// return;
			// }
			/** 如果线上不行，就删除掉或者注释此段↑↑↑↑↑↑↑↑↑↑↑↑↑------------结束 */
			request.getSession().setAttribute("dmpid", dmpid);
			// System.out.println("请求oms传递dmpid:"+dmpid);

		}
		/** 如果线上不行，就删除掉或者注释此段↓↓↓↓↓↓↓↓↓↓------------开始 */
		// else{
		//
		// String dmpidSession =
		// request.getSession().getAttribute("dmpid")==null?"":request.getSession().getAttribute("dmpid").toString();
		// String branchStr = JSONReslutUtil.getResultMessage( dmpUrl
		// +"/OMSInterface/getNowUserId;jsessionid="+dmpidSession,"UTF-8","POST").toString();
		// if(branchStr == null || branchStr.equals("")){
		// response.sendRedirect("/dmp/login");
		// return;
		// }
		// JSONObject jsonValue = JSONObject.fromObject(branchStr);
		// if(jsonValue == null || jsonValue.isEmpty()){
		// response.sendRedirect("/dmp/login");
		// return;
		// }else if(jsonValue.get("nowUserId").toString().equals("")){
		// response.sendRedirect("/dmp/login");
		// return;
		// }
		// }
		/** 如果线上不行，就删除掉或者注释此段↑↑↑↑↑↑↑↑↑↑↑↑↑------------结束 */
		filterChain.doFilter(request, response);

	}

	public void init(FilterConfig filterConfig) throws ServletException {

	}

}
