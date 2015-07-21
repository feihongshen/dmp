/**
 *
 */
package cn.explink.service;

import java.math.BigDecimal;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.PFAreaDAO;
import cn.explink.dao.PFbasicDAO;
import cn.explink.dao.PFbusinessDAO;
import cn.explink.dao.PFcollectionDAO;
import cn.explink.dao.PFinsertionDAO;
import cn.explink.dao.PFoverareaDAO;
import cn.explink.dao.PFoverbigDAO;
import cn.explink.dao.PFoverweightDAO;
import cn.explink.dao.PaiFeiRuleDAO;
import cn.explink.domain.Basicjson;
import cn.explink.domain.Collectionjson;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
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
import cn.explink.enumutil.PaiFeiRuleTypeEnum;

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
		if (tab.getOverarea() == 1) {
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
								big.setAreaid(id);
								this.savedata(big);
							}
						}
						List<PFoverweight> weights = (List<PFoverweight>) JSONArray.toCollection(JSONArray.fromObject(areajson.getOverweight()), PFoverweight.class);
						if (weights != null) {
							for (PFoverweight weight : weights) {
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
				this.pFbasicDAO.credata(pf);
				return pf.getId();
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
				this.pFcollectionDAO.credata(pf);
				return pf.getId();
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
				this.pFbusinessDAO.credata(pf);
				return pf.getId();
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
				this.pFinsertionDAO.credata(pf);
				return pf.getId();
			}
		}

		if (obj instanceof PFoverarea) {
			PFoverarea pf = (PFoverarea) obj;
			PFoverarea pftemp = this.pFoverareaDAO.getPFoverareaByRTC(pf.getPfruleid(), pf.getTypeid(), pf.getTabid(), pf.getState());
			if (pftemp != null) {
				// pf.setId();
				return pf.getId();
			} else {
				this.pFoverareaDAO.credata(pf);
				return pf.getId();
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
				this.pFareaDAO.credata(pf);
				return pf.getId();
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
				this.pFoverbigDAO.credata(pf);
				return pf.getId();
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
				this.pFoverweightDAO.credata(pf);
				return pf.getId();
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
	@Transactional
	public BigDecimal getPFRulefee(long pfruleid, PaiFeiRuleTabEnum tab, String cwb) {
		BigDecimal fee = new BigDecimal("0");
		CwbOrder co = this.cwbDAO.getCwbByCwb(cwb);
		PaiFeiRule pf = this.paiFeiRuleDAO.getPaiFeiRuleById(pfruleid);
		if ((co != null) && (pf != null)) {
			PFbasic baFbasic = this.pFbasicDAO.getPFbasicByRTC(pfruleid, PaiFeiRuleTypeEnum.Customer.getValue(), tab.getValue(), co.getCustomerid());
			if (baFbasic != null) {// 获取基本补助
				fee.add(baFbasic.getBasicPFfee());
			}
			PFcollection pFcollection = this.pFcollectionDAO.getPFcollectionByRTC(pfruleid, PaiFeiRuleTypeEnum.Customer.getValue(), tab.getValue(), co.getCustomerid());
			if (pFcollection != null) {// 获取代收补助
				fee.add(pFcollection.getCollectionPFfee());
			}
			// 获取区域补助
			// 需要获取订单中的区域
			long areaid = 0;
			PFarea pFarea = this.pFareaDAO.getPFareaBypfruleidAndTabidAndAreaid(pfruleid, tab.getValue(), areaid);
			if (pFarea != null) {
				fee.add(pFarea.getAreafee());
				if (pFarea.getOverbigflag() == 1) {
					// 需要调用申请表
				} else {
					// 货物体积
					PFoverbig pFoverbig = this.pFoverbigDAO.getPFoverbigByAreaidAndCount(areaid, 0);
					if (pFoverbig != null) {
						fee.add(pFoverbig.getSubsidyfee());
					}
				}
				PFoverweight pFoverweight = this.pFoverweightDAO.getPFoverweightByAreaidAndCount(areaid, 0);
				if (pFoverweight != null) {
					fee.add(pFoverweight.getSubsidyfee());
				}
			}
			PFoverarea pFoverarea = this.pFoverareaDAO.getPFoverareaByPaifeiruleAndtabid(pfruleid, tab.getValue());
			if (pFoverarea != null) {
				// 需要掉用申请表中的方法
			}
			PFbusiness pFbusiness = this.pFbusinessDAO.getPFbusinessByRTC(pfruleid, tab.getValue());
			if (pFbusiness != null) {
				fee.add(pFbusiness.getSubsidyfee());
			}
			PFinsertion pFinsertion = this.pFinsertionDAO.getPFinsertionByPfruleidAndCount(pfruleid, tab.getValue(), 0);
			if (pFinsertion != null) {
				fee.add(pFinsertion.getInsertionfee());
			}
		}
		return fee;
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
	public BigDecimal getPFTypefeeByType(long pfruleid, PaiFeiRuleTabEnum tab, PaiFeiBuZhuTypeEnum type, String cwb) {

		BigDecimal fee = new BigDecimal("0");
		CwbOrder co = this.cwbDAO.getCwbByCwb(cwb);
		PaiFeiRule pf = this.paiFeiRuleDAO.getPaiFeiRuleById(pfruleid);
		if ((co != null) && (pf != null)) {
			if (type == PaiFeiBuZhuTypeEnum.Basic) {
				PFbasic baFbasic = this.pFbasicDAO.getPFbasicByRTC(pfruleid, PaiFeiRuleTypeEnum.Customer.getValue(), tab.getValue(), co.getCustomerid());
				if (baFbasic != null) {// 获取基本补助
					return baFbasic.getBasicPFfee();
				}
			}
			if (type == PaiFeiBuZhuTypeEnum.Collection) {
				PFcollection pFcollection = this.pFcollectionDAO.getPFcollectionByRTC(pfruleid, PaiFeiRuleTypeEnum.Customer.getValue(), tab.getValue(), co.getCustomerid());
				if (pFcollection != null) {// 获取代收补助
					return pFcollection.getCollectionPFfee();
				}
			}
			// 获取区域补助
			// 需要获取订单中的区域
			if (type == PaiFeiBuZhuTypeEnum.Area) {
				long areaid = 0;
				BigDecimal areafee = new BigDecimal("0");
				PFarea pFarea = this.pFareaDAO.getPFareaBypfruleidAndTabidAndAreaid(pfruleid, tab.getValue(), areaid);
				if (pFarea != null) {
					areafee.add(pFarea.getAreafee());
					if (pFarea.getOverbigflag() == 1) {
						// 需要调用申请表
					} else {
						// 货物体积
						PFoverbig pFoverbig = this.pFoverbigDAO.getPFoverbigByAreaidAndCount(areaid, 0);
						if (pFoverbig != null) {
							areafee.add(pFoverbig.getSubsidyfee());
						}
					}
					PFoverweight pFoverweight = this.pFoverweightDAO.getPFoverweightByAreaidAndCount(areaid, 0);
					if (pFoverweight != null) {
						areafee.add(pFoverweight.getSubsidyfee());
					}
				}
				return areafee;
			}
			if (type == PaiFeiBuZhuTypeEnum.Overarea) {
				PFoverarea pFoverarea = this.pFoverareaDAO.getPFoverareaByPaifeiruleAndtabid(pfruleid, tab.getValue());
				if (pFoverarea != null) {
					// 需要掉用申请表中的方法
				}
			}
			if (type == PaiFeiBuZhuTypeEnum.Business) {
				PFbusiness pFbusiness = this.pFbusinessDAO.getPFbusinessByRTC(pfruleid, tab.getValue());
				if (pFbusiness != null) {
					return pFbusiness.getSubsidyfee();
				}
			}
			if (type == PaiFeiBuZhuTypeEnum.Insertion) {
				PFinsertion pFinsertion = this.pFinsertionDAO.getPFinsertionByPfruleidAndCount(pfruleid, tab.getValue(), 0);
				if (pFinsertion != null) {
					return pFinsertion.getInsertionfee();
				}
			}

		}
		return fee;

	}
	//货物超大补助
	public BigDecimal getOverbigFee(long pfruleid, PaiFeiRuleTabEnum tab, String cwb) {
		CwbOrder co = this.cwbDAO.getCwbByCwb(cwb);
		long areaid = 0;
		BigDecimal areafee = new BigDecimal("0");
		if (co != null) {
			PFarea pFarea = this.pFareaDAO.getPFareaBypfruleidAndTabidAndAreaid(pfruleid, tab.getValue(), areaid);
			if (pFarea != null) {
				if (pFarea.getOverbigflag() == 1) {
					// 需要调用申请表
				} else {
					// 货物体积
					PFoverbig pFoverbig = this.pFoverbigDAO.getPFoverbigByAreaidAndCount(areaid, 0);
					if (pFoverbig != null) {
						areafee.add(pFoverbig.getSubsidyfee());
					}
				}
			}
		}
		return areafee;
	}
	//货物超重补助
	public BigDecimal getOverweightFee(long pfruleid, PaiFeiRuleTabEnum tab, String cwb) {
		CwbOrder co = this.cwbDAO.getCwbByCwb(cwb);
		long areaid = 0;
		BigDecimal areafee = new BigDecimal("0");
		if (co != null) {
			PFarea pFarea = this.pFareaDAO.getPFareaBypfruleidAndTabidAndAreaid(pfruleid, tab.getValue(), areaid);
			if (pFarea != null) {
				PFoverweight pFoverweight = this.pFoverweightDAO.getPFoverweightByAreaidAndCount(areaid, 0);
				if (pFoverweight != null) {
					areafee.add(pFoverweight.getSubsidyfee());
				}
			}
		}
		return areafee;
	}

	/**
	 * @param model
	 * @param pfruleid
	 */
	public void getEditData(Model model, long pfruleid) {
		PaiFeiRule rule = this.paiFeiRuleDAO.getPaiFeiRuleById(pfruleid);

		// 查询基本补助
		List<PFbasic> basicListPS = this.pFbasicDAO.getPFbasicByPfruleidAndTabid(pfruleid, PaiFeiRuleTabEnum.Paisong.getValue());
		String showflag_basic = "no";
		if (basicListPS != null) {
			showflag_basic = this.getShowflagBasic(basicListPS, showflag_basic);
			model.addAttribute("basicPS", basicListPS.get(0));
		}
		model.addAttribute("ps_showflag_basic", showflag_basic);
		model.addAttribute("basicListPS", basicListPS);

		// 查询代收补助
		List<PFcollection> collectionListPS = this.pFcollectionDAO.getPFcollectionByPfruleidAndTabid(pfruleid, PaiFeiRuleTabEnum.Paisong.getValue());
		String showflag_collectionPS = "no";
		if (collectionListPS != null) {
			showflag_collectionPS = this.getShowflagCollection(collectionListPS, showflag_collectionPS);
			model.addAttribute("collectionPS", collectionListPS.get(0));
		}
		model.addAttribute("ps_showflag_collection", showflag_collectionPS);
		model.addAttribute("collectionListPS", collectionListPS);

		List<PFarea> pfareaListPS = this.pFareaDAO.getPFareaByPfruleidAndTabid(pfruleid, PaiFeiRuleTabEnum.Paisong.getValue());
		model.addAttribute("pfareaListPS", pfareaListPS);

		PFbusiness buFbusinessPS = this.pFbusinessDAO.getPFbusinessByPfruleidAndTabid(pfruleid, PaiFeiRuleTabEnum.Paisong.getValue());
		model.addAttribute("buFbusinessPS", buFbusinessPS);

		PFoverarea overareaPS = this.pFoverareaDAO.getPFoverareaByPaifeiruleAndtabid(pfruleid, PaiFeiRuleTabEnum.Paisong.getValue());
		model.addAttribute("overareaPS", overareaPS);

		List<PFinsertion> insertionListPS = this.pFinsertionDAO.getPFinsertionByPfruleidAndTabid(pfruleid, PaiFeiRuleTabEnum.Paisong.getValue());
		model.addAttribute("insertionListPS", insertionListPS);

		model.addAttribute("rule", rule);
		model.addAttribute("edit", 1);
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
	 * @param model
	 * @param request
	 */
	@Transactional
	public void editType(String json, String rulejson, String type, Model model) {
		// String edittype=request.getParameter("edittype");
		JSONObject object = JSONObject.fromObject(rulejson);
		PaiFeiRule rule = (PaiFeiRule) JSONObject.toBean(object, PaiFeiRule.class);
		if (type.equals("_rule")) {
			this.paiFeiRuleDAO.updatePaiFeiRule(rule);
			model.addAttribute("rule", rule);
		}
		if (type.contains("_")) {
			String flags[] = type.split("_");
			String tabs = flags[0];
			String bztype = flags[1];
			if (tabs.equals("ps")) {
				if (bztype.equals("basic")) {
					List<PFbasic> pfbasicList = (List<PFbasic>) JSONArray.toCollection(JSONArray.fromObject(json), PFbasic.class);
					if (pfbasicList != null) {

						this.pFbasicDAO.deletePFbasicByPfRuleidAndTabid(rule.getId(), PaiFeiRuleTabEnum.Paisong.getValue());

						for (PFbasic pf : pfbasicList) {
							pf.setPfruleid(rule.getId());
							pf.setTypeid(rule.getType());
							pf.setTabid(PaiFeiRuleTabEnum.Paisong.getValue());
							this.savedata(pf);
						}
					}
				}
			}
		}

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
}
