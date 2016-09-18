package cn.explink.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.SmtCwbDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.EmailDate;
import cn.explink.domain.ImportValidationManager;
import cn.explink.domain.SmtCwb;
import cn.explink.domain.User;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;

@Service
public class DataImportSingleService {
	
	@Autowired
	private SmtCwbDAO smtCwbDAO;
	
	@Autowired
	private ImportValidationManager importValidationManager;
	
	@Autowired
	private CwbDAO cwbDAO;
	
	@Autowired
	private CwbOrderService cwbOrderService;
	
	/**
	 * 单独导入数据，抽象出来，因为原来方法无法支持事务
	 * @author chunlei05.li
	 * @date 2016年8月23日 下午2:31:55
	 * @param user
	 * @param ed
	 * @param isReImport
	 * @param cwbOrderDTO
	 */
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public void importSingleData(User user, EmailDate ed, boolean isReImport, CwbOrderDTO cwbOrderDTO) {
		for (CwbOrderValidator cwbOrderValidator : this.importValidationManager.getCommonValidators()) {
			cwbOrderValidator.validate(cwbOrderDTO);
		}
		this.insertCwb(cwbOrderDTO, ed.getCustomerid(), ed.getBranchid(), user, ed, isReImport);
		/**订单导入模板新增退货地址，及商家退货号 add by chunlei05.li 2016/8/22*/
		if (cwbOrderDTO.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue()) {
			if (StringUtils.isNotBlank(cwbOrderDTO.getReturnno()) || StringUtils.isNotBlank(cwbOrderDTO.getReturnaddress())) {
				SmtCwb smtCwb = new SmtCwb();
				smtCwb.setCwb(cwbOrderDTO.getCwb());
				smtCwb.setReturnNo(cwbOrderDTO.getReturnno());
				smtCwb.setReturnAddress(cwbOrderDTO.getReturnaddress());
				this.smtCwbDAO.saveSmtCwb(smtCwb);
			}
		}
		/***************** end **************/
	}
	
	/**
	 * 正常导入
	 *
	 * @param cwbOrderDTO
	 * @param customerid
	 *            供货商
	 * @param branchid
	 * @param operatoruserid操作员
	 */
	private void insertCwb(final CwbOrderDTO cwbOrderDTO, final long customerid, long warhouseid, User user, EmailDate ed, boolean isReImport) {
		CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwbOrderDTO.getCwb());
		if (cwbOrder == null) {
			this.cwbOrderService.insertCwbOrder(cwbOrderDTO, customerid, warhouseid, user, ed);
			return;
		}
		
		//只允许状态为1的订单导入系统-----刘武强20160914
		if(cwbOrder.getFlowordertype() != FlowOrderTypeEnum.DaoRuShuJu.getValue()){
			throw new RuntimeException("订单状态不是数据导入，不允许再次导入");
		}
		if (cwbOrder.getEmaildateid() > 0) {
			if (!isReImport) {
				throw new RuntimeException("重复单号");
			} else if (ed.getEmaildateid() != cwbOrder.getEmaildateid()) {
				throw new RuntimeException("重复单号");
			}
		}
		this.cwbOrderService.updateExcelCwb(cwbOrderDTO, customerid, warhouseid, user, ed, isReImport);
	}
}
