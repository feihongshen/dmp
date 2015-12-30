package cn.explink.b2c.auto.order.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.tmall.CwbColumnSet;
import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.domain.EmailDate;
import cn.explink.domain.ImportValidationManager;
import cn.explink.domain.User;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.exception.CwbException;
import cn.explink.service.DataImportService;

@Service
public class TPSOrderImportService_B2c {
	private Logger logger = LoggerFactory.getLogger(DataImportService.class);
	@Autowired
	ImportValidationManager importValidationManager;
	@Autowired
	CwbColumnSet cwbColumnSet;
	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;
	@Autowired
	CustomWareHouseDAO customWarehouseDAO;
	@Autowired
	DataImportService_B2c dataImportService_B2c;

	/**
	 * 提供数据导入接口-对接用到
	 * 
	 * @param customerid
	 * @param branchid
	 * @param ed
	 * @param b2cFlag
	 * @param xmlList
	 * @return
	 */
	@Transactional
	public void Analizy_DataDealByB2c(long customerid, String b2cFlag, CwbOrderDTO cwbOrder, long warehouse_id, // 对接设置中传过来的ID
			EmailDate ed) throws Exception {
	
		long warehouseid = warehouse_id == 0 ? dataImportService_B2c.getTempWarehouseIdForB2c() : warehouse_id; // 获取虚拟库房Id,所有的B2C对接如果未设置都会导入默认的虚拟库房里面，方便能够统计到。
		cwbOrder.setStartbranchid(warehouseid);
		User user = new User();
		user.setUserid(1);
		user.setBranchid(warehouseid);

		 // 导入临时表，然后定时器处理
		try {
			//接口数据插入临时表
			dataImportDAO_B2c.insertCwbOrder_toTempTable(cwbOrder, customerid, warehouseid, user, ed);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(b2cFlag + "数据插入临时表发生未知异常cwb=" + cwbOrder.getCwb(), e);
			Exception ex=null;
			long flowordertye=FlowOrderTypeEnum.DaoRuShuJu.getValue();
			if(e instanceof CwbException){
				flowordertye=((CwbException)e).getFlowordertye();
				ex=e;
			}else{
				ex=new CwbException(cwbOrder.getCwb(),flowordertye,e.getMessage());
			}
			throw e;
		}
	}
	

}
