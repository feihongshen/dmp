package cn.explink.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class ResultCollectorManager {
	private Map<String, ResultCollector> map = new HashMap<String, ResultCollector>();

	public ResultCollector createNewResultCollector() {
		ResultCollector resultCollector = new ResultCollector();
		resultCollector.setId(UUID.randomUUID().toString());
		map.put(resultCollector.getId(), resultCollector);
		return resultCollector;
	}

	public ResultCollector getResultCollector(String id) {
		return map.get(id);
	}

	public ResultCollector getResultCollectorByEmaildateid(long emaildateid) {
		return map.get(emaildateid);
	}
}
