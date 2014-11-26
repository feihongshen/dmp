package cn.explink.service;

import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.BackDetailDAO;
import cn.explink.dao.BranchDAO;
import cn.explink.domain.BackDetail;
import cn.explink.domain.Branch;
import cn.explink.domain.User;
import cn.explink.enumutil.BackTypeEnum;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;

@Service
public class BackDetailService {
	private Logger logger = LoggerFactory.getLogger(BackDetailService.class);

	@Autowired
	BackDetailDAO backDetailDAO;
	@Autowired
	BranchDAO branchDAO;

	/**
	 * 操作环节创建退货中心出入库跟踪表
	 * 
	 * @param user
	 * @param cwb
	 * @param flowordertype
	 * @param credate
	 */
	public void createBackDetail(User user, String cwb, int flowordertype, Long credate) {
		String nowtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(credate);
		Branch userbranch = branchDAO.getBranchByBranchid(user.getBranchid());

		// =========退货出站环节&&站点==========
		if (flowordertype == FlowOrderTypeEnum.TuiHuoChuZhan.getValue() && userbranch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
			// 站点设置了24/72小时失效
			if (userbranch.getBacktime() > 0) {
				long time24 = 0, time72 = 0;
				if (userbranch.getBacktime() == 24) {
					time24 = credate + (24 * 60 * 60 * 1000);// 24小时后的时间
					// time24=credate+(1*15*60*1000);//24小时后的时间
				} else {
					time72 = credate + (72 * 60 * 60 * 1000);// 72小时后的时间
					// time72=credate+(1*30*60*1000);//24小时后的时间
				}
				// 订单是否有退货出站数据
				BackDetail oldBackDetail = backDetailDAO.getBackDetail(cwb, user.getBranchid(), BackTypeEnum.TuiHuoChuZhan.getValue());
				if (oldBackDetail == null) {// 新增
					BackDetail backDetail = this.formForBackDetail(user.getBranchid(), cwb, time24, time72, BackTypeEnum.TuiHuoChuZhan.getValue(), 0, nowtime);
					long detailId = backDetailDAO.createBackDetail(backDetail);
					logger.info("===退货中心出入库跟踪表(退货出站-新增)===id:{},订单:{},站点:{},time24:{},time72:{}", new Object[] { detailId, cwb, userbranch.getBranchname(), time24, time72 });
				} else {// 修改
					backDetailDAO.updateBackDetail(user.getBranchid(), time24, time72, 0, oldBackDetail.getId(), nowtime);
					logger.info("===退货中心出入库跟踪表(退货出站-更新)===id:{},订单:{},站点:{},time24:{},time72:{}", new Object[] { oldBackDetail.getId(), cwb, userbranch.getBranchname(), time24, time72 });
				}
			}
		}

		// =========退货站==========
		if (userbranch.getSitetype() == BranchEnum.TuiHuo.getValue()) {
			// 更新订单为已入库状态
			backDetailDAO.updateTuiHuoZhanRuKu(cwb, BackTypeEnum.TuiHuoChuZhan.getValue());
			logger.info("===退货中心出入库跟踪表(退货已入库)===订单:" + cwb);

			// =========退供应商出库环节==========
			if (flowordertype == FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue()) {
				// 订单是否有退供应商出库数据
				BackDetail oldBackDetail = backDetailDAO.getBackDetailByCwb(cwb, BackTypeEnum.TuiGongHuoShangChuKu.getValue());
				if (oldBackDetail == null) {// 新增
					BackDetail backDetail = this.formForBackDetail(user.getBranchid(), cwb, 0, 0, BackTypeEnum.TuiGongHuoShangChuKu.getValue(), 0, nowtime);
					long detailId = backDetailDAO.createBackDetail(backDetail);
					logger.info("===退货中心出入库跟踪表(退供应商出库-新增)===id:{},订单:{},退货站:{},createtime:{}", new Object[] { detailId, cwb, userbranch.getBranchname(), nowtime });
				} else {// 修改
					backDetailDAO.updateBackDetail(user.getBranchid(), 0, 0, 0, oldBackDetail.getId(), nowtime);
					logger.info("===退货中心出入库跟踪表(退供应商出库-更新)===id:{},订单:{},退货站:{},createtime:{}", new Object[] { oldBackDetail.getId(), cwb, userbranch.getBranchname(), nowtime });
				}
			}
		}

		// =========库房==========
		if (userbranch.getSitetype() == BranchEnum.KuFang.getValue()) {
			// 订单是否有入库数据
			BackDetail oldBackDetail = backDetailDAO.getBackDetailByCwb(cwb, BackTypeEnum.KuFangRuKu.getValue());
			if (oldBackDetail == null) {// 新增
				BackDetail backDetail = this.formForBackDetail(user.getBranchid(), cwb, 0, 0, BackTypeEnum.KuFangRuKu.getValue(), 0, nowtime);
				long detailId = backDetailDAO.createBackDetail(backDetail);
				logger.info("===退货中心出入库跟踪表(库房入库-新增)===id:{},订单:{},库房:{},createtime:{}", new Object[] { detailId, cwb, userbranch.getBranchname(), nowtime });
			} else {// 修改
				backDetailDAO.updateBackDetail(user.getBranchid(), 0, 0, 0, oldBackDetail.getId(), nowtime);
				logger.info("===退货中心出入库跟踪表(库房入库-更新)===id:{},订单:{},库房:{},createtime:{}", new Object[] { oldBackDetail.getId(), cwb, userbranch.getBranchname(), nowtime });
			}
		}

	}

	public BackDetail formForBackDetail(long branchid, String cwb, long time24, long time72, int type, int intoflag, String nowtime) {
		BackDetail o = new BackDetail();
		o.setBranchid(branchid);
		o.setCwb(cwb);
		o.setTime24(time24);
		o.setTime72(time72);
		o.setType(type);
		o.setIntoflag(intoflag);
		o.setCreatetime(nowtime);
		return o;
	}

}
