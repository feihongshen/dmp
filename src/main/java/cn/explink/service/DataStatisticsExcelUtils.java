package cn.explink.service;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;

import cn.explink.dao.OrderFlowDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Common;
import cn.explink.domain.Complaint;
import cn.explink.domain.CustomWareHouse;
import cn.explink.domain.Customer;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.Reason;
import cn.explink.domain.Remark;
import cn.explink.domain.TuihuoRecord;
import cn.explink.domain.User;
import cn.explink.util.ExcelUtils;
import cn.explink.util.Page;
import cn.explink.util.StreamingStatementCreator;

/**
 * @author leo01.liao
 *
 */
public class DataStatisticsExcelUtils extends ExcelUtils {
	private static Logger logger = LoggerFactory.getLogger(DataStatisticsExcelUtils.class);

	private DataStatisticsService dataStatisticsService;

	private String[] cloumnName4;
	private String[] cloumnName5;
	private String[] cloumnName6;
	private String sql;
	private long   total = 0; //总记录数
	private long   page  = 0;
	private int    batchSize = 10000; //每批数量为1万

	public DataStatisticsExcelUtils(DataStatisticsService dataStatisticsService) {
		this.dataStatisticsService = dataStatisticsService;
	}

	@Override
	public void fillData(final Sheet sheet, final CellStyle style) {
		List<User>          uList = dataStatisticsService.userDAO.getAllUserByuserDeleteFlag();
		Map<Long, Customer> cMap  = dataStatisticsService.customerDAO.getAllCustomersToMap();
		List<Branch>        bList = dataStatisticsService.branchDAO.getAllBranches();
		List<Common> commonList   = dataStatisticsService.commonDAO.getAllCommons();
		List<CustomWareHouse> cWList = dataStatisticsService.customWareHouseDAO.getAllCustomWareHouse();
		List<Remark> remarkList = dataStatisticsService.remarkDAO.getAllRemark();
		Map<String, Map<String, String>> remarkMap = dataStatisticsService.exportService.getInwarhouseRemarks(remarkList);
		List<Reason> reasonList = dataStatisticsService.reasonDao.getAllReason();

		//分批取数并写到excel中
		int cntLoop = Page.EXCEL_PAGE_NUMBER/batchSize;
		for(int i=0; i<cntLoop; i++){
			long start = page + (i * batchSize);
			
			if(start >= total){
				break;
			}
					
			String sqlTmp = sql.replace(OrderFlowDAO.LIMIT_FLAG, " limit " + start + "," + batchSize + " ");
			
			logger.info("fillData sqlTmp:{}", sqlTmp);
			
			long startTm = System.currentTimeMillis();
			
			toFillData(sqlTmp, sheet, style, uList, cMap, bList, commonList, cWList, remarkList, remarkMap, reasonList);
			
			logger.info("fillData toFillData need times:{} ms", (System.currentTimeMillis() - startTm));
		}
	}
	
	/**
	 * 获取数据并创建excel行列
	 * @param sqlTmp
	 * @param sheet
	 * @param style
	 * @param uList
	 * @param cMap
	 * @param bList
	 * @param commonList
	 * @param cWList
	 * @param remarkList
	 * @param remarkMap
	 * @param reasonList
	 */
	private void toFillData(final String sqlTmp, final Sheet sheet, final CellStyle style, final List<User> uList, final Map<Long, Customer> cMap, 
							final List<Branch> bList, final List<Common> commonList, final List<CustomWareHouse> cWList, 
							final List<Remark> remarkList, final Map<String, Map<String, String>> remarkMap, 
							final List<Reason> reasonList){
		//获取数据并创建excel行列
		dataStatisticsService.jdbcTemplate.query(new StreamingStatementCreator(sqlTmp), new ResultSetExtractor<Object>() {
			private int count = 0;
			ColumnMapRowMapper columnMapRowMapper = new ColumnMapRowMapper();
			private List<Map<String, Object>> recordbatch = new ArrayList<Map<String, Object>>();

			public void processRow(ResultSet rs) throws SQLException {
				Map<String, Object> mapRow = this.columnMapRowMapper.mapRow(rs, this.count);
				this.recordbatch.add(mapRow);
				this.count++;
				if ((this.count % 100) == 0) {
					this.writeBatch();
				}
			}

			private void writeSingle(Map<String, Object> mapRow, TuihuoRecord tuihuoRecord, DeliveryState ds,
					Map<String, String> allTime, int rownum, Map<String, String> cwbspayupidMap,
					Map<String, String> complaintMap) throws SQLException {				
				//Row row = sheet.createRow(rownum + 1);
				Row row = sheet.createRow(sheet.getLastRowNum() + 1);
				
				row.setHeightInPoints(15);
				for (int i = 0; i < cloumnName4.length; i++) {
					Cell cell = row.createCell((short) i);
					cell.setCellStyle(style);
					// sheet.setColumnWidth(i, (short) (5000));
					// //设置列宽
					Object a = dataStatisticsService.exportService.setObjectA(cloumnName5, mapRow, i, uList, cMap,
							bList, commonList, tuihuoRecord, ds, allTime, cWList, remarkMap, reasonList,
							cwbspayupidMap, complaintMap);
					if (cloumnName6[i].equals("double")) {
						cell.setCellValue(a == null ? BigDecimal.ZERO.doubleValue() : a.equals("") ? BigDecimal.ZERO
								.doubleValue() : Double.parseDouble(a.toString()));
					} else {
						cell.setCellValue(a == null ? "" : a.toString());
					}
				}
			}

			@Override
			public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
				while (rs.next()) {
					this.processRow(rs);
				}
				this.writeBatch();
				return null;
			}

			public void writeBatch() throws SQLException {
				if (this.recordbatch.size() > 0) {
					List<String> cwbs = new ArrayList<String>();
					for (Map<String, Object> mapRow : this.recordbatch) {
						cwbs.add(mapRow.get("cwb").toString());
					}
					Map<String, DeliveryState> deliveryStates = getDeliveryListByCwbs(cwbs);
					Map<String, TuihuoRecord> tuihuorecoredMap = getTuihuoRecoredMap(cwbs);
					Map<String, String> cwbspayupMsp = getcwbspayupidMap(cwbs);
					Map<String, String> complaintMap = getComplaintMap(cwbs);
					Map<String, Map<String, String>> orderflowList = dataStatisticsService.getOrderFlowByCredateForDetailAndExportAllTime(cwbs, bList);
					int size = this.recordbatch.size();
					for (int i = 0; i < size; i++) {
						String cwb = this.recordbatch.get(i).get("cwb").toString();
						this.writeSingle(this.recordbatch.get(i), tuihuorecoredMap.get(cwb), deliveryStates.get(cwb), orderflowList.get(cwb), (this.count - size) + i, cwbspayupMsp, complaintMap);
					}
					this.recordbatch.clear();
				}
			}

			private Map<String, TuihuoRecord> getTuihuoRecoredMap(List<String> cwbs) {
				Map<String, TuihuoRecord> map = new HashMap<String, TuihuoRecord>();
				for (TuihuoRecord tuihuoRecord : dataStatisticsService.tuihuoRecordDAO.getTuihuoRecordByCwbs(cwbs)) {
					map.put(tuihuoRecord.getCwb(), tuihuoRecord);
				}
				return map;
			}

			private Map<String, DeliveryState> getDeliveryListByCwbs(List<String> cwbs) {
				Map<String, DeliveryState> map = new HashMap<String, DeliveryState>();
				for (DeliveryState deliveryState : dataStatisticsService.deliveryStateDAO
						.getActiveDeliveryStateByCwbs(cwbs)) {
					map.put(deliveryState.getCwb(), deliveryState);
				}
				return map;
			}

			private Map<String, String> getComplaintMap(List<String> cwbs) {
				Map<String, String> complaintMap = new HashMap<String, String>();
				for (Complaint complaint : dataStatisticsService.complaintDAO.getActiveComplaintByCwbs(cwbs)) {
					complaintMap.put(complaint.getCwb(), complaint.getContent());
				}
				return complaintMap;
			}

			private Map<String, String> getcwbspayupidMap(List<String> cwbs) {
				Map<String, String> cwbspayupidMap = new HashMap<String, String>();
				/*
				 * for(DeliveryState deliveryState:deliveryStateDAO. getActiveDeliveryStateByCwbs(cwbs)){ String ispayup
				 * = "否"; GotoClassAuditing goclass = gotoClassAuditingDAO .getGotoClassAuditingByGcaid(deliveryState
				 * .getGcaid());
				 * 
				 * if(goclass!=null&&goclass.getPayupid()!=0){ ispayup = "是"; }
				 * cwbspayupidMap.put(deliveryState.getCwb(), ispayup); }
				 */
				return cwbspayupidMap;
			}
		});
	}

	public void setCloumnName4(String[] cloumnName4) {
		this.cloumnName4 = cloumnName4;
	}

	public void setCloumnName5(String[] cloumnName5) {
		this.cloumnName5 = cloumnName5;
	}

	public void setCloumnName6(String[] cloumnName6) {
		this.cloumnName6 = cloumnName6;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public void setPage(long page) {
		this.page = page;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}	

}
