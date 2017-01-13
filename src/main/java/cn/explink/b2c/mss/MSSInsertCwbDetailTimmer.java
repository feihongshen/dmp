package cn.explink.b2c.mss;

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
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.EmailDateDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.EmailDate;
import cn.explink.domain.User;
import cn.explink.service.CwbOrderService;
import cn.explink.service.DataImportService;
import cn.explink.service.addressmatch.AddressMatchService;
import cn.explink.util.B2cUtil;

@Service
public class MSSInsertCwbDetailTimmer {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	MssService mssService;
	@Autowired
	DataImportService_B2c dataImportService_B2c;
	@Autowired
	EmailDateDAO emaildateDAO;
	@Autowired
	JointService jointService;
	@Autowired
	CwbOrderService cwbOrderService;
	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;

	@Produce(uri = "jms:topic:addressmatch")
	ProducerTemplate addressmatch;
	@Autowired
	DataImportService dataImportService;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	B2cUtil b2cUtil;
	@Autowired
	AddressMatchService addressMatchService;
	@Autowired
	BranchDAO branchDAO;

	public void selectTempAndInsertToCwbDetail() {
		int key = B2cEnum.MSS.getKey();
		int isOpenFlag = this.jointService.getStateForJoint(key);
		if (isOpenFlag == 0) {
			this.logger.info("未开启【美食送】对接！");
			return;
		}
		this.execute(key);

	}

	/**
	 * 【美食送】定时器，查询临时表，插入数据到detail表中,每50条一批。
	 */
	private void execute(int key) {
		Mss dms = this.b2cUtil.getViewBean(key, Mss.class);
		List<CwbOrderDTO> cwbOrderList = this.dataImportDAO_B2c.getCwbOrderTempByKeys(dms.getCustomerid());
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
			if ((k * batch) > cwbOrderList.size()) {
				toIdx = cwbOrderList.size();
			}
			List<CwbOrderDTO> subList = cwbOrderList.subList(fromIndex, toIdx);
			this.ImportSubList(dms, subList);
			k++;
		}
	}

	/**
	 * 订单写入detail
	 * 
	 * @param smile
	 * @param cwbOrderList
	 */
	@Transactional
	public void ImportSubList(Mss smile, List<CwbOrderDTO> cwbOrderList) {

		for (CwbOrderDTO cwbOrder : cwbOrderList) {
			try {
				this.ImportSignOrder(smile, cwbOrder);
			} catch (Exception e) {
				this.logger.error("定时器临时表插入或修改方法执行异常!cwb=" + cwbOrder.getCwb(), e);
			}

		}
	}

	/**
	 * 从临时表读取数据写入到detail
	 *
	 * @param dms
	 * @param cwbOrder
	 */
	private void ImportSignOrder(Mss dms, CwbOrderDTO cwbOrder) {
		CwbOrder order = this.cwbDAO.getCwbByCwb(cwbOrder.getCwb());
		if (order != null) { // 要合单子
			this.logger.warn("查询临时表-检测到0美食送0有重复数据,已过滤!订单号：{},运单号:{}", cwbOrder.getCwb(), cwbOrder.getShipcwb());
		} else {
			User user = new User();
			user.setUserid(1);
			//long warehouse_id = dms.getWarehouseid();
			long warehouse_id=0L;
			long warehouseid = warehouse_id != 0 ? warehouse_id : this.dataImportService_B2c.getTempWarehouseIdForB2c(); // 获取虚拟库房
			// Id,所有的B2C对接都会导入默认的虚拟库房里面，方便能够统计到。
			user.setBranchid(warehouseid);
			EmailDate ed = this.dataImportService.getOrCreateEmailDate(cwbOrder.getCustomerid(), 0, warehouseid);

			this.emaildateDAO.editEditEmaildateForCwbcountAdd(ed.getEmaildateid());
			this.cwbOrderService.insertCwbOrder(cwbOrder, cwbOrder.getCustomerid(), ed.getWarehouseid(), user, ed);
			this.logger.info("定时器临时表插入detail表成功!cwb={},shipcwb={}", cwbOrder.getCwb(), cwbOrder.getShipcwb());
			if ((cwbOrder.getExcelbranch() == null) || (cwbOrder.getExcelbranch().length() == 0)) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("cwb", cwbOrder.getCwb());
				map.put("userid", "1");
				this.addressmatch.sendBodyAndHeaders(null, map);
			}
		}
		this.dataImportDAO_B2c.update_CwbDetailTempByCwb(cwbOrder.getOpscwbid());

	}
}
