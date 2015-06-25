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
import cn.explink.domain.AbnormalOrder;
import cn.explink.domain.AbnormalType;
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.MissPiece;
import cn.explink.domain.MissPieceView;
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
				view.setCreuserid(a.getLong("creuserid"));
				view.setCwb(a.getString("cwb"));
				view.setCustomerName(this.getCustomer(customers, a.getLong("customerid")));
				view.setDescribe(a.getString("describe"));
				view.setEmaildate(a.getString("emaildate"));
				view.setIshandle(a.getLong("ishandle"));
				view.setCredatetime(a.getString("credatetime"));
				view.setFileposition(a.getString("fileposition"));
				view.setDealResultContent(this.getDealResult(a.getLong("dealresult")));
				view.setCwborderType(this.getCwbOrderType(a.getString("cwbordertypeid")));
				view.setIsfinecontent(this.getIsFine(a.getLong("isfine")));
				view.setDutybranchname(this.getBranchName(branchs, a.getLong("dutybrachid")));
				view.setQuestionno(a.getString("questionno"));
				view.setLosebackContent(this.checkfindOrnot(a.getLong("losebackid")));
				view.setDutyperson(this.getCreName(users, a.getLong("dutypersonid")));
				view.setDutypersonid(a.getLong("dutypersonid"));
				view.setIsfindInfo(this.checkfindOrnot(a.getLong("isfind")));
				view.setLastdutybranch(this.getBranchName(branchs, a.getLong("lastdutybranchid")));
				view.setLastdutyuser(this.getCreName(users, a.getLong("lastdutyuserid")));
				views.add(view);
			}
		}
		return views;
	}
	public String checkfindOrnot(long losebackid){
		if (losebackid==0) {
			return "未找回";
		}else {
			return "已找回";
		}
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
	public void creAbnormalOrder(CwbOrder co, User user, long abnormaltypeid, String nowtime, Map<Long, JSONObject> mapForAbnormalorder, long action, long handleBranch,String name,String abnormalinfo,String questionNo,long isfind,long ishandle) {
		// long abnormalorderid =
		// abnormalOrderDAO.creAbnormalOrderLong(co.getOpscwbid(),
		// co.getCustomerid(), "", user.getUserid(), user.getBranchid(),
		// abnormaltypeid, nowtime);
		long abnormalorderid=0;
		abnormalorderid = this.abnormalOrderDAO.creAbnormalOrderLongAdd(co, abnormalinfo, user.getUserid(), user.getBranchid(), abnormaltypeid, nowtime, handleBranch,name,questionNo,isfind,ishandle);
		this.abnormalWriteBackDAO.creAbnormalOrderAdd(co.getOpscwbid(), abnormalinfo, user.getUserid(), action, nowtime, abnormalorderid, abnormaltypeid, co.getCwb(),name);
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
				if (name.indexOf(".")!=-1) {
					String suffix=name.substring(name.lastIndexOf("."));
					 name = System.currentTimeMillis() + suffix;
				}else {
					 name = System.currentTimeMillis()+"";
				}
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
                file.getOriginalFilename();
                name=this.loadexceptfile(file)+",";
            }
        }
        if (name!="") {
			name=name.substring(0, name.length()-1);
		}
        return name;
	}
	//文件批量上传的路径（新增）
	public List<String> getExceptnameAdd(HttpServletRequest request){
		List<String> filepaths=new ArrayList<String>();
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
                file.getOriginalFilename();
                name=this.loadexceptfile(file)+",";
                if (name!="") {
        			name=name.substring(0, name.length()-1);
        		}
                filepaths.add(name);
            }
        }
        
        return filepaths;
	}
	//结案处理修改两张表的信息
	public void reviseAbnormalAndwritebackLast(AbnormalOrder co,String describe,long dealresult,long dutybranchid,long dutyname,String filepathsum,long action,User user,String nowtime,String filepath,long ishandle){
		//,dutybranchid,dutyname
		abnormalOrderDAO.saveAbnormalOrderResultByid(co.getId(), dealresult,filepathsum,ishandle,describe,nowtime,dutybranchid,dutyname);
		//long dutybranchid,long dutyusernameid,
		abnormalWriteBackDAO.creAbnormalOrderAdd(co.getOpscwbid(), describe, user.getUserid(), action, nowtime, co.getId(), co.getAbnormaltypeid(), co.getCwb(), filepath);
	}
	//构建丢失件导出数据
	public List<MissPieceView> setMissPieceView(List<MissPiece> missPieces,List<Branch> branchs,List<User> users,List<Customer> customers){
		List<MissPieceView> missPieceViews=new ArrayList<MissPieceView>();
		if (missPieces.size()>0&&missPieces!=null) {
			for(MissPiece missPiece:missPieces){
				MissPieceView missPieceView=new MissPieceView();
				missPieceView.setId(missPiece.getId());
				missPieceView.setCwb(missPiece.getCwb());
				missPieceView.setCreatetime(missPiece.getCreatetime());
				missPieceView.setDescribe(missPiece.getDescribeinfo());
				missPieceView.setQuestionno(missPiece.getQuestionno());
				missPieceView.setCallbackbranchname(this.getBranchName(branchs, missPiece.getCallbackbranchid()));
				missPieceView.setCreusername(this.getCreName(users, missPiece.getCreuserid()));
				missPieceView.setCustomername(this.getCustomer(customers, missPiece.getCustomerid()));
				missPieceView.setFlowordertypeName(this.getFloworderType(missPiece.getFlowordertype()));
				missPieceView.setOrdertype(this.getCwbOrderType(String.valueOf(missPiece.getCwbordertypeid())));
				missPieceViews.add(missPieceView);
			}
			return missPieceViews;
		}else {
			return null;
		}
	}

   }
