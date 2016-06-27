package cn.explink.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import cn.explink.schedule.Constants;

public class CustomPDAAccessFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		if(request.getSession() != null){
			request.getSession().setAttribute(Constants.IS_ACCESS_SOURCE_FROM_PDA, true);
		}
		super.doFilter(request, response, filterChain);
	}

}
