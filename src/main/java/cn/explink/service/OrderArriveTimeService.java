package cn.explink.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.MqExceptionDAO;
import cn.explink.dao.OrderArriveTimeDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.CustomWareHouse;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.MqExceptionBuilder;
import cn.explink.domain.OrderArriveTime;
import cn.explink.domain.User;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.util.ExcelUtils;
import cn.explink.util.StreamingStatementCreator;

@Service
public class OrderArriveTimeService {
	private Logger logger = LoggerFactory.getLogger(OrderArriveTimeService.class);
	@Autowired
	DataStatisticsService dataStatisticsService;
	@Autowired
	OrderArriveTimeDAO orderArriveTimeDAO;
	@Autowired
	CustomWareHouseDAO customWareHouseDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	ExportService exportService;
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Produce(uri = "jms:topic:daochetime")
	ProducerTemplate daocheProducerTemplate;
	
	@Autowired
	private MqExceptionDAO mqExceptionDAO;

	public List<OrderArriveTime> getOrderArriveTimeList(List<OrderArriveTime> oatList, List<Customer> customerList, List<Branch> branchList, List<CustomWareHouse> customerWareHouseList) {
		List<OrderArriveTime> list = new ArrayList<OrderArriveTime>();
		if (oatList != null && !oatList.isEmpty()) {
			for (OrderArriveTime o : oatList) {
				o.setCustomername(dataStatisticsService.getQueryCustomerName(customerList, o.getCustomerid()));// 供货商的名称
				o.setOutbranchname(dataStatisticsService.getQueryCustomWareHouse(customerWareHouseList, o.getOutbranchid()));// 发货仓库
				o.setInbranchname(dataStatisticsService.getQueryBranchName(branchList, o.getInbranchid()));// 入库库房
				// o.setUsername(dataStatisticsService.getQueryUserName(userList,o.getUserid()));
				o.setCwbordertypename(CwbOrderTypeIdEnum.getByValue((int) o.getCwbordertypeid()).getText());
				list.add(o);
			}
		}
		return list;
	}

	/**
	 * 提交到车时间
	 * 
	 * @param oatList
	 * @param arrivetime
	 */
	@Transactional
	public void save(List<OrderArriveTime> oatList, String arrivetime) {
		for (OrderArriveTime list : oatList) {
			// 更新到车时间
			orderArriveTimeDAO.updateOrderArriveTimeById(arrivetime, list.getId());
			JSONObject json = new JSONObject();
			try {
				json.put("cwb", list.getCwb());
				json.put("time", arrivetime);
				daocheProducerTemplate.sendBodyAndHeader(null, "daocheCwbAndTime", json.toString());
			} catch (Exception ee) {
				logger.error("send flow message error", ee);
				//写MQ异常表
				this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode("save")
						.buildExceptionInfo(ee.toString()).buildTopic(this.daocheProducerTemplate.getDefaultEndpoint().getEndpointUri())
						.buildMessageHeader("daocheCwbAndTime", json.toString()).getMqException());
			}
		}
	}

	public OrderArriveTime loadFormForOrderArriveTime(CwbOrder co, long inbranchid, String outtime, long userid) {
		OrderArriveTime orderArriveTime = new OrderArriveTime();
		orderArriveTime.setCwb(co.getCwb());
		orderArriveTime.setCustomerid(co.getCustomerid());
		orderArriveTime.setSendcarnum(co.getSendcarnum());
		orderArriveTime.setScannum(1);
		orderArriveTime.setCwbordertypeid(co.getCwbordertypeid());
		orderArriveTime.setUserid(userid);
		orderArriveTime.setOutbranchid(Long.parseLong(co.getCustomerwarehouseid()));// 发货仓库
		orderArriveTime.setInbranchid(inbranchid);
		orderArriveTime.setUserid(userid);
		orderArriveTime.setOuttime("".equals(outtime) ? null : outtime);// 发货时间
		orderArriveTime.setIntime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));// 入库时间
		return orderArriveTime;
	}

	/**
	 * 导出Excel
	 * 
	 * @param request
	 * @param response
	 * @param user
	 * @param cwb
	 * @param flag
	 * @param starttime
	 * @param endtime
	 */
	public void export(HttpServletRequest request, HttpServletResponse response, User user, String cwb, long flag, String starttime, String endtime) {
		String[] cloumnName1 = {};
		String[] cloumnName2 = {};
		String[] cloumnName3 = {};
		cloumnName1 = new String[10];
		cloumnName2 = new String[10];
		cloumnName3 = new String[10];
		exportService.setOrderArriveTimeFields(cloumnName1, cloumnName2, cloumnName3);
		final String[] cloumnName4 = cloumnName1;
		final String[] cloumnName5 = cloumnName2;
		final String[] cloumnName6 = cloumnName3;
		String sheetName = "到车时间"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "Order_" + df.format(new Date()) + ".xlsx"; // 文件名
		try {
			// 查询出数据
			final String sql = orderArriveTimeDAO.getOrderArriveTimeListSql(user.getBranchid(), cwb, starttime, endtime, flag);
			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(final Sheet sheet, final CellStyle style) {
					final List<Customer> customerList = customerDAO.getAllCustomers();
					final List<Branch> branchList = branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue());
					final List<CustomWareHouse> customerWareHouseList = customWareHouseDAO.getAllCustomWareHouse();

					jdbcTemplate.query(new StreamingStatementCreator(sql), new RowCallbackHandler() {
						private int count = 0;

						@Override
						public void processRow(ResultSet rs) throws SQLException {
							Row row = sheet.createRow(count + 1);
							row.setHeightInPoints((float) 15);
							for (int i = 0; i < cloumnName4.length; i++) {
								Cell cell = row.createCell((short) i);
								cell.setCellStyle(style);
								Object a = null;
								try {
									try {
										if ("customername".equals(cloumnName5[i])) {
											for (Customer c : customerList) {
												if (c.getCustomerid() == rs.getLong("customerid")) {
													a = c.getCustomername();
													break;
												}
											}
										} else if ("cwbordertypename".equals(cloumnName5[i])) {
											if ("1".equals(rs.getString("cwbordertypeid"))) {
												a = "配送";
											} else if ("2".equals(rs.getString("cwbordertypeid"))) {
												a = "上门退";
											} else if ("3".equals(rs.getString("cwbordertypeid"))) {
												a = "上门换";
											} else {
												a = "未确定";
											}
										} else if ("outbranchname".equals(cloumnName5[i])) {
											for (CustomWareHouse customWareHouse : customerWareHouseList) {
												if (customWareHouse.getWarehouseid() == rs.getLong("outbranchid")) {
													a = customWareHouse.getCustomerwarehouse();
													break;
												}
											}
										} else if ("inbranchname".equals(cloumnName5[i])) {
											for (Branch branch : branchList) {
												if (branch.getBranchid() == rs.getLong("inbranchid")) {
													a = branch.getBranchname();
													break;
												}
											}
										} else {
											a = rs.getObject(cloumnName5[i]);
										}
										// if(cloumnName6[i].equals("double")){
										// cell.setCellValue(a == null ?
										// BigDecimal.ZERO.doubleValue() :
										// a.equals("")?BigDecimal.ZERO.doubleValue():Double.parseDouble(a.toString()));
										// }else{
										cell.setCellValue(a == null ? "" : a.toString());
										// }
									} catch (Exception e) {
										logger.error("", e);
									}
								} catch (IllegalArgumentException e) {
									logger.error("", e);
								}
							}
							count++;
						}
					});
				}
			};
			excelUtil.excel(response, cloumnName4, sheetName, fileName);
		} catch (Exception e) {
			logger.error("", e);
		}

	}
}
