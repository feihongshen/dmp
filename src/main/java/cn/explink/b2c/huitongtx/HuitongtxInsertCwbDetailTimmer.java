package cn.explink.b2c.huitongtx;

import java.util.HashMap;
import java.util.List;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JointService;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.EmailDateDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.EmailDate;
import cn.explink.domain.User;
import cn.explink.service.CwbOrderService;
import cn.explink.service.DataImportService;
import cn.explink.support.transcwb.TransCwbDao;

@Service
public class HuitongtxInsertCwbDetailTimmer {
	private Logger logger = LoggerFactory.getLogger(HuitongtxInsertCwbDetailTimmer.class);
	@Autowired
	HuitongtxDAO huitongtxDAO;
	@Autowired
	HuitongtxService huitongtxService;
	@Autowired
	EmailDateDAO emaildateDAO;
	@Autowired
	JointService jointService;
	@Autowired
	CwbOrderService cwbOrderService;
	@Autowired
	DataImportService_B2c dataImportInterface;
	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;
	@Produce(uri = "jms:topic:addressmatch")
	ProducerTemplate addressmatch;
	@Autowired
	TransCwbDao transCwbDao;
	@Autowired
	DataImportService dataImportService;
	@Autowired
	CwbDAO cwbDAO;

	/**
	 * tmall定时器，查询临时表，插入数据到detail表中。
	 */

	public void selectTempAndInsertToCwbDetail() {

		try {
			int isOpenFlag = jointService.getStateForJoint(B2cEnum.Huitongtx.getKey());
			if (isOpenFlag == 0) {
				logger.info("未开启汇通天下对接");
				return;
			}

			Huitongtx httx = huitongtxService.getHuitongtx(B2cEnum.Huitongtx.getKey());

			logger.info("开始执行了[汇通天下]定时器");
			List<CwbOrderDTO> cwbOrderList = dataImportDAO_B2c.getCwbOrderTempByKeys(httx.getCustomerids());
			if (cwbOrderList == null) {
				return;
			}
			if (cwbOrderList.size() == 0) {
				return;
			}

			int k = 1;
			int batch = 50;
			while (true) {

				int fromIndex = (k - 1) * batch;
				if (fromIndex >= cwbOrderList.size()) {
					break;
				}
				int toIdx = k * batch;
				if (k * batch > cwbOrderList.size()) {
					toIdx = cwbOrderList.size();
				}
				List<CwbOrderDTO> subList = cwbOrderList.subList(fromIndex, toIdx);
				ImportSubList(httx.getCustomerids(), httx.getWarehouseid(), subList);
				k++;
			}
			logger.info("执行了[汇通天下]定时器结束");
		} catch (Exception e) {
			logger.error("执行了[汇通天下]定时器未知异常", e);
		}

	}

	@Transactional
	public void ImportSubList(String customerid, long tmallWarehouseId, List<CwbOrderDTO> cwbOrderList) {

		for (CwbOrderDTO cwbOrder : cwbOrderList) {
			try {
				ImportSignOrder(customerid, tmallWarehouseId, cwbOrder);
			} catch (Exception e) {
				logger.error("定时器临时表插入或修改方法执行异常!customerid=" + customerid, e);
			}
		}
	}

	private void ImportSignOrder(String customerid, long tmallWarehouseId, CwbOrderDTO cwbOrder) {

		boolean isExistsTogetherFlag = huitongtxDAO.isExistsTogetherCwbInfo(cwbOrder.getCwb(), customerid); // 查询是否存在合单情况
		if (isExistsTogetherFlag) { // 要合单子
			huitongtxDAO.update_CwbTogetherInfo(cwbOrder.getCwb(), cwbOrder.getShipcwb(), cwbOrder.getTranscwb());

			logger.info("查询临时表-汇通天下[一件多票]产生合单数据!订单号：{},运单号:{}", cwbOrder.getCwb(), cwbOrder.getShipcwb());
		} else {
			// insert
			User user = new User();
			user.setUserid(1);
			long warehouse_id = tmallWarehouseId;
			long warehouseid = warehouse_id != 0 ? warehouse_id : dataImportInterface.getTempWarehouseIdForB2c(); // 获取虚拟库房
																													// Id,所有的B2C对接都会导入默认的虚拟库房里面，方便能够统计到。
			user.setBranchid(warehouseid);
			EmailDate ed = dataImportService.getOrCreateEmailDate(cwbOrder.getCustomerid(), 0, warehouseid);

			emaildateDAO.editEditEmaildateForCwbcountAdd(ed.getEmaildateid());

			cwbOrderService.insertCwbOrder(cwbOrder, cwbOrder.getCustomerid(), ed.getWarehouseid(), user, ed);
			logger.info("定时器临时表插入detail表成功!cwb={},shipcwb={}", cwbOrder.getCwb(), cwbOrder.getShipcwb());

			if (cwbOrder.getExcelbranch() == null || cwbOrder.getExcelbranch().length() == 0) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("cwb", cwbOrder.getCwb());
				map.put("userid", "1");
				addressmatch.sendBodyAndHeaders(null, map);
			}

		}
		dataImportDAO_B2c.update_CwbDetailTempByCwb(cwbOrder.getOpscwbid());
	}

}
