<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="cn.explink.domain.AccountFeeDetail,cn.explink.domain.AccountFeeType,cn.explink.domain.Branch"%>
<%
	AccountFeeDetail accountFeeDetail = (AccountFeeDetail)request.getAttribute("accountFeeDetail");
	List<Branch> branchList=(List<Branch>)request.getAttribute("branchList");
	List<AccountFeeType> feeTypeList =request.getAttribute("feeTypeList")==null?null:(List<AccountFeeType>)request.getAttribute("feeTypeList");
%>

<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>修改款项明细<%=accountFeeDetail.getFeedetailid()%></h1>
		<form id="fee_detail_Form" name="fee_detail_Form"
			 onSubmit="if(check_accountfeedetail()){submitCreateFormAndCloseBox(this);}return false;" 
			 action="<%=request.getContextPath()%>/accountfeedetail/save/<%=accountFeeDetail.getFeedetailid()%>" method="post"  >
		<div id="box_form">
				<ul>
				<li><span>款项：</span> 
				  <select id="feetypeid" name="feetypeid" style="width:150px;">	
					<option value="0">==请选择==</option>
					<%for(AccountFeeType b: feeTypeList){
						if(b.getFeetypeid()==accountFeeDetail.getFeetypeid()){%>
							<option value=<%=b.getFeetypeid()%> selected="true"><%=b.getFeetypename()%></option>
						<%}else{%>
							<option value=<%=b.getFeetypeid()%>><%=b.getFeetypename()%></option>	
						<%}
					}%>	
				  </select>*
				<li><span>站点：</span> 
				  <select id="branchid" name="branchid" style="width:150px;">	
					<option value="0">==请选择==</option>
					<%for(Branch b: branchList){
						if(b.getBranchid()==accountFeeDetail.getBranchid()){%>
							<option value=<%=b.getBranchid()%> selected="true"><%=b.getBranchname()%></option>
						<%}else{%>
							<option value=<%=b.getBranchid()%>><%=b.getBranchname()%></option>	
						<%}
					}%>	
				  </select>*
				</li>
				<li><span>金额[元]：</span><input type="text" name="customfee" id="customfee" value="${accountFeeDetail.customfee}" maxlength="20"/>*</li>
          		<li><span>备注：</span><input type="text" name="detailname" id="detailname" value="${accountFeeDetail.detailname}" maxlength="200"/></li>
            </ul>
		</div>
		<div align="center">
		<input type="hidden" name="feedetailid" id="feedetailid" value="${accountFeeDetail.feedetailid}"/>
		<input type="submit" value="保存"  class="button" /></div>
	</form>
	</div>
</div>
<div id="box_yy"></div>
