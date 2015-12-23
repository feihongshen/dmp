package cn.explink.service.express;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.dao.UserDAO;
import cn.explink.dao.express.ExpressCodBillDAO;
import cn.explink.dao.express.ExpressCodBillDetailDAO;
import cn.explink.dao.express.ExpressOrderDao;
import cn.explink.dao.express.GeneralDAO;
import cn.explink.dao.express.ProvinceDAO;
import cn.explink.domain.User;
import cn.explink.domain.VO.TransProvincialAuditReconciliationExportVO;
import cn.explink.domain.VO.express.AdressVO;
import cn.explink.domain.VO.express.EmbracedOrderVO;
import cn.explink.domain.express.ExpressCodBill;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.express.ExpressBillStateEnum;
import cn.explink.enumutil.express.ExpressBillTypeEnum;
import cn.explink.util.ExportUtil4Order;
import cn.explink.util.Page;

@Transactional
@Service
public class TransProvincialReceivedReconciliationService extends ExpressCommonService {
	@Autowired
	private ExpressCodBillDAO expressCodBillDAO;
	@Autowired
	private ProvinceDAO provinceDAO;
	@Autowired
	private GeneralDAO generalDAO;
	@Autowired
	private ExpressOrderDao expressOrderDao;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private ExpressCodBillDetailDAO expressCodBillDetailDAO;

	/**
	 *
	 * @Title: getCodeBillInfo
	 * @description 跨省代收货款（应收/应付）的查询方法
	 * @author 刘武强
	 * @date 2015年8月14日上午9:29:06
	 * @param @param billNo 查询条件
	 * @param @param billState 查询条件
	 * @param @param creatStart 查询条件
	 * @param @param creatEnd 查询条件
	 * @param @param cavStart 查询条件
	 * @param @param cavEnd 查询条件
	 * @param @param provinceId 查询条件
	 * @param @param sequenceField 查询条件
	 * @param @param ascOrDesc 查询条件
	 * @param @param billType 账单类型（应付/应收）
	 * @param @param page 当前页数
	 * @param @param pageNumber 每页的记录数
	 * @param @return
	 * @return Map<String,Object>
	 * @throws
	 */
	public Map<String, Object> getCodeBillInfo(String billNo, int billState, Date creatStart, Date creatEnd, Date cavStart, Date cavEnd, int provinceId, String sequenceField, String ascOrDesc, int billType, long page, int pageNumber) {
		return this.expressCodBillDAO.getCodeBillInfo(billNo, billState, creatStart, creatEnd, cavStart, cavEnd, provinceId, sequenceField, ascOrDesc, billType, page, pageNumber);
	}

	/**
	 *
	 * @Title: delete
	 * @description 删除指定id的数据后（删除账单表里的数据，同时更新与该账单相关的订单数据），重新查询出数据在页面显示（放在一个事务里，防止脏读）
	 * @author 刘武强
	 * @date 2015年8月14日上午9:49:31
	 * @param @param id 待删除的账单id
	 * @param @param billType 账单类型（应付/应收）
	 * @param @param page 当前页数
	 * @param @param pageNumber 每页的记录数
	 * @param @return
	 * @return Map<String,Object>
	 * @throws
	 */
	public Map<String, Object> delete(Long id, int billType, long page, int pageNumber) {
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("id", id);
		paramsMap.put("bill_state", ExpressBillStateEnum.UnAudit.getValue());
		String flag = "true";
		//删除账单表里的数据
		this.generalDAO.deleteByKey("express_ops_cod_bill", paramsMap);
		ExpressCodBill expressCodBill = new ExpressCodBill();
		expressCodBill.setId(id);
		if (billType == ExpressBillTypeEnum.AcrossProvinceCodReceivableBill.getValue()) {
			//更新与账单相关的订单信息
			this.updateExpressOrder(expressCodBill, "delete");
		} else {
			Map<String, Object> whereMap = new HashMap<String, Object>();
			whereMap.put("bill_id", id);
			this.generalDAO.deleteByKey("express_ops_cod_bill_detail_import", whereMap);
		}
		String billNo = null;
		int billState = -1;
		Date creatStart = null;
		Date creatEnd = null;
		Date cavStart = null;
		Date cavEnd = null;
		int provinceId = -1;
		String sequenceField = null;
		String ascOrDesc = null;
		Map<String, Object> map = this.expressCodBillDAO.getCodeBillInfo(billNo, billState, creatStart, creatEnd, cavStart, cavEnd, provinceId, sequenceField, ascOrDesc, billType, page, pageNumber);
		map.put("flag", flag);
		return map;
	}

	/**
	 *
	 * @Title: audit
	 * @description 跨省代收货款审核（应收）里面，往账单表中更新审核信息，并查询出最新的显示数据
	 * @author 刘武强
	 * @date  2015年8月19日上午11:42:09
	 * @param  @param id 待审核的账单id
	 * @param @param billType 账单类型（应付/应收）
	 * @param  @param page 当前页数
	 * @param  @param pageNumber 每页的记录数
	 * @param  @param auditOrCancalAuditOrVerificated  "audit":审核,"cancalAudit":取消审核,"verificated":核销
	 * @param  @return
	 * @return  Map<String,Object>
	 * @throws
	 */
	public Map<String, Object> auditOrCancalAuditOrVerificated(Long id, int billType, long page, int pageNumber, String auditOrCancalAuditOrVerificated) {
		String flag = "true";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		Map<String, Object> whereMap = new HashMap<String, Object>();
		if ("audit".equals(auditOrCancalAuditOrVerificated)) {
			paramMap.put("auditor_id", this.getSessionUser().getUserid());
			paramMap.put("auditor_name", this.getSessionUser().getUsername());
			paramMap.put("audit_time", new Date());
			paramMap.put("bill_state", ExpressBillStateEnum.Audited.getValue());
			whereMap.put("bill_state", ExpressBillStateEnum.UnAudit.getValue());
		} else if ("cancalAudit".equals(auditOrCancalAuditOrVerificated)) {
			paramMap.put("auditor_id", 0);
			paramMap.put("auditor_name", null);
			paramMap.put("audit_time", null);
			paramMap.put("bill_state", ExpressBillStateEnum.UnAudit.getValue());
			whereMap.put("bill_state", ExpressBillStateEnum.Audited.getValue());
		} else if ("verificated".equals(auditOrCancalAuditOrVerificated)) {
			paramMap.put("cav_id", this.getSessionUser().getUserid());
			paramMap.put("cav_name", this.getSessionUser().getUsername());
			paramMap.put("cav_time", new Date());
			paramMap.put("bill_state", ExpressBillStateEnum.Verificated.getValue());
			whereMap.put("bill_state", ExpressBillStateEnum.Audited.getValue());
		} else {
			flag = "false";
		}
		whereMap.put("id", id);

		this.generalDAO.update(paramMap, "express_ops_cod_bill", whereMap);

		String billNo = null;
		int billState = -1;
		Date creatStart = null;
		Date creatEnd = null;
		Date cavStart = null;
		Date cavEnd = null;
		int provinceId = -1;
		String sequenceField = null;
		String ascOrDesc = null;
		Map<String, Object> map = this.expressCodBillDAO.getCodeBillInfo(billNo, billState, creatStart, creatEnd, cavStart, cavEnd, provinceId, sequenceField, ascOrDesc, billType, page, pageNumber);
		map.put("flag", flag);
		return map;
	}

	/**
	 *
	 * @Title: update
	 * @description 跨省代收货款对账（应收）更新方法（更新数据到数据库并重新查询线显示数据）
	 * @author 刘武强
	 * @date  2015年8月15日下午6:01:22
	 * @param  @param expressCodBill 待更新的数据
	 * @param @param billType 账单类型（应付/应收）
	 * @param  @param page 当前页数
	 * @param  @param pageNumber 每页的记录数
	 * @param  @return
	 * @return  Map<String,Object>
	 * @throws
	 */
	public Map<String, Object> update(ExpressCodBill expressCodBill, int billType, long page, int pageNumber) {
		boolean flag = this.insertOrUpdate(expressCodBill, "update", billType);
		String billNo = null;
		int billState = -1;
		Date creatStart = null;
		Date creatEnd = null;
		Date cavStart = null;
		Date cavEnd = null;
		int provinceId = -1;
		String sequenceField = null;
		String ascOrDesc = null;
		Map<String, Object> map = this.expressCodBillDAO.getCodeBillInfo(billNo, billState, creatStart, creatEnd, cavStart, cavEnd, provinceId, sequenceField, ascOrDesc, billType, page, pageNumber);
		map.put("flag", flag);
		return map;
	}

	/**
	 *
	 * @Title: insert
	 * @description 跨省代收货款对账新增方法（更新数据到数据库并重新查询显示数据）
	 * @author 刘武强
	 * @date  2015年8月17日上午9:39:52
	 * @param  @param expressCodBill 代插入的数据
	 * @param @param billType 账单类型（应付/应收）
	 * @param  @param page 当前页数
	 * @param  @param pageNumber 每页的记录数
	 * @param  @return
	 * @return  Map<String,Object>
	 * @throws
	 */
	public Map<String, Object> insert(ExpressCodBill expressCodBill, int billType, long page, int pageNumber) {
		boolean flag = true;
		//TODO 获取账单编号，暂时用输入代替

		long key = this.insert(expressCodBill, billType);
		expressCodBill.setId(key);
		if (billType == ExpressBillTypeEnum.AcrossProvinceCodReceivableBill.getValue()) {
			this.updateExpressOrder(expressCodBill, "insert");
		} else {
			Map<String, Object> whereMap = new HashMap<String, Object>();
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			whereMap.put("bill_id", expressCodBill.getId());
			paramsMap.put("effect_flag", 1);
			this.generalDAO.update(paramsMap, "express_ops_cod_bill_detail_import", whereMap);
		}
		String billNo = null;
		int billState = -1;
		Date creatStart = null;
		Date creatEnd = null;
		Date cavStart = null;
		Date cavEnd = null;
		int provinceId = -1;
		String sequenceField = null;
		String ascOrDesc = null;
		Map<String, Object> map = this.expressCodBillDAO.getCodeBillInfo(billNo, billState, creatStart, creatEnd, cavStart, cavEnd, provinceId, sequenceField, ascOrDesc, billType, page, pageNumber);
		map.put("flag", flag);
		return map;
	}

	/**
	 *
	 * @Title: getOrderByProvincereceivablecodbillid
	 * @description 根据跨省货款账单id，获取该账单下的所有订单/应付订单信息
	 * @author 刘武强
	 * @date 2015年8月14日下午6:58:32
	 * @param @param provincereceivablecodbillid 应收账单id
	 * @param @param billType 账单类型（应付/应收）
	 * @param @param page 当前页数
	 * @param @param pageNumber 每页的记录数
	 * @param @return
	 * @return Map<String,Object>
	 * @throws
	 */
	public Map<String, Object> getOrderByProvincereceivablecodbillid(Long provincereceivablecodbillid, int billType, long page, int pageNumber) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (billType == ExpressBillTypeEnum.AcrossProvinceCodReceivableBill.getValue()) {
			map = this.expressOrderDao.getOrderByProvincereceivablecodbillid(provincereceivablecodbillid, page, pageNumber);
		} else if (billType == ExpressBillTypeEnum.AcrossProvinceCodPayableBill.getValue()) {
			// 从应付订单表找到数据
			map = this.expressCodBillDetailDAO.getOrderByBillId(provincereceivablecodbillid, page, pageNumber);
		}
		List<EmbracedOrderVO> list = (List<EmbracedOrderVO>) map.get("list");
		List<User> users = this.userDAO.getAllUser();
		for (EmbracedOrderVO embracedOrderVO : list) {
			for (User user : users) {
				if (embracedOrderVO.getDeliverid().equals(user.getUserid())) {
					embracedOrderVO.setDelivername(user.getUsername());
				}
			}
		}
		return map;
	}

	/**
	 *
	 * @Title: getOrderByConditions
	 * @description 通过前台传入的条件，查询满足条件的订单
	 * @author 刘武强
	 * @date  2015年8月17日下午7:46:31
	 * @param  @param expressCodBill 查询条件
	 * @param  @param page 当前页数
	 * @param  @param pageNumber 每页的数据量
	 * @param  @param billType 表示账单类型的标志（应付/应收）
	 * @param  @return
	 * @return  Map<String,Object>
	 * @throws
	 */
	public Map<String, Object> getOrderByConditions(ExpressCodBill expressCodBill, int billType, long page, int pageNumber) {
		Map<String, Object> map = this.expressOrderDao
				.getOrderByConditions(expressCodBill.getReceivableProvinceId(), expressCodBill.getPayableProvinceId(), expressCodBill.getClosingDate(), DeliveryStateEnum.PeiSongChengGong.getValue(), FlowOrderTypeEnum.YiShenHe
						.getValue(), CwbOrderTypeIdEnum.Express.getValue(), page, pageNumber);
		List<EmbracedOrderVO> list = (List<EmbracedOrderVO>) map.get("list");
		List<User> users = this.userDAO.getAllUser();
		for (EmbracedOrderVO embracedOrderVO : list) {
			for (User user : users) {
				if (embracedOrderVO.getDeliverid().equals(user.getUserid())) {
					embracedOrderVO.setDelivername(user.getUsername());
				}
			}
		}
		return map;
	}

	/**
	 *
	 * @Title: getExpressBillState
	 * @description  获取账单的状态枚举
	 * @author 刘武强
	 * @date 2015年8月13日下午2:39:43
	 * @param @return
	 * @return List<Map<String,Object>>
	 * @throws
	 */
	public List<Map<String, Object>> getExpressBillState() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<ExpressBillStateEnum> infolist = ExpressBillStateEnum.getAllStatus();
		Map<String, Object> mapall = new HashMap<String, Object>();
		mapall.put("key", "-1");
		mapall.put("value", "全部");
		list.add(mapall);
		for (int i = 0; i < infolist.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			ExpressBillStateEnum enmu = infolist.get(i);
			map.put("key", enmu.getValue());
			map.put("value", enmu.getText());
			list.add(map);
		}
		return list;
	}

	/**
	 *
	 * @Title: getAllProvince
	 * @description 获取所有省份
	 * @author 刘武强
	 * @date 2015年8月13日下午2:48:37
	 * @param haveAllOrNot
	 *            0:不含有全部 1：含有全部
	 * @param @return
	 * @return List<Map<String,Object>>
	 * @throws
	 */
	public List<Map<String, Object>> getAllProvince(int haveAllOrNot) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<AdressVO> infolist = this.provinceDAO.getAllProvince();
		Map<String, Object> mapall = new HashMap<String, Object>();
		if (haveAllOrNot == 1) {
			mapall.put("key", "-1");
			mapall.put("value", "全部");
			list.add(mapall);
		}
		for (int i = 0; i < infolist.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			AdressVO adressVO = infolist.get(i);
			map.put("key", adressVO.getId());
			map.put("value", adressVO.getName());
			list.add(map);
		}
		return list;
	}

	/**
	 *
	 * @Title: updateExpressOrder
	 * @description 更新订单（新增时把账单id更新到订单/应付订单上；删除时把账单id从订单/应付订单中删掉）
	 * @author 刘武强
	 * @date  2015年8月17日上午9:38:40
	 * @param  @param expressCodBill 账单数据
	 * @param  @param deleteOrInsert “delete”和“insert”字符串
	 * @param  @param billType 表示账单类型的标志（应付/应收）
	 * @param  @return
	 * @return  boolean
	 * @throws
	 */
	private boolean updateExpressOrder(ExpressCodBill expressCodBill, String deleteOrInsert) {
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> whereParam = new HashMap<String, Object>();
		List<Integer> orderList = new ArrayList<Integer>();
		if ("insert".equals(deleteOrInsert)) {
			params.put("provincereceivablecodbillid", expressCodBill.getId());
			orderList = this.expressOrderDao
					.getOrderByConditionsNonfenye(expressCodBill.getReceivableProvinceId(), expressCodBill.getPayableProvinceId(), expressCodBill.getClosingDate(), DeliveryStateEnum.PeiSongChengGong
							.getValue(), FlowOrderTypeEnum.YiShenHe.getValue(), CwbOrderTypeIdEnum.Express.getValue());

		} else {
			params.put("provincereceivablecodbillid", 0);
			orderList = this.expressOrderDao.getOrderByProvincereceivablecodbillidNonfenye(expressCodBill.getId());
		}
		StringBuffer orderIds = new StringBuffer();
		orderIds.append("(");
		for (Integer temp : orderList) {
			orderIds.append("'" + temp + "'").append(",");
		}
		if (orderIds.indexOf(",") > -1) {
			whereParam.put("opscwbid", orderIds.substring(0, orderIds.lastIndexOf(",")) + ")");
		} else {
			whereParam.put("opscwbid", orderIds + "'')");
		}
		boolean flag = true;

		this.generalDAO.updateByIn(params, "express_ops_cwb_detail", whereParam);

		return flag;
	}

	/**
	 *
	 * @Title: insert
	 * @description 插入账单数据
	 * @author 刘武强
	 * @date  2015年8月17日下午7:46:16
	 * @param  @param expressCodBill 待插入的数据
	 * @param  @param billType 账单类型（应付/应收）
	 * @param  @return
	 * @return  long
	 * @throws
	 */
	private long insert(ExpressCodBill expressCodBill, int billType) {

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("bill_no", StringUtils.isNotBlank(expressCodBill.getBillNo()) ? expressCodBill.getBillNo() : null);
		params.put("bill_state", ExpressBillStateEnum.UnAudit.getValue());
		params.put("closing_date", expressCodBill.getClosingDate() != null ? expressCodBill.getClosingDate() : null);
		params.put("order_count", expressCodBill.getOrderCount());
		params.put("bill_type", billType);
		params.put("cod", expressCodBill.getCod());
		params.put("creator_id", this.getSessionUser().getBranchid());
		params.put("creator_name", this.getUserByUserid(this.getSessionUser().getBranchid()).getUsername());
		params.put("create_time", new Date());
		params.put("receivable_province_id", expressCodBill.getReceivableProvinceId());
		params.put("receivable_province_name", expressCodBill.getReceivableProvinceName());
		params.put("payable_province_id", expressCodBill.getPayableProvinceId());
		params.put("payable_province_name", expressCodBill.getPayableProvinceName());
		params.put("remark", StringUtils.isNotBlank(expressCodBill.getRemark()) ? expressCodBill.getRemark() : null);

		return this.generalDAO.insertReturnKey(params, "express_ops_cod_bill");
	}

	/**
	 *
	 * @Title: insertOrUpdate
	 * @description 新增/更新调用的方法
	 * @author 刘武强
	 * @date 2015年8月14日下午3:41:46
	 * @param @param expressCodBill 待插入或待更新的数据
	 * @param @param insertOrUpdate（insert废弃不用了，因为insert需要返回id,而本方法不能）
	 * @param @param billType 账单类型（应付/应收）
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	private boolean insertOrUpdate(ExpressCodBill expressCodBill, String insertOrUpdate, int billType) {
		if ("update".equals(insertOrUpdate)) {
			// 如果为跟新，那么先判断该id的记录是否为未审核
			ExpressCodBill vo = this.expressCodBillDAO.getInfobyId(expressCodBill.getId());
			if ((vo == null) || (vo.getBillState() != ExpressBillStateEnum.UnAudit.getValue())) {
				return false;
			}
		}
		Map<String, Object> params = new HashMap<String, Object>();
		if ("update".equals(insertOrUpdate)) {
			params.put("remark", StringUtils.isNotBlank(expressCodBill.getRemark()) ? expressCodBill.getRemark() : null);
		} else {
			params.put("bill_no", StringUtils.isNotBlank(expressCodBill.getBillNo()) ? expressCodBill.getBillNo() : null);
			params.put("bill_state", ExpressBillStateEnum.UnAudit.getValue());
			params.put("closing_date", expressCodBill.getClosingDate() != null ? expressCodBill.getClosingDate() : null);
			params.put("order_count", expressCodBill.getOrderCount());
			params.put("bill_type", billType);
			params.put("cod", expressCodBill.getCod());
			params.put("creator_id", this.getSessionUser().getBranchid());
			params.put("creator_name", this.getUserByUserid(this.getSessionUser().getBranchid()).getUsername());
			params.put("create_time", new Date());
			params.put("receivable_province_id", expressCodBill.getReceivableProvinceId());
			params.put("receivable_province_name", expressCodBill.getReceivableProvinceName());
			params.put("payable_province_id", expressCodBill.getPayableProvinceId());
			params.put("payable_province_name", expressCodBill.getPayableProvinceName());
			params.put("remark", StringUtils.isNotBlank(expressCodBill.getRemark()) ? expressCodBill.getRemark() : null);
		}

		boolean flag;
		if ("insert".equals(insertOrUpdate)) {
			flag = this.generalDAO.insert(params, "express_ops_cod_bill");
		} else {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", expressCodBill.getId());
			flag = this.generalDAO.update(params, "express_ops_cod_bill", map);
		}
		if ("true".equals(flag)) {
			this.logger.info("保存成功");

		} else {
			this.logger.info("保存失败");
		}
		return flag;
	}

	/**
	 *
	 * @Title: getExpressCodBillById
	 * @description 通过id，获取跨省账单数据
	 * @author 刘武强
	 * @date  2015年8月17日下午7:51:37
	 * @param  @param codBillId 账单id
	 * @param  @return
	 * @return  ExpressCodBill
	 * @throws
	 */
	public ExpressCodBill getExpressCodBillById(Long codBillId) {
		return this.expressCodBillDAO.getInfobyId(codBillId);
	}

	/**
	 *
	 * @Title: exportBill
	 * @description 跨省代收货款审核（应收）导出
	 * @author 刘武强
	 * @date  2015年8月19日下午4:06:16
	 * @param  @param provincereceivablecodbillid 账单的id
	 * @param  @param page 表格当前页数
	 * @param  @param request
	 * @param  @param response
	 * @return  void
	 * @throws
	 */
	public void exportBill(Long provincereceivablecodbillid, Integer page, HttpServletRequest request, HttpServletResponse response) {
		@SuppressWarnings("unchecked")
		List<EmbracedOrderVO> infoList = (List<EmbracedOrderVO>) this
				.getOrderByProvincereceivablecodbillid(provincereceivablecodbillid, ExpressBillTypeEnum.AcrossProvinceCodReceivableBill.getValue(), page, Page.ONE_PAGE_NUMBER).get("list");
		List<TransProvincialAuditReconciliationExportVO> list = new ArrayList<TransProvincialAuditReconciliationExportVO>();
		for (EmbracedOrderVO temp : infoList) {
			TransProvincialAuditReconciliationExportVO vo = new TransProvincialAuditReconciliationExportVO();
			vo.setOrderNo(temp.getOrderNo());
			vo.setNumber(temp.getNumber());
			vo.setDelivermanName(temp.getDelivermanName());
			vo.setDelivername(temp.getDelivername());
			vo.setCollection_amount(temp.getCollection_amount());
			vo.setInstationname(temp.getInstationname());
			list.add(vo);
		}
		String fileName = "跨省代收货款对账（应收）账单明细";
		ExportUtil4Order.myExportXls(request, response, list, TransProvincialAuditReconciliationExportVO.class, fileName);
	}
}
