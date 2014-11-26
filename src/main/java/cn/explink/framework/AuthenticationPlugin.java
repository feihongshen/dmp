package cn.explink.framework;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.web.AuthenticationEntryPoint;

public interface AuthenticationPlugin {

	AuthenticationEntryPoint getAuthenticationEntryPoint();

	AuthenticationProvider getAuthenticationProvider();
}
