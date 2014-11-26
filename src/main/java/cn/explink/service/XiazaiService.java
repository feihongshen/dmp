package cn.explink.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.SystemInstallDAO;
import cn.explink.domain.SystemInstall;

@Service
public class XiazaiService {

	@Autowired
	SystemInstallDAO systemInstallDAO;

	final Map<Long, Map<Long, String>> map = new HashMap<Long, Map<Long, String>>();
	Map<Long, Long> map1 = new HashMap<Long, Long>();

	@PostConstruct
	public void init() throws Exception {
		map1.put(1l, 0l);
		List<SystemInstall> sList = systemInstallDAO.getAllProperties();
		Map<Long, String> mapA = new HashMap<Long, String>();
		if (sList != null || sList.size() > 0) {
			for (SystemInstall systemInstall : sList) {
				mapA.put(systemInstall.getId(), systemInstall.getName());
			}
		}
		for (long i = 0; i < 10; i++) {
			map.put(i, mapA);
		}
	}

	public void test() {
		if (map1.get(1l) == 1) {
			System.out.println("不可执行");
			return;
		} else {
			map1.put(1l, 1l);
		}
		for (int i = 0; i < 10000000; i++) {
			System.out.println("=====" + i);
		}
		map1.put(1l, 0l);

	}

	public void zhongduanTest() {
		Map<Long, String> mapA = new HashMap<Long, String>();
		mapA.put(1l, "aaa");
		map.put(1l, mapA);
	}

}
