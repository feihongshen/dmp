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
		List<BranchRoute> allBranchRoute = branchRouteDAO.getAllBranchRoute();
		for (BranchRoute branchRoute : allBranchRoute) {
			switch (branchRoute.getType()) {
			case 2:
				tempbranchRoutes.put(branchRoute.getFromBranchId(), addToRouts(tempbranchRoutes, branchRoute));
				break;

			case 3:
				tempbranchbackRoutes.put(branchRoute.getFromBranchId(), addToRouts(tempbranchbackRoutes, branchRoute));
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
		List<Long> children = branchRoutes.get(currentBranchid) == null ? new ArrayList<Long>() : branchRoutes.get(currentBranchid);
		int routtype = 0;
		for (long routes : children) {
			if (routes == destinationid) {// 有
				routtype = 1;
				break;
			}
		}
		if (routtype == 0) {
			Branch branch = branchDAO.getBranchByBranchid(destinationid);
			if (branch != null && !"2".equals(branch.getContractflag())) {// 如果不是二及站，直接返回0
				return 0L;
			}
		}

		List<Long> nextBranchList = getNextBranch(currentBranchid, destinationid, new ArrayList<Long>(), new ArrayList<Long>(), -1);
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
		List<Long> children = branchRoutes.get(currentBranchid) == null ? new ArrayList<Long>() : branchRoutes.get(currentBranchid);
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
			samsaraNextBranchidList = getNextBranch(childid, destinationid, currentRoutes, samsaraNextBranchidList, times);
			// 如果 计算出的路程List长度为0或者最后一站id为0，则认为此路线无效
			if (samsaraNextBranchidList.size() > 0 && samsaraNextBranchidList.get(samsaraNextBranchidList.size() - 1) > 0) {
				samsaraNextBranchidList.set(times, childid);
				// 如果排序使用的路程List最后一站id是0，则表示从未获得计算数据，则进行初始化
				if (orderBySamsaraNextBranchidList.size() == 0 || orderBySamsaraNextBranchidList.get(orderBySamsaraNextBranchidList.size() - 1) == 0) {

					orderBySamsaraNextBranchidList = samsaraNextBranchidList;

					// 如果 排序使用的路程List长度大于0，并且递归返回的路程List长度小于排序使用的List
					// 那么就更新排序list
				} else if (orderBySamsaraNextBranchidList.size() > 0 && samsaraNextBranchidList.size() < orderBySamsaraNextBranchidList.size()) {

					orderBySamsaraNextBranchidList = samsaraNextBranchidList;
				}
				if (times == 0 && samsaraNextBranchidList.size() == 2) {// 在筛选过程中，如果获得的列表距离为2，则表示已经是递归取值中最短路程了。
					return orderBySamsaraNextBranchidList;
				}
			}

		}
		return orderBySamsaraNextBranchidList;
	}

	public long getPreviousBranch(long currentBranchid, long destinationid) {
		long previousBranch = getPreviousBranch(currentBranchid, destinationid, new ArrayList<Long>());
		return previousBranch == currentBranchid ? 0 : previousBranch;
	}

	private long getPreviousBranch(long currentBranchid, long destinationid, List<Long> routes) {
		List<Long> children = branchbackRoutes.get(currentBranchid) == null ? new ArrayList<Long>() : branchbackRoutes.get(currentBranchid);
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
			List<Long> nextBranchList = getNextBranch(childid, destinationid, currentRoutes, new ArrayList<Long>(), -1);
			long nextBranch = nextBranchList.size() > 0 ? nextBranchList.get(0) : 0L;
			if (nextBranch != 0) {
				return childid;
			}
		}
		return 0;
	}

	public List<Long> getNextPossibleBranch(long currentBranchid) {
		return branchRoutes.get(currentBranchid) == null ? new ArrayList<Long>() : branchRoutes.get(currentBranchid);
	}

	public List<Long> getNextPossibleBackBranch(long currentBranchid) {
		return branchbackRoutes.get(currentBranchid) == null ? new ArrayList<Long>() : branchbackRoutes.get(currentBranchid);
	}

	@Transactional
	public void saveNextPossileBranch(long currentBranchid, List<Long> possibleNextBranchIds) {
		branchRouteDAO.deleteBranchRouteBatch(currentBranchid, BranchRouteEnum.JinZhengXiang.getValue());
		for (long nextBranchId : possibleNextBranchIds) {
			branchRouteDAO.creBranchRoute(currentBranchid, nextBranchId, BranchRouteEnum.JinZhengXiang.getValue());
		}
		reload();
	}

	@Transactional
	public void saveNextPossileBackBranch(long currentBranchid, List<Long> possibleNextBackBranchIds) {
		branchRouteDAO.deleteBranchRouteBatch(currentBranchid, BranchRouteEnum.JinDaoXiang.getValue());
		for (long nextBranchId : possibleNextBackBranchIds) {
			branchRouteDAO.creBranchRoute(currentBranchid, nextBranchId, BranchRouteEnum.JinDaoXiang.getValue());
		}
		reload();
	}

}
