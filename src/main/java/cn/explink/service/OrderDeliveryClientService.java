package cn.explink.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import cn.explink.controller.CwbOrderView;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.OrderDeliveryClientDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.OrderDeliveryClient;
import cn.explink.domain.User;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.ExceptionCwbErrorTypeEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.exception.CwbException;
import cn.explink.util.ExcelUtils;
import cn.explink.util.StreamingStatementCreator;

@Service
public class OrderDeliveryClientService {
	private Logger logger = LoggerFactory.getLogger(OrderDeliveryClientService.class);
	@Autowired
	OrderDeliveryClientDAO orderDeliveryClientDAO;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	DataStatisticsService dataStatisticsService;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	ExportService exportService;
	@Autowired
	JdbcTemplate jdbcTemplate;

	/**
	 * 已领货列表
	 * 
	 * @param user
	 * @return
	 */
	public List<CwbOrderView> getYiLingHuo(User user) {
		List<CwbOrderView> list = new ArrayList<CwbOrderView>();
		// 查询当前站点所有 已领货&&未反馈 的订单
		List<DeliveryState> deliverystateList = deliveryStateDAO.getDeliveryByDeliver(user.getBranchid(), DeliveryStateEnum.WeiFanKui.getValue(), 1);
		if (deliverystateList != null && !deliverystateList.isEmpty()) {
			// List<String> cwbs=new ArrayList<String>();
			StringBuffer sb = new StringBuffer();
			for (DeliveryState ds : deliverystateList) {
				sb.append("'").append(ds.getCwb()).append("',");
			}
			String cwbs = sb.length() == 0 ? "" : sb.substring(0, sb.length() - 1).toString();
			// 根据订单号查询订单信息
			// List<CwbOrder>
			// cwbOrderList=cwbDAO.getZhanDianYiChuZhanbyBranchidList(cwbs);
			List<CwbOrder> cwbOrderList = cwbDAO.getCwbOrderByDelivery(cwbs);
			List<Customer> customerList = customerDAO.getAllCustomersNew();
			List<User> userList = userDAO.getAllUserByuserDeleteFlag();
			list = this.getCwbOrderViewList(cwbOrderList, userList, customerList, null);
		}
		return list;
	}

	/**
	 * 已委派、已撤销列表
	 * 
	 * @param user
	 * @param 1：已委派 0：已撤销
	 * @return
	 */
	public List<CwbOrderView> getOrderDeliveryList(List<OrderDeliveryClient> clientList, String weipaicwbs) {
		List<CwbOrderView> list = new ArrayList<CwbOrderView>();
		if (clientList != null && !clientList.isEmpty()) {
			// 根据订单号查询订单信息
			List<CwbOrder> cwbOrderList = cwbDAO.getCwbOrderByDelivery(weipaicwbs);
			List<Customer> customerList = customerDAO.getAllCustomersNew();
			List<User> userList = userDAO.getAllUserByuserDeleteFlag();
			list = this.getCwbOrderViewList(cwbOrderList, userList, customerList, clientList);
		}
		return list;
	}

	public List<CwbOrderView> getCwbOrderViewList(List<CwbOrder> cwbOrderList, List<User> userList, List<Customer> customerList, List<OrderDeliveryClient> clientList) {
		List<CwbOrderView> list = new ArrayList<CwbOrderView>();
		if (cwbOrderList != null && !cwbOrderList.isEmpty()) {
			for (CwbOrder o : cwbOrderList) {
				CwbOrderView cwbOrderView = new CwbOrderView();
				cwbOrderView.setCwb(o.getCwb());
				cwbOrderView.setDelivername(dataStatisticsService.getQueryUserName(userList, o.getDeliverid()));// 小件员名称
				cwbOrderView.setCustomername(dataStatisticsService.getQueryCustomerName(customerList, o.getCustomerid()));// 供货商的名称
				cwbOrderView.setEmaildate(o.getEmaildate());// 发货时间
				cwbOrderView.setConsigneename(o.getConsigneename());// 收件人
				cwbOrderView.setReceivablefee(o.getReceivablefee());// 代收货款
				cwbOrderView.setConsigneeaddress(o.getConsigneeaddress());
				cwbOrderView.setCwbremark(o.getCwbremark());
				if (clientList != null && !clientList.isEmpty()) {
					for (OrderDeliveryClient dc : clientList) {
						if (o.getCwb().equals(dc.getCwb())) {
							cwbOrderView.setFdelivername(dataStatisticsService.getQueryUserName(userList, dc.getClientid()));// 委托派送小件员
							cwbOrderView.setEdittime(dc.getDeletetime());
							cwbOrderView.setEditman(dataStatisticsService.getQueryUserName(userList, dc.getDeleteuserid()));// 撤销人
						}
					}
				}
				list.add(cwbOrderView);
			}
		}
		return list;
	}

	/**
	 * 委托派货扫描
	 * 
	 * @param user
	 * @param cwb
	 * @param clientid
	 * @return
	 */
	@Transactional
	public CwbOrder saveLingHuoWeiTuo(User user, String cwb, long clientid) {
		logger.info("===委托派货扫描开始===");
		if (clientid == -1) {// 未选择小件员
			throw new CwbException(cwb, FlowOrderTypeEnum.LingHuoWeiTuo.getValue(), ExceptionCwbErrorTypeEnum.Qing_Xuan_Ze_Xiao_Jian_Yuan);
		}

		CwbOrder co = cwbDAO.getCwbByCwbLock(cwb);
		if (co == null) {// 无此单号
			throw new CwbException(cwb, FlowOrderTypeEnum.LingHuoWeiTuo.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		}

		// 校验配送状态
		DeliveryState ds = deliveryStateDAO.getActiveDeliveryStateByCwb(cwb);
		if (ds != null) {
			// 限制不允许委派非本站点货
			if (ds.getDeliverybranchid() != user.getBranchid()) {// DeliveryStateEnum
				throw new CwbException(cwb, FlowOrderTypeEnum.LingHuoWeiTuo.getValue(), ExceptionCwbErrorTypeEnum.LingHuoWeiTuoYiChangDingDan);
			}
			// 限制已反馈的订单不允许做委托领货操作
			if (ds.getDeliverystate() != 0) {// DeliveryStateEnum
				throw new CwbException(cwb, FlowOrderTypeEnum.LingHuoWeiTuo.getValue(), ExceptionCwbErrorTypeEnum.STATE_CONTROL_ERROR, "已反馈", FlowOrderTypeEnum.LingHuoWeiTuo.getText());
			}
			// 该订单已是此小件员的货，不允许委托领货
			if (ds.getDeliveryid() == clientid) {
				throw new CwbException(cwb, FlowOrderTypeEnum.LingHuoWeiTuo.getValue(), ExceptionCwbErrorTypeEnum.XiaoJianYuanBuYunXuLingHuoWeiTuo);
			}

			OrderDeliveryClient dc = this.loadFormForOrderDeliveryClient(user, cwb, clientid, ds.getDeliveryid());
			// 先删除
			orderDeliveryClientDAO.deleteOrderDeliveryClientByCwb(cwb);
			// 新建领货扫描数据
			orderDeliveryClientDAO.createOrderDeliveryClient(dc);

			logger.info("用户：{},创建委托派活扫描：订单号：{}，小件员：{},委托派送小件员：{}", new Object[] { user.getRealname(), cwb, ds.getDeliveryid(), clientid });
		} else {
			throw new CwbException(cwb, FlowOrderTypeEnum.LingHuoWeiTuo.getValue(), ExceptionCwbErrorTypeEnum.LingHuoWeiTuoYiChangDingDan);
		}
		return co;
	}

	/**
	 * 委派撤销
	 * 
	 * @param user
	 * @param cwb
	 * @param clientid
	 * @return
	 */
	@Transactional
	public CwbOrder deleteLingHuoWeiTuo(User user, String cwb) {
		logger.info("===委托撤销开始===");
		OrderDeliveryClient o = orderDeliveryClientDAO.getOrderDeliveryClientByCwbLock(cwb);
		if (o == null) {// 无此单号
			throw new CwbException(cwb, FlowOrderTypeEnum.WeiPaiCheXiao.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		} else {
			if (o.getState() == 0) {// 已撤销
				throw new CwbException(cwb, FlowOrderTypeEnum.WeiPaiCheXiao.getValue(), ExceptionCwbErrorTypeEnum.STATE_CONTROL_ERROR, "已撤销", FlowOrderTypeEnum.WeiPaiCheXiao.getText());
			}
		}
		CwbOrder co = cwbDAO.getCwbByCwb(cwb);
		String deletetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		orderDeliveryClientDAO.updateOrderDeliveryClientByCwb(user.getUserid(), cwb, deletetime);
		logger.info("用户：{},委派撤销：订单号：{}", new Object[] { user.getRealname(), cwb });
		return co;
	}

	public OrderDeliveryClient loadFormForOrderDeliveryClient(User user, String cwb, long clientid, long deliveryid) {
		OrderDeliveryClient orderDeliveryClient = new OrderDeliveryClient();
		orderDeliveryClient.setCwb(cwb);
		orderDeliveryClient.setClientid(clientid);
		orderDeliveryClient.setDeliveryid(deliveryid);
		orderDeliveryClient.setBranchid(user.getBranchid());
		orderDeliveryClient.setUserid(user.getUserid());
		orderDeliveryClient.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		orderDeliveryClient.setState(1);// 已委派
		return orderDeliveryClient;
	}

	/**
	 * 已领货导出Excel
	 * 
	 * @param request
	 * @param response
	 * @param user
	 * @param cwb
	 * @param flag
	 * @param starttime
	 * @param endtime
	 */
	public void exportYiLingHuo(HttpServletRequest request, HttpServletResponse response, User user) {
		String[] cloumnName1 = {};
		String[] cloumnName2 = {};
		String[] cloumnName3 = {};
		cloumnName1 = new String[8];
		cloumnName2 = new String[8];
		cloumnName3 = new String[8];
		exportService.setDeliveryClientYiLingHuoFields(cloumnName1, cloumnName2, cloumnName3);
		final String[] cloumnName4 = cloumnName1;
		final String[] cloumnName5 = cloumnName2;
		final String[] cloumnName6 = cloumnName3;
		String sheetName = "已领货"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "Order_" + df.format(new Date()) + ".xlsx"; // 文件名
		try {
			// 已委派列表
			List<OrderDeliveryClient> clientList = orderDeliveryClientDAO.getOrderDeliveryClientList(user.getBranchid(), 1, 1);
			StringBuffer weipaisb = new StringBuffer();
			if (clientList != null && !clientList.isEmpty()) {
				for (OrderDeliveryClient dc : clientList) {
					weipaisb.append("'").append(dc.getCwb()).append("',");
				}
			}
			String weipaicwbs = weipaisb.length() == 0 ? "" : weipaisb.substring(0, weipaisb.length() - 1).toString();

			// 查询当前站点所有 已领货&&未反馈 的订单
			List<DeliveryState> deliverystateList = deliveryStateDAO.getDeliveryByDeliver(user.getBranchid(), DeliveryStateEnum.WeiFanKui.getValue(), 1);
			if (deliverystateList != null && !deliverystateList.isEmpty()) {
				List<String> cwbs = new ArrayList<String>();
				for (DeliveryState ds : deliverystateList) {
					cwbs.add(ds.getCwb());
				}

				StringBuffer sb = new StringBuffer();
				for (String s : cwbs) {
					sb.append("'");
					sb.append(s);
					sb.append("',");
				}
				String cwb = sb.length() == 0 ? "" : sb.substring(0, sb.length() - 1).toString();
				String ordercwbs = "'" + cwb.replace("'", "") + "'";

				final String sql = "SELECT * FROM express_ops_cwb_detail  WHERE cwb in(" + cwb + ")  and state=1 ORDER BY FIND_IN_SET(cwb," + ordercwbs + ")";

				ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
					@Override
					public void fillData(final Sheet sheet, final CellStyle style) {
						final List<Customer> customerList = customerDAO.getAllCustomers();
						final List<User> userList = userDAO.getAllUserByuserDeleteFlag();
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
											if ("delivername".equals(cloumnName5[i])) {
												for (User c : userList) {
													if (c.getUserid() == rs.getLong("deliverid")) {
														a = c.getRealname();
														break;
													}
												}
											} else if ("customername".equals(cloumnName5[i])) {
												for (Customer c : customerList) {
													if (c.getCustomerid() == rs.getLong("customerid")) {
														a = c.getCustomername();
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
											e.printStackTrace();
										}
									} catch (IllegalArgumentException e) {
										e.printStackTrace();
									}
								}
								count++;
							}
						});
					}
				};
				excelUtil.excel(response, cloumnName4, sheetName, fileName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 已委派导出Excel
	 * 
	 * @param request
	 * @param response
	 * @param user
	 */
	public void exportYiWeiPai(HttpServletRequest request, HttpServletResponse response, User user) {
		String[] cloumnName1 = {};
		String[] cloumnName2 = {};
		String[] cloumnName3 = {};
		cloumnName1 = new String[9];
		cloumnName2 = new String[9];
		cloumnName3 = new String[9];
		exportService.setDeliveryClientYiWeiPaiFields(cloumnName1, cloumnName2, cloumnName3);
		final String[] cloumnName4 = cloumnName1;
		final String[] cloumnName5 = cloumnName2;
		final String[] cloumnName6 = cloumnName3;
		String sheetName = "已委派"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "Order_" + df.format(new Date()) + ".xlsx"; // 文件名
		try {
			final String sql = "SELECT a.`clientid`,b.* FROM ops_order_delivery_client a LEFT JOIN express_ops_cwb_detail b ON a.`cwb`=b.`cwb` WHERE a.`state`=1 AND b.state=1 AND a.branchid="
					+ user.getBranchid() + " order by id desc";

			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(final Sheet sheet, final CellStyle style) {
					final List<Customer> customerList = customerDAO.getAllCustomers();
					final List<User> userList = userDAO.getAllUserByuserDeleteFlag();
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
										if ("delivername".equals(cloumnName5[i])) {
											for (User c : userList) {
												if (c.getUserid() == rs.getLong("deliverid")) {
													a = c.getRealname();
													break;
												}
											}
										} else if ("clientname".equals(cloumnName5[i])) {
											for (User c : userList) {
												if (c.getUserid() == rs.getLong("clientid")) {
													a = c.getRealname();
													break;
												}
											}
										} else if ("customername".equals(cloumnName5[i])) {
											for (Customer c : customerList) {
												if (c.getCustomerid() == rs.getLong("customerid")) {
													a = c.getCustomername();
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
										e.printStackTrace();
									}
								} catch (IllegalArgumentException e) {
									e.printStackTrace();
								}
							}
							count++;
						}
					});
				}
			};
			excelUtil.excel(response, cloumnName4, sheetName, fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
