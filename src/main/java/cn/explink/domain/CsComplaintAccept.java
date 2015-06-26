package cn.explink.domain;
/**
 * 
 * @author wzy
 *主键
收件人id
工单号
订单号
被投诉机构
被投诉人
受理类型（枚举ComplaintTypeEnum）
一级分类（枚举ComplaintOneLevelEnum）
二级分类（数据字典）
投诉内容
受理开始时间
受理结束时间、投诉时间
处理时间、认责时间
处理人
处理结果(枚举ComplaintResultEnum)（未处理、成立”和“不成立）
有无申诉（0：无  1：有）
工单状态(枚举ComplaintStateEnum）（待处理、,daishenhen, yishenhe、已结案）
申诉时间
申诉内容
认责、处理内容
申诉自动到期时间(从系统参数设置里取后加的时间)
联系次数

 */
public class CsComplaintAccept {
			private int id;                
			private int consigneeId;   //收件人id
			private String acceptNo;	//工单号
			private String orderNo;		//订单号
			private int codOrgId;		//被投诉机构
			private String ComplaintUser;	//被投诉人
			private int complaintType;		//被投诉类型
			private int complaintOneLevel;	//一级分类
			private int complaintTwoLevel;	//二级分类
			private String content;		//投诉人内容
			private String beginTime;	//开始时间
			private String endTime;	//结束时间
			private String handleTime; //受理时间
			private String handleUser;	 //处理人
			private int complaintResult; //处理结果
			private int isComplaint;  //有无申诉 0有1无
			private int complaintState; //工单状态
			private String complaintTime;
			private String complaintContent;
			private String handleContent;
			private int complaintEndTime;
			private int remaindNum;
			private long cwbstate;
			private long flowordertype;
			private String currentBranch;
			private String queryContent;
			private String acceptTime; //受理时间
			private String provence;
			private String phoneOne;
			private String remark;
			private String heshiTime;
			private String jieanTime;	
			private long customerid;
			private String jieanremark;
			private String shensuremark;
			private String jieanchongshenremark;
			private String jieanchongshenTime;
			private int ifpunish;
			private String heshiUser;
			private String jieanUser;
			private String shensuUser;
			private String chongshenUser;
			private int cuijianNum = 0;
			private int ifOutofdate;
			
			

			public int getIfOutofdate() {
				return ifOutofdate;
			}
			public void setIfOutofdate(int ifOutofdate) {
				this.ifOutofdate = ifOutofdate;
			}
			private String downloadheshipath;
			public String getDownloadheshipath() {
				return downloadheshipath;
			}
			public void setDownloadheshipath(String downloadheshipath) {
				this.downloadheshipath = downloadheshipath;
			}
			public String getDownloadjieanpath() {
				return downloadjieanpath;
			}
			public void setDownloadjieanpath(String downloadjieanpath) {
				this.downloadjieanpath = downloadjieanpath;
			}
			public String getDownloadchongshenpath() {
				return downloadchongshenpath;
			}
			public void setDownloadchongshenpath(String downloadchongshenpath) {
				this.downloadchongshenpath = downloadchongshenpath;
			}
			public String getDownloadshensupath() {
				return downloadshensupath;
			}
			public void setDownloadshensupath(String downloadshensupath) {
				this.downloadshensupath = downloadshensupath;
			}
			private String downloadjieanpath;
			private String downloadchongshenpath;
			private String downloadshensupath;
			
			
			
			
			
		
			
			public int getCuijianNum() {
				return cuijianNum;
			}
			public void setCuijianNum(int cuijianNum) {
				this.cuijianNum = cuijianNum;
			}
			public String getHeshiUser() {
				return heshiUser;
			}
			public void setHeshiUser(String heshiUser) {
				this.heshiUser = heshiUser;
			}
			public String getJieanUser() {
				return jieanUser;
			}
			public void setJieanUser(String jieanUser) {
				this.jieanUser = jieanUser;
			}
			public String getShensuUser() {
				return shensuUser;
			}
			public void setShensuUser(String shensuUser) {
				this.shensuUser = shensuUser;
			}
			public String getChongshenUser() {
				return chongshenUser;
			}
			public void setChongshenUser(String chongshenUser) {
				this.chongshenUser = chongshenUser;
			}
			public int getIfpunish() {
				return ifpunish;
			}
			public void setIfpunish(int ifpunish) {
				this.ifpunish = ifpunish;
			}
			public String getJieanTime() {
				return jieanTime;
			}
			public String getJieanchongshenTime() {
				return jieanchongshenTime;
			}
			public void setJieanchongshenTime(String jieanchongshenTime) {
				this.jieanchongshenTime = jieanchongshenTime;
			}
			public void setJieanTime(String jieanTime) {
				this.jieanTime = jieanTime;
			}	
			
			
			public String getJieanchongshenremark() {
				return jieanchongshenremark;
			}
			public void setJieanchongshenremark(String jieanchongshenremark) {
				this.jieanchongshenremark = jieanchongshenremark;
			}
			public String getShensuremark() {
				return shensuremark;
			}
			public void setShensuremark(String shensuremark) {
				this.shensuremark = shensuremark;
			}
			public String getJieanremark() {
				return jieanremark;
			}
			public void setJieanremark(String jieanremark) {
				this.jieanremark = jieanremark;
			}
			public long getCustomerid() {
				return customerid;
			}
			public void setCustomerid(long customerid) {
				this.customerid = customerid;
			}
			public String getHeshiTime() {
				return heshiTime;
			}
			public void setHeshiTime(String heshiTime) {
				this.heshiTime = heshiTime;
			}
			public String getRemark() {
				return remark;
			}
			public void setRemark(String remark) {
				this.remark = remark;
			}
			public String getProvence() {
				return provence;
			}
			public void setProvence(String provence) {
				this.provence = provence;
			}
			public String getPhoneOne() {
				return phoneOne;
			}
			public void setPhoneOne(String phoneOne) {
				this.phoneOne = phoneOne;
			}
			public String getAcceptTime() {
				return acceptTime;
			}
			public void setAcceptTime(String acceptTime) {
				this.acceptTime = acceptTime;
			}
			public String getQueryContent() {
				return queryContent;
			}
			public void setQueryContent(String queryContent) {
				this.queryContent = queryContent;
			}
			public String getCurrentBranch() {
				return currentBranch;
			}
			public void setCurrentBranch(String currentBranch) {
				this.currentBranch = currentBranch;
			}
			
			
			public long getCwbstate() {
				return cwbstate;
			}
			public void setCwbstate(long cwbstate) {
				this.cwbstate = cwbstate;
			}
			public long getFlowordertype() {
				return flowordertype;
			}
			public void setFlowordertype(long flowordertype) {
				this.flowordertype = flowordertype;
			}
			public int getId() {
				return id;
			}
			public void setId(int id) {
				this.id = id;
			}
			public int getConsigneeId() {
				return consigneeId;
			}
			public void setConsigneeId(int consigneeId) {
				this.consigneeId = consigneeId;
			}
			public String getAcceptNo() {
				return acceptNo;
			}
			public void setAcceptNo(String acceptNo) {
				this.acceptNo = acceptNo;
			}
			public String getOrderNo() {
				return orderNo;
			}
			public void setOrderNo(String orderNo) {
				this.orderNo = orderNo;
			}
			public int getCodOrgId() {
				return codOrgId;
			}
			public void setCodOrgId(int codOrgId) {
				this.codOrgId = codOrgId;
			}
			public String getComplaintUser() {
				return ComplaintUser;
			}
			public void setComplaintUser(String complaintUser) {
				ComplaintUser = complaintUser;
			}
			public int getComplaintType() {
				return complaintType;
			}
			public void setComplaintType(int complaintType) {
				this.complaintType = complaintType;
			}
			public int getComplaintOneLevel() {
				return complaintOneLevel;
			}
			public void setComplaintOneLevel(int complaintOneLevel) {
				this.complaintOneLevel = complaintOneLevel;
			}
			public int getComplaintTwoLevel() {
				return complaintTwoLevel;
			}
			public void setComplaintTwoLevel(int complaintTwoLevel) {
				this.complaintTwoLevel = complaintTwoLevel;
			}
			public String getContent() {
				return content;
			}
			public void setContent(String content) {
				this.content = content;
			}
			public String getBeginTime() {
				return beginTime;
			}
			public void setBeginTime(String beginTime) {
				this.beginTime = beginTime;
			}
			public String getEndTime() {
				return endTime;
			}
			public void setEndTime(String endTime) {
				this.endTime = endTime;
			}
			public String getHandleTime() {
				return handleTime;
			}
			public void setHandleTime(String handleTime) {
				this.handleTime = handleTime;
			}
			public String getHandleUser() {
				return handleUser;
			}
			public void setHandleUser(String handleUser) {
				this.handleUser = handleUser;
			}
			public int getComplaintResult() {
				return complaintResult;
			}
			public void setComplaintResult(int complaintResult) {
				this.complaintResult = complaintResult;
			}
			public int getIsComplaint() {
				return isComplaint;
			}
			public void setIsComplaint(int isComplaint) {
				this.isComplaint = isComplaint;
			}
			public int getComplaintState() {
				return complaintState;
			}
			public void setComplaintState(int complaintState) {
				this.complaintState = complaintState;
			}
			public String getComplaintTime() {
				return complaintTime;
			}
			public void setComplaintTime(String complaintTime) {
				this.complaintTime = complaintTime;
			}
			public String getComplaintContent() {
				return complaintContent;
			}
			public void setComplaintContent(String complaintContent) {
				this.complaintContent = complaintContent;
			}
			public String getHandleContent() {
				return handleContent;
			}
			public void setHandleContent(String handleContent) {
				this.handleContent = handleContent;
			}
			public int getComplaintEndTime() {
				return complaintEndTime;
			}
			public void setComplaintEndTime(int complaintEndTime) {
				this.complaintEndTime = complaintEndTime;
			}
			public int getRemaindNum() {
				return remaindNum;
			}
			public void setRemaindNum(int remaindNum) {
				this.remaindNum = remaindNum;
			}
			
			
			
}	
