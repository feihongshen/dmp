package cn.explink.dao;

import java.util.HashMap;

import org.springframework.stereotype.Component;

@Component
public class SysConfigDAO {

	private HashMap<String, String> configs = new HashMap<String, String>();

	public String getConfig(String param) {
		return configs.get(param);
	}
}
