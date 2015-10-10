package cn.explink.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.BranchDAO;
import cn.explink.domain.Branch;
import cn.explink.enumutil.AccountFlowOrderTypeEnum;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.util.DateTimeUtil;

/**
 * 核心处理类
 * @author Administrator
 *
 */
@Service
public class AccountService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	FinaceDAO finaceDAO;
	@Autowired
	BranchDAO branchDAO;
	

	
	/**
	 * @param deliverybranchid 派送站点
	 * @param chongzhiAmount 页面传送过来的条件，手动录入
	 * @param xitongBalance 站点显示的系统余额，手动录入
	 */
	public void calc(long deliverybranchid,double chongzhiAmount,HttpServletRequest request){
		long starttime = System.currentTimeMillis();
		/**
		 * 获取所有的基础计算属性的数据
		 */
		Map<String, TypeVo> map = getAttriutes(deliverybranchid);
		
		double chongzhi=chongzhiAmount;
		double tuotou=map.get(AccountAttrVo.tuotou).getAmount();
		double diushi=map.get(AccountAttrVo.diushi).getAmount();
		double weiwanjie=map.get(AccountAttrVo.weiwanjie).getAmount();
		double zhongzhuan=map.get(AccountAttrVo.zhongzhuan).getAmount();
		double tuihuo=map.get(AccountAttrVo.tuihuo).getAmount();
		double tuihuozhanruku_weifankuan = map.get(AccountAttrVo.tuihuozhanruku_weifankuan).getAmount(); //退货站入库未返款 
		double jsWTHCZAmount = map.get(AccountAttrVo.jsWTHCZAmount).getAmount();//拒收未退货出站
		double thczWrkAmount = map.get(AccountAttrVo.thczWrkAmount).getAmount(); //退货出站（未入库）
		double zhongzhuanchuzhan = map.get(AccountAttrVo.zhongzhuanchuzhan).getAmount(); //中转出站(startbranchid)	
		double zhongzhuanzhanruku_weifankuan= map.get(AccountAttrVo.zhongzhuanzhanruku_weifankuan).getAmount();  //中转站入库（未返款）(startbranchid) 
		
		
	
		
		Branch branch = branchDAO.getBranchById(deliverybranchid);
		BigDecimal xitongBalance = branch.getBalance().subtract(branch.getDebt());//系统余额=站点余额-欠款
		
		
		//中转和退货重复总金额
		double zzthChongfuAmount = calcAmount(getZhongZhuanTuiHuoChongFuList(deliverybranchid));
		
		//中转和妥投重复
		double zzttChongfuAmount = calcAmount(getZhongZhuanTuoTouChongFuList(deliverybranchid));
		
		//退货和妥投重复
		double thttChongfuAmount = calcAmount(getTuiHuoTuoTouChongFuList(deliverybranchid));
		
		//中转和丢失重复
		double zzdsChongfuAmount = calcAmount(getZhongZhuanDiuShiChongFuList(deliverybranchid));
		
		//退货和丢失重复
		double thdsChongfuAmount = calcAmount(getTuiHuoDiuShiChongFuList(deliverybranchid));
		
		//未完结和中转重复
		double wwjzzChongfuAmount = calcAmount(getWeiWanJieZhongZhuanChongFuList(deliverybranchid));
		
		//未完结和退货重复
		double wwjthChongfuAmount = calcAmount(getWeiWanJieTuiHuoChongFuList(deliverybranchid));

		//中转出站和中转d
		double zzczzzChongfuAmount = calcAmount(getZhongZhuanChuZhanZhongZhuanChongFuList(deliverybranchid));
		
		//中转出站和退货d
		
		double zzczthChongfuAmount = calcAmount(getZhongZhuanChuZhanTuiHuoChongFuList(deliverybranchid));
		//中转站入库（未返款）和退货d
		double zzrkwfkthChongfuAmount = calcAmount(getZhongZhuanZhanRuKuTuiHuoChongFuList(deliverybranchid));
		
		//拒收未退货出库和中转d
		double jswthckzzChongfuAmount = calcAmount(getJuShouWeiTuiHuoChuKuZhongZhuanChongFuList(deliverybranchid));
		
		//拒收未退货出库和退货
		double jswthckthChongfuAmount = calcAmount(getJuShouWeiTuiHuoChuKuJuShouChongFuList(deliverybranchid));
		
		//拒收已退货出站（未入库）和中转
		double jsythczzzChongfuAmount = calcAmount(getJuShouYiTuiHuoChuZhanWeiRuKuZhongZhanChongFuList(deliverybranchid));
		
		//拒收已退货出站（未入库）和拒收
		double jsythczthChongfuAmount = calcAmount(getJuShouYiTuiHuoChuZhanWeiRuKuJuShouChongFuList(deliverybranchid));
		
		//退货站入库(未返款)和中转
		double thzrkwfkzzChongfuAmount = calcAmount(getTuiHuoZhanRuKuWeiFanKuanZhongZhuanChongFuList(deliverybranchid));//??
		
		/**
		 * 扣款和强制出库差异,如果有差异则说明多返款，每单差异= (Math.abs(扣款-强制出库)+1)*每单金额
		 */
		double kkqzckDiffAmount = this.getQiangZhiChuKuKouKuanDiffAmount(deliverybranchid);
		
		
		
		/**
		 * 获得实际扣款金额
		 * 实际扣款(总扣款)=   (妥投+丢失+未完结订单)num1    
							+ (拒收未退货出库+拒收已退货出站（未入库）+退货站入库(未返款)) 
							+ 中转出站+中转站入库（未返款）
							+ (中转+退货)num2    
-		 *  
		 *	-中转和退货重复-中转和妥投重复-退货和妥投重复-中转和丢失-退货和丢失
		 * -未完结和中转-未完结和退货-
		 * -中转出站和中转-中转出站和退货-中转站入库（未返款）和退货
		 * -拒收未退货出库和中转-拒收未退货出库和退货-拒收已退货出站（未入库）和中转-拒收已退货出站（未入库）和退货-退货站入库(未返款)和中转

			中转（未返款）=中转出站+中转站入库（未返款）
		 */
		 //(妥投+丢失+未完结订单)num1
		double num1 = tuotou+diushi+weiwanjie; 
		 //(中转+退货)num2
		double num2 =zhongzhuan+tuihuo;
		//拒收未退货出库+拒收已退货出站（未入库）+退货站入库(未返款)num_tuihuo
		double num_tuihuo=jsWTHCZAmount+thczWrkAmount+tuihuozhanruku_weifankuan;
		// 所有中转（未返款） =  中转出站(startbranchid)+中转站入库（未返款startbranchid） num_zhongzhuan
		double num_zhongzhuan=zhongzhuanchuzhan+zhongzhuanzhanruku_weifankuan;
		
		//所有的重复的金额，计算结果要扣除这些
		double filterAllChongfuKouKuan = zzthChongfuAmount + zzttChongfuAmount+ zzdsChongfuAmount  //中转
				+ thttChongfuAmount  + thdsChongfuAmount   //退货
				+ wwjzzChongfuAmount + wwjthChongfuAmount 
				+ zzczzzChongfuAmount + zzczthChongfuAmount +zzrkwfkthChongfuAmount  
				+jswthckzzChongfuAmount +jswthckthChongfuAmount +jsythczzzChongfuAmount +jsythczthChongfuAmount
				+thzrkwfkzzChongfuAmount;
		
		//实际扣款
		double infactKouKuan = num1 + num2 + num_tuihuo + num_zhongzhuan 
				-filterAllChongfuKouKuan;
		
		
		/**
		 * （财务角度）--实际余额=充值-妥投-丢失-未完结订单(不包括拒收) - 拒收未退货出站    
		 *   
		 */
		double infactBalance1=chongzhi-tuotou-diushi-weiwanjie-jsWTHCZAmount;
		
		/**
		 * (站点角度)--实际余额=充值+中转+退货    +退货出站（未入库）+退货出站已入库（未返款）  +中转未返款+中转出站
		 *  -去除所有重复的金额（filterAllChongfuKouKuan）
		 *  -实际扣款
		 */
		double infactBalance2=chongzhi+zhongzhuan+tuihuo + (thczWrkAmount+tuihuozhanruku_weifankuan)+num_zhongzhuan
							 -filterAllChongfuKouKuan- infactKouKuan;
		
		
		if(infactBalance1==infactBalance2){
			System.out.println("财务账务盘平了，success");
			//差异金额
			double chayi=infactBalance1-xitongBalance.doubleValue(); 
			
		}else{
			System.out.println("财务账务计算错误，failed");
		}
		
		long endtime = System.currentTimeMillis();
		logger.info("财务统计：{}总耗时={}",branch.getBranchname(),((endtime - starttime) / 1000));
		
		StringBuffer showSub=new StringBuffer("========="+branch.getBranchname()+"("+DateTimeUtil.getNowTime()+")"+"统计结果========\n");
		showSub.append("充值金额:"+chongzhiAmount+"\n");
		showSub.append("妥投:"+map.get(AccountAttrVo.tuotou).getNum()+"单("+map.get(AccountAttrVo.tuotou).getAmount()+")"     +"\n");
		showSub.append("妥投差异:"+map.get(AccountAttrVo.tuotou_chayi).getNum()+"单("+map.get(AccountAttrVo.tuotou_chayi).getAmount()+")"     +"\n");
		showSub.append("丢失:"+map.get(AccountAttrVo.diushi).getNum()+"单("+map.get(AccountAttrVo.diushi).getAmount()+")"     +"\n");
		showSub.append("丢失差异:"+map.get(AccountAttrVo.diushi_chayi).getNum()+"单("+map.get(AccountAttrVo.diushi_chayi).getAmount()+")"     +"\n");
		showSub.append("中转:"+map.get(AccountAttrVo.zhongzhuan).getNum()+"单("+map.get(AccountAttrVo.zhongzhuan).getAmount()+")"
		     +"，中转出站:"+map.get(AccountAttrVo.zhongzhuanchuzhan).getNum()+"单("+map.get(AccountAttrVo.zhongzhuanchuzhan).getAmount()
		     +")，中转站入库（未返款）:"+map.get(AccountAttrVo.zhongzhuanzhanruku_weifankuan).getNum()+"单("+map.get(AccountAttrVo.zhongzhuanzhanruku_weifankuan).getAmount()+")"
		     +   "\n");
		showSub.append("退货:"+map.get(AccountAttrVo.tuihuo).getNum()+"单("+map.get(AccountAttrVo.tuihuo).getAmount()+")"    
			+"，拒收未退货出站:"+map.get(AccountAttrVo.jsWTHCZAmount).getNum()+"单("+map.get(AccountAttrVo.jsWTHCZAmount).getAmount()
			+")，退货出站（未入库）:"+map.get(AccountAttrVo.thczWrkAmount).getNum()+"单("+map.get(AccountAttrVo.thczWrkAmount).getAmount()
			+")，退货站入库（未返款）:"+map.get(AccountAttrVo.tuihuozhanruku_weifankuan).getNum()+"单("+map.get(AccountAttrVo.tuihuozhanruku_weifankuan).getAmount()+")"
		 +"\n");
		 showSub.append("未完结订单:"+map.get(AccountAttrVo.weiwanjie).getNum()+"单("+map.get(AccountAttrVo.weiwanjie).getAmount()+")"     +"\n");
		 
		 showSub.append("====================订单重复=====================\n");
		 showSub.append("中转和妥投重复:"+zzttChongfuAmount+",中转和退货重复:"+zzthChongfuAmount+",中转丢失重复:"+zzdsChongfuAmount+"\n");
		 showSub.append("退货和妥投重复:"+thttChongfuAmount+",退货和丢失重复:"+thdsChongfuAmount+"\n");
		 showSub.append("未完结和中转重复:"+wwjzzChongfuAmount+",未完结和退货重复:"+wwjthChongfuAmount+"\n");
		 showSub.append("中转出站和中转重复:"+zzczzzChongfuAmount+",中转出站和退货重复:"+zzczthChongfuAmount+"\n");
		 showSub.append("中转站入库（未返款）和退货:"+zzrkwfkthChongfuAmount+"\n");
		 showSub.append("拒收未退货出库和中转:"+jswthckzzChongfuAmount+",拒收未退货出库和退货:"+jswthckthChongfuAmount+",拒收已退货出站（未入库）和中转:"+jsythczzzChongfuAmount +",拒收已退货出站（未入库）和拒收:"+jsythczthChongfuAmount +"\n");
		 showSub.append("退货站入库(未返款)和中转:"+thzrkwfkzzChongfuAmount+"\n");
		 showSub.append("\n\n");
		 showSub.append("系统余额:"+xitongBalance+"\n");
		 
		String infactDesc ="实际扣款(总扣款)= (妥投+丢失+未完结订单) + (拒收未退货出库+拒收已退货出站（未入库）+退货站入库(未返款)) + 中转出站+中转站入库（未返款）+ (中转+退货)-中转和退货重复-中转和妥投重复-退货和妥投重复-中转和丢失-退货和丢失"
		   +"-未完结和中转-未完结和退货-"
		   +"-中转出站和中转-中转出站和退货-中转站入库（未返款）和退货"
		   +"-拒收未退货出库和中转-拒收未退货出库和退货-拒收已退货出站（未入库）和中转-拒收已退货出站（未入库）和退货-退货站入库(未返款)和中转";
		 showSub.append(infactDesc+"\n");
		 showSub.append("实际扣款(总扣款公式):  ("+(tuotou+"+"+diushi+"+"+weiwanjie +")+("+ (jsWTHCZAmount+"+"+thczWrkAmount+"+"+tuihuozhanruku_weifankuan)
				 +")+("+ (zhongzhuanchuzhan+"+"+zhongzhuanzhanruku_weifankuan)
				 +")+("+ (zhongzhuan+"+"+tuihuo)+")-"+decimalFormat(filterAllChongfuKouKuan)+" = "+infactKouKuan) +"\n");
		
		 showSub.append("实际余额（财务）:  实际余额=充值-妥投-丢失-未完结订单(不包括拒收) - 拒收未退货出站" +"\n");
		 showSub.append("实际余额（财务公式）: "+chongzhi+"-"+tuotou+"-"+diushi+"-"+weiwanjie+"-"+jsWTHCZAmount+" = "+decimalFormat(infactBalance1) +"\n");
		 showSub.append("实际余额（站点）:  实际余额= 充值+中转+退货 +退货出站（未入库）+退货出站已入库（未返款） +中转未返款+中转出站-去除所有重复的金额-实际扣款"+"\n");
		 showSub.append("实际余额（站点公式）: "+chongzhi+"+"+zhongzhuan+"+"+tuihuo +"+"+ (thczWrkAmount+"+"+tuihuozhanruku_weifankuan)+"+"
				 			 +zhongzhuanchuzhan+"+"+zhongzhuanzhanruku_weifankuan
							 +"-"+decimalFormat(filterAllChongfuKouKuan)+"-"+ decimalFormat(infactKouKuan)+" = "+decimalFormat(infactBalance2)+"\n");
		 showSub.append("差异金额=实际余额-系统错误余额-强制出库和扣款差异"+"\n");
		 showSub.append("差异金额="+decimalFormat(infactBalance1)+"-"+xitongBalance.doubleValue()+"-"+kkqzckDiffAmount+" = "+decimalFormat(infactBalance1-xitongBalance.doubleValue())+"\n");
		 
		 double datadiff=decimalFormat(infactBalance1-xitongBalance.doubleValue()-kkqzckDiffAmount);
		 
		 request.setAttribute("showresult", showSub.toString());
		 request.setAttribute("datadiff", String.valueOf(datadiff));
		 
		
	}
	public static double decimalFormat(double str) {
		java.text.DecimalFormat   df=new   java.text.DecimalFormat("#.##");  
  		return Double.valueOf(df.format(str)); 
		
	}
	/**
	 * 根据 重复list拿到对应的订单金额总和
	 * @param chongfuList
	 * @return
	 */
	private double calcAmount(List<TypeVo> chongfuList) {
		double amounts=0;
		if(chongfuList == null ||chongfuList.size() == 0){
			return amounts;
		}
		for(TypeVo vo:chongfuList){
			amounts=amounts+vo.getAmount();
		}
		return amounts;
	}
	
	private TypeVo calcTypeVo(List<TypeVo> chongfuList) {
		double amounts=0;
		long nums=0;
		if(chongfuList == null ||chongfuList.size() == 0){
			return new TypeVo(0,0.00);
		}
		for(TypeVo vo:chongfuList){
			amounts=amounts+vo.getAmount();
		}
		return  new TypeVo(chongfuList.size(),amounts);
	}
	
	//得到中转和退货重复list
	private List<TypeVo> getZhongZhuanTuiHuoChongFuList(long deliverybranchid) {
		List<TypeVo> zhongzhuanList = finaceDAO.getDeductListByType(AccountFlowOrderTypeEnum.ZhongZhuan.getValue(), deliverybranchid);
		List<TypeVo> tuihuoList = finaceDAO.getDeductListByType(AccountFlowOrderTypeEnum.TuiHuo.getValue(), deliverybranchid);
		
		List<TypeVo> zzthChongfuList = getChongfuList(zhongzhuanList, tuihuoList);
		
		return zzthChongfuList;
	}
	
	//得到中转和妥投重复list
	private List<TypeVo> getZhongZhuanTuoTouChongFuList(long deliverybranchid) {
		
		List<TypeVo> zhongzhuanList = finaceDAO.getDeductListByType(AccountFlowOrderTypeEnum.ZhongZhuan.getValue(), deliverybranchid);
		List<TypeVo> tuotouList = finaceDAO.getDeliveryListByType(DeliveryStateEnum.PeiSongChengGong.getValue(), deliverybranchid);
		
		List<TypeVo> zzttChongfuList  =	getChongfuList(zhongzhuanList, tuotouList);
		
		
		return zzttChongfuList;
	}
	
	//得到退货和妥投重复list
	private List<TypeVo> getTuiHuoTuoTouChongFuList(long deliverybranchid) {
		
		List<TypeVo> tuihuoList = finaceDAO.getDeductListByType(AccountFlowOrderTypeEnum.TuiHuo.getValue(), deliverybranchid);
		List<TypeVo> tuotouList = finaceDAO.getDeliveryListByType(DeliveryStateEnum.PeiSongChengGong.getValue(), deliverybranchid);
		
		List<TypeVo> chongfuList  =	getChongfuList(tuihuoList, tuotouList);
		
		
		return chongfuList;
	}
	
	//得到中转和丢失重复list
	private List<TypeVo> getZhongZhuanDiuShiChongFuList(long deliverybranchid) {
		
		List<TypeVo> zhongzhuanList = finaceDAO.getDeductListByType(AccountFlowOrderTypeEnum.ZhongZhuan.getValue(), deliverybranchid);
		List<TypeVo> diushiList = finaceDAO.getDeliveryListByType(DeliveryStateEnum.HuoWuDiuShi.getValue(), deliverybranchid);
		
		List<TypeVo> chongfuList  =	getChongfuList(zhongzhuanList, diushiList);
		
		return chongfuList;
	}
	
	//得到退货和丢失重复list
	private List<TypeVo> getTuiHuoDiuShiChongFuList(long deliverybranchid) {
		
		List<TypeVo> tuihuoList = finaceDAO.getDeductListByType(AccountFlowOrderTypeEnum.TuiHuo.getValue(), deliverybranchid);
		List<TypeVo> diushiList = finaceDAO.getDeliveryListByType(DeliveryStateEnum.HuoWuDiuShi.getValue(), deliverybranchid);
		
		List<TypeVo> chongfuList  =	getChongfuList(tuihuoList, diushiList);
		
		return chongfuList;
	}
	//得到未完结和中转重复list
	private List<TypeVo> getWeiWanJieZhongZhuanChongFuList(long deliverybranchid) {
		
		List<TypeVo> zhongzhuanList = finaceDAO.getDeductListByType(AccountFlowOrderTypeEnum.ZhongZhuan.getValue(), deliverybranchid);
		List<TypeVo> weiwanjieList = finaceDAO.getDeliveryNoResultListByType(deliverybranchid);
		
		List<TypeVo> chongfuList  =	getChongfuList(zhongzhuanList, weiwanjieList);
		
		return chongfuList;
	}
	
	//得到未完结和退货重复list
	private List<TypeVo> getWeiWanJieTuiHuoChongFuList(long deliverybranchid) {
		
		List<TypeVo> tuihuoList = finaceDAO.getDeductListByType(AccountFlowOrderTypeEnum.TuiHuo.getValue(), deliverybranchid);
		List<TypeVo> weiwanjieList = finaceDAO.getDeliveryNoResultListByType(deliverybranchid);
		
		List<TypeVo> chongfuList  =	getChongfuList(tuihuoList, weiwanjieList);
		
		return chongfuList;
	}
	
	//得到退货未返款和中转重复list
	private List<TypeVo> getTuiHuoWeiFanKuanZhongZhuanChongFuList(long deliverybranchid) {
		
		List<String> tuihuoDect=finaceDAO.getDeductByType(deliverybranchid, AccountFlowOrderTypeEnum.TuiHuo.getValue());//退货记录
		List<TypeVo> thwfkList=  finaceDAO.getDeliveryDetailByStateNoBack(DeliveryStateEnum.JuShou.getValue(), deliverybranchid,this.getCwbs(tuihuoDect));
		List<TypeVo> zhongzhuanList = finaceDAO.getDeductListByType(AccountFlowOrderTypeEnum.ZhongZhuan.getValue(), deliverybranchid);
		
		List<TypeVo> chongfuList  =	getChongfuList(thwfkList, zhongzhuanList);
		
		return chongfuList;
	}
	
	
	//得到拒收未退货出库和中转重复list
	private List<TypeVo> getJuShouWeiTuiHuoChuKuZhongZhuanChongFuList(long deliverybranchid) {
		
		 List<TypeVo> jjwthczList= finaceDAO.getDeliveryJuShouNoBackOut(deliverybranchid);
	
		List<TypeVo> zhongzhuanList = finaceDAO.getDeductListByType(AccountFlowOrderTypeEnum.ZhongZhuan.getValue(), deliverybranchid);
		
		List<TypeVo> chongfuList  =	getChongfuList(jjwthczList, zhongzhuanList);
		
		return chongfuList;
	}
	
	
	//得到拒收未退货出库和拒收重复list
	private List<TypeVo> getJuShouWeiTuiHuoChuKuJuShouChongFuList(long deliverybranchid) {
		
		 List<TypeVo> jjwthczList= finaceDAO.getDeliveryJuShouNoBackOut(deliverybranchid);
	
		List<TypeVo> zhongzhuanList = finaceDAO.getDeductListByType(AccountFlowOrderTypeEnum.TuiHuo.getValue(), deliverybranchid);
		
		List<TypeVo> chongfuList  =	getChongfuList(jjwthczList, zhongzhuanList);
		
		return chongfuList;
	}
//	finaceDAO.getDeliveryTuiHuoChuZhanWeiRuKu(deliverybranchid)

	//得到拒收已退货出站未入库和中转重复list
	private List<TypeVo> getJuShouYiTuiHuoChuZhanWeiRuKuZhongZhanChongFuList(long deliverybranchid) {
		
		 List<TypeVo> jjythczList= finaceDAO.getDeliveryTuiHuoChuZhanWeiRuKu(deliverybranchid);
	
		List<TypeVo> tuihuoList = finaceDAO.getDeductListByType(AccountFlowOrderTypeEnum.ZhongZhuan.getValue(), deliverybranchid);
		
		List<TypeVo> chongfuList  =	getChongfuList(jjythczList, tuihuoList);
		
		return chongfuList;
	}


	//得到拒收未退货出库和拒收重复list
	private List<TypeVo> getJuShouYiTuiHuoChuZhanWeiRuKuJuShouChongFuList(long deliverybranchid) {
		
		 List<TypeVo> jjythczList= finaceDAO.getDeliveryTuiHuoChuZhanWeiRuKu(deliverybranchid);
	
		List<TypeVo> tuihuoList = finaceDAO.getDeductListByType(AccountFlowOrderTypeEnum.TuiHuo.getValue(), deliverybranchid);
		
		List<TypeVo> chongfuList  =	getChongfuList(jjythczList, tuihuoList);
		
		return chongfuList;
	}
	
	//得到退货未返款和退货重复list
	private List<TypeVo> getTuiHuoWeiFanKuanTuiHuoChongFuList(long deliverybranchid) {
		
		List<String> tuihuoDect=finaceDAO.getDeductByType(deliverybranchid, AccountFlowOrderTypeEnum.TuiHuo.getValue());//退货记录
		List<TypeVo> thwfkList=  finaceDAO.getDeliveryDetailByStateNoBack(DeliveryStateEnum.JuShou.getValue(), deliverybranchid,this.getCwbs(tuihuoDect));
		List<TypeVo> tuihuoList = finaceDAO.getDeductListByType(AccountFlowOrderTypeEnum.TuiHuo.getValue(), deliverybranchid);
		
		List<TypeVo> chongfuList  =	getChongfuList(thwfkList, tuihuoList);
		
		return chongfuList;
	}
	
	//得到退货站入库未返款和中转重复list
	private List<TypeVo> getTuiHuoZhanRuKuWeiFanKuanZhongZhuanChongFuList(long deliverybranchid) {
		
		List<String> tuihuoDect=finaceDAO.getDeductByType(deliverybranchid, AccountFlowOrderTypeEnum.TuiHuo.getValue());//退货记录
		
		List<String> deliveryList=finaceDAO.getDeliveryListByJuShou(deliverybranchid);//退货记录
		
		List<TypeVo> thwfkList =  finaceDAO.getDetaiListNoBackAmount(DeliveryStateEnum.JuShou.getValue(), deliverybranchid,this.getCwbs(tuihuoDect),this.getCwbs(deliveryList));
		
		List<TypeVo> zhongzhuanList = finaceDAO.getDeductListByType(AccountFlowOrderTypeEnum.ZhongZhuan.getValue(), deliverybranchid);
		
		List<TypeVo> chongfuList  =	getChongfuList(thwfkList, zhongzhuanList);
		
		return chongfuList;
	}
	
	
	//得到中转出站和中转重复list
	private List<TypeVo> getZhongZhuanChuZhanZhongZhuanChongFuList(long deliverybranchid) {
		String zzBranchIds = getBranchListByZhongZhuan(); //得到所有中转订单
		
		List<TypeVo> zhongzhuanchuzhanList=  finaceDAO.getDetailZhongZhuanChuZhan(deliverybranchid,zzBranchIds);
		List<TypeVo> zhongzhuanList = finaceDAO.getDeductListByType(AccountFlowOrderTypeEnum.ZhongZhuan.getValue(), deliverybranchid);
		
		List<TypeVo> chongfuList  =	getChongfuList(zhongzhuanchuzhanList, zhongzhuanList);
		
		return chongfuList;
	}
	
	//得到中转出站和退货重复list
	private List<TypeVo> getZhongZhuanChuZhanTuiHuoChongFuList(long deliverybranchid) {
		String zzBranchIds = getBranchListByZhongZhuan(); //得到所有中转订单
		List<TypeVo> zhongzhuanchuzhanList=  finaceDAO.getDetailZhongZhuanChuZhan(deliverybranchid,zzBranchIds);
		List<TypeVo> tuihuoList = finaceDAO.getDeductListByType(AccountFlowOrderTypeEnum.TuiHuo.getValue(), deliverybranchid);
		
		List<TypeVo> chongfuList  =	getChongfuList(zhongzhuanchuzhanList, tuihuoList);
		
		return chongfuList;
	}
	
	//得到中转站入库未返款和退货重复list 2015-06-22暂时不参与查询
	private List<TypeVo> getZhongZhuanZhanRuKuTuiHuoChongFuList(long deliverybranchid) {
		
	//	List<String> zhongzhuanDect=finaceDAO.getDeductByType(deliverybranchid, AccountFlowOrderTypeEnum.ZhongZhuan.getValue());//中转返款表记录
//		String zzBranchIds = getBranchListByZhongZhuan(); //得到所有中转订单
//		
//		List<TypeVo> zzrkwfkVoList =  finaceDAO.getDetaiListZhongZhuanNoBackAmount(deliverybranchid,this.getCwbs(zhongzhuanDect),zzBranchIds);
//		
//		List<TypeVo> tuihuoList = finaceDAO.getDeductListByType(AccountFlowOrderTypeEnum.TuiHuo.getValue(), deliverybranchid);
//		
//		List<TypeVo> chongfuList  =	getChongfuList(zzrkwfkVoList, tuihuoList);
		
		return null;
	}
	
	/**
	 * 
	 * @param assistList 辅助list
	 * @param mainList  主要list
	 * @return
	 */
	private List<TypeVo> getChongfuList(List<TypeVo> assistList, List<TypeVo> mainList) {
		List<TypeVo> chongfuList = new ArrayList<TypeVo>();
		for(TypeVo zzVo:assistList){
			for(TypeVo thVo:mainList){
				if(zzVo.getCwb().equals(thVo.getCwb())){
					chongfuList.add(zzVo);
				}
			}
		}
		return chongfuList;
	}
	
	
	/**
	 * 查询出所有的基础计算数据
	 * @param deliverybranchid
	 * @return
	 */
	private Map<String, TypeVo> getAttriutes(long deliverybranchid) {
		Branch branch= branchDAO.getBranchByBranchid(deliverybranchid);
		List<String>  listAttr = this.initAttrList();
		
		Map<String, TypeVo> mapList=new HashMap<String, TypeVo>();
		
		for(String attr:listAttr){
			Map<String, TypeVo> map=new HashMap<String, TypeVo>();
			long starttime = System.currentTimeMillis();
			if(attr.equals(AccountAttrVo.tuotou)){
				map = buildAttrDeliveryResult(DeliveryStateEnum.PeiSongChengGong.getValue(), deliverybranchid,AccountAttrVo.tuotou,null);
			}
			if(attr.equals(AccountAttrVo.tuotou_chayi)){
				map = buildAttrDeliveryResultDiff(DeliveryStateEnum.PeiSongChengGong.getValue(), deliverybranchid,AccountAttrVo.tuotou_chayi,null);
			}
			
			if(attr.equals(AccountAttrVo.diushi)){
				map = buildAttrDeliveryResult(DeliveryStateEnum.HuoWuDiuShi.getValue(), deliverybranchid,AccountAttrVo.diushi,null);
			}
			if(attr.equals(AccountAttrVo.diushi_chayi)){
				map = buildAttrDeliveryResultDiff(DeliveryStateEnum.HuoWuDiuShi.getValue(), deliverybranchid,AccountAttrVo.diushi_chayi,null);
			}
			
			if(attr.equals(AccountAttrVo.zhongzhuan)){
				map = buildAttrZhongZhuanTuiHuo(deliverybranchid,AccountFlowOrderTypeEnum.ZhongZhuan.getValue(),attr);
			}
			if(attr.equals(AccountAttrVo.tuihuo)){
				map = buildAttrZhongZhuanTuiHuo(deliverybranchid,AccountFlowOrderTypeEnum.TuiHuo.getValue(),attr);
			}
//			if(attr.equals(AccountAttrVo.tuihuo_weifankuan)){
//				map = buildTuiHuoWeiFanKuan(deliverybranchid);
//			}
			if(attr.equals(AccountAttrVo.tuihuozhanruku_weifankuan)){
				map = buildAttrTuiHuoZhanRukuWeiFanKuan(deliverybranchid,AccountFlowOrderTypeEnum.TuiHuo.getValue(),attr);
			}
			if(attr.equals(AccountAttrVo.zhongzhuanzhanruku_weifankuan)){ //中转站入库未返款
				map = buildAttrZhongZhuanZhanRukuWeiFanKuan(deliverybranchid,AccountFlowOrderTypeEnum.ZhongZhuan.getValue(),attr);
			}
			
			
			if(attr.equals(AccountAttrVo.weiwanjie)){
				map = buildWeiWanJie(deliverybranchid);
			}
			
			if(attr.equals(AccountAttrVo.jsWTHCZAmount)){
				TypeVo vo = calcTypeVo(finaceDAO.getDeliveryJuShouNoBackOut(deliverybranchid));//拒收未退货出站
				map.put(attr,vo);
			}
			if(attr.equals(AccountAttrVo.thczWrkAmount)){
				TypeVo vo = calcTypeVo(finaceDAO.getDeliveryTuiHuoChuZhanWeiRuKu(deliverybranchid));   //退货出站（未入库）
				map.put(attr, vo);
			}
			if(attr.equals(AccountAttrVo.zhongzhuanchuzhan)){
				String zzBranchIds = getBranchListByZhongZhuan(); //得到所有中转订单
				TypeVo vo = calcTypeVo(finaceDAO.getDetailZhongZhuanChuZhan(deliverybranchid,zzBranchIds));  //中转出站(startbranchid)	
				map.put(attr, vo);
			}
				
			
			
			if(map.size()>0){
				mapList.put(attr, map.get(attr));
				long endtime = System.currentTimeMillis();
				logger.info("财务统计："+branch+"-查询={}，耗时={}秒",attr, ((endtime - starttime) / 1000));
			}
			
		}
		return mapList;
	}
	

	private Map<String, TypeVo> buildWeiWanJie(long deliverybranchid) {
		TypeVo vo=finaceDAO.getDetailWeiWanJie(deliverybranchid);
		Map<String,TypeVo> map6=setValue(AccountAttrVo.weiwanjie, vo.getNum(), vo.getAmount());
		return map6;
	}

//	//构建拒收未返款
//	private Map<String, TypeVo> buildTuiHuoWeiFanKuan(long deliverybranchid) {
//		List<String> tuihuoDect=finaceDAO.getDeductByType(deliverybranchid, AccountFlowOrderTypeEnum.TuiHuo.getValue());//退货记录
//		TypeVo typeVo =  finaceDAO.getDeliveryByStateNoBack(DeliveryStateEnum.JuShou.getValue(), deliverybranchid,this.getCwbs(tuihuoDect));
//		Map<String,TypeVo> map5=setValue(AccountAttrVo.tuihuo_weifankuan, typeVo.getNum(), typeVo.getAmount());
//		return map5;
//	}
	
	


	//构建返款扣款
	private Map<String, TypeVo> buildAttrZhongZhuanTuiHuo(long deliverybranchid,long accountFlowType,String attrVo) {
		TypeVo typeVo =  finaceDAO.getDeliveryZhongZhuanTuiHuo(accountFlowType, deliverybranchid);
		Map<String,TypeVo> map3=setValue(attrVo, typeVo.getNum(), typeVo.getAmount());
		return map3;
	}

	//构建配送结果
	private Map<String, TypeVo> buildAttrDeliveryResult(long deliverystate,long deliverybranchid,String attr,String cwbs) {
		TypeVo typeVo =  finaceDAO.getDeliveryByState(deliverystate, deliverybranchid,cwbs);
		Map<String,TypeVo> map=setValue(attr, typeVo.getNum(), typeVo.getAmount());
		return map;
	}
	
	//妥投差异丢失差异
	private Map<String, TypeVo> buildAttrDeliveryResultDiff(long deliverystate,long deliverybranchid,String attr,String cwbs) {
	 List<String> deductList =finaceDAO.getDeductByType(deliverybranchid, AccountFlowOrderTypeEnum.KouKuan.getValue());
		TypeVo typeVo =  finaceDAO.getDeliveryByStateDiff(deliverystate, deliverybranchid,this.getCwbs(deductList));
		Map<String,TypeVo> map=setValue(attr, typeVo.getNum(), typeVo.getAmount());
		return map;
	}
	
	//构建 退货站入库及之后的所有流程（未返款）
	private Map<String, TypeVo> buildAttrTuiHuoZhanRukuWeiFanKuan(long deliverybranchid,long accountFlowType,String attrVo) {
		List<String> tuihuoDect=finaceDAO.getDeductByType(deliverybranchid, accountFlowType);//退货记录
		
		List<String> deliveryList=finaceDAO.getDeliveryListByJuShou(deliverybranchid);//退货记录
		TypeVo typeVo =  finaceDAO.getDetaiNoBackAmount(DeliveryStateEnum.JuShou.getValue(), deliverybranchid,this.getCwbs(tuihuoDect),this.getCwbs(deliveryList));
		Map<String,TypeVo> map5=setValue(AccountAttrVo.tuihuozhanruku_weifankuan, typeVo.getNum(), typeVo.getAmount());
		return map5;
	}
	

	//得到强制出库和扣款的差异
	private double getQiangZhiChuKuKouKuanDiffAmount(long deliverybranchid) {
		
		List<Map<String,Long>> qzckList = finaceDAO.getQiangZhiChukuChongfu(deliverybranchid); //强制出库重复
		String cwbs=this.getMapCwbs(qzckList);
		List<Map<String,Long>> kkcfList=finaceDAO.getKouKuanByBranchidList(deliverybranchid, cwbs); //扣款重复
		
		Map<String,Long> needDiffMap = getQzckkkDiffMap(qzckList, kkcfList);//定义强制出库和扣款差异单
		
		double totalAmount = 0.00;
		for(String cwb:needDiffMap.keySet()){
			long count=needDiffMap.get(cwb);
			TypeVo typeVo =	finaceDAO.getDeliveryByType(DeliveryStateEnum.PeiSongChengGong.getValue(), deliverybranchid);
			totalAmount += typeVo.getAmount()*count;
		}
		
		return totalAmount;
	}
	
	//得到强制出库和扣款的差异
	public Map<String,Double> getQiangZhiChuKuKouKuanDiffList(long deliverybranchid) {
		
		List<Map<String,Long>> qzckList = finaceDAO.getQiangZhiChukuChongfu(deliverybranchid); //强制出库重复
		String cwbs=this.getMapCwbs(qzckList);
		List<Map<String,Long>> kkcfList=finaceDAO.getKouKuanByBranchidList(deliverybranchid, cwbs); //扣款重复
		
		Map<String,Long> needDiffMap = getQzckkkDiffMap(qzckList, kkcfList);//定义强制出库和扣款差异单
		
		Map<String,Double> diffMap = new HashMap<String, Double>();
		
		for(String cwb:needDiffMap.keySet()){
			long count=needDiffMap.get(cwb);
			TypeVo typeVo =	finaceDAO.getDeliveryByCwb(cwb);
			diffMap.put(typeVo.getCwb(), typeVo.getAmount()*count);
		}
		
		return diffMap;
	}
	
	private Map<String,Long> getQzckkkDiffMap(List<Map<String, Long>> qzckList,List<Map<String, Long>> kkcfList) {
		Map<String,Long> needDiffMap = new HashMap<String, Long>();//定义强制出库和扣款差异单
		
		for(Map<String,Long> qzckMap:qzckList){
			for(Map<String,Long> kkcfMap:kkcfList){
				String qzckCwb=getKey(qzckMap.entrySet());
				String kkcfCwb=getKey(kkcfMap.entrySet());
				long  qzckCount=getValue(qzckMap.entrySet());
				long  kkcfCount=getValue(kkcfMap.entrySet());
				if(qzckCwb.equals(kkcfCwb)){
					if(kkcfCount-qzckCount!=1&&kkcfCount-qzckCount<2){ //如果扣款-强制出库<>1，则认为订单 多返款，需要减去
						needDiffMap.put(kkcfCwb, Math.abs(kkcfCount-qzckCount)+1);
					}
				}
			}
		}
		return needDiffMap;
	}
	private String getKey(Set<Map.Entry<String, Long>> qzckEntry) {
		for(Map.Entry<String, Long> entry:qzckEntry){
			return entry.getKey();
		}
		return null;
	}
	private long getValue(Set<Map.Entry<String, Long>> qzckEntry) {
		for(Map.Entry<String, Long> entry:qzckEntry){
			return entry.getValue();
		}
		return 0;
	}
	
	
	
	//构建中转站入库（未返款）2015-06-22修改暂时不查询，默认所有的中转站入库全部是记录返款表的
	private Map<String, TypeVo> buildAttrZhongZhuanZhanRukuWeiFanKuan(long deliverybranchid,long accountFlowType,String attrVo) {
//		List<String> zhongzhuanDect=finaceDAO.getDeductByType(deliverybranchid, accountFlowType);//中转返款表记录
//		
//		String zzBranchIds = getBranchListByZhongZhuan(); //得到所有中转订单
//		
//		TypeVo typeVo =  finaceDAO.getDetaiZhongZhuanNoBackAmount(deliverybranchid,this.getCwbs(zhongzhuanDect),zzBranchIds);
		Map<String,TypeVo> map5=setValue(AccountAttrVo.zhongzhuanzhanruku_weifankuan, 0, 0.00);
		return map5;
	}
	 
	private String getBranchListByZhongZhuan() {
		String zzBranchIds="";
		List<Branch> branchZZList=branchDAO.getBranchBySiteType(BranchEnum.ZhongZhuan.getValue()); //拿到所有中转站
		for(Branch b:branchZZList){
			zzBranchIds+=b.getBranchid()+",";
		}
		if(zzBranchIds.contains(",")){
			zzBranchIds=zzBranchIds.substring(0,zzBranchIds.length()-1);
		}
		return zzBranchIds;
	}
	
	
	

	public List<String> initAttrList() {
		List<String> list=new ArrayList<String>();
		list.add(AccountAttrVo.chongzhi);
		list.add(AccountAttrVo.tuotou);
		list.add(AccountAttrVo.tuotou_chayi);
		list.add(AccountAttrVo.diushi);
		list.add(AccountAttrVo.diushi_chayi);
		list.add(AccountAttrVo.zhongzhuan);
		list.add(AccountAttrVo.tuihuo);
//		list.add(AccountAttrVo.tuihuo_weifankuan);
		list.add(AccountAttrVo.weiwanjie);
		list.add(AccountAttrVo.tuihuozhanruku_weifankuan);
		list.add(AccountAttrVo.zhongzhuanzhanruku_weifankuan);
		list.add(AccountAttrVo.jsWTHCZAmount);
		list.add(AccountAttrVo.thczWrkAmount);
		list.add(AccountAttrVo.zhongzhuanchuzhan);
		return list;
	}
	
	/**
	 * 封装属性
	 * @param attribute
	 * @param num
	 * @param amount
	 * @return
	 */
	public Map<String,TypeVo>  setValue(String attribute,long num,double amount){
		Map<String,TypeVo> map=new HashMap<String, TypeVo>();
		map.put(attribute, new TypeVo(num,amount));
		return map;
	}
	private String getCwbs(List<String> cwblist) {
		String cwbArrs="'--'";
		StringBuffer  sub= new StringBuffer();
		for(String cwb:cwblist){
			sub.append("'"+cwb+"',");
		}
		if(sub.indexOf(",")>0){
			cwbArrs=sub.substring(0,sub.length()-1);
		}
		return cwbArrs;
	}
	
	private String getMapCwbs(List<Map<String,Long>> cwblist) {
		String cwbArrs="'--'";
		StringBuffer  sub= new StringBuffer();
		for(Map<String,Long> map:cwblist){
			for(String cwb:map.keySet()){
				sub.append("'"+cwb+"',");
			}
		}
		if(sub.indexOf(",")>0){
			cwbArrs=sub.substring(0,sub.length()-1);
		}
		return cwbArrs;
	}
	

}
