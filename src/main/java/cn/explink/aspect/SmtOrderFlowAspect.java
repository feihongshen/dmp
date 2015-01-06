package cn.explink.aspect;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.explink.dao.OverdueExMoDAO;
import cn.explink.domain.orderflow.OrderFlow;

/**
 * 上门退订单创建订单流程切面.
 *
 * @author zhaoshb
 * @since DMP3.0
 */
@Aspect
@Component
public class SmtOrderFlowAspect {

	@Autowired
	private OverdueExMoDAO overdueExMODAO = null;

	private ExecutorService executorService = null;

	@After("execution(* cn.explink.dao.OrderFlowDAO.creOrderFlow(..))")
	public void beforeCreateOrderFlow(JoinPoint point) {
		Object[] args = point.getArgs();
		OrderFlow orderFlow = (OrderFlow) args[0];
		String strCreDate = (String) args[1];

		this.saveOverdueData(orderFlow, strCreDate);
	}

	@PostConstruct
	public void initExecutorService() {
		this.executorService = Executors.newFixedThreadPool(1);
	}

	private void saveOverdueData(OrderFlow orderFlow, String strCreateDate) {
		this.getExecutorService().submit(new SaveTask(orderFlow, strCreateDate));
	}

	private OverdueExMoDAO getOverdueExMODAO() {
		return this.overdueExMODAO;
	}

	private ExecutorService getExecutorService() {
		return this.executorService;
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
			return SmtOrderFlowAspect.this.getOverdueExMODAO();
		}
	}

}
