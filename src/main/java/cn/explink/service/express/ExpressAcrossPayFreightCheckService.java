package cn.explink.service.express;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.express.ExpressFreightBillDAO;
import cn.explink.dao.express.ExpressFreightBillImportDetailDao;
import cn.explink.dao.express.ExpressFreightBillSelectDAO;
import cn.explink.dao.express.ProvinceDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.User;
import cn.explink.domain.VO.express.AdressVO;
import cn.explink.domain.VO.express.CwbOrderBillInfo;
import cn.explink.domain.VO.express.CwbOrderPartInfo;
import cn.explink.domain.VO.express.ExpressBillBasePageView;
import cn.explink.domain.VO.express.ExpressBillParams4Create;
import cn.explink.domain.VO.express.ExpressOpeAjaxResult;
import cn.explink.domain.VO.express.ExpressPayImportParams;
import cn.explink.domain.VO.express.SelectReturnVO;
import cn.explink.domain.VO.express.UserInfo;
import cn.explink.domain.express.ExpressFreightBillImportDetail;
import cn.explink.enumutil.express.ExpressEffectiveEnum;
import cn.explink.enumutil.express.ExpressNotMatchReasonEnum;
import cn.explink.service.ExcelExtractor;
import cn.explink.service.express.impl.ExpressBillCommonServiceImpl;
import cn.explink.util.Tools;
import cn.explink.util.express.DataSetHelper;
import cn.explink.util.express.ExpressDataSetUtil;
import cn.explink.util.express.ExpressImportSelfException;
import cn.explink.util.express.FieldValidator;
import cn.explink.util.express.Page;

@Service
public class ExpressAcrossPayFreightCheckService extends ExpressBillCommonServiceImpl {
	private Logger logger = LoggerFactory.getLogger(ExpressAcrossPayFreightCheckService.class);

	@Autowired
	ExpressFreightBillSelectDAO selectDao;
	@Autowired
	ProvinceDAO provinceDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	ExpressFreightBillImportDetailDao expressFreightBillImportDetailDao;
	@Autowired
	ExpressFreightBillDAO expressFreightBillDAO;

	/**
	 * 应付省份数据
	 * 
	 * @return
	 */
	public List<SelectReturnVO> initReceAbleProvinceSelect(Long currentBranchId) {
		// 通过机构获取机构的省份
		Branch branch = branchDAO.getBranchByBranchid(currentBranchId);
		return selectDao.getProvinceData(branch.getBranchprovinceid());
	}

	/**
	 * 获取当前登录人的信息
	 * 
	 * @param user
	 * @return
	 */
	public UserInfo initUserInfo(User user) {
		UserInfo userInfo = null;
		// 通过机构获取机构的省份
		Branch branch = branchDAO.getBranchByBranchid(user.getBranchid());
		try {
			SelectReturnVO selectVo = selectDao.getCurrentUserInfo(branch.getBranchprovinceid());
			if (selectVo != null) {
				userInfo = new UserInfo(branch.getBranchid(), branch.getBranchname(), selectVo.getHiddenValue(), selectVo.getDisplayValue(), user.getUserid(), user.getRealname());
			} else {
				userInfo = new UserInfo(branch.getBranchid(), branch.getBranchname(), 0L, "", user.getUserid(), user.getRealname());
			}
		} catch (Exception e) {
			logger.info("查询用户所在站点的省份信息异常：" + e.getMessage());
			userInfo = new UserInfo(branch.getBranchid(), branch.getBranchname(), 0L, "", user.getUserid(), user.getRealname());
		}
		// 查询当前用户站点所在的
		return userInfo;
	}

	/**
	 * 获取所有的省份信息
	 * 
	 * @return
	 */
	public Map<Long, String> initProvince() {
		Map<Long, String> map = new HashMap<Long, String>();
		List<AdressVO> provinces = provinceDAO.getAllProvince();
		if (provinces != null && provinces.size() > 0) {
			for (AdressVO adressVO : provinces) {
				map.put(Long.valueOf(adressVO.getId()), adressVO.getName());
			}
		}
		return map;
	}

	/**
	 * 仅仅创建空账单
	 * @param params
	 * @return
	 */
	@Transactional(readOnly = false)
	public ExpressOpeAjaxResult createBillAlong(ExpressBillParams4Create params) {
		ExpressOpeAjaxResult res = new ExpressOpeAjaxResult();
		params.setTransFee(BigDecimal.ZERO);
		params.setReceiveableFee(BigDecimal.ZERO);
		try {
			Long billId = expressFreightDao.createBillRecord(params);
			ExpressBillBasePageView view = expressFreightDao.getEditPageViewByBillId(billId);
			params.setBillId(billId);// 缓存账单的id
			res.setObj(view);
			res.setStatus(true);
		} catch (Exception e) {
			res.setStatus(false);
			res.setMsg(e.getMessage());
		}
		return res;
	}
	
	
	/**
	 * 更新账单的信息
	 */
	@Transactional(readOnly = false)
	public ExpressOpeAjaxResult updateBillByBillId(Long billId, String remark) {
		ExpressOpeAjaxResult res = new ExpressOpeAjaxResult();
		try {
			//将导入的记录关联导入的应收订单
			//1.修改记录状态
			Integer orderCount = expressFreightBillImportDetailDao.batchUpdateImportDetail(billId,ExpressEffectiveEnum.Effective.getValue());
			//更新账单的信息
			expressFreightBillImportDetailDao.updateBillByBillId(billId,remark,orderCount);
			res.setStatus(true);
		} catch (Exception e) {
			res.setStatus(false);
			res.setMsg("账单更新操作失败："+e.getMessage());
			logger.debug("账单更新操作失败："+e.getMessage());
		}
		return res;
	}
	
	/**
	 * 导入
	 * @param inputStream
	 * @param excelExtractor
	 * @param user 
	 * @return
	 * @throws ExpressImportSelfException 
	 */
	@Transactional(readOnly = false)
	public ExpressOpeAjaxResult importExcelForBill(InputStream is, ExcelExtractor excelExtractor, ExpressPayImportParams params) throws ExpressImportSelfException{
		// TODO 【6.导入应收账单：根据应收所导出的信息进行导入操作（配送站点需要从系统抓取）；】
		
		//运单若匹配不到省公司，处理方式同上
		ExpressOpeAjaxResult result = new ExpressOpeAjaxResult();
		Map<String, Object> map = new HashMap<String, Object>();
		
		
		//获取数据
		List<Object> rowsList = excelExtractor.getRows(is);
		//1.校验数据存不存在
		if (rowsList==null || rowsList.size()<=2) {
			result.setStatus(false);
			result.setMsg("数据不存在");
			return result;
		}
		
		
		//缓存真实的数据
		List<Object> realData = new ArrayList<Object>();
		//存放导入的运单号
		List<String> orderNos = new ArrayList<String>();
		//存放格式校验通过的运单记录
		List<ExpressFreightBillImportDetail> records = new ArrayList<ExpressFreightBillImportDetail>();
		
		
		//2.获取导入的数据
		realData = rowsList.subList(3, rowsList.size());//TODO
		//3.循环解析数据
		for (Object row : realData) {
			//开始解析
			ExpressFreightBillImportDetail billDetail = new ExpressFreightBillImportDetail(params);
			//格式的校验【一条失败则全部失败】
			checkDatas(billDetail,row,excelExtractor);
			orderNos.add(billDetail.getOrderNo());
			records.add(billDetail);
		}
		
		//4.查询系统中是否存在已经校验成功的订单记录
		Map<String, Object> existDataMap = expressFreightBillImportDetailDao.queryRecordIsExist(orderNos);
		//5.校验记录是否存在别的账单中
		for (ExpressFreightBillImportDetail detail : records) {
			if (!Tools.isEmpty(existDataMap.get(detail.getOrderNo()))) {//如果账单中包含了此订单则不能够导入，也即重复导入的校验
				//抛出异常
				throw new ExpressImportSelfException("订单"+detail.getOrderNo()+"已存在账单"+existDataMap.get(detail.getOrderNo())+"中");
			}
		}
		
		
		//6.修改之前导入的订单状态的改变[将之前导入的记录状态为0的修改为状态为2]
		expressFreightBillImportDetailDao.batchUpdateImportDetail(params.getBillId(),ExpressEffectiveEnum.other.getValue());
		//7.批量插入操作
		expressFreightBillImportDetailDao.batchInsertOrderRecord(records);
		
		map.put("totalCount", realData.size());
		result.setStatus(true);
		result.setAttributes(map);
		return result;
	}
	
	/**
	 * 获取编辑的页面
	 */
	@Transactional(readOnly = true)
	public Map<String, Object> getEditViewBillInfo(Long billId, Long page,Integer effectiveState,Boolean isPaging) {
		Map<String, Object> map = new HashMap<String, Object>();
		ExpressBillBasePageView view = expressFreightBillDAO.getEditPageViewByBillId(billId);
		List<CwbOrderBillInfo> cwbList = expressFreightBillImportDetailDao.getBillRelateCwb(billId,page,effectiveState,isPaging);
		Page<CwbOrderBillInfo> pageEntity = new Page<CwbOrderBillInfo>();
		Long count = expressFreightBillImportDetailDao.getExpressRecordCount(billId,effectiveState);
		pageEntity.setPageNo(page.intValue());
		pageEntity.setPageSize(cn.explink.util.Page.ONE_PAGE_NUMBER);
		pageEntity.setTotalCount(count);
		map.put("page", pageEntity);
		map.put("list", cwbList);
		map.put("view", view);
		return map;
	}
	
	
	/**
	 * 校验导入的数据
	 * @param billDetail
	 * @param row
	 * @param excelExtractor
	 * @throws ExpressImportSelfException 
	 */
	private void checkDatas(ExpressFreightBillImportDetail billDetail, Object row,ExcelExtractor excelExtractor) throws ExpressImportSelfException{
		List<DataSetHelper> dataHelper = ExpressDataSetUtil.getInstance().getDataSetHelper();
		String orderNo = excelExtractor.getXRowCellData(row, 1);
		for (DataSetHelper h : dataHelper) {
			FieldValidator f = new FieldValidator(h.field.getValidateRule());
			// 根据导入模板中字段，找到对应的excel数据及字段对应的值
			String data = removeZero(excelExtractor.getXRowCellData(row, h.index));
			try {
				f.validator(data);
				h.method.invoke(billDetail, h.clazz.cast(h.parse(data)));
			} catch(NumberFormatException e1){
				logger.info(h.field.getFiledName() + "<" + data+ ">验证错误,"+"数据格式错误");
				throw new ExpressImportSelfException("运单号："+orderNo+"--"+h.field.getFiledName() + "<" + data+ ">验证错误,"+"数据格式错误");
			}catch (Exception e) {
				logger.info(h.field.getFiledName() + "<" + data+ ">验证错误,"+e.getMessage());
				throw new ExpressImportSelfException("运单号："+orderNo+"--"+h.field.getFiledName() + "<" + data+ ">验证错误,"+e.getMessage());
			}
		}
	}
	
	
	private String removeZero(String cwb) {
		int end = cwb.indexOf(".0");
		if (end > -1) {
			cwb = cwb.substring(0, end);
		}
		return cwb;
	}
	
	/**
	 * 自动核对订单数据
	 * @param billId
	 * @param page
	 * @param value
	 * @return 
	 */
	@Transactional(readOnly=false)
	public Map<String, Object> verifyRecords(Long billId, Long page, Integer value) {
		Map<String, Object> map = new HashMap<String, Object>();
		//【核对的逻辑】
		//1.查询核对之后的数据
		List<String> orderNos = new ArrayList<String>();
		List<CwbOrderBillInfo> cwbList = expressFreightBillImportDetailDao.getBillRelateCwb(billId,page,ExpressEffectiveEnum.UnEffective.getValue(),false);
		if (cwbList!=null&&cwbList.size()>0) {
			for (CwbOrderBillInfo detail : cwbList) {
				orderNos.add(detail.getOrderNo());
			}
		}
		//查询对应的主表中【订单表】的记录
		Map<String,CwbOrderPartInfo> recordMap = expressFreightBillImportDetailDao.getCwbOrderPartInfo(orderNos);
		List<CwbOrderBillInfo> pageViewList = new ArrayList<CwbOrderBillInfo>();
		List<CwbOrderBillInfo> redList = new ArrayList<CwbOrderBillInfo>();
		List<CwbOrderBillInfo> blackList = new ArrayList<CwbOrderBillInfo>();
		//核对订单信息
		if (cwbList!=null&&cwbList.size()>0) {
			for (CwbOrderBillInfo order : cwbList) {
				try {
					validateOrderInfo(recordMap, order);
					blackList.add(order);
				} catch (ExpressImportSelfException e) {
					order.setNotMatchReason(e.getMessage());
					order.setTrColor("red");
					redList.add(order);
				}
			}
			pageViewList.addAll(redList);
			pageViewList.addAll(blackList);
		}
		//将原因字段更新
		if (redList!=null&&redList.size()>0) {
			for (CwbOrderBillInfo cwbOrderBillInfo : redList) {
				expressFreightBillImportDetailDao.updateNotMatchReason(cwbOrderBillInfo,billId);
			}
		}
		
		Page<CwbOrderBillInfo> pageEntity = new Page<CwbOrderBillInfo>();
		List<CwbOrderBillInfo> cwbOrderedList = expressFreightBillImportDetailDao.getBillRelateCwb(billId,page,ExpressEffectiveEnum.UnEffective.getValue(),true);
		Long count = expressFreightBillImportDetailDao.getExpressRecordCount(billId,ExpressEffectiveEnum.UnEffective.getValue());
		pageEntity.setPageNo(page.intValue());
		pageEntity.setPageSize(cn.explink.util.Page.ONE_PAGE_NUMBER);
		pageEntity.setTotalCount(count);
		map.put("page", pageEntity);
		map.put("list", pageViewList);
		return map;
		
	}
	/**
	 * 核对时候的校验
	 * @param recordMap
	 * @param order
	 * @throws ExpressImportSelfException
	 */
	private void validateOrderInfo(Map<String, CwbOrderPartInfo> recordMap, CwbOrderBillInfo order) throws ExpressImportSelfException {
		if (Tools.isEmpty(recordMap.get(order.getOrderNo()))) {
			//1.运单号不存在
			throw new ExpressImportSelfException(ExpressNotMatchReasonEnum.TransNoNotExist.getText());
		}else {
			if (!order.getTransportFeeTotal().equals(recordMap.get(order.getOrderNo()).getDeliveryFee())) {
				//2.运费合计不匹配
				throw new ExpressImportSelfException(ExpressNotMatchReasonEnum.TransNoNotExist.getText());
			}else if(order.getBranchId().equals(recordMap.get(order.getOrderNo()).getBranchId())){
				//3.站点不匹配
				throw new ExpressImportSelfException(ExpressNotMatchReasonEnum.TransNoNotExist.getText());
			}
		}
	}
}
