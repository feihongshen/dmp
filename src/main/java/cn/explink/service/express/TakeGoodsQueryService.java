package cn.explink.service.express;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.CwbDAO;
import cn.explink.dao.RoleDAO;
import cn.explink.dao.UserDAO;
import cn.explink.dao.express.ExpressOutStationInfoDao;
import cn.explink.domain.Role;
import cn.explink.domain.User;
import cn.explink.domain.VO.express.ExpressCwbOrderForTakeGoodsQueryVO;
import cn.explink.domain.express.ExpressCwb4TakeGoodsQuery;
import cn.explink.domain.express.ExpressOutStationInfo;
import cn.explink.enumutil.express.ExpressSettleWayEnum;

/**
 * 揽件查询Service
 * @author wangzy
 *
 */
@Service
public class TakeGoodsQueryService {

	@Autowired
	CwbDAO cwbDao;
	@Autowired
	UserDAO userDao;
	@Autowired
	RoleDAO roleDAO;
	@Autowired
	ExpressOutStationInfoDao expressOutStationInfoDao;
	@Autowired
	ExpressOutStationService expressOutStationService;

	/**
	 * 揽件查询，按条件查订单实体集合，不带分页
	 *
	 * @param page
	 * @return
	 */
	public List<ExpressCwbOrderForTakeGoodsQueryVO> getcwbOrders(ExpressCwb4TakeGoodsQuery cwb4TakeGoodsQuery, String userIds) {

		return this.cwbDao.queryCwbExpressTakeGoodsQueryByPage(null, cwb4TakeGoodsQuery, userIds);
	}

	/**
	 * 揽件查询，按条件查订单实体集合，带分页
	 *
	 * @param page
	 * @return
	 * @throws ParseException
	 */
	public List<ExpressCwbOrderForTakeGoodsQueryVO> getcwbOrderByPage(Long page, ExpressCwb4TakeGoodsQuery cwb4TakeGoodsQuery, User user, String userIds) throws ParseException {
		return this.cwbDao.queryCwbExpressTakeGoodsQueryByPage(page, cwb4TakeGoodsQuery, userIds);
	}

	/**
	 * 揽件查询，按条件查询订单数量
	 *
	 * @param emaildateid
	 * @return
	 * @throws ParseException
	 * @throws NumberFormatException
	 */
	public Long getcwborderCount(Long page, ExpressCwb4TakeGoodsQuery cwb4TakeGoodsQuery, User user, List<ExpressCwbOrderForTakeGoodsQueryVO> listOrderfdsdsadfsdf, String userIds) throws NumberFormatException, ParseException {
		//		return cwbDao.getcwborderCount(emaildateid);
		/*if(cwb4TakeGoodsQuery.getStatus()!=null &&cwb4TakeGoodsQuery.getStatus()==3){

			List<ExpressCwbOrderForTakeGoodsQueryVO> list = getAllOutStationInfo(cwb4TakeGoodsQuery,user,userIds);

			return (long)list.size();
		}else{*/
		return this.cwbDao.queryCwbExpressTakeGoodsQueryCountByPage(cwb4TakeGoodsQuery, userIds);
		/*}*/
	}

	/**
	 * 根据角色id获取用户
	 *
	 * @param roleid
	 * @return
	 */
	public List<User> getUserByRole(int roleid) {
		return this.userDao.getUserByRole(roleid);
	}

	/**
	 * 获取用户
	 * @return
	 */
	public List<User> getRoleUsers(User user) {
		String roleids = "2,4";
		List<Role> roles = this.roleDAO.getRolesByIsdelivery();
		if ((roles != null) && (roles.size() > 0)) {
			for (Role r : roles) {
				roleids += "," + r.getRoleid();
			}
		}
		List<User> uList = this.userDao.getUserByRolesAndBranchid(roleids, user.getBranchid());
		return uList;
	}

	/**
	 *
	 * @Title: getAllRoleUsers
	 * @description 获取所有状态下（工作、离职、休假）的小件员
	 * @author 刘武强
	 * @date  2016年1月20日下午3:44:01
	 * @param  @param user
	 * @param  @return
	 * @return  List<User>
	 * @throws
	 */
	public List<User> getAllRoleUsers(User user) {
		String roleids = "2,4";
		List<Role> roles = this.roleDAO.getRolesByIsdelivery();
		if ((roles != null) && (roles.size() > 0)) {
			for (Role r : roles) {
				roleids += "," + r.getRoleid();
			}
		}
		List<User> uList = this.userDao.getAllUserByRoleidsAndBranchid(roleids, user.getBranchid());
		return uList;
	}

	/**
	 * 获得总费用和数量
	 * @throws ParseException
	 */
	public ExpressCwb4TakeGoodsQuery getShouldfareAndCount(Long page, ExpressCwb4TakeGoodsQuery cwb4TakeGoodsQuery, User user, String userIds) throws ParseException {
		List<ExpressCwbOrderForTakeGoodsQueryVO> OrderList = new ArrayList<ExpressCwbOrderForTakeGoodsQueryVO>();
		/*if(cwb4TakeGoodsQuery.getStatus()!=null && cwb4TakeGoodsQuery.getStatus()==3){
		//			List<Branch> nextBranchList = expressOutStationService.getNextBranchList(user.getBranchid(),user);

			OrderList = getAllOutStationInfo(cwb4TakeGoodsQuery,user,userIds);
		}else{*/

		OrderList = this.cwbDao.queryCwbExpressTakeGoodsQuery(cwb4TakeGoodsQuery, userIds);
		/*}*/

		//查询所有符合cwb4TakeGoodsQuery中条件的订单
		//		List<ExpressCwbOrderForTakeGoodsQueryVO> OrderList = cwbDao.queryCwbExpressTakeGoodsQuery(cwb4TakeGoodsQuery);
		//算出总费用、到付总费用、现付总费用、月付总费用还有各自的数量
		BigDecimal shouldfareTotal = new BigDecimal(0);
		BigDecimal shouldfareArrive = new BigDecimal(0);
		BigDecimal shouldfareNow = new BigDecimal(0);
		BigDecimal shouldfareMonth = new BigDecimal(0);
		int countAll = 0;
		int countArrive = 0;
		int countNow = 0;
		int countMonth = 0;
		for (ExpressCwbOrderForTakeGoodsQueryVO cwbOrder : OrderList) {
			int payMethod = cwbOrder.getPaymethodint();
			BigDecimal shouldfare = cwbOrder.getShouldfare().add(cwbOrder.getPackagefee()).add(cwbOrder.getInsuredfee());//运费 +包装费+保价费
			//			BigDecimal packagefee = cwbOrder.getPackagefee();//包装费
			//			BigDecimal insuredfee = cwbOrder.getInsuredfee();//保价费用
			//计算现付总费用和单量
			if (payMethod == ExpressSettleWayEnum.NowPay.getValue().intValue()) {
				shouldfareNow = shouldfareNow.add(shouldfare);
				shouldfareTotal = shouldfareTotal.add(shouldfare);
				countNow++;
				countAll++;
				continue;
			}
			//计算到付总费用和单量
			if (payMethod == ExpressSettleWayEnum.ArrivePay.getValue().intValue()) {
				shouldfareArrive = shouldfareArrive.add(shouldfare);
				shouldfareTotal = shouldfareTotal.add(shouldfare);
				countArrive++;
				countAll++;
				continue;
			}
			//计算月付总费用和单量
			if (payMethod == ExpressSettleWayEnum.MonthPay.getValue().intValue()) {
				shouldfareMonth = shouldfareMonth.add(shouldfare);
				shouldfareTotal = shouldfareTotal.add(shouldfare);
				countMonth++;
				countAll++;
				continue;
			}
			shouldfareTotal = shouldfareTotal.add(shouldfare);
		}
		//用Cwb4TakeGoodsQuery来接收数据算出来的值
		cwb4TakeGoodsQuery.setShouldfareAll(shouldfareTotal);
		cwb4TakeGoodsQuery.setShouldfareNow(shouldfareNow);
		cwb4TakeGoodsQuery.setShouldfareArrive(shouldfareArrive);
		cwb4TakeGoodsQuery.setShouldfareMonth(shouldfareMonth);
		cwb4TakeGoodsQuery.setCountAll(countAll);
		cwb4TakeGoodsQuery.setCountNow(countNow);
		cwb4TakeGoodsQuery.setCountArrive(countArrive);
		cwb4TakeGoodsQuery.setCountMonth(countMonth);
		//将此VO返回
		return cwb4TakeGoodsQuery;
	}

	public List<ExpressCwbOrderForTakeGoodsQueryVO> getAllOutStationInfo(ExpressCwb4TakeGoodsQuery cwb4TakeGoodsQuery, User user, String userIds) {
		Map<Long, ExpressOutStationInfo> map = this.expressOutStationInfoDao.getOutStationInfo(cwb4TakeGoodsQuery, user.getBranchid(), userIds);
		List<ExpressCwbOrderForTakeGoodsQueryVO> list = new ArrayList<ExpressCwbOrderForTakeGoodsQueryVO>();
		Set<Long> keySet = map.keySet();
		String idsId = "";
		String[] outStationTime = new String[map.size()];
		int i = 0;
		//获得所有查询出的id和时间
		for (Long obj : keySet) {
			idsId += "," + map.get(obj).getCwbId();
			outStationTime[i] = map.get(obj).getOutstationTime();
			i++;
		}
		i = 0;
		//		将对应的时间按id填入到对应的实体当中
		if (idsId.length() > 0) {
			idsId = idsId.substring(1, idsId.length());
			List<ExpressCwbOrderForTakeGoodsQueryVO> listaaa = this.cwbDao.queryCwbOrdersExpressTakeGoodsQueryLanJianChuZhan(idsId, cwb4TakeGoodsQuery.getPayWay());
			for (ExpressCwbOrderForTakeGoodsQueryVO expressCwbOrderForTakeGoodsQueryVO : listaaa) {
				expressCwbOrderForTakeGoodsQueryVO.setOutstationdatetime(outStationTime[i]);
				list.add(expressCwbOrderForTakeGoodsQueryVO);
				i++;
			}
		}

		return list;
	}

}
