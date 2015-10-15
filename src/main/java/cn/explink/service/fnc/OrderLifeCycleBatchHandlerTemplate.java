package cn.explink.service.fnc;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class OrderLifeCycleBatchHandlerTemplate<T> {

	
	private Logger logger = LoggerFactory
			.getLogger(OrderLifeCycleBatchHandlerTemplate.class);

	
	public void batchHandle(int batchSize,int reportdate){
		
		if (batchSize <= 0) {
			batchSize = 1000;
		}
		
		// 获取总的记录数
		long countOfRecord = countBatch();
		
		// 计算批次
		int batch = getBatch(countOfRecord, batchSize);

		logger.info(
				"[batchHandle][total record:{}, batch size : {}]", countOfRecord, batch);

		long st, et;
		List<T> batchList = null;
		long lastRowId = 0L;
		
		for (int i = 1; i <= batch; i++) {
			// 记录开始时间
			st = System.currentTimeMillis();
			
			batchList = getNextBatchList(i, batchSize,lastRowId);
			
			if(CollectionUtils.isNotEmpty(batchList)){
				
				doBatch(batchList,reportdate);
				
				//Retrieve the last opscwbid for next iterator
				lastRowId = getLastRowId(batchList);
				
			}

			et = (System.currentTimeMillis() - st);

			logger.info(
					"[batchHandle][batch {} procceed {} rows，spend time={} ms]",
					new Object[] { i, batchList.size(), et });
		}
		
		
		
	}
	
	
	private int getBatch(long countOfRecord, int pageSize) {
		// 计算批次
		int batch = 0;
		if ((countOfRecord % pageSize) == 0) {
			batch = (int) (countOfRecord / pageSize);
		} else {
			batch = (int) (countOfRecord / pageSize + 1);
		}
		return batch;

	}

	protected abstract void doBatch(List<T> batchList, int reportdate);
	
	protected abstract List<T> getNextBatchList(int currentBatch, int batchSzie, long lastRowId);
	
	protected abstract long getLastRowId(List<T> batchList);
	
	protected abstract long countBatch();
}
