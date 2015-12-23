package cn.explink.service.express;

import java.util.Map;

import cn.explink.domain.VO.express.ExpressBillParams4Create;
import cn.explink.domain.VO.express.ExpressBillParamsVO4Query;
import cn.explink.domain.VO.express.ExpressOpeAjaxResult;
import cn.explink.domain.express.ExpressFreightBill;

/**
 * 账单的公共接口
 * @author jiangyu 2015年8月11日
 *
 */
public interface ExpressBillCommonService {
	/**
	 * 查询列表
	 * @param params
	 * @return
	 */
	Map<String, Object> getFreightBillList(ExpressBillParamsVO4Query params);
	/**
	 * 删除账单
	 * @return
	 */
	ExpressOpeAjaxResult deleteBillByBillId(Long billId,Integer billType);
	/**
	 * 创建账单
	 * @param params
	 * @return
	 */
	ExpressOpeAjaxResult createBill(ExpressBillParams4Create params);
	/**
	 * 查询记录通过id
	 * @param billId
	 * @return
	 */
	ExpressFreightBill getRecordById(Long billId);
	/**
	 * 通过账单号查询账单
	 * @param billNo
	 * @return
	 */
	ExpressFreightBill getRecoredByBillNo(String billNo);
	/**
	 * 更新信息
	 * @param billId
	 */
	void updateRecordRemarkById(Long billId,String remark);
	
	/**
	 * 预览
	 * @param params
	 * @return
	 */
	Map<String, Object> getCwbInfoList(ExpressBillParams4Create params);
	/**
	 * 编辑
	 * @param billId
	 * @return
	 */
	Map<String, Object> getEditViewBillInfo(Long billId,Long page,Integer opeFlag);
	/**
	 * 更新账单信息
	 * @param billId
	 * @param remark
	 * @return
	 */
	ExpressOpeAjaxResult updateBillByBillId(Long billId, String remark);
	
}
