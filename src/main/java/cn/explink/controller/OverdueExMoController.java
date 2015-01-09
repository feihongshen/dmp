package cn.explink.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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
import cn.explink.domain.OverdueResultVO;
import cn.explink.domain.TimeEffectiveVO;
import cn.explink.domain.TimeTypeEnum;

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
		mav.addObject("const", this.getOverdueConstantVO());
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
		Branch branch = this.getBranchDAO().getBranchByBranchid(branchId);
		Map<ShowColEnum, TimeEffectiveVO> teMap = this.queryTimeEffectiveMap(cond);
		for (Integer showColIndex : showColList) {
			resultVO.addResult(this.loadShowData(branch, venderId, showColIndex, cond, teMap));
		}
		return resultVO;
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
		}
		return teMap;
	}

	private List<Object> loadShowData(Branch branch, long venderId, Integer index, OverdueExMoCondVO condVO, Map<ShowColEnum, TimeEffectiveVO> teMap) {
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
			showColNameList.add("未匹配量");
			showColNameList.add("未匹配率");
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
		List<Customer> customerList = this.getCustomerDAO().getAllCustomers();
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
		SystemAccept("系统接收"), OutAreaTransfer("超区转单"), NotMatched("未匹配"), StationAccept("站点接收"), Print("打印"), Dispatch("分派"), RptOutArea("上报超区"), GetBack("揽退");

		String colName;

		ShowColEnum(String colName) {
			this.colName = colName;
		}

		public String getColName() {
			return this.colName;
		}

	}

	private interface LoadDataAction extends Cloneable {
		public List<Object> loadData(Branch branch, long venderId, OverdueExMoCondVO condVO, Map<ShowColEnum, TimeEffectiveVO> teMap);
	}

	private class BaseLoadAction implements LoadDataAction {

		private Branch branch = null;

		private long venderId = -1;

		private OverdueExMoCondVO condVO = null;

		private Map<ShowColEnum, TimeEffectiveVO> timeEffectiveMap = null;

		@Override
		public List<Object> loadData(Branch branch, long venderId, OverdueExMoCondVO condVO, Map<ShowColEnum, TimeEffectiveVO> teMap) {
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

		protected List<Object> loadData() {
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
				return "100%";
			}
			double percent = ((1.0 * count) / total) * 100;

			return df.format(percent) + "%";
		}

	}

	private class SystemAcceptAction extends BaseLoadAction {

		@Override
		protected List<Object> loadData() {
			List<Object> dataList = new ArrayList<Object>();
			int count = this.getDAO().queryForInt(this.getSql(false, false), this.getBranchId(), this.getVenderId());
			int acceptCount = this.getDAO().queryForInt(this.getSql(true, this.isEnabelTEQuery()), this.getBranchId(), this.getVenderId());
			dataList.add(acceptCount);
			dataList.add(this.getPercent(acceptCount, count));

			return dataList;
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
		protected List<Object> loadData() {
			List<Object> dataList = new ArrayList<Object>();
			int sysAcceptCount = this.getDAO().queryForInt(this.getSql(), this.getBranchId(), this.getVenderId());
			dataList.add(sysAcceptCount);

			return dataList;
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
		protected List<Object> loadData() {
			List<Object> dataList = new ArrayList<Object>();
			int total = this.getDAO().queryForInt(this.getSql(false, false), this.getBranchId(), this.getVenderId());
			int count = this.getDAO().queryForInt(this.getSql(true, this.isEnabelTEQuery()), this.getBranchId(), this.getVenderId());

			dataList.add(count);
			dataList.add(this.getPercent(count, total));

			return dataList;
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
		protected List<Object> loadData() {
			List<Object> dataList = new ArrayList<Object>();
			int total = this.getDAO().queryForInt(this.getSql(false, false), this.getBranchId(), this.getVenderId());
			int count = this.getDAO().queryForInt(this.getSql(true, this.isEnabelTEQuery()), this.getBranchId(), this.getVenderId());

			dataList.add(count);
			dataList.add(this.getPercent(count, total));

			return dataList;
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
		protected List<Object> loadData() {
			List<Object> dataList = new ArrayList<Object>();
			int total = this.getDAO().queryForInt(this.getSql(false, false), this.getBranchId(), this.getVenderId());
			int count = this.getDAO().queryForInt(this.getSql(true, this.isEnabelTEQuery()), this.getBranchId(), this.getVenderId());

			dataList.add(count);
			dataList.add(this.getPercent(count, total));

			return dataList;
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
		protected List<Object> loadData() {
			List<Object> dataList = new ArrayList<Object>();
			int total = this.getDAO().queryForInt(this.getSql(false, false), this.getBranchId(), this.getVenderId());
			int count = this.getDAO().queryForInt(this.getSql(true, this.isEnabelTEQuery()), this.getBranchId(), this.getVenderId());

			dataList.add(count);
			dataList.add(this.getPercent(count, total));

			return dataList;
		}

		private String getSql(boolean match, boolean enableTEQuery) {
			StringBuilder sql = new StringBuilder();
			sql.append(this.getSelectPart());
			sql.append("where warehouse_id = ? and vender_id = ?");
			sql.append(" and " + this.getTimeTypeWhereCond());
			if (match) {
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
		protected List<Object> loadData() {
			List<Object> dataList = new ArrayList<Object>();
			int total = this.getDAO().queryForInt(this.getSql(false, false), this.getBranchId(), this.getVenderId());
			int count = this.getDAO().queryForInt(this.getSql(true, this.isEnabelTEQuery()), this.getBranchId(), this.getVenderId());

			dataList.add(count);
			dataList.add(this.getPercent(count, total));

			return dataList;
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
					sql.append(" and (unix_timestamp(exception_match_time) - unix_timestamp(" + subField + ")) >=" + teVO.getScope());
				}
			}
			return sql.toString();
		}
	}

	private class GetBackAction extends BaseLoadAction {
		@Override
		protected List<Object> loadData() {
			List<Object> dataList = new ArrayList<Object>();
			int total = this.getDAO().queryForInt(this.getSql(false, false), this.getBranchId(), this.getVenderId());
			int count = this.getDAO().queryForInt(this.getSql(true, this.isEnabelTEQuery()), this.getBranchId(), this.getVenderId());

			dataList.add(count);
			dataList.add(this.getPercent(count, total));

			return dataList;
		}

		private String getSql(boolean match, boolean enableTEQuery) {
			StringBuilder sql = new StringBuilder();
			sql.append(this.getSelectPart());
			sql.append("where warehouse_id = ? and vender_id = ?");
			sql.append(" and " + this.getTimeTypeWhereCond());
			if (match) {
				sql.append(" and feedback_time != '0000-00-00 00:00:00'");
			}
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

}
