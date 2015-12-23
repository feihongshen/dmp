package cn.explink.service.express;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.CwbDAO;
import cn.explink.dao.express.ExpressCustomerFreightAuditDao;
import cn.explink.dao.express.ExpressFreightBillSelectDAO;
import cn.explink.domain.VO.express.ExpressCwbOrderForTakeGoodsQueryVO;
import cn.explink.domain.VO.express.ExpressFreightAuditBillVO;
import cn.explink.domain.VO.express.SelectReturnVO;
import cn.explink.enumutil.express.ExpressBillStateEnum;

/**
 * 客户运费审核service
 * @author wangzy 2015年8月11日
 *
 */
@Service
public class ExpressCustomerFreightAuditService {
	@Autowired
	ExpressCustomerFreightAuditDao expressCustomerFreight;
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
		return expressCustomerFreight.queryBillForFreight(page,expressFreightAuditBillVO);
	}
	/**
	 * 查询账单条数
	 * @author 王志宇
	 */
	public int getBillCount(ExpressFreightAuditBillVO expressFreightAuditBillVO){
		return expressCustomerFreight.getBillCount(expressFreightAuditBillVO);
	}
	/**
	 * 查询客户
	 * @author 王志宇
	 */
	public List<SelectReturnVO>  getCustomers(){
		return expressFreightBillSelectDAO.getCustomerSelectInfo();
	}
	
	
	/**
	 * 编辑操作的数据回写
	 * @author 王志宇
	 */
	public ExpressFreightAuditBillVO edit(Long id){
		return expressCustomerFreight.getBillById(id);
	}
	
	
	
	
	/**
	 * 查询编辑页面的账单表条目的详细订单
	 */
	public List<ExpressCwbOrderForTakeGoodsQueryVO>  orderDetailsForBill(Long id){
		return cwbDao.queryCwbExpressCustomerFreight(id,1);
		
	}
	/**
	 * 查询编辑页面的账单表条目的详细订单----分页
	 */
	public List<ExpressCwbOrderForTakeGoodsQueryVO>  orderDetailsForBillByPage(Long id,Integer pageNo){
		return cwbDao.queryCwbExpressCustomerFreightByPage(id,pageNo,1);
		
	}

	
	
	
	
	/**
	 * 审核账单
	 * @author 王志宇
	 */
	public int audit(Long id,String state){
		//TODO：只有未审核的账单才能够被审核
		if(state.equals(ExpressBillStateEnum.UnAudit.getText())){
			return expressCustomerFreight.updateState(id, ExpressBillStateEnum.Audited.getValue());
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
			return expressCustomerFreight.updateState(id, ExpressBillStateEnum.UnAudit.getValue());
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
			return expressCustomerFreight.updateState(id, ExpressBillStateEnum.Verificated.getValue());
		}else{
			return 0;
		}
	}
}
