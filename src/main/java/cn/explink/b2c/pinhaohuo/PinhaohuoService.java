package cn.explink.b2c.pinhaohuo;

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

import cn.explink.b2c.pinhaohuo.jsondto.AddressResp;
import cn.explink.b2c.pinhaohuo.jsondto.AddressShell;
import cn.explink.b2c.pinhaohuo.jsondto.OrderShell;
import cn.explink.b2c.pinhaohuo.jsondto.PhhOrder;
import cn.explink.b2c.pinhaohuo.jsondto.RespShell;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.domain.Branch;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.service.addressmatch.AddressMatchService;
import cn.explink.util.MD5.MD5Util;


@Service
public class PinhaohuoService {
	private Logger logger =LoggerFactory.getLogger(PinhaohuoService.class);
	
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
	AddressMatchService addressMatchService;
	@Autowired
	BranchDAO branchDAO;
	
	
	public String getObjectMethod(int key){
		JointEntity obj=jiontDAO.getJointEntity(key);
		return obj==null?null:obj.getJoint_property();
	}
	public Pinhaohuo getPinhaohuo(int key){
		if(getObjectMethod(key)==null){
			return null;
		}
	    JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key)); 
	    Pinhaohuo smile = (Pinhaohuo)JSONObject.toBean(jsonObj,Pinhaohuo.class);
		return smile;
	}
	public void edit(HttpServletRequest request,int joint_num){
		Pinhaohuo p=new Pinhaohuo();
		String customerids = request.getParameter("customerids");
		p.setCustomerids(customerids);
		p.setFeedbackUrl(request.getParameter("feedbackUrl"));
		p.setImportUrl(request.getParameter("importUrl"));
		String maxcount="".equals(request.getParameter("maxcount"))?"0":request.getParameter("maxcount");
		p.setMaxCount(Integer.parseInt(maxcount));
		p.setPrivate_key(request.getParameter("private_key"));
		String warehouseid=request.getParameter("warehouseid");
		p.setWarehouseid(Long.valueOf(warehouseid));
		p.setCode(request.getParameter("code"));
		p.setCustomerArrs(request.getParameter("customerArrs"));
		String oldCustomerids = "";
		
		JSONObject jsonObj = JSONObject.fromObject(p);
		JointEntity jointEntity = jiontDAO.getJointEntity(joint_num);
		if(jointEntity == null){
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Create(jointEntity);
		}else{
			try {
				oldCustomerids = getPinhaohuo(joint_num).getCustomerids();
			    } catch (Exception e) {
			    }
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Update(jointEntity);
		}
		//保存 枚举到供货商表中
		customerDAO.updateB2cEnumByJoint_num(customerids, oldCustomerids, joint_num);
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
	 * @param result   True or False
	 * @param remark   备注信息，如果订单重复， 则True ,remark="运单重复"
	 * @return
	 */
	public String buildResponseJSON(String result,String error_code,String error_info){
		RespShell resp=new RespShell();
		resp.setResult(result);
		resp.setError_code(error_code);
		resp.setError_info(error_info);
		String respString="";
		try {
			respString = JacksonMapper.getInstance().writeValueAsString(resp);
		} catch (Exception e) {
			logger.error("json格式转化异常",e);
		}
		
		logger.info("返回拼好货json={}",respString);
		return respString;
	}
	

	
	/**
	 * 处理思迈请求数据，并且导入系统 数据，一单一单的导入
	 * @return
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	public String dealwithHxgdmsOrders(String jsonstr,int smileKey,long customerid) throws NumberFormatException, Exception{
		
		
		int isOpenFlag=jiontDAO.getStateByJointKey(smileKey);
		Pinhaohuo dms=this.getPinhaohuo(smileKey);
		if (isOpenFlag == 0) {
			return buildResponseJSON("fail","1111","未开启接口");
		}
		
		OrderShell shell = JacksonMapper.getInstance().readValue(jsonstr,OrderShell.class);
		
		String signData = JacksonMapper.getInstance().writeValueAsString(shell.getOrder());
		
		String localSign=MD5Util.md5(signData+dms.getPrivate_key(),"UTF-8");
		if(!localSign.equalsIgnoreCase(shell.getKey())){
			logger.error("签名验证失败signData="+signData);
			return buildResponseJSON("fail","1111","签名验证失败");
		}
		
		List<PhhOrder> datalist=shell.getOrder();
		if(datalist==null||datalist.size()==0){
			return buildResponseJSON("fail","1111","没有内容");
		}
		
		List<Map<String,String>> orderlist=parseCwbArrByOrderDto(datalist,dms,customerid);
		
		if(orderlist==null||orderlist.size()==0){
			logger.warn("拼好货-请求没有封装参数，订单号可能为空");
			return buildResponseJSON("fail","1111","运单重复");
		}
		
		long warehouseid=dms.getWarehouseid();  //订单导入的库房Id
		customerid=customerid==0?Long.parseLong(dms.getCustomerids()):customerid;
		
		List<CwbOrderDTO> cwbOrders = dataImportInterface.Analizy_DataDealByB2c(customerid,B2cEnum.PinHaoHuo.getMethod(), orderlist,warehouseid,true);
		
		if(cwbOrders == null){
			return buildResponseJSON("fail","1111","业务异常");
		}
		
		logger.info("拼好货-订单导入成功");
		
		return buildResponseJSON("ok",null,null);
	}
	
	
	/**
	 * 返回一个转化为导入接口可识别的对象
	 */
	private List<Map<String,String>> parseCwbArrByOrderDto(List<PhhOrder> orderlist,Pinhaohuo dms,long customerid) {
		List<Map<String,String>> cwbList=new ArrayList<Map<String,String>>();
		
		for(PhhOrder order:orderlist){
			
				CwbOrderDTO cwbOrder=dataImportDAO_B2c.getCwbByCwbB2ctemp(order.getOrder_id());
				if(cwbOrder!=null){
					logger.warn("获取拼好货订单中含有重复数据cwb={}",order.getTracking_number());
					continue;
				}
			
				String address=order.getProvince_name()+order.getCity_name()+order.getDistrict_name()+order.getShipping_address();
				
				Map<String,String> cwbMap=new HashMap<String,String>();
				cwbMap.put("cwb",order.getTracking_number());   //cwb 订单号
				cwbMap.put("transcwb", order.getOrder_id());  //transcwb 客户单号
				cwbMap.put("consigneename",order.getReceive_name());
				cwbMap.put("consigneeaddress",address);
				cwbMap.put("consigneephone",order.getTel());
				cwbMap.put("consigneemobile",order.getMobile());
				cwbMap.put("sendcarnum","1");   //发货数量
				cwbMap.put("cwbordertypeid","1");
				cwbMap.put("receivablefee","0");
				cwbMap.put("customerwarehouseid",order.getNote());
				cwbMap.put("customercommand", customerid+"");
				
				cwbList.add(cwbMap);
				
		}
		return cwbList;
	}
	
	
	
	/**
	 * 处理思迈请求数据，并且导入系统 数据，一单一单的导入
	 * @return
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	public String addressmatchService(String jsonstr) throws NumberFormatException, Exception{
		
		int smileKey=B2cEnum.PinHaoHuo.getKey();
		int isOpenFlag=jiontDAO.getStateByJointKey(smileKey);
		if (isOpenFlag == 0) {
			return buildResponseJSON("fail","1111","未开启接口");
		}
		AddressShell shell = JacksonMapper.getInstance().readValue(jsonstr,AddressShell.class);
		String address =shell.getProvince_name()+shell.getCity_name()+shell.getDistrict_name()+shell.getShipping_address();
		
		JSONObject addressJson =  addressMatchService.matchAddressByInterfaces("pinhaohuo", address);
		String station=addressJson.getString("station"); //对应返回的站点ID
		if(station.isEmpty()){
			return buildResponseJSON("fail","1111","请求地址库发生异常或者匹配站点失败！");
		}
		
		Branch branch=branchDAO.getBranchByBranchid(Long.valueOf(station));
		AddressResp resp=new AddressResp();
		resp.setDepotCode(branch.getBranchcode());
		resp.setDepotName(branch.getBranchname());
		String respJson = JacksonMapper.getInstance().writeValueAsString(resp);
		
		logger.info("拼好货站点匹配返回:{}",respJson);
		
		return respJson;
	}
	



}
