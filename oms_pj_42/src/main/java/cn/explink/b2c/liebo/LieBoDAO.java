package cn.explink.b2c.liebo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import cn.explink.domain.CwbOrder;
import cn.explink.enumutil.FlowOrderTypeEnum;

@Component
public class LieBoDAO {
	@Autowired
	JdbcTemplate jdbcTemplate;

	public List<CwbOrder> selectCwbtolistByStatus(String customerid, String status) {

		return null;
	}

	// 修改反馈
	public void updateResponseStatus(Map<String, Object> retMap, int status, String cwbs) {
		if (retMap != null && retMap.size() > 0) {
			List<Object[]> paralist = new ArrayList<Object[]>();
			JSONObject jobject = (JSONObject) retMap.get("jsontotal");
			JSONArray jsonArray = (JSONArray) retMap.get("jsonarray");
			if (jobject != null && jobject.size() > 0) {
				int statusCodeTotal = jobject.get("statusCode") != null ? jobject.getInt("statusCode") : -1;
				if (statusCodeTotal == 0) {
					updateSQLResponseStatus_AllSuccess(cwbs, status);
				} else {
					if (jsonArray != null && jsonArray.size() > 0) {
						for (int i = 0; i < jsonArray.size(); i++) {
							JSONObject job = (JSONObject) jsonArray.get(i);
							String order_id = job.get("order_id") != null ? job.getString("order_id") : "";
							int statusCode = job.get("statusCode") != null ? job.getInt("statusCode") : 0;
							String errorCode = job.get("errorCode") != null ? job.getString("errorCode") : "";

							Object[] obj = { statusCode == 0 ? 2 : 3, order_id };
							paralist.add(obj);
						}
						updateSQLResponseStatus(paralist, status);
					}

				}
			}

		}

	}

	/**
	 * 修改所有成功的。
	 * 
	 * @param paralist
	 * @param status
	 */
	public void updateSQLResponseStatus_AllSuccess(String cwbs, int status) {
		String cwbstrs = cwbs.substring(0, cwbs.length() - 1);
		String sql = " update express_ops_cwb_detail set  ";
		if (status == FlowOrderTypeEnum.RuKu.getValue()) {
			sql += " ruku_dangdang_flag=2 where  cwb in (" + cwbstrs + ") ";
		} else if (status == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
			sql += " chuku_dangdang_flag=2 where ruku_dangdang_flag=2  and cwb in (" + cwbstrs + ") ";
		} else if (status == FlowOrderTypeEnum.YiShenHe.getValue()) {
			sql += " deliverystate_dangdang_flag=2  where chuku_dangdang_flag=2  and cwb in (" + cwbstrs + ")   ";
		}
		try {
			jdbcTemplate.update(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void updateSQLResponseStatus(List<Object[]> paralist, int status) {

		String sql = " update express_ops_cwb_detail set 1=1 ";
		if (status == FlowOrderTypeEnum.RuKu.getValue()) {
			sql += " ruku_dangdang_flag=? where  cwb=? ";
		} else if (status == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
			sql += " chuku_dangdang_flag=? where ruku_dangdang_flag=2 and   cwb=? ";
		} else if (status == FlowOrderTypeEnum.YiShenHe.getValue()) {
			sql += " deliverystate_dangdang_flag=?  where chuku_dangdang_flag=2  and  cwb=?  ";
		}

		int[] updateBatch = null;
		try {
			updateBatch = jdbcTemplate.batchUpdate(sql, paralist);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 修改跟踪日志反馈
	public void updateResponseTrackStatus(Map retMap, String flowids) {
		if (retMap != null && retMap.size() > 0) {
			List<Object[]> paralist = new ArrayList<Object[]>();
			updateSQLResponseTrackStatus(paralist, flowids);
		}

	}

	public void updateSQLResponseTrackStatus(List<Object[]> paralist, String flowids) {
		String sql = " update express_ops_order_flow set send_b2c_flag=1 where floworderid in (" + flowids + ") ";
		try {
			jdbcTemplate.update(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
