package cn.explink.b2c.jiuye;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.jiuye.jsondto.JiuYe_request;
import cn.explink.b2c.jiuye.jsondto.JiuYe_response;
import cn.explink.b2c.jiuye.jsondto.SubCode;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.CustomerDAO;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.service.CustomerService;
import cn.explink.util.B2cUtil;
import cn.explink.util.StringUtil;
import cn.explink.util.MD5.MD5Util;

@Service
public class JiuYeService {
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	DataImportService_B2c dataImportService_B2c;
	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	B2cUtil b2cUtil;
	@Autowired
	CustomerService customerService;
	
	private Logger logger =LoggerFactory.getLogger(JiuYe.class);
	public void update(int joint_num,int state){
		this.jiontDAO.UpdateState(joint_num, state);
	}
	@Transactional
	public void edit(HttpServletRequest request,int joint_num){
		JiuYe dms=new JiuYe();
		String customerid=request.getParameter("customerid");
		dms.setCustomerid(customerid);
		dms.setFeedbackUrl(request.getParameter("feedbackUrl"));
		dms.setImportUrl(request.getParameter("importUrl"));
		String maxCount = StringUtil.nullConvertToEmptyString(request.getParameter("maxCount"));
		dms.setMaxCount(("".equals(maxCount))?0:(Integer.valueOf(request.getParameter("maxCount"))));
		dms.setWarehouseid(Integer.valueOf(request.getParameter("warehouseid")));
		dms.setPrivate_key(request.getParameter("private_key"));
		dms.setDmsCode(request.getParameter("dmsCode"));
		dms.setB2cenum(joint_num);
		String oldCustomerids = "";
		
		JSONObject jsonObj = JSONObject.fromObject(dms);
		JointEntity jointEntity = this.jiontDAO.getJointEntity(joint_num);
		if(jointEntity == null){
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			this.jiontDAO.Create(jointEntity);
		}else{
			try {
				oldCustomerids = this.b2cUtil.getViewBean(joint_num,new JiuYe().getClass()).getCustomerid();
			} catch (Exception e) {
				this.logger.error("解析【九曳】基础设置异常,原因:{}",e);
			}
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			this.jiontDAO.Update(jointEntity);
		}
		//保存 枚举到供货商表中
		this.customerDAO.updateB2cEnumByJoint_num(customerid, oldCustomerids, joint_num);
		this.customerService.initCustomerList();
	}
	
	/**
	 * 处理【九曳】请求主方法
	 * @param jiuyeReq
	 * @param jiuye
	 * @return
	 */
	public String RequestOrdersToTMS(JiuYe_request jiuyeReq,JiuYe jiuye) {
		try {
			
			if(!jiuyeReq.getRequestName().equals("RequestOrdersToTMS")){
				this.logger.info("订单号:{},requestName:{}",jiuyeReq.getContent().getWorkCode(),jiuyeReq.getRequestName());
				return responseJson(jiuyeReq.getRequestName(), "102", false, "RequestName值不正确",0);
			}
			
			String localSign=MD5Util.md5(jiuyeReq.getTimeStamp()+jiuye.getPrivate_key());
			if(!localSign.equalsIgnoreCase(jiuyeReq.getSign())){
				return responseJson(jiuyeReq.getRequestName(), "101", false, "sign不正确",0);
			} 
			
			List<Map<String,String>> orderlist=parseCwbArrByOrderDto(jiuyeReq,jiuye);
			if(orderlist==null||orderlist.size()==0){
				logger.warn("九曳-请求没有封装参数，订单号可能为空");
				return responseJson(jiuyeReq.getRequestName(), "0", true, "运单重复",0);
			}

			long warehouseid=jiuye.getWarehouseid();  //订单导入的库房Id
			dataImportService_B2c.Analizy_DataDealByB2c(Long.valueOf(jiuye.getCustomerid()),B2cEnum.JiuYe1.getMethod(), orderlist,warehouseid,true);
			logger.info("九曳-订单导入成功");
			return responseJson(jiuyeReq.getRequestName(), "0", true, "成功",0);
		} catch (Exception e) {
			logger.error("九曵-异常",e);
			return responseJson("RequestOrdersToTMS", "100", false, "RequestName值不正确",0);
		}
	}
	
	/**
	 * 返回一个转化为导入接口可识别的对象
	 */
	private List<Map<String,String>> parseCwbArrByOrderDto(JiuYe_request dmsOrder,JiuYe jiuye) {
		List<Map<String,String>> cwbList=new ArrayList<Map<String,String>>();
		if(dmsOrder!=null){
			CwbOrderDTO cwbOrder=dataImportDAO_B2c.getCwbByCwbB2ctemp(dmsOrder.getContent().getWorkCode());
			if(cwbOrder!=null){
				logger.warn("获取九曳订单中含有重复数据cwb={}",dmsOrder.getContent().getWorkCode());
				return null;
			}
			String customercommand = "";
			int scheduleType = dmsOrder.getContent().getScheduleType();
			if(scheduleType==1){
				customercommand = "工作日";
			}else if(scheduleType==2){
				customercommand = "节假日";
			}else if(scheduleType==101){
				customercommand = "当日达";
			}else if(scheduleType==102){
				customercommand = "次晨达";
			}else if(scheduleType==103){
				customercommand = "次日达";
			}
			customercommand+=","+dmsOrder.getContent().getScheduleStart()+"~"+dmsOrder.getContent().getScheduleEnd();
			
			//运单类型
			//String remark1 = null;
			String cwbordertypeid="";
			int worktype = dmsOrder.getContent().getWorkType();
			if(worktype==0){
				cwbordertypeid=CwbOrderTypeIdEnum.Peisong.getValue()+"";
			}else if(worktype==1){
				cwbordertypeid=CwbOrderTypeIdEnum.Shangmenhuan.getValue()+"";
			}else if(worktype==2){
				cwbordertypeid=CwbOrderTypeIdEnum.Shangmentui.getValue()+"";
				//remark1 = "订单类型为2时必选！";
			}
			
			//退单是否回收发票
			String remark3 = "";
			int isRecInvoices = dmsOrder.getContent().getIsRecInvoices();
			if(isRecInvoices==0){
				remark3="不回收";
			}else if(isRecInvoices==1){
				remark3="回收";
			}
			
			
			String remark4 = "";
			int isIimitSchedule = dmsOrder.getContent().getIsIimitSchedule();
			if(isIimitSchedule==0){
				remark4 = "限时";
			}else if(isIimitSchedule==1){
				remark4 = "不限时";
			}
			
			Map<String,String> cwbMap=new HashMap<String,String>();
			cwbMap.put("cwb",dmsOrder.getContent().getWorkCode());   //cwb 订单号
			List<SubCode> subCodeList = dmsOrder.getContent().getSubCodeList();
			String str = "";
			if(subCodeList!=null&&!subCodeList.isEmpty()){
				for(SubCode sc : subCodeList){
					String scStr = sc.getSubCode();
					str += scStr+",";
				}
			}
			if(str.length()>0){
				str = str.substring(0,str.length()-1);
			}
			
			if("".equals(str)){
				str = dmsOrder.getContent().getWorkCode();
			}
			cwbMap.put("transcwb", str);  //transcwb 客户单号(一票多件)
			cwbMap.put("remark5", dmsOrder.getContent().getClientCode());//(九曳自身需要处理的订单号)
			cwbMap.put("remark1",dmsOrder.getContent().getrWorkCode());//退货号
			cwbMap.put("remark2",dmsOrder.getContent().getHyWorkCode());//原运单号
			cwbMap.put("cwbordertypeid", cwbordertypeid);// 1配送，2上门退，3上门换
			cwbMap.put("sendcarnum", dmsOrder.getContent().getOrderCount());//运单数量
			cwbMap.put("receivablefee", dmsOrder.getContent().getReplCost().toString());//应收金额
			cwbMap.put("remark3", remark3);//货单是否回收发票   0 ：不回收 1：回收
			cwbMap.put("consigneename", dmsOrder.getContent().getGetPerson());//收货人名称
			cwbMap.put("consigneepostcode", dmsOrder.getContent().getGetZip());//收货人邮编
			String province = dmsOrder.getContent().getGetProvince()==null?"":dmsOrder.getContent().getGetProvince();
			String city = dmsOrder.getContent().getGetCity()==null?"":dmsOrder.getContent().getGetCity();
			String county = dmsOrder.getContent().getGetCounty()==null?"":dmsOrder.getContent().getGetCounty();
			String address = dmsOrder.getContent().getGetAddress()==null?"":dmsOrder.getContent().getGetAddress();
			String alladdress = province+city+county+address;
			cwbMap.put("cwbprovince",province);//收货人所在省份
			cwbMap.put("cwbcity",city);//收货人所在城市
			cwbMap.put("cwbcounty",county);//所在县城
			cwbMap.put("consigneeaddress",alladdress);//收货人详细地址
			cwbMap.put("consigneemobile", dmsOrder.getContent().getGetPhone());//收货人手机号
			cwbMap.put("consigneephone", dmsOrder.getContent().getGetTel());//收货人座机
			cwbMap.put("cargorealweight", dmsOrder.getContent().getPackageHav().toString());//包裹重量
			cwbMap.put("carsize", dmsOrder.getContent().getPackageHav().toString());//包裹体积，单位是立方厘米
			cwbMap.put("remark4", remark4);//是否限时配送  0 限时 1不限时
			cwbMap.put("customercommand",customercommand);//投递时延要求
			String sendcarname="";
			double cargoAmount=0;
			cwbMap.put("sendcarname",sendcarname); //发货货物名称
			cwbMap.put("caramount",String.valueOf(cargoAmount) );
			
			cwbList.add(cwbMap);
		}
		return cwbList;
	}

	public String responseJson(String requestName, String code,boolean success,String msg,int count){
		JiuYe_response resonse=new JiuYe_response();
		resonse.setRequestName(requestName);
		resonse.setCode(code);
		resonse.setContent(null);
		resonse.setPartner("");
		resonse.setSuccess(success);
		resonse.setMsg(msg);
		resonse.setCount(count);
		String returnStr=null;
		try { 
			 returnStr=JacksonMapper.getInstance().writeValueAsString(resonse);
			 logger.info("九曵-订单下发回传 returnstr={}",returnStr);
//			 RestHttpServiceHanlder.sendHttptoServer(returnStr,jiuye.getFeedbackUrl());
		} catch (Exception e) {
			logger.error("九曵-json转化异常",e);
		} 
		
		return returnStr;
	}
	
}
