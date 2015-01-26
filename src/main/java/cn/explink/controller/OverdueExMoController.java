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
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.OverdueExMoDAO;
import cn.explink.dao.TimeEffectiveDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.domain.OverdueBranchResultVO;
import cn.explink.domain.OverdueConstantVO;
import cn.explink.domain.OverdueExMoCondVO;
import cn.explink.domain.OverdueExMoDetailCondVO;
import cn.explink.domain.OverdueExMoDetailResultVO;
import cn.explink.domain.OverdueExMoDetailVO;
import cn.explink.domain.OverdueResultVO;
import cn.explink.domain.TimeEffectiveVO;
import cn.explink.domain.TimeTypeEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.util.ExcelUtils;

/**
 * 超期异常监控控制器.
 *
 * @author zhaoshb
 * @since DMP3.0
 */
@Controller
@RequestMapping("/overdueexmo")
public class OverdueExMoController {

	@Autowired
	private CustomerDAO customerDAO = null;

	@Autowired
	private BranchDAO branchDAO = null;

	@Autowired
	private OverdueExMoDAO overdueExMoDAO = null;

	@Autowired
	private JdbcTemplate jdbcTemplate = null;

	@Autowired
	private TimeEffectiveDAO timeEffectiveVO = null;

	@RequestMapping("/{page}")
	public ModelAndView list(@PathVariable("page") int page, OverdueExMoCondVO condVO) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("cond", condVO);
		mav.addObject("constant", this.getOverdueConstantVO());
		mav.addObject("result", this.getOverdueResultVO(condVO, page));
		mav.setViewName("/orverdueexmo/overdueexmo");

		return mav;
	}

	@RequestMapping("/getbranchdata/{branch_id}/{vender_id}")
	@ResponseBody
	public OverdueBranchResultVO showBranchInfo(@PathVariable("branch_id") long branchId, @PathVariable("vender_id") long venderId, HttpServletRequest request) {
		String jsonCond = request.getParameter("cond");
		JSONObject jsonObject = JSONObject.fromObject(jsonCond);
		OverdueExMoCondVO cond = new OverdueExMoCondVO(jsonObject);
		List<Integer> showColList = cond.getShowCols();
		OverdueBranchResultVO resultVO = new OverdueBranchResultVO(branchId);
		if (showColList.isEmpty()) {
			return resultVO;
		}
		List<TDCell> resultList = this.getShowResult(branchId, venderId, cond);
		resultVO.setResultList(resultList);

		return resultVO;
	}

	@RequestMapping("/exportdata")
	public void exportData(OverdueExMoCondVO condVO, HttpServletResponse response) throws Exception {
		if (condVO.getOrgs().isEmpty() || (condVO.getVenderId() == 0)) {
			return;
		}
		long venderId = condVO.getVenderId();
		String venderName = this.getCustomerDAO().getCustomerName(venderId);

		Set<Long> stationIdSet = new HashSet<Long>(condVO.getOrgs());
		Map<Long, String> stationNameMap = this.getBranchDAO().getBranchNameMap(stationIdSet);

		List<List<Object>> dataList = new ArrayList<List<Object>>();
		for (Long orgId : condVO.getOrgs()) {
			List<Object> resultList = new ArrayList<Object>();
			resultList.add(stationNameMap.get(orgId));
			resultList.add(venderName);
			resultList.addAll(this.getShowResult(orgId, venderId, condVO));

			dataList.add(resultList);
		}
		new ExportDataUtils(response, condVO, dataList).exportData();
	}

	@RequestMapping("/showdetail/{page}")
	public ModelAndView showDetial(@PathVariable("page") int page, ModelAndView mav, OverdueExMoDetailCondVO condVO) {
		mav.addObject("cond", condVO);
		mav.addObject("result", this.getDetailResult(condVO, page));
		mav.setViewName("/orverdueexmo/overdueexmo_detail");

		return mav;
	}

	@RequestMapping("/exportdetail")
	public void exportDetail(HttpServletResponse response, OverdueExMoDetailCondVO condVO) throws Exception {
		Branch branch = this.getBranchDAO().getBranchByBranchid(condVO.getOrgId());
		Map<ShowColEnum, TimeEffectiveVO> teMap = this.queryTimeEffectiveMap(condVO);
		List<OverdueExMoDetailVO> dtList = this.queryDetail(branch, condVO, -1, teMap);
		ExportDetailUtil util = new ExportDetailUtil(response, dtList);
		util.export();
	}

	private OverdueExMoDetailResultVO getDetailResult(OverdueExMoDetailCondVO condVO, int page) {
		Branch branch = this.getBranchDAO().getBranchByBranchid(condVO.getOrgId());
		Map<ShowColEnum, TimeEffectiveVO> teMap = this.queryTimeEffectiveMap(condVO);

		OverdueExMoDetailResultVO result = new OverdueExMoDetailResultVO();
		List<OverdueExMoDetailVO> dtList = this.queryDetail(branch, condVO, page, teMap);
		result.setResultList(dtList);

		this.fillPageInfo(branch, page, result, condVO, teMap);

		return result;
	}

	private void fillPageInfo(Branch branch, int page, OverdueExMoDetailResultVO result, OverdueExMoDetailCondVO condVO, Map<ShowColEnum, TimeEffectiveVO> teMap) {
		result.setPage(page);
		String cntSql = this.getQueryDetailCountSql(branch, condVO, teMap);
		Object[] paras = new Object[] { condVO.getOrgId(), condVO.getVenderId() };
		int total = this.getJdbcTemplate().queryForInt(cntSql, paras);
		result.setCount(total);
		result.setPageCount((total % 10) == 0 ? total / 10 : (total / 10) + 1);
	}

	private List<OverdueExMoDetailVO> queryDetail(Branch branch, OverdueExMoDetailCondVO condVO, int page, Map<ShowColEnum, TimeEffectiveVO> teMap) {
		String sql = this.getQueryDetailSql(branch, condVO, teMap, page);
		Object[] paras = new Object[] { condVO.getOrgId(), condVO.getVenderId() };
		List<OverdueExMoDetailVO> dtList = this.getJdbcTemplate().query(sql, paras, new DetailRowMapper());
		this.fillExtraInfo(dtList, condVO);

		return dtList;
	}

	private void fillExtraInfo(List<OverdueExMoDetailVO> dtList, OverdueExMoDetailCondVO condVO) {
		if (dtList.isEmpty()) {
			return;
		}
		String venderName = this.getVenderName(condVO);
		Set<Long> orgIdSet = this.getOrgIdSet(dtList);
		Map<Long, String> orgNameMap = this.getBranchDAO().getBranchNameMap(orgIdSet);
		for (OverdueExMoDetailVO dt : dtList) {
			dt.setDeliverStationName(this.getOrgName(orgNameMap, dt.getDeliverStationId()));
			dt.setWarehouseName(this.getOrgName(orgNameMap, dt.getWarehouseId()));
			dt.setVenderName(venderName);
		}
	}

	private String getVenderName(OverdueExMoDetailCondVO condVO) {
		return this.getCustomerDAO().getCustomerName(condVO.getVenderId());
	}

	private String getOrgName(Map<Long, String> orgNameMap, long orgId) {
		if (!orgNameMap.containsKey(orgId)) {
			return "";
		}
		return orgNameMap.get(orgId);
	}

	private Set<Long> getOrgIdSet(List<OverdueExMoDetailVO> dtList) {
		Set<Long> orgIdSet = new HashSet<Long>();
		for (OverdueExMoDetailVO dt : dtList) {
			orgIdSet.add(dt.getDeliverStationId());
			orgIdSet.add(dt.getWarehouseId());
		}
		return orgIdSet;
	}

	private String getDbField(int showColIndex) {
		ShowColEnum showColEnum = ShowColEnum.values()[showColIndex];
		switch (showColEnum) {
		case Dispatch:
			return "dispatch_time";
		case GetBack:
			return "feedback_time";
		case NotMatched:
			return "station_accept_time";
		case OutAreaTransfer:
			return "exception_match_time";
		case Print:
			return "print_time";
		case RptOutArea:
			return "report_outarea_time";
		case StationAccept:
			return "station_accept_time";
		case SystemAccept:
			return "system_accept_time";
		default:
			break;
		}
		return "";
	}

	private class DetailRowMapper implements RowMapper<OverdueExMoDetailVO> {

		@Override
		public OverdueExMoDetailVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			OverdueExMoDetailVO vo = new OverdueExMoDetailVO();
			vo.setCwb(rs.getString("cwb"));
			vo.setCreateDate(rs.getString("create_time"));
			vo.setDeliverState(DeliveryStateEnum.values()[rs.getInt("deliver_state")].getText());
			vo.setDeliverStationId(rs.getLong("deliver_station_id"));
			vo.setWarehouseId(rs.getLong("warehouse_id"));

			return vo;
		}
	}

	private String getQueryDetailSql(Branch branch, OverdueExMoDetailCondVO condVO, Map<ShowColEnum, TimeEffectiveVO> teMap, int page) {
		StringBuilder sql = new StringBuilder();
		sql.append("select cwb , create_time , deliver_state , warehouse_id , deliver_station_id from express_ops_smt_cwb_opt_time ");
		sql.append("where ");
		sql.append(this.getOrgWhereCond(condVO));
		sql.append("and vender_id = ? and ");
		sql.append(this.getTimeTypeWhereCond(condVO));
		sql.append(" and ");
		sql.append(this.getQueryDetailTimeEffectiveCond(condVO, teMap));
		if (page != -1) {
			int start = (page - 1) * 10;
			sql.append(" limit " + start + " , " + 10);
		}

		return sql.toString();
	}

	private String getOrgWhereCond(OverdueExMoDetailCondVO condVO) {
		ShowColEnum showColEnum = ShowColEnum.values()[condVO.getShowColIndex()];
		switch (showColEnum) {
		case Dispatch:
		case GetBack:
		case Print:
		case RptOutArea:
		case StationAccept:
			return "deliver_station_id = ? ";
		case SystemAccept:
		case NotMatched:
		case OutAreaTransfer:
			return "warehouse_id = ? ";
		default: {
			return null;
		}
		}
	}

	private String getQueryDetailCountSql(Branch branch, OverdueExMoDetailCondVO condVO, Map<ShowColEnum, TimeEffectiveVO> teMap) {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(cwb) from express_ops_smt_cwb_opt_time ");
		sql.append("where ");
		sql.append(this.getOrgWhereCond(condVO));
		sql.append("and vender_id = ? and ");
		sql.append(this.getTimeTypeWhereCond(condVO));
		sql.append(" and ");
		sql.append(this.getQueryDetailTimeEffectiveCond(condVO, teMap));

		return sql.toString();
	}

	private String getQueryDetailTimeEffectiveCond(OverdueExMoDetailCondVO condVO, Map<ShowColEnum, TimeEffectiveVO> teMap) {
		StringBuilder sql = new StringBuilder();
		String field = this.getDbField(condVO.getShowColIndex());
		if (field.equals("feedback_time")) {
			sql.append("deliver_state = 2 and dispatch_time != '0000-00-00 00:00:00'");
		} else {
			sql.append(field + "!= '0000-00-00 00:00:00' ");
		}

		if (condVO.isEnableTEQuery()) {
			TimeEffectiveVO teVO = teMap.get(ShowColEnum.values()[condVO.getShowColIndex()]);
			if (teVO != null) {
				String subField = teVO.getTimeType().getField();
				sql.append(" and (unix_timestamp(" + field + ") - unix_timestamp(" + subField + ")) <=" + teVO.getScope());
			}
		}
		return sql.toString();
	}

	protected String getTimeTypeWhereCond(OverdueExMoDetailCondVO condVO) {
		String startTime = condVO.getStartTime();
		String endTime = condVO.getEndTime();
		TimeTypeEnum timeType = TimeTypeEnum.values()[condVO.getOptTimeType()];
		StringBuilder whereCond = new StringBuilder();
		String field = null;
		if (TimeTypeEnum.SystemAcceptTime.equals(timeType)) {
			field = "system_accept_time";
		} else {
			field = "create_time";
		}
		whereCond.append(field + " >= '" + startTime + "'");
		whereCond.append(" and " + field + " <= '" + endTime + "'");

		return whereCond.toString();
	}

	private List<TDCell> getShowResult(long branchId, Long venderId, OverdueExMoCondVO condVO) {
		Branch branch = this.getBranchDAO().getBranchByBranchid(branchId);
		List<Integer> showColList = condVO.getShowCols();
		Map<ShowColEnum, TimeEffectiveVO> teMap = this.queryTimeEffectiveMap(condVO);
		List<TDCell> resultList = new ArrayList<TDCell>();
		for (Integer showColIndex : showColList) {
			resultList.add(this.loadShowData(branch, venderId, showColIndex, condVO, teMap));
		}
		return resultList;
	}

	private Map<ShowColEnum, TimeEffectiveVO> queryTimeEffectiveMap(OverdueExMoCondVO cond) {
		Map<String, TimeEffectiveVO> teCodeMap = new HashMap<String, TimeEffectiveVO>();
		Map<ShowColEnum, TimeEffectiveVO> teMap = new HashMap<ShowColEnum, TimeEffectiveVO>();
		if (cond.isEnableTEQuery()) {
			List<TimeEffectiveVO> teList = this.getTimeEffectiveVO().getAllTimeEffectiveVO();
			for (TimeEffectiveVO te : teList) {
				teCodeMap.put(te.getCode(), te);
			}
			// 打印时效.
			teMap.put(ShowColEnum.Print, teCodeMap.get("print"));
			// 分派时效.
			teMap.put(ShowColEnum.Dispatch, teCodeMap.get("dispatch"));
			// 上报超区时效.
			teMap.put(ShowColEnum.RptOutArea, teCodeMap.get("rpt_oa"));
			// 超区处理时效.
			teMap.put(ShowColEnum.OutAreaTransfer, teCodeMap.get("oa_handle"));
			// 揽退时效.
			teMap.put(ShowColEnum.GetBack, teCodeMap.get("gb_suc"));
			// 有效接收时效.
			teMap.put(ShowColEnum.SystemAccept, teCodeMap.get("ef_rev"));
			// 未分配接收时效.
			teMap.put(ShowColEnum.NotMatched, teCodeMap.get("not_mat"));
		}
		return teMap;
	}

	private Map<ShowColEnum, TimeEffectiveVO> queryTimeEffectiveMap(OverdueExMoDetailCondVO cond) {
		Map<String, TimeEffectiveVO> teCodeMap = new HashMap<String, TimeEffectiveVO>();
		Map<ShowColEnum, TimeEffectiveVO> teMap = new HashMap<ShowColEnum, TimeEffectiveVO>();
		if (cond.isEnableTEQuery()) {
			List<TimeEffectiveVO> teList = this.getTimeEffectiveVO().getAllTimeEffectiveVO();
			for (TimeEffectiveVO te : teList) {
				teCodeMap.put(te.getCode(), te);
			}
			// 打印时效.
			teMap.put(ShowColEnum.Print, teCodeMap.get("print"));
			// 分派时效.
			teMap.put(ShowColEnum.Dispatch, teCodeMap.get("dispatch"));
			// 上报超区时效.
			teMap.put(ShowColEnum.RptOutArea, teCodeMap.get("rpt_oa"));
			// 超区处理时效.
			teMap.put(ShowColEnum.OutAreaTransfer, teCodeMap.get("oa_handle"));
			// 揽退时效.
			teMap.put(ShowColEnum.GetBack, teCodeMap.get("gb_suc"));
			// 有效接收时效.
			teMap.put(ShowColEnum.SystemAccept, teCodeMap.get("ef_rev"));
			// 未匹配时效.
			teMap.put(ShowColEnum.NotMatched, teCodeMap.get("not_mat"));
		}

		return teMap;
	}

	private TDCell loadShowData(Branch branch, long venderId, Integer index, OverdueExMoCondVO condVO, Map<ShowColEnum, TimeEffectiveVO> teMap) {
		ShowColEnum showColEnum = ShowColEnum.values()[index];
		BaseLoadAction loadAction = this.getLoadAction(showColEnum);

		return loadAction.loadData(branch, venderId, condVO, teMap);
	}

	private OverdueResultVO getOverdueResultVO(OverdueExMoCondVO condVO, int page) {
		OverdueResultVO result = new OverdueResultVO();
		result.setShowColList(this.getShowColList(condVO));
		result.setVenderId(condVO.getVenderId());
		result.setVenderName(this.getVenderName(condVO));
		result.setBranchMap(this.getBranchMap(condVO, page));
		this.setPageInfo(condVO, result, page);

		return result;
	}

	private String getVenderName(OverdueExMoCondVO condVO) {
		if (condVO.getVenderId() == 0) {
			return "";
		}
		return this.getCustomerDAO().getCustomerName(condVO.getVenderId());
	}

	private void setPageInfo(OverdueExMoCondVO condVO, OverdueResultVO result, int page) {
		result.setPage(page);
		result.setPageCount(this.getPageCount(condVO, page));
		result.setCount(condVO.getOrgs().size());
	}

	private int getPageCount(OverdueExMoCondVO condVO, int page) {
		List<Long> branchList = condVO.getOrgs();
		if (branchList.isEmpty()) {
			return 1;
		}
		if ((branchList.size() % 10) == 0) {
			return branchList.size() / 10;
		}
		return (branchList.size() / 10) + 1;
	}

	private Map<Long, String> getBranchMap(OverdueExMoCondVO condVO, int page) {
		List<Long> branchList = condVO.getOrgs();
		if (branchList.isEmpty()) {
			return new HashMap<Long, String>();
		}
		if (condVO.getVenderId() == 0) {
			return new HashMap<Long, String>();
		}
		return this.getBranchDAO().getBranchNameMap(new HashSet<Long>(branchList), page);
	}

	private List<String> getShowColList(OverdueExMoCondVO condVO) {
		List<Integer> showColIndexList = condVO.getShowCols();
		List<String> showColNameList = new ArrayList<String>();
		if (showColIndexList.isEmpty()) {
			return showColNameList;
		}
		for (Integer index : showColIndexList) {
			this.fillColNameToList(showColNameList, index);
		}
		return showColNameList;
	}

	private void fillColNameToList(List<String> showColNameList, Integer ordinal) {
		ShowColEnum showColEnum = ShowColEnum.values()[ordinal];
		switch (showColEnum) {
		case Dispatch:
			showColNameList.add("分派单量");
			showColNameList.add("分派率");
			break;
		case GetBack:
			showColNameList.add("揽退成功量");
			showColNameList.add("揽退成功率");
			break;
		case NotMatched:
			showColNameList.add("已匹配量");
			showColNameList.add("已匹配率");
			break;
		case OutAreaTransfer:
			showColNameList.add("超区转单量");
			showColNameList.add("超区转单率");
			break;
		case Print:
			showColNameList.add("打印单量");
			showColNameList.add("打印率");
			break;
		case RptOutArea:
			showColNameList.add("上报超区单量");
			showColNameList.add("上报超区率");
			break;
		case StationAccept:
			showColNameList.add("站点接收单量");
			break;
		case SystemAccept:
			showColNameList.add("系统接收单量");
			showColNameList.add("系统接收率");
			break;
		default:
			break;
		}
	}

	private OverdueConstantVO getOverdueConstantVO() {
		OverdueConstantVO constantVO = new OverdueConstantVO();
		constantVO.setTimeTypeMap(this.getTimeTypeMap());
		constantVO.setTimeType(TimeTypeEnum.SystemAcceptTime.ordinal());
		constantVO.setVenderMap(this.getVenderMap());
		constantVO.setOrgMap(this.getOrgMap());
		constantVO.setShowColMap(this.getShowColMap());

		return constantVO;
	}

	private Map<Long, String> getOrgMap() {
		return this.getBranchDAO().getBranchAndWarehouseNameMap();
	}

	private Map<Integer, String> getTimeTypeMap() {
		Map<Integer, String> timeTypeMap = new LinkedHashMap<Integer, String>();
		timeTypeMap.put(TimeTypeEnum.OrderCreateTime.ordinal(), TimeTypeEnum.OrderCreateTime.getName());
		timeTypeMap.put(TimeTypeEnum.SystemAcceptTime.ordinal(), TimeTypeEnum.SystemAcceptTime.getName());

		return timeTypeMap;
	}

	private Map<Long, String> getVenderMap() {
		Map<Long, String> venderMap = new LinkedHashMap<Long, String>();
		List<Customer> customerList = this.getCustomerDAO().getAllCustomersWithDisable();
		for (Customer customer : customerList) {
			venderMap.put(customer.getCustomerid(), customer.getCustomername());
		}
		return venderMap;
	}

	private Map<Integer, String> getShowColMap() {
		Map<Integer, String> showColMap = new LinkedHashMap<Integer, String>();
		for (ShowColEnum showCol : ShowColEnum.values()) {
			showColMap.put(showCol.ordinal(), showCol.getColName());
		}
		return showColMap;
	}

	private CustomerDAO getCustomerDAO() {
		return this.customerDAO;
	}

	private BranchDAO getBranchDAO() {
		return this.branchDAO;
	}

	private TimeEffectiveDAO getTimeEffectiveVO() {
		return this.timeEffectiveVO;
	}

	private BaseLoadAction getLoadAction(ShowColEnum showColEnum) {
		switch (showColEnum) {
		case Dispatch:
			return new DispatchAction();
		case GetBack:
			return new GetBackAction();
		case NotMatched:
			return new NotMatchAction();
		case OutAreaTransfer:
			return new OutAreaTransferAction();
		case Print:
			return new PrintAction();
		case RptOutArea:
			return new ReportOutAreaAction();
		case StationAccept:
			return new StationAcceptAction();
		case SystemAccept:
			return new SystemAcceptAction();
		default:
			return null;
		}
	}

	private enum ShowColEnum {
		SystemAccept("系统接收"), OutAreaTransfer("超区转单"), NotMatched("已匹配"), StationAccept("站点接收"), Print("打印"), Dispatch("分派"), RptOutArea("上报超区"), GetBack("揽退");

		String colName;

		ShowColEnum(String colName) {
			this.colName = colName;
		}

		public String getColName() {
			return this.colName;
		}

	}

	private interface LoadDataAction extends Cloneable {
		public TDCell loadData(Branch branch, long venderId, OverdueExMoCondVO condVO, Map<ShowColEnum, TimeEffectiveVO> teMap);
	}

	private JdbcTemplate getJdbcTemplate() {
		return this.jdbcTemplate;
	}

	private class BaseLoadAction implements LoadDataAction {

		private Branch branch = null;

		private long venderId = -1;

		private OverdueExMoCondVO condVO = null;

		private Map<ShowColEnum, TimeEffectiveVO> timeEffectiveMap = null;

		@Override
		public TDCell loadData(Branch branch, long venderId, OverdueExMoCondVO condVO, Map<ShowColEnum, TimeEffectiveVO> teMap) {
			this.branch = branch;
			this.venderId = venderId;
			this.condVO = condVO;
			this.timeEffectiveMap = teMap;

			return this.loadData();
		}

		@Override
		public BaseLoadAction clone() {
			try {
				return (BaseLoadAction) super.clone();
			} catch (CloneNotSupportedException e) {
			}
			return null;
		}

		protected TDCell loadData() {
			return null;
		}

		protected Branch getBranch() {
			return this.branch;
		}

		protected OverdueExMoCondVO getCondVO() {
			return this.condVO;
		}

		protected long getBranchId() {
			return this.getBranch().getBranchid();
		}

		protected long getVenderId() {
			return this.venderId;
		}

		protected JdbcTemplate getDAO() {
			return OverdueExMoController.this.jdbcTemplate;
		}

		protected Map<ShowColEnum, TimeEffectiveVO> getTimeEffectiveMap() {
			return this.timeEffectiveMap;
		}

		protected boolean isEnabelTEQuery() {
			return this.getCondVO().isEnableTEQuery();
		}

		protected String getSelectPart() {
			return "select count(cwb) from express_ops_smt_cwb_opt_time ";
		}

		protected String getTimeTypeWhereCond() {
			String startTime = this.getCondVO().getStartTime();
			String endTime = this.getCondVO().getEndTime();
			TimeTypeEnum timeType = TimeTypeEnum.values()[this.getCondVO().getOptTimeType()];
			StringBuilder whereCond = new StringBuilder();
			String field = null;
			if (TimeTypeEnum.SystemAcceptTime.equals(timeType)) {
				field = "system_accept_time";
			} else {
				field = "create_time";
			}
			whereCond.append(field + " >= '" + startTime + "'");
			whereCond.append(" and " + field + " <= '" + endTime + "'");

			return whereCond.toString();
		}

		protected TimeEffectiveVO getTimeEffectiveVO(ShowColEnum showColEnum) {
			return this.getTimeEffectiveMap().get(showColEnum);
		}

		protected String getPercent(int count, int total) {
			DecimalFormat df = new DecimalFormat("######0.00");
			if (total == 0) {
				return "--";
			}
			double percent = ((1.0 * count) / total) * 100;

			return df.format(percent) + "%";
		}

	}

	private class SystemAcceptAction extends BaseLoadAction {

		@Override
		protected TDCell loadData() {
			int count = this.getDAO().queryForInt(this.getSql(false, false), this.getBranchId(), this.getVenderId());
			int acceptCount = this.getDAO().queryForInt(this.getSql(true, this.isEnabelTEQuery()), this.getBranchId(), this.getVenderId());
			TDCell cell = new TDCell();
			cell.setCount(acceptCount);
			cell.setPercent(this.getPercent(acceptCount, count));
			cell.setShowColIndex(ShowColEnum.SystemAccept.ordinal());

			return cell;
		}

		private String getSql(boolean accept, boolean enableTEQuery) {
			StringBuilder sql = new StringBuilder();
			sql.append(this.getSelectPart());
			sql.append(" where warehouse_id = ? and vender_id = ?");
			sql.append(" and " + this.getTimeTypeWhereCond());
			if (accept) {
				sql.append(" and system_accept_time != '0000-00-00 00:00:00'");
			}
			if (enableTEQuery) {
				TimeEffectiveVO teVO = this.getTimeEffectiveVO(ShowColEnum.SystemAccept);
				String subField = teVO.getTimeType().getField();
				sql.append(" and (unix_timestamp(system_accept_time) - unix_timestamp(" + subField + ")) <=" + teVO.getScope());
			}
			return sql.toString();
		}

	}

	private class StationAcceptAction extends BaseLoadAction {

		@Override
		protected TDCell loadData() {
			int satAcceptCount = this.getDAO().queryForInt(this.getSql(), this.getBranchId(), this.getVenderId());
			TDCell cell = new TDCell();
			cell.setCount(satAcceptCount);
			cell.setShowColIndex(ShowColEnum.StationAccept.ordinal());

			return cell;
		}

		private String getSql() {
			StringBuilder sql = new StringBuilder();
			sql.append(this.getSelectPart());
			sql.append("where deliver_station_id = ? and vender_id = ?");
			sql.append(" and  " + this.getTimeTypeWhereCond());
			sql.append(" and  station_accept_time != '0000-00-00 00:00:00'");

			return sql.toString();
		}
	}

	private class PrintAction extends BaseLoadAction {
		@Override
		protected TDCell loadData() {
			int total = this.getDAO().queryForInt(this.getSql(false, false), this.getBranchId(), this.getVenderId());
			int count = this.getDAO().queryForInt(this.getSql(true, this.isEnabelTEQuery()), this.getBranchId(), this.getVenderId());

			TDCell cell = new TDCell();
			cell.setCount(count);
			cell.setPercent(this.getPercent(count, total));
			cell.setShowColIndex(ShowColEnum.Print.ordinal());

			return cell;
		}

		private String getSql(boolean print, boolean enableTEQuery) {
			StringBuilder sql = new StringBuilder();
			sql.append(this.getSelectPart());
			sql.append("where deliver_station_id = ? and vender_id = ?");
			sql.append(" and " + this.getTimeTypeWhereCond());
			if (print) {
				sql.append(" and print_time != '0000-00-00 00:00:00'");
			}
			if (enableTEQuery) {
				TimeEffectiveVO teVO = this.getTimeEffectiveVO(ShowColEnum.Print);
				if (teVO != null) {
					String subField = teVO.getTimeType().getField();
					sql.append(" and (unix_timestamp(print_time) - unix_timestamp(" + subField + ")) <=" + teVO.getScope());
				}
			}
			return sql.toString();
		}
	}

	private class DispatchAction extends BaseLoadAction {
		@Override
		protected TDCell loadData() {
			int total = this.getDAO().queryForInt(this.getSql(false, false), this.getBranchId(), this.getVenderId());
			int count = this.getDAO().queryForInt(this.getSql(true, this.isEnabelTEQuery()), this.getBranchId(), this.getVenderId());

			TDCell cell = new TDCell();
			cell.setCount(count);
			cell.setPercent(this.getPercent(count, total));
			cell.setShowColIndex(ShowColEnum.Dispatch.ordinal());

			return cell;
		}

		private String getSql(boolean print, boolean enableTEQuery) {
			StringBuilder sql = new StringBuilder();
			sql.append(this.getSelectPart());
			sql.append("where deliver_station_id = ? and vender_id = ?");
			sql.append(" and " + this.getTimeTypeWhereCond());
			if (print) {
				sql.append(" and dispatch_time != '0000-00-00 00:00:00'");
			}
			if (enableTEQuery) {
				TimeEffectiveVO teVO = this.getTimeEffectiveVO(ShowColEnum.Dispatch);
				if (teVO != null) {
					String subField = teVO.getTimeType().getField();
					sql.append(" and (unix_timestamp(dispatch_time) - unix_timestamp(" + subField + ")) <=" + teVO.getScope());
				}
			}
			return sql.toString();
		}
	}

	private class ReportOutAreaAction extends BaseLoadAction {
		@Override
		protected TDCell loadData() {
			int total = this.getDAO().queryForInt(this.getSql(false, false), this.getBranchId(), this.getVenderId());
			int count = this.getDAO().queryForInt(this.getSql(true, this.isEnabelTEQuery()), this.getBranchId(), this.getVenderId());

			TDCell cell = new TDCell();
			cell.setCount(count);
			cell.setPercent(this.getPercent(count, total));
			cell.setShowColIndex(ShowColEnum.RptOutArea.ordinal());

			return cell;
		}

		private String getSql(boolean rptOutArea, boolean enableTEQuery) {
			StringBuilder sql = new StringBuilder();
			sql.append(this.getSelectPart());
			sql.append("where deliver_station_id = ? and vender_id = ?");
			sql.append(" and " + this.getTimeTypeWhereCond());
			if (rptOutArea) {
				sql.append(" and report_outarea_time != '0000-00-00 00:00:00'");
			}
			if (enableTEQuery) {
				TimeEffectiveVO teVO = this.getTimeEffectiveVO(ShowColEnum.RptOutArea);
				if (teVO != null) {
					String subField = teVO.getTimeType().getField();
					sql.append(" and (unix_timestamp(report_outarea_time) - unix_timestamp(" + subField + ")) <=" + teVO.getScope());
				}
			}
			return sql.toString();
		}
	}

	private class NotMatchAction extends BaseLoadAction {
		@Override
		protected TDCell loadData() {
			int total = this.getDAO().queryForInt(this.getSql(false, false), this.getBranchId(), this.getVenderId());
			int count = this.getDAO().queryForInt(this.getSql(true, this.isEnabelTEQuery()), this.getBranchId(), this.getVenderId());

			TDCell cell = new TDCell();
			cell.setCount(count);
			cell.setPercent(this.getPercent(count, total));
			cell.setShowColIndex(ShowColEnum.NotMatched.ordinal());

			return cell;
		}

		private String getSql(boolean matched, boolean enableTEQuery) {
			StringBuilder sql = new StringBuilder();
			sql.append(this.getSelectPart());
			sql.append("where warehouse_id = ? and vender_id = ?");
			sql.append(" and " + this.getTimeTypeWhereCond());
			if (matched) {
				sql.append(" and station_accept_time != '0000-00-00 00:00:00'");
			}
			if (enableTEQuery) {
				TimeEffectiveVO teVO = this.getTimeEffectiveVO(ShowColEnum.NotMatched);
				if (teVO != null) {
					String subField = teVO.getTimeType().getField();
					sql.append(" and (unix_timestamp(station_accept_time) - unix_timestamp(" + subField + ")) <=" + teVO.getScope());
				}
			}
			return sql.toString();
		}
	}

	private class OutAreaTransferAction extends BaseLoadAction {
		@Override
		protected TDCell loadData() {
			int total = this.getDAO().queryForInt(this.getSql(false, false), this.getBranchId(), this.getVenderId());
			int count = this.getDAO().queryForInt(this.getSql(true, this.isEnabelTEQuery()), this.getBranchId(), this.getVenderId());

			TDCell cell = new TDCell();
			cell.setCount(count);
			cell.setPercent(this.getPercent(count, total));
			cell.setShowColIndex(ShowColEnum.OutAreaTransfer.ordinal());

			return cell;
		}

		private String getSql(boolean match, boolean enableTEQuery) {
			StringBuilder sql = new StringBuilder();
			sql.append(this.getSelectPart());
			sql.append("where warehouse_id = ? and vender_id = ?");
			sql.append(" and " + this.getTimeTypeWhereCond());
			if (match) {
				sql.append(" and exception_match_time != '0000-00-00 00:00:00'");
			}
			if (enableTEQuery) {
				TimeEffectiveVO teVO = this.getTimeEffectiveVO(ShowColEnum.OutAreaTransfer);
				if (teVO != null) {
					String subField = teVO.getTimeType().getField();
					sql.append(" and (unix_timestamp(exception_match_time) - unix_timestamp(" + subField + ")) <=" + teVO.getScope());
				}
			}
			return sql.toString();
		}
	}

	private class GetBackAction extends BaseLoadAction {
		@Override
		protected TDCell loadData() {
			int total = this.getDAO().queryForInt(this.getSql(false, false), this.getBranchId(), this.getVenderId());
			int count = this.getDAO().queryForInt(this.getSql(true, this.isEnabelTEQuery()), this.getBranchId(), this.getVenderId());

			TDCell cell = new TDCell();
			cell.setCount(count);
			cell.setPercent(this.getPercent(count, total));
			cell.setShowColIndex(ShowColEnum.GetBack.ordinal());

			return cell;
		}

		private String getSql(boolean getBack, boolean enableTEQuery) {
			StringBuilder sql = new StringBuilder();
			sql.append(this.getSelectPart());
			sql.append("where deliver_station_id = ? and vender_id = ?");
			sql.append(" and " + this.getTimeTypeWhereCond());
			if (getBack) {
				sql.append(" and deliver_state = 2");
			}
			sql.append(" and dispatch_time != '0000-00-00 00:00:00'");
			if (enableTEQuery) {
				TimeEffectiveVO teVO = this.getTimeEffectiveVO(ShowColEnum.GetBack);
				if (teVO != null) {
					String subField = teVO.getTimeType().getField();
					sql.append(" and (unix_timestamp(feedback_time) - unix_timestamp(" + subField + ")) <=" + teVO.getScope());
				}
			}
			return sql.toString();
		}
	}

	private class ExportDataUtils extends ExcelUtils {

		private OverdueExMoCondVO condVO = null;

		private List<List<Object>> dataList = null;

		private HttpServletResponse response = null;

		public ExportDataUtils(HttpServletResponse response, OverdueExMoCondVO condVO, List<List<Object>> exportDataList) {
			this.response = response;
			this.condVO = condVO;
			this.dataList = exportDataList;
		}

		public void exportData() throws Exception {
			this.excel(this.getResponse(), this.getShowColNames(), "sheet1", "超期监控.xlsx");
		}

		@Override
		public void fillData(Sheet sheet, CellStyle style) {
			int rowNum = 1;
			for (List<Object> rowData : this.getDataList()) {
				this.createRow(rowNum++, sheet, rowData);
			}
		}

		private void createRow(int rowNum, Sheet sheet, List<Object> rowData) {
			Row row = sheet.createRow(rowNum);
			int colNum = 0;
			for (Object cellData : rowData) {
				if (cellData instanceof TDCell) {
					TDCell tdCell = (TDCell) cellData;
					this.createCell(colNum++, row, tdCell.getCount());
					if ((tdCell.getPercent() != null) && !tdCell.getPercent().isEmpty()) {
						this.createCell(colNum++, row, tdCell.getPercent());
					}
				} else {
					this.createCell(colNum++, row, cellData);
				}

			}
		}

		private void createCell(int colNum, Row row, Object cellData) {
			Cell cell = row.createCell(colNum);
			cell.setCellValue(cellData.toString());
		}

		private String[] getShowColNames() {
			List<String> showColNameList = new ArrayList<String>();
			showColNameList.add("机构名称");
			showColNameList.add("供应商");
			showColNameList.addAll(OverdueExMoController.this.getShowColList(this.getCondVO()));

			return showColNameList.toArray(new String[0]);
		}

		private List<List<Object>> getDataList() {
			return this.dataList;
		}

		private OverdueExMoCondVO getCondVO() {
			return this.condVO;
		}

		private HttpServletResponse getResponse() {
			return this.response;
		}
	}

	public static class TDCell {
		private int count = 0;

		private String percent = "";

		private int showColIndex = -1;

		public int getCount() {
			return this.count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public String getPercent() {
			return this.percent;
		}

		public void setPercent(String percent) {
			this.percent = percent;
		}

		public int getShowColIndex() {
			return this.showColIndex;
		}

		public void setShowColIndex(int showColIndex) {
			this.showColIndex = showColIndex;
		}
	}

	private class ExportDetailUtil extends ExcelUtils {

		private HttpServletResponse response = null;

		private List<OverdueExMoDetailVO> dtList = null;

		public ExportDetailUtil(HttpServletResponse response, List<OverdueExMoDetailVO> dtList) {
			this.response = response;
			this.dtList = dtList;
		}

		public void export() throws Exception {
			this.excel(this.getResponse(), this.getTitles(), "sheet1", "汇总明细.xlsx");
		}

		@Override
		public void fillData(Sheet sheet, CellStyle style) {
			int rowIndex = 1;
			for (OverdueExMoDetailVO dt : this.getDtList()) {
				this.createRow(sheet, rowIndex++, dt);
			}
		}

		private void createRow(Sheet sheet, int rowIndex, OverdueExMoDetailVO dt) {
			Row row = sheet.createRow(rowIndex);
			Cell cell = row.createCell(0);
			cell.setCellValue(dt.getCwb());

			cell = row.createCell(1);
			cell.setCellValue(dt.getVenderName());

			cell = row.createCell(2);
			cell.setCellValue(dt.getCreateDate());

			cell = row.createCell(3);
			cell.setCellValue(dt.getDeliverState());

			cell = row.createCell(4);
			cell.setCellValue(dt.getWarehouseName());

			cell = row.createCell(5);
			cell.setCellValue(dt.getDeliverStationName());
		}

		private HttpServletResponse getResponse() {
			return this.response;
		}

		private String[] getTitles() {
			return new String[] { "订单号", "供应商", "订单创建时间", "配送状态", "仓库", "配送站点" };
		}

		private List<OverdueExMoDetailVO> getDtList() {
			return this.dtList;
		}

	}

}
