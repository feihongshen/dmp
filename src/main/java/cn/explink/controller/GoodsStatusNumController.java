package cn.explink.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.CwbDAO;
import cn.explink.dao.GoodsStatusNumDao;
import cn.explink.dao.UserDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.User;
import cn.explink.pos.tools.JacksonMapper;


@Controller
@RequestMapping("/GoodsStatusNum")
public class GoodsStatusNumController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private GoodsStatusNumDao goodsStatusNumDao;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private UserDAO userdao;
	@Autowired
	private CwbDAO cwbdao;
	@RequestMapping("/findGoodsStatusNum/{flowordertype}/{branchid}")	
	public @ResponseBody String findGoodsStatusNum(
			@PathVariable(value="flowordertype") int flowordertype,
			@PathVariable(value="branchid") int branchid){
		int goodsStatus=goodsStatusNumDao.findGoodsStatusNum(flowordertype,branchid);
		String callback = request.getParameter("callback");
		logger.info("符合条件数量{}",goodsStatus);
		return callback + "("+goodsStatus+")";		
	}
	
	@RequestMapping("/finddeliverymaninfobybranchid")
	public @ResponseBody String finddeliverymaninfobybranchid(
			@RequestParam(value="branchid",defaultValue="",required=true) int branchid){
			List<User> userlist=userdao.findDeliveryManinfoBybranchid(branchid);
			String userlistJson=null;
			String callback = request.getParameter("callback");
			try {
				userlistJson=JacksonMapper.getInstance().writeValueAsString(userlist);
			} catch (JsonGenerationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return callback+"("+userlistJson+")";
	}
	
	@RequestMapping("/findcwbBydeliverId")
	public @ResponseBody String findcwbBydeliveryId(
			@RequestParam(value="deliverid",defaultValue="",required=true) int deliverid)
	{
		List<CwbOrder> CwbOrderlist=cwbdao.findcwbinfoBydeliveryId(deliverid);
		List<CwbOrder> CwbOrderlist1= new ArrayList<CwbOrder>();
		String callback = request.getParameter("callback");
		for(CwbOrder c:CwbOrderlist){
			CwbOrder co = new CwbOrder();
			co.setCwb(c.getCwb());
			co.setConsigneeaddress(c.getConsigneeaddress());
			CwbOrderlist1.add(co);
			}
		
	String CwbOrderlistJson=null;
		try {
				CwbOrderlistJson=JacksonMapper.getInstance().writeValueAsString(CwbOrderlist1);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return callback+"("+CwbOrderlistJson+")";
			
	}

}
