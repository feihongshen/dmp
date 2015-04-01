package cn.explink.b2c.sfxhm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.homegobj.Homegobj;
import cn.explink.b2c.homegobj.xmldto.Good;
import cn.explink.b2c.homegobj.xmldto.OrderDto;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dbPool.DBOperator;
import cn.explink.dbPool.Database;
import cn.explink.enumutil.CwbOrderTypeIdEnum;

@Service
public class SfxhmService_getOrder extends SfxhmService {
	private static Logger logger = LoggerFactory.getLogger(SfxhmService_getOrder.class);

	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CustomerDAO customerDAO;

	public void testSqlserver() {
		Database db = new Database("expressurl#0");
		List list = null;

		String sql = "select * from express_set_user ";

		try {
			list = db.selectDataList(sql, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (list == null) {
			return;
		}
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			System.out.println(map.get("username"));
			System.out.println(map.get("realname"));

		}

	}

	/**
	 * 调用远程数据库的存储过程下载订单
	 * 
	 * @param downloadCount
	 */
	@Transactional
	public void downloadOrdersforRemoteProc() {
		try {

			int isOpenFlag = jointService.getStateForJoint(B2cEnum.SFexpressXHM.getKey());
			if (isOpenFlag == 0) {
				return;
			}

			Sfxhm xm = getSfxhm(B2cEnum.SFexpressXHM.getKey());

			List<Map<String, Object>> list = buildParamsInvokeRemoteServer(xm);

			if (list == null || list.size() == 0) {
				logger.info("当前下载不到小红帽到顺丰的订单数据");
				return;
			}

			List<Map<String, String>> datalist = parseCwbArrByOrderDto(list, xm);

			long warehouseid = xm.getWarehouseid(); // 订单导入的库房Id

			dataImportInterface.Analizy_DataDealByB2c(Long.parseLong(xm.getCustomerid()), B2cEnum.SFexpressXHM.getMethod(), datalist, warehouseid, true);
			logger.info("小红帽-顺丰导入订单临时表成功");

		} catch (Exception e) {
			logger.error("获取小红帽远程数据库订单数据异常", e);
		}

	}

	/**
	 * 返回一个转化为导入接口可识别的对象
	 */
	private List<Map<String, String>> parseCwbArrByOrderDto(List<Map<String, Object>> orderDtoList, Sfxhm xm) {
		List<Map<String, String>> cwbList = new ArrayList<Map<String, String>>();

		for (Map<String, Object> orderDto : orderDtoList) {
			CwbOrderDTO cwbOrder = dataImportDAO_B2c.getCwbByCwbB2ctemp(orderDto.get("Mailno").toString());
			if (cwbOrder != null) {
				logger.warn("获取小红帽-顺丰订单中含有重复数据cwb={}", cwbOrder.getCwb());
				continue;
			}

			Map<String, String> cwbMap = new HashMap<String, String>();
			cwbMap.put("cwb", orderDto.get("Mailno").toString()); // cwb
			cwbMap.put("transcwb", convertEmptyString(orderDto.get("Orderno"))); // transcwb
			cwbMap.put("consigneename", convertEmptyString(orderDto.get("d_contact")));
			cwbMap.put("consigneephone", convertEmptyString(orderDto.get("d_tel")));
			cwbMap.put("consigneemobile", convertEmptyString(orderDto.get("d_mobile")));
			cwbMap.put("cargorealweight", convertEmptyString(orderDto.get("cargo_total_weight"))); // 订单重量(KG)
			cwbMap.put("cwbprovince", convertEmptyString(orderDto.get("d_province")));// 省
			cwbMap.put("cwbcity", convertEmptyString(orderDto.get("d_city")));// 市
			cwbMap.put("cwbcounty", convertEmptyString(orderDto.get("d_county")));// 区
			cwbMap.put("consigneeaddress", convertEmptyString(orderDto.get("d_address")));
			cwbMap.put("receivablefee", "0"); // COD代收款
			cwbMap.put("cwbordertypeid", "1");
			cwbMap.put("customercommand", convertEmptyString(orderDto.get("remark"))); // 备注信息
			cwbMap.put("remark1", convertEmptyString(orderDto.get("C_flag"))); // 备注信息

			cwbList.add(cwbMap);
		}

		return cwbList;
	}

	private static String convertEmptyString(Object str) {
		String returnStr = str == null ? "" : str.toString();
		return returnStr;
	}

	private static String convertZeroString(String str, Map m) {
		String returnStr = str == null ? "0" : str.toString();
		return returnStr;
	}

	/**
	 * 调用远程服务 sqlserver
	 * 
	 * @param xm
	 * @return
	 */
	private List<Map<String, Object>> buildParamsInvokeRemoteServer(Sfxhm xm) {
		DBOperator db = new DBOperator("expressurl#0");
		String callsql = "p_download_order(?)";
		Object para[] = { "int", xm.getDownloadCount(), "in", "" };
		Map<String, Object> map = db.executeProcedure(callsql, para);
		List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("return_table_list");
		return list;
	}

}
