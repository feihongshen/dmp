
<%@page import="net.sf.json.JSONObject"%>
<%@page import="cn.explink.util.DateTimeUtil"%>
<%@page import="cn.explink.enumutil.*"%>
<%@page import="cn.explink.domain.*"%>

<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<Branch> branchList = (List<Branch>)request.getAttribute("branchList");
String url=request.getContextPath()+"/abnormalOrder/getbranchusers";
List<PenalizeType> penalizebigList=(List<PenalizeType>)request.getAttribute("penalizebigList");
List<PenalizeType> penalizesmallList=(List<PenalizeType>)request.getAttribute("penalizesmallList");
%>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg" >
		<h1><div id="close_box" onclick="closeBox()"></div>根据订单创建对内扣罚单</h1>
		<form method="post" id="form1" onSubmit="if(check_createbycwb()){submitPunishCreateBycwb(this);}return false;" action="<%=request.getContextPath()%>/inpunish/createbycwbwithfile;jsessionid=<%=session.getId()%>" enctype="multipart/form-data">
			<table width="800" border="0" cellspacing="0" cellpadding="0" id="chatlist_alertbox">
				<tr>
					<td width="600" valign="top"><table width="100%" border="0" cellspacing="1" cellpadding="10" class="table_2" style="height:280px">
						<tr class="font_1">
							<td colspan="2" align="left" valign="top">订单号：<font color="red">*</font>
							<input type="text" id="cwb" name="cwb" class="input_text1" style="height:15px;width: 120px;" onkeyup="createbycwbinit($(this).val());" onblur="createbycwbinit($(this).val());"/>
							&nbsp;&nbsp;订单状态：
							<input type="text" id="flowordertype" name="flowordertype" readonly="readonly" class="input_text1" style="height:15px;width: 120px;border-style:none"/>
							<input type="hidden" id="cwbstate" name="cwbstate" />
							&nbsp;&nbsp;订单金额：
							<input type="text" id="cwbprice" name="cwbprice" readonly="readonly" class="input_text1" style="height:15px;width: 120px;border-style:none"/></td>
						</tr>
						<tr class="font_1">
							<td  align="left" valign="top">扣罚大类<font color="red">*</font>：
							<select id="punishbigsort" name="punishbigsort" class="select1" onchange="findsmallAdd('<%=request.getContextPath() %>',this,'punishsmallsort');">
								<option value="0">请选择扣罚大类</option>
								<%if(penalizebigList!=null&&penalizebigList.size()>0){for(PenalizeType   pType :penalizebigList) {%>
								<option value="<%=pType.getId()%>"><%=pType.getText() %></option>
								<%}} %>
							</select>
							&nbsp;&nbsp;扣罚小类：
							<select id="punishsmallsort" name="punishsmallsort" class="select1" onchange="findbigAdd(this,'punishbigsortpunishbigsort');">
							<option value="0">请选择扣罚小类</option>
								<%if(penalizesmallList!=null&&penalizesmallList.size()>0){for(PenalizeType     penType:penalizesmallList) {%>
								<option value="<%=penType.getId()%>" id="<%=penType.getParent() %>"><%=penType.getText() %></option>
								<%}} %>
							</select>
							&nbsp;&nbsp;责任机构<font color="red">*</font>:<select  id="dutybranchid" name="dutybranchid" onchange="selectbranchUsers('dutyname','dutybranchid');" class="select1">
							<option value="0" selected="selected">请选择责任机构</option>
							<%if(branchList!=null){for(Branch branch:branchList){ %>
							<option value="<%=branch.getBranchid() %>"><%=branch.getBranchname() %></option>
							<% }}%>
						</select></td>
						</tr>
						<tr class="font_1">
						<td align="left" valign="top">
						责任人:<select  id="dutyname" name="dutyname" class="select1">
						<option value ='0'>请选择机构责任人</option>
						</select>
						</tr>
						<tr class="font_1">
						<td align="left" valign="top">
						扣罚金额<font color="red">*</font>:<input type="text" id="punishprice" name="punishprice" class="input_text1" style="height:15px;width: 120px;"/>
									<input type="hidden" id="punishinsidetype" name="punishinsidetype" value="1"/>
						</td>
						</tr>
						<tr class="font_1">
							<td  align="left" valign="top">扣罚说明：<textarea onfocus="if(this.value == '最多100个字') this.value = ''" onblur="if(this.value == '') this.value = '最多100个字'" name="punishdescribe" id="punishdescribe" cols="40" rows="4" id="textfield"  >最多100个字</textarea></td>
						</tr>
						 <tr class="font_1">
							<td  align="left" valign="top"> 
								上传附件：<iframe id="update" name="update" src="penalize/penalizeIn/update?fromAction=form1&a=<%=Math.random() %>" width="240px" height="25px"   frameborder="0" scrolling="auto" marginheight="0" marginwidth="0" allowtransparency="yes" ></iframe>    
							</td>
						</tr>  
					</table>
					</td>
				</tr>
			</table>
			<input type="hidden" name="getbranchusers" id="getbranchusers" value="<%=url %>">
			<input type="hidden" name="absolutepath" id="absolutepath" value="<%=request.getContextPath() %>">
			<div align="center">
				<input type="submit" value="提交" class="button">
				<input type="button" onclick="closeBox();"  value="取消" class="button">
			</div>
		</form>
	</div>
</div>
