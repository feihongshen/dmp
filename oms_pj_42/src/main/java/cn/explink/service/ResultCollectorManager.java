package cn.explink.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class ResultCollectorManager {
	private Map<String, ResultCollector> map = new HashMap<String, ResultCollector>();
	private Map<String, Long> ImportCountMap = new HashMap<String, Long>();

	public ResultCollector createNewResultCollector() {
		ResultCollector resultCollector = new ResultCollector();
		resultCollector.setId(UUID.randomUUID().toString());
		map.put(resultCollector.getId(), resultCollector);
		return resultCollector;
	}

	public ResultCollector getResultCollector(String id) {
		return map.get(id);
	}

	public void setSuccessImportCount(String key, Long count) {
		ImportCountMap.put(key + "Success", count);
	}

	public void setFailureImportCount(String key, Long count) {
		ImportCountMap.put(key + "Failure", count);
	}

	public Long getSuccessImportCount(String key) {
		return ImportCountMap.get(key + "Success");
	}

	public Long getFailureImportCount(String key) {
		return ImportCountMap.get(key + "Failure");
	}
}
