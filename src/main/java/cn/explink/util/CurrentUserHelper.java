package cn.explink.util;

import org.springframework.security.core.context.SecurityContextHolder;

import cn.explink.domain.User;
import cn.explink.service.ExplinkUserDetail;

public class CurrentUserHelper {
	private static CurrentUserHelper instance = new CurrentUserHelper();

	private CurrentUserHelper() {
	}

	public static CurrentUserHelper getInstance() {
		return instance;
	}

	public User getUser() {
		ExplinkUserDetail userDetail = (SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal() instanceof ExplinkUserDetail) ? (ExplinkUserDetail) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal()
				: null;
		User user = (userDetail != null) ? userDetail.getUser() : null;
		return user;
	}
	
	public String getUserName() {
		User currentUser = getUser();
		if (currentUser != null) {
			return currentUser.getUsername();
		} else {
			return "";
		}
	}
}