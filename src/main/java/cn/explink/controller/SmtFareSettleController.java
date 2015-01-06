package cn.explink.controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.domain.SmtFareSettleCondVO;
import cn.explink.domain.SmtFareSettleConstVO;
import cn.explink.domain.SmtFareSettleResultVO;
import cn.explink.domain.SmtFareSettleVO;
import cn.explink.domain.TimeTypeEnum;

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
		mav.addObject("const", this.getSmtFareSettleConstVO(true));
		mav.addObject("result", this.getStationResultVO(page, cond));
		mav.addObject("cond", cond);
		mav.setViewName("/smtfaresettle/smtfaresettle_station");

		return mav;
	}

	@RequestMapping("/deliver/{page}")
	public ModelAndView showDeliver(@PathVariable("page") int page, SmtFareSettleCondVO cond, ModelAndView mav) {
		mav.addObject("const", this.getSmtFareSettleConstVO(false));
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
		if (cond.getStartTime() == null) {
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
		int end = (pageCount + 11) <= allPairList.size() ? (pageCount + 11) : allPairList.size();
		pairList.addAll(allPairList.subList(pageCount + 1, end));

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
		List<Long> deliverIdList = cond.getDelivers();
		if (!deliverIdList.isEmpty()) {
			return deliverIdList;
		}
		return this.getUserDAO().getAllDeliverId();
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
		sql.append("select count(cwb) total,count(feed_back) success ,sum(should_fee) should_fee,sum(received_fee) received_fee from express_ops_smt_cwb_opt_time ");
		sql.append("where deliver_station_id = ? and vender_id = ? and ");
		sql.append(this.getTimeWhereCond(cond));

		return sql.toString();
	}

	private String getTimeWhereCond(SmtFareSettleCondVO cond) {
		int index = cond.getOptTimeType();
		TimeTypeEnum timeType = TimeTypeEnum.values()[index];
		StringBuilder whereCond = new StringBuilder();
		whereCond.append(timeType.getField());
		whereCond.append(" >= '" + cond.getStartTime() + "' ");
		whereCond.append("and " + timeType.getField() + " <= '" + cond.getEndTime() + "'");

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

	private SmtFareSettleConstVO getSmtFareSettleConstVO(boolean deliver) {
		SmtFareSettleConstVO constantVO = new SmtFareSettleConstVO();
		constantVO.setTimeTypeMap(this.getTimeTypeMap());
		constantVO.setTimeType(TimeTypeEnum.SystemAcceptTime.ordinal());
		constantVO.setVenderMap(this.getVenderMap());
		constantVO.setOrgMap(this.getOrgMap());
		if (deliver) {
			constantVO.setDeliverMap(this.getAllDeliverMap());
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
		List<Branch> branchList = this.getBranchDAO().getAllBranchBySiteType(1L);
		Map<Long, String> branchMap = new LinkedHashMap<Long, String>();
		for (Branch branch : branchList) {
			branchMap.put(branch.getBranchid(), branch.getBranchname());
		}
		return branchMap;
	}

	private Map<Long, String> getAllDeliverMap() {
		return this.getUserDAO().getAllDeliverMap();
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
}
