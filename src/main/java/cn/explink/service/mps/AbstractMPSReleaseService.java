/**
 *
 */
package cn.explink.service.mps;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.explink.domain.CwbOrder;
import cn.explink.domain.TransCwbDetail;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.MPSAllArrivedFlagEnum;
import cn.explink.exception.CwbException;
import cn.explink.support.transcwb.TranscwbView;

/**
 * @author songkaojun 2016年1月8日
 */
public abstract class AbstractMPSReleaseService extends AbstractMPSService {

	protected static final String VALIDATE_RELEASE_CONDITION = "[判断一票多件是否放行]";
	protected static final Set<Integer> BEFORE_INTOWAREHOUSE_STATE = new HashSet<Integer>();
	protected static final Set<Integer> BEFORE_SUBSTATION_GOODS_ARRIVED_STATE = new HashSet<Integer>();

	static {
		AbstractMPSReleaseService.BEFORE_INTOWAREHOUSE_STATE.add(FlowOrderTypeEnum.DaoRuShuJu.getValue());
		AbstractMPSReleaseService.BEFORE_INTOWAREHOUSE_STATE.add(FlowOrderTypeEnum.TiHuo.getValue());
		AbstractMPSReleaseService.BEFORE_INTOWAREHOUSE_STATE.add(FlowOrderTypeEnum.TiHuoYouHuoWuDan.getValue());

		AbstractMPSReleaseService.BEFORE_SUBSTATION_GOODS_ARRIVED_STATE.addAll(AbstractMPSReleaseService.BEFORE_INTOWAREHOUSE_STATE);
		AbstractMPSReleaseService.BEFORE_SUBSTATION_GOODS_ARRIVED_STATE.add(FlowOrderTypeEnum.RuKu.getValue());
		AbstractMPSReleaseService.BEFORE_SUBSTATION_GOODS_ARRIVED_STATE.add(FlowOrderTypeEnum.ChuKuSaoMiao.getValue());
	}

	/**
	 * 校验放行条件
	 *
	 * @param transCwb
	 */
	protected abstract void validateReleaseCondition(String transCwb);

	protected void validateMPS(String transCwb, CwbOrder cwbOrder, CwbException exception, Set<Integer> beforeStateSet) {
		int mpsallarrivedflag = cwbOrder.getMpsallarrivedflag();
		if (MPSAllArrivedFlagEnum.NO.getValue() == mpsallarrivedflag) {
			throw exception;
		} else {
			List<TranscwbView> transCwbViewList = this.getTransCwbDao().getTransCwbByCwb(cwbOrder.getCwb());
			List<String> siblingTransCwbList = new ArrayList<String>();
			for (TranscwbView transcwbView : transCwbViewList) {
				if (transcwbView.getTranscwb().equals(transCwb)) {
					continue;
				}
				siblingTransCwbList.add(transcwbView.getTranscwb());
			}
			List<TransCwbDetail> siblingTransCwbDetailList = this.getTransCwbDetailDAO().getTransCwbDetailListByTransCwbList(siblingTransCwbList);
			if (siblingTransCwbDetailList == null) {
				return;
			}
			for (TransCwbDetail siblingTransCwbDetail : siblingTransCwbDetailList) {
				if (beforeStateSet.contains(siblingTransCwbDetail.getTranscwboptstate())) {
					throw exception;
				}
			}
		}
	}

}
