/**
 *
 */
package cn.explink.service.express;

import org.springframework.transaction.annotation.Transactional;

import cn.explink.domain.express.ExpressWeigh;

/**
 * @author songkaojun 2015年10月23日
 */

@Transactional
public interface ExpressWeighService {

	public boolean isWeightExist(String cwb);

	/**
	 * 保存重量
	 *
	 * @param expressWeigh
	 * @return
	 */
	public long saveWeight(ExpressWeigh expressWeigh);

	/**
	 *
	 * @param expressWeigh
	 */
	public void updateWeight(ExpressWeigh expressWeigh);

}
