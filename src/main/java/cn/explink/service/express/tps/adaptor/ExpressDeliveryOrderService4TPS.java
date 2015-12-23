/**
 *
 */
package cn.explink.service.express.tps.adaptor;

import java.util.List;

/**
 *
 * 回传TPS服务接口
 *
 * @author songkaojun 2015年8月28日
 */
public interface ExpressDeliveryOrderService4TPS {
	/**
	 *
	 * @param preOrderNoList
	 * @return
	 */
	public void assignDeliver(List<String> preOrderNoList, String branchCode, String operator);

	/**
	 *
	 * @param preOrderNoList
	 * @param branchCode
	 * @param operator
	 */
	public void superzone(List<String> preOrderNoList, String branchCode, String operator);

	/**
	 * 获取包号
	 *
	 * @return
	 */
	public String getPackageNo();

	/**
	 * 批量获取包号
	 *add by 王志宇
	 * @return
	 */
	public List<String> getBatchPackageNo(int count);

	/**
	 * 上传打包信息
	 *
	 * @param packageNo
	 * @param waybillNoList
	 * @param branchCode
	 * @param packageMan
	 * @return
	 */
	public void upLoadPackageInfo(String packageNo, List<String> waybillNoList, String branchCode, String packageMan);

}
