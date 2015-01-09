package cn.explink.controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Customer;
import cn.explink.domain.SmtFareSettleCondVO;
import cn.explink.domain.SmtFareSettleConstVO;
import cn.explink.domain.SmtFareSettleDetailCondVO;
import cn.explink.domain.SmtFareSettleDetailResultVO;
import cn.explink.domain.SmtFareSettleDetailVO;
import cn.explink.domain.SmtFareSettleResultVO;
import cn.explink.domain.SmtFareSettleVO;
import cn.explink.domain.TimeTypeEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.util.ExcelUtils;

@Controller
@RequestMapping("/smtfaresettle")
public class SmtFareSettleController {

	@Autowired
	private CustomerDAO customerDAO = null;

	@Autowired
	private BranchDAO branchDAO = null;

	@Autowired
	private UserDAO userDAO = null;

	@Autowired
	private JdbcTemplate jdbcTemplate = null;

	@RequestMapping("/station/{page}")
	public ModelAndView showStation(@PathVariable("page") int page, SmtFareSettleCondVO cond, ModelAndView mav) {
		mav.addObject("const", this.getSmtFareSettleConstVO(false, cond));
		mav.addObject("result", this.getStationResultVO(page, cond));
		mav.addObject("cond", cond);
		mav.setViewName("/smtfaresettle/smtfaresettle_station");

		return mav;
	}

	@RequestMapping("/deliver/{page}")
	public ModelAndView showDeliver(@PathVariable("page") int page, SmtFareSettleCondVO cond, ModelAndView mav) {
		mav.addObject("const", this.getSmtFareSettleConstVO(true, cond));
		mav.addObject("result", this.getDeliverResultVO(page, cond));
		mav.addObject("cond", cond);
		mav.setViewName("/smtfaresettle/smtfaresettle_deliver");

		return mav;
	}

	@RequestMapping("/getdeliverdata/{deliverId}/{venderId}")
	@ResponseBody
	public SmtFareSettleVO getDeliverData(@PathVariable("deliverId") long deliverId, @PathVariable("venderId") long venderId, HttpServletRequest request) {
		String strCond = request.getParameter("cond");
		JSONObject obj = JSONObject.fromObject(strCond);
		SmtFareSettleCondVO condVO = new SmtFareSettleCondVO(obj);

		return this.queryDeliverData(deliverId, venderId, condVO);
	}

	@RequestMapping("/getstationdata/{stationId}/{venderId}")
	@ResponseBody
	public SmtFareSettleVO getStationData(@PathVariable("stationId") long stationId, @PathVariable("venderId") long venderId, HttpServletRequest request) {
		String strCond = request.getParameter("cond");
		JSONObject obj = JSONObject.fromObject(strCond);
		SmtFareSettleCondVO condVO = new SmtFareSettleCondVO(obj);

		return this.queryStationData(stationId, venderId, condVO);
	}

	@RequestMapping("/getstationdeliver")
	@ResponseBody
	public Map<Long, String> getStationDeliver(HttpServletRequest request) {
		String statonIds = request.getParameter("stationIds");

		return this.getUserDAO().getDeliverNameMapByBranch(statonIds);
	}

	@RequestMapping("/detail_d/{page}")
	public ModelAndView showDeliverDetail(@PathVariable("page") int page, SmtFareSettleDetailCondVO condVO) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("cond", condVO);
		mav.addObject("result", this.getDeliverDetialResult(page, condVO));
		mav.setViewName("/smtfaresettle/smtfaresettle_detail");

		return mav;

	}

	@RequestMapping("/detail_s/{page}")
	public ModelAndView showStationDetail(@PathVariable("page") int page, SmtFareSettleDetailCondVO condVO) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("cond", condVO);
		mav.addObject("result", this.getStationDetialResult(page, condVO));
		mav.setViewName("/smtfaresettle/smtfaresettle_detail");

		return mav;
	}

	@RequestMapping("/export/detail_s")
	public void exportStationDetial(SmtFareSettleDetailCondVO condVO, HttpServletResponse response) throws Exception {
		String sql = this.getDetialSql(-1, condVO, false, false);
		Object[] paras = new Object[] { condVO.getStationId(), condVO.getVenderId() };
		List<SmtFareSettleDetailVO> result = this.queryDetailResult(sql, paras);
		ExportDetailUtil util = new ExportDetailUtil(result);
		util.export(response, "sheet1", "配送明细.xls");
	}

	@RequestMapping("/export/detail_d")
	public void exportDeliverDetial(SmtFareSettleDetailCondVO condVO, HttpServletResponse response) throws Exception {
		String sql = this.getDetialSql(-1, condVO, true, false);
		Object[] paras = new Object[] { condVO.getDeliverId(), condVO.getVenderId() };
		List<SmtFareSettleDetailVO> result = this.queryDetailResult(sql, paras);
		ExportDetailUtil util = new ExportDetailUtil(result);
		util.export(response, "sheet1", "配送明细.xlsx");
	}

	private SmtFareSettleDetailResultVO getStationDetialResult(int page, SmtFareSettleDetailCondVO condVO) {
		String sql = this.getDetialSql(page, condVO, false, true);
		String cntSql = this.getDetailCntSql(condVO, false);
		Object[] paras = new Object[] { condVO.getStationId(), condVO.getVenderId() };
		SmtFareSettleDetailResultVO result = this.queryDetailResult(page, sql, cntSql, paras);
		result.setDetail("detail_s");

		return result;
	}

	private SmtFareSettleDetailResultVO getDeliverDetialResult(int page, SmtFareSettleDetailCondVO condVO) {
		String sql = this.getDetialSql(page, condVO, true, true);
		String cntSql = this.getDetailCntSql(condVO, true);
		Object[] paras = new Object[] { condVO.getDeliverId(), condVO.getVenderId() };
		SmtFareSettleDetailResultVO result = this.queryDetailResult(page, sql, cntSql, paras);
		result.setDetail("detail_d");

		return result;
	}

	private SmtFareSettleDetailResultVO queryDetailResult(int page, String sql, String cntSql, Object[] paras) {
		List<SmtFareSettleDetailVO> dtList = this.getJdbcTemplate().query(sql, paras, new DetailRowMapper());
		int totalCnt = this.getJdbcTemplate().queryForInt(cntSql, paras);
		SmtFareSettleDetailResultVO result = new SmtFareSettleDetailResultVO();
		result.setPage(page);
		int pageCnt = (totalCnt % 10) == 0 ? totalCnt / 10 : (totalCnt / 10) + 1;
		result.setPageCount(pageCnt);
		result.setCount(totalCnt);
		this.fillExtraInfo(dtList);
		result.setResultList(dtList);

		return result;
	}

	private List<SmtFareSettleDetailVO> queryDetailResult(String sql, Object[] paras) {
		List<SmtFareSettleDetailVO> dtList = this.getJdbcTemplate().query(sql, paras, new DetailRowMapper());
		this.fillExtraInfo(dtList);

		return dtList;
	}

	private void fillExtraInfo(List<SmtFareSettleDetailVO> dtList) {
		if (dtList.isEmpty()) {
			return;
		}
		Map<Long, String> stationNameMap = this.getStationNameMap(dtList);
		Map<Long, String> deliverNameMap = this.getDeliverNameMap(dtList);
		for (SmtFareSettleDetailVO dt : dtList) {
			dt.setStationName(stationNameMap.get(dt.getStationId()));
			dt.setDeliverName(deliverNameMap.get(dt.getDeliverId()));
		}
	}

	private Map<Long, String> getStationNameMap(List<SmtFareSettleDetailVO> dtList) {
		Set<Long> stationIdSet = this.getStationIdSet(dtList);

		return this.getBranchDAO().getBranchNameMap(stationIdSet);
	}

	private Set<Long> getStationIdSet(List<SmtFareSettleDetailVO> dtList) {
		Set<Long> stationIdSet = new HashSet<Long>();
		for (SmtFareSettleDetailVO dt : dtList) {
			stationIdSet.add(dt.getStationId());
		}
		return stationIdSet;
	}

	private Map<Long, String> getDeliverNameMap(List<SmtFareSettleDetailVO> dtList) {
		Set<Long> deliverIdSet = this.getDeliverIdSet(dtList);

		return this.getUserDAO().getUserNameMap(deliverIdSet);
	}

	private Set<Long> getDeliverIdSet(List<SmtFareSettleDetailVO> dtList) {
		Set<Long> deliverIdSet = new HashSet<Long>();
		for (SmtFareSettleDetailVO dt : dtList) {
			deliverIdSet.add(dt.getDeliverId());
		}
		return deliverIdSet;
	}

	private String getDetialSql(int page, SmtFareSettleDetailCondVO condVO, boolean deliver, boolean limit) {
		StringBuilder sql = new StringBuilder();
		sql.append("select deliver_station_id , deliver_id , cwb , should_fee , received_fee , pay_type , feedback_result , feedback_time ,system_accept_time , create_time ");
		sql.append("from express_ops_smt_cwb_opt_time ");
		if (deliver) {
			sql.append("where deliver_id = ? ");
		} else {
			sql.append("where deliver_station_id = ? ");
		}
		sql.append("and vender_id = ? ");
		sql.append("and " + this.getTimeWhereCond(condVO));
		if (limit) {
			sql.append(" limit " + ((page - 1) * 10) + ",10");
		}

		return sql.toString();
	}

	private String getDetailCntSql(SmtFareSettleDetailCondVO condVO, boolean deliver) {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(cwb) ");
		sql.append("from express_ops_smt_cwb_opt_time ");
		if (deliver) {
			sql.append("where deliver_id = ? ");
		} else {
			sql.append("where deliver_station_id = ? ");
		}
		sql.append("and vender_id = ? ");
		sql.append("and " + this.getTimeWhereCond(condVO));

		return sql.toString();
	}

	private SmtFareSettleVO queryDeliverData(long deliverId, long venderId, SmtFareSettleCondVO condVO) {
		SmtFareSettleVO resultVO = new SmtFareSettleVO();
		resultVO.setDeliverId(deliverId);
		resultVO.setVenderId(venderId);
		String sql = this.getDeliverSql(condVO);
		Object[] args = new Object[] { deliverId, venderId };
		this.getJdbcTemplate().query(sql, args, new DeliverRowHander(resultVO));

		return resultVO;
	}

	private SmtFareSettleVO queryStationData(long stationId, long venderId, SmtFareSettleCondVO condVO) {
		SmtFareSettleVO resultVO = new SmtFareSettleVO();
		resultVO.setStationId(stationId);
		resultVO.setVenderId(venderId);
		String sql = this.getStationSql(condVO);
		Object[] args = new Object[] { stationId, venderId };
		this.getJdbcTemplate().query(sql, args, new StationRowHander(resultVO));

		return resultVO;
	}

	private SmtFareSettleResultVO getStationResultVO(int page, SmtFareSettleCondVO cond) {
		SmtFareSettleResultVO resultVO = new SmtFareSettleResultVO();
		if (cond.getStartTime().isEmpty()) {
			return resultVO;
		}
		PairList<Long, Long> pairList = this.getStationPairList(page, cond, resultVO);
		Set<Long> stationIdSet = new HashSet<Long>(pairList.getV1List());
		Set<Long> customerIdSet = new HashSet<Long>(pairList.getV2List());
		Map<Long, String> stationMap = this.getBranchDAO().getBranchNameMap(stationIdSet);
		Map<Long, String> customerMap = this.getCustomerDAO().getCustomerNameMap(customerIdSet);
		for (Pair<Long, Long> pair : pairList) {
			String stationName = stationMap.get(pair.getV1());
			String venderName = customerMap.get(pair.getV2());
			resultVO.addStationResult(pair.getV1(), stationName, pair.getV2(), venderName);
		}
		return resultVO;
	}

	private PairList<Long, Long> getStationPairList(int page, SmtFareSettleCondVO cond, SmtFareSettleResultVO resultVO) {
		PairList<Long, Long> pairList = new PairList<Long, Long>();
		List<Long> stationIdList = this.getStationIdList(cond);
		List<Long> customerIdList = this.getCustomerIdList(cond);
		this.fillPairList(page, stationIdList, customerIdList, pairList, resultVO);

		return pairList;
	}

	private PairList<Long, Long> getDeliverPairList(int page, SmtFareSettleCondVO cond, SmtFareSettleResultVO resultVO) {
		PairList<Long, Long> pairList = new PairList<Long, Long>();
		List<Long> deliverIdList = this.getDeliverIdList(cond);
		List<Long> customerIdList = this.getCustomerIdList(cond);
		this.fillPairList(page, deliverIdList, customerIdList, pairList, resultVO);

		return pairList;
	}

	private void fillPairList(int page, List<Long> idList, List<Long> customerIdList, PairList<Long, Long> pairList, SmtFareSettleResultVO resultVO) {
		int stationCnt = idList.size();
		int customerCnt = customerIdList.size();
		int pageCount = (page - 1) * 10;
		int dataCount = stationCnt * customerCnt;
		if (pageCount >= dataCount) {
			return;
		}
		PairList<Long, Long> allPairList = this.getPairList(idList, customerIdList);
		int end = (pageCount + 10) <= allPairList.size() ? (pageCount + 10) : allPairList.size();
		pairList.addAll(allPairList.subList(pageCount, end));

		resultVO.setCount(dataCount);
		resultVO.setPage(page);
		resultVO.setPageCount((dataCount % 10) == 0 ? dataCount / 10 : (dataCount / 10) + 1);
	}

	private PairList<Long, Long> getPairList(List<Long> stationIdList, List<Long> customerIdList) {
		PairList<Long, Long> pairList = new PairList<Long, Long>();
		for (Long stationId : stationIdList) {
			for (Long customerId : customerIdList) {
				pairList.add(stationId, customerId);
			}
		}
		return pairList;
	}

	private List<Long> getStationIdList(SmtFareSettleCondVO cond) {
		List<Long> stationIdList = cond.getOrgs();
		if (!stationIdList.isEmpty()) {
			return stationIdList;
		}
		return this.getBranchDAO().getAllBranchId();
	}

	private List<Long> getDeliverIdList(SmtFareSettleCondVO cond) {
		long deliverId = cond.getDeliverId();
		if (deliverId != 0) {
			List<Long> deliverIdList = new ArrayList<Long>();
			deliverIdList.add(deliverId);
			return deliverIdList;
		}
		Set<Long> branchIdSet = this.getBranchIdSet(cond);
		if (branchIdSet.isEmpty()) {
			return this.getUserDAO().getAllDeliverId();
		}
		return this.getUserDAO().getBranchDeliverId(branchIdSet);
	}

	private Set<Long> getBranchIdSet(SmtFareSettleCondVO cond) {
		List<Long> orgIdList = cond.getOrgs();
		Set<Long> orgIdSet = new HashSet<Long>(orgIdList);

		return orgIdSet;
	}

	private List<Long> getCustomerIdList(SmtFareSettleCondVO cond) {
		List<Long> venderIdList = cond.getVenders();
		if (!venderIdList.isEmpty()) {
			return venderIdList;
		}
		return this.getCustomerDAO().getAllCustomerId();
	}

	private String getDeliverSql(SmtFareSettleCondVO cond) {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(cwb) total,count(feedback_time) success ,sum(should_fee) should_fee,sum(received_fee) received_fee from express_ops_smt_cwb_opt_time ");
		sql.append("where deliver_id = ? and vender_id = ? and ");
		sql.append(this.getTimeWhereCond(cond));

		return sql.toString();
	}

	private String getStationSql(SmtFareSettleCondVO cond) {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(cwb) total,count(feedback_time) success ,sum(should_fee) should_fee,sum(received_fee) received_fee from express_ops_smt_cwb_opt_time ");
		sql.append("where deliver_station_id = ? and vender_id = ? and ");
		sql.append(this.getTimeWhereCond(cond));

		return sql.toString();
	}

	private String getTimeWhereCond(SmtFareSettleCondVO cond) {
		return this.getTimeWhereCond(cond.getOptTimeType(), cond.getStartTime(), cond.getEndTime());
	}

	private String getTimeWhereCond(SmtFareSettleDetailCondVO cond) {
		return this.getTimeWhereCond(cond.getOptTimeType(), cond.getStartTime(), cond.getEndTime());
	}

	private String getTimeWhereCond(int index, String startTime, String endTime) {
		TimeTypeEnum timeType = TimeTypeEnum.values()[index];
		StringBuilder whereCond = new StringBuilder();
		whereCond.append(timeType.getField());
		whereCond.append(" >= '" + startTime + "' ");
		whereCond.append("and " + timeType.getField() + " <= '" + endTime + "'");

		return whereCond.toString();
	}

	private SmtFareSettleResultVO getDeliverResultVO(int page, SmtFareSettleCondVO cond) {
		SmtFareSettleResultVO resultVO = new SmtFareSettleResultVO();
		if (cond.getStartTime().isEmpty()) {
			return resultVO;
		}
		PairList<Long, Long> pairList = this.getDeliverPairList(page, cond, resultVO);
		Set<Long> deliverIdSet = new HashSet<Long>(pairList.getV1List());
		Set<Long> customerIdSet = new HashSet<Long>(pairList.getV2List());
		Map<Long, String> userMap = this.getUserDAO().getUserNameMap(deliverIdSet);
		Map<Long, String> customerMap = this.getCustomerDAO().getCustomerNameMap(customerIdSet);

		for (Pair<Long, Long> pair : pairList) {
			String userName = userMap.get(pair.getV1());
			String venderName = customerMap.get(pair.getV2());
			resultVO.addDeliverResult(pair.getV1(), userName, pair.getV2(), venderName);
		}
		return resultVO;
	}

	private SmtFareSettleConstVO getSmtFareSettleConstVO(boolean deliver, SmtFareSettleCondVO cond) {
		SmtFareSettleConstVO constantVO = new SmtFareSettleConstVO();
		constantVO.setTimeTypeMap(this.getTimeTypeMap());
		constantVO.setTimeType(TimeTypeEnum.SystemAcceptTime.ordinal());
		constantVO.setVenderMap(this.getVenderMap());
		constantVO.setOrgMap(this.getOrgMap());
		if (deliver) {
			constantVO.setDeliverMap(this.getDeliverNameMap(cond));
		}
		return constantVO;
	}

	private Map<Integer, String> getTimeTypeMap() {
		Map<Integer, String> timeTypeMap = new LinkedHashMap<Integer, String>();
		timeTypeMap.put(TimeTypeEnum.OrderCreateTime.ordinal(), TimeTypeEnum.OrderCreateTime.getName());
		timeTypeMap.put(TimeTypeEnum.SystemAcceptTime.ordinal(), TimeTypeEnum.SystemAcceptTime.getName());

		return timeTypeMap;
	}

	private Map<Long, String> getVenderMap() {
		Map<Long, String> venderMap = new LinkedHashMap<Long, String>();
		List<Customer> customerList = this.getCustomerDAO().getAllCustomers();
		for (Customer customer : customerList) {
			venderMap.put(customer.getCustomerid(), customer.getCustomername());
		}
		return venderMap;
	}

	private CustomerDAO getCustomerDAO() {
		return this.customerDAO;
	}

	private JdbcTemplate getJdbcTemplate() {
		return this.jdbcTemplate;
	}

	private Map<Long, String> getOrgMap() {
		return this.getBranchDAO().getBranchNameMap(2);
	}

	private Map<Long, String> getDeliverNameMap(SmtFareSettleCondVO cond) {
		List<Long> stationIdList = cond.getOrgs();
		if (stationIdList.isEmpty()) {
			return new HashMap<Long, String>();
		}
		return this.getUserDAO().getDeliverNameMapByBranch(new HashSet<Long>(stationIdList));
	}

	private BranchDAO getBranchDAO() {
		return this.branchDAO;
	}

	private UserDAO getUserDAO() {
		return this.userDAO;
	}

	private class PairList<K, V> extends ArrayList<Pair<K, V>> {

		private static final long serialVersionUID = 8855045155045335560L;

		public void add(K v1, V v2) {
			this.add(new Pair<K, V>(v1, v2));
		}

		public List<K> getV1List() {
			List<K> v1List = new ArrayList<K>();
			for (Pair<K, V> tmp : this) {
				v1List.add(tmp.getV1());
			}
			return v1List;
		}

		public List<V> getV2List() {
			List<V> v2List = new ArrayList<V>();
			for (Pair<K, V> tmp : this) {
				v2List.add(tmp.getV2());
			}
			return v2List;
		}
	}

	private class Pair<K, V> {
		private K v1 = null;

		private V v2 = null;

		public Pair(K k, V v) {
			this.v1 = k;
			this.v2 = v;
		}

		public K getV1() {
			return this.v1;
		}

		public V getV2() {
			return this.v2;
		}
	}

	private class DeliverRowHander implements RowCallbackHandler {

		private SmtFareSettleVO vo = null;

		public DeliverRowHander(SmtFareSettleVO vo) {
			this.vo = vo;
		}

		@Override
		public void processRow(ResultSet rs) throws SQLException {
			this.getVo().setDeliverPickingCnt(rs.getString("total"));
			this.getVo().setSmtSuccessedCnt(rs.getString("success"));
			this.getVo().setShouldFee(this.formatDecimal(rs.getDouble("should_fee")));
			this.getVo().setReceivedFee(this.formatDecimal(rs.getDouble("received_fee")));
		}

		private SmtFareSettleVO getVo() {
			return this.vo;
		}

		public String formatDecimal(double value) {
			return SmtFareSettleController.this.formatDecimal(value);
		}

	}

	private class StationRowHander implements RowCallbackHandler {

		private SmtFareSettleVO vo = null;

		public StationRowHander(SmtFareSettleVO vo) {
			this.vo = vo;
		}

		@Override
		public void processRow(ResultSet rs) throws SQLException {
			this.getVo().setStationAcceptCnt(rs.getString("total"));
			this.getVo().setSmtSuccessedCnt(rs.getString("success"));
			this.getVo().setShouldFee(this.formatDecimal(rs.getDouble("should_fee")));
			this.getVo().setReceivedFee(this.formatDecimal(rs.getDouble("received_fee")));
		}

		private SmtFareSettleVO getVo() {
			return this.vo;
		}

		public String formatDecimal(double value) {
			return SmtFareSettleController.this.formatDecimal(value);
		}

	}

	public String formatDecimal(double value) {
		DecimalFormat decimal = new DecimalFormat("#.##");
		return decimal.format(value);
	}

	private class DetailRowMapper implements RowMapper<SmtFareSettleDetailVO> {

		@Override
		public SmtFareSettleDetailVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			SmtFareSettleDetailVO dtVO = new SmtFareSettleDetailVO();
			dtVO.setStationId(rs.getLong("deliver_station_id"));
			dtVO.setDeliverId(rs.getLong("deliver_id"));
			dtVO.setCwb(rs.getString("cwb"));
			dtVO.setShouldFee(SmtFareSettleController.this.formatDecimal(rs.getDouble("should_fee")));
			dtVO.setReceivedFee(SmtFareSettleController.this.formatDecimal(rs.getDouble("received_fee")));
			dtVO.setPayType(PaytypeEnum.values()[rs.getInt("pay_type")].getText());
			dtVO.setFeedbackResult(DeliveryStateEnum.values()[rs.getInt("feedback_result")].getText());
			dtVO.setFeedbackTime(rs.getString("feedback_time"));
			dtVO.setSystemAcceptTime(rs.getString("system_accept_time"));
			dtVO.setCreateTime(rs.getString("create_time"));

			return dtVO;
		}
	}

	private class ExportDetailUtil extends ExcelUtils {

		private List<SmtFareSettleDetailVO> dtList = null;

		public ExportDetailUtil(List<SmtFareSettleDetailVO> dtList) {
			this.dtList = dtList;
		}

		public void export(HttpServletResponse response, String sheetName, String fileName) throws Exception {
			super.excel(response, this.getColNames(), sheetName, fileName);
		}

		@Override
		public void fillData(Sheet sheet, CellStyle style) {
			int rowNumber = 1;
			for (SmtFareSettleDetailVO dt : this.getDtList()) {
				this.createRow(sheet, dt, rowNumber++);
			}
		}

		private void createRow(Sheet sheet, SmtFareSettleDetailVO dt, int rowNum) {
			Row row = sheet.createRow(rowNum);
			Cell cell = row.createCell(0);
			cell.setCellValue(dt.getStationName());

			cell = row.createCell(1);
			cell.setCellValue(dt.getDeliverName());

			cell = row.createCell(2);
			cell.setCellValue(dt.getCwb());

			cell = row.createCell(3);
			cell.setCellValue(dt.getShouldFee());

			cell = row.createCell(4);
			cell.setCellValue(dt.getReceivedFee());

			cell = row.createCell(5);
			cell.setCellValue(dt.getPayType());

			cell = row.createCell(6);
			cell.setCellValue(dt.getFeedbackResult());

			cell = row.createCell(7);
			cell.setCellValue(dt.getFeedbackTime());

			cell = row.createCell(8);
			cell.setCellValue(dt.getSystemAcceptTime());

			cell = row.createCell(9);
			cell.setCellValue(dt.getCreateTime());
		}

		private String[] getColNames() {
			String[] colNames = new String[DetailColEnum.values().length];
			for (int i = 0; i < DetailColEnum.values().length; i++) {
				colNames[i] = DetailColEnum.values()[i].getName();
			}
			return colNames;
		}

		private List<SmtFareSettleDetailVO> getDtList() {
			return this.dtList;
		}

	}

	private enum DetailColEnum {
		DeliverStation("配送站点"), Delvier("小件员"), Cwb("订单号"), ShouldFee("应收运费"), ReceivedFee("实收运费"), PayType("支付方式"), FeedbackResult("反馈结果"), FeedbackTime("反馈时间"), SystemAcceptTime("系统接收时间"), CreateTime(
				"创建时间");

		String name;

		DetailColEnum(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}
	}
}
