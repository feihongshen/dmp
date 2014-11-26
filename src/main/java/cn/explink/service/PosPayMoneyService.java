package cn.explink.service;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Service;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.PosPayMoneyDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.domain.User;
import cn.explink.pos.tools.PosEnum;
import cn.explink.util.ExcelUtils;
import cn.explink.util.StreamingStatementCreator;

/**
 * POS款项查询
 * 
 * @author Administrator
 *
 */
@Service
public class PosPayMoneyService {

	@Autowired
	PosPayMoneyDAO posPayMoneyDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	ExportService exportService;
	@Autowired
	JdbcTemplate jdbcTemplate;

	/**
	 * 导出查询POS款项查询记录
	 */
	public void PosPayRecord_selectSaveAsExcel(List<User> userList, HttpServletRequest request, HttpServletResponse response) {

		String[] cloumnName1 = {};
		String[] cloumnName2 = {};
		String[] cloumnName3 = {};
		cloumnName1 = new String[17];
		cloumnName2 = new String[17];
		cloumnName3 = new String[17];
		exportService.SetPosPayFields(cloumnName1, cloumnName2, cloumnName3);
		final String[] cloumnName4 = cloumnName1;
		final String[] cloumnName5 = cloumnName2;
		final String[] cloumnName6 = cloumnName3;

		String sheetName = "订单信息"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "Order_" + df.format(new Date()) + ".xlsx"; // 文件名

		try {
			// 查询出数据
			String cwbValue = request.getSession().getAttribute("cwb").toString();
			String pos_codeValue = request.getSession().getAttribute("pos_code").toString();
			String starttime = request.getSession().getAttribute("starttime").toString();
			String endtime = request.getSession().getAttribute("endtime").toString();
			String deliveryids = request.getSession().getAttribute("deliveryids").toString();
			long isSuccessFlag = Long.parseLong(request.getSession().getAttribute("isSuccessFlag").toString());

			final String sql = posPayMoneyDAO.getPosPayExpoSql(cwbValue, pos_codeValue, starttime, endtime, deliveryids, isSuccessFlag);
			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(final Sheet sheet, final CellStyle style) {
					final List<User> userList = userDAO.getAllUser();
					final List<Branch> branchList = branchDAO.getAllBranches();
					final List<Customer> customerList = customerDAO.getAllCustomers();
					jdbcTemplate.query(new StreamingStatementCreator(sql), new RowCallbackHandler() {
						private int count = 0;

						@Override
						public void processRow(ResultSet rs) throws SQLException {
							Row row = sheet.createRow(count + 1);
							row.setHeightInPoints((float) 15);

							// System.out.println(ds.getCwb()+":"+System.currentTimeMillis());
							for (int i = 0; i < cloumnName4.length; i++) {
								Cell cell = row.createCell((short) i);
								cell.setCellStyle(style);
								Object a = null;
								try {
									try {
										if ("pos_code".equals(cloumnName5[i])) {
											for (PosEnum poe : PosEnum.values()) {
												if (poe.getMethod().equals(rs.getString("pos_code"))) {
													a = poe.getText();
													break;
												}
											}
										} else if ("tradeDeliverId".equals(cloumnName5[i])) {
											for (User user : userList) {
												if (user.getUserid() == rs.getLong("tradeDeliverId")) {
													a = user.getRealname();
													break;
												}
											}

										} else if ("customerid".equals(cloumnName5[i])) {
											for (Customer c : customerList) {
												if (c.getCustomerid() == rs.getLong("customerid")) {
													a = c.getCustomername();
													break;
												}
											}
										} else if ("signtypeid".equals(cloumnName5[i])) {
											a = rs.getLong("isSuccessFlag") == 2 ? "" : rs.getLong("signtypeid") == 2 ? "他人签收" : "本人签收";
										} else if ("isSuccessFlag".equals(cloumnName5[i])) {
											a = rs.getLong("isSuccessFlag") == 2 ? "已撤销" : "交易成功";
										} else if ("branchid".equals(cloumnName5[i])) {
											a = getBranchName(rs.getLong("tradeDeliverId"), branchList, userList);
										} else if ("paywayid".equals(cloumnName5[i])) {
											if ("1".equals(rs.getString("paywayid"))) {
												a = "现金";
											} else if ("2".equals(rs.getString("paywayid"))) {
												a = "POS";
											} else if ("3".equals(rs.getString("paywayid"))) {
												a = "支票";
											} else if ("4".equals(rs.getString("paywayid"))) {
												a = "其他";
											} else {
												a = "现金";
											}
										} else if ("newpaywayid".equals(cloumnName5[i])) {
											if ("1".equals(rs.getString("newpaywayid"))) {
												a = "现金";
											} else if ("2".equals(rs.getString("newpaywayid"))) {
												a = "POS";
											} else if ("3".equals(rs.getString("newpaywayid"))) {
												a = "支票";
											} else if ("4".equals(rs.getString("newpaywayid"))) {
												a = "其他";
											} else {
												a = "现金";
											}
										}

										else {
											a = rs.getObject(cloumnName5[i]);
										}
										if (cloumnName6[i].equals("double")) {
											cell.setCellValue(a == null ? BigDecimal.ZERO.doubleValue() : a.equals("") ? BigDecimal.ZERO.doubleValue() : Double.parseDouble(a.toString()));
										} else {
											cell.setCellValue(a == null ? "" : a.toString());
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
								} catch (IllegalArgumentException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							count++;

						}
					});

				}
			};
			excelUtil.excel(response, cloumnName4, sheetName, fileName);
			// System.out.println("get end:"+System.currentTimeMillis());

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String getBranchName(long urserId, List<Branch> branchList, List<User> userList) {
		String branchname = "";
		long branchid = 0;

		if (userList != null && userList.size() > 0) {
			for (User user : userList) {
				if (urserId == user.getUserid()) {
					branchid = user.getBranchid();
					break;
				}
			}

		}
		if (branchList != null && branchList.size() > 0) {
			for (Branch branch : branchList) {
				if (branchid == branch.getBranchid()) {
					branchname = branch.getBranchname();
					break;
				}
			}

		}
		return branchname;
	}

	public String getDeliveryids(List<User> userList) {
		String deliveryids = "";
		if (userList.size() > 0) {
			StringBuffer str = new StringBuffer();
			for (User user : userList) {
				str.append(user.getUserid() + ",");
			}
			deliveryids = str.substring(0, str.length() - 1);
		} else {
			deliveryids = "''";
		}
		return deliveryids;
	}

}
