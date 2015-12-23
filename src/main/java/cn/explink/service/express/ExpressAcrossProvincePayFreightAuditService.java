package cn.explink.service.express;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.CwbDAO;
import cn.explink.dao.express.ExpressAcrossProvincePayFreightAuditDao;
import cn.explink.dao.express.ExpressFreightBillSelectDAO;
import cn.explink.domain.VO.express.ExportBillCwb;
import cn.explink.domain.VO.express.ExpressCwbOrderForTakeGoodsQueryVO;
import cn.explink.domain.VO.express.ExpressFreightAuditBillVO;
import cn.explink.domain.VO.express.SelectReturnVO;
import cn.explink.enumutil.express.ExpressBillStateEnum;

/**
 * 跨省到付运费审核（应付）
 * @author wangzy 2015年8月11日
 *
 */
@Service
public class ExpressAcrossProvincePayFreightAuditService {
	@Autowired
	ExpressAcrossProvincePayFreightAuditDao expressAcrossProvinceFreightAudit;
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
		return expressAcrossProvinceFreightAudit.queryBillForFreight(page,expressFreightAuditBillVO);
	}
	
	/**
	 * 查询账单条数
	 * 
	 */
	public int getBillCount(ExpressFreightAuditBillVO expressFreightAuditBillVO){
		return expressAcrossProvinceFreightAudit.getBillCount(expressFreightAuditBillVO);
	}

	/**
	 * 编辑操作的数据回写
	 * @author 王志宇
	 */
	public ExpressFreightAuditBillVO edit(Long id){
		return expressAcrossProvinceFreightAudit.getBillById(id);
	}

	/**
	 * 查询编辑页面的账单表条目的详细订单
	 */
	public List<ExpressCwbOrderForTakeGoodsQueryVO>  orderDetailsForBill(Long id){
		return cwbDao.queryCwbExpressCustomerFreight(id,3);
	}

	/**
	 * 查询编辑页面的账单表条目的详细订单----分页
	 */
	public List<ExpressCwbOrderForTakeGoodsQueryVO>  orderDetailsForBillByPage(Long id,Integer pageNo){
		return cwbDao.queryCwbExpressCustomerFreightByPage(id,pageNo,3);
		
	}

	/**
	 * 查询编辑页面的账单表条目的详细订单----分页
	 */
	public List<ExportBillCwb>  orderDetailsForBillByPageForExport(Long id,Integer pageNo){
		return cwbDao.queryCwbExpressCustomerFreightByPageForExport(id,pageNo,3);
		
	}
	
	/**
	 * 获得所有省份（不包含自己）
	 * @author 王志宇
	 */
	public List<SelectReturnVO> getProvinces(){
		return expressFreightBillSelectDAO.getProvinceData(1l);
	}
	
	/**
	 * 审核账单
	 * @author 王志宇
	 */
	public int audit(Long id,String state){
		//只有未审核的账单才能够被审核
		if(state.equals(ExpressBillStateEnum.UnAudit.getText())){
			return expressAcrossProvinceFreightAudit.updateState(id, ExpressBillStateEnum.Audited.getValue());
		}else{
			return 0;
		}
	}
	/**
	 * 取消审核账单
	 * @author 王志宇
	 */
	public int cancelAudit(Long id,String state){
		//只有已审核的账单才能够被取消审核
		if(state.equals(ExpressBillStateEnum.Audited.getText())){
			return expressAcrossProvinceFreightAudit.updateState(id, ExpressBillStateEnum.UnAudit.getValue());
		}else{
			return 0;
		}

	}
	/**
	 * 核销账单
	 * @author 王志宇
	 */
	public int checkAudit(Long id,String state){
		// 只有已审核的账单才能被核销
		if(state.equals(ExpressBillStateEnum.Audited.getText())){
			return expressAcrossProvinceFreightAudit.updateState(id, ExpressBillStateEnum.Verificated.getValue());
		}else{
			return 0;
		}

	}
}
