/**
 *
 */
package cn.explink.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.explink.core.utils.StringUtils;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.TranscwbOrderFlowDAO;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.orderflow.TranscwbOrderFlow;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.support.transcwb.TransCwbDao;
import cn.explink.support.transcwb.TranscwbView;

/**
 *
 *
 * @author songkaojun 2016年1月6日
 */
@Component
public class MPSOptStateService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MPSOptStateService.class);

	private static final String LOG_MSG_PREFIX = "[更新一票多件状态]";

	@Autowired
	private TransCwbDao transCwbDao;

	@Autowired
	private TranscwbOrderFlowDAO transcwbOrderFlowDAO;

	@Autowired
	private CwbDAO cwbDAO;

	@Autowired
	private CustomerDAO customerDAO;

	/**
	 *
	 * 根据传入的运单号，更新运单号所属订单一票多件状态和运单操作状态、当前机构和下一机构信息
	 *
	 * @param transCwb
	 *            运单号
	 * @param transcwboptstate
	 *            运单操作状态
	 * @param currentbranchid
	 *            当前机构
	 * @param nextbranchid
	 *            下一机构信息
	 */
	public void updateMPSInfo(String transCwb, FlowOrderTypeEnum transcwboptstate, long currentbranchid, long nextbranchid) {
		if (StringUtils.isEmpty(transCwb)) {
			MPSOptStateService.LOGGER.info(MPSOptStateService.LOG_MSG_PREFIX + "传入的运单号为空！");
			return;
		}
		// 1、根据运单号查询订单
		String cwb = this.transCwbDao.getCwbByTransCwb(transCwb);
		if (StringUtils.isEmpty(cwb)) {
			MPSOptStateService.LOGGER.info(MPSOptStateService.LOG_MSG_PREFIX + "根据传入的运单号没有查询到相应的订单号！");
			return;
		}
		CwbOrder cwbOrder = this.cwbDAO.getCwborder(cwb);
		if (cwbOrder == null) {
			MPSOptStateService.LOGGER.info(MPSOptStateService.LOG_MSG_PREFIX + "没有查询到订单数据！");
			return;
		}

		// 2、查询供应商
		long customerid = cwbOrder.getCustomerid();
		Customer customer = this.customerDAO.getCustomerById(customerid);
		if (customer.getCustomerid() == 0L) {
			MPSOptStateService.LOGGER.info(MPSOptStateService.LOG_MSG_PREFIX + "没有查询到订单的供应商信息！");
			return;
		}
		// customer.get

		// 2、获取集单是否开启

		List<TranscwbView> transCwbViewList = this.transCwbDao.getTransCwbByCwb(cwb);

		// 4、获取运单状态流程信息
		List<TranscwbOrderFlow> transcwbOrderFlowList = this.transcwbOrderFlowDAO.getTranscwbOrderFlowByCwb(cwb);
		// 5、计算出最晚的状态
		for (TranscwbOrderFlow transcwbOrderFlow : transcwbOrderFlowList) {

		}
		// 6、更新最晚状态到一票多件状态

	}
}
