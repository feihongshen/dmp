/**
 *
 */
package cn.explink.controller.express;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;

import cn.explink.domain.User;
import cn.explink.service.ExplinkUserDetail;

/**
 * @author songkaojun 2015年7月30日
 */
@Controller
public class ExpressCommonController {
	/**
	 * 管理员角色ID
	 */
	private static final long ADMIN_ROLE_ID = 0;

	@Autowired
	private SecurityContextHolderStrategy securityContextHolderStrategy;

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	protected User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	protected boolean isAdmin() {
		User user = this.getSessionUser();
		if (user.getRoleid() == ExpressCommonController.ADMIN_ROLE_ID) {
			return true;
		}
		return false;
	}
}
