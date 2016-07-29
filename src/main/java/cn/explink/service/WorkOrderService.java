package cn.explink.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import cn.explink.core.utils.StringUtils;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.ReasonDao;
import cn.explink.dao.UserDAO;
import cn.explink.dao.WorkOrderDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.CsComplaintAcceptViewVo;
import cn.explink.domain.CsConsigneeInfo;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.Reason;
import cn.explink.domain.User;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.ResourceBundleUtil;
import cn.explink.util.ServiceUtil;

@Service
public class WorkOrderService {
	@Autowired
	private WorkOrderDAO workorderdao;
	@Autowired
	private CwbDAO cwbdao;
	@Autowired
	CustomerDAO customerDao;
	@Autowired
	ReasonDao reasonDao;
	@Autowired
	BranchDAO branchDao;
	@Autowired
	UserDAO userDao;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public void addcsconsigneeInfo(CsConsigneeInfo cci) {
		if(cci!=null)
				workorderdao.save(cci);
		
	}

	public CsConsigneeInfo querycciByPhoneNum(String phoneonOne){
		CsConsigneeInfo cc=null;
		try {
			cc=workorderdao.queryByPhoneNum(phoneonOne);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return null;
		}
		return cc;
	}

	public List<CwbOrder> SelectCwbdetalForm(String phone,long currentPage) {

		List<CwbOrder> lc=cwbdao.SelectDetalForm(phone,currentPage);
		return lc;		
	}
	
	public  String loadexceptfile(MultipartFile file){
		String name="";
		try {
			if ((file != null) && !file.isEmpty()) {
				String filePath = ResourceBundleUtil.EXCEPTPATH;
				name=file.getOriginalFilename();
				if (name.indexOf(".")!=-1) {
					String suffix=name.substring(name.indexOf("."));
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
                name=this.loadexceptfile(file)+",";
            }
        }
        if (name!="") {
			name=name.substring(0, name.length()-1);
		}
        return name;
	}

	public long SelectDetalFormCount(String phone) {
		// TODO Auto-generated method stub
	
		return cwbdao.SelectDetalFormCount(phone);
	}
	
	public Map<String,List<CsComplaintAcceptViewVo>> importWorkOrders(InputStream input, ExcelExtractor extractor, String creator, int type) {
		// 导入工单记录
		List<Object> list = extractor.getRows(input);
		List<CsComplaintAcceptViewVo> results = new ArrayList<CsComplaintAcceptViewVo>();
		String handleTime = DateTimeUtil.formatDate(new Date(), DateTimeUtil.DEF_DATETIME_FORMAT);
		Date createDatetime = new Date();
		for (Object row : list) { 
			CsComplaintAcceptViewVo complain = analyzeComplainData(row, extractor, type);//分析导入工单excel
			if (complain != null) {
				results.add(complain);
				if (complain.isCorrect()) {
					if (type == 1) {
						complain.setComplaintResult(0);
						complain.setComplaintState(1);
					} else {
						complain.setComplaintResult(2);
						complain.setComplaintState(3);
						complain.setHeshiUser(creator);
						complain.setHeshiTime(handleTime);
						complain.setJieanUser(creator);
						complain.setJieanTime(handleTime);
						complain.setJieanremark("导入结案");
					}
					createDatetime = DateTimeUtil.getNextSecondDateTime(createDatetime);
					complain.setAcceptNo("G"+DateTimeUtil.formatDate(createDatetime, DateTimeUtil.DEF_DATETIME_NUM_FORMAT)+((int)(Math.random()*10)));
					complain.setHandleUser(creator);
					complain.setAcceptTime(handleTime);
					workorderdao.saveComplainWorkOrderForImport(complain);
				}
			}
		}
		Map<String,List<CsComplaintAcceptViewVo>> map = new HashMap<String,List<CsComplaintAcceptViewVo>>();
		map.put("result", results);
		return map;
	}
	
	private CsComplaintAcceptViewVo analyzeComplainData(Object row, ExcelExtractor extractor, int type) {
		CsComplaintAcceptViewVo complain = null;
		CwbOrder cwbOrder = null;
		boolean isValidated = true;
		int[] sequence;
		if (type == 1)
			sequence = new int[]{5,4,6,7,8,9,10,1};
		else 
			sequence = new int[]{5,4,6,7,8,9,10,11,1};
		
		for (int index : sequence) {
			switch(index) {
			case 1 : //联系人
				String contact = extractor.getXRowCellData(row, 1).trim();//联系人
				String telephone = extractor.getXRowCellData(row, 2).trim();//电话
				String email = extractor.getXRowCellData(row, 3).trim();//邮箱
				complain.setContact(contact);
				complain.setPhoneOne(telephone);
				complain.setEmail(email);
				if (complain.isCorrect()) {//校验的字段通过，保存联系人信息
					try {
						CsConsigneeInfo cci = new CsConsigneeInfo();//目前只有联系人姓名、电话、邮箱
						cci.setPhoneonOne(telephone);
						cci.setConsigneeType(0);
						cci.setSex(0);
						cci.setProvince("");
						cci.setCity("");
						cci.setName(contact);
						cci.setContactLastTime(DateTimeUtil.formatDate(new Date(), DateTimeUtil.DEF_DATETIME_FORMAT));
						cci.setContactNum(1);
						cci.setMailBox(email);
						cci.setCallerremark("导入工单创建！");
						workorderdao.save(cci);
					} catch (Exception e) {
						logger.error("工单导入创建联系人出现异常", e);
					}
				}
			    break;
			case 4 : //客户
				String cusName = extractor.getXRowCellData(row, 4).trim();//客户名
				if (!StringUtils.isBlank(cusName)) {
					Customer customer = customerDao.getCustomerByName(cusName);
					if (customer != null && cwbOrder != null) {
						if (customer.getCustomerid() == cwbOrder.getCustomerid()) {
							complain.setCustomerid(customer.getCustomerid());
							complain.setCustomerName(customer.getCustomername());
						} else {
							complain.setCustomerid(-1);//找不到该客户
							complain.setCustomerName(cusName);
							complain.setCorrect(false);
							complain.setErrorMsg("订单客户与该客户不符！");
						}
						break;
					}
				}
				complain.setCustomerid(-1);//找不到该客户
				complain.setCustomerName(cusName);
				complain.setCorrect(false);
				complain.setErrorMsg("该客户不存在！");
				break;	
			case 5 ://订单号
				String cwb = extractor.getXRowCellData(row, 5).trim();//订单号
				if (StringUtils.isBlank(cwb)) { //订单号为空跳过该行（过滤空行）
					isValidated = false;
					break;
				}
				complain = new CsComplaintAcceptViewVo(true);
				cwbOrder = cwbdao.getCwbByCwb(cwb);
				if (cwbOrder != null) {
					complain.setOrderNo(cwb);
					complain.setCwbstate(cwbOrder.getCwbstate());
					complain.setFlowordertype(cwbOrder.getFlowordertype());
					complain.setProvence(cwbOrder.getCwbprovince());
					break;
				}
				complain.setOrderNo(cwb);
				complain.setCorrect(false);
				complain.setErrorMsg("该订单不存在！");
				break;
			case 6 ://一级分类
				String Category1 = extractor.getXRowCellData(row, 6).trim();//一级分类
				if (!StringUtils.isBlank(Category1)) {
					Reason reason = reasonDao.getWorkFirstReasonByContent(Category1);
					if (reason != null) {
						complain.setComplaintOneLevel((int)reason.getReasonid());
						complain.setComplaintOneLevelName(reason.getReasoncontent());
						break;
					}
				}
				complain.setComplaintOneLevel(-1);
				complain.setComplaintOneLevelName(Category1);
				complain.setCorrect(false);
				complain.setErrorMsg("一级分类不存在！");
				break;
			case 7 ://二级分类
				String Category2 = extractor.getXRowCellData(row, 7).trim();//二级分类
                if (!StringUtils.isBlank(Category2) && complain.getComplaintOneLevel() != -1) {
                	Reason reason = reasonDao.getWorkSecondReasonByContent(complain.getComplaintOneLevel(), Category2);
					if (reason != null) {
						complain.setComplaintTwoLevel((int)reason.getReasonid());
						complain.setComplaintTwoLevelName(reason.getReasoncontent());
						break;
					}
				}
                complain.setComplaintTwoLevel(-1);
                complain.setComplaintTwoLevelName(Category2);
				complain.setCorrect(false);
				complain.setErrorMsg("二级分类不存在！");
				break;
			case 8 ://工单内容
				String content = extractor.getXRowCellData(row, 8).trim();//工单内容
				if (StringUtils.isBlank(content) && type == 2) {
					complain.setCorrect(false);
					complain.setErrorMsg("工单内容为空！");
				}
				complain.setContent(content);
				break;
			case 9 ://责任机构
				String responsibilityBranch = extractor.getXRowCellData(row, 9).trim();//责任机构
				if (!StringUtils.isBlank(responsibilityBranch)) {
					Branch branch = branchDao.getBranchByBranchname(responsibilityBranch);
					if (branch != null && branch.getBranchid() != 0) {
						complain.setCodOrgId((int)branch.getBranchid());
						complain.setCodOrgIdName(branch.getBranchname());
						break;
					}
					complain.setErrorMsg("该责任机构不存在！");
				} else {
					if (cwbOrder != null && type ==1) {
						Branch branch = branchDao.getBranchByBranchid(cwbOrder.getDeliverybranchid());
						if (branch != null && branch.getBranchid() != 0) {
							complain.setCodOrgId((int)branch.getBranchid());
							complain.setCodOrgIdName(branch.getBranchname());
							break;
						}
					}
					complain.setErrorMsg("责任机构不能为空！");
				}
				complain.setCorrect(false);
				complain.setCodOrgId(-1);
				complain.setCodOrgIdName(responsibilityBranch);
				break;
			case 10 ://责任人
				String responsibilityPerson = extractor.getXRowCellData(row, 10).trim();//责任人
				if (StringUtils.isBlank(responsibilityPerson)) { 
					complain.setComplaintUser(responsibilityPerson);
					break;
				}
				User user = userDao.getUserByUsername(responsibilityPerson);
				if (user != null) {
					complain.setComplaintUser(user.getUsername());
					break;
				}
				complain.setCorrect(false);
				complain.setErrorMsg("该责任人不存在！");
				complain.setComplaintUser(responsibilityPerson);
				break;
			case 11 : //处理结果
				String handleContent = extractor.getXRowCellData(row, 11).trim();//处理结果
				if (StringUtils.isBlank(handleContent) && type == 2) {
					complain.setCorrect(false);
					complain.setErrorMsg("处理结果为空！");
				}
				complain.setRemark(handleContent);
				break;
			}
			if (!isValidated) break; //跳过空行
		}
		return complain;
	}
}