package cn.explink.b2c.tools.b2cmonntor;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;

import org.apache.camel.CamelContext;
import org.apache.camel.Header;
import org.apache.camel.builder.RouteBuilder;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.dangdang.DangDangFlowEnum;
import cn.explink.b2c.tmall.TmallFlowEnum;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.vipshop.VipShopFlowEnum;
import cn.explink.b2c.yihaodian.YihaodianFlowEnum;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.CwbFlowOrderTypeEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.service.CwbOrderWithDeliveryState;
import cn.explink.util.JMath;

/**
 * 针对B2C对接的监控，若OMS没有存入数据或发生推送失败等等，则可以用此类来监控
 * 
 * @author Administrator
 *
 */
@Service
public class B2cAutoDownloadMonitorService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	B2cJointMonitorDAO b2cdataDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	B2cAutoDownloadMonitorDAO b2cAutoDownloadMonitorDAO;

	// public void exportB2cDataExptInfo(String cwb,String customerid,
	// long flowordertype, String starttime, String endtime,
	// long send_b2c_flag, long hand_deal_flag,HttpServletResponse response) {
	//
	// List<B2CMonitorData>
	// b2cdatalist=b2cAutoDownloadMonitorDAO.selectB2cIdsByB2cMonitorDataList(cwb,
	// customerid,flowordertype,
	// send_b2c_flag,hand_deal_flag,starttime,endtime);
	// String excelbranch = "B2C对接异常订单列表";
	// JMath.CreateExcelHeader(response, excelbranch);
	// // 创建excel 新的工作薄
	// HSSFWorkbook workbook = new HSSFWorkbook();
	// HSSFSheet sheet = workbook.createSheet("对接异常订单列表"); //
	// Sheet1没个新建的Excel都是这个默认值
	// sheet.getPrintSetup().setLandscape(true); // true：横向 false：纵向
	// int rows = 0; // 第一行
	// int c = 0; // 第一列
	//
	// HSSFRow row = sheet.createRow((short) rows++);
	// JMath.setCellValue(row, c++, "序号");
	// JMath.setCellValue(row, c++, "供货商");
	// JMath.setCellValue(row, c++, "订单号");
	// JMath.setCellValue(row, c++, "记录时间");
	// JMath.setCellValue(row, c++, "当前状态");
	// JMath.setCellValue(row, c++, "推送状态");
	// JMath.setCellValue(row, c++, "是否手动处理");
	// JMath.setCellValue(row, c++, "异常原因");
	//
	//
	// int k=0;
	// for (B2CMonitorData pe:b2cdatalist) {
	// k++;
	// row = sheet.createRow((short) rows++);
	// c = 0; // 第一列
	// String customername="";
	// List<Customer> customerlist=customerDAO.getAllCustomers();
	// for(Customer cu:customerlist){
	// if(cu.getCustomerid()==pe.getCustomerid()){
	// customername=cu.getCustomername();
	// }
	// }
	// String method="";
	// for(CwbFlowOrderTypeEnum cc:CwbFlowOrderTypeEnum.values()){
	// if(cc.getValue()==pe.getFlowordertype()){
	// method=cc.getText();
	// }
	// }
	//
	//
	// JMath.setCellValue(row, c++, "" + (k + 1));
	// JMath.setCellValue(row, c++, customername);
	// JMath.setCellValue(row, c++, pe.getCwb());
	// JMath.setCellValue(row, c++, pe.getPosttime());
	// JMath.setCellValue(row, c++, method);
	// JMath.setCellValue(row, c++,pe.getSend_b2c_flagStr()+"");
	// JMath.setCellValue(row, c++,pe.getHand_deal_flagStr()+"");
	// JMath.setCellValue(row, c++, pe.getExpt_reason());
	// }
	// row = sheet.createRow((short) rows++);
	// c = 0; // 第一列
	// JMath.setCellValue(row, c++, "合计");
	// JMath.setCellValue(row, c++, b2cdatalist.size() + "[条]");
	//
	// JMath.CreateOutPutExcel(response, workbook);
	// }

}
