package cn.explink.b2c.ems;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import sun.misc.BASE64Encoder;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointService;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.OrderGoodsDAO;
import cn.explink.dao.UserDAO;
import cn.explink.service.CwbOrderService;

@Controller
@RequestMapping("/ems")
public class EMSController {

	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	JointService jointService;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	CwbOrderService  cwbOrderService;
	@Autowired
	UserDAO  userDAO;
	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	OrderGoodsDAO orderGoodsDAO;
	@Autowired
	EMSService eMSService;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		model.addAttribute("emsObject", this.eMSService.getEMS(key));
		model.addAttribute("joint_num", key);
		return "b2cdj/ems";

	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String emsSave(Model model, @PathVariable("id") int key, HttpServletRequest request) {

		if ((request.getParameter("password") != null) && "explink".equals(request.getParameter("password"))) {
			try{
				this.eMSService.edit(request, key);
				return "{\"errorCode\":0,\"error\":\"修改成功\"}";
			}catch(Exception e){
				return "{\"errorCode\":1,\"error\":\"" + e.getMessage() + "\"}";
			}
		} else {
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
		// 保存

	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		this.eMSService.update(key, state);
		// 保存
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";

	}
	
	/**
	 * 获取ems运单轨迹
	 */
	@SuppressWarnings({ "unchecked", "finally" })
	@RequestMapping("/transcwbFlow")
	public @ResponseBody String getTranscwbFlow(HttpServletRequest request, HttpServletResponse response) {
		EMSEmailBack backInfo = new EMSEmailBack();
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			
			int isOpenFlag = jointService.getStateForJoint(B2cEnum.EMS.getKey());
			if (isOpenFlag == 0) {
				backInfo.setSuccess("0");
				backInfo.setRemark("品骏未开启接口对接");
				this.logger.info("品骏未开启EMS接口对接!");
			}else{
				// 读取请求内容
		        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(),"GBK"));
		        String line = null;
		        StringBuilder sb = new StringBuilder();
		        while((line = br.readLine())!=null){
		            sb.append(line);
		        }
		        // 将资料解码
		        String reqBody = sb.toString();
		       /* byte[] bytes = sb.toString().getBytes("UTF-8");
		        reqBody=new String(bytes,"UTF-8");*/
		        this.logger.info("EMS运单轨迹报文:{}",reqBody);
		        JSONObject jsonObject=JSONObject.fromObject(reqBody);
		        String listexpressmail=jsonObject.getString("listexpressmail");
		        
		        JSONArray jsonarray = JSONArray.fromObject(listexpressmail);  
			    List<ExpressMail> dataList = (List<ExpressMail>)JSONArray.toCollection(jsonarray,ExpressMail.class);
			    
			    //获取EMS接口配置信息
				/*EMS ems = eMSService.getEmsObject(B2cEnum.EMS.getKey());*/
			    if(dataList.size()!=0){
	            	 for(int i=0;i<dataList.size();i++){
	            		 String transcwb = "";
	            		 eMSService.checkEMSData(dataList.get(i),reqBody);
	            		 backInfo.setSuccess("1");
	     				 backInfo.setRemark("落地配数据接收成功");
	     				 logger.info("接收EMS轨迹信息成功，dmp运单号:{}",transcwb);
	                 }
	            }
			}
		} catch (Exception e) {
			 backInfo.setSuccess("0");
			 backInfo.setRemark(e.getMessage());
			 logger.error("[EMS_运单轨迹]处理业务逻辑异常:{}", e);
			 e.printStackTrace();
		}finally{
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("response", jsonObject);
			return jsonObject.toString();
		}
	}
	
	public static void mian(String args[]){
		String str = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><response> "
				+ "<result>0</result><errorDesc></errorDesc><errorCode></errorCode>"
				+ "<qryData><bigAccountDataId>test0017-1</bigAccountDataId><billno>ems0017-1</billno>"
				+ "<backBillno></backBillno></qryData></response>";
		@SuppressWarnings("restriction")
		String base64Sendstr = new BASE64Encoder().encode(str.getBytes());
		System.out.println(base64Sendstr);
	}
	
}
