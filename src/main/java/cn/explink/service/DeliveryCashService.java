package cn.explink.service;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Service;

import com.pjbest.splitting.aspect.DataSource;
import com.pjbest.splitting.routing.DatabaseType;

import cn.explink.dao.CustomerDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Customer;
import cn.explink.domain.DeliveryCash;
import cn.explink.domain.User;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.StreamingStatementCreator;

@Service
public class DeliveryCashService {
	@Autowired
	UserDAO userDao;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private static Logger logger = LoggerFactory.getLogger(DeliveryCashService.class);

	/**
	 * 获取对应站点的小件员审核后的记录统计
	 * 
	 * @param customerList
	 * @param deliverList
	 * @param flowordertype
	 * @param begindate
	 * @param enddate
	 * @param deliverystate
	 * @param paybackfeeIsZero
	 *            代收金额是否大于0
	 * @return 最外层的是变量存储 第二层是小件员 第三层是供货商 对应金额或者数量
	 */
	@DataSource(DatabaseType.REPLICA)
	public Map<String, Map<Long, Map<Long, BigDecimal>>> getSummary(List<Customer> customerList, List<User> deliverList, final long deliveryid, long flowordertype, String begindate, String enddate,
			String[] deliverystate, Integer paybackfeeIsZero) {
		String sql = getSummarySQL(deliverList, flowordertype, begindate, enddate, deliverystate, paybackfeeIsZero);

		final Map<String, Map<Long, Map<Long, BigDecimal>>> reMap = new HashMap<String, Map<Long, Map<Long, BigDecimal>>>();

		jdbcTemplate.query(new StreamingStatementCreator(sql), new RowCallbackHandler() {
			private StringBuffer cwbs = new StringBuffer();
			Map<Long, Map<Long, BigDecimal>> countMap = new HashMap<Long, Map<Long, BigDecimal>>();
			Map<Long, Map<Long, BigDecimal>> amountNOPOSMap = new HashMap<Long, Map<Long, BigDecimal>>();
			Map<Long, Map<Long, BigDecimal>> amountPOSMap = new HashMap<Long, Map<Long, BigDecimal>>();
			Map<Long, Map<Long, BigDecimal>> amountPaybackMap = new HashMap<Long, Map<Long, BigDecimal>>();

			@Override
			public void processRow(ResultSet rs) throws SQLException {
				if (cwbs.indexOf("'" + rs.getString("cwb") + "'") > -1) {
					return;
				}
				cwbs = cwbs.append("'" + rs.getString("cwb") + "'");
				if (deliveryid > 0 && rs.getLong("deliverid") != deliveryid) {// 如果有小件员条件
																				// 则使用小件员条件
					return;
				}

				if (countMap.get(rs.getLong("deliverid")) == null) {
					Map<Long, BigDecimal> customerMap = new HashMap<Long, BigDecimal>();
					countMap.put(rs.getLong("deliverid"), customerMap);
				}
				Long this_count = 1L;
				if (countMap.get(rs.getLong("deliverid")).get(rs.getLong("customerid")) != null) {
					this_count = countMap.get(rs.getLong("deliverid")).get(rs.getLong("customerid")).longValue() + this_count;
				}
				countMap.get(rs.getLong("deliverid")).put(rs.getLong("customerid"), BigDecimal.valueOf(this_count));

				if (amountNOPOSMap.get(rs.getLong("deliverid")) == null) {
					Map<Long, BigDecimal> customerMap = new HashMap<Long, BigDecimal>();
					amountNOPOSMap.put(rs.getLong("deliverid"), customerMap);
				}

				if (amountPOSMap.get(rs.getLong("deliverid")) == null) {
					Map<Long, BigDecimal> customerMap = new HashMap<Long, BigDecimal>();
					amountPOSMap.put(rs.getLong("deliverid"), customerMap);
				}

				if (amountPaybackMap.get(rs.getLong("deliverid")) == null) {
					Map<Long, BigDecimal> customerMap = new HashMap<Long, BigDecimal>();
					amountPaybackMap.put(rs.getLong("deliverid"), customerMap);
				}
				BigDecimal this_noposamount = rs.getBigDecimal("receivableNoPosfee");

				BigDecimal this_posamount = rs.getBigDecimal("receivablePosfee");

				BigDecimal this_paybackamount = rs.getBigDecimal("paybackfee");

				if (amountNOPOSMap.get(rs.getLong("deliverid")).get(rs.getLong("customerid")) != null) {
					this_noposamount = amountNOPOSMap.get(rs.getLong("deliverid")).get(rs.getLong("customerid")).add(this_noposamount);
				}

				if (amountPOSMap.get(rs.getLong("deliverid")).get(rs.getLong("customerid")) != null) {
					this_posamount = amountPOSMap.get(rs.getLong("deliverid")).get(rs.getLong("customerid")).add(this_posamount);
				}

				if (amountPaybackMap.get(rs.getLong("deliverid")).get(rs.getLong("customerid")) != null) {
					this_paybackamount = amountPaybackMap.get(rs.getLong("deliverid")).get(rs.getLong("customerid")).add(this_paybackamount);
				}
				amountNOPOSMap.get(rs.getLong("deliverid")).put(rs.getLong("customerid"), this_noposamount);
				amountPOSMap.get(rs.getLong("deliverid")).put(rs.getLong("customerid"), this_posamount);
				amountPaybackMap.get(rs.getLong("deliverid")).put(rs.getLong("customerid"), this_paybackamount);
				reMap.put("count", countMap);
				reMap.put("amountNOPOSMap", amountNOPOSMap);
				reMap.put("amountPOSMap", amountPOSMap);
				reMap.put("amountPaybackMap", amountPaybackMap);
			}

		});

		return reMap;
	}

	/**
	 * 获取对应站点的小件员审核后的记录统计脚本
	 * @param deliverList
	 * @param flowordertype
	 * @param begindate
	 * @param enddate
	 * @param deliverystate
	 * @param paybackfeeIsZero
	 * @return
	 */
	private String getSummarySQL(List<User> deliverList, long flowordertype, String begindate, String enddate, String[] deliverystate, Integer paybackfeeIsZero) {
		StringBuffer deliveryids = new StringBuffer();
		for (User u : deliverList) {
			deliveryids = deliveryids.append(u.getUserid()).append(",");
		}
		if (deliveryids.length() > 0) {
			deliveryids = deliveryids.append(deliveryids.substring(0, deliveryids.length() - 1));
		}
		if (deliveryids.length() == 0) {
			deliveryids = deliveryids.append("0");
		}

		String sql = "select * from express_ops_deliver_cash ";

		if (flowordertype > 0 || deliverystate.length > 0 || deliveryids.length() > 0) {
			sql += " where ";
			StringBuffer strsql = new StringBuffer();
			String timeType = "";
			if (flowordertype == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
				strsql.append(" and linghuotime> '" + begindate + "' and linghuotime< '" + enddate + "'");
				timeType = "linghuotime";
			} else if (flowordertype == FlowOrderTypeEnum.YiFanKui.getValue()) {
				strsql.append(" and fankuitime> '" + begindate + "' and fankuitime< '" + enddate + "'");
				timeType = "fankuitime";
			} else if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue()) {
				strsql.append(" and guibantime> '" + begindate + "' and guibantime< '" + enddate + "'");
				timeType = "guibantime";
			}
			if (deliverystate.length > 0) {
				StringBuffer deliverystates = new StringBuffer();
				for (int i = 0; i < deliverystate.length; i++) {
					deliverystates = deliverystates.append(deliverystate[i]).append(",");
				}
				strsql.append(" and deliverystate in(" + deliverystates.substring(0, deliverystates.length() - 1) + ")");
			}

			if (deliveryids.length() > 0) {
				strsql.append(" and deliverid in (" + deliveryids + ") ");
			}
			if (paybackfeeIsZero > -1) {
				if (paybackfeeIsZero == 0) {
					strsql.append(" and receivableNoPosfee=0 and receivablePosfee=0 ");
				} else {
					strsql.append(" and receivableNoPosfee + receivablePosfee >0  ");
				}
			}

			strsql.append("and state=1  order by " + timeType + " desc");

			sql += strsql.substring(4, strsql.length());
		}
		return sql;
	}
	
	/**
	 * 获取对应站点的小件员审核后的记录统计脚本，根据每个客户的合计降序
	 * @Date 2016-07-12
     * @author jian.xie
	 * @return
	 */
	private String getSummaryForCustomerSQL(List<User> deliverList, long flowordertype, String begindate, String enddate, String[] deliverystate, Integer paybackfeeIsZero) {
		StringBuffer deliveryids = new StringBuffer();
		for (User u : deliverList) {
			deliveryids = deliveryids.append(u.getUserid()).append(",");
		}
		if (deliveryids.length() > 0) {
			deliveryids = deliveryids.append(deliveryids.substring(0, deliveryids.length() - 1));
		}
		if (deliveryids.length() == 0) {
			deliveryids = deliveryids.append("0");
		}

		String sql = "select customerid from express_ops_deliver_cash ";

		if (flowordertype > 0 || deliverystate.length > 0 || deliveryids.length() > 0) {
			sql += " where ";
			StringBuffer strsql = new StringBuffer();
			if (flowordertype == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
				strsql.append(" and linghuotime> '" + begindate + "' and linghuotime< '" + enddate + "'");
			} else if (flowordertype == FlowOrderTypeEnum.YiFanKui.getValue()) {
				strsql.append(" and fankuitime> '" + begindate + "' and fankuitime< '" + enddate + "'");
			} else if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue()) {
				strsql.append(" and guibantime> '" + begindate + "' and guibantime< '" + enddate + "'");
			}
			if (deliverystate.length > 0) {
				StringBuffer deliverystates = new StringBuffer();
				for (int i = 0; i < deliverystate.length; i++) {
					deliverystates = deliverystates.append(deliverystate[i]).append(",");
				}
				strsql.append(" and deliverystate in(" + deliverystates.substring(0, deliverystates.length() - 1) + ")");
			}

			if (deliveryids.length() > 0) {
				strsql.append(" and deliverid in (" + deliveryids + ") ");
			}
			if (paybackfeeIsZero > -1) {
				if (paybackfeeIsZero == 0) {
					strsql.append(" and receivableNoPosfee=0 and receivablePosfee=0 ");
				} else {
					strsql.append(" and receivableNoPosfee + receivablePosfee >0  ");
				}
			}

			strsql.append("and state=1 GROUP BY customerid  order by COUNT(customerid) desc");

			sql += strsql.substring(4, strsql.length());
		}
		return sql;
	}
	
	
	/**
	 * 获取对应客户记录统计，根据合计降序
	 * @Date 2016-07-12
	 * add by jian_xie
	 *            代收金额是否大于0
	 * @return 客户id
	 */
	public List<String> getCustomerListForSummary(List<User> deliverList, final long deliveryid, long flowordertype, String begindate, String enddate, String[] deliverystate,
			Integer paybackfeeIsZero) {
		String sql = getSummaryForCustomerSQL(deliverList, flowordertype, begindate, enddate, deliverystate, paybackfeeIsZero);
		List<String> list = jdbcTemplate.queryForList(sql, String.class);
		
		return list;
	}

	/**
	 * 获取对应站点的小件员审核后的记录统计
	 * 
	 * @param customerList
	 * @param deliverList
	 * @param flowordertype
	 * @param begindate
	 * @param enddate
	 * @param deliverystate
	 * @return 最外层的是变量存储 第二层是小件员 第三层是供货商 对应金额或者数量
	 */
	@DataSource(DatabaseType.REPLICA)
	public List<DeliveryCash> getSummaryList(List<Customer> customerList, List<User> deliverList, final long deliveryid, long flowordertype, String begindate, String enddate, String[] deliverystate,
			Long customerId, Integer paybackfeeIsZero) {
		StringBuffer deliveryids = new StringBuffer();
		for (User u : deliverList) {
			deliveryids = deliveryids.append(u.getUserid()).append(",");
		}
		if (deliveryids.length() > 0) {
			deliveryids = deliveryids.append(deliveryids.substring(0, deliveryids.length() - 1));
		}
		if (deliveryids.length() == 0) {
			deliveryids = deliveryids.append("0");
		}

		String sql = "select * from express_ops_deliver_cash ";

		if (flowordertype > 0 || deliverystate.length > 0 || deliveryids.length() > 0 || customerId > 0) {
			sql += " where ";
			StringBuffer strsql = new StringBuffer();
			String timeType = "";

			if (customerId > 0) {
				strsql.append(" and customerid=" + customerId);
			}

			if (flowordertype == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
				strsql.append(" and linghuotime> '" + begindate + "' and linghuotime< '" + enddate + "'");
				timeType = "linghuotime";
			} else if (flowordertype == FlowOrderTypeEnum.YiFanKui.getValue()) {
				strsql.append(" and fankuitime> '" + begindate + "' and fankuitime< '" + enddate + "'");
				timeType = "fankuitime";
			} else if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue()) {
				strsql.append(" and guibantime> '" + begindate + "' and guibantime< '" + enddate + "'");
				timeType = "guibantime";
			}
			if (deliverystate.length > 0 && !deliverystate.equals("-1")) {
				StringBuffer deliverystates = new StringBuffer();
				for (int i = 0; i < deliverystate.length; i++) {
					deliverystates = deliverystates.append(deliverystate[i]).append(",");
				}
				strsql.append(" and deliverystate in(" + deliverystates.substring(0, deliverystates.length() - 1) + ")");
			}

			if (deliveryids.length() > 0) {
				strsql.append(" and deliverid in (" + deliveryids + ") ");
			}
			if (paybackfeeIsZero > -1) {
				if (paybackfeeIsZero == 0) {
					strsql.append(" and receivableNoPosfee=0 and receivablePosfee=0 ");
				} else {
					strsql.append(" and receivableNoPosfee + receivablePosfee >0  ");
				}
			}

			strsql.append("and state=1  order by " + timeType + " desc");

			sql += strsql.substring(4, strsql.length());
		}

		final List<DeliveryCash> reList = new ArrayList<DeliveryCash>();

		jdbcTemplate.query(new StreamingStatementCreator(sql), new RowCallbackHandler() {
			private StringBuffer cwbs = new StringBuffer();

			@Override
			public void processRow(ResultSet rs) throws SQLException {
				if (cwbs.indexOf("'" + rs.getString("cwb") + "'") > -1) {
					return;
				}
				cwbs = cwbs.append("'" + rs.getString("cwb") + "'");
				if (deliveryid > 0 && rs.getLong("deliverid") != deliveryid) {// 如果有小件员条件
																				// 则使用小件员条件
					return;
				}

				DeliveryCash deliveryCash = new DeliveryCash();
				deliveryCash.setDeliverystateid(rs.getLong("deliverystateid"));
				deliveryCash.setFankuitime(rs.getString("fankuitime"));
				deliveryCash.setGuibantime(rs.getString("guibantime"));
				deliveryCash.setLinghuotime(rs.getString("linghuotime"));
				deliveryCash.setPaybackfee(rs.getBigDecimal("paybackfee"));
				deliveryCash.setReceivableNoPosfee(rs.getBigDecimal("receivableNoPosfee"));
				deliveryCash.setReceivablePosfee(rs.getBigDecimal("receivablePosfee"));
				deliveryCash.setId(rs.getLong("id"));
				deliveryCash.setCwb(rs.getString("cwb"));
				deliveryCash.setDeliverid(rs.getLong("deliverid"));
				deliveryCash.setDeliverystate(rs.getLong("deliverystate"));
				deliveryCash.setGcaid(rs.getLong("gcaid"));
				deliveryCash.setDeliverybranchid(rs.getLong("deliverybranchid"));
				deliveryCash.setCustomerid(rs.getLong("customerid"));
				deliveryCash.setState(rs.getLong("state"));
				reList.add(deliveryCash);
			}

		});

		return reList;
	}

	@DataSource(DatabaseType.REPLICA)
	public List<String> getSummaryCwbList(List<Customer> customerList, List<User> deliverList, final long deliveryid, long flowordertype, String begindate, String enddate, String[] deliverystate,
			Long customerId, Integer paybackfeeIsZero) {

		StringBuffer deliveryids = new StringBuffer();
		for (User u : deliverList) {
			deliveryids = deliveryids.append(u.getUserid()).append(",");
		}
		if (deliveryids.length() > 0) {
			deliveryids = deliveryids.append(deliveryids.substring(0, deliveryids.length() - 1));
		}
		if (deliveryids.length() == 0) {
			deliveryids = deliveryids.append("0");
		}

		String sql = "select * from express_ops_deliver_cash ";

		if (flowordertype > 0 || deliverystate.length > 0 || deliveryids.length() > 0 || customerId > 0) {
			sql += " where ";
			StringBuffer strsql = new StringBuffer();
			String timeType = "";

			if (customerId > 0) {
				strsql.append(" and customerid=" + customerId);
			}

			if (flowordertype == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
				strsql.append(" and linghuotime> '" + begindate + "' and linghuotime< '" + enddate + "'");
				timeType = "linghuotime";
			} else if (flowordertype == FlowOrderTypeEnum.YiFanKui.getValue()) {
				strsql.append(" and fankuitime> '" + begindate + "' and fankuitime< '" + enddate + "'");
				timeType = "fankuitime";
			} else if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue()) {
				strsql.append(" and guibantime> '" + begindate + "' and guibantime< '" + enddate + "'");
				timeType = "guibantime";
			}
			if (deliverystate.length > 0 && !deliverystate.equals("-1")) {
				StringBuffer deliverystates = new StringBuffer();
				for (int i = 0; i < deliverystate.length; i++) {
					deliverystates = deliverystates.append(deliverystate[i]).append(",");
				}
				strsql.append(" and deliverystate in(" + deliverystates.substring(0, deliverystates.length() - 1) + ")");
			}

			if (deliveryids.length() > 0) {
				strsql.append(" and deliverid in (" + deliveryids + ") ");
			}
			if (paybackfeeIsZero > -1) {
				if (paybackfeeIsZero == 0) {
					strsql.append(" and receivableNoPosfee=0 and receivablePosfee=0 ");
				} else {
					strsql.append(" and receivableNoPosfee + receivablePosfee >0  ");
				}
			}

			strsql.append("  and state=1 order by " + timeType + " desc");

			sql += strsql.substring(4, strsql.length());
		}

		final List<String> reList = new ArrayList<String>();

		jdbcTemplate.query(new StreamingStatementCreator(sql), new RowCallbackHandler() {
			private StringBuffer cwbs = new StringBuffer();

			@Override
			public void processRow(ResultSet rs) throws SQLException {
				if (cwbs.indexOf("'" + rs.getString("cwb") + "'") > -1) {
					return;
				}
				cwbs = cwbs.append("'" + rs.getString("cwb") + "'");
				if (deliveryid > 0 && rs.getLong("deliverid") != deliveryid) {// 如果有小件员条件
																				// 则使用小件员条件
					return;
				}

				DeliveryCash deliveryCash = new DeliveryCash();
				deliveryCash.setCwb(rs.getString("cwb"));
				reList.add(deliveryCash.getCwb());
			}

		});

		return reList;
	}

	/**
	 * 小件员工作量统计 导出
	 * modify by jian_xie
	 * 2016-07-12
	 * @param summary
	 * @param response
	 * @param customerSorList 
	 */
	public void export(Map<String, Map<Long, Map<Long, BigDecimal>>> summary, HttpServletResponse response, List<String> customerSorList) {
		SXSSFWorkbook wb = new SXSSFWorkbook(); // excel文件,一个excel文件包含多个表
		Sheet sheet = wb.createSheet(); // 表，一个表包含多个行
		String filename = "小件员工作量统计" + DateTimeUtil.getNowDate() + ".xlsx";
		wb.setSheetName(0, filename);
		// 设置字体等样式
		Font font = wb.createFont();
		font.setFontName("Courier New");
		CellStyle style = wb.createCellStyle();
		style.setFont(font);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

		// 单元格
		Row row = sheet.createRow(0); // 由HSSFSheet生成行
		row.setHeightInPoints((float) 15); // 设置行高

		List<Customer> customers = customerDAO.getAllCustomers();
		customers = resetCustomer(customers, customerSorList);
		String[] firstLine = new String[2];
		firstLine[0] = "小件员";
		firstLine[1] = "供货商";
		Map<Long, Map<Long, BigDecimal>> countMap = summary.get("count");
		Map<Long, Map<Long, BigDecimal>> amountNOPOSMap = summary.get("amountNOPOSMap");
		Map<Long, Map<Long, BigDecimal>> amountPOSMap = summary.get("amountPOSMap");
		Map<Long, Map<Long, BigDecimal>> amountPaybackMap = summary.get("amountPaybackMap");
		Set<Long> userids = countMap.keySet();
		String useridsStr = getUserString(userids);
		List<User> userList = userDao.getAllUserByUserIds(useridsStr);
		if (userList != null && userList.size() > 0) {
			sheet.addMergedRegion(new CellRangeAddress(0, (short) 0, 1, (short) customers.size() * 4 + 4)); // 合并第一行
																											// 供货商
																											// 单元格
			sheet.createFreezePane(1, 0, 3 * userList.size() + 1, 0);// 冻结第一列
			Cell cell = row.createCell(0);
			cell.setCellStyle(style);
			cell.setCellValue(firstLine[0]);
			Cell cusCell = row.createCell(1);
			cusCell.setCellStyle(style);
			cusCell.setCellValue(firstLine[1]);

			for (int i = 1; i < userList.size() + 1; i++) {
				long count = 0;// 投递量
				double nopos = 0;
				double pos = 0;
				double backFee = 0;
				Row rowFir = sheet.createRow(3 * i - 2);
				Row rowSec = sheet.createRow(3 * i - 1);
				Row rowThr = sheet.createRow(3 * i);

				sheet.addMergedRegion(new CellRangeAddress(rowFir.getRowNum(), rowThr.getRowNum(), (short) 0, (short) 0));
				Cell deliveryCell = rowFir.createCell(0);
				deliveryCell.setCellStyle(style);
				deliveryCell.setCellValue(userList.get(i - 1).getRealname());
				for (int j = 1; j < customers.size() + 1; j++) {

					// 第一层
					Cell cellFir = rowFir.createCell(4 * j - 3);
					Cell cellSec = rowFir.createCell(4 * j - 2);
					Cell cellThr = rowFir.createCell(4 * j - 1);
					Cell cellFou = rowFir.createCell(4 * j);
					sheet.addMergedRegion(new CellRangeAddress(cellFir.getRowIndex(), cellFir.getRowIndex(), cellFir.getColumnIndex(), cellFou.getColumnIndex()));
					cellFir.setCellStyle(style);
					cellFir.setCellValue(customers.get(j - 1).getCustomername());
					// 第二层
					Cell cellFir2 = rowSec.createCell(4 * j - 3);
					Cell cellSec2 = rowSec.createCell(4 * j - 2);
					Cell cellThr2 = rowSec.createCell(4 * j - 1);
					Cell cellFou2 = rowSec.createCell(4 * j);
					cellFir2.setCellStyle(style);
					cellSec2.setCellStyle(style);
					cellThr2.setCellStyle(style);
					cellFou2.setCellStyle(style);
					cellFir2.setCellValue("投递量");
					cellSec2.setCellValue("非POS");
					cellThr2.setCellValue("POS");
					cellFou2.setCellValue("应退款");
					// 第三层
					Cell cellFir3 = rowThr.createCell(4 * j - 3);
					Cell cellSec3 = rowThr.createCell(4 * j - 2);
					Cell cellThr3 = rowThr.createCell(4 * j - 1);
					Cell cellFou3 = rowThr.createCell(4 * j);
					cellFir3.setCellStyle(style);
					cellSec3.setCellStyle(style);
					cellThr3.setCellStyle(style);
					cellFou3.setCellStyle(style);

					count += Long.parseLong(countMap.get(userList.get(i - 1).getUserid()).get(customers.get(j - 1).getCustomerid()) == null ? "0" : countMap.get(userList.get(i - 1).getUserid())
							.get(customers.get(j - 1).getCustomerid()).toString());
					nopos += Double.parseDouble(amountNOPOSMap.get(userList.get(i - 1).getUserid()).get(customers.get(j - 1).getCustomerid()) == null ? "0" : amountNOPOSMap
							.get(userList.get(i - 1).getUserid()).get(customers.get(j - 1).getCustomerid()).toString());
					pos += Double.parseDouble(amountPOSMap.get(userList.get(i - 1).getUserid()).get(customers.get(j - 1).getCustomerid()) == null ? "0" : amountPOSMap
							.get(userList.get(i - 1).getUserid()).get(customers.get(j - 1).getCustomerid()).toString());
					backFee += Double.parseDouble(amountPaybackMap.get(userList.get(i - 1).getUserid()).get(customers.get(j - 1).getCustomerid()) == null ? "0" : amountPaybackMap
							.get(userList.get(i - 1).getUserid()).get(customers.get(j - 1).getCustomerid()).toString());

					cellFir3.setCellValue(countMap.get(userList.get(i - 1).getUserid()).get(customers.get(j - 1).getCustomerid()) == null ? "0" : countMap.get(userList.get(i - 1).getUserid())
							.get(customers.get(j - 1).getCustomerid()).toString());
					cellSec3.setCellValue(amountNOPOSMap.get(userList.get(i - 1).getUserid()).get(customers.get(j - 1).getCustomerid()) == null ? "0" : amountNOPOSMap
							.get(userList.get(i - 1).getUserid()).get(customers.get(j - 1).getCustomerid()).toString());
					cellThr3.setCellValue(amountPOSMap.get(userList.get(i - 1).getUserid()).get(customers.get(j - 1).getCustomerid()) == null ? "0" : amountPOSMap.get(userList.get(i - 1).getUserid())
							.get(customers.get(j - 1).getCustomerid()).toString());
					cellFou3.setCellValue(amountPaybackMap.get(userList.get(i - 1).getUserid()).get(customers.get(j - 1).getCustomerid()) == null ? "0" : amountPaybackMap
							.get(userList.get(i - 1).getUserid()).get(customers.get(j - 1).getCustomerid()).toString());

				}
				// 右边合计
				// 第一层
				Cell cellFir = rowFir.createCell(4 * customers.size() + 1);
				Cell cellSec = rowFir.createCell(4 * customers.size() + 2);
				Cell cellThr = rowFir.createCell(4 * customers.size() + 3);
				Cell cellFou = rowFir.createCell(4 * customers.size() + 4);
				sheet.addMergedRegion(new CellRangeAddress(cellFir.getRowIndex(), cellFir.getRowIndex(), cellFir.getColumnIndex(), cellFou.getColumnIndex()));
				cellFir.setCellStyle(style);
				cellFir.setCellValue("合计");
				// 第二层
				Cell cellFir2 = rowSec.createCell(4 * customers.size() + 1);
				Cell cellSec2 = rowSec.createCell(4 * customers.size() + 2);
				Cell cellThr2 = rowSec.createCell(4 * customers.size() + 3);
				Cell cellFou2 = rowSec.createCell(4 * customers.size() + 4);
				cellFir2.setCellStyle(style);
				cellSec2.setCellStyle(style);
				cellThr2.setCellStyle(style);
				cellFou2.setCellStyle(style);
				cellFir2.setCellValue("投递量");
				cellSec2.setCellValue("非POS");
				cellThr2.setCellValue("POS");
				cellFou2.setCellValue("应退款");
				// 第三层
				Cell cellFir3 = rowThr.createCell(4 * customers.size() + 1);
				Cell cellSec3 = rowThr.createCell(4 * customers.size() + 2);
				Cell cellThr3 = rowThr.createCell(4 * customers.size() + 3);
				Cell cellFou3 = rowThr.createCell(4 * customers.size() + 4);
				cellFir3.setCellStyle(style);
				cellSec3.setCellStyle(style);
				cellThr3.setCellStyle(style);
				cellFou3.setCellStyle(style);

				cellFir3.setCellValue(count);
				cellSec3.setCellValue(nopos);
				cellThr3.setCellValue(pos);
				cellFou3.setCellValue(backFee);

				// 下边合计
				if (i == userList.size()) {
					Row rowFou = sheet.createRow(3 * i + 1);
					Cell cellCount = rowFou.createCell(0);
					cellCount.setCellStyle(style);
					cellCount.setCellValue("合计");

					long countDown = 0;
					double noposDown = 0;
					double posDown = 0;
					double backFeeDown = 0;

					for (int j = 1; j < customers.size() + 1; j++) {
						// 针对供货商 的合计
						long countCus = 0;// 投递量
						double noposCus = 0;
						double posCus = 0;
						double backFeeCus = 0;

						Cell cellFir4 = rowFou.createCell(4 * j - 3);
						Cell cellSec4 = rowFou.createCell(4 * j - 2);
						Cell cellThr4 = rowFou.createCell(4 * j - 1);
						Cell cellFou4 = rowFou.createCell(4 * j);

						cellFir4.setCellStyle(style);
						cellSec4.setCellStyle(style);
						cellThr4.setCellStyle(style);
						cellFou4.setCellStyle(style);

						for (int k = 0; k < userList.size(); k++) {
							countCus += Long.parseLong(countMap.get(userList.get(k).getUserid()).get(customers.get(j - 1).getCustomerid()) == null ? "0" : countMap.get(userList.get(k).getUserid())
									.get(customers.get(j - 1).getCustomerid()).toString());
							noposCus += Double.parseDouble(amountNOPOSMap.get(userList.get(k).getUserid()).get(customers.get(j - 1).getCustomerid()) == null ? "0" : amountNOPOSMap
									.get(userList.get(k).getUserid()).get(customers.get(j - 1).getCustomerid()).toString());
							posCus += Double.parseDouble(amountPOSMap.get(userList.get(k).getUserid()).get(customers.get(j - 1).getCustomerid()) == null ? "0" : amountPOSMap
									.get(userList.get(k).getUserid()).get(customers.get(j - 1).getCustomerid()).toString());
							backFeeCus += Double.parseDouble(amountPaybackMap.get(userList.get(k).getUserid()).get(customers.get(j - 1).getCustomerid()) == null ? "0" : amountPaybackMap
									.get(userList.get(k).getUserid()).get(customers.get(j - 1).getCustomerid()).toString());

						}

						// 右下角总合计
						countDown += countCus;
						noposDown += noposCus;
						posDown += posCus;
						backFeeDown += backFeeCus;

						cellFir4.setCellValue(countCus);
						cellSec4.setCellValue(noposCus);
						cellThr4.setCellValue(posCus);
						cellFou4.setCellValue(backFeeCus);

					}
					// 右边的总合计
					Cell cellRightCount = rowFou.createCell(4 * customers.size() + 1);
					Cell cellRightNopos = rowFou.createCell(4 * customers.size() + 2);
					Cell cellRightPos = rowFou.createCell(4 * customers.size() + 3);
					Cell cellRightBackFee = rowFou.createCell(4 * customers.size() + 4);

					cellRightCount.setCellValue(countDown);
					cellRightNopos.setCellValue(noposDown);
					cellRightPos.setCellValue(posDown);
					cellRightBackFee.setCellValue(backFeeDown);

				}
			}

		}
		// 导出

		OutputStream out = null;
		try {
			response.setContentType("application/x-msdownload");
//			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
			response.setHeader("content-disposition",  String.format("attachment;filename*=utf-8'zh_cn'%s",URLEncoder.encode(filename, "utf-8")));
			out = response.getOutputStream();
			wb.write(out);
			out.close();
		} catch (IOException e) {
			try {
				out.close();
			} catch (IOException e1) {
				logger.error("", e1);
			}
			logger.error("", e);
		}

	}

	private String getUserString(Set<Long> userids) {
		String strs = "";
		if (userids.size() > 0) {
			for (Long str : userids) {
				strs += "'" + str + "',";
			}
		}

		if (strs.length() > 0) {
			strs = strs.substring(0, strs.length() - 1);
		}
		return strs;

	}
	
	/**
	 * 根据已经排好的客户顺序，对所有的客户排序。
	 * @return
	 */
	public List<Customer> resetCustomer(List<Customer> customerList, List<String> sortList){
		List<Customer> newCustomer = new ArrayList<Customer>();
		Customer customer = null;
		for(String customerid : sortList){
			for(int i = 0, size = customerList.size(); i < size; i++){
				customer = customerList.get(i);
				if(customerid.equals(customer.getCustomerid() + "")){
					newCustomer.add(customer);
					customerList.remove(i);
					break;
				}
			}
		}
		newCustomer.addAll(customerList);
		return newCustomer;
	}
	
}
