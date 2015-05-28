package cn.explink.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import cn.explink.controller.AbnormalView;
import cn.explink.dao.AbnormalOrderDAO;
import cn.explink.dao.AbnormalTypeDAO;
import cn.explink.dao.AbnormalWriteBackDAO;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.AbnormalType;
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.User;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.ResourceBundleUtil;
import cn.explink.util.ServiceUtil;

@Service
public class AbnormalService {
	@Autowired
	AbnormalTypeDAO abnormalTypeDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	AbnormalOrderDAO abnormalOrderDAO;
	@Autowired
	AbnormalWriteBackDAO abnormalWriteBackDAO;
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	public List<AbnormalView> setViews(List<JSONObject> alist, List<Branch> branchs, List<User> users, List<Customer> customers, List<AbnormalType> atlist) {

		List<AbnormalView> views = new ArrayList<AbnormalView>();
		if (alist.size() > 0) {

			for (JSONObject a : alist) {
				AbnormalView view = new AbnormalView();
				view.setId(a.getInt("id"));
				view.setFlowordertype(this.getFloworderType(a.getLong("flowordertype")));
				view.setAbnormalType(this.getAbnormalType(atlist, a.getLong("abnormaltypeid")));
				view.setBranchName(this.getBranchName(branchs, a.getLong("branchid")));
				view.setCreuserName(this.getCreName(users, a.getLong("creuserid")));
				view.setCwb(a.getString("cwb"));
				view.setCustomerName(this.getCustomer(customers, a.getLong("customerid")));
				view.setDescribe(a.getString("describe"));
				view.setEmaildate(a.getString("emaildate"));
				view.setIshandle(a.getLong("ishandle"));
				view.setCredatetime(a.getString("credatetime"));
				view.setFileposition(a.getString("fileposition"));
				view.setDealResultContent(this.getDealResult(a.getLong("dealresult")));
				view.setCwborderType(this.getCwbOrderType(a.getString("cwbordertypeid")));
				view.setDutybrachid(a.getLong("dutybrachid"));
				view.setIsfinecontent(this.getIsFine(a.getLong("isfine")));
				view.setDutybranchname(this.getBranchName(branchs, a.getLong("dutybrachid")));
				view.setQuestionno(a.getString("questionno"));
				//缺少一个是否丢失找回
				view.setLosebackContent("");
				views.add(view);
			}
		}
		return views;
	}
	public String getDealResult(long dealResultContent){
		String dealresultContent="";
		if (dealResultContent==1) {
			dealresultContent="问题成立";
		}else if(dealResultContent==2) {
			dealresultContent="问题不成立";
		}
		return  dealresultContent;
	}
	public String getIsFine(long isfine){
		String returndata="";
		if (isfine==2) {
			returndata="是";
		}else if(isfine==1) {
			returndata="否";
		}
		return returndata;
	}
	public String getCwbOrderType(String cwbordertypeid){
		String cwbordertypereturn="";
		for (CwbOrderTypeIdEnum cwbordertype : CwbOrderTypeIdEnum.values()) {
			if (cwbordertype.getValue()==Integer.parseInt(cwbordertypeid)) {
				cwbordertypereturn=cwbordertype.getText();
			}
		}
		return cwbordertypereturn;
	}
	private String getCustomer(List<Customer> customers, long long1) {
		String customername = "";
		for (Customer customer : customers) {
			if (customer.getCustomerid() == long1) {
				customername = customer.getCustomername();
				break;
			}
		}
		return customername;
	}

	private String getCreName(List<User> users, long userid) {
		String username = "";
		for (User user : users) {
			if (user.getUserid() == userid) {
				username = user.getRealname();
				break;
			}
		}

		return username;
	}

	private String getBranchName(List<Branch> branchs, long long1) {
		String branchname = "";
		for (Branch branch : branchs) {
			if (branch.getBranchid() == long1) {
				branchname = branch.getBranchname();
				break;
			}
		}
		return branchname;
	}

	private String getAbnormalType(List<AbnormalType> atlist, long long1) {
		String type = "";
		for (AbnormalType abnormalType : atlist) {
			if (abnormalType.getId() == long1) {
				type = abnormalType.getName();
				break;
			}
		}
		return type;
	}

	private String getFloworderType(long type) {
		return FlowOrderTypeEnum.getText(type).getText();
	}

	/**
	 * 创建
	 *
	 * @param co
	 * @param user
	 * @param abnormaltypeid
	 * @param nowtime
	 * @param mapForAbnormalorder
	 */
	@Transactional
	public void creAbnormalOrder(CwbOrder co, User user, long abnormaltypeid, String nowtime, Map<Long, JSONObject> mapForAbnormalorder, long action, long handleBranch,String name,String abnormalinfo,String questionNo) {
		// long abnormalorderid =
		// abnormalOrderDAO.creAbnormalOrderLong(co.getOpscwbid(),
		// co.getCustomerid(), "", user.getUserid(), user.getBranchid(),
		// abnormaltypeid, nowtime);
		long abnormalorderid = this.abnormalOrderDAO.creAbnormalOrderLong(co, abnormalinfo, user.getUserid(), user.getBranchid(), abnormaltypeid, nowtime, handleBranch,name,questionNo);
		this.abnormalWriteBackDAO.creAbnormalOrder(co.getOpscwbid(), abnormalinfo, user.getUserid(), action, nowtime, abnormalorderid, abnormaltypeid, co.getCwb(),name);
		JSONObject json = new JSONObject();
		json.put("abnormalorderid", abnormalorderid);
		json.put("abnormalordertype", abnormaltypeid);
		mapForAbnormalorder.put(co.getOpscwbid(), json);
	}
	//问题件添加到指定路径下
	public  String loadexceptfile(MultipartFile file){
		String name="";
		try {
			if ((file != null) && !file.isEmpty()) {
				String filePath = ResourceBundleUtil.EXCEPTPATH;
				name=file.getOriginalFilename();
				String suffix=name.substring(name.indexOf("."));
				 name = System.currentTimeMillis() + suffix;
				ServiceUtil.uploadWavFile(file, filePath, name);
			}
		} catch (Exception e) {
			this.logger.error("问题件添加到指定路径下出现错误");
		}
		return name;
	}
	public String getExceptname(HttpServletRequest request){
		String name="";
		 //创建一个通用的多部分解析器  
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());  
        if(multipartResolver.isMultipart(request)){  
        	 //转换成多部分request    
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;  
            //取得request中的所有文件名  
            Iterator<String> iter = multiRequest.getFileNames(); 
            while(iter.hasNext()){  
            	 //取得上传文件  
                MultipartFile file = multiRequest.getFile(iter.next());  
                name=this.loadexceptfile(file)+",";
            }
        }
        if (name!="") {
			name=name.substring(0, name.length()-1);
		}
        return name;
	}
	

}
