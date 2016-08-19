package cn.explink.filter;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;


public abstract class HttpRequestWordFilter implements Filter{
	
	/**
	 * 过滤从request里获取的Parameter的value值
	 * @param paramValue
	 * @return
	 */
	protected abstract String filterParamValue(String paramValue);
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
	      FilterChain chain) throws IOException, ServletException {
	
		HttpRequestWordFilterWrapper xssRequest = new HttpRequestWordFilterWrapper(
														(HttpServletRequest) request)
		{
			@Override
			protected String filterWords(String paramValue) {
				return filterParamValue(paramValue);
			}
			
		};
		chain.doFilter(xssRequest, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}
	
	@Override
	public void destroy() {
	}
	
	/**
	 * 内部wapper类
	 * @author zhili01.liang
	 *
	 */
	public abstract class HttpRequestWordFilterWrapper extends HttpServletRequestWrapper {

		/**
		 * 过滤从request里获取的Parameter值
		 * @param content
		 * @return
		 */
		protected abstract String filterWords(String paramValue);

		public HttpRequestWordFilterWrapper(HttpServletRequest request) {
			super(request);
		}

		/**
		 * 覆盖getHeader方法
		 * 
		 * @param name
		 * @return
		 */
		@Override
		public String getHeader(String name) {
			return filterWords(super.getHeader(name));
		}

		/**
		 * 覆盖getParameter方法
		 * 
		 * @param name
		 * @return
		 */
		@Override
		public String getParameter(String name) {
			return filterWords(super.getParameter(name));
		}

		/**
		 * 覆盖getParameterValues方法。 
		 * @param name
		 * @return
		 */
		@Override
		public String[] getParameterValues(String parameter) {
			String[] values = super.getParameterValues(parameter);
			if (values == null || values.length < 1) {
				return values;
			}
			int count = values.length;
			String[] encodedValues = new String[count];
			for (int i = 0; i < count; i++) {
				encodedValues[i] = filterWords(values[i]);
			}
			return encodedValues;
		}

		/**
		 * 覆盖getParameterMap方法。 
		 * @return
		 */
		@Override
		public Map<String, String[]> getParameterMap() {
			@SuppressWarnings("unchecked")
			Map<String, String[]> paramMap = super.getParameterMap();
			if (paramMap == null) {
				return paramMap;
			}
			Iterator<Entry<String, String[]>> iterator = paramMap.entrySet().iterator();

			while (iterator.hasNext()) {
				Entry<String, String[]> entry = iterator.next();
				String[] values = entry.getValue();
				for (int i = 0; i < values.length; i++) {
					values[i] = filterWords(values[i]);
				}
				entry.setValue(values);
			}
			return paramMap;
		}

		/*
		 * FireFox，Chrome下不需覆盖。但IE下需要
		 *  @Override 
		 * public String getQueryString() 
		 * { 
		 * 		return wordFilter(super.getQueryString()); 
		 * }
		 */

	}
	
}
