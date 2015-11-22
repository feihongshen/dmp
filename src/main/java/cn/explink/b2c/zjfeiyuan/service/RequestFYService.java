package cn.explink.b2c.zjfeiyuan.service;

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

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.tools.RestHttpServiceHanlder;
import cn.explink.b2c.zjfeiyuan.jspbean.FeiYuan;
import cn.explink.b2c.zjfeiyuan.requestdto.Item;
import cn.explink.b2c.zjfeiyuan.requestdto.ItemsBody;
import cn.explink.b2c.zjfeiyuan.requestdto.RequestData;
import cn.explink.b2c.zjfeiyuan.responsedto.ResponseData;
import cn.explink.b2c.zjfeiyuan.util.BeanToXml;
import cn.explink.b2c.zjfeiyuan.util.XmlToBean;
import cn.explink.dao.CustomerDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.User;
import cn.explink.util.StringUtil;
import cn.explink.util.MD5.MD5Util;

@Service
public class RequestFYService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	DataImportService_B2c dataImportService_B2c;
	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;
	@Autowired
	CustomerDAO customerDAO;
	
	public void update(int joint_num,int state){
		jiontDAO.UpdateState(joint_num, state);
	}
	public void edit(HttpServletRequest request,int joint_num){
		FeiYuan dms=new FeiYuan();
		String private_key = request.getParameter("private_key");
		String userCode = request.getParameter("userCode");
		String batchno = request.getParameter("batchno");
		String requestUrl = request.getParameter("requestUrl");
		dms.setPrivate_key(StringUtil.nullConvertToEmptyString(private_key));
		dms.setUsercode(StringUtil.nullConvertToEmptyString(userCode));
		dms.setBatchno(StringUtil.nullConvertToEmptyString(batchno));
		dms.setRequestUrl(StringUtil.nullConvertToEmptyString(requestUrl));
		JSONObject jsonObj = JSONObject.fromObject(dms);
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
	}
	
	//获取页面配置对象
	public FeiYuan getFeiyuan(int yhd_key) {
		if (getObjectMethod(yhd_key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(yhd_key));
		FeiYuan feiyuan = (FeiYuan) JSONObject.toBean(jsonObj, FeiYuan.class);
		return feiyuan;
	}
	
	//数据库查询获取页面设置的实体
	public Object getObjectMethod(int key) {
		JointEntity obj = jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}
	
	//获取请求参数并进行请求飞远地址库
	public ResponseData dealAddressMatch(CwbOrder co,User user) throws Exception{
		FeiYuan fy = this.getFeiyuan(B2cEnum.FeiyuanAddress.getKey());
		RequestData rd = new RequestData();
		cn.explink.b2c.zjfeiyuan.requestdto.Headers head = new cn.explink.b2c.zjfeiyuan.requestdto.Headers();
		String requestUrl = StringUtil.nullConvertToEmptyString(fy.getRequestUrl());
		String private_key = StringUtil.nullConvertToEmptyString(fy.getPrivate_key()); 
		String userCode = StringUtil.nullConvertToEmptyString(fy.getUsercode());
		String batchno = StringUtil.nullConvertToEmptyString(fy.getBatchno());
		String key = MD5Util.md5(userCode+private_key+batchno);
		head.setUsercode(userCode);
		head.setBatchno(batchno);
		head.setKey(key);
		this.logger.info("请求浙江飞远地址库关键信息:private_key={},usercode={},batchno={},key={}",new Object[]{private_key,userCode,batchno,key});
		List<Item> items = new ArrayList<Item>();
		Item item = new Item();
		item.setItemno("1");//地址库请求处理，每次请求一单(单条处理时传1)
		item.setProvince(co.getCwbprovince());//省
		item.setCity(co.getCwbcity());//市
		item.setArea(co.getCwbcounty());//区/县
		item.setTown("");//镇
		item.setAddress(co.getConsigneeaddress());//收件人地址
		items.add(item);
		ItemsBody ib = new ItemsBody();
		ib.setItems(items);
		//封装请求参数
		rd.setHead(head);
		rd.setItems(ib);
		//转化成xml字符串
		String xmlStr = BeanToXml.toXml(rd);
		this.logger.info("请求飞远地址库,单号:{},信息:{}",co.getCwb(),xmlStr);
		Map<String, String> map = new HashMap<String, String>();
		map.put("data",xmlStr);
		//请求飞远地址库
		String responseXML = RestHttpServiceHanlder.sendHttptoServer(map, requestUrl);
		this.logger.info("飞远地址库返回信息:{}",responseXML);
		//解析返回数据并匹配站点
		ResponseData respdata = (ResponseData)XmlToBean.toBean(responseXML);
		return respdata;
	}
	
}
