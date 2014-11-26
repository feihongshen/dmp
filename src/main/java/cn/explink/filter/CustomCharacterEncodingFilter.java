package cn.explink.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.CharacterEncodingFilter;

public class CustomCharacterEncodingFilter extends CharacterEncodingFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		if (requestURI.startsWith(request.getContextPath() + "/dangdangdatasyn/")) {
			request.setCharacterEncoding("GBK");
			filterChain.doFilter(request, response);
			return;
		}
		super.doFilterInternal(request, response, filterChain);
	}

}
