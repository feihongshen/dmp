package cn.explink.b2c.huanqiugou;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;
import javax.xml.bind.PropertyException;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.huanqiugou.reqDto.Order;
import cn.explink.b2c.huanqiugou.reqDto.ReqDto;
import cn.explink.b2c.huanqiugou.respDto.ResponseDto;
import cn.explink.b2c.huanqiugou.respDto.ReturnDto;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.tools.ObjectUnMarchal;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.service.CustomerService;
import cn.explink.util.MD5.Base64Utils;
import cn.explink.util.MD5.MD5Util;

@Service
public class HuanqiugouService {
	private Logger logger =LoggerFactory.getLogger(HuanqiugouService.class);
	
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	DataImportService_B2c dataImportInterface;
	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;
	@Autowired
	CustomWareHouseDAO customWareHouseDAO;
	@Autowired
	CustomerService customerService;
	
	public String getObjectMethod(int key){
		JointEntity obj=jiontDAO.getJointEntity(key);
		return obj==null?null:obj.getJoint_property();
	}
	public Huanqiugou getHuanqiugou(int key){
		if(getObjectMethod(key)==null){
			return null;
		}
	    JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key)); 
	    Huanqiugou smile = (Huanqiugou)JSONObject.toBean(jsonObj,Huanqiugou.class);
		return smile;
	}
	@Transactional
	public void edit(HttpServletRequest request,int joint_num){
		Huanqiugou dms=new Huanqiugou();
		String customerids = request.getParameter("customerids");
		dms.setCustomerids(customerids);
		dms.setFeedbackUrl(request.getParameter("feedbackUrl"));
		String maxcount="".equals(request.getParameter("maxcount"))?"0":request.getParameter("maxcount");
		dms.setMaxcount(Integer.parseInt(maxcount));
		dms.setPrivateKey(request.getParameter("privateKey"));
		String warehouseid=request.getParameter("warehouseid");
		dms.setWarehouseid(Long.valueOf(warehouseid));
		dms.setReceivedUrl(request.getParameter("receivedUrl"));
		dms.setExpressid(request.getParameter("expressid"));
		
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
				oldCustomerids = getHuanqiugou(joint_num).getCustomerids();
			    } catch (Exception e) {
			    }
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Update(jointEntity);
		}
		//保存 枚举到供货商表中
		customerDAO.updateB2cEnumByJoint_num(customerids, oldCustomerids, joint_num);
		this.customerService.initCustomerList();
	}
	public void update(int joint_num,int state){
			jiontDAO.UpdateState(joint_num, state);
	}
	
	public int getStateForYihaodian(int key){
		JointEntity obj=null;
		int state=0;
		try {
			obj = jiontDAO.getJointEntity(key);
			state=obj.getState();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return state;
	}

	/**
	 * 
	 * @return
	 */
	public String buildResponse(String returnCode,String returnDesc,String returnFlag){
		ResponseDto responseDto =new ResponseDto();
		ReturnDto dto=new ReturnDto();
		dto.setReturnCode(returnCode);
		dto.setReturnDesc(returnDesc);
		dto.setResultInfo("");
		dto.setReturnFlag(returnFlag);
		responseDto.setReturnDto(dto);
		String response=null;
		try {
			response = ObjectUnMarchal.POJOtoXml(responseDto);
		} catch (Exception e) {
		} 
		return response;
	}
	

	
	/**
	 * 处理思迈请求数据，并且导入系统 数据，一单一单的导入
	 * @return
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	public String dealwithHxgdmsOrders(String method,String expressid,String sign,String timestamp,String data) throws NumberFormatException, Exception{
		
		int smileKey=B2cEnum.HuanQiuGou.getKey();
		int isOpenFlag=jiontDAO.getStateByJointKey(smileKey);
		Huanqiugou dms=this.getHuanqiugou(smileKey);
		if (isOpenFlag == 0) {
			return this.buildResponse("S007", "未开启接口", "0");
		}
		if(!expressid.equals(dms.getExpressid())){
			return  this.buildResponse("S001", "指定expressid不正确", "0");
		}
		
		String localMd5=MD5Util.md5(dms.getPrivateKey()+data+dms.getPrivateKey(),"UTF-8");
		String localBase64=Base64Utils.base64(localMd5, "UTF-8").toUpperCase();
		if(!localBase64.equalsIgnoreCase(sign)){
			return this.buildResponse("S001", "签名验证失败", "0");
		}
	
		ReqDto dto=(ReqDto) ObjectUnMarchal.XmltoPOJO(data,new ReqDto());
		
		List<Map<String,String>> orderlist=parseCwbArrByOrderDto(dto.getOrderlist(),dms);
		
		if(orderlist==null||orderlist.size()==0){
			logger.warn("环球购物-请求没有封装参数，订单号可能为空");
			return this.buildResponse("S003", "订单解析异常", "0");
		}
		
		long warehouseid=dms.getWarehouseid();  //订单导入的库房Id
		dataImportInterface.Analizy_DataDealByB2c(Long.parseLong(dms.getCustomerids()),B2cEnum.HuanQiuGou.getMethod(), orderlist,warehouseid,true);
		
		return buildResponse("0000", "OK", "1");
	}
	
	
	/**
	 * 返回一个转化为导入接口可识别的对象
	 */
	private List<Map<String,String>> parseCwbArrByOrderDto(List<Order> orderList,Huanqiugou dms) {
		List<Map<String,String>> cwbList=new ArrayList<Map<String,String>>();
		
		if(orderList!=null&&orderList.size()>0){
			for(Order order:orderList){
				CwbOrderDTO cwbOrder=dataImportDAO_B2c.getCwbByCwbB2ctemp(order.getSubmailno());
				if(cwbOrder!=null){
					logger.warn("获取环球购物订单中含有重复数据cwb={}",order.getSubmailno());
					return null;
				}
				long cwbordertypeid=0;
				String ordertype=order.getOrdertype();
				if("1".equals(ordertype)){
					cwbordertypeid=CwbOrderTypeIdEnum.Peisong.getValue();
				}else if("2".equals(ordertype)){
					cwbordertypeid=CwbOrderTypeIdEnum.Shangmentui.getValue();
				}else if("3".equals(ordertype)){
					cwbordertypeid=CwbOrderTypeIdEnum.Shangmenhuan.getValue();
				}
				
				Map<String,String> cwbMap=new HashMap<String,String>();
				cwbMap.put("cwb", order.getSubmailno());   //cwb 面单号
				cwbMap.put("transcwb",order.getMailno()) ;  //transcwb 子单号,不开启一票多件使用运单号
				cwbMap.put("remark1","出库单号:"+order.getOrderno()); 
				cwbMap.put("remark2","寄件日期:"+order.getSenddate()); 
				cwbMap.put("remark3","发货仓:"+order.getWarehouse());
				cwbMap.put("consigneename",order.getReceiveperson());
				cwbMap.put("consigneemobile",order.getReceivetel());
				cwbMap.put("consigneephone",order.getReceivetel());
				cwbMap.put("sendcargoname",order.getGoodsname());
				cwbMap.put("sendcarnum",order.getQty());
				cwbMap.put("cwbprovince",order.getReceiveprovince());
				cwbMap.put("cwbcity",order.getReceivecity());
				cwbMap.put("cwbcountry",order.getReceivearea());
				cwbMap.put("consigneeaddress",order.getReceiveaddress());
				cwbMap.put("cargorealweight",order.getWeight());
				cwbMap.put("remark4", order.getSkulength()+"*"+order.getSkuwidth()+"*"+order.getSkuhigh());  //单位，立方厘米
				cwbMap.put("cwbordertypeid",String.valueOf(cwbordertypeid));
				cwbMap.put("cargotype",order.getGoodstype());
				cwbMap.put("cargoamount",order.getPrice());
				cwbMap.put("receivablefee",order.getPayamount());
				cwbMap.put("paywayid",("2".equals(order.getPaytype())?PaytypeEnum.Pos.getValue():PaytypeEnum.Xianjin.getValue())+"");
				cwbMap.put("customercommand","客户可收货时间:"+order.getReceiveTime()+" "+getTpcontrol(order));
				cwbList.add(cwbMap);
			}
		}
		return cwbList;
	}
	private String getTpcontrol(Order order) {
		String tpcontrol=order.getTpcontrol();
		if(tpcontrol!=null&&tpcontrol.equals("1")){
			tpcontrol="冷藏";
		}else if(tpcontrol!=null&&tpcontrol.equals("2")){
			tpcontrol="冷冻";
		}
		return tpcontrol;
	}
	
	public static void main(String[] args) throws PropertyException, JAXBException {
		ReqDto shell=new ReqDto();
		List<Order> list=new ArrayList<Order>();
		Order order= new Order();
		order.setMailno("111111");
		order.setGoodsname("AAAAA");
		Order order2= new Order();
		order2.setMailno("2222");
		order2.setGoodsname("BBBBB");
		list.add(order2);
		list.add(order);
		shell.setOrderlist(list);
		
		System.out.println(ObjectUnMarchal.POJOtoXml(shell));
		
	}



}
