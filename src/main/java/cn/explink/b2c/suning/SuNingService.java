package cn.explink.b2c.suning;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.suning.requestdto.Body;
import cn.explink.b2c.suning.requestdto.Order;
import cn.explink.b2c.suning.requestdto.RequestBody;
import cn.explink.b2c.suning.requestdto.RequestData;
import cn.explink.b2c.suning.responsedto.ResponseBody;
import cn.explink.b2c.suning.responsedto.ResponseData;
import cn.explink.b2c.suning.responsedto.Result;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.service.CwbOrderService;
import cn.explink.util.DateDayUtil;
import cn.explink.util.StringUtil;
import cn.explink.util.MD5.MD5Util;
@Service
public class SuNingService {
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	DataImportService_B2c dataImportService_B2c;
	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	CwbOrderService cwbOrderService;
	
	private Logger logger =LoggerFactory.getLogger(this.getClass());
	public void update(int joint_num,int state){
		jiontDAO.UpdateState(joint_num, state);
	}
	/**
	 * 【苏宁易购】基础设置
	 * @param request
	 * @param joint_num
	 */
	public void edit(HttpServletRequest request,int joint_num){
		SuNing sn=new SuNing();
		String customerid=request.getParameter("customerid");
		sn.setCustomerid(customerid);
		sn.setImportUrl(request.getParameter("importUrl"));
		sn.setFeedbackUrl(request.getParameter("feedbackUrl"));
		String maxCount = StringUtil.nullConvertToEmptyString(request.getParameter("maxcount"));
		sn.setMaxcount(("".equals(maxCount))?0:(Long.valueOf(maxCount)));
		sn.setWarehouseid(Integer.valueOf(request.getParameter("warehouseid")));
		sn.setPrivate_key(request.getParameter("private_key"));
		sn.setEverythreaddonum(request.getParameter("everythreaddonum")==null?1:(Integer.valueOf(request.getParameter("everythreaddonum"))));
		sn.setSpCode(request.getParameter("spCode")==null?"":(request.getParameter("spCode")));
		String oldCustomerids = "";
		JSONObject jsonObj = JSONObject.fromObject(sn);
		JointEntity jointEntity = jiontDAO.getJointEntity(joint_num);
		if(jointEntity == null){
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Create(jointEntity);
		}else{
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Update(jointEntity);
		}
		//保存 枚举到供货商表中
		customerDAO.updateB2cEnumByJoint_num(customerid, oldCustomerids, joint_num);
	}
	/**
	 *获取创建电商的信息 
	 * @param key
	 * @return
	 */
	public SuNing getSuNing(int key) {
		if (getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
		SuNing suning = (SuNing) JSONObject.toBean(jsonObj, new SuNing().getClass());
		return suning;
	}
	public Object getObjectMethod(int key) {
		JointEntity obj = jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}
	
	/**
	 * 验证并处理【苏宁易购】请求参数
	 * @param params(苏宁易购请求参数)
	 * @return
	 */
	public String RequestOrdersToTMS(String params) throws Exception {
		ObjectMapper obj = JacksonMapper.getInstance();
		RequestData rd = obj.readValue(params, new RequestData().getClass());
		RequestBody body = rd.getBody();
		String bodyStr = obj.writeValueAsString(body);
		SuNing suning = getSuNing(B2cEnum.SuNing.getKey());
		String private_key = suning.getPrivate_key();
		String sign = MD5Util.md5(bodyStr+private_key);
		this.logger.info("用于加密的body字符串信息:{},密钥信息:{},苏宁加密信息:{},我方用于比对的加密信息:{}",new Object[]{bodyStr,private_key,rd.getContentMesDigest(),sign});
		//校验并进行数据返回
		return dealwithSuNingData(rd,body,suning,sign,obj);
			
	}
	
	//执行【苏宁易购】数据核心处理方法
	private String dealwithSuNingData(RequestData rd, RequestBody body,
			SuNing suning,String sign,ObjectMapper obj) throws Exception {
		ResponseData respda = new ResponseData();
		ResponseBody respbo = new ResponseBody();
		String failedReason = "";
		//验证加密信息是否正确
		/*if(!rd.getContentMesDigest().equals(sign)){
			failedReason = FailedReasonEnum.signfail.getFailmsg();
			respda.setSuccess("false");
			respda.setFailedReason("签名验证失败!");
		}*/
		//校验苏宁合作运营商名字是否正确
		if(!rd.getSpCode().equals(suning.getSpCode())){
			failedReason = FailedReasonEnum.spcodefail.getFailmsg();
			respda.setSuccess("false");
			respda.setFailedReason("合作运营商编码不对!");
		}
		
		//存储失效的结果
		List<Result> resultsShixiao = new ArrayList<Result>();
		
		//校验通过处理数据
		if("".equals(failedReason)){
			List<Map<String,String>> cwbList=new ArrayList<Map<String,String>>();
			List<String> strRealList = new ArrayList<String>();
			List<String> strList = new ArrayList<String>();
			List<Result> results = new ArrayList<Result>();
			
			List<Body> bodyList =  body.getPackages();
			//List<Order> orderList = body.getDeliveryOrders();
			Map<String,Body> sbMap = new HashMap<String, Body>();
			//遍历请求的订单
			for(Body bd : bodyList){
				String cwb = bd.getWork_id();
				strRealList.add(cwb);
				String receivablefee = bd.getOwn_money();
				if(!"".equals(bd.getWork_rela_id())){
					//根据作业标识查看是否之前存在（存在则修改当前代收款为0.000）
					//String dateStr = DateDayUtil.getDateBefore("",3);//获取3天前数据
					CwbOrderDTO codto = this.dataImportDAO_B2c.getCwbByremark4(bd.getWork_rela_id());
					if(codto!=null){
						receivablefee = "0.000";
					}else{
						if(sbMap.get(bd.getWork_rela_id())==null){
							sbMap.put(bd.getWork_rela_id(), bd);
						}else{
							receivablefee = "0.000";
						}
					}
				}
				
				Map<String,String> ssMap = new HashMap<String, String>();
				ssMap.put("transcwb", cwb);//未经处理的订单号(【苏宁易购】扫描单号)
				String cwbordertype = bd.getWork_type();
				if("01".equals(cwbordertype)){
					cwbordertype = ""+CwbOrderTypeIdEnum.Peisong.getValue();
					boolean result = cwb.matches("[0-9]+");//判断是否为纯数字
					if(result == true){//此时为纯数字(业务1只有纯数字时需要截取)
						int index = 0;
						//List<String> strList = new ArrayList<String>();
						//纯数字是进行截取=====直至第一个数为大于0的数
						index = getfirstindex(cwb);
						//截取获取第一位数字不为0的纯数字字符串
						cwb = cwb.substring(index, cwb.length());
					}
				}else if("02".equals(cwbordertype)){
					cwbordertype = ""+CwbOrderTypeIdEnum.Shangmentui.getValue();
					
				}
				
				//【苏宁易购】传递失效标识，失效当前的订单信息
				if(RequestWorkstatusEnum.shixiao.getMsg().equals(bd.getWork_status())){
					try{
						this.dataImportDAO_B2c.dataLoseB2ctempByCwb(cwb);
						this.cwbDAO.dataLoseByCwb(cwb);
						this.cwbOrderService.datalose_vipshop(cwb);
						//新加处理（失效订单信息先存到失效list中）最后和插入数据库的信息一起追加返回给【苏宁易购】
						Result reul = new Result();
						reul.setWork_id(bd.getWork_id());//接收过来的原生cwb
						reul.setReturn_code("S");
						reul.setReturn_message("订单失效成功");
						resultsShixiao.add(reul);
						
						//list中有一单或多单失效后，立即循环执行下一条订单信息
						continue;
					}catch(Exception e){
						this.logger.error("异常订单为:"+cwb+",异常原因:",e);
					}
					this.logger.info("【苏宁易购】订单失效,订单号:{}",cwb);
				}
				
				//在临时表查询，验证是否有重复订单数据
				CwbOrderDTO cwbOrder=dataImportDAO_B2c.getCwbByCwbB2ctemp(cwb);
				if(cwbOrder!=null){
					this.logger.warn("【苏宁易购】请求信息中含有重复数据cwb={}",cwb);
					//查重后循环下一条数据
					continue;
				}
				
				//验证【苏宁易购】数据通过后（去掉重复数据以及要失效数据）将实际导入临时表的cwb放入list
				strList.add(cwb);
				
				ssMap.put("cwb", cwb);
				ssMap.put("cwbordertypeid", cwbordertype);//订单类型（苏宁推送 01 送件，02 取件）
				ssMap.put("sendcarnum", "1");//发货件数(包裹数)
				ssMap.put("remark3", "商品数量【"+bd.getCommdty_num()+"】");//商品数量
				ssMap.put("sendcarname",bd.getCommdty_name());//发货名字
				ssMap.put("receivablefee", receivablefee);//代收货款
				ssMap.put("consigneename", bd.getCustome_name());//收件人姓名
				ssMap.put("consigneepostcode", bd.getPostal_code());//邮编
				String cwbprovince = bd.getProvince_name();
				ssMap.put("cwbprovince",cwbprovince);//收货人所在省份
				String cwbcity = bd.getCity_name();
				ssMap.put("cwbcity",cwbcity);//收货人所在城市
				String cwbcounty = bd.getArea_name();
				ssMap.put("cwbcounty",cwbcounty);//所在县城
				ssMap.put("consigneeaddress",cwbprovince+cwbcity+cwbcounty+bd.getAdress());//收货人详细地址
				ssMap.put("consigneemobile", bd.getMobilephone());//收货人手机号
				ssMap.put("cargorealweight", bd.getPack_weight());//包裹重量
				ssMap.put("carsize", bd.getPack_volume());//包裹体积，单位是立方厘米
				ssMap.put("customercommand",bd.getMark());//客户要求
				ssMap.put("remark1", bd.getWeak_id());//weak_id(所属物流中心)
				ssMap.put("remark2", bd.getZex_id());//zex_id(所属K点)
				ssMap.put("remark4", bd.getWork_rela_id());//作业绑定标识(不为空时多个相同时只扣一次钱)
				String payway = bd.getPay_type();
				if("02".equals(payway)){
					payway = PaytypeEnum.Pos.getValue()+"";
				}else if("03".equals(payway)){
					payway = PaytypeEnum.Xianjin.getValue()+"";
				}else if("08".equals(payway)){
					payway = PaytypeEnum.CodPos.getValue()+"";
				}else{
					payway = PaytypeEnum.Qita.getValue()+"";
				}
				ssMap.put("paywayid",payway);//支付方式（苏宁推送  02 pos，03 现金，08 扫码支付 ）
				cwbList.add(ssMap);
			}
			//【苏宁易购】筛选后需要插入临时表中的未经截取的订单数据
			List<String> strrealLast = new ArrayList<String>();
			for(String strReal : strRealList){
				for(String str : strList){
					if(strReal.contains(str)){
						strrealLast.add(strReal);
					}
				}
			}
			long warehouseid=suning.getWarehouseid();  //订单导入的库房Id
			results = dataImportService_B2c.suning_DataDealByB2c(Long.valueOf(suning.getCustomerid()),B2cEnum.SuNing.getMethod(),cwbList,warehouseid,true,respda,strrealLast);
			respbo.setResults(results);
		}
		//失效的订单在这里遍历添加到总的返回信息中
		if(resultsShixiao.size()>0){
			for(Result rsl : resultsShixiao){
				respbo.getResults().add(rsl);
			}
		}
		
		respda.setBody(respbo);
		respda.setSpCode(rd.getSpCode());
		String responsebody = obj.writeValueAsString(respbo);
		respda.setContentMesDigest(MD5Util.md5(responsebody+suning.getPrivate_key()));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		respda.setTime(sdf.format(new Date()));//
		//如果success字段等于null，则说明插入临时表没有报异常
		if(respda.getSuccess()==null){
			respda.setSuccess("true");
			respda.setFailedReason("");
		}
		String respdaStr = obj.writeValueAsString(respda);
		this.logger.info("返回给【苏宁易购】的信息:{}",respdaStr);
		return respdaStr;
	}
	
	/**
	 * 通过输入字符串(纯数字)获取第一个不为0的索引位置
	 * @param str （请求参数）
	 * @return 第一个大于0的索引位置
	 */
	public Integer getfirstindex(String str){
		Integer inte = 0;
		char[] bytes = str.toCharArray();
		for(int i=0;i<bytes.length;i++){
			char strbyte =bytes[i];
			if(strbyte!='0'){
				inte = i;
				return inte;
			}
		}
		return inte;
	}
	
}
