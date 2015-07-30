/**
 *
 */
package cn.explink.controller.express;

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

	@Autowired
	private SecurityContextHolderStrategy securityContextHolderStrategy;

	protected User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}
}
