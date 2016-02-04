/**
 *
 */
package cn.explink.service.mps;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cn.explink.core.utils.StringUtils;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.TransCwbDetailDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.TransCwbDetail;
import cn.explink.enumutil.IsmpsflagEnum;
import cn.explink.support.transcwb.TransCwbDao;

/**
 * @author songkaojun 2016年1月8日
 */
public abstract class AbstractMPSService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractMPSService.class);

	@Autowired
	private TransCwbDao transCwbDao;

	@Autowired
	private CwbDAO cwbDAO;

	@Autowired
	private CustomerDAO customerDAO;

	@Autowired
	private TransCwbDetailDAO transCwbDetailDAO;

	protected CwbOrder getMPSCwbOrderByTransCwb(String transCwb, String logPrefix) {
		if (StringUtils.isEmpty(transCwb)) {
			AbstractMPSService.LOGGER.error(logPrefix + "传入的运单号为空！");
			return null;
		}
		// 根据运单号查询订单
		String cwb = this.transCwbDetailDAO.getCwbByTransCwb(transCwb);
		if (StringUtils.isEmpty(cwb)) {
			AbstractMPSService.LOGGER.debug(logPrefix + "根据传入的运单号没有查询到相应的订单号！");
			return null;
		}
		return this.getMPSCwbOrderByCwb(cwb, logPrefix);
	}

	protected CwbOrder getMPSCwbOrderByCwb(String cwb, String logPrefix) {
		if (StringUtils.isEmpty(cwb)) {
			AbstractMPSService.LOGGER.error(logPrefix + "传入的订单号为空！");
			return null;
		}
		CwbOrder cwbOrder = this.cwbDAO.getCwborder(cwb);
		if (cwbOrder == null) {
			AbstractMPSService.LOGGER.error(logPrefix + "没有查询到订单数据！");
			return null;
		}

		// 判断订单是否是一票多件
		if (cwbOrder.getIsmpsflag() == IsmpsflagEnum.no.getValue()) {
			AbstractMPSService.LOGGER.debug(logPrefix + cwbOrder.getCwb() + "不是一票多件订单，不处理！");
			return null;
		}
		return cwbOrder;
	}

	protected List<TransCwbDetail> getSiblingTransCwbDetailList(String transCwb, String cwb) {
		List<TransCwbDetail> transCwbDetailList = this.getTransCwbDetailDAO().getTransCwbDetailListByCwb(cwb);
		List<String> siblingTransCwbList = new ArrayList<String>();
		for (TransCwbDetail transCwbDetail : transCwbDetailList) {
			if (transCwbDetail.getTranscwb().equals(transCwb)) {
				continue;
			}
			siblingTransCwbList.add(transCwbDetail.getTranscwb());
		}
		return this.getTransCwbDetailDAO().getTransCwbDetailListByTransCwbList(siblingTransCwbList);
	}

	public TransCwbDao getTransCwbDao() {
		return this.transCwbDao;
	}

	public CwbDAO getCwbDAO() {
		return this.cwbDAO;
	}

	public CustomerDAO getCustomerDAO() {
		return this.customerDAO;
	}

	public TransCwbDetailDAO getTransCwbDetailDAO() {
		return this.transCwbDetailDAO;
	}

}
