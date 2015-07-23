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
			if (StringUtils.isNotBlank(punishinsideBill.getPunishNos())) {
				List<String> punishNoList = Arrays.asList(punishinsideBill
						.getPunishNos().split(","));
				String punishNos = StringUtil.getStringsByStringList(punishNoList);
				penalizeInsideList = this.punishinsideBillDAO.findByCondition(0,
						0, punishNos, 0, 0, "", "", -1, -9);
			}
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
		String nowTime = DateTimeUtil.getNowTime("yyyyMMddHHmmssSSS");
		String billBatch = rule + nowTime;
		return billBatch;
	}

	@Transactional
	public int createPunishinsideBill(
			ExpressOpsPunishinsideBill punishinsideBill) {

		String punishNos = punishinsideBill.getPunishNos();
		if(StringUtils.isNotBlank(punishNos)){
			List<String> list = Arrays.asList(punishNos.split(","));
			punishNos = StringUtil.getStringsByStringList(list);
		}
		List<PenalizeInside> penalizeInsideList = this.punishinsideBillDAO
				.findByCondition(punishinsideBill.getPunishbigsort(),
						punishinsideBill.getPunishsmallsort(),
						punishNos,
						punishinsideBill.getDutybranchid(),
						punishinsideBill.getDutypersonid(),
						punishinsideBill.getPunishNoCreateBeginDate(),
						punishinsideBill.getPunishNoCreateEndDate(),
						PunishInsideStateEnum.koufachengli.getValue(), -9);
		PenalizeInside penalizeInside = null;
		punishNos = "";
		BigDecimal sumPrice = new BigDecimal(0.0);
		if (penalizeInsideList != null && penalizeInsideList.size() > 0) {
			for (int i = 0; i < penalizeInsideList.size(); i++) {
				penalizeInside = penalizeInsideList.get(i);
				if (StringUtils.isNotBlank(penalizeInside.getPunishNo())) {
					punishNos += penalizeInside.getPunishNo() + ",";
				}
				if (penalizeInside.getPunishInsideprice() != null) {
					sumPrice = sumPrice.add(penalizeInside
							.getPunishInsideprice());
				}
			}
		}
		if (StringUtils.isNotBlank(punishNos)) {
			punishNos = punishNos.substring(0, punishNos.length() - 1);
		}
		punishinsideBill.setPunishNos(punishNos);
		punishinsideBill.setSumPrice(sumPrice);
		punishinsideBill.setCreateDate(DateTimeUtil.getNowDate());
		String billBatch = this.generateBillBatch();
		punishinsideBill.setBillBatch(billBatch);
		punishinsideBill.setBillState(PunishBillStateEnum.WeiShenHe.getValue());
		punishinsideBill.setShenHeDate(StringUtil
				.nullConvertToEmptyString(punishinsideBill.getShenHeDate()));
		punishinsideBill.setCheXiaoDate(StringUtil
				.nullConvertToEmptyString(punishinsideBill.getCheXiaoDate()));
		punishinsideBill.setHeXiaoDate(StringUtil
				.nullConvertToEmptyString(punishinsideBill.getHeXiaoDate()));
		punishinsideBill.setQuXiaoHeXiaoDate(StringUtil
				.nullConvertToEmptyString(punishinsideBill
						.getQuXiaoHeXiaoDate()));
		punishinsideBill
				.setPunishInsideIds(StringUtil
						.nullConvertToEmptyString(punishinsideBill
								.getPunishInsideIds()));
		long id = this.punishinsideBillDAO
				.createPunishinsideBill(punishinsideBill);
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
			List<ExpressOpsPunishinsideBill> billList = this.punishinsideBillDAO
					.getMaxBillBatch();
			String punishNosSaved = "";
			if (billList != null && !billList.isEmpty()) {
				for (int i = 0; i < billList.size(); i++) {
					if (billList.get(i).getId() != billVO.getId()) {
						punishNosSaved += billList.get(i).getPunishNos() + ",";
					}
				}
				if (StringUtils.isNotBlank(punishNosSaved)) {
					punishNos = StringUtil.removalDuplicateString(punishNos,
							punishNosSaved);
				}
			}
			if(StringUtils.isNotBlank(punishNos)){
				sumPrice = this.punishinsideBillDAO
						.calculateSumPrice(StringUtil.getStringsByStringList(Arrays
								.asList(punishNos.split(","))));
			}
		}
		this.punishinsideBillDAO.updatePunishinsideBill(punishNos,sumPrice,
				billVO.getId());
	}
}
