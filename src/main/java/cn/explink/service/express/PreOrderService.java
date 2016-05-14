package cn.explink.service.express;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.pjwl.ExpressPreOrderDTO;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.UserDAO;
import cn.explink.dao.express.PreOrderDao;
import cn.explink.domain.Branch;
import cn.explink.domain.User;
import cn.explink.domain.VO.express.ExtralInfo4Address;
import cn.explink.domain.VO.express.PreOrderQueryDateVO;
import cn.explink.domain.VO.express.PreOrderQueryVO;
import cn.explink.domain.VO.express.UseridAndBranchnameVO;
import cn.explink.domain.express.ExpressOperationInfo;
import cn.explink.domain.express.ExpressPreOrderVOForDeal;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.express.AddressMatchEnum;
import cn.explink.enumutil.express.ExcuteStateEnum;
import cn.explink.enumutil.express.ExpressOperationEnum;
import cn.explink.enumutil.express.ExpressOrderStatusEnum;
import cn.explink.service.addressmatch.AddressMatchExpressService;
import cn.explink.service.express.tps.enums.FeedbackOperateTypeEnum;
import cn.explink.util.StringUtil;

import com.pjbest.deliveryorder.bizservice.PjDeliveryOrderService;
import com.pjbest.deliveryorder.bizservice.PjDeliveryOrderServiceHelper;
import com.pjbest.deliveryorder.service.PjSaleOrderFeedbackRequest;

/**
 * 预订单处理Service
 *
 * @author wangzy
 */
@Service
@Transactional
public class PreOrderService extends ExpressCommonService {
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	PreOrderDao preOrderDao;
	@Autowired
	UserDAO userDao;
	@Autowired
	TpsInterfaceExecutor tpsInterfaceExecutor;

	@Autowired
	AddressMatchExpressService addressMatchExpressService;

	/**
	 *
	 * @Title: getPreOrderInfo
	 * @description 预订单查询中请求数据库数据的方法
	 * @author 刘武强
	 * @date 2015年8月6日下午4:12:25
	 * @param @param preOrderQueryVO
	 * @param @param page
	 * @param @param pageNumber
	 * @param @return
	 * @return Map<String,Object>
	 * @throws
	 */
	public Map<String, Object> getPreOrderInfo(PreOrderQueryVO preOrderQueryVO, long page, int pageNumber) {
		List<UseridAndBranchnameVO> userInfo = this.preOrderDao.getUseridAndBranchnameInfo();
		Map<Integer, String> useridAndBranchnameMap = new HashMap<Integer, String>();
		// 把得到的站点信息放到一个map里面，以userid为key，branchname为value，这样就能避免双重for循环
		for (int i = 0; i < userInfo.size(); i++) {
			useridAndBranchnameMap.put(userInfo.get(i).getUserid(), userInfo.get(i).getBranchname());
		}
		Map<String, Object> map = this.preOrderDao.getPreOrderQueryInfo(ExpressOrderStatusEnum.Normal.getValue(), preOrderQueryVO.getStart(), preOrderQueryVO.getEnd(),
				preOrderQueryVO.getExcuteState(), preOrderQueryVO.getMobile(), preOrderQueryVO.getPreordercode(), preOrderQueryVO.getSender(), preOrderQueryVO.getStation(), page, pageNumber);
		List<PreOrderQueryDateVO> preorderInfo = (List<PreOrderQueryDateVO>) map.get("list");
		for (PreOrderQueryDateVO temp : preorderInfo) {
			// 给每个有反馈人id的预订单都set上反馈站点
			temp.setFeedbackBranchName(useridAndBranchnameMap.get(temp.getFeedbackUserId()));
		}
		map.put("list", preorderInfo);
		return map;
	}

	/**
	 *
	 * @Title: getBranchStations
	 * @description 获取站点
	 * @author 刘武强
	 * @date 2015年7月30日下午4:17:57
	 * @param flag
	 *            =true时，获取所有的站点；否则获取当前branchid对应的站点信息
	 * @return List
	 * @throws
	 */
	public List<Map<String, String>> getBranchStations(long branchid, boolean flag) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		if (flag) {
			// 获取所有的站点
			List<Branch> branchListinfo = this.branchDAO.getBranchBySiteType(BranchEnum.ZhanDian.getValue());
			// 把“全部”拼接进去
			Map<String, String> mapall = new HashMap<String, String>();
			mapall.put("key", "0");
			mapall.put("value", "");
			list.add(mapall);
			for (Branch temp : branchListinfo) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("key", temp.getBranchid() + "");
				map.put("value", temp.getBranchname());
				list.add(map);
			}
		} else {
			// 根据branchid获取站点信息
			Branch branch = this.branchDAO.getBranchByBranchid(branchid);
			Map<String, String> map = new HashMap<String, String>();
			map.put("key", branch.getBranchid() + "");
			map.put("value", branch.getBranchname());
			list.add(map);
		}
		return list;
	}

	/**
	 *
	 * @Title: getExcuteState
	 * @description 获取执行状态（不包括“未分配站点”这个状态）
	 * @author 刘武强
	 * @date 2015年7月30日下午4:13:22
	 * @param sgsflag
	 *            :如果sgsflag="shenggongsi",则需要“预订单关闭”和“退回总部”，否则要去掉
	 * @param @return
	 * @return List
	 * @throws
	 */
	public List<Map<String, Object>> getExcuteState(String sgsflag) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<ExcuteStateEnum> infolist = ExcuteStateEnum.getAllStatus();
		for (int i = 0; i < infolist.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			ExcuteStateEnum enmu = infolist.get(i);
			// 将“未分配站点”去掉
			if (enmu.getValue() == 0) {
				continue;
			}
			// 如果不是省公司，那么需要“预订单关闭”和“退回总部”，否则要去掉
			if (!"shenggongsi".equals(sgsflag)) {
				if ((enmu.getValue() == 8) || (enmu.getValue() == 9)) {
					continue;
				}
			}
			map.put("key", enmu.getValue());
			map.put("value", enmu.getText());
			list.add(map);
		}
		return list;
	}

	/**
	 * 查询预订单
	 *
	 * @author 王志宇
	 * @return
	 */
	public List<ExpressPreOrderVOForDeal> query(String preOrderNo, Integer status, Long page) {
		// 取出所有预订单
		List<ExpressPreOrderVOForDeal> list = this.preOrderDao.query(preOrderNo, status, page);
		// 取出所有站点
		List<Branch> listBranch = this.branchDAO.getAllBranches();

		int length = list.size();
		int branchLength = listBranch.size();
		for (int i = 0; i < length; i++) {
			this.userDao.getUserByUserid(list.get(i).getFeedbackUserId()).getBranchid();
			for (int a = 0; a < branchLength; a++) {
				long branchId = this.userDao.getUserByUserid(list.get(i).getFeedbackUserId()).getBranchid();
				if (branchId == 0) {
					list.get(i).setOperationBranchName("");
				}
				if (branchId == listBranch.get(a).getBranchid()) {
					list.get(i).setOperationBranchName(listBranch.get(a).getBranchname());
				}

			}
		}
		return list;
	}

	/**
	 * 查询预订单数量
	 *
	 * @author 王志宇
	 * @return
	 */
	public int queryCount(String preOrderNo, Integer status) {
		return this.preOrderDao.queryCount(preOrderNo, status);
	}

	/**
	 * 关闭预订单
	 *
	 * @author 王志宇
	 * @return
	 */
	public int updatePreOrederClose(String id, String preOrderNo, String reason, User user) {
		Boolean backFlag = true;
		Long userId = user.getUserid();
		String userName = user.getRealname();
		Date date = new Date(System.currentTimeMillis());
		int count = this.preOrderDao.updatePreOrderClose(id, userId, userName, date);

		String[] preOrderNoArray = preOrderNo.split(",");
		String[] reasonArray = reason.split(",");
		for (int a = 0; a < preOrderNoArray.length; a++) {
			// 调用tps接口
			backFlag = this.feedBack(user, preOrderNoArray[a], reasonArray.length == 0 ? "" : reasonArray[a], "close");
			if (!backFlag) {
				return 0;
			}
		}

		if (backFlag) {
			return count;
		}
		return 0;
	}

	/**
	 * 退回总部预订单
	 *
	 * @author 王志宇
	 * @return
	 */
	public int updatePreOrederReturn(String id, String preOrderNo, String reason, User user) {
		Boolean backFlag = true;
		Long userId = user.getUserid();
		String userName = user.getRealname();
		Date date = new Date(System.currentTimeMillis());
		int count = this.preOrderDao.updatePreOrderReturn(id, userId, userName, date);

		String[] preOrderNoArray = preOrderNo.split(",");
		String[] reasonArray = reason.split(",");
		for (int a = 0; a < preOrderNoArray.length; a++) {
			// 调接口返回状态到TPS
			backFlag = this.feedBack(user, preOrderNoArray[a], reasonArray.length == 0 ? "" : reasonArray[a], "return");
			if (!backFlag) {
				return 0;
			}

		}
		if (backFlag) {
			return count;
		}
		return 0;
	}

	/**
	 * 手动分配站点预订单
	 *
	 * @author 王志宇
	 * @return
	 */
	public int updatePreOrderHand(String id, int siteId, String siteName, String preOrderNo, String reason, User user) {
		Boolean backFlag = true;
		Long userId = user.getUserid();
		String userName = user.getRealname();
		Date date = new Date(System.currentTimeMillis());
		int count = this.preOrderDao.updatePreOrderHand(id, siteId, siteName, userId, userName, date);

		String[] preOrderNoArray = preOrderNo.split(",");
		// String[] reasonArray = reason.split(",");
		for (int a = 0; a < preOrderNoArray.length; a++) {
			// 调用tps接口

			backFlag = this.feedBack(user, preOrderNoArray[a], reason, "match");
			if (!backFlag) {

				return 0;
			}

		}
		if (backFlag) {
			return count;
		}
		return 0;

	}

	/**
	 * 新增预订单记录
	 *
	 * @param preOrderDto
	 * @param addressInfo
	 */
	public void insertPreOrder(ExpressPreOrderDTO preOrderDto, Map<String, Map<String, String>> addressInfo) {
		String detailAddresStr = this.transferAddressCode2DetailAddress(preOrderDto, addressInfo);
		// 匹配站点的逻辑
		Branch branch = this.matchDeliveryBranch(preOrderDto.getReserveOrderNo(), detailAddresStr);
		this.preOrderDao.insertPreOrderRecord(preOrderDto, detailAddresStr, branch);
	}

	/**
	 * 匹配站点
	 *
	 * @param cneeAddr
	 *            收件人地址
	 * @return
	 */
	private Branch matchDeliveryBranch(String cwb, String cneeAddr) {
		ExtralInfo4Address info = new ExtralInfo4Address(cwb, 1L, cneeAddr);
		// 匹配站点
		Branch branch = this.addressMatchExpressService.matchAddress4SinfferTransData(info);
		return branch;
	}

	/**
	 * 数据的转换
	 *
	 * @param preOrderDto
	 * @param addressInfo
	 * @return
	 */
	private String transferAddressCode2DetailAddress(ExpressPreOrderDTO preOrderDto, Map<String, Map<String, String>> addressInfo) {

		String province = StringUtil.nullConvertToEmptyString(addressInfo.get("province").get(preOrderDto.getCnorProv()));
		String city = StringUtil.nullConvertToEmptyString(addressInfo.get("city").get(preOrderDto.getCnorCity()));
		String region = StringUtil.nullConvertToEmptyString(addressInfo.get("county").get(preOrderDto.getCnorRegion()));
		String town = StringUtil.nullConvertToEmptyString(addressInfo.get("town").get(preOrderDto.getCnorTown()));
		String addres = preOrderDto.getCnorAddr();
		// 详细地址
		String detailAddress = province + city + region + town + addres;
		return detailAddress;
	}

	/**
	 * 更新预订单的状态为关闭状态
	 *
	 * @param preOrderDto
	 */
	public void updatePreOrderState(ExpressPreOrderDTO preOrderDto) {
		this.preOrderDao.updatePreOrderState2Closed(preOrderDto);
	}

	/**
	 * 自动匹配站点
	 *
	 * @author 王志宇
	 */
	public void autoMatch(User user, String preOrderNo, String address, String reason) {
		Boolean backFlag = true;
		Long userId = user.getUserid();
		String[] preOrderNoArray = preOrderNo.split(",");
		String[] addressArray = address.split(",");
		String[] reasonArray = reason.split(",");
		for (int i = 0; i < preOrderNoArray.length; i++) {
			// 匹配地址库
			Boolean matchFlag = this.tpsInterfaceExecutor.autoMatch(preOrderNoArray[i], userId, addressArray[i], AddressMatchEnum.PreOrderMatch.getValue());
			if (matchFlag) {
				// 返回tps
				backFlag = this.feedBack(user, preOrderNoArray[i], reasonArray.length == 0 ? "" : reasonArray[i], "match");
			}
		}

	}

	/**
	 * 调用tps接口-----状态反馈
	 *
	 * @author 王志宇
	 */
	public Boolean feedBack(User user, String preOrderNo, String reason, String mark) {
		PjDeliveryOrderService pjDeliveryOrderService = new PjDeliveryOrderServiceHelper.PjDeliveryOrderServiceClient();
		Boolean backFlag = true;
		// 根据站点id获取站点表的brachcode值
		Branch branch = this.branchDAO.getBranchByBranchid(user.getBranchid());
		try {
			PjSaleOrderFeedbackRequest req = new PjSaleOrderFeedbackRequest();
			req.setReserveOrderNo(preOrderNo);
			// 关闭预订单
			if (mark.equals("close")) {

				req.setOperateType(FeedbackOperateTypeEnum.ReserveClose.getValue());
			}
			// 退回总部的状态暂时用28来表示
			if (mark.equals("return")) {
				req.setOperateType(FeedbackOperateTypeEnum.ProvinceSurpass.getValue());
			}
			// 匹配站点
			if (mark.equals("match")) {
				req.setOperateType(FeedbackOperateTypeEnum.DistributionOrg.getValue());
			}
			req.setTransportNo("");
			req.setReason(reason);
			// 从站点表中获取brachcode字段的值
			req.setOperateOrg(branch.getTpsbranchcode());
			// req.setOperateOrg("gdfy");
			req.setOperater(user.getRealname());
			req.setOperateTime(System.currentTimeMillis());

			// 将反馈状态通知到TPS
			ExpressOperationInfo paramObj = new ExpressOperationInfo(ExpressOperationEnum.PreOrderFeedBack);
			// 设置预订单号
			paramObj.setReserveOrderNo(req.getReserveOrderNo());
			paramObj.setPreOrderfeedBack(req);
			this.tpsInterfaceExecutor.executTpsInterface(paramObj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return backFlag;
	}

	public void updateAllInfo(ExpressPreOrderDTO preOrderDto, Map<String, Map<String, String>> addressInfo) {
		String detailAddresStr = this.transferAddressCode2DetailAddress(preOrderDto, addressInfo);
		// 匹配站点的逻辑
		Branch branch = this.matchDeliveryBranch(preOrderDto.getReserveOrderNo(), detailAddresStr);
		this.preOrderDao.updatePreOrderRecord(preOrderDto, detailAddresStr, branch);
	}

	/**
	 * 先删除再插入
	 *
	 * @param preOrderDto
	 * @param addressInfo
	 */
	public void deleteAndInsert(ExpressPreOrderDTO preOrderDto, Map<String, Map<String, String>> addressInfo) {
		int record = this.preOrderDao.deleteRecordByPreOrderNo(preOrderDto.getReserveOrderNo());
		if (record > 0) {
			this.logger.info("操作提示：删除重复记录成功，预订单号为{0}", preOrderDto.getReserveOrderNo());
		}
		String detailAddresStr = this.transferAddressCode2DetailAddress(preOrderDto, addressInfo);
		// 匹配站点的逻辑
		Branch branch = this.matchDeliveryBranch(preOrderDto.getReserveOrderNo(), detailAddresStr);
		this.preOrderDao.insertPreOrderRecord(preOrderDto, detailAddresStr, branch);
	}

}
