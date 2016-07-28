/**
 * 
 */
package cn.explink.service.docking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 与武汉飞远自动化设备的中间件对接单例帮助类
 * 
 * @author wangwei 2016年7月14日
 *
 */
@Service
public class AutoAllocationHelper {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	AutoAllocationService autoAllocationService;

	public AutoAllocationHelper() {
		super();
		singleton = this;
	}

	private static AutoAllocationHelper singleton;

	public static void handleResult(String msg) {
		singleton.autoAllocationService.handleResultWhenReceiveReply(msg);
	}
}
