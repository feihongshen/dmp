package cn.explink.service.express.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.dao.express.ExpressFreightBillDAO;
import cn.explink.domain.VO.express.CwbOrderBillInfo;
import cn.explink.domain.VO.express.ExpressBillBasePageView;
import cn.explink.domain.VO.express.ExpressBillParams4Create;
import cn.explink.domain.VO.express.ExpressBillParamsVO4Query;
import cn.explink.domain.VO.express.ExpressBillSummary;
import cn.explink.domain.VO.express.ExpressOpeAjaxResult;
import cn.explink.domain.VO.express.SelectReturnVO;
import cn.explink.domain.express.ExpressFreightBill;
import cn.explink.enumutil.express.ExpressBillStateEnum;
import cn.explink.service.express.ExpressBillCommonService;
import cn.explink.util.express.Page;


@Service
@Transactional
public class ExpressBillCommonServiceImpl implements ExpressBillCommonService {
	private Logger logger = org.slf4j.LoggerFactory.getLogger(ExpressBillCommonServiceImpl.class);
	@Autowired
	protected ExpressFreightBillDAO expressFreightDao;
	
	@Override
	public Map<String, Object> getFreightBillList(ExpressBillParamsVO4Query params) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<ExpressBillBasePageView> list = expressFreightDao.getRecordByParams(params);
		Long record = expressFreightDao.getRecordTotalCount(params);
		map.put("list", list);
		map.put("count", record);
		return map;
	}

	@Override
	public ExpressOpeAjaxResult deleteBillByBillId(Long id,Integer billType) {
		ExpressOpeAjaxResult res = new ExpressOpeAjaxResult();
		try{
			// 查询记录
			ExpressFreightBill bill = this.getRecordById(id);
			if (bill != null) {
				if (!ExpressBillStateEnum.UnAudit.getValue().equals(bill.getBillState())) {
					//不是未审核的账单
					res.setStatus(false);
					res.setMsg(bill.getBillNo()+"账单状态为，"+ExpressBillStateEnum.getByValue(bill.getBillState())+"不能删除");
				}else {
					expressFreightDao.deleteBillByBillId(id);
					expressFreightDao.updateCwbDetailBillId(id,billType);
					
					res.setStatus(true);
					logger.debug("账单{}，删除成功",bill.getBillNo());
				}
			}else {
				res.setStatus(false);
				res.setMsg("账单不存在");
				logger.debug("账单id为{}的账单不存在",id);
			}
		}catch(Exception e){
			res.setStatus(false);
			res.setMsg("系统错误，请联系管理员");
			logger.info("删除账单异常："+e.getMessage());
		}
		return res;
	}

	@Override
	public ExpressOpeAjaxResult createBill(ExpressBillParams4Create params) {
		ExpressOpeAjaxResult res = new ExpressOpeAjaxResult();
		params.setIsPreScan(true);
		try {
			Map<Long, String> deliverManMap = new HashMap<Long, String>();
			//结果的map
			double transportFee = BigDecimal.ZERO.doubleValue();
			double receiveableFee = BigDecimal.ZERO.doubleValue();
			List<String> cwbs = new ArrayList<String>();
			List<CwbOrderBillInfo> list = expressFreightDao.getCwbRecordListByPage(deliverManMap,params,false);
			if (list!=null&&list.size()>0) {
				params.setOrderCount(list.size());//设置单量
				for (CwbOrderBillInfo entity : list) {
					transportFee += entity.getTransportFeeTotal().doubleValue();
					receiveableFee += entity.getReceivablefee().doubleValue();
					cwbs.add(entity.getOrderNo());
				}
				params.setTransFee(new BigDecimal(transportFee));
				params.setReceiveableFee(new BigDecimal(receiveableFee));
				//创建账单
				Long billId = expressFreightDao.createBillRecord(params);
				//查询出账单
//				ExpressFreightBill bill = this.getRecordById(Long.valueOf(billId));
				ExpressBillBasePageView view = expressFreightDao.getEditPageViewByBillId(billId);
				//批量更新订单数据
				expressFreightDao.updateCwbRecord(billId,cwbs,params.getOpeFlag());
				params.setBillId(billId);//缓存账单的id
				res.setObj(view);
				res.setStatus(true);
			}else {
				res.setStatus(false);
				res.setMsg("没有可以生成账单的订单数据");
			}
		} catch (Exception e) {
			res.setStatus(false);
			res.setMsg(e.getMessage());
		}
		return res;
	}

	@Override
	public ExpressFreightBill getRecordById(Long billId) {
		return expressFreightDao.getRecordById(billId);
	}

	@Override
	public ExpressFreightBill getRecoredByBillNo(String billNo) {
		return expressFreightDao.getRecoredByBillNo(billNo);
	}

	@Override
	public void updateRecordRemarkById(Long billId, String remark) {
		expressFreightDao.updateRecordById(billId,remark);
	}

	@Override
	public Map<String, Object> getCwbInfoList(ExpressBillParams4Create params) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		Map<Long, String> deliverManMap = new HashMap<Long, String>();
		//获取分页数据
		List<CwbOrderBillInfo> list = expressFreightDao.getCwbRecordListByPage(deliverManMap,params,true);
		if(list!=null&&list.size()>0){
			//获取总的记录数
			Long recordCount = expressFreightDao.getExpressRecordCount(params);
			if (params.getIsPreScan()) {//预览界面才需要看汇总
				//查询汇总记录
				ExpressBillSummary summary = expressFreightDao.getSummaryRecord(params);
				resultMap.put("summary", summary);
			}
			//给前台分页使用
			Page<CwbOrderBillInfo> page = new Page<CwbOrderBillInfo>();
			page.setPageSize(cn.explink.util.Page.ONE_PAGE_NUMBER);
			page.setTotalCount(recordCount);
			page.setPageNo(params.getPage().intValue());
			resultMap.put("page", page);
			resultMap.put("list", list);
			resultMap.put("isCorrect", true);
		}else {
			resultMap.put("isCorrect", false);
			resultMap.put("msg", "没有可以生成账单的订单数据");
		}
		
		return resultMap;
	}

	@Override
	public Map<String, Object> getEditViewBillInfo(Long billId, Long page,Integer opeFlag) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<Long, String> deliverManMap = new HashMap<Long, String>();
		ExpressBillBasePageView view = expressFreightDao.getEditPageViewByBillId(billId);
		List<CwbOrderBillInfo> cwbList = expressFreightDao.getBillRelateCwb(billId,deliverManMap,page,opeFlag);
		Page<CwbOrderBillInfo> pageEntity = new Page<CwbOrderBillInfo>();
		Long count = expressFreightDao.getExpressRecordCount(billId,opeFlag);
		pageEntity.setPageNo(page.intValue());
		pageEntity.setPageSize(cn.explink.util.Page.ONE_PAGE_NUMBER);
		pageEntity.setTotalCount(count);
		map.put("page", pageEntity);
		map.put("list", cwbList);
		map.put("view", view);
		return map;
	}

	@Override
	public ExpressOpeAjaxResult updateBillByBillId(Long billId, String remark) {
		ExpressOpeAjaxResult res = new ExpressOpeAjaxResult();
		try {
			expressFreightDao.updateBillByBillId(billId,remark);
			res.setStatus(true);
		} catch (Exception e) {
			res.setStatus(false);
			res.setMsg("账单更新操作失败："+e.getMessage());
			logger.debug("账单更新操作失败："+e.getMessage());
		}
		return res;
	}
	/**
	 * 将查询出来的信息转换为map
	 * @param data
	 * @return
	 */
	protected Map<Long, String> getMapInfo(List<SelectReturnVO> data){
		Map<Long, String> mapInfo = new HashMap<Long, String>();
		if (data!=null&&data.size()>0) {
			for (SelectReturnVO sele : data) {
				mapInfo.put(sele.getHiddenValue(), sele.getDisplayValue());
			}
		}
		return mapInfo;
	}
	
}
