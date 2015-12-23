package cn.explink.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import cn.explink.domain.VO.express.EmbracedImportResult;

@Service
public class ResultCollectorManager {
	private Map<String, ResultCollector> map = new HashMap<String, ResultCollector>();
	private Map<String, EmbracedImportResult> EmbracedImportmap = new HashMap<String, EmbracedImportResult>();

	public ResultCollector createNewResultCollector() {
		ResultCollector resultCollector = new ResultCollector();
		resultCollector.setId(UUID.randomUUID().toString());
		this.map.put(resultCollector.getId(), resultCollector);
		return resultCollector;
	}

	/**
	 *
	 * @Title: createNewEmbracedImportResultCollector
	 * @description 创建补录导入的结果类
	 * @author 刘武强
	 * @date  2015年10月13日下午3:32:19
	 * @param  @return
	 * @return  EmbracedImportResult
	 * @throws
	 */
	public EmbracedImportResult createNewEmbracedImportResultCollector() {
		EmbracedImportResult resultCollector = new EmbracedImportResult();
		resultCollector.setId(UUID.randomUUID().toString());
		this.EmbracedImportmap.put(resultCollector.getId(), resultCollector);
		return resultCollector;
	}

	/**
	 *
	 * @Title: getEmbracedResultCollector
	 * @description 通过id获取补录导入时产生的结果类
	 * @author 刘武强
	 * @date  2015年10月13日下午3:32:50
	 * @param  @param id
	 * @param  @return
	 * @return  EmbracedImportResult
	 * @throws
	 */
	public EmbracedImportResult getEmbracedResultCollector(String id) {
		return this.EmbracedImportmap.get(id);
	}

	public ResultCollector getResultCollector(String id) {
		return this.map.get(id);
	}

	public ResultCollector getResultCollectorByEmaildateid(long emaildateid) {
		return this.map.get(emaildateid);
	}
}
