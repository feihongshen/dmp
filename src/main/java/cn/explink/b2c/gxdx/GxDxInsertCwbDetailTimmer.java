/**
 * 
 */
package cn.explink.b2c.gxdx;

import java.util.HashMap;
import java.util.List;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.gxdx.xmldto.RequestDto;
import cn.explink.b2c.jiuye.JiuYe;
import cn.explink.b2c.jiuye.JiuYeInsertCwbDetailTimmer;
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

/**
 * @ClassName: GxDxInsertCwbDetailTimmer 
 * @Description: TODO
 * @Author: 王强
 * @Date: 2015年11月13日上午11:40:59
 */
@Service
public class GxDxInsertCwbDetailTimmer {
	private Logger logger =LoggerFactory.getLogger(GxDxInsertCwbDetailTimmer.class);
	
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	GxDxService gxDxService;
	@Autowired
	JointService jointService;
	@Autowired
	EmailDateDAO emaildateDAO;
	@Autowired
	CwbOrderService cwbOrderService;
	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;
	@Autowired
	DataImportService_B2c dataImportService_B2c;
	@Autowired
	DataImportService  dataImportService;
	@Produce(uri="jms:topic:addressmatch")
	ProducerTemplate addressmatch;
	/**
	 * 广信电信定时器，查询临时表，插入数据到detail表中。 
	 */
	public void execute(int key){
		
		GxDx dms=gxDxService.getGxDx(key);
		List<CwbOrderDTO> cwbOrderList=dataImportDAO_B2c.getCwbOrderTempByKeys(dms.getCustomerid());
		if(cwbOrderList==null){
			return;
		}
		if(cwbOrderList.size()==0){
			return;
		}
		int k=1;
		int batch=50;
		while(true){
			int fromIndex = (k-1)*batch;
			if(fromIndex>=cwbOrderList.size()){
				break;
			}
			int toIdx = k*batch;
			if(k*batch>cwbOrderList.size()){
				toIdx=cwbOrderList.size();
			}
			List<CwbOrderDTO> subList = cwbOrderList.subList(fromIndex, toIdx);
			ImportSubList(dms, subList); 
			k++;
		}
	
		
	}
	
	public void selectTempAndInsertToCwbDetail(){
		for(B2cEnum enums:B2cEnum.values()){
			if(enums.getMethod().contains("guangxindianxin")){
				int isOpenFlag=jointService.getStateForJoint(enums.getKey());
				if (isOpenFlag == 0) {
					logger.info("未开启[广信电信]对接！");
					continue;
				}
				execute(enums.getKey());
				
			}
		}
	}

	@Transactional
	public void ImportSubList(GxDx gxDx,List<CwbOrderDTO> cwbOrderList) {
		
		for(CwbOrderDTO cwbOrder:cwbOrderList){
			try {
				ImportSignOrder(gxDx, cwbOrder);
			} catch (Exception e) {
				logger.error("广信电信 定时器临时表插入或修改方法执行异常!cwb="+cwbOrder.getCwb(),e);
			}
		}
	}
	
	private void ImportSignOrder(GxDx  dms, CwbOrderDTO cwbOrder) {
		CwbOrder order=cwbDAO.getCwbByCwb(cwbOrder.getCwb());
		if (order!=null) {                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
			logger.warn("查询临时表-检测到0广信电信0有重复数据,已过滤!订单号：{},运单号:{}",cwbOrder.getCwb(),cwbOrder.getShipcwb());
		}else{
			User user=new User();
			user.setUserid(1);
			long warehouse_id=dms.getExportbranchid();
			long warehouseid =warehouse_id!=0?warehouse_id:dataImportService_B2c.getTempWarehouseIdForB2c();  //获取虚拟库房 Id,所有的B2C对接都会导入默认的虚拟库房里面，方便能够统计到。
			user.setBranchid(warehouseid);
			EmailDate ed = dataImportService.getOrCreateEmailDate(cwbOrder.getCustomerid(), 0,warehouseid);
			
			emaildateDAO.editEditEmaildateForCwbcountAdd(ed.getEmaildateid());
			
			cwbOrderService.insertCwbOrder(cwbOrder, cwbOrder.getCustomerid(), ed.getWarehouseid(), user, ed);
			logger.info("0广信电信0定时器临时表插入detail表成功!cwb={},shipcwb={}",cwbOrder.getCwb(),cwbOrder.getShipcwb());
			
			if(cwbOrder.getExcelbranch()==null||cwbOrder.getExcelbranch().length()==0){
				HashMap<String, Object> map=new HashMap<String, Object>();
				map.put("cwb", cwbOrder.getCwb());
				map.put("userid", "1");
				addressmatch.sendBodyAndHeaders(null, map);
			}
			
		}
		dataImportDAO_B2c.update_CwbDetailTempByCwb(cwbOrder.getOpscwbid());
	}
}