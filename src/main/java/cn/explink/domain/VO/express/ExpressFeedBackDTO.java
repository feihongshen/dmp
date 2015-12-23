package cn.explink.domain.VO.express;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.explink.enumutil.express.ExcuteStateEnum;
/**
 * 揽件反馈
 * @author jiangyu 2015年8月6日
 *
 */
public class ExpressFeedBackDTO {

	private Logger logger = LoggerFactory.getLogger(ExpressFeedBackDTO.class);

	// ----------- 已反馈订单 ------------[成功，失败，站点超区，揽件超区，延迟揽件]
	private long feedBackSuccess = 0; // 反馈成功
	private long feedBackSuccessNotDeal = 0; // 反馈成功 暂不处理
	private List<ExpressFeedBackView> feedBackSuccessList = new ArrayList<ExpressFeedBackView>();

	private long feedBackFailed = 0; // 反馈失败
	private long feedBackFailedNotDeal = 0; // 反馈失败 暂不处理
	private List<ExpressFeedBackView> feedBackFailedList = new ArrayList<ExpressFeedBackView>();

	private long feedBackAreaWrong = 0; // 反馈为站点超区
	private long feedBackAreaWrongNotDeal = 0; // 反馈为站点超区 暂不处理
	private List<ExpressFeedBackView> feedBackAreaWrongList = new ArrayList<ExpressFeedBackView>();

	private long feedBackPickWrong = 0; // 反馈为揽件超区
	private long feedBackPickWrongNotDeal = 0; // 反馈为揽件超区 暂不处理
	private List<ExpressFeedBackView> feedBackPickWrongList = new ArrayList<ExpressFeedBackView>();

	private long feedBackDelayPicked = 0;// 拒收
	private long feedBackDelayPickedNotDeal = 0;// 拒收暂不处理
	private List<ExpressFeedBackView> feedBackDelayPickedList = new ArrayList<ExpressFeedBackView>();
	// ----------- 已反馈订单 end ------------

	// ----------- 未反馈订单 ------------
	private long unFeedBack = 0;

	private List<ExpressFeedBackView> unFeedBackList = new ArrayList<ExpressFeedBackView>();

	private long surplus = 0; // 遗留
	private List<ExpressFeedBackView> surplusList = new ArrayList<ExpressFeedBackView>();
	// ----------- 未反馈订单 end ------------

	// ----------- 历史未归班审核订单 ------------
	private long historyNotAudit = 0; // 跟已反馈放到一起
	private List<ExpressFeedBackView> historyNotAuditList = new ArrayList<ExpressFeedBackView>();
	// ----------- 历史未归班审核订单 end ------------
	// ----------- 暂不处理的订单 ------------
	private long notDeal = 0;

	// ----------- 暂不处理的订单 end ------------

	/**
	 * @return 全部订单记录 不包括 暂不处理 和 历史未归班
	 */
	public long getAllNumber() {
		return getNowNumber() + surplus + historyNotAudit + notDeal;
	}

	/**
	 * @return 今日领货
	 */
	public long getNowNumber() {
		return getAlreadyFeedBackCount() + unFeedBack - historyNotAudit - notDeal;
	}

	/**
	 * @return 已反馈货物数
	 */
	public long getAlreadyFeedBackCount() {
		return feedBackSuccess + feedBackFailed + feedBackAreaWrong + feedBackPickWrong + feedBackDelayPicked;
	}

	/**
	 * @return 审核的单数
	 */
	public long getSubNumber() {
		return getAlreadyFeedBackCount() - notDeal;
	}

	/**
	 * @return 未反馈货物数
	 */
	public long getUnfeedBackNumber() {
		return unFeedBack + surplus;
	}

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 统计每个状态的数量和明细
	 * 
	 * @param preOrderAndFeedBackState
	 */
	public void analysisFeedBackStateList(List<ExpressFeedBackView> preOrderAndFeedBackState) {

		for (ExpressFeedBackView efb : preOrderAndFeedBackState) {
			if (efb.getDistributeDelivermanTime()==null) {
				continue;
			}
			String allocateTimeStr = sdf.format(efb.getDistributeDelivermanTime()).substring(0, 10);
			// 未反馈
			if (efb.getExcuteState() == ExcuteStateEnum.AllocatedDeliveryman.getValue()) {// 已分配小件员==未反馈
				if (sdf.format(new Date()).equals(allocateTimeStr)) {// 如果是当天领的货
					unFeedBack++;
				} else {// 否则为历史未反馈订单 也就是遗留订单
					surplus++;
					surplusList.add(efb);
				}
				unFeedBackList.add(efb);
				continue;
			}
			// 是否需要暂不处理的功能
			if (efb.getGcaid() == -1) {
				notDeal++;
			} else if (!sdf.format(new Date()).equals(allocateTimeStr)) {// 如果不是当天领的货
				historyNotAudit++;
				historyNotAuditList.add(efb);
			}
			
			// 反馈为成功
			if (efb.getExcuteState() == ExcuteStateEnum.Succeed.getValue()) {
				feedBackSuccess++;
				feedBackSuccessList.add(efb);
				if (efb.getGcaid() == -1) {// 暂不处理
					feedBackSuccessNotDeal++;
				}
				// 失败
			} else if (efb.getExcuteState() == ExcuteStateEnum.fail.getValue()) {
				feedBackFailed++;
				feedBackFailedList.add(efb);
				if (efb.getGcaid() == -1) {
					feedBackFailedNotDeal++;
				}
				// 站点超区
			} else if (efb.getExcuteState() == ExcuteStateEnum.StationSuperzone.getValue()) {
				feedBackAreaWrong++;
				feedBackAreaWrongList.add(efb);
				if (efb.getGcaid() == -1) {
					feedBackAreaWrongNotDeal++;
				}
				// 揽件超区
			} else if (efb.getExcuteState() == ExcuteStateEnum.EmbraceSuperzone.getValue()) {
				feedBackPickWrong++;
				feedBackPickWrongList.add(efb);
				if (efb.getGcaid() == -1) {
					feedBackPickWrongNotDeal++;
				}
				// 延迟揽件
			} else if (efb.getExcuteState() == ExcuteStateEnum.DelayedEmbrace.getValue()) {
				feedBackDelayPicked++;
				feedBackDelayPickedList.add(efb);
				if (efb.getGcaid() == -1) {
					feedBackDelayPickedNotDeal++;
				}
			} else {
				logger.error("归班审核时为统计到的订单编号:{}", efb.getPreOrderNo());
				continue;
			}
		}
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public long getFeedBackSuccess() {
		return feedBackSuccess;
	}

	public void setFeedBackSuccess(long feedBackSuccess) {
		this.feedBackSuccess = feedBackSuccess;
	}

	public long getFeedBackSuccessNotDeal() {
		return feedBackSuccessNotDeal;
	}

	public void setFeedBackSuccessNotDeal(long feedBackSuccessNotDeal) {
		this.feedBackSuccessNotDeal = feedBackSuccessNotDeal;
	}

	public List<ExpressFeedBackView> getFeedBackSuccessList() {
		return feedBackSuccessList;
	}

	public void setFeedBackSuccessList(List<ExpressFeedBackView> feedBackSuccessList) {
		this.feedBackSuccessList = feedBackSuccessList;
	}

	public long getFeedBackFailed() {
		return feedBackFailed;
	}

	public void setFeedBackFailed(long feedBackFailed) {
		this.feedBackFailed = feedBackFailed;
	}

	public long getFeedBackFailedNotDeal() {
		return feedBackFailedNotDeal;
	}

	public void setFeedBackFailedNotDeal(long feedBackFailedNotDeal) {
		this.feedBackFailedNotDeal = feedBackFailedNotDeal;
	}

	public List<ExpressFeedBackView> getFeedBackFailedList() {
		return feedBackFailedList;
	}

	public void setFeedBackFailedList(List<ExpressFeedBackView> feedBackFailedList) {
		this.feedBackFailedList = feedBackFailedList;
	}

	public long getFeedBackAreaWrong() {
		return feedBackAreaWrong;
	}

	public void setFeedBackAreaWrong(long feedBackAreaWrong) {
		this.feedBackAreaWrong = feedBackAreaWrong;
	}

	public long getFeedBackAreaWrongNotDeal() {
		return feedBackAreaWrongNotDeal;
	}

	public void setFeedBackAreaWrongNotDeal(long feedBackAreaWrongNotDeal) {
		this.feedBackAreaWrongNotDeal = feedBackAreaWrongNotDeal;
	}

	public List<ExpressFeedBackView> getFeedBackAreaWrongList() {
		return feedBackAreaWrongList;
	}

	public void setFeedBackAreaWrongList(List<ExpressFeedBackView> feedBackAreaWrongList) {
		this.feedBackAreaWrongList = feedBackAreaWrongList;
	}

	public long getFeedBackPickWrong() {
		return feedBackPickWrong;
	}

	public void setFeedBackPickWrong(long feedBackPickWrong) {
		this.feedBackPickWrong = feedBackPickWrong;
	}

	public long getFeedBackPickWrongNotDeal() {
		return feedBackPickWrongNotDeal;
	}

	public void setFeedBackPickWrongNotDeal(long feedBackPickWrongNotDeal) {
		this.feedBackPickWrongNotDeal = feedBackPickWrongNotDeal;
	}

	public List<ExpressFeedBackView> getFeedBackPickWrongList() {
		return feedBackPickWrongList;
	}

	public void setFeedBackPickWrongList(List<ExpressFeedBackView> feedBackPickWrongList) {
		this.feedBackPickWrongList = feedBackPickWrongList;
	}

	public long getFeedBackDelayPicked() {
		return feedBackDelayPicked;
	}

	public void setFeedBackDelayPicked(long feedBackDelayPicked) {
		this.feedBackDelayPicked = feedBackDelayPicked;
	}

	public long getFeedBackDelayPickedNotDeal() {
		return feedBackDelayPickedNotDeal;
	}

	public void setFeedBackDelayPickedNotDeal(long feedBackDelayPickedNotDeal) {
		this.feedBackDelayPickedNotDeal = feedBackDelayPickedNotDeal;
	}

	public List<ExpressFeedBackView> getFeedBackDelayPickedList() {
		return feedBackDelayPickedList;
	}

	public void setFeedBackDelayPickedList(List<ExpressFeedBackView> feedBackDelayPickedList) {
		this.feedBackDelayPickedList = feedBackDelayPickedList;
	}

	public long getUnFeedBack() {
		return unFeedBack;
	}

	public void setUnFeedBack(long unFeedBack) {
		this.unFeedBack = unFeedBack;
	}

	public List<ExpressFeedBackView> getUnFeedBackList() {
		return unFeedBackList;
	}

	public void setUnFeedBackList(List<ExpressFeedBackView> unFeedBackList) {
		this.unFeedBackList = unFeedBackList;
	}

	public long getSurplus() {
		return surplus;
	}

	public void setSurplus(long surplus) {
		this.surplus = surplus;
	}

	public List<ExpressFeedBackView> getSurplusList() {
		return surplusList;
	}

	public void setSurplusList(List<ExpressFeedBackView> surplusList) {
		this.surplusList = surplusList;
	}

	public long getHistoryNotAudit() {
		return historyNotAudit;
	}

	public void setHistoryNotAudit(long historyNotAudit) {
		this.historyNotAudit = historyNotAudit;
	}

	public List<ExpressFeedBackView> getHistoryNotAuditList() {
		return historyNotAuditList;
	}

	public void setHistoryNotAuditList(List<ExpressFeedBackView> historyNotAuditList) {
		this.historyNotAuditList = historyNotAuditList;
	}

	public long getNotDeal() {
		return notDeal;
	}

	public void setNotDeal(long notDeal) {
		this.notDeal = notDeal;
	}

}
