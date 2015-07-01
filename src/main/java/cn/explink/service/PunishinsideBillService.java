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
import cn.explink.util.BeanUtilsSelfDef;
import cn.explink.util.DateTimeUtil;

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

		ExpressOpsPunishinsideBill punishinsideBill = this.punishinsideBillDAO.getPunishinsideBillListById(id);
		if (punishinsideBill != null) {
			rtnVO = new ExpressOpsPunishinsideBillVO();
			BeanUtilsSelfDef.copyPropertiesIgnoreException(rtnVO,
					punishinsideBill);
			rtnVO = setPersonName(rtnVO); 
			if(StringUtils.isNotBlank(punishinsideBill.getPunishNos())){
				List<String> punishNoList = Arrays.asList(punishinsideBill.getPunishNos().split(","));
				PenalizeInside penalizeInside = null;
				for(int i = 0; i< punishNoList.size(); i++){
					penalizeInside = this.punishInsideDao.getInsideByPunishNo(punishNoList.get(i));
					penalizeInsideList.add(penalizeInside);
				}
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
	public void deletePunishinsideBill(int id) {
		this.punishinsideBillDAO.deletePunishinsideBill(id);
	}

	public String generateBillBatch(){
		String rule = "K";
		String nowTime = DateTimeUtil.getNowTime("yyyyMMddHHmmssSSS");
		String billBatch = rule + nowTime;
		return billBatch;
	}
	
	@Transactional
	public void createPunishinsideBill(ExpressOpsPunishinsideBill punishinsideBill){
		List<PenalizeInside> penalizeInsideList = this.punishinsideBillDAO
				.findByCondition(punishinsideBill.getPunishbigsort(),
						punishinsideBill.getPunishsmallsort(),
						punishinsideBill.getPunishNos(),
						punishinsideBill.getDutybranchid(),
						punishinsideBill.getDutypersonid(),
						punishinsideBill.getPunishNoCreateBeginDate(),
						punishinsideBill.getPunishNoCreateEndDate(), -9);
		PenalizeInside penalizeInside = null;
		String punishNos = "";
		BigDecimal sumPrice = new BigDecimal(0.0);
		if(penalizeInsideList != null && penalizeInsideList.size() > 0){
			for(int i = 0; i < penalizeInsideList.size(); i++){
				penalizeInside = penalizeInsideList.get(i);
				if(StringUtils.isNotBlank(penalizeInside.getPunishNo())){
					punishNos += penalizeInside.getPunishNo() + ",";
				}
				if(penalizeInside.getPunishInsideprice() != null){
					sumPrice.add(penalizeInside.getPunishInsideprice());
				}
			}
		}
		
		punishinsideBill.setPunishNos(punishNos);
		punishinsideBill.setSumPrice(sumPrice);
		this.punishinsideBillDAO.createPunishinsideBill(punishinsideBill);
	}
	
	public ExpressOpsPunishinsideBillVO setPersonName(ExpressOpsPunishinsideBillVO billVO) {
		if (billVO != null) {
			User user = null;
			if(billVO.getCreator() != 0){
				user = this.userDAO.getAllUserByid(billVO.getCreator());
				if(user != null){
					billVO.setCreatorName(user.getRealname());
				}
			}
			if(billVO.getShenHePerson() != 0){
				user = this.userDAO.getAllUserByid(billVO.getShenHePerson());
				if(user != null){
					billVO.setShenHePersonName(user.getRealname());
				}
			}
			if(billVO.getCheXiaoPerson() != 0){
				user = this.userDAO.getAllUserByid(billVO.getCheXiaoPerson());
				if(user != null){
					billVO.setCheXiaoPersonName(user.getRealname());
				}
			}
			if(billVO.getHeXiaoPerson() != 0){
				user = this.userDAO.getAllUserByid(billVO.getHeXiaoPerson());
				if(user != null){
					billVO.setHeXiaoPersonName(user.getRealname());
				}
			}
			if(billVO.getQuXiaoHeXiaoPerson() != 0){
				user = this.userDAO.getAllUserByid(billVO.getQuXiaoHeXiaoPerson());
				if(user != null){
					billVO.setQuXiaoHeXiaoPersonName(user.getRealname());
				}
			}
			if(billVO.getDutypersonid() != 0){
				user = this.userDAO.getAllUserByid(billVO.getDutypersonid());
				if(user != null){
					billVO.setDutypersonname(user.getRealname());
				}
			}
		}
		return billVO;
	}
}
