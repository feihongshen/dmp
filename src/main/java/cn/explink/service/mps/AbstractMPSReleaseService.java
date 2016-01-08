/**
 *
 */
package cn.explink.service.mps;

/**
 * @author songkaojun 2016年1月8日
 */
public abstract class AbstractMPSReleaseService extends AbstractMPSService {

	protected static final String VALIDATE_RELEASE_CONDITION = "[判断一票多件是否放行]";

	/**
	 * 校验放行条件
	 *
	 * @param transCwb
	 */
	public abstract void validateReleaseCondition(String transCwb);

}
