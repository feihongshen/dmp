package cn.explink.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import scala.Array;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.PaybusinessbenefitsDao;
import cn.explink.dao.SalaryCountDAO;
import cn.explink.dao.SalaryGatherDao;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.Paybusinessbenefits;
import cn.explink.domain.SalaryCount;
import cn.explink.domain.SalaryGather;
import cn.explink.domain.Smtcount;
import cn.explink.domain.User;
import cn.explink.enumutil.PaiFeiBuZhuTypeEnum;
import cn.explink.enumutil.PaiFeiRuleTabEnum;
import cn.explink.util.StringUtil;

@Service
public class SalaryGatherService {
	@Autowired
	PaiFeiRuleService paiFeiRuleService;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	PaybusinessbenefitsDao paybusinessbenefitsDao;
	@Autowired
	UserDAO userDAO;
	@Autowired
	SalaryCountDAO salaryCountDAO;
	@Autowired
	SalaryGatherDao salaryGatherDao;
	
	//获取基本派费，区域派费，超区补助，脱单补助，业务补助 方法
	/*public List<BigDecimal> getSalarypush(long pfruleid, List<?> list){
		List<BigDecimal> bdList = new ArrayList<BigDecimal>();
		BigDecimal bdbasicarea = BigDecimal.ZERO;
		//单件配送费
		BigDecimal bd = BigDecimal.ZERO;
		//基本派费
		BigDecimal bd1 = BigDecimal.ZERO;
		//区域派费
		BigDecimal bd2 = BigDecimal.ZERO;
		//超区补助
		BigDecimal bd3 = BigDecimal.ZERO;
		//拖单补助
		BigDecimal bd4 = BigDecimal.ZERO;
		//业务补助
		BigDecimal bd5 = BigDecimal.ZERO;
		//大件补助 
		BigDecimal bd6 = BigDecimal.ZERO;
		//超重补助 
		BigDecimal bd7 = BigDecimal.ZERO;
		
		bd1 = this.paiFeiRuleService.getPFTypefeeByType(pfruleid, PaiFeiRuleTabEnum.Paisong, PaiFeiBuZhuTypeEnum.Basic, cwb);
		bd2 = this.paiFeiRuleService.getPFTypefeeByType(pfruleid, PaiFeiRuleTabEnum.Paisong, PaiFeiBuZhuTypeEnum.Area, cwb);
		bd3 = this.paiFeiRuleService.getPFTypefeeByType(pfruleid, PaiFeiRuleTabEnum.Paisong, PaiFeiBuZhuTypeEnum.Overarea, cwb);
		bd4 = this.paiFeiRuleService.getPFTypefeeByType(pfruleid, PaiFeiRuleTabEnum.Paisong, PaiFeiBuZhuTypeEnum.Insertion, cwb);
		bd5 = this.paiFeiRuleService.getPFTypefeeByType(pfruleid, PaiFeiRuleTabEnum.Paisong, PaiFeiBuZhuTypeEnum.Business, cwb);
		bd6 = this.paiFeiRuleService.getOverbigFee(pfruleid, PaiFeiRuleTabEnum.Paisong, cwb);
		bd7 = this.paiFeiRuleService.getOverbigFee(pfruleid, PaiFeiRuleTabEnum.Paisong, cwb);
		bd = bd1.add(bd2).add(bd3).add(bd4).add(bd5).add(bd6).add(bd7);
		bdList.add(0, bd);
		bdbasicarea = bd1.add(bd2);//基本派费+区域派费
		bdList.add(1, bdbasicarea);
		return bdList;
	}*/
	
	public List<BigDecimal> getSalarypush(long pfruleid, List<CwbOrder> cwbList){
		/*//基本派费
		BigDecimal bd1 = BigDecimal.ZERO;
		//区域派费
		BigDecimal bd2 = BigDecimal.ZERO;
		//超区补助
		BigDecimal bd3 = BigDecimal.ZERO;
		//拖单补助
		BigDecimal bd4 = BigDecimal.ZERO;
		//业务补助
		BigDecimal bd5 = BigDecimal.ZERO;
		//大件补助 
		BigDecimal bd6 = BigDecimal.ZERO;
		//超重补助 
		BigDecimal bd7 = BigDecimal.ZERO;*/
		Map<String, BigDecimal> strBig1 = this.paiFeiRuleService.getPFTypefeeByTypeOfBatch(pfruleid, PaiFeiRuleTabEnum.Paisong, PaiFeiBuZhuTypeEnum.Basic,cwbList);
		Map<String, BigDecimal> strBig2 = this.paiFeiRuleService.getPFTypefeeByTypeOfBatch(pfruleid, PaiFeiRuleTabEnum.Paisong, PaiFeiBuZhuTypeEnum.Area,cwbList);
		Map<String, BigDecimal> strBig3 = this.paiFeiRuleService.getPFTypefeeByTypeOfBatch(pfruleid, PaiFeiRuleTabEnum.Paisong, PaiFeiBuZhuTypeEnum.Overarea,cwbList);
		Map<String, BigDecimal> strBig4 = this.paiFeiRuleService.getPFTypefeeByTypeOfBatch(pfruleid, PaiFeiRuleTabEnum.Paisong, PaiFeiBuZhuTypeEnum.Insertion,cwbList);
		Map<String, BigDecimal> strBig5 = this.paiFeiRuleService.getPFTypefeeByTypeOfBatch(pfruleid, PaiFeiRuleTabEnum.Paisong, PaiFeiBuZhuTypeEnum.Business,cwbList);
		Map<String, BigDecimal> strBig6 = this.paiFeiRuleService.getOverbigFeeOfBatch(pfruleid, PaiFeiRuleTabEnum.Paisong,cwbList);
		Map<String, BigDecimal> strBig7 = this.paiFeiRuleService.getOverweightFeeOfBacth(pfruleid, PaiFeiRuleTabEnum.Paisong,cwbList);
		List<BigDecimal> bdbasicareaList = new ArrayList<BigDecimal>();
		List<BigDecimal> bdList = new ArrayList<BigDecimal>();
		if(strBig1!=null&&strBig1.size()>0){
			/*Set<String> list =  strBig1.keySet();
			Iterator iter = list.iterator();
			while(iter.hasNext()){
				String str = (String)iter.next();
			}*/
			Collection<BigDecimal> bdcolls = strBig1.values();
			for(BigDecimal bde : bdcolls){
				bdbasicareaList.add(bde);
				bdList.add(bde);
			}
		}
		if(strBig2!=null&&strBig2.size()>0){
			Collection<BigDecimal> bdcolls = strBig2.values();
			for(BigDecimal bde : bdcolls){
				bdbasicareaList.add(bde);
				bdList.add(bde);
			}
		}
		if(strBig3!=null&&strBig3.size()>0){
			Collection<BigDecimal> bdcolls = strBig3.values();
			for(BigDecimal bde : bdcolls){
				bdList.add(bde);
			}
		}
		if(strBig4!=null&&strBig4.size()>0){
			Collection<BigDecimal> bdcolls = strBig4.values();
			for(BigDecimal bde : bdcolls){
				bdList.add(bde);
			}
		}
		if(strBig5!=null&&strBig5.size()>0){
			Collection<BigDecimal> bdcolls = strBig5.values();
			for(BigDecimal bde : bdcolls){
				bdList.add(bde);
			}
		}
		if(strBig6!=null&&strBig6.size()>0){
			Collection<BigDecimal> bdcolls = strBig6.values();
			for(BigDecimal bde : bdcolls){
				bdList.add(bde);
			}
		}
		if(strBig7!=null&&strBig7.size()>0){
			Collection<BigDecimal> bdcolls = strBig7.values();
			for(BigDecimal bde : bdcolls){
				bdList.add(bde);
			}
		}
		
		List<BigDecimal> lsit = new ArrayList<BigDecimal>();
		//（基本派费+区域派费）总和
		BigDecimal bdbasicarea = BigDecimal.ZERO;
		if(bdbasicareaList!=null&&!bdbasicareaList.isEmpty()){
			for(BigDecimal bd : bdbasicareaList){
				bdbasicarea = bdbasicarea.add(bd);
			}
		}
		lsit.add(bdbasicarea);
		//计件配送费总和（kpi补助和其他补助排除）
		BigDecimal salaryfee = BigDecimal.ZERO;
		if(bdList!=null&&!bdList.isEmpty()){
			for(BigDecimal bd : bdList){
				salaryfee = salaryfee.add(bd);
			}
		}
		lsit.add(salaryfee);
		return lsit;
	}
	//个税计算公式
	public BigDecimal getTax(BigDecimal salaryaccrual) {
		BigDecimal bdreal = BigDecimal.ZERO;
		BigDecimal tax = BigDecimal.ZERO;
		DecimalFormat df = new DecimalFormat("0.00");
		double accrual = salaryaccrual.doubleValue();
		if(accrual<=0){
			salaryaccrual = new BigDecimal("0.00");
			tax = new BigDecimal("0.00");
		}else if(accrual>80000){
			tax = salaryaccrual.multiply(new BigDecimal(0.45)).subtract(new BigDecimal(13505));
			bdreal = new BigDecimal(df.format(tax));
		}else{
			bdreal = getSalaryTax(salaryaccrual,accrual,tax,df);
		}
		return bdreal;
	}
	
	//范围在0-80000区间的个人所得税(个税)
	public BigDecimal getSalaryTax(BigDecimal salaryaccrual,double accrual,BigDecimal tax,DecimalFormat df){
		BigDecimal tax3 = new BigDecimal(0.03);
		BigDecimal tax10 = new BigDecimal(0.10);
		BigDecimal tax20 = new BigDecimal(0.20);
		BigDecimal tax25 = new BigDecimal(0.25);
		BigDecimal tax30 = new BigDecimal(0.30);
		BigDecimal tax35 = new BigDecimal(0.35);
 		if(accrual>0&&accrual<=1500){
 			tax = salaryaccrual.multiply(tax3);
		}else if(accrual>1500&&accrual<=4500){
			tax = salaryaccrual.multiply(tax10).subtract(new BigDecimal(105));
		}else if(accrual>4500&&accrual<=9000){
			tax = salaryaccrual.multiply(tax20).subtract(new BigDecimal(555));
		}else if(accrual>9000&&accrual<=35000){
			tax = salaryaccrual.multiply(tax25).subtract(new BigDecimal(1005));
		}else if(accrual>35000&&accrual<=55000){
			tax = salaryaccrual.multiply(tax30).subtract(new BigDecimal(2755));
		}else if(accrual>55000&&accrual<=80000){
			tax = salaryaccrual.multiply(tax35).subtract(new BigDecimal(5505));
		}
 		String str = df.format(tax);
 		BigDecimal bigdeci = new BigDecimal(str);
		return bigdeci;
	}
	
	/*//(根据供货商)获取小件员的妥投比例
	public List<Map> getCount(long userid,String starttime,String endtime){
		List<Map> mapList = new ArrayList<Map>();
		List<Customer> cusList = this.customerDAO.getAllCustomers();
		if(cusList!=null&&!cusList.isEmpty()){
			for(Customer cus : cusList){
				Map map = new HashMap();
				//long linghuo = 0;
				//TODO
				List<Smtcount> scList = this.deliveryStateDAO.getLinghuoCount(userid,starttime,endtime);
				List<Smtcount> linghuos  = this.deliveryStateDAO.getLinghuocwbs(userid,starttime,endtime);
				if(dsList!=null&&!dsList.isEmpty()){
					for(DeliveryState ds : dsList){
						if(ds.getDeliverystate()==DeliveryStateEnum.PeiSongChengGong.getValue()||ds.getDeliverystate()==DeliveryStateEnum.ShangMenTuiChengGong.getValue()||ds.getDeliverystate()==DeliveryStateEnum.ShangMenHuanChengGong.getValue()){
							linghuo ++;
						}
					}
				}
				//TODO 待处理
				if(linghuo>0&&linghuos>0){
					double flo = (double)linghuo/linghuos;
					map.put(1, cus.getCustomerid());
					map.put(2, flo);
					mapList.add(map);
				}
			}
		}
		return mapList;
		//List<Smtcount> linghuos  = this.deliveryStateDAO.getLinghuocwbs(userid,starttime,endtime);
	}*/
	@SuppressWarnings("unused")
	public List<BigDecimal> getKpiOthers(long userid,String starttime,String endtime){
		List<Map> mapLists = new ArrayList<Map>();
		List<Map> mapList = new ArrayList<Map>();
		List<Smtcount> scList = this.deliveryStateDAO.getLinghuoCount(userid,starttime,endtime);
		List<Smtcount> linghuos  = this.deliveryStateDAO.getLinghuocwbs(userid,starttime,endtime);
		List<Paybusinessbenefits> pbbfList = this.paybusinessbenefitsDao.getAllpbbf();
		if(scList!=null&&!scList.isEmpty()&&!linghuos.isEmpty()&&linghuos!=null){
			for(Smtcount sc : scList){
				for(Smtcount linghuo : linghuos){
					if(sc.getCount()==linghuo.getCount()){
						Map map = new HashMap();
						map.put(1, sc.getCount());//供货商
						map.put(2, (double)(sc.getPscount())/(linghuo.getPscount()));//妥投率
						mapList.add(map);
					}
				}
			}
		}
		if(mapList!=null&&!mapList.isEmpty()&&pbbfList!=null&&!pbbfList.isEmpty()){
			for(Map map : mapList){
				long customerid = (Long)(map.get(1));
				double tuotoulv = (Double)(map.get(2));
				for(Paybusinessbenefits pbbf : pbbfList){
					if(customerid == pbbf.getCustomerid()){
						double lower = Double.parseDouble((pbbf.getLower()==null)||("".equals(pbbf.getLower()))?"0":pbbf.getLower());
						double upper = Double.parseDouble((pbbf.getUpper()==null)||("".equals(pbbf.getUpper()))?"0":pbbf.getUpper());
						if(tuotoulv>lower&&tuotoulv<=upper){
							Map maps = new HashMap();
							maps.put(1, pbbf.getCustomerid());//供货商
							maps.put(2, pbbf.getKpifee());//kpi业务补助
							maps.put(3, pbbf.getOthersubsidies());//其他补贴
							mapLists.add(maps);
						}
					}
				}
			}
		}
		List<BigDecimal> bdecList = new ArrayList<BigDecimal>();
		//kpi业务补助金额(总共)
		BigDecimal bdkpi = BigDecimal.ZERO;
		//其他补助金额(总共)
		BigDecimal others = BigDecimal.ZERO;
		if(scList!=null&&!scList.isEmpty()&&mapLists!=null&&!mapLists.isEmpty()){
			for(Smtcount sc : scList){
				for(Map mapss : mapLists){
					long customerid = (Long)(mapss.get(1));//供货商
					BigDecimal bd1 = (BigDecimal)(mapss.get(2));//kpi补助
					BigDecimal bd2 = (BigDecimal)(mapss.get(3));//其他补助
					if(sc.getCount()==customerid){
						long lcount = (Long)(sc.getPscount());
						BigDecimal bdcount = BigDecimal.valueOf(lcount);
						bdkpi = bdkpi.add(bdcount.multiply(bd1));
						others = others.add(bdcount.multiply(bd2));
					}
				}
			}
		}
		bdecList.add(bdkpi);
		bdecList.add(others);
		return bdecList;
	}
	
	@Transactional
	public long getHexiaoCounts(String ids, User use, String dateStr, String batchidstr) {
		long counts = 0;
		long usercount = 0;
		if(!"".equals(ids)){
			String[] strArray = ids.split(",");
			usercount = strArray.length;
			
			//对user表维护字段进行修改（后期预付款字段later...）
			Map<Long, BigDecimal> map = new HashMap<Long, BigDecimal>();
			List<SalaryGather> sgList = this.salaryGatherDao.getSalaryGathersByids(ids,batchidstr);
			if(sgList!=null&&!sgList.isEmpty()){
				for(SalaryGather sg : sgList){
					map.put(sg.getUserid(), sg.getImprestgoods());
				}
			}
			List<Long> strList = new ArrayList<Long>();
			if(!map.isEmpty()&&map.size()>0){
				Set<Long> ssList =  map.keySet();
				Iterator oter = ssList.iterator();
				while(oter.hasNext()){
					long str = (Long)(oter.next());
					strList.add(str);
				}
			}
			
			List<User> userList = this.userDAO.getUsersByuserids(ids);
			
			if(strList!=null&&!strList.isEmpty()&&userList!=null&&!userList.isEmpty()){
				for(long userid : strList){ 
					for(User user : userList){
						if(userid==user.getUserid()){
							BigDecimal bdec = map.get(userid);
							//修改此时配送员的当前(后期预付款)
							this.userDAO.updatelateradvanceByuserid(userid,user.getLateradvance().add(bdec));
						}
					}
				}
			}
			
			/*String batchid = "''";
			//SalaryCount sc = new SalaryCount();
			SalaryGather sg = new SalaryGather();
			if(sgList!=null&&!sgList.isEmpty()){
				sg = sgList.get(0);
				batchid = sg.getBatchid();
			}*/
			
			/*long userlong = 0;
			String userid = strArray[0];
			if(!"".equals(userid)){
				userlong = Long.parseLong(userid);
			}*/
			//SalaryCount sc = this.salaryCountDAO.getSalarycount(userlong);
			//更改批次为已核销状态(批量)
			//返回修改成功的单量
			/*if(sc!=null){
				batchid = sc.getBatchid();
			}*/
			counts = this.salaryCountDAO.updateSalaryState(batchidstr,use.getUserid(),dateStr,usercount);
		} 
		return usercount;
	}
	
}
