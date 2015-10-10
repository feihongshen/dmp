package cn.explink.test;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;

import cn.explink.enumutil.AccountFlowOrderTypeEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.service.ExportService;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.StreamingStatementCreator;

@Service
public class FinaceService  {
	@Autowired
	FinaceDAO finaceDAO;
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	ExportService exportService;
	@Autowired
	AccountService  accountService;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	
	/**
	 * 导出所有数据明细
	 * @param deliveybranchid
	 * @param deliverystate
	 * @param response
	 * @throws Exception
	 */
	public void download_detail(long deliveybranchid,long deliverystate,HttpServletResponse response) throws Exception{
		String[] cloumnName1 = new String[2]; // 导出的列名
		String[] cloumnName2 = new String[2]; // 导出的英文列名
		String[] cloumnName3 = new String[2]; // 导出的类型

	
		setExcelFields(cloumnName1, cloumnName2, cloumnName3);
		
		
		final String[] cloumnName4 = cloumnName1;
		final String[] cloumnName5 = cloumnName2;
		final String[] cloumnName6 = cloumnName3;
		long maxCount=150000;
		final String sql = finaceDAO.getDownloadDataSql(maxCount,deliveybranchid,deliverystate);
		final long mapid =1;
		 String deliveryName = DeliveryStateEnum.getByValue((int)deliverystate).getText();
		String partName=deliverystate==1?"success":"lose";
		 
		String sheetName = deliveryName+"明细"; // sheet的名称
		String fileName = "OrdersDetail_"+partName+"_"+DateTimeUtil.getNowTime("MMddHHmmss")+ ".xlsx"; // 文件名
		
		exportExcel(sql, cloumnName4, cloumnName5, cloumnName6, mapid, sheetName, fileName,response);
	}
	
	public void setExcelFields(String[] cloumnName1, String[] cloumnName2, String[] cloumnName3) {
		cloumnName1[0] = "订单号";
		cloumnName2[0] = "Cwb";
		cloumnName3[0] = "string";
		cloumnName1[1] = "应收金额";
		cloumnName2[1] = "Businessfee";
		cloumnName3[1] = "double";
	
	}
	
	public void setExcelFields1(String[] cloumnName1, String[] cloumnName2, String[] cloumnName3) {
		cloumnName1[0] = "订单号";
		cloumnName2[0] = "Cwb";
		cloumnName3[0] = "string";
		cloumnName1[1] = "差异汇总";
		cloumnName2[1] = "Businessfee";
		cloumnName3[1] = "double";
	
	}
	
	/**
	 * 导出扣款差异
	 * @param deliveybranchid
	 * @param deliverystate
	 * @param response
	 * @throws Exception
	 */
	public void koukuan_diff(long deliveybranchid,long deliverystate,HttpServletResponse response) throws Exception{
		String[] cloumnName1 = new String[2]; // 导出的列名
		String[] cloumnName2 = new String[2]; // 导出的英文列名
		String[] cloumnName3 = new String[2]; // 导出的类型

	
		setExcelFields(cloumnName1, cloumnName2, cloumnName3);
		
		
		final String[] cloumnName4 = cloumnName1;
		final String[] cloumnName5 = cloumnName2;
		final String[] cloumnName6 = cloumnName3;
		long maxCount=150000;
		List<String> debtList=finaceDAO.getAccountDecuctByType(maxCount, deliveybranchid, AccountFlowOrderTypeEnum.KouKuan.getValue());
		
		final String sql = finaceDAO.getDownloadDataDiffSql(maxCount,deliveybranchid,deliverystate,this.getCwbs(debtList));
		final long mapid =1;
		 String deliveryName = DeliveryStateEnum.getByValue((int)deliverystate).getText();
		 String partName=deliverystate==1?"success":"lose";
		String sheetName = deliveryName+"未扣款差异"; // sheet的名称
		String fileName = "KoukuanDiff_"+partName+"_"+DateTimeUtil.getNowTime("MMddHHmmss")+ ".xlsx"; // 文件名
		
		
		exportExcel(sql, cloumnName4, cloumnName5, cloumnName6, mapid, sheetName, fileName,response);
	}
	
	
	
	/**
	 * 拒收和中转差异  
	 * 存在重复操作多返款情况
	 * @param deliveybranchid
	 * @param flowordertype  8 扣款    7强制出库需返款   11中转    12退货
	 * @param response
	 * @throws Exception
	 */
	public void jushouzhongzhuan_detail(long deliveybranchid,long flowordertype,HttpServletResponse response) throws Exception{
		String[] cloumnName1 = new String[2]; // 导出的列名
		String[] cloumnName2 = new String[2]; // 导出的英文列名
		String[] cloumnName3 = new String[2]; // 导出的类型

	
		setExcelFields(cloumnName1, cloumnName2, cloumnName3);
		
		
		final String[] cloumnName4 = cloumnName1;
		final String[] cloumnName5 = cloumnName2;
		final String[] cloumnName6 = cloumnName3;
		long maxCount=150000;
		final String sql = finaceDAO.getDownloadDataJuShouZhongZhuanSql(maxCount,deliveybranchid,flowordertype);
		final long mapid =1;
		 String deliveryName = AccountFlowOrderTypeEnum.getText(flowordertype).getText();
		 String partName=flowordertype==11?"zhongzhuan":"tuihuo";
		String sheetName = deliveryName+"明细记录"; // sheet的名称
		String fileName = "Detail_"+partName+"_"+DateTimeUtil.getNowTime("MMddHHmmss")+ ".xlsx"; // 文件名
		
		
		exportExcel(sql, cloumnName4, cloumnName5, cloumnName6, mapid, sheetName, fileName,response);
	}
	
	
	/**
	 * 拒收和中转差异  
	 * 存在重复操作多返款情况
	 * @param deliveybranchid
	 * @param flowordertype  
	 * @param response
	 * @throws Exception
	 */
	public void jushouzhongzhuan_diff(long deliveybranchid,long flowordertype,HttpServletResponse response) throws Exception{
		String[] cloumnName1 = new String[2]; // 导出的列名
		String[] cloumnName2 = new String[2]; // 导出的英文列名
		String[] cloumnName3 = new String[2]; // 导出的类型

	
		setExcelFields(cloumnName1, cloumnName2, cloumnName3);
		
		
		final String[] cloumnName4 = cloumnName1;
		final String[] cloumnName5 = cloumnName2;
		final String[] cloumnName6 = cloumnName3;
		long maxCount=150000;
		final String sql = finaceDAO.getDownloadDataJuShouZhongZhuanDiffSql(maxCount,deliveybranchid,flowordertype);
		final long mapid =1;
		 String deliveryName = AccountFlowOrderTypeEnum.getText(flowordertype).getText();
		 String partName=flowordertype==11?"zhongzhuan":"tuihuo";
		String sheetName = deliveryName+"重复返款差异"; // sheet的名称
		String fileName = "ChongfuFankuanDiff_"+partName+"_"+DateTimeUtil.getNowTime("MMddHHmmss")+ ".xlsx"; // 文件名
		
		
		exportExcel(sql, cloumnName4, cloumnName5, cloumnName6, mapid, sheetName, fileName,response);
	}
	
	
	
	
	
	/**
	 *  未扣款+拒收和中转差异  
	 * 存在重复操作多返款情况
	 * @param deliveybranchid
	 * @param flowordertype  
	 * @param response
	 * @throws Exception
	 */
	public void koukuanjushouzhongzhuan_diff(long deliveybranchid,HttpServletResponse response) throws Exception{
		String[] cloumnName1 = new String[2]; // 导出的列名
		String[] cloumnName2 = new String[2]; // 导出的英文列名
		String[] cloumnName3 = new String[2]; // 导出的类型

	
		setExcelFields(cloumnName1, cloumnName2, cloumnName3);
		
		
		final String[] cloumnName4 = cloumnName1;
		final String[] cloumnName5 = cloumnName2;
		final String[] cloumnName6 = cloumnName3;
		long maxCount=150000;
		
		//拿到所有 已扣款记录
		List<String> cwblist = finaceDAO.getAccountDecuctByType(maxCount, deliveybranchid, AccountFlowOrderTypeEnum.KouKuan.getValue());
		String cwbs = getCwbs(cwblist);
		
		//查询出反馈表中不包含已扣款记录的数据
		List<String> deliveryNoKouKuanList = finaceDAO.getDeliveryStateNotInCwbs(cwbs);
		String cwbsErr = getCwbs(deliveryNoKouKuanList);
		//得到最终未扣款并且中转或退货重复
		List<String> resultList= finaceDAO.getZhongzhongTuihuoChongfuCwbList(deliveybranchid, cwbsErr);
		String resultCwbs = getCwbs(resultList);
		
		final String sql = finaceDAO.getDeliveryStateByCwbsSql(resultCwbs);
		final long mapid =1;
		String sheetName = "中转和退货未扣款并重复返款差异"; // sheet的名称
		String fileName = "weikoukuanChongfuFankuanDiff_zhongzhuantuihuo_"+DateTimeUtil.getNowTime("MMddHHmmss")+ ".xlsx"; // 文件名
		
		
		exportExcel(sql, cloumnName4, cloumnName5, cloumnName6, mapid, sheetName, fileName,response);
	}

	private String getCwbs(List<String> cwblist) {
		String cwbArrs="";
		StringBuffer  sub= new StringBuffer();
		for(String cwb:cwblist){
			sub.append("'"+cwb+"',");
		}
		if(sub.indexOf(",")>0){
			cwbArrs=sub.substring(0,sub.length()-1);
		}
		return cwbArrs;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	private void exportExcel(final String sql, final String[] cloumnName4, final String[] cloumnName5, final String[] cloumnName6,
			final long mapid, String sheetName, String fileName,HttpServletResponse response) throws Exception {

		
		FinaceExcelUtils excelUtil = exportExcelHelp(sql, cloumnName4, cloumnName5,cloumnName6);

		excelUtil.invokeExcel(response, cloumnName4, sheetName, fileName);
		
	}
	
	private void exportExcel1(final Map<String,Double> diffMap, final String[] cloumnName4, final String[] cloumnName5, final String[] cloumnName6,
			final long mapid, String sheetName, String fileName,HttpServletResponse response) throws Exception {

		
		FinaceExcelUtils excelUtil = exportExcelHelp1(diffMap, cloumnName4, cloumnName5,cloumnName6);

		excelUtil.invokeExcel(response, cloumnName4, sheetName, fileName);
		
	}

	private FinaceExcelUtils exportExcelHelp(final String sql,
			final String[] cloumnName4, final String[] cloumnName5,
			final String[] cloumnName6) {
		FinaceExcelUtils excelUtil = new FinaceExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
			@Override
			public void fillData(final Sheet sheet, final CellStyle style) {
				
				jdbcTemplate.query(new StreamingStatementCreator(sql), new ResultSetExtractor<Object>() {
					private int count = 0;
					ColumnMapRowMapper columnMapRowMapper = new ColumnMapRowMapper();
					private List<Map<String, Object>> recordbatch = new ArrayList<Map<String, Object>>();

					public void processRow(ResultSet rs) throws SQLException {
						Map<String, Object> mapRow = columnMapRowMapper.mapRow(rs, count);
						recordbatch.add(mapRow);
						count++; 
						if (count % 100 == 0) {
							writeBatch();
						}
					}

					private void writeSingle(Map<String, Object> mapRow, int rownum) throws SQLException {
						Row row = sheet.createRow(rownum + 1);
						row.setHeightInPoints((float) 15);
						for (int i = 0; i < cloumnName4.length; i++) {
							Cell cell = row.createCell((short) i);
							cell.setCellStyle(style);
							// sheet.setColumnWidth(i, (short) (5000));
							// //设置列宽
							Object a = exportService.setObjectFinace(cloumnName5, mapRow, i);
							if (cloumnName6[i].equals("double")) {
								cell.setCellValue(a == null ? BigDecimal.ZERO.doubleValue() : a.equals("") ? BigDecimal.ZERO.doubleValue() : Double.parseDouble(a.toString()));
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
						writeBatch();
						return null;
					}

					public void writeBatch() throws SQLException {
						if (recordbatch.size() > 0) {
							List<String> cwbs = new ArrayList<String>();
							for (Map<String, Object> mapRow : recordbatch) {
								cwbs.add(mapRow.get("cwb").toString());
							}
						
							int size = recordbatch.size();
							for (int i = 0; i < size; i++) {
								String cwb = recordbatch.get(i).get("cwb").toString();
								writeSingle(recordbatch.get(i), count - size + i);
							}
							recordbatch.clear();
						}
					}

					
				});

			}
		};
		return excelUtil;
	}
	
	
	
	
	
	/**
	 * 导出最终的差异订单
	 * @param deliveybranchid
	 * @param deliverystate
	 * @param response
	 * @throws Exception
	 */
	public void download_diff(long deliveybranchid,long flowordertype,double datadiff,HttpServletResponse response) throws Exception{
		String[] cloumnName1 = new String[2]; // 导出的列名
		String[] cloumnName2 = new String[2]; // 导出的英文列名
		String[] cloumnName3 = new String[2]; // 导出的类型

	
		setExcelFields(cloumnName1, cloumnName2, cloumnName3);
		
		
		final String[] cloumnName4 = cloumnName1;
		final String[] cloumnName5 = cloumnName2;
		final String[] cloumnName6 = cloumnName3;
		
		final long mapid =1;
	
		String partName=flowordertype==11?"zhongzhuan":"tuihuo";
		 
		String sheetName = partName+"明细"; // sheet的名称
		String fileName = "OrdersDiffdetail"+DateTimeUtil.getNowTime("MMddHHmmss")+ ".xlsx"; // 文件名
		long starttime = System.currentTimeMillis();
		List<TypeVo> zzttList = finaceDAO.getDeductsListByType(AccountFlowOrderTypeEnum.ZhongZhuan.getValue()+","+AccountFlowOrderTypeEnum.TuiHuo.getValue(), deliveybranchid);
		long endtime = System.currentTimeMillis();
		logger.info("财务统计：导出差异-查询中转和退货记录耗时:"+((endtime - starttime) / 1000));
		
		long starttime1 = System.currentTimeMillis();
		List<String>  cwbList=AccountCalc.getCalcDiffList(datadiff, zzttList);
		long endtime2 = System.currentTimeMillis();
		logger.info("财务统计：导出差异-查询中转和退货随机得出订单耗时:"+((endtime2 - starttime1) / 1000));
		
		String cwbs="-1";
		if(cwbList==null){
			logger.info("财务统计：导出差异-订单不符合条件，不能导出!");
		}else{
			 cwbs=this.getCwbs(cwbList);
		}
		
		String sql="select cwb,receivablefee as businessfee from express_ops_cwb_detail where state=1 and cwb in ("+cwbs+") ";
		logger.info("财务统计：导出差异-查询sql:{}",sql);
		exportExcel(sql, cloumnName4, cloumnName5, cloumnName6, mapid, sheetName, fileName,response);
	}
	
	
	
	/**
	 * 导出扣款和强制出库差异
	 * 存在重复操作多返款情况
	 * @param deliverybranchid
	 * @param flowordertype  8 扣款    7强制出库需返款   11中转    12退货
	 * @param response
	 * @throws Exception
	 */
	public void kkqzckdiffdetail_detail(long deliverybranchid,long flowordertype,HttpServletResponse response) throws Exception{
		String[] cloumnName1 = new String[2]; // 导出的列名
		String[] cloumnName2 = new String[2]; // 导出的英文列名
		String[] cloumnName3 = new String[2]; // 导出的类型

	
		setExcelFields1(cloumnName1, cloumnName2, cloumnName3);
		
		
		final String[] cloumnName4 = cloumnName1;
		final String[] cloumnName5 = cloumnName2;
		final String[] cloumnName6 = cloumnName3;
		
		final long mapid =1;
	
		String sheetName = "扣款强制出库明细记录"; // sheet的名称
		String fileName = "Detail_koukuanqiangzhichuku_diff_"+DateTimeUtil.getNowTime("MMddHHmmss")+ ".xlsx"; // 文件名
		final Map<String,Double> diffMap = accountService.getQiangZhiChuKuKouKuanDiffList(deliverybranchid);
		
		exportExcel1(diffMap, cloumnName4, cloumnName5, cloumnName6, mapid, sheetName, fileName,response);
	}

	
	
	public FinaceExcelUtils exportExcelHelp1(final Map<String,Double> diffMap,
			final String[] cloumnName4, final String[] cloumnName5,
			final String[] cloumnName6) {
		
		FinaceExcelUtils excelUtil = new FinaceExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
			@Override
			public void fillData(Sheet sheet, CellStyle style) {
				int k=0;
				for(String cwb:diffMap.keySet()){
					Row row = sheet.createRow(k + 1);
					row.setHeightInPoints(15);
					for (int i = 0; i < cloumnName4.length; i++) {
						Cell cell = row.createCell((short) i);
						cell.setCellStyle(style);
						cell.setCellValue(i==0?cwb:String.valueOf(diffMap.get(cwb)));
					}
					k++;
				}
		  }
		
		};
		return excelUtil;
	}
	
}
