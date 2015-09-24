package cn.explink.aspect;
import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.explink.b2c.tools.RestHttpServiceHanlder;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.domain.SystemInstall;


@Aspect
@Component
public class SysteminstallAspect {
	@Autowired
	SystemInstallDAO systemInstallDAO;
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@After("execution(* cn.explink.dao.CustomerDAO.*(..))&&@annotation(SystemInstallOperation)")
	public void updateCustomerCache(JoinPoint joinPoint){
		this.commonCacheSynchrno("customercache");
	}
	@After("execution(* cn.explink.dao.UserDAO.*(..))&&@annotation(SystemInstallOperation)")
	public void updateUserSysteminstallData(JoinPoint joinPoint){
		this.commonCacheSynchrno("usercache");
	}
	@After("execution(* cn.explink.b2c.tools.JiontDAO.*(..))&&@annotation(SystemInstallOperation)")
	public void updateJointSysteminstallData(JoinPoint joinPoint){
		this.commonCacheSynchrno("jointcache");
	}
	@After("execution(* cn.explink.dao.BranchDAO.*(..))&&@annotation(SystemInstallOperation)")
	public void updateBranchSysteminstallData(JoinPoint joinPoint){
		this.commonCacheSynchrno("branchcache");
	}
	public void commonCacheSynchrno(String synchroType){
		SystemInstall systemInstall=systemInstallDAO.getSystemInstall("cachesynchro");
		String cachePath="";
		if(systemInstall!=null){
			try {
				//System.out.println("进入URL");
				cachePath=systemInstall.getValue()+"/"+synchroType;
				RestHttpServiceHanlder.sendHttptoServer("",cachePath);
				//System.out.println("退出url");
			} catch (Exception e) {
				this.logger.error("库房站点缓存同步异常,请求路径为"+cachePath, e);
			}
		}
		//System.out.println("你好呀---");
	}
}
