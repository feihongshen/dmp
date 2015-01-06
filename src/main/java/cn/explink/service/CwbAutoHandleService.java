package cn.explink.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.EmailDateDAO;
import cn.explink.dao.ExceptionCwbDAO;
import cn.explink.dao.GroupDetailDao;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.StockResultDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.User;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.CwbFlowOrderTypeEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;

@Service
public class CwbAutoHandleService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	EmailDateDAO emailDateDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	StockResultDAO stockResultDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	GroupDetailDao groupDetailDAO;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	ExceptionCwbDAO exceptionCwbDAO;
	@Autowired
	UserDAO userDAO;

	@Autowired
	CwbOrderService cwborderService;

	/**
	 * 根据传入的参数判断需要自动补充哪个环节
	 * 
	 * @param user
	 * @param nowflowordertype
	 * @param co
	 * @param requestbatchno
	 */
	public CwbOrder autoSupplyLink(User user, long nowflowordertype, CwbOrder co, long requestbatchno, String scancwb, boolean iszhongzhuanout) {
		Long credate = System.currentTimeMillis();
		Long everyAdd = 1000L;
		credate -= everyAdd;// 如果是自动补充环节，则要比当前时间提前5分钟

		String flowvalue = "";
		String cwbfloworder = "";
		for (CwbFlowOrderTypeEnum ce : CwbFlowOrderTypeEnum.values()) {
			if (nowflowordertype == ce.getValue()) {
				flowvalue = ce.getText();
			}

			if (co.getFlowordertype() == ce.getValue()) {
				cwbfloworder = ce.getText();
			}

		}
		logger.info("系统自动补充环节功能开始,cwb:{},客户现在的操作类型：{},订单的当前状态是：" + cwbfloworder + "", co.getCwb(), flowvalue);
		String comment = "系统自动处理";

		// 当前站点信息
		Branch localbranch = branchDAO.getBranchByBranchid(user.getBranchid());
		logger.info("当前站点,branchid:{},当前站点类型：{}", localbranch.getBranchid(), localbranch.getSitetype());
		logger.info("开始自动补流程前startbranchid:{},nextbranchid：{},currentbranchid:" + co.getCurrentbranchid() + "", co.getStartbranchid(), co.getNextbranchid());

		if (nowflowordertype == FlowOrderTypeEnum.RuKu.getValue() && localbranch.getSitetype() == BranchEnum.KuFang.getValue()) {
			logger.info("库房入库扫描：startbranchid:{},nextbranchid：{},currentbranchid:" + co.getCurrentbranchid() + "", co.getStartbranchid(), co.getNextbranchid());
			if (co.getFlowordertype() == FlowOrderTypeEnum.RuKu.getValue() && co.getCurrentbranchid() != user.getBranchid()) {
				// 当订单当前状态为非当前站点入库操作时，自动补充他站出库、当前站入库环节
				co = isOutWarehouse(user, co, requestbatchno, comment, scancwb, iszhongzhuanout, credate);

			}
		} else if (nowflowordertype == FlowOrderTypeEnum.ChuKuSaoMiao.getValue() && localbranch.getSitetype() == BranchEnum.KuFang.getValue()) {
			logger.info("库房出库扫描：startbranchid:{},nextbranchid：{},currentbranchid:" + co.getCurrentbranchid() + "", co.getStartbranchid(), co.getNextbranchid());
			if (co.getFlowordertype() == FlowOrderTypeEnum.DaoRuShuJu.getValue()) {
				// 当订单当前状态为导入数据时，自动补充入库环节
				co = isIntoWarehouse(user, user.getBranchid(), co, requestbatchno, comment, scancwb, credate);
			} else if (co.getFlowordertype() == FlowOrderTypeEnum.TiHuo.getValue() || co.getFlowordertype() == FlowOrderTypeEnum.TiHuoYouHuoWuDan.getValue()) {
				// 当订单当前状态为提货、提货有货无单时，自动补充入库环节
				co = isIntoWarehouse(user, user.getBranchid(), co, requestbatchno, comment, scancwb, credate);
			} else if (co.getFlowordertype() == FlowOrderTypeEnum.RuKu.getValue() && co.getCurrentbranchid() != user.getBranchid()) {
				// 当订单当前状态为非当前站点入库操作时，自动补充他站出库、当前站入库环节
				credate -= everyAdd;
				co = isOutWarehouse(user, co, requestbatchno, comment, scancwb, iszhongzhuanout, credate);
				credate += everyAdd;
				co = isIntoWarehouse(user, user.getBranchid(), co, requestbatchno, comment, scancwb, credate);
			} else if (co.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue() && co.getStartbranchid() != user.getBranchid()) {
				// 当订单当前状态为非当前站点的出库操作时，自动补充当前站点的入库环节
				co = isIntoWarehouse(user, user.getBranchid(), co, requestbatchno, comment, scancwb, credate);
			}
			if (co.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()) {
				// 当订单当前状态是分站到货时，自动补充出库（中转出站）、入库（中转站）环节
				credate -= everyAdd;
				co = isOutWarehouse(user, co, requestbatchno, comment, scancwb, iszhongzhuanout, credate);
				credate += everyAdd;
				co = isIntoWarehouse(user, user.getBranchid(), co, requestbatchno, comment, scancwb, credate);
			}

		} else if (nowflowordertype == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() || nowflowordertype == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()) {
			logger.info("分站到货扫描或者到错货扫描：startbranchid:{},nextbranchid：{},currentbranchid:" + co.getCurrentbranchid() + "", co.getStartbranchid(), co.getNextbranchid());
			if (co.getFlowordertype() == FlowOrderTypeEnum.DaoRuShuJu.getValue()) {
				// 当订单当前状态为导入数据时，自动补充入库、出库环节
				credate -= everyAdd;
				co = isIntoWarehouse(user, co.getNextbranchid(), co, requestbatchno, comment, scancwb, credate);
				credate += everyAdd;
				co = isOutWarehouse(user, co, requestbatchno, comment, scancwb, iszhongzhuanout, credate);
				Branch b = branchDAO.getBranchByBranchid(co.getCurrentbranchid());
				long flowtye = FlowOrderTypeEnum.ChuKuSaoMiao.getValue();
				if (b != null) {
					if (b.getSitetype() == BranchEnum.KuFang.getValue()) {
						flowtye = FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue();
					}
				}
				groupDetailDAO.creGroupDetail(co.getCwb(), 0, user.getUserid(), flowtye, co.getStartbranchid(), co.getNextbranchid(), co.getDeliverid(), co.getCustomerid(),0,0,"");

			} else if (co.getFlowordertype() == FlowOrderTypeEnum.RuKu.getValue()) {
				// 当订单当前状态为入库时，自动补充出库环节
				co = isOutWarehouse(user, co, requestbatchno, comment, scancwb, iszhongzhuanout, credate);
				groupDetailDAO.creGroupDetail(co.getCwb(), 0, user.getUserid(), FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), Long.parseLong(co.getCarwarehouse()), co.getNextbranchid(),
						co.getDeliverid(), co.getCustomerid(),0,0,"");
			}/*
			 * else
			 * if(co.getFlowordertype()==FlowOrderTypeEnum.ChuKuSaoMiao.getValue
			 * ()&&co.getNextbranchid()!=user.getBranchid()){
			 * //当订单当前状态是出库时，出库到的下一站不等于当前站的时候自动补充到错货 co = isDaoCuoHuo(user, co,
			 * requestbatchno, comment); }
			 */

		} else if (nowflowordertype == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
			logger.info("分站领货扫描：startbranchid:{},nextbranchid：{},currentbranchid:" + co.getCurrentbranchid() + "", co.getStartbranchid(), co.getNextbranchid());
			if (co.getFlowordertype() == FlowOrderTypeEnum.DaoRuShuJu.getValue()) {
				// 当订单当前状态是导入数据时，自动补充入库、出库、分站到货环节
				credate -= everyAdd * 2;
				co = isIntoWarehouse(user, co.getNextbranchid(), co, requestbatchno, comment, scancwb, credate);
				credate += everyAdd;
				co = isOutWarehouse(user, co, requestbatchno, comment, scancwb, iszhongzhuanout, credate);
				credate += everyAdd;
				co = isSubstationGoods(user, user.getBranchid(), co, requestbatchno, comment, scancwb, credate);
			} else if (co.getFlowordertype() == FlowOrderTypeEnum.RuKu.getValue()) {
				// 当订单当前状态是入库时，自动补充出库、分站到货环节
				credate -= everyAdd;
				co = isOutWarehouse(user, co, requestbatchno, comment, scancwb, iszhongzhuanout, credate);
				credate += everyAdd;
				co = isSubstationGoods(user, user.getBranchid(), co, requestbatchno, comment, scancwb, credate);
			} else if (co.getCurrentbranchid() != user.getBranchid() && co.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()) {
				// 当订单当前状态是非本站的到错货入库时，自动补充出库、分站到货环节
				credate -= everyAdd;
				co = isOutWarehouse(user, co, requestbatchno, comment, scancwb, iszhongzhuanout, credate);
				credate += everyAdd;
				co = isSubstationGoods(user, user.getBranchid(), co, requestbatchno, comment, scancwb, credate);
			} else if (co.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue() || co.getFlowordertype() == FlowOrderTypeEnum.DaoCuoHuoChuLi.getValue()) {
				// 当订单当前状态是出库时，自动补充分站到货环节
				/*
				 * if(co.getNextbranchid()!=user.getBranchid()){ co =
				 * isDaoCuoHuo(user, co, requestbatchno, comment); }else{
				 */
				co = isSubstationGoods(user, user.getBranchid(), co, requestbatchno, comment, scancwb, credate);
				// }

			}
		} else if (nowflowordertype == FlowOrderTypeEnum.RuKu.getValue() && branchDAO.getBranchByBranchid(user.getBranchid()).getSitetype() == BranchEnum.ZhongZhuan.getValue()) {
			logger.info("中转站入库扫描：startbranchid:{},nextbranchid：{},currentbranchid:" + co.getCurrentbranchid() + "", co.getStartbranchid(), co.getNextbranchid());
			if (co.getFlowordertype() == FlowOrderTypeEnum.DaoRuShuJu.getValue()) {
				// 当订单当前状态是导入数据时，自动补充入库、出库环节
				credate -= everyAdd;
				co = isIntoWarehouse(user, co.getNextbranchid(), co, requestbatchno, comment, scancwb, credate);
				credate += everyAdd;
				co = isOutWarehouse(user, co, requestbatchno, comment, scancwb, iszhongzhuanout, credate);
			} else if (co.getFlowordertype() == FlowOrderTypeEnum.RuKu.getValue() && co.getCurrentbranchid() != user.getBranchid()) {
				// 当订单当前状态为非当前站点入库操作时，自动补充他站出库环节
				co = isOutWarehouse(user, co, requestbatchno, comment, scancwb, iszhongzhuanout, credate);
			}
			if (co.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()) {
				// 当订单当前状态是分站到货时，自动补充出库（中转出站）环节
				co = isOutWarehouse(user, co, requestbatchno, comment, scancwb, iszhongzhuanout, credate);
			}
		} else if (nowflowordertype == FlowOrderTypeEnum.ChuKuSaoMiao.getValue() && localbranch.getSitetype() == BranchEnum.ZhongZhuan.getValue()) {
			logger.info("中转站出库扫描startbranchid:{},nextbranchid：{},currentbranchid:" + co.getCurrentbranchid() + "", co.getStartbranchid(), co.getNextbranchid());
			if (co.getFlowordertype() == FlowOrderTypeEnum.DaoRuShuJu.getValue()) {
				// 当订单当前状态为导入数据时，自动补充入库、出库、中转站入库环节
				credate -= everyAdd * 2;
				co = isIntoWarehouse(user, co.getNextbranchid(), co, requestbatchno, comment, scancwb, credate);
				credate += everyAdd;
				co = isOutWarehouse(user, co, requestbatchno, comment, scancwb, iszhongzhuanout, credate);
				credate += everyAdd;
				co = isIntoWarehouse(user, user.getBranchid(), co, requestbatchno, comment, scancwb, credate);
			} else if (co.getFlowordertype() == FlowOrderTypeEnum.RuKu.getValue() && co.getCurrentbranchid() != user.getBranchid()) {
				// 当订单当前状态为非当前站点入库操作时，自动补充他站出库、当前站入库环节
				credate -= everyAdd;
				co = isOutWarehouse(user, co, requestbatchno, comment, scancwb, iszhongzhuanout, credate);
				credate += everyAdd;
				co = isIntoWarehouse(user, user.getBranchid(), co, requestbatchno, comment, scancwb, credate);
			} else if (co.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue() && co.getStartbranchid() != user.getBranchid()) {
				// 当订单当前状态为非当前站点的出库操作时，自动补充当前站点的入库环节
				co = isIntoWarehouse(user, user.getBranchid(), co, requestbatchno, comment, scancwb, credate);
			}
			if (co.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()) {
				// 当订单当前状态是分站到货时，自动补充出库（中转出站）、入库（中转站）环节
				credate -= everyAdd;
				co = isOutWarehouse(user, co, requestbatchno, comment, scancwb, iszhongzhuanout, credate);
				credate += everyAdd;
				co = isIntoWarehouse(user, user.getBranchid(), co, requestbatchno, comment, scancwb, credate);
			}
		}
		logger.info("自动补充结束--cwborder：{}", co);
		return co;
	}

	/*
	 * 自动调取入库的方法
	 */

	private CwbOrder isIntoWarehouse(User user, long branchid, CwbOrder co, long requestbatchno, String comment, String scancwb, Long credate) {
		co = cwborderService.intoWarehousHandle(user, co.getCwb(), scancwb, branchid, co.getCustomerid(), 0, requestbatchno, comment, true, "", credate, false);
		return co;
	}

	/*
	 * 自动调取分站到货的方法
	 */

	private CwbOrder isSubstationGoods(User user, long branchid, CwbOrder co, long requestbatchno, String comment, String scancwb, Long credate) {
		co = cwborderService.substationGoodsHandle(user, co.getCwb(), scancwb, branchid, 0, requestbatchno, comment, true, "", credate, false);
		return co;
	}

	/*
	 * 自动调取出库的方法
	 */

	private CwbOrder isOutWarehouse(User user, CwbOrder co, long requestbatchno, String comment, String scancwb, boolean iszhongzhuanout, Long credate) {
		Boolean forceOut = false;
		long branchid = user.getBranchid();
		if (co.getNextbranchid() != branchid) {
			forceOut = true;
		}
		co = cwborderService.outWarehousHandle(user, co.getCwb(), scancwb, co.getCurrentbranchid(), 0, 0, branchid, requestbatchno, forceOut, comment, "", true, 0, iszhongzhuanout, credate, false);
		return co;
	}

}
