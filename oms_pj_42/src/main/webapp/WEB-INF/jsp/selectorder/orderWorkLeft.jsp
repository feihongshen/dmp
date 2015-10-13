<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import="cn.explink.domain.orderflow.OrderFlow"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<%
List<CwbOrder> oList = (List<CwbOrder>)request.getAttribute("allcwborderList");
Page page_obj =(Page)request.getAttribute("page_obj");
%>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/highcharts/highcharts.js"></script>
<script src="<%=request.getContextPath()%>/js/highcharts/exporting.js"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script>

$(document).ready(function() {
   
	$("#cleanForm").click(function(){
	$("#consigneename").val("收件人姓名");
	$("#consigneemobile").val("手机号");
	$("#consigneephone").val("电话号");
	$("#consigneeaddress").val("地址（模糊）");
	$("#marksflag").val(0);
});

	
});

function dgetViewBox(key){
	var durl = $("#view").val();
	window.parent.dgetViewBox(key,durl);
}
</script>
</HEAD>
<body >

<form action="<%=request.getContextPath()%>/order/selectAll/1" method="post" style="position:relative; z-index:0" id="searchAllForm">
		
		<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_2">
			<tbody><tr>
				<td width="10%" align="left" valign="top"><table width="100%" border="0" cellspacing="5" cellpadding="0" class="right_set2">
					<tbody><tr>
						<td>
						<input name="consigneename" type="text" id="consigneename" value="<%=request.getParameter("consigneename")==null?"收件人姓名":request.getParameter("consigneename")%>" onfocus="if(this.value=='收件人姓名'){this.value=''}" onblur="if(this.value==''){this.value='收件人姓名'}">
							
						<input name="consigneemobile" type="text" id="consigneemobile" value="<%=request.getParameter("consigneemobile")==null?"手机号":request.getParameter("consigneemobile")%>" onfocus="if(this.value=='手机号'){this.value=''}" onblur="if(this.value==''){this.value='手机号'}"></td>
					</tr>
					<tr>
						<td>
						<input name="consigneeaddress" type="text" id="consigneeaddress" value="<%=request.getParameter("consigneeaddress")==null?"地址（模糊）":request.getParameter("consigneeaddress")%>" onfocus="if(this.value=='地址（模糊）'){this.value=''}" onblur="if(this.value==''){this.value='地址（模糊）'}">
						
						<input name="consigneephone" type="text" id="consigneephone" value="<%=request.getParameter("consigneephone")==null?"电话号":request.getParameter("consigneephone")%>" onfocus="if(this.value=='电话号'){this.value=''}" onblur="if(this.value==''){this.value='电话号'}">
						<input type="hidden" name="showflag" value="1"/>	
							</td>
					</tr>
					<tr>
						<td>
						<select name="marksflag" id="marksflag">
							<option value="1">我标记的</option>
							<option value="0" <%="0".equals(request.getParameter("marksflag"))?"selected":"" %>>全部标记</option>
							
						</select>
							<input type="submit" name="button3" id="submitForm" value="搜索" class="input_button2">
							<input type="reset" name="button4" id="cleanForm" value="清空" class="input_button2"></td>
					</tr>
					</tbody></table></td>
			</tr>
			<tr class="font_1">
				<td width="10%" height="26" align="left" valign="middle" bgcolor="#eef6ff">&nbsp;排序：
					<label for="select"></label>
					<select name="orderbyName" id="sorderbyName" onchange="goMyForm();">
						<option value="marksflagtime" <%if((request.getParameter("orderbyName")==null?"":request.getParameter("orderbyName")).equals("marksflagtime")){ %>selected="selected"<%}%>>标记时间 </option>
						<option value="editsignintime" <%if((request.getParameter("orderbyName")==null?"":request.getParameter("orderbyName")).equals("editsignintime")){ %>selected="selected"<%}%>>签收时间 </option>
						<option value="emaildate" <%if((request.getParameter("orderbyName")==null?"":request.getParameter("orderbyName")).equals("emaildate")){ %>selected="selected"<%}%>>发货时间 </option>
						<option value="edittime" <%if((request.getParameter("orderbyName")==null?"":request.getParameter("orderbyName")).equals("edittime")){ %>selected="selected"<%}%>>修改时间 </option>
					</select>
					<select name="orderbyId" id="sorderbyId" onchange="goMyForm();">
						<option value="DESC" <%if((request.getParameter("orderbyId")==null?"":request.getParameter("orderbyId")).equals("DESC")){ %>selected="selected"<%}%>>倒序</option>
						<option value="ASC" <%if((request.getParameter("orderbyId")==null?"":request.getParameter("orderbyId")).equals("ASC")){ %>selected="selected"<%}%>>正序</option>
					</select></td>
			</tr>
			<tr>
				<td valign="top">
				
				<table width="100%" border="0" cellspacing="5" cellpadding="0" class="right_set3">
					<%if(oList!=null && oList.size()>0){ %>
			           <% for(CwbOrder o:oList){%>
						<tr id="t<%=o.getCwb() %>">
							<td>
							<div onclick="goForm('<%=o.getCwb() %>');">
							<p>订单号：<%=o.getCwb() %></p>
								<p>运单号：<%=o.getTranscwb() %></p>
								<p><%=o.getConsigneename()%>,
								<%if(o.getConsigneemobile()!=null && !o.getConsigneemobile().equals("")){%><%=o.getConsigneemobile()%>,<%} %>
								<%if(o.getConsigneephone()!=null && !o.getConsigneephone().equals("")){%><%=o.getConsigneephone()%>,<%} %>
								<font color="red">
								<%if(o.getCustomername()!=null && !o.getCustomername().equals("")){%><%=o.getCustomername()%>,<%} %></font>
								<font color="blue"><%if(o.getEmaildate()!=null && !o.getEmaildate().equals("")){%><%=o.getEmaildate()%>&quot;发货&quot;,<%} %></font>
								<font color="red"><%=o.getMarksflagtime()%>被&quot;<%=o.getMarksflagmen()%>&quot;
								<%if(o.getMarksflag()==1){%>标记<%}else{ %>取消标记<%} %></font>,
								<font color="green"><%if(o.getSignintime()!=null && !o.getSignintime().equals("")){%><%=o.getSignintime()%>被&quot;<%=o.getSigninman()%>&quot;&quot;签收&quot;</font>,<%} %></p>
								<p><%=o.getCwbremark()%></p>
							</div>
								<p align="right">
								<%if(o.getMarksflag() == 1){ %> 
								<input type="button" value="取消标记" id="quxiaoMackButton" class="input_button2" onclick="updateMack('<%=o.getCwb()%>')" />
								<%} else{%>
								<input type="button" value="标记" id="quxiaoMackButton"  class="input_button2" onclick="updateMack('<%=o.getCwb()%>')" />
								<%} %>
								<input type="button" value="操作日志" class="input_button2" onclick="dgetViewBox('<%=o.getCwb()%>')" />
								</p></td>
						</tr>
				    <%}} %>	
				</table>
				
				<div class="jg_35"></div>
				    <div class="paging" style="top: 460px; ">
				       <%if(page_obj.getMaxpage()>0){ %>
						   <a title="首页" href="javascript:;" onclick="$('#searchAllForm').attr('action','<%=request.getContextPath()%>/order/selectAll/1');$('#searchAllForm').submit()">|&lt; </a>
						   <%if(Integer.parseInt(request.getAttribute("page").toString())>1){ %>
						   <a title="上一页" href="javascript:;" onclick="$('#searchAllForm').attr('action','<%=request.getContextPath()%>/order/selectAll/<%=page_obj.getPrevious() %>');$('#searchAllForm').submit()"> &lt; </a>
						   <%} %>
						    <%if(page_obj.getMaxpage()>Integer.parseInt(request.getAttribute("page").toString())){ %>
						   <a title="下一页"  href="javascript:;" onclick="$('#searchAllForm').attr('action','<%=request.getContextPath()%>/order/selectAll/<%=page_obj.getNext() %>');$('#searchAllForm').submit()" > &gt; </a>
						   <%} %>
						   <a  title="末页" href="javascript:;" onclick="$('#searchAllForm').attr('action','<%=request.getContextPath()%>/order/selectAll/<%=page_obj.getMaxpage() %>');$('#searchAllForm').submit()" > &gt;| </a>
						 共<%=page_obj.getMaxpage() %>页　共<font id="countALl"> <%=page_obj.getTotal() %></font>条记录　第<%=request.getAttribute("page")%>页 
                     <%} %>
                    </div>
                    
				</td>
			</tr>
		</tbody></table></form>
		
		<script type="text/javascript">
		function goForm(cwb){
			$("#WORK_AREA_RIGHT",parent.document)[0].contentWindow.gotoForm(cwb);
		}
		function goMyForm(){
			$("#searchAllForm").submit();
		}
		function updateMack(cwb){
			var type = "0";
			if($("#quxiaoMackButton").val()=="标记"){
				type ="1";
			}
			$.ajax({
				type: "POST",
				url:"<%=request.getContextPath()%>/order/updateMack",
				data:{mackType:type,cwb:cwb},
				dataType:"json",
				success : function(data) {
					if(data.errorCode==0){
						alert(data.error);
						if($("#quxiaoMackButton").val()=="标记"){
							$("#quxiaoMackButton").val("取消标记");
						}else{
							$("#quxiaoMackButton").val("标记");
						}
					}else{
						alert(data.error);
					}
				}
			});
			
			$("#WORK_AREA_LEFT",parent.document)[0].contentWindow.goMyForm();
			$("#WORK_AREA_RIGHT",parent.document)[0].contentWindow.gotoForm(cwb);
		}
		</script>
<!-- 操作日志的ajax地址 -->
<input type="hidden" id="view" value="<%=request.getContextPath()%>/order/makelog/" />	
</body>
</html>