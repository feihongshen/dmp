package cn.explink.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.BranchRouteDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.BranchRoute;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.BranchRouteEnum;

@Service
public class CwbRouteService {

	Map<Long, List<Long>> branchRoutes = new HashMap<Long, List<Long>>();

	Map<Long, List<Long>> branchbackRoutes = new HashMap<Long, List<Long>>();

	@Autowired
	BranchRouteDAO branchRouteDAO;
	@Autowired
	BranchDAO branchDAO;

	@PostConstruct
	public void reload() {
		Map<Long, List<Long>> tempbranchRoutes = new HashMap<Long, List<Long>>();
		Map<Long, List<Long>> tempbranchbackRoutes = new HashMap<Long, List<Long>>();
		List<BranchRoute> allBranchRoute = this.branchRouteDAO.getAllBranchRoute();
		for (BranchRoute branchRoute : allBranchRoute) {
			switch (branchRoute.getType()) {
			case 2:
				tempbranchRoutes.put(branchRoute.getFromBranchId(), this.addToRouts(tempbranchRoutes, branchRoute));
				break;

			case 3:
				tempbranchbackRoutes.put(branchRoute.getFromBranchId(), this.addToRouts(tempbranchbackRoutes, branchRoute));
				break;

			default:
				break;
			}
		}
		this.branchRoutes = tempbranchRoutes;
		this.branchbackRoutes = tempbranchbackRoutes;
	}

	private List<Long> addToRouts(Map<Long, List<Long>> temp, BranchRoute branchRoute) {
		List<Long> tolist = temp.get(branchRoute.getFromBranchId());
		if (tolist == null) {
			tolist = new ArrayList<Long>();
		}
		tolist.add(branchRoute.getToBranchId());
		return tolist;
	}

	public long getNextBranch(long currentBranchid, long destinationid) {
		if (destinationid == 0) {// 如果没有目的站 则直接返回0
			return 0L;
		}
		List<Long> children = this.branchRoutes.get(currentBranchid) == null ? new ArrayList<Long>() : this.branchRoutes.get(currentBranchid);
		int routtype = 0;
		for (long routes : children) {
			if (routes == destinationid) {// 有
				routtype = 1;
				break;
			}
		}
		if (routtype == 0) {
			Branch branch = this.branchDAO.getBranchByBranchid(destinationid);
			if ((branch != null) && !"1".equals(branch.getContractflag())) {// 如果不是二及站，直接返回0
				return 0L;
			}
		}

		List<Long> nextBranchList = this.getNextBranch(currentBranchid, destinationid, new ArrayList<Long>(), new ArrayList<Long>(), -1);
		long nextBranch = nextBranchList.size() > 0 ? nextBranchList.get(0) : 0L;
		return nextBranch == currentBranchid ? 0 : nextBranch;
	}

	/**
	 * 计算下一站
	 *
	 * @param currentBranchid
	 *            当前站点id
	 * @param destinationid
	 *            目的地站点id
	 * @param routes
	 *            需要配对的站点id
	 * @param NextBranchid
	 *            待筛选的下一站排序
	 * @param times
	 *            当前循环次数，用于比对和筛选最有效的路程
	 * @return
	 */
	private List<Long> getNextBranch(long currentBranchid, long destinationid, List<Long> routes, List<Long> NextBranchidList, int times) {
		times++;
		List<Long> children = this.branchRoutes.get(currentBranchid) == null ? new ArrayList<Long>() : this.branchRoutes.get(currentBranchid);
		for (long childid : children) {
			if (childid == destinationid) {// 如果匹配到了下一站，并且比之前匹配到的路线要短的话 就更新
				NextBranchidList.add(times, childid);
				return NextBranchidList;
			}
		}

		NextBranchidList.add(times, 0L);

		List<Long> orderBySamsaraNextBranchidList = NextBranchidList;
		if (times > 5) {// 限制获得下一站最高5级
			return orderBySamsaraNextBranchidList;
		}
		for (long childid : children) {
			if (routes.contains(childid)) {
				continue;
			}
			List<Long> currentRoutes = new ArrayList<Long>(routes);
			currentRoutes.add(childid);
			List<Long> samsaraNextBranchidList = new ArrayList<Long>(NextBranchidList);
			samsaraNextBranchidList = this.getNextBranch(childid, destinationid, currentRoutes, samsaraNextBranchidList, times);
			// 如果 计算出的路程List长度为0或者最后一站id为0，则认为此路线无效
			if ((samsaraNextBranchidList.size() > 0) && (samsaraNextBranchidList.get(samsaraNextBranchidList.size() - 1) > 0)) {
				samsaraNextBranchidList.set(times, childid);
				// 如果排序使用的路程List最后一站id是0，则表示从未获得计算数据，则进行初始化
				if ((orderBySamsaraNextBranchidList.size() == 0) || (orderBySamsaraNextBranchidList.get(orderBySamsaraNextBranchidList.size() - 1) == 0)) {

					orderBySamsaraNextBranchidList = samsaraNextBranchidList;

					// 如果 排序使用的路程List长度大于0，并且递归返回的路程List长度小于排序使用的List
					// 那么就更新排序list
				} else if ((orderBySamsaraNextBranchidList.size() > 0) && (samsaraNextBranchidList.size() < orderBySamsaraNextBranchidList.size())) {

					orderBySamsaraNextBranchidList = samsaraNextBranchidList;
				}
				if ((times == 0) && (samsaraNextBranchidList.size() == 2)) {// 在筛选过程中，如果获得的列表距离为2，则表示已经是递归取值中最短路程了。
					return orderBySamsaraNextBranchidList;
				}
			}

		}
		return orderBySamsaraNextBranchidList;
	}

	public long getPreviousBranch(long currentBranchid, long destinationid) {
		long previousBranch = this.getPreviousBranch(currentBranchid, destinationid, new ArrayList<Long>());
		return previousBranch == currentBranchid ? 0 : previousBranch;
	}

	private long getPreviousBranch(long currentBranchid, long destinationid, List<Long> routes) {
		List<Long> children = this.branchbackRoutes.get(currentBranchid) == null ? new ArrayList<Long>() : this.branchbackRoutes.get(currentBranchid);
		for (long childid : children) {
			if (childid == destinationid) {
				return childid;
			}
		}
		for (long childid : children) {
			if (routes.contains(childid)) {
				continue;
			}
			List<Long> currentRoutes = new ArrayList<Long>(routes);
			currentRoutes.add(childid);
			List<Long> nextBranchList = this.getNextBranch(childid, destinationid, currentRoutes, new ArrayList<Long>(), -1);
			long nextBranch = nextBranchList.size() > 0 ? nextBranchList.get(0) : 0L;
			if (nextBranch != 0) {
				return childid;
			}
		}
		return 0;
	}

	public List<Long> getNextPossibleBranch(long currentBranchid) {
		return this.branchRoutes.get(currentBranchid) == null ? new ArrayList<Long>() : this.branchRoutes.get(currentBranchid);
	}

	public List<Long> getNextPossibleBackBranch(long currentBranchid) {
		return this.branchbackRoutes.get(currentBranchid) == null ? new ArrayList<Long>() : this.branchbackRoutes.get(currentBranchid);
	}

	@Transactional
	public void saveNextPossileBranch(long currentBranchid, List<Long> possibleNextBranchIds) {
		this.branchRouteDAO.deleteBranchRouteBatch(currentBranchid, BranchRouteEnum.JinZhengXiang.getValue());
		for (long nextBranchId : possibleNextBranchIds) {
			this.branchRouteDAO.creBranchRoute(currentBranchid, nextBranchId, BranchRouteEnum.JinZhengXiang.getValue());
		}
		this.reload();
	}

	@Transactional
	public void saveNextPossileBackBranch(long currentBranchid, List<Long> possibleNextBackBranchIds) {
		this.branchRouteDAO.deleteBranchRouteBatch(currentBranchid, BranchRouteEnum.JinDaoXiang.getValue());
		for (long nextBranchId : possibleNextBackBranchIds) {
			this.branchRouteDAO.creBranchRoute(currentBranchid, nextBranchId, BranchRouteEnum.JinDaoXiang.getValue());
		}
		this.reload();
	}

	/**
	 * @throws Exception
	 * @Title: getNextInterceptBranch
	 * @description TODO
	 * @author 刘武强
	 * @date  2016年1月11日下午5:01:22
	 * @param  @param currentBranchid
	 * @param  @return
	 * @return  List<BranchRoute>
	 * @throws
	 */
	public List<Branch> getNextInterceptBranch(long currentBranchid) {
		List<BranchRoute> branchRouteList = this.branchRouteDAO.getNextBranch(currentBranchid, BranchRouteEnum.JinDaoXiang.getValue());
		List<Branch> branchList = this.getKufangByBranchRoutes(branchRouteList);
		return branchList;
	}

	/**
	 *
	 * @Title: getKufangByBranchRoutes
	 * @description 根据站点配置的流向站点，查询出他对应的退货站
	 * @author 刘武强
	 * @date  2016年1月12日上午9:47:18
	 * @param  @param branchList
	 * @param  @return
	 * @return  List<Branch>
	 * @throws
	 */
	private List<Branch> getKufangByBranchRoutes(List<BranchRoute> branchList) {
		List<Branch> list = new ArrayList<Branch>();
		StringBuffer inStr = new StringBuffer();
		inStr.append("('',");
		for (int i = 0; i < branchList.size(); i++) {
			String temp = branchList.get(i).getToBranchId() + "";
			inStr.append("'").append(temp).append("'").append(",");
		}
		list = this.branchDAO.getBranchsByBranchidAndType(inStr.substring(0, inStr.length() - 1) + ")", BranchEnum.TuiHuo.getValue());
		return list;
	}

}
