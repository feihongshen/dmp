package cn.explink.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Service;

import cn.explink.dao.BranchPayamountDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.GetDmpDAO;
import cn.explink.dao.GotoClassDAO;
import cn.explink.domain.BranchPayamount;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.JMath;

@Service
public class BranchPayamountService {
	@Autowired
	BranchPayamountDAO branchPayamountDao;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	GetDmpDAO getDmpDAO;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	DeliveryStateDAO deliverStateDAO;
	@Autowired
	GotoClassDAO gotoClassDAO;
	@Produce(uri = "jms:queue:sendPayUpBack")
	ProducerTemplate sendPayUpBackMsg;

	private Logger logger = LoggerFactory.getLogger(BranchPayamountService.class);
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public long updatePay(String conStr, String mStr, String dmpid) {

		String realname = getDmpDAO.getNowRealname(dmpid);
		int userid = getDmpDAO.getNowUserId(dmpid);
		try {
			if (conStr != null && conStr.length() > 0 && conStr.indexOf(":") > -1 && conStr.indexOf("_") > -1) {
				List<BranchPayamount> list = branchPayamountDao.getBranchPayamount(Long.parseLong(conStr.split(":")[0].split("_")[0]), conStr.split(":")[0].split("_")[1]);
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						if (i == 0) {
							BranchPayamount b = list.get(i);
							b.setBranchpayid(b.getBranchpayid());
							// b.setReceivedfeecash(new
							// BigDecimal(conStr.split(":")[1].split(",")[0]));
							// b.setOtherbranchfee_checked(new
							// BigDecimal(conStr.split(":")[1].split(",")[1]));
							// b.setReceivedfeepos_checked(new
							// BigDecimal(conStr.split(":")[1].split(",")[2])) ;
							// b.setReceivedfeecheque_checked(new
							// BigDecimal(conStr.split(":")[1].split(",")[3]));
							b.setUpstate(1);
							b.setCheckdate(sdf.format(new Date()));
							b.setUserid(userid);
							b.setUsername(realname);
							if (mStr != null && mStr.length() > 0 && mStr.indexOf("P:P") > -1) {
								b.setCheckremark(mStr.split("P:P").length > 1 ? mStr.split("P:P")[1] : "");
							}
							branchPayamountDao.save(b);
							logger.info("发送反馈消息给站点---payUpBack");
							// sendPayUpBackMsg.sendBodyAndHeader(null,
							// "payUpBack", b.getObjctToJSON().toString());

						} else {
							BranchPayamount b = list.get(i);
							b.setBranchpayid(b.getBranchpayid());
							// b.setReceivedfeecash(new BigDecimal("0.00"));
							// b.setOtherbranchfee_checked(new
							// BigDecimal("0.00"));
							// b.setReceivedfeepos_checked(new
							// BigDecimal("0.00")) ;
							// b.setReceivedfeecheque_checked(new
							// BigDecimal("0.00"));
							b.setUpstate(1);
							b.setCheckdate(sdf.format(new Date()));
							b.setUserid(userid);
							b.setUsername(realname);
							if (mStr != null && mStr.length() > 0 && mStr.indexOf("P:P") > -1) {
								b.setCheckremark(mStr.split("P:P").length > 1 ? mStr.split("P:P")[1] : "");
							}
							branchPayamountDao.save(b);
							logger.info("发送反馈消息给站点---payUpBack");
							// sendPayUpBackMsg.sendBodyAndHeader(null,
							// "payUpBack", b.getObjctToJSON().toString());

						}
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return 1;
	}

	public List<CwbOrder> show(String conStr) {
		List<CwbOrder> cwblist = null;
		String cwb = "";
		List<BranchPayamount> list = branchPayamountDao.getBranchPayamount(Long.parseLong(conStr.split("_")[0]), conStr.split("_")[1]);
		if (list != null && list.size() > 0) {
			for (BranchPayamount branchPayamount : list) {
				cwb += branchPayamount.getOrderStr() + ",";
			}
			cwb = cwb.length() > 0 ? cwb.substring(0, cwb.length() - 1) : "''";
		}
		cwblist = cwbDAO.getCwbOrderByCwb(cwb);
		return cwblist;
	}

	public String getGoClassIds(String conStr) {
		String goClassIds = "";
		List<BranchPayamount> list = branchPayamountDao.getBranchPayamount(Long.parseLong(conStr.split("_")[0]), conStr.split("_")[1]);
		if (list != null && list.size() > 0) {
			for (BranchPayamount branchPayamount : list) {
				goClassIds += branchPayamount.getOrderStr() + ",";
			}
			goClassIds = goClassIds.length() > 0 ? goClassIds.substring(0, goClassIds.length() - 1) : "0";
		}
		return goClassIds;
	}

	public String getBackGoClassIds(String conStr) {
		String goClassIds = "";
		List<BranchPayamount> list = branchPayamountDao.getBranchPayamountBack(Long.parseLong(conStr.split("_")[0]), conStr.split("_")[1]);
		if (list != null && list.size() > 0) {
			for (BranchPayamount branchPayamount : list) {
				goClassIds += branchPayamount.getOrderStr() + ",";
			}
			goClassIds = goClassIds.length() > 0 ? goClassIds.substring(0, goClassIds.length() - 1) : "0";
		}
		return goClassIds;
	}

	public String getDeliveIds(String conStr) {
		String DeliveIds = gotoClassDAO.getGobackidStr(conStr);
		return DeliveIds;
	}

	public List<DeliveryState> showDeliveryState(String gobackidStr, long page) {
		List<DeliveryState> deliveryStatelist = deliverStateDAO.getDeliveryStateBygobackid(gobackidStr, page);
		return deliveryStatelist;
	}

	/**
	 * 查询各款项
	 * 
	 * @param gobackidStr
	 * @param type
	 * @param page
	 * @return
	 */
	public List<DeliveryState> showDeliveryStateType(String gobackidStr, long type, long page) {
		List<DeliveryState> deliveryStatelist = deliverStateDAO.getDeliveryStateBygobackidBytype(gobackidStr, type, page);
		return deliveryStatelist;
	}

	public long getCount(String conStr) {
		String cwb = "";
		List<BranchPayamount> list = branchPayamountDao.getBranchPayamount(Long.parseLong(conStr.split("_")[0]), conStr.split("_")[1]);
		if (list != null && list.size() > 0) {
			for (BranchPayamount branchPayamount : list) {
				cwb += branchPayamount.getOrderStr() == null ? "" : (branchPayamount.getOrderStr() + ",");
			}
			cwb = cwb.length() > 0 ? cwb.substring(0, cwb.length() - 1) : "''";
		}
		return cwbDAO.getCwbCountByCwb(cwb);
	}

	public long getDeliveryCountStr(String conStr) {
		return deliverStateDAO.getDeliveryCountByconStr(conStr);
	}

	/**
	 * 查询各款项数量
	 * 
	 * @param conStr
	 * @param type
	 * @return
	 */
	public long getDeliveryCountStrType(String conStr, long type) {
		return deliverStateDAO.getDeliveryCountByconStrType(conStr, type);
	}

	public List<CwbOrder> searchDetailByFlowType(long type, long customerid, long page, String starttime, String endtime) {
		List<CwbOrder> list = cwbDAO.searchCwbDetailByFlowOrdertype(type, starttime, endtime, customerid, page);
		return list;
	}

	public List<CwbOrder> searchDetailByFlowTypeback(String type, long customerid, long page, String starttime, String endtime, String startaudittime, String endaudittime) {
		List<CwbOrder> list = cwbDAO.searchCwbDetailByFlowOrdertypeBack(type, starttime, endtime, customerid, page, startaudittime, endaudittime);
		return list;
	}

	/**
	 * 导出站点交款审核数据 结算管理 > 结算与审核 > 货款结算
	 */
	public void getCwbDetailByFlowOrder_payment(HttpServletResponse response, List<DeliveryState> datalist, int showphoneflag) {

		String excelbranch = "站点交款审核明细";
		CreateExcelHeader(response, excelbranch);
		// 创建excel 新的工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("站点交款审核"); // Sheet1没个新建的Excel都是这个默认值
		sheet.getPrintSetup().setLandscape(true); // true：横向 false：纵向
		int rows = 0; // 第一行
		int c = 0; // 第一列

		HSSFRow row = sheet.createRow((short) rows++);
		JMath.setCellValue(row, c++, "订单号");
		JMath.setCellValue(row, c++, "小件员");
		JMath.setCellValue(row, c++, "订单应收金额");
		JMath.setCellValue(row, c++, "退还金额");
		JMath.setCellValue(row, c++, "应处理金额");
		JMath.setCellValue(row, c++, "现金实收");
		JMath.setCellValue(row, c++, "pos实收");
		JMath.setCellValue(row, c++, "pos备注");
		JMath.setCellValue(row, c++, "pos反馈时间");
		JMath.setCellValue(row, c++, "其他金额");
		JMath.setCellValue(row, c++, "支票实收");
		JMath.setCellValue(row, c++, "支票号备注");
		JMath.setCellValue(row, c++, "商品名称");
		JMath.setCellValue(row, c++, "货物金额");
		JMath.setCellValue(row, c++, "收件人姓名");
		JMath.setCellValue(row, c++, "收件人手机 ");
		JMath.setCellValue(row, c++, "收件人地址");
		JMath.setCellValue(row, c++, "发货时间");
		for (DeliveryState co : datalist) {
			row = sheet.createRow((short) rows++);
			c = 0; // 第一列
			JMath.setCellValue(row, c++, co.getCwb());
			JMath.setCellValue(row, c++, co.getDeliveryName());
			JMath.setCellValue(row, c++, co.getReceivedfee() + "");
			JMath.setCellValue(row, c++, co.getReturnedfee() + "");
			JMath.setCellValue(row, c++, co.getBusinessfee() + "");
			JMath.setCellValue(row, c++, co.getCash() + "");
			JMath.setCellValue(row, c++, co.getPos() + "");
			JMath.setCellValue(row, c++, co.getPosremark());
			JMath.setCellValue(row, c++, co.getMobilepodtime() + "");
			JMath.setCellValue(row, c++, co.getOtherfee() + "");
			JMath.setCellValue(row, c++, co.getCheckfee() + "");
			JMath.setCellValue(row, c++, co.getCheckremark());
			JMath.setCellValue(row, c++, co.getSendcarname());
			JMath.setCellValue(row, c++, co.getCaramount() + "");
			JMath.setCellValue(row, c++, showphoneflag == 1 ? co.getConsigneename() : "[不可见]");
			JMath.setCellValue(row, c++, co.getConsigneephone());
			JMath.setCellValue(row, c++, co.getConsigneeaddress());
			JMath.setCellValue(row, c++, co.getEmaildate());
		}

		CreateOutPutExcel(response, workbook);

	}

	/**
	 * 导出相对应的b2c 各个环节的数据 结算管理 > 结算与审核 > 货款结算
	 */
	public void getCwbDetailByFlowOrder_SaveAsExcel(HttpServletResponse response, long type, String starttime, String endtime, long customerid) {

		List<CwbOrder> datalist = cwbDAO.searchCwbDetailByFlowOrdertype_excel(type, starttime, endtime, customerid);

		String excelbranch = "货款结算详情数据列表";
		CreateExcelHeader(response, excelbranch);
		// 创建excel 新的工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("货款结算详情"); // Sheet1没个新建的Excel都是这个默认值
		sheet.getPrintSetup().setLandscape(true); // true：横向 false：纵向
		int rows = 0; // 第一行
		int c = 0; // 第一列

		HSSFRow row = sheet.createRow((short) rows++);
		JMath.setCellValue(row, c++, "序号");
		JMath.setCellValue(row, c++, "订单号");
		JMath.setCellValue(row, c++, "收件人");
		JMath.setCellValue(row, c++, "收件地址");
		JMath.setCellValue(row, c++, "电话");
		JMath.setCellValue(row, c++, "手机");
		JMath.setCellValue(row, c++, "邮编");
		JMath.setCellValue(row, c++, "送货时间要求");
		JMath.setCellValue(row, c++, "代收金额");
		JMath.setCellValue(row, c++, "应退金额");
		JMath.setCellValue(row, c++, "当前状态");
		int k = 0;
		for (CwbOrder co : datalist) {
			k++;
			row = sheet.createRow((short) rows++);
			c = 0; // 第一列
			String flowtypename = "";
			long flowtypeid = co.getFlowordertype();
			for (FlowOrderTypeEnum f : FlowOrderTypeEnum.values()) {
				if (f.getValue() == flowtypeid) {
					flowtypename = f.getText();
					break;
				}
			}
			JMath.setCellValue(row, c++, "" + (k + 1));
			JMath.setCellValue(row, c++, co.getCwb());
			JMath.setCellValue(row, c++, co.getConsigneename());
			JMath.setCellValue(row, c++, co.getConsigneeaddress());
			JMath.setCellValue(row, c++, co.getConsigneephone());
			JMath.setCellValue(row, c++, co.getConsigneemobile());
			JMath.setCellValue(row, c++, co.getConsigneepostcode());
			JMath.setCellValue(row, c++, co.getCustomercommand());
			JMath.setCellValue(row, c++, (co.getOrderResultType() == DeliveryStateEnum.QuanBuTuiHuo.getValue() ? co.getReceivablefee() : 0) + "");
			JMath.setCellValue(row, c++, flowtypename);
		}
		row = sheet.createRow((short) rows++);
		c = 0; // 第一列
		JMath.setCellValue(row, c++, "合计");
		JMath.setCellValue(row, c++, datalist.size() + "[条]");

		CreateOutPutExcel(response, workbook);

	}

	/**
	 * 导出相对应的b2c 各个环节的数据 结算管理 > 结算与审核 > 货款结算
	 */
	public void getCwbDetailByFlowOrder_back_SaveAsExcel(HttpServletResponse response, String type, String starttime, String endtime, long customerid, String startaudittime, String endaudittime) {

		List<CwbOrder> datalist = cwbDAO.searchCwbDetailByFlowOrdertypeback_excel(type, starttime, endtime, customerid, startaudittime, endaudittime);

		String excelbranch = "退货款结算详情数据列表";
		CreateExcelHeader(response, excelbranch);
		// 创建excel 新的工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("退货款结算详情"); // Sheet1没个新建的Excel都是这个默认值
		sheet.getPrintSetup().setLandscape(true); // true：横向 false：纵向
		int rows = 0; // 第一行
		int c = 0; // 第一列

		HSSFRow row = sheet.createRow((short) rows++);
		JMath.setCellValue(row, c++, "序号");
		JMath.setCellValue(row, c++, "订单号");
		JMath.setCellValue(row, c++, "收件人");
		JMath.setCellValue(row, c++, "收件地址");
		JMath.setCellValue(row, c++, "电话");
		JMath.setCellValue(row, c++, "手机");
		JMath.setCellValue(row, c++, "邮编");
		JMath.setCellValue(row, c++, "送货时间要求");
		JMath.setCellValue(row, c++, "应退金额");
		JMath.setCellValue(row, c++, "当前状态");
		int k = 0;
		for (CwbOrder co : datalist) {
			k++;
			row = sheet.createRow((short) rows++);
			c = 0; // 第一列
			String flowtypename = "";
			long flowtypeid = co.getFlowordertype();
			for (FlowOrderTypeEnum f : FlowOrderTypeEnum.values()) {
				if (f.getValue() == flowtypeid) {
					flowtypename = f.getText();
					break;
				}
			}
			JMath.setCellValue(row, c++, "" + (k + 1));
			JMath.setCellValue(row, c++, co.getCwb());
			JMath.setCellValue(row, c++, co.getConsigneename());
			JMath.setCellValue(row, c++, co.getConsigneeaddress());
			JMath.setCellValue(row, c++, co.getConsigneephone());
			JMath.setCellValue(row, c++, co.getConsigneemobile());
			JMath.setCellValue(row, c++, co.getConsigneepostcode());
			JMath.setCellValue(row, c++, co.getCustomercommand());
			JMath.setCellValue(row, c++, co.getReceivablefee() + "");
			JMath.setCellValue(row, c++, flowtypename);
		}
		row = sheet.createRow((short) rows++);
		c = 0; // 第一列
		JMath.setCellValue(row, c++, "合计");
		JMath.setCellValue(row, c++, datalist.size() + "[条]");

		CreateOutPutExcel(response, workbook);

	}

	private void CreateExcelHeader(HttpServletResponse response, String excelbranch) {
		try {
			excelbranch = new String(excelbranch.getBytes("GBK"), "iso8859-1");
		} catch (UnsupportedEncodingException e1) {
			logger.error("export Excel exception!");
			e1.printStackTrace();
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		GregorianCalendar thisday = new GregorianCalendar(); // 现在的时间
		String filename = format.format(thisday.getTime()).toString() + excelbranch; // 文件名
		response.setContentType("application/ms-excel");
		response.setHeader("Content-Disposition", "attachment;filename=" + filename + ".xls");
	}

	private void CreateOutPutExcel(HttpServletResponse response, HSSFWorkbook workbook) {
		ServletOutputStream fOut;
		try {
			fOut = response.getOutputStream();// 把相应的Excel 工作簿存盘
			workbook.write(fOut);
			fOut.flush();
			fOut.close();// 操作结束，关闭文件
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("文件生成...");
	}

}