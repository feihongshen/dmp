package cn.explink.b2c.meilinkai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONObject;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.codehaus.xfire.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.dao.CustomerDAO;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.util.StringUtil;

import com.sun.xml.ws.api.message.Message;

@Service
public class MLKService {
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	DataImportService_B2c dataImportService_B2c;
	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;
	
	private static Logger logger = LoggerFactory.getLogger(MLKService.class);
    private static Map<String,Client> clientMap = new HashMap<String, Client>();
   
    /**
     * 【玫琳凯】开关开启与关闭
     * @param joint_num
     * @param state
     */
	public void update(int joint_num,int state){
		this.jiontDAO.UpdateState(joint_num, state);
	}
	/**
	 * 【玫琳凯】页面基础设置创建（以及更新）
	 * @param request
	 * @param joint_num
	 */
	public void edit(HttpServletRequest request,int joint_num){
		MeiLinKai meilinkai = new MeiLinKai();
		meilinkai.setUsrename(StringUtil.nullConvertToEmptyString(request.getParameter("usrename")));
		meilinkai.setPwd(StringUtil.nullConvertToEmptyString(request.getParameter("pwd")));
		meilinkai.setCheckUsermethod(StringUtil.nullConvertToEmptyString(request.getParameter("checkUsermethod")));
		meilinkai.setHdtolipsmethod(StringUtil.nullConvertToEmptyString(request.getParameter("hdtolipsmethod")));
		String customerid = StringUtil.nullConvertToEmptyString(request.getParameter("customerid"));
		meilinkai.setCustomerid(customerid);
		meilinkai.setMaxCount(StringUtil.nullConvertToEmptyString(request.getParameter("maxCount")));
		meilinkai.setHdtolipsUrl(StringUtil.nullConvertToEmptyString(request.getParameter("hdtolipsUrl")));
		meilinkai.setLipstohdUrl(StringUtil.nullConvertToEmptyString(request.getParameter("lipstohdUrl")));
		meilinkai.setShipJDECarrierNum(StringUtil.nullConvertToEmptyString(request.getParameter("shipJDECarrierNum")));
		meilinkai.setWarehouseid(request.getParameter("warehouseid")==null?0:(Integer.valueOf(request.getParameter("warehouseid"))));
		String oldCustomerids = "";
		JSONObject jsonObj = JSONObject.fromObject(meilinkai);
		JointEntity jointEntity = this.jiontDAO.getJointEntity(joint_num);
		if(jointEntity == null){
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			this.jiontDAO.Create(jointEntity);
		}else{
			try {
				oldCustomerids = getMLK(joint_num).getCustomerid();
			    } catch (Exception e) {
			    }
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			this.jiontDAO.Update(jointEntity);
		}
		//保存 枚举到供货商表中
		this.customerDAO.updateB2cEnumByJoint_num(customerid, oldCustomerids, joint_num);
	}
    
    /**
     * 获取【玫琳凯】基础配置
     * @param yhd_key
     * @return
     */
    public MeiLinKai getMLK(int yhd_key) {
		if (getObjectMethod(yhd_key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(yhd_key));
		return (MeiLinKai) JSONObject.toBean(jsonObj, MeiLinKai.class);
	}
	public Object getObjectMethod(int key) {
		JointEntity obj = this.jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}
    
	/**
	 * 【玫琳凯】
	 * 调用webService获取订单信息
	 */
	public void getOrderData(){
		//获取请求【玫琳凯】url地址
	    MeiLinKai meilinkai = getMLK(B2cEnum.meilinkai.getKey())==null?(new MeiLinKai()):(getMLK(B2cEnum.meilinkai.getKey()));
	    String lipstohdUrl = meilinkai.getHdtolipsUrl()==null?"":(meilinkai.getLipstohdUrl());
	    String username = meilinkai.getUsrename();
	    String pwd = meilinkai.getPwd();
	    try{
		    //1.校验用户登录是否成功
		    Object[] objUserResult = sendWebService("CheckUser",lipstohdUrl,username,pwd);
		    logger.info("【玫琳凯】用户校验返回结果:{}",Arrays.toString(objUserResult));
		    if(((Integer)objUserResult[0])==-2){
			   logger.info("【玫琳凯】用户校验失败,用户名:{},密码:{}",username,pwd);
			   return;
		    }
		    //2.校验通过后，获取订单列表
		    Object[] objectOrderListresult = sendWebService("GetOrderList",lipstohdUrl);
		    logger.info("【玫琳凯】获取订单编号列表返回结果:{}",Arrays.toString(objectOrderListresult));
		    if(objectOrderListresult.length==1&&("-3".equals((String)objectOrderListresult[0]))){
			   logger.info("【玫琳凯】获取订单编号列表失败");
			   return;
		    }
		    //3.根据返回的订单编号列表进行请求获取订单明细信息
		    for(String str : (String[])objectOrderListresult ){
		    	String[] strArray = str.split("-");
		    	String orderNumJDE = strArray[0];
		    	String orderNumJDESeq = strArray[1];
		    	Object[] orders = sendWebService("GetOrderList",lipstohdUrl,orderNumJDE,orderNumJDESeq);
		    	logger.info("【玫琳凯】获取订单编号列表返回结果:{}",Arrays.toString(orders));
		    	if(orders.length==0){
		    		logger.info("【玫琳凯】请求orderNumJDE:{},orderNumJDESeq:{}",orderNumJDE,orderNumJDESeq);
		    		continue;
		    	}
		    	String cwb= ((OrderData)(orders[0])).getOrderDeliveryInstruct2();
		    	List<Map<String,String>> maplist = builddataList(orders[0]);
		    	if(maplist.size()==0){
		    		OrderData od =  (OrderData)(orders[0]);
					logger.warn("【玫琳凯】封装数据失败,当前订单(玫琳凯运单号):{}",cwb);
					continue;
				}
		    	long warehouseid = meilinkai.getWarehouseid();//订单导入的库房Id
		    	try{
		    		this.dataImportService_B2c.analizy_DataDealByMLK(Long.valueOf(meilinkai.getCustomerid()),B2cEnum.meilinkai.getMethod(), maplist,warehouseid,true);
		    		//4.【玫琳凯】更新状态为成功
		    		Object[] updateResult = sendWebService("UpdateDownLoadFlag",lipstohdUrl,orderNumJDE,orderNumJDESeq);
		    		if("-1".equals(updateResult[0])){
		    			//玫琳凯修改订单标记时发生错误（此时临时表无法推送到主表：改为已推送到主表状态，实际未推送，目的不让导入主表）
		    			this.dataImportDAO_B2c.updateGetdataflagByCWB(cwb);
		    			logger.info("【玫琳凯】订单标记时发生错误订单无法导入主表,订单号:{}",cwb);
		    		}else{
		    			logger.info("【玫琳凯】订单导入成功");
		    		}
		    	}catch(Exception e){
		    		logger.error("订单处理异常,订单号"+cwb+",原因:{}",e);
		    	}
		    }
	    }catch(Exception e){
		    logger.error("【玫琳凯】webService处理异常,原因:{}",e);
	    }
	}
	
	public List<Map<String,String>> builddataList(Object obj){
		List<Map<String,String>> mapList = new ArrayList<Map<String,String>>();
		
		Map<String,String> strMap = new HashMap<String, String>();
		OrderData oda = (OrderData)obj;
		strMap.put("cwb", oda.getOrderDeliveryInstruct2());//运单号（扫描编号）
		strMap.put("transcwb", oda.getOrdernumJDTandSEQ());//JDE+SEQ
		strMap.put("cwbordertypeid", CwbOrderTypeIdEnum.Peisong.getValue()+"");//【玫琳凯】只有配送单
		strMap.put("sendcarnum",oda.getOrderContainer());//箱数（仅仅描述商品数量）
		strMap.put("receivablefee","0.00");//【玫琳凯】没有代收款
		strMap.put("consigneename",oda.getShipToName());//收件人姓名
		strMap.put("consigneepostcode",oda.getShipToZipCode());//目的地邮编
		strMap.put("cwbprovince",oda.getShipToState());//目的省份
		strMap.put("cwbcity",oda.getShipToCity());//目的城市
		strMap.put("cwbcounty",oda.getShipToCounty());//目的县城
		strMap.put("consigneeaddress",oda.getShipToAddressLine1()+oda.getShipToAddressLine2());//收件人详细地址
		strMap.put("consigneemobile",oda.getShipToAddressLine3());//收件人电话
		strMap.put("cargorealweight",oda.getOrderActualWeight());//重量
		strMap.put("remark1","订单金额:"+oda.getOrderTotal());//报价金额，原则上等于订单金额，当订单金额为0或-2订单时，报价金额为1
		strMap.put("remark2","网上订单号:"+oda.getOrderNumCorporate());//网上订单号
		mapList.add(strMap);
		return mapList;
	}
	
	
    
    /**
     * 发送WebService请求
     * 【玫琳凯】server
     * 【飞远】client
     * ----客户端请求----
     * @param service
     *            请求的服务<p>
     * @param method
     *            请求的方法<p>
     * @param param
     *            方法参数<p>
     */
    @SuppressWarnings("rawtypes")
    public Object[] sendWebService(String method,String url,Object... param) {
        Long startTime = System.currentTimeMillis();
        logger.info("WebService请求地址:"+url+",请求方法:"+method+",请求请求参数:"+Arrays.toString(param));
        ArrayList<Interceptor<? extends Message>> list = new ArrayList<Interceptor<? extends Message>>();
        // 添加soap消息日志打印
        list.add(new LoggingOutInterceptor());
         
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
         
        Object clObj = clientMap.get(url);
        Client client = null;
        //这里使用map 可将第一次请求的Client 缓存在本地，第二次请求的时候可直接使用，大大加快的请求速度。
        if (clObj == null) {
            client = (Client) dcf.createClient(url);
            clientMap.put(url, client);
        } else {
            client = (Client)clObj;
        }
        //Client client = dcf.createClient(url);
        HTTPConduit http = (HTTPConduit) ((org.apache.cxf.endpoint.Client) client).getConduit();        
        HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();        
        httpClientPolicy.setConnectionTimeout(3000);  //连接超时      
        httpClientPolicy.setAllowChunking(false);    //取消块编码   
        httpClientPolicy.setReceiveTimeout(30000);   //响应超时  
        http.setClient(httpClientPolicy);  
         
        ((InterceptorProvider) client).getOutInterceptors().addAll(list);
        try {
            //调用方法
            Object[] objs = client.invoke(method, param);
            for(Object obj :objs) {
            	logger.info("webService接收值："+obj);
            }
            Long endTime = System.currentTimeMillis();
            logger.info("调用webService方法["+method+"]用时："+(endTime-startTime)+"毫秒");
            return objs;
        } catch (Exception e) {
        	logger.debug("webService方法调用错误："+e.getMessage());
            return new Object[]{"webService调用异常"};
        }
    }
    
    
}
