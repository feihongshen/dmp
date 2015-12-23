/**
 *
 */
package cn.explink.service.express;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.transaction.annotation.Transactional;

import cn.explink.domain.Bale;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.User;
import cn.explink.domain.VO.express.CombineQueryView;
import cn.explink.domain.VO.express.DeliverSummaryView;
import cn.explink.domain.VO.express.ExpressOpeAjaxResult;
import cn.explink.domain.express.CwbOrderForCombine;
import cn.explink.enumutil.express.ExpressCombineTypeEnum;

/**
 * @author songkaojun 2015年8月6日
 */
@Transactional
public interface StationOperationService {

	/**
	 *
	 * @param preOrderIdList
	 * @param delivermanId
	 * @param branchid
	 * @param distributeUser
	 * @return
	 */
	public boolean assignDeliver(List<Integer> preOrderIdList, int delivermanId, long branchid, User distributeUser);

	public boolean superzone(List<Integer> idList, String note, long branchid, User distributeUser);

	/**
	 *
	 * @return
	 */
	public String getPackageNo();

	/**
	 * 通过运单号查询快递单
	 *
	 * @param waybillNo
	 * @return
	 */
	public CwbOrder getExpressOrderByWaybillNo(String waybillNo, long currentBranchid, List<ExpressCombineTypeEnum> combineTypeList);

	/**
	 *
	 * @param waybillNoList
	 * @return
	 */
	public List<CwbOrderForCombine> getExpressOrderListByWaybillNoList(List<String> waybillNoList, long currentBranchid, List<ExpressCombineTypeEnum> combineTypeList);

	/**
	 *
	 * @param page
	 * @param provinceId
	 * @param excludeWaybillNoList
	 * @return
	 */
	public List<CwbOrderForCombine> getExpressOrderByPage(long page, Integer provinceId, List<Integer> cityIdList, List<String> excludeWaybillNoList, long currentBranchid,
			List<ExpressCombineTypeEnum> combineTypeList);

	/**
	 * 合包服务
	 *
	 * @param packageNo
	 * @param waybillNoList
	 * @param branchCode
	 * @param packageManName
	 * @return
	 */
	public boolean combinePackage(List<String> waybillNoList, String branchCode, Bale bale);

	/**
	 *
	 * 根据日期区间获取小件员汇总
	 *
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public DeliverSummaryView getDeliverSummary(String beginDate, String endDate, long branchid);

	/**
	 * 导出汇总单
	 *
	 * @param request
	 * @param response
	 * @param head
	 * @param list
	 * @param clz
	 * @param fileName
	 */
	public void exportSummaryXls(HttpServletRequest request, HttpServletResponse response, List<?> head, List<?> list, Class<?> clz, String fileName);

	/**
	 * 校验包号是否被使用
	 *
	 * @param packageNo
	 * @return
	 */
	public ExpressOpeAjaxResult checkPackageNoIsUsed(String packageNo);

	/**
	 * 校验包号是否唯一
	 *
	 * @param packageNo
	 * @return false:该包号已经存在，不能使用 true:包号不存在
	 */
	public boolean checkPackageNoUnique(String packageNo);

	/**
	 * 根据包号查询包里面的运单信息
	 *
	 * @param packageNo
	 * @return
	 */
	public CombineQueryView queryCombineInfo(String packageNo);

}
