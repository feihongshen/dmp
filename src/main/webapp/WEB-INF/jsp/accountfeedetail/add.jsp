<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.AccountFeeType,cn.explink.domain.Branch"%>
<%
	List<Branch> branchList=(List<Branch>)request.getAttribute("branchList");
	List<AccountFeeType> feeTypeList =request.getAttribute("feeTypeList")==null?null:(List<AccountFeeType>)request.getAttribute("feeTypeList");
%>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>创建款项明细</h1>
		<form id="fee_detail_Form" name="fee_detail_Form" 
			 onSubmit="if(check_accountfeedetail()){submitCreateFormAndCloseBox(this);}return false;" 
			 action="<%=request.getContextPath()%>/accountfeedetail/create" method="post"  >
		<div id="box_form">
			<ul>
				<li><span>款项：</span> 
				  <select id="feetypeid" name="feetypeid" style="width:150px;">	
					<option value="0">==请选择==</option>
					<%for(AccountFeeType b: feeTypeList){%>
						<option value=<%=b.getFeetypeid()%> ><%=b.getFeetypename()%></option>
					<%}%>	
				  </select>*

				<%-- <input type="hidden" id="feetypeid" name="feetypeid" value="<%=accountFeeType.getFeetypeid()%>"/></li> --%>
				
				<li><span>站点：</span> 
				  <select id="branchid" name="branchid" style="width:150px;">	
					<option value="0">==请选择==</option>
					<%for(Branch b: branchList){%>
						<option value=<%=b.getBranchid()%> ><%=b.getBranchname()%></option>
					<%}%>	
				  </select>*
				</li>
				<li><span>金额[元]：</span><input type="text" name="customfee" id="customfee" maxlength="20"/>*</li>
          		<li><span>备注：</span><input type="text" name="detailname" id="detailname" maxlength="200"/></li>
            </ul>
		</div>
		<div align="center">
		<input type="hidden" value="2" name="checkoutstate" id="checkoutstate"/>
		<input type="submit" value="确认" class="button" id="sub" /></div>
		</form>	
	</div>
</div>
