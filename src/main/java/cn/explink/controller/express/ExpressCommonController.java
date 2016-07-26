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
import cn.explink.service.UserService;

/**
 * @author songkaojun 2015年7月30日
 */
@Controller
public class ExpressCommonController {
	/**
	 * 管理员角色ID
	 */
	private static final long ADMIN_ROLE_ID = 0;
	
	/**
	 * 站长角色ID
	 */
	private static final long WAREHOUSE_MASTER_ID = 4;
	
	/**
	 * 省质控
	 */
	private static final long CUSTOM_SERVICE_ID = 1;

    /**
     * 小件员角色ID
     */
    private static final long COURIER_ROLE_ID = 2;


    @Autowired
	private SecurityContextHolderStrategy securityContextHolderStrategy;

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private UserService userService;

	protected User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		
		//Modified by leoliao at 2016-07-26 如果session用户的branchid为0则重新获取用户branchid
		User currUser = userDetail.getUser();
		userService.convertSessionUserBranchId(currUser);
		
		return currUser;
		//return userDetail.getUser();
		//Modified end
	}

	protected boolean isAdmin() {
		User user = this.getSessionUser();
		if (user.getRoleid() == ExpressCommonController.ADMIN_ROLE_ID) {
			return true;
		}
		return false;
	}
	
	protected boolean isWarehouseMaster() {
		User user = this.getSessionUser();
		if (user.getRoleid() == ExpressCommonController.WAREHOUSE_MASTER_ID) {
			return true;
		}
		return false;
	}
	
	protected boolean isCustomService() {
		User user = this.getSessionUser();
		if (user.getRoleid() == ExpressCommonController.CUSTOM_SERVICE_ID) {
			return true;
		}
		return false;
	}

    protected boolean isCourier() {
        User user = this.getSessionUser();
        if (user.getRoleid() == ExpressCommonController.COURIER_ROLE_ID) {
            return true;
        }
        return false;
    }
}
