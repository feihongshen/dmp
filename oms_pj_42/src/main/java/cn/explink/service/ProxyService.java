package cn.explink.service;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.ProxyConfDAO;
import cn.explink.domain.ProxyConf;

@Service
public class ProxyService {
	@Autowired
	ProxyConfDAO proxyConfDAO;

	private Logger logger = LoggerFactory.getLogger(ProxyService.class);

	private ProxyConf refresh() {
		int nowUseId = 0;
		// 获取当前使用的
		ProxyConf nowProxyconf = proxyConfDAO.getProxyNowUse(1);
		nowUseId = nowProxyconf == null ? 0 : nowProxyconf.getId();
		// 获取随机的
		ProxyConf randomProxyconf = proxyConfDAO.getProxyRandom(0);

		if (randomProxyconf != null && nowUseId != 0) {
			proxyConfDAO.updateProxyType(nowUseId, 0);// 把当前使用设为未使用
			proxyConfDAO.updateProxyType(randomProxyconf.getId(), 1);// 把随机查到的设置为使用
			return randomProxyconf;
		} else {
			if (randomProxyconf != null) {
				proxyConfDAO.updateProxyType(randomProxyconf.getId(), 1);// 把随机查到的设置为使用
			}
			return nowProxyconf;
		}
	}

	public SocketAddress getNextProxy() {

		// "49.212.161.19", 3128 从数据库中读取
		SocketAddress socketAddress = null;
		boolean check = true;
		int k = 0;
		while (check && k < 10) {
			ProxyConf proxyConf = refresh();
			ProxyConf defualtproxyConf = proxyConfDAO.getDefualtProxy();
			try {
				if (proxyConf != null) {
					Socket socket1 = new Socket(proxyConf.getIp(), proxyConf.getPort());
					socketAddress = socket1.getRemoteSocketAddress();
					socket1.close();
					logger.info("获取的代理ip:" + proxyConf.getIp());
					check = false;
				} else if (defualtproxyConf != null) {
					Socket socket1 = new Socket(defualtproxyConf.getIp(), defualtproxyConf.getPort());
					socketAddress = socket1.getRemoteSocketAddress();
					socket1.close();
					logger.info("获取数据库默认的代理ip:" + defualtproxyConf.getIp());
					check = false;
				} else {
					Socket socket1 = new Socket("161.139.195.98", 80);
					socketAddress = socket1.getRemoteSocketAddress();
					socket1.close();
					logger.info("获取的程序写死默认代理ip:161.139.195.98");
					check = false;
				}

			} catch (UnknownHostException e) {
				// 报异常重新获取
				logger.info("获取的代理ip:" + proxyConf == null ? "null" : proxyConf.getIp() + " 出异常");
				check = true;
				k++;
				proxyConfDAO.updateProxyState(proxyConf == null ? 0 : proxyConf.getId(), 0);
			} catch (IOException e) {
				// 报异常重新获取
				check = true;
				logger.info("获取的代理ip:" + proxyConf == null ? "null" : proxyConf.getIp() + " 出异常");
				k++;
				proxyConfDAO.updateProxyState(proxyConf == null ? 0 : proxyConf.getId(), 0);
			}
		}

		return socketAddress;
	}

	public boolean getProxy() {
		long count = proxyConfDAO.getProxyAllConut(-1);
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	public void removProxy(ProxyConf proxyConf) {
		if (proxyConf != null) {
			proxyConfDAO.updateProxyState(proxyConf.getId(), 0);
			logger.info("删除id为：" + proxyConf.getId() + "代理 ");
		}
	}

}
