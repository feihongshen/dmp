package cn.explink.dmp40;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Component;

import cn.explink.domain.User;
import cn.explink.service.ExplinkUserDetail;

@Component
public class Dmp40FunctionDAO {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	public Map<Integer, List<Dmp40Function>> getFunctionMap() {

		Map<Integer, List<Dmp40Function>> functionMap = new HashMap<Integer, List<Dmp40Function>>();
		Map<String, Dmp40Function> loginActionlist = this.getFunctions();

		if (loginActionlist.size() > 0) {
			Collection<Dmp40Function> allFunctions = loginActionlist.values();
			for (Dmp40Function function : allFunctions) {
				if (!functionMap.containsKey(function.getFunctionLevel() + 0)) {
					functionMap.put(function.getFunctionLevel() + 0,
							new ArrayList<Dmp40Function>());
				}
				functionMap.get(function.getFunctionLevel() + 0).add(function);
				if (loginActionlist.get(function.getParentFunctionId()) != null) {
					Dmp40Function parentFunction = loginActionlist.get(function
							.getParentFunctionId());
					function.setDmp40Function(parentFunction);
					parentFunction.getDmp40FunctionList().add(function);
				}
			}
			// 菜单栏排序
			Collection<List<Dmp40Function>> c = functionMap.values();
			for (List<Dmp40Function> list : c) {
				Collections.sort(list, new NumberComparator());
			}
		}
		return functionMap;
	}

	public Map<String, Dmp40Function> getFunctions() {

		Map<String, Dmp40Function> loginActionlist = new HashMap<String, Dmp40Function>();
		List<Dmp40Function> functionList = getFunctionFromDB(this
				.getSessionUser().getRoleid());
		for (Dmp40Function function : functionList) {
			loginActionlist.put(function.getId(), function);
		}
		return loginActionlist;
	}

	public List<Dmp40Function> getAllFunctionFromDB() {
		return this.getFunctionFromDB(-1);
	}

	public List<Dmp40Function> getFunctionFromDB(long roleid) {
		String sql = null;
		if (roleid == -1) {
			sql = "select * from dmp40_function";
		} else {
			sql = "select * from dmp40_function a inner join express_set_role_menu_new b on a.ID = b.menuid where b.roleid = "
					+ roleid;
		}
		List<Dmp40Function> lt = new ArrayList<Dmp40Function>();
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql);
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = list.get(i);
				Dmp40Function tsf = new Dmp40Function();
				tsf.setId(map.get("ID").toString());
				if (map.get("functionlevel") != null) {
					tsf.setFunctionLevel((Integer) map.get("functionlevel"));
				}
				if (map.get("functionname") != null) {
					tsf.setFunctionName((String) map.get("functionname"));
				}
				if (map.get("functionorder") != null) {
					tsf.setFunctionOrder((String) map.get("functionorder"));
				}
				if (map.get("functionurl") != null) {
					tsf.setFunctionUrl((String) map.get("functionurl"));
				}
				if (map.get("parentfunctionid") != null) {
					tsf.setParentFunctionId((String) map
							.get("parentfunctionid"));
				}
				lt.add(tsf);
			}
		}
		return lt;
	}

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy
				.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}
}

class NumberComparator implements Comparator<Object> {

	private boolean ignoreCase = true;

	public NumberComparator() {
	}

	public NumberComparator(boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
	}

	public int compare(Object obj1, Object obj2) {
		String o1 = "";
		String o2 = "";
		if (ignoreCase) {
			Dmp40Function c1 = (Dmp40Function) obj1;
			Dmp40Function c2 = (Dmp40Function) obj2;
			o1 = c1.getFunctionOrder();
			o2 = c2.getFunctionOrder();
		}
		if (o1 != null && o2 != null) {
			for (int i = 0; i < o1.length(); i++) {
				if (i == o1.length() && i < o2.length()) {
					return -1;
				} else if (i == o2.length() && i < o1.length()) {
					return 1;
				}
				char ch1 = o1.charAt(i);
				char ch2 = o2.charAt(i);
				if (ch1 >= '0' && ch2 <= '9') {
					int i1 = getNumber(o1.substring(i));
					int i2 = getNumber(o2.substring(i));
					if (i1 == i2) {
						continue;
					} else {
						return i1 - i2;
					}
				} else if (ch1 != ch2) {
					return ch1 - ch2;
				}
			}
		}
		return 0;
	}

	private int getNumber(String str) {
		int num = Integer.MAX_VALUE;
		int bits = 0;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) >= '0' && str.charAt(i) <= '9') {
				bits++;
			} else {
				break;
			}
		}
		if (bits > 0) {
			num = Integer.parseInt(str.substring(0, bits));
		}
		return num;
	}
}
