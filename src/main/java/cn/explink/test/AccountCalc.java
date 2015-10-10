package cn.explink.test;
import java.util.ArrayList;
import java.util.List;
  
public class AccountCalc {
 
	      
	    public static void main(String[] args) {
	    	List<TypeVo> tList=new ArrayList<TypeVo>();
	    	TypeVo v1=new TypeVo();
	    	v1.setCwb("111");
	    	v1.setAmount(5);
	    	tList.add(v1);
	    	
	    	TypeVo v2=new TypeVo();
	    	v2.setCwb("222");
	    	v2.setAmount(10);
	    	tList.add(v2);
	    	
	    	TypeVo v3=new TypeVo();
	    	v3.setCwb("33");
	    	v3.setAmount(15);
	    	tList.add(v3);
	    	
	    	TypeVo v4=new TypeVo();
	    	v4.setCwb("44");
	    	v4.setAmount(50);
	    	tList.add(v4);
	    	
	    	String cwbs=getCalcDiff(30, tList);
	    	 List<String> cwbList= new ArrayList<String>();
	    	for(String cwb:cwbs.split(",")){
	    		cwbList.add(cwb);
	    	}
	    	System.out.println("===========================");
	    	for(String cw1:cwbList){
	    		System.out.println(cw1);
	    	}
	    }

	    /**
	     * 传入一组订单list返回 一组等于订单结果的数据
	     * @param totalAmount
	     * @param tList
	     * @return
	     */
	    public static  List<String> getCalcDiffList(double totalAmount,List<TypeVo> tList) {
	    	if(totalAmount==0){
	    		return null;
	    	}
	    	String cwbs=getCalcDiff(totalAmount, tList);
	    	if(cwbs==null){
	    		return null;
	    	}
	    	List<String> cwbList= new ArrayList<String>();
	    	for(String cwb:cwbs.split(",")){
	    		cwbList.add(cwb);
	    	}
	    	return cwbList;
	    }
	    /**
	     * 传入list和总额，返回对应的订单号
	     * @param totalAmount
	     * @param tList
	     * @return
	     */
		public static  String getCalcDiff(double totalAmount,List<TypeVo> tList) {
	       
	        for (int i = 1; i < 1 << tList.size(); i++) {
	            int sum = 0;
	            StringBuffer sb = new StringBuffer();
	            for (int j = 0; j < tList.size(); j++) {
	            	
	                if ((i & 1 << j) != 0) { 
	                    sum += tList.get(j).getAmount();
	                    sb.append(tList.get(j).getCwb()).append(",");
	                }
	            }
	            if (sum == totalAmount) {
	            	System.out.println(sb.toString());
	            	return sb.toString();
	            }
	        }
	        return null;
		}
	    
	   
	 
}
