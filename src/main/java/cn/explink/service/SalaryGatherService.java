package cn.explink.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.CustomerDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.domain.Customer;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.Smtcount;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.PaiFeiBuZhuTypeEnum;
import cn.explink.enumutil.PaiFeiRuleTabEnum;

@Service
public class SalaryGatherService {
	@Autowired
	PaiFeiRuleService paiFeiRuleService;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	CustomerDAO customerDAO;
	
	//获取基本派费，区域派费，超区补助，脱单补助，业务补助 方法
	public List<BigDecimal> getSalarypush(long pfruleid, String cwb){
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
		//业务KPI补助 TODO
		//BigDecimal bd8 = BigDecimal.ZERO;
		//其他补助 TODO
		//BigDecimal bd9 = BigDecimal.ZERO;
		
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
	
	//(根据供货商)获取小件员的妥投比例
	public List<Map> getCount(long userid,String starttime,String endtime){
		List<Map> mapList = new ArrayList<Map>();
		List<Customer> cusList = this.customerDAO.getAllCustomers();
		if(cusList!=null&&!cusList.isEmpty()){
			for(Customer cus : cusList){
				Map map = new HashMap();
				//long linghuo = 0;
				//TODO
				List<Smtcount> scList = this.deliveryStateDAO.getLinghuoCount(cus.getCustomerid(),userid,starttime,endtime);
				long linghuos  = this.deliveryStateDAO.getLinghuocwbs(cus.getCustomerid(),userid,starttime,endtime);
				/*if(dsList!=null&&!dsList.isEmpty()){
					for(DeliveryState ds : dsList){
						if(ds.getDeliverystate()==DeliveryStateEnum.PeiSongChengGong.getValue()||ds.getDeliverystate()==DeliveryStateEnum.ShangMenTuiChengGong.getValue()||ds.getDeliverystate()==DeliveryStateEnum.ShangMenHuanChengGong.getValue()){
							linghuo ++;
						}
					}
				}*/
				//TODO 待处理
				/*if(linghuo>0&&linghuos>0){
					double flo = (double)linghuo/linghuos;
					map.put(1, cus.getCustomerid());
					map.put(2, flo);
					mapList.add(map);
				}*/
			}
		}
		return mapList;
	}
}
