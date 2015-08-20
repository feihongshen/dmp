package cn.explink.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.dao.PunishInsideDao;
import cn.explink.dao.PunishinsideBillDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.ExpressOpsPunishinsideBill;
import cn.explink.domain.PenalizeInside;
import cn.explink.domain.User;
import cn.explink.domain.VO.ExpressOpsPunishinsideBillVO;
import cn.explink.enumutil.PunishBillStateEnum;
import cn.explink.enumutil.PunishInsideStateEnum;
import cn.explink.util.BeanUtilsSelfDef;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.StringUtil;

@Service
public class PunishinsideBillService {

	@Autowired
	PunishinsideBillDAO punishinsideBillDAO;
	@Autowired
	PunishInsideDao punishInsideDao;
	@Autowired
	UserDAO userDAO;

	public ExpressOpsPunishinsideBillVO getPunishinsideBillVO(int id) {
		// 返回值
		ExpressOpsPunishinsideBillVO rtnVO = null;
		List<PenalizeInside> penalizeInsideList = new ArrayList<PenalizeInside>();

		ExpressOpsPunishinsideBill punishinsideBill = this.punishinsideBillDAO
				.getPunishinsideBillListById(id);
		if (punishinsideBill != null) {
			rtnVO = new ExpressOpsPunishinsideBillVO();
			BeanUtilsSelfDef.copyPropertiesIgnoreException(rtnVO,
					punishinsideBill);
			rtnVO = setPersonName(rtnVO);
			penalizeInsideList = this.punishinsideBillDAO.getPunishinsideDetailListByBillId(id);
		}
		rtnVO.setPenalizeInsideList(penalizeInsideList);
		return rtnVO;
	}

	@Transactional
	public void updatePunishinsideBillVO(
			ExpressOpsPunishinsideBillVO punishinsideBillVO) {
		ExpressOpsPunishinsideBill punishinsideBill = new ExpressOpsPunishinsideBill();
		if (punishinsideBillVO != null) {
			BeanUtilsSelfDef.copyPropertiesIgnoreException(punishinsideBill,
					punishinsideBillVO);
			this.punishinsideBillDAO.updatePunishinsideBill(punishinsideBill);
		}
	}

	@Transactional
	public void deletePunishinsideBill(String ids) {
		List<String> list = Arrays.asList(ids.split(","));
		ids = StringUtil.getStringsByStringList(list);
		this.punishinsideBillDAO.deletePunishinsideBill(ids);
	}

	public String generateBillBatch() {
		String rule = "K";
		String nowTime = DateTimeUtil.getNowTime("yyyyMMdd");
		String billBatch = rule + nowTime;
		String orderStr = "0001";
		ExpressOpsPunishinsideBill bill = this.punishinsideBillDAO.getMaxBillBatch(billBatch);
		if(bill != null){
			String maxBillBatch = bill.getBillBatch();
			if(StringUtils.isNotBlank(maxBillBatch)){
				String maxOrderStr = maxBillBatch.substring(9);
				int maxOrderInt = Integer.valueOf(maxOrderStr);
				maxOrderInt++;
				orderStr = String.valueOf(maxOrderInt);
				while(orderStr.length() < 4){
					orderStr = "0" + orderStr;
				}
			}
		}
		billBatch = billBatch + orderStr;
		return billBatch;
	}

	@Transactional
	public int createPunishinsideBill(
			ExpressOpsPunishinsideBillVO billVO) {

		String punishNos = billVO.getPunishNos();
		if(StringUtils.isNotBlank(punishNos)){
			List<String> list = Arrays.asList(punishNos.split(","));
			punishNos = StringUtil.getStringsByStringList(list);
		}
		// 获取对内扣罚列表
		List<PenalizeInside> penalizeInsideList = this.punishinsideBillDAO
				.findByCondition(billVO.getPunishbigsort(),
						billVO.getPunishsmallsort(),
						punishNos,
						billVO.getDutybranchid(),
						billVO.getDutypersonid(),
						billVO.getPunishNoCreateBeginDate(),
						billVO.getPunishNoCreateEndDate(),
						PunishInsideStateEnum.koufachengli.getValue(), -9);
		PenalizeInside penalizeInside = null;
		punishNos = "";
		BigDecimal sumPrice = new BigDecimal(0.0);
		if (penalizeInsideList != null && penalizeInsideList.size() > 0) {
			for (int i = 0; i < penalizeInsideList.size(); i++) {
				penalizeInside = penalizeInsideList.get(i);
				if (StringUtils.isNotBlank(penalizeInside.getPunishNo())) {
					punishNos += penalizeInside.getPunishNo() + ",";
					if (penalizeInside.getPunishInsideprice() != null) {
						sumPrice = sumPrice.add(penalizeInside.getPunishInsideprice());
					}
				}
			}
		}
		billVO.setSumPrice(sumPrice);
		billVO.setCreateDate(DateTimeUtil.getNowDate());
		billVO.setBillBatch(this.generateBillBatch());
		billVO.setBillState(PunishBillStateEnum.WeiShenHe.getValue());
		billVO.setShenHeDate(StringUtil.nullConvertToEmptyString(billVO.getShenHeDate()));
		billVO.setCheXiaoDate(StringUtil.nullConvertToEmptyString(billVO.getCheXiaoDate()));
		billVO.setHeXiaoDate(StringUtil.nullConvertToEmptyString(billVO.getHeXiaoDate()));
		billVO.setQuXiaoHeXiaoDate(StringUtil.nullConvertToEmptyString(billVO.getQuXiaoHeXiaoDate()));
		// 创建对内扣罚账单表
		long id = this.punishinsideBillDAO.createPunishinsideBill(billVO);
		// 更新对内扣罚表中billId字段(billId为对内扣罚账单表主键id)
		if (StringUtils.isNotBlank(punishNos)) {
			punishNos = punishNos.substring(0, punishNos.length() - 1);
			List<String> list = Arrays.asList(punishNos.split(","));
			punishNos = StringUtil.getStringsByStringList(list);
			this.punishinsideBillDAO.updatePunishinsideDetail(punishNos, (new Long(id)).intValue());
		}
		return new Long(id).intValue();
	}

	public ExpressOpsPunishinsideBillVO setPersonName(
			ExpressOpsPunishinsideBillVO billVO) {
		if (billVO != null) {
			User user = null;
			if (billVO.getCreator() != 0) {
				user = this.userDAO.getAllUserByid(billVO.getCreator());
				if (user != null) {
					billVO.setCreatorName(user.getRealname());
				}
			}
			if (billVO.getShenHePerson() != 0) {
				user = this.userDAO.getAllUserByid(billVO.getShenHePerson());
				if (user != null) {
					billVO.setShenHePersonName(user.getRealname());
				}
			}
			if (billVO.getCheXiaoPerson() != 0) {
				user = this.userDAO.getAllUserByid(billVO.getCheXiaoPerson());
				if (user != null) {
					billVO.setCheXiaoPersonName(user.getRealname());
				}
			}
			if (billVO.getHeXiaoPerson() != 0) {
				user = this.userDAO.getAllUserByid(billVO.getHeXiaoPerson());
				if (user != null) {
					billVO.setHeXiaoPersonName(user.getRealname());
				}
			}
			if (billVO.getQuXiaoHeXiaoPerson() != 0) {
				user = this.userDAO.getAllUserByid(billVO
						.getQuXiaoHeXiaoPerson());
				if (user != null) {
					billVO.setQuXiaoHeXiaoPersonName(user.getRealname());
				}
			}
			if (billVO.getDutypersonid() != 0) {
				user = this.userDAO.getAllUserByid(billVO.getDutypersonid());
				if (user != null) {
					billVO.setDutypersonname(user.getRealname());
				}
			}
		}
		return billVO;
	}

	public void updatePunishinsideBill(ExpressOpsPunishinsideBillVO billVO) {
		String punishNos = billVO.getPunishNos();
		BigDecimal sumPrice = new BigDecimal(0.0);
		if(StringUtils.isNotBlank(punishNos)){
			punishNos = StringUtil.removalDuplicateString(billVO
					.getPunishNos());
			List<PenalizeInside> detailList = this.punishinsideBillDAO
					.getExistedPunishinsideDetailList();
			// 对内扣罚账单表中已存在的扣罚单号
			String punishNosSaved = "";
			if (detailList != null && !detailList.isEmpty()) {
				for (int i = 0; i < detailList.size(); i++) {
					if (detailList.get(i).getBillId() != billVO.getId()) {
						punishNosSaved += detailList.get(i).getPunishNo() + ",";
					}
				}
				if (StringUtils.isNotBlank(punishNosSaved)) {
					// 移除表中已存在的扣罚单号
					punishNos = StringUtil.removalDuplicateString(punishNos,
							punishNosSaved);
				}
			}
			if(StringUtils.isNotBlank(punishNos)){
				sumPrice = this.punishinsideBillDAO
						.calculateSumPrice(StringUtil.getStringsByStringList(Arrays
								.asList(punishNos.split(","))));
				List<String> list = Arrays.asList(punishNos.split(","));
				punishNos = StringUtil.getStringsByStringList(list);
				// 更新对内扣罚表billId字段
				this.punishinsideBillDAO.updatePunishinsideDetail(punishNos, billVO.getId());
				// 更新对内扣罚账单表
				this.punishinsideBillDAO.updatePunishinsideBill(sumPrice, billVO.getId());
				return;
			}
		}
		// 更新对内扣罚表billId字段
		this.punishinsideBillDAO.updatePunishinsideDetail(billVO.getId());
		// 更新对内扣罚账单表
		this.punishinsideBillDAO.updatePunishinsideBill(sumPrice, billVO.getId());
		return;
	}
	
	public 	String getExistedPunishNos(int id){
		// 所有已存在的赔付单号
		String existedPunishNos = "";
		List<PenalizeInside> detailList = this.punishinsideBillDAO.getExistedPunishinsideDetailList();
		if (detailList != null && !detailList.isEmpty()) {
			for (int i = 0; i < detailList.size(); i++) {
				if (StringUtils.isNotBlank(detailList.get(i).getPunishNo())) {
					existedPunishNos = detailList.get(i).getPunishNo() + "," + existedPunishNos;
				}
			}
		}
		existedPunishNos = existedPunishNos.substring(0, existedPunishNos.length()-1);
		return existedPunishNos;
	}
}
