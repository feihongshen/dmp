package cn.explink.b2c.feiniuwang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import net.sf.json.JSONObject;
import cn.explink.b2c.feiniuwang.CwbResponse;
import cn.explink.b2c.feiniuwang.FeiNiuRequest;
import cn.explink.b2c.feiniuwang.FeiNiuWang;
import cn.explink.b2c.feiniuwang.Items;
import cn.explink.b2c.feiniuwang.Responseitems;
import cn.explink.b2c.feiniuwang.MD5Util;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.CustomerDAO;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.pos.tools.JacksonMapper;

@Service
public class FNWService {
	
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	DataImportService_B2c dataImportService_B2c;
	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;
	private Logger logger =LoggerFactory.getLogger(FNWService.class);
	
	public void update(int joint_num,int state){
		jiontDAO.UpdateState(joint_num, state);
	}
	
	public void edit(HttpServletRequest request,int joint_num){
		FeiNiuWang dms=new FeiNiuWang();
		String customerid=request.getParameter("customerid");
		dms.setCustomerid(customerid);
		dms.setFeedbackUrl(request.getParameter("feedbackUrl"));
		dms.setMaxCount(request.getParameter("maxCount"));
		dms.setWarehouseid(Integer.valueOf(request.getParameter("warehouseid")));
		dms.setRequestKey(request.getParameter("requestKey"));
		dms.setResponseKey(request.getParameter("responseKey"));
		dms.setDmsCode(request.getParameter("dmsCode"));
		String oldCustomerids = "";
		
		JSONObject jsonObj = JSONObject.fromObject(dms);
		JointEntity jointEntity = jiontDAO.getJointEntity(joint_num);
		if(jointEntity == null){
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Create(jointEntity);
		}else{
			try {
				oldCustomerids = getFeiniuwang(joint_num).getCustomerid();
			    } catch (Exception e) {
			    }
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Update(jointEntity);
		}
		//保存 枚举到供货商表中
			customerDAO.updateB2cEnumByJoint_num(customerid, oldCustomerids, joint_num);
	}

	public FeiNiuWang getFeiniuwang(int key) {
		if (getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
		FeiNiuWang feiniuwang = (FeiNiuWang) JSONObject.toBean(jsonObj, FeiNiuWang.class);
		return feiniuwang;
	}

	private Object getObjectMethod(int key) {
		JointEntity obj = jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}
	
	
	//处理电商请求推送过来的报文方法
	public String requestOrdersToTMS(FeiNiuRequest feiniuReq,String logistics_interface) {
		try {
			
			FeiNiuWang feiniuwang = getFeiniuwang(B2cEnum.Feiniuwang.getKey());
			
			if(logistics_interface==null){
				logger.info("非法的json格式,参数为={}",logistics_interface);
				return respJson(feiniuReq,"false",ErrorInfoEnum.geshi.getReason());
			}
			String key = feiniuwang.getRequestKey();
			String sign = MD5Util.doSign(logistics_interface,key,"UTF-8");
			
			logger.info("请求的json crjson={},秘钥信息key={},加密结果sign={}",new Object[]{logistics_interface,key,sign});
			
			String requestSign = feiniuReq.getData_digest();
			/*if(!sign.equals(requestSign)){
				logger.info("非法的数字签名,签名为={}",requestSign);
				return respJson(feiniuReq,"false",ErrorInfoEnum.qianming.getReason());
			}*/
			
			List<Map<String,String>> orderlist=parseCwbArrByOrderDto(feiniuReq,feiniuwang);
			if(orderlist==null||orderlist.size()==0){
				logger.warn("飞牛网(http)-请求没有封装参数，订单号可能为空");
				return null;
			}
			long warehouseid=feiniuwang.getWarehouseid();  //订单导入的库房Id
			dataImportService_B2c.Analizy_DataDealByB2c(Long.valueOf(feiniuwang.getCustomerid()),B2cEnum.Feiniuwang.getMethod(), orderlist,warehouseid,true);
			logger.info("飞牛网(http)-订单导入成功");
			return "飞牛网(http)订单导入成功！";
		} catch (Exception e) {
			logger.error("飞牛网(http)-异常",e);
			return "飞牛网(http)-异常！";
		}
	}
	
	private List<Map<String, String>> parseCwbArrByOrderDto(
			FeiNiuRequest feiniuReq, FeiNiuWang feiniuwang) {
		
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		CwbOrderDTO cwbOrder=dataImportDAO_B2c.getCwbByCwbB2ctemp(feiniuReq.getLogistics_interface().getMailno());
		if(cwbOrder!=null){
			logger.warn("获取飞牛网(http)订单中含有重复数据cwb={}",feiniuReq.getLogistics_interface().getTxlogisticid());
			return null;
		}
		Map< String, String> map = new HashMap<String, String>();
		map.put("remark1", feiniuReq.getLogistics_interface().getEccompanyid());//电商标识
		map.put("remark2", feiniuReq.getLogistics_interface().getLogisticproviderid());//物流公司代码
		map.put("customercommand", "往飞犇D3系统下单的发件客户:"+feiniuReq.getLogistics_interface().getCustomerid());//客户标识
		map.put("cwb", feiniuReq.getLogistics_interface().getMailno());
		map.put("transcwb", feiniuReq.getLogistics_interface().getTxlogisticid());
		String othersite = feiniuReq.getLogistics_interface().getOthersite()==null?"":feiniuReq.getLogistics_interface().getOthersite();
		String weight = feiniuReq.getLogistics_interface().getWeight()==null?"":feiniuReq.getLogistics_interface().getWeight();
		if(othersite.equals("1")){
			map.put("cargorealweight", weight);//重量
		}else if(othersite.equals("0")){
			map.put("cargorealweight", "0");//重量
			map.put("cwbremark", weight);//当标识为子单时将重量存储到订单备注中
		}else{
			map.put("cargorealweight", "0.000");//重量
			map.put("cwbremark", weight);//当无法区分主单,子单时将重量存储到订单备注中
		}
		
//		map.put("",feiniuReq.getLogistics_interface().getTradeno());//业务交易号
		map.put("remark3", feiniuReq.getLogistics_interface().getReturnno());//返单号
		int ordertype = Integer.valueOf(feiniuReq.getLogistics_interface().getOrdertype())==null?0:Integer.valueOf(feiniuReq.getLogistics_interface().getOrdertype());
		if(ordertype==2){
			ordertype = CwbOrderTypeIdEnum.Shangmentui.getValue();
		}
		if(ordertype==1){//配送单
			ordertype = CwbOrderTypeIdEnum.Peisong.getValue();
		}
		map.put("cwbordertypeid",String.valueOf(ordertype));//订单类型
		map.put("remark5",feiniuReq.getLogistics_interface().getServicetype());
		
		//收件人个人信息归总
		String name = feiniuReq.getLogistics_interface().getReceiver().getName()==null?"":feiniuReq.getLogistics_interface().getReceiver().getName();
		String postcode = feiniuReq.getLogistics_interface().getReceiver().getPostcode()==null?"":feiniuReq.getLogistics_interface().getReceiver().getPostcode();
		String phone = feiniuReq.getLogistics_interface().getReceiver().getPhone()==null?"":feiniuReq.getLogistics_interface().getReceiver().getPhone();
		String mobile = feiniuReq.getLogistics_interface().getReceiver().getMobile()==null?"":feiniuReq.getLogistics_interface().getReceiver().getMobile();
		String prov = feiniuReq.getLogistics_interface().getReceiver().getProv()==null?"":feiniuReq.getLogistics_interface().getReceiver().getProv();
		String city = feiniuReq.getLogistics_interface().getReceiver().getCity()==null?"":feiniuReq.getLogistics_interface().getReceiver().getCity();
		String address = feiniuReq.getLogistics_interface().getReceiver().getAddress()==null?"":feiniuReq.getLogistics_interface().getReceiver().getAddress();
		String mainAddress = prov+city+address;
		map.put("consigneename", name);
		map.put("consigneepostcode", postcode);//邮编
		map.put("consigneephone", phone);
		map.put("consigneemobile",mobile);
		map.put("cwbprovince",prov);
		map.put("cwbcity", city);
		map.put("consigneeaddress",mainAddress);
		
	//		String date = feiniuReq.getLogistics_interface().getCreateordertime().toString();
	//		map.put("emaildateid", date);
		String goodsvalue = feiniuReq.getLogistics_interface().getGoodsvalue()==null?"":feiniuReq.getLogistics_interface().getGoodsvalue();
		map.put("caramount",goodsvalue);//货物金额
		String itemsvalue = feiniuReq.getLogistics_interface().getItemsvalue()==null?"":feiniuReq.getLogistics_interface().getItemsvalue();
		map.put("receivablefee",itemsvalue);//代收货款
		String itemname = "";
		String number = "";
		String itemvalue = "";
		String packagenumber = "";
		String itemservicetype = "";
		List<Items> items = feiniuReq.getLogistics_interface().getItems();
		if(items!=null){
			for(Items item:items){
				itemname += item.getItemname()+",";//商品名称
				number += String.valueOf(item.getNumber())+",";//商品数量
				itemvalue += String.valueOf(item.getItemvalue())+",";//商品单价
				packagenumber = String.valueOf(item.getPackagenumber());
				itemservicetype = item.getItemservicetype();
			}
		}
		String itemsname = "";
		String itemvalues = "";
		if(!itemname.equals("")&&!itemvalue.equals("")){
			itemsname = itemname.substring(0, itemname.length()-1);
			itemvalues = itemvalue.substring(0, itemvalue.length()-1);
		}
		map.put("sendcarname", itemsname);//发出商品名称
		map.put("sendcarnum", "1");//发出商品数量
		map.put("remark4", itemvalues);//商品单价（货物金额）
	//		map.put("", packagenumber);//家居包件数
	//		map.put("",itemservicetype);//家居服务类型
		String paytype = feiniuReq.getLogistics_interface().getPaytype()==""?"1":feiniuReq.getLogistics_interface().getPaytype();
		int paytypes = 1;
		if(paytype.equals("1")){
			paytypes = PaytypeEnum.Xianjin.getValue();//现金支付
		}else if(paytype.equals("2")){
			paytypes = PaytypeEnum.Pos.getValue();//pos支付
		}else if(paytype.equals("3")){
			paytypes = PaytypeEnum.CodPos.getValue();//支付宝COD扫码支付
		}else if(paytype.equals("4")){
			paytypes = PaytypeEnum.Qita.getValue();//一部分现金，一部分支付
		}
		map.put("paywayid", String.valueOf(paytypes));//支付方式
		list.add(map);
		return list;
	}
	
	public String respJson(FeiNiuRequest feiniuReq,String success,String reason) {
		CwbResponse cwbResponse = new CwbResponse();
		Responseitems responseitems = new Responseitems();
		cwbResponse.setLogisticproviderid(feiniuReq.getLogistics_interface().getLogisticproviderid());
		List<Responseitems> responseitemsList=new ArrayList<Responseitems>();
		responseitems.setTxlogisticid(feiniuReq.getLogistics_interface().getTxlogisticid());
		responseitems.setMailno(feiniuReq.getLogistics_interface().getMailno());
		responseitems.setSuccess(success);
		responseitems.setReason(reason);
		responseitemsList.add(responseitems);
		cwbResponse.setResponseitems(responseitemsList);
		String responseJsonStr = null;
		try {
			responseJsonStr = JacksonMapper.getInstance().writeValueAsString(cwbResponse);
			logger.info("飞牛网(http)订单推送产生异常！返回信息为:{}",responseJsonStr);
		} catch (Exception e) {
			logger.error("接收订单后的返回数据异常:{}",e);
		} 
		return responseJsonStr;
	}
}	
