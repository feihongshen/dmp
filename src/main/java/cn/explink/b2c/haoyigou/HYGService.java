package cn.explink.b2c.haoyigou;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.jiuye.JiuYe;
import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.dao.CustomerDAO;
import cn.explink.util.StringUtil;

@Service
public class HYGService {
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	DataImportService_B2c dataImportService_B2c;
	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;
	@Autowired
	CustomerDAO customerDAO;
	private Logger logger =LoggerFactory.getLogger(this.getClass());
	public void update(int joint_num,int state){
		jiontDAO.UpdateState(joint_num, state);
	}
	public void edit(HttpServletRequest request,int joint_num){
		HaoYiGou hyg=new HaoYiGou();
		String sendCode = request.getParameter("sendCode");
		String customercode = request.getParameter("customercode");
		String companyid = request.getParameter("companyid");
		String customerid = request.getParameter("customerid");
		String partener = request.getParameter("partener");
		String maxcountStr = request.getParameter("maxcount");
		long maxcount = Long.parseLong(getStr(maxcountStr));
		String ftp_host = request.getParameter("ftp_host");
		String ftp_username = request.getParameter("ftp_username");
		String ftp_password = request.getParameter("ftp_password");
		String ftp_portStr = request.getParameter("ftp_port");
		int ftp_port = Integer.parseInt(getStr(ftp_portStr));
		String charencode = request.getParameter("charencode");
		//==========上传位置（包括上传到FTP服务器上的位置）=============
		String upload_remotePathps = request.getParameter("upload_remotePathps");//配送位置
		String upload_remotePathth = request.getParameter("upload_remotePathth");//退货位置
		String uploadPath = request.getParameter("uploadPath");//当前服务器位置
		String uploadPath_bak = request.getParameter("uploadPath_bak");//当前服务器备份
	
		//封装页面请求参数存储在基础设置表中
		hyg.setSendCode(sendCode);
		hyg.setCustomercode(customercode);
		hyg.setCompanyid(companyid);
		hyg.setCustomerid(customerid);
		hyg.setPartener(partener);
		hyg.setMaxcount(maxcount);
		hyg.setFtp_host(ftp_host);
		hyg.setFtp_username(ftp_username);
		hyg.setFtp_password(ftp_password);
		hyg.setFtp_port(ftp_port);
		hyg.setCharencode(charencode);
		hyg.setUpload_remotePathps(upload_remotePathps);
		hyg.setUpload_remotePathth(upload_remotePathth);
		hyg.setUploadPath(uploadPath);
		hyg.setUploadPath_bak(uploadPath_bak);
		
		String oldCustomerids = "";
		JSONObject jsonObj = JSONObject.fromObject(hyg);
		JointEntity jointEntity = jiontDAO.getJointEntity(joint_num);
		if(jointEntity == null){
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Create(jointEntity);
		}else{
			try {
					HaoYiGou haoyig = getHYG(joint_num);
					if(haoyig!=null){
						oldCustomerids = haoyig.getCustomerid();
					}
			    } catch (Exception e) {
			    	this.logger.error("获取【好易购】之前设置的id异常,异常原因:", e);
			    }
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Update(jointEntity);
		}
		//保存 枚举到供货商表中
		customerDAO.updateB2cEnumByJoint_num(customerid, oldCustomerids, joint_num);
	}
	
	
	public String getStr(String str){
		if((str == null)||("".equals(str))){
			return "0";
		}
		return str;
	}
	
	public HaoYiGou getHYG(int yhd_key) {
		if (getObjectMethod(yhd_key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(yhd_key));
		HaoYiGou hyg = (HaoYiGou) JSONObject.toBean(jsonObj, HaoYiGou.class);
		return hyg;
	}
	
	public Object getObjectMethod(int key) {
		JointEntity obj = jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}
}
