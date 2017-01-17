package cn.explink.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.neo4j.cypher.internal.compiler.v2_1.ast.False;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;



import cn.explink.dao.CwbDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.enumutil.UploadResponseCodeEnum;
import cn.explink.util.FilesHelperUtils;
import cn.explink.util.HttpClientUtil;
import cn.explink.util.Page;

/**
 * 
 * @author lily
 *
 */
@RequestMapping("deliveryreceiptupload")
@Controller
public class DeliveryReceiptUploadController {
	@Autowired
	CwbDAO cwbDAO;
	
	private static Logger logger = LoggerFactory.getLogger(DeliveryPercentController.class);
	
	/**
	 *	进入交接单上传页面 
	 */
	@RequestMapping("deliveryReceipt/{page}")
	public String deliveryReceipt(@PathVariable("page") long page,
			@RequestParam(value="cwb", required = false, defaultValue = "") String cwb,
			@RequestParam(value="consigneeaddress",required=false,defaultValue="") String consigneeaddress,
			@RequestParam(value = "starttime", required = false, defaultValue = "") String starttime, @RequestParam(value = "endtime", required = false, defaultValue = "") String endtime,
			Model model){
		if(starttime.equals("")){
			starttime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		}
		if(endtime.equals("") ){
			endtime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		}
		//订单信息
		List<CwbOrder> yishouhuolist =  cwbDAO.getCwbOrderByReceiveOrder(page,cwb,consigneeaddress,starttime,endtime);
		model.addAttribute("yishouhuolist", yishouhuolist);
		model.addAttribute("page_obj", new Page(this.cwbDAO.getCwbOrderByReceiveOrderCount(cwb,consigneeaddress,starttime,endtime),page,Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "deliveryreceiptupload/deliveryReceipt";
	}
	
	/**
	 * 通过流的方式上传文件
	 */
	@RequestMapping(value="/uploadFileStream",method = RequestMethod.POST)
	public String fileUploadStream(Model model,HttpServletRequest request,
			@RequestParam(value="file_upload") MultipartFile multipartFile,
			@RequestParam(value="mailNo",required=false,defaultValue="") String mailNo){
		//将图片上传到本地
		String filePath = "/files/upload";
		String relativePath = FilesHelperUtils.fileUploadStream(request, multipartFile, filePath);
		String localpath = "/usr/local/tomcat8080-ouky/webapps/ouky"+relativePath;
		//发送请求给otms并接收相应
		String result ;
		String resultMsg = "发送请求失败";
		try {
			result = HttpClientUtil.sendPost(mailNo, localpath);
			if(result!=null){
				String[] str = result.split(",");
				for (int i = 0; i < str.length-1; i++) {
					String[] code = str[0].split(":");
					for (int j = 1; j < code.length; j++) {
						code[j] = code[j].substring(1, code[j].length()-1);
						if(code[j].equals(UploadResponseCodeEnum.Chenggong.getCode())){
							//上传状态为成功的话，将数据添加到数据库中
							resultMsg = UploadResponseCodeEnum.Chenggong.getMeaning();
							logger.info("订单号为"+mailNo+"的交接单上传成功，图片位置保存在"+localpath+"");
							this.cwbDAO.addreciveimgInfo(mailNo,localpath,resultMsg);
						}else if(code[j].equals(UploadResponseCodeEnum.OrderError.getCode())){
							resultMsg = UploadResponseCodeEnum.OrderError.getMeaning();
							logger.info("订单号为"+mailNo+"的交接单上传失败，错误提示为订单信息错误或不完整");
						}else if(code[j].equals(UploadResponseCodeEnum.weizhihzs.getCode())){
							resultMsg = UploadResponseCodeEnum.weizhihzs.getMeaning();
							logger.info("订单号为"+mailNo+"的交接单上传失败，错误提示为未知或无合作关系的承运商");
						}else if(code[j].equals(UploadResponseCodeEnum.NoExist.getCode())){
							resultMsg = UploadResponseCodeEnum.NoExist.getMeaning();
							logger.info("订单号为"+mailNo+"的交接单上传失败，错误提示为订单不存在");
						}else if(code[j].equals(UploadResponseCodeEnum.Noallow.getCode())){
							resultMsg = UploadResponseCodeEnum.Noallow.getMeaning();
							logger.info("订单号为"+mailNo+"的交接单上传失败，错误提示为当前订单状态不允许做该操作");
						}else if(code[j].equals(UploadResponseCodeEnum.Exception.getCode())){
							resultMsg = UploadResponseCodeEnum.Exception.getMeaning();
							logger.info("订单号为"+mailNo+"的交接单上传失败，错误提示为处理异常");
						}
					}
				}
			}
		} catch (IOException e) {
			logger.info("检查订单号为"+mailNo+"的位置,系统设置的路径为"+localpath+"");
			resultMsg = "系统错误，请联系技术人员";
			e.printStackTrace();
		}
		
		model.addAttribute("statecode", resultMsg);
		return "deliveryreceiptupload/deliveryReceipt";
	}

} 
