/**
 *
 */
package cn.explink.service.express.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.express.ExpressWeighDAO;
import cn.explink.domain.express.ExpressWeigh;
import cn.explink.service.express.ExpressWeighService;

/**
 * @author songkaojun 2015年10月23日
 */
@Service
public class ExpressWeighServiceImpl implements ExpressWeighService {

	@Autowired
	private ExpressWeighDAO expressWeighDAO;

	@Override
	public long saveWeight(ExpressWeigh expressWeigh) {
		return this.expressWeighDAO.insert(expressWeigh);
	}

	@Override
	public boolean isWeightExist(String cwb) {
		return this.expressWeighDAO.isWeightExist(cwb);
	}

	@Override
	public void updateWeight(ExpressWeigh expressWeigh) {
		this.expressWeighDAO.updateWeight(expressWeigh);
	}

}
