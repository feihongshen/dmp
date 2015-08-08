/**
 *
 */
package cn.explink.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import cn.explink.dao.AreaDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.ExceedSubsidyApplyDAO;
import cn.explink.dao.PFAreaDAO;
import cn.explink.dao.PFbasicDAO;
import cn.explink.dao.PFbusinessDAO;
import cn.explink.dao.PFcollectionDAO;
import cn.explink.dao.PFinsertionDAO;
import cn.explink.dao.PFoverareaDAO;
import cn.explink.dao.PFoverbigDAO;
import cn.explink.dao.PFoverweightDAO;
import cn.explink.dao.PaiFeiRuleDAO;
import cn.explink.domain.Area;
import cn.explink.domain.Basicjson;
import cn.explink.domain.Collectionjson;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.ExpressSetExceedSubsidyApply;
import cn.explink.domain.PFarea;
import cn.explink.domain.PFareaJson;
import cn.explink.domain.PFbasic;
import cn.explink.domain.PFbusiness;
import cn.explink.domain.PFcollection;
import cn.explink.domain.PFinsertion;
import cn.explink.domain.PFoverarea;
import cn.explink.domain.PFoverbig;
import cn.explink.domain.PFoverweight;
import cn.explink.domain.PaiFeiRule;
import cn.explink.domain.PaifeiRuleJson;
import cn.explink.domain.PaifeiRulePS;
import cn.explink.domain.PaifeiRuleTH;
import cn.explink.domain.PaifeiRuleTab;
import cn.explink.domain.PaifeiRuleZZ;
import cn.explink.enumutil.PaiFeiBuZhuTypeEnum;
import cn.explink.enumutil.PaiFeiRuleTabEnum;
import cn.explink.util.StringUtil;

@SuppressWarnings("unchecked")
/**
 * @author Administrator
 *
 */
@Service
public class PaiFeiRuleService {

	@Autowired
	PFbasicDAO pFbasicDAO;
	@Autowired
	PFcollectionDAO pFcollectionDAO;
	@Autowired
	PFinsertionDAO pFinsertionDAO;
	@Autowired
	PFbusinessDAO pFbusinessDAO;
	@Autowired
	PFoverareaDAO pFoverareaDAO;
	@Autowired
	PFAreaDAO pFareaDAO;
	@Autowired
	PFoverbigDAO pFoverbigDAO;
	@Autowired
	PFoverweightDAO pFoverweightDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	PaiFeiRuleDAO paiFeiRuleDAO;
	@Autowired
	AreaDAO areaDAO;
	@Autowired
	ExceedSubsidyApplyDAO exceedSubsidyApplyDAO;

	/**
	 * @param object
	 * @param pfruleid
	 * @param pfruletypeid
	 */
	@Transactional
	public Object save(JSONObject object, int pfruletypeid, long pfruleid) {
		List<Customer> customers = this.customerDAO.getAllCustomers();

		PaifeiRuleJson ruleJson = (PaifeiRuleJson) JSONObject.toBean(object, PaifeiRuleJson.class);
		if (ruleJson != null) {
			PaifeiRulePS ps = ruleJson.getPs();
			PaifeiRuleTH th = ruleJson.getTh();
			PaifeiRuleZZ zz = ruleJson.getZz();
			if (ps != null) {
				this.saveData(PaiFeiRuleTabEnum.Paisong, pfruletypeid, pfruleid, customers, ps);
			}
			if (zz != null) {
				this.saveData(PaiFeiRuleTabEnum.Zhongzhuan, pfruletypeid, pfruleid, customers, zz);
			}
			if (th != null) {
				this.saveData(PaiFeiRuleTabEnum.Tihuo, pfruletypeid, pfruleid, customers, th);
			}

		}
		return null;
	}

	/**
	 * @param pfruletypeid
	 * @param pfruleid
	 * @param customers
	 * @param tab
	 */
	private void saveData(PaiFeiRuleTabEnum pfenum, int pfruletypeid, long pfruleid, List<Customer> customers, PaifeiRuleTab tab) {
		// 基本补助
		Basicjson basic = tab.getBasic();
		if (null != basic) {
			if (basic.getShowflag().equals("yes")) {
				List<PFbasic> pFbasics = (List<PFbasic>) JSONArray.toCollection(JSONArray.fromObject(basic.getPFfees()), PFbasic.class);
				for (PFbasic pf : pFbasics) {
					pf.setTabid(pfenum.getValue());
					pf.setPfruleid(pfruleid);
					pf.setTypeid(pfruletypeid);
					pf.setShowflag(1);
					this.savedata(pf);
				}
			} else {
				PFbasic pf = new PFbasic();
				pf.setTabid(pfenum.getValue());
				pf.setPfruleid(pfruleid);
				pf.setTypeid(pfruletypeid);
				pf.setBasicPFfee(basic.getPFfee());
				pf.setShowflag(0);
				for (Customer customer : customers) {
					pf.setCustomerid(customer.getCustomerid());
					this.savedata(pf);
				}
			}
		}
		// 代收补助费
		Collectionjson collection = tab.getCollection();
		if (null != collection) {
			if (collection.getShowflag().equals("yes")) {
				List<PFcollection> pfcollection = (List<PFcollection>) JSONArray.toCollection(JSONArray.fromObject(collection.getPFfees()), PFcollection.class);
				for (PFcollection pf : pfcollection) {
					pf.setTabid(pfenum.getValue());
					pf.setPfruleid(pfruleid);
					pf.setTypeid(pfruletypeid);
					pf.setShowflag(1);
					// 验证是否已经存在相应的规则
					this.savedata(pf);

				}
			} else {
				PFcollection pf = new PFcollection();
				pf.setTabid(pfenum.getValue());
				pf.setPfruleid(pfruleid);
				pf.setTypeid(pfruletypeid);
				pf.setShowflag(0);
				pf.setCollectionPFfee(collection.getPFfee());
				for (Customer customer : customers) {
					pf.setCustomerid(customer.getCustomerid());
					this.savedata(pf);
				}
			}
		}
		// 拖件补助
		if (tab.getInsertion() != null) {
			List<PFinsertion> pFinsertions = (List<PFinsertion>) JSONArray.toCollection(JSONArray.fromObject(tab.getInsertion()), PFinsertion.class);
			for (PFinsertion pf : pFinsertions) {
				pf.setPfruleid(pfruleid);
				pf.setTabid(pfenum.getValue());
				pf.setTypeid(pfruletypeid);
				this.savedata(pf);
			}
		}
		// 业务补助
		if (tab.getSubsidyfee() != null) {
			BigDecimal subsidyfee = tab.getSubsidyfee();
			PFbusiness pf = new PFbusiness();
			pf.setSubsidyfee(subsidyfee);
			pf.setPfruleid(pfruleid);
			pf.setTabid(pfenum.getValue());
			pf.setTypeid(pfruletypeid);
			this.savedata(pf);
		}
		// 超区补助
		if ((pfruletypeid==3)&&(tab.getOverarea() == 1)) {
			PFoverarea pf = new PFoverarea();
			pf.setPfruleid(pfruleid);
			pf.setTabid(pfenum.getValue());
			pf.setTypeid(pfruletypeid);
			pf.setState(1);
			this.savedata(pf);
		}
		// 区域补助
		if (tab.getArea() != null) {
			List<PFareaJson> areajsons = (List<PFareaJson>) JSONArray.toCollection(JSONArray.fromObject(tab.getArea()), PFareaJson.class);
			if (areajsons != null) {
				for (PFareaJson areajson : areajsons) {
					PFarea pf = new PFarea();
					pf.setAreafee(areajson.getAreafee());
					pf.setAreaid(areajson.getAreaid());
					pf.setAreaname(areajson.getAreaname());
					pf.setTabid(pfenum.getValue());
					pf.setPfruleid(pfruleid);
					pf.setTypeid(pfruletypeid);
					pf.setOverbigflag(areajson.getOverbigflag());
					long id = this.savedata(pf);
					if (areajson.getOverbigflag() == 0) {

						List<PFoverbig> bigs = (List<PFoverbig>) JSONArray.toCollection(JSONArray.fromObject(areajson.getOverbig()), PFoverbig.class);
						if (bigs != null) {
							for (PFoverbig big : bigs) {
								if (big != null) {
									big.setAreaid(id);
									this.savedata(big);
								}
							}
						}

					}
					List<PFoverweight> weights = (List<PFoverweight>) JSONArray.toCollection(JSONArray.fromObject(areajson.getOverweight()), PFoverweight.class);
					if (weights != null) {
						for (PFoverweight weight : weights) {
							if(weight!=null){
							weight.setAreaid(id);
							this.savedata(weight);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * @param pf
	 */
	private long savedata(Object obj) {
		// 验证该记录是否已经存在了！
		if (obj instanceof PFbasic) {
			PFbasic pf = (PFbasic) obj;
			PFbasic pftemp = this.pFbasicDAO.getPFbasicByRTC(pf.getPfruleid(), pf.getTypeid(), pf.getTabid(), pf.getCustomerid());
			if (pftemp != null) {
				pf.setId(pftemp.getId());
				this.pFbasicDAO.updatePFbasic(pf);
				return pf.getId();
			} else {
				return this.pFbasicDAO.credataOfID(pf);
			}
		}
		if (obj instanceof PFcollection) {
			PFcollection pf = (PFcollection) obj;
			PFcollection pftemp = this.pFcollectionDAO.getPFcollectionByRTC(pf.getPfruleid(), pf.getTypeid(), pf.getTabid(), pf.getCustomerid());
			if (pftemp != null) {
				pf.setId(pftemp.getId());
				this.pFcollectionDAO.updatePFcollection(pf);
				return pf.getId();
			} else {
				return this.pFcollectionDAO.credataOfID(pf);
			}
		}
		if (obj instanceof PFbusiness) {
			PFbusiness pf = (PFbusiness) obj;
			PFbusiness pftemp = this.pFbusinessDAO.getPFbusinessByRTC(pf.getPfruleid(), pf.getTypeid(), pf.getTabid());
			if (pftemp != null) {
				pf.setId(pftemp.getId());
				this.pFbusinessDAO.updatePFbusiness(pf);
				return pf.getId();
			} else {
				return this.pFbusinessDAO.credataOfID(pf);
			}
		}
		if (obj instanceof PFinsertion) {
			PFinsertion pf = (PFinsertion) obj;
			PFinsertion pftemp = this.pFinsertionDAO.getPFinsertionByRTC(pf.getPfruleid(), pf.getTypeid(), pf.getTabid(), pf.getMincount(), pf.getMaxcount());
			if (pftemp != null) {
				pf.setId(pftemp.getId());
				this.pFinsertionDAO.updatePFinsertion(pf);
				return pf.getId();
			} else {
				return this.pFinsertionDAO.credataOfID(pf);
			}
		}

		if (obj instanceof PFoverarea) {
			PFoverarea pf = (PFoverarea) obj;
			PFoverarea pftemp = this.pFoverareaDAO.getPFoverareaByRTC(pf.getPfruleid(), pf.getTypeid(), pf.getTabid(), pf.getState());
			if (pftemp != null) {
				pf.setId(pftemp.getId());
				this.pFoverareaDAO.updatePFoverareaByPfruleidAndTabid(pf);
				return pf.getId();
			} else {
				return this.pFoverareaDAO.credataOfID(pf);
			}
		}

		if (obj instanceof PFarea) {
			PFarea pf = (PFarea) obj;
			PFarea pftemp = this.pFareaDAO.getPFareaByRTC(pf.getPfruleid(), pf.getTypeid(), pf.getTabid(), pf.getAreaid());
			if (pftemp != null) {
				pf.setId(pftemp.getId());
				this.pFareaDAO.updatePFarea(pf);
				return pf.getId();
			} else {
				return this.pFareaDAO.credataOfID(pf);
			}
		}
		if (obj instanceof PFoverbig) {
			PFoverbig pf = (PFoverbig) obj;
			PFoverbig pftemp = this.pFoverbigDAO.getPFoverbigByRTC(pf.getAreaid(), pf.getMincount(), pf.getMaxcount());
			if (pftemp != null) {
				pf.setId(pftemp.getId());
				this.pFoverbigDAO.updatePFoverbig(pf);
				return pf.getId();
			} else {
				return this.pFoverbigDAO.credataOfID(pf);
			}
		}
		if (obj instanceof PFoverweight) {
			PFoverweight pf = (PFoverweight) obj;
			PFoverweight pftemp = this.pFoverweightDAO.getPFoverweightByRTC(pf.getAreaid(), pf.getMincount(), pf.getMaxcount());
			if (pftemp != null) {
				pf.setId(pftemp.getId());
				this.pFoverweightDAO.updatePFoverweight(pf);
				return pf.getId();
			} else {
				return this.pFoverweightDAO.credataOfID(pf);
			}
		}
		return 0;

	}

	public static String createPaifeiNo(String flag) {
		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyyMMddHHmmssSSS");
		java.util.Date d = new java.util.Date();
		return flag + df.format(d);

	}

	/**
	 * 根据费用类型获取相应的派费
	 *
	 * @param pfruleid
	 *            派费规则
	 *
	 * @param tab
	 *            费用类型
	 * @param cwb
	 *            订单号
	 * @return 派费
	 */
	@Deprecated
	@Transactional
	public BigDecimal getPFRulefee(long pfruleid, PaiFeiRuleTabEnum tab, String cwb) {
		BigDecimal fee = new BigDecimal("0");
		CwbOrder co = this.cwbDAO.getCwbByCwb(cwb);
		PaiFeiRule pf = this.paiFeiRuleDAO.getPaiFeiRuleById(pfruleid);
		if ((co != null) && (pf != null)) {
			PFbasic baFbasic = this.pFbasicDAO.getPFbasicByRTC(pfruleid, tab.getValue(), co.getCustomerid());
			if (baFbasic != null) {// 获取基本补助
				fee=fee.add(baFbasic.getBasicPFfee());
			}
			PFcollection pFcollection = this.pFcollectionDAO.getPFcollectionByRTC(pfruleid, tab.getValue(), co.getCustomerid());
			if (pFcollection != null) {// 获取代收补助
				fee=fee.add(pFcollection.getCollectionPFfee());
			}
			// 获取区域补助
			// 需要获取订单中的区域
			Area area = null;
			String cwbcity = StringUtil.nullConvertToEmptyString(co.getCity());
			String cwbarea = StringUtil.nullConvertToEmptyString(co.getArea());
			if (!cwbarea.equals("") && !cwbcity.equals("")) {

				area = this.areaDAO.getAreaByCityAndArea(cwbcity, cwbarea);
			} else if (cwbarea.equals("") && !cwbcity.equals("")) {

				area = this.areaDAO.getAreaByCity(cwbcity);
			}
			long areaid = 0;
			if (area != null) {
				areaid = area.getId();
			}
			PFarea pFarea = this.pFareaDAO.getPFareaBypfruleidAndTabidAndAreaid(pfruleid, tab.getValue(), areaid);
			if (pFarea != null) {
				fee=fee.add(pFarea.getAreafee());
				if (pFarea.getOverbigflag() == 1) {
					// 需要调用申请表
					ExpressSetExceedSubsidyApply subsidy = this.exceedSubsidyApplyDAO.getExceedSubsidyApplyByCwb(co.getCwb());
					if (subsidy != null) {
						BigDecimal overbigfee = subsidy.getBigGoodsSubsidyAmount();
						if (overbigfee != null) {
							fee=fee.add(overbigfee);
						}
					}
				} else {
					// 货物体积
					long carsize = 0;
					try {
						carsize = Long.parseLong(co.getCarsize());
					} catch (Exception e) {
					}
					PFoverbig pFoverbig = this.pFoverbigDAO.getPFoverbigByAreaidAndCount(areaid, carsize);
					if (pFoverbig != null) {
						fee=fee.add(pFoverbig.getSubsidyfee());
					}
				}
				BigDecimal carrealweight = co.getCarrealweight();
				if (carrealweight == null) {
					carrealweight = new BigDecimal("0");
				}
				PFoverweight pFoverweight = this.pFoverweightDAO.getPFoverweightByAreaidAndCount(areaid, carrealweight);
				if (pFoverweight != null) {
					fee=fee.add(pFoverweight.getSubsidyfee());
				}
			}
			PFoverarea pFoverarea = this.pFoverareaDAO.getPFoverareaByPaifeiruleAndtabid(pfruleid, tab.getValue());
			if (pFoverarea != null) {
				// 需要掉用申请表中的方法
				ExpressSetExceedSubsidyApply subsidy = this.exceedSubsidyApplyDAO.getExceedSubsidyApplyByCwb(co.getCwb());
				if (subsidy != null) {
					BigDecimal overbigfee = subsidy.getExceedAreaSubsidyAmount();
					if (overbigfee != null) {
						fee=fee.add(overbigfee);
					}
				}
			}
			PFbusiness pFbusiness = this.pFbusinessDAO.getPFbusinessByRTC(pfruleid, tab.getValue());
			if (pFbusiness != null) {
				fee=fee.add(pFbusiness.getSubsidyfee());
			}
			long count = co.getSendcarnum();
			PFinsertion pFinsertion = this.pFinsertionDAO.getPFinsertionByPfruleidAndCount(pfruleid, tab.getValue(), count);
			if (pFinsertion != null) {
				fee=fee.add(pFinsertion.getInsertionfee());
			}
		}
		return fee;
	}

	@Transactional
	public Map<String, BigDecimal> getPFRulefeeOfBatch(long pfruleid, PaiFeiRuleTabEnum tab, List<CwbOrder> cwbOrders) {

		PaiFeiRule pf = this.paiFeiRuleDAO.getPaiFeiRuleById(pfruleid);
		Map<String, BigDecimal> feeMap = new HashMap<String, BigDecimal>();
		if ((cwbOrders != null) && (pf != null)) {
			List<PFbasic> pFbasics = this.pFbasicDAO.getPFbasicByPfruleidAndTabid(pfruleid, tab.getValue());
			List<PFcollection> pfcollections = this.pFcollectionDAO.getPFcollectionByPfruleidAndTabid(pfruleid, tab.getValue());
			List<Area> areas = this.areaDAO.getAllArea();
			List<PFarea> pFareas = this.pFareaDAO.getPFareaByPfruleidAndTabid(pfruleid, tab.getValue());
			List<ExpressSetExceedSubsidyApply> subsidys = this.exceedSubsidyApplyDAO.getExceedSubsidyApplyList();
			List<PFoverbig> pFoverbigs = this.pFoverbigDAO.getAllPFoverbig();
			List<PFoverweight> pFoverweights = this.pFoverweightDAO.getAllPfoverweight();
			PFoverarea pFoverarea = this.pFoverareaDAO.getPFoverareaByPaifeiruleAndtabid(pfruleid, tab.getValue());
			PFbusiness pFbusiness = this.pFbusinessDAO.getPFbusinessByPfruleidAndTabid(pfruleid, tab.getValue());
			List<PFinsertion> pFinsertions = this.pFinsertionDAO.getPFinsertionByPfruleidAndTabid(pfruleid, tab.getValue());
			for (CwbOrder co : cwbOrders) {
				BigDecimal fee = new BigDecimal("0");
				PFbasic baFbasic = this.getPFbasicByCustomerid(pFbasics, co.getCustomerid());
				if (baFbasic != null) {// 获取基本补助
					fee=fee.add(baFbasic.getBasicPFfee());
				}
				PFcollection pFcollection = this.getPFcollectionByCustomerid(pfcollections, co.getCustomerid());
				if (pFcollection != null) {// 获取代收补助
					fee=fee.add(pFcollection.getCollectionPFfee());
				}
				Area area = null;
				String cwbcity = StringUtil.nullConvertToEmptyString(co.getCity());
				String cwbarea = StringUtil.nullConvertToEmptyString(co.getArea());
				// 获取区域补助
				// 需要获取订单中的区域

				area = this.getAreaByCityAndArea(areas, cwbcity, cwbarea);
				long areaid = 0;
				if (area != null) {
					areaid = area.getId();
				}
				PFarea pFarea = null;
				if (areaid > 0) {
					pFarea = this.getPFareaByAreaid(pFareas, areaid);
				}
				if (pFarea != null) {
					fee=fee.add(pFarea.getAreafee());
					if (pFarea.getOverbigflag() == 1) {
						// 需要调用申请表
						ExpressSetExceedSubsidyApply subsidy = this.getExceedSubsidyApplyByCwb(subsidys, co.getCwb());
						if (subsidy != null) {
							BigDecimal overbigfee = subsidy.getBigGoodsSubsidyAmount();
							if (overbigfee != null) {
								fee=fee.add(overbigfee);
							}
						}
					} else {
						// 货物体积
						long carsize = 0;
						try {
							carsize = Long.parseLong(co.getCarsize());
						} catch (Exception e) {
						}
						PFoverbig pFoverbig = this.getPFoverbigByCount(pFoverbigs, carsize,pFarea.getId());
						if (pFoverbig != null) {
							fee=fee.add(pFoverbig.getSubsidyfee());
						}
					}
					BigDecimal carrealweight = co.getCarrealweight();
					if (carrealweight == null) {
						carrealweight = new BigDecimal("0");
					}
					PFoverweight pFoverweight = this.getPFoverweightByCount(pFoverweights, carrealweight,pFarea.getId());
					if (pFoverweight != null) {
						fee=fee.add(pFoverweight.getSubsidyfee());
					}
				}
				if (pFoverarea != null) {
					// 需要掉用申请表中的方法
					ExpressSetExceedSubsidyApply subsidy = this.getExceedSubsidyApplyByCwb(subsidys, co.getCwb());
					if (subsidy != null) {
						BigDecimal overbigfee = subsidy.getExceedAreaSubsidyAmount();
						if (overbigfee != null) {
							fee=fee.add(overbigfee);
						}
					}
				}
				if (pFbusiness != null) {
					fee=fee.add(pFbusiness.getSubsidyfee());
				}
				long count = co.getSendcarnum();
				PFinsertion pFinsertion = this.getPFinsertionByCount(pFinsertions, count);
				if (pFinsertion != null) {
					fee=fee.add(pFinsertion.getInsertionfee());
				}
				feeMap.put(co.getCwb(), fee);
			}

		}
		return feeMap;
	}

	/**
	 * @param pFinsertions
	 * @param count
	 * @return
	 */
	private PFinsertion getPFinsertionByCount(List<PFinsertion> pFinsertions, long count) {
		if (pFinsertions != null) {
			for (PFinsertion pf : pFinsertions) {
				if ((count >= pf.getMincount()) && (count < pf.getMaxcount())) {
					return pf;
				}
			}
		}
		return null;
	}

	/**
	 * @param pFoverweights
	 * @param carrealweight
	 * @return
	 */
	private PFoverweight getPFoverweightByCount(List<PFoverweight> pFoverweights, BigDecimal carrealweight,long areaid) {
		if (pFoverweights != null) {
			for (PFoverweight pf : pFoverweights) {
				if ((pf.getAreaid()==areaid)&&(carrealweight.compareTo(pf.getMincount()) >= 0) && (carrealweight.compareTo(pf.getMaxcount()) < 0)) {
					return pf;
				}
			}
		}
		return null;
	}

	/**
	 * @param pFoverbigs
	 * @param carsize
	 * @return
	 */
	private PFoverbig getPFoverbigByCount(List<PFoverbig> pFoverbigs, long carsize,long areaid) {
		if (pFoverbigs != null) {
			for (PFoverbig pf : pFoverbigs) {
				if ((pf.getAreaid()==areaid)&&(carsize >= pf.getMincount()) && (carsize < pf.getMaxcount())) {
					return pf;
				}
			}
		}
		return null;
	}

	/**
	 * @param cwb
	 * @return
	 */
	private ExpressSetExceedSubsidyApply getExceedSubsidyApplyByCwb(List<ExpressSetExceedSubsidyApply> subsidyApplys, String cwb) {
		if (subsidyApplys != null) {
			for (ExpressSetExceedSubsidyApply subsidyApply : subsidyApplys) {
				if (cwb.equals(subsidyApply.getCwbOrder())) {
					return subsidyApply;
				}
			}
		}
		return null;
	}

	/**
	 * @param pFareas
	 * @param areaid
	 * @return
	 */
	private PFarea getPFareaByAreaid(List<PFarea> pFareas, long areaid) {
		if (pFareas != null) {
			for (PFarea pf : pFareas) {
				if (pf.getAreaid() == areaid) {
					return pf;
				}
			}
		}
		return null;
	}

	/**
	 * @param areas
	 * @param cwbcity
	 * @param cwbarea
	 * @return
	 */
	private Area getAreaByCityAndArea(List<Area> areas, String cwbcity, String cwbarea) {
		if (areas != null) {
			for (Area area : areas) {
				if (!cwbarea.equals("") && !cwbcity.equals("")) {
					if (area.getArea().equals(cwbarea) && area.getCity().equals(cwbcity)) {
						return area;
					}
				} else if (cwbarea.equals("") && !cwbcity.equals("")) {

					if (area.getCity().equals(cwbcity)) {
						return area;
					}
				}
			}
		}
		return null;
	}

	private PFcollection getPFcollectionByCustomerid(List<PFcollection> pfcollections, long customerid) {
		if (pfcollections != null) {
			for (PFcollection pf : pfcollections) {
				if (pf.getCustomerid() == customerid) {
					return pf;
				}
			}
		}
		return null;
	}

	private PFbasic getPFbasicByCustomerid(List<PFbasic> pFbasics, long customerid) {
		if (pFbasics != null) {
			for (PFbasic pf : pFbasics) {
				if (pf.getCustomerid() == customerid) {
					return pf;
				}
			}
		}
		return null;
	}

	/**
	 *
	 * @param pfruleid
	 *            派费规则
	 * @param tab
	 *            费用类型
	 * @param type
	 *            补助类型
	 * @param cwb
	 *            订单号
	 * @return
	 */
	@Deprecated
	public BigDecimal getPFTypefeeByType(long pfruleid, PaiFeiRuleTabEnum tab, PaiFeiBuZhuTypeEnum type, String cwb) {

		BigDecimal fee = new BigDecimal("0");
		CwbOrder co = this.cwbDAO.getCwbByCwb(cwb);
		PaiFeiRule pf = this.paiFeiRuleDAO.getPaiFeiRuleById(pfruleid);
		if ((co != null) && (pf != null)) {
			if (type == PaiFeiBuZhuTypeEnum.Basic) {
				PFbasic baFbasic = this.pFbasicDAO.getPFbasicByRTC(pfruleid, tab.getValue(), co.getCustomerid());
				if (baFbasic != null) {// 获取基本补助
					return baFbasic.getBasicPFfee();
				}
			}
			if (type == PaiFeiBuZhuTypeEnum.Collection) {
				PFcollection pFcollection = this.pFcollectionDAO.getPFcollectionByRTC(pfruleid, tab.getValue(), co.getCustomerid());
				if (pFcollection != null) {// 获取代收补助
					return pFcollection.getCollectionPFfee();
				}
			}
			// 获取区域补助
			// 需要获取订单中的区域
			if (type == PaiFeiBuZhuTypeEnum.Area) {
				Area area = null;
				String cwbcity = StringUtil.nullConvertToEmptyString(co.getCity());
				String cwbarea = StringUtil.nullConvertToEmptyString(co.getArea());
				if (!cwbarea.equals("") && !cwbcity.equals("")) {

					area = this.areaDAO.getAreaByCityAndArea(cwbcity, cwbarea);
				} else if (cwbarea.equals("") && !cwbcity.equals("")) {

					area = this.areaDAO.getAreaByCity(cwbcity);
				}
				long areaid = 0;
				if (area != null) {
					areaid = area.getId();
				}
				BigDecimal areafee = new BigDecimal("0");
				PFarea pFarea = this.pFareaDAO.getPFareaBypfruleidAndTabidAndAreaid(pfruleid, tab.getValue(), areaid);
				if (pFarea != null) {
					areafee=areafee.add(pFarea.getAreafee());
					if (pFarea.getOverbigflag() == 1) {
						ExpressSetExceedSubsidyApply subsidy = this.exceedSubsidyApplyDAO.getExceedSubsidyApplyByCwb(co.getCwb());
						if (subsidy != null) {
							BigDecimal overbigfee = subsidy.getBigGoodsSubsidyAmount();
							if (overbigfee != null) {
								areafee=areafee.add(overbigfee);
							}
						}

					} else {
						// 货物体积
						long carsize = 0;
						try {
							carsize = Long.parseLong(co.getCarsize());
						} catch (Exception e) {
						}
						PFoverbig pFoverbig = this.pFoverbigDAO.getPFoverbigByAreaidAndCount(areaid, carsize);
						if (pFoverbig != null) {
							areafee=areafee.add(pFoverbig.getSubsidyfee());
						}
					}
					// 货物重量
					BigDecimal carrealweight = co.getCarrealweight();
					if (carrealweight == null) {
						carrealweight = new BigDecimal("0");
					}
					PFoverweight pFoverweight = this.pFoverweightDAO.getPFoverweightByAreaidAndCount(areaid, carrealweight);
					if (pFoverweight != null) {
						areafee=areafee.add(pFoverweight.getSubsidyfee());
					}
				}
				return areafee;
			}
			if (type == PaiFeiBuZhuTypeEnum.Overarea) {
				PFoverarea pFoverarea = this.pFoverareaDAO.getPFoverareaByPaifeiruleAndtabid(pfruleid, tab.getValue());
				if (pFoverarea != null) {
					// 需要掉用申请表中的方法
					ExpressSetExceedSubsidyApply subsidy = this.exceedSubsidyApplyDAO.getExceedSubsidyApplyByCwb(co.getCwb());
					if (subsidy != null) {
						BigDecimal overbigfee = subsidy.getExceedAreaSubsidyAmount();
						if (overbigfee != null) {
							return overbigfee;
						}
					}
				}
				return new BigDecimal("0");
			}
			if (type == PaiFeiBuZhuTypeEnum.Business) {
				PFbusiness pFbusiness = this.pFbusinessDAO.getPFbusinessByRTC(pfruleid, tab.getValue());
				if (pFbusiness != null) {
					return pFbusiness.getSubsidyfee();
				}
			}
			if (type == PaiFeiBuZhuTypeEnum.Insertion) {
				long count = co.getSendcarnum();
				PFinsertion pFinsertion = this.pFinsertionDAO.getPFinsertionByPfruleidAndCount(pfruleid, tab.getValue(), count);
				if (pFinsertion != null) {
					return pFinsertion.getInsertionfee();
				}
			}

		}
		return fee;

	}
	@Transactional
	public Map<String, BigDecimal> getPFTypefeeByTypeOfBatch(long pfruleid, PaiFeiRuleTabEnum tab, PaiFeiBuZhuTypeEnum type, List<CwbOrder> cwbOrders) {
		PaiFeiRule pf = this.paiFeiRuleDAO.getPaiFeiRuleById(pfruleid);
		if ((cwbOrders != null) && (pf != null)) {
			List<PFbasic> pFbasics = this.pFbasicDAO.getPFbasicByPfruleidAndTabid(pfruleid, tab.getValue());
			List<PFcollection> pfcollections = this.pFcollectionDAO.getPFcollectionByPfruleidAndTabid(pfruleid, tab.getValue());
			List<Area> areas = this.areaDAO.getAllArea();
			List<PFarea> pFareas = this.pFareaDAO.getPFareaByPfruleidAndTabid(pfruleid, tab.getValue());
			List<ExpressSetExceedSubsidyApply> subsidys = this.exceedSubsidyApplyDAO.getExceedSubsidyApplyList();
			List<PFoverbig> pFoverbigs = this.pFoverbigDAO.getAllPFoverbig();
			List<PFoverweight> pFoverweights = this.pFoverweightDAO.getAllPfoverweight();
			PFoverarea pFoverarea = this.pFoverareaDAO.getPFoverareaByPaifeiruleAndtabid(pfruleid, tab.getValue());
			PFbusiness pFbusiness = this.pFbusinessDAO.getPFbusinessByPfruleidAndTabid(pfruleid, tab.getValue());
			List<PFinsertion> pFinsertions = this.pFinsertionDAO.getPFinsertionByPfruleidAndTabid(pfruleid, tab.getValue());
			Map<String, BigDecimal> feeMap = new HashMap<String, BigDecimal>();
			if (type == PaiFeiBuZhuTypeEnum.Basic) {
				for (CwbOrder co : cwbOrders) {
					PFbasic baFbasic = this.getPFbasicByCustomerid(pFbasics, co.getCustomerid());
					if (baFbasic != null) {// 获取基本补助
						feeMap.put(co.getCwb(), baFbasic.getBasicPFfee());
					}
					else {
						feeMap.put(co.getCwb(), BigDecimal.ZERO);
					}
				}
				return feeMap;
			}
			if (type == PaiFeiBuZhuTypeEnum.Collection) {
				for (CwbOrder co : cwbOrders) {
					PFcollection pFcollection = this.getPFcollectionByCustomerid(pfcollections, co.getCustomerid());
					if (pFcollection != null) {// 获取代收补助
						feeMap.put(co.getCwb(), pFcollection.getCollectionPFfee());
					}
					else {
						feeMap.put(co.getCwb(), BigDecimal.ZERO);
					}
				}
				return feeMap;
			}

			// 获取区域补助
			// 需要获取订单中的区域
			if (type == PaiFeiBuZhuTypeEnum.Area) {
				for (CwbOrder co : cwbOrders) {
					Area area = null;
					String cwbcity = StringUtil.nullConvertToEmptyString(co.getCity());
					String cwbarea = StringUtil.nullConvertToEmptyString(co.getArea());

					area = this.getAreaByCityAndArea(areas, cwbcity, cwbarea);
					long areaid = 0;
					if (area != null) {
						areaid = area.getId();
					}
					BigDecimal areafee = new BigDecimal("0");
					PFarea pFarea = this.getPFareaByAreaid(pFareas, areaid);
					if (pFarea != null) {
						areafee=areafee.add(pFarea.getAreafee());
						if (pFarea.getOverbigflag() == 1) {
							ExpressSetExceedSubsidyApply subsidy = this.getExceedSubsidyApplyByCwb(subsidys, co.getCwb());
							if (subsidy != null) {
								BigDecimal overbigfee = subsidy.getBigGoodsSubsidyAmount();
								if (overbigfee != null) {
									areafee=areafee.add(overbigfee);
								}
							}

						} else {
							// 货物体积
							long carsize = 0;
							try {
								carsize = Long.parseLong(co.getCarsize());
							} catch (Exception e) {
							}
							PFoverbig pFoverbig = this.getPFoverbigByCount(pFoverbigs, carsize,pFarea.getId());
							if (pFoverbig != null) {
								areafee=areafee.add(pFoverbig.getSubsidyfee());
							}
						}
						// 货物重量
						BigDecimal carrealweight = co.getCarrealweight();
						if (carrealweight == null) {
							carrealweight = new BigDecimal("0");
						}
						PFoverweight pFoverweight = this.getPFoverweightByCount(pFoverweights, carrealweight,pFarea.getId());
						if (pFoverweight != null) {
							areafee=areafee.add(pFoverweight.getSubsidyfee());
						}
					}
					feeMap.put(co.getCwb(), areafee);
				}
				return feeMap;
			}
			if (type == PaiFeiBuZhuTypeEnum.Overarea) {
				for (CwbOrder co : cwbOrders) {
					if (pFoverarea != null) {
						// 需要掉用申请表中的方法
						ExpressSetExceedSubsidyApply subsidy = this.getExceedSubsidyApplyByCwb(subsidys, co.getCwb());
						if (subsidy != null) {
							BigDecimal overbigfee = subsidy.getExceedAreaSubsidyAmount();
							if (overbigfee != null) {
								feeMap.put(co.getCwb(), overbigfee);
							}
							else {
								feeMap.put(co.getCwb(), BigDecimal.ZERO);
							}
						}
					}
				}
				return feeMap;
			}
			if (type == PaiFeiBuZhuTypeEnum.Business) {
				for (CwbOrder co : cwbOrders) {
					if (pFbusiness != null) {
						feeMap.put(co.getCwb(), pFbusiness.getSubsidyfee());
					}
					else {
						feeMap.put(co.getCwb(), BigDecimal.ZERO);
					}
				}
				return feeMap;
			}
			if (type == PaiFeiBuZhuTypeEnum.Insertion) {
				for (CwbOrder co : cwbOrders) {
					long count = co.getSendcarnum();
					PFinsertion pFinsertion = this.getPFinsertionByCount(pFinsertions, count);
					if (pFinsertion != null) {
						feeMap.put(co.getCwb(), pFinsertion.getInsertionfee());
					}
					else {
						feeMap.put(co.getCwb(), BigDecimal.ZERO);
					}

				}
				return feeMap;
			}
		}
		return null;

	}

	// 货物超大补助
	@Deprecated
	public BigDecimal getOverbigFee(long pfruleid, PaiFeiRuleTabEnum tab, String cwb) {
		CwbOrder co = this.cwbDAO.getCwbByCwb(cwb);
		long areaid = 0;
		List<Area> areas = this.areaDAO.getAllArea();
		BigDecimal areafee = new BigDecimal("0");
		if (co != null) {
			Area area = null;
			String cwbcity = StringUtil.nullConvertToEmptyString(co.getCity());
			String cwbarea = StringUtil.nullConvertToEmptyString(co.getArea());

			area = this.getAreaByCityAndArea(areas, cwbcity, cwbarea);
			if (area != null) {
				areaid = area.getId();
			}
			PFarea pFarea = this.pFareaDAO.getPFareaBypfruleidAndTabidAndAreaid(pfruleid, tab.getValue(), areaid);
			if (pFarea != null) {
				if (pFarea.getOverbigflag() == 1) {
					// 需要调用申请表
					ExpressSetExceedSubsidyApply subsidy = this.exceedSubsidyApplyDAO.getExceedSubsidyApplyByCwb(co.getCwb());
					if (subsidy != null) {
						BigDecimal overbigfee = subsidy.getBigGoodsSubsidyAmount();
						if (overbigfee != null) {
							areafee=areafee.add(overbigfee);
						}
					}
				} else {
					// 货物体积
					long carsize = 0;
					try {
						carsize = Long.parseLong(co.getCarsize());
					} catch (Exception e) {
					}
					PFoverbig pFoverbig = this.pFoverbigDAO.getPFoverbigByAreaidAndCount(areaid, carsize);
					if (pFoverbig != null) {
						areafee=areafee.add(pFoverbig.getSubsidyfee());
					}
				}
			}
		}
		return areafee;
	}

	// 货物超重补助
	@Deprecated
	public BigDecimal getOverweightFee(long pfruleid, PaiFeiRuleTabEnum tab, String cwb) {
		CwbOrder co = this.cwbDAO.getCwbByCwb(cwb);
		long areaid = 0;
		BigDecimal areafee = new BigDecimal("0");
		List<Area> areas = this.areaDAO.getAllArea();
		if (co != null) {
			Area area = null;
			String cwbcity = StringUtil.nullConvertToEmptyString(co.getCity());
			String cwbarea = StringUtil.nullConvertToEmptyString(co.getArea());

			area = this.getAreaByCityAndArea(areas, cwbcity, cwbarea);
			if (area != null) {
				areaid = area.getId();
			}
			PFarea pFarea = this.pFareaDAO.getPFareaBypfruleidAndTabidAndAreaid(pfruleid, tab.getValue(), areaid);
			if (pFarea != null) {
				BigDecimal carrealweight = co.getCarrealweight();
				if (carrealweight == null) {
					carrealweight = new BigDecimal("0");
				}
				PFoverweight pFoverweight = this.pFoverweightDAO.getPFoverweightByAreaidAndCount(areaid, carrealweight);
				if (pFoverweight != null) {
					areafee=areafee.add(pFoverweight.getSubsidyfee());
				}
			}
		}
		return areafee;
	}

	// 货物超大补助
	@Transactional
	public Map<String, BigDecimal> getOverbigFeeOfBatch(long pfruleid, PaiFeiRuleTabEnum tab, List<CwbOrder> cwbOrders) {
		Map<String, BigDecimal> feeMap = new HashMap<String, BigDecimal>();
		if (cwbOrders != null) {
			List<PFarea> pFareas = this.pFareaDAO.getPFareaByPfruleidAndTabid(pfruleid, tab.getValue());
			List<ExpressSetExceedSubsidyApply> subsidys = this.exceedSubsidyApplyDAO.getExceedSubsidyApplyList();
			List<PFoverbig> pFoverbigs = this.pFoverbigDAO.getAllPFoverbig();
			List<Area> areas = this.areaDAO.getAllArea();
			for (CwbOrder co : cwbOrders) {
				BigDecimal areafee = new BigDecimal("0");

				Area area = null;
				String cwbcity = StringUtil.nullConvertToEmptyString(co.getCity());
				String cwbarea = StringUtil.nullConvertToEmptyString(co.getArea());

				area = this.getAreaByCityAndArea(areas, cwbcity, cwbarea);
				long areaid = 0;
				if (area != null) {
					areaid = area.getId();
				}
				if (co != null) {
					PFarea pFarea = this.getPFareaByAreaid(pFareas, areaid);
					if (pFarea != null) {
						if (pFarea.getOverbigflag() == 1) {
							// 需要调用申请表
							ExpressSetExceedSubsidyApply subsidy = this.getExceedSubsidyApplyByCwb(subsidys, co.getCwb());
							if (subsidy != null) {
								BigDecimal overbigfee = subsidy.getBigGoodsSubsidyAmount();
								if (overbigfee != null) {
									areafee=areafee.add(overbigfee);
								}
							}
						} else {
							// 货物体积
							long carsize = 0;
							try {
								carsize = Long.parseLong(co.getCarsize());
							} catch (Exception e) {
							}
							PFoverbig pFoverbig = this.getPFoverbigByCount(pFoverbigs, carsize,pFarea.getId());
							if (pFoverbig != null) {
								areafee=areafee.add(pFoverbig.getSubsidyfee());
							}
						}
					}
					feeMap.put(co.getCwb(), areafee);
				}
			}

		}
		return feeMap;
	}

	// 货物超重补助
	@Transactional
	public Map<String, BigDecimal> getOverweightFeeOfBacth(long pfruleid, PaiFeiRuleTabEnum tab, List<CwbOrder> cwbOrders) {
		Map<String, BigDecimal> feeMap = new HashMap<String, BigDecimal>();
		if (cwbOrders != null) {
			List<Area> areas = this.areaDAO.getAllArea();
			List<PFarea> pFareas = this.pFareaDAO.getPFareaByPfruleidAndTabid(pfruleid, tab.getValue());
			List<PFoverweight> pFoverweights = this.pFoverweightDAO.getAllPfoverweight();
			for (CwbOrder co : cwbOrders) {
				long areaid = 0;
				Area area = null;
				String cwbcity = StringUtil.nullConvertToEmptyString(co.getCity());
				String cwbarea = StringUtil.nullConvertToEmptyString(co.getArea());

				area = this.getAreaByCityAndArea(areas, cwbcity, cwbarea);
				if (area != null) {
					areaid = area.getId();
				}
				BigDecimal areafee = new BigDecimal("0");
				if (co != null) {
					PFarea pFarea = this.getPFareaByAreaid(pFareas, areaid);
					if (pFarea != null) {
						BigDecimal carrealweight = co.getCarrealweight();
						if (carrealweight == null) {
							carrealweight = new BigDecimal("0");
						}
						PFoverweight pFoverweight = this.getPFoverweightByCount(pFoverweights, carrealweight,pFarea.getId());
						if (pFoverweight != null) {
							areafee=areafee.add(pFoverweight.getSubsidyfee());
						}
					}
					feeMap.put(co.getCwb(), areafee);
				}
			}
		}
		return feeMap;
	}

	/**
	 * @param model
	 * @param pfruleid
	 */
	public void getEditData(Model model, long pfruleid) {
		PaiFeiRule rule = this.paiFeiRuleDAO.getPaiFeiRuleById(pfruleid);

		// 查询基本补助
		List<PFbasic> basicListPS = this.pFbasicDAO.getPFbasicByPfruleidAndTabid(pfruleid, PaiFeiRuleTabEnum.Paisong.getValue());
		String ps_showflag_basic = "no";
		if (basicListPS != null) {
			ps_showflag_basic = this.getShowflagBasic(basicListPS, ps_showflag_basic);
			if (basicListPS.size() > 0) {
				model.addAttribute("basicPS", basicListPS.get(0));
			}
		}
		this.IsClearList(basicListPS, ps_showflag_basic);
		model.addAttribute("ps_showflag_basic", ps_showflag_basic);
		model.addAttribute("basicListPS", basicListPS);

		List<PFbasic> basicListTH = this.pFbasicDAO.getPFbasicByPfruleidAndTabid(pfruleid, PaiFeiRuleTabEnum.Tihuo.getValue());
		String th_showflag_basic = "no";
		if (basicListTH != null) {
			th_showflag_basic = this.getShowflagBasic(basicListTH, th_showflag_basic);
			if (basicListTH.size() > 0) {
				model.addAttribute("basicTH", basicListTH.get(0));
			}
		}
		this.IsClearList(basicListTH, th_showflag_basic);
		model.addAttribute("th_showflag_basic", th_showflag_basic);
		model.addAttribute("basicListTH", basicListTH);

		List<PFbasic> basicListZZ = this.pFbasicDAO.getPFbasicByPfruleidAndTabid(pfruleid, PaiFeiRuleTabEnum.Zhongzhuan.getValue());
		String zz_showflag_basic = "no";
		if (basicListZZ != null) {
			zz_showflag_basic = this.getShowflagBasic(basicListZZ, zz_showflag_basic);
			if (basicListZZ.size() > 0) {
				model.addAttribute("basicZZ", basicListZZ.get(0));
			}
		}
		this.IsClearList(basicListZZ, zz_showflag_basic);
		model.addAttribute("zz_showflag_basic", zz_showflag_basic);
		model.addAttribute("basicListZZ", basicListZZ);

		// 查询代收补助
		List<PFcollection> collectionListPS = this.pFcollectionDAO.getPFcollectionByPfruleidAndTabid(pfruleid, PaiFeiRuleTabEnum.Paisong.getValue());
		String ps_showflag_collection = "no";
		if (collectionListPS != null) {
			ps_showflag_collection = this.getShowflagCollection(collectionListPS, ps_showflag_collection);
			if (collectionListPS.size() > 0) {
				model.addAttribute("collectionPS", collectionListPS.get(0));
			}
		}
		this.IsClearList(collectionListPS, ps_showflag_collection);
		model.addAttribute("ps_showflag_collection", ps_showflag_collection);
		model.addAttribute("collectionListPS", collectionListPS);

		List<PFcollection> collectionListTH = this.pFcollectionDAO.getPFcollectionByPfruleidAndTabid(pfruleid, PaiFeiRuleTabEnum.Tihuo.getValue());
		String th_showflag_collection = "no";
		if (collectionListTH != null) {
			th_showflag_collection = this.getShowflagCollection(collectionListTH, th_showflag_collection);
			if (collectionListTH.size() > 0) {
				model.addAttribute("collectionTH", collectionListTH.get(0));
			}
		}
		this.IsClearList(collectionListTH, th_showflag_collection);
		model.addAttribute("th_showflag_collection", th_showflag_collection);
		model.addAttribute("collectionListTH", collectionListTH);

		List<PFcollection> collectionListZZ = this.pFcollectionDAO.getPFcollectionByPfruleidAndTabid(pfruleid, PaiFeiRuleTabEnum.Zhongzhuan.getValue());
		String zz_showflag_collection = "no";
		if (collectionListZZ != null) {
			zz_showflag_collection = this.getShowflagCollection(collectionListZZ, zz_showflag_collection);
			if (collectionListZZ.size() > 0) {
				model.addAttribute("collectionZZ", collectionListZZ.get(0));
			}
		}
		this.IsClearList(collectionListZZ, zz_showflag_collection);
		model.addAttribute("zz_showflag_collection", zz_showflag_collection);
		model.addAttribute("collectionListZZ", collectionListZZ);

		List<PFarea> pfareaListPS = this.pFareaDAO.getPFareaByPfruleidAndTabid(pfruleid, PaiFeiRuleTabEnum.Paisong.getValue());
		Map<Long, List<PFoverbig>> overbigMapPS = new HashMap<Long, List<PFoverbig>>();
		for (PFarea area : pfareaListPS) {
			List<PFoverbig> pFoverbigList = this.pFoverbigDAO.getPFoverbigByAreaidAndTabid(area.getId());
			overbigMapPS.put(area.getId(), pFoverbigList);
		}
		Map<Long, List<PFoverweight>> overweightMapPS = new HashMap<Long, List<PFoverweight>>();
		for (PFarea area : pfareaListPS) {
			List<PFoverweight> overweightList = this.pFoverweightDAO.getPFoverweightByAreaidAndTabid(area.getId());
			overweightMapPS.put(area.getId(), overweightList);
		}
		model.addAttribute("pfareaListPS", pfareaListPS);
		model.addAttribute("overbigMapPS", overbigMapPS);
		model.addAttribute("overweightMapPS", overweightMapPS);
		List<PFarea> pfareaListTH = this.pFareaDAO.getPFareaByPfruleidAndTabid(pfruleid, PaiFeiRuleTabEnum.Tihuo.getValue());
		Map<Long, List<PFoverbig>> overbigMapTH = new HashMap<Long, List<PFoverbig>>();
		for (PFarea area : pfareaListTH) {
			List<PFoverbig> pFoverbigList = this.pFoverbigDAO.getPFoverbigByAreaidAndTabid(area.getId());
			overbigMapTH.put(area.getId(), pFoverbigList);
		}
		Map<Long, List<PFoverweight>> overweightMapTH = new HashMap<Long, List<PFoverweight>>();
		for (PFarea area : pfareaListTH) {
			List<PFoverweight> overweightList = this.pFoverweightDAO.getPFoverweightByAreaidAndTabid(area.getId());
			overweightMapTH.put(area.getId(), overweightList);
		}
		model.addAttribute("pfareaListTH", pfareaListTH);
		model.addAttribute("overbigMapTH", overbigMapTH);
		model.addAttribute("overweightMapTH", overweightMapTH);
		List<PFarea> pfareaListZZ = this.pFareaDAO.getPFareaByPfruleidAndTabid(pfruleid, PaiFeiRuleTabEnum.Zhongzhuan.getValue());
		Map<Long, List<PFoverbig>> overbigMapZZ = new HashMap<Long, List<PFoverbig>>();
		for (PFarea area : pfareaListZZ) {
			List<PFoverbig> pFoverbigList = this.pFoverbigDAO.getPFoverbigByAreaidAndTabid(area.getId());
			overbigMapZZ.put(area.getId(), pFoverbigList);
		}
		Map<Long, List<PFoverweight>> overweightMapZZ = new HashMap<Long, List<PFoverweight>>();
		for (PFarea area : pfareaListZZ) {
			List<PFoverweight> overweightList = this.pFoverweightDAO.getPFoverweightByAreaidAndTabid(area.getId());
			overweightMapZZ.put(area.getId(), overweightList);
		}
		model.addAttribute("overbigMapZZ", overbigMapZZ);
		model.addAttribute("overweightMapZZ", overweightMapZZ);
		model.addAttribute("pfareaListZZ", pfareaListZZ);

		PFbusiness buFbusinessPS = this.pFbusinessDAO.getPFbusinessByPfruleidAndTabid(pfruleid, PaiFeiRuleTabEnum.Paisong.getValue());
		model.addAttribute("buFbusinessPS", buFbusinessPS);
		PFbusiness buFbusinessTH = this.pFbusinessDAO.getPFbusinessByPfruleidAndTabid(pfruleid, PaiFeiRuleTabEnum.Tihuo.getValue());
		model.addAttribute("buFbusinessTH", buFbusinessTH);
		PFbusiness buFbusinessZZ = this.pFbusinessDAO.getPFbusinessByPfruleidAndTabid(pfruleid, PaiFeiRuleTabEnum.Zhongzhuan.getValue());
		model.addAttribute("buFbusinessZZ", buFbusinessZZ);

		PFoverarea overareaPS = this.pFoverareaDAO.getPFoverareaByPaifeiruleAndtabid(pfruleid, PaiFeiRuleTabEnum.Paisong.getValue());
		model.addAttribute("overareaPS", overareaPS);
		PFoverarea overareaTH = this.pFoverareaDAO.getPFoverareaByPaifeiruleAndtabid(pfruleid, PaiFeiRuleTabEnum.Tihuo.getValue());
		model.addAttribute("overareaTH", overareaTH);
		PFoverarea overareaZZ = this.pFoverareaDAO.getPFoverareaByPaifeiruleAndtabid(pfruleid, PaiFeiRuleTabEnum.Zhongzhuan.getValue());
		model.addAttribute("overareaZZ", overareaZZ);

		List<PFinsertion> insertionListPS = this.pFinsertionDAO.getPFinsertionByPfruleidAndTabid(pfruleid, PaiFeiRuleTabEnum.Paisong.getValue());
		model.addAttribute("insertionListPS", insertionListPS);
		List<PFinsertion> insertionListTH = this.pFinsertionDAO.getPFinsertionByPfruleidAndTabid(pfruleid, PaiFeiRuleTabEnum.Tihuo.getValue());
		model.addAttribute("insertionListTH", insertionListTH);
		List<PFinsertion> insertionListZZ = this.pFinsertionDAO.getPFinsertionByPfruleidAndTabid(pfruleid, PaiFeiRuleTabEnum.Zhongzhuan.getValue());
		model.addAttribute("insertionListZZ", insertionListZZ);

		model.addAttribute("rule", rule);
		model.addAttribute("edit", 1);
	}

	/**
	 * @param collectionListPS
	 * @param ps_showflag_collection
	 */
	private void IsClearList(List<?> list, String flag) {
		if (flag.equals("no")) {
			list.clear();
		}
	}

	/**
	 * @param basicListPS
	 * @param showflag_basic
	 * @return
	 */
	private String getShowflagBasic(List<PFbasic> basicList, String showflag_basic) {
		for (PFbasic pf : basicList) {
			if (pf.getShowflag() == 1) {
				showflag_basic = "yes";
				break;
			}
		}
		return showflag_basic;
	}

	private String getShowflagCollection(List<PFcollection> PFcollection, String showflag_collection) {
		for (PFcollection pf : PFcollection) {
			if (pf.getShowflag() == 1) {
				showflag_collection = "yes";
				break;
			}
		}
		return showflag_collection;
	}

	/**
	 * @param areaid
	 * @param model
	 * @param request
	 */
	@Transactional
	public int editType(String json, String rulejson, String type, long areaid, Model model) {
		List<Customer> customers = this.customerDAO.getAllCustomers();
		JSONObject object = JSONObject.fromObject(rulejson);
		int count = 0;
		PaiFeiRule rule = (PaiFeiRule) JSONObject.toBean(object, PaiFeiRule.class);
		if (type.equals("_rule")) {
			count = this.paiFeiRuleDAO.updatePaiFeiRule(rule);
			model.addAttribute("rule", rule);
			return count;
		}
		if (type.contains("_")) {
			String flags[] = type.split("_");
			String tabs = flags[0];
			String bztype = flags[1];
			int tabid = 0;
			if (tabs.equals("ps")) {
				tabid = PaiFeiRuleTabEnum.Paisong.getValue();
			} else if (tabs.equals("th")) {
				tabid = PaiFeiRuleTabEnum.Tihuo.getValue();
			} else if (tabs.equals("zz")) {
				tabid = PaiFeiRuleTabEnum.Zhongzhuan.getValue();
			}
			if (bztype.contains("basic")) {// 修改基本补助
				if (bztype.equals("basicno")) {
					PFbasic pf = (PFbasic) JSONObject.toBean(JSONObject.fromObject(json), PFbasic.class);
					pf.setTabid(tabid);
					pf.setPfruleid(rule.getId());
					pf.setTypeid(rule.getType());
					pf.setShowflag(0);
					this.pFbasicDAO.deletePFbasicByPfRuleidAndTabid(rule.getId(), tabid);

					for (Customer customer : customers) {
						pf.setCustomerid(customer.getCustomerid());
						long id = this.savedata(pf);
						if (id > 0) {
							count++;
						}
					}

					return count;
				} else {
					List<PFbasic> pfbasicList = (List<PFbasic>) JSONArray.toCollection(JSONArray.fromObject(json), PFbasic.class);
					if (pfbasicList != null) {

						int i = this.pFbasicDAO.deletePFbasicByPfRuleidAndTabid(rule.getId(), tabid);
						if ((i > 0) && (pfbasicList.size() == 0)) {
							count = i;
						}
						for (PFbasic pf : pfbasicList) {
							pf.setPfruleid(rule.getId());
							pf.setTypeid(rule.getType());
							pf.setTabid(tabid);
							long id = this.savedata(pf);
							if (id > 0) {
								count++;
							}
						}
						return count;
					}
				}
			} else if (bztype.contains("collection")) {// 修改代收补助
				if (bztype.equals("collectionno")) {
					PFcollection pf = (PFcollection) JSONObject.toBean(JSONObject.fromObject(json), PFcollection.class);
					pf.setTabid(tabid);
					pf.setPfruleid(rule.getId());
					pf.setTypeid(rule.getType());
					pf.setShowflag(0);
					for (Customer customer : customers) {
						pf.setCustomerid(customer.getCustomerid());
						long id = this.savedata(pf);
						if (id > 0) {
							count++;
						}
					}
					return count;
				} else {
					List<PFcollection> pfcollectionList = (List<PFcollection>) JSONArray.toCollection(JSONArray.fromObject(json), PFcollection.class);
					if (pfcollectionList != null) {

						int i = this.pFcollectionDAO.deletePFcollectionByPfRuleidAndTabid(rule.getId(), tabid);
						if ((i > 0) && (pfcollectionList.size() == 0)) {
							count = i;
						}
						for (PFcollection pf : pfcollectionList) {
							pf.setPfruleid(rule.getId());
							pf.setTypeid(rule.getType());
							pf.setTabid(tabid);
							long id = this.savedata(pf);
							if (id > 0) {
								count++;
							}
						}
						return count;
					}
				}
			} else if (bztype.equals("overarea")) {// 超区补助
				PFoverarea pf = (PFoverarea) JSONObject.toBean(JSONObject.fromObject(json), PFoverarea.class);
				this.pFoverareaDAO.deletePFoverareaByPfRuleidAndTabid(rule.getId(), tabid);
				pf.setPfruleid(rule.getId());
				pf.setTypeid(rule.getType());
				pf.setTabid(tabid);
				long id = this.savedata(pf);
				if (id > 0) {
					count++;
				}
				return count;
			} else if (bztype.equals("business")) {// 业务补助
				PFbusiness pf = (PFbusiness) JSONObject.toBean(JSONObject.fromObject(json), PFbusiness.class);
				pf.setPfruleid(rule.getId());
				pf.setTypeid(rule.getType());
				pf.setTabid(tabid);
				long id = this.savedata(pf);
				if (id > 0) {
					count++;
				}
				return count;
			} else if (bztype.equals("insertion")) {// 托单补助
				List<PFinsertion> pfinsertionList = (List<PFinsertion>) JSONArray.toCollection(JSONArray.fromObject(json), PFinsertion.class);
				if (pfinsertionList != null) {

					int i = this.pFinsertionDAO.deletePFinsertionByPfRuleidAndTabid(rule.getId(), tabid);
					if ((i > 0) && (pfinsertionList.size() == 0)) {
						count = i;
					}
					for (PFinsertion pf : pfinsertionList) {
						pf.setPfruleid(rule.getId());
						pf.setTypeid(rule.getType());
						pf.setTabid(tabid);
						long id = this.savedata(pf);
						if (id > 0) {
							count++;
						}
					}
					return count;
				}
			} else if (bztype.equals("overbig")) {
				List<PFoverbig> pfoverbigList = (List<PFoverbig>) JSONArray.toCollection(JSONArray.fromObject(json), PFoverbig.class);
				if (pfoverbigList.size() == 0) {
					count = this.pFoverbigDAO.deletePFoverbigByAreaid(areaid);
				}
				if ((pfoverbigList != null) && (pfoverbigList.size() > 0)) {
					this.pFoverbigDAO.deletePFoverbigByAreaid(pfoverbigList.get(0).getAreaid());
					for (PFoverbig pf : pfoverbigList) {
						long id = this.pFoverbigDAO.credataOfID(pf);
						if (id > 0) {
							count++;
						}
					}
				}
				return count;
			} else if (bztype.equals("overweight")) {
				List<PFoverweight> pfoverweightList = (List<PFoverweight>) JSONArray.toCollection(JSONArray.fromObject(json), PFoverweight.class);
				if (pfoverweightList.size() == 0) {
					count = this.pFoverweightDAO.deletePFoverweightByAreaid(areaid);
				}
				if ((pfoverweightList != null) && (pfoverweightList.size() > 0)) {
					this.pFoverweightDAO.deletePFoverweightByAreaid(pfoverweightList.get(0).getAreaid());
					for (PFoverweight pf : pfoverweightList) {
						long id = this.pFoverweightDAO.credataOfID(pf);
						if (id > 0) {
							count++;
						}
					}
				}
				return count;
			}

		}
		return count;
	}

	@Transactional
	public int deletePaifeiRule(long pfruleid) {
		int i = 0;
		i = this.paiFeiRuleDAO.deletePaiFeiRuleByPfruleid(pfruleid);
		if (i > 0) {
			this.pFbasicDAO.deletePFbasicByPfruleid(pfruleid);
			this.pFcollectionDAO.deletePFbasicByPfruleid(pfruleid);
			this.pFbusinessDAO.deleteBusinessByPfruleid(pfruleid);
			List<PFarea> pFareas = this.pFareaDAO.getPFareaByPfruleid(pfruleid);
			if (pFareas != null) {
				for (PFarea pf : pFareas) {
					this.pFoverbigDAO.deletePFoverbigByAreaid(pf.getId());
					this.pFoverweightDAO.deletePFoverweightByAreaid(pf.getId());
				}
			}
			this.pFareaDAO.deleteAreaByPfruleid(pfruleid);
			this.pFoverareaDAO.deletePFoverareaByPfruleid(pfruleid);
			this.pFinsertionDAO.deletePFinsertionByPfruleid(pfruleid);
		}
		return i;
	}

	@Test
	public void test() {
		JSONArray jsonStrs = new JSONArray();
		jsonStrs.add(0, "2011-01-01");
		jsonStrs.add(1, "2011-01-03");
		jsonStrs.add(2, "2011-01-04 11:11:11");
		String json = "[{\"showflag\":\"1\",\"customerid\":\"1\",\"basicPFfee\":\"1111.00\",\"remark\":\"1111\"},{\"customerid\":\"2\",\"basicPFfee\":\"222\",\"remark\":\"222\"}]";
		JSONArray array = JSONArray.fromObject(json);
		List<PFbasic> pfbasicList = (List<PFbasic>) JSONArray.toCollection(array, PFbasic.class);
		System.out.println(pfbasicList);
	}

	@Transactional
	public long saveArea(String areajson, String rulejson, String tab) {
		PaiFeiRule rule = (PaiFeiRule) JSONObject.toBean(JSONObject.fromObject(rulejson), PaiFeiRule.class);
		PFareaJson pFareaJson = (PFareaJson) JSONObject.toBean(JSONObject.fromObject(areajson), PFareaJson.class);
		int tabid = 0;
		long id =0;
		if (tab.equals("ps")) {
			tabid = PaiFeiRuleTabEnum.Paisong.getValue();
		} else if (tab.equals("th")) {
			tabid = PaiFeiRuleTabEnum.Tihuo.getValue();
		} else if (tab.equals("zz")) {
			tabid = PaiFeiRuleTabEnum.Zhongzhuan.getValue();
		}

		PFarea pf = new PFarea();
		pf.setAreafee(pFareaJson.getAreafee());
		pf.setAreaname(pFareaJson.getAreaname());
		pf.setAreaid(pFareaJson.getAreaid());
		pf.setTabid(tabid);
		pf.setPfruleid(rule.getId());
		pf.setTypeid(rule.getType());
		pf.setOverbigflag(pFareaJson.getOverbigflag());
		id= this.savedata(pf);

		if (pf.getOverbigflag() != 1) {

			List<PFoverbig> bigs = (List<PFoverbig>) JSONArray.toCollection(JSONArray.fromObject(pFareaJson.getOverbig()), PFoverbig.class);
			if (bigs != null) {
				for (PFoverbig big : bigs) {
					if (big != null) {
						big.setAreaid(id);
						this.savedata(big);
					}
				}
			}

		}
		List<PFoverweight> weights = (List<PFoverweight>) JSONArray.toCollection(JSONArray.fromObject(pFareaJson.getOverweight()), PFoverweight.class);
		if (weights != null) {
			for (PFoverweight weight : weights) {
				if (weight != null) {
					weight.setAreaid(id);
					this.savedata(weight);
				}
			}
		}

		// p
		/*
		 * PFarea area=new PFarea(); area.setAreaid(areaid);
		 * area.setAreaname(areaname); int tabid=0; if (tabs.equals("ps")) {
		 * tabid = PaiFeiRuleTabEnum.Paisong.getValue(); } else if
		 * (tabs.equals("th")) { tabid = PaiFeiRuleTabEnum.Tihuo.getValue(); }
		 * else if (tabs.equals("zz")) { tabid =
		 * PaiFeiRuleTabEnum.Zhongzhuan.getValue(); } area.setTabid(tabid);
		 * area.setPfruleid(rule.getId()); area.setTypeid(rule.getType());
		 * this.savedata(area);
		 */
		return id;
	}

	/**
	 * @param ruleid
	 * @param tabVal
	 * @param type
	 * @return
	 */
	public int  deleteData(long pfruleid, int tabid, String type) {

		if(type.equals("basic"))
		{
			return this.pFbasicDAO.deletePFbasicByPfRuleidAndTabid(pfruleid, tabid);
		}
		else if(type.equals("collection"))
		{
			return this.pFcollectionDAO.deletePFcollectionByPfRuleidAndTabid(pfruleid, tabid);
		}
		else if(type.equals("area"))
		{
			List<PFarea> areas=this.pFareaDAO.getPFareaByPfruleidAndTabid(pfruleid, tabid);
			if(areas!=null) {
				for(PFarea pf:areas)
				{
					this.pFoverbigDAO.deletePFoverbigByAreaid(pf.getAreaid());
					this.pFoverweightDAO.deletePFoverweightByAreaid(pf.getAreaid());
				}
			}
			return	this.pFareaDAO.deleteAreaByPfruleidAndTabid(pfruleid,tabid);
		}
		else if(type.equals("overarea"))
		{
			return	this.pFoverareaDAO.deletePFoverareaByPfRuleidAndTabid(pfruleid, tabid);
		}
		else if(type.equals("business"))
		{
			return	this.pFbusinessDAO.deleteBusinessByPfruleidAndTabid(pfruleid, tabid);
		}
		else if(type.equals("insertion"))
		{
			return	this.pFinsertionDAO.deletePFinsertionByPfRuleidAndTabid(pfruleid, tabid);
		}
		return 0;
	}
}
