/**
 *
 */
package cn.explink.service;

import java.math.BigDecimal;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.dao.CustomerDAO;
import cn.explink.dao.PFbasicDAO;
import cn.explink.dao.PFbusinessDAO;
import cn.explink.dao.PFcollectionDAO;
import cn.explink.dao.PFinsertionDAO;
import cn.explink.dao.PFoverareaDAO;
import cn.explink.domain.Basicjson;
import cn.explink.domain.Collectionjson;
import cn.explink.domain.Customer;
import cn.explink.domain.PFbasic;
import cn.explink.domain.PFbusiness;
import cn.explink.domain.PFcollection;
import cn.explink.domain.PFinsertion;
import cn.explink.domain.PaifeiRuleJson;
import cn.explink.domain.PaifeiRulePS;
import cn.explink.domain.PaifeiRuleTH;
import cn.explink.domain.PaifeiRuleTab;
import cn.explink.domain.PaifeiRuleZZ;
import cn.explink.enumutil.PaiFeiBuZhuTypeEnum;
import cn.explink.enumutil.PaiFeiRuleTabEnum;

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
	CustomerDAO customerDAO;

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
		Basicjson basic = tab.getBasic();
		if (null != basic) {
			if (basic.getShowflag().equals("yes")) {
				List<PFbasic> pFbasics = (List<PFbasic>) JSONArray.toCollection(JSONArray.fromObject(basic.getPFfees()), PFbasic.class);
				for (PFbasic pf : pFbasics) {
					pf.setTabid(pfenum.getValue());
					pf.setPfruleid(pfruleid);
					pf.setTypeid(pfruletypeid);
					this.save(pf);
				}
			} else {
				PFbasic pf = new PFbasic();
				pf.setTabid(pfenum.getValue());
				pf.setPfruleid(pfruleid);
				pf.setTypeid(pfruletypeid);
				pf.setBasicPFfee(basic.getPFfee());
				for (Customer customer : customers) {
					pf.setCustomerid(customer.getCustomerid());
					this.save(pf);
				}
			}
		}
		Collectionjson collection = tab.getCollection();
		if (null != collection) {
			if (collection.getShowflag().equals("yes")) {
				List<PFcollection> pfcollection = (List<PFcollection>) JSONArray.toCollection(JSONArray.fromObject(collection.getPFfees()), PFcollection.class);
				for (PFcollection pf : pfcollection) {
					pf.setTabid(pfenum.getValue());
					pf.setPfruleid(pfruleid);
					pf.setTypeid(pfruletypeid);
					// 验证是否已经存在相应的规则
					this.save(pf);

				}
			} else {
				PFcollection pf = new PFcollection();
				pf.setTabid(pfenum.getValue());
				pf.setPfruleid(pfruleid);
				pf.setTypeid(pfruletypeid);
				pf.setCollectionPFfee(collection.getPFfee());
				for (Customer customer : customers) {
					pf.setCustomerid(customer.getCustomerid());
					this.save(pf);
				}
			}
		}
		List<PFinsertion> pFinsertions = (List<PFinsertion>) JSONArray.toCollection(JSONArray.fromObject(tab.getInsertion()), PFinsertion.class);
		for (PFinsertion pf : pFinsertions) {
			pf.setPfruleid(pfruleid);
			pf.setTabid(pfenum.getValue());
			pf.setTypeid(pfruletypeid);
			this.save(pf);
		}
		if (tab.getSubsidyfee() != null) {
			BigDecimal subsidyfee = tab.getSubsidyfee();
			PFbusiness pf = new PFbusiness();
			pf.setSubsidyfee(subsidyfee);
			pf.setPfruleid(pfruleid);
			pf.setTabid(pfenum.getValue());
			pf.setTypeid(pfruletypeid);
			this.save(pf);
		}
	}

	/**
	 * @param pf
	 */
	private void save(Object obj) {
		// 验证该记录是否已经存在了！
		if (obj instanceof PFbasic) {
			PFbasic pf = (PFbasic) obj;
			PFbasic pftemp = this.pFbasicDAO.getPFbasicByRTC(pf.getPfruleid(), pf.getTypeid(), pf.getTabid(), pf.getCustomerid());
			if (pftemp != null) {
				pf.setId(pftemp.getId());
				this.pFbasicDAO.updatePFbasic(pf);
			} else {
				this.pFbasicDAO.credata(pf);
			}
		}
		if (obj instanceof PFcollection) {
			PFcollection pf = (PFcollection) obj;
			PFcollection pftemp = this.pFcollectionDAO.getPFcollectionByRTC(pf.getPfruleid(), pf.getTypeid(), pf.getTabid(), pf.getCustomerid());
			if (pftemp != null) {
				pf.setId(pftemp.getId());
				this.pFcollectionDAO.updatePFcollection(pf);
			} else {
				this.pFcollectionDAO.credata(pf);
			}
		}
		if (obj instanceof PFbusiness) {
			PFbusiness pf = (PFbusiness) obj;
			PFbusiness pftemp = this.pFbusinessDAO.getPFbusinessByRTC(pf.getPfruleid(), pf.getTypeid(), pf.getTabid());
			if (pftemp != null) {
				pf.setId(pftemp.getId());
				this.pFbusinessDAO.updatePFbusiness(pf);
			} else {
				this.pFbusinessDAO.credata(pf);
			}
		}
		if (obj instanceof PFinsertion) {
			PFinsertion pf = (PFinsertion) obj;
			PFinsertion pftemp = this.pFinsertionDAO.getPFinsertionByRTC(pf.getPfruleid(), pf.getTypeid(), pf.getTabid(), pf.getMincount(), pf.getMaxcount());
			if (pftemp != null) {
				pf.setId(pftemp.getId());
				this.pFinsertionDAO.updatePFinsertion(pf);
			} else {
				this.pFinsertionDAO.credata(pf);
			}
		}
		/*
		 * if(obj instanceof PFoverarea){ PFoverarea pf=(PFoverarea) obj; }
		 */

	}

	/*
	 * @Test public void test() throws Exception, JsonMappingException,
	 * IOException { JSONObject json = JSONObject .fromObject(
	 * "{\"ps\":{\"collection\":{\"PFfees\":[],\"showflag\":\"yes\"},\"basic\":{\"PFfees\":[{\"customerid\":\"1\",\"basicPFfee\":\"12321\",\"remark\":\"11\"}],\"showflag\":\"yes\"}}}"
	 * ); PaifeiRuleJson pj = (PaifeiRuleJson) JSONObject.toBean(json,
	 * PaifeiRuleJson.class); List<PFbasic> pfList =
	 * pj.getPs().getBasic().getPFfees(); JSONArray list =
	 * JSONArray.fromObject(pfList); List<PFbasic> pfList1 = (List<PFbasic>)
	 * JSONArray.toCollection(list, PFbasic.class); System.out.println(pfList1);
	 *
	 * }
	 */

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
	public BigDecimal getPFRulefee(long pfruleid, PaiFeiRuleTabEnum tab, String cwb) {

		return new BigDecimal("0");
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

		return new BigDecimal("0");
	}
}
