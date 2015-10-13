package cn.explink.b2c.tools.b2cmonitor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jca.cci.InvalidResultSetAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.rowset.ResultSetWrappingSqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2CDataDAO;
import cn.explink.dao.GetDmpDAO;
import cn.explink.domain.Customer;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.DateDayUtil;
import cn.explink.util.ExcelUtilsOld;
import cn.explink.util.StreamingStatementCreator;

/**
 * 针对B2C对接的监控，若OMS没有存入数据或发生推送失败等等，则可以用此类来监控
 *
 * @author Administrator
 *
 */
@Service
public class B2cMonitorService {
	@Autowired
	B2CDataDAO b2cJointMonitorDAO;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	GetDmpDAO getDmpDAO;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public void exportB2cDataExptInfo(String cwb, long customerid, long flowordertypeid, String starttime, String endtime, HttpServletResponse response) {

		String[] cloumnName1 = new String[7]; // 导出的列名
		String[] cloumnName2 = new String[7];// 导出的英文列名
		cloumnName1[0] = "供货商";
		cloumnName2[0] = "customer";
		cloumnName1[1] = "订单号";
		cloumnName2[1] = "cwb";
		cloumnName1[2] = "操作时间";
		cloumnName2[2] = "posttime";
		cloumnName1[3] = "操作当前状态";
		cloumnName2[3] = "flowordertype";
		cloumnName1[4] = "推送状态";
		cloumnName2[4] = "send_b2c_flag";
		cloumnName1[5] = "超时时长";
		cloumnName2[5] = "timeout";
		cloumnName1[6] = "推送失败原因";
		cloumnName2[6] = "remark";

		final String[] cloumnName = cloumnName1;
		final String[] cloumnName3 = cloumnName2;
		String sheetName = "订单信息"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "Order_" + df.format(new Date()) + ".xlsx"; // 文件名
		try {
			final String sql = this.b2cJointMonitorDAO.selectB2cMonitorDataListSql(cwb, customerid, flowordertypeid, starttime, endtime);
			//
			logger.info("对接异常监控导出sql：{}",sql);
			ExcelUtilsOld excelUtil = new ExcelUtilsOld() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(final Sheet sheet, final CellStyle style) {
					final List<Customer> cList = B2cMonitorService.this.getDmpDAO.getAllCustomers();
					B2cMonitorService.this.jdbcTemplate.query(new StreamingStatementCreator(sql), new RowCallbackHandler() {
						private int count = 0;

						@Override
						public void processRow(ResultSet rs) throws SQLException {
							Row row = sheet.createRow(this.count + 1);
							row.setHeightInPoints(15);
							// System.out.println(ds.getCwb()+":"+System.currentTimeMillis());
							for (int i = 0; i < cloumnName.length; i++) {
								Cell cell = row.createCell((short) i);
								cell.setCellStyle(style);
								Object a = B2cMonitorService.this.setObjectA(cloumnName3, new ResultSetWrappingSqlRowSet(rs), i, cList);
								cell.setCellValue(a == null ? "" : a.toString());
							}
							this.count++;

						}
					});
				}
			};
			excelUtil.excel(response, cloumnName, sheetName, fileName);
			// System.out.println("get end:"+System.currentTimeMillis());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Object setObjectA(String[] cloumnName3, SqlRowSet r, int i, List<Customer> cList) {
		Object a = null;
		String cloumname = cloumnName3[i];
		try {
			if ("customer".equals(cloumname)) {
				long customerid = r.getLong("customerid");
				for (Customer c : cList) {
					if (c.getCustomerid() == customerid) {
						return c.getCustomername();
					}
				}
			} else if ("cwb".equals(cloumname)) {
				a = r.getObject("cwb") == null ? "" : r.getObject("cwb");
			} else if ("posttime".equals(cloumname)) {
				a = r.getObject("posttime") == null ? "" : r.getObject("posttime");
			} else if ("flowordertype".equals(cloumname)) {
				a = r.getObject("flowordertype") == null ? "" : r.getObject("flowordertype");
				for (FlowOrderTypeEnum fote : FlowOrderTypeEnum.values()) {
					if (fote.getValue() == Integer.parseInt(a.toString())) {
						return fote.getText();
					}
				}
			} else if ("posttime".equals(cloumname)) {
				a = r.getObject("posttime") == null ? "" : r.getObject("posttime");
			} else if ("send_b2c_flag".equals(cloumname)) {
				a = r.getObject("send_b2c_flag") == null ? "等待推送" : (r.getObject("send_b2c_flag").toString().equals("0") ? "等待推送" : "推送失败");
			} else if ("timeout".equals(cloumname)) {
				a = r.getObject("posttime") == null ? "" : r.getObject("posttime");
				if (DateDayUtil.getQuotMin(a.toString()) > 60) {

					return (DateDayUtil.getQuotMin(a.toString()) / 60) + "小时" + (DateDayUtil.getQuotMin(a.toString()) % 60) + "分";
				} else {
					return DateDayUtil.getQuotMin(a.toString()) + "分";
				}

			} else if ("remark".equals(cloumname)) {
				a = r.getObject("remark") == null ? "" : r.getObject("remark");
			}
		} catch (InvalidResultSetAccessException e) {
			// System.out.println(cloumname);
		}
		// System.out.println("pp:"+System.currentTimeMillis());
		return a;

	}

}
