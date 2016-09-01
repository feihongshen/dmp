package cn.explink.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.SystemInstallDAO;
import cn.explink.domain.SystemInstall;

@Service
public class SystemInstallValueService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	SystemInstallDAO systemInstallDAO;	

	public SystemInstallValueService() {
		super();
		singleton = this;
	}

	private static SystemInstallValueService singleton;

	public static SystemInstall getSystemInstallByName(String name) {
		
		return singleton.systemInstallDAO.getSystemInstall(name);
	}
}
