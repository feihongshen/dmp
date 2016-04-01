/**
 *
 */
package cn.explink.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.PenalizeOutDAO;
import cn.explink.dao.PenalizeTypeDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Customer;
import cn.explink.domain.PenalizeOut;
import cn.explink.domain.PenalizeType;
import cn.explink.domain.User;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.PenalizeSateEnum;
import cn.explink.util.ExcelUtils;

/**
 * @author Administrator
 *
 */
@Service
public class PenalizeOutService {
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	PenalizeTypeDAO penalizeTypeDAO;
	@Autowired
	PenalizeOutDAO penalizeOutDAO;

	private static Logger logger = LoggerFactory.getLogger(PenalizeOutService.class);
	
	public void setExcelstyle(String[] cloumnName1, String[] cloumnName2) {
		cloumnName1[0] = "赔付单号";
		cloumnName2[0] = "PenalizeOutNO";
		cloumnName1[1] = "订单号";
		cloumnName2[1] = "Cwb";
		cloumnName1[2] = "供货商";
		cloumnName2[2] = "Customerid";
		cloumnName1[3] = "订单状态";
		cloumnName2[3] = "Flowordertype";
		cloumnName1[4] = "订单金额";
		cloumnName2[4] = "Caramount";
		cloumnName1[5] = "赔付金额";
		cloumnName2[5] = "PenalizeOutfee";
		cloumnName1[6] = "赔付大类";
		cloumnName2[6] = "PenalizeOutbig";
		cloumnName1[7] = "赔付小类";
		cloumnName2[7] = "PenalizeOutsmall";
		cloumnName1[8] = "创建人";
		cloumnName2[8] = "Createruser";
		cloumnName1[9] = "创建日期";
		cloumnName2[9] = "Createrdate";
		cloumnName1[10] = "撤销人";
		cloumnName2[10] = "Canceluser";
		cloumnName1[11] = "撤销时间";
		cloumnName2[11] = "Canceldate";
		cloumnName1[12] = "赔付单状态";
		cloumnName2[12] = "PenalizeOutstate";
	}
	/**
	 * @param response
	 * @param cwbs
	 * @param customerid
	 * @param penalizeOutbig
	 * @param penalizeOutsmall
	 * @param penalizeState
	 * @param flowordertype
	 * @param starttime
	 * @param endtime
	 */
	public  void exportExcels(HttpServletResponse response, String cwbs, long customerid, int penalizeOutbig, int penalizeOutsmall, int penalizeState, long flowordertype, String starttime,
			String endtime) {
		String[] cloumnName1 = new String[13]; // 导出的列名
		String[] cloumnName2 = new String[13]; // 导出的英文列名

		this.setExcelstyle(cloumnName1, cloumnName2);
		final String[] cloumnName3 = cloumnName1;
		final String[] cloumnName4 = cloumnName2;
		String sheetName = "对外赔付信息"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "PensizeOut_" + df.format(new Date()) + ".xlsx"; // 文件名
		try {
			// 查询出数据
			final List<PenalizeOut> penalizeOutlList = this.penalizeOutDAO.getPenalizeOutByListExport(cwbs, flowordertype, customerid, penalizeOutbig, penalizeOutsmall, penalizeState, starttime, endtime);
			final BigDecimal penalizeOutfeetotal=this.penalizeOutDAO.getPenalizeOutByPenalizeOutfeeSum(cwbs, flowordertype, customerid, penalizeOutbig, penalizeOutsmall, penalizeState, starttime, endtime);
			final List<Customer> customers = this.customerDAO.getAllCustomers();
			final List<User> users = this.userDAO.getAllUser();
			final List<PenalizeType> penalizeTypeList = this.penalizeTypeDAO.getAllPenalizeType();

			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(final Sheet sheet, final CellStyle style) {
					style.setAlignment(CellStyle.ALIGN_CENTER);
					for (int k = 0; k < penalizeOutlList.size(); k++) {
						Row row = sheet.createRow(k + 1);
						row.setHeightInPoints(15);
						for (int i = 0; i < cloumnName4.length; i++) {
							sheet.setColumnWidth(i, 15 * 256);
							Cell cell = row.createCell((short) i);
							cell.setCellStyle(style);
							Object a = null;
							if((i==9)||(i==11)){
								sheet.setColumnWidth(i, 25 * 256);
								style.setDataFormat(HSSFDataFormat.getBuiltinFormat("yyyy/MM/dd hh:mm:ss"));
							}
							// 给导出excel赋值
							a = this.setExcelObject(cloumnName4, a, i, penalizeOutlList.get(k));

							cell.setCellValue(a == null ? "" : a.toString());
						}
					}
					Row row=sheet.createRow(penalizeOutlList.size()+1);


					Cell cell0=row.createCell(1);
					cell0.setCellStyle(style);
					cell0.setCellValue("赔付总额");
					Cell cell4=row.createCell(5);
					cell4.setCellStyle(style);
					cell4.setCellValue(penalizeOutfeetotal+"");

				}
				private Object setExcelObject(String[] cloumnName, Object a, int i, PenalizeOut out) {
					try {
						 if (cloumnName[i].equals("Createruser")) {

							for (User u : users) {
								if (u.getUserid() == out.getCreateruser()) {
									a = u.getRealname();
								}
							}
						}
						 else if (cloumnName[i].equals("Canceluser")) {

							 for (User u : users) {
								 if (u.getUserid() == out.getCanceluser()) {
									 a = u.getRealname();
								 }
							 }
						 }
						 else if (cloumnName[i].equals("Customerid")) {

							for (Customer cu : customers) {
								if (cu.getCustomerid() == out.getCustomerid()) {
									a = cu.getCustomername();
								}
							}
						}  else if (cloumnName[i].equals("PenalizeOutstate")) {

							a=PenalizeSateEnum.getPenalizebigEnum(out.getPenalizeOutstate()).getText();

						}
						else if (cloumnName[i].equals("PenalizeOutbig")) {
							for (PenalizeType pty : penalizeTypeList) {
								if (pty.getId() == out.getPenalizeOutbig()) {
									a = pty.getText();
								}
							}
						}else if (cloumnName[i].equals("PenalizeOutsmall")) {
							for (PenalizeType pty : penalizeTypeList) {
								if (pty.getId() == out.getPenalizeOutsmall()) {
									a = pty.getText();
								}
							}
						}
						else if (cloumnName[i].equals("Flowordertype")) {
							for (FlowOrderTypeEnum flow : FlowOrderTypeEnum.values()) {
								if (flow.getValue()== out.getFlowordertype()) {
									a = flow.getText();
								}
							}
						}
						 else {
							a = out.getClass().getMethod("get" + cloumnName[i]).invoke(out);

						}
					} catch (Exception e) {
						logger.error("", e);
					}
					return a;
				}
			};
			excelUtil.excel(response, cloumnName3, sheetName, fileName);

		} catch (Exception e) {
			logger.error("", e);
		}
	}
}
