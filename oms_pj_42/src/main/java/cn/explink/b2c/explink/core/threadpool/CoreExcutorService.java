package cn.explink.b2c.explink.core.threadpool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.JacksonMapper;
import cn.explink.dao.CommonSendDataDAO;
import cn.explink.dao.GetDmpDAO;
import cn.explink.dao.WarehouseCommenDAO;
import cn.explink.domain.CommenSendData;
import cn.explink.domain.Common;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.JSONReslutUtil;

/**
 * 系统之间的对接（上游） 接收下游状态回传
 *  启用多线程模式执行 
 * @author Administrator
 *
 */

@Service
public class CoreExcutorService {
	private Logger logger = LoggerFactory.getLogger(CoreExcutorService.class);

	@Autowired
	WarehouseCommenDAO warehouseCommenDAO;
	@Autowired
	GetDmpDAO getDmpDAO;
	@Autowired
	CommonSendDataDAO commonSendDataDAO;


	/**
	 * 获取dmp承运商列表
	 * @return
	 */
	public List<Common> getCommonList() {
		return getDmpDAO.getAllCommons();
	}

	/**
	 * 上游- oms定时回传至dmp，反馈
	 */
	public void selectOMStemp_feedback() {

		List<Common> commonlist = getCommonList();

		if (commonlist == null || commonlist.size() == 0) {
			logger.warn("上游-未开启系统对接");
			return ;
		}
		for (Common com : commonlist) {
			if (com.getIsopenflag() == 0) {
				logger.info("上游对接未开启name={}", com.getCommonname());
				continue;
			}
			
			excuteRequestDmp(com);
			
		}
		

	}

	/**
	 * 执行每个具体的承运商业务
	 * @param com
	 */
	private void excuteRequestDmp(Common com) {

		try {
			
			int loop = 0;
			while (true) {
				loop++;
				if (loop > 100) {
					logger.warn("上游OMS临时表反馈DMP循环超过100次,怀疑程序未知bug,return");
					return ;
				}

				long loopcount = com.getLoopcount() == 0 ? 3 : com.getLoopcount(); //默认循环三次

				List<CommenSendData> datalist = commonSendDataDAO.getCommenCwbListBycommoncode(com.getCommonnumber(), com.getPageSize(), loopcount);
				if (datalist == null || datalist.size() == 0) {
					logger.info("上游-oms目前没有待发送dmp的订单,commonCode={},pagesize={}", com.getCommonnumber(), com.getPageSize());
					return ;
				}
				
				ExecutorService executor = Executors.newCachedThreadPool();
				int threadCounts=5;
				int totalSize=datalist.size();
				
				 int len=totalSize/threadCounts;//平均分割List   
		         if(len==0){   
		            threadCounts=totalSize;//采用一个线程处理List中的一个元素   
		            len=totalSize/(threadCounts==0?1:threadCounts);//重新平均分割List   
		         }   
		         CyclicBarrier  barrier=new CyclicBarrier(threadCounts+1);   
		         for(int i=0;i<threadCounts;i++){     //创建线程任务   
		        	 
		        	List<CommenSendData> sublist = null;
		        	 
		            if(i==threadCounts-1){ //最后一个线程承担剩下的所有元素的计算   
		            	sublist = datalist.subList(i*len,totalSize);
		            }else{   
		                sublist = datalist.subList(i*len, len*(i+1)>totalSize?totalSize:len*(i+1));
		            }   
		            executor.execute(new SubExcuteCoreTask(sublist,com,getDmpDAO,commonSendDataDAO));   
		         }
		         
		         try {   
		             barrier.await();//关键，使该线程在障栅处等待，直到所有的线程都到达障栅处   
		         } catch (Exception e) {   
		        	 logger.error(Thread.currentThread().getName(),e);
		         }  
		         executor.shutdown();   
				
				
			}
		} catch (Exception e) {
			logger.error("环形对接oms状态回写dmp异常",e);
		}

	}



}
