package cn.explink.aspect;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.OverdueExMoDAO;
import cn.explink.domain.PrintcwbDetail;
import cn.explink.domain.User;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.util.DateTimeUtil;

/**
 * 上门退订单创建订单流程切面.
 *
 * @author zhaoshb
 * @since DMP3.0
 */
@Aspect
@Component
public class SmtOptTimeAspect {

	private Logger logger = LoggerFactory.getLogger(SmtOptTimeAspect.class);

	@Autowired
	private OverdueExMoDAO overdueExMODAO = null;

	private ExecutorService executeService = null;

	@After("execution(* cn.explink.dao.OrderFlowDAO.creAndUpdateOrderFlow(..))")
	public void afterCreateOrderFlow(JoinPoint point) {
		Object[] args = point.getArgs();
		OrderFlow orderFlow = (OrderFlow) args[0];
		String strCreateDate = DateTimeUtil.formatDate(orderFlow.getCredate());

		this.saveOverdueData(orderFlow, strCreateDate);
	}

	@Before("execution(* cn.explink.b2c.tools.DataImportDAO_B2c.insertCwbOrder_toTempTable(..))")
	public void afterImportData(JoinPoint point) {
		this.getLogger().info("执行上门退订单插入逻辑.");
		Object[] args = point.getArgs();
		CwbOrderDTO dto = (CwbOrderDTO) args[0];
		this.getLogger().info("执行上门退订单插入逻辑{订单类型:" + dto.getCwbordertypeid() + "}");
		if (dto.getCwbordertypeid() != CwbOrderTypeIdEnum.Shangmentui.getValue()) {
			return;
		}
		this.getLogger().info("执行上门退订单插入逻辑{订单类型:" + dto.getCwbordertypeid() + "}");
		this.getExecuteService().submit(new CreateOrderTask(dto));
	}

	@After("execution(* cn.explink.service.CwbOrderService.deliverStatePod(..))")
	public void afterFeedback(JoinPoint point) {
		Object[] args = point.getArgs();
		String cwb = (String) args[1];
		@SuppressWarnings("unchecked")
		Map<String, Object> params = (Map<String, Object>) args[3];
		BigDecimal receivedFee = (BigDecimal) params.get("infactfare");
		long deliverState = (Long) params.get("podresultid");

		this.submitTask(new UpdateDeliverStateTask(cwb, (int) deliverState, receivedFee));
	}

	@After("execution(* cn.explink.dao.PrintcwbDetailDAO.crePrintcwbDetail(..))")
	public void afterPrint(JoinPoint point) {
		Object[] args = point.getArgs();
		PrintcwbDetail cwb = (PrintcwbDetail) args[0];

		this.submitTask(new UpdatePrintTimeTask(cwb));
	}

	@After("execution(* cn.explink.dao.DeliveryStateDAO.creDeliveryState(..))")
	public void afterCreateDeliverState(JoinPoint point) {
		Object[] args = point.getArgs();
		int cwbordertypeid = (Integer) args[2];
		if (cwbordertypeid != CwbOrderTypeIdEnum.Shangmentui.getValue()) {
			return;
		}
		String cwb = (String) args[0];
		User delvier = (User) args[3];
		String strCreateTime = (String) args[4];

		this.submitTask(new UpdateDeliverInfoTask(cwb, delvier.getUserid(), strCreateTime));
	}

	@After("execution(* cn.explink.dao.OrderFlowDAO.batchOutArea(..))")
	public void afterReportOutArea(JoinPoint point) {
		Object[] args = point.getArgs();
		String[] cwbs = (String[]) args[0];
		String strTime = DateTimeUtil.getNowTime();

		this.submitTask(new UpdateOutAreaTimeTask(cwbs, strTime));
	}

	@After("execution(* cn.explink.service.MatchExceptionHandleService.redistributionBranch(..))")
	public void afterExceptionMatchHandle(JoinPoint point) {
		Object[] args = point.getArgs();
		String cwb = (String) args[0];
		String strTime = DateTimeUtil.getNowTime();

		this.submitTask(new UpdateMEHTimeTask(cwb, strTime));
	}

	@PostConstruct
	public void initExecutorService() {
		this.executeService = Executors.newFixedThreadPool(2);
	}

	private Logger getLogger() {
		return this.logger;
	}

	private void saveOverdueData(OrderFlow orderFlow, String strCreateDate) {
		this.getExecuteService().submit(new SaveTask(orderFlow, strCreateDate));
	}

	private void submitTask(Runnable task) {
		this.getExecuteService().submit(task);
	}

	private OverdueExMoDAO getOverdueExMODAO() {
		return this.overdueExMODAO;
	}

	private ExecutorService getExecuteService() {
		if (this.executeService == null) {
			this.executeService = Executors.newFixedThreadPool(2);
		}
		return this.executeService;
	}

	private class SaveTask implements Runnable {

		private OrderFlow orderFlow = null;

		private String strCreateDate = null;

		public SaveTask(OrderFlow orderFlow, String strCreateDate) {
			this.orderFlow = orderFlow;
			this.strCreateDate = strCreateDate;
		}

		@Override
		public void run() {
			this.getOverdueExMODAO().updateSmtOverdueTime(this.getOrderFlow(), this.getStrCreateDate());
		}

		private OrderFlow getOrderFlow() {
			return this.orderFlow;
		}

		private String getStrCreateDate() {
			return this.strCreateDate;
		}

		private OverdueExMoDAO getOverdueExMODAO() {
			return SmtOptTimeAspect.this.getOverdueExMODAO();
		}
	}

	private class CreateOrderTask implements Runnable {

		private CwbOrderDTO smtOrder = null;

		public CreateOrderTask(CwbOrderDTO smtOrderList) {
			this.smtOrder = smtOrderList;
		}

		@Override
		public void run() {
			this.getOverdueExMODAO().insertSmtOrder(this.getSmtOrder());
		}

		public CwbOrderDTO getSmtOrder() {
			return this.smtOrder;
		}

		private OverdueExMoDAO getOverdueExMODAO() {
			return SmtOptTimeAspect.this.getOverdueExMODAO();
		}
	}

	private class UpdateDeliverStateTask implements Runnable {

		private String cwb = null;

		private BigDecimal receivedFee = null;

		private int deliverState = 0;

		public UpdateDeliverStateTask(String cwb, int deliverState, BigDecimal receivedFee) {
			this.cwb = cwb;
			this.receivedFee = receivedFee;
			this.deliverState = deliverState;
		}

		@Override
		public void run() {
			this.getOverdueExMODAO().updateDeliverState(this.getCwb(), this.getDeliverState(), this.getReceivedFee());
		}

		private String getCwb() {
			return this.cwb;
		}

		private BigDecimal getReceivedFee() {
			return this.receivedFee;
		}

		private int getDeliverState() {
			return this.deliverState;
		}

		private OverdueExMoDAO getOverdueExMODAO() {
			return SmtOptTimeAspect.this.getOverdueExMODAO();
		}
	}

	private class UpdatePrintTimeTask implements Runnable {

		public PrintcwbDetail printDetail = null;

		public UpdatePrintTimeTask(PrintcwbDetail printDetail) {
			this.printDetail = printDetail;
		}

		@Override
		public void run() {
			this.getOverdueExMODAO().updatePrintTime(this.getPrintDetail());
		}

		public PrintcwbDetail getPrintDetail() {
			return this.printDetail;
		}

		private OverdueExMoDAO getOverdueExMODAO() {
			return SmtOptTimeAspect.this.getOverdueExMODAO();
		}
	}

	private class UpdateDeliverInfoTask implements Runnable {

		private String cwb = null;

		private long deliverId = 0;

		private String dispatchTime = null;

		public UpdateDeliverInfoTask(String cwb, long deliverId, String dispatchTime) {
			this.cwb = cwb;
			this.deliverId = deliverId;
			this.dispatchTime = dispatchTime;
		}

		@Override
		public void run() {
			this.getOverdueExMODAO().updateDeliverInfo(this.getCwb(), this.getDeliverId(), this.getDispatchTime());
		}

		private String getCwb() {
			return this.cwb;
		}

		private long getDeliverId() {
			return this.deliverId;
		}

		private String getDispatchTime() {
			return this.dispatchTime;
		}

		private OverdueExMoDAO getOverdueExMODAO() {
			return SmtOptTimeAspect.this.getOverdueExMODAO();
		}
	}

	private class UpdateOutAreaTimeTask implements Runnable {

		private String[] cwbs = null;

		private String strTime = null;

		public UpdateOutAreaTimeTask(String[] cwbs, String strTime) {
			this.cwbs = cwbs;
			this.strTime = strTime;
		}

		@Override
		public void run() {
			this.getOverdueExMODAO().updateOutAreaTime(this.getCwbs(), this.getStrTime());
		}

		private String[] getCwbs() {
			return this.cwbs;
		}

		private String getStrTime() {
			return this.strTime;
		}

		private OverdueExMoDAO getOverdueExMODAO() {
			return SmtOptTimeAspect.this.getOverdueExMODAO();
		}
	}

	private class UpdateMEHTimeTask implements Runnable {

		private String cwb = null;

		private String strTime = null;

		public UpdateMEHTimeTask(String cwb, String strTime) {
			this.cwb = cwb;
			this.strTime = strTime;
		}

		@Override
		public void run() {
			this.getOverdueExMODAO().updateEMHTime(this.getCwb(), this.getStrTime());
		}

		private String getCwb() {
			return this.cwb;
		}

		private String getStrTime() {
			return this.strTime;
		}

		private OverdueExMoDAO getOverdueExMODAO() {
			return SmtOptTimeAspect.this.getOverdueExMODAO();
		}
	}

}
