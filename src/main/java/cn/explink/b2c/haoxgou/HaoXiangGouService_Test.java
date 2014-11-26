package cn.explink.b2c.haoxgou;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.explink.ExplinkService;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointService;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.pos.tools.JacksonMapper;

/**
 * 测试订单导入人
 * 
 * @author Administrator
 *
 */
@Service
public class HaoXiangGouService_Test extends HaoXiangGouService {
	private Logger logger = LoggerFactory.getLogger(HaoXiangGouService_Test.class);

	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	ExplinkService explinkService;
	@Autowired
	CwbDAO cwbDAO;

	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	CustomWareHouseDAO customWarehouseDAO;

	@Autowired
	DataImportService_B2c dataImportService_B2c;
	@Autowired
	JointService jointService;
	@Autowired
	HaoXiangGouService_PeiSong haoXiangGouService_PeiSong;
	@Autowired
	HaoXiangGouService_TuiHuo haoXiangGouService_TuiHuo;

	/**
	 * 订单下载接口 配送 上门换货
	 */
	public String GetOrdWayBillInfoForD2D_Test(String request_data, String requesttype) {
		HaoXiangGou hxg = getHaoXiangGou(B2cEnum.HaoXiangGou.getKey());
		int isOpenFlag = jointService.getStateForJoint(B2cEnum.Rufengda.getKey());
		if (isOpenFlag == 0) {
			logger.warn("未开启0好享购0获取订单接口");
			return "未开启0好享购0获取订单接口";
		}

		try {

			String rtn_data = DESUtil.encrypt(request_data, hxg.getDes_key()); // DES加密，JSON格式字符串
			rtn_data = DESUtil.decrypt(request_data, hxg.getDes_key()); // DES解密，JSON格式字符串

			List<Map<String, Object>> datalist = parseOrderListfromJSON(rtn_data); // JSON转化为集合

			if (datalist == null || datalist.size() == 0) {
				logger.info("当前没有下载到待处理的订单0好享购0-Test");
				return "当前没有下载到待处理的订单0好享购0-Test";
			}
			List<Map<String, String>> orderlist = null;
			if (requesttype.equals("1")) {
				orderlist = haoXiangGouService_PeiSong.getPamarsListByDataList(datalist, hxg);
			} else {
				orderlist = haoXiangGouService_TuiHuo.getPamarsListByDataList(datalist, hxg);
			}

			dataImportService_B2c.Analizy_DataDealByB2c(Long.valueOf(hxg.getCustomerids()), B2cEnum.HaoXiangGou.getMethod(), orderlist, hxg.getWarehouseid(), true);

			logger.info("Test处理好享购-配送导入后的订单信息成功");

			return "Test Orders Import Success!";

		} catch (Exception e) {
			logger.error("调用0好享购0webservice服务器异常" + e.getMessage(), e);
			return "Test Orders Import  Error!" + e.getMessage();
		}

	}

	private List<Map<String, Object>> parseOrderListfromJSON(String json) {
		try {
			return JacksonMapper.getInstance().readValue(json, List.class);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("rtn_data转化为List<Map<String, Object>>发生异常", e);
			return null;
		}

	}

}
