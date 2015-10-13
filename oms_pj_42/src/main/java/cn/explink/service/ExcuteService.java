package cn.explink.service;

import org.springframework.stereotype.Service;

@Service
public class ExcuteService {

	public String ExcuteByPageWhereSql(String opscwbids) {
		String sql = "SELECT CONVERT(opscwbid USING gb2312),CONVERT(cwb USING gb2312),CONVERT(startbranchid USING gb2312),"
				+ "CONVERT(customerid USING gb2312),CONVERT(carwarehouse USING gb2312),CONVERT(caramount USING gb2312),CONVERT(deliverid USING gb2312),"
				+ "CONVERT(cwbreachbranchid USING gb2312),CONVERT(cwbreachdeliverbranchid USING gb2312),CONVERT(posremark USING gb2312),CONVERT(podfeetoheadtime USING gb2312),"
				+ "CONVERT(podfeetoheadchecktime USING gb2312),CONVERT(receivedfeecash USING gb2312),CONVERT(receivedfeepos3 USING gb2312),CONVERT(shipcwb USING gb2312),"
				+ "CONVERT(consigneename USING gb2312),CONVERT(destination USING gb2312) FROM express_ops_cwb_detail";
		if (opscwbids.length() > 0) {
			sql += " where opscwbid in (";
			StringBuffer w = new StringBuffer();
			if (opscwbids.trim().length() > 0) {
				for (String id : opscwbids.split("\r\n")) {
					w.append("'" + id.trim());
					w.append("',");
				}
				String u = w.substring(0, w.length() - 1) + ")";
				w = new StringBuffer(u);
			}
			sql += w.substring(0, w.length());
		}
		return sql;
	}

}
