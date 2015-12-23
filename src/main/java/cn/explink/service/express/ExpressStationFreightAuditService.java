package cn.explink.service.express;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.CwbDAO;
import cn.explink.dao.express.ExpressFreightBillSelectDAO;
import cn.explink.dao.express.ExpressStationFreightAuditDao;
import cn.explink.domain.VO.express.ExpressCwbOrderForTakeGoodsQueryVO;
import cn.explink.domain.VO.express.ExpressFreightAuditBillVO;
import cn.explink.domain.VO.express.SelectReturnVO;
import cn.explink.domain.express.ExpressFreightBill;
import cn.explink.enumutil.express.ExpressBillStateEnum;

/**
 * 站点运费审核service
 * @author wangzy 2015年8月11日
 *
 */
@Service
public class ExpressStationFreightAuditService {
	@Autowired
	ExpressStationFreightAuditDao expressStationFreightAudit;
	@Autowired
	CwbDAO cwbDao;
	@Autowired
	ExpressFreightBillSelectDAO expressFreightBillSelectDAO;


	/**
	 * 查询账单
	 * @param page
	 * @param expressFreightAuditBillVO
	 * @return
	 */
	public List<ExpressFreightAuditBillVO>  queryBillForFreight(Long page,ExpressFreightAuditBillVO expressFreightAuditBillVO){
		return expressStationFreightAudit.queryBillForFreight(page,expressFreightAuditBillVO);
	}
	/**
	 * 查询账单条数
	 * @author 王志宇
	 */
	public int getBillCount(ExpressFreightAuditBillVO expressFreightAuditBillVO){
		return expressStationFreightAudit.getBillCount(expressFreightAuditBillVO);
	}

	
	
	
	/**
	 * 审核页面数据回写
	 * @author 王志宇
	 */
	public ExpressFreightAuditBillVO edit(Long id){
		return expressStationFreightAudit.getBillById(id);
	}

	
	
	
	/**
	 * 查询编辑页面的账单表条目的详细订单
	 * @author 王志宇
	 */
	public List<ExpressCwbOrderForTakeGoodsQueryVO>  orderDetailsForBill(Long id){
		return cwbDao.queryCwbExpressCustomerFreight(id,2);
	}
	/**
	 * 查询编辑页面的账单表条目的详细订单----分页
	 * @author 王志宇
	 */
	public List<ExpressCwbOrderForTakeGoodsQueryVO>  orderDetailsForBillByPage(Long id,Integer pageNo){
		return cwbDao.queryCwbExpressCustomerFreightByPage(id,pageNo,2);
		
	}

	
	/**
	 * 获得站点（省下所有站点）
	 * @author 王志宇
	 */
	public List<SelectReturnVO> getStations(){
		return expressFreightBillSelectDAO.getBranchData();
	}
	
	
	/**
	 * 审核账单
	 * @author 王志宇
	 */
	public int audit(Long id,String state){
		//TODO：只有未审核的账单才能够被审核
		if(state.equals(ExpressBillStateEnum.UnAudit.getText())){
			return expressStationFreightAudit.updateState(id, ExpressBillStateEnum.Audited.getValue());
		}else{
			return 0;
		}
	}
	/**
	 * 取消审核账单
	 * @author 王志宇
	 */
	public int cancelAudit(Long id,String state){
		//TODO：只有已审核的账单才能够被取消审核
		if(state.equals(ExpressBillStateEnum.Audited.getText())){
			return expressStationFreightAudit.updateState(id, ExpressBillStateEnum.UnAudit.getValue());
		}else{
			return 0;
		}
	}
	/**
	 * 核销账单
	 * @author 王志宇
	 */
	public int checkAudit(Long id,String state){
		//TODO: 只有已审核的账单才能被核销
		if(state.equals(ExpressBillStateEnum.Audited.getText())){
			return expressStationFreightAudit.updateState(id, ExpressBillStateEnum.Verificated.getValue());
		}else{
			return 0;
		}
	}
}
