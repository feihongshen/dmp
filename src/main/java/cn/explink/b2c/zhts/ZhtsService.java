package cn.explink.b2c.zhts;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;
import javax.xml.bind.PropertyException;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.tools.JointService;
import cn.explink.b2c.weisuda.xml.ObjectUnMarchal;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CommonDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.OrderTrackDAO;
import cn.explink.domain.Common;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.orderflow.OrderNote;
import cn.explink.domain.orderflow.OrderTrack;
import cn.explink.util.MD5.MD5Util;

@Service
public class ZhtsService {
	private static Logger logger = LoggerFactory.getLogger(ZhtsService.class);

	
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	JointService jointService;
	@Autowired
	OrderTrackDAO orderTrackDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	CommonDAO commonDAO;
	@Autowired
	BranchDAO branchDAO;
	
	private static List<B2cEnum> zhtsearchList=new ArrayList<B2cEnum>();
	static{
		for(B2cEnum enums:B2cEnum.values()){
			if(enums.getMethod().contains("zhts_ordertrack")){
				zhtsearchList.add(enums);
			}
		}
		
	}
	
	public Zhts searchZhts(String userCode){
		Zhts zhts= null;
		for(B2cEnum enums:zhtsearchList){
			
			int isOpenFlag = jointService.getStateForJoint(enums.getKey());
			if(isOpenFlag==0){
				return null;
			}
			
			zhts=this.getZhts(enums.getKey());
			
			if(zhts!=null&&zhts.getUserCode().equals(userCode)){
				return zhts;
			}
		}
		
		return null;
	}
	
	public String getObjectMethod(int key) {
		JointEntity obj = jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}

	public Zhts getZhts(int key) {
		if (getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
		Zhts dangdang = (Zhts) JSONObject.toBean(jsonObj, Zhts.class);
		return dangdang;
	}

	public void edit(HttpServletRequest request, int joint_num) {
		Zhts sf = new Zhts();
		sf.setUserCode(request.getParameter("userCode"));
		sf.setPrivate_key(request.getParameter("private_key"));
		sf.setSearhUrl(request.getParameter("searhUrl"));

		JSONObject jsonObj = JSONObject.fromObject(sf);

		JointEntity jointEntity = jiontDAO.getJointEntity(joint_num);
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Create(jointEntity);
		} else {
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Update(jointEntity);
		}

	}

	public void update(int joint_num, int state) {
		jiontDAO.UpdateState(joint_num, state);
	}
	
	private String responseMsg(String code,String msg){
		String str="<IFReturn>"
					+"<errCode>"+code+"</errCode>"
					+"<errMsg>"+msg+"</errMsg>"
				+ "</IFReturn>";
		
		return str;
	}
	
	public String receivedOrderTrack(String userCode,String sign,String requestTime,String content){
		try {
			
			Zhts zhts = this.searchZhts(userCode);
			
			if(zhts==null){
				return responseMsg(ZhtsExpEmum.YongHuBuCunZai.getErrCode(), ZhtsExpEmum.YongHuBuCunZai.getErrMsg());
			}
			
			Common common =  commonDAO.getCommonByCommonnumber(userCode);
			if(common==null){
				return responseMsg(ZhtsExpEmum.YongHuBuCunZai.getErrCode(), ZhtsExpEmum.YongHuBuCunZai.getErrMsg());
			}
			
			String localSign=MD5Util.md5(userCode+requestTime+zhts.getPrivate_key());
			if(!localSign.equalsIgnoreCase(sign)){
				return responseMsg(ZhtsExpEmum.QianMingCuoWu.getErrCode(), ZhtsExpEmum.QianMingCuoWu.getErrMsg());
			}
			
			OrderTrack orderTrack = (OrderTrack) ObjectUnMarchal.XmltoPOJO(content, new OrderTrack());
			
			CwbOrder co =cwbDAO.getCwbByCwb(orderTrack.getOrder().getOrderNo());
			if(co==null){
				return responseMsg(ZhtsExpEmum.YeWuYiChang.getErrCode(), "订单不存在");
			}
			
			OrderNote order = orderTrack.getOrder();
			order.setUserCode(userCode);
			
			replaceSensitive(common, order); //敏感词替换
			
			orderTrackDAO.creOrderTrack(order);
			
			return responseMsg(ZhtsExpEmum.Success.getErrCode(), ZhtsExpEmum.Success.getErrMsg());
			
		} catch (Exception e) {
			logger.error("中浩接收订单轨迹异常",e);
			return  responseMsg(ZhtsExpEmum.XiTongYiChang.getErrCode(), ZhtsExpEmum.XiTongYiChang.getErrMsg()+","+e.getMessage());
		}
		
		
	}

	private void replaceSensitive(Common common, OrderNote order) {
		
		try {
			String sensitive = common.getSensitiveWord();  //敏感词 A|B|C
			if(sensitive!=null&&!sensitive.isEmpty()){
				String operationTrack=order.getOperationTrack();
				for(String arrStr:sensitive.split("\\|")){
					operationTrack=operationTrack.replace(arrStr,branchDAO.getBranchById(common.getBranchid()).getBranchname());
				}
				order.setOperationTrack(operationTrack);
			}
		} catch (Exception e) {
			logger.error("敏感词替换失败",e);
		}
	}
	

	
	public static void main(String[] args) throws PropertyException, JAXBException, UnsupportedEncodingException {
		String str="A|B|C";
		System.out.println(str.split("\\|").length);
		for(String s:str.split("\\|")){
			System.out.println(s);
		}
		
		OrderTrack tao=new OrderTrack();
		OrderNote order=new OrderNote();
		order.setOrderNo("111");
		order.setTransOrderNo("222");
		order.setOperatorName("战三");
		tao.setOrder(order);
		
		
		
		System.out.println(ObjectUnMarchal.POJOtoXml(tao));
		String content="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><OrderTrack>"
		  +"<order>"
		  +"<order_no>35501201</order_no>"  
		    +"<transorder_no>35501201</transorder_no>"  
		    +"<operation_time>2016-03-07 17:19:05</operation_time>" 
		    +"<operation_name>张三</operation_name>"
		    +"<operation_track>货物由XX站派送员正在派件...联系方式：138</operation_track>"
		  +"</order>"
		+"</OrderTrack>";
		
		OrderTrack orderTrack = (OrderTrack) ObjectUnMarchal.XmltoPOJO(content, new OrderTrack());
		
		System.out.println(orderTrack.getOrder().getOrderNo()+"=="+orderTrack.getOrder().getOperationTime());
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

}
