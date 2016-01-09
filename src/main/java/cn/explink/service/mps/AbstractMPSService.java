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
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.TransCwbDetail;
import cn.explink.enumutil.IsmpsflagEnum;
import cn.explink.enumutil.MpsswitchTypeEnum;
import cn.explink.support.transcwb.TransCwbDao;
import cn.explink.support.transcwb.TranscwbView;

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

	private MpsswitchTypeEnum mpsswitchType;

	protected CwbOrder getCwbOrder(String transCwb, String logPrefix) {
		if (StringUtils.isEmpty(transCwb)) {
			AbstractMPSService.LOGGER.error(logPrefix + "传入的运单号为空！");
			return null;
		}
		// 根据运单号查询订单
		String cwb = this.transCwbDao.getCwbByTransCwb(transCwb);
		if (StringUtils.isEmpty(cwb)) {
			AbstractMPSService.LOGGER.error(logPrefix + "根据传入的运单号没有查询到相应的订单号！");
			return null;
		}
		CwbOrder cwbOrder = this.cwbDAO.getCwborder(cwb);
		if (cwbOrder == null) {
			AbstractMPSService.LOGGER.error(logPrefix + "没有查询到订单数据！");
			return null;
		}

		// 判断订单是否是一票多件
		if (cwbOrder.getIsmpsflag() == IsmpsflagEnum.no.getValue()) {
			AbstractMPSService.LOGGER.info(logPrefix + cwbOrder.getCwb() + "不是一票多件订单，不处理！");
			return null;
		}

		// 查询供应商，判断该供应商是否开启了集单模式
		long customerid = cwbOrder.getCustomerid();
		Customer customer = this.customerDAO.getCustomerById(customerid);
		if (customer.getCustomerid() == 0L) {
			AbstractMPSService.LOGGER.error(logPrefix + "没有查询到订单的供应商信息！");
			return null;
		}
		int mpsswitch = customer.getMpsswitch();
		if (mpsswitch == MpsswitchTypeEnum.WeiKaiQiJiDan.getValue()) {
			AbstractMPSService.LOGGER.info(logPrefix + customer.getCustomername() + "未启用集单模式！");
			return null;
		}
		this.mpsswitchType = MpsswitchTypeEnum.getByValue(mpsswitch);
		return cwbOrder;
	}

	protected List<TransCwbDetail> getSiblingTransCwbDetailList(String transCwb, String cwb) {
		List<TranscwbView> transCwbViewList = this.getTransCwbDao().getTransCwbByCwb(cwb);
		List<String> siblingTransCwbList = new ArrayList<String>();
		for (TranscwbView transcwbView : transCwbViewList) {
			if (transcwbView.getTranscwb().equals(transCwb)) {
				continue;
			}
			siblingTransCwbList.add(transcwbView.getTranscwb());
		}
		List<TransCwbDetail> siblingTransCwbDetailList = this.getTransCwbDetailDAO().getTransCwbDetailListByTransCwbList(siblingTransCwbList);
		return siblingTransCwbDetailList;
	}

	public MpsswitchTypeEnum getMpsswitchType() {
		return this.mpsswitchType;
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
